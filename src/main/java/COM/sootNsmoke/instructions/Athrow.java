package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Athrow  extends  NoArgsSequence
{
    public Athrow ()
    {
        super(0, -1, opc_athrow);
    }
}
