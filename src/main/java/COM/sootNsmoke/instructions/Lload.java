package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

/** Load double from local variable */
public class Lload  extends  LoadSequence
{
    public Lload (int n)
    {
        super(2, 2, n+1, n < 4 ? opc_lload_0 : opc_lload, n);
    }
}
