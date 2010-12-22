package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Imul extends NoArgsSequence
{
    public Imul()
    {
        super(0, -1, opc_imul);
    }
}
