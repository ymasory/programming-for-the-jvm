package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ifnull  extends  LabelSequence
{
    public Ifnull (String label)
    {
        super(0, -1, opc_ifnull, label);
    }
}
