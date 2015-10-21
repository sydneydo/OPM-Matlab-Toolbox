package extensionTools.sqlGeneration.xmlParsing;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 *                              Class - ElementOpcat: 
 *                              
 * This Class is a wrapper for the JDOM Element Class.
 * It is used in order to handle the elements values and attributes values.
 *                              
 */

public class ElementOpcat
{
    private Element element = null;
    
    public ElementOpcat()
    {  
    }
    
    public ElementOpcat(Element element)
    {   
        this.element = element;
    }
    
    /**
     * returns the attribute value without spaces.
     * @param attName - attribute name.
     * @param defualtString 
     * @return 
     */
    public String getAttributeValue(String attName , String defualtString)
    {
        String result = element.getAttributeValue(attName , defualtString);
        return (clearString(result));
    }
    
    /**
     * Leave in the result string only LETTERS, letters and underscore
     * 
     * @param stringToClear
     * @return 
     */
    public String clearString (String stringToClear)
    {
        String result = null;
        if (stringToClear != null)
        {
            result = "";
            char[] arrChars = stringToClear.toCharArray();
            
            for (int i = 0; i < arrChars.length; i++)
            {
                if ((arrChars[i] >= 'A' && arrChars[i]<= 'Z') ||
                    (arrChars[i] >= 'a' && arrChars[i]<= 'z') ||
                    (arrChars[i] >= '0' && arrChars[i]<= '9') ||
                    arrChars[i] == '_' || arrChars[i] == '-')
                {
                    result = result + arrChars[i];
                }
            }
        }
        return (result);
    }
    
    /**
     * return the element value.
     * @return 
     */
    public String getValue()
    {
        String result = element.getValue();
        return (clearString(result));        
    }
    
    /**
     * Returns the children as ElementOpcat.
     * @return 
     */
    public List getChildren()
    {
        Iterator it = element.getChildren().iterator();
        List opcatElemetList = new ArrayList();
        
        while(it.hasNext())
        {
             opcatElemetList.add(new ElementOpcat((Element)it.next()));
        }
        
        return (opcatElemetList);
    }
    
    /**
     * get a specific child as elementOpcat
     * @param childName
     * @return 
     */
    public List getChildren(String childName)
    {
        Iterator it = element.getChildren(childName).iterator();
        List opcatElemetList = new ArrayList();
        
        while(it.hasNext())
        {
             opcatElemetList.add(new ElementOpcat((Element)it.next()));
        }
        
        return (opcatElemetList);
    }
    
    /**
     * returns the element name.
     * @return 
     */
    public String getName()
    {
        String result = element.getName();
        return (clearString(result));
    }
    
    /**
     * returns the element of a specific child.
     * @param childName
     * @return 
     */
    public Element getChild(String childName)
    {
        return (element.getChild(childName));
    }
    
    /**
     * get the value of a specific element,
     * @param childName
     * @return 
     */
    public String getChildText(String childName)
    {
        String result = element.getChildText(childName);
        return (clearString(result));
    }
    
    /**
     * returns the attribute from an element by its name.
     * @param attName
     * @return 
     */
    public Attribute getAttribute(String attName)
    {
        
        return(element.getAttribute(attName));
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        //ElementOpcat elementOpcat = new ElementOpcat();
    }

    public Element getElement()
    {
        return element;
    }

    public void setElement(Element element)
    {
        this.element = element;
    }
}