package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dup2 extends  NoArgsSequence
{
    public Dup2()
    {
        super(2, 2, opc_dup2);
    }
}
