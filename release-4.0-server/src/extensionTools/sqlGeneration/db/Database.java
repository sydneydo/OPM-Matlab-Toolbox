package extensionTools.sqlGeneration.db;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import extensionTools.sqlGeneration.util.Constants;

public class Database implements Constants
{
    private String name;
    ArrayList tables = new ArrayList();
    ArrayList constraints = new ArrayList();
    ArrayList rows = new ArrayList();

    public Database(String databaseName)
    {
        name = databaseName;
    }

    /**
     * Add row to database.
     * Function has package access besause only table can add rows to database
     */
    void addRow (Row newRow)
    {
        rows.add(newRow);
    }
    
    public boolean addTable (Table newTable)
    {
        if (isTableExists(newTable.getName()) == true)
        {
            logger.out("Error: Table" + newTable.getName() + 
                       "already exists in the database " +
                       getName());
            return (false);
        }
        tables.add(newTable);
        return (true);
    }
    
    /**
     * Check if the given table exists in the database
     * @param tableName - name of the table to check
     * @return true - if the given table is existing and false otherwise
     */
    public boolean isTableExists (String tableName)
    {
        Iterator it = tables.iterator();
        Table currTable = null;
        while (it.hasNext())
        {
            currTable = (Table)it.next();
            if (currTable.getName().equalsIgnoreCase(tableName) == true)
            {
                return (true);
            }
        }
        return (false);
    }
    
    /**
     * This function finds the table with the given name and returns it
     * @param tableName - find coumn with this name
     * @return Table object if the table exists and null if the table does not exist
     */
    public Table getTableByName (String tableName)
    {
        Iterator it = tables.iterator();
        Table currTable = null;
        while (it.hasNext())
        {
            currTable = (Table)it.next();
            if (currTable.getName().equalsIgnoreCase(tableName) == true)
            {
                return (currTable);
            }
        }
        return (null);
    }
    
    /**
     * This function writes all tables to the script file
     */
    public void writeScript (PrintWriter out)
    {
        out.println("-- CREATING DATABASE: " + getName());
        out.println();
        
        // WRITE ALL TABLES
        int countTables = 0;
        Iterator it = tables.iterator();
        Table currTable = null;
        while (it.hasNext())
        {
            currTable = (Table)it.next();
            currTable.writeScript(out, this);
            countTables++;
        }
        logger.out("Message: Number of tables in the script: " + countTables);
        logger.out("");
        
        // WRITE ALL CONSTRAINTS
        int countConstraints = 0;
        Iterator iter = constraints.iterator();
        Constraint currConstraint = null;
        while (iter.hasNext())
        {
            currConstraint = (Constraint)iter.next();
            currConstraint.writeScript(out);
            countConstraints++;
        }
        logger.out("Message: Number of constraints in the script: " + countConstraints);
        logger.out("");
        
        // WRITE ALL ROWS
        int countRows = 0;
        Iterator iterRows = rows.iterator();
        Row currRow = null;
        while (iterRows.hasNext())
        {
            currRow = (Row)iterRows.next();
            currRow.writeScript(out);
            countRows++;
        }
        logger.out("Message: Number of rows in the script: " + countRows);
        logger.out("");
        
        // WRITE COMMIT IN THE END OF THE SCRIPT
        out.println();
        out.println("-- SCRIPT FINISHED. DO COMMIT IN THE END !!!");
        out.println();
        out.println("COMMIT;");
        out.println();
        
        logger.out("Message: Commit was added");
        logger.out("");
        
    }
    
    /**
     * This function searches for given columnName that has foreign key
     * that refers to the father or grandfather or... of the given table.
     * 
     * The aim of this function is that all inherited sons will recieve
     * all featues of their fathers.
     * 
     * For example: we have FATHER table, SON table, SON is inherited son of
     * FATHER, and LEGS table, LEGS table has FK that refers to FATHER. 
     * But we want that the SON will have LEGS too. So we search for all
     * columns that are FK and have reference to FATHER. Then, for each
     * column that we found, we will add another FK that refers to SON table.
     * 
     * So the result will be that LEGS table has FK_FATHER_TO_FATHER that refers to
     * FATHER and FK_FATHER_TO_SON that refers to SON
     * 
     * We must not leave son without legs...
     * 
     * @param inheritedSonName - name of inheried son table. SON in the example
     * @param fkName - name of FK column - FATHER in the example.
     */
    public void addInheritedReferences (String inheritedSonName, String fkColumnName)
    {
        Table currTable = null;
        Iterator it = tables.iterator();
        while (it.hasNext())
        {
            currTable = (Table)it.next();
            currTable.addInheritedReference(inheritedSonName, fkColumnName, this);
        }
    }
    
    /**
     * This function returns name of PK of given table
     * if given table has more than one PK so the first found PK will be 
     * returned
     * 
     * @param tableName
     * @return 
     */
    public String getTablePKByName (String tableName)
    {
        Table tblTable = getTableByName(tableName);
        
        if (tblTable == null)
        {
            return (null);
        }
        
        return (tblTable.getPKName());
    }
    
    /**
     * This function drops table with the given name 
     * @param tableName - name of table to drop
     * @return true - if the table was dropped from the table,
     *         false - if the calumn was not found in the table
     */
    public boolean dropTable (String tableName)
    {
        Iterator it = tables.iterator();
        Table currTable = null;
        while (it.hasNext())
        {
            currTable = (Table)it.next();
            if (currTable.getName().equalsIgnoreCase(tableName) == true)
            {
                it.remove();
                return (true);
            }
        }
        
        logger.out("Error: Trying to drop " + tableName + " table " +
                    " but it does not exist in the " + getName() + " database");
        return (false);
    }
    
    /**
     * Add constrint to the database
     * @param constr
     */
    public void addConstraint (Constraint constr)
    {
        constraints.add(constr);
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}