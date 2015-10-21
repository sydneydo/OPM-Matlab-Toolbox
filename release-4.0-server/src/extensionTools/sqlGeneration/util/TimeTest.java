package extensionTools.sqlGeneration.util;
import java.util.Date;

public class TimeTest implements Constants
{
    long startTime = 0;
    
    public TimeTest()
    {
    }

    private void rawStart ()
    {
        startTime = new Date().getTime();
    }
    
    /**
     * Function returns number of seconds that the proccess worked.
     * If start was not called before this function, so -1 will be returned
     * 
     * @param processDescription
     * @return 
     */
    private double rawFinish (String processDescription)
    {
        long finishTime = new Date().getTime();
        long difference;
        
        if (startTime == 0)
        {
            difference = (-1);
        }
        else
        {
            difference = finishTime - startTime;
        }
        
        double diff = difference / 1000d;
        logger.out();
        logger.out("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");   
        logger.out(processDescription + " took " + diff + " seconds");        
        logger.out("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");   
        logger.out();
        
        return (diff);
    }
    
    public void start ()
    {
        rawStart();
    }
    
    public void begin ()
    {
        rawStart();
    }
    
    public void end (String processDescription)
    {
        rawFinish(processDescription);
    }
    
    public void finish (String processDescription)
    {
        rawFinish(processDescription);
    }
    
    
    
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        
    }
}