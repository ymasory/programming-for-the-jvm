package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Tableswitch extends Sequence
{
    /** The lowest number in the calculation */
    private int low;
    private String dflt;
    private String[] offsets;

    public Tableswitch(int low, String dflt, String[] offsets)
    {
        super(0, -1);
        this.low = low;
        this.dflt = dflt;
        this.offsets = offsets;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer("tableswitch " + low + "\n");
        for(int i = 0; i < offsets.length; i++)
            sb.append("    " + offsets[i] + "\n");
        sb.append("default: " + dflt);
        return sb.toString();
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int source_offset = 1;  // Keeps track of the relative location of the
                                // opcode
        byte[] four_zeros = { (byte) 0, (byte) 0, (byte) 0, (byte) 0 };
        bytecodes.write((byte) opc_tableswitch);
        switch(bytecodes.length() % 4)
        {
            case 1: bytecodes.write((byte) 0); source_offset++;
            case 2: bytecodes.write((byte) 0); source_offset++;
            case 3: bytecodes.write((byte) 0); source_offset++;
        }
        bytecodes.addWideAddressSlotHere(dflt, source_offset);
        bytecodes.write(four_zeros);

        bytecodes.writeInt(low);
        bytecodes.writeInt(low + offsets.length - 1);

        source_offset += 12;

        for(int i = 0; i < offsets.length; i++)
        {
            bytecodes.addWideAddressSlotHere(offsets[i], source_offset);
            bytecodes.write(four_zeros);
            source_offset += 4;
        }
    }
}
