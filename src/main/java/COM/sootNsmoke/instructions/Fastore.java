package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Fastore extends NoArgsSequence
{
    public Fastore()
    {
        super(0, -3, opc_fastore);
    }
}
