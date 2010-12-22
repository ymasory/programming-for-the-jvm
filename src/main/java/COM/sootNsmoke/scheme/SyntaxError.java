package COM.sootNsmoke.scheme;

public class SyntaxError extends SchemeException
{
    public SyntaxError(String s)
    {
        super("Syntax error: " + s);
    }

}