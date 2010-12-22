package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantDouble extends GenericConstant {

    private double value;

    public double value()
    {
        return value;
    }

    ConstantDouble(double d)
    {
        this.value = d;
    }

    public byte tag()
    {
        return (byte) CONSTANT_DOUBLE;
    }

    public String toString()
    {
        return "Double " + value;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeDouble(value);
    }

    public void read(DataInputStream data_is)
      throws IOException
	{
	    value = data_is.readDouble();
	}
}

