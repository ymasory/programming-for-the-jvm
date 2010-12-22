package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

/** An abstraction if the Wide opcode.  Takes three arguments:
 * an opcode, and index, and an optional constant.  The opcode
 * must be a load instruction {i,a,l,f,d}, a store instruction
 * {i,a,l,f,d}, a ret instruction, or iinc.  The index is the
 * index argument to the opcode.  The constant is permitted only
 * for the iinc opcode.
 */
public class Wide extends Sequence
{
    int opcode;
    int index;
    int constant;

    public Wide(int opcode, int index)
    {
        super(opcode == opc_ret ? 0 :1,
              opcode == opc_ret ? 0 :1);
        this.index = index;
    }

    public Wide(int iinc_opcode, int index, int constant)
    {
        super(0, 0);
        this.index = index;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.write((byte) opcode);
        bytecodes.write(highbyte((short) index));
        bytecodes.write(lowbyte((short) index));
        if(opcode == opc_iinc)
        {
            bytecodes.write(highbyte((short) constant));
            bytecodes.write(lowbyte((short) constant));
        }
    }
}
