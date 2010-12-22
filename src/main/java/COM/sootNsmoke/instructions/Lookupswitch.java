package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lookupswitch extends Sequence
{
    private String dflt;
    private MatchLabel[] pairs;

    public Lookupswitch(String dflt, MatchLabel[] pairs)
    {
        super(0, -1);
        this.dflt = dflt;
        this.pairs = pairs;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer("lookupswitch\n");
        for(int i = 0; i < pairs.length; i++)
            sb.append("    " + pairs[i].match + ": " + pairs[i].label + "\n");
        sb.append("default: " + dflt);
        return sb.toString();
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int source_offset = 1;  // Keeps track of the relative location of the
                                // opcode

        byte[] four_zeros = { (byte) 0, (byte) 0, (byte) 0, (byte) 0 };
        bytecodes.write((byte) opc_lookupswitch);
        switch(bytecodes.length() % 4)
        {
            case 1: bytecodes.write((byte) 0); source_offset++;
            case 2: bytecodes.write((byte) 0); source_offset++;
            case 3: bytecodes.write((byte) 0); source_offset++;
        }
        bytecodes.addWideAddressSlotHere(dflt, source_offset);
        bytecodes.write(four_zeros);

        bytecodes.writeInt(pairs.length);

        source_offset += 8;

        for(int i = 0; i < pairs.length; i++)
        {
            bytecodes.writeInt(pairs[i].match);
            source_offset += 4;
            bytecodes.addWideAddressSlotHere(pairs[i].label, source_offset);
            bytecodes.write(four_zeros);
            source_offset += 4;
        }
    }
}
