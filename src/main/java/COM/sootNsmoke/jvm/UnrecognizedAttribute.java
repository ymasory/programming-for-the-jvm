package COM.sootNsmoke.jvm;
import java.io.*;

/** An attribute we don't recognize.  Just store the raw contents */
public class UnrecognizedAttribute extends Attribute {
    private byte[] unrecognized_data;

    /** The data for the attribute */
    public byte[] data() { return unrecognized_data; }

    UnrecognizedAttribute(int attribute_name)
    {
        super(attribute_name, 0);
    }

    UnrecognizedAttribute(ClassFile cf, String name, byte[] bytes)
    {
        super(cf.lookupConstantString(name),
              bytes.length);
        this.unrecognized_data = bytes;
    }


    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.write(unrecognized_data, 0, unrecognized_data.length);
    }

    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    attribute_length = data_is.readInt();
	    unrecognized_data = new byte[attribute_length];
	    is.read(unrecognized_data, 0, attribute_length);
	}
}
