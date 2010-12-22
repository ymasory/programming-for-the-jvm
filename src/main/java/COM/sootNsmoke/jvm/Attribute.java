package COM.sootNsmoke.jvm;
import java.io.*;

/** There are two ways to create attributes.
 * One way is to read it from a file.
 * The other is to create it by hand.
 * In the first case, attribute_length is set by the subclass' read()
 * when it comes from the file file.  attribute_name is read in with
 * Attribute.read() and passed to the constructor of the subclass.
 *
 * In the second case, attribute_length and attribute_name are set by
 * the constructors of the functions.
 */
public abstract class Attribute {
    protected Attribute(int attribute_name,
        int attribute_length)
    {
        this.attribute_name = attribute_name;
        this.attribute_length = attribute_length;
    }


    final public int length()
    {
        return attribute_length;  // +6?
    }

    private int attribute_name;

    /** A UTF8 entry which is the name of the attribute */
    public int attribute_name()
    {
        return attribute_name;
    }

    /** This is the length of the data.  Total length of an
     * attribute includes 6 more bytes (2 for the name, 4 for
     * the length.
     */
    protected int attribute_length;

    public String toString()
    {
        return "length: " + attribute_length;
    }

    public void write(DataOutputStream os) throws IOException
    {
        os.writeShort(attribute_name);
        os.writeInt(attribute_length);
    }

    public static Attribute read(ClassFile cf, InputStream is)
      throws IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    int attribute_name = data_is.readShort();

	    // get the constant out of the constant pool and find out the real name
	    GenericConstant cp_info =
  	        cf.getConstant(attribute_name);

        Attribute attribute;

	    if(cp_info.tag() != ClassFile.CONSTANT_UTF8) {
            attribute = new UnrecognizedAttribute(attribute_name);
	    }
	    else
	    {

    	    String attribute_name_string =
    	      ((ConstantUtf8)cp_info).getValue();


        	if(attribute_name_string.equals("SourceFile"))
        	    attribute = new SourceFileAttribute(attribute_name);
        	else if(attribute_name_string.equals("ConstantValue"))
        	    attribute = new ConstantValueAttribute(attribute_name);
        	else if(attribute_name_string.equals("Code"))
        	    attribute = new CodeAttribute(cf, attribute_name);
        	else if(attribute_name_string.equals("Exceptions"))
        	    attribute = new ExceptionsAttribute(attribute_name);
        	else if(attribute_name_string.equals("LineNumberTable"))
        	    attribute = new LineNumberTableAttribute(attribute_name);
        	else if(attribute_name_string.equals("LocalVariableTable"))
        	    attribute = new LocalVariableTableAttribute(attribute_name);
        	else if(attribute_name_string.equals("InnerClasses"))
        	    attribute = new InnerClassesAttribute(attribute_name);
        	else
        	    attribute = new UnrecognizedAttribute(attribute_name);
    	}

	    attribute.read(is);
	    return attribute;
	}

	abstract void read(InputStream is) throws IOException;

}

