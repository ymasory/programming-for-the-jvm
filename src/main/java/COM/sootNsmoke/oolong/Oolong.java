package COM.sootNsmoke.oolong;
import java.io.*;
import COM.sootNsmoke.jvm.*;
import COM.sootNsmoke.instructions.*;
import java.util.*;

/** The Oolong assembler
 * Running from the command line:
 *     java COM.sootNsmoke.oolong.Oolong [-g] [-n] [file...]
 *
 * Options:
 *    -g generate debugging info
 *    -n automatically generate line numbers (implies -g)
 */
public class Oolong implements RuntimeConstants
{
    /** The name of the file being processed */
    public String inputFilename = "";

    /** The name of the source of the data (either the input name or
     * from the .source line )
     */
    public String filename;

    /** The lexical analyzer being used */
    Lexer lexer;

    /** The class file being built */
    JavaClass cf;

    /** The name of the class being built */
    String className;

    /** The directory in which to place the file */
    String directory = ".";

    /** Whether or not to auto-number */
    public boolean autonumber = false;

    /** Whether or not to add debugging info */
    public boolean debug_info = false;

    /** Print extra messages */
    public boolean verbose = false;

    public static void main(String a[])
    {
        Oolong oolong = new Oolong();
        for(int i = 0; i < a.length; i++)
        {
            try
            {
                if(a[i].equals("-g"))
                    oolong.debug_info = true;
                else if(a[i].equals("-n"))
                {
                    oolong.debug_info = true;
                    oolong.autonumber = true;
                }
                else if(a[i].equals("-d"))
                {
                    i++;
                    if(i >= a.length)
                        usage();
                    else
                        oolong.directory = a[i];
                }
                else if(a[i].equals("-verbose"))
                    oolong.verbose = true;
                else if(a[i].startsWith("-"))
                {
                    usage();
                }
                else
                {
                    oolong.inputFilename = a[i];
                    oolong.filename = a[i];
                    InputStream is = new FileInputStream(oolong.filename);
                    oolong.assemble(is, defaultClassName(a[i]));
                }
            }
            catch(SyntaxError e)
            {
                System.err.println(e.getMessage());
            }
            catch(Exception e)
            {
                System.err.println("Cannot open file: " + a[i]);
                e.printStackTrace();
            }
        }
    }

    /** Print some usage information */
    static void usage()
    {
        System.err.println("Oolong - a Java assembler");
        System.err.println("Usage:");
        System.err.println("   Oolong [-g] [-n] [-d directory] [-verbose] file1.j ...");
        System.err.println("   -g Generate debugging info");
        System.err.println("   -n Generate debugging info using auto-generated line numbers");
        System.err.println("   -d Place output files in directory");
        System.err.println("   -verbose Verbose messages");
    }

    /** Takes the unscoped name of the class (everything after the last dot)
     * and tacks a ".class" onto it
     */
    public String outputFile(String className)
    {
        String fileName = className.substring(className.lastIndexOf("/")+1);
        return directory + "/" + fileName + ".class";
    }

    public void assemble(InputStream is, String default_classname)
        throws SyntaxError, IOException
    {
        lexer = new Lexer(is);

        int n = 0;

        while(!lexer.eof)
        {
            className = default_classname;
            assembleClass();
            cf.setSourceFile(filename);
            OutputStream os = new FileOutputStream(outputFile(className));
            cf.write(os);

            // Peek at the next token
            lexer.nextToken();
            lexer.pushBack();
        }
    }

    void assembleClass() throws SyntaxError, IOException
    {
        String superclass = "java/lang/Object";
        Vector interfaces = new Vector();

        int modifiers = 0;
        while(lexer.nextToken() == Lexer.TT_WORD)
        {
            if(lexer.sval.equals(".class") || lexer.sval.equals(".interface"))
            {
                if(lexer.sval.equals(".interface"))
                    modifiers |= ACC_INTERFACE;
                while(lexer.nextToken() == Lexer.TT_WORD)
                {
                    String modifier = lexer.sval;
                    if(modifier.equals("public"))
                        modifiers |= ACC_PUBLIC;
                    else if(modifier.equals("final"))
                        modifiers |= ACC_FINAL;
                    else if(modifier.equals("super"))
                        modifiers |= ACC_SUPER;
                    else if(modifier.equals("interface"))
                        modifiers |= ACC_INTERFACE;
                    else if(modifier.equals("abstract"))
                        modifiers |= ACC_ABSTRACT;
		    else if(modifier.equals("strictfp"))
			modifiers |= ACC_STRICTFP;
                    else
                    {
                        className = lexer.sval;
                        break;
                    }
                }
                if(className == null)
                    throw new SyntaxError(this, ".class directive requires a class name");
            }
            else if(lexer.sval.equals(".super"))
            {
                if(lexer.nextToken() == Lexer.TT_WORD)
                    superclass = lexer.sval;
                else
                    throw new SyntaxError(this, ".super directive requires a class name");
            }
            else if(lexer.sval.equals(".implements"))
            {
                String classname = nextWord();
                interfaces.addElement(classname);
            }
            else if(lexer.sval.equals(".source"))
            {
                filename = nextWord("source file name");
            }
            else
            {
                lexer.pushBack();
                break;
            }
        }

        cf = new JavaClass(className, superclass);

        for(int i = 0; i < interfaces.size(); i++)
            cf.addInterface((String) interfaces.elementAt(i));
        cf.setAccess(modifiers);

        while(lexer.nextToken() != Lexer.TT_EOF)
        {
            if(lexer.ttype != Lexer.TT_WORD)
                throw new SyntaxError(this, "unexpected token");
            else if(lexer.sval.equals(".field"))
                field();
            else if(lexer.sval.equals(".method"))
                method();
            else if(lexer.sval.equals(".end"))
                if(lexer.nextToken() == Lexer.TT_WORD && lexer.sval.equals("class"))
                    break;
                else
                    throw new SyntaxError(this,
                        "Unknown directive: .end " + lexer.sval);
            else
                throw new SyntaxError(this,
                         "Unknown directive: " + lexer.sval);
        }
    }

    /** Returns the default class name for a file.
     * That's everthing after the last "/" or "\"
     */
    static String defaultClassName(String name)
    {
        int lastSlash = name.lastIndexOf('/');
        if(lastSlash == -1)
            lastSlash = name.lastIndexOf('\\');
        name = name.substring(lastSlash+1);
        if(name.endsWith(".j"))
            name = name.substring(0, name.length()-2);
        return name;
    }

    void field() throws SyntaxError, IOException
    {
        int modifiers = 0;
        while(lexer.nextToken() == Lexer.TT_WORD)
        {
            if(lexer.sval.equals("public"))
                modifiers |= ACC_PUBLIC;
            else if(lexer.sval.equals("private"))
                modifiers |= ACC_PRIVATE;
            else if(lexer.sval.equals("protected"))
                modifiers |= ACC_PROTECTED;
            else if(lexer.sval.equals("static"))
                modifiers |= ACC_STATIC;
            else if(lexer.sval.equals("final"))
                modifiers |= ACC_FINAL;
            else if(lexer.sval.equals("transient"))
                modifiers |= ACC_TRANSIENT;
            else if(lexer.sval.equals("volatile"))
                modifiers |= ACC_VOLATILE;
	    else if(lexer.sval.equals("strictfp"))
		modifiers |= ACC_STRICTFP;
            else
            {
                lexer.pushBack();
                break;
            }
        }
        if(lexer.nextToken() != Lexer.TT_WORD)
            throw new SyntaxError(this, "expecting field name");
        String fieldname = lexer.sval;
        String signature = signature();
        if(lexer.nextToken() == '=')
        {
            String initializer = nextWord();
            if(signature.equals("I") ||
               signature.equals("B") ||
               signature.equals("S") ||
               signature.equals("Z") ||
               signature.equals("C"))
            {
                if(lexer.isInt(initializer))
                    cf.addField(fieldname, modifiers,
                        (int) lexer.parseLong(initializer));
                else
                    throw new SyntaxError(this, "illegal initializer for integer");
            }
            else if(signature.equals("Ljava/lang/String;"))
            {
                if(lexer.ttype == '"')
                    cf.addField(fieldname, modifiers, lexer.sval);
                else
                    throw new SyntaxError(this, "Illegal initializer for String");
            }
            else if(signature.equals("D"))
            {
                if(lexer.isDouble(initializer))
                    cf.addField(fieldname, modifiers,
                        lexer.parseDouble(initializer));
                else
                    throw new SyntaxError(this, "Illegal initializer for double");
            }
            else if(signature.equals("F"))
            {
                if(lexer.isFloat(initializer))
                    cf.addField(fieldname, modifiers,
                    (float) lexer.parseDouble(initializer));
                else
                    throw new SyntaxError(this, "Illegal initializer for float");
            }
            else if(signature.equals("J"))
            {
                if(lexer.isLong(initializer))
                    cf.addField(fieldname, modifiers,
                        (long) lexer.parseLong(initializer));
                else
                    throw new SyntaxError(this, "Illegal initializer for long");
            }
            else
                throw new SyntaxError(this, "fields of type " + signature + " cannot be initialized");
        }
        else
        {
            cf.addField(fieldname, signature, modifiers);
            lexer.pushBack();
        }
    }

    void method() throws SyntaxError, IOException
    {
        // Holds a list of strings, each of which is the name of a
        // class which might be thrown
        Vector exceptions = new Vector();

        // Holds catch data; each element is a list of strings
        // { classname, label1, label2, label3 }
        Vector catches = new Vector();

        // Holds variable data; each element is an array of strings
        // { number, name, signature, from, to }
        // If the last two are null, they are 0 and bytecode-length,
        // respectively.
        Vector vars = new Vector();


        int modifiers = 0;
        while(lexer.nextToken() == Lexer.TT_WORD)
        {
            if(lexer.sval.equals("public"))
                modifiers |= ACC_PUBLIC;
            else if(lexer.sval.equals("private"))
                modifiers |= ACC_PRIVATE;
            else if(lexer.sval.equals("protected"))
                modifiers |= ACC_PROTECTED;
            else if(lexer.sval.equals("static"))
                modifiers |= ACC_STATIC;
            else if(lexer.sval.equals("final"))
                modifiers |= ACC_FINAL;
            else if(lexer.sval.equals("synchronized"))
                modifiers |= ACC_SYNCHRONIZED;
            else if(lexer.sval.equals("native"))
                modifiers |= ACC_NATIVE;
            else if(lexer.sval.equals("abstract"))
                modifiers |= ACC_ABSTRACT;
            else
            {
                lexer.pushBack();
                break;
            }

        }

        String method_name = nextWord("method name");
        String method_sig = method_signature();

        Sequence seq = new EmptySequence();
        int limit_stack = -1;
        int limit_locals = -1;
        while(true)
        {
            lexer.nextToken();
            if(lexer.ttype == -1)
                throw new SyntaxError(this, "Unexpected end of file");
            if(lexer.ttype != Lexer.TT_WORD)
                throw new SyntaxError(this, "Unexpected token on line " + lexer.lineno());
            if(lexer.sval.equals(".end"))
                if(lexer.nextToken() == Lexer.TT_WORD && lexer.sval.equals("method"))
                    break;
                else
                    throw new SyntaxError(this, "unexpected token");
            else if(lexer.sval.equals(".limit"))
            {
                String wd = nextWord("stack or locals");
                if(wd.equals("stack"))
                    limit_stack = (int) nextInt();
                else if(wd.equals("locals"))
                    limit_locals = (int) nextInt();
                else
                    throw new SyntaxError(this, "unrecognized limit directive: " + wd);
            }
            else if(lexer.sval.equals(".throws"))
            {
                String classname = nextWord();
                exceptions.addElement(classname);

            }
            else if(lexer.sval.equals(".catch"))
            {
                //.catch <classname> from <label1> to <label2> using <label3>
                // catch_labels is the list {classname, label1, label2, label3}
                String[] catch_labels = new String[4];
                catch_labels[0] = nextWord();
                if(!(nextWord().equals("from")))
                    throw new SyntaxError(this, "catch requires from");
                catch_labels[1] = nextWord();
                if(!(nextWord().equals("to")))
                    throw new SyntaxError(this, "catch requires to");
                catch_labels[2] = nextWord();
                if(!(nextWord().equals("using")))
                    throw new SyntaxError(this, "catch requires using");
                catch_labels[3] = nextWord();
                catches.addElement(catch_labels);
            }
            else if(lexer.sval.equals(".line"))
            {
                int n = nextInt();
                seq = seq.appendSequence(
                    new LineNumber(n));
            }
            else if(lexer.sval.equals(".var"))
            {
                // .var <var-number> is  <name> <signature> from <label1> to <label2>
                String var_number = nextWord("variable number");
                if(!(nextWord().equals("is")))
                    throw new SyntaxError(this, "\"is\" expected");
                String name = nextWord("variable name");
                String signature = signature();
                String from = null;
                String to = null;
                if(nextWord().equals("from"))
                {
                    from = nextWord();
                    if(nextWord().equals("to"))
                        to = nextWord();
                    else
                        lexer.pushBack();
                }
                else
                    lexer.pushBack();
                String[] var_info = { var_number, name, signature, from, to };
                vars.addElement(var_info);
            }
            else // Must be mnemonic
            {
                Sequence inst = translateMnemonic(lexer.sval);
                if(autonumber && !(inst instanceof Label))
                    seq = seq.appendSequence(
                        new LineNumber(lexer.lineno()));
                seq = seq.appendSequence(inst);
            }
        }


        /** Add the method to the class file */
        Bytecodes bytecodes;
        try {
            bytecodes = new Bytecodes(seq, cf);
        }
        catch(UndefinedLabelException e) {
            throw new SyntaxError(this, e.getMessage());
        }

        if(limit_stack < 0)
        {
            limit_stack = seq.max_stack();
            if(verbose)
                System.err.println("Inferred max stack for " + method_name
                                   + " = " + limit_stack);
        }

        if(limit_locals < 0)
        {
            limit_locals = seq.max_vars()+1;
            int param_count = Invoke.countArgs(method_sig)+1;
            if(param_count > limit_locals)
                limit_locals = param_count;
            if(verbose)
                System.err.println("Inferred max locals for " + method_name
                                   + " = " + limit_locals);
        }

        Attribute code_attribs[] = new Attribute[0];
        if(debug_info)
        {
            code_attribs = new Attribute[2];
            code_attribs[0] =
                bytecodes.createLineNumberTableAttribute();

            LocalVariableTableEntry[] local_var_table =
                new LocalVariableTableEntry[vars.size()];
            for(int i = 0; i < vars.size(); i++)
            {
                String[] var_info = (String[]) vars.elementAt(i);
                int index = (int) Integer.parseInt(var_info[0]);
                String name = var_info[1];
                String sig = var_info[2];
                int start_pc = 0;
                int end_pc = (int) bytecodes.length();
                if(var_info[3] != null)
                    start_pc = (int) bytecodes.labelLoc(var_info[3]);
                if(var_info[4] != null)
                    end_pc = (int) bytecodes.labelLoc(var_info[4]);
                local_var_table[i] =
                    cf.createLocalVariableTableEntry(
                        start_pc,
                        (int) (end_pc - start_pc),
                        name,
                        sig,
                        index);
            }

            code_attribs[1] = cf.createLocalVariableTableAttribute(
                    local_var_table);
        }

        // Create table of exception handlers
        ExceptionTableEntry[] exception_table =
            new ExceptionTableEntry[catches.size()];

        for(int i = 0; i < catches.size(); i++)
        {
            String[] handler = (String[]) catches.elementAt(i);
            String catch_type = handler[0];
            int start_pc = (int) bytecodes.labelLoc(handler[1]);
            int end_pc = (int) bytecodes.labelLoc(handler[2]);
            int handler_pc = (int) bytecodes.labelLoc(handler[3]);
            // Special case: all handles all exceptions
            if(handler[i].equals("all"))
                exception_table[i] =
                    cf.createExceptionTableEntry(start_pc, end_pc, handler_pc);
            else
                exception_table[i] =
                    cf.createExceptionTableEntry(catch_type,
                                                 start_pc, end_pc, handler_pc);
        }

        // Create list of thrown exceptions
        String[] excpts = new String[exceptions.size()];
        exceptions.copyInto(excpts);
        Attribute exception_attribute =
            cf.createExceptionsAttribute(excpts);
        Attribute method_attribs[] = {
            exception_attribute };


        byte[] bc = bytecodes.toByteArray();

        cf.addMethod(method_name,
                     method_sig,
                     modifiers,
                     limit_stack,
                     limit_locals,
                     bc,
                     exception_table,
                     code_attribs,
                     method_attribs);
    }

    /** Parses a method signature */
    String method_signature() throws SyntaxError, IOException
    {
        if(lexer.nextToken() != '(')
            throw new SyntaxError(this, "expected a (");
        String args = signature();
        if(lexer.nextToken() != ')')
            throw new SyntaxError(this, "expected a )");
        String ret = signature();
        return "(" + args + ")" + ret;
    }

    /** Parses a signature */
    String signature() throws SyntaxError, IOException
    {
        String sig = "";
        if(lexer.nextToken() == Lexer.TT_WORD)
        {
            lexer.pushBack();
            sig = nextWord("signature");
        }
        else
            lexer.pushBack();
            /*
        try {
            lexer.ordinaryChar(';');
            lexer.wordChars(';', ';');
            if(lexer.nextToken() == Lexer.TT_WORD)
            {
                lexer.pushBack();
                sig = nextWord("signature");
            }
            else
                lexer.pushBack();

        } finally {
            lexer.commentChar(';');
        }
                    */

        return sig;
    }

    String nextWord() throws SyntaxError, IOException
    {
        return lexer.nextWord(this);
    }

    String nextWord(String token_type) throws SyntaxError, IOException
    {
        try
        {
            return lexer.nextWord(this);
        }
        catch(SyntaxError e)
        {
            throw new SyntaxError(this, token_type + " expected");
        }
    }

    int nextInt() throws SyntaxError, IOException
    {
        return nextInt("number expected");
    }

    int nextInt(String msg) throws SyntaxError, IOException
    {
        String word = nextWord();
        try
        {
            return (int) lexer.parseLong(word);
        }
        catch(NumberFormatException e)
        {
            throw new SyntaxError(this, "Invalid number " + word);
        }
    }


    Sequence translateMnemonic(String mnemonic)
        throws SyntaxError, IOException
    {
        if(mnemonic.equals("aaload"))       return new Aaload();
        if(mnemonic.equals("aastore"))      return new Aastore();
        if(mnemonic.equals("aconst_null"))  return new AconstNull();
        if(mnemonic.equals("aload"))
            return new Aload(nextInt());
        if(mnemonic.equals("aload_0"))      return new Aload(0);
        if(mnemonic.equals("aload_1"))      return new Aload(1);
        if(mnemonic.equals("aload_2"))      return new Aload(2);
        if(mnemonic.equals("aload_3"))      return new Aload(3);
        if(mnemonic.equals("anewarray"))
            return new Anewarray(nextWord());
        if(mnemonic.equals("areturn"))      return new Areturn();
        if(mnemonic.equals("arraylength"))  return new ArrayLength();
        if(mnemonic.equals("astore"))
            return new Astore(nextInt());
        if(mnemonic.equals("astore_0"))     return new Astore(0);
        if(mnemonic.equals("astore_1"))     return new Astore(1);
        if(mnemonic.equals("astore_2"))     return new Astore(2);
        if(mnemonic.equals("astore_3"))     return new Astore(3);
        if(mnemonic.equals("athrow"))       return new Athrow();
        if(mnemonic.equals("baload"))       return new Baload();
        if(mnemonic.equals("bastore"))      return new Bastore();
        if(mnemonic.equals("bipush"))       return new Bipush((byte) nextInt());
        if(mnemonic.equals("caload"))       return new Caload();
        if(mnemonic.equals("castore"))      return new Castore();
        if(mnemonic.equals("checkcast"))
            return new CheckCast(nextWord());
        if(mnemonic.equals("d2f"))          return new D2f();
        if(mnemonic.equals("d2i"))          return new D2i();
        if(mnemonic.equals("d2l"))          return new D2l();
        if(mnemonic.equals("dadd"))         return new Dadd();
        if(mnemonic.equals("daload"))       return new Daload();
        if(mnemonic.equals("dastore"))      return new Dastore();
        if(mnemonic.equals("dcmpg"))        return new Dcmpg();
        if(mnemonic.equals("dcmpl"))        return new Dcmpl();
        if(mnemonic.equals("dconst_0"))     return new Dconst(0);
        if(mnemonic.equals("dconst_1"))     return new Dconst(1);
        if(mnemonic.equals("ddiv"))         return new Ddiv();
        if(mnemonic.equals("dload"))        return new Dload(nextInt());
        if(mnemonic.equals("dload_0"))      return new Dload(0);
        if(mnemonic.equals("dload_1"))      return new Dload(1);
        if(mnemonic.equals("dload_2"))      return new Dload(2);
        if(mnemonic.equals("dload_3"))      return new Dload(3);
        if(mnemonic.equals("dmul"))         return new Dmul();
        if(mnemonic.equals("dneg"))         return new Dneg();
        if(mnemonic.equals("drem"))         return new Drem();
        if(mnemonic.equals("dreturn"))      return new Dreturn();
        if(mnemonic.equals("dstore"))       return new Dstore(nextInt());
        if(mnemonic.equals("dstore_0"))     return new Dstore(0);
        if(mnemonic.equals("dstore_1"))     return new Dstore(1);
        if(mnemonic.equals("dstore_2"))     return new Dstore(2);
        if(mnemonic.equals("dstore_3"))     return new Dstore(3);
        if(mnemonic.equals("dsub"))         return new Dsub();
        if(mnemonic.equals("dup"))          return new Dup();
        if(mnemonic.equals("dup_x1"))       return new Dup_x1();
        if(mnemonic.equals("dup_x2"))       return new Dup_x2();
        if(mnemonic.equals("dup2"))         return new Dup2();
        if(mnemonic.equals("dup2_x1"))      return new Dup2_x1();
        if(mnemonic.equals("dup2_x2"))      return new Dup2_x2();
        if(mnemonic.equals("f2d"))          return new F2d();
        if(mnemonic.equals("f2i"))          return new F2i();
        if(mnemonic.equals("f2l"))          return new F2l();
        if(mnemonic.equals("fadd"))         return new Fadd();
        if(mnemonic.equals("faload"))       return new Faload();
        if(mnemonic.equals("fastore"))      return new Fastore();
        if(mnemonic.equals("fcmpg"))        return new Fcmpg();
        if(mnemonic.equals("fcmpl"))        return new Fcmpl();
        if(mnemonic.equals("fconst_0"))     return new Fconst(0);
        if(mnemonic.equals("fconst_1"))     return new Fconst(1);
        if(mnemonic.equals("fconst_2"))     return new Fconst(2);
        if(mnemonic.equals("fdiv"))         return new Fdiv();
        if(mnemonic.equals("fload"))        return new Fload(nextInt());
        if(mnemonic.equals("fload_0"))      return new Fload(0);
        if(mnemonic.equals("fload_1"))      return new Fload(1);
        if(mnemonic.equals("fload_2"))      return new Fload(2);
        if(mnemonic.equals("fload_3"))      return new Fload(3);
        if(mnemonic.equals("fmul"))         return new Fmul();
        if(mnemonic.equals("fneg"))         return new Fneg();
        if(mnemonic.equals("frem"))         return new Frem();
        if(mnemonic.equals("freturn"))      return new Freturn();
        if(mnemonic.equals("fstore"))       return new Fstore(nextInt());
        if(mnemonic.equals("fstore_0"))     return new Fstore(0);
        if(mnemonic.equals("fstore_1"))     return new Fstore(1);
        if(mnemonic.equals("fstore_2"))     return new Fstore(2);
        if(mnemonic.equals("fstore_3"))     return new Fstore(3);
        if(mnemonic.equals("fsub"))         return new Fsub();
        if(mnemonic.equals("getfield"))
        {
            String field = nextWord();
            String[] field_info = split_last(field, "/");
            String sig = signature();
            return new Getfield(field_info[0], field_info[1], sig);
        }
        if(mnemonic.equals("getstatic"))
        {
            String field = nextWord();
            String[] field_info = split_last(field, "/");
            String sig = signature();
            return new Getstatic(field_info[0], field_info[1], sig);
        }
        if(mnemonic.equals("goto"))
            return new Goto(nextWord());
        if(mnemonic.equals("goto_w"))       return new Goto_w(nextWord());
        if(mnemonic.equals("i2b"))          return new I2b();
        if(mnemonic.equals("i2c"))          return new I2c();
        if(mnemonic.equals("i2d"))          return new I2d();
        if(mnemonic.equals("i2f"))          return new I2f();
        if(mnemonic.equals("i2l"))          return new I2l();
        if(mnemonic.equals("i2s"))          return new I2s();
        if(mnemonic.equals("iadd"))         return new Iadd();
        if(mnemonic.equals("iaload"))       return new Iaload();
        if(mnemonic.equals("iand"))         return new Iand();
        if(mnemonic.equals("iastore"))      return new Iastore();
        if(mnemonic.equals("iconst_m1"))    return new Iconst(-1);
        if(mnemonic.equals("iconst_0"))     return new Iconst(0);
        if(mnemonic.equals("iconst_1"))     return new Iconst(1);
        if(mnemonic.equals("iconst_2"))     return new Iconst(2);
        if(mnemonic.equals("iconst_3"))     return new Iconst(3);
        if(mnemonic.equals("iconst_4"))     return new Iconst(4);
        if(mnemonic.equals("iconst_5"))     return new Iconst(5);
        if(mnemonic.equals("idiv"))         return new Idiv();
        if(mnemonic.equals("if_acmpeq"))    return new IfAcmpEq(nextWord("target"));
        if(mnemonic.equals("if_acmpne"))    return new IfAcmpNe(nextWord("target"));
        if(mnemonic.equals("if_icmpeq"))    return new IfIcmpEq(nextWord("target"));
        if(mnemonic.equals("if_icmpne"))    return new IfIcmpNe(nextWord("target"));
        if(mnemonic.equals("if_icmplt"))    return new IfIcmpLt(nextWord("target"));
        if(mnemonic.equals("if_icmpge"))    return new IfIcmpGe(nextWord("target"));
        if(mnemonic.equals("if_icmpgt"))    return new IfIcmpGt(nextWord("target"));
        if(mnemonic.equals("if_icmple"))    return new IfIcmpLe(nextWord("target"));
        if(mnemonic.equals("ifeq"))         return new Ifeq(nextWord("target"));
        if(mnemonic.equals("ifne"))         return new Ifne(nextWord("target"));
        if(mnemonic.equals("iflt"))         return new Iflt(nextWord("target"));
        if(mnemonic.equals("ifge"))         return new Ifge(nextWord("target"));
        if(mnemonic.equals("ifgt"))         return new Ifgt(nextWord("target"));
        if(mnemonic.equals("ifle"))         return new Ifle(nextWord("target"));
        if(mnemonic.equals("ifnonnull"))    return new Ifnonnull(nextWord("target"));
        if(mnemonic.equals("ifnull"))       return new Ifnull(nextWord("target"));
        if(mnemonic.equals("iinc"))         return new Iinc(nextInt(), nextInt());
        if(mnemonic.equals("iload"))        return new Iload(nextInt());
        if(mnemonic.equals("iload_0"))      return new Iload(0);
        if(mnemonic.equals("iload_1"))      return new Iload(1);
        if(mnemonic.equals("iload_2"))      return new Iload(2);
        if(mnemonic.equals("iload_3"))      return new Iload(3);
        if(mnemonic.equals("imul"))         return new Imul();
        if(mnemonic.equals("ineg"))         return new Ineg();
        if(mnemonic.equals("instanceof"))   return new Instanceof(nextWord("class"));
        if(mnemonic.equals("invokeinterface"))
            return invoke(mnemonic);
        if(mnemonic.equals("invokenonvirtual")) // For backwards compatibility
            return invoke("invokespecial");
        if(mnemonic.equals("invokespecial"))
            return invoke(mnemonic);
        if(mnemonic.equals("invokestatic"))
            return invoke(mnemonic);
        if(mnemonic.equals("invokevirtual"))
            return invoke(mnemonic);
        if(mnemonic.equals("ior"))          return new Ior();
        if(mnemonic.equals("irem"))         return new Irem();
        if(mnemonic.equals("ireturn"))      return new Ireturn();
        if(mnemonic.equals("ishl"))         return new Ishl();
        if(mnemonic.equals("ishr"))         return new Ishr();
        if(mnemonic.equals("istore"))       return new Istore(nextInt());
        if(mnemonic.equals("istore_0"))     return new Istore(0);
        if(mnemonic.equals("istore_1"))     return new Istore(1);
        if(mnemonic.equals("istore_2"))     return new Istore(2);
        if(mnemonic.equals("istore_3"))     return new Istore(3);
        if(mnemonic.equals("isub"))         return new Isub();
        if(mnemonic.equals("iushr"))        return new Iushr();
        if(mnemonic.equals("ixor"))         return new Ixor();
        if(mnemonic.equals("jsr"))          return new Jsr(nextWord());
        if(mnemonic.equals("jsr_w"))        return new Jsr_w(nextWord());
        if(mnemonic.equals("l2d"))          return new L2d();
        if(mnemonic.equals("l2f"))          return new L2f();
        if(mnemonic.equals("l2i"))          return new L2i();
        if(mnemonic.equals("ladd"))         return new Ladd();
        if(mnemonic.equals("laload"))       return new Laload();
        if(mnemonic.equals("land"))         return new Land();
        if(mnemonic.equals("lastore"))      return new Lastore();
        if(mnemonic.equals("lcmp"))         return new Lcmp();
        if(mnemonic.equals("lconst_0"))     return new Lconst(0);
        if(mnemonic.equals("lconst_1"))     return new Lconst(1);
        if(mnemonic.equals("ldc") ||
           mnemonic.equals("ldc_w"))
        {
            String word = nextWord();
            if(lexer.ttype == '"')
                return new Ldc(lexer.sval);
            if(lexer.isInt(word))
                return new Ldc((int) lexer.parseLong(word));
            else if(lexer.isFloat(word))
                return new Ldc((float) lexer.parseDouble(word));
            throw new SyntaxError(this, "invalid argument to ldc");
        }
        if(mnemonic.equals("ldc2_w"))
        {
            String word = nextWord();
            if(lexer.isLong(word))
                return new Ldc2_w(lexer.parseLong(word));
            if(lexer.isDouble(word))
                return new Ldc2_w(lexer.parseDouble(word));
            throw new SyntaxError(this, "invalid argument to ldc2_w");
        }
        if(mnemonic.equals("ldiv"))         return new Ldiv();
        if(mnemonic.equals("lload"))        return new Lload(nextInt());
        if(mnemonic.equals("lload_0"))      return new Lload(0);
        if(mnemonic.equals("lload_1"))      return new Lload(1);
        if(mnemonic.equals("lload_2"))      return new Lload(2);
        if(mnemonic.equals("lload_3"))      return new Lload(3);
        if(mnemonic.equals("lmul"))         return new Lmul();
        if(mnemonic.equals("lneg"))         return new Lneg();
        if(mnemonic.equals("lookupswitch"))
        {
            Vector v = new Vector();
            String default_target = null;
            while(default_target == null)
            {
                String match = nextWord("number or default");
                if(lexer.nextToken() != ':')
                    throw new SyntaxError(this, ": expected");
                String target = nextWord("target");
                if(match.equals("default"))
                {
                    default_target = target;
                }
                else
                {
                    MatchLabel pair = new MatchLabel(
                        Integer.parseInt(match), target);
                    v.addElement(pair);
                }
            }

            MatchLabel[] pairs = new MatchLabel[v.size()];
            v.copyInto(pairs);

            return new Lookupswitch(default_target, pairs);
        }
        if(mnemonic.equals("lor"))          return new Lor();
        if(mnemonic.equals("lrem"))         return new Lrem();
        if(mnemonic.equals("lreturn"))      return new Lreturn();
        if(mnemonic.equals("lshl"))         return new Lshl();
        if(mnemonic.equals("lshr"))         return new Lshr();
        if(mnemonic.equals("lstore"))       return new Lstore(nextInt());
        if(mnemonic.equals("lstore_0"))     return new Lstore(0);
        if(mnemonic.equals("lstore_1"))     return new Lstore(1);
        if(mnemonic.equals("lstore_2"))     return new Lstore(2);
        if(mnemonic.equals("lstore_3"))     return new Lstore(3);
        if(mnemonic.equals("lsub"))         return new Lsub();
        if(mnemonic.equals("lushr"))        return new Lushr();
        if(mnemonic.equals("lxor"))         return new Lxor();
        if(mnemonic.equals("monitorenter")) return new Monitorenter();
        if(mnemonic.equals("monitorexit"))  return new Monitorexit();
        if(mnemonic.equals("multianewarray"))
        {
            String sig = signature();
            int dim = nextInt("array dimension");
            return new Multianewarray(sig, dim);
        }
        if(mnemonic.equals("new"))          return new New(nextWord());
        if(mnemonic.equals("newarray"))
        {
            String t = nextWord();
            byte atype = Newarray.atype(t);
            if(atype == -1)
                throw new SyntaxError(this,
                                      "unknown array type " + t);
            return new Newarray(atype);
        }
        if(mnemonic.equals("nop"))          return new Nop();
        if(mnemonic.equals("pop"))          return new Pop();
        if(mnemonic.equals("pop2"))         return new Pop2();
        if(mnemonic.equals("putfield"))
        {
            String field = nextWord();
            String[] field_info = split_last(field, "/");
            String sig = signature();
            return new Putfield(field_info[0], field_info[1], sig);
        }
        if(mnemonic.equals("putstatic"))
        {
            String field = nextWord();
            String[] field_info = split_last(field, "/");
            String sig = signature();
            return new Putstatic(field_info[0], field_info[1], sig);
        }
        if(mnemonic.equals("ret"))          return new Ret(nextInt());
        if(mnemonic.equals("return"))       return new Return();
        if(mnemonic.equals("saload"))       return new Saload();
        if(mnemonic.equals("sastore"))      return new Sastore();
        if(mnemonic.equals("sipush"))       return new Sipush(nextInt());
        if(mnemonic.equals("swap"))         return new Swap();
        if(mnemonic.equals("tableswitch"))
        {
            int low = nextInt("low index of switch");
            Vector v = new Vector();
            String default_target = null;
            while(default_target == null)
            {
                String label = nextWord("label or default");
                if(label.equals("default"))
                {
                    if(lexer.nextToken() != ':')
                        throw new SyntaxError(this, ": expected");
                    default_target = nextWord("default target");
                }
                else
                    v.addElement(label);
            }

            String[] offsets = new String[v.size()];
            v.copyInto(offsets);
            return new Tableswitch(low, default_target, offsets);
        }
        if(mnemonic.equals("wide"))
        {
            String op = nextWord("wide mnemonic");
            int index = nextInt("index");
            if(op.equals("iload"))
                return new Wide(opc_iload, index);
            if(op.equals("fload"))
                return new Wide(opc_fload, index);
            if(op.equals("aload"))
                return new Wide(opc_aload, index);
            if(op.equals("lload"))
                return new Wide(opc_lload, index);
            if(op.equals("dload"))
                return new Wide(opc_dload, index);
            if(op.equals("istore"))
                return new Wide(opc_istore, index);
            if(op.equals("fstore"))
                return new Wide(opc_fstore, index);
            if(op.equals("astore"))
                return new Wide(opc_astore, index);
            if(op.equals("lstore"))
                return new Wide(opc_lstore, index);
            if(op.equals("dstore"))
                return new Wide(opc_dstore, index);
            if(op.equals("ret"))
                return new Wide(opc_ret, index);
            if(op.equals("iinc"))
            {
                int constant = nextInt("constant");
                return new Wide(opc_iinc, index, constant);
            }
            throw new SyntaxError(this, "invalid wide instruction " + op);
        }
        if(lexer.nextToken() == ':')
            return new Label(mnemonic);
        lexer.pushBack();

        throw new SyntaxError(this, "Unrecognized mnemonic: " + mnemonic);

    }

    /** Splits the string into an array of 2, where the first is
     * everything before the last delim, and the second is
     * everything after it.
     */
    String[] split_last(String str, String delim) throws SyntaxError
    {
        int ix = str.lastIndexOf(delim);
        if(ix == -1)
            throw new SyntaxError(this, str + " should contain " + delim);
        String ret[] = new String[2];
        ret[0] = str.substring(0, ix);
        ret[1] = str.substring(ix+1);
        return ret;
    }

    /** Handles the cases of an invocation mnemonic */
    Sequence invoke(String mnemonic) throws SyntaxError, IOException
    {
        String method_spec = nextWord();
        String[] spec_parts = split_last(method_spec, "/");
        String method_class = spec_parts[0];
        String method_name = spec_parts[1];
        String method_sig = method_signature();

        if(mnemonic.equals("invokeinterface"))
            return new InvokeInterface(method_class, method_name, method_sig, nextInt());
        if(mnemonic.equals("invokespecial"))
            return new InvokeSpecial(method_class, method_name, method_sig);
        if(mnemonic.equals("invokevirtual"))
            return new InvokeVirtual(method_class, method_name, method_sig);
        if(mnemonic.equals("invokestatic"))
            return new InvokeStatic(method_class, method_name, method_sig);
        throw new SyntaxError(this, "internal error: unknown invoke mnemonic");
    }
}



