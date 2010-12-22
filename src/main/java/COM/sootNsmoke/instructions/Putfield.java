package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Putfield extends FieldSequence
{
    public Putfield(String classname, String field_name,
                         String signature)
    {
        super(0, fieldSize(signature),
            opc_putfield, classname, field_name, signature);
    }
}
