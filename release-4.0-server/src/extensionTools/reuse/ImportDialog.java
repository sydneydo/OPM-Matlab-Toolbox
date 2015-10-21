package extensionTools.reuse;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.opdProject.OpdProject;
import gui.util.CustomFileFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * The target of this class is to manage the import dialog with the user,
 * allowing the user to choose the location of a file to be imported directly to
 * the current model.
 * 
 */
public class ImportDialog extends JDialog {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    /**
         * 
         */

    JPanel importPanel = new JPanel();

    JProgressBar pBar = new JProgressBar();

    Border border1;

    TitledBorder titledBorder1;

    JTextField fileNameTextField = new JTextField();

    JButton chooseFileButton = new JButton();

    JLabel reuseTypeLabel = new JLabel();

    JLabel saveWarningLabel = new JLabel();

    JComboBox reuseTypeComboBox = new JComboBox();

    JButton cancelButton = new JButton();

    JButton importButton = new JButton();

    OpdProject reusedProject = null;

    OpdProject curr;

    JDesktopPane desktop;

    JFileChooser fc = new JFileChooser();

    Opcat2 opcat2;

    JWindow waitScreen;

    private boolean isOperated = true;

    private int typeOfReuse = ReuseCommand.NO_REUSE;

    private String importFileName = "";

    private boolean showConnectType = false;

    // ProgressBar progressBar;
    public ImportDialog(OpdProject currProject, JDesktopPane _desktop,
	    boolean showConnectType) throws HeadlessException {
	super(currProject.getMainFrame(), true);
	this.showConnectType = showConnectType;
	GuiControl gui = GuiControl.getInstance();
	if (!gui.IsProjectOpen()) {
	    return;
	}
	try {
	    this.curr = currProject;
	    this.jbInit();
	    this.desktop = _desktop;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	this.initComboBox();
	this.addListeners();
	int fX = currProject.getMainFrame().getX();
	int fY = currProject.getMainFrame().getY();
	int pWidth = currProject.getMainFrame().getWidth();
	int pHeight = currProject.getMainFrame().getHeight();
	this.setLocation(fX + Math.abs(pWidth / 3 - this.getWidth() / 3), fY
		+ Math.abs(pHeight / 3 - this.getHeight() / 3));
	this.setSize(450, 250);
    }

    public ImportDialog() {
	GuiControl gui = GuiControl.getInstance();
	if (!gui.IsProjectOpen()) {
	    return;
	}

	try {
	    this.jbInit();
	    this.setBounds(200, 300, 300, 200);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	KeyListener kListener = new KeyAdapter() {
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		    ImportDialog.this.cancelButton.doClick();
		    return;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    ImportDialog.this.importButton.doClick();
		    return;
		}

	    }
	};
	this.addKeyListener(kListener);

    }

    private void jbInit() throws Exception {
	this.border1 = new EtchedBorder(EtchedBorder.RAISED, Color.white,
		new Color(134, 134, 134));
	this.titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(
		Color.white, new Color(134, 134, 134)), "Import Model");
	this.setTitle("Import Model");
	this.getContentPane().setLayout(null);
	this.importPanel.setBorder(this.titledBorder1);
	this.importPanel.setMinimumSize(new Dimension(22, 37));
	this.importPanel.setInputVerifier(null);
	this.importPanel.setBounds(new Rectangle(7, 8, 409, 112));
	this.importPanel.setLayout(null);
	this.fileNameTextField.setText("");
	this.fileNameTextField.setBounds(new Rectangle(15, 33, 253, 20));
	this.chooseFileButton.setBounds(new Rectangle(289, 33, 101, 21));
	this.chooseFileButton.setText("Browse...");
	this.reuseTypeLabel.setText("Reuse Type:");
	this.reuseTypeLabel.setBounds(new Rectangle(16, 67, 142, 19));

	String message = "";
	if (this.curr == null) {
	    return;
	}
	if ((this.curr.getFileName() == null)
		|| this.curr.getFileName().equals("")) {
	    message = "You will be prompted to save the project before importing";
	} else {
	    message = "Your project will be automatically saved before importing";
	}

	this.saveWarningLabel.setText(message);
	this.saveWarningLabel.setBounds(new Rectangle(16, 125, 440, 20));

	this.reuseTypeComboBox.setBounds(new Rectangle(223, 70, 169, 20));

	this.cancelButton.setBounds(new Rectangle(318, 155, 95, 25));
	this.cancelButton.setText("Cancel");
	this.importButton.setText("Import");
	this.importButton.setBounds(new Rectangle(215, 155, 95, 25));
	this.importPanel.add(this.fileNameTextField, null);
	this.importPanel.add(this.chooseFileButton, null);
	this.importPanel.add(this.reuseTypeLabel, null);
	this.importPanel.add(this.reuseTypeComboBox, null);
	this.getContentPane().add(this.saveWarningLabel, null);
	this.getContentPane().add(this.cancelButton, null);
	this.getContentPane().add(this.importButton, null);
	this.getContentPane().add(this.importPanel, null);
	this.setSize(60, 25);
    }

    private void addListeners() {
	// open "Open File" dialog window
	this.chooseFileButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ImportDialog.this.fc.resetChoosableFileFilters();
		String[] exts = { "opx", "opz" };
		CustomFileFilter myFilter = new CustomFileFilter(exts,
			"Opcat2 Files");

		ImportDialog.this.fc.addChoosableFileFilter(myFilter);
		String ld = FileControl.getInstance().getLastDirectory();
		if (!ld.equals("")) {
		    ImportDialog.this.fc.setCurrentDirectory(new File(ld));
		}
		ImportDialog.this.fc.showOpenDialog(null);

		if (ImportDialog.this.fc.getSelectedFile() != null) {
		    ImportDialog.this.fileNameTextField
			    .setText(ImportDialog.this.fc.getSelectedFile()
				    .getPath());
		    String newDirectory = ImportDialog.this.fc
			    .getSelectedFile().getParent();
		    FileControl.getInstance().setLastDirectory(newDirectory);
		}
	    }
	});

	// import close window and import selected system
	this.importButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ImportDialog.this.importPerformed();
	    }
	});

	// cancel - close Dialog without doint anything
	this.cancelButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		ImportDialog.this.CancelPerformed();
	    }
	});

	this.addWindowListener(new myAdapter());
    }

    private void initComboBox() {
	// reuseTypeComboBox.addItem("Select Reuse Type");
	this.reuseTypeComboBox.addItem("Copy Elements");
	if (showConnectType)
	    reuseTypeComboBox.addItem("Copy and Connect");
	/*
         * TODO: commanted untill we fix the bug in the locked import type
         * raanan
         */
	// Commented by Eran
	// reuseTypeComboBox.addItem("Open Reuse");
    }

    private void CancelPerformed() {
	this.isOperated = false;
	this.dispose();
    }

    private void importPerformed() {
	this.importFileName = this.fileNameTextField.getText();
	this.typeOfReuse = this.reuseTypeComboBox.getSelectedIndex();
	this.dispose();
    }

    /**
         * @return Returns the type Of Reuse. See ReuseCommand static elements
         *         for possible types of reuse.
         */
    public int getTypeOfReuse() {
	return this.typeOfReuse;
    }

    /**
         * @return Returns the import File Name.
         */
    public String getImportFileName() {
	return this.importFileName;
    }

    /**
         * @return Returns <code>true</code> if the user choose to import.
         *         <code>false</code> if the user choose to cancel the import.
         */
    public boolean isOperated() {
	return this.isOperated;
    }

    class myAdapter extends WindowAdapter {

	public void windowClosing() {
	    ImportDialog.this.CancelPerformed();
	}
    }
}
