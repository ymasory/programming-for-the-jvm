package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

/** Load float from local variable */
public class Fload  extends  LoadSequence
{
    public Fload (int n)
    {
        super(1, 1, n, opc_fload_0, n);
    }
}
