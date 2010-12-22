package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Aastore extends NoArgsSequence
{
    public Aastore()
    {
        super(0, -3, opc_aastore);
    }
}
