package COM.sootNsmoke.jvm;
import java.io.*;

public class LocalVariableTableAttribute extends Attribute {
    LocalVariableTableAttribute(int attribute_name)
    {
        super(attribute_name, 2);
    }

    LocalVariableTableAttribute(ClassFile cf, LocalVariableTableEntry[] lvt)
    {
        super(cf.lookupConstantString("LocalVariableTable"),
              2 + lvt.length * 10);

        local_variable_table = lvt;
    }

    private LocalVariableTableEntry[] local_variable_table;

    public LocalVariableTableEntry[] local_variable_table()
    {
        return local_variable_table;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeShort(local_variable_table.length);
        for(int i = 0; i < local_variable_table.length; i++)
            local_variable_table[i].write(os);
    }

    public void read(InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    attribute_length = data_is.readInt();
	    int local_variable_table_length = data_is.readShort();
	    local_variable_table = new LocalVariableTableEntry[local_variable_table_length];
	    for(int i=0; i < local_variable_table_length; i++) {
    		local_variable_table[i] = new LocalVariableTableEntry();
	    	local_variable_table[i].read(is);
	    }
        attribute_length = 2+local_variable_table.length *(2+2+2+2);
	}
}
