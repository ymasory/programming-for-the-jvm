package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;


/** Not really an instruction, but nore of a locator,
 * Whenever the label with this label tag is used in
 * a branch instruction (like Goto or Ifeq), the
 * instruction which is next in the Sequence after
 * this Label is the real target
 */
public class Label  extends  Sequence
{
    String label;

    /** The label is a tag which can be used in the various branch instructions */
    public Label (String label)
    {
        super(0, 0);
        this.label = label;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        bytecodes.addNameHere(label);
    }

    public String toString()
    {
        return label + ":";
    }
}
