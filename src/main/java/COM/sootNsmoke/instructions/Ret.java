package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ret  extends  Sequence
{
    int index;
    public Ret (int index)
    {
        super(0, -1, index);
        this.index = index;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opc_ret);
        bytecodes.write((byte) index);
    }
}
