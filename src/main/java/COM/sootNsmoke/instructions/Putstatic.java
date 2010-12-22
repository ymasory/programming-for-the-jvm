package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Putstatic extends FieldSequence
{
    public Putstatic(String classname, String field_name,
                         String signature)
    {
        super(0, fieldSize(signature), opc_putstatic,
            classname, field_name, signature);
    }
}
