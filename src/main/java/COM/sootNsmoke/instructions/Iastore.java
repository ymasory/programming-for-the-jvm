package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Iastore  extends  NoArgsSequence
{
    public Iastore ()
    {
        super(0, -3, opc_iastore);
    }
}
