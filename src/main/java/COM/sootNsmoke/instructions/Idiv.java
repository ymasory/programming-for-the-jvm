package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Idiv extends NoArgsSequence
{
    public Idiv()
    {
        super(0, -1, opc_idiv);
    }
}
