package COM.sootNsmoke.jvm;

import java.io.*;
import java.util.*;


public class InnerClassesAttribute extends Attribute {

    private int inner_class_info_index[];   //  u2 CONSTANT_Class_info index
    private int outer_class_info_index[];   //  u2 CONSTANT_Class_info index
    private int inner_name_index[];         //  u2 CONSTANT_Utf8_info index
    private int inner_class_access_flags[]; //  u2 access_flags bitmask


    public int number_of_classes()
    {
        return inner_class_info_index.length;
    }
    public int inner_class_info_index(int i)
    {
        return inner_class_info_index[i];
    }
    public int outer_class_info_index(int i)
    {
        return outer_class_info_index[i];
    }
    public int inner_name_index(int i)
    {
        return inner_name_index[i];
    }
    public int inner_class_access_flags(int i)
    {
        return inner_class_access_flags[i];
    }


    /**
     * Each parameter is an array; all are expected to have the same length
     * as the first.
     *
     * @param
     * inner_classes - The inner class (reference to Class constant)
     * inner_classes - The outer class (reference to Class constant)
     * inner_name    - The inner class's orignal name (as a UTF8)
     * inner_class_access - The access flags for the inner class
     */
    InnerClassesAttribute(ClassFile cf,
                          int inner_classes[],
                          int outer_classes[],
                          int inner_name[],
                          int inner_class_access[])
    {
        super(cf.lookupConstantString("InnerClasses"), 2+2*inner_classes.length);
        inner_class_info_index = inner_classes;
        outer_class_info_index = outer_classes;
        inner_name_index = inner_name;
        inner_class_access_flags = inner_class_access;
    }

    InnerClassesAttribute(int attribute_name)
    {
        super(attribute_name, 2);
    }


    public String toString()
    {
        return "InnerClass attribute";
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeShort(inner_class_info_index.length);
        for(int i = 0; i < inner_class_info_index.length; i++)
        {
            os.writeShort(inner_class_info_index[i]);
            os.writeShort(outer_class_info_index[i]);
            os.writeShort(inner_name_index[i]);
            os.writeShort(inner_class_access_flags[i]);
        }
    }

    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    attribute_length = data_is.readInt();
	    int number_of_classes = data_is.readShort();
	    inner_class_info_index = new int[number_of_classes];
	    outer_class_info_index = new int[number_of_classes];
	    inner_name_index = new int[number_of_classes];
	    inner_class_access_flags = new int[number_of_classes];
	    for(int i=0; i < number_of_classes; i++)
	    {
		    inner_class_info_index[i] = data_is.readShort();
		    outer_class_info_index[i] = data_is.readShort();
		    inner_name_index[i] = data_is.readShort();
		    inner_class_access_flags[i] = data_is.readShort();
	    }
	}
}
