package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Jsr extends LabelSequence
{
    public Jsr(String label)
    {
        super(0, -1, opc_jsr, label);
    }
}

