package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Newarray extends Sequence
{
    int atype;

    public Newarray(int n)
    {
        super(1, 1);
        this.atype = n;
    }

    /** Takes a string {boolean, char, float, double, byte,
     * short, int, long } and returns the corresponding atype.
     * If it's none, returns -1.
     */
    public static byte atype(String str)
    {
        if(str.length() == 1)
        {
            switch(str.charAt(0))
            {
                case 'Z':
                    return (byte) T_BOOLEAN;
                case 'C':
                    return (byte) T_CHAR;
                case 'F':
                    return (byte) T_FLOAT;
                case 'D':
                    return (byte) T_DOUBLE;
                case 'B':
                    return (byte) T_BYTE;
                case 'S':
                    return (byte) T_SHORT;
                case 'I':
                    return (byte) T_INT;
                case 'J':
                    return (byte) T_LONG;
                default:
                    return (byte) -1;
            }
        }

        if(str.equals("boolean"))
            return (byte) T_BOOLEAN;
        else if(str.equals("char"))
            return (byte) T_CHAR;
        else if(str.equals("float"))
            return (byte) T_FLOAT;
        else if(str.equals("double"))
            return (byte) T_DOUBLE;
        else if(str.equals("byte"))
            return (byte) T_BYTE;
        else if(str.equals("short"))
            return (byte) T_SHORT;
        else if(str.equals("int"))
            return (byte) T_INT;
        else if(str.equals("long"))
            return (byte) T_LONG;
        try
        {
            int i = Integer.parseInt(str);
            return (byte) i;
        }
        catch(Exception e)
        {
            // Badly formatted number
        }

        return (byte) -1;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opc_newarray);
        bytecodes.write((byte) atype);
    }
}
