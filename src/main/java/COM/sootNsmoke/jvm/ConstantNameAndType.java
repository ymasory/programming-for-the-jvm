package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantNameAndType extends GenericConstant {

    private int name_index;
    private int descriptor_index;

    public int name_index() { return name_index; }
    public int descriptor_index() { return descriptor_index; }

    public String toString()
    {
        return "NameAndType name #" + name_index +
               " descriptor #" + descriptor_index;
    }

    public ConstantNameAndType(int name_index, int descriptor_index)
    {
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeShort(name_index);
        os.writeShort(descriptor_index);
    }

    public void read(DataInputStream data_is)
      throws IOException
	{
	    name_index = data_is.readShort();
	    descriptor_index = data_is.readShort();
	}

    public byte tag()
    {
        return (byte) CONSTANT_NAMEANDTYPE;
    }
}

