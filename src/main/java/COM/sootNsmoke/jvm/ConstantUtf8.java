package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantUtf8 extends GenericConstant {

    String value;

    public ConstantUtf8(String value)
    {
        this.value = value;
    }


    public byte tag()
    {
        return (byte) CONSTANT_UTF8;
    }

    public String toString()
    {
        return "UTF8 " + value;
    }


    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeUTF(value);
    }

    public void read(DataInputStream data_is)
      throws IOException
	{
	    value = data_is.readUTF();
	}

    public String getValue() {
	    return value;
    }
}

