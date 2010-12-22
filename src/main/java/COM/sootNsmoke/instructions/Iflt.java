package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Iflt  extends  LabelSequence
{
    public Iflt (String label)
    {
        super(0, -1, opc_iflt, label);
    }
}
