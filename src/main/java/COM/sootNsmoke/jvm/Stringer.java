package COM.sootNsmoke.jvm;

/** Holds string-munging functions */
public class Stringer
{
    /** Pads or trims s to exactly n characters */
    public static String pad(String s, int n)
    {
        char c[] = new char[n];
        int i;
        for(i = 0; i < n && i < s.length(); i++)
            c[i] = s.charAt(i);
        for(; i < n; i++)
            c[i] = ' ';
        return new String(c);
    }

    /** Adds a prefix before each line in str */
    public static String indent(String prefix, String str)
    {
        String ret = new String();
        boolean doit = true;
        for(int i = 0; i < str.length(); i++)
        {
            if(doit)
            {
                ret += prefix;
                doit = false;
            }
            ret += str.charAt(i);
            if(str.charAt(i) == '\n')
                doit = true;

        }
        return ret;
    }

    /** Converts n to an array of 2 bytes, first the high byte then
     * the low byte
     */
    public static byte[] shortToBytes(short n)
    {
        byte[] b = new byte[2];
        b[0] = (byte) ((n >> 8) & 0xff);
        b[1] = (byte) (n & 0xff);
        return b;
    }

    /** Converts an array of n short values to an array of 2*n
     * bytes, alternating high and low bytes from consecutive
     * entries.
     */
    public static byte[] shortToBytes(short[] n)
    {
        byte[] b = new byte[2*n.length];
        for(int i = 0; i < n.length; i++)
        {
            b[2*i+0] = (byte) ((n[i] >> 8) & 0xff);
            b[2*i+1] = (byte) (n[i] & 0xff);
        }
        return b;
    }


    /** Converts n to an array of 4 bytes */
    public static byte[] intToBytes(int n)
    {
        byte[] b = new byte[4];
        b[0] = (byte) ((n >> 24) & 0xff);
        b[1] = (byte) ((n >> 16) & 0xff);
        b[2] = (byte) ((n >> 8) & 0xff);
        b[3] = (byte) (n & 0xff);
        return b;
    }

}




