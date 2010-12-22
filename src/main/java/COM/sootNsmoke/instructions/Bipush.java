package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Bipush extends Sequence
{
    byte b;

    public Bipush(byte b)
    {
        super(1, 1);
        this.b = b;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opc_bipush);
        bytecodes.write((byte) b);
    }

    public String toString()
    {
        return "bipush " + b;
    }

}
