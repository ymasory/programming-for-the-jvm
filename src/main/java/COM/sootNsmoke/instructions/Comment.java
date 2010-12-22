package COM.sootNsmoke.instructions;
import COM.sootNsmoke.jvm.*;

/** CommentSequence has no effect on the compiled code,
 * but when the sequence is printed the comments appear
 */
public class Comment extends  EmptySequence
{
    String comment;
    /** Comment is the comment to print out.
     */
    public Comment(String comment)
    {
        this.comment = comment;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        int begin = 0;
        while(true)
        {
            int end = comment.indexOf("\n", begin);
            if(end == -1 && begin == 0)
                return ";" + comment;
            if(end == -1)
                return sb.append("; " + comment.substring(begin)).toString();
            else
                sb.append("; " + comment.substring(begin, end+1));
        }
    }
}
