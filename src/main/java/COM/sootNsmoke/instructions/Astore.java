package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Astore  extends  LoadSequence
{
    public Astore (int n)
    {
        super(0, -1, n,
            n < 4 ? opc_astore_0 : opc_astore, n);
    }
}
