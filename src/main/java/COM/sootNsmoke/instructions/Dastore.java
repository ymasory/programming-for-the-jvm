package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dastore extends NoArgsSequence
{
    public Dastore()
    {
        super(0, -4, opc_dastore);
    }
}
