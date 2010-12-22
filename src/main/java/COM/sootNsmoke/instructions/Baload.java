package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Baload extends NoArgsSequence
{
    public Baload()
    {
        super(0, -1, opc_baload);
    }
}
