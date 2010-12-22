package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dneg extends NoArgsSequence
{
    public Dneg()
    {
        super(0, 0, opc_dneg);
    }
}
