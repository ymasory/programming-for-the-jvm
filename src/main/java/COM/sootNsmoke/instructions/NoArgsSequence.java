package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;


/** Handles the simple cases, where the instruction
 * takes no arguments
 */
public class NoArgsSequence extends Sequence
{
    int opcode;
    public NoArgsSequence(int a, int b, int opcode)
    {
        super(a, b);
        this.opcode = opcode;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opcode);
    }

    public String toString()
    {
        return COM.sootNsmoke.oolong.Disassembler.ops[opcode].mnemonic;
    }
}
