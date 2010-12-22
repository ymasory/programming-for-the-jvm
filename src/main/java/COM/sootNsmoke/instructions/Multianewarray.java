package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Multianewarray  extends  Sequence
{
    String classname;
    int dims;

    public Multianewarray (String classname, int n)
    {
        super(1, 1-n);
        this.classname = classname;
        dims = n;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int cp_class = bytecodes.cf.addConstantClass(classname);
        byte[] bytearray = {(byte) opc_multianewarray,
                                   highbyte(cp_class),
                                   lowbyte(cp_class),
                            (byte) dims };
        bytecodes.write(bytearray);
    }
}
