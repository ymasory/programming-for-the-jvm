package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Sastore  extends  NoArgsSequence
{
    public Sastore ()
    {
        super(0, -3, opc_sastore);
    }
}
