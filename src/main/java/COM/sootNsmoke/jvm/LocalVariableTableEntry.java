package COM.sootNsmoke.jvm;
import java.io.*;


public class LocalVariableTableEntry {

    private int start_pc;
    private int length;
    private int name_index;
    private int descriptor_index;
    private int index;

    public int start_pc() { return start_pc; }
    public int length() { return length; }
    public int name_index() { return name_index; }
    public int descriptor_index() { return descriptor_index; }
    public int index() { return index; }


    LocalVariableTableEntry()
    {
    }

    public LocalVariableTableEntry(
        int start_pc,
        int length,
        int name_index,
        int descriptor_index,
        int index)
    {
        this.start_pc = start_pc;
        this.length = length;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
        this.index = index;

    }


    public void write(DataOutputStream os) throws IOException
    {
        os.writeShort(start_pc);
        os.writeShort(length);
        os.writeShort(name_index);
        os.writeShort(descriptor_index);
        os.writeShort(index);
    }

    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    start_pc = data_is.readShort();
	    length = data_is.readShort();
	    name_index = data_is.readShort();
	    descriptor_index = data_is.readShort();
	    index = data_is.readShort();
	}
}
