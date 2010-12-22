package COM.sootNsmoke.oolong;
import java.io.*;

/** A hex-dump program vaguely similar to Unix od.
 *
 * @author Joshua Engel
 */
public class hd
{
    /** Arguments to this class are a list of file names. Produces
     * a hex dump to the standard output
     */
    public static void main(String[] a)
    {
        // Parse args
        int next_arg = 0;

        for(int i = next_arg; i < a.length; i++)
        {
            try
            {
                InputStream is = new FileInputStream(a[i]);
                dump(System.out, is);
            }
            catch(Exception e)
            {
                System.err.println(e.getMessage());
            }
            System.out.flush();
        }
    }

    /** Reads the bytes in the input stream and produces a hex
     * dump on the output stream
     */
    public static void dump(OutputStream os, InputStream is) throws IOException
    {
        int b;
        int i = 0;
        int line_length = 0;
        char c[] = new char[16];
        while((b = is.read()) != -1)
        {
            if(line_length == 0)
            {
                System.out.print(toHex(i, 8));
                System.out.print(" ");
            }
            System.out.print(toHex(b, 2));
            c[line_length] = (char) b;
            if(b < 32 || b > 127)
                c[line_length] = '.';
            i++;
            line_length++;
            if(i % 2 == 0)
                System.out.print(" ");
            if(line_length == 16)
            {
                line_length = 0;
                System.out.print(new String(c));
                System.out.println();
            }
        }
        // Take care of characters on the last line
        for(int j = 0; j < 16 - line_length; j++)
        {
            System.out.print("  ");
            if(j % 2 == 0)
                System.out.print(" ");
        }
        for(int j = 0; j < line_length; j++)
            System.out.print(c[j]);
        System.out.println();
    }

    /** Converts the integer n into zero-leading string length chars long.
     * If it doesn't fit, you get the low bits. */
    public static String toHex(int n, int length)
    {
        char[] b = new char[length];
        for(int i = length-1; i >= 0; i--)
        {
            b[i] = Character.forDigit(n & 0xF, 16);
            n = n >> 4;
        }
        return new String(b);
    }

}