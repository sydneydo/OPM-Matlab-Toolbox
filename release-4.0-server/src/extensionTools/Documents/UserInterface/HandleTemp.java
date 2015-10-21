package extensionTools.Documents.UserInterface;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import exportedAPI.opcatAPI.ISystem;
import extensionTools.Documents.Doc.Document;
/**
 * HandleTemp Class extends the standard JDialog. Allows user to work with
 * templates: create new template, delete or edit templates.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */


public class HandleTemp extends JDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
	 * 
	 */
	 
ISystem mySys;
  JPanel contentPane;
  String data[];
  JPanel HTPanel = new JPanel();
  JButton HTNew = new JButton();
  JButton HTEdit = new JButton();
  JButton HTDel = new JButton();
  JButton HTCancel = new JButton();
  JScrollPane HTScroll = new JScrollPane();
  JList HTList;
  DefaultListModel list_model = new DefaultListModel();
  Border border1;
  TitledBorder titledBorder1;
  Border border2;
  File myDir = new File("Templates");


  /**
   * Constructs the frame.
   * @param sys of type ISystem
   */
  public HandleTemp(ISystem sys) {
    super(sys.getMainFrame(), true);
    this.mySys = sys;
    this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    //setting the location in the center
    int fX = sys.getMainFrame().getX();
    int fY = sys.getMainFrame().getY();
    int pWidth = sys.getMainFrame().getWidth();
    int pHeight = sys.getMainFrame().getHeight();
    this.setLocation(fX + Math.abs(pWidth/2-this.getWidth()/2), fY + Math.abs(pHeight/2-this.getHeight()/2));
    //Esc key listener
    KeyListener kListener = new KeyAdapter(){
        public void keyReleased(KeyEvent e){
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                HandleTemp.this.HTCancel.doClick();
                return;
            }
        }
    };
    this.addKeyListener(kListener);

  }
  /**
   * Component initialization.
   */
  private void jbInit(){
    this.setResizable(false);//disable changing the size of the dialog
    this.contentPane = (JPanel) this.getContentPane();
    this.border1 = BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134));
    this.titledBorder1 = new TitledBorder(this.border1,"Handle Template");
    this.border2 = BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134));
    this.contentPane.setLayout(null);
    this.setSize(new Dimension(378, 240));
    this.setTitle("Handle Template");
    this.HTPanel.setBorder(this.titledBorder1);
    this.HTPanel.setBounds(new Rectangle(6, 5, 351, 198));
    this.HTPanel.setLayout(null);
    this.HTNew.setBounds(new Rectangle(216, 28, 79, 27));
    this.HTNew.setBorder(this.border2);
    this.HTNew.setText("New");
    this.HTNew.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HandleTemp.this.HTNew_actionPerformed(e);
      }
    });
    this.HTEdit.setBounds(new Rectangle(216, 59, 79, 27));
    this.HTEdit.setBorder(this.border2);
    this.HTEdit.setText("Edit");
    this.HTEdit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HandleTemp.this.HTEdit_actionPerformed(e);
      }
    });
    this.HTDel.setBounds(new Rectangle(216, 89, 79, 27));
    this.HTDel.setBorder(this.border2);
    this.HTDel.setText("Delete");
    this.HTDel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HandleTemp.this.HTDel_actionPerformed(e);
      }
    });
    this.HTCancel.setBounds(new Rectangle(216, 148, 79, 27));
    this.HTCancel.setBorder(this.border2);
    this.HTCancel.setText("Cancel");
    this.HTCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HandleTemp.this.HTCancel_actionPerformed(e);
      }
    });
    this.HTScroll.setBounds(new Rectangle(29, 28, 137, 147));

    this.contentPane.add(this.HTPanel, null);
    this.HTPanel.add(this.HTScroll, null);
    this.HTPanel.add(this.HTNew, null);
    this.HTPanel.add(this.HTEdit, null);
    this.HTPanel.add(this.HTDel, null);
    this.HTPanel.add(this.HTCancel, null);
    //creating the list of available templates
    int counter=0;
    if (this.myDir.exists()) {
      File[] contents = this.myDir.listFiles();
      while (counter < contents.length) {
        this.list_model.addElement(contents[counter].getName());
        counter++;
      }
    }
    this.HTList = new JList(this.list_model);
    this.HTList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.HTScroll.getViewport().add(this.HTList, null);
  }
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        this.dispose();
    }
  }
  /**
   * Opens a TempEdit screen in order to make selections for a new template.
   * @param e ActionEvent on New button
   */
  void HTNew_actionPerformed(ActionEvent e) {
    //creates and shows a screen for selection of fields for template
    TempEdit te = new TempEdit(this.mySys, "-1", this);
    te.setVisible(true);

  }
  /**
   * Openes a TempEdit screen, with the selected fields of the template marked, in
   * order to allow the user editing.
   * @param e ActionEvent on Edit button
   */
  void HTEdit_actionPerformed(ActionEvent e) {
    try {
      //check if a template for editing was selected
      if (!this.HTList.isSelectionEmpty()) {
        Document doc =new Document();//create a document
        String filename = (String)this.HTList.getModel().getElementAt(this.HTList.getSelectedIndex());
        FileInputStream file = new FileInputStream("Templates/"+filename);
        doc.FromTemp(file);//insert the info from the template to document
        //create an editing screen and show the selected fields
        TempEdit te = new TempEdit(this.mySys, filename, this);
        te.TempToScreen(doc);
        te.setVisible(true);
        file.close();
      }
      //if a template was not chosen -> error
      else {
        JOptionPane.showMessageDialog(this, "Please select a Template.", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
    }
    catch(IOException er){
       System.out.println("Couldn't open a template");
    }
  }
  /**
   * Cancels working with templates.
   * @param e ActionEvent on cancel button
   */
  void HTCancel_actionPerformed(ActionEvent e) {
    this.dispose();
  }
  /**
   * Deletes the chosen template.
   * @param e ActionEvent on Delete button
   */
  void HTDel_actionPerformed(ActionEvent e) {
    //if a template was selected
    if (!this.HTList.isSelectionEmpty()){
      String filename = (String)this.HTList.getModel().getElementAt(this.HTList.getSelectedIndex());
      //makes sure the user wants to delete
      int retval=JOptionPane.showConfirmDialog( this, "Are you sure you want to delete '"+filename+"' template?", "Delete Template", JOptionPane.YES_NO_OPTION);
      if (retval== JOptionPane.YES_OPTION){
        File aFile = new File ("Templates/"+filename);
        aFile.delete();//remove file
        this.list_model.removeElement(filename);//remove from the list on the screen
      }
     }
     //if template was not chosen -> error
     else {
        JOptionPane.showMessageDialog(this, "Please select a Template.", "ERROR", JOptionPane.ERROR_MESSAGE);
      }

   }
}