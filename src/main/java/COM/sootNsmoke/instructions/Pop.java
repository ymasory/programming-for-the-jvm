package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Pop  extends  NoArgsSequence
{
    public Pop () { super(0, -1, opc_pop); }
}
