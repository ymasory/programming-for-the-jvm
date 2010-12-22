package COM.sootNsmoke.scheme;
import COM.sootNsmoke.jvm.*;
import COM.sootNsmoke.instructions.*;
import java.io.*;
/**
 * A compiler which compiles Scheme into Java bytecodes.  The
 * main entry point is compileExpression, which takes a
 * Scheme expression and returns a Sequence, which is our
 * abstraction of a sequence of JVM instructions.
 *
 * Each instruction sequence expects that a BindingEnv
 * is stored in variable slot 1.
 */
public class SchemeCompiler extends Instructions implements RuntimeConstants
{
    /** Whether or not to save code generated on disk */
    boolean save = false;

    static ByteArrayClassLoader loader = new ByteArrayClassLoader();
    /** The maximum number of arguments for which we can call applyX, where
     * X is the number of arguments (instead of calling applyN).
     */
    static final int maxDirectArgs = 3;

    /** A constant indicating that the number of parameters is unknown. */
    static final int arbitraryParameterLength = -1;

    /** Compiles an expression into a complete method body.
     * This is basically just compiling the expression, with
     * a little code around it to set up the binding environment\
     * and return the result.
     */
    public Sequence compileTopLevelFunction(Object exp)
        throws SchemeException
    {
        return appendSequences(
            new Aload(0),
            new Getfield(
                "COM/sootNsmoke/scheme/CompiledProcedure",
                "env",
                "LCOM/sootNsmoke/scheme/BindingEnv;"),
            new Astore(1),
            compileExpression(exp),
            new Areturn());
    }

    /** Compile exp into a sequence of JVM bytecodes */
    public Sequence compileExpression(Object exp)
                                          throws SchemeException
    {
        if(SchemeSyntax.isSelfEvaluating(exp))
            return compileConstant(exp);
        else if(SchemeSyntax.isQuoted(exp))
            return compileConstant(SchemeSyntax.textOfQuotation(exp));
        else if(SchemeSyntax.isForm(exp, "quasiquote"))
            return compileQuasiquote(SchemeSyntax.textOfQuotation(exp));
        else if(SchemeSyntax.isVariable(exp))
            return compileVariableAccess(exp);
        else if(SchemeSyntax.isIf(exp))
            return compileIf(exp);
        else if(SchemeSyntax.isCond(exp))
            return compileCond(exp);
        else if(SchemeSyntax.isForm(exp, "case"))
            return compileCase(exp);
        else if(SchemeSyntax.isForm(exp, "and"))
            return compileAnd(exp);
        else if(SchemeSyntax.isForm(exp, "or"))
            return compileOr(exp);
        else if(SchemeSyntax.isForm(exp, "do"))
            return compileDo(exp);
        else if(SchemeSyntax.isForm(exp, "let"))
            return compileLet(exp);
        else if(SchemeSyntax.isForm(exp, "let*"))
            return compileLet(exp);
        else if(SchemeSyntax.isForm(exp, "letrec"))
            return compileLet(exp);
        else if(SchemeSyntax.isForm(exp, "set!"))
            return compileSetBang(exp);
        else if(SchemeSyntax.isForm(exp, "delay"))
            return compileDelay(exp);
        else if(SchemeSyntax.isDefinition(exp))
            return compileDefinition(exp);
        else if(SchemeSyntax.isLambda(exp))
            return compileLambda(exp);
        else if(SchemeSyntax.isBegin(exp))
            return compileSequence(SchemeSyntax.actions(exp));
        else if(SchemeSyntax.isApplication(exp))
            return compileApplication(exp);
        else
            throw new SchemeException("Unknown expression type " + exp);
    }

    /** Syntax (quasiquote <template>)
     * "Backquote" or "quasiquote" expressions are useful for
     * constructing a lost or vector structure when most but
     * not all of the desired structure is known in advance.
     * If no commas appear within the <template>, the result
     * of evaluating `<template> is equivalent to the result
     * of evaluating '<template>.  If a comma appears within
     * the <tempate>, however, the expression following the
     * comma is evaluated ("unquoted") and its result is
     * inserted into the structure instead of the comma and
     * the expression.  if a comma appears followed immediately
     * by an at-sign (@), then the following expression must
     * evaluate to a list; the opening and closing parentheses
     * of the list are then "stripped away" and the elements
     * of the list are inserted in place of the comma at-sign
     * expression sequence
     */
    protected Sequence compileQuasiquote(Object exp) throws SchemeException
    {
        return compileQuasiquote(exp, 0);
    }
    protected Sequence compileQuasiquote(Object exp, int depth) throws SchemeException
    {
        if(SchemeSyntax.isNull(exp))
            return aconst_null();
        if(SchemeSyntax.isForm(exp, "quasiquote"))
            return appendSequences(
                new_("COM/sootNsmoke/scheme/Cons"),
                dup(),
                compileConstant(((Cons) exp).car()),
                compileQuasiquote(Cons.cdr(exp), depth+1),
                invokespecial("COM/sootNsmoke/scheme/Cons",
                              "<init>",
                              "(Ljava/lang/Object;Ljava/lang/Object;)V"));
        if(SchemeSyntax.isForm(exp, "unquote") ||
           SchemeSyntax.isForm(exp, "unquote-splicing"))
            if(depth == 0)
                return compileExpression(SchemeSyntax.textOfQuotation(exp));
            else
                return appendSequences(
                    new_("COM/sootNsmoke/scheme/Cons"),
                    dup(),
                    compileConstant(Cons.car(exp)),
                    compileQuasiquote(Cons.cdr(exp), depth-1),
                    invokespecial("COM/sootNsmoke/scheme/Cons",
                                  "<init>",
                                  "(Ljava/lang/Object;Ljava/lang/Object;)V"));
        else if(exp instanceof Cons)
        {
            Cons cons = (Cons) exp;

            if(SchemeSyntax.isForm(cons.car(), "unquote-splicing"))
            {
                Sequence seq = appendSequences(
                    compileExpression(
                        SchemeSyntax.textOfQuotation(cons.car())),
                    compileQuasiquote(cons.cdr(), depth),
                    invokestatic("COM/sootNsmoke/scheme/SchemeLibrary",
                                 "append",
                                 "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
                return seq;
            }
            else
            {
                Sequence seq = appendSequences(
                    new New("COM/sootNsmoke/scheme/Cons"),
                    new Dup(),
                    compileQuasiquote(cons.car(), depth),
                    compileQuasiquote(cons.cdr(), depth),
                    new InvokeSpecial(
                        "COM/sootNsmoke/scheme/Cons",
                        "<init>",
                        "(Ljava/lang/Object;Ljava/lang/Object;)V",
                        2));
                return seq;
            }
        }
        else if(exp instanceof Object[])
        {
            Sequence seq = appendSequences(
                new_("java/util/Vector"),
                dup(),
                invokespecial("java/util/Vector", "<init>", "()V"));
            Object a[] = (Object[]) exp;
            for(int i = 0; i < a.length; i++)
            {
                if(SchemeSyntax.isForm(a[i], "unquote-splicing"))
                {
                    seq = appendSequences(
                        seq,
                        dup(),
                        compileQuasiquote(a[i], depth),
                        invokestatic("COM/sootNsmoke/scheme/SchemeLibrary",
                                     "spliceVector",
                                     "(Ljava/util/Vector;Ljava/lang/Object;)V"));
                }
                else
                    seq = appendSequences(
                        seq,
                        dup(),
                        compileQuasiquote(a[i], depth),
                        invokevirtual("java/util/Vector",
                                      "addElement",
                                      "(Ljava/lang/Object;)V"));
            }
            seq = appendSequences(
                seq,
                dup(),
                invokevirtual("java/util/Vector", "size", "()I"),
                anewarray("java/lang/Object"),
                dup_x1(),
                invokevirtual("java/util/Vector", "copyInto",
                              "([Ljava/lang/Object;)V"));
            return seq;
        }
        else
            return compileConstant(exp);
    }

    /** Compiles a constant, which may be nil, a string,
     * a number, a vector, or a cons cell.
     */
    protected Sequence compileConstant(Object constant)
    {
        if(SchemeSyntax.isNull(constant))
            return new AconstNull();
        else if(constant instanceof Boolean)
        {
            if(constant == Boolean.FALSE)
                return new Getstatic("java/lang/Boolean",
                                                "FALSE",
                                                "Ljava/lang/Boolean;");
            else
                return new Getstatic("java/lang/Boolean",
                                                "TRUE",
                                                "Ljava/lang/Boolean;");
        }
        else if(constant instanceof String)
            return new Ldc((String) constant);
        else if(constant instanceof Character)
            return appendSequences(
                new_("java/lang/Character"),
                dup(),
                ldc(((Character) constant).charValue()),
                invokespecial("java/lang/Character",
                              "<init>",
                              "(C)V"));
        else if(constant instanceof Integer)
            return appendSequences(
                new New("java/lang/Integer"),
                new Dup(),
                new Ldc(((Integer) constant).intValue()),
                new InvokeSpecial("java/lang/Integer",
                                             "<init>",
                                             "(I)V"));
        else if(constant instanceof Double)
            return appendSequences(
                new New("java/lang/Double"),
                new Dup(),
                new Ldc2_w(((Double) constant).doubleValue()),
                new InvokeSpecial("java/lang/Double",
                                             "<init>",
                                             "(D)V"));
        else if(constant instanceof Float)
            return appendSequences(
                new New("java/lang/Float"),
                new Dup(),
                new Ldc(((Float) constant).floatValue()),
                new InvokeSpecial("java/lang/Float",
                                             "<init>",
                                             "(F)V"));
        else if(constant instanceof Long)
            return appendSequences(
                new New("java/lang/Long"),
                new Dup(),
                new Ldc2_w(((Long) constant).longValue()),
                new InvokeSpecial("java/lang/Long",
                                             "<init>",
                                             "(J)V"));
        else if(constant instanceof Complex)
        {
            Complex c = (Complex) constant;
            return appendSequences(
                new New("COM/sootNsmoke/scheme/Complex"),
                new Dup(),
                compileConstant(c.realPart()),
                compileConstant(c.imagPart()),
                new InvokeSpecial("COM/sootNsmoke/scheme/Complex",
                                             "<init>",
                                             "(Ljava/lang/Number;Ljava/lang/Number;)V"));
        }
        else if(constant instanceof Rational)
        {
            Rational r = (Rational) constant;
            return appendSequences(
                new New("COM/sootNsmoke/scheme/Rational"),
                new Dup(),
                new Ldc2_w(r.numerator()),
                new Ldc2_w(r.denominator()),
                new InvokeSpecial("COM/sootNsmoke/scheme/Rational",
                                             "<init>",
                                             "(JJ)V"));
        }
        if(constant instanceof Cons)
        {
            Sequence seq = appendSequences(
                new New("COM/sootNsmoke/scheme/Cons"),
                new Dup(),
                compileConstant(((Cons) constant).car()),
                compileConstant(((Cons) constant).cdr()),
                new InvokeSpecial(
                    "COM/sootNsmoke/scheme/Cons",
                    "<init>",
                    "(Ljava/lang/Object;Ljava/lang/Object;)V",
                    2));
            return seq;
        }
        if(constant instanceof Symbol)
        {
            return appendSequences(
                new Ldc(((Symbol) constant).getName()),
                new InvokeStatic(
                    "COM/sootNsmoke/scheme/Naming",
                    "name",
                    "(Ljava/lang/String;)LCOM/sootNsmoke/scheme/Symbol;",
                    1));
        }
        else if(constant instanceof Object[])
        {
            Object[] a = (Object[]) constant;
            Sequence seq = appendSequences(
                new Sipush(a.length),
                new Anewarray("java/lang/Object"));
            for(int i = 0; i < a.length; i++)
            {
                seq = seq
                .appendSequence(new Dup())
                .appendSequence(new Sipush(i))
                .appendSequence(compileConstant(a[i]))
                .appendSequence(new Aastore());
            }
            return seq;
        }
        throw new SchemeException("Cannot compile constant "
            + constant);
    }

    protected Sequence compileVariableAccess(Object variable)
    {
        return appendSequences(
            new Aload(1),
            new Ldc(((Symbol) variable).getName()),
            new InvokeVirtual("COM/sootNsmoke/scheme/BindingEnv",
                                         "lookup",
                                         "(Ljava/lang/String;)Ljava/lang/Object;",
                                         1));
    }

    /** Compiles a procedure application of the form (fn . args)
     * into fn.call(args)
     */
    protected Sequence compileApplication(
        Object app) throws SchemeException
    {
        Object operands = SchemeSyntax.operandsOf(app);
        int num_rands = numParams(operands);

        Sequence call_code = appendSequences(
            compileExpression(SchemeSyntax.operatorOf(app)),
            new CheckCast("COM/sootNsmoke/scheme/CompiledProcedure"),
            compileOperands(operands, num_rands),
            compileCall(num_rands));

        return call_code;
    }

    /** Compiles all the operands.  n is the index of this operand.
     * If there are fewer than maxDirectArgs arguments, each is just pushed
     * onto the stack.  If not, then each is added to the array which
     * compileOperands will push onto the stack.
     */
    protected Sequence compileOperands(
        Object rands, int num_rands)
        throws SchemeException
    {
        Sequence seq = new EmptySequence();
        if(num_rands <= maxDirectArgs)
        {
            while(rands instanceof Cons)
            {
                seq = appendSequences(
                    seq,
                    compileExpression(SchemeSyntax.firstOperand(rands)));
                rands = SchemeSyntax.restOperands(rands);
            }
        }
        else
        {
            while(rands instanceof Cons)
            {
                seq = appendSequences(
                    seq,
                    new New("COM/sootNsmoke/scheme/Cons"),
                    new Dup(),
                    compileExpression(SchemeSyntax.firstOperand(rands)));
                rands = SchemeSyntax.restOperands(rands);
            }
            seq = appendSequences(
                seq,
                new AconstNull());
            for(int i = 0; i < num_rands; i++)
            {
                seq = appendSequences(
                    seq,
                    new InvokeSpecial(
                        "COM/sootNsmoke/scheme/Cons",
                        "<init>",
                        "(Ljava/lang/Object;Ljava/lang/Object;)V",
                        2));
            }
        }
        return seq;
    }

    /** Compiles a procedure application where there are no
     * arguments.
     */
    protected Sequence compileNoArgs(
                            Object app) throws SchemeException
    {
        return appendSequences(
            compileExpression(SchemeSyntax.operatorOf(app)),
            compileCall(0));
    }

    /**
     * At this point, the function should be the first thing on the stack,
     * then up to maxDirectArgs arguments, then an array of to
     * n-maxDirectArgs arguments.  (If this last is <= 0, then there is
     * no array.
     */
    protected Sequence compileCall(int n)
    {
        return new InvokeVirtual(
                "COM/sootNsmoke/scheme/CompiledProcedure",
                "apply" + (n <= maxDirectArgs ? Integer.toString(n) : "N"),
                signature(n),
               (n <= maxDirectArgs ? n : 1));
    }

    /**
     * An if expression of the form (if test consequent alternative)
     * is implemented as:
     *     test
     *     if true goto l1
     *     alternative
     *     goto l2:
     * l1:
     *     consequent
     * l2:
     */
    protected Sequence compileIf(Object exp) throws SchemeException
    {
        Object test_expr = SchemeSyntax.testOf(exp);
        Object true_expr = SchemeSyntax.consequenceOf(exp);
        Object false_expr = SchemeSyntax.alternateOf(exp);

        String l1 = Counter.makeNewLabel("label");
        String l2 = Counter.makeNewLabel("label");
        Sequence test = compileExpression(test_expr);
        Sequence consequent =
            appendSequences(
                new Label(l1),
                compileExpression(true_expr));
        Sequence alternative =
            appendSequences(
                compileExpression(false_expr),
                new Goto(l2));

        Sequence alternative_and_consequent =
            parallelSequences(alternative, consequent);
        Sequence seq = appendSequences(
            test,
            new Getstatic("java/lang/Boolean",
                                     "FALSE",
                                     "Ljava/lang/Boolean;"),
            new IfAcmpNe(l1),
            alternative_and_consequent,
            new Label(l2));
        return seq;
    }

    /** Syntax: (and <test1> ...)
     * The <test> expressions are evaluated from left to right,
     * and the value of the first expression that evaluates
     * to a false value is returned.  Any remaining expresisons
     * are not evaluated.  If all the expressions evaluate to
     * true values, the value of the last expression is returned.
     * If there are no expressions then #t is returned
     *
     *
     * Compiles to:
     *    <test1>
     *    dup
     *    getfield java/lang/Boolean/FALSE Ljava/lang/Boolean;
     *    if_acmpeq done
     *    pop
     *    [repeat for tests 2..n-1]
     *    <testn>
     * done:
     */
    protected Sequence compileAnd(Object cond) throws SchemeException
    {

        Sequence seq = comment(write(cond));
        String done = Counter.makeNewLabel("done");

        Cons c = (Cons) cond;

        if(c.cdr() instanceof Cons)
        {
            c = (Cons) c.cdr();
            Object expr = c.car();
            seq = seq.append(compileExpression(expr));
            while(true)
            {
                if(c.cdr() instanceof Cons)
                    c = (Cons) c.cdr();
                else if(c.cdr() == null)
                    break;
                else
                    throw new SyntaxError("Improper list in and expression " +
                        write(c));

                expr = c.car();

                seq = appendSequences(
                    seq,
                    dup(),
                    getstatic("java/lang/Boolean",
                             "FALSE",
                             "Ljava/lang/Boolean;"),
                    if_acmpeq(done),
                    pop(),
                    compileExpression(expr));
            }
            seq = seq.append(label(done));
        }
        else
            seq = seq.append(getstatic("java/lang/Boolean",
                             "TRUE",
                             "Ljava/lang/Boolean;"));
        return seq;
    }

    /** Syntax: (or <test1> ...)
     * The <test> expressions are evaluated from left to right,
     * and the value of the first expression that evaluates to
     * a true value is returned.  Any remaining expressions are
     * not evaluated.  If all expressions evaluate to false
     * values, the value of the last expression is returned.
     * If there are no expressions then #f is returned.
     */
    protected Sequence compileOr(Object cond) throws SchemeException
    {

        Sequence seq = comment(write(cond));
        String done = Counter.makeNewLabel("done");

        Cons c = (Cons) cond;
        if(c.cdr() instanceof Cons)
        {
            c = (Cons) c.cdr();
            Object expr = c.car();
            seq = seq.append(compileExpression(expr));
            while(true)
            {
                if(c.cdr() instanceof Cons)
                    c = (Cons) c.cdr();
                else if(c.cdr() == null)
                    break;
                else
                    throw new SyntaxError("Improper list in or expression " +
                        write(c));
                expr = c.car();

                seq = appendSequences(
                    seq,
                    dup(),
                    getstatic("java/lang/Boolean",
                             "FALSE",
                             "Ljava/lang/Boolean;"),
                    if_acmpne(done),
                    pop(),
                    compileExpression(expr));
            }
            seq = seq.append(label(done));
        }
        else
            seq = seq.append(getstatic("java/lang/Boolean",
                             "FALSE",
                             "Ljava/lang/Boolean;"));
        return seq;

    }

    /** Syntax: (do ((<variable1> <init1> <step1>) ...)
     *              (<test> <expression>)
     *              <command> ...)
     * Do is an iteration construct.  It specifies a set of
     * veriables to be bound, how they are to be initialized
     * at the sert, and how they are to be updated on each
     * iteration.  Whena  termination condition is met, the loop
     * exits with a specified result value.
     *
     * Compiles to:
     *     aload_1
     *     invokevirtual BindingEnv/push()LBindingEnv;
     *
     *     dup
     *     <variable1>
     *     <init1> ...
     *     BindingEnv/bind(LString;LObject;)V
     *     [repeat for each variable]
     *
     *     astore_1
     * loop:
     *     <test>
     *     getstatic Boolean/FALSE
     *     if_acmpne done
     *     <command> ...
     *     pop
     *
     *     aload_1
     *     <variable1>
     *     <step1>
     *     [repeat for each variable]
     *
     *     invokevirtual BindingEnv/set(LString;LObject;)V
     *     [repeat for each variable]
     *
     *     goto loop
     *
     * done:
     *     <expression> ...
     *     aload_1
     *     invokevirtual BindiEnv/pop()LBindingEnv;
     *     astore_1
     *
     *
     */
    protected Sequence compileDo(Object exp) throws SchemeException
    {
        Object bindings = Cons.cadr(exp);
        Object termination = Cons.caddr(exp);
        Object commands = Cons.cdddr(exp);
        Object test = Cons.car(termination);
        Object expressions = Cons.cdr(termination);

        if(bindings == null || termination == null ||
           test == null || expressions == null)
            throw new SyntaxError("Invalid do loop: " +
                ReadEvalPrint.write(exp));

        String loop = Counter.makeNewLabel("loop");
        String done = Counter.makeNewLabel("done");

        Sequence seq = appendSequences(
            comment(write(exp)),
            aload(1),
            invokevirtual("COM/sootNsmoke/scheme/BindingEnv",
                          "push",
                          "()LCOM/sootNsmoke/scheme/BindingEnv;"));
        Sequence incr = comment("Increment variables");
        Sequence store = comment("Store variables");
        Cons vars = (Cons) bindings;
        while(vars != null)
        {
            Object v = vars.car();
            Symbol variable = (Symbol) Cons.car(v);
            Object init = Cons.cadr(v);
            seq = appendSequences(
                seq,
                comment("Initialize " + variable.getName()),
                dup(),
                compileConstant(variable.getName()),
                compileExpression(init),
                invokevirtual("COM/sootNsmoke/scheme/BindingEnv",
                              "bind",
                              "(Ljava/lang/String;Ljava/lang/Object;)V"));

            if(Cons.cddr(v) instanceof Cons)
            {
                Object step = Cons.caddr(v);

                incr = appendSequences(
                    incr,
                    aload(1),
                    compileConstant(variable.getName()),
                    compileExpression(step));
                store = store.append(
                    invokevirtual("COM/sootNsmoke/scheme/BindingEnv",
                                  "bind",
                                  "(Ljava/lang/String;Ljava/lang/Object;)V"));
            }
            vars = (Cons) vars.cdr();
        }


        seq = appendSequences(
            seq,
            astore(1),
            label(loop),
            compileExpression(test),
            getstatic("java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;"),
            if_acmpne(done),
            appendSequences(
                compileSequence(commands),
                pop(),
                incr,
                store,
                goto_(loop)));

        seq = appendSequences(seq,
            label(done),
            compileSequence(expressions),
            aload(1),
            invokevirtual("COM/sootNsmoke/scheme/BindingEnv",
                          "pop",
                          "()LCOM/sootNsmoke/scheme/BindingEnv;"),
            astore(1));

        return seq;
    }

    /** Syntax: (let <bindings> <body>)
     * <bindings> should have the form
     *    ((<variable1) <init1>) ...)
     * where each <init> is an expression, and <body> should be a
     * sequence of one or more expressions.  It is an error for a
     * <variable> to appear more than once in the list of
     * variables being bound.
     *
     * Semantics:
     * The <init>s are evaluated in the current environment (in
     * some unspecified order), the <variable>s are bound to
     * fresh locations holding the results, the <body> is
     * evaluated in the extended environment, and the
     * value of the last expression of <body> is returned.
     * Each binding of a <variable> has <body> as its region.
     *
     * Let* is similar to let, but the bindings are performed
     * sequentially from left to right, and the region of
     * a binding indicated by (<variable> <init> is that part of
     * the let* expression to the right of the binding.  This the
     * second binding is done in an environment in which the
     * first binding is visible, and so on.
     *
     * Compiles to:
     *     aload_1
     *     invokevirtual BindingEnv/push()LBindingEnv;
     *
     *     dup
     *     <variable1>
     *     <init1> ...
     *     BindingEnv/bind(LString;LObject;)V
     *     [repeat for each variable]
     *
     *     astore_1
     *     <body>
     *     aload_1
     *     invokevirtual BindiEnv/pop()LBindingEnv;
     *     astore_1
     *
     * For let*, perform a dup and astore_1 after the push
     */
    protected Sequence compileLet(Object exp) throws SchemeException
    {
        boolean let_star = false;
        boolean letrec = false;

        if(SchemeSyntax.isForm(exp, "let*"))
            let_star = true;
        else if(SchemeSyntax.isForm(exp, "letrec"))
            letrec = true;

        Sequence seq = appendSequences(
            comment(write(exp)),
            aload(1),
            invokevirtual("COM/sootNsmoke/scheme/BindingEnv",
                          "push",
                          "()LCOM/sootNsmoke/scheme/BindingEnv;"));
        if(let_star)
            seq = appendSequences(
                seq,
                dup(),
                astore(1));
        Object c = ((Cons) exp).cdr();
        if(!(c instanceof Cons))
            throw new SyntaxError("Invalid let expression: " +
                write(exp));
        Object bindings = ((Cons) c).car();
        Object body = ((Cons) c).cdr();

        while(bindings != null)
        {
            if(!(bindings instanceof Cons))
                throw new SyntaxError("Invalid let body " +
                    write(bindings));
            Object binding = ((Cons) bindings).car();
            if(!(binding instanceof Cons))
                throw new SyntaxError("Invalid variable binding " +
                    write(binding));
            Object variable = ((Cons) binding).car();
            Object inits = ((Cons) binding).cdr();

            seq = appendSequences(seq,
                dup(),
                compileConstant(((Symbol) variable).getName()),
                compileSequence(inits),
                invokevirtual("COM/sootNsmoke/scheme/BindingEnv",
                              "bind",
                              "(Ljava/lang/String;Ljava/lang/Object;)V"));

            bindings = ((Cons) bindings).cdr();
        }

        seq = appendSequences(seq,
            astore(1),
            compileSequence(body),
            aload(1),
            invokevirtual("COM/sootNsmoke/scheme/BindingEnv",
                          "pop",
                          "()LCOM/sootNsmoke/scheme/BindingEnv;"),
            astore(1));

        return seq;
    }


    /** Compiles a form of the form (cond <clause1> <clause2>...),
     * where each <clause> is of the form:
     *    (<test> <expression1>, <expression2>, ...)).
     * Semantics: A cond expresison is evaluated by evaluating the
     * <test> expressions of successive <clauses>s in order until
     * one of them eavlauates to a true value.  When a <test>
     * evaluates to a true value, then the remaining <expressions>
     * in its <clause> are evaluated in order,and the result of the
     * last <expression> in the <clause> is returned as the result
     * of the entire <cond> expression.  If the selected <clause>
     * contains only the <test> and no <expressions>s then the
     * value of the <test> is returned as the result.  If all
     * <test>s evaluate to false values, and there is no else cause,
     * then the result of the conditional expression is unspecified;
     * if there is an else clause, then it's expressions are
     * evaluated, and the value of the last one is returned. */
    protected Sequence compileCond(Object cond) throws SchemeException
    {
        Sequence seq = new EmptySequence();
        Sequence else_seq = null;
        String end = Counter.makeNewLabel("end");
        Cons c = (Cons) cond;

        while(c.cdr() != null)
        {
            c = (Cons) c.cdr();
            Cons clause = (Cons) c.car();
            String l2 = Counter.makeNewLabel("label");
            if(SchemeSyntax.isForm(clause, "else"))
            {
                Object expressions = clause.cdr();
                else_seq = compileSequence(expressions);
            }
            else
            {
                Object test = clause.car();
                Object expressions = clause.cdr();
                Sequence test_seq = compileExpression(test);
                Sequence expr_seq = compileSequence(expressions);
                Sequence clause_seq =
                            test_seq
                    .append(new Getstatic("java/lang/Boolean",
                                 "FALSE",
                                 "Ljava/lang/Boolean;"))
                    .append(new IfAcmpEq(l2))
                    .append(expr_seq)
                    .append(new Goto(end))
                    .append(new Label(l2));
                seq = parallelSequences(seq, clause_seq);
            }
        }

        if(else_seq == null)
            else_seq = new AconstNull();

        seq = parallelSequences(seq, else_seq).append(
            new Label(end));
        return seq;
    }


    /** Syntax: (case <key> <clause1> <clause2> ...)
     * <Key> may be any expression.  Each <clause>
     * should have the form:
     *     ((<datum1> ...) <expression1> <expression2>)
     * where each <datum,> is a external represetnation fo some
     * object.  All the <datum>s must be distinct.  The last
     * <clause> may be an "else clause", which has the form:
     *     (else <expression1> <expression2> ...)
     * Semantics: A case expression is evaluated as follows.
     * <Key> is evaluated and its result is compared against each
     * <datum>.  If the result of evaluating <key> is equivalent
     * (in the sense of eqv?) to a <datum>, then the expressions
     * are evaluated from left to right and the result of the lsat
     * expression in the <clause> is returned as the result of
     * the case expression.  If the result of evaluating <key>
     * is different frome very <datum>, then if there is an else
     * clause its expressions are evaluated and the result of the
     * lasat is the result of the case expression; otherwise,
     * the result of the case expression is unspecified.
     *
     * Compiles to:
     *    <key>
     *    dup
     *    <clause1-datum1>
     *    invokestatic isEqv/isEqv
     *    ifne label1:
     *    [more datum comparisons]
     *    goto label2
     * label1
     *    <clause1-expression1> <clause1-expression2> ...
     *    goto end
     * label2:
     *    [more clauses]
     * else:
     *    <else-clause>  (or aconst_null)
     * end:
     */
    protected Sequence compileCase(Object exp) throws SchemeException
    {
        Sequence else_seq = null;
        String end = Counter.makeNewLabel("end");
        Cons c = (Cons) ((Cons) exp).cdr();
        Object key_expr = c.car();
        Sequence seq = compileExpression(key_expr);

        while(c.cdr() != null)
        {
            c = (Cons) c.cdr();
            Cons clause = (Cons) c.car();
            if(SchemeSyntax.isForm(clause, "else"))
            {
                Object expressions = clause.cdr();
                else_seq = compileSequence(expressions);
            }
            else
            {
                Object data = clause.car();
                Object expressions = clause.cdr();
                if(!(data instanceof Cons))
                    throw new SchemeException("Elements of case must be a list");
                Cons datums = (Cons) data;
                Sequence datums_seq = comment(
                    "Compare " + key_expr + " to " + data);
                String label1 = Counter.makeNewLabel("label1_");
                String label2 = Counter.makeNewLabel("label2_");
                while(datums != null)
                {
                    Object datum = datums.car();
                    datums = (Cons) datums.cdr();
                    datums_seq = appendSequences(
                        datums_seq,
                        dup(),
                        compileConstant(datum),
                        invokestatic("COM/sootNsmoke/scheme/SchemeLibrary",
                                     "isEqv",
                                     "(Ljava/lang/Object;Ljava/lang/Object;)Z"),
                        ifne(label1));
                }
                seq = parallelSequences(
                    seq,
                    appendSequences(
                        datums_seq,
                        goto_(label2),
                        label(label1),
                        compileSequence(expressions),
                        goto_(end),
                        label(label2)));
            }
        }

        if(else_seq == null)
            else_seq = new AconstNull();

        seq = parallelSequences(seq, else_seq).append(
            new Label(end));
        return seq;
    }

    /** Compiles a sequence.  The sequence
     * takes the form (expr1 expr2 ... exprn) and compiles it
     * into:
     *    <expr1>
     *    pop
     *    <expr2>
     *    pop
     *    ...
     *    <exprn>
     */
    protected Sequence compileSequence(
        Object seq) throws SchemeException
    {
        if(SchemeSyntax.isNull(seq))
            return aconst_null();
        if(SchemeSyntax.isLastExp(seq))
            return compileExpression(SchemeSyntax.firstExp(seq));
        else
            return appendSequences(
                compileExpression(SchemeSyntax.firstExp(seq)),
                new Pop(),
                compileSequence(SchemeSyntax.restExps(seq)));

    }

    /** Compiles a Scheme expression (define var value) into
     * env.bind(var, <value>)
     */
    protected Sequence compileDefinition(
        Object exp) throws SchemeException
    {
        Object variable = SchemeSyntax.definitionVariable(exp);
        return appendSequences(
        new Aload(1),
        new Ldc(((Symbol) variable).getName()),
        compileExpression(SchemeSyntax.definitionValue(exp)),
        new InvokeVirtual(
            "COM/sootNsmoke/scheme/BindingEnv",
            "bind",
            "(Ljava/lang/String;Ljava/lang/Object;)V",
             2),
        new AconstNull());
    }

    /** Syntax: (set! <variable> <expression>)
     * <expresion> is evaluated, and teh resulting value is
     * stored in the location to which <variable> is bound.
     * <Variable> must be bound either in some region enclosing
     * the set! expresion or at top level.  The result of the
     * set! expression is undefined.
     *
     * Compiles to:
     *    aload_1
     *    <variable>
     *    <expression>
     *    invokevirtual BindingEnv/set(LString;LObject;)V
     *    aconst_null
     */
    protected Sequence compileSetBang(Object exp) throws SchemeException
    {
        Object body = ((Cons) exp).cdr();
        if(!(body instanceof Cons))
            throw new SyntaxError("Invalid set! expression " +
                write(exp));
        Object variable = ((Cons) body).car();
        Object expr = ((Cons) body).cdr();
        return appendSequences(
            aload(1),
            compileConstant(((Symbol) variable).getName()),
            compileSequence(expr),
            invokevirtual("COM/sootNsmoke/scheme/BindingEnv",
                          "set",
                          "(Ljava/lang/String;Ljava/lang/Object;)V"),
            aconst_null());
    }


    /** Syntax: (delay <expression>)
     * The delay construct is used together with the procedure
     * force to implement lazy evaluation or call by need.
     * (delay <expression>) returns an object called a promise
     * which at some point in the future may be asked (by the
     * force procedure) to evaluate <expression> and deliver
     * the resulting value.
     *
     * Compilation:
     *   new Promise
     *   dup
     *   [lambda]
     *   invokevirtual Promise/<init>(LCompiledProcedure;)V
     *
     * where [lambda] is the code produced by compiling
     * (lambda () <expression>).
     *
     * @see compileLambda
     */
     Sequence compileDelay(Object exp) throws SchemeException
     {
        Cons c = (Cons) exp;
        if(!(c.cdr() instanceof Cons))
            throw new SyntaxError("delay requires an expression " + exp);
        Object expression = ((Cons) c.cdr()).car();
        Object lambda = new Cons("lambda",
                        new Cons(null,
                        new Cons(expression, null)));

        Sequence seq = appendSequences(
            new_("COM/sootNsmoke/scheme/Promise"),
            dup(),
            compileLambda(lambda),
            invokespecial("COM/sootNsmoke/scheme/Promise",
                          "<init>",
                          "(LCOM/sootNsmoke/scheme/CompiledProcedure;)V"));
        return seq;
     }


    /**
     * Returns a new Sequence which places a reference to
     * the compiled procedure on the stack.  The body is
     * compiled into a compiled procedure, which is loaded
     * using the loader.
     *
     */
    protected Sequence compileLambda(
        Object exp) throws SchemeException
    {
        String lambda_name = Counter.makeNewLabel("lambda");
        Sequence code = compileLambdaBody(exp);

        int num_params = numParams(SchemeSyntax.lambdaParameters(exp));
        JavaClass javaclass = buildClass(lambda_name, code, num_params);

        try {
            ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            javaclass.write(os);
            loader.load(lambda_name, os.toByteArray());
        }
        catch(Exception e)
        {
            // Not really expecting exceptions here
            e.printStackTrace();
        }

        Sequence createClosure =
            appendSequences(
                new New(lambda_name),
                new Dup(),
                new Aload(1),
                new InvokeSpecial(
                    lambda_name,
                    "<init>",
                    "(LCOM/sootNsmoke/scheme/BindingEnv;)V",
                    1));

        return createClosure;
    }

    protected Sequence compileLambdaBody(
        Object exp) throws SchemeException
    {
        return appendSequences(
            makeEnvironmentSwitch(SchemeSyntax.lambdaParameters(exp)),
            compileSequence(SchemeSyntax.lambdaBody(exp)),
            new Areturn());
    }

    public Class load(String classname, Sequence seq)
    {
        try
        {
            JavaClass cf = buildClass(classname, seq, 0);
            java.io.ByteArrayOutputStream os =
                new java.io.ByteArrayOutputStream();
            cf.write(os);
            Class c = loader.load(classname, os.toByteArray());
            loader.loadClass(classname, true);
            return c;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    /** Compile the instruction sequence, and return a Java class
     * subclassed from CompiledProcedure which implements it.
     * The instruction sequence will have a method named
     * applyN, where N is the number of arguments.  It will also have
     * a null constructor and a constructor which takes an environment,
     * which it will use to set the CompiledProcedure's env field.
     */
    JavaClass buildClass(String classname, Sequence seq, int num_params)
    {
        try {
            JavaClass cf = new JavaClass(classname, "COM/sootNsmoke/scheme/CompiledProcedure");
            cf.setAccess(ACC_PUBLIC);

            Sequence constructor0 = appendSequences(
                new Aload(0),
                new InvokeSpecial(
                    "COM/sootNsmoke/scheme/CompiledProcedure",
                    "<init>",
                    "()V", 0),
                new Return());
            Bytecodes bytecodes;
            bytecodes = new Bytecodes(constructor0, cf);
            cf.addMethod("<init>",
                         "()V",
                          ACC_PUBLIC,
                          1,
                          1,
                         bytecodes.toByteArray(),
                         new ExceptionTableEntry[0],
                         new Attribute[0],
                         new Attribute[0]);

            // Bytecodes for constructor taking 1 argument
            Sequence constructor1 = appendSequences(
                new Aload(0),
                new Aload(1),
                new InvokeSpecial(
                    "COM/sootNsmoke/scheme/CompiledProcedure",
                    "<init>",
                    "(LCOM/sootNsmoke/scheme/BindingEnv;)V", 1),
                new Return());

            bytecodes = new Bytecodes(constructor1, cf);
            cf.addMethod("<init>",
                         "(LCOM/sootNsmoke/scheme/BindingEnv;)V",
                          ACC_PUBLIC,
                          2,
                          2,
                         bytecodes.toByteArray(),
                         new ExceptionTableEntry[0],
                         new Attribute[0], new Attribute[0]);

            // The actual function
            bytecodes = new Bytecodes(seq, cf);
            cf.addMethod("apply" +
                         (num_params != arbitraryParameterLength &&
                          num_params <= maxDirectArgs
                         ? Integer.toString(num_params) : "N"),
                         signature(num_params),
                          ACC_PUBLIC,
                          seq.max_stack(),
                          (num_params != arbitraryParameterLength
                               && num_params <= maxDirectArgs
                                  ? num_params+3 : 3),
                         bytecodes.toByteArray(),
                         new ExceptionTableEntry[0],
                         new Attribute[0], new Attribute[0]);
            if(save)
                try
                {
                    OutputStream os =
                        new FileOutputStream(classname + ".class");
                    cf.write(os);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            return cf;
        }
        catch(UndefinedLabelException e) {
            // Not really expecting this
            throw new RuntimeException("Unexpected exception: " + e.getMessage());
        }
    }

    /** Generate code to extend the environment with the arguments */
    protected Sequence makeEnvironmentSwitch(Object params)
    {
        int num_params = numParams(params);
        // Push a new binding environment on the stack
        Sequence seq = appendSequences(
            new Aload(0),
            new Getfield("COM/sootNsmoke/scheme/CompiledProcedure",
                         "env",
                         "LCOM/sootNsmoke/scheme/BindingEnv;"),
            new InvokeVirtual("COM/sootNsmoke/scheme/BindingEnv",
                              "push",
                              "()LCOM/sootNsmoke/scheme/BindingEnv;",
                              0));

        Object params_list = params;

        // Special case where there are only a few parameters
        if(num_params <= maxDirectArgs &&
           num_params != arbitraryParameterLength)
        {
            int num_args = 0;
            while(params_list != null)
            {
                Symbol sym = (Symbol) ((Cons) params_list).car();
                params_list = ((Cons) params_list).cdr();

                // Push the binding environment into which this arg will be bound
                // Put the symbol name on the stack
                // Put the arg on the stack
                // Do the binding
                seq = appendSequences(
                    seq,
                    new Dup(),
                    new Ldc(((Symbol) sym).getName()),
                    new Aload(num_args+1),
                    new InvokeVirtual(
                        "COM/sootNsmoke/scheme/BindingEnv",
                        "bind",
                        "(Ljava/lang/String;Ljava/lang/Object;)V",
                        2));
                num_args++;
            }
        }
        else    // General case (many params, or variable number
        {
            // cdr down the list of params.
            // For each one, bind the car of the argument list to it
            while(params_list instanceof Cons)
            {
                Symbol sym = (Symbol) ((Cons) params_list).car();
                params_list = ((Cons) params_list).cdr();

                seq = appendSequences(
                    seq,
                    new Dup(),
                    new Ldc(((Symbol) sym).getName()),
                    appendSequences(
                        new Aload(1),
                        new CheckCast("COM/sootNsmoke/scheme/Cons"),
                        new InvokeVirtual(
                            "COM/sootNsmoke/scheme/Cons",
                            "car",
                            "()Ljava/lang/Object;",
                            0),
                        new InvokeVirtual(
                            "COM/sootNsmoke/scheme/BindingEnv",
                            "bind",
                            "(Ljava/lang/String;Ljava/lang/Object;)V",
                            2)),
                    appendSequences(
                        new Aload(1),
                        new CheckCast("COM/sootNsmoke/scheme/Cons"),
                        new InvokeVirtual(
                            "COM/sootNsmoke/scheme/Cons",
                            "cdr",
                            "()Ljava/lang/Object;",
                            0),
                        new Astore(1)));
            }

            // If there's a symbol left, bind the rest of the list to it
            if(params_list instanceof Symbol)
            {
    		    Symbol sym = (Symbol) params_list;
                seq = appendSequences(
                    seq,
                    new Dup(),
                    new Ldc(((Symbol) sym).getName()),
                    new Aload(1),
            	    new InvokeVirtual(
            	        "COM/sootNsmoke/scheme/BindingEnv",
                        "bind",
                        "(Ljava/lang/String;Ljava/lang/Object;)V",
                        2));
            }
        }

        // Store new env in a convenient place
        return appendSequences(
            seq,
            new Astore(1));
    }

    /** Generates code to give the variable the value of init, where init
     * is code which leaves the initial value of the variable on the stack.
     */
    protected Sequence makeVariableBinding(Object variable, Sequence init)
    {
        Sequence seq = appendSequences(
            new Aload(1),
            new Ldc(((Symbol) variable).getName()),
            appendSequences(
                init,
                new InvokeVirtual(
                    "COM/sootNsmoke/scheme/BindingEnv",
                    "bind",
                    "(Ljava/lang/String;Ljava/lang/Object;)V",
                     2),
                new AconstNull()));
        return seq;
    }

//***************************************************************************
// Convenience stuff
//***************************************************************************

    /** Appends two instruction sequences in parallel.  */
    protected static Sequence parallelSequences(
        Sequence seq1, Sequence seq2)
    {
        return seq1.parallelSequence(seq2);
    }
    /** Appends two instruction sequences.   */
    protected static Sequence appendSequences(
        Sequence seq1, Sequence seq2)
    {
        return seq1.appendSequence(seq2);
    }

    /** Appends three instruction sequences.   */
    protected static Sequence appendSequences(
        Sequence seq1, Sequence seq2, Sequence seq3)
    {
        return seq1.appendSequence(seq2).appendSequence(seq3);
    }


    /** Appends four instruction sequences.   */
    protected static Sequence appendSequences(
        Sequence seq1, Sequence seq2, Sequence seq3, Sequence seq4)
    {
        return seq1.appendSequence(seq2).appendSequence(seq3).appendSequence(seq4);
    }

    /** Appends five instruction sequences.   */
    protected static Sequence appendSequences(
        Sequence seq1, Sequence seq2, Sequence seq3, Sequence seq4, Sequence seq5)
    {
        return seq1.appendSequence(seq2).appendSequence(seq3).appendSequence(seq4).appendSequence(seq5);
    }
    /** Appends six instruction sequences */
    protected static Sequence appendSequences(
        Sequence seq1, Sequence seq2, Sequence seq3,
        Sequence seq4, Sequence seq5, Sequence seq6)
    {
        return seq1.appendSequence(seq2).appendSequence(seq3)
                   .appendSequence(seq4).appendSequence(seq5)
                   .appendSequence(seq6);
    }
    /** Appends seven instruction sequences */
    protected static Sequence appendSequences(
        Sequence seq1, Sequence seq2, Sequence seq3,
        Sequence seq4, Sequence seq5, Sequence seq6,
        Sequence seq7)
    {
        return seq1.appendSequence(seq2).appendSequence(seq3)
                   .appendSequence(seq4).appendSequence(seq5)
                   .appendSequence(seq6).appendSequence(seq7);
    }

    /** Takes a lambda parameter expression and returns number of arguments
     * to it. If the number of parameters is indefinite, return
     * arbitraryParameterLength.
     */
    int numParams(Object params)
    {
        int n = 0;
        while(params instanceof Cons)
        {
            n++;
            params = ((Cons) params).cdr();
        }

        if(SchemeSyntax.isNull(params))
            return n;
        else
            return arbitraryParameterLength;
    }

    static String signature(int num_params)
    {
        switch(num_params)
        {
            case 0: return "()Ljava/lang/Object;";
            case 1: return "(Ljava/lang/Object;)Ljava/lang/Object;";
            case 2: return "(Ljava/lang/Object;Ljava/lang/Object;)" +
                           "Ljava/lang/Object;";
            case 3: return "(Ljava/lang/Object;Ljava/lang/Object;" +
                           "Ljava/lang/Object;)Ljava/lang/Object;";
            default: return "(LCOM/sootNsmoke/scheme/Cons;)Ljava/lang/Object;";
        }
    }

    static String write(Object o)
    {
        return ReadEvalPrint.write(o);
    }
}

/** Provides a central one-up counter */
class Counter
{
    static int val = 0;

    static int next()
    {
        return val++;
    }

    public static String makeNewLabel(String name)
    {
        return name + Integer.toString(Counter.next());
    }
}
