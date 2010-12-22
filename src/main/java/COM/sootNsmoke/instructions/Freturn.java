package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Freturn extends NoArgsSequence
{
    public Freturn()
    {
        super(0, -1, opc_freturn);
    }
}
