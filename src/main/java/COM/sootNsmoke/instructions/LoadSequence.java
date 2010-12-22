package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;
import COM.sootNsmoke.oolong.*;


/** Handles the Handles the case of the Xload instructions
 * Create with the _0 opcode (e.g. aload_0) and the number
 * of the variable to load.
 */
public class LoadSequence extends Sequence
{
    int opcode;
    int n;
    public LoadSequence(int max_stack, int net_stack, 
			int nvars,
                        int opcode, int n)
    {
        super(max_stack, net_stack, nvars);
        this.opcode = opcode;
        this.n = n;
    }


    public void toBytecodes(Bytecodes bytecodes)
    {
        if(n <= 3)
            bytecodes.write(opcode+n);
        else
        {
            bytecodes.write((byte) opcode);
            bytecodes.write((byte) n);
        }
    }

    public String toString()
    {
        if(n <= 3)
            return Disassembler.ops[opcode+n].mnemonic;
        else
            return Disassembler.ops[opcode].mnemonic + " " + n;
    }
}
