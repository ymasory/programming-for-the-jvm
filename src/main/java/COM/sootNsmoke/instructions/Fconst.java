package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Fconst  extends  NoArgsSequence
{
    int n;

    public Fconst (int n)
    {
        super(0, 1, n == 0 ? opc_fconst_0 :
                    n == 1 ? opc_fconst_1 :
                             opc_fconst_2);
    }
}
