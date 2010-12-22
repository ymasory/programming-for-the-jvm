package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ifge  extends  LabelSequence
{
    public Ifge (String label)
    {
        super(0, -1, opc_ifge, label);
    }
}
