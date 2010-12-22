package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class IfIcmpNe  extends  LabelSequence
{
    public IfIcmpNe (String label)
    {
        super(0, -2, opc_if_icmpne, label);
    }
}
