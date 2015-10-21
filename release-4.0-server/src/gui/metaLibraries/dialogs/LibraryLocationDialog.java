package gui.metaLibraries.dialogs;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.dataProject.DataCreatorType;
import gui.license.Features;
import gui.util.CustomFileFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * A dialog enabling to choose a meta-library to be imported into the current
 * project model. The dialog allow to choose the type of the library reference
 * (URL or local file), and the path to the meta-library.
 * <p>
 * The method <code>showDialog</code> displays the dialog and returns the
 * input given by the user after OK was pressed. The return value is a
 * {@link java.util.HashMap} object that contains the path and type of the
 * reference. The code below present a scenario of using the dialog:
 * <p>
 * <code>
 * HashMap newRef = addLocation.showDialog();<br>
 * String path = (String)newRef.get("path");<br>
 * int type = ((Integer)newRef.get("type")).intValue();<br>
 * </code>
 * 
 * @author Eran Toch
 * @version 1.0
 */

/*
 * FIXME: this should realy be in the generic dialog section of OPCAT I'll use
 * it here but i need to move it and call it from the generic part.
 * 
 * changes - 1) remove the Metalibrary dialog title and let the caller set it.
 * 2) file dialog title 3) forgot raanan
 */

public class LibraryLocationDialog extends JDialog {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    /**
         * 
         */

    private JPanel mainPanel = new JPanel();

    public JRadioButton chooseFile = new JRadioButton();

    private JRadioButton chooseURL = new JRadioButton();

    private ButtonGroup chooseButtonGroup = new ButtonGroup();

    private JLabel choosingLabel = new JLabel();

    private JTextField locationName = new JTextField();

    private JButton cancelButton = new JButton();

    private JButton okButton = new JButton();

    private JButton broswButton = new JButton();

    private JLabel typeLabel = new JLabel();

    private boolean isPolicy = false;

    private JCheckBox jCheckBoxIsPolicy = new JCheckBox();

    /**
         * The path to the meta-library.
         */
    private String libraryLocation = "";

    /**
         * Contains the path to the meta-library. The default is File.
         */
    private int libraryResourceType = DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE;

    private boolean okPressed = false;

    // Layout manager
    private BorderLayout borderLayout1 = new BorderLayout();

    // strings
    private String ResourceLabel = "Template Location:";

    private String FileChooserTitle = "Add Template";

    private boolean showPolicyCheckBox = false;

    private boolean freeForm = false;

    /**
         * Base constructor - refer all inputs to super.
         */
    protected LibraryLocationDialog(Frame frame, String title, boolean modal) {
	super(frame, title, modal);
    }

    /**
         * Creates a dialog for an existing meta-library reference, allowing the
         * user to modify the reference.
         * 
         * @param _location
         *                String The meta-library path.
         * @param _resourceType
         *                int The reference type.
         * @see LibraryReference#FILE_TYPE_FILE
         * @see LibraryReference#REFERENCE_TYPE_URL
         */
    public LibraryLocationDialog(JFrame parent, String _location,
	    int _resourceType, boolean showPolicyCheckBox, boolean isPolicy) {
	this(parent, "", false);
	this.libraryLocation = _location;
	this.libraryResourceType = _resourceType;
	this.showPolicyCheckBox = showPolicyCheckBox;
	this.isPolicy = isPolicy;
	this.init();
    }

    /**
         * Construct an empty dialog without any existing library reference.
         */
    public LibraryLocationDialog(JFrame parent, boolean showPolicyCheckBox) {
	this(parent, "Location of Imported Template", false);
	this.showPolicyCheckBox = showPolicyCheckBox;
	isPolicy = false;
	freeForm = true;
	this.init();
    }

    /**
         * Construct an empty dialog without any existing library reference. and
         * no limits on the file type
         */
    public LibraryLocationDialog(JFrame parent) {
	this(parent, "Location of File", false);
	this.showPolicyCheckBox = false;
	isPolicy = false;
	freeForm = true ; 
	this.init();
    }

    /**
         * JBuilder's init method.
         */
    public void init() {
	try {
	    this.jbInit();
	    this.pack();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    /**
         * Shows the dialog and return the path of the library that whose
         * choosen. The path might be a local file path or an URL.
         * 
         * @return String The path of the library location.
         */
    public HashMap showDialog() {
	this.setVisible(true);
	if (this.okPressed) {
	    HashMap<String, Object> ref = new HashMap<String, Object>();
	    ref.put("path", this.libraryLocation);
	    ref.put("type", new Integer(this.libraryResourceType));
	    ref.put("policy", new Boolean(isPolicy));
	    return ref;
	} else {
	    return null;
	}
    }

    /**
         * Main dialog creation method.
         * 
         * @throws Exception
         */
    private void jbInit() throws Exception {
	this.chooseURL.setActionCommand("URL");
	this.mainPanel.setLayout(null);
	this.chooseFile
		.setToolTipText("The imported Template is located in the local file system");
	if (this.libraryResourceType == DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE) {
	    this.changeToFile();
	} else {
	    this.changeToURL();
	}
	this.chooseFile.setText("Local File");
	this.chooseFile.setBounds(new Rectangle(141, 13, 104, 23));
	this.chooseFile
		.addActionListener(new OntologyLocationDialog_chooseFile_actionAdapter(
			this));
	this.chooseURL.setText("URL");
	this.chooseURL.setBounds(new Rectangle(246, 14, 127, 23));
	this.chooseURL
		.addActionListener(new OntologyLocationDialog_chooseURL_actionAdapter(
			this));
	this.choosingLabel.setToolTipText("");
	this.choosingLabel.setText(this.ResourceLabel);
	this.choosingLabel.setBounds(new Rectangle(11, 49, 294, 15));
	this.locationName.setBounds(new Rectangle(9, 72, 390, 23));
	this.locationName.setText(this.libraryLocation);
	this.cancelButton.setMinimumSize(new Dimension(71, 23));
	this.cancelButton.setText("Cancel");
	this.cancelButton.setBounds(new Rectangle(461, 126, 100, 29));
	this.cancelButton.setAlignmentX((float) 1.0);
	this.okButton.setText("OK");
	this.okButton.setBounds(new Rectangle(351, 126, 100, 29));
	this.okButton.setAlignmentX((float) 1.0);
	this.okButton.setPreferredSize(new Dimension(65, 23));
	this.okButton
		.addActionListener(new OntologyLocationDialog_okButton_actionAdapter(
			this));
	this.broswButton.setBounds(new Rectangle(400, 71, 100, 24));
	this.broswButton.setText("Browse...");
	this.broswButton
		.addActionListener(new OntologyLocationDialog_broswButton_actionAdapter(
			this));
	this.typeLabel.setText("Resource Type:");
	this.typeLabel.setBounds(new Rectangle(11, 17, 125, 15));
	this.setModal(true);
	this.setTitle("Add Library");
	this.getContentPane().setLayout(this.borderLayout1);

	this.getContentPane().add(this.mainPanel, BorderLayout.CENTER);
	this.mainPanel.setMinimumSize(new Dimension(400, 175));
	this.mainPanel.setPreferredSize(new Dimension(570, 175));

	this.jCheckBoxIsPolicy.setBounds(new Rectangle(9, 110, 200, 24));
	jCheckBoxIsPolicy.setSelected(isPolicy);
	jCheckBoxIsPolicy.setText("Design Policy");

	this.mainPanel.add(this.typeLabel, null);
	this.mainPanel.add(this.chooseFile, null);
	this.mainPanel.add(this.chooseURL, null);
	this.mainPanel.add(this.choosingLabel, null);
	this.mainPanel.add(this.cancelButton, null);
	this.mainPanel.add(this.okButton, null);
	this.mainPanel.add(this.locationName, null);
	this.mainPanel.add(this.broswButton, null);
	if (showPolicyCheckBox && !freeForm) {
	    this.mainPanel.add(this.jCheckBoxIsPolicy);
	    jCheckBoxIsPolicy.setSelected(true);
	    jCheckBoxIsPolicy.setEnabled(false);
	} else if (!freeForm) {
	    this.mainPanel.add(this.jCheckBoxIsPolicy);
	    jCheckBoxIsPolicy.setSelected(false);
	    jCheckBoxIsPolicy.setEnabled(false);
	} else {
	    // free form
	}

	this.chooseButtonGroup.add(this.chooseFile);
	this.chooseButtonGroup.add(this.chooseURL);
	this.cancelButton
		.addActionListener(new OntologyLocationDialog_cancelButton_actionAdapter(
			this));

	// Setting the icon
	// ImageIcon logoIcon = MiscImages.LOGO_SMALL_ICON;
	// this.setIconImage(logoIcon.getImage());

	this.pack();
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	// Locating the dialog
	int fX = Opcat2.getFrame().getX();
	int fY = Opcat2.getFrame().getY();
	int pWidth = Opcat2.getFrame().getWidth();
	int pHeight = Opcat2.getFrame().getHeight();

	this.setLocation(fX + Math.abs(pWidth / 2 - this.getWidth() / 2), fY
		+ Math.abs(pHeight / 2 - this.getHeight() / 2));

	this.setResizable(true);

    }

    /**
         * Choosing to use file. The method displays the "browse" button.
         */
    private void changeToFile() {
	if (this.libraryResourceType != DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE) {
	    this.locationName.setText("");
	}
	this.chooseFile.setSelected(true);
	this.chooseURL.setSelected(false);
	this.broswButton.setEnabled(true);
	this.broswButton.repaint();
	this.libraryResourceType = DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE;
    }

    /**
         * Choosing to use a URL.
         */
    private void changeToURL() {
	if (this.libraryResourceType != DataCreatorType.REFERENCE_TYPE_URL) {
	    this.locationName.setText("http://");
	}
	this.chooseFile.setSelected(false);
	this.chooseURL.setSelected(true);
	this.broswButton.setEnabled(false);
	this.broswButton.repaint();
	this.libraryResourceType = DataCreatorType.REFERENCE_TYPE_URL;
    }

    /**
         * Clicking on cancel
         * 
         * @param e
         *                ActionEvent
         */
    void cancelButton_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    /**
         * Clicking on browse.
         * 
         * @param e
         *                ActionEvent
         */
    void broswButton_actionPerformed(ActionEvent e) {
	JFileChooser myFileChooser = new JFileChooser();
	myFileChooser.setSelectedFile(new File(""));
	myFileChooser.resetChoosableFileFilters();
	if (!freeForm) {
	    myFileChooser.removeChoosableFileFilter(myFileChooser
		    .getAcceptAllFileFilter());
	    CustomFileFilter myFilter;
	    if (!jCheckBoxIsPolicy.isSelected()) {
		String[] csv = { "csv", "txt" };
		myFilter = new CustomFileFilter(csv, "Classification Files");
		if (Features.hasReq()) {
		    myFileChooser.addChoosableFileFilter(myFilter);
		}
	    }

	    String[] exts = { "opx", "opz" };
	    if (!jCheckBoxIsPolicy.isSelected()) {
		myFilter = new CustomFileFilter(exts, "Classification Models");
	    } else {
		myFilter = new CustomFileFilter(exts, "Opcat2 Files");
	    }
	    myFileChooser.addChoosableFileFilter(myFilter);
	}
	String ld = FileControl.getInstance().getLastDirectory();
	if (!ld.equals("")) {
	    myFileChooser.setCurrentDirectory(new File(ld));
	}
	int retVal = myFileChooser.showDialog(this, this.FileChooserTitle);

	if (retVal != JFileChooser.APPROVE_OPTION) {
	    return;
	}
	String fileName = myFileChooser.getSelectedFile().getPath();
	String newDirectory = myFileChooser.getSelectedFile().getParent();
	FileControl.getInstance().setLastDirectory(newDirectory);
	this.locationName.setText(fileName);
	this.locationName.repaint();
    }

    /**
         * Clicking on OK: saving the values to the proper fields.
         * 
         * @param e
         *                ActionEvent
         */
    void okButton_actionPerformed(ActionEvent e) {
	this.libraryLocation = this.locationName.getText();
	if (this.chooseFile.isSelected()) {
	    this.libraryResourceType = DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE;
	} else if (this.chooseURL.isSelected()) {
	    this.libraryResourceType = DataCreatorType.REFERENCE_TYPE_URL;
	}
	this.isPolicy = this.jCheckBoxIsPolicy.isSelected();
	this.okPressed = true;
	this.dispose();
    }

    /**
         * Clicking on the choose file radio button.
         * 
         * @param e
         *                ActionEvent
         */
    void chooseFile_actionPerformed(ActionEvent e) {
	this.changeToFile();
    }

    /**
         * Clicking on the choose URL radio button.
         * 
         * @param e
         *                ActionEvent
         */
    void chooseURL_actionPerformed(ActionEvent e) {
	this.changeToURL();
    }

    /**
         * @param resourceLabel
         *                The resourceLabel to set.
         */
    public void setResourceLabel(String resourceLabel) {
	this.ResourceLabel = resourceLabel;
	this.choosingLabel.setText(this.ResourceLabel);
    }

    /**
         * @param fileChooserTitle
         *                The fileChooserTitle to set.
         */
    public void setFileChooserTitle(String fileChooserTitle) {
	this.FileChooserTitle = fileChooserTitle;
    }

}

class OntologyLocationDialog_cancelButton_actionAdapter implements
	java.awt.event.ActionListener {
    LibraryLocationDialog adaptee;

    OntologyLocationDialog_cancelButton_actionAdapter(
	    LibraryLocationDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.cancelButton_actionPerformed(e);
    }
}

class OntologyLocationDialog_broswButton_actionAdapter implements
	java.awt.event.ActionListener {
    LibraryLocationDialog adaptee;

    OntologyLocationDialog_broswButton_actionAdapter(
	    LibraryLocationDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.broswButton_actionPerformed(e);
    }
}

class OntologyLocationDialog_okButton_actionAdapter implements
	java.awt.event.ActionListener {
    LibraryLocationDialog adaptee;

    OntologyLocationDialog_okButton_actionAdapter(LibraryLocationDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.okButton_actionPerformed(e);
    }
}

class OntologyLocationDialog_chooseFile_actionAdapter implements
	java.awt.event.ActionListener {
    LibraryLocationDialog adaptee;

    OntologyLocationDialog_chooseFile_actionAdapter(
	    LibraryLocationDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.chooseFile_actionPerformed(e);
    }
}

class OntologyLocationDialog_chooseURL_actionAdapter implements
	java.awt.event.ActionListener {
    LibraryLocationDialog adaptee;

    OntologyLocationDialog_chooseURL_actionAdapter(LibraryLocationDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.chooseURL_actionPerformed(e);
    }
}
