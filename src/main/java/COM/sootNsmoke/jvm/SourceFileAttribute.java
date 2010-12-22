package COM.sootNsmoke.jvm;
import java.io.*;

public class SourceFileAttribute extends Attribute {

    private int sourcefile_index;

    public int sourcefile_index()
    {
        return sourcefile_index;
    }

    SourceFileAttribute(int attribute_name)
    {
        super(attribute_name, 2);
    }

    public SourceFileAttribute(ClassFile cf, int sourcefile_index)
    {
        super(cf.lookupConstantString("SourceFile"), 2);
        this.sourcefile_index = sourcefile_index;
    }

    public String toString()
    {
        return super.toString() + " sourcefile: " + sourcefile_index;
    }

    public void write(DataOutputStream os) throws IOException
    {
       super.write(os);
       os.writeShort(sourcefile_index);
    }

    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);
	    attribute_length = data_is.readInt();
	    sourcefile_index = data_is.readShort();
	}
}
