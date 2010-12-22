package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;


/** Handles case of fields; superclass of all instructions
 * which take fields
 */
public class FieldSequence extends Sequence
{
    String classname;
    String fieldname;
    String signature;

    int opcode;
    public FieldSequence(int a, int b,
        int opcode,
        String classname,
        String fieldname,
        String signature)
    {
        super(a, b);
        this.opcode = opcode;
        this.classname = classname;
        this.fieldname = fieldname;
        this.signature = signature;
    }

    public String toString()
    {
        return COM.sootNsmoke.oolong.Disassembler.ops[opcode].mnemonic
            + " " + classname + "/" + fieldname + " " +
            signature;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int ref = bytecodes.cf.addFieldref(classname,
                                     fieldname,
                                     signature);

        byte[] bytearray = { (byte) opcode,
                             highbyte(ref),
                             lowbyte(ref) };
        bytecodes.write(bytearray);
    }

    /** Returns -2 if this is field takes up 2 words (is a
     * double or long), and -1 otherwise
     */
    static int fieldSize(String sig)
    {
        if(sig.equals("J") || sig.equals("D"))
            return -2;
        else
            return -1;
    }
}
