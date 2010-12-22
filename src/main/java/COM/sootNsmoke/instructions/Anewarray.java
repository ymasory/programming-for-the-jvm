package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Anewarray extends Sequence
{
    String classname;

    public Anewarray(String classname)
    {
        super(0, 0);
        this.classname = classname;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int cp_class = bytecodes.cf.addConstantClass(classname);
        bytecodes.write((byte) opc_anewarray);
        bytecodes.write(highbyte(cp_class));
        bytecodes.write(lowbyte(cp_class));
    }

    public String toString()
    {
        return "anewarray " + classname;
    }
}
