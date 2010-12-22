package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Castore extends NoArgsSequence
{
    public Castore()
    {
        super(0, -3, opc_castore);
    }
}
