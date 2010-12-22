package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dup2_x1 extends  NoArgsSequence
{
    public Dup2_x1()
    {
        super(2, 2, opc_dup2_x1);
    }
}
