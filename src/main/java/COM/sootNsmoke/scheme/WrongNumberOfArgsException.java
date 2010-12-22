package COM.sootNsmoke.scheme;

public class WrongNumberOfArgsException extends SchemeException
{
    public WrongNumberOfArgsException(int n)
    {
        super("Wrong number of argments: " + n);
    }
}
