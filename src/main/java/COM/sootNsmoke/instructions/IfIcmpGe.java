package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class IfIcmpGe  extends  LabelSequence
{
    public IfIcmpGe (String label)
    {
        super(0, -2, opc_if_icmpge, label);
    }
}
