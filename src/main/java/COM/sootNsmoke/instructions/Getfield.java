package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Getfield  extends  FieldSequence
{
    public Getfield (String classname, String field_name,
                         String signature)
    {
        super(1, 1, opc_getfield, classname, field_name, signature);
    }
}
