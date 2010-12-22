package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Iadd extends NoArgsSequence
{
    public Iadd()
    {
        super(0, -1, opc_iadd);
    }
}
