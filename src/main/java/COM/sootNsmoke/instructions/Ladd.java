package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ladd extends NoArgsSequence
{
    public Ladd()
    {
        super(0, -2, opc_ladd);
    }
}
