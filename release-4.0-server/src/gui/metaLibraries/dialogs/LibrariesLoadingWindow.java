package gui.metaLibraries.dialogs;

import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.MetaManager;
import gui.opdProject.OpdProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The window enable users to configure meta-libraries if there were any errors
 * while loading the libraries. Users can change the path of the library or to
 * remove it from the list.
 * 
 * @author Eran Toch
 * 
 */
public class LibrariesLoadingWindow extends JDialog {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
         * 
         */

	private MetaManager metaManager = null;

	private JButton okButton = new JButton();

	private JButton cancelButton = new JButton();

	private LibrariesImportsDialog libsFix = null;

	private boolean okPressed = false;

	private Vector<MetaLibrary> updatedLibs = null;

	private OpdProject currentProject = null;

	private JFrame parentFrame = null;

	private boolean libFix = true;

	private boolean showIsPolicy;

	public LibrariesLoadingWindow(MetaManager meta, OpdProject _project,
			JFrame parent) {
		super(parent, "Templates Configuration", true);
		this.metaManager = meta;
		this.currentProject = _project;
		this.parentFrame = parent;
		try {
			this.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LibrariesLoadingWindow(MetaManager meta, OpdProject _project,
			boolean isLibFix, boolean showIsPolicy, JFrame parent) {
		super(parent, "Templates Configuration", true);
		this.metaManager = meta;
		this.currentProject = _project;
		this.parentFrame = parent;
		this.libFix = isLibFix;
		this.showIsPolicy = showIsPolicy;
		try {
			this.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {

		this.okButton.setText("OK");
		this.okButton.setBounds(new Rectangle(235, 278, 96, 28));
		this.okButton.addActionListener(new okAdapter(this));
		this.cancelButton.setBounds(new Rectangle(343, 278, 96, 28));
		this.cancelButton.setText("Cancel");
		this.cancelButton.addActionListener(new cancelAdapter(this));
		if (libFix) {
			this.libsFix = new LibrariesFix(this.metaManager,
					this.currentProject, this.parentFrame);
		} else {
			this.libsFix = new LibrariesImportsDialog(this.metaManager,
					this.currentProject, this.parentFrame, showIsPolicy);
		}
		JPanel libsFrame = new JPanel();
		libsFrame
				.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		libsFrame.setBounds(new Rectangle(20, 13, 440, 250));
		libsFrame.setLayout(new BorderLayout());
		libsFrame.add(this.libsFix, BorderLayout.CENTER);
		this.getContentPane().setLayout(null);
		this.getContentPane().add(libsFrame, null);
		this.getContentPane().add(this.okButton, null);
		this.getContentPane().add(this.cancelButton, null);
		this.setBounds(0, 0, 480, 360);
		this.setLocationRelativeTo(this.getParent());

	}

	/**
	 * Handles OK - Set the new libraries vector and disposes the window.
	 */
	public void doOkay() {
		this.okPressed = true;
		this.updatedLibs = this.libsFix.getMetaManager().getVectorClone();
		this.setVisible(false);
	}

	/**
	 * handles Cancel - disposes the window.
	 */
	public void doCancel() {
		this.okPressed = false;
		this.dispose();
	}

	public boolean showDialog() {
		this.setVisible(true);
		return this.okPressed;
	}

	/**
	 * Returns a Vector of the updated meta-libraries (after the changes that
	 * the user has done).
	 * 
	 * @return
	 */
	public Vector<MetaLibrary> getUpdatedLibs() {
		return this.updatedLibs;
	}
}

class okAdapter implements java.awt.event.ActionListener {
	private LibrariesLoadingWindow adaptee;

	okAdapter(LibrariesLoadingWindow adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.doOkay();
	}
}

class cancelAdapter implements java.awt.event.ActionListener {
	private LibrariesLoadingWindow adaptee;

	cancelAdapter(LibrariesLoadingWindow adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.doCancel();
	}
}