package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Dup  extends  NoArgsSequence
{
    public Dup ()
    {
        super(1, 1, opc_dup);
    }
}
