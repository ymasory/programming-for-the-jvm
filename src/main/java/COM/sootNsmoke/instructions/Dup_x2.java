package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dup_x2 extends  NoArgsSequence
{
    public Dup_x2()
    {
        super(1, 1, opc_dup_x2);
    }
}
