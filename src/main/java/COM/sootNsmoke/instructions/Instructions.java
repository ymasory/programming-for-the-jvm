package COM.sootNsmoke.instructions;

/** A collection of static methods which are really just interfaces to
 * the constructors for the classes in this package.  If you make this
 * class a superclass of yours, then you can write:
 *    aload(1)
 * instead of
 *    new Aload(1)
 *
 * Most instructions have names like the standard mnemonics.
 * For those which are also keywords (goto, new) an underscore
 * is appended (goto_, new_).
 */
public class Instructions
{


public static Aaload aaload()   { return new Aaload(); }
public static Aastore aastore() { return new Aastore(); }
public static AconstNull aconst_null()    { return new AconstNull(); }
public static Aload aload_0() { return new Aload(0); }
public static Aload aload_1() { return new Aload(1); }
public static Aload aload_2() { return new Aload(2); }
public static Aload aload_3() { return new Aload(3); }
public static Aload aload(int n){ return new Aload(n); }
public static Anewarray anewarray(String classname)    { return new Anewarray(classname); }
public static Areturn areturn() { return new Areturn (); }
public static ArrayLength arraylength()    { return new ArrayLength (); }
public static Astore astore(int n)    { return new Astore (n); }
public static Athrow athrow()   { return new Athrow (); }
public static Baload baload()   { return new Baload(); }
public static Bastore bastore() { return new Bastore(); }
public static Bipush bipush(byte b)    { return new Bipush(b); }
public static Caload caload()   { return new Caload(); }
public static Castore castore() { return new Castore(); }
public static CheckCast checkcast(String classname)    { return new CheckCast(classname); }
public static D2f d2f()         { return new D2f(); }
public static D2i d2i()         { return new D2i(); }
public static D2l d2l()         { return new D2l(); }
public static Dadd dadd()       { return new Dadd(); }
public static Daload daload()   { return new Daload(); }
public static Dastore dastore() { return new Dastore(); }
public static Dcmpg dcmpg()     { return new Dcmpg(); }
public static Dcmpl dcmpl()     { return new Dcmpl(); }
public static Dconst dconst_0(int n)    { return new Dconst (0); }
public static Dconst dconst_1(int n)    { return new Dconst (1); }
public static Dconst dconst(int n)    { return new Dconst (n); }
public static Ddiv ddiv()       { return new Ddiv(); }
public static Dload dload_0()   { return new Dload (0); }
public static Dload dload_1()   { return new Dload (1); }
public static Dload dload_2()   { return new Dload (2); }
public static Dload dload_3()   { return new Dload (3); }
public static Dload dload(int n){ return new Dload (n); }
public static Dmul dmul()       { return new Dmul(); }
public static Dneg dneg()       { return new Dneg(); }
public static Drem drem()       { return new Drem(); }
public static Dreturn dreturn() { return new Dreturn(); }
public static Dstore dstore(int n)    { return new Dstore (n); }
public static Dsub dsub()       { return new Dsub(); }
public static Dup dup()         { return new Dup (); }
public static Dup2 dup2()       { return new Dup2(); }
public static Dup2_x1 dup2_x1()    { return new Dup2_x1(); }
public static Dup2_x2 dup2_x2()    { return new Dup2_x2(); }
public static Dup_x1 dup_x1()      { return new Dup_x1(); }
public static Dup_x2 dup_x2()      { return new Dup_x2(); }
public static EmptySequence emptysequence()    { return new EmptySequence(); }
public static F2d f2d()         { return new F2d(); }
public static F2i f2i()         { return new F2i(); }
public static F2l f2l()         { return new F2l(); }
public static Fadd fadd()       { return new Fadd(); }
public static Faload faload()   { return new Faload(); }
public static Fastore fastore() { return new Fastore(); }
public static Fcmpg fcmpg()     { return new Fcmpg(); }
public static Fcmpl fcmpl()     { return new Fcmpl(); }
public static Fconst fconst_0()    { return new Fconst(0); }
public static Fconst fconst_1()    { return new Fconst(1); }
public static Fconst fconst_2()    { return new Fconst(2); }
public static Fconst fconst(int n)    { return new Fconst(n); }
public static Fdiv fdiv()       { return new Fdiv(); }
public static Fload fload_0()   { return new Fload(0); }
public static Fload fload_1()   { return new Fload(1); }
public static Fload fload_2()   { return new Fload(2); }
public static Fload fload_3()   { return new Fload(3); }
public static Fload fload(int n){ return new Fload(n); }
public static Fmul fmul()       { return new Fmul(); }
public static Fneg fneg()       { return new Fneg(); }
public static Frem frem()       { return new Frem(); }
public static Freturn freturn() { return new Freturn(); }
public static Fstore fstore(int n)    { return new Fstore(n); }
public static Fsub fsub()       { return new Fsub(); }
public static Getfield getfield(String classname, String field_name,
			   String signature)
{
    return new Getfield(classname, field_name, signature);
}
public static Getstatic getstatic(String classname, String field_name,
			     String signature)
{
    return new Getstatic(classname, field_name, signature);
}
public static Goto goto_(String cont)    { return new Goto(cont); }
public static Goto_w goto_w(String cont)    { return new Goto_w(cont); }
public static I2b i2b()         { return new I2b(); }
public static I2c i2c()         { return new I2c(); }
public static I2d i2d()         { return new I2d(); }
public static I2f i2f()         { return new I2f(); }
public static I2l i2l()         { return new I2l(); }
public static I2s i2s()         { return new I2s(); }
public static Iadd iadd()       { return new Iadd(); }
public static Iaload iaload()   { return new Iaload(); }
public static Iand iand()       { return new Iand(); }
public static Iastore iastore() { return new Iastore(); }
public static Iconst iconst_m1()    { return new Iconst(-1); }
public static Iconst iconst_0()    { return new Iconst(0); }
public static Iconst iconst_1()    { return new Iconst(1); }
public static Iconst iconst_2()    { return new Iconst(2); }
public static Iconst iconst_3()    { return new Iconst(3); }
public static Iconst iconst_4()    { return new Iconst(4); }
public static Iconst iconst_5()    { return new Iconst(5); }
public static Iconst iconst(int n)    { return new Iconst(n); }
public static Idiv idiv()       { return new Idiv(); }
public static IfAcmpEq if_acmpeq(String label)    { return new IfAcmpEq(label); }
public static IfAcmpNe if_acmpne(String label)    { return new IfAcmpNe(label); }
public static IfIcmpEq if_icmpeq(String label)    { return new IfIcmpEq(label); }
public static IfIcmpGe if_icmpge(String label)    { return new IfIcmpGe(label); }
public static IfIcmpGt if_icmpgt(String label)    { return new IfIcmpGt(label); }
public static IfIcmpLe if_icmple(String label)    { return new IfIcmpLe(label); }
public static IfIcmpLt if_icmplt(String label)    { return new IfIcmpLt(label); }
public static IfIcmpNe if_icmpne(String label)    { return new IfIcmpNe(label); }
public static Ifeq ifeq(String label)    { return new Ifeq(label); }
public static Ifge ifge(String label)    { return new Ifge(label); }
public static Ifgt ifgt(String label)    { return new Ifgt(label); }
public static Ifle ifle(String label)    { return new Ifle(label); }
public static Iflt iflt(String label)    { return new Iflt(label); }
public static Ifne ifne(String label)    { return new Ifne(label); }
public static Ifnonnull ifnonnull(String label)    { return new Ifnonnull(label); }
public static Ifnull ifnull(String label)    { return new Ifnull (label); }
public static Iinc iinc(int index, int inc)    { return new Iinc (index, inc); }
public static Iload iload_0()       { return new Iload(0); }
public static Iload iload_1()       { return new Iload(1); }
public static Iload iload_2()       { return new Iload(2); }
public static Iload iload_3()       { return new Iload(3); }
public static Iload iload(int n)    { return new Iload(n); }
public static Imul imul()       { return new Imul(); }
public static Ineg ineg()       { return new Ineg(); }
public static Instanceof instanceof_(String classname)    { return new Instanceof(classname); }
public static InvokeSpecial invokespecial(String class_name,
				     String func_name,
				     String signature)
{
    return new InvokeSpecial (class_name, func_name, signature);
}
public static InvokeSpecial invokespecial(String class_name,
				     String func_name,
				     String signature,
				     int nargs)
{
    return new InvokeSpecial (class_name, func_name, signature, nargs);

}

public static InvokeStatic invokestatic(String class_name,
				   String func_name,
				   String signature)
{
    return new InvokeStatic(class_name, func_name, signature);
}
public static InvokeStatic invokestatic(String class_name,
				   String func_name,
				   String signature,
				   int nargs)
{
    return new InvokeStatic(class_name, func_name, signature, nargs);

}

public static InvokeVirtual invokevirtual(String class_name,
				     String func_name,
				     String signature)
{
    return new InvokeVirtual(class_name, func_name, signature);
}
public static InvokeVirtual invokevirtual(String class_name,
				     String func_name,
				     String signature,
				     int nargs)
{
    return new InvokeVirtual(class_name, func_name, signature, nargs);

}
public static InvokeInterface invokeinterface(String class_name,
				     String func_name,
				     String signature,
				     int nargs)
{
    return new InvokeInterface(class_name, func_name, signature, nargs);
}

public static Ior ior()         { return new Ior(); }
public static Irem irem()       { return new Irem(); }
public static Ireturn ireturn() { return new Ireturn(); }
public static Ishl ishl()       { return new Ishl(); }
public static Ishr ishr()       { return new Ishr(); }
public static Istore istore(int n)    { return new Istore (n); }
public static Isub isub()       { return new Isub(); }
public static Iushr iushr()     { return new Iushr(); }
public static Ixor ixor()       { return new Ixor(); }
public static Jsr jsr(String label)    { return new Jsr(label); }
public static Jsr_w jsr_w(String cont)    { return new Jsr_w(cont); }
public static L2d l2d()         { return new L2d(); }
public static L2f l2f()         { return new L2f(); }
public static L2i l2i()         { return new L2i(); }
public static Label label(String label)    { return new Label (label); }

public static LabelSequence labelsequence(int max_stack, int net_stack, int
    opcode, String label)
{
    return new LabelSequence(max_stack,	net_stack,
			     opcode, label);
}

public static Ladd ladd()       { return new Ladd(); }
public static Laload laload()   { return new Laload(); }
public static Land land()       { return new Land(); }
public static Lastore lastore() { return new Lastore(); }
public static Lcmp lcmp()       { return new Lcmp(); }
public static Lconst lconst_0()  { return new Lconst (0); }
public static Lconst lconst_1()  { return new Lconst (1); }
public static Lconst lconst(int n)  { return new Lconst (n); }
public static Ldc ldc(String s) { return new Ldc (s); }
public static Ldc ldc(int i)    { return new Ldc (i); }
public static Ldc ldc(float f)  { return new Ldc (f); }
public static Ldc2_w ldc2_w(double d)    { return new Ldc2_w (d); }
public static Ldc2_w ldc2_w(long l)    { return new Ldc2_w (l); }
public static Ldiv ldiv()       { return new Ldiv(); }
public static LineNumber linenumber(int number)    { return new LineNumber (number); }
public static Lload lload(int n){ return new Lload (n); }
public static Lmul lmul()       { return new Lmul(); }
public static Lneg lneg()       { return new Lneg(); }
public static Lookupswitch lookupswitch(String dflt, MatchLabel[] pairs)    { return new Lookupswitch(dflt, pairs); }
public static Lor lor()    { return new Lor(); }
public static Lrem lrem()    { return new Lrem(); }
public static Lreturn lreturn()    { return new Lreturn(); }
public static Lshl lshl()    { return new Lshl(); }
public static Lshr lshr()    { return new Lshr(); }
public static Lstore lstore(int n)    { return new Lstore(n); }
public static Lsub lsub()    { return new Lsub(); }
public static Lushr lushr()    { return new Lushr(); }
public static Lxor lxor()    { return new Lxor(); }
public static Monitorenter monitorenter()    { return new Monitorenter (); }
public static Monitorexit monitorexit()    { return new Monitorexit (); }
public static Multianewarray multianewarray(String classname, int n)    { return new Multianewarray (classname, n); }
public static New new_(String classname)
{
    return new New(classname);
}
public static Newarray newarray(int n)    { return new Newarray(n); }
public static Nop nop()         { return new Nop (); }
public static Pop pop()         { return new Pop (); }
public static Pop2 pop2()       { return new Pop2 (); }
public static Putfield putfield(String classname, String field_name,
			   String signature)
{
    return new Putfield(classname, field_name, signature);
}
public static Putstatic putstatic(String classname, String field_name,
			     String signature)
{
    return new Putstatic(classname, field_name, signature);
}
public static Ret ret(int index){ return new Ret (index); }
public static Return return_()   { return new Return(); }
public static Saload saload()   { return new Saload (); }
public static Sastore sastore() { return new Sastore (); }
public static Sipush sipush(int val)    { return new Sipush (val); }
public static Swap swap()    { return new Swap (); }
public static Tableswitch tableswitch(int low, String dflt,
				 String[] offsets)
{
    return new Tableswitch(low, dflt, offsets);
}
public static Wide wide(int opcode, int index)
{
    return new Wide(opcode, index);
}
public static Wide wide(int iinc_opcode, int index, int constant)
{
    return new Wide(iinc_opcode, index, constant);
}

/** Nonstandard */
public static CompoundInstruction compoundinstruction(Sequence[] seqs)
{
    return new CompoundInstruction(seqs);
}
public static CompoundInstruction compoundinstruction(Sequence a,
						 Sequence b)
{
    return new CompoundInstruction(a, b);
}
public static CompoundInstruction compoundinstruction(Sequence a,
						 Sequence b,
						 boolean parallel)
{
    return new CompoundInstruction( a, b, parallel);
}
public static LoadSequence loadsequence(int max_stack, int net_stack,
				   int nvars, int opcode, int n)
{
    return new LoadSequence(max_stack, net_stack, nvars, opcode, n);
}
public static MatchLabel matchlabel(int match, String label)
{
    return new MatchLabel(match, label);
}
public static Comment comment(String comment)
{
    return new Comment(comment);
}
}
