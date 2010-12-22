package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ireturn extends NoArgsSequence
{
    public Ireturn()
    {
        super(0, -1, opc_ireturn);
    }
}
