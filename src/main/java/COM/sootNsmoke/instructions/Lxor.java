package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Lxor extends NoArgsSequence
{
    public Lxor()
    {
        super(0, -2, opc_lxor);
    }
}
