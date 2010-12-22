package COM.sootNsmoke.jvm;
import java.io.*;


public class ConstantValueAttribute extends Attribute {

    private int constantvalue_index;

    public int constantvalue_index()
    {
        return constantvalue_index;
    }

    ConstantValueAttribute(int attribute_name)
    {
        super(attribute_name, 2);
    }

    ConstantValueAttribute(ClassFile cf, int value)
    {
        super(cf.lookupConstantString("ConstantValue"), 2);
        this.constantvalue_index = value;
    }

    public String toString()
    {
        return super.toString() + " constant value: " + constantvalue_index;
    }


    public void write(DataOutputStream os) throws IOException
    {
       super.write(os);
       os.writeShort(constantvalue_index);
    }

    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    attribute_length = data_is.readInt();
	    constantvalue_index = data_is.readShort();
	}
}


