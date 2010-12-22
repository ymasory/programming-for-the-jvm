package COM.sootNsmoke.scheme;

import java.util.*;

/** A binding environment maps symbols to values.  This is also where we are going to
 * keep the mapping of strings to symbols, which the reader uses.
 *
 * Also maintains a pointer to the previous binding environment.  If you evaluate a
 * symbol, we check up the stack if it isn't found here.
 */
public class BindingEnv
{
    /** Points to the previous binding environment on the stack */
    private BindingEnv previousBindingEnv = null;

    /** Maps symbols to values */
    private Hashtable symVal = new Hashtable();

    /** A special identifier to serve as null in the hashtable, because
     * java Hashtables don't permit nulls.  We use it only in the hashtables,
     * and null everywhere else.  Its value doesn't mean anything, except that
     * it's not null.
     */
    private final static Object theNull = new Integer(0);

    /** Create a new binding environment */
    BindingEnv(BindingEnv env)
    {
        previousBindingEnv = env;
    }

    public BindingEnv()
    {
        this(null);
    }

    /** Evaluate the symbol in this environment.  If it isn't bound here, check
     * up the stack.  If it isn't bound anywhere, throw an UnboundException.
     */
    public Object eval(Symbol sym) throws UnboundException
    {
        Object val = symVal.get(sym);
        if(val == null)
            if(previousBindingEnv == null)
                throw new UnboundException(sym);
            else
               val = previousBindingEnv.eval(sym);
        else if(val == theNull)
            val = null;
        return val;
    }

    public Object lookup(String name) throws UnboundException
    {
        Symbol sym = Naming.name(name);
        return eval(sym);
    }

    /** Finds the slot named by name, either in this environment
     * or in the previous environment, and rebinds it to
     * the value.
     */
    public void set(Symbol sym, Object value) throws UnboundException
    {
        BindingEnv env = this;
        while(true)
        {
            if(env.symVal.containsKey(sym))
            {
                env.symVal.put(sym, value);
                return;
            }
            env = env.previousBindingEnv;
            if(env == null)
                throw new UnboundException(sym);
        }
    }

    public void set(String name, Object value) throws UnboundException
    {
        set(Naming.name(name), value);
    }

    /**
     * Binds a symbol to a new value.
     */
    public void bind(Symbol sym, Object val)
    {
        if(val == null)
            val = theNull;
        symVal.put(sym, val);
    }

    /**
     * Convenience: takes a symbol name, finds the symbol, and binds it
     * to val
     */
    public void bind(String name, Object val)
    {
        Symbol sym = Naming.name(name);
        bind(sym, val);
    }

    /** Push a new binding environment.  That means creating a new environment
     * which points to this one and returning it.
     */
    public BindingEnv push()
    {
        return new BindingEnv(this);
    }

    /** Pop a binding environment.  That means returning my pointer to the
     * previous environment.
     */
    public BindingEnv pop()
    {
        return previousBindingEnv;
    }

    /** Conses together everything on the stack after n */
    public static Cons makeVarArgs(Object[] vals, int n)
    {
        if(n >= vals.length)
            return null;
        else
        {
            Cons c = null;
            for(int i = vals.length-1; i >= n; i--)
                c = new Cons(vals[i], c);

            return c;
        }
    }
}