package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dstore  extends LoadSequence
{
    public Dstore (int n)
    {
        super(0, -2, n+1,
            n < 4 ? opc_dstore_0 : opc_dstore, n);
    }
}
