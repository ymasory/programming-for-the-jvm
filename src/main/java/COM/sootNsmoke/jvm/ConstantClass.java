package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantClass extends GenericConstant {
    public byte tag()
    {
        return (byte) CONSTANT_CLASS;
    }

    public ConstantClass(int n)
    {
        this.name_index = n;
    }

    private int name_index;

    /** Index of UTF8 which is the name of the class */
    public int name_index()
    {
        return name_index;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeShort(name_index);
    }


    public void read(DataInputStream data_is) throws IOException
	{
	    name_index = data_is.readShort();
	}

	public String toString()
	{
	    return "Class name #" + name_index;
	}
}
