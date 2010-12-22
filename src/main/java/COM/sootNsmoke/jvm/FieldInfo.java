package COM.sootNsmoke.jvm;

public class FieldInfo extends FieldOrMethodInfo {
    public FieldInfo(ClassFile cf, int access_flags, int name_index,
                     int descriptor_index)
    {
        super(cf, access_flags, name_index, descriptor_index);
    }

    public void addConstantValue(int index)
    {
        ConstantValueAttribute cva = new ConstantValueAttribute(cf, index);
        add(cva);
    }
}
