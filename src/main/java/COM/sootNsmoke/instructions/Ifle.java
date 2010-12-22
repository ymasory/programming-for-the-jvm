package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ifle  extends  LabelSequence
{
    public Ifle (String label)
    {
        super(0, -1, opc_ifle, label);
    }
}
