package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Caload extends NoArgsSequence
{
    public Caload()
    {
        super(0, -1, opc_caload);
    }
}
