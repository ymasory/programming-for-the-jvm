package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dadd extends NoArgsSequence
{
    public Dadd()
    {
        super(0, -2, opc_dadd);
    }
}
