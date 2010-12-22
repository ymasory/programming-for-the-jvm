package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Bastore extends NoArgsSequence
{
    public Bastore()
    {
        super(0, -3, opc_bastore);
    }
}
