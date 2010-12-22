package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Iand extends NoArgsSequence
{
    public Iand()
    {
        super(0, -1, opc_iand);
    }
}
