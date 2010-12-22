package COM.sootNsmoke.scheme;

/** A compiled procedure */
public abstract class CompiledProcedure
{
    public BindingEnv env;

    public CompiledProcedure()
    {
    }

    public CompiledProcedure(BindingEnv env)
    {
        this.env = env;
    }


    public Object apply0() throws SchemeException
    {
        return applyN(null);
    }
    public Object apply1(Object arg1)
        throws SchemeException
    {
        return applyN(new Cons(arg1, null));
    }
    public Object apply2(Object arg1, Object arg2)
        throws SchemeException
    {
        return applyN(new Cons(arg1, new Cons(arg2, null)));
    }
    public Object apply3(Object arg1, Object arg2, Object arg3)
        throws SchemeException
    {
        return applyN(new Cons(arg1, new Cons(arg2,
                      new Cons(arg3, null))));
    }
    public Object applyN(Cons arglist)
        throws SchemeException
    {
        throw new WrongNumberOfArgsException(0);
    }


    public String toString()
    {
        return ("<compiled procedure " + super.toString() + ">");
    }
}
