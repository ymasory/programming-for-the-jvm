package COM.sootNsmoke.jvm;

public class UndefinedLabelException extends RuntimeException
{
    String label;

    public UndefinedLabelException(String str)
    {
        label = str;
    }

    public String getMessage()
    {
        return "Could not resolve label " + label;
    }
}
