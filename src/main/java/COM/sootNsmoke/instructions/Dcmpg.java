package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dcmpg extends NoArgsSequence
{
    public Dcmpg()
    {
        super(0, -3, opc_dcmpg);
    }
}
