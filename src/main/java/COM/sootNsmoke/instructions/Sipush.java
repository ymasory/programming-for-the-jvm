package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Sipush  extends  Sequence
{
    int val;

    public Sipush (int val)
    {
        super(1, 1);
        this.val = val;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opc_sipush);
        bytecodes.write((byte) (val >> 8));
        bytecodes.write((byte) (val & 0xff));
    }

    public String toString()
    {
        return "sipush " + val;
    }
}
