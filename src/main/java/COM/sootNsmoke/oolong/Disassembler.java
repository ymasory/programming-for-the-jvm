package COM.sootNsmoke.oolong;
import java.io.*;
import COM.sootNsmoke.jvm.*;


/** A micro-disassembler, for use in the DumpClass disassembly step */
public class Disassembler implements RuntimeConstants
{
    /** A list of instructions, including the mnemonics, opcodes, and
     * the format of the bytecode arguments.  See InstructionFormat for more
     * about each format.
     */
    public static Instruction inst[] =
    {
        new Instruction("aaload",       0x32, InstructionFormat.noArgs),
        new Instruction("aastore",      0x53, InstructionFormat.noArgs),
        new Instruction("aconst_null",  0x01, InstructionFormat.noArgs),
        new Instruction("aload",        0x19, InstructionFormat.localVar),
        new Instruction("aload_0",      0x2a, InstructionFormat.noArgs),
        new Instruction("aload_1",      0x2b, InstructionFormat.noArgs),
        new Instruction("aload_2",      0x2c, InstructionFormat.noArgs),
        new Instruction("aload_3",      0x2d, InstructionFormat.noArgs),
        new Instruction("anewarray",    0xbd, InstructionFormat.constantClass),
        new Instruction("areturn",      0xb0, InstructionFormat.noArgs),
        new Instruction("arraylength",  0xbe, InstructionFormat.noArgs),
        new Instruction("astore",       0x3a, InstructionFormat.localVar),
        new Instruction("astore_0",     0x4b, InstructionFormat.noArgs),
        new Instruction("astore_1",     0x4c, InstructionFormat.noArgs),
        new Instruction("astore_2",     0x4d, InstructionFormat.noArgs),
        new Instruction("astore_3",     0x4e, InstructionFormat.noArgs),
        new Instruction("athrow",       0xbf, InstructionFormat.noArgs),
        new Instruction("baload",       0x33, InstructionFormat.noArgs),
        new Instruction("bastore",      0x54, InstructionFormat.noArgs),
        new Instruction("bipush",       0x10, InstructionFormat.localVar),
        new Instruction("caload",       0x34, InstructionFormat.noArgs),
        new Instruction("castore",      0x55, InstructionFormat.noArgs),
        new Instruction("checkcast",    0xc0, InstructionFormat.constantClass),
        new Instruction("d2f",          0x90, InstructionFormat.noArgs),
        new Instruction("d2i",          0x8e, InstructionFormat.noArgs),
        new Instruction("d2l",          0x8f, InstructionFormat.noArgs),
        new Instruction("dadd",         0x63, InstructionFormat.noArgs),
        new Instruction("daload",       0x31, InstructionFormat.noArgs),
        new Instruction("dastore",      0x52, InstructionFormat.noArgs),
        new Instruction("dcmpg",        0x98, InstructionFormat.noArgs),
        new Instruction("dcmpl",        0x97, InstructionFormat.noArgs),
        new Instruction("dconst_0",     0x0e, InstructionFormat.noArgs),
        new Instruction("dconst_1",     0x0f, InstructionFormat.noArgs),
        new Instruction("ddiv",         0x6f, InstructionFormat.noArgs),
        new Instruction("dload",        0x18, InstructionFormat.localVar2),
        new Instruction("dload_0",      0x26, InstructionFormat.noArgs),
        new Instruction("dload_1",      0x27, InstructionFormat.noArgs),
        new Instruction("dload_2",      0x28, InstructionFormat.noArgs),
        new Instruction("dload_3",      0x29, InstructionFormat.noArgs),
        new Instruction("dmul",         0x6b, InstructionFormat.noArgs),
        new Instruction("dneg",         0x77, InstructionFormat.noArgs),
        new Instruction("drem",         0x73, InstructionFormat.noArgs),
        new Instruction("dreturn",      0xaf, InstructionFormat.noArgs),
        new Instruction("dstore",       0x39, InstructionFormat.localVar2),
        new Instruction("dstore_0",     0x47, InstructionFormat.noArgs),
        new Instruction("dstore_1",     0x48, InstructionFormat.noArgs),
        new Instruction("dstore_2",     0x49, InstructionFormat.noArgs),
        new Instruction("dstore_3",     0x4a, InstructionFormat.noArgs),
        new Instruction("dsub",         0x67, InstructionFormat.noArgs),
        new Instruction("dup",          0x59, InstructionFormat.noArgs),
        new Instruction("dup_x1",       0x5a, InstructionFormat.noArgs),
        new Instruction("dup_x2",       0x5b, InstructionFormat.noArgs),
        new Instruction("dup2",         0x5c, InstructionFormat.noArgs),
        new Instruction("dup2_x1",      0x5d, InstructionFormat.noArgs),
        new Instruction("dup2_x2",      0x5e, InstructionFormat.noArgs),
        new Instruction("f2d",          0x8d, InstructionFormat.noArgs),
        new Instruction("f2i",          0x8b, InstructionFormat.noArgs),
        new Instruction("f2l",          0x8c, InstructionFormat.noArgs),
        new Instruction("fadd",         0x62, InstructionFormat.noArgs),
        new Instruction("faload",       0x30, InstructionFormat.noArgs),
        new Instruction("fastore",      0x51, InstructionFormat.noArgs),
        new Instruction("fcmpg",        0x96, InstructionFormat.noArgs),
        new Instruction("fcmpl",        0x95, InstructionFormat.noArgs),
        new Instruction("fconst_0",     0x0b, InstructionFormat.noArgs),
        new Instruction("fconst_1",     0x0c, InstructionFormat.noArgs),
        new Instruction("fconst_2",     0x0d, InstructionFormat.noArgs),
        new Instruction("fdiv",         0x6e, InstructionFormat.noArgs),
        new Instruction("fload",        0x17, InstructionFormat.localVar),
        new Instruction("fload_0",      0x22, InstructionFormat.noArgs),
        new Instruction("fload_1",      0x23, InstructionFormat.noArgs),
        new Instruction("fload_2",      0x24, InstructionFormat.noArgs),
        new Instruction("fload_3",      0x25, InstructionFormat.noArgs),
        new Instruction("fmul",         0x6a, InstructionFormat.noArgs),
        new Instruction("fneg",         0x76, InstructionFormat.noArgs),
        new Instruction("frem",         0x72, InstructionFormat.noArgs),
        new Instruction("freturn",      0xae, InstructionFormat.noArgs),
        new Instruction("fstore",       0x38, InstructionFormat.localVar),
        new Instruction("fstore_0",     0x43, InstructionFormat.noArgs),
        new Instruction("fstore_1",     0x44, InstructionFormat.noArgs),
        new Instruction("fstore_2",     0x45, InstructionFormat.noArgs),
        new Instruction("fstore_3",     0x46, InstructionFormat.noArgs),
        new Instruction("fsub",         0x66, InstructionFormat.noArgs),
        new Instruction("getfield",     0xb4, InstructionFormat.constantFieldref),
        new Instruction("getstatic",    0xb2, InstructionFormat.constantFieldref),
        new Instruction("goto",         0xa7, InstructionFormat.localBranch),
        new Instruction("goto_w",       0xc8, InstructionFormat.wideBranch),
        new Instruction("i2b",          0x91, InstructionFormat.noArgs),
        new Instruction("i2c",          0x92, InstructionFormat.noArgs),
        new Instruction("i2d",          0x87, InstructionFormat.noArgs),
        new Instruction("i2f",          0x86, InstructionFormat.noArgs),
        new Instruction("i2l",          0x85, InstructionFormat.noArgs),
        new Instruction("i2s",          0x93, InstructionFormat.noArgs),
        new Instruction("iadd",         0x60, InstructionFormat.noArgs),
        new Instruction("iaload",       0x2e, InstructionFormat.noArgs),
        new Instruction("iand",         0x7e, InstructionFormat.noArgs),
        new Instruction("iastore",      0x4f, InstructionFormat.noArgs),
        new Instruction("iconst_m1",    0x02, InstructionFormat.noArgs),
        new Instruction("iconst_0",     0x03, InstructionFormat.noArgs),
        new Instruction("iconst_1",     0x04, InstructionFormat.noArgs),
        new Instruction("iconst_2",     0x05, InstructionFormat.noArgs),
        new Instruction("iconst_3",     0x06, InstructionFormat.noArgs),
        new Instruction("iconst_4",     0x07, InstructionFormat.noArgs),
        new Instruction("iconst_5",     0x08, InstructionFormat.noArgs),
        new Instruction("idiv",         0x6c, InstructionFormat.noArgs),
        new Instruction("if_acmpeq",    0xa5, InstructionFormat.localBranch),
        new Instruction("if_acmpne",    0xa6, InstructionFormat.localBranch),
        new Instruction("if_icmpeq",    0x9f, InstructionFormat.localBranch),
        new Instruction("if_icmpne",    0xa0, InstructionFormat.localBranch),
        new Instruction("if_icmplt",    0xa1, InstructionFormat.localBranch),
        new Instruction("if_icmpge",    0xa2, InstructionFormat.localBranch),
        new Instruction("if_icmpgt",    0xa3, InstructionFormat.localBranch),
        new Instruction("if_icmple",    0xa4, InstructionFormat.localBranch),
        new Instruction("ifeq",         0x99, InstructionFormat.localBranch),
        new Instruction("ifne",         0x9a, InstructionFormat.localBranch),
        new Instruction("iflt",         0x9b, InstructionFormat.localBranch),
        new Instruction("ifge",         0x9c, InstructionFormat.localBranch),
        new Instruction("ifgt",         0x9d, InstructionFormat.localBranch),
        new Instruction("ifle",         0x9e, InstructionFormat.localBranch),
        new Instruction("ifnonnull",    0xc7, InstructionFormat.localBranch),
        new Instruction("ifnull",       0xc6, InstructionFormat.localBranch),
        new Instruction("iinc",         0x84, InstructionFormat.iinc),
        new Instruction("iload",        0x15, InstructionFormat.localVar),
        new Instruction("iload_0",      0x1a, InstructionFormat.noArgs),
        new Instruction("iload_1",      0x1b, InstructionFormat.noArgs),
        new Instruction("iload_2",      0x1c, InstructionFormat.noArgs),
        new Instruction("iload_3",      0x1d, InstructionFormat.noArgs),
        new Instruction("imul",         0x68, InstructionFormat.noArgs),
        new Instruction("ineg",         0x74, InstructionFormat.noArgs),
        new Instruction("instanceof",   0xc1, InstructionFormat.constantClass),
        new Instruction("invokeinterface",      0xb9, InstructionFormat.invokeInterface),
        new Instruction("invokespecial",        0xb7, InstructionFormat.constantMethodref),
        new Instruction("invokestatic",         0xb8, InstructionFormat.constantMethodref),
        new Instruction("invokevirtual",        0xb6, InstructionFormat.constantMethodref),
        new Instruction("ior",          0x80, InstructionFormat.noArgs),
        new Instruction("irem",         0x70, InstructionFormat.noArgs),
        new Instruction("ireturn",      0xac, InstructionFormat.noArgs),
        new Instruction("ishl",         0x78, InstructionFormat.noArgs),
        new Instruction("ishr",         0x7a, InstructionFormat.noArgs),
        new Instruction("istore",       0x36, InstructionFormat.localVar),
        new Instruction("istore_0",     0x3b, InstructionFormat.noArgs),
        new Instruction("istore_1",     0x3c, InstructionFormat.noArgs),
        new Instruction("istore_2",     0x3d, InstructionFormat.noArgs),
        new Instruction("istore_3",     0x3e, InstructionFormat.noArgs),
        new Instruction("isub",         0x64, InstructionFormat.noArgs),
        new Instruction("iushr",        0x7c, InstructionFormat.noArgs),
        new Instruction("ixor",         0x82, InstructionFormat.noArgs),
        new Instruction("jsr",          0xa8, InstructionFormat.localBranch),
        new Instruction("jsr_w",        0xc9, InstructionFormat.wideBranch),
        new Instruction("l2d",          0x8a, InstructionFormat.noArgs),
        new Instruction("l2f",          0x89, InstructionFormat.noArgs),
        new Instruction("l2i",          0x88, InstructionFormat.noArgs),
        new Instruction("ladd",         0x61, InstructionFormat.noArgs),
        new Instruction("laload",       0x2f, InstructionFormat.noArgs),
        new Instruction("land",         0x7f, InstructionFormat.noArgs),
        new Instruction("lastore",      0x50, InstructionFormat.noArgs),
        new Instruction("lcmp",         0x94, InstructionFormat.noArgs),
        new Instruction("lconst_0",     0x09, InstructionFormat.noArgs),
        new Instruction("lconst_1",     0x0a, InstructionFormat.noArgs),
        new Instruction("ldc",          0x12, InstructionFormat.constant1),
        new Instruction("ldc_w",        0x13, InstructionFormat.constant2),
        new Instruction("ldc2_w",       0x14, InstructionFormat.constant2Wide),
        new Instruction("ldiv",         0x6d, InstructionFormat.noArgs),
        new Instruction("lload",        0x16, InstructionFormat.localVar2),
        new Instruction("lload_0",      0x1e, InstructionFormat.noArgs),
        new Instruction("lload_1",      0x1f, InstructionFormat.noArgs),
        new Instruction("lload_2",      0x20, InstructionFormat.noArgs),
        new Instruction("lload_3",      0x21, InstructionFormat.noArgs),
        new Instruction("lmul",         0x69, InstructionFormat.noArgs),
        new Instruction("lneg",         0x75, InstructionFormat.noArgs),
        new Instruction("lookupswitch", 0xab, InstructionFormat.lookupswitch),
        new Instruction("lor",          0x81, InstructionFormat.noArgs),
        new Instruction("lrem",         0x71, InstructionFormat.noArgs),
        new Instruction("lreturn",      0xad, InstructionFormat.noArgs),
        new Instruction("lshl",         0x79, InstructionFormat.noArgs),
        new Instruction("lshr",         0x7b, InstructionFormat.noArgs),
        new Instruction("lstore",       0x37, InstructionFormat.localVar2),
        new Instruction("lstore_0",     0x3f, InstructionFormat.noArgs),
        new Instruction("lstore_1",     0x40, InstructionFormat.noArgs),
        new Instruction("lstore_2",     0x41, InstructionFormat.noArgs),
        new Instruction("lstore_3",     0x42, InstructionFormat.noArgs),
        new Instruction("lsub",         0x65, InstructionFormat.noArgs),
        new Instruction("lushr",        0x7d, InstructionFormat.noArgs),
        new Instruction("lxor",         0x83, InstructionFormat.noArgs),
        new Instruction("monitorenter", 0xc2, InstructionFormat.noArgs),
        new Instruction("monitorexit",  0xc3, InstructionFormat.noArgs),
        new Instruction("multianewarray",0xc5, InstructionFormat.multianewarray),
        new Instruction("new",          0xbb, InstructionFormat.constantClass),
        new Instruction("newarray",     0xbc, InstructionFormat.arrayType),
        new Instruction("nop",          0x00, InstructionFormat.noArgs),
        new Instruction("pop",          0x57, InstructionFormat.noArgs),
        new Instruction("pop2",         0x58, InstructionFormat.noArgs),
        new Instruction("putfield",     0xb5, InstructionFormat.constantFieldref),
        new Instruction("putstatic",    0xb3, InstructionFormat.constantFieldref),
        new Instruction("ret",          0xa9, InstructionFormat.localVar),
        new Instruction("return",       0xb1, InstructionFormat.noArgs),
        new Instruction("saload",       0x35, InstructionFormat.noArgs),
        new Instruction("sastore",      0x56, InstructionFormat.noArgs),
        new Instruction("sipush",       0x11, InstructionFormat.shortVal),
        new Instruction("swap",         0x5f, InstructionFormat.noArgs),
        new Instruction("tableswitch",  0xaa, InstructionFormat.tableswitch),
        new Instruction("wide",         0xc4, InstructionFormat.wide)
    };

    /** Array of instructions, indexed by opcode.
     * @see #inst
     */
    static public Instruction[] ops = null;

    static
    {
        ops = new Instruction[256];
        for(int i = 0; i < inst.length; i++)
            ops[inst[i].opcode] = inst[i];
    }

    static int offset = 0;

    /** Disassembles the input stream up to n bytes, and write a disassembly to os
     * @param classfile classfile against which to resolve constants, or null
     */
    static void disassemble(InputStream is, PrintStream os,
                            ClassFile cf) throws IOException
    {
        int opcode = 0;
        offset = 0;
        while((opcode = is.read()) != -1)
        {
            offset++;
            if(ops[opcode] != null)
            {
                if(ops[opcode].format == InstructionFormat.noArgs)
                    disassembleNoArgs(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.localVar)
                    disassembleVar(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.localVar2)
                    disassembleVar(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.constantClass)
                    disassembleRef(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.constantFieldref)
                    disassembleRef(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.constantMethodref)
                    disassembleRef(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.arrayType)
                    disassembleArrayType(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.localBranch)
                    disassembleShortAddr(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.wideBranch)
                    ;
                else if(ops[opcode].format == InstructionFormat.invokeInterface)
                    ;
                else if(ops[opcode].format == InstructionFormat.iinc)
                    disassembleIinc(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.constant1)
                    disassembleLdc(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.constant2)
                    ;
                else if(ops[opcode].format == InstructionFormat.constant2Wide)
                    ;
                else if(ops[opcode].format == InstructionFormat.shortVal)
                    disassembleShortVal(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.lookupswitch)
                    disassembleLookupswitch(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.tableswitch)
                    disassembleTableswitch(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.wide)
                    disassembleWide(is, os,  opcode);
                else if(ops[opcode].format == InstructionFormat.multianewarray)
                    disassembleMultianewarray(is, os,  opcode);
                else
                    throw new RuntimeException("Internal error.  Unknown instruction format " + ops[opcode].format);
            }
            else
            {
                os.println("Unknown opcode: " + opcode);
            }
        }
    }

    static void disassembleIinc(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        int index = is.read();
        int constant = is.read();
        offset += 2;

        hexdump(opcode, index, constant);
        os.println((offset-1) + " " + ops[opcode].mnemonic +
            " " + index + " " + constant);
    }

    static void disassembleShortAddr(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        int loc = offset-1;
        int b1 = is.read();
        int b2 = is.read();

        hexdump(opcode, b1, b2);
        int branchoffset = makeUnsignedInt(b1, b2);
        os.print(offset-1 + " ");
        os.println(ops[opcode].mnemonic + " " + (branchoffset + loc));
        offset += 2;
    }

    static void disassembleShortVal(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        int b1 = is.read();
        int b2 = is.read();

        hexdump(opcode, b1, b2);
        int val = makeUnsignedInt(b1, b2);
        os.print(offset-1 + " ");
        os.println(ops[opcode].mnemonic + " " + val);
        offset += 2;
    }

    static void disassembleArrayType(InputStream is, PrintStream os,
        int opcode)
        throws IOException

    {
        int b = is.read();
        hexdump(opcode, b);
        //### change b to appropriate type
        os.println(offset-1 + " " + ops[opcode].mnemonic + " " + b);
        offset++;
    }

    static void disassembleRef(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        int b1 = is.read();
        int b2 = is.read();

        hexdump(opcode, b1, b2);
        os.print(offset-1 + " " + ops[opcode].mnemonic);

        if(b1 == -1 || b2 == -1)
            os.println("<unknown args>");
        else
        {
            int ref = (b1 << 4 | b2);
            os.println(" #" + ref);
        }
        offset += 2;

    }

    static void disassembleNoArgs(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        hexdump(opcode);
        os.print(offset-1 + " ");
        os.println(ops[opcode].mnemonic);
    }

    static void disassembleVar(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        int b = is.read();

        hexdump(opcode, b);
        os.println(offset-1 + " " + ops[opcode].mnemonic + " " + b);
        offset++;
    }


    static void disassembleLdc(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        int b1 = is.read();
        if(b1 < 0)
            b1 += 256;

        hexdump(opcode, b1);
        os.print(offset-1 + " ");
        os.println(ops[opcode].mnemonic + " #" + b1);
        offset += 1;
    }

    static void disassembleWide(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        int subopc = is.read();
        if(subopc < 0)
            subopc += 256;

        // 84 = iinc
        if(subopc == 0x84)
        {
            byte[] b = new byte[6];
            is.read(b,2,4);
            b[0] = (byte) opcode;
            b[1] = (byte) subopc;
            DumpClass.hexdump(b);
            int index = makeUnsignedInt(b[2], b[3]);
            int inc = makeUnsignedInt(b[4], b[5]);
            os.print(offset-1 + " ");
            os.println(ops[opcode].mnemonic + " " +
                       ops[subopc].mnemonic + " #" + index + " " + inc);
            offset += 5;
        }
        else
        {
            byte[] b = new byte[4];
            is.read(b,2,2);
            b[0] = (byte) opcode;
            b[1] = (byte) subopc;
            int index = makeUnsignedInt(b[2], b[3]);
            DumpClass.hexdump(b);
            os.print(offset-1 + " ");
            os.println(ops[opcode].mnemonic + " " +
                       ops[subopc].mnemonic + " #" + index);
            offset += 5;
        }

    }

    /** Takes a high byte and a low byte and returns the
     * value of the 2-byte unsigned int number */
    public static int makeUnsignedInt(int a, int b)
    {
        if(a < 0)
            a += 256;
        a = a << 8;
        a += b;
        if(b < 0)
            a += 256;
        return a;
    }
    static void disassembleMultianewarray(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        byte[] b = new byte[4];
        is.read(b,1,3);
        b[0] = (byte) opcode;


        int index = makeUnsignedInt(b[1], b[2]);
        DumpClass.hexdump(b);
        os.print(offset-1 + " ");
        os.println(ops[opcode].mnemonic + " #" + index + " " + b[3]);
        offset += 3;
    }

    static void disassembleTableswitch(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        int loc = offset-1;

        int pad = 0;
        // Read padding
        while(offset % 4 != 0)
        {
            int b1 = is.read();
            offset++;
            if(b1 < 0)
                b1 += 256;
            pad++;
        }

        byte[] b = new byte[1+pad];
        b[0] = (byte) opcode;
        DumpClass.hexdump(b);
        os.print(loc + " ");
        os.println(ops[opcode].mnemonic);

        // Read default, low, and high
        byte[] b1 = new byte[4];
        byte[] b2 = new byte[8];
        is.read(b1, 0, 4);
        is.read(b2, 0, 8);
        offset += 12;

        int dflt = 0, low = 0, high = 0;
        for(int i = 0; i < 4; i++)
        {
            dflt = dflt << 8;
            dflt += b1[i];
            if(b1[i] < 0)
                dflt += 256;
        }
        for(int i = 0; i < 4; i++)
        {
            low = low << 8;
            low += b2[i];
            if(b2[i] < 0)
                low += 256;
        }
        for(int i = 4; i < 8; i++)
        {
            high = high << 8;
            high += b2[i];
            if(b2[i] < 0)
                high += 256;
        }


        DumpClass.hexdump(b1);
        os.print("     ");
        os.println("default: " + dflt);
        DumpClass.hexdump(b2);
        os.print("     ");
        os.println("low: " + low + " high: " + high);

        for(int i = low; i < high+1; i++)
        {
            b = new byte[4];

            // Read branch location
            is.read(b, 0, 4);
            offset += 4;

            int branch = 0;
            for(int j = 0; j < 4; j++)
            {
                branch = branch << 8;
                branch += b[j];
                if(b[j] < 0)
                    branch += 256;
            }

            DumpClass.hexdump(b);
            os.print("  ");
            os.println("    case " + i + ": " + (loc + branch));
        }
    }
    static void disassembleLookupswitch(InputStream is, PrintStream os,
        int opcode)
        throws IOException
    {
        int loc = offset-1;

        int pad = 0;
        // Read padding
        while(offset % 4 != 0)
        {
            int b1 = is.read();
            offset++;
            if(b1 < 0)
                b1 += 256;
            pad++;
        }

        byte[] b = new byte[1+pad];
        b[0] = (byte) opcode;
        DumpClass.hexdump(b);
        os.print(loc + " ");
        os.println(ops[opcode].mnemonic);

        // Read default & npairs;
        b = new byte[8];
        is.read(b, 0, 8);
        offset += 8;

        int dflt = 0, npairs = 0;
        for(int i = 0; i < 4; i++)
        {
            dflt = dflt << 8;
            dflt += b[i];
            if(b[i] < 0)
                dflt += 256;
        }
        for(int i = 4; i < 8; i++)
        {
            npairs = npairs << 8;
            npairs += b[i];
            if(b[i] < 0)
                npairs += 256;
        }


        DumpClass.hexdump(b);
        os.print("     ");
        os.println(" default: " +
                         dflt + " npairs: " + npairs);

        for(int i = 0; i < npairs; i++)
        {
            b = new byte[8];

            // Read match and offset
            is.read(b, 0, 8);
            offset += 8;

            int match = 0, branch = 0;
            for(int j = 0; j < 4; j++)
            {
                match = match << 8;
                match += b[j];
                if(b[j] < 0)
                    match += 256;
            }
            for(int j = 4; j < 8; j++)
            {
                branch = branch << 8;
                branch += b[j];
                if(b[j] < 0)
                    branch += 256;
            }

            DumpClass.hexdump(b);
            os.print("  ");
            os.println("    case " + match + ": " + (loc + branch));
        }
    }

    /** Some quick&dirty functions to make hex dumping easier */
    static void hexdump(int x1, int x2, int x3)
    {
        byte[] bytes = { (byte) x1, (byte) x2, (byte) x3 };
        DumpClass.hexdump(bytes);
    }

    static void hexdump(int x1, int x2)
    {
        byte[] bytes = { (byte) x1, (byte) x2 };
        DumpClass.hexdump(bytes);
    }

    static void hexdump(int x1)
    {
        byte[] bytes = { (byte) x1 };
        DumpClass.hexdump(bytes);
    }
}
