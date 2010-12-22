package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ifeq  extends  LabelSequence
{
    public Ifeq (String label)
    {
        super(0, -1, opc_ifeq, label);
    }
}
