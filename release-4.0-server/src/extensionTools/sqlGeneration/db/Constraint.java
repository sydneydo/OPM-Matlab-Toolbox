package extensionTools.sqlGeneration.db;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Constraint 
{
    // CONSTANTS
    static final String TYPE_FOREIGN_KEY = "FOREIGN KEY";
    static final String TYPE_MULTIPLE_PK = "PRIMARY KEY";
    
    private String tableName;
    private String name;
    private String type;
    private String columnName;
    String referenceTable;
    ArrayList arrPKColumns = new ArrayList();

    /*
    public Constraint(String name, String type, String columnName, String referenceTable)
    {
        this.name = name;
        this.type = type;
        this.columnName = columnName;
        this.referenceTable = referenceTable;
    }
    */
    public Constraint(String tableName, String name, String type, 
                                String columnName, String referenceTable)
    {
        this.tableName = tableName;
        this.name = name;
        this.type = type;
        this.columnName = columnName;
        this.referenceTable = referenceTable;
    }
    
    /**
     * Multiple PK constraint will save all its columns.
     * @param pkColumnName
     */
    public void addPKColumn (String pkColumnName)
    {
        arrPKColumns.add(pkColumnName);    
    }
    
    /**
     * Get array of PK columns
     * @return 
     */
    public ArrayList getPKColumns ()
    {
        return (arrPKColumns);
    }
    
    /**
     * Write one FOREIGN KEY constrint to the script
     * @param out
     */
    public void writeScript (PrintWriter out)
    {
        // Write comment to script
        out.println("-- CREATING CONSTRAINT: " + getName());
        out.println();
        
        String strOutput = "ALTER TABLE " + getTableName() + " ADD CONSTRAINT " +
                    getName() + " " + getType() + " (" + getColumnName() + 
                    ") REFERENCES " + getReferenceTable();
                    
        out.println(strOutput);
        out.println();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getReferenceTable()
    {
        return referenceTable;
    }

    public void setReferenceTable(String referenceTable)
    {
        this.referenceTable = referenceTable;
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