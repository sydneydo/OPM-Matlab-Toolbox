package extensionTools.sqlGeneration.util;

import java.io.*;
import java.util.*;

/**
 *                                     Class Logger
 *                                     ************
 *                                     
 * This class the the program logger. it writes the exceptions to an outside
 * file.
 */
public class Logger 
{
    File logFile = null;
    boolean isAppend = true;
    boolean isDebug = false;
    
    public Logger()
    {
        this("C:\\LOG_" + new Date().getTime() + ".log", false);
    }
    
    public Logger(String filename, boolean isToOverrride)
    {
        try
        {
            logFile = new File(filename);
            if (logFile.exists() == false)
            {
                logFile.createNewFile();
            }
            else if (isToOverrride == true)
            {
                logFile.delete();
                logFile.createNewFile();
            }
            
            out ("");
            outStars ();
            out ("PROGRAM STARTED AT: " + new Date().toString());
            outStars ();
            out ("");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public Logger(String filename, boolean isToOverrride, boolean isDebug)
    {
        this(filename, isToOverrride);
        this.isDebug = isDebug;
    }
    
    public void outStars ()
    {
        out("**************************************************************************************");
    }
    
    public void out ()
    {
        out("");    
    }
    
    /**
     * 
     * @param e
     */
    public void out (Exception e)
    {
        e.printStackTrace();
        out ("Exception: " + e.getMessage());
    }

    /**
     * Print one line to the log file.
     * @param stringToWrite
     */
    public void out(String stringToWrite)
    {
        try
        {
            PrintWriter out
                 = new PrintWriter(new BufferedWriter(new FileWriter(logFile, isAppend)));
                 
            out.println(stringToWrite);
            
            out.close();
            
            if (isDebug == true)
            {
                System.out.println(stringToWrite);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    
    /**
     * Open LOG file
     */
    public boolean openMe ()
    {
        if (logFile.exists() == true)
        {
            try
            {
                Runtime rt = Runtime.getRuntime();
                rt.exec("NOTEPAD " + logFile.getAbsolutePath());
                return (true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return (false);
    }
    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {        
        
    }
}