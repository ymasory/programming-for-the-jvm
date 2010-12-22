package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lstore  extends LoadSequence
{
    public Lstore (int n)
    {
        super(0, -2, n+1, n < 4 ? opc_lstore_0 : opc_lstore, n);
    }
}
