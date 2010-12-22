package COM.sootNsmoke.scheme;

/** A collection of static methods which perform basic arithmetic
 * on Numbers despite Java's library giving us no help.  This
 * means that the code in here involves a lot of tedious checking
 * for type.
 * This is the hierarchy:
 *   Complex
 *   Real  (Double or Float)
 *   Rational
 *   Integer (Long or Integer)
 * Both numbers are cast to the highest level of either,
 * and the result is of that type.
 * Just so this doesn't take absolutely FOREVER,
 * there is a special case at the beginning to handle
 * two ints or floats.  (It's still way too slow for a real
 * system, but I can't do better without a radical change
 * to the way numbers are stored)
 */
public class Numbers
{
    /** Return true if the number is positive.
     * If Complex, examines only the real part.
     */
    public static boolean positive(Number n)
    {
        return n.doubleValue() > 0;
    }

    public static boolean negative(Number n)
    {
        return n.doubleValue() > 0;
    }

    public static Number abs(Number n)
    {
        if(positive(n))
            return n;
        if(n instanceof Integer)
            return new Integer(-n.intValue());
        if(n instanceof Float)
            return new Float(-n.floatValue());
        if(n instanceof Long)
            return new Long(-n.longValue());
        if(n instanceof Double)
            return new Double(-n.doubleValue());
        if(n instanceof Rational)
        {
            Rational r = (Rational) n;
            return new Rational(-r.numerator(), r.denominator());
        }
        if(n instanceof Complex)
            return new Double(((Complex) n).magnitude());
        throw new SchemeException("Invalid number " + n);
    }

    public static Number sqrt(Number n)
    {
        if(n instanceof Integer)
            return new Integer((int) Math.sqrt(n.intValue()));
        if(n instanceof Float)
            return new Float((float) Math.sqrt(n.floatValue()));
        if(n instanceof Long)
            return new Long((long) Math.sqrt(n.longValue()));
        if(n instanceof Double)
            return new Double(Math.sqrt(n.doubleValue()));
        if(n instanceof Rational)
            return ((Rational) n).sqrt();
        if(n instanceof Complex)
            return ((Complex) n).sqrt();
        throw new SchemeException("Invalid number " + n);
    }


    /** Returns arg1/arg2. */
    public static Number divide(Number arg1, Number arg2)
    {
        if(arg1 instanceof Integer && arg2 instanceof Integer)
        {
            int a = ((Integer) arg1).intValue();
            int b = ((Integer) arg2).intValue();
            if(b == 1)
                return arg1;
            return new Rational(a, b);
        }
        if(arg1 instanceof Float && arg2 instanceof Float)
        {
            Float a = (Float) arg1;
            Float b = (Float) arg2;
            return new Float(a.floatValue() / b.floatValue());
        }
        if(arg1 instanceof Complex || arg2 instanceof Complex)
        {
            Complex a = Complex.coerceComplex(arg1);
            Complex b = Complex.coerceComplex(arg2);
            return a.divide(b);
        }
        if(arg1 instanceof Double || arg1 instanceof Float ||
           arg2 instanceof Double || arg2 instanceof Float)
        {
            double a = ((Number) arg1).doubleValue();
            double b = ((Number) arg2).doubleValue();
            if(arg1 instanceof Double || arg2 instanceof Double)
                return new Double(a/b);
            else
                return new Float(a/b);
        }
        if(arg1 instanceof Rational || arg2 instanceof Rational)
        {
            Rational a = Rational.coerceRational(arg1);
            Rational b = Rational.coerceRational(arg2);
            return a.divide(b);
        }
        if(arg1 instanceof Integer || arg1 instanceof Long ||
           arg2 instanceof Integer || arg2 instanceof Long)
        {
            long a = ((Number) arg1).longValue();
            long b = ((Number) arg2).longValue();
            if(b == 1)
                return arg1;
            return new Rational(a, b);
        }
        throw new SchemeException("Cannot multiply " +
            arg1 + " and " + arg2);
    }
    /** Multply two numbers.  See add for more about
     * how this relates to the type system.
     */
    public static Number multiply(Number arg1, Number arg2)
    {
        if(arg1 instanceof Integer && arg2 instanceof Integer)
        {
            Integer a = (Integer) arg1;
            Integer b = (Integer) arg2;
            return new Integer(a.intValue() * b.intValue());
        }
        if(arg1 instanceof Float && arg2 instanceof Float)
        {
            Float a = (Float) arg1;
            Float b = (Float) arg2;
            return new Float(a.floatValue() * b.floatValue());
        }
        if(arg1 instanceof Complex || arg2 instanceof Complex)
        {
            Complex a = Complex.coerceComplex(arg1);
            Complex b = Complex.coerceComplex(arg2);
            return a.multiply(b);
        }
        if(arg1 instanceof Double || arg1 instanceof Float ||
           arg2 instanceof Double || arg2 instanceof Float)
        {
            double a = ((Number) arg1).doubleValue();
            double b = ((Number) arg2).doubleValue();
            if(arg1 instanceof Double || arg2 instanceof Double)
                return new Double(a*b);
            else
                return new Float(a*b);
        }
        if(arg1 instanceof Rational || arg2 instanceof Rational)
        {
            Rational a = Rational.coerceRational(arg1);
            Rational b = Rational.coerceRational(arg2);
            return a.multiply(b);
        }
        if(arg1 instanceof Integer || arg1 instanceof Long ||
           arg2 instanceof Integer || arg2 instanceof Long)
        {
            long a = ((Number) arg1).longValue();
            long b = ((Number) arg2).longValue();
            return new Long(a*b);
        }
        throw new SchemeException("Cannot multiply " +
            arg1 + " and " + arg2);
    }

    /** Add two numbers.       */
    public static Number add(Number arg1, Number arg2)
    {
        if(arg1 instanceof Integer && arg2 instanceof Integer)
        {
            Integer a = (Integer) arg1;
            Integer b = (Integer) arg2;
            return new Integer(a.intValue() + b.intValue());
        }
        if(arg1 instanceof Float && arg2 instanceof Float)
        {
            Float a = (Float) arg1;
            Float b = (Float) arg2;
            return new Float(a.floatValue() + b.floatValue());
        }
        if(arg1 instanceof Complex || arg2 instanceof Complex)
        {
            Complex a = Complex.coerceComplex(arg1);
            Complex b = Complex.coerceComplex(arg2);
            return a.add(b);
        }
        if(arg1 instanceof Double || arg1 instanceof Float ||
           arg2 instanceof Double || arg2 instanceof Float)
        {
            double a = ((Number) arg1).doubleValue();
            double b = ((Number) arg2).doubleValue();
            if(arg1 instanceof Double || arg2 instanceof Double)
                return new Double(a+b);
            else
                return new Float(a+b);
        }
        if(arg1 instanceof Rational || arg2 instanceof Rational)
        {
            Rational a = Rational.coerceRational(arg1);
            Rational b = Rational.coerceRational(arg2);
            return a.add(b);
        }
        if(arg1 instanceof Integer || arg1 instanceof Long ||
           arg2 instanceof Integer || arg2 instanceof Long)
        {
            long a = ((Number) arg1).longValue();
            long b = ((Number) arg2).longValue();
            return new Long(a+b);
        }
        throw new SchemeException("Cannot add " +
            arg1 + " and " + arg2);
    }

    public static Number subtract(Number arg1, Number arg2)
    {
        if(arg1 instanceof Integer && arg2 instanceof Integer)
        {
            Integer a = (Integer) arg1;
            Integer b = (Integer) arg2;
            return new Integer(a.intValue() - b.intValue());
        }
        if(arg1 instanceof Float && arg2 instanceof Float)
        {
            Float a = (Float) arg1;
            Float b = (Float) arg2;
            return new Float(a.floatValue() - b.floatValue());
        }
        if(arg1 instanceof Complex || arg2 instanceof Complex)
        {
            Complex a = Complex.coerceComplex(arg1);
            Complex b = Complex.coerceComplex(arg2);
            return a.subtract(b);
        }
        if(arg1 instanceof Double || arg1 instanceof Float ||
           arg2 instanceof Double || arg2 instanceof Float)
        {
            double a = ((Number) arg1).doubleValue();
            double b = ((Number) arg2).doubleValue();
            if(arg1 instanceof Double || arg2 instanceof Double)
                return new Double(a-b);
            else
                return new Float(a-b);
        }
        if(arg1 instanceof Rational || arg2 instanceof Rational)
        {
            Rational a = Rational.coerceRational(arg1);
            Rational b = Rational.coerceRational(arg2);
            return a.subtract(b);
        }
        if(arg1 instanceof Integer || arg1 instanceof Long ||
           arg2 instanceof Integer || arg2 instanceof Long)
        {
            long a = ((Number) arg1).longValue();
            long b = ((Number) arg2).longValue();
            return new Long(a-b);
        }
        throw new SchemeException("Cannot subtract " +
            arg1 + " and " + arg2);
    }
    public static boolean greaterThan(Number arg1, Number arg2)
    {
        if(arg1 instanceof Integer && arg2 instanceof Integer)
        {
            Integer a = (Integer) arg1;
            Integer b = (Integer) arg2;
            return a.intValue() > b.intValue();
        }
        if(arg1 instanceof Float && arg2 instanceof Float)
        {
            Float a = (Float) arg1;
            Float b = (Float) arg2;
            return a.floatValue() > b.floatValue();
        }
        if(arg1 instanceof Complex || arg2 instanceof Complex)
        {
            Complex a = Complex.coerceComplex(arg1);
            Complex b = Complex.coerceComplex(arg2);
            return a.greaterThan(b);
        }
        if(arg1 instanceof Double || arg1 instanceof Float ||
           arg2 instanceof Double || arg2 instanceof Float)
        {
            double a = ((Number) arg1).doubleValue();
            double b = ((Number) arg2).doubleValue();
            return a > b;
        }
        if(arg1 instanceof Rational || arg2 instanceof Rational)
        {
            Rational a = Rational.coerceRational(arg1);
            Rational b = Rational.coerceRational(arg2);
            return a.greaterThan(b);
        }
        if(arg1 instanceof Integer || arg1 instanceof Long ||
           arg2 instanceof Integer || arg2 instanceof Long)
        {
            long a = ((Number) arg1).longValue();
            long b = ((Number) arg2).longValue();
            return a > b;
        }
        throw new SchemeException("Cannot compare " +
            arg1 + " and " + arg2);
    }

    public static boolean lessThan(Number arg1, Number arg2)
    {
        if(arg1 instanceof Integer && arg2 instanceof Integer)
        {
            Integer a = (Integer) arg1;
            Integer b = (Integer) arg2;
            return a.intValue() < b.intValue();
        }
        if(arg1 instanceof Float && arg2 instanceof Float)
        {
            Float a = (Float) arg1;
            Float b = (Float) arg2;
            return a.floatValue() < b.floatValue();
        }
        if(arg1 instanceof Complex || arg2 instanceof Complex)
        {
            Complex a = Complex.coerceComplex(arg1);
            Complex b = Complex.coerceComplex(arg2);
            return a.lessThan(b);
        }
        if(arg1 instanceof Double || arg1 instanceof Float ||
           arg2 instanceof Double || arg2 instanceof Float)
        {
            double a = ((Number) arg1).doubleValue();
            double b = ((Number) arg2).doubleValue();
            return a < b;
        }
        if(arg1 instanceof Rational || arg2 instanceof Rational)
        {
            Rational a = Rational.coerceRational(arg1);
            Rational b = Rational.coerceRational(arg2);
            return a.lessThan(b);
        }
        if(arg1 instanceof Integer || arg1 instanceof Long ||
           arg2 instanceof Integer || arg2 instanceof Long)
        {
            long a = ((Number) arg1).longValue();
            long b = ((Number) arg2).longValue();
            return a < b;
        }
        throw new SchemeException("Cannot compare " +
            arg1 + " and " + arg2);
    }

    public static boolean equals(Number arg1, Number arg2)
    {
        if(arg1 instanceof Integer && arg2 instanceof Integer)
        {
            Integer a = (Integer) arg1;
            Integer b = (Integer) arg2;
            return a.intValue() == b.intValue();
        }
        if(arg1 instanceof Float && arg2 instanceof Float)
        {
            Float a = (Float) arg1;
            Float b = (Float) arg2;
            return a.floatValue() == b.floatValue();
        }
        if(arg1 instanceof Complex || arg2 instanceof Complex)
        {
            Complex a = Complex.coerceComplex(arg1);
            Complex b = Complex.coerceComplex(arg2);
            return a.equals(b);
        }
        if(arg1 instanceof Double || arg1 instanceof Float ||
           arg2 instanceof Double || arg2 instanceof Float)
        {
            double a = ((Number) arg1).doubleValue();
            double b = ((Number) arg2).doubleValue();
            return a == b;
        }
        if(arg1 instanceof Rational || arg2 instanceof Rational)
        {
            Rational a = Rational.coerceRational(arg1);
            Rational b = Rational.coerceRational(arg2);
            return a.equals(b);
        }
        if(arg1 instanceof Integer || arg1 instanceof Long ||
           arg2 instanceof Integer || arg2 instanceof Long)
        {
            long a = ((Number) arg1).longValue();
            long b = ((Number) arg2).longValue();
            return a == b;
        }
        throw new SchemeException("Cannot compare " +
            arg1 + " and " + arg2);
    }
}