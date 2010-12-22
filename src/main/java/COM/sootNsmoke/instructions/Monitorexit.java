package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Monitorexit  extends  NoArgsSequence
{
    public Monitorexit () { super(0, -1, opc_monitorexit); }
}
