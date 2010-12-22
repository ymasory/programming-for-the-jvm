package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Fsub extends Sequence
{
    public Fsub()
    {
        super(0, -1);
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opc_fsub);
    }
}
