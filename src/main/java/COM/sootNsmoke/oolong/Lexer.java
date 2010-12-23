package COM.sootNsmoke.oolong;
import java.io.*;

/** A tokenizer.  Divides everything into words.  Doesn't
 * distinguish between words and numbers
 *
 * Note to anybody looking at this: I apologize for not writing
 * this as a subclass of StreamTokenizer. The Oolong lex syntax
 * is a legacy of the Jasmin language, which uses semicolons as
 * comments instead of Java comments.  Semicolons are also used
 * in separators.  Apparently JAVA_CUP could handle this, but
 * StreamTokenzier can't, and I finally got fed up and wrote
 * my own.  Also, I wanted Java comments for commenting stuff out
 * of Oolong code, and that turned out to be too much.
 *
 * Also: this code is designed to be compatible with JDK 1.0,
 * so there are some depracated nasty things
 */

/* Slight modification by Pat Terry (p.terry@ru.ac.za) - marked as "pdt"
   to allow for \t and \n to be correctly processed in strings
 */

class Lexer // extends StreamTokenizer
{
    boolean pushedBack = false;
    boolean eof = false;
    static final int TT_EOF = StreamTokenizer.TT_EOF;
    static final int TT_WORD = StreamTokenizer.TT_WORD;
    int ttype;
    int lineno = 1;

    void pushBack()
    {
        pushedBack = true;
    }

    int lineno()
    {
        return lineno;
    }
    /*
    Lexer(InputStream is)
    {
        super(is);
        resetSyntax();
    	slashStarComments(true);
    	slashSlashComments(true);
        wordChars('_', '_');
        wordChars('+', '+');
        wordChars('-', '-');
        wordChars('>', '>');
        wordChars('<', '<');
        wordChars('.', '.');
        wordChars('$', '$');
        wordChars('/', '/');
        wordChars('[', '[');
        wordChars(';', ';');
    	wordChars('a', 'z');
    	wordChars('A', 'Z');
    	wordChars('0', '9');
    	wordChars(128 + 32, 255);
    	whitespaceChars(0, ' ');
    	commentChar(';');
    	quoteChar('"');
    	quoteChar('\'');
    }
    */
    InputStream is;
    String sval;
    Lexer(InputStream is)
    {
        this.is = is;
    }

    int pushedbackChar = -1;

    char nextChar() throws IOException
    {
        int c;
        if(pushedbackChar == -1)
        {
            c = is.read();
            if(c == -1)
                throw new IOException("Done");
        }
        else
        {
            c = pushedbackChar;
            pushedbackChar = -1;
        }
        return (char) c;
    }

    void pushbackChar(char c)
    {
        pushedbackChar = c;
    }

    public int nextToken() throws IOException
    {
        StringBuffer sb = null;
        try {
            if(pushedBack)
                pushedBack = false;

            else
            {
                ttype = TT_EOF;
                char c;
                // Loop over white space
                while(true)
                {
                    c = nextChar();
                    if(c == '\n')
                        lineno++;
                    if(Character.isSpace(c))
                        ;
                    else if(c == ';')
                    {
                        // Read ' comment
                        while((c = nextChar()) != '\n')
                            ;
                        lineno++;
                    }
                    else if(c == '/')
                    {
                        c = nextChar();
                        if(c == '*')
                        {
                            // Read /**/ comment
                            while(true)
                            {
                                while((c = nextChar()) != '*')
                                    if(c == '\n')
                                        lineno++;
                                c = nextChar();
                                if(c == '/')
                                    break;
                            }
                        }
                        else if(c == '/')
                        {
                            // Read // comment
                            while((c = nextChar()) != '\n')
                                ;
                            lineno++;
                        }
                        else
                            break;
                    }
                    else
                    {
                        break;
                    }
                }
                if(c == '"' || c == '\'')
                {
                    ttype = c;
                    sb = new StringBuffer();
                    while((c = nextChar()) != ttype)
                    {
                        if(c == '\\')
                        {
                            c = nextChar();
                            if(c == 'n')
                                sb.append('\n');
                            else if(c == 't')       // pdt
                                sb.append('\t');
                            else
                                sb.append(c);

                        }
                        else
                            sb.append(c);
                    }
                    sval = sb.toString();
                }
                else if(!(Character.isJavaLetterOrDigit(c) ||
                   c == '<' || c == '.' || c == '-' || c == '+'
                    || c == '[' || c == '$'))
                {
                    ttype = c;
                }
                else
                {
                    ttype = TT_WORD;
                    sb = new StringBuffer();
                    sb.append((char) c);
                    while(true)
                    {
                        c = nextChar();
                        if(Character.isJavaLetterOrDigit(c) ||
                           c == '<' || c == '>' || c == '/' ||
                           c == ';' || c == '.' ||
                           c == '-' || c == '+' || c == '[' ||
                           c == '$')
                            sb.append((char) c);
                        else
                        {
                            sval = sb.toString();
                            pushbackChar(c);
                            break;
                        }
                    }
                }
            }
            return ttype;
        }
        catch(IOException e)
        {
            if(e.getMessage().equals("Done"))
            {
                if(ttype == TT_EOF)
                    eof = true;
                if(sb != null)
                    sval = sb.toString();
                return ttype;
            }
            else
                throw e;
        }
    }


    /** like nextToken, except it throws a SyntaxError if the next token isn't a WORD */
    String nextWord(Oolong oolong) throws SyntaxError, IOException
    {
        int t = nextToken();
        if(t == '\'')
        {
            if(sval.length() != 1)
                throw new SyntaxError(oolong, "Invalid character literal: '" + sval + "'");
            ttype = TT_WORD;
            sval = "" + (int) sval.charAt(0);
            return sval;
        }
        if(t == '"')
            return sval;
        if(t != TT_WORD)
        {
            String msg = "Unexpected token";
            if(sval != null)
                sval += ": " + sval;
            throw new SyntaxError(oolong, msg);
        }
        else
            return sval;
    }

    static boolean isInt(String s)
    {
        try {
            if(s.startsWith("0x") || s.startsWith("0X"))
                return true;
            if(s.endsWith("l") || s.endsWith("L"))
                return false;
            long l = Long.parseLong(s);
            int i = (int) l;
            if(i == l)
                return true;
        }
        catch(Exception e) {}
        return false;
    }

    static boolean isLong(String s)
    {
        String val = s;
        if(val.endsWith("l") || val.endsWith("L"))
            val = s.substring(0, s.length()-1);
        try
        {
            Long.parseLong(val);
            return true;
        }
        catch(Exception e) {}
        return false;
    }

    static boolean isDouble(String s)
    {
        try
        {
            parseDouble(s);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

    static boolean isFloat(String s)
    {
        String val = s;
        try {
            if(val.endsWith("D") || val.endsWith("d"))
                return false;
            double d = Double.valueOf(val).doubleValue();
            if(d == (float) d)
                return true;
        }
        catch(Exception e) {}
        return false;
    }

    public static long parseLong(String sval) throws NumberFormatException
    {
        String val = sval;
        long sign = 1;
        if(val.startsWith("+"))
            val = val.substring(1);
        else if(sval.startsWith("-"))
        {
            val = val.substring(1);
            sign = -1;
        }
        int base = 10;
        if(val.startsWith("0x") || val.startsWith("0X"))
        {
            base = 16;
            val = val.substring(2);
        }
        else if(val.startsWith("0"))
        {
            base = 8;
            val = val.substring(1);
        }

        if(val.endsWith("L") || val.endsWith("l"))
            val = val.substring(0, val.length()-1);

        if(val.length() == 0)
            return 0;

        return sign * Long.parseLong(val, base);
    }

    double getDouble(Oolong oolong) throws SyntaxError, IOException
    {
        try {
            return parseDouble(sval);
        }
        catch(NumberFormatException e) {
            throw new SyntaxError(oolong, "Badly formatted number: " + sval);
        }
    }

    public static double parseDouble(String sval) throws NumberFormatException
    {
        String val = sval;
        if(val.endsWith("D") || val.endsWith("d"))
            val = val.substring(0, val.length()-1);
        double dval = Double.valueOf(val).doubleValue();
        return dval;
    }
}
