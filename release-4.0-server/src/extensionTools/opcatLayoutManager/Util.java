package extensionTools.opcatLayoutManager;

/**
 * Exposes various utility functions.
 */
public class Util
{
    /**
     * Calculates a square (degree 2) of a value.
     *
     * @param value The value to be raised to degree 2.
     *
     * @return The square of the value.
     */
    public static double Square(double value)
    {
        return value * value;
    }

    /**
     * Checks whether a given double is "valid".
     *
     * @param The double to check.
     *
     * @return true if the double is "valid", false otherwise.
     */
    public static boolean IsValidDouble(double number)
    {
        Double objDouble = new Double(number);
        return (objDouble.compareTo(new Double(0)) > 0) && (objDouble.compareTo(new Double(10000)) < 0);
    }
}
