package COM.sootNsmoke.jvm;

/** Describes a single method entry in the class file
 * @author Joshua Engel
 */
public class MethodInfo extends FieldOrMethodInfo {
    /** Create a MethodInfo.
     * @param cf The ClassFile that this method will be added to
     * @param access_flags The access flags (ACC_PUBLIC, ACC_FINAL, etc.)
     * @param name_index UTF8 constant pool entry giving the method name
     * @param descriptor_index UTF8 constant pool entry giving the method descriptor
     * @see COM.sootNsmoke.jvm.RuntimeConstants
     */
    public MethodInfo(ClassFile cf, int access_flags, int name_index,
                      int descriptor_index)
    {
        super(cf, access_flags, name_index, descriptor_index);
    }

    /** Add a Code attribute to the method. This should be done for all
     * non-native and non-abstract methods.
     * @param max_stack Maximum stack that the method may use
     * @param max_locals Maximum parameters that the class may use
     * @param code       The code, as bytecodes.  All constant pool references
     *                   in the code should be found in the constant pool
     *                   of the ClassFile given in the constructor
     * @param exceptions A list of entries in the exception handler table
     * @param attributes A list of attributes for the Code attribute
     */
    public void addCode(int max_stack, int max_locals, byte[] code,
                        ExceptionTableEntry exceptions[],
                        Attribute[] attributes)
    {
        CodeAttribute code_attr = new CodeAttribute(cf, max_stack,
            max_locals, code, exceptions, attributes);
        add(code_attr);
    }
}