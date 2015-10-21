
package extensionTools.opcatLayoutManager.autoArrange;


public final class Utils
{
    /**
     * A small auxiliary function that extracts the last component of the
     * class description of the given object.
     */
    public static String showClass(Object o)
    {
        String cn = o.getClass().getName();
        int lastDot = cn.lastIndexOf('.');
        if (lastDot > -1)
            cn = cn.substring(lastDot + 1);
        
        return cn;
    }
    
    /**
     * A small auxiliary function that calculate the square of the given number
     */
    public static long square(long n)
    {
        return n * n;
    }
    
    /**
     * A small auxiliary function that calculate the square of the given number
     */
    public static double square(double n)
    {
        return n * n;
    }
    
    /**
     * A small auxiliary function that rounds a double and returns an int
     */
    public static int round(double n)
    {
        return (int) Math.round(n);
    }
}
