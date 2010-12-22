package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ifne  extends  LabelSequence
{
    public Ifne (String label)
    {
        super(0, -1, opc_ifne, label);
    }
}
