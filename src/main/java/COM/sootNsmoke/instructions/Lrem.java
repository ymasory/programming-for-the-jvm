package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lrem extends NoArgsSequence
{
    public Lrem()
    {
        super(0, -2, opc_lrem);
    }
}
