package COM.sootNsmoke.scheme;

import java.util.*;

/** A class to map strings to symbols.  */
public class Naming
{
    /** Maps the string to the symbol.  This swizzling is necessary
     * because we want two symbols with the same name to be identical
     * objects, so we can compare them very quickly.
     */
    private static Hashtable stringToSym = new Hashtable();

    /** Looks up the name and returns the symbol associated with it.
     * This may mean creating a new symbol.
     */
    public static Symbol name(String name)
    {
        Symbol sym = (Symbol) stringToSym.get(name);
        if(sym == null)
        {
            sym = new Symbol(name);
            stringToSym.put(name, sym);
        }
        return sym;
    }
}