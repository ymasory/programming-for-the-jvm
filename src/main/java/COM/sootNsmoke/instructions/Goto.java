package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Goto extends LabelSequence
{
    public Goto(String cont)
    {
        super(0, 0, opc_goto, cont);
    }
}

