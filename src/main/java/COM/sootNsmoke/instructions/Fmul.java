package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Fmul extends NoArgsSequence
{
    public Fmul()
    {
        super(0, -1, opc_fmul);
    }
}
