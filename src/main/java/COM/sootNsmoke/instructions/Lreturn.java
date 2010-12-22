package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lreturn extends NoArgsSequence
{
    public Lreturn()
    {
        super(0, -2, opc_lreturn);
    }
}
