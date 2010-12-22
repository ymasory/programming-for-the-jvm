package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class InvokeSpecial  extends Invoke
{
    public InvokeSpecial (String class_name,
                                 String func_name, String signature)
    {
        super(class_name, func_name, signature,
            Invoke .countArgs(signature),
            opc_invokespecial);
    }

    public InvokeSpecial (String class_name, String func_name,
                             String signature,
                             int num_args)
    {
        super(class_name, func_name, signature, num_args,
            opc_invokespecial);
    }
}
