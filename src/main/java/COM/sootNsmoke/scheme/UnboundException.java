package COM.sootNsmoke.scheme;

public class UnboundException extends SchemeException
{
    public UnboundException(Symbol sym)
    {
        super("Unbound symbol: " + sym);
    }
}
