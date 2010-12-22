package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class IfIcmpEq  extends  LabelSequence
{
    public IfIcmpEq (String label)
    {
        super(0, -2, opc_if_icmpeq, label);
    }
}
