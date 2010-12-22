package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantString extends GenericConstant {

    int string_index;

    public int string_index()
    {
        return string_index;
    }

    public byte tag()
    {
        return (byte) CONSTANT_STRING;
    }

    public ConstantString(int index)
    {
        string_index = index;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeShort(string_index);
    }


    public void read(DataInputStream data_is)
      throws IOException
	{
	    string_index = data_is.readShort();
	}

	public String toString()
	{
	    return "String #" + string_index;
	}
}
