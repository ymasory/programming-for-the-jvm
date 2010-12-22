package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;
import COM.sootNsmoke.oolong.*;


/** Handles the Handles the case of the goto and if
 * instructions which take a label as an argument
 * Does not apply to the wide instructions.
 */
public class LabelSequence extends Sequence
{
    int opcode;
    String label;
    public LabelSequence(int a, int b,
                        int opcode, String label)
    {
        super(a, b);
        this.opcode = opcode;
        this.label = label;
    }



    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opcode);
        bytecodes.addAddressSlotHere(label);
        // Reserve two bytes for the address
        bytecodes.write((byte) 0);
        bytecodes.write((byte) 0);
    }

    public String toString()
    {
        return Disassembler.ops[opcode].mnemonic + " " + label;
    }
}
