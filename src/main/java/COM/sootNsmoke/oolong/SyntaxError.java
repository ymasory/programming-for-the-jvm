package COM.sootNsmoke.oolong;

class SyntaxError extends Exception
{
    SyntaxError(Oolong oolong, String msg)
    {
        super(oolong.inputFilename + ":" + oolong.lexer.lineno() + ":" + msg);

    }
}
