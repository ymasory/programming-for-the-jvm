package COM.sootNsmoke.prolog;

public class Structure
{
    public Object functor;
    public Object arg[];

    public Structure(Object functor, Object[] arg)
    {
        this.functor = functor;
        this.arg = arg;
    }

    public Structure(Object functor, Object arg1)
    {
        this.functor = functor;
        arg = new Object[2];
        arg[0] = arg1;
    }


    public Structure(Object functor, Object arg1, Object arg2)
    {
        this.functor = functor;
        arg = new Object[2];
        arg[0] = arg1;
        arg[1] = arg2;
    }

    /** Return the structure as a string, dereferencing the
     * variables
     */
    public String toString(Prolog p)
    {
        if(functor.equals(":-"))
            return p.toString(arg[0]) + " :- " + p.toString(arg[1]);

        String s = p.toString(functor);
        if(arg.length == 0)
            return s;
        s += "(";
        for(int i = 0; i < arg.length; i++)
        {
            if(i > 0)
                s += ", ";
            s += p.toString(arg[i]);
        }
        s += ")";
        return s;

    }
}