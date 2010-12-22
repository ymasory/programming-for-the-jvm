package COM.sootNsmoke.jvm;
import java.io.*;

public class LineNumberTableEntry {

    private int start_pc;
    private int line_number;

    public int start_pc() { return start_pc; }
    public int line_number() { return line_number; }

    LineNumberTableEntry()
    {
    }

    LineNumberTableEntry(int start_pc, int line_number)
    {
        this.start_pc = start_pc;
        this.line_number = line_number;
    }


    public String toString()
    {
        return "start_pc: " + start_pc + " line_number: " + line_number;

    }

    public void write(DataOutputStream os) throws IOException
    {
        os.writeShort(start_pc);
        os.writeShort(line_number);
    }

    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    start_pc = data_is.readShort();
	    line_number = data_is.readShort();
	}
}
