package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantMethodref extends ConstantRef {
    public ConstantMethodref(int class_index, int name_and_type_index)
    { super(class_index, name_and_type_index); }

    public byte tag()
    {
        return (byte) CONSTANT_METHOD;
    }
    public String toString()
    {
        return "Methodref class #" + class_index +
               " name-and-type #" + name_and_type_index;
    }


}
