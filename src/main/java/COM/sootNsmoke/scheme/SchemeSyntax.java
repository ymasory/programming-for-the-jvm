package COM.sootNsmoke.scheme;

/** Keeps a couple of dozen boring tiny functions which parse
 * Scheme structures
 */
public class SchemeSyntax
{
    /** Returns true if o is a special form beginning with
     * name (i.e. of the form (name ...))
     */
    public static boolean isForm(Object exp, String name)
    {
            if(isAtom(exp))
            return false;
        else
            return ((Cons) exp).car() == Naming.name(name);
    }

    public static boolean isNull(Object exp)
    {
        return exp == null;
    }

    public static boolean isAtom(Object o)
    {
        return !(o instanceof Cons);
    }

    public static boolean isSelfEvaluating(Object exp)
    {
        return exp instanceof String  ||
               exp instanceof Number  ||
               exp instanceof Character ||
               exp instanceof Boolean ||
               exp == null;
    }

    public static boolean isCond(Object e){return isForm(e, "cond"); }
    public static boolean isQuoted(Object e){return isForm(e, "quote");   }

    public static Object textOfQuotation(Object exp)
        throws SchemeException
    {
        return Cons.cadr(exp);
    }

    public static boolean isVariable(Object exp)
    {
        return exp instanceof Symbol;
    }

    public static boolean isDefinition(Object exp)
        throws NotAConsException
    {
        if(isAtom(exp))
            return false;
        else
            return Cons.car(exp) == Naming.name("define");
    }

    public static Object definitionVariable(Object expr)
        throws NotAConsException
    {
        if(isVariable(Cons.cadr(expr)))
            return Cons.cadr(expr);
        else
            return Cons.caadr(expr);
    }
    /** If this is a variable definition, return the value.  Otherwise, it
     * is a lambda.  Construct a lambda expression and return it
     */
    public static Object definitionValue(Object expr)
        throws NotAConsException
    {
        if(isVariable(Cons.cadr(expr)))
            return Cons.caddr(expr);
        else
            return new Cons(Naming.name("lambda"),
                            new Cons(Cons.cdadr(expr),
                                     Cons.cddr(expr)));
    }

    public static Object predicate(Object clause)
        throws NotAConsException
    {
        return Cons.car(clause);
    }


    public static Object actions(Object clause)
        throws NotAConsException
    {
        return Cons.cdr(clause);
    }

    public static boolean isLastExp(Object seq) throws NotAConsException
    {
        return isNull(Cons.cdr(seq));
    }

    public static boolean isLastOperand(Object seq) throws NotAConsException
    {
        return isNull(Cons.cdr(seq));
    }

    public static Object firstExp(Object seq)
        throws NotAConsException
    {
        return Cons.car(seq);
    }

    public static Object restExps(Object seq)        throws NotAConsException
    {
        return Cons.cdr(seq);
    }

    public static boolean isNoArgs(Object exp)
    {
        if(isAtom(exp))
            return false;
        else
            return isNull(Cons.cdr(exp));
    }

    public static boolean isApplication(Object exp) throws NotAConsException
    {
        if(isAtom(exp))
            return false;
        else
            return true;
    }

    public static Object operatorOf(Object app)
        throws NotAConsException
    {
        return Cons.car(app);
    }

    public static Object operandsOf(Object app)
        throws NotAConsException
    {
        return Cons.cdr(app);
    }

    public static boolean isNoOperands(Object args)
    {
        return isNull(args);
    }

    public static Object firstOperand(Object args)
        throws NotAConsException
    {
        return Cons.car(args);
    }

    public static Object restOperands(Object args) throws NotAConsException
    {
        return Cons.cdr(args);
    }

    public static boolean isIf(Object exp) throws NotAConsException
    {
        if(isAtom(exp))
            return false;
        else
            return Cons.car(exp) == Naming.name("if");
    }

    public static boolean isBegin(Object exp) throws NotAConsException
    {
        if(isAtom(exp))
            return false;
        else
            return Cons.car(exp) == Naming.name("begin");
    }


    public static boolean isLambda(Object exp)
    {
        if(isAtom(exp))
            return false;
        else
            return Cons.car(exp) == Naming.name("lambda");
    }


    public static Object lambdaParameters(Object exp)
        throws NotAConsException
    {
        return Cons.cadr(exp);
    }


    public static Object lambdaBody(Object exp)
        throws NotAConsException
    {
        return Cons.cddr(exp);
    }

    public static Object testOf(Object o) throws SchemeException
    {
        try {
            return Cons.cadr(o);
        } catch(NotAConsException e) {
            throw new SyntaxError("if requires test: " + o);
        }
    }

    public static Object consequenceOf(Object o) throws SchemeException
    {
        try {
            return Cons.caddr(o);
        } catch(NotAConsException e) {
            throw new SyntaxError("if requires consequence: " + o);
        }
    }

    public static Object alternateOf(Object o) throws SchemeException
    {
        try {
            return Cons.cadr(Cons.cddr(o));
        } catch(NotAConsException e) {
            return null;
        }
    }
}
