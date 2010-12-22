package COM.sootNsmoke.scheme;

/** Represents a rational number */
public class Rational extends Number
{
    long numerator;
    long denominator;

    public static void main(String a[])
    {
        System.out.println("gcd 17, 22 = " + gcd(17, 22));
        System.out.println("gcd 18, 27 = " + gcd(18, 27));
        System.out.println("gcd 360, 48 = " + gcd(360, 48));
        System.out.println("gcd 17, 22 = " + gcd(22, 17));
        System.out.println("gcd 18, 27 = " + gcd(27, 18));
        System.out.println("gcd 360, 48 = " + gcd(48, 360));
    }

    public Rational(long numerator, long denominator)
    {
        if(denominator < 0)
        {
            numerator *= -1;
            denominator *= -1;
        }
        long gcd = gcd(numerator, denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    public long numerator() { return numerator; }
    public long denominator() { return denominator; }

    public int intValue() { return (int) (numerator / denominator); }
    public float floatValue() { return (float) ((double) numerator / denominator); }
    public long longValue() { return (long) (numerator / denominator); }
    public double doubleValue() { return (double) ((double) numerator / denominator); }

    public String toString()
    {
        if(denominator == 1)
            return Long.toString(numerator);
        return Long.toString(numerator) + "/" +
            Long.toString(denominator);
    }

    /** Coerce n to be a rational number, if possible */
    public static Rational coerceRational(Object n)
    {
        if(n instanceof Rational)
            return (Rational) n;
        if(n instanceof Integer || n instanceof Long)
            return new Rational(((Number) n).longValue(), 1);
        throw new SchemeException(n + " cannot be coerced to a rational");
    }

    /** Create a new rational number which is the sum of
     * this and r
     */
    public Rational add(Rational r)
    {
        long denom = lcm(this.denominator, r.denominator);
        return new Rational(numerator*denom/denominator+
                            r.numerator*denom/r.denominator,
                            denom);
    }

    public Rational subtract(Rational r)
    {
        long denom = lcm(this.denominator, r.denominator);
        return new Rational(numerator*denom/denominator-
                            r.numerator*denom/r.denominator,
                            denom);
    }

    public Rational multiply(Rational r)
    {
        // A smarter implementation will do some reduction
        // before multiplying
        return new Rational(numerator*r.numerator,
                            denominator*r.denominator);
    }
    public Rational divide(Rational r)
    {
        return r.multiply(new Rational(denominator, numerator));
    }

    public Rational sqrt()
    {
        return new Rational((long) Math.sqrt(numerator),
                            (long) Math.sqrt(denominator));
    }

    /** Computes the least common multiple of x and y. */
    public static long lcm(long x, long y)
    {
        return x/gcd(x,y)*y;
    }

    /** Computes the greates common divisor of x and y */
    public static long gcd(long x, long y)
    {
        long r;

        do
        {
            r = x % y;
            x = y;
            y = r;
        }
        while(r != 0);
        return x;
    }

    /** Rationalize returns the simplest rational number
     * differing from x by no more than y.  A rational number
     * r1 us simpler than another rational number r2 if r1=p1/q1
     * and r2=p2/q2 (in lowest terms) and |p1| < |p2| and
     * |q1| < |q2|.  Thus 3/5 is simpler than 4/7.  Although
     * not all rationals are comparable in this ordering
     * (consider 2/7 and 3/5) any interval contains a rational
     * number in that interval (the simpler 2/5 lies between
     * 2/7 and 3/5).
     */
    public static Rational rationalize(double x, double y)
    {
        return new Rational(0, 1);
    }

    public boolean equals(Object o)
    {
        if(o instanceof Number && !(o instanceof Complex))
        {
            Rational r = coerceRational(o);
            return r.numerator == numerator &&
                   r.denominator == denominator;
        }
        else
            return false;
    }
    public boolean greaterThan(Rational r)
    {
        long denom = lcm(this.denominator, r.denominator);
        return numerator*denom/denominator >
             r.numerator*denom/r.denominator;
    }
    public boolean lessThan(Rational r)
    {
        long denom = lcm(this.denominator, r.denominator);
        return numerator*denom/denominator <
             r.numerator*denom/r.denominator;
    }

}