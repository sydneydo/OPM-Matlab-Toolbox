package extensionTools.Documents.UserInterface;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import exportedAPI.opcatAPI.IOpd;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import extensionTools.Documents.Doc.Document;

/**
 * DocOPDByName Class extends the standard JDialog. Allows user to select
 * diagrams and OPL paragraphs by name for the document he is interested in.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */

public class DocOPDByName extends JDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
	 * 
	 */
	 
ISystem mySys;
  int Type;
  Document doc;
  JDialog dad;
  JPanel OPDOPLMain;
  Vector opl_list=new Vector(0, 1);
  Vector opd_list = new Vector(0, 1);
  String[] data;
  String[] data1;
  Border border1;
  TitledBorder titledBorder1;
  JPanel OPDOPLSub1 = new JPanel();
  JPanel OPDButtonPanel = new JPanel();
  JScrollPane OPDScrollPanel = new JScrollPane();
  JButton OPDOPLOk = new JButton();
  JScrollPane OPDScrollPanel1 = new JScrollPane();
  JButton OPDOPLCancel = new JButton();
  Box box2;
  JPanel OPDOPLSub = new JPanel();
  JList OPDList;
  JList OPLList;
  Border border2;
  TitledBorder titledBorder2;
/**
 * Construct the frame.
 * @param sys of type ISystem
 * @param type of type int: 0 - OPD only; 1 - OPL only; 2 - OPD and OPL
 * @param d of type Document
 * @param prev of type JDialog - the previous screen
 */
  public DocOPDByName(ISystem sys, int type, Document d, JDialog prev) {
    super(prev, "OPD & OPL by name selection", true );
    this.mySys = sys;
    this.Type = type;
    this.doc=d;
    this.dad=prev;
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
          DocOPDByName.this.OPDOPLCancel.doClick();
          return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
          DocOPDByName.this.OPDOPLOk.doClick();
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
    IOpd abstractEntry;
    ISystemStructure MyStructure = this.mySys.getISystemStructure();
    //get all the OPDs in the project
    Enumeration e = MyStructure.getOpds();
    int i=0;

    Vector v_opd=new Vector(1, 1);
    Vector v_opl=new Vector(1, 1);

    while (e.hasMoreElements()) {
      abstractEntry = (IOpd)e.nextElement();
      v_opd.addElement(abstractEntry);
      v_opl.addElement(abstractEntry);
    }
    String data[] = new String [v_opd.size()];//list of opd names
    String data1[] = new String [v_opl.size()];//list of opl names
    i=0;
    //insert names to OPD list
    while (i<v_opd.size()) {
      data[i]=((IOpd)v_opd.get(i)).getName();
      i++;
    }
    i=0;
    //insert names to OPD list
    while (i<v_opl.size()) {
      data1[i]=((IOpd)v_opl.get(i)).getName();
      i++;
    }
    //sorts the names in the correct order
    Arrays.sort(data);
    Arrays.sort(data1);

    this.OPDList = new JList(data);
    this.OPLList = new JList(data1);

    this.OPDOPLMain = (JPanel) this.getContentPane();
    this.box2 = Box.createHorizontalBox();
    this.border2 = BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134));
    this.titledBorder2 = new TitledBorder(this.border2,"Please select OPL by name:");
    this.OPDOPLSub1.setLayout(null);
    this.OPDOPLSub1.setBorder(this.titledBorder2);
    this.border1 = BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134));
    this.titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Please select OPD by name:");
    this.OPDOPLMain.setLayout(null);
    this.getContentPane().setBackground(SystemColor.control);
    this.setResizable(false);
    this.setSize(new Dimension(376, 259));
    this.setTitle("OPD&OPL By Name");
    this.OPDOPLMain.setBackground(new Color(204, 204, 204));
    this.OPDButtonPanel.setBounds(new Rectangle(6, 195, 364, 34));
    this.OPDButtonPanel.setLayout(null);
    this.OPDScrollPanel.setBounds(new Rectangle(24, 27, 121, 117));
    this.OPDOPLOk.setText("OK");
    this.OPDOPLOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocOPDByName.this.OPDOPLOk_actionPerformed(e);
      }
    });
    this.OPDOPLOk.setBounds(new Rectangle(160, 0, 76, 28));
    this.OPDScrollPanel1.setBounds(new Rectangle(24, 27, 120, 117));
    this.OPDOPLCancel.setText("Cancel");
    this.OPDOPLCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DocOPDByName.this.OPDOPLCancel_actionPerformed(e);
      }
    });
    this.OPDOPLCancel.setBounds(new Rectangle(253, 0, 81, 27));
    this.OPDOPLSub.setBorder(this.titledBorder1);
    this.OPDOPLSub.setLayout(null);
    this.OPDOPLSub1.setVisible(true);
    this.box2.setBackground(new Color(204, 204, 204));
    this.box2.setBounds(new Rectangle(4, 7, 365, 176));
    this.OPDScrollPanel1.getViewport();
    this.OPDButtonPanel.add(this.OPDOPLOk, null);
    this.OPDButtonPanel.add(this.OPDOPLCancel, null);
    this.OPDOPLMain.add(this.box2, null);
    this.box2.add(this.OPDOPLSub, null);
    this.OPDOPLSub.add(this.OPDScrollPanel, null);
    this.box2.add(this.OPDOPLSub1, null);
    this.OPDOPLSub1.add(this.OPDScrollPanel1, null);
    this.OPDOPLMain.add(this.OPDButtonPanel, null);
    this.OPDScrollPanel1.getViewport().add(this.OPLList, null);
    this.OPDScrollPanel.getViewport().add(this.OPDList, null);
    if (this.Type == 0) {
      this.OPDOPLSub.setVisible(true);
      this.OPDOPLSub1.setVisible(false);
    }
    if (this.Type == 1) {
      this.OPDOPLSub.setVisible(false);
      this.OPDOPLSub1.setVisible(true);
    }
    if (this.Type == 2) {
      this.OPDOPLSub.setVisible(true);
      this.OPDOPLSub1.setVisible(true);
    }
  }

  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        this.dispose();
    }
  }

/**
 * After the user finished choosing and pressed ok, checks for valid inputs.
 * If all the inputs are valid, opens the save window and calls for print
 * function in "doc".
 * @param e ActionEvent on OK button
 */
  void OPDOPLOk_actionPerformed(ActionEvent e) {
    // updating doc with the lists of diagrams and paragraphs
    this.opd_list.clear();
    this.opl_list.clear();
    Object[] temp_elem=this.OPDList.getSelectedValues();
    Object[] temp_elem2=this.OPLList.getSelectedValues();

    //if opd by name was selected and no name was chosen - error
    if ((((this.Type==0)||(this.Type==2))&&(temp_elem.length==0))||(((this.Type==1)||(this.Type==2))&&(temp_elem2.length==0))){
      JOptionPane.showMessageDialog(this, "Please select at least one diagram.", "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    else{
      this.doc.DocInfo.opdopl.setOPDList(temp_elem); //save opd list into document
      this.doc.DocInfo.opdopl.setOPLList(temp_elem2); //save opl list into document

      String filename="";
      DocSave doc_save = new DocSave(this);
      int returnVal = doc_save.SDFileChooser.showSaveDialog(doc_save.SDFileChooser);
      if(returnVal == JFileChooser.APPROVE_OPTION) {
        String name = doc_save.SDFileChooser.getSelectedFile().getName();
        filename = doc_save.SDFileChooser.getSelectedFile().getPath();
        StringTokenizer st = new StringTokenizer(filename, ".", false);
        filename = st.nextToken()+".htm";
        //check for files with the same name
        File myDir = doc_save.SDFileChooser.getCurrentDirectory();
        int flag = 0; //0 - no such file yet
        int i=0;
        if (myDir.exists()) {
          File[] contents = myDir.listFiles();
          while (i < contents.length) {
             if (contents[i].getName().compareTo(name)==0) {
				flag = 1;
			}
               i++;
             }//of if
          }//of while
          //if there is a file with this name already
          //ask if overwrite
          //if YES -> save the file
          if (flag==1){
           int retval=JOptionPane.showConfirmDialog( this, "A document with this name already exists. Do you want to overwrite it?", "Overwrite Document", JOptionPane.YES_NO_OPTION);
           if (retval== JOptionPane.YES_OPTION){
              try{
               this.doc.PrintDocument(this.mySys,filename);//make the file
               this.dad.dispose();//close the previous screen (DocGen)
               this.dispose();//close this screen
              }// of try
              catch(IOException er){
               System.out.println("There was a problem saving the file");
              }// of catch
           }//of if
        }//of if
        else {
         try{
           this.doc.PrintDocument(this.mySys,filename);//make the file
           this.dad.dispose();//close the previous screen (DocGen)
           this.dispose();//close this screen
         }//of try
         catch(IOException er){
           System.out.println("There was a problem saving the file");
         }//of catch
        }//of else
      }//of if
    }//of else
  }
/**
 * Cancels choosing by name and returns to the DocGen Screen.
 * @param e ActionEvent on cancel button
 */
  void OPDOPLCancel_actionPerformed(ActionEvent e) {
    this.dispose();
  }
}