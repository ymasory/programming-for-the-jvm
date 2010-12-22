package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class StoreArray  extends  Sequence
{
    StoreArray ()
    {
        super(0, -3);
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opc_aastore);
    }
}
