package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class CheckCast extends Sequence
{
    String classname;

    public CheckCast(String classname)
    {
        super(1, 1);
        this.classname = classname;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int cp_class = bytecodes.cf.addConstantClass(classname);
        byte[] bytearray = { (byte) opc_checkcast,
                                    highbyte(cp_class),
                                    lowbyte(cp_class) };
        bytecodes.write(bytearray);
    }

    public String toString()
    {
        return "checkcast " + classname;
    }
}
