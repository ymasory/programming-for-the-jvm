package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantInteger extends GenericConstant {

    private int value;

    public int value()
    {
        return value;
    }



    ConstantInteger(int i)
    {
        value = i;
    }


    public byte tag()
    {
        return (byte) CONSTANT_INTEGER;
    }

    public String toString()
    {
        return "Integer " + value;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeInt(value);
    }

    public void read(DataInputStream data_is)
      throws IOException
	{
	    value = data_is.readInt();
	}
}
