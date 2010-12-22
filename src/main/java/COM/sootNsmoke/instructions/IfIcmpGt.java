package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class IfIcmpGt  extends  LabelSequence
{
    public IfIcmpGt (String label)
    {
        super(0, -2, opc_if_icmpgt, label);
    }
}
