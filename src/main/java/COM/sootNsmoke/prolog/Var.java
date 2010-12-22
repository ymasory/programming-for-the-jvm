package COM.sootNsmoke.prolog;
public class Var
{
    String name;
    private boolean bound = false;
    private Object binding;
    static int n = 0;

    /** Create an anonymous variable */
    public Var() { name = "_" + new Integer(n++); }

    /** Create a variable with this name */
    public Var(String name){ this.name = name; }

    /** Is it bound? */
    public boolean bound() { return bound; }

    /** If it's bound, what is it bound to? */
    public Object binding() { return binding; }

    /** Set the current binding */
    public void setBinding(Object o)
    {
        binding = o;
        bound = true;
    }

    /** Clear the current binding */
    public void makeUnbound() { bound = false; }

    public boolean equals(Object o)
    {
        if(o instanceof Var && ((Var) o).name.equals(name))
            return true;
        return false;
    }

    public String toString() { return name; }

    public int hashCode() { return name.hashCode(); }
}