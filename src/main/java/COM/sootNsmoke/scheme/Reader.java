package COM.sootNsmoke.scheme;
import java.io.*;
import java.util.*;

class ReaderError extends SchemeException {
    public ReaderError(String s) {
        super(s);
    }
}

class SchemeTokenizer extends StreamTokenizer {
    public SchemeTokenizer(InputStream s) {
        super(s);
        commentChar(';');
        ordinaryChars('0', '9');
        ordinaryChar('-');
        ordinaryChar('.');
        wordChars('a','z');
        wordChars('A','Z');
        wordChars('0','9');
        wordChars('+', '+');
        wordChars('-', '-');
        wordChars('*', '*');
        wordChars('/', '/');
        wordChars('_', '_');
        wordChars('$', '&'); // Also gets %
        wordChars('<', '>'); // Also gets =
        wordChars('~', '~');
        wordChars('^', '^');
        wordChars('!', '!');
        wordChars('?', '?');
        wordChars('#', '#');
        wordChars(':', ':');
        wordChars('.', '.');
        ordinaryChar('\'');
        wordChars('\\', '\\');
    }
}

/** Implements a reader for the scheme package.  Instantiate this
 * class with the input stream you want to read.  Then each call to
 * readExpr will return the next Scheme expression from the input
 * stream. */
public class Reader {
    SchemeTokenizer lex;

    public static final Object eof =
        new Symbol("You should never see this");

	Reader(InputStream is)
    {
        lex = new SchemeTokenizer(is);
    }

    /** Returns the next scheme form from the input stream.  It returns:
     * <PRE>
     *     #t         -> Boolean.TRUE
     *     #f         -> Boolean.FALSE
     *     #\\space   -> ' '  (as a Character)
     *     #\\newline -> '\n' (as a Character)
     *     #\\c       -> c    (as a Character)
     *     #(...)     -> a Vector
     *     number     -> a Number
     *     'x         -> Cons("quote", x)
     *     `x         -> Cons("quasiquote", x)
     *     ,@x        -> Cons("unquote-splicing", x)
     *     ,x         -> Cons("unquote", x)
     *     "..."      -> a String
     *     ...        -> a Symbol
     * </PRE>
     */
    public Object readExpr() throws SchemeException, IOException
    {
        switch (lex.nextToken()) {
            case StreamTokenizer.TT_WORD:
            {
                String s = lex.sval.toLowerCase();
                if(s.equals("#t"))
                    return Boolean.TRUE;
                if(s.equals("#f"))
                    return Boolean.FALSE;
                if(s.equals("#\\space"))
                    return new Character(' ');
                if(s.equals("#\\newline"))
                    return new Character('\n');
                if(s.startsWith("#\\"))
                {
                    if(lex.sval.length() == 3)
                        return new Character(lex.sval.charAt(2));
                    else
                        throw new SyntaxError("Invalid character " + lex.sval);
                }
                if(s.equals("#"))
                    return readVector();
                if(isValidSymbol(s))
                    return Naming.name(s);
                Object num = parseNumber(s);
                if(num != null)
                    return num;
                throw new SchemeException("Unrecognized token : "
                    + lex.sval);
            }
            case StreamTokenizer.TT_NUMBER:
                return new Integer((int) lex.nval);
            case '\'':
                return new Cons(Naming.name("quote"),
                       new Cons(readExpr(), null));
            case '`':
                return new Cons(Naming.name("quasiquote"),
                       new Cons(readExpr(), null));
            case ',':
                if(lex.nextToken() == '@')
                    return new Cons(Naming.name("unquote-splicing"),
                           new Cons(readExpr(), null));

                else
                {
                    lex.pushBack();
                    return new Cons(Naming.name("unquote"),
                           new Cons(readExpr(), null));
                }
            case '"':
                return lex.sval;
            case '(':
                return readList();
            case StreamTokenizer.TT_EOF:
                return eof;
            default:
            {
                char[] chars = { (char) lex.ttype };
                return Naming.name(new String(chars));
            }
        }
    }

    /** A vector is #(item item item...). They are returned
     * as an array of Objects
     */
    Object[] readVector() throws SchemeException, IOException
    {
        int paren = lex.nextToken();
        if(paren != '(')
            throw new SchemeException("Invalid character after #");
        return readVec();
    }

    Object[] readVec() throws SchemeException, IOException
    {
        Vector v = new Vector();
        while(true)
        {
            int t = lex.nextToken();
            if(t == ')')
            {
                Object[] l = new Object[v.size()];
                v.copyInto(l);
                return l;
            }
            lex.pushBack();
            Object o = readExpr();
            v.addElement(o);
        }
    }


    Object readList() throws SchemeException, IOException
    {
        int t = lex.nextToken();
        if(t == ')')
            return null;
        if(t == StreamTokenizer.TT_WORD && lex.sval.equals("."))
        {
            Object val = readExpr();
            if (lex.nextToken() != ')')
                throw new ReaderError("No right paren after dot");
            return val;
        }
        lex.pushBack();
        Object car = readExpr();
        Object cdr = readList();
        return new Cons(car, cdr);
    }

    /** Recognizes valid Scheme symbols.  Syntax:
     * identifer -> <initial> <subsequent>*
     *            | <peculiar identifier>
     * <initial> -> a | b | ... | z |
     *              ! | $ | % | & | * | / | : | < | =
     *            | > | ? | ~ | _ | ^
     * <subsequent> -> <initial> | 0 | 1 | ... | 9 |
     *                 . | + | -
     * <peculiar identifier> -> + | - |  ...
     */
    public boolean isValidSymbol(String s)
    {
        if(s.equals("+"))
            return true;
        if(s.equals("-"))
            return true;
        if(s.equals("..."))
            return true;
        char c = s.charAt(0);
        if(!isInitial(c))
            return false;
        for(int i = 1; i < s.length(); i++)
            if(!isSubsequent(s.charAt(i)))
                return false;
        return true;
    }

    public boolean isInitial(char c)
    {
        return
            c == '!' || c == '$' || c == '%' || c == '&' ||
            c == '*' ||
            c == '/' || c == ':' || c == '<' || c == '=' ||
            c == '>' || c == '?' || c == '~' || c == '_' ||
            c == '^' || Character.isLetter(c);
    }

    public boolean isSubsequent(char c)
    {
        return
            (c >= '0' && c <= '9') ||
            c == '+' || c == '-' || c == '.' ||
            isInitial(c);
    }

    /** Scheme has a unbelievably complex syntax for
     * numbers.
     * If I can parse the number, then I return some
     * subclass of Number (including my own Rational or
     * Complex.)
     * If not, I return null.
     */
    Object parseNumber(String num)
    {
        int radix = 10;
        boolean exact = true;

        boolean prefix = true;

        while(prefix)
        {
            if(num.startsWith("#i"))
                exact = false;
            else if(num.startsWith("#e"))
                exact = true;
            else if(num.startsWith("#b"))
                radix = 2;
            else if(num.startsWith("#o"))
                radix = 8;
            else if(num.startsWith("#d"))
                radix = 10;
            else if(num.startsWith("#x"))
                radix = 16;
            else
                prefix = false;
            if(prefix)
                num = num.substring(2);
        }
        if(num.equals("+i"))
            return new Complex(new Integer(0), new Integer(1));
        if(num.equals("-i"))
            return new Complex(new Integer(0), new Integer(-1));

        String real_part = num;
        String im_part = null;
        int at = num.indexOf('@');
        if(at != -1)
        {
            real_part = num.substring(0, at);
            im_part = num.substring(at);
        }
        else if(num.endsWith("i"))
        {
            // If it ends with a 'i', everything back to the
            // +, - is part of the imaginary part.
            // If the + or - is part of an exponent (e+, d-, etc).
            // then keep looking.
            for(int i = num.length() - 2; i >= 0; i--)
            {
                if(num.charAt(i) == '-' ||
                   num.charAt(i) == '+')
                {
                    if(i > 0)
                    {
                        char q = num.charAt(i-1);
                        if(q == 'e' || q == 'd' || q == 'f' ||
                           q == 'l' || q == 's')
                            continue;
                    }
                    real_part = num.substring(0, i);
                    im_part = num.substring(i, num.length()-1);
                    break;
                }
            }
        }

        Number real = new Integer(0);
        if(real_part.length() > 0)
            real = real(real_part, radix, exact);
        if(real == null)
            return null;
        if(im_part != null)
        {
            Number imaginary = real(im_part, radix, exact);
            return new Complex(real, imaginary);
        }
        return real;
    }

    /** Parses a real number.
     * Returns a Float, Integer, Long, Double, or Rational
     */
    Number real(String num, int radix, boolean exact)
    {
        // Extricate sign
        int sign = 1;
        if(num.startsWith("+"))
        {
            sign = 1;
            num = num.substring(1);
        }
        else if(num.startsWith("-"))
        {
            sign = -1;
            num = num.substring(1);
        }

        if(num.length() == 0)
            return null;

        long l = 0;
        int i = 0;
        if(num.length() == 0)
            return null;
        for(i = 0; i < num.length(); i++)
        {
            char c = num.charAt(i);
            int d = digit(c);
            if(d == -1 || d >= radix)
                break;
            else
                l = l * radix + d;
        }
        num = num.substring(i);

        long numerator = 0;
        long denominator = 1;
        if(num.startsWith(".") || num.startsWith("/"))
        {
            boolean rational = num.startsWith("/");
            if(num.length() == 1)
                return null;
            if(!rational)
                exact = false;
            else
            {
                numerator = l;
                l = 0;
                denominator = 0;
            }

            for(i = 1; i < num.length(); i++)
            {
                int d = digit(num.charAt(i));
                if(d == -1 || d >= radix)
                    break;
                if(rational)
                    denominator = denominator * radix + d;
                else
                {
                    numerator = numerator * radix + d;
                    denominator *= radix;
                }
            }
            num = num.substring(i);
        }

        Number prec = new Integer(0);
        int exponent = 0;

        if(num.startsWith("s"))
            prec = new Integer(0);
        else if(num.startsWith("f") || num.startsWith("e"))
        {
            exact = false;
            prec = new Float(0);
        }
        else if(num.startsWith("d"))
        {
            exact = false;
            prec = new Double(0);
        }
        else if(num.startsWith("l"))
            prec = new Long(0);

        if(num.startsWith("e") ||
           num.startsWith("f") ||
           num.startsWith("d") ||
           num.startsWith("s") ||
           num.startsWith("l"))
        {
            num = num.substring(1);
            int exp_sign = 1;
            if(num.startsWith("-"))
            {
                exp_sign = -1;
                num = num.substring(1);
            }
            else if(num.startsWith("+"))
            {
                exp_sign = 1;
                num = num.substring(1);
            }
            if(num.length() == 0)
                return null;
            for(i = 0; i < num.length(); i++)
            {
                int d = digit(num.charAt(i));
                // Spec says that exponent must be base 10
                if(d == -1 || d >= 10)
                    break;
                exponent = exponent*10 + d;
            }
            num = num.substring(i);
            exponent *= exp_sign;
        }
        Number real = null;
        // Now I have a complete real number
        l = l * sign;
        if(exact)
        {
            if(denominator == 1)
            {
                if((int) l == l)
                    real = new Integer((int) l);
                else
                    real = new Long(l);
            }
            else
                real = new Rational(sign*numerator, denominator);
        }
        else
        {
            double d = ((double) l +
                    ((double) numerator / denominator)*sign)
                    * Math.pow(radix, exponent);

            if(prec instanceof Double)
                real = new Double(d);
            else
                real = new Float(d);
        }

        if(num.length() == 0)
            return real;
        else
            return null;
    }


    int digit(char c)
    {
        if(c >= '0' && c <= '9')
            return c - '0';
        else if(c >= 'a' && c <= 'f')
            return c - 'a' + 10;
        else if(c == '#')
            return 0;
        return -1;
    }
}
