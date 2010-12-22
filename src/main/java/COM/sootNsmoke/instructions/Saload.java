package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Saload  extends  NoArgsSequence
{
    public Saload ()
    {
        super(0, -1, opc_saload);
    }
}
