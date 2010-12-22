package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ifgt  extends  LabelSequence
{
    public Ifgt (String label)
    {
        super(0, -1, opc_ifgt, label);
    }
}
