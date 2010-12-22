package COM.sootNsmoke.jvm;
import java.io.*;
import java.util.*;

public class ExceptionTableEntry {

    private int start_pc;
    private int end_pc;
    private int handler_pc;
    private int catch_type;

    public int start_pc()  { return start_pc; }
    public int end_pc() { return end_pc; }
    public int handler_pc()  { return handler_pc; }
    public int catch_type() { return catch_type; }

    ExceptionTableEntry()
    {
    }

    public ExceptionTableEntry(int start_pc, int end_pc,
                               int handler_pc, int catch_type)
    {
        this.start_pc = start_pc;
        this.end_pc = end_pc;
        this.handler_pc = handler_pc;
        this.catch_type = catch_type;
    }

    public String toString()
    {
        return "start_pc: " + start_pc + " end_pc " + end_pc
             + " handler_pc " + handler_pc + " catch_type " + catch_type;
    }

    public void write(DataOutputStream os) throws IOException
    {
        os.writeShort(start_pc);
        os.writeShort(end_pc);
        os.writeShort(handler_pc);
        os.writeShort(catch_type);
    }


    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    start_pc = data_is.readShort();
	    end_pc = data_is.readShort();
	    handler_pc = data_is.readShort();
	    catch_type = data_is.readShort();
	}
}
