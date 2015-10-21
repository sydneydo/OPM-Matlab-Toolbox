package extensionTools.Documents.UserInterface;

import gui.util.CustomFileFilter;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * DocSave Class extends the standard JDialog. Allows user to select
 * the path and the name for the document he is interested in.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */
public class DocSave extends JDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
	 * 
	 */
	 
JPanel SDMain;
  JPanel SDPanel = new JPanel();
  JFileChooser SDFileChooser = new JFileChooser();
  String[] filters = {"htm" , "html"};

/**
 * Construct the frame.
 * @param dad of type JDialog - the previous screen
 */
  public DocSave(JDialog dad) {
    super(dad, "Save Document", true);
    this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
/**
 * Component initialization
 */
  private void jbInit() {

    this.SDMain = (JPanel) this.getContentPane();
    this.SDMain.setLayout(null);
    this.setResizable(false);
    this.setSize(new Dimension(493, 325));
    this.SDPanel.setBounds(new Rectangle(1, 1, 486, 321));
    this.SDPanel.setLayout(null);
    this.SDFileChooser.setToolTipText("");
    this.SDFileChooser.setFileFilter(new CustomFileFilter(this.filters));
    this.SDFileChooser.setBounds(new Rectangle(10, 5, 466, 283));
    this.SDMain.setBackground(new Color(204, 204, 204));
    this.SDMain.add(this.SDPanel, null);
    this.SDPanel.add(this.SDFileChooser, null);
    this.SDFileChooser.setApproveButtonText("Save");
  }

  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      this.dispose();
    }
  }
}