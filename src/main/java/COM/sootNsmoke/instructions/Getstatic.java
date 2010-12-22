package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class Getstatic extends FieldSequence
{
    public Getstatic(String classname, String field_name,
                         String signature)
    {
        super(1, 1, opc_getstatic, classname, field_name, signature);
    }
}
