package extensionTools.sqlGeneration.db;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import extensionTools.sqlGeneration.util.Constants;

public class Column implements Constants
{
    // Constants
    public static final String CONSTRAINT_PRIMARY_KEY = "PRIMARY KEY";
    public static final String CONSTRAINT_NOT_NULL = "NOT NULL";
    public static final String CONSTRAINT_UNIQUE = "UNIQUE";
    public static final String CONSTRAINT_FOREIGN_KEY = "FOREIGN KEY";
    
    // DATA TYPES
    public static final String DATA_TYPE_CHAR = "CHAR";
    public static final String DATA_TYPE_CHARACTER  = "CHARACTER";
    public static final String DATA_TYPE_VARCHAR = "VARCHAR";
    public static final String DATA_TYPE_NUMERIC = "NUMERIC";
    public static final String DATA_TYPE_DECIMAL = "DECIMAL";
    public static final String DATA_TYPE_DEC = "DEC";
    public static final String DATA_TYPE_INTEGER = "INTEGER";
    public static final String DATA_TYPE_SMALLINT = "SMALLINT";
    public static final String DATA_TYPE_FLOAT = "FLOAT";
    public static final String DATA_TYPE_REAL = "REAL";
    public static final String DATA_TYPE_DATE = "DATE";
    public static final String DATA_TYPE_TIME = "TIME";
    public static final String DATA_TYPE_TIMESTAMP = "TIMESTAMP";
    
    private String name;
    private boolean isPrimaryKey;
    private boolean isNotNull;
    private boolean isUnique;
    private boolean isForeignKey;
    //private String referenceTable;
    private ArrayList arrReferenceTables = new ArrayList();
    private String dataType;
    private int length;

    public Column()
    {
    }
     
    /**
     * Create column without length of data type
     * @param name - name of the column
     * @param dataType - name of data type
     * @param isPrimaryKey - if is primary key
     * @param isNotNull - if has not null constraint
     * @param isUnique - if has unique constraint
     * @param isForeignKey - true if this coumn is foreign key
     * @param referenceTable - table to which this column references
     * @return 
     */
    public static Column createColumn (String name, String dataType, 
                   boolean isPrimaryKey, boolean isNotNull,
                   boolean isUnique, boolean isForeignKey, String referenceTable,
                   Database currDatabase)
    {
        return (Column.createColumn(name, dataType, isPrimaryKey, isNotNull, 
                isUnique, isForeignKey, referenceTable, currDatabase, 0));
    }
    
    /**
     * Create column that is primary key and not FK.
     * @param name - name of the column
     * @param dataType - name of data type
     * @return 
     */
    public static Column createColumn (String name, String dataType,int Length, boolean isPrimaryKey)
    {
        return (Column.createColumn(name, dataType, true, false, false, false, null, null, Length));
    }
    
    /**
     * Create column copy (as given column)
     * @param other
     * @param currDatabase
     * @return 
     */
    public static Column createColumn (Column other, Database currDatabase)
    {
        return (Column.copyColumn(other.name, other.dataType, other.isPrimaryKey,
                other.isNotNull, other.isUnique, other.isForeignKey, other.arrReferenceTables,
                currDatabase, other.length));    
    }
    
    /**
     * Create custom column 
     * @param name - name of the column
     * @param dataType - name of data type
     * @param length - maximum length of data type
     * @param isPrimaryKey - if is primary key
     * @param isNotNull - if has not null constraint
     * @param isUnique - if has unique constraint
     * @param isForeignKey - true if this coumn is foreign key
     * @param referenceTable - table to which this column references
     */
    public static Column copyColumn (String name, String dataType, 
                   boolean isPrimaryKey, boolean isNotNull,
                   boolean isUnique, boolean isForeignKey, ArrayList theReferenceTables,
                   Database currDatabase, int length)
    {
        Column newColumn = new Column();
        
        newColumn.setName(name);
        newColumn.setDataType(dataType);
        newColumn.setIsPrimaryKey(isPrimaryKey);
        newColumn.setIsNotNull(isNotNull);
        newColumn.setIsUnique(isUnique);
        newColumn.setIsForeignKey(isForeignKey);
        newColumn.setLength(length);
        //newColumn.arrReferenceTables.add(referenceTable);
        //newColumn.setReferenceTable(referenceTable);
        
       String currRef = null;
       String newRef = null;
       Iterator it = theReferenceTables.iterator();
       while (it.hasNext())
       {
            currRef = (String)it.next();
            newRef = new String (currRef);
            newColumn.arrReferenceTables.add(newRef);
       }
        
        
        
        return (newColumn);
    }
    
    /**
     * Get all reference tables of this column
     * @return 
     */
    public ArrayList getReferenceTables ()
    {
        return (arrReferenceTables);
    }
    /**
     * Create default column that is not primary key
     * @param name - name of the column
     * @param dataType - name of data type
     * @return 
     */
    public static Column createColumn (String name, String dataType)
    {
        return (Column.createColumn(name, dataType, false, false, false, false, null, null, 0));
    }
    
    /**
     * Create custom column 
     * @param name - name of the column
     * @param dataType - name of data type
     * @param length - maximum length of data type
     * @param isPrimaryKey - if is primary key
     * @param isNotNull - if has not null constraint
     * @param isUnique - if has unique constraint
     * @param isForeignKey - true if this coumn is foreign key
     * @param referenceTable - table to which this column references
     */
    public static Column createColumn (String name, String dataType, 
                   boolean isPrimaryKey, boolean isNotNull,
                   boolean isUnique, boolean isForeignKey, String referenceTable,
                   Database currDatabase, int length)
    {
        Column newColumn = new Column();
        
        newColumn.setName(name);
        newColumn.setDataType(dataType);
        newColumn.setIsPrimaryKey(isPrimaryKey);
        newColumn.setIsNotNull(isNotNull);
        newColumn.setIsUnique(isUnique);
        newColumn.setIsForeignKey(isForeignKey);
        newColumn.setLength(length);
        //newColumn.setReferenceTable(referenceTable);
        
        if (dataType.equalsIgnoreCase(DATA_TYPE_VARCHAR) == true && length < 1)
        {
            logger.out("Error: Column " + newColumn.getName() + 
                       " has VARCHAR data type and " + length + 
                       " length. Length must be positive number.");
            return (null);
        }
        
        if (isForeignKey == true)
        {
            if (currDatabase.isTableExists(referenceTable) == false)
            {
                logger.out("Error: Column " + newColumn.getName() + 
                   " references to non-existing table " + referenceTable);
                return (null);
            }
            else
            {
                newColumn.getReferenceTables().add(referenceTable);
            }
        }
        
        return (newColumn);
    }
    
    /**
     * If this column has reference table <<fkColumnName>> so add to this column
     * FK that refers to <<inheritedSonName>> table
     * 
     * If this column doesnt refer such table nothing will happen
     * 
     * @param inheritedSonName
     * @param fkColumnName
     */
    public void addInheritedReference (String inheritedSonName, String fkColumnName, 
                                        Database currDatabase)
    {
        String currRefTable = null;
        Iterator it = arrReferenceTables.iterator();
        boolean isFound = false;
        while (it.hasNext() && isFound == false)
        {
            currRefTable = (String)it.next();
            // If this column has not such FK yet
            if (currRefTable.equals(fkColumnName) == true)
            {
                isFound = true;
            }
        }
        
        if (isFound == true)
        {
            addFK(inheritedSonName, currDatabase);
        }
    }
    
    /**
     * This function writes single row to the CREATE TABLE statement.
     * The written row desfcribes one column.
     * 
     * Example of the script:
     * 
     * LAST_NAME VARCHAR(20) NOT NULL UNIQUE,
     * 
     * @param out - output stream
     */
    public void writeScript (PrintWriter out, boolean needWritePK)
    {
        String columnScript = getName() + " " + getDataType();
        
        // Add maximum length of datatype
        if (length > 0)
        {
            columnScript = columnScript + " (" + getLength() + ")";
        }
        
        // Add RPIMARY KEY constraint
        if (getIsPrimaryKey() == true && needWritePK == true)
        {
            columnScript = columnScript + " " + Column.CONSTRAINT_PRIMARY_KEY;    
        }
        
        // Add NOT NULL constraint
        if (getIsNotNull() == true)
        {
            columnScript = columnScript + " " + Column.CONSTRAINT_NOT_NULL;
        }
        
        // Add UNIQUE constraint
        if (getIsUnique() == true)
        {
            columnScript = columnScript + " " + Column.CONSTRAINT_UNIQUE;
        }
        
        out.print(columnScript);
    }
    
    /**
     * Add forgien key for this column.
     * @param referenceTable - Table to reference.
     * @param currDatabase - the db the table is in.
     * @return true if succeded, false if failed.
     */
    public boolean addFK (String referenceTable, Database currDatabase)
    {
        if (currDatabase.isTableExists(referenceTable) == false)
        {
            logger.out("Error: Column " + getName() + 
                       " references to non-existing table " + referenceTable);
            return (false);
        }
        else
        {
            setIsForeignKey(true);
            arrReferenceTables.add(referenceTable);
            //setReferenceTable(referenceTable);
            return (true);
        }
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean getIsPrimaryKey()
    {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean isPrimaryKey)
    {
        this.isPrimaryKey = isPrimaryKey;
    }

    public boolean getIsNotNull()
    {
        return isNotNull;
    }

    public void setIsNotNull(boolean isNotNull)
    {
        this.isNotNull = isNotNull;
    }

    public boolean getIsUnique()
    {
        return isUnique;
    }

    public void setIsUnique(boolean isUnique)
    {
        this.isUnique = isUnique;
    }

    public boolean getIsForeignKey()
    {
        return isForeignKey;
    }

    public void setIsForeignKey(boolean isForeignKey)
    {
        this.isForeignKey = isForeignKey;
    }

    /* can have mane ref tables
    public String getReferenceTable()
    {
        return referenceTable;
    }

    public void setReferenceTable(String referenceTable)
    {
        this.referenceTable = referenceTable;
    }
*/

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }
}