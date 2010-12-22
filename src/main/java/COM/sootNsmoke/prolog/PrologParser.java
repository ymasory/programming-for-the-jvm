package COM.sootNsmoke.prolog;
import java.io.*;
import java.util.Vector;


/** A parer which reads in Prolog statements from the input
 * stream
 */
class PrologParser
{
    PrologLexer lexer;
    PrologParser(InputStream is)
    {
        lexer = new PrologLexer(is);
    }

    /** The top level of Prolog parser. Returns null on EOF
     * command := statement '.'
     */
    Object command() throws IOException
    {
        Object statement = statement();
        if(lexer.nextWord().equals("."))
            return statement;
        else
        {
            throw new SyntaxError(lexer,
                " command " + statement + " must end with a period");
        }
    }

    /** Parses a Prolog statement.
     * statement := [term conjunct]* term
     * where conjunct is any symbol but a period
     */
    Object statement() throws IOException
    {
        Object head = term();
        String conjunct = lexer.nextWord();
        if(conjunct.equals("."))
        {
            lexer.pushBack();
            return head;
        }
        else
        {
            return new Structure(conjunct,
                head,
                statement());

        }
    }


    /** Parse a term.
     * term := word [ '(' term [, term]* ')' ]
     * term := '[' [term [,term]*] ']'
     */
    Object term() throws IOException
    {
        String head = lexer.nextWord();
        if(head.equals("["))
        {
            String next = lexer.nextWord();
            if(next.equals("]"))
                return null;
            lexer.pushBack();
            Cons list = list();
            next = lexer.nextWord();
            if(!next.equals("]"))
               throw new SyntaxError(lexer, "List must end with ], found " + next);
            return list;
        }

        String tok = lexer.nextWord();
        if(!tok.equals("("))
        {
            lexer.pushBack();
            // Upper-case are variables; lower-case are constants
            if(Character.isUpperCase(head.charAt(0)))
                return new Var(head);
            return head;
        }
        Vector v = new Vector();
        Object term = term();
        v.addElement(term);
        while(true)
        {
            String word = lexer.nextWord();
            if(word.equals(","))
            {
                term = term();
                v.addElement(term);
            }
            else if(word.equals(")"))
            {
                Object[] arg = new Object[v.size()];
                v.copyInto(arg);
                return new Structure(head, arg);
            }
            else
                throw new SyntaxError(lexer, "Unexpected word " + word);
        }

    }

    /** Returns the body of a list.
     * list ::= term [',' term]*
     */
    Cons list() throws IOException
    {
        Object head = term();
        Cons list = new Cons(head, null);
        Cons current = list;
        while(true)
        {
            String word = lexer.nextWord();
            if(word.equals(","))
            {
                head = term();
                current.tail = new Cons(head, null);
                current = (Cons) current.tail;
            }
            else if(word.equals("|"))
                current.tail = term();
            else
            {
                lexer.pushBack();
                break;
            }
        }
        return list;
    }
}

/** A lexical analyzer for Prolog.
 * Lexically, prolog is similar to Java, except that
 * symbols like : and - are valid symbol characters.
 * Also, numbers are not parsed.
 */
class PrologLexer extends StreamTokenizer
{
    PrologLexer(InputStream is)
    {
        super(is);
        wordChars(':', ':');
        wordChars('-', '-');
        wordChars('_', '_');
        ordinaryChar('.');
        ordinaryChars('0', '9');
        wordChars('0', '9');
    }

    String nextWord() throws IOException
    {
        int x = nextToken();
        if(x == TT_EOF)
            throw new IOException("EOF");
        else if(x == TT_WORD || x == '"')
            return sval;
        else
        {
            return new Character((char) x).toString();
        }
    }
}
