package extensionTools.sqlGeneration.db;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import extensionTools.sqlGeneration.util.Constants;

public class Table implements Constants
{
    private String name;
    ArrayList columns = new ArrayList();
    ArrayList rows = new ArrayList();

    public Table(String tableName)
    {
        this.name = tableName;
    }

    /**
     * This function adds column to the table.
     * If column alraedy exists in the table so it will not be added,
     * message will be written to the logger and false will be returned.
     * 
     * @param newColumn - new column object.
     * @return true - if the column was successfully added to the table,
     *         false - otherwise.
     */
    public boolean addColumn (Column newColumn)
    {
        if (isColumnExists(newColumn.getName()) == true)
        {
            logger.out("Error: Column" + newColumn.getName() + 
                       "already exists in the table " +
                       getName());
            return (false);
        }
        columns.add(newColumn);
        return (true);
    }
    
    /**
     * Returns name of PK of this table.
     * The assumption is that this table has only one PK.
     * If the table has two PKs or more so the first found PK
     * will be returned.
     * 
     * @return 
     */
    public String getPKName ()
    {
       Column currObject = null;
       Iterator it = columns.iterator();
       while (it.hasNext())
       {
            currObject = (Column)it.next();
            if (currObject.getIsPrimaryKey() == true)
            {
                return (currObject.getName());
            }
       } 
       
       logger.out ("Error: table " + getName() + " has not PK at all");
       
       return (null);
    }
    
    /**
     * This function drops column with the given name 
     * @param columnName - name of column to drop
     * @return true - if the column was dropped from the table,
     *         false - if the calumn was not found in the table
     */
    public boolean dropColumn (String columnName)
    {
        Iterator it = columns.iterator();
        Column currColumn = null;
        while (it.hasNext())
        {
            currColumn = (Column)it.next();
            if (currColumn.getName().equalsIgnoreCase(columnName) == true)
            {
                it.remove();
                return (true);
            }
        }
        
        logger.out("Error: Trying to drop " + columnName + " column " +
                    " but it does not exist in the " + getName() + " table");
        return (false);
    }
    
    /**
     * This function finds the column with the given name and returns it
     * @param columnName - find coumn with this name
     * @return Column object if the column exists and null if the column does not exist
     */
    public Column getColumnByName (String columnName)
    {
        Iterator it = columns.iterator();
        Column currColumn = null;
        while (it.hasNext())
        {
            currColumn = (Column)it.next();
            if (currColumn.getName().equalsIgnoreCase(columnName) == true)
            {
                return (currColumn);
            }
        }
        return (null);
    }
    
    /**
     * If this talbe has FK column so ask this column to check if it needs
     * to add to it inherited reference
     * 
     * @param inheritedSonName
     * @param fkColumnName
     * @param currDatabase - current Database
     */
    public void addInheritedReference (String inheritedSonName, String fkColumnName,
                                        Database currDatabase)
    {
        Column currColumn = null;
        Iterator it = columns.iterator();
        while (it.hasNext())
        {
            currColumn = (Column)it.next();
            if (currColumn.getIsForeignKey() == true)
            {
                currColumn.addInheritedReference(inheritedSonName, fkColumnName, 
                                                                    currDatabase);
            }
            
        }
    }
    
    /**
     * Get all columns of this table
     * @return 
     */
    public ArrayList getColumns ()
    {
        return (columns);
    }
    
    /**
     * Check if the given column exists in the table
     * @param columnName - name of the column to check
     * @return true - if the given column is existing and false otherwise
     */
    public boolean isColumnExists (String columnName)
    {
        Iterator it = columns.iterator();
        Column currColumn = null;
        while (it.hasNext())
        {
            currColumn = (Column)it.next();
            if (currColumn.getName().equalsIgnoreCase(columnName) == true)
            {
                return (true);
            }
        }
        return (false);
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * 
     * @param arrValues - array of Strings (must be strings)
     *                    all types in the DB are VARCHARS
     */
    public void insertRow (String strValue)
    {
        rows.add(strValue);
    }
    
    /**
     * This function writes CREATE TABLE script for one table.
     * Each column of the table writes its script.
     * If we have columns that are foreign keys, so we will save these constraints
     * and write them after all columns.
     * 
     * Example of script:
     * 
     * CREATE TABLE MLSB (
     *      ID INTEGER PRIMARY KEY,
     *      FIRST_NAME VARCHAR(10) NOT NULL,
     *      LAST_NAME VARCHAR(20) NOT NULL UNIQUE,
     *      HEIGHT FLOAT,
     *      HAIR_CODE INTEGER,
     *      CONSTRAINT HAIR_CODE_FK FOREIGN KEY (HAIR_CODE) REFERENCES HAIR
     * );
     * 
     * @param out - output stream
     */
    public void writeScript (PrintWriter out, Database dbDatabase)
    {
        int countPKs = 0;
        Column currColumn = null;
        Constraint multiplePK = null;
        
        // Write comment to script
        out.println("-- CREATING TABLE: " + getName());
        out.println();
        
        // Open statement
        out.println("CREATE TABLE " + getName() + " (");
        
        // Count PKs in this table
        Iterator itCounter = columns.iterator();
        while (itCounter.hasNext())
        {
            currColumn = (Column)itCounter.next();
            if (currColumn.getIsPrimaryKey() == true)
            {
                countPKs++;
            }
        }
        
        Iterator it = columns.iterator();
        while (it.hasNext())
        {
            // Get the column and write it's script
            currColumn = (Column)it.next();
            
            // If we have multiple primary key so it will not be written
            // after each PK column, but as constraint after all columns 
            if (countPKs == 1)
            {
                currColumn.writeScript(out, true);
            }
            else
            {
                currColumn.writeScript(out, false);
            }
            
            // In this case we have multiple PK, so we will create constraint
            // for it.
            if (currColumn.getIsPrimaryKey() == true && countPKs > 1)
            {
                if (multiplePK != null)
                {
                    multiplePK.addPKColumn(currColumn.getName());
                    multiplePK.setName(multiplePK.getName() + "_" +
                                        currColumn.getName());
                }
                else
                {
                    multiplePK = new Constraint(getName(), "PK_" + currColumn.getName(),
                                            Constraint.TYPE_MULTIPLE_PK, null, null);
                    multiplePK.addPKColumn(currColumn.getName());
                }
            }
            
            // Constraints are specified after columns definition, so we
            // will save them and write after columns
            if (currColumn.getIsForeignKey() == true)
            {
                // FK consraint will be specified after creation of all tables
                // so we save them to the database
                Constraint currConstraint = null;
                String currRef = null;
                Iterator itRef = currColumn.getReferenceTables().iterator();
                // One column can be referenced to more than one table.
                while (itRef.hasNext())
                {
                    currRef = (String)itRef.next();
                    currConstraint = new Constraint(getName(), "FK_" + getName() + "_" + currColumn.getName() +
                                 "_REFERS_" + currRef,
                                 Column.CONSTRAINT_FOREIGN_KEY, currColumn.getName(), 
                                 currRef);
                    dbDatabase.addConstraint(currConstraint);
                    //arrConstraints.add(currConstraint);
                }
                
                
                
            }
            
            // If this column is not last column, so comma must be after it
            if (it.hasNext() == true || multiplePK != null)
            {
                out.print(",");
            }
            out.println();
        }
        
        // If we had multiple primary key, we will write it here (after all columns,
        // but inside CREATE TABLE statement.
        String output = null;
        if (multiplePK != null)
        {
            // Example: CONSTRAINT PK_COLA_COLB PRIMARY KEY (COLA, COLB)
            output = "CONSTRAINT " + multiplePK.getName() + " " + 
                    multiplePK.getType() + " (";
                    
            ArrayList arrPKColumns = multiplePK.getPKColumns();
            Iterator itPK = arrPKColumns.iterator();
            String currPKColumn;
            while (itPK.hasNext())
            {
                currPKColumn = (String)itPK.next();
                output = output + currPKColumn;
                
                if (itPK.hasNext())
                {
                    output = output + ",";
                }
            }
            
            output = output + ")";
            
            out.print(output);
            out.println();
        }
        
        // Add rows to the Database
        // All rows will be added to the script after all DDL definitions
        int rowIndex = 1;
        String currStrRow;
        Row newRow = null;
        if (rows.size() > 0)
        {
            Iterator itRows = rows.iterator();
            while (itRows.hasNext())
            {
                currStrRow = (String)itRows.next();
                newRow = new Row(rowIndex, getName(), currStrRow);
                rowIndex++;
                dbDatabase.addRow(newRow);
            }
        }

        /* OLD
        while (it.hasNext())
        {
            currConstraint = (Constraint)it.next();
            // Example: CONSTRAINT HAIR_CODE_FK FOREIGN KEY (HAIR_CODE) REFERENCES HAIR
            out.print("CONSTRAINT " + currConstraint.getName() + " " +
                        Constraint.TYPE_FOREIGN_KEY + " (" + currConstraint.getColumnName() + 
                        ") REFERENCES " + currConstraint.getReferenceTable());
                        
            // If this constraint is not last, so comma must be after it
            if (it.hasNext() == true)
            {
                out.println(",");
            }
            
            out.println();
        }
        */
       
        
        // Close stattement
        out.println(");");
        out.println();
    }
}