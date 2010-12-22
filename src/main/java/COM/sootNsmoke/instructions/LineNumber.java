package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

/** A pseudo-instruction which marks where line numbers go */
public class LineNumber  extends  Sequence
{
    int number;
    public LineNumber (int number)
    {
        super(0, 0);
        this.number = number;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.addLineNumberHere(number);
    }
}
