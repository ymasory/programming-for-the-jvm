package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Goto_w extends Sequence
{
    String cont;
    public Goto_w(String cont)
    {
        super(0, 0);
        this.cont = cont;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opc_goto_w);
        bytecodes.addAddressSlotHere(cont);
        // Reserve two bytes for the address
        bytecodes.write((byte) 0);
        bytecodes.write((byte) 0);
        bytecodes.write((byte) 0);
        bytecodes.write((byte) 0);
    }
}

