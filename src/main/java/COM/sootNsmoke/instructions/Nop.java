package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Nop  extends  NoArgsSequence
{
    public Nop ()
    {
        super(0, 0, opc_nop);
    }
}
