package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

/** Load double from local variable */
public class Dload  extends LoadSequence
{
    public Dload (int n)
    {
        super(2, 2, n+1,
           n < 4 ? opc_dload_0 : opc_dload, n);
    }
}
