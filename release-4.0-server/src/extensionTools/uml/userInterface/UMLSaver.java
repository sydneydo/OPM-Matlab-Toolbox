package extensionTools.uml.userInterface;

import gui.util.CustomFileFilter;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Saves the created XML file.
 */
public class UMLSaver extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
	 * 
	 */
	 
JPanel UMLSaveMain;
  JFileChooser UMLSaver = new JFileChooser();


  /**
   * Construct the frame
   */
  public UMLSaver() {
    this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   *Component initialization
   */
  private void jbInit() throws Exception  {
    this.UMLSaveMain = (JPanel) this.getContentPane();
    this.UMLSaveMain.setLayout(null);
    this.setResizable(false);
    this.setSize(new Dimension(493, 325));
    this.setTitle("Save XMI Document");
    this.UMLSaver.setBackground(new Color(204, 204, 204));
    this.UMLSaver.setFont(new java.awt.Font("Dialog", 0, 12));
    this.UMLSaver.setBounds(new Rectangle(10, 5, 466, 283));
    this.UMLSaver.setFileFilter(new CustomFileFilter("xmi", "XMI Files"));
    this.UMLSaver.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        UMLSaver.this.UMLSaver_actionPerformed(e);
      }
    });
    this.UMLSaveMain.setBackground(new Color(204, 204, 204));
    this.UMLSaveMain.add(this.UMLSaver, null);
    this.UMLSaver.setApproveButtonText("Export to XMI");
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      this.dispose();
    }
  }

  void UMLSaver_actionPerformed(ActionEvent e) {
  this.dispose();
  }
}