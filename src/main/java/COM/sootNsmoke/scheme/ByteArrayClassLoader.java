package COM.sootNsmoke.scheme;
import java.util.*;

/** This is a classloader that is capable of loading up a class from an
 * array of bytes
 */
public class ByteArrayClassLoader extends ClassLoader
{
    Hashtable cache = new Hashtable();

    public synchronized Class loadClass(String name, boolean resolve) throws java.lang.ClassNotFoundException
    {
        Class c = (Class) cache.get(name);
        boolean isSystem = false;
        if(c == null)
        {
            c = findSystemClass(name);
            isSystem = true;
        }
        if(c != null && resolve)
        {
            resolveClass(c);
        }
        return c;
    }

    public Class fetchClass(String name)
    {
        return (Class) cache.get(name);
    }

    public Class load(String name, byte[] data)
        throws ClassNotFoundException
    {
        Class c = defineClass(data, 0, data.length);
        cache.put(name, c);
        return c;
    }
}