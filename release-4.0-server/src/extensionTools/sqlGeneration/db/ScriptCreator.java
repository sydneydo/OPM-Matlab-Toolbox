package extensionTools.sqlGeneration.db;
import java.io.*;
import extensionTools.sqlGeneration.util.Constants;

public class ScriptCreator implements Constants
{
    public ScriptCreator()
    {
    }

    /**
     * This function opens output stream and ask database to write the script
     * to the stream.
     * 
     * @param db - database that knows how to write creation script of itself.
     */
    public void createScript (File scriptFile, Database db)
    {
        try
        {
            PrintWriter out
                 = new PrintWriter(new BufferedWriter(new FileWriter(scriptFile, false)));

            db.writeScript(out);
            
            out.close();
        }
        catch (IOException e)
        {
            logger.out(e);
        }
    }
    /*
     * String createTableHairColor = "CREATE TABLE HAIR" +
            "(HAIR_CODE INTEGER PRIMARY KEY, " +
            "HAIR_COLOR VARCHAR(15) UNIQUE)";
            
        String createTableMLSB = "CREATE TABLE MLSB " +
            "(ID INTEGER PRIMARY KEY , " +
            "FIRST_NAME VARCHAR(10) NOT NULL, " +
            "LAST_NAME VARCHAR(20) NOT NULL UNIQUE, " +
            "HEIGHT FLOAT, " +
            "HAIR_CODE INTEGER, " +
            "CONSTRAINT HAIR_CODE_FK FOREIGN KEY (HAIR_CODE) REFERENCES HAIR) ";
          //  "WEIGHT INTEGER"; // constraints must be after definition of columns

     */
     
    public void test ()
    {
        test (false);    
    }
    
    private void test (boolean isRunScript)
    {
        Database dbTest = new Database("Anna");
        Column colFKPK = null;
        Column colFK01 = null;
       
        
        // -------------------------------------------------------------------------
        // TABLE ANIMAL HAIR
        Table tblAnimalHair = new Table ("ANIMAL_HAIR");
        
        colFKPK = Column.createColumn("HAIR_CODE", "INTEGER", true, false, false, false, null, dbTest);
        tblAnimalHair.addColumn(colFKPK);
        colFK01 = Column.createColumn("HAIR_WIDTH", Column.DATA_TYPE_INTEGER);
        tblAnimalHair.addColumn(colFK01);
        
        dbTest.addTable(tblAnimalHair);
        
        // -------------------------------------------------------------------------
        // TABLE MY HAIR
        Table tblMyHair = new Table ("MY_HAIR");
        
        colFKPK = Column.createColumn("HAIR_CODE", "INTEGER", true, false, false, false, null, dbTest);
        tblMyHair.addColumn(colFKPK);
        colFK01 = Column.createColumn("HAIR_LENGTH", Column.DATA_TYPE_INTEGER);
        tblMyHair.addColumn(colFK01);
        
        dbTest.addTable(tblMyHair);
        
        // -------------------------------------------------------------------------
        // TABLE WITH ROWS
        Table color = new Table ("COLORS");
        
        Column colColorID = Column.createColumn("COL_ID", "INTEGER", true, false, false, false, null, dbTest);
        color.addColumn(colColorID);
        Column colColor = Column.createColumn("SIZE", Column.DATA_TYPE_VARCHAR, false, false, true, false, null, null, 15);
        color.addColumn(colColor);
        
        color.insertRow("WHITE");
        color.insertRow("GREEN");
        color.insertRow("BLACK");
        color.insertRow("RED");
        color.insertRow("PINK");
        
        dbTest.addTable(color);
        
        // -------------------------------------------------------------------------
        // Two PKs
        Table book = new Table ("BOOK");
        
        Column colBookName = Column.createColumn("BOOK_NAME", "INTEGER", true, false, false, false, null, dbTest);
        book.addColumn(colBookName);
        Column colBookPages = Column.createColumn("BOOK_PAGES", "INTEGER", true, false, false, false, null, dbTest);
        book.addColumn(colBookPages);
        Column colBookSize = Column.createColumn("SIZE", Column.DATA_TYPE_VARCHAR, false, false, true, false, null, null, 15);
        book.addColumn(colBookSize);
        
        dbTest.addTable(book);
        
        
        // -------------------------------------------------------------------------
        Table hair = new Table ("HAIR");
        
        Column colHairCode = Column.createColumn("HAIR_CODE", "INTEGER", true, false, false, false, null, dbTest);
        hair.addColumn(colHairCode);
        Column colHairColor = Column.createColumn("HAIR_COLOR", Column.DATA_TYPE_VARCHAR, false, false, true, false, null, null, 15);
        hair.addColumn(colHairColor);
        
        dbTest.addTable(hair);
        
        // -------------------------------------------------------------------------
        
        Table mlsb = new Table("MLSB");
        
        Column colID = Column.createColumn("ID", "INTEGER", true, false, false, false, null, dbTest);
        mlsb.addColumn(colID);
        Column colFirstName = Column.createColumn("FIRST_NAME", Column.DATA_TYPE_VARCHAR, false, true, false, false, null, null, 10);
        mlsb.addColumn(colFirstName);
        Column colLastName = Column.createColumn("LAST_NAME", Column.DATA_TYPE_VARCHAR, false, true, true, false, null, null, 20);
        mlsb.addColumn(colLastName);
        Column colHeigth = Column.createColumn("HIGHHT", Column.DATA_TYPE_FLOAT);
        mlsb.addColumn(colHeigth);
        colHairCode = Column.createColumn("HAIR_CODE", Column.DATA_TYPE_INTEGER, false, false, false, true, "HAIR", dbTest);
        mlsb.addColumn(colHairCode);
        
        dbTest.addTable(mlsb);
        
        
        
         // -------------------------------------------------------------------------
        // TABLE COLUMN THAT HAS MANY FKs
        Table manyFKs = new Table ("FK_FUN");
        
        colFKPK = Column.createColumn("COL_ID_OFFK", "INTEGER", true, false, false, false, null, dbTest);
        manyFKs.addColumn(colFKPK);
        colFK01 = Column.createColumn("HAIR_CODE", Column.DATA_TYPE_INTEGER, false, false, false, true, "HAIR", dbTest);
        manyFKs.addColumn(colFK01);
        colFK01.addFK("ANIMAL_HAIR", dbTest);
        colFK01.addFK("MY_HAIR", dbTest);
        
        dbTest.addTable(manyFKs);
        
        // -------------------------------------------------------------------------
        createScript(OUTPUT_SCRIPT_FILE, dbTest);
        
        if (isRunScript == true)
        {
            ScriptRunner runner = new ScriptRunner();
            runner.runScript(OUTPUT_SCRIPT_FILE, new File(DEFAULT_DB_FILENAME));
        }
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        ScriptCreator scriptCreator = new ScriptCreator();
        scriptCreator.test(true);
        
    }
}