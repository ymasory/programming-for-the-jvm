package COM.sootNsmoke.instructions;

public class New extends ClassSequence
{
    public New(String classname)
    {
        super(1, 1, opc_new, classname);
    }
}
