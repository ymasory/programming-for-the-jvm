package COM.sootNsmoke.prolog;

class SyntaxError extends RuntimeException
{
    SyntaxError(PrologLexer lex, String msg)
    {
        super(lex.lineno() + ": " + msg);
    }
}