package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class IfIcmpLt  extends  LabelSequence
{
    public IfIcmpLt (String label)
    {
        super(0, -2, opc_if_icmplt, label);
    }
}
