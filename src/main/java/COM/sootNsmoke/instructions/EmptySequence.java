package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

public class EmptySequence extends  Sequence
{
    public EmptySequence()
    {
        super(0, 0);
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
    }

    public String toString()
    {
        return ";";
    }
}
