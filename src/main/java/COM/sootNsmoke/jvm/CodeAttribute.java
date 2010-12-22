package COM.sootNsmoke.jvm;
import java.io.*;

public class CodeAttribute extends Attribute {
    //  here's where the good stuff is
    private int max_stack;
    private int max_locals;
    private byte[] code;
    private ExceptionTableEntry[] exception_table;
    private Attribute[] attributes;

    public int max_stack() { return max_stack; }
    public int max_locals() { return max_locals; }
    public byte[] code() { return code; }
    public ExceptionTableEntry[] exception_table() { return exception_table; }
    public Attribute[] attributes() { return attributes; }


    // Requires class file because of sub-attributes
    private ClassFile cf;

    CodeAttribute(ClassFile cf, int attribute_name)
    {
        super(attribute_name,0);
        this.cf = cf;
    }

    CodeAttribute(ClassFile cf,
         int max_stack,
         int max_locals,
         byte[] code,
         ExceptionTableEntry[] exception_table,
         Attribute[] attributes)
    {
        super(cf.lookupConstantString("Code"), 0);
	if(exception_table == null)
	    exception_table = new ExceptionTableEntry[0];
	if(attributes == null)
	    attributes = new Attribute[0];

        this.max_stack = max_stack;
        this.max_locals = max_locals;
        this.code = code;
        this.exception_table = exception_table;
        this.attributes = attributes;
        this.cf = cf;
        attribute_length =  2+2+4+ code.length + 2 + 8*exception_table.length + 2;
        for(int i = 0; i < attributes.length; i++)
        {
            attribute_length += attributes[i].length()+6;
        }
    }


    public String toString()
    {
        String str = super.toString() + " max_stack: " + max_stack + " max_locals " + max_locals + "\n";
        for(int i = 0; i < code.length; i++)
        {
            if(code[i] < 16 & code[i] >= 0)
                str += "0";
            str += Integer.toString(code[i] < 0 ? code[i]+256 : code[i], 16);
        }
        str += "\n";
        str += exception_table.length + " exception table entries:\n";
        for(int i = 0; i < exception_table.length; i++)
            str += Stringer.indent("    ", exception_table[i].toString()) + "\n";
        str += attributes.length + " attributes:\n";
        for(int i = 0; i < attributes.length; i++)
            str += Stringer.indent("    ", attributes[i].toString()) + "\n";
        return str;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeShort(max_stack);
        os.writeShort(max_locals);
        os.writeInt(code.length);
        os.write(code, 0, code.length);
        os.writeShort(exception_table.length);
        for(int i = 0; i < exception_table.length; i++)
            exception_table[i].write(os);
        os.writeShort(attributes.length);
        for(int i = 0; i < attributes.length; i++)
            attributes[i].write(os);
    }


    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);
	    int i;

	    attribute_length = data_is.readInt();
	    max_stack = data_is.readShort();
	    max_locals = data_is.readShort();
	    int code_length = data_is.readInt();
	    code = new byte[code_length];
	    is.read(code, 0, code_length);

	    int exception_table_length = data_is.readShort();
	    exception_table = new ExceptionTableEntry[exception_table_length];
	    for(i=0; i < exception_table_length; i++) {
		    exception_table[i] = new ExceptionTableEntry();
		    exception_table[i].read(is);
	    }

	    int attributes_count = data_is.readShort();
	    attributes = new Attribute[attributes_count];
	    for(i=0; i < attributes_count; i++) {
		    attributes[i] = Attribute.read(cf, is);
	    }
	}
}

