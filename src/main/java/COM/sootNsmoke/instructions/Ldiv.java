package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Ldiv extends NoArgsSequence
{
    public Ldiv()
    {
        super(0, -2, opc_ldiv);
    }
}
