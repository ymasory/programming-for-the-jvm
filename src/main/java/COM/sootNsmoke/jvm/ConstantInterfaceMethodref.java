package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantInterfaceMethodref extends ConstantRef {
    public ConstantInterfaceMethodref(int class_index, int name_and_type_index)
    { super(class_index, name_and_type_index); }

    public byte tag()
    {
        return (byte) CONSTANT_INTERFACEMETHOD;
    }

    public String toString()
    {
        return "InterfaceMethodref class #" + class_index +
               " name-and-type #" + name_and_type_index;
    }


}

