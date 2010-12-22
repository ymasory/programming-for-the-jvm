package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Fstore  extends  LoadSequence
{
    public Fstore (int n)
    {
        super(0, -1, n, opc_fstore_0, n);
    }
}
