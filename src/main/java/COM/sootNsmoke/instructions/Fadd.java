package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Fadd extends NoArgsSequence
{
    public Fadd()
    {
        super(0, -1, opc_fadd);
    }
}
