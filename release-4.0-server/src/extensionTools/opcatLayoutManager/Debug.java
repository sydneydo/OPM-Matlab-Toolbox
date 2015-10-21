package extensionTools.opcatLayoutManager;

import java.text.MessageFormat;

/**
 * This class contains various debug helpers.
 */
public class Debug
{
    /**
     * Prints a debug message.
     *
     * @param message A message to print.
     */
    public static void Print(String message)
    {
        Print(0, message);
    }

    /**
     * Prints a debug message at a given verbosity level.
     *
     * @param level A verbosity level.
     *
     * @param message A message to print.
     */
    public static void Print(int level, String message)
    {
        if (level > LEVEL)
        {
            return;
        }

        StringBuffer outputBuffer = new StringBuffer();

        if (DISPLAY_LEVEL)
        {
            outputBuffer.append("[");
            outputBuffer.append(level);
            outputBuffer.append("] ");
        }

        if (DISPLAY_TIME)
        {
            outputBuffer.append("[");
            outputBuffer.append(System.currentTimeMillis());
            outputBuffer.append("] ");
        }

        outputBuffer.append(message);

        System.out.println(outputBuffer);
    }

    /**
     * Formats and prints a debug message.
     *
     * @param A format string.
     *
     * @param An array of parameters for formatting the message.
     */
    public static void Print(String format, Object[] args)
    {
        Print(0, format, args);
    }

    /**
     * Formats and prints a debug message at a given verbosity level.
     *
     * @param A verbosity level.
     *
     * @param A format string.
     *
     * @param An array of parameters for formatting the message.
     */
    public static void Print(int level, String format, Object[] args)
    {
        if (level > LEVEL)
        {
            return;
        }

        Print(level, MessageFormat.format(format, args));
    }

    /**
     * Checks whether the system is operating in the debug mode.
     *
     * @return true if the system is operating in the debug mode, false otherwise.
     */
    public static boolean IsDebug()
    {
        return LEVEL > 0;
    }

    private static int     LEVEL         = 0;
    private static boolean DISPLAY_LEVEL = true;
    private static boolean DISPLAY_TIME  = true;
}
