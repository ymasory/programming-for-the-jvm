package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Fneg extends NoArgsSequence
{
    public Fneg()
    {
        super(0, 0, opc_fneg);
    }
}
