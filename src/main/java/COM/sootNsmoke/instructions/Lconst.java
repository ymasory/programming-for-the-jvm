package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lconst  extends  NoArgsSequence
{
    int n;

    public Lconst (int n)
    {
        super(0, 2, n == 0 ? opc_lconst_0 : opc_lconst_1);
    }
}
