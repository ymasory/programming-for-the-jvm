package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Return  extends  NoArgsSequence
{
    public Return ()
    {
        super(0, 0, opc_return);
    }
}
