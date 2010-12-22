package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class InvokeInterface  extends  Sequence
{
    /** Number of arguments */
    int n;
    String classname;
    String func_name;
    String signature;
    public InvokeInterface (String classname, String func_name, String signature,
        int n)
    {
        super(1, -n);
        this.classname = classname;
        this.func_name = func_name;
        this.signature = signature;
        this.n = n;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        int cp_methodref =
            bytecodes.cf.addConstantInterfaceMethodref(
                classname, func_name, signature);
        byte[] bytearray = { (byte) opc_invokeinterface,
                                    highbyte(cp_methodref),
                                    lowbyte(cp_methodref),
                             (byte) (n),
                             (byte) 0 };
        bytecodes.write(bytearray);
    }
}
