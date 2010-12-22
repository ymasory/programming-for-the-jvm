package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

/** An instruction which takes a class as an argument */
public class ClassSequence extends Sequence
{
    String classname;
    int opcode;

    public ClassSequence(int max_stack, int net_stack,
        int opcode, String classname)
    {
        super(max_stack, net_stack);
        this.classname = classname;
        this.opcode = opcode;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int cp_class = bytecodes.cf.addConstantClass(classname);
        byte[] bytearray = { (byte) opcode,
                                    highbyte(cp_class),
                                    lowbyte(cp_class) };
        bytecodes.write(bytearray);
    }

    public String toString()
    {
        return COM.sootNsmoke.oolong.Disassembler.ops[opcode].mnemonic
            + " " + classname;
    }
}
