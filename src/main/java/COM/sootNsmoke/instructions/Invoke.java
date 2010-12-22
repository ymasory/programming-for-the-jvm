package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

/**
 * The base class for the Invoke{Virtual,Special,Static}
 * Call the function f with signature sig in class c, with n
 * arguments, using opcode (which should be opc_invokevirtual,
 * opc_invokespecial, or opc_invokestatic
 * Suggestion: compute n from the signature.
 */
public abstract class Invoke  extends  Sequence
{
    String class_name;
    String method_name;
    String signature;
    int num_args;
    int opcode;

    public String toString()
    {
        return COM.sootNsmoke.oolong.Disassembler.ops[opcode].mnemonic
               + " " +
               class_name + "/" + method_name + " " + signature;
    }

    public Invoke (String class_name, String method_name,
                      String signature,
                      int num_args,
                      int opcode)
    {

        // The max stack will be 0 if the return is void, otherwise 1
        int max_stack = sizeOf(signature.substring(signature.lastIndexOf(')')+1));
        if(opcode != opc_invokestatic)
            max_stack--;
        int net_stack = (max_stack - num_args);

        this.max_stack = max_stack;
        this.net_stack = net_stack;

        this.opcode = opcode;
        this.class_name = class_name;
        this.method_name = method_name;
        this.signature = signature;
        this.num_args = num_args;
    }

    /** Counts the number of arguments inside a method signature */
    public static int countArgs(String signature)
    {
        int n = 0;
        boolean ignore = false;             // Set to true after a bracket
        for(int i = 0; i < signature.length(); i++)
        {
            char c = signature.charAt(i);
            if(c == ')')
                break;

            switch(c)
            {
                case '[':
                    if(!ignore)
                        n += 1;
                    ignore = true;
                    break;
                case 'J':
                case 'D':
                    if(!ignore)
                        n += 2;
                    ignore = false;
                    break;
                case 'B':
                case 'C':
                case 'F':
                case 'I':
                case 'S':
                case 'Z':
                    if(!ignore)
                        n += 1;
                    ignore = false;
                    break;
                case 'L':
                    if(!ignore)
                        n += 1;
                    ignore = false;
                    for(i++;
                        i < signature.length() && signature.charAt(i) !=  ';';
                        i++)
                        ;
                    break;

            }
        }

        return n;
    }

    /** Returns the number of slots taken up by an element with
     * type descriptor desc.  J and D return 2, V returns 0, and
     * anything else is 1.
     */
    public static int sizeOf(String desc)
    {
        if(desc.length() == 1)
            switch(desc.charAt(0))
            {
                case 'D':
                case 'J':
                    return 2;
                case 'V':
                    return 0;
                default:
                    return 1;
            }
        else
            return 1;
    }


    public void toBytecodes(Bytecodes bytecodes)
    {
        int cp_methodref =
            bytecodes.cf.addMethodref(class_name,
                                    method_name,
                                    signature);
        byte[] bytearray = {        (byte) opcode,
                                    highbyte(cp_methodref),
                                    lowbyte(cp_methodref) };
        bytecodes.write(bytearray);
    }

}
