package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class AconstNull extends NoArgsSequence
{
    public AconstNull()
    {
        super(1, 1, opc_aconst_null);
    }
}
