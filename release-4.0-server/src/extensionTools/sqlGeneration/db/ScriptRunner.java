package extensionTools.sqlGeneration.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import extensionTools.sqlGeneration.util.Constants;

public class ScriptRunner implements Constants
{
    final static String COMMENT_PREFIX = "--";
    
    static Connection conn;
    private final String CONNECTION_STRING = 
            "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
  
    public ScriptRunner ()
    {
        
    }
    
    /**
     * This function connects to Microsoft Access Database
     */
    public void connect (File dbFile)
    {
        // connection to an ACCESS MDB
        try
        {
            
            conn = DriverManager.getConnection(CONNECTION_STRING + dbFile.getAbsolutePath());         
         }
        catch (Exception e)
        {
            logger.out(e);
        }
    } 
  
    /**
     * This function reads script file and executes all statements
     * in this file.
     * 
     * @param scriptFile - filecontaining script.
     */
    public void runScript (File scriptFile, File dbFile)
    {
        // Connect
        connect(dbFile);
        
        // Run Script
        String currLine = null;
        String strSQL = null;
        int countExecuted = 0;
        boolean isStatementStarted = false;
        try
        {
            BufferedReader in
                 = new BufferedReader(new FileReader(scriptFile));
            
            Statement stmt = conn.createStatement();
            
            currLine = in.readLine();
            while (currLine != null)
            {
                // In this case we need to execute statement
                if (currLine.length() == 0 || currLine.startsWith(COMMENT_PREFIX))
                {
                    if (isStatementStarted == true)
                    {
                        stmt.executeUpdate(strSQL);
                        logger.out("Message: Executed Statement: " + strSQL);
                        countExecuted++;
                        isStatementStarted = false;
                    }
                }
                else if (isStatementStarted == true)
                {
                    strSQL = strSQL + " " + currLine;
                }
                else if (isStatementStarted == false)
                {
                    strSQL = currLine;
                    isStatementStarted = true;
                }
                
                // Read next line
                currLine = in.readLine();
            }
            
            // Run the last statement
            if (isStatementStarted == true)
            {
                stmt.executeUpdate(strSQL);
                logger.out("Message: Executed Statement: " + strSQL);
                countExecuted++;
                isStatementStarted = false;
            }
            
            in.close();
            
            
            
            logger.out("Message: Number of Executed Statements: " + countExecuted);
            
            conn.commit();
            logger.out("Commit executed.");
            
        }
        catch (Exception e)
        {
            logger.out("Cause of problem is " + strSQL);
            logger.out(e);
            logger.out("Failed to run the script... but if it failed on " +
                        "COMMIT so its OK");
            
        }
    }
    
    /**
     * This function reads script file and executes all statements
     * in this file.
     * 
     * @param scriptFile - filecontaining script.
     */
    public void runScriptResumeNext (File scriptFile)
    {
        // Connect
        connect(new File(DEFAULT_DB_FILENAME));
        
        // Run Script
        String currLine = null;
        String strSQL = null;
        int countExecuted = 0;
        boolean isStatementStarted = false;
        try
        {
            BufferedReader in
                 = new BufferedReader(new FileReader(scriptFile));
            
            Statement stmt = conn.createStatement();
            
            currLine = in.readLine();
            while (currLine != null)
            {
                // In this case we need to execute statement
                if (currLine.length() == 0 || currLine.startsWith(COMMENT_PREFIX))
                {
                    if (isStatementStarted == true)
                    {
                        try
                        {
                            stmt.executeUpdate(strSQL);
                        }
                        catch (Exception e)
                        {
                            logger.out("Deleteing table but it does not exist");
                        }
                        logger.out("Message: Executed Statement: " + strSQL);
                        countExecuted++;
                        isStatementStarted = false;
                    }
                }
                else if (isStatementStarted == true)
                {
                    strSQL = strSQL + " " + currLine;
                }
                else if (isStatementStarted == false)
                {
                    strSQL = currLine;
                    isStatementStarted = true;
                }
                
                // Read next line
                currLine = in.readLine();
            }
            
            // Run the last statement
            if (isStatementStarted == true)
            {
                stmt.executeUpdate(strSQL);
                logger.out("Message: Executed Statement: " + strSQL);
                countExecuted++;
                isStatementStarted = false;
            }
            
            in.close();
            
            logger.out("Message: Number of Executed Statements: " + countExecuted);
        }
        catch (Exception e)
        {
            logger.out(e);
            logger.out("Failed to run the script... but if it failed on " +
                        "COMMIT so its OK");
            
        }
    }
  
    public static void main (String args[]) {
        ScriptRunner scriptRunner = new ScriptRunner();
        //scriptRunner.runScript(OUTPUT_SCRIPT_FILE);
        //File sc = new File("C:\\outDB01.txt");
        //scriptRunner.runScript(sc);
        
        // Delete all tabvles
        File sc = new File(WORKING_DIR + "dropTables.txt");
        scriptRunner.runScriptResumeNext(sc);
        
        // Testing Script
        //scriptRunner.runScript(OUTPUT_SCRIPT_FILE);
        
        //
        File fg = new File("C:\\outDB01.txt");
        scriptRunner.runScript(fg, new File(DEFAULT_DB_FILENAME));
        
  }
}
