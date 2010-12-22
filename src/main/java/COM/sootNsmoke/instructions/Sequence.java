package COM.sootNsmoke.instructions;
import java.io.*;
import java.util.*;
import COM.sootNsmoke.jvm.*;

public abstract class  Sequence
implements RuntimeConstants
{
    /** Maximum amount of stack used */
    int max_stack = 0;

    /** Net stack usage; difference in stack height between beginning and
     * and of this code */
    int net_stack = 0;

    /** Number of the highest local variable used in the sequence.
     * -1 if no variables are used
     */
    int max_vars = 0;

    Sequence()
    {
        this(0, 0);
    }

    /** Create a new Sequence with the given max_stack and net_stack and no
     * variables
     */
    protected Sequence(int max_stack, int net_stack)
    {
        this(max_stack, net_stack, -1);
    }


    /** Create a new Sequence with the given max_stack, net_stack and
     * max_vars
     */
    protected Sequence(int max_stack, int net_stack, int max_vars)
    {
        this.max_stack = max_stack;
        this.net_stack = net_stack;
        this.max_vars = max_vars;
    }

    /** The maximum amount of stack required to execute this sequence
     * of instructions
     */
    public int max_stack()
    {
        return max_stack;
    }

    /** The net effect on the stack from executing this sequence of
     * instructions
     */
    public int net_stack()
    {
        return net_stack;
    }


    /** The largest numbered variable used in this sequence. -1 if no
     * variables are used.*/
    public int max_vars()
    {
        return max_vars;
    }

    /** Returns the high byte of the 2-byte value x */
    public static byte highbyte(int x)
    {
        return (byte) ((x >> 8) & 0xff);
    }

    /** Returns the low byte of the 2-byte value x */
    public static byte lowbyte(int x)
    {
        return (byte) (x & 0xff);
    }

    /** Converts the instruction sequence to an array of bytecodes.
     * Constants are entered into the Java class file cf.
     * labels are resolved with the symbol table syms.
     */
    public abstract void toBytecodes(Bytecodes bytecode );

    /** Converts the instruction sequence into an array of bytes.
     * Constants are put into the JavaClass cf.
     */
    public byte[] toByteArray(JavaClass cf) throws UndefinedLabelException
    {
        Bytecodes bc = new Bytecodes(this, cf);
        return bc.toByteArray();
    }

    /** Inserts this method into the class */
    public void addMethod(JavaClass cf, String name, String type, int access)
    {
	cf.addMethod(name, type, access,
		     max_stack(), max_vars()+1,
		     toByteArray(cf),
		     null, null, null);
    }

			  
    /** Creates a new instruction sequence which is the merge of this one and
     * the argument
     */
    public  Sequence appendSequence( Sequence seq)
    {
        return new CompoundInstruction(this, seq);
    }

    /** Same as appendSequence
     * @see #appendSequence
     */
    public Sequence append(Sequence seq)
    {
       return appendSequence(seq);
    }

    /**
     * This combiner is used for the two alternatives of an if statement.
     * The two sequences will never be exectued sequentially; for an particuar
     * executation of the conditional, one sequence of the other will be entered.
     * Because of this, the registers needed by the second sequence are still
     * by the combined sequence, even if they are modified by the first sequence
     */
    public  Sequence parallelSequence( Sequence seq)
    {
        return new CompoundInstruction(this, seq, true);
    }
}

