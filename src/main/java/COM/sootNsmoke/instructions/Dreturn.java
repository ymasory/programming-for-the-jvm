package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dreturn extends NoArgsSequence
{
    public Dreturn()
    {
        super(0, -2, opc_dreturn);
    }
}
