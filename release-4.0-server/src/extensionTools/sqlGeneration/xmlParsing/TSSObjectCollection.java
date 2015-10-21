package extensionTools.sqlGeneration.xmlParsing;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *                              Class - TSSObjectCollection:
 *                              
 * Collection that holds all TSSObjects.
 */

public class TSSObjectCollection extends ArrayList
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TSSObjectCollection()
    {
    }
    
    /**
     * Remove single object from the collection by object name
     * (tss name)
     * 
     * @param objectName
     */
    public void removeTSSObject (String objectName)
    {
        Iterator it = this.iterator();
        TSSObject currObject;
        
        while (it.hasNext())
        {
            currObject = (TSSObject)it.next();
            if (currObject.getName().equalsIgnoreCase(objectName) == true)
            {
                it.remove();
            }
        }
    }
    
    /**
     * Find object by name and return it
     * It the object does not exist so return null
     * @param attrName
     * @return 
     */
    public TSSObject getTSSObjectByAttributeName (String Name)
    {
        Iterator it = this.iterator();
        TSSObject currObject;
        
        while (it.hasNext())
        {
            currObject = (TSSObject)it.next();
            if (currObject.getName().equalsIgnoreCase(Name) == true)
            {
                return (currObject);
            }
        }
        
        return (null);
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        
    }
}