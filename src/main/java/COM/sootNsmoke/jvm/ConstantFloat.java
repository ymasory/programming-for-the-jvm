package COM.sootNsmoke.jvm;
import java.io.*;

public class ConstantFloat extends GenericConstant {

    private float value;

    public ConstantFloat(float f)
    {
        this.value = f;
    }

    public byte tag()
    {
        return (byte) CONSTANT_FLOAT;
    }

    public float value()
    {
        return value;
    }



    public String toString()
    {
        return "Float " + value;
    }

    public void write(DataOutputStream os) throws IOException
    {
        super.write(os);
        os.writeFloat(value);
    }

    public void read(DataInputStream data_is)
      throws IOException
	{
	    value = data_is.readFloat();
	}
}

