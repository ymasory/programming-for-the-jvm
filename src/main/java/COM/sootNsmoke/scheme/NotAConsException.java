package COM.sootNsmoke.scheme;

public class NotAConsException extends SchemeException
{
    NotAConsException(Object exp)
    {
        super("Not a cons: " + exp);
    }
}