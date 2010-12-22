package COM.sootNsmoke.oolong;

/** A tuple mnemonic x opcode x instruction format */
public class Instruction
{
    public String mnemonic;
    public InstructionFormat format;
    public int opcode;

    Instruction(String mnemonic, int opcode, InstructionFormat format)
    {
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.format = format;
    }
}