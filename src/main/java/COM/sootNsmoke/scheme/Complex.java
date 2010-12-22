package COM.sootNsmoke.scheme;

/** An implementation of complex numbers for Scheme.
 * Real and imag may be any of (Integer, Long, Double, Float,
 * or Rational).
 *
 * This class also contains a number of static routines
 * which do things I'd rather have as methods on Integer,
 * Float, etc. (such as positive and add).
 */
public class Complex extends Number
{
    private Number real;
    private Number imag;

    /** Constructs a complex number given the real and imaginary
     * parts as real numbers.
     * real and imag should not be Complex themselves.
     * If Java's type system supported it, I'd have a superclass
     * Real for Float and Double, and Rational for Long and
     * Integer.
     */
    public Complex(Number real, Number imag)
    {
        this.real = real;
        this.imag = imag;
    }

    public Number realPart() { return real; }
    public Number imagPart() { return imag; }
    public double magnitude()
    {
        double x = real.doubleValue();
        double y = imag.doubleValue();
        return  Math.sqrt(x*x+y*y);
    }
    public double angle()
    {
        double x = real.doubleValue();
        double y = imag.doubleValue();
        return  Math.atan(y/x);
    }

    public String toString()
    {
        if(!Numbers.negative(imag))
            return real + "+" + imag + "i";
        else
            return real.toString() + imag + "i";
    }


    /** Returns the int value of the real part */
    public int intValue() { return real.intValue(); }
    public float floatValue() { return real.floatValue(); }
    public long longValue() { return real.longValue(); }
    public double doubleValue() { return real.doubleValue(); }

    public static Complex coerceComplex(Object n)
    {
        if(n instanceof Complex)
            return (Complex) n;
        if(n instanceof Number)
            return new Complex((Number) n, new Integer(0));
        throw new SchemeException("Can't make a compex number from " + n);
    }

    public Complex add(Complex n)
    {
        return new Complex(Numbers.add(real, n.real),
                           Numbers.add(imag, n.imag));
    }

    public Complex subtract(Complex n)
    {
        return new Complex(Numbers.subtract(real, n.real),
                           Numbers.subtract(imag, n.imag));
    }
    public Complex multiply(Complex b)
    {
        Complex a = this;
        return new Complex(Numbers.subtract(
                            Numbers.multiply(a.real, b.real),
                            Numbers.multiply(a.imag, b.imag)),
                           Numbers.add(
                            Numbers.multiply(a.real, b.imag),
                            Numbers.multiply(b.real, a.imag)));
    }


    public Complex divide(Complex b)
    {
        // c+di             e+fi
        // ---- = (c+di) * -------
        // e+fi            e^2+f^2
        Number e = b.real;
        Number f = b.imag;
        Number base = Numbers.add(
            Numbers.multiply(e,e),
            Numbers.multiply(f,f));
        Complex b_1 =
            new Complex(Numbers.divide(e, base),
                        Numbers.multiply(
                            new Integer(-1),
                            Numbers.divide(f, base)));
        return multiply(b_1);
    }

    public Complex sqrt()
    {
        double theta = .5*angle();
        double r = Math.sqrt(magnitude());
        double a = r * Math.cos(theta);
        double b = r * Math.sin(theta);
        return new Complex(new Double(a),
                           new Double(b));
    }

    public boolean equals(Object o)
    {
        if(o instanceof Number)
        {
            Complex c = coerceComplex(o);
            return Numbers.equals(imag, c.imag) &&
                   Numbers.equals(real, c.real);
        }
        else
            return false;
    }
    public boolean greaterThan(Complex b)
    {
        return magnitude() > b.magnitude();
    }
    public boolean lessThan(Complex b)
    {
        return magnitude() < b.magnitude();
    }
}