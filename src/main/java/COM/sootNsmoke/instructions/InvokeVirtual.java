package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class InvokeVirtual  extends Invoke
{
    public InvokeVirtual (String class_name,
                             String func_name, String signature)
    {
        super(class_name, func_name, signature,
            countArgs(signature),
            opc_invokevirtual);
    }

    public InvokeVirtual (String class_name,
                             String func_name, String signature,
                             int num_args)
    {
        super(class_name, func_name, signature, num_args,
            opc_invokevirtual);
    }
}
