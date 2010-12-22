package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class IfAcmpEq  extends LabelSequence
{
    public IfAcmpEq (String label)
    {
        super(0, -1, opc_if_acmpeq, label);
    }
}
