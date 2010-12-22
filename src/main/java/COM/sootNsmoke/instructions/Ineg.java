package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ineg extends NoArgsSequence
{
    public Ineg()
    {
        super(0, 0, opc_ineg);
    }
}
