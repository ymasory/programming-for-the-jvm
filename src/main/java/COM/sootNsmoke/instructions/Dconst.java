package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dconst  extends  NoArgsSequence
{
    public Dconst (int n)
    {
        super(0, 2, n == 0 ? opc_dconst_0 : opc_dconst_1);
    }
}
