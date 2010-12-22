package COM.sootNsmoke.jvm;
import java.io.*;

public class LineNumberTableAttribute extends Attribute {

    private LineNumberTableEntry[] line_number_table = new LineNumberTableEntry[0];

    public LineNumberTableEntry[] line_number_table()
    {
        return line_number_table;
    }

    LineNumberTableAttribute(ClassFile cf, LineNumberTableEntry[] lnt)
    {
        super(cf.lookupConstantString("LineNumberTable"), 2+lnt.length*4);
        line_number_table = lnt;
    }

    LineNumberTableAttribute(int attribute_name)
    {
        super(attribute_name, 2);
    }


    public String toString()
    {
        String s = "Line number table: ";
        for(int i = 0; i < line_number_table.length; i++)
            s += Stringer.indent("    ", line_number_table[i].toString()) + "\n";
        return s;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeShort(line_number_table.length);
        for(int i = 0; i < line_number_table.length; i++)
            line_number_table[i].write(os);
    }

    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    attribute_length = data_is.readInt();
	    int line_number_table_length = data_is.readShort();
	    line_number_table = new LineNumberTableEntry[line_number_table_length];
	    for(int i=0; i < line_number_table_length; i++) {
    		line_number_table[i] = new LineNumberTableEntry();
    		line_number_table[i].read(is);
	    }
	}

	public void add(int start_pc, int line_number)
	{
	    LineNumberTableEntry lnt = new LineNumberTableEntry(start_pc, line_number);
        LineNumberTableEntry line_number_table2[] =
            new LineNumberTableEntry[line_number_table.length+1];
        System.arraycopy(line_number_table, 0, line_number_table2, 0, line_number_table.length);
        line_number_table2[line_number_table.length] = lnt;
        line_number_table = line_number_table2;
	}
}

