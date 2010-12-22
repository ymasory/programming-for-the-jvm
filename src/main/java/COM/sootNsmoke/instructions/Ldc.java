package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ldc  extends  Sequence
{
    private boolean isInt;
    private String s = null;
    private int i;
    private float f;

    public Ldc (String s)
    {
        super(1, 1);
        this.s = s;
    }
    public Ldc (int i)
    {
        super(1, 1);
        this.i = i;
        isInt = true;
    }
    public Ldc (float f)
    {
        super(1, 1);
        this.f = f;
    }
    public void toBytecodes(Bytecodes bytecodes)
    {
        int ref = 0;
        if(s != null)
            ref = bytecodes.cf.addConstantString(s);
        else if(isInt)
            ref = bytecodes.cf.addConstant(i);
        else
            ref = bytecodes.cf.addConstant(f);
        if(ref < 0x100)
        {
            bytecodes.write((byte) opc_ldc);
            bytecodes.write(lowbyte((short) ref));
        }
        else
        {
            bytecodes.write((byte) opc_ldc_w);
            bytecodes.write(highbyte((short) ref));
            bytecodes.write(lowbyte((short) ref));
        }
    }

    public String toString()
    {
        if(s != null)
            return "ldc \"" + s + "\"";
        else if(isInt)
            return "ldc " + i;
        else
            return "ldc " + f;
    }
}
