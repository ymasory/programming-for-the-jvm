package COM.sootNsmoke.jvm;

import java.io.*;
import java.util.*;


public class ExceptionsAttribute extends Attribute {

    private int exception_index_table[];

    public int[] exception_index_table()
    {
        return exception_index_table;
    }


    ExceptionsAttribute(ClassFile cf, int classes[])
    {
        super(cf.lookupConstantString("Exceptions"), 2+2*classes.length);
        exception_index_table = classes;
    }

    ExceptionsAttribute(int attribute_name)
    {
        super(attribute_name, 2);
    }


    public String toString()
    {
        String s = new String();
        for(int i = 0; i < exception_index_table.length; i++)
            s += exception_index_table[i] + " ";
        return s;
    }

    public void write(DataOutputStream os) throws IOException
    {
       super.write(os);
        os.writeShort(exception_index_table.length);
        for(int i = 0; i < exception_index_table.length; i++)
            os.writeShort(exception_index_table[i]);
    }

    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    attribute_length = data_is.readInt();
	    int number_of_exceptions = data_is.readShort();
	    exception_index_table = new int[number_of_exceptions];
	    for(int i=0; i < number_of_exceptions; i++) {
		    exception_index_table[i] = data_is.readShort();
	    }
	}
}
