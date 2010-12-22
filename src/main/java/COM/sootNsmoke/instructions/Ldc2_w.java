package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ldc2_w  extends  Sequence
{
    private boolean isDouble;
    private double d;
    private long l;

    public Ldc2_w (double d)
    {
        super(2, 2);
        this.d = d;
        isDouble = true;
    }
    public Ldc2_w (long l)
    {
        super(2, 2);
        this.l = l;
        isDouble = false;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int ref = 0;
        if(isDouble)
            ref = bytecodes.cf.addConstant(d);
        else
            ref = bytecodes.cf.addConstant(l);
        byte[] bytearray = { (byte) opc_ldc2_w,
                                    highbyte(ref),
                                    lowbyte(ref) };
        bytecodes.write(bytearray);
    }

    public String toString()
    {
        if(isDouble)
            return "ldc2_w " + d;
        else
            return "ldc2_w " + l;
    }
}
