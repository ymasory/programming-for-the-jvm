package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Instanceof extends Sequence
{
    String classname;

    public Instanceof(String classname)
    {
        super(1, 1);
        this.classname = classname;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int cp_class = bytecodes.cf.addConstantClass(classname);
        byte[] bytearray = { (byte) opc_instanceof,
                                    highbyte(cp_class),
                                    lowbyte(cp_class) };
        bytecodes.write(bytearray);
    }
}
