package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ifnonnull  extends  LabelSequence
{
    public Ifnonnull (String label)
    {
        super(0, -1, opc_ifnonnull, label);
    }
}
