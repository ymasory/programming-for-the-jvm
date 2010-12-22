package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Iaload  extends  NoArgsSequence
{
    public Iaload ()
    {
        super(0, -1, opc_iaload);
    }
}
