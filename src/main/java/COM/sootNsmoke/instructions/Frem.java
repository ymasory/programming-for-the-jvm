package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Frem extends NoArgsSequence
{
    public Frem()
    {
        super(0, -1, opc_frem);
    }
}
