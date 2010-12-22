package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Jsr_w extends Sequence
{
    String cont;
    public Jsr_w(String cont)
    {
        super(0, -1);
        this.cont = cont;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opc_jsr_w);
        bytecodes.addAddressSlotHere(cont);
        // Reserve four bytes for the address
        bytecodes.write((byte) 0);
        bytecodes.write((byte) 0);
        bytecodes.write((byte) 0);
        bytecodes.write((byte) 0);
    }
}

