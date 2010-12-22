package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dmul extends NoArgsSequence
{
    public Dmul()
    {
        super(0, -2, opc_dmul);
    }
}
