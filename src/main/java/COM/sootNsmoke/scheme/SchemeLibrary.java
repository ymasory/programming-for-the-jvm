package COM.sootNsmoke.scheme;
import java.io.*;

/**
 * This class contains an initalizer for the Scheme
 * binding environment, and defined in the Revised^4 Scheme
 * specification.  Those procedures defined as "essential"
 * are generally defined using Java.  Those defined as
 * non-essential are usually written using Scheme, in the
 * file "library.scm".
 * (A few non-essential procedures are written in Java, either
 * for performance reasons or just so that the author could
 * switch languages for a bit.)
 */
public class SchemeLibrary
{
    /** Initialize the binding environment to the standard Scheme environment
    */
    public static void initializeEnvironment(BindingEnv env)
    {
        // Section 6.1
        env.bind("boolean?", new isBoolean(env));
        env.bind("not", new not(env));

        // Section 6.2
        env.bind("eq?", new isEq(env));
        env.bind("eqv?", new isEqv(env));
        env.bind("equal?", new isEqual(env));

        // Section 6.3
        env.bind("pair?", new isPair(env));
        env.bind("car", new car(env));
        env.bind("cons", new makeCons(env));
        env.bind("cdr", new cdr(env));
        env.bind("set-car!", new setCar(env));
        env.bind("set-cdr!", new setCdr());
        env.bind("caar", new cXr("aa"));
        env.bind("cadr", new cXr("ad"));
        env.bind("caaar", new cXr("aaa"));
        env.bind("caadr", new cXr("aad"));
        env.bind("cadar", new cXr("ada"));
        env.bind("caddr", new cXr("add"));
        env.bind("cdaar", new cXr("daa"));
        env.bind("cdadr", new cXr("dad"));
        env.bind("cddar", new cXr("dda"));
        env.bind("cdddr", new cXr("ddd"));
        env.bind("caaaar", new cXr("aaaa"));
        env.bind("caaadr", new cXr("aaad"));
        env.bind("caadar", new cXr("aada"));
        env.bind("caaddr", new cXr("aadd"));
        env.bind("cadaar", new cXr("adaa"));
        env.bind("cadadr", new cXr("adad"));
        env.bind("caddar", new cXr("adda"));
        env.bind("cadddr", new cXr("addd"));
        env.bind("cdaaar", new cXr("daaa"));
        env.bind("cdaadr", new cXr("daad"));
        env.bind("cdadar", new cXr("dada"));
        env.bind("cdaddr", new cXr("dadd"));
        env.bind("cddaar", new cXr("ddaa"));
        env.bind("cddadr", new cXr("ddad"));
        env.bind("cdddar", new cXr("ddda"));
        env.bind("cddddr", new cXr("dddd"));
        env.bind("null?", new isNull());
        env.bind("list?", new isList());
        env.bind("list", new list());
        env.bind("length", new length());
        env.bind("append", new append());
        env.bind("reverse", new reverse());


        // Section 6.4
        env.bind("symbol?", new isSymbol(env));
        env.bind("symbol->string", new symbolToString(env));
        env.bind("string->symbol", new stringToSymbol(env));


        // Section 6.5.5
        env.bind("number?", new isNumber());
        env.bind("complex?", new isComplex());
        env.bind("real?", new isReal());
        env.bind("rational?", new isRational());
        env.bind("integer?", new isInteger());
        env.bind("exact?", new isExact());
        env.bind("inexact?", new isInexact());
        env.bind("zero?", new isZero());
        env.bind("positive?", new isPositive());
        env.bind("negative?", new isNegative());
        env.bind("even?", new isEven());
        env.bind("odd?", new isOdd());
        env.bind("=", new numEquals());
        env.bind("<", new lessThan());
        env.bind(">", new greaterThan());
        env.bind(">=", new greaterOrEqual());
        env.bind("<=", new lessOrEqual());
        env.bind("max", new max());
        env.bind("min", new min());
        env.bind("+", new plus());
        env.bind("-", new minus());
        env.bind("*", new multiply());
        env.bind("/", new divide());
        env.bind("abs", new abs());
        env.bind("quotient", new quotient());
        env.bind("remainder", new remainder());
        env.bind("modulo", new modulo());
        env.bind("gcd", new gcd());
        env.bind("lcm", new lcm());
        env.bind("rationalize", new rationalize());
        env.bind("numerator", new numerator());
        env.bind("denominator", new denominator());
        env.bind("floor", new floor());
        env.bind("ceiling", new ceiling());
        env.bind("truncate", new truncate());
        env.bind("round", new round());
        env.bind("exp", new exp());
        env.bind("log", new log());
        env.bind("sin", new sin());
        env.bind("cos", new cos());
        env.bind("tan", new tan());
        env.bind("asin", new asin());
        env.bind("acos", new acos());
        env.bind("atan", new atan());
        env.bind("sqrt", new sqrt());
        env.bind("expt", new expt());
        /*
        env.bind("make-rectangular", new makeRectangular());
        env.bind("make-polar", new makePolar());
        env.bind("real-part", new realPart());
        env.bind("imag-part", new imagPart());
        env.bind("magnitude", new magnitude());
        env.bind("angle", new sqrt());
        */
        env.bind("exact->inexact", new exactToInexact());
        env.bind("inexact->exact", new inexactToExact());

        env.bind("char?", new isChar(env));

        env.bind("display", new display(env));

        // Section 6.7 Strings
        env.bind("string?", new isString());
        env.bind("make-string", new make_string());
        env.bind("string", new string());
        env.bind("string-length", new string_length());
        env.bind("string-ref", new string_ref());
        env.bind("string-set!", new string_set());
        env.bind("string=?", new stringEquals());
        env.bind("string-ci=?", new string_ciEquals());
        env.bind("string<?", new stringLess());
        env.bind("string>?", new stringGreater());
        env.bind("string<=?", new stringLessOrEqual());
        env.bind("string>=?", new stringGreaterOrEqual());
        env.bind("string-ci<?", new string_ciLess());
        env.bind("string-ci>", new string_ciGreater());
        env.bind("string-ci<=", new string_ciLessOrEqual());
        env.bind("string-ci>=", new string_ciGreaterOrEqual());
        env.bind("substring", new substring());
        env.bind("string-append", new string_append());
        env.bind("string->list", new string_to_list());
        env.bind("list->string", new list_to_string());
        env.bind("string-copy", new string_copy());
        env.bind("string-fill", new string_fill());


        // Section 6.8 Vectors
        env.bind("vector?", new isVector());
        env.bind("make-vector", new make_vector());
        env.bind("vector", new vector());
        env.bind("vector-length", new vector_length());
        env.bind("vector-ref", new vector_ref());
        env.bind("vector-set!", new vector_set());
        env.bind("vector->list", new vector_to_list());
        env.bind("list->vector", new list_to_vector());
        env.bind("vector_fill", new vector_fill());

        // Section 6.9
        env.bind("apply", new apply());
        env.bind("map", new map());
        env.bind("force", new force());

        // Section 6.10.4
        env.bind("load", new load(env));

        // Special: terminates the program
        env.bind("quit", new quit());

    }

    public static int num(Object o)
    {
        if(o instanceof Number)
            return ((Number) o).intValue();
        throw new SchemeException("Expected a number, got " +
            ReadEvalPrint.write(o));
    }
    public static boolean isEqv(Object arg1, Object arg2)
    {
        if(arg1 == null && arg2 == null)
            return true;
        if(arg1.equals(arg2))
            return true;
        else
            return false;
    }

    /** Appends list arg2 to the end of arg1.  arg1 should be a
     * list. Returns the head of the new list (which will
     * be arg1 unless arg1 is null, in which case it's arg2.)
     */
    public static Object append(Object arg1, Object arg2)
    {
        if(arg1 == null)
            return arg2;
        Object ret = arg1;
        while(arg1 instanceof Cons)
        {
            if(((Cons) arg1).cdr() == null)
            {
                ((Cons) arg1).set_cdr(arg2);
                break;
            }
            arg1 = ((Cons) arg1).cdr();
        }
        return ret;
    }

    /** Apply procedure proc to the args.  proc is
     * a CompiledProcedure.  args is a Cons list of arguments
     */
    public static Object apply(Object proc, Object args)
    {
        CompiledProcedure p = (CompiledProcedure) proc;
        Object orig_args = args;
        if(args == null)
            return p.apply0();
        Object arg1 = ((Cons) args).car();
        if(((Cons) args).cdr() == null)
            return p.apply1(arg1);
        args = ((Cons) args).cdr();
        Object arg2 = ((Cons) args).car();
        if(((Cons) args).cdr() == null)
            return p.apply2(arg1, arg2);
        args = ((Cons) args).cdr();
        Object arg3 = ((Cons) args).car();
        if(((Cons) args).cdr() == null)
            return p.apply3(arg1, arg2, arg3);
        return p.applyN(new Cons(proc, orig_args));
    }

    /** Splices the object o into the vector v.  If o is
     * a Cons list, each car of the list is added to v.
     * If o is an array, then the elements tacked on to the end
     * of the vector.  Otherwise, the results are undefined.
     */
    public static void spliceVector(java.util.Vector v, Object o)
    {
        if(o instanceof Object[])
        {
            Object a[] = (Object[]) o;
            for(int i = 0; i < a.length; i++)
                v.addElement(a[i]);
        }
        else if(o instanceof Cons)
        {
            while(o instanceof Cons)
            {
                v.addElement(((Cons) o).car());
                o = ((Cons) o).cdr();
            }
        }
        else
            v.addElement(o);
    }

    /** Returns the length of a list. */
    public static int length(Object l)
    {
        int n = 0;
        for(; l instanceof Cons; l = ((Cons) l).cdr())
            n++;
        return n;
    }

    /** Converts a long to an Integer or Long, as appropriate */
    public static Number value(long l)
    {
        if((int) l == l)
            return new Integer((int) l);
        else
            return new Long(l);
    }

    /** Compares two mutable or immutable strings.  Returns
     * <0 if s1<s2, 0 if s1=s2, or >0 of s1>s2.
     */
    public static int compare(Object s1, Object s2,
                              boolean ignoreCase)
    {
        int n =(s1 instanceof String)
            ? ((String) s1).length()
            : ((Character[]) s1).length;

        for(int i = 0; i < n; i++)
        {
            char c1 = (s1 instanceof String)
                ? ((String) s1).charAt(i)
                : ((Character[]) s1)[i].charValue();
            char c2 = (s2 instanceof String)
                ? ((String) s2).charAt(i)
                : ((Character[]) s2)[i].charValue();
            if(ignoreCase)
            {
                c1 = Character.toLowerCase(c1);
                c2 = Character.toLowerCase(c2);
            }
            if(c1 != c2)
                return c1-c2;
        }
        int n2 = (s2 instanceof String)
            ? ((String) s2).length()
            : ((Character[]) s2).length;
        return n-n2;
    }
}

class isChar extends CompiledProcedure
{
    isChar(BindingEnv env) { this.env = env; }

    public Object apply1(Object arg)
    {
        if(arg instanceof Character)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}

//*************************************************************
// Section 6.1
//*************************************************************
class not extends CompiledProcedure
{
    not(BindingEnv env) { this.env = env; }
    public Object apply1(Object arg)
    {
        if(arg == Boolean.FALSE)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}

class isBoolean extends CompiledProcedure
{
    isBoolean(BindingEnv env) { this.env = env; }

    public Object apply1(Object arg)
    {
        if(arg == Boolean.TRUE || arg == Boolean.FALSE)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}


//*************************************************************
// Section 6.2
//*************************************************************
class isEq extends CompiledProcedure
{
    isEq(BindingEnv env)
    {
        this.env = env;
    }

    public Object apply2(Object arg1, Object arg2)
    {
        if(arg1 == arg2)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }
}

class isEqv extends CompiledProcedure
{
    isEqv(BindingEnv env)
    {
        this.env = env;
    }

    public Object apply2(Object arg1, Object arg2)
    {
        if(SchemeLibrary.isEqv(arg1, arg2))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}

class isEqual extends CompiledProcedure
{
    isEqual(BindingEnv env) { this.env = env; }

    public Object apply2(Object arg1, Object arg2)
    {
        return isEqual(arg1, arg2) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static boolean isEqual(Object arg1, Object arg2)
    {
        if(arg1 instanceof Cons)
        {
            if(arg2 instanceof Cons)
            {
                Cons c1 = (Cons) arg1;
                Cons c2 = (Cons) arg2;
                if(isEqual(c1.car(), c2.car()))
                    return isEqual(c1.cdr(), c2.cdr());
                else
                    return false;
            }
            else
                return false;
        }
        if(arg1 instanceof Object[] && arg2 instanceof Object[])
        {
            Object[] a1 = (Object[]) arg1;
            Object[] a2 = (Object[]) arg2;
            if(a1.length != a2.length)
                return false;
            for(int i = 0; i < a1.length; i++)
                if(!isEqual(a1[i], a2[i]))
                    return false;
            return true;
        }
        if(arg1 == null && arg2 == null)
            return true;
        if(arg1.equals(arg2))
            return true;
        return false;
    }
}

//*************************************************************
// Section 6.3
//*************************************************************
class makeCons extends CompiledProcedure
{
    makeCons(BindingEnv env) { this.env = env; }
    public Object apply2(Object arg1, Object arg2)
    {
        return new Cons(arg1, arg2);
    }
}
class isPair extends CompiledProcedure {
    isPair(BindingEnv env) { this.env = env; }
    public Object apply1(Object o) {
        return o instanceof Cons ? Boolean.TRUE : Boolean.FALSE;
    }
}
class car extends CompiledProcedure
{
    car(BindingEnv env) { this.env = env; }
    public Object apply1(Object arg) { return ((Cons) arg).car(); }
}
class cdr extends CompiledProcedure
{
    cdr(BindingEnv env) { this.env = env; }
    public Object apply1(Object arg) { return ((Cons) arg).cdr(); }
}
class setCar extends CompiledProcedure
{
    setCar(BindingEnv env) { this.env = env; }
    public Object apply2(Object arg1, Object arg2)
    {
        ((Cons) arg1).set_car(arg2);
        return null;
    }
}
class setCdr extends CompiledProcedure
{
    public Object apply2(Object arg1, Object arg2)
    {
        ((Cons) arg1).set_cdr(arg2);
        return null;
    }
}
/** This class takes care of caar, cadar, cadr, ... cdddr */
class cXr extends CompiledProcedure
{
    String pattern;
    cXr(String pattern) { this.pattern = pattern; }
    public Object apply1(Object arg)
    {
        for(int i = pattern.length()-1; i >= 0; i--)
            if(pattern.charAt(i) == 'a')
                arg = ((Cons) arg).car();
            else
                arg = ((Cons) arg).cdr();
        return arg;
    }
}
class isNull extends CompiledProcedure {
    public Object apply1(Object arg) {
        return arg == null ? Boolean.TRUE : Boolean.FALSE; }
}
/** Returns #t if obj is a list, otherwise returns #f.
 * By definition, all lists have finite length and are
 * terminate by the empty list.
 */
class isList extends CompiledProcedure {
    public Object apply1(Object arg) {
        java.util.Hashtable t = new java.util.Hashtable();

        while(arg instanceof Cons && !t.containsKey(arg))
        {
            t.put(arg, Boolean.TRUE);
            arg = ((Cons) arg).cdr();
        }
        return arg == null ? Boolean.TRUE : Boolean.FALSE;
    }
}
class list extends CompiledProcedure {
    public Object applyN(Cons args) { return args; }
}
class length extends CompiledProcedure {
    public Object apply1(Object arg)
    {
        int l = 0;
        while(arg != null)
        {
            l++;
            arg = ((Cons) arg).cdr();
        }
        return new Integer(l);
    }
}
class append extends CompiledProcedure {
    public Object applyN(Cons args)
    {
        if(args.cdr() == null)
            return args.car();
        Cons c = (Cons) args.car();
        Object rest = applyN((Cons) args.cdr());
        if(c == null)
            return rest;
        Cons list = new Cons(c.car(), null);
        Cons q = list;
        while((c = (Cons) c.cdr()) != null)
        {
            Cons tail = new Cons(c.car(), null);
            q.set_cdr(tail);
            q = tail;
        }
        q.set_cdr(rest);
        return list;
    }
}
class reverse extends CompiledProcedure {
    public Object apply1(Object arg)
    {
        Cons list = null;
        while(arg != null)
        {
            list = new Cons(((Cons) arg).car(), list);
            arg = ((Cons) arg).cdr();
        }
        return list;
    }
}

//*************************************************************
// Section 6.4
//*************************************************************
class isSymbol extends CompiledProcedure {
    isSymbol(BindingEnv env) { this.env = env; }
    public Object apply1(Object obj) {
        return obj instanceof Symbol ? Boolean.TRUE : Boolean.FALSE;
    }
}
class symbolToString extends CompiledProcedure {
    symbolToString(BindingEnv env) { this.env = env; }
    public Object apply1(Object symbol) {
        return symbol.toString(); }
}
class stringToSymbol extends CompiledProcedure {
    stringToSymbol(BindingEnv env) { this.env = env; }
    public Object apply1(Object string) {
        return Naming.name((String) string); }
}



//*************************************************************
// Section 6.5.5
//*************************************************************
class max extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number max = (Number) args.car();
        args = (Cons) args.cdr();
        while(args != null)
        {
            Number n2 = (Number) args.car();
            if(Numbers.greaterThan(n2, max))
                max = n2;
            args = (Cons) args.cdr();
        }
        return max;
    }
}
class min extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number min = (Number) args.car();
        args = (Cons) args.cdr();
        while(args != null)
        {
            Number n2 = (Number) args.car();
            if(Numbers.lessThan(n2, min))
                min = n2;
            args = (Cons) args.cdr();
        }
        return min;
    }
}

class lessOrEqual extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number n = (Number) args.car();
        args = (Cons) args.cdr();
        while(args != null)
        {
            Number n2 = (Number) args.car();
            if(Numbers.greaterThan(n, n2))
                return Boolean.FALSE;
            args = (Cons) args.cdr();
        }

        return Boolean.TRUE;
    }
}class greaterOrEqual extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number n = (Number) args.car();
        args = (Cons) args.cdr();
        while(args != null)
        {
            Number n2 = (Number) args.car();
            if(Numbers.lessThan(n, n2))
                return Boolean.FALSE;
            args = (Cons) args.cdr();
        }

        return Boolean.TRUE;
    }
}
class greaterThan extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number n = (Number) args.car();
        args = (Cons) args.cdr();
        while(args != null)
        {
            Number n2 = (Number) args.car();
            if(!Numbers.greaterThan(n, n2))
                return Boolean.FALSE;
            args = (Cons) args.cdr();
        }

        return Boolean.TRUE;
    }
}
class lessThan extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number n = (Number) args.car();
        args = (Cons) args.cdr();
        while(args != null)
        {
            Number n2 = (Number) args.car();
            if(!Numbers.lessThan(n, n2))
                return Boolean.FALSE;
            args = (Cons) args.cdr();
        }

        return Boolean.TRUE;
    }
}

class numEquals extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number n = (Number) args.car();
        args = (Cons) args.cdr();
        while(args != null)
        {
            if(!n.equals(args.car()))
                return Boolean.FALSE;
            args = (Cons) args.cdr();
        }

        return Boolean.TRUE;
    }
}

class multiply extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number sum = new Integer(1);
        while(args != null)
        {
            Number next = (Number) args.car();
            sum = Numbers.multiply(sum, next);
            args = (Cons) args.cdr();
        }

        return sum;
    }
}

class plus extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number sum = new Integer(0);
        while(args != null)
        {
            Number next = (Number) args.car();
            sum = Numbers.add(sum, next);
            args = (Cons) args.cdr();
        }

        return sum;
    }
}

class divide extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number first = (Number) args.car();
        Number sum = new Integer(1);
        args = (Cons) args.cdr();
        if(args == null)
            return Numbers.divide(sum, first);
        while(args != null)
        {
            Number next = (Number) args.car();
            sum = Numbers.multiply(sum, next);
            args = (Cons) args.cdr();
        }
        return Numbers.divide(first, sum);
    }
}


class minus extends CompiledProcedure
{
    public Object applyN(Cons args)
    {
        Number first = (Number) args.car();
        Number sum = new Integer(0);
        args = (Cons) args.cdr();
        if(args == null)
            return Numbers.subtract(sum, first);
        while(args != null)
        {
            Number next = (Number) args.car();
            sum = Numbers.add(sum, next);
            args = (Cons) args.cdr();
        }
        return Numbers.subtract(first, sum);
    }
}

class isZero extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(Numbers.equals((Number) arg, new Integer(0)))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}

class isReal extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(arg instanceof Number &&
           !(arg instanceof Complex))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
class isNumber extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(arg instanceof Number)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
class isComplex extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(arg instanceof Number)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
class isInteger extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(arg instanceof Integer || arg instanceof Long)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
class isRational extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(arg instanceof Integer || arg instanceof Long ||
           arg instanceof Rational)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
class isExact extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(isExact(arg))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    static boolean isExact(Object arg)
    {
        return
            arg instanceof Integer || arg instanceof Long ||
            arg instanceof Rational ||
            (arg instanceof Complex &&
                isExact(((Complex) arg).realPart()) &&
                isExact(((Complex) arg).imagPart()));
    }
}

class isInexact extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(isExact.isExact(arg))
            return Boolean.FALSE;
        else
            return Boolean.TRUE;
    }
}
class isEven extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if((((Number) arg).intValue() & 1) == 0)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
class isOdd extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if((((Number) arg).intValue() & 1) == 1)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
class isPositive extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(((Number) arg).doubleValue() > 0)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
class isNegative extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        if(((Number) arg).doubleValue() < 0)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
class abs extends CompiledProcedure
{
    public Object apply1(Object arg) {
            return Numbers.abs((Number) arg); }
}

class quotient extends CompiledProcedure
{
    public Object apply2(Object a1, Object a2) {
        Number n1 = (Number) a2;
        Number n2 = (Number) a2;
        return SchemeLibrary.value(n1.longValue() / n2.longValue());
    }
}
class remainder extends CompiledProcedure
{
    public Object apply2(Object a1, Object a2) {
        Number n1 = (Number) a2;
        Number n2 = (Number) a2;
        long quot = n1.longValue() / n2.longValue();
        long rem = n1.longValue() - (n2.longValue()) * quot;
        return SchemeLibrary.value(rem);
    }
}
class modulo extends CompiledProcedure
{
    public Object apply2(Object a1, Object a2) {
        Number n1 = (Number) a2;
        Number n2 = (Number) a2;
        return SchemeLibrary.value(n1.longValue() % n2.longValue());
    }
}
class gcd extends CompiledProcedure
{
    public Object applyN(Cons list)
    {
        long gcd = ((Number) list.car()).longValue();
        list = (Cons) list.cdr();
        while(list != null)
        {
            long n = ((Number) list.car()).longValue();
            gcd = Rational.gcd(gcd, n);
            list = (Cons) list.cdr();
        }
        return SchemeLibrary.value(gcd);
    }
}
class lcm extends CompiledProcedure
{
    public Object applyN(Cons list)
    {
        long lcm = ((Number) list.car()).longValue();
        list = (Cons) list.cdr();
        while(list != null)
        {
            long n = ((Number) list.car()).longValue();
            lcm = Rational.lcm(lcm, n);
            list = (Cons) list.cdr();
        }
        return SchemeLibrary.value(lcm);
    }
}
class rationalize extends CompiledProcedure
{
    public Object apply2(Object x, Object y)
    {
        double a = ((Number) x).doubleValue();
        double b = ((Number) y).doubleValue();
        return Rational.rationalize(a, b);
    }
}


class numerator extends CompiledProcedure
{
    public Object apply1(Object q) {
        return SchemeLibrary.value(((Rational) q).numerator()); }
}
class denominator extends CompiledProcedure
{
    public Object apply1(Object q) {
        return SchemeLibrary.value(((Rational) q).denominator()); }
}

/** A convenience class for functions with a single
 * double argument
 */
abstract class F extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        return new Double(f(((Number) arg).doubleValue()));
    }

    public abstract double f(double n);
}
abstract class F2 extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        return f(((Number) arg));
    }
    public abstract Number f(Number n);
}

class floor extends F2 {
    public Number f(Number n) {
        return SchemeLibrary.value((long) Math.floor(n.doubleValue())); }
}
class ceiling extends F2 {
    public Number f(Number n) {
        return SchemeLibrary.value((long) Math.ceil(n.doubleValue())); }
}
class truncate extends F2 {
    public Number f(Number n) {
        return SchemeLibrary.value(n.longValue()); }
}
class round extends F2 {
    public Number f(Number n) {
        return SchemeLibrary.value(Math.round(n.doubleValue())); }
}

class exp extends F { public double f(double n) { return Math.exp(n); }}
class log extends F { public double f(double n) { return Math.log(n); }}
class sin extends F { public double f(double n) { return Math.sin(n); }}
class cos extends F { public double f(double n) { return Math.cos(n); }}
class tan extends F { public double f(double n) { return Math.tan(n); }}
class acos extends F { public double f(double n) { return Math.acos(n); }}
class asin extends F { public double f(double n) { return Math.asin(n); }}
class atan extends F { public double f(double n) { return Math.acos(n); }
    public Object apply2(Object y, Object x)
    {
        double y_ = ((Number) y).doubleValue();
        double x_ = ((Number) x).doubleValue();
        return new Double(Math.atan2(y_, x_));
    }
}
class expt extends CompiledProcedure
{
    public Object apply2(Object z1, Object z2)
    {
        double x1 = ((Number) z1).doubleValue();
        double x2 = ((Number) z2).doubleValue();
        return new Double(Math.pow(x1, x2));

    }
}
class exactToInexact extends CompiledProcedure
{
    public Object apply1(Object z)
    {
        if(z instanceof Integer || z instanceof Long ||
           z instanceof Rational)
            return new Double(((Number) z).doubleValue());
        return z;
    }
}
class inexactToExact extends CompiledProcedure
{
    public Object apply1(Object z)
    {
        if(z instanceof Integer || z instanceof Long ||
           z instanceof Rational)
            return z;
        if(z instanceof Double || z instanceof Float)
            return new Long(((Number) z).longValue());
        return z;
    }
}

class sqrt extends CompiledProcedure
{
    public Object apply1(Object arg) {
            return Numbers.sqrt((Number) arg); }
}
//*************************************************************
// Section 6.7 Strings
//*************************************************************
class isString extends CompiledProcedure
{
    public Object apply1(Object arg) {
        return arg instanceof String || arg instanceof Character[] ?
            Boolean.TRUE : Boolean.FALSE;
    }
}
class make_string extends CompiledProcedure
{
    public Object apply1(Object k) {
        int n = SchemeLibrary.num(k);
        return new Character[n];
    }
    public Object apply2(Object k, Object c)
    {
        if(!(c instanceof Character))
            throw new SchemeException("make-string requires a character; got " +
                ReadEvalPrint.write(k));
        Character[] ret = (Character[]) apply1(k);
        for(int i = 0; i < ret.length; i++)
            ret[i] = (Character) c;
        return ret;
    }
}
class string extends CompiledProcedure
{
    public Object applyN(Cons chars)
    {
        int l = SchemeLibrary.length(chars);
        Character[] arr = new Character[l];
        for(int i = 0; i < l; i++)
        {
            arr[i] = (Character) chars.car();
            chars = (Cons) chars.cdr();
        }
        return arr;
    }
}
class string_length extends CompiledProcedure
{
    public Object apply1(Object o)
    {
        if(o instanceof String)
            return new Integer(((String) o).length());
        else
            return new Integer(((Character[]) o).length);

    }
}
class string_ref extends CompiledProcedure
{
    public Object apply2(Object string, Object k)
    {
        int n = SchemeLibrary.num(k);
        if(string instanceof String)
            return new Character(((String) string).charAt(n));
        return ((Character[]) string)[n];
    }
}
class string_set extends CompiledProcedure
{
    public Object apply3(Object string, Object k, Object c)
    {
        int n = SchemeLibrary.num(k);
        if(string instanceof String)
            throw new SchemeException("Cannot alter immutable string");
        ((Character[]) string)[n] = (Character) c;
        return c;
    }
}
class stringEquals extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return SchemeLibrary.compare(s1, s2, false) == 0
            ? Boolean.TRUE
            : Boolean.FALSE;
    }
}
class string_ciEquals extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return SchemeLibrary.compare(s1, s2, true) == 0
            ? Boolean.TRUE
            : Boolean.FALSE;
    }
}

class stringLess extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return (SchemeLibrary.compare(s1, s2, false) < 0)
            ? Boolean.TRUE : Boolean.FALSE;
    }
}
class stringGreater extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return (SchemeLibrary.compare(s1, s2, false) > 0)
            ? Boolean.TRUE : Boolean.FALSE;
    }
}
class stringGreaterOrEqual extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return (SchemeLibrary.compare(s1, s2, false) >= 0)
            ? Boolean.TRUE : Boolean.FALSE;
    }
}
class stringLessOrEqual extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return (SchemeLibrary.compare(s1, s2, false) <= 0)
            ? Boolean.TRUE : Boolean.FALSE;
    }
}
class string_ciLess extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return (SchemeLibrary.compare(s1, s2, true) < 0)
            ? Boolean.TRUE : Boolean.FALSE;
    }
}
class string_ciGreater extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return (SchemeLibrary.compare(s1, s2, true) > 0)
            ? Boolean.TRUE : Boolean.FALSE;
    }
}
class string_ciGreaterOrEqual extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return (SchemeLibrary.compare(s1, s2, true) >= 0)
            ? Boolean.TRUE : Boolean.FALSE;
    }
}
class string_ciLessOrEqual extends CompiledProcedure
{
    public Object apply2(Object s1, Object s2)
    {
        return (SchemeLibrary.compare(s1, s2, true) <= 0)
            ? Boolean.TRUE : Boolean.FALSE;
    }
}


class substring extends CompiledProcedure
{
    public Object apply3(Object string, Object start, Object end)
    {
        int s = SchemeLibrary.num(start);
        int e = SchemeLibrary.num(end);
        Character[] ss = new Character[e-s];
        if(string instanceof String)
            for(int i = s; i < e; i++)
                ss[i-s] = new Character(((String) string).charAt(i));
        else
            for(int i = s; i < e; i++)
                ss[i-s] = ((Character[]) string)[i];
        return ss;
    }
}

class string_append extends CompiledProcedure
{
    public Object applyN(Cons list)
    {
        int length = 0;
        Cons l = list;
        while(l != null)
        {
            length += (list.car() instanceof String)
                    ? ((String) list.car()).length()
                    : ((Character[]) list.car()).length;
        }
        Character[] s = new Character[length];
        int n = 0;
        while(list != null)
        {
            if(list.car() instanceof String)
            {
                String st = (String) list.car();
                for(int i = 0; i < st.length(); i++)
                    s[n++] = new Character(st.charAt(i));
            }
            else
            {
                Character[] c = (Character[]) list.car();
                for(int i = 0; i < c.length; i++)
                    s[n++] = c[i];
            }

            list = (Cons) list.cdr();
        }
        return s;
    }
}

class string_to_list extends CompiledProcedure
{
    public Object apply1(Object string)
    {
        Cons l = null;
        if(string instanceof String)
        {
            String s = (String) string;
            for(int i = s.length() -1; i >= 0; i--)
                l = new Cons(new Character(s.charAt(i)), l);
        }
        else
        {
            Character[] s = (Character[]) string;
            for(int i = s.length-1; i >= 0; i--)
                l = new Cons(s[i], l);
        }
        return l;
    }
}
class list_to_string extends CompiledProcedure
{
    public Object apply1(Object list)
    {
        int n = SchemeLibrary.length(list);
        Character[] s = new Character[n];
        for(int i = 0; i < s.length; i++)
        {
            s[i] = (Character) ((Cons) list).car();
            list = ((Cons) list).cdr();
        }
        return s;
    }
}
class string_copy extends CompiledProcedure
{
    public Object apply1(Object string)
    {
        if(string instanceof String[])
        {
            String s = (String) string;
            Character[] c = new Character[s.length()];
            for(int i = 0; i < s.length(); i++)
                c[i] = new Character(s.charAt(i));
            return c;
        }
        else
        {
            Character[] s = (Character[]) string;
            Character[] c = new Character[s.length];
            for(int i = 0; i < s.length; i++)
                c[i] = s[i];
            return c;
        }
    }
}
class string_fill extends CompiledProcedure
{
    public Object apply2(Object string, Object c)
    {
        if(string instanceof Character[])
        {
            Character[] s = (Character[]) string;
            for(int i = 0; i < s.length; i++)
                s[i] = (Character) c;
            return null;
        }
        if(string instanceof String)
            throw new SchemeException("Cannot fill immutable string");
        throw new SchemeException("Invalid string");
    }
}
//*************************************************************
// Section 6.8 Vectors
//*************************************************************
class isVector extends CompiledProcedure {
    public Object apply1(Object arg) {
        return (arg instanceof Object) ? Boolean.TRUE : Boolean.FALSE;
    }
}

class make_vector extends CompiledProcedure {
    public Object apply1(Object k) {
        return new Object[((Number) k).intValue()];
    }
    public Object apply2(Object k, Object fill) {
        Object[] v = (Object[]) apply1(k);
        for(int i = 0; i < v.length; i++)
            v[i] = fill;
        return v;
    }
}

class vector extends CompiledProcedure
{
    public Object applyN(Cons objs)
    {
        int n = SchemeLibrary.length(objs);
        Object[] o = new Object[n];
        for(int i = 0; i < n; i++)
        {
            o[i] = objs.car();
            objs = (Cons) objs.cdr();
        }
        return o;
    }
}

class vector_length extends CompiledProcedure {
    public Object apply1(Object v) {
        return new Integer(((Object[]) v).length); }
}

class vector_ref extends CompiledProcedure {
    public Object apply2(Object v, Object k) {
        return ((Object[]) v)[((Number) k).intValue()];
    }
}

class vector_set extends CompiledProcedure {
    public Object apply3(Object v, Object k, Object obj) {
        return ((Object[]) v)[((Number) k).intValue()] = obj;
    }
}

class vector_to_list extends CompiledProcedure {
    public Object apply1(Object vector)
    {
        if(!(vector instanceof Object[]))
            throw new SchemeException("vector->list requires a list; got "
                + ReadEvalPrint.write(vector));
        Cons ret = null;
        Object[] v = (Object[]) vector;
        for(int i = v.length-1; i >= 0; i--)
            ret = new Cons(v[i], ret);
        return ret;
    }
}

class list_to_vector extends CompiledProcedure {
    public Object apply1(Object list)
    {
        if(!(list instanceof Cons))
            throw new SchemeException("list->vector requires a list; got "
                + ReadEvalPrint.write(list));
        Cons objs = (Cons) list;
        int n = SchemeLibrary.length(objs);
        Object[] o = new Object[n];
        for(int i = 0; i < n; i++)
        {
            o[i] = objs.car();
            objs = (Cons) objs.cdr();
        }
        return o;
    }
}

class vector_fill extends CompiledProcedure {
    public Object apply2(Object v, Object fill)
    {
        Object[] vec = (Object[]) v;
        for(int i = 0; i < vec.length; i++)
            vec[i] = fill;
        return null;
    }
}

//*************************************************************
// Section 6.9
//*************************************************************
class apply extends CompiledProcedure
{
    public Object apply2(Object proc, Object args) throws SchemeException
    {
        return SchemeLibrary.apply(proc, args);
    }
    public Object applyN(Cons args) throws SchemeException
    {
        return apply2(args.car(), args.cdr());
    }
}

class map extends CompiledProcedure
{
    Cons buildArgs(Cons lists)
    {
        if(lists == null)
            return null;
        Cons c = (Cons) lists.car();
        if(c == null)
            return null;
        Object head = c.car();
        lists.set_car(c.cdr());
        return new Cons(head,
                        buildArgs((Cons) lists.cdr()));
    }

    public Object applyN(Cons arg)
    {
        if(!(arg.car() instanceof CompiledProcedure))
            throw new SchemeException("map requires a procedure argument");
        Object proc = arg.car();
        Cons list = (Cons) arg.cdr();
        Object args = buildArgs(list);
        Cons ret = null;
        Cons tail = ret;

        while(args != null)
        {
            Object result = SchemeLibrary.apply(proc, args);
            Cons t = new Cons(result, null);
            if(ret == null)
                ret = t;
            else
                tail.set_cdr(t);
            tail = t;
            args = buildArgs(list);
        }
        return ret;
    }
}

class force extends CompiledProcedure
{
    public Object apply1(Object arg)
    {
        return ((Promise) arg).force();
    }
}


//*************************************************************
// Section 6.10.3
//*************************************************************
class display extends CompiledProcedure
{
    display(BindingEnv env) { this.env = env; }

    public Object apply1(Object o)
    {
        System.out.println(display(o));
        return null;
    }

    public static String display(Object o)
    {
        if(o == Boolean.TRUE)
            return "#t";
        else if(o == Boolean.FALSE)
            return "#f";
        else if(o == null)
            return "nil";
        else
            return o.toString();

    }
}
class write extends CompiledProcedure
{
    write(BindingEnv env) { this.env = env; }

    public Object apply1(Object o)
    {
        System.out.print(ReadEvalPrint.write(o));
        return null;
    }
}

//*************************************************************
// Section 6.10.4
//*************************************************************
class load extends CompiledProcedure
{
    ReadEvalPrint eval = new ReadEvalPrint();
    load(BindingEnv env) { this.env = env; }
    public Object apply1(Object filename)
    {
        try {
            InputStream is = new FileInputStream((String) filename);
            // Run ReadEvalPrint, without printing prompts or saving
            // the results
            eval.run(is, env, false, false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}


//*************************************************************
// Special
//*************************************************************
class quit extends CompiledProcedure
{
    public Object apply0() { System.exit(0); return null; }
}






/*
class apply extends CompiledProcedure
{
    apply(BindingEnv env) { this.env = env; }

    public Object apply2(Object f, Object args)
    {
        int nargs = 0;
        CompiledProcedure closure = (CompiledProcedure) f;
        Object cons = args;
        Object arglist[] = new Object[3];
        // compute the number of arguments and build the arglist
        while(cons instanceof Cons)
        {
            nargs++;
            if(nargs > 3)
                break;
            arglist[nargs-1] = ((Cons) cons).car();
            cons = ((Cons) cons).cdr();
        }
        switch(nargs)
        {
            case 0:
                return closure.apply0();
            case 1:
                return closure.apply1(arglist[0]);
            case 2:
                return closure.apply2(arglist[0], arglist[1]);
            case 3:
                return closure.apply3(arglist[0], arglist[1], arglist[2]);
            default:
                return closure.applyN((Cons) args);
        }
    }
}
*/