package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class IfAcmpNe  extends  LabelSequence
{
    public IfAcmpNe (String label)
    {
        super(0, -1, opc_if_acmpne, label);
    }
}
