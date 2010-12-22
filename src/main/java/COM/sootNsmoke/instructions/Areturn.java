package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Areturn  extends  NoArgsSequence
{
    public Areturn ()
    {
        super(0, -1, opc_areturn);
    }
}
