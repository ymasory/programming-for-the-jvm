package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Irem extends NoArgsSequence
{
    public Irem()
    {
        super(0, -1, opc_irem);
    }
}
