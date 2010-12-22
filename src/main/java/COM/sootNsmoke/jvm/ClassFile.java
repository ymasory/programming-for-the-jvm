package COM.sootNsmoke.jvm;

import java.io.*;
import java.util.*;


/**
 * A class used for reading and building Java class files.
 *
 * @author Joshua Engel
 */
public class ClassFile implements RuntimeConstants
{

    // members of the ClassFile structure as described in the spec

    private byte[] magic = { (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe };
    private int minor_version = 3;
    private int major_version = 45;
    private GenericConstant[] constant_pool = new GenericConstant[1];
    private int access_flags;
    private int this_class;
    private int super_class;
    private int interfaces[] = new int[0];
    private FieldInfo[] fields = new FieldInfo[0];
    private MethodInfo[] methods = new MethodInfo[0];
    private Attribute[] attributes = new Attribute[0];

    /** Gets the magic number */
    public byte[] getMagic()        { return magic; }

    /** Gets the minor version (should be 3) */
    public int getMinorVersion()    { return minor_version; }

    /** Gets the major version (should be 45) */
    public int getMajorVersion()    { return major_version; }

    /** Gets the constant pool */
    public GenericConstant[] getConstantPool() { return constant_pool; }

    /** Gets a particular entry from the constant pool */
    public GenericConstant getConstantPool(int i)  { return constant_pool[i]; }

    /** Gets the access flags. See RuntimeConstants for more details
     * on the meaning of the bits
     */
    public int getAccessFlags()    { return access_flags; }

    /** Gets the constant pool index of this class */
    public int getThis()            { return this_class; }

    /** Gets the constant pool index of the super class */
    public int getSuper()           { return super_class; }

    /** Gets the constant pool indices of the implemented interfaces */
    public int[] getInterfaces()    { return interfaces; }
    public int getInterfaces(int i)    { return interfaces[i]; }

    /** Gets the list of fields */
    public FieldInfo[] getFields()  { return fields; }
    public FieldInfo getFields(int i)  { return fields[i]; }

    /** Gets the list of methods */
    public MethodInfo[] getMethods(){ return methods; }
    public MethodInfo getMethods(int i){ return methods[i]; }

    /** Gets the list of attributes */
    public Attribute[] getAttributes()   { return attributes; }

    /** Sets the magic number */
    public void setMagic(byte[] magic)        { this.magic = magic; }

    /** Sets the minor version (should be 3) */
    public void setMinorVersion(int minor_version)    { this.minor_version = minor_version; }

    /** Sets the major version (should be 45) */
    public void setMajorVersion(int major_version)    { this.major_version = major_version; }

    /** Sets the constant pool */
    public void setConstantPool(GenericConstant[] constant_pool) { this.constant_pool = constant_pool; }

    /** Sets the access flags. See RuntimeConstants for more details
     * on the meaning of the bits
     */
    public void setAccessFlags(int access_flags)    { this.access_flags = access_flags; }

    /** Sets the constant pool index of this class */
    public void setThis(int this_class)            { this.this_class = this_class; }

    /** Sets the constant pool index of the super class */
    public void setSuper(int super_class)           { this.super_class = super_class; }

    /** Sets the constant pool indices of the implemented interfaces */
    public void setInterfaces(int[] interfaces)    { this.interfaces = interfaces; }

    /** Sets the list of fields */
    public void setFields(FieldInfo[] fields)  { this.fields = fields; }

    /** Sets the list of methods */
    public void setMethods(MethodInfo[] methods){ this.methods = methods; }

    /** Sets the list of attributes */
    public void setAttributes(Attribute[] attributes)   { this.attributes = attributes; }

    public String toString()
    {
        String s = new String("magic = ");
        for(int i = 0; i < magic.length; i++)
        {
            int l = magic[i];
            if(l < 0)
                l = l + 256;
            s += Integer.toString(l, 16) + " ";
        }
        s += "\n";
        s += "minor version = " + minor_version + "\n";
        s += "major version = " + major_version + "\n";
        s += "" + constant_pool.length + " constants\n";
        for(int i = 1; i < constant_pool.length; i++)
        {
            GenericConstant cpi = constant_pool[i];
            s += Stringer.indent("    ", i + ". " + cpi.toString()) + "\n";
    		byte tag = cpi.tag();
    		if(tag == CONSTANT_DOUBLE ||
    		   tag == CONSTANT_LONG) {
    		    i++;
    		}
        }
        s += "access_flags = " + access_flags + "\n";
        s += "this = " + this_class + "\n";
        s += "super = " + super_class + "\n";

        s += interfaces.length + " interfaces\n";
        for(int i = 0; i < interfaces.length; i++)
            s += Stringer.indent("    ", Integer.toString(interfaces[i]));

        s += "" + fields.length + " fields\n";
        for(int i = 0; i < fields.length; i++)
            s += Stringer.indent("    ", fields[i].toString()) + "\n";
        s += "" + methods.length + " methods\n";
        for(int i = 0; i < methods.length; i++)
            s += Stringer.indent("    ", methods[i].toString()) + "\n";
        s += "" + attributes.length + " attributes\n";
        for(int i = 0; i < attributes.length; i++)
            s += Stringer.indent("    ", attributes[i].toString()) + "\n";
        return s;

    }

    /** Adds a constant, and returns the number of the new pool entry */
    public int add(GenericConstant constant)
    {
        int len = constant_pool.length;
        GenericConstant[] constant_pool2;
        if(constant.tag() == CONSTANT_DOUBLE ||
    	   constant.tag() == CONSTANT_LONG)
            constant_pool2 = new GenericConstant[len+2];
        else
            constant_pool2 = new GenericConstant[len+1];
        System.arraycopy(constant_pool, 0, constant_pool2, 0, len);
        constant_pool2[len] = constant;
        constant_pool = constant_pool2;
        return  len;
    }

    /** Adds a method, and returns the number of the new entry */
    public int add(MethodInfo method)
    {
        int len = methods.length;
        MethodInfo[] methods2 = new MethodInfo[len+1];
        System.arraycopy(methods, 0, methods2, 0, len);
        methods2[len] = method;
        methods = methods2;
        return  len;
    }

    /** Adds an attribute, and returns the number of the new entry */
    public int add(Attribute attribute)
    {
        int len = attributes.length;
        Attribute[] attributes2 = new Attribute[len+1];
        System.arraycopy(attributes, 0, attributes2, 0, len);
        attributes2[len] = attribute;
        attributes = attributes2;
        return  len;
    }

    /** Adds a field, and returns the number of the new entry */
    public int add(FieldInfo field)
    {
        int len = fields.length;
        FieldInfo[] fields2 = new FieldInfo[len+1];
        System.arraycopy(fields, 0, fields2, 0, len);
        fields2[len] = field;
        fields = fields2;
        return len;
    }

    /** Adds an interface, and returns the number of the entry */
    public int add(int intf)
    {
        // Painful decision: because there won't be many interfaces,
        // I'll take the performance hit of growing the array each time
        // in order to gain the syntactic nicety of having an array of ints
        int[] interfaces2 = new int[interfaces.length+1];
        System.arraycopy(interfaces, 0, interfaces2, 0, interfaces.length);
        interfaces2[interfaces.length] = intf;
        interfaces = interfaces2;
        return (interfaces.length-1);
    }

    /** Writes the class file to the stream in class file format */
    public void write(OutputStream output_stream) throws IOException
    {
        DataOutputStream os = new DataOutputStream(output_stream);
        os.write(magic, 0, magic.length);
        os.writeShort(minor_version);
        os.writeShort(major_version);
        os.writeShort(constant_pool.length);

        for(int i = 1; i < constant_pool.length; i++)
        {
            GenericConstant cpi = constant_pool[i];
            cpi.write(os);
            // Handle the fact that 8-byte constants are 2 constant pool entries
    		byte tag = cpi.tag();
    		if(tag == CONSTANT_DOUBLE ||
    		   tag == CONSTANT_LONG) {
    		    i++;
    		}
        }

        os.writeShort(access_flags);
        os.writeShort(this_class);
        os.writeShort(super_class);

        os.writeShort(interfaces.length);
        for(int i = 0; i < interfaces.length; i++)
            os.writeShort(interfaces[i]);

        os.writeShort(fields.length);
        for(int i = 0; i < fields.length; i++)
            ((FieldInfo) fields[i]).write(os);

        os.writeShort(methods.length);
        for(int i = 0; i < methods.length; i++)
            ((MethodInfo) methods[i]).write(os);

        os.writeShort(attributes.length);
        for(int i = 0; i < attributes.length; i++)
            ((Attribute) attributes[i]).write(os);
    }

    /** Initialize the constant pool entry from a stream, which is
     * expected to point to something in the class file format.
     * @exception ClassFileException If the class file is in error
     */
    public void read(InputStream is)
      throws ClassFileException, IOException
	{
	    DataInputStream data_is = new DataInputStream(is);
	    int i;

	    magic = new byte[4];
	    for(i=0; i < 4; i++) {
    		magic[i] = data_is.readByte();
	    }
	    // the magic number should be 0xCAFEBABE
	    if(magic[0] != (byte)0xca ||
	       magic[1] != (byte)0xfe ||
	       magic[2] != (byte)0xba ||
	       magic[3] != (byte)0xbe) {
		throw new ClassFileException("bad magic number.  magic number is " + magic[0] + magic[1] + magic[2] + magic[3]);
	    }

	    minor_version = data_is.readShort();
	    major_version = data_is.readShort();

	    int constant_pool_count = data_is.readShort();

        constant_pool = new GenericConstant[constant_pool_count];

	    // leave the zeroth, 'reserved for internal VM use'
	    // entry unused, just so our indexes match up
	    for(i = 1; i < constant_pool_count; i++) {
    		GenericConstant constant = GenericConstant.readConstant(data_is);
    		constant_pool[i] = constant;

    		// if the constant pool entry is of type double or long,
    		// we have to skip over the next constant table entry.
    		// the spec says, "In retrospect, making eight-byte constants
    		// take two constant pool entries was a poor choice."
    		byte tag = constant.tag();
    		if(tag == CONSTANT_DOUBLE ||
    		   tag == CONSTANT_LONG) {
    		    i++;
    		}
	    }

	    access_flags = data_is.readShort();
	    this_class = data_is.readShort();
	    super_class = data_is.readShort();

	    int interfaces_count = data_is.readShort();
	    interfaces = new int[interfaces_count];
	    for(i=0; i < interfaces_count; i++) {
		    interfaces[i] = data_is.readShort();
	    }

	    int fields_count = data_is.readShort();
	    fields = new FieldInfo[fields_count];
	    for(i=0; i < fields_count; i++) {
    		FieldInfo field = new FieldInfo(this, 0,0,0);
    		field.read(is);
    		fields[i] = field;
	    }

	    int methods_count = data_is.readShort();
	    methods = new MethodInfo[methods_count];
	    for(i=0; i < methods_count; i++) {
    		MethodInfo method = new MethodInfo(this, 0, 0, 0);
    		method.read(is);
    		methods[i] = method;
	    }

	    int attributes_count = data_is.readShort();
	    attributes = new Attribute[attributes_count];
	    for(i=0; i < attributes_count; i++) {
    		Attribute specific_attr = Attribute.read(this, is);
    		attributes[i] = specific_attr;
	    }
	}

    /** Looks up a class entry in the constant pool which points to a string
     *  which is equal to the parameter.  If it doesn't exist, it creates one.
     */
    public int lookupClass(String class_name)
    {
        for(int i = 1; i < constant_pool.length; i++)
        {
            GenericConstant cpi = (GenericConstant) constant_pool[i];
            if(cpi instanceof ConstantClass)
            {
                ConstantClass class_entry = (ConstantClass) (cpi);
                ConstantUtf8 class_entry_name =
                    (ConstantUtf8) getConstant(class_entry.name_index());
                if(class_entry_name.value.equals(class_name))
                    return i;
            }
        }
        int index = lookupConstantString(class_name);
        return add(new ConstantClass(index));
    }

    /** Looks up a string in the constant pool, and returns its
     *  index if it exists.  If it doesn't it creates the entry
     *  and returns the new entry
     */
    public int lookupConstantString(String str)
    {
        for(int i = 1; i < constant_pool.length; i++)
        {
            GenericConstant cpi = (GenericConstant) constant_pool[i];
            if(cpi instanceof ConstantUtf8)
            {
                ConstantUtf8 const_str = (ConstantUtf8) cpi;
                if(const_str.value.equals(str))
                    return i;
            }
        }
        return add(new ConstantUtf8(str));
    }

    /** Returns the number of entries in the constant pool.  */
    public int getConstantPoolLength()
    {
        return constant_pool.length;
    }

    /** Returns the constant referred to by the constant pool index.
     * Returns null if the index is not valid
     */
    public GenericConstant getConstant(int entry_number)
    {
        if(entry_number < 0 || entry_number >= constant_pool.length)
            return null;
	    return constant_pool[entry_number];
    }

    /** Takes a constant pool index and returns the value of
     * the UTF8 that it refers to as a String.
     * Returns null in the event it does not refer to a a UTF8
     */
    public String utf8(int entry_number)
    {
        if(entry_number >= constant_pool.length)
            return null;
        GenericConstant cpi = constant_pool[entry_number];
        if(cpi instanceof ConstantUtf8)
            return ((ConstantUtf8) cpi).value;
        else
            return null;
    }
}