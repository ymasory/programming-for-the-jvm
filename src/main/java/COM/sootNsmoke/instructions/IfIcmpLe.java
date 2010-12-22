package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class IfIcmpLe  extends  LabelSequence
{
    public IfIcmpLe (String label)
    {
        super(0, -2, opc_if_icmple, label);
    }
}
