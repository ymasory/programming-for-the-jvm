package COM.sootNsmoke.scheme;
import java.io.*;

class EndOfStreamException extends Exception
{
}

class SchemeReader
{
    BindingEnv env;
    PushbackInputStream is;
    String pushback_token = null;

    public static final Object eof =
        new Symbol("You should never see this");


    SchemeReader(BindingEnv env, InputStream is)
    {
        this.env = env;
        this.is = new PushbackInputStream(is);
    }

    boolean isWhiteSpace(int b)
    {
        return b == ' ' || b == '\n' || b == '\t';
    }

    boolean isEof(int b)
    {
        return b == -1;
    }


    boolean isDelimeter(int n)
    {
        return isWhiteSpace(n) || isEof(n) ||
               n == ')' || n == '(' || n == '"' || n == ';';
    }

    boolean isDecimalDigit(int n)
    {
        switch(n)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
            default:
                return false;
        }
    }

    String token() throws IOException, EndOfStreamException
    {
        if(pushback_token != null)
        {
            String token = pushback_token;
            pushback_token = null;
            return token;
        }
        while(true)
        {
            int b = is.read();
            switch(b)
            {
                case -1:
                    throw new EndOfStreamException();
                case '(':  return "(";
                case ')':  return ")";
                case '\'':  return "'";
                case '`':  return "`";
                case '.':  return ".";
                case '"':  return "\"";
                case ',':
                    b = is.read();
                    if(b == '@')
                        return ",@";
                    is.unread(b);
                    return ",";
                case ' ':
                case '\n':
                case '\t':
                    break;
                case ';':
                    while(true)
                    {
                        b = is.read();
                        if(b == '\n' || b == -1)
                            break;
                    }
                case '#':
                    b = is.read();
                    if(isEof(b))
                        return "#";
                    else
                        return "#" + (char) b;
                default:
                    String s = new String();
                    s += (char) b;
                    while(true)
                    {
                        b = is.read();
                        if(isDelimeter(b))
                        {
                            is.unread(b);
                            return s;
                        }
                        s += (char) b;
                    }
            }
        }
    }


    Object readExpr() throws SchemeException, IOException
    {
        while(true)
        {
            try
            {
                String b = token();

                if(b.equals("("))
                    return list();
                else if(b.equals("#t"))
                    return Boolean.TRUE;
                else if(b.equals("#f"))
                    return Boolean.FALSE;
                else if(b.equals("+"))
                    return Naming.name("+");
                else if(b.equals("-"))
                    return Naming.name("-");
                else if(b.equals("..."))
                    return Naming.name("...");
                else if(b.equals("'"))
                    return quoted_symbol();
                else if(b.equals("\""))
                    return string();
                else if(b.startsWith("#\\"))
                    return character();
                else if(isDecimalDigit(b.charAt(0)) ||
                        b.startsWith("+") ||
                        b.startsWith("-") ||
                        b.startsWith("#"))
                    return number(b);
                else
                    return Naming.name(b);
            }
            catch(EndOfStreamException e)
            {
                return eof;
            }
        }
    }

    Object number(String num)
    {
        return new Integer(Integer.parseInt(num));
    }


    Object quoted_symbol() throws IOException
    {
        return new Cons(Naming.name("quote"),
                        new Cons(readExpr(),
                                 null));
    }

     Object list() throws IOException, EndOfStreamException
    {
        String b = token();
        if(b.equals(")"))
                return null;
        else if(b.equals("."))
        {
            Object val = readExpr();
            if(!token().equals(")"))
                throw new ReaderError("Only 1 token permitted after dot");
            return val;
        }
        else
        {
            pushback_token = b;
            Object car = readExpr();
            Object cdr = list();
            if(car == eof || cdr == eof)
                return eof;
            return new Cons(car, cdr);
        }
    }

    Object string() throws IOException
    {
        String s = new String();
        while(true)
        {
            int b = is.read();
            if(b == -1)
                return eof;
            else if(b == '"')
                return s;
            else if(b == '\\')
            {
                b = is.read();
                if(b == -1)
                    return eof;
                else
                    s += (char) b;
            }
            else
                s += (char) b;
        }
    }

    Object character() throws IOException
    {
        while(true)
        {
            String s = new String();
            int b = is.read();
            while(!isDelimeter(b))
            {
                s += (char) b;
                b = is.read();
            }
            is.unread(b);
            if(s.equals("space"))
                return new Character(' ');
            else if(s.equals("newline"))
                return new Character('\n');
            else
                if(s.length() > 1)
                    throw new ReaderError("Invalid character " + (char) b);
                else
                    return new Character(s.charAt(0));
        }
    }

}
