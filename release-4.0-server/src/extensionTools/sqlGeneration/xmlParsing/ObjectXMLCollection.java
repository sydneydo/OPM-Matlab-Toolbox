package extensionTools.sqlGeneration.xmlParsing;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *                            Class ObjectXMLCollection
 *                            *************************
 *                            
 * This Class Represents a Sentence as a collection of the ObjectXML.
 */

public class ObjectXMLCollection extends ArrayList
{   
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ObjectXMLCollection()
    {
    }

    /**
     * Remove single object from the collection by object name
     * (attribute name)
     * 
     * @param objectName
     */
    public void removeXMLObject (String objectName)
    {
        Iterator it = this.iterator();
        ObjectXML currObject;
        
        while (it.hasNext())
        {
            currObject = (ObjectXML)it.next();
            if (currObject.getAttrName().equalsIgnoreCase(objectName) == true)
            {
                it.remove();
            }
        }
    }
    
    /**
     * Find object by attibute name and return it
     * It the object does not exist so return null
     * @param attrName
     * @return 
     */
    public ObjectXML getXMLObjectByAttributeName (String attrName)
    {
        Iterator it = this.iterator();
        ObjectXML currObject;
        
        while (it.hasNext())
        {
            currObject = (ObjectXML)it.next();
            if (currObject.getAttrName().equalsIgnoreCase(attrName) == true)
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