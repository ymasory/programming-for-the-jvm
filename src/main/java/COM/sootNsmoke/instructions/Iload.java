package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Iload  extends  LoadSequence
{
    public Iload (int n)
    {
        super(1, 1, n, n < 4 ? opc_iload_0 : opc_iload, n);
    }
}
