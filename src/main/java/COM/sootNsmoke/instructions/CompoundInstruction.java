package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;
import java.util.*;

public class CompoundInstruction extends Sequence
{
    Sequence[] seqs;

    public String toString()
    {
        StringBuffer s = new StringBuffer();
        for(int i = 0; i < seqs.length; i++)
        {
            if(i > 0)
                s.append("\n");
            s.append(seqs[i]);
        }
        return s.toString();
    }

    /** Combine all of the sequences in sequence */
    public CompoundInstruction(Sequence[] seqs)
    {
        super(0,0,-1);

        for(int i = 0; i < seqs.length; i++)
        {
            max_stack = Math.max(max_stack(), net_stack() + seqs[i].max_stack());
            net_stack = net_stack() + seqs[i].net_stack();
            max_vars = Math.max(max_vars(), seqs[i].max_vars());
        }

        this.seqs = seqs;
    }

    /** Combine sequences a and b sequentially */
    public CompoundInstruction( Sequence a,  Sequence b)
    {
        /*
        super(Math.max(a.max_stack(), a.net_stack()+b.max_stack()),
              a.net_stack() + b.net_stack(),
              Math.max(a.max_vars(), b.max_vars()));
        seqs = new Sequence[2];
        seqs[0] = a;
        seqs[1] = b;
        */
        this(array(a, b));
    }

    private static Sequence[] array(Sequence a, Sequence b)
    {
        Sequence[] seq = new Sequence[] { a, b };
        return seq;
    }

    /** Used to build parallel instruction sequences.  The parallel parameter is
     * ignored; its presence is used to distinguish this constructor
     */
    public CompoundInstruction( Sequence a,  Sequence b, boolean parallel)
    {
        super(Math.max(a.max_stack(), b.max_stack()),
              Math.max(a.net_stack(), b.net_stack()),
              Math.max(a.max_vars(),  b.max_vars()));
        seqs = new Sequence[2];
        seqs[0] = a;
        seqs[1] = b;
    }

    public void toBytecodes(Bytecodes bytecodes)
    {
        for(int i = 0; i < seqs.length; i++)
            seqs[i].toBytecodes(bytecodes);
    }
}

