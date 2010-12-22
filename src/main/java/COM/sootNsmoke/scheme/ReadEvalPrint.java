package COM.sootNsmoke.scheme;
import java.io.*;
import COM.sootNsmoke.instructions.*;

/** A class that implements a read-eval-print loop */
public class ReadEvalPrint
{
    boolean debug = false;

    public static void main(String[] a)
    {
        BindingEnv env = new BindingEnv();
        SchemeLibrary.initializeEnvironment(env);

        boolean save = false;
        boolean debug = false;
        ReadEvalPrint p = new ReadEvalPrint();
        for(int i = 0; i < a.length; i++)
        {
            if(a[i].equals("-save"))
                save = true;
            else if(a[i].equals("-debug"))
                p.debug = true;
            else
            {
                try {
                    FileInputStream is = new FileInputStream(a[i]);
                    p.run(is, env, save, false);
                }
                catch(IOException e)
                {
                    System.err.println("Cannot open file: " + e.getMessage());
                }
            }

        }
        try {
            InputStream is = System.in;
            p.run(is, env, save, true);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Done.");
    }

    void run(InputStream is, BindingEnv env,
             boolean save,
             boolean prompt) throws IOException
    {
        SchemeCompiler compiler = new SchemeCompiler();
        compiler.save = save;
        Object expr;
        Reader reader = new Reader(is);
        if(prompt)
        {
            System.out.print("> ");
            System.out.flush();
        }
        while((expr = reader.readExpr()) != reader.eof)
        {
            try
            {
                Object val = eval(env, expr, compiler);
                System.out.println(write(val));

                if(prompt)
                {
                    System.out.print("> ");
                    System.out.flush();
                }
            }
            catch(Throwable e)
            {
                e.printStackTrace();
            }
        }
    }

    public Object eval(BindingEnv env,
                       Object expr,
                       SchemeCompiler compiler)
    {
        boolean save = false;
        try
        {
            Sequence compiled_expr =
                compiler.compileTopLevelFunction(expr);
            if(debug)
                System.out.println(compiled_expr);
            String classname = gensym();
            Class c = compiler.load(classname, compiled_expr);
            CompiledProcedure func = (CompiledProcedure) c.newInstance();
            func.env = env;
            Object ret = func.apply0();
            return ret;
        }
        catch(Exception e)
        {
            System.out.println("Error:");
            System.out.println("Stacktrace:");
            e.printStackTrace();
            System.out.println("Stacktrace done");
            return null;
        }
    }

    static int gensymCounter = 1;

    public String gensym()
    {
        return "sym" + Integer.toString(gensymCounter++);
    }

    public static String write(Object o)
    {
        if(o == null)
            return "()";
        else if(o == Boolean.TRUE)
            return "#t";
        else if(o == Boolean.FALSE)
            return "#f";
        else if(o instanceof Long)
            return o.toString() + "l0";
        else if(o instanceof Double)
            return o.toString() + "d0";
        else if(o instanceof Character)
        {
            char c = ((Character) o).charValue();
            if(c == '\n')
                return "#\\newline";
            else if(c == ' ')
                return "#\\space";
            return "#\\" + c;
        }
        else if(o instanceof Character[])
        {
            StringBuffer sb = new StringBuffer("\"");
            Character[] s = (Character[]) o;
            for(int i = 0; i < s.length; i++)
                sb.append(s[i].charValue());
            sb.append("\"");
            return sb.toString();
        }
        else if(o instanceof Object[])
        {
            Object[] v= (Object[]) o;
            StringBuffer sb = new StringBuffer("#(");
            for(int i = 0; i < v.length; i++)
            {
                if(i > 0)
                    sb.append(" ");
                sb.append(write(v[i]));
            }
            sb.append(")");
            return sb.toString();
        }
        else if(o instanceof String)
        {
            String s = (String) o;
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < s.length(); i++)
            {
                char c = s.charAt(i);
                if(c == '\\' || c == '"')
                    sb.append("\\" + c);
                else
                    sb.append(c);
            }
            return "\"" + sb.toString() + "\"";
        }
        else
            return o.toString();

    }

    public static String display(Object o)
    {
        if(o == null)
            return "()";
        else if(o == Boolean.TRUE)
            return "#t";
        else if(o == Boolean.FALSE)
            return "#f";
        else if(o instanceof Character[])
        {
            StringBuffer sb = new StringBuffer();
            Character[] s = (Character[]) o;
            for(int i = 0; i < s.length; i++)
                sb.append(s[i].charValue());
            return sb.toString();
        }
        else if(o instanceof Object[])
        {
            Object[] v= (Object[]) o;
            StringBuffer sb = new StringBuffer("#(");
            for(int i = 0; i < v.length; i++)
            {
                if(i > 0)
                    sb.append(" ");
                sb.append(write(v[i]));
            }
            sb.append(")");
            return sb.toString();
        }
        else
            return o.toString();

    }
}


