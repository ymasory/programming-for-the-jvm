package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Faload extends NoArgsSequence
{
    public Faload()
    {
        super(0, -1, opc_faload);
    }
}
