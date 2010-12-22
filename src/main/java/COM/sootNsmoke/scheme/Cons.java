package COM.sootNsmoke.scheme;

/** A Cons cell */
public class Cons
{
    private Object car;
    private Object cdr;

    public Cons(Object car, Object cdr)
    {
        this.car = car;
        this.cdr = cdr;
    }

    public String toString()
    {
        return write();
    }

    protected String writeNoParens()
    {
        if(SchemeSyntax.isForm(this, "quote"))
            return "'" + ReadEvalPrint.write(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "unquote"))
            return "," + ReadEvalPrint.write(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "quasiquote"))
            return "`" + ReadEvalPrint.write(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "unquote-splicing"))
            return ",@" + ReadEvalPrint.write(SchemeSyntax.textOfQuotation(this));
        if(cdr instanceof Cons)
            return ReadEvalPrint.write(car) + " " +
                   ((Cons) cdr).writeNoParens();
        else if(cdr == null)
            return ReadEvalPrint.write(car);
        else
            return ReadEvalPrint.write(car) + " . " +
                ReadEvalPrint.write(cdr);
    }

    public String write()
    {
        if(SchemeSyntax.isForm(this, "quote"))
            return "'" + ReadEvalPrint.write(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "unquote"))
            return "," + ReadEvalPrint.write(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "quasiquote"))
            return "`" + ReadEvalPrint.write(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "unquote-splicing"))
            return ",@" + ReadEvalPrint.write(SchemeSyntax.textOfQuotation(this));
        return "(" + writeNoParens() + ")";
    }

    protected String displayNoParens()
    {
        if(SchemeSyntax.isForm(this, "quote"))
            return "'" + ReadEvalPrint.display(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "unquote"))
            return "," + ReadEvalPrint.display(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "quasiquote"))
            return "`" + ReadEvalPrint.display(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "unquote-splicing"))
            return ",@" + ReadEvalPrint.display(SchemeSyntax.textOfQuotation(this));
        if(cdr instanceof Cons)
            return car + " " + ((Cons) cdr).displayNoParens();
        else if(cdr == null)
            return ReadEvalPrint.display(car);
        else
            return ReadEvalPrint.display(car) + " . " +
                ReadEvalPrint.display(cdr);
    }

    public String display()
    {
        if(SchemeSyntax.isForm(this, "quote"))
            return "'" + ReadEvalPrint.display(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "unquote"))
            return "," + ReadEvalPrint.display(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "quasiquote"))
            return "`" + ReadEvalPrint.display(SchemeSyntax.textOfQuotation(this));
        if(SchemeSyntax.isForm(this, "unquote-splicing"))
            return ",@" + ReadEvalPrint.display(SchemeSyntax.textOfQuotation(this));
        return "(" + displayNoParens() + ")";
    }

    static Cons toCons(Object o) throws NotAConsException
    {
        if(o instanceof Cons)
            return (Cons) o;
        else
            throw new NotAConsException(o);
    }

    static Object car(Object o) throws NotAConsException
    {
        if(o instanceof Cons)
            return ((Cons) o).car;
        else
            throw new NotAConsException(o);
    }

    static Object cdr(Object o) throws NotAConsException
    {
        if(o instanceof Cons)
            return ((Cons) o).cdr;
        else
            throw new NotAConsException(o);
    }

    static Object caadr(Object o) throws NotAConsException
    {
        return car(car(cdr(o)));
    }

    static Object caddr(Object o) throws NotAConsException
    {
        return car(cdr(cdr(o)));
    }
    static Object cdddr(Object o) throws NotAConsException
    {
        return cdr(cdr(cdr(o)));
    }

    static Object cadr(Object o) throws NotAConsException
    {
        return car(cdr(o));
    }

    static Object cddr(Object o) throws NotAConsException
    {
        return cdr(cdr(o));
    }

    static Object cdadr(Object o) throws NotAConsException
    {
        return cdr(car(cdr(o)));
    }


    Cons set_car(Object car)
    {
        this.car = car;
        return this;
    }

    Cons set_cdr(Object cdr)
    {
        this.cdr = cdr;
        return this;
    }

    public Object car()
    {
        return car;
    }

    public Object cdr()
    {
        return cdr;
    }
}
