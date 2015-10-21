package extensionTools.sqlGeneration.gui;
import extensionTools.sqlGeneration.db.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import extensionTools.sqlGeneration.util.*;

import extensionTools.sqlGeneration.xmlParsing.*;
import gui.Opcat2;

public class startForm extends JDialog implements Constants
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // COMPONENTS
    private JPanel pnlMainPanel = null;
    private JPanel pnlLabelsPanel = null;
    private JPanel pnlButtonsPanel = null;
    private JPanel pnlTextPanel = null;
    private JPanel pnlCommandsPanel = null;
    
    private JLabel lblScriptFile = null;
    private JLabel lblAccessFile = null;
    private JLabel lblXMLFile = null;
    
    private JTextField txtScriptFile = null;
    private JTextField txtAccessFile = null;
    private JTextField txtXMLFile = null;
    
    private JButton cmdBrowseXMLFile = null;
    private JButton cmdBrowseScriptFile = null;
    private JButton cmdBrowseAccessFile = null;
    private JButton cmdRunScript = null;
    private JButton cmdCreateScript = null;
    private JButton cmdOpenLog = null;
    private JButton cmdOpenScript = null;
    
    File fSctiptFile = OUTPUT_SCRIPT_FILE;
    File fAccessFile = new File (DEFAULT_DB_FILENAME);
    File fXMLFile = new File(DEFAULT_XML_FILENAME);
    
    public startForm(String title)
    {
        super(Opcat2.getFrame(), title);
    }
    
    private JPanel createComponents ()
    {
        final JDialog frmFrame = this;
        pnlMainPanel = new JPanel(null);
        final JDialog frame = this;
        final int H_PANEL = 80;
        
        // LABELS
        pnlLabelsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlLabelsPanel.setBounds(20, 20, 130, H_PANEL);
        
        lblAccessFile = new JLabel("DB *.mdb filename");
        pnlLabelsPanel.add(lblAccessFile);
        
        lblScriptFile = new JLabel("Script filename");
        pnlLabelsPanel.add(lblScriptFile);
        
        lblXMLFile = new JLabel("XML filename");
        pnlLabelsPanel.add(lblXMLFile);
        
        pnlMainPanel.add(pnlLabelsPanel);
        
        // TEXT
        pnlTextPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlTextPanel.setBounds(160, 20, 300, H_PANEL);
        
        txtAccessFile = new JTextField(DEFAULT_DB_FILENAME);
        txtAccessFile.setEditable(false);
        pnlTextPanel.add(txtAccessFile);
        
        txtScriptFile = new JTextField(OUTPUT_FILENAME);
        txtScriptFile.setEditable(false);
        pnlTextPanel.add(txtScriptFile);
        
        txtXMLFile = new JTextField(DEFAULT_XML_FILENAME);
        txtXMLFile.setEditable(false);
        pnlTextPanel.add(txtXMLFile);
        
        pnlMainPanel.add(pnlTextPanel);
        
        // BUTTONS
        pnlButtonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlButtonsPanel.setBounds(480, 20, 100, H_PANEL);
        
        cmdBrowseAccessFile = new JButton("Browse...");
        pnlButtonsPanel.add(cmdBrowseAccessFile);
        cmdBrowseAccessFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fc = new JFileChooser();
                ExtensionsFileFilter extFilter = new ExtensionsFileFilter();
                extFilter.addExtension("MDB");
                fc.setFileFilter(extFilter);
                int returnVal = fc.showDialog(frame, "Open");
                
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    fAccessFile = fc.getSelectedFile();
                    txtAccessFile.setText(fAccessFile.getAbsolutePath());
                }
            }
        });
        
        cmdBrowseScriptFile = new JButton("Browse...");
        pnlButtonsPanel.add(cmdBrowseScriptFile);
        cmdBrowseScriptFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fc = new JFileChooser();
                ExtensionsFileFilter extFilter = new ExtensionsFileFilter();
                extFilter.addExtension("TXT");
                extFilter.addExtension("SQL");
                fc.setFileFilter(extFilter);
                int returnVal = fc.showDialog(frame, "Open");
                
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    fSctiptFile = fc.getSelectedFile();
                    txtScriptFile.setText(fSctiptFile.getAbsolutePath());
                }
            }
        });
        
        cmdBrowseXMLFile = new JButton("Browse...");
        pnlButtonsPanel.add(cmdBrowseXMLFile);
        cmdBrowseXMLFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fc = new JFileChooser();
                ExtensionsFileFilter extFilter = new ExtensionsFileFilter();
                extFilter.addExtension("XML");
                fc.setFileFilter(extFilter);
                int returnVal = fc.showDialog(frame, "Open");
                
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    fXMLFile = fc.getSelectedFile();
                    txtXMLFile.setText(fXMLFile.getAbsolutePath());
                }
            }
        });
        
        pnlMainPanel.add(pnlButtonsPanel);
        
        // COMMAND BUTTONS
        
        pnlCommandsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlCommandsPanel.setBounds(10, 130, 580, H_PANEL * 2);
        
        cmdRunScript = new JButton("Run Script on MDB");
        pnlCommandsPanel.add(cmdRunScript);
        cmdRunScript.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                TimeTest tt = new TimeTest();
                tt.start();
                
                ScriptRunner runner = new ScriptRunner();
                runner.runScript(fSctiptFile, fAccessFile);
                
                tt.finish("Running script on Access database");
                
                JOptionPane.showMessageDialog(frmFrame, 
                    "Script was executed on MDB.\nOpen log file to check it",
                    "Script Runner", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        
        cmdCreateScript = new JButton("Create Script from XML file");
        pnlCommandsPanel.add(cmdCreateScript);
        cmdCreateScript.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                TimeTest tt = new TimeTest();
                tt.start();
                
                XMLParser parser = new XMLParser();
                parser.parse(fXMLFile);
                
                ScriptCreator creator = new ScriptCreator();
                creator.createScript(fSctiptFile, parser.getDatabase());
                
                tt.finish("Parsing XML file and creation script");
                
                JOptionPane.showMessageDialog(frmFrame, 
                    "Script was created.\nOpen log file to check it",
                    "Script Creation", JOptionPane.INFORMATION_MESSAGE);
                //creator.test();
            }
        });
        
        cmdOpenLog = new JButton("Open log file");
        pnlCommandsPanel.add(cmdOpenLog);
        cmdOpenLog.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                if (logger.openMe() == false)
                {
                    JOptionPane.showMessageDialog(frmFrame, 
                    "LOG File does not exist",
                    "LOG was not found", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cmdOpenScript = new JButton("Open script file");
        pnlCommandsPanel.add(cmdOpenScript);
        cmdOpenScript.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                if (openFile("NOTEPAD", fSctiptFile) == false)
                {
                    JOptionPane.showMessageDialog(frmFrame, 
                    "Script file not found",
                    "Script file", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
        pnlMainPanel.add(pnlCommandsPanel);
        
        return (pnlMainPanel);
    }
    
    /**
     * Open file with given program
     * @param programName
     * @param file
     * @return 
     */
    public boolean openFile (String programName, File file)
    {
        if (file.exists() == true)
        {
            try
            {
                Runtime rt = Runtime.getRuntime();
                rt.exec(programName + " " + file.getAbsolutePath());
                return (true);
            }
            catch (IOException e)
            {
                logger.out(e);
            }
        }
        return (false);
    }
    
    public void start ()
    {
        setSize(600, 350);
        setLocation(200, 200);
        setResizable(false);
        setContentPane(createComponents());
        setVisible(true);
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        startForm startForm = new startForm("SQL Script Genetator 1.0");
        startForm.start();
    }
    
    /**
     * This class filters file by its extensions.
     * If extension is in the list of extensions so the file will be filtered.
     */
    class ExtensionsFileFilter extends FileFilter
    {
        ArrayList arrExtensions = new ArrayList();
        
        public void addExtension (String ext)
        {
            arrExtensions.add(ext);    
        }
        
        public boolean accept (File file)
        {
            if (file.isDirectory() == true)
            {
                return (true);
            }
            
            String currExt = null;
            Iterator it = arrExtensions.iterator();
            while (it.hasNext())
            {
                currExt = (String)it.next();
                if (file.getName().toUpperCase().endsWith(currExt.toUpperCase()))
                {
                    return (true);
                }
            }
            return (false);
        }
        
        public String getDescription ()
        {
            String currExt = null;
            String desc = "File types: ";
            Iterator it = arrExtensions.iterator();
            while (it.hasNext())
            {
                currExt = (String)it.next();
                desc = desc + " " + currExt;
                if (it.hasNext())
                {
                    desc = desc + ",";
                }
            }
            return (desc);
        }
    }
}