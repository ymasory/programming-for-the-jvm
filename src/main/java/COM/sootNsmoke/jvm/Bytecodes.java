package COM.sootNsmoke.jvm;
import COM.sootNsmoke.instructions.*;
import java.util.*;

/** For assembling bytecodes from sequences.
 */
public class Bytecodes
{
    /** A list of LocalVariablePseudoInstructions */
    private Vector local_vars = new Vector();

    /** A list of ExceptionHandlerPseudoInstructions */
    private Vector exceptions = new Vector();

    /** The class file being built */
    public JavaClass cf;

    public Bytecodes(Sequence seq, JavaClass cf) throws UndefinedLabelException
    {
        this.cf = cf;
        seq.toBytecodes(this);
        resolve();
    }

    private byte[] bytes = new byte[1024];
    private int length = 0;

    public int length()
    {
        return length;
    }

    public void writeInt(int i)
    {
        if(length + 4 >= bytes.length)
            bytes = grow(bytes);
        bytes[length++] = (byte) ((i >> 24) & 0xff);
        bytes[length++] = (byte) ((i >> 16) & 0xff);
        bytes[length++] = (byte) ((i >> 8) & 0xff);
        bytes[length++] = (byte) (i & 0xff);
    }

    /** Same as write(byte) */
    public void write(int b)
    {
        write((byte) b);
    }

    /** Writes a single byte to the array of bytecodes */
    public void write(byte b)
    {
        if(length+1 >= bytes.length)
            bytes = grow(bytes);
        bytes[length] = (byte) b;
        length++;
    }

    public void write(byte[] b)
    {
        if(length + b.length >= bytes.length)
            bytes = grow(bytes);
        System.arraycopy(b, 0, bytes, length, b.length);
        length += b.length;

    }

    static byte[] grow(byte[] bytes)
    {
        byte[] b = new byte[bytes.length*2];
        System.arraycopy(bytes, 0, b, 0, bytes.length);
        return b;
    }

    /** Return the bytes which make up the bytecodes of this method */
    public byte[] toByteArray()
    {
        byte[] b = new byte[length];
        System.arraycopy(bytes, 0, b, 0, length);
        return b;
    }

    /** Maps the location in the byte array of a slot to be filled in
     * to the label which it should be filled in with.  The source of
     * the instruction is assumed to be right before the slot.
     */
    Hashtable slots = new Hashtable();

    /** Maps a location in the byte array of a slot to be filled in
     * the name of a label which it should be filled in with _and_
     * the source of the whole instruction.  The mapped-to value is
     * a NameSource (see below.)
     */
    Hashtable wide_slots = new Hashtable();

    /** Maps label names to locations (Integer) */
    Hashtable names = new Hashtable();

    /** For building the LineNumberTableAttribute.
     * Each element is an int[2], where the first is
     * the start_pc and the second is the line number
     */
    private Vector lineNums = new Vector();

    /** Designates a location to be filled in with an address named name when
     * name becomes known
     */
    public void addAddressSlotHere(String name)
    {
        slots.put(new Integer(length), name);
    }

    /** Like addAddressSlotHere, but refers to a 4-byte-wide location */
    public void addWideAddressSlotHere(String name, int source)
    {
        NameSource name_source = new NameSource(name, source);
        wide_slots.put(new Integer(length), name_source);
    }


    /** Add a name to the list of known addresses, set to the next byte */
    public void addNameHere(String name)
    {
        names.put(name, new Integer(length));
    }

    /** Set the source line numbered lineno to include the next instruction
     * to be added.
     */
    public void addLineNumberHere(int lineno)
    {
        int[] fact = { length, lineno };
        lineNums.addElement(fact);
    }

    /** Create a LineNumberTableAttribute from the LineNumber elements
     * in the sequence
     */
    public LineNumberTableAttribute createLineNumberTableAttribute()
    {
        int[][] data = getLineNums();
        return cf.createLineNumberTableAttribute(data);
    }

    /** Create a LocalVariableTableAttribute from a list of local
     * variable table entries */
    public LocalVariableTableAttribute createLocalVariableTableAttribute(
        LocalVariableTableEntry[] lvt)
    {
        return cf.createLocalVariableTableAttribute(lvt);
    }

    /** Builds up the line number table based on the LineNumber
     * instructions found in the sequence. The return value is
     * an array of line number information. For each element,
     * The first element is the start_pc for the line number,
     * and the second is the source code line number that it points to.
     */
    public int[][] getLineNums()
    {
        int[][] line_nums = new int[lineNums.size()][];
        lineNums.copyInto(line_nums);
        return line_nums;
    }

    /** Resolves the addresses with the names.
     * It makes the assumption that the place pointed by the slot is one
     * after the opcode.  It inserts the offset between that location and
     * the address into the slot location.
     */
    private void resolve() throws UndefinedLabelException
    {
        for(Enumeration e = slots.keys(); e.hasMoreElements(); )
        {
            Integer slot = (Integer) e.nextElement();
            String name = (String) slots.get(slot);
            Integer dest = (Integer) names.get(name);
            if(dest == null)
                throw new UndefinedLabelException(name);
            int source = slot.intValue();
            short offset = (short) (dest.intValue() - source + 1);
            bytes[source] = Sequence.highbyte(offset);
            bytes[source+1] = Sequence.lowbyte(offset);

        }
        for(Enumeration e = wide_slots.keys(); e.hasMoreElements(); )
        {
            Integer slot = (Integer) e.nextElement();
            NameSource name_source = (NameSource) wide_slots.get(slot);
            String name = name_source.name;

            Integer dest = (Integer) names.get(name);
            int source = slot.intValue();
            if(dest == null)
                throw new UndefinedLabelException(name);

            int offset = (dest.intValue() -
                                   source + name_source.source_offset);
            bytes[source++] = (byte) ((offset >> 24) & 0xff);
            bytes[source++] = (byte) ((offset >> 16) & 0xff);
            bytes[source++] = (byte) ((offset >>  8) & 0xff);
            bytes[source++] = (byte) ((offset      ) & 0xff);

        }
    }

    /** Returns the location in the byte array of a label.
     * Returns -1 if the name is not found.
     */
    public int labelLoc(String label)
    {
        Integer loc = (Integer) names.get(label);
        if(loc == null)
            return -1;
        else
            return loc.intValue();
    }

}

/** A pair String x int for the name of a label and the offset of
 * the instruction to resolve against
 */
class NameSource
{
    String name;
    int source_offset;

    NameSource(String name, int source_offset)
    {
        this.name = name;
        this.source_offset = source_offset;
    }
}
