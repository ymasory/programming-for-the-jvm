package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lneg extends NoArgsSequence
{
    public Lneg()
    {
        super(0, 0, opc_lneg);
    }
}
