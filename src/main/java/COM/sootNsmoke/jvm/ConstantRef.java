package COM.sootNsmoke.jvm;
import java.io.*;

/** Fieldref's, Methodref's, and InterfaceMethodref's all contain
 * exactly the same data.  This class serves as a base to all three.
 *
 * @see COM.sootNsmoke.Fieldref
 * @see COM.sootNsmoke.Methodref
 * @see COM.sootNsmoke.InterfaceMethodref
 * @author Joshua Engel
 */
public abstract class ConstantRef extends GenericConstant {

    protected int class_index;
    protected int name_and_type_index;

    /** Index of the ConstantClass which holds the field/method */
    public int class_index() { return class_index; }

    /** Index of the NameAndType which names the field/method and
     * gives its type descriptor
     */
    public int name_and_type_index()
    {
        return name_and_type_index;
    }

    public ConstantRef(int class_index, int name_and_type_index)
    {
        this.class_index = class_index;
        this.name_and_type_index = name_and_type_index;
    }

    public String toString()
    {
        return "class #" + class_index +
               " name-and-type #" + name_and_type_index;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeShort(class_index);
        os.writeShort(name_and_type_index);
    }

    public void read(DataInputStream data_is) throws IOException
	{
	    class_index = data_is.readShort();
	    name_and_type_index = data_is.readShort();
	}
}

