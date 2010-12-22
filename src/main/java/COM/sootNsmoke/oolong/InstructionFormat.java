package COM.sootNsmoke.oolong;

/** Enumerated type of instruction formats */
public class InstructionFormat
{
    private String name;
    private InstructionFormat(String name) { this.name = name; }

    public String toString()
    {
        return name;
    }

    /** There are no arguments */
    public static final InstructionFormat noArgs    = new InstructionFormat("noArgs");

    /** One-byte local variable index */
    public static final InstructionFormat localVar  = new InstructionFormat("localVar");

    /** One-byte local variable index (used with lload, lstore, dload, and dstore) */
    public static final InstructionFormat localVar2  = new InstructionFormat("localVar2");

    /** Two-byte index to a ConstantClass */
    public static final InstructionFormat constantClass    = new InstructionFormat("constantClass");

    /** Two-byte index to a ConstantFieldref */
    public static final InstructionFormat constantFieldref    = new InstructionFormat("constantFieldref");

    /** One-byte type indicator (used with newarray) */
    public static final InstructionFormat arrayType = new InstructionFormat("arrayType");

    /** Two-byte branch offset */
    public static final InstructionFormat localBranch = new InstructionFormat("localBranch");

    /** Four-byte branch offset (used with goto_w and jsr_w) */
    public static final InstructionFormat wideBranch = new InstructionFormat("wideBranch");

    /** Two-byte index to a ConstantInterfaceMethodref,
     * one-byte argument count, one-byte '0' for luck */
    public static final InstructionFormat invokeInterface = new InstructionFormat("invokeInterface");

    /** Two-byte index to a ConstantMethodref */
    public static final InstructionFormat constantMethodref = new InstructionFormat("constantMethodref");

    /** One-byte local variable number, one-byte increment */
    public static final InstructionFormat iinc = new InstructionFormat("iinc");

    /** One-byte index to a ConstantInteger, ConstantFloat, or ConstantString (used with ldc) */
    public static final InstructionFormat constant1 = new InstructionFormat("constant1");

    /** Two-byte index to a ConstantInteger, ConstantFloat, or ConstantString (used with ldc_w) */
    public static final InstructionFormat constant2 = new InstructionFormat("constant2");

    /** Two-byte index to a ConstantDouble or ConstantLong (used with ldc2_w) */
    public static final InstructionFormat constant2Wide = new InstructionFormat("constant2Wide");

    /** 2 bytes as a short */
    public static final InstructionFormat shortVal = new InstructionFormat("shortVal");

    /** Variable length instruction */
    public static final InstructionFormat lookupswitch = new InstructionFormat("lookupswitch");

    /** Variable length instruction */
    public static final InstructionFormat tableswitch = new InstructionFormat("tableswitch");

    /** Variable length instruction */
    public static final InstructionFormat wide = new InstructionFormat("wide");

    /** Two-byte index to a ConstantClass and one-byte array dimension count */
    public static final InstructionFormat multianewarray = new InstructionFormat("multianewarray");
}
