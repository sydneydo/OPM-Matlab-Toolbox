package extensionTools.Documents.UserInterface;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import exportedAPI.opcatAPI.ISystem;
import extensionTools.Documents.Doc.Document;
/**
 * <P>DocSel is a class that extends JDialog.
 * The class allows the user to select one of the three options:
 * Analysis Document, Design Document or User defined document
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */

public class DocSel extends JDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
	 * 
	 */
	 
JPanel DocSelP = new JPanel();
  JRadioButton Anal = new JRadioButton();
  JRadioButton Des = new JRadioButton();
  JRadioButton Cust = new JRadioButton();
  JButton ok = new JButton();
  JButton Cancel = new JButton();
  ButtonGroup group = new ButtonGroup();
  ISystem mySys;

/**
 * Construct the frame
 * @param sys of type ISystem
 */
  public DocSel(ISystem sys) {
    super(sys.getMainFrame(), true);
    this.setTitle("Generate a Document");
    this.mySys = sys;
    this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    //Esc and Enter Keys active
    KeyListener kListener = new KeyAdapter(){
        public void keyReleased(KeyEvent e){
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
              DocSel.this.Cancel.doClick();
              return;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER){
              DocSel.this.ok.doClick();
              return;
            }
        }
    };
    this.addKeyListener(kListener);
    //set the screen in the center
    int fX = sys.getMainFrame().getX();
    int fY = sys.getMainFrame().getY();
    int pWidth = sys.getMainFrame().getWidth();
    int pHeight = sys.getMainFrame().getHeight();
    this.setLocation(fX + Math.abs(pWidth/2-this.getWidth()/2), fY + Math.abs(pHeight/2-this.getHeight()/2));
  }
    /**
   * Component initialization
   */

  private void jbInit() {
    this.getContentPane().setLayout(null);
    this.setSize(new Dimension(285, 250));
    this.DocSelP.setBorder(BorderFactory.createEtchedBorder());
    this.DocSelP.setBounds(new Rectangle(8, 6, 253, 202));
    this.DocSelP.setLayout(null);
    this.Anal.setText("Analysis Document");
    this.Anal.setBounds(new Rectangle(38, 28, 191, 25));
    this.Des.setText("Design Document");
    this.Des.setBounds(new Rectangle(38, 59, 191, 25));
    this.Cust.setText("Customized Document");
    this.Cust.setBounds(new Rectangle(38, 90, 191, 25));
    this.ok.setBounds(new Rectangle(74, 159, 79, 27));
    this.ok.setText("OK");
    this.ok.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocSel.this.ok_actionPerformed(e);
      }
    });
    this.Cancel.setBounds(new Rectangle(160, 159, 79, 27));
    this.Cancel.setText("Cancel");
    this.Cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocSel.this.Cancel_actionPerformed(e);
      }
    });
    this.setResizable(false);
    this.getContentPane().add(this.DocSelP, null);
    this.DocSelP.add(this.Anal, null);
    this.DocSelP.add(this.Des, null);
    this.DocSelP.add(this.Cust, null);
    this.DocSelP.add(this.Cancel, null);
    this.DocSelP.add(this.ok, null);
    this.group.add(this.Anal);
    this.group.add(this.Des);
    this.group.add(this.Cust);
    this.Anal.setSelected(true);
  }
/**
 * In case of Analysis or Design Documents call for save screen and
 * creates the document using the Print function in "doc".<BR>
 * In case of the Customized Document - opens the DocGen screen.
 * @param e ActionEvent on OK button
 */
  void ok_actionPerformed(ActionEvent e) {
    //if personal -> open DocGen screen
    if (this.Cust.isSelected()) {
      DocGen new_doc = new DocGen(this.mySys);
      new_doc.setVisible(true);
    }// of cust
    //if Analysis
    if (this.Anal.isSelected()){
        Document doc = this.AnalDocCreate();//create analysis document
        String filename="";
        //saving the file
        DocSave doc_save = new DocSave(this);
        int returnVal = doc_save.SDFileChooser.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
          String name = doc_save.SDFileChooser.getSelectedFile().getName();
          filename = doc_save.SDFileChooser.getSelectedFile().getPath();
          StringTokenizer st = new StringTokenizer(filename, ".", false);
          filename = st.nextToken()+".htm";
          File myDir = doc_save.SDFileChooser.getCurrentDirectory();
          int flag = 0; //0 - no such file yet
          int i=0;
          //check for files with this name
          if (myDir.exists()) {
            File[] contents = myDir.listFiles();
            while (i < contents.length) {
              if (contents[i].getName().compareTo(name)==0) {
				flag = 1;
			}
              i++;
            }//of while
          }//if
          //if there are files with this name
          if (flag==1){
            int retval=JOptionPane.showConfirmDialog( this, "A document with this name already exists. Do you want to overwrite it?", "Overwrite Document", JOptionPane.YES_NO_OPTION);
            //if the user wants to overwrite the file
            if (retval== JOptionPane.YES_OPTION){
              try{
                doc.PrintDocument(this.mySys,filename);//save the file
                this.dispose();//close this screen
              }// of try
              catch(IOException er){
                System.out.println("There was a problem saving the document");
              }// of catch
            }//of if
          }//of if
          //if there are no files with this name
          else {
            try{
              doc.PrintDocument(this.mySys,filename);//save the file
              this.dispose();//close this screen
            }// of try
            catch(IOException er){
              System.out.println("There was a problem saving the document");
            }// of catch
          }//of else
        }//of if
    }//if anal
    //if Design Document
    if (this.Des.isSelected()){
        Document doc = this.DesDocCreate();//create document
        String filename="";
        //saving document
        DocSave doc_save = new DocSave(this);
        int returnVal = doc_save.SDFileChooser.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
          String name = doc_save.SDFileChooser.getSelectedFile().getName();
          filename = doc_save.SDFileChooser.getSelectedFile().getPath();
          StringTokenizer st = new StringTokenizer(filename, ".", false);
          filename = st.nextToken()+".htm";
          File myDir = doc_save.SDFileChooser.getCurrentDirectory();
          int flag = 0; //0 - no such file yet
          int i=0;
          //check for files with this name
          if (myDir.exists()) {
            File[] contents = myDir.listFiles();
            while (i < contents.length) {
              if (contents[i].getName().compareTo(name)==0) {
				flag = 1;
			}
              i++;
            }//of while
          }//if
          //if there are files with this name
          if (flag==1){
            int retval=JOptionPane.showConfirmDialog( this, "A document with this name already exists. Do you want to overwrite it?", "Overwrite Document", JOptionPane.YES_NO_OPTION);
            //if overwrite
            if (retval== JOptionPane.YES_OPTION){
              try{
                doc.PrintDocument(this.mySys,filename);//save the document
                this.dispose();//close this screen
              }// of try
              catch(IOException er){
                System.out.println("There was a problem saving the document");
              }// of catch
            }//of if
          }//of if
          //if there are no files with this name
          else {
            try{
              doc.PrintDocument(this.mySys,filename);//save the file
              this.dispose();//close this screen
            }// of try
            catch(IOException er){
              System.out.println("There was a problem saving the document");
            }// of catch
          }//of else
        }//of if
    }//if Des
    this.dispose();//close this window
  }
/**
 * Closing the screen in case cancel was pressed.
 * @param e ActionEvent on Cancel button
 */
  void Cancel_actionPerformed(ActionEvent e) {
    this.dispose();
  }

/**
 * Creates a new document of type Document,
 * with the fields relevant to Analysis document selected.
 * @return doc of type Document
 */
  Document AnalDocCreate (){

    Document doc = new Document();
    boolean[] obj_array =new boolean[11];//array for object fields
    boolean[] proc_array =new boolean[6];//array for process fields
    boolean[] rel_array =new boolean[6];//array for relation fields
    boolean[] link_array =new boolean[12];//array for link fields
    boolean[] opd_array  =new boolean[7];//array for opd and opl fields
    boolean[] gi_array = new boolean[12];//array for gen info fields


    obj_array[0] = false; //ObjType
    obj_array[1] = true;//ObjDesc
    obj_array[2] = false;//ObjInitVal
    obj_array[3] = true;//ObjEssence
    obj_array[4] = false;//ObjIndex
    obj_array[5] = true;//ObjScope
    obj_array[6] = true;//ObjOrigin
    obj_array[7] = true;//ObjStates
    obj_array[8] = true;//ObjStateDescr
    obj_array[9] = false;//ObjStateInitial
    obj_array[10] = false;//ObjStateTime

    proc_array[0] = true;//ProcDescr
    proc_array[1] = true;//ProcEssence
    proc_array[2] = true;//ProcOrigin
    proc_array[3] = true;//ProcScope
    proc_array[4] = false;//ProcBody
    proc_array[5] = false;//ProcActTime

    rel_array[0] = true;//RelAggreg
    rel_array[1] = true;//RelClassif
    rel_array[2] = true;//RelFeature
    rel_array[3] = true;//RelGeneral
    rel_array[4] = true;//RelBi
    rel_array[5] = true;//RelUni

    link_array[0] = true;//LinkAgent
    link_array[1] = true;//LinkCondition
    link_array[2] = true;//LinkEffect
    link_array[3] = true;//LinkEvent
    link_array[4] = true;//LinkException
    link_array[5] = true;//LinkInstrEv
    link_array[6] = true;//LinkInvoc
    link_array[7] = true;//LinkRes
    link_array[8] = true;//LinkInstr
    link_array[9] = true;//LinkPropCond
    link_array[10] = true;//LinkPropPath
    link_array[11] = false;//LinkPropReacTime

    opd_array[0] = true;//OPDAll
    opd_array[1] = false;//OPDByName
    opd_array[2] = false;//OPDNone
    opd_array[3] = false;//OPLAll
    opd_array[4] = false;//OPLByName
    opd_array[5] = false;//OPLNone
    opd_array[6] = true;//OPLandOPD

    gi_array[0] = true;//GIClient
    gi_array[1] = true;//GIOverview
    gi_array[2] = true;//GICurrent
    gi_array[3] = true;//GIGoals
    gi_array[4] = true;//GIBusiness
    gi_array[5] = true;//GIFuture
    gi_array[6] = true;//GIHard
    gi_array[7] = true;//GIInputs
    gi_array[8] = true;//GIIssues
    gi_array[9] = true;//GIProblems
    gi_array[10] = true;//GIUsers
    gi_array[11] = true;//GIOper

    String OPDUntilT,OPLUntilT;
    OPDUntilT="-1";
    OPLUntilT="-1";

    doc.DocInfo.Data.Proc.ProcInit(proc_array);
    doc.DocInfo.Data.Obj.ObjInit(obj_array);
    doc.DocInfo.Data.Link.LinkInit(link_array);
    doc.DocInfo.Data.Rel.RelInit(rel_array);
    doc.DocInfo.GI.GIInit(gi_array);
    doc.DocInfo.opdopl.OPDOPLInit(opd_array,OPDUntilT,OPLUntilT);
    return doc;
  }// of AnalDocCreate
/**
 * Creates a new document of type Document,
 * with the fields relevant to Design document selected.
 * @return doc of type Document
 */

  Document DesDocCreate (){

    Document doc = new Document();
    boolean[] obj_array =new boolean[11];//array for object fields
    boolean[] proc_array =new boolean[6];//array for process fields
    boolean[] rel_array =new boolean[6];//array for relation fields
    boolean[] link_array =new boolean[12];//array for link fields
    boolean[] opd_array  =new boolean[7];//array for opd and opl fields
    boolean[] gi_array = new boolean[12];//array for gen info fields

    obj_array[0] = true;//ObjType
    obj_array[1] = true;//ObjDesc
    obj_array[2] = true;//ObjInitVal
    obj_array[3] = true;//ObjEssence
    obj_array[4] = true;//ObjIndex
    obj_array[5] = true;//ObjScope
    obj_array[6] = true;//ObjOrigin
    obj_array[7] = true;//ObjStates
    obj_array[8] = true;//ObjStateDescr
    obj_array[9] = true;//ObjStateInitial
    obj_array[10] = true;//ObjStateTime

    proc_array[0] = true;//ProcDescr
    proc_array[1] = true;//ProcEssence
    proc_array[2] = true;//ProcOrigin
    proc_array[3] = true;//ProcScope
    proc_array[4] = true;//ProcBody
    proc_array[5] = true;//ProcActTime

    rel_array[0] = true;//RelAggreg
    rel_array[1] = true;//RelClassif
    rel_array[2] = true;//RelFeature
    rel_array[3] = true;//RelGeneral
    rel_array[4] = true;//RelBi
    rel_array[5] = true;//RelUni

    link_array[0] = true;//LinkAgent
    link_array[1] = true;//LinkCondition
    link_array[2] = true;//LinkEffect
    link_array[3] = true;//LinkEvent
    link_array[4] = true;//LinkException
    link_array[5] = true;//LinkInstrEv
    link_array[6] = true;//LinkInvoc
    link_array[7] = true;//LinkRes
    link_array[8] = true;//LinkInstr
    link_array[9] = true;//LinkPropCond
    link_array[10] = true;//DDLinkPropPath
    link_array[11] = true;//DDLinkPropReacTime

    opd_array[0] = true;//OPDAll
    opd_array[1] = false;//OPDByName
    opd_array[2] = false;//OPDNone
    opd_array[3] = false;//OPLAll
    opd_array[4] = false;//OPLByName
    opd_array[5] = false;//OPLNone
    opd_array[6] = true;//OPLandOPD

    gi_array[0] = true;//GIClient
    gi_array[1] = true;//GIOverview
    gi_array[2] = true;//GICurrent
    gi_array[3] = true;//GIGoals
    gi_array[4] = true;//GIBusiness
    gi_array[5] = true;//GIFuture
    gi_array[6] = true;//GIHard
    gi_array[7] = true;//GIInputs
    gi_array[8] = true;//GIIssues
    gi_array[9] = true;//GIProblems
    gi_array[10] = true;//GIUsers
    gi_array[11] = true;//GIOper

    String OPDUntilT,OPLUntilT;
    OPDUntilT="-1";
    OPLUntilT="-1";

    doc.DocInfo.Data.Proc.ProcInit(proc_array);
    doc.DocInfo.Data.Obj.ObjInit(obj_array);
    doc.DocInfo.Data.Link.LinkInit(link_array);
    doc.DocInfo.Data.Rel.RelInit(rel_array);
    doc.DocInfo.GI.GIInit(gi_array);
    doc.DocInfo.opdopl.OPDOPLInit(opd_array,OPDUntilT,OPLUntilT);
    return doc;
  }// of DesDocCreate
  }