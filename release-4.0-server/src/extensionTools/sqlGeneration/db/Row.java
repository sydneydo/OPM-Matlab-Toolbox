package extensionTools.sqlGeneration.db;
import java.io.PrintWriter;

public class Row 
{
    private int index = 0;
    private String tableName = null;
    private String strValue = null;
    
    public Row()
    {
    }
    
    public Row (int index, String tableName, String strValue)
    {
        this.index = index;
        this.tableName = tableName;
        this.strValue = strValue;
    }
    
    /**
     * Write one ROW the script
     * @param out
     */
    public void writeScript (PrintWriter out)
    {
        // Write comment to script
        out.println();
        out.println("-- ADD ROW TO <<" + getTableName() + ">> table");
        out.println();
        
        String strOutput = "INSERT INTO " + getTableName();
        out.print(strOutput);
        out.println();
        
        strOutput = "VALUES (" + getIndex() + ", '" + getStrValue() + "');"; 
        out.print(strOutput);
        out.println();
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public String getStrValue()
    {
        return strValue;
    }

    public void setStrValue(String strValue)
    {
        this.strValue = strValue;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    
}