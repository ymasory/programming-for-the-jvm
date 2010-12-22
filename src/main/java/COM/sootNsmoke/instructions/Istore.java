package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Istore  extends LoadSequence
{
    public Istore (int n)
    {
        super(0, -1, n,
            n < 4 ? opc_istore_0 : opc_istore, n);
    }
}

