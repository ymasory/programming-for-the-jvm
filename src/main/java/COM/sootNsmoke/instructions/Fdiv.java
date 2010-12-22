package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Fdiv extends NoArgsSequence
{
    public Fdiv()
    {
        super(0, -1, opc_fdiv);
    }
}
