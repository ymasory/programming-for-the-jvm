package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Iconst  extends NoArgsSequence
{
    int n;

    public Iconst (int n)
    {
        super(1, 1,
                n == -1 ? opc_iconst_m1 :
                n == 0  ? opc_iconst_0 :
                n == 1  ? opc_iconst_1 :
                n == 2  ? opc_iconst_2 :
                n == 3  ? opc_iconst_3 :
                n == 4  ? opc_iconst_4 :
                          opc_iconst_5);
    }
}
