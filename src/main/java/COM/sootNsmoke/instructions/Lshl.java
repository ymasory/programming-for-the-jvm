package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lshl extends NoArgsSequence
{
    public Lshl()
    {
        super(0, -1, opc_lshl);
    }
}
