package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lastore extends NoArgsSequence
{
    public Lastore()
    {
        super(0, -4, opc_lastore);
    }
}
