package COM.sootNsmoke.oolong;
import java.io.*;
import COM.sootNsmoke.jvm.*;

/** A class for producing a byte-by-byte disassembly of a class file.
 * This is more detailed than javap or Gnoloo, since it displays the
 * exact constant pool and all of the attributes.
 */
public class DumpClass
{
    static ClassFile cf;

    public static void main(String a[])
    {
        for(int i = 0; i < a.length; i++)
        {
            try
            {
                cf = new ClassFile();
                InputStream is = new FileInputStream(a[i]);
                cf.read(is);
                dump(cf);
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("Done.");
        }
    }

    public static void dump(ClassFile cf)
    {
        numHexBytes = 0;

        String s = new String("magic = ");
        for(int i = 0; i < cf.getMagic().length; i++)
        {
            int l = cf.getMagic()[i];
            if(l < 0)
                l = l + 256;
            s += Integer.toString(l, 16) + " ";
        }
        hexdump(cf.getMagic());
        System.out.println(s);

        hexdump(Stringer.shortToBytes((short) cf.getMinorVersion()));
        System.out.println("minor version = " + cf.getMinorVersion());
        hexdump(Stringer.shortToBytes((short) cf.getMajorVersion()));
        System.out.println("major version = " + cf.getMajorVersion());

        hexdump(Stringer.shortToBytes((short) cf.getConstantPool().length));
        System.out.println("" + cf.getConstantPool().length + " constants");

        for(int i = 1; i < cf.getConstantPool().length; i++)
        {
            try
            {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                GenericConstant cpi = (GenericConstant) cf.getConstantPool(i);
                cpi.write(new DataOutputStream(os));
                byte[] bytes = os.toByteArray();

                indentLevel++;
                if(cpi instanceof ConstantUtf8)
                {
                    String str = ((ConstantUtf8) cpi).getValue();

                    hexdump(bytes, 0, 3, 18);
                    System.out.println(i + ". UTF length=" +str.length());
                    for(int j = 0; j < str.length(); j += 16)
                    {
                        String substr;
                        if(j+16 < str.length())
                            substr = str.substring(j, j+16);
                        else
                            substr = str.substring(j);
                        hexdump(bytes, j+3, 16, 32);
                        System.out.println(substr);
                    }
                }
                else
                {
                    hexdump(bytes);
                    System.out.println(i + ". " + cpi);
                }
                indentLevel--;

        		byte tag = cpi.tag();
        		if(tag == ClassFile.CONSTANT_DOUBLE ||
    		       tag == ClassFile.CONSTANT_LONG) {
        		    i++;
    	    	}
    	    }
    	    catch(Exception e)
    	    {
    	        e.printStackTrace();
    	    }
        }

        hexdump(Stringer.shortToBytes((short) cf.getAccessFlags()));
        System.out.println("access_flags = " + cf.getAccessFlags());
        hexdump(Stringer.shortToBytes((short) cf.getThis()));
        System.out.println("this = #" + cf.getThis());
        hexdump(Stringer.shortToBytes((short) cf.getSuper()));
        System.out.println("super = #" + cf.getSuper());

        hexdump(Stringer.shortToBytes((short) cf.getInterfaces().length));
        System.out.println(cf.getInterfaces().length + " interfaces");
        indentLevel++;
        for(int i = 0; i < cf.getInterfaces().length; i++)
        {
            hexdump(Stringer.shortToBytes((short) cf.getInterfaces(i)));
            System.out.println(Integer.toString(cf.getInterfaces(i)));
        }
        indentLevel--;

        hexdump(Stringer.shortToBytes((short) cf.getFields().length));
        System.out.println("" + cf.getFields().length + " fields");
        for(int i = 0; i < cf.getFields().length; i++)
        {
            hexdump(new byte[0]);
            System.out.println("Field " + i + ":");
            indentLevel++;
            dump((FieldOrMethodInfo) cf.getFields(i));
            indentLevel--;
        }

        hexdump(Stringer.shortToBytes((short) cf.getMethods().length));
        System.out.println("" + cf.getMethods().length + " methods");
        for(int i = 0; i < cf.getMethods().length; i++)
        {
            hexdump(new byte[0]);
            System.out.println("Method " + i + ":");
            indentLevel++;
            dump((FieldOrMethodInfo) cf.getMethods()[i]);
            indentLevel--;
        }

        hexdump(Stringer.shortToBytes((short) cf.getAttributes().length));
        System.out.println(cf.getAttributes().length + " classfile attributes");
        for(int i = 0; i < cf.getAttributes().length; i++)
        {
            hexdump(new byte[0]);
            System.out.println("Attribute " + i + ":");
            indentLevel++;
            dump((Attribute) cf.getAttributes()[i]);
            indentLevel--;
        }
    }


    static void dump(Attribute attr)
    {
        hexdump(Stringer.shortToBytes((short) attr.attribute_name()));
        System.out.println("name = #" + attr.attribute_name()
                    + "<" + cf.utf8(attr.attribute_name()) + ">");
        hexdump(Stringer.intToBytes(attr.length()));
        System.out.println("length = " + attr.length());
        if(attr instanceof UnrecognizedAttribute)
            dump((UnrecognizedAttribute) attr);
        else if(attr instanceof LocalVariableTableAttribute)
            dump((LocalVariableTableAttribute) attr);
        else if(attr instanceof ExceptionsAttribute)
            dump((ExceptionsAttribute) attr);
        else if(attr instanceof CodeAttribute)
            dump((CodeAttribute) attr);
        else if(attr instanceof LineNumberTableAttribute)
            dump((LineNumberTableAttribute) attr);
        else if(attr instanceof ConstantValueAttribute)
            dump((ConstantValueAttribute) attr);
        else if(attr instanceof SourceFileAttribute)
            dump((SourceFileAttribute) attr);
        else if(attr instanceof InnerClassesAttribute)
            dump((InnerClassesAttribute) attr);
    }

    static void dump(UnrecognizedAttribute attr)
    {
        byte[] data = attr.data();

        for(int j = 0; j < data.length; j += 16)
        {
            hexdump(data, j, 16, 32);
            System.out.println();
        }
    }

    static void dump(LocalVariableTableAttribute attr)
    {
        hexdump((short) attr.local_variable_table().length);
        System.out.println("local_variable_table length: " + attr.local_variable_table().length);
        for(int i = 0; i < attr.local_variable_table().length; i++)
            dump(attr.local_variable_table()[i]);
    }

    static void dump(LocalVariableTableEntry me)
    {
        hexdump(me.start_pc());
        System.out.println("start pc: " + me.start_pc());
        hexdump(me.length());
        System.out.println("length: " + me.length());
        hexdump(me.name_index());
        System.out.println("name_index: " + me.name_index());
        hexdump(me.descriptor_index());
        System.out.println("descriptor_index: " + me.descriptor_index());
        hexdump(me.index());
        System.out.println("index: " + me.index());
    }


    static void dump(LineNumberTableEntry me)
    {

        int fudged_int = me.start_pc() << 16 | me.line_number();
        hexdump(Stringer.intToBytes(fudged_int));
        System.out.println("start pc: " + me.start_pc() +
            " line number: " + me.line_number());
    }

    static void dump(ExceptionsAttribute attr)
    {
        hexdump((short) attr.exception_index_table().length);
        System.out.println("Exception table length: " + attr.exception_index_table().length);
        for(int i = 0; i < attr.exception_index_table().length; i++)
        {
            hexdump(attr.exception_index_table()[i]);
            System.out.println(i + ". exception table entry: #" + attr.exception_index_table()[i]);
        }
    }

    static void dump(CodeAttribute attr)
    {
        hexdump(attr.max_stack());
        System.out.println("max stack: " + attr.max_stack());
        hexdump(attr.max_locals());
        System.out.println("max locals: " + attr.max_locals());

        hexdump(Stringer.intToBytes(attr.code().length));
        System.out.println("code length: " + attr.code().length);

        try
        {
            Disassembler.disassemble(new ByteArrayInputStream(attr.code()),
                                     System.out, cf);
        }
        catch(IOException e)
        {
            // Not expecting this.
            throw new RuntimeException("Unexpected IOException");
        }

        hexdump((short) attr.exception_table().length);
        System.out.println(attr.exception_table().length + " exception table entries:");
        for(int i = 0; i < attr.exception_table().length; i++)
            dump(attr.exception_table()[i]);

        hexdump((short) attr.attributes().length);
        System.out.println(attr.attributes().length + " code attributes:");
        for(int i = 0; i < attr.attributes().length; i++)
        {
            hexdump();
            System.out.println("code attribute " + i + ":");
            indentLevel++;
            dump(attr.attributes()[i]);
            indentLevel--;
        }
    }

    static void dump(LineNumberTableAttribute attr)
    {
        hexdump();
        System.out.println("Line number table:");
        hexdump((short) attr.line_number_table().length);
        System.out.println("length = " + attr.line_number_table().length);
        indentLevel++;
        for(int i = 0; i < attr.line_number_table().length; i++)
            dump(attr.line_number_table()[i]);
        indentLevel--;
    }

    static void dump(InnerClassesAttribute attr)
    {
        int l = attr.number_of_classes();
        hexdump((short) l);
        System.out.println("number of classes = " + l);
        indentLevel++;
        for(int i = 0; i < l; i++)
        {
            hexdump(attr.inner_class_info_index(i));
            System.out.println("inner class: #" + attr.inner_class_info_index(i));
            hexdump(attr.outer_class_info_index(i));
            System.out.println("outer class: #" + attr.outer_class_info_index(i));
            hexdump(attr.inner_name_index(i));
            System.out.println("inner name: #" + attr.inner_name_index(i));
            hexdump(attr.inner_class_access_flags(i));
            System.out.println("flags: " + attr.inner_class_access_flags(i));
        }
        indentLevel--;
    }
    static void dump(SourceFileAttribute attr)
    {
        hexdump(attr.sourcefile_index());
        System.out.println("sourcefile index = #" + attr.sourcefile_index());
    }

    static void dump(FieldOrMethodInfo me)
    {
        hexdump(Stringer.shortToBytes((short) me.access_flags()));
        System.out.println("access flags = " + me.access_flags());
        hexdump(Stringer.shortToBytes((short) me.name_index()));
        System.out.println("name = #" + me.name_index()
            + "<" + cf.utf8(me.name_index()) + ">");
        hexdump(Stringer.shortToBytes((short) me.descriptor_index()));
        System.out.println("descriptor = #" + me.descriptor_index()
            + "<" + cf.utf8(me.descriptor_index()) + ">");
        hexdump((short) me.attributes().length);
        System.out.println(me.attributes().length + " field/method attributes:");
        if(me.attributes() != null)
            for(int i = 0; i < me.attributes().length; i++)
            {
                hexdump(new byte[0]);
                System.out.println("field/method attribute " + i);
                indentLevel++;
                dump(me.attributes()[i]);
                indentLevel--;
            }
    }

    static void dump(ConstantValueAttribute attr)
    {
        hexdump(attr.constantvalue_index());
        System.out.println("Constant value: #" + attr.constantvalue_index());
    }



    static void dump(ExceptionTableEntry me)
    {
        hexdump(me.start_pc());
        System.out.println("start pc = " + me.start_pc());
        hexdump(me.end_pc());
        System.out.println("end pc = " + me.end_pc());
        hexdump(me.handler_pc());
        System.out.println("handler pc = " + me.handler_pc());
        hexdump(me.catch_type());
        System.out.println("catch type = " + me.catch_type());
    }


    static int numHexBytes = 0;
    static int indentLevel = 0;

    /** Dumps an array of bytes into readable hex, left justified
     * in 2*max characters, padded with spaces
     * Also has the responsibility of adding indentLevel*3 spaces afterwards*/
    static void hexdump(byte[] b, int begin, int length, int max)
    {
        if(length > 0)
        {
            System.out.print(hd.toHex(numHexBytes, 6));
            System.out.print(" ");
        }
        else
            System.out.print("       ");

        int c = 0;
        for(int i = 0; c < max; i++)
        {
            if(i < length && begin+i < b.length)
            {
                System.out.print(hd.toHex(b[begin+i], 2));
                numHexBytes++;
                c += 2;
            }
            else
            {
                System.out.print("  ");
                c += 2;
            }
        }

        for(int i = 0; i < indentLevel; i++)
            System.out.print("   ");
    }

    static void hexdump(byte[] s)
    {
        hexdump(s, 0, s.length, 18);
    }
    static void hexdump()
    {
        hexdump(new byte[0]);
    }

    static void hexdump(int n)
    {
        hexdump(Stringer.shortToBytes((short) n));

    }
}
