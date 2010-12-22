package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Aload extends LoadSequence
{
    public Aload(int n)
    {
        super(1, 1, n,
              n < 4 ? opc_aload_0 : opc_aload, n);
    }
}
