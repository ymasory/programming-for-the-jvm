package COM.sootNsmoke.prolog;

/** A cons cell */
public class Cons
{
    public Object head;
    public Object tail;

    public Cons(Object head, Object tail)
    {
        this.head = head;
        this.tail = tail;
    }

    String consBodyString(Prolog p)
    {
        StringBuffer s = new StringBuffer(p.toString(head));
        tail = p.deref(tail);
        if(tail == null)
            ;
        else if(tail instanceof Cons)
            s.append(", " + ((Cons) tail).consBodyString(p));
        else
            s.append(" | " + p.toString(tail));
        return s.toString();
    }

    /** Print the array, dereferencing the variables */
    public String toString(Prolog p)
    {
        return "[" + consBodyString(p) + "]";
    }
}