package COM.sootNsmoke.jvm;
import java.io.*;
import java.util.*;

/**
 * A class to make it easier to build ClassFiles.   It handles most of
 * the string-valued constants
 * as Strings instead of index entries, and it eliminates duplicate
 * entries in the constant pool.
 * @see COM.sootNsmoke.jvm.ClassFile
 * @author Joshua Engel
 */
public class JavaClass
{
    private ClassFile cf = new ClassFile();
    private Hashtable utf8s = new Hashtable();
    private Hashtable strings = new Hashtable();
    private Hashtable floats = new Hashtable();
    private Hashtable ints = new Hashtable();
    private Hashtable doubles = new Hashtable();
    private Hashtable longs = new Hashtable();
    private Hashtable classes = new Hashtable();
    private Hashtable name_and_types = new Hashtable();
    private Hashtable refs = new Hashtable();


    /** Create a new class
     *  @param name          Name of the class, absolute (e.g. java/lang/Object)
     *  @param base          Name of the base class, absolute
     */
    public JavaClass(String name, String base)
    {
        cf.setThis(cf.lookupClass(name));
        cf.setSuper(cf.lookupClass(base));
    }

    public String getClassName()
    {
        ConstantClass cc = (ConstantClass) cf.getConstantPool(cf.getThis());
        return cf.utf8(cc.name_index());
    }

    public String getSuperclassName()
    {
        ConstantClass cc = (ConstantClass) cf.getConstantPool(cf.getSuper());
        return cf.utf8(cc.name_index());
    }

    /** Creates a new JavaClass using an existing class as the base.
     *
     * @param is Holds a class file
     *
     * @exception ClassFileException If there is an error in the class file format
     */
    public JavaClass(InputStream is) throws IOException, ClassFileException
    {
        cf.read(is);
    }

    /** Return the ClassFile which is being built, in case you
     * need to manipulate it directly */
    public ClassFile getClassFile()
    {
        return cf;
    }

    /** Add an interface to a class
     *  @param name         Name of the interface, internal fully-qualified form
     */
    public void addInterface(String name)
    {
        int intf = cf.lookupClass(name);
        cf.add(intf);
    }

    /** Turn an array of 2-element arrays { start_pc, line_number} into
     * a LineNumberTableAttribute
     */
    public LineNumberTableAttribute
        createLineNumberTableAttribute(int[][] linenos)
    {
        LineNumberTableEntry[] lnt = new LineNumberTableEntry[linenos.length];
        for(int i = 0; i < linenos.length; i++)
        {
            int start_pc = linenos[i][0];
            int line_number = linenos[i][1];
            lnt[i] = new LineNumberTableEntry(start_pc, line_number);
        }
        LineNumberTableAttribute attr = new LineNumberTableAttribute(cf, lnt);
        return attr;
    }

    /** Create an exception table entry.  If there is no classname argument,
     * then the catch_type is set to 0 (catch everything)
     */
    public ExceptionTableEntry createExceptionTableEntry(
        String catch_type,
        int start_pc,
        int end_pc,
        int handler_pc)
    {
        int cp_catch_type = addConstantClass(catch_type);
        return new ExceptionTableEntry(start_pc,
                                       end_pc, handler_pc,
                                       cp_catch_type);
    }

    /** Build an exception table entry which handles any exception between
     * start_pc and end_pc, branching to handler_pc when an exception
     * occurs.
     */
    public ExceptionTableEntry createExceptionTableEntry(
        int start_pc,
        int end_pc,
        int handler_pc)
    {
        return new ExceptionTableEntry(start_pc,
                                       end_pc, handler_pc,
                                       (int) 0);
    }



    /** Create an entry for the LocalVariableTable attribute.
     * @param start_pc The offset where the local variable begins
     * @param length   The number of bytes that it is value for
     * @param name     The name of the local variable
     * @param desc     The type descriptor of the local variable
     * @param index    The local variable pool index of this variable
     */
    public LocalVariableTableEntry createLocalVariableTableEntry(
        int start_pc,
        int length,
        String name,
        String desc,
        int index)
    {
        int cp_name = addConstantUtf8(name);
        int cp_sig = addConstantUtf8(desc);
        return new LocalVariableTableEntry(start_pc, length, cp_name,
         cp_sig, index);
    }

    /** Create a LocalVariableTable attribute given the entries */
    public LocalVariableTableAttribute createLocalVariableTableAttribute(
        LocalVariableTableEntry[] lvt)
    {
        return new LocalVariableTableAttribute(cf, lvt);
    }

    /** Given a list of names of classes which are exceptions,
     * create an ExceptionsAttribute
     */
    public ExceptionsAttribute createExceptionsAttribute(String[] exceptions)
    {
        int[] classes = new int[exceptions.length];
        for(int i = 0; i < exceptions.length; i++)
            classes[i] = addConstantClass(exceptions[i]);
        ExceptionsAttribute attr = new ExceptionsAttribute(cf, classes);
        return attr;
    }


    /** Add a method to the class
     *  @param name         The name of the method
     *  @param type         Type of the method (in mangled format; e.g.
     *                      ()V for function returning void)
     *  @param access_flags The access flags: logical OR of ACC_PUBLIC,
     *                      ACC_FINAL, etc. from RuntimeConstants.
     *  @param max_stack    Maximum number of words on the operand stack
     *  @param max_locals   Number of local variables used, incl. params
     *  @param code         Java bytecodes.  (If null or empty,
     *                      then no code attribute will be added)
     *  @param exceptions   List of exceptions
     *  @param attributes   List of additional code attributes.  Currently,
     *                      only LineNumberTable and LocalVariableTable are
     *                      recognized.
     *  @return the index of constant_methodref the new method (what you'd
     *          give to invokevirtual)
     *  @see COM.sootNsmoke.jvm.RuntimeConstants
     */
    public void addMethod(String name, String type, int access_flags,
                          int max_stack, int max_locals, byte[] code,
                          ExceptionTableEntry exceptions[],
                          Attribute code_attributes[],
                          Attribute method_attributes[])
    {
        int name_index = addConstantUtf8(name);
        int type_index = addConstantUtf8(type);

        MethodInfo method_info = new MethodInfo(cf, access_flags, name_index, type_index);
        if(code != null && code.length > 0)
            method_info.addCode(max_stack, max_locals, code, exceptions, code_attributes);
        if(method_attributes != null)
            for(int i = 0; i < method_attributes.length; i++)
                method_info.add(method_attributes[i]);
        cf.add(method_info);
    }

    private FieldInfo newField(String name, String type, int access_flags)
    {
        int name_index = addConstantUtf8(name);
        int type_index = addConstantUtf8(type);
        return new FieldInfo(cf, access_flags, name_index, type_index);
    }

    /**
     * Adds a field to the class with no default value
     * @param name         Name of the field
     * @param type         Type of the field in Java VM mangled-typr-name format
     * @param access_flags Access flags
     * @return the index of the constant_fieldref used to access the field
     */
    public int addField(String name, String type, int access_flags)
    {
        return cf.add(newField(name, type, access_flags));
    }

    /**
     * Adds a field to the class with an int default
     * @param name         Name of the field
     * @param access_flags Access flags
     * @param init         Initializer for the field
     * @return the index of the constant_fieldref used to access the field
     */
    public int addField(String name, int access_flags,
                            int init)
    {
        FieldInfo fi = newField(name, "I", access_flags);
        int cp = addConstant(init);
        fi.addConstantValue(cp);
        return cf.add(fi);
    }
    /**
     * Adds a field to the class with a String default
     * @param name         Name of the field
     * @param access_flags Access flags
     * @param init         Initializer for the field
     * @return the index of the constant_fieldref used to access the field
     */
    public int addField(String name, int access_flags,
                          String init)
    {
        FieldInfo fi = newField(name, "Ljava/lang/String;", access_flags);
        int cp = addConstantString(init);
        fi.addConstantValue(cp);
        return cf.add(fi);
    }
    /**
     * Adds a field to the class with a double default
     * @param name         Name of the field
     * @param access_flags Access flags
     * @param init         Initializer for the field
     * @return the index of the constant_fieldref used to access the field
     */
    public int addField(String name, int access_flags,
                          double init)
    {
        FieldInfo fi = newField(name, "D", access_flags);
        int cp = addConstant(init);
        fi.addConstantValue(cp);
        return cf.add(fi);
    }
    /**
     * Adds a field to the class with a long default
     * @param name         Name of the field
     * @param access_flags Access flags
     * @param init         Initializer for the field
     * @return the index of the constant_fieldref used to access the field
     */
    public int addField(String name, int access_flags,
                          long init)
    {
        FieldInfo fi = newField(name, "J", access_flags);
        int cp = addConstant(init);
        fi.addConstantValue(cp);
        return cf.add(fi);
    }
    /**
     * Adds a field to the class with a float default
     * @param name         Name of the field
     * @param access_flags Access flags
     * @param init         Initializer for the field
     * @return the index of the constant_fieldref used to access the field
     */
    public int addField(String name, int access_flags,
                          float init)
    {
        FieldInfo fi = newField(name, "F", access_flags);
        int cp = addConstant(init);
        fi.addConstantValue(cp);
        return cf.add(fi);
    }

    /*
    public int addString(String s)
    {
        int cp_s = addConstantUtf8(s);
        return cf.add(new ConstantString(cp_s));
    }
    */

    public int addFieldref(String class_name, String name, String type)
    {
        String full = class_name + "/" + name + "/" + type;
        Integer i = (Integer) refs.get(full);
        if(i != null)
            return i.intValue();

        int cp_class = addConstantClass(class_name);
        int cp_name_type = addConstantNameAndType(name, type);
        int c = cf.add(new ConstantFieldref(cp_class, cp_name_type));
        refs.put(full, new Integer(c));
        return c;
    }

    public int addMethodref(String class_name, String name, String type)
    {
        String full = class_name + "/" + name + "/" + type;
        Integer i = (Integer) refs.get(full);
        if(i != null)
            return (int) i.intValue();

        int cp_class = addConstantClass(class_name);
        int cp_name_type = addConstantNameAndType(name, type);
        int c = cf.add(new ConstantMethodref(cp_class, cp_name_type));
        refs.put(full, new Integer(c));
        return c;
    }
    /**
     * Adds a methodref constant to the constant pool.
     * @return the index of the new entry in the constant pool
     */
    public int addConstantInterfaceMethodref(int cp_class, int cp_name_and_type)
    {
        return cf.add(
                new ConstantInterfaceMethodref(cp_class, cp_name_and_type));
    }

    /**
     * Adds a methodref constant to the constant pool.
     * @return the index of the new entry in the constant pool
     */
    public int addConstantInterfaceMethodref(String classname, String name, String type)
    {
        int cp_class = addConstantClass(classname);
        int cp_name_and_type = addConstantNameAndType(name, type);
        int cp_methodref = addConstantInterfaceMethodref(cp_class, cp_name_and_type);
        return cp_methodref;
    }

    /**
     * Adds a name&type constant to the constant pool.
     * @return the index of the new entry in the constant pool
     */
     /*
    public int addConstantNameAndType(int cp_name, int cp_type)
    {
        return cf.add(
                new ConstantNameAndType(cp_name, cp_type));
    }
    */

    /**
     * Adds a name&type constant to the constant pool.
     * @return the index of the new entry in the constant pool
     */
    public int addConstantNameAndType(String name, String type)
    {
        String foo = name + "/" + type;
        Integer i = (Integer) name_and_types.get(foo);
        if(i != null)
            return i.intValue();
        int cp_name = addConstantUtf8(name);
        int cp_type = addConstantUtf8(type);
        int c = cf.add(
                new ConstantNameAndType(cp_name, cp_type));
        name_and_types.put(foo, new Integer(c));
        return c;
    }


    /**
     * Adds a constant to the constant pool.
     * @return the index of the new entry in the constant pool
     */
    public int addConstant(float val)
    {
        Float v = new Float(val);
        Integer i = (Integer) floats.get(v);
        if(i != null)
            return i.intValue();
        int c = cf.add(new ConstantFloat(val));
        floats.put(v, new Integer(c));

        return c;
    }

    /**
     * Adds a class constant to the constant pool.
     * @return the index of the new entry in the constant pool
     */
    public int addConstantClass(String classname)
    {
        Integer i = (Integer) classes.get(classname);
        if(i != null)
            return i.intValue();
        int cp_classname = addConstantUtf8(classname);
        int c = cf.add(new ConstantClass(cp_classname));
        classes.put(classname, new Integer(c));
        return c;
    }


    /**
     * Adds a constant to the constant pool.
     * @return the index of the new entry in the constant pool
     */
    public int addConstant(double val)
    {
        Double v = new Double(val);
        Integer i = (Integer) doubles.get(v);
        if(i != null)
            return i.intValue();
        int c = cf.add(new ConstantDouble(val));
        doubles.put(v, new Integer(c));

        return c;
    }

    /**
     * Adds a long constant to the constant pool.
     * @return the index of the new entry in the constant pool
     */
    public int addConstant(long val)
    {
        Long v = new Long(val);
        Integer i = (Integer) longs.get(v);
        if(i != null)
            return i.intValue();
        int c = cf.add(new ConstantLong(val));
        longs.put(v, new Integer(c));

        return c;
    }


    /**
     * Adds a int constant to the constant pool.
     * @return the index of the new entry in the constant pool
     */
    public int addConstant(int val)
    {
        Integer v = new Integer(val);
        Integer i = (Integer) ints.get(v);
        if(i != null)
            return i.intValue();
        int c = cf.add(new ConstantInteger(val));
        ints.put(v, new Integer(c));

        return c;
    }


    /**
     * Adds a string constant to the constant pool
     * @return the index of the new entry in the constant pool
     */
    public int addConstantUtf8(String str)
    {
        Integer i = (Integer) utf8s.get(str);
        if(i != null)
            return i.intValue();
        int c = cf.add(new ConstantUtf8(str));
        utf8s.put(str, new Integer(c));

        return c;
    }

    /**
     * Adds a new String constant into the constant pool
     */
    public int addConstantString(String str)
    {
        Integer i = (Integer) strings.get(str);
        if(i != null)
            return i.intValue();
        int cp_str = addConstantUtf8(str);
        int c = cf.add(new ConstantString(cp_str));
        strings.put(str, new Integer(c));

        return c;
    }

    /**
     * Adds a sourcefile attribute, with the name sourcefile
     */
    public void setSourceFile(String source)
    {
        int sourcefile = addConstantUtf8(source);
        cf.add(new SourceFileAttribute(cf, sourcefile));
    }

    /**
     * Sets the access flags.  access is the logical or the access
     * settings. The access flags can be found in the RuntimeConstants
     * package; the names begin with ACC_.
     * @see  COM.sootNsmoke.jvm.RuntimeConstants
     */
    public void setAccess(int access)
    {
        cf.setAccessFlags(access);
    }

    /** Write the class file to the output stream */
    public void write(OutputStream os) throws IOException
    {
        cf.write(os);
    }


    /**
     * Adds a generalized attribute
     */
    public void addAttribute(String name, byte[] bytes)
    {
        UnrecognizedAttribute gen_attr =
            new UnrecognizedAttribute(cf, name, bytes);
        cf.add(gen_attr);
    }

    public String toString()
    {
        return cf.toString();
    }

}



