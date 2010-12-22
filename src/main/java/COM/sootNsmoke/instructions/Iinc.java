package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Iinc  extends  Sequence
{
    int index;
    int inc;

    public Iinc (int index, int inc)
    {
        super(0, 0, index);
        this.index = index;
        this.inc = inc;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opc_iinc);
        bytecodes.write((byte) index);
        bytecodes.write((byte) inc);
    }
}
