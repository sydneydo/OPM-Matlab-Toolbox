package extensionTools.sqlGeneration.xmlParsing;

/**
 *                                  Class - TSSObject
 *                                  
 * This class is a representation of the "Thing Sentence Set" tag in the XML.
 * it hold all its main attributes for later use.
 */

public class TSSObject 
{
    private String cardinality = null;
    private String realFatherName = null;
    private String name = null;
    private String transId = null;
    private String fakeFatherName = null;
    
    public TSSObject()
    {
    }
    
    public TSSObject(String cardinality, String realFatherName,
                        String name,String transId, String fakeFatherName)
    {
        this.cardinality = cardinality;
        this.realFatherName = realFatherName;
        this.name = name;
        this.transId = transId;
        this.fakeFatherName = fakeFatherName;
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        
    }

    public String getCardinality()
    {
        return cardinality;
    }

    public void setCardinality(String cardinality)
    {
        this.cardinality = cardinality;
    }

    public String getFakeFatherName()
    {
        return fakeFatherName;
    }

    public void setFakeFatherName(String fakeFatherName)
    {
        this.fakeFatherName = fakeFatherName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRealFatherName()
    {
        return realFatherName;
    }

    public void setRealFatherName(String realFatherName)
    {
        this.realFatherName = realFatherName;
    }

    public String getTransId()
    {
        return transId;
    }

    public void setTransId(String transId)
    {
        this.transId = transId;
    }
}