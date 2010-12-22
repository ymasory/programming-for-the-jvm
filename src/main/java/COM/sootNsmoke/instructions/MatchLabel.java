package COM.sootNsmoke.instructions;

/** A pair int x String to represent match-offset pairs in a lookupswitch */
public class MatchLabel
{
    public int match;
    public String label;

    public MatchLabel(int match, String label)
    {
        this.match = match;
        this.label = label;
    }
}
