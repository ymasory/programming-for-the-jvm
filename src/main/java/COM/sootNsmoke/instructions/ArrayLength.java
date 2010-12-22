package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class ArrayLength  extends  NoArgsSequence
{
    public ArrayLength ()
    {
        super(0, 0, opc_arraylength);
    }
}
