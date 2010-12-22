package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Swap  extends  NoArgsSequence
{
    public Swap ()
    {
        super(0, 0, opc_swap);
    }
}
