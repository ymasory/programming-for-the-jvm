package COM.sootNsmoke.jvm;
import java.io.*;
import java.util.*;

/** an abstract base class for the different types of constants */
public abstract class GenericConstant implements RuntimeConstants
{
    /** Returns the tag byte of this constant type */
    public abstract byte tag();

    public void write(DataOutputStream os) throws IOException
    {
        os.writeByte(tag());
    }

    public abstract void read(DataInputStream is) throws IOException;

    public static GenericConstant readConstant(InputStream is)
        throws ClassFileException, IOException
    {
        GenericConstant info;
	    DataInputStream data_is = new DataInputStream(is);

	    byte tag = data_is.readByte();
	    switch(tag) {
	      case(CONSTANT_CLASS):
	        info = new ConstantClass(0); break;
	      case(CONSTANT_FIELD):
	        info = new ConstantFieldref(0,0); break;
	      case(CONSTANT_METHOD):
	        info = new ConstantMethodref(0,0); break;
	      case(CONSTANT_INTERFACEMETHOD):
	          info = new ConstantInterfaceMethodref(0,0); break;
	      case(CONSTANT_STRING):
	        info = new ConstantString(0); break;
	      case(CONSTANT_INTEGER):
	        info = new ConstantInteger(0); break;
	      case(CONSTANT_FLOAT):
	        info = new ConstantFloat(0); break;
	      case(CONSTANT_LONG):
	        info = new ConstantLong(0); break;
	      case(CONSTANT_DOUBLE):
	        info = new ConstantDouble(0); break;
	      case(CONSTANT_NAMEANDTYPE):
             info = new ConstantNameAndType(0,0); break;
	      case(CONSTANT_UTF8):
	        info = new ConstantUtf8(""); break;
	      default:
		  throw new ClassFileException("got bad tag " + tag);
	    }

	    info.read(data_is);
	    return info;
    }
}

