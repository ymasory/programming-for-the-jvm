package COM.sootNsmoke.oolong;
import java.io.*;
import COM.sootNsmoke.jvm.*;

/** A Oolong disassembler. Reads in a .class file and produces
 * a disassembly in the Oolong language.  The Oolong assembler
 * can read this file and produce a class file equivalent (but not
 * necessarily identical) to the original
 */
public class Gnoloo implements RuntimeConstants
{
    private static ClassFile cf;
    static PrintStream out;
    static boolean printConstants = false;

    private static String directory = ".";

    /** Used to keep track of the relevant line number table */
    static LineNumberTableEntry[] lnt;

    /** For an explanation, of the parameters, see usage().
     * @see #usage
     */
    public static void main(String a[])
    {
        boolean useStdout = false;
        for(int i = 0; i < a.length; i++)
        {
            try
            {
                if(a[i].equals("-"))
                {
                    useStdout = true;
                }
                else if(a[i].equals("-d"))
                {
                    i++;
                    if(i >= a.length)
                        usage();
                    else
                        directory = a[i];
                }
                else if(a[i].equals("-c"))
                    printConstants = true;
                else
                {
                    cf = new ClassFile();
                    InputStream is = new FileInputStream(a[i]);
                    cf.read(is);
                    if(useStdout)
                        out = System.out;
                    else
                    {
                        out = null;
                        GenericConstant c = cf.getConstant(cf.getThis());
                        if(c instanceof ConstantClass)
                        {
                            c = cf.getConstant(((ConstantClass) c).name_index());
                            if(c instanceof ConstantUtf8)
                            {
                                String classname = ((ConstantUtf8) c).getValue();
                                out = new PrintStream(
                                    new FileOutputStream(
                                        outputFileName(classname)));
                            }
                        }
                        if(out == null)
                            throw new Exception("Invalid class file format: " + a[i]);
                    }
                    if(out == null)
                        System.err.println("Invalid class file format");
                    else
                        disassemble();
                }
            }
            catch(IOException e)
            {
                System.err.println("Could not open file: " + e.getMessage());
            }
            catch(Exception e)
            {
                System.err.println(e.getMessage());
            }
        }
    }

    /** Compute the name of the output file name.
     * @param input The input file name.
     */
    public static String outputFileName(String input)
    {
        int i = input.lastIndexOf('/');
        return directory + "/" + input.substring(i+1) + ".j";
    }

    /** Produces a usage message
     * Gnoloo [-d directory] [-] file1.class ...
     *    -d write files in directory"
     *"    -  write output to System.out"
     */
    public static void usage()
    {
        System.err.println("Gnoloo - a Java class file disassembler");
        System.err.println("Usage:");
        System.err.println("    Gnoloo [-d directory] [-] file1.class ...");
        System.err.println("    -d write files in directory");
        System.err.println("    -  write output to System.out");
    }

    /** Produce a disassembly */
    public static void disassemble()
    {
        if(printConstants)
        {
            for(int i = 1; i < cf.getConstantPoolLength(); i++)
            {
                GenericConstant c = cf.getConstant(i);
                out.println("; #" + i + " " + c);
                if(c instanceof ConstantDouble || c instanceof ConstantLong)
                    i++;
            }
        }
        printSourceDirective();

        ConstantClass this_class = (ConstantClass) cf.getConstant(cf.getThis());
        ConstantClass super_class = (ConstantClass) cf.getConstant(cf.getSuper());
        out.print(".class ");
        printFlags(cf.getAccessFlags(), true);
        out.print(cf.utf8(this_class.name_index()));
        if(printConstants)
            out.print(" ;# " + this_class.name_index());
        out.println();
        out.print(".super " + cf.utf8(super_class.name_index()));
        if(printConstants)
            out.print(" ;# " + super_class.name_index());
        out.println();

        out.println();

        for(int i = 0; i < cf.getFields().length; i++)
            disassemble(cf.getFields()[i]);
        out.println();

        for(int i = 0; i < cf.getMethods().length; i++)
            disassemble(cf.getMethods()[i]);
    }

    /** Finds any SouceFile attributes and uses them to print
     * the .source directive
     */
    public static void printSourceDirective()
    {
        for(int i = 0; i < cf.getAttributes().length; i++)
        {
            Attribute attr = cf.getAttributes()[i];
            if(attr instanceof SourceFileAttribute)
            {
                SourceFileAttribute sfa = (SourceFileAttribute) attr;
                out.println(".source " + cf.utf8(sfa.sourcefile_index()));
            }
        }
    }

    /** Produce a .field directive from the FieldInfo structure */
    public static void disassemble(FieldInfo f)
    {
        out.print(".field ");
        printFlags(f.access_flags(), false);
        out.print(cf.utf8(f.name_index()) + " ");
        out.print(cf.utf8(f.descriptor_index()));
        for(int i = 0; i < f.attributes().length; i++)
            if(f.attributes()[i] instanceof ConstantValueAttribute)
            {
                out.print(" = ");
                int index = ((ConstantValueAttribute)
                    f.attributes()[i]).constantvalue_index();
                GenericConstant init = cf.getConstant(index);
                printValue(init);
            }
        if(printConstants)
            out.print(" ; #" + f.name_index() + " #" + f.descriptor_index());
        // ### Add stuff for ConstantValueAttribute, if present
        out.println();
    }

    /** Print the value of the constant */
    public static void printValue(GenericConstant init)
    {
            if(init instanceof ConstantDouble)
                out.print(((ConstantDouble) init).value());
            else if(init instanceof ConstantFloat)
                out.print(((ConstantFloat) init).value());
            else if(init instanceof ConstantInteger)
                out.print(((ConstantInteger) init).value());
            else if(init instanceof ConstantLong)
                out.print(((ConstantLong) init).value());
            else if(init instanceof ConstantString)
                out.print("\"" + cf.utf8(((ConstantString) init).string_index()) + "\"");
    }


    /** Print the keywords corresponding to the set flags.
     * @param class_flags  If set, prints super instead of synchronized, which
     *                     actually have the same value in the class file
     */
    public static void printFlags(int flags, boolean class_flags)
    {
        if((flags & ACC_PUBLIC) != 0)
            out.print("public ");
        if((flags & ACC_PRIVATE) != 0)
            out.print("private ");
        if((flags & ACC_PROTECTED) != 0)
            out.print("protected ");
        if((flags & ACC_STATIC) != 0)
            out.print("static ");
        if((flags & ACC_FINAL) != 0)
            out.print("final ");
        if((flags & ACC_SYNCHRONIZED) != 0 && !class_flags)
            out.print("synchronized ");
        if((flags & ACC_SUPER) != 0 && class_flags)
            out.print("super ");
        if((flags & ACC_VOLATILE) != 0)
            out.print("volatile ");
        if((flags & ACC_TRANSIENT) != 0)
            out.print("transient ");
        if((flags & ACC_NATIVE) != 0)
            out.print("native ");
        if((flags & ACC_INTERFACE) != 0)
            out.print("interface ");
        if((flags & ACC_ABSTRACT) != 0)
            out.print("abstract ");
	if((flags & ACC_STRICTFP) != 0)
	    out.print("strictfp ");
    }

    public static void disassemble(MethodInfo m)
    {
        out.print(".method ");
        printFlags(m.access_flags(), false);
        out.print(cf.utf8(m.name_index()) + " " +
                    cf.utf8(m.descriptor_index()));
        if(printConstants)
            out.print(" ; #" + m.name_index() + " #" + m.descriptor_index());
        out.println();
        for(int i = 0; i < m.attributes().length; i++)
        {

            if(m.attributes()[i] instanceof CodeAttribute)
                disassemble((CodeAttribute) m.attributes()[i]);
            else if(m.attributes()[i] instanceof ExceptionsAttribute)
                disassemble((ExceptionsAttribute) m.attributes()[i]);
        }
        out.println(".end method");
        out.println();
    }

    public static void disassemble(CodeAttribute code)
    {
        out.println(".limit stack " + code.max_stack());
        out.println(".limit locals " + code.max_locals());

        disassemble(code.exception_table());

        Attribute[] attributes = code.attributes();

        lnt = new LineNumberTableEntry[0];
        for(int i = 0; i < attributes.length; i++)
        {
            if(attributes[i] instanceof LocalVariableTableAttribute)
            {
                disassemble((LocalVariableTableAttribute) attributes[i]);
            }
            else if(attributes[i] instanceof LineNumberTableAttribute)
            {
                disassemble((LineNumberTableAttribute) attributes[i]);
            }
        }

        disassemble(code.code());

        out.println();
    }

    public static void disassemble(ExceptionTableEntry[] excps)
    {
        for(int i = 0; i < excps.length; i++)
        {
            String classname = "all";
            if(excps[i].catch_type() != 0)
            {
                ConstantClass cc = (ConstantClass) cf.getConstant(excps[i].catch_type());
                classname = cf.utf8(cc.name_index());
            }
            String label1 = "l" + excps[i].start_pc();
            String label2 = "l" + excps[i].end_pc();
            String label3 = "l" + excps[i].handler_pc();
            out.println(".catch " + classname + " from " + label1 + " to " +
                label2 + " using " + label3);
        }

    }

    public static void disassemble(LineNumberTableAttribute attr)
    {
        lnt = attr.line_number_table();
    }

    public static void disassemble(LocalVariableTableAttribute attr)
    {
        LocalVariableTableEntry[] lvt = attr.local_variable_table();
        for(int i = 0; i < lvt.length; i++)
        {
            int var_number = lvt[i].index();
            String name = cf.utf8(lvt[i].name_index());
            String signature = cf.utf8(lvt[i].descriptor_index());
            String label1 = "l" + lvt[i].start_pc();
            String label2 = "l" + (lvt[i].start_pc() + lvt[i].length());
            out.println(".var " + var_number + " is " + name + " " +
                signature + " from " + label1 + " to " + label2);
        }
    }


    public static void disassemble(ExceptionsAttribute attr)
    {
        int[] excps = attr.exception_index_table();
        for(int i = 0; i < excps.length; i++)
        {
            out.print(".throws ");
            ConstantClass cls = (ConstantClass) cf.getConstant(excps[i]);
            out.println(cf.utf8(cls.name_index()));
        }
    }


    static int offset = 0;
    static byte[] code;

    public static void disassemble(byte[] code)
    {
        Gnoloo.code = code;
        offset = 0;
        while(offset < code.length)
        {
            // Print out line number, if any
            for(int i = 0; i < lnt.length; i++)
            {
                if(lnt[i].start_pc() == offset)
                {
                    out.println(".line " + lnt[i].line_number());
                    break;
                }
            }
            out.print("l" + offset + ":    ");
            int opcode = code[offset++];
            if(opcode < 0)
                opcode += 256;
            if(Disassembler.ops[opcode] != null)
            {
                if(Disassembler.ops[opcode].format == InstructionFormat.noArgs)
                    disassembleNoArgs(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.localVar)
                    disassembleVar(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.localVar2)
                    disassembleVar(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.constantClass)
                    disassembleRef(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.constantFieldref)
                    disassembleRef(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.constantMethodref)
                    disassembleRef(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.arrayType)
                    disassembleArrayType(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.localBranch)
                    disassembleShortAddr(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.wideBranch)
                    disassembleWideAddr(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.invokeInterface)
                    disassembleInvokeInterface(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.iinc)
                    disassembleIinc(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.constant1)
                    disassembleLdc(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.constant2)
                    disassembleLdcW(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.constant2Wide)
                    disassembleLdc2W(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.shortVal)
                    disassembleShortVal(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.lookupswitch)
                    disassembleLookupSwitch(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.tableswitch)
                    disassembleTableSwitch(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.wide)
                    disassembleWide(opcode);
                else if(Disassembler.ops[opcode].format == InstructionFormat.multianewarray)
                    disassembleMultianewarray(opcode);
                else
                    throw new RuntimeException("Internal error.  Unknown instruction format " + Disassembler.ops[opcode].format);
                out.println();
            }
            else
            {
                out.println("Unknown opcode: " + opcode);
            }
        }
    }


    static void disassembleLdc(int opcode)
    {
        int index = code[offset++];
        if(index < 0)
            index += 256;
        GenericConstant val = cf.getConstant(index);

        out.print(Disassembler.ops[opcode].mnemonic + " ");
        printValue(val);
    }

    static void disassembleLdcW(int opcode)
    {
        int b1 = code[offset++];
        int b2 = code[offset++];
        if(b1 < 0) b1 += 256;
        if(b2 < 0) b2 += 256;

        int index = ((b1 << 8) | b2);
        GenericConstant val = cf.getConstant(index);

        out.print(Disassembler.ops[opcode].mnemonic + " ");
        printValue(val);
    }

    static void disassembleLdc2W(int opcode)
    {
        int b1 = code[offset++];
        int b2 = code[offset++];
        if(b1 < 0) b1 += 256;
        if(b2 < 0) b2 += 256;

        int index = ((b1 << 8) | b2);
        GenericConstant val = cf.getConstant(index);

        out.print(Disassembler.ops[opcode].mnemonic + " ");
        printValue(val);
    }

    static void disassembleIinc(int opcode)
    {
        int index = code[offset++];
        int constant = code[offset++];

        out.print(Disassembler.ops[opcode].mnemonic +
            " " + index + " " + constant);
    }

    static void disassembleShortAddr(int opcode)
    {
        int loc = offset-1;
        int b1 = code[offset++];
        int b2 = code[offset++];
        if(b1 < 0) b1 += 256;
        if(b2 < 0) b2 += 256;

        short branchoffset = (short) ((b1 << 8) | b2);
        out.print(Disassembler.ops[opcode].mnemonic + " l" + (branchoffset + loc));
    }

    static void disassembleWideAddr(int opcode)
    {
        out.print("wide ");
        int loc = offset-1;
        int b1 = code[offset++];
        int b2 = code[offset++];
        int b3 = code[offset++];
        int b4 = code[offset++];
        if(b1 < 0) b1 += 256;
        if(b2 < 0) b2 += 256;
        if(b3 < 0) b3 += 256;
        if(b4 < 0) b4 += 256;

        int branchoffset = (int) ((b1 << 24) | (b2 << 16) | (b3 << 8) | b4);
        out.print(Disassembler.ops[opcode].mnemonic + " l" + (branchoffset + loc));
    }


    static void disassembleShortVal(int opcode)
    {
        int loc = offset-1;
        int b1 = code[offset++];
        int b2 = code[offset++];
        if(b1 < 0) b1 += 256;
        if(b2 < 0) b2 += 256;

        int val = (int) ((b1 << 8) | b2);
        out.print(Disassembler.ops[opcode].mnemonic + " " + val);
    }

    static void disassembleRef(int opcode)
    {
        int b1 = code[offset++];
        int b2 = code[offset++];
        if(b1 < 0) b1 += 256;
        if(b2 < 0) b2 += 256;

        out.print(Disassembler.ops[opcode].mnemonic);
        out.print(" ");

        int ref = (b1 << 8 | b2);
        GenericConstant constant = cf.getConstant(ref);
        if(constant instanceof ConstantClass)
        {
            out.print(cf.utf8(((ConstantClass) constant).name_index()));
            if(printConstants)
                out.print(" ; #" + ((ConstantClass) constant).name_index());
        }
        else if(constant instanceof ConstantRef)
        {
            ConstantRef gr = (ConstantRef) constant;
            ConstantClass cc = (ConstantClass) cf.getConstant(gr.class_index());
            ConstantNameAndType nt = (ConstantNameAndType)
                cf.getConstant(gr.name_and_type_index());
            out.print(cf.utf8(cc.name_index()) + "/" +
                      cf.utf8(nt.name_index()) + " " +
                      cf.utf8(nt.descriptor_index()));
            if(printConstants)
            {
                out.print(" ;#" + cc.name_index() +
                          "/#" + nt.name_index() +
                          " #" + nt.descriptor_index());
            }
        }
        else
            out.print("; Error in class file: incorrect constant #" + ref +
                " " + constant);

    }


    static void disassembleInvokeInterface(int opcode)
    {
        disassembleRef(opcode);
        int nargs = code[offset++];
        int zero = code[offset++];
        out.print(" " + nargs);
    }


    static void disassembleNoArgs(int opcode)
    {
        out.print(Disassembler.ops[opcode].mnemonic);
    }

    static void disassembleVar(int opcode)
    {
        int b = code[offset++];

        out.print(Disassembler.ops[opcode].mnemonic + " " + b);
    }

    static void disassembleLookupSwitch(int opcode)
    {
        int loc = offset - 1;

        out.println(Disassembler.ops[opcode].mnemonic);
        // Skip over padding
        switch(offset % 4)
        {
            case 1: offset++;
            case 2: offset++;
            case 3: offset++;
        }
        int defaultb1 = code[offset++];
        int defaultb2 = code[offset++];
        int defaultb3 = code[offset++];
        int defaultb4 = code[offset++];
        if(defaultb1 < 0) defaultb1 += 256;
        if(defaultb2 < 0) defaultb2 += 256;
        if(defaultb3 < 0) defaultb3 += 256;
        if(defaultb4 < 0) defaultb4 += 256;
        int dflt = (int) ((defaultb1 << 24) | (defaultb2 << 16) | (defaultb3 << 8) | defaultb4);

        int npairsb1 = code[offset++];
        int npairsb2 = code[offset++];
        int npairsb3 = code[offset++];
        int npairsb4 = code[offset++];
        if(npairsb1 < 0) npairsb1 += 256;
        if(npairsb2 < 0) npairsb2 += 256;
        if(npairsb3 < 0) npairsb3 += 256;
        if(npairsb4 < 0) npairsb4 += 256;
        int npairs = (int) ((npairsb1 << 24) | (npairsb2 << 16) | (npairsb3 << 8) | npairsb4);

        for(int i = 0; i < npairs; i++)
        {

            int matchb1 = code[offset++];
            int matchb2 = code[offset++];
            int matchb3 = code[offset++];
            int matchb4 = code[offset++];
            if(matchb1 < 0) matchb1 += 256;
            if(matchb2 < 0) matchb2 += 256;
            if(matchb3 < 0) matchb3 += 256;
            if(matchb4 < 0) matchb4 += 256;
            int match = (int) ((matchb1 << 24) | (matchb2 << 16) | (matchb3 << 8) | matchb4);

            int offsetb1 = code[offset++];
            int offsetb2 = code[offset++];
            int offsetb3 = code[offset++];
            int offsetb4 = code[offset++];
            if(offsetb1 < 0) offsetb1 += 256;
            if(offsetb2 < 0) offsetb2 += 256;
            if(offsetb3 < 0) offsetb3 += 256;
            if(offsetb4 < 0) offsetb4 += 256;
            int offset = (int) ((offsetb1 << 24) | (offsetb2 << 16) | (offsetb3 << 8) | offsetb4);
            out.println("    " +  match + ": l" + (loc + offset));
        }
        out.println("    default: l" + (dflt+loc));
    }

    static void disassembleTableSwitch(int opcode)
    {
        int loc = offset - 1;

        // Skip over padding
        switch(offset % 4)
        {
            case 1: offset++;
            case 2: offset++;
            case 3: offset++;
        }
        int defaultb1 = code[offset++];
        int defaultb2 = code[offset++];
        int defaultb3 = code[offset++];
        int defaultb4 = code[offset++];
        if(defaultb1 < 0) defaultb1 += 256;
        if(defaultb2 < 0) defaultb2 += 256;
        if(defaultb3 < 0) defaultb3 += 256;
        if(defaultb4 < 0) defaultb4 += 256;
        int dflt = (int) ((defaultb1 << 24) | (defaultb2 << 16) | (defaultb3 << 8) | defaultb4);

        int lowb1 = code[offset++];
        int lowb2 = code[offset++];
        int lowb3 = code[offset++];
        int lowb4 = code[offset++];
        if(lowb1 < 0) lowb1 += 256;
        if(lowb2 < 0) lowb2 += 256;
        if(lowb3 < 0) lowb3 += 256;
        if(lowb4 < 0) lowb4 += 256;
        int low = (int) ((lowb1 << 24) | (lowb2 << 16) | (lowb3 << 8) | lowb4);

        int highb1 = code[offset++];
        int highb2 = code[offset++];
        int highb3 = code[offset++];
        int highb4 = code[offset++];
        if(highb1 < 0) highb1 += 256;
        if(highb2 < 0) highb2 += 256;
        if(highb3 < 0) highb3 += 256;
        if(highb4 < 0) highb4 += 256;
        int high = (int) ((highb1 << 24) | (highb2 << 16) | (highb3 << 8) | highb4);


        out.println(Disassembler.ops[opcode].mnemonic + " " + low);

        for(int i = 0; i < high-low+1; i++)
        {
            int offsetb1 = code[offset++];
            int offsetb2 = code[offset++];
            int offsetb3 = code[offset++];
            int offsetb4 = code[offset++];
            if(offsetb1 < 0) offsetb1 += 256;
            if(offsetb2 < 0) offsetb2 += 256;
            if(offsetb3 < 0) offsetb3 += 256;
            if(offsetb4 < 0) offsetb4 += 256;
            int offset = (int) ((offsetb1 << 24) | (offsetb2 << 16) | (offsetb3 << 8) | offsetb4);
            out.println("    l" + (loc + offset));
        }
        out.println("    default: l" + (dflt+loc));
    }


    static void disassembleMultianewarray(int opcode)
    {
        int b1 = code[offset++];
        int b2 = code[offset++];
        if(b1 < 0) b1 += 256;
        if(b2 < 0) b2 += 256;

        int ref = (b1 << 8 | b2);

        int dimensions = code[offset++];
        if(dimensions < 0) dimensions += 256;

        ConstantClass cc = (ConstantClass) cf.getConstant(ref);

        out.print(Disassembler.ops[opcode].mnemonic + " " +
           cf.utf8(cc.name_index()) + " " + dimensions);
        out.print(" ;#" + ref);
    }

    static void disassembleWide(int opcode)
    {
        int op = code[offset++];
        if(op < 0)
            op += 256;
        if(op == Disassembler.opc_iinc)
        {
            int b1 = code[offset++];
            int b2 = code[offset++];
            int b3 = code[offset++];
            int b4 = code[offset++];
            if(b1 < 0) b1 += 256;
            if(b2 < 0) b2 += 256;
            if(b3 < 0) b3 += 256;
            if(b4 < 0) b4 += 256;
            int index = (b1 << 8 | b2);
            int constant = (b3 << 8 | b4);
            out.print(Disassembler.ops[op].mnemonic + " " + index + " " + constant);
        }
        else
        {
            int b1 = code[offset++];
            int b2 = code[offset++];
            if(b1 < 0) b1 += 256;
            if(b2 < 0) b2 += 256;

            int var = (b1 << 8 | b2);
            out.print(Disassembler.ops[op].mnemonic + " " + var);
        }



    }

    static void disassembleArrayType(int opcode)
    {
        int b = code[offset++];
        String keyword = "<invalid type>";

        switch(b)
        {
            case T_BOOLEAN:
                keyword ="boolean"; break;
            case T_CHAR:
                keyword ="char"; break;
            case T_FLOAT:
                keyword ="float"; break;
            case T_DOUBLE:
                keyword ="double"; break;
            case T_BYTE:
                keyword ="byte"; break;
            case T_SHORT:
                keyword ="short"; break;
            case T_INT:
                keyword ="int"; break;
            case T_LONG:
                keyword ="long"; break;
        }

        out.print(Disassembler.ops[opcode].mnemonic + " " + keyword);
    }
}
