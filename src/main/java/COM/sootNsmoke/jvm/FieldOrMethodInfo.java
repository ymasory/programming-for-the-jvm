package COM.sootNsmoke.jvm;

import java.io.*;
import java.util.*;


/** The base class of FieldInfo and MethodInfo. In the class file,
 * FieldInfo structures and MethodInfo structures are almost identical,
 * except for the types of attributes that they support.
 *
 * @see COM.sootNsmoke.FieldInfo
 * @see COM.sootNsmoke.MethodInfo
 *
 * @author Joshua Engel
 */
public class FieldOrMethodInfo {

    ClassFile cf;

    private int access_flags;
    private int name_index;
    private int descriptor_index;
    private Attribute[] attributes = {};

    public int access_flags()
    {
        return access_flags;
    }

    public int name_index()
    {
        return name_index;
    }
    public int descriptor_index()
    {
        return descriptor_index;
    }
    public Attribute[] attributes()
    {
        return attributes;
    }

    public FieldOrMethodInfo(ClassFile cf, int access_flags, int name_index,
                             int descriptor_index)
    {
        this(cf);
        this.access_flags = access_flags;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
    }

    FieldOrMethodInfo(ClassFile cf)
    {
        this.cf = cf;
    }

    public void add(Attribute attr)
    {
        // Results of painful strong-typing/efficiency decision
        Attribute attributes2[] = new Attribute[attributes.length+1];
        System.arraycopy(attributes, 0, attributes2, 0, attributes.length);
        attributes2[attributes.length] = attr;
        attributes = attributes2;
    }

    public String toString()
    {
        String s = "access_flags = " + access_flags + " name_index = " + name_index + " descriptor_index = " + descriptor_index + "\n";
        if(attributes != null)
            for(int i = 0; i < attributes.length; i++)
                s += "attribute " + i + " = " + attributes[i] + "\n";
        return s;
    }

    public void write(DataOutputStream os) throws IOException
    {
        os.writeShort(access_flags);
        os.writeShort(name_index);
        os.writeShort(descriptor_index);
        os.writeShort(attributes.length);
        for(int i = 0; i < attributes.length; i++)
            attributes[i].write(os);
    }

    public void read(InputStream is)
      throws ClassFileException, IOException
	{
	    DataInputStream data_is = new DataInputStream(is);

	    access_flags = data_is.readShort();
	    name_index = data_is.readShort();
	    descriptor_index = data_is.readShort();

	    int attributes_count = data_is.readShort();

	    attributes = new Attribute[attributes_count];
	    for(int i=0; i < attributes_count; i++) {
    		attributes[i] = Attribute.read(cf, is);
	    }
	}
}
