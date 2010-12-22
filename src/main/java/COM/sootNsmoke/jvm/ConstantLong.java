package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantLong extends GenericConstant {

    long value;

    public long value()
    {
        return value;
    }



    public ConstantLong(long l)
    {
        value = l;
    }

    public byte tag()
    {
        return (byte) CONSTANT_LONG;
    }

    public String toString()
    {
        return "Long " + value;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeLong(value);
    }

    public void read(DataInputStream data_is)
      throws IOException
	{
	    value = data_is.readLong();
	}
}
