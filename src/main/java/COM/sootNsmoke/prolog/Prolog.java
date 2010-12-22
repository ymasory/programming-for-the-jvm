package COM.sootNsmoke.prolog;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;

/** The main class of the Prolog system.
 */
public class Prolog
{
    /** Main is an interactive Prolog interpreter.
     */
    public static void main(String args[])
    {
        InputStream is = System.in;

        while(true)
        {
            try {
                System.out.print("?- ");
                System.out.flush();
                PrologParser parser = new PrologParser(is);
                Object command = parser.command();
                interpret(command);
            }
            catch(SyntaxError e)
            {
                System.out.println("syntax error: "+ e.getMessage());
            }
            catch(IOException e1)
            {
                break;
            }
            catch(Exception e2)
            {
                e2.printStackTrace();
            }
        }
    }

    /** A trivial Prolog interpreter which can interpret
     * rules with only a single predicate.
     */
    static void interpret(Object command) throws Exception
    {
        Prolog p = new Prolog();
        if(command instanceof Structure)
        {
            Vector vars = new Vector();
            findVars(command, vars);
            Structure s = (Structure) command;

            Class c = Class.forName(s.functor + "_" + s.arg.length);
            Constructor inits[] = c.getConstructors();
            Object pred = inits[0].newInstance(new Object[] {p});

            Method m[] = c.getDeclaredMethods();
	    int numAnswers = 0;
            while(((Boolean) m[0].invoke(pred, s.arg)).booleanValue())
            {
                for(int i = 0; i < vars.size(); i++)
                {
                    Var v = (Var) vars.elementAt(i);
                    if(i > 0)
                        System.out.print(", ");
                    System.out.print(v + " = " + p.toString(p.deref(v)));
                }
		if(vars.size() > 0)
		    System.out.println();
		numAnswers++;
            }
	    if(vars.size() == 0)
		System.out.println(numAnswers > 0);
		
            System.out.println("No (more) answers");
        }
    }

    /** Returns the list of variables in the command
     * in the vector vars
     */
    static void findVars(Object o, Vector vars)
    {
        if(o instanceof Var)
        {
            if(!vars.contains(o))
                vars.addElement(o);
        }
        if(o instanceof Structure)
        {
            Structure s = (Structure) o;
            for(int i = 0; i < s.arg.length; i++)
                findVars(s.arg[i], vars);
        }
        if(o instanceof Cons)
        {
            findVars(((Cons) o).head, vars);
            findVars(((Cons) o).tail, vars);
        }
    }

    /** The stack trail.  It is used for keeping track of
     * of variables which should be unbound during backtracking.
     * Other things may be on the stack as well; they are treated
     * as points where backtracking should stop.
     * @see markTrail
     */
    public Stack trail = new Stack();

    /** Follow pointers for bound variables */
    public Object deref(Object exp)
    {
        while(exp instanceof Var && ((Var) exp).bound())
            exp = ((Var)exp).binding();
        return exp;
    }

    /** Destructively unify x and y. This means that
     * the variables in x and y are set so that they
     * when x and y are fully dereferenced, they are identical.
     */
    public boolean unify(Object x, Object y)
    {
        x = deref(x);
        y = deref(y);


        if(x instanceof Var)
        {
            setBinding((Var) x, y);
            return true;
        }
        if(y instanceof Var)
        {
            setBinding((Var) y, x);
            return true;
        }
        if(x == null)
            return y == null;
        if(y == null)
            return false;

        if(x.equals(y))
            return true;
        if(x instanceof Cons && y instanceof Cons)
        {
            Cons xc = (Cons) x;
            Cons yc = (Cons) y;
            return unify(xc.head, yc.head) && unify(xc.tail, yc.tail);
        }
        return false;
    }

    /** Push v unto the trail stack, and rebind it to val */
    public void setBinding(Var v, Object val)
    {
        if(v != val)
        {
            trail.push(v);
            v.setBinding(val);
        }
    }

    /** Undo all bindings until tag is reached. If tag
     * is null, then don't undo anything.
     */
    public void undoBindings(Object tag)
    {
        if(tag == null)
            return;
        while(true)
        {
            Object o = trail.pop();
            if(o == tag)
                break;
            if(o instanceof Var)
                ((Var) o).makeUnbound();
        }
    }


    /** Marks the current location on the trail. Returns
     * the token used to mark the trail
     */
    public Object markTrail()
    {
        Object o = new Object();
        trail.push(o);
        return o;
    }

    /** Returns the prolog object as a String */
    public String toString(Object o)
    {
        if(o == null)
            return "[]";
        if(o instanceof Var)
            return toString(deref(o));
        if(o instanceof Structure)
            return ((Structure) o).toString(this);
        if(o instanceof Cons)
            return ((Cons) o).toString(this);
        return o.toString();
    }
}
