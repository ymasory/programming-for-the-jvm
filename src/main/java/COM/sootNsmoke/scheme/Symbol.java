package COM.sootNsmoke.scheme;

/**
 * This class represents symbols.  Symbols don't do much; they exist mostly to
 * be different from all other symbols with different names.
 */
class Symbol
{
    private String name;

    Symbol(String name)
    {
        this.name = name;
    }

    String getName()
    {
        return name;
    }

    public String toString()
    {
        return name;
    }
}
