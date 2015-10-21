package gui.scenarios;

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

import gui.metaLibraries.dialogs.LibrariesImportsDialog;
import gui.metaLibraries.logic.MetaManager;
import gui.opdProject.OpdProject;

public class ScenarioSettingsDialog extends JDialog {

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

	private Vector updatedLibs = null;

	private OpdProject currentProject = null;

	private JFrame parentFrame = null;

	public ScenarioSettingsDialog(MetaManager meta, OpdProject _project,
			JFrame parent) {
		super(parent, "Libraries Configuration", true);
		this.metaManager = meta;
		this.currentProject = _project;
		this.parentFrame = parent;
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
		this.libsFix = new LibrariesImportsDialog(this.metaManager, this.currentProject, 
				this.parentFrame);
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
		this.dispose();
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
	public Vector getUpdatedLibs() {
		return this.updatedLibs ;
	}
}

class okAdapter implements java.awt.event.ActionListener {
	private ScenarioSettingsDialog adaptee;

	okAdapter(ScenarioSettingsDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.doOkay();
	}
}

class cancelAdapter implements java.awt.event.ActionListener {
	private ScenarioSettingsDialog adaptee;

	cancelAdapter(ScenarioSettingsDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.doCancel();
	}
}
