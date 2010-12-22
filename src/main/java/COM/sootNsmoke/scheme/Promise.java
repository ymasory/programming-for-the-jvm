package COM.sootNsmoke.scheme;

/** A promise is returned by the delay construct.  It contains
 * code to evaluate an expression, but that code is not called
 * until the promise is forced with the force procedure.
 *
 * Once it has been forced, the expression is not evaluated
 * again, but the original expression value is returned.
 */
public class Promise
{
    Object value;

    /** expr should be a compiled procedure expecting no
     * arguments
     */
    CompiledProcedure expr;

    public Promise(CompiledProcedure expr)
    {
        this.expr = expr;
    }

    public Object force()
    {
        if(expr != null)
        {
            value = expr.apply0();
            expr = null;
        }
        return value;
    }
}