package COM.sootNsmoke.prolog;
import java.util.*;
import java.io.*;
import COM.sootNsmoke.jvm.*;
import COM.sootNsmoke.instructions.*;

/** Compiles Prolog files.  List the names of the Prolog source files
 *  on the command line.  This will generate various class files to
 *  the current directory.  These files are used with the Prolog
 *  interpreter in the class COM.sootNsmoke.prolog.Prolog.  It will
 *  expect the generated files to be somewhere in the CLASSPATH.
 * <P>
 * Use the "-debug" command line prompt to get debugging information.
 * <P>
 * Note that this is a trivial Prolog interpreter; do not expect great things 
 * of it.  */
public class PrologCompiler  implements RuntimeConstants
{
    PrologParser parse;

    /** A counter for the variables which store continuations */
    int fieldCount = 0;

    /** A counter for labels of loops */
    int loopCount = 0;

    /** Print code for debugging */
    boolean printCode = false;

    public PrologCompiler(InputStream is)
    {
        parse = new PrologParser(is);
    }

    public static void main(String args[])
    {
	boolean printCode = false;
        for(int i = 0; i < args.length; i++)
        {
            try {
		if(args[i].equals("-debug"))
		    printCode = true;
                FileInputStream is = new FileInputStream(args[i]);
		PrologCompiler c = new PrologCompiler(is);
		c.printCode = printCode;
                c.compile();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    void compile()
    {
        loopCount = 0;
        Hashtable hash = new Hashtable();
        try {
            while(true)
            {
                // Read all commands
                // Transform bound arguments into = calls
                // Use a hash table to group commands
                // by functor and arguments
                Object a = parse.command();
                Structure s = transformHead(a);
                Structure head = (Structure) s.arg[0];
                String name = head.functor + "/" +
                    head.arg.length;
                Vector v = (Vector) hash.get(name);
                if(v == null)
                {
                    v = new Vector();
                    v.addElement(s);
                    hash.put(name, v);
                }
                else
                    v.addElement(s);
            }
        }
        catch(IOException e)
        {
            if(e.getMessage().equals("EOF"))
                ;
            else
                e.printStackTrace();
        }

        // Generate a class for each functor/args
        for(Enumeration keys = hash.keys(); keys.hasMoreElements();)
        {
            Vector v = (Vector) hash.get(keys.nextElement());
            Structure[] s = new Structure[v.size()];
            v.copyInto(s);
            generateCode(s);
        }
    }
    /** Converts the rule into standard form beginning with :-
     * and no variables in the head.
     */
    Structure transformHead(Object a)
    {
        if(a instanceof Structure)
        {
            Structure s = (Structure) a;
            if(!s.functor.equals(":-"))
                s = new Structure(":-", s, null);
            Structure rule = transformHead(s.arg[0], s.arg[1]);
            return rule;
        }
        else
            return new Structure(":-",
                new Structure(a, new Object[0]),
                null);
    }


    /** Converts a structure of the form w(x,y,z) in a rule
     * head into:
     * w(X,Y,Z), X=x, Y=y, Z=z
     * The tail is tacked  onto the end
     */
    Structure transformHead(Object o, Object head)
    {
        if(o instanceof Structure)
        {
            Structure s = (Structure) o;
            for(int i = s.arg.length-1; i >= 0; i--)
            {
                Var v = new Var();
                Structure s2 = new Structure("=", v, s.arg[i]);
                s.arg[i] = v;
                if(head == null)
                    head = s2;
                else
                    head = new Structure(",", s2, head);
            }
        }
        else
            o = new Structure(o, new Object[0]);
        return new Structure(":-", o, head);
    }

    /** Given a set of structures of the form
     * w(X,Y,Z) :- body
     * Generate a class called w with a method f which
     * runs body. f returns true if it finds a binding,
     * and false if it fails. Call multiple times for
     * different bindings.
     *
     * Body looks like:
     * =(X,foo), f(W,X), g(P,Q),
     * This translates into (roughly)
     *   p.undoBindings()
     *   state_tag = p.markTrail();
     *   f = new f(p);
     *   g = new g(p);
     *   if(!p.unify(X, foo)) goto test1break
     *       loop1: if(!f(W,X))  goto loop1break
     *           loop2: if(!g(P,Q)) goto loop2break
     *                      return true;
     *                  stateX:
     *                      goto loop2
     *           loop2break:
     *              goto loop1
     *       loop1break:
     *   test1break:
     *   state = X+1;
     *
     * All variables are fields in the class.
     * yield is implemented as a return, setting state
     * just before returning. The method begins with a
     * tableswitch which jumps directly to stateX.
     *
     * @see Prolog
     */
    void generateCode(Structure[] rules)
    {
        boolean save = true;
        Structure head = (Structure) rules[0].arg[0];
        String classname = head.functor + "_" + head.arg.length;

        JavaClass cf = new JavaClass(classname, "java/lang/Object");
        cf.setAccess(ACC_PUBLIC);

        cf.addField("p", "LCOM/sootNsmoke/prolog/Prolog;", 0);
        cf.addField("state", "I", 0);
        cf.addField("stackTag", "Ljava/lang/Object;", 0);

        addConstructor(cf);

        // There are rules.length+1 states: one for each rule,
        // plus one at the beginning
        String states[] = new String[rules.length+1];
        for(int i = 0; i < rules.length+1; i++)
            states[i] = "state"+i;
        Sequence method =
            new Aload(0).append(
            new Getfield(classname, "state", "I")).append(
            new Tableswitch(0, "fail", states));
        method = method.append(new Label("state0"));

        int numLocalVars=0;

        for(int i = 0; i < rules.length; i++)
        {
            // Get the list of variables. The unbound ones
            // are the ones which appear after the
            // arguments. Create a binding for each.
            Vector v = new Vector();
            variablesList(rules[i], v);
            Var[] vars = new Var[v.size()];
            v.copyInto(vars);
            Hashtable bindings = new Hashtable();
            for(int q = 0; q < vars.length; q++)
            {
                Sequence load;
                if(q < head.arg.length)
                {
                    load = new Aload(q+1);

                }
                else
                {
                    String fieldname = "v"+(numLocalVars++);
                    cf.addField(fieldname,
                                "LCOM/sootNsmoke/prolog/Var;",
                                0);
                    method = method
                    .append(new Aload(0))
                    .append(new New("COM/sootNsmoke/prolog/Var"))
                    .append(new Dup())
                    .append(new InvokeSpecial(
                                "COM/sootNsmoke/prolog/Var",
                                "<init>",
                                "()V"))
                    .append(new Putfield(
                                classname, fieldname,
                                "LCOM/sootNsmoke/prolog/Var;"));
                    load =  new Aload(0)
                    .append(new Getfield(classname, fieldname,
                                "LCOM/sootNsmoke/prolog/Var;"));
                }
                bindings.put(vars[q], load);
            }

            // This is the base case: yield true
            int currentState = i+1;
            Sequence success =
                    new Aload(0)
            .append(new Sipush(i+1))
            .append(new Putfield(classname, "state", "I"))
            .append(new Iconst(1))
            .append(new Ireturn())
            .append(new Label("state"+currentState));


            // Each rule is of the form :-(head, body).
            // We want to compile the body. The main work
            // is done by compileTerm().
            // Each rule begins with code to prepare the
            // variable trail by erasing everything since the
            // last tag, then tagging the trail with a new tag
            Object body = rules[i].arg[1];
            Sequence rule =
                    new Aload(0)
            .append(new Getfield(classname,
                                 "p",
                                 "LCOM/sootNsmoke/prolog/Prolog;"))
            .append(new Aload(0))
            .append(new Getfield(classname, "stackTag", "Ljava/lang/Object;"))
            .append(new InvokeVirtual("COM/sootNsmoke/prolog/Prolog",
                                      "undoBindings",
                                      "(Ljava/lang/Object;)V"))
            .append(new Aload(0))
            .append(new Aload(0))
            .append(new Getfield(classname, "p", "LCOM/sootNsmoke/prolog/Prolog;"))
            .append(new InvokeVirtual("COM/sootNsmoke/prolog/Prolog",
                                      "markTrail",
                                      "()Ljava/lang/Object;"))
            .append(new Putfield(classname,
                             "stackTag",
                             "Ljava/lang/Object;"))
            .append(compileTerm(body, bindings, success,
                    classname, currentState, cf))
            .append(new Label("failstate"+currentState));

            method = method.append(rule);
        }

        // Add the final fail case
        method = method.append(new Label("fail").append(
                              new Iconst(0)).append(

                              new Ireturn()));

        // Add the method to the class
        try {
            if(printCode)
		System.out.println(method);
            Bytecodes bytecodes =  new Bytecodes(method, cf);
            String signature = "";
            for(int i = 0; i < head.arg.length; i++)
                signature += "Ljava/lang/Object;";
            signature = "(" + signature + ")Z";
            cf.addMethod("f",
                         signature,
                         ACC_PUBLIC,
                         method.max_stack(),
                         head.arg.length+1,
                         bytecodes.toByteArray(),
                         new ExceptionTableEntry[0],
                         new Attribute[0], new Attribute[0]);

            if(save)
            {
                OutputStream os =
                    new FileOutputStream(classname + ".class");
                cf.write(os);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Generate code to push a value onto the stack.
     * If val is a String, load it
     * If val is a Variable in the parameter list, use aload_x
     *    where x is the position in the parameter list.
     *    (Add 1 because 0 is this)
     * If val is a non-parmeter variable, create it
     * Otherwise, give up.
     */
    Sequence compileArg(Object val, Hashtable bindings)
    {
        if(val == null)
            return new AconstNull();
        if(val instanceof String)
            return new Ldc((String) val);
        if(val instanceof Var)
            return (Sequence) bindings.get(val);
        if(val instanceof Cons)
        {
            Cons cons = (Cons) val;
            return  new New("COM/sootNsmoke/prolog/Cons")
            .append(new Dup())
            .append(compileArg(cons.head, bindings))
            .append(compileArg(cons.tail, bindings))
            .append(new InvokeSpecial("COM/sootNsmoke/prolog/Cons",
                                      "<init>",
                                      "(Ljava/lang/Object;Ljava/lang/Object;)V"));
        }

        throw new RuntimeException("Don't understand how to load " + val);
    }

    /** Compile the term o into a sequence of instructions
     * which evaluates it. The arguments mean:
     *
     * o: the code to compile
     * bindings: a hashtable of Variable -> Sequence, which
     *           given a variable returns the code to load it.
     * cont: code to do if this term succeeds.
     * classname: the name of the class in which this code is
     *            generated
     * currentState: the current state.  (Duh.)
     *
     * o should be of one of these forms:
     * =(X,Y),(X, Y), and W(X, Y, Z, ...)
     *
     * For =(X, Y), the code compiles to:
     *    if(p.unify(X, Y))
     *        cont
     * For ,(X, Y), the code compiles to
     *    compileTerm(Y, compileTerm(X))
     *    (That is, compile X, then use it as the continuation
     *    for the compilation of y).
     * For w(X, Y, Z, ...)
     *    w = new w();
     *    while(w(X, Y, Z, ...)
     *        cont
     */
    public Sequence compileTerm(Object o,
                                Hashtable bindings,
                                Sequence cont,
                                String classname,
                                int currentState,
                                JavaClass cf)
    {
        if(o == null)
            return cont;
        else if(o instanceof Structure)
        {
            Structure s = (Structure) o;
            if(s.functor.equals("="))
            {
                Sequence args = new EmptySequence();
                for(int k = 0; k < s.arg.length; k++)
                    args = args.append(
                        compileArg(s.arg[k], bindings));
                String endLoopLabel = "endloop"+(loopCount++);
                Sequence seq =
                            new Aload(0)
                    .append(new Getfield(
                                classname,
                                "p",
                                "LCOM/sootNsmoke/prolog/Prolog;"))
                    .append(args)
                    .append(new InvokeVirtual(
                                "COM/sootNsmoke/prolog/Prolog",
                                "unify",
                                "(Ljava/lang/Object;Ljava/lang/Object;)Z"))
                    .append(new Ifeq(endLoopLabel))
                    .append(cont)
                    .append(new Label(endLoopLabel));
                return seq;
            }
            else if(s.functor.equals(","))
            {
                for(int i = s.arg.length-1; i >= 0; i--)
                    cont = compileTerm(
                        s.arg[i], bindings, cont,
                        classname, currentState, cf);
                return cont;
            }
            else
            {
                // Add a field to hold an instance of the
                // predicate being called, and create
                // that instance
                String predicate = s.functor+"_"+s.arg.length;
                String fieldname = predicate + "_" +(fieldCount++);
                String fieldtype = "L"+predicate+";";
                cf.addField(fieldname,fieldtype, 0);
                Sequence seq =
                    new Aload(0).append(
                    new New(predicate)).append(
                    new Dup()).append(
                    new Aload(0)).append(
                    new Getfield(classname, "p", "LCOM/sootNsmoke/prolog/Prolog;")).append(
                    new InvokeSpecial(predicate, "<init>",
                        "(LCOM/sootNsmoke/prolog/Prolog;)V")).append(
                    new Putfield(classname, fieldname, fieldtype));

                String descriptor = "";
                Sequence loadArgs = new EmptySequence();
                for(int i = 0; i < s.arg.length; i++)
                {
                    descriptor += "Ljava/lang/Object;";
                    loadArgs = loadArgs.append(compileArg(s.arg[i], bindings));
                }
                descriptor = "("+descriptor + ")Z";

                String endLabel = "end"+loopCount;
                seq = seq
                    .append(new Label("loop"+loopCount)
                    .append(new Aload(0))
                    .append(new Getfield(classname, fieldname, fieldtype))
                    .append(loadArgs)
                    .append(new InvokeVirtual(predicate, "f", descriptor))
                    .append(new Ifeq(endLabel))
                    .append(cont)
                    .append(new Goto("loop"+loopCount)))
                    .append(new Label(endLabel));
                loopCount++;

                return seq;
            }
        }
        else if(o instanceof String)
        {
            // Treat a String as a no-argument structure
            return compileTerm(new Structure(o, new Object[0]),
                               bindings, cont,
                               classname, currentState, cf);
        }
        throw new RuntimeException("Don't know how to compile term " + o);
    }


    /** Adds a constructor to the class. The constructor
     * takes a single argument (a Prolog) and stores it in
     * the p field
     */
    public void addConstructor(JavaClass cf)
    {
        Sequence seq =
            new Aload(0).append(
            new InvokeSpecial(cf.getSuperclassName(), "<init>", "()V")).append(
            new Aload(0)).append(
            new Aload(1)).append(
            new Putfield(cf.getClassName(), "p",
                         "LCOM/sootNsmoke/prolog/Prolog;")).append(
            new Return());
        try {
            Bytecodes bytecodes = new Bytecodes(seq, cf);
            cf.addMethod("<init>",
                         "(LCOM/sootNsmoke/prolog/Prolog;)V",
                         ACC_PUBLIC,
                         seq.max_stack(),
                         seq.max_vars()+1,
                         bytecodes.toByteArray(),
                         new ExceptionTableEntry[0],
                         new Attribute[0], new Attribute[0]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /** Builds a list of variables in the object.
     * into vector v. They appear in the vector in
     * parse order.
     */
    void variablesList(Object o, Vector v)
    {
        if(o instanceof Var)
        {
            boolean found = false;
            for(int i = 0; i < v.size() && !found; i++)
                if(o.equals(v.elementAt(i)))
                    found = true;
            if(!found)
                v.addElement(o);
        }
        else if(o instanceof Structure)
        {
            Structure s = (Structure) o;
            for(int i = 0; i < s.arg.length; i++)
                variablesList(s.arg[i], v);
        }
        else if(o instanceof Cons)
        {
            Cons cons = (Cons) o;
            variablesList(cons.head, v);
            variablesList(cons.tail, v);
        }
    }
}
