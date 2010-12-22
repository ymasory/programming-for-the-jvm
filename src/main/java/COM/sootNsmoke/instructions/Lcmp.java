package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lcmp extends NoArgsSequence
{
    public Lcmp()
    {
        super(0, -3, opc_lcmp);
    }
}
