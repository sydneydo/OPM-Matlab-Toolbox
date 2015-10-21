package extensionTools.sqlGeneration.xmlParsing;

/**
 *                      Class ObjectXML
 *                      ***************
 *                      
 * Class represents an ObjectName / AttributeName inside a Exb / Agg Set. * 
 */
public class ObjectXML 
{
    private String maxCardinality;
    private String attrName;
    
    public ObjectXML()
    {
        
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        
    }
    
    /**
     * Get the attribute name.
     * @return String.
     */
    public String getAttrName()
    {
        return attrName;
    }
    
    /**
     * set the attricute name.
     * @param attrName - The new attribute name.
     */
    public void setAttrName(String attrName)
    {
        this.attrName = attrName;
    }
    
    /**
     * Gets the max Cardinality value.
     * @return String.
     */
    public String getMaxCardinality()
    {
        return maxCardinality;
    }
    
    /**
     * sets the Max Cardinality value.
     * @param maxCardinality - the new cardinality.
     */
    public void setMaxCardinality(String maxCardinality)
    {
        this.maxCardinality = maxCardinality;
    }
}