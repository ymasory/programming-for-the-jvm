package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantFieldref extends ConstantRef {

    public byte tag()
    {
        return (byte) CONSTANT_FIELD;
    }

    public ConstantFieldref(int class_index, int name_and_type_index)
    { super(class_index, name_and_type_index); }

    public String toString()
    {
        return "Fieldref class #" + class_index +
               " name-and-type #" + name_and_type_index;
    }


}
