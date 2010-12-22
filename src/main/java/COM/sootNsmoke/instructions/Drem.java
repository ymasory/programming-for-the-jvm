package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Drem extends NoArgsSequence
{
    public Drem()
    {
        super(0, -2, opc_drem);
    }
}
