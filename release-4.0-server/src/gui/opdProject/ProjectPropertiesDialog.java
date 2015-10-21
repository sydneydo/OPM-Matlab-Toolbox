package gui.opdProject;

import expose.OpcatExposeEntity;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import gui.Opcat2;
import gui.images.misc.MiscImages;
import gui.metaLibraries.dialogs.LibrariesImportsDialog;
import gui.projectStructure.Entry;
import gui.util.OpcatLogger;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;

//end of by eran's imports

/**
 * This class is a Properties Dialog for Project. It displays such information
 * as author, name, creation date of the project and gives change them,
 * excepting creation date. Furthermore, the dialog allows allows authors to add
 * requirements and implementations information in free text to the system.
 * Afterwards, the information is processed by the <code>Documents</code> tool.
 * 
 * @see extensionTools#Documents
 */
public class ProjectPropertiesDialog extends JDialog {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
         * 
         */

	private final int NUM_OF_ENTRIES = 11;

	JTabbedPane tabs = new JTabbedPane();

	JPanel advanced = new JPanel();

	JPanel basic = new JPanel();

	JButton cancelButton = new JButton();

	JButton okButton = new JButton();

	private JTextField name = new JTextField();

	private JTextField creator = new JTextField();

	private JComboBox type;

	// private JTextField client = new JTextField();
	private JTextField date = new JTextField();

	private JLabel jLabel1 = new JLabel();

	private JLabel jLabel2 = new JLabel();

	private JLabel jLabel3 = new JLabel();

	private JLabel jLabel4 = new JLabel();

	private JLabel jLabelType = new JLabel();

	// Holds an aray of GenInfo entries
	private GenInfoEntry[] informations;

	// Holds the current GenInfoEntry that the focus is on
	GenInfoEntry current = null;

	OpdProject currentProject = null;

	private boolean okPressed;

	private JLabel genInfoLabels = new JLabel();

	private JLabel fieldLabel = new JLabel();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private JTextArea textarea = new JTextArea();

	private JScrollPane jScrollPane2 = new JScrollPane();

	private JList genList = new JList();

	JLabel logoLabel = new JLabel(MiscImages.LOGO_BIG_ICON);

	private JFrame parentFrame;

	// private JScrollPane scrollPane = new JScrollPane();

	/**
	 * Contains a dialog in which the user can select the libraries to import.
	 * 
	 * @author Eran Toch
	 */
	private LibrariesImportsDialog importedLibraries;

	// private LibrariesImportsDialog importedReq;

	/**
	 * Constructor.<br>
	 * Constructs and shows <code>ProjectPropertiesDialog</code>.
	 * 
	 * @param
	 *        <code>currProject <a href = "OpdProject.html">OpdProject</a><code>, the project to show properties for.
	 * @param <code>parent JFrame</code>, the <code>Frame</code> from which the
	 *        dialog is displayed.
	 */
	public ProjectPropertiesDialog(OpdProject currProject, JFrame parent,
			String title) {
		super(parent, title, true);
		this.currentProject = currProject;
		this.parentFrame = parent;
		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * An empty Constructor.<br>
	 */
	public ProjectPropertiesDialog() {
		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Builds and constructs the dialog. THe method initialize all the dialog
	 * variables (name, creator, date and all the general informaiton variables.
	 * The general information variables are hard coded into the code in this
	 * method. The code should be updated if any change should be done to the
	 * list.
	 * 
	 * @see GenInfoEntry
	 */
	private void jbInit() throws Exception {
		// A list of all the entries.
		this.informations = new GenInfoEntry[this.NUM_OF_ENTRIES];
		this.informations[0] = new GenInfoEntry("System_Overview",
				"System Overview", this.currentProject
						.getInfoValue("System_Overview"));
		this.informations[1] = new GenInfoEntry(
				"Goals_and_Objectives_of_the_Project", "Goals and Objectives",
				this.currentProject
						.getInfoValue("Goals_and_Objectives_of_the_Project"));
		this.informations[2] = new GenInfoEntry(
				"Possible_Users_for_the_System", "Possible Users",
				this.currentProject
						.getInfoValue("Possible_Users_for_the_System"));
		this.informations[3] = new GenInfoEntry(
				"Hardware_and_Software_Requirements", "Hardware and Software",
				this.currentProject
						.getInfoValue("Hardware_and_Software_Requirements"));
		this.informations[4] = new GenInfoEntry("Inputs_Processing",
				"Input / Output Definition", this.currentProject
						.getInfoValue("Inputs_Processing"));
		this.informations[5] = new GenInfoEntry("Future_Goals", "Future Goals",
				this.currentProject.getInfoValue("Future_Goals"));
		this.informations[6] = new GenInfoEntry("Operation_and_Maintenance",
				"Operation and Maintenance", this.currentProject
						.getInfoValue("Operation_and_Maintenance"));
		this.informations[7] = new GenInfoEntry("Problems", "Problems",
				this.currentProject.getInfoValue("Problems"));
		this.informations[8] = new GenInfoEntry("The_Current_State",
				"The Current State", this.currentProject
						.getInfoValue("The_Current_State"));
		this.informations[9] = new GenInfoEntry("Open_Issues", "Open Issues",
				this.currentProject.getInfoValue("Open_Issues"));
		this.informations[10] = new GenInfoEntry("Client", "Client",
				this.currentProject.getInfoValue("Client"));

		// Setting the first choice of the general information
		this.current = this.informations[0];

		// Init of the regular state variables
		// client = new JTextField(currentProject.getInfoValue("Client"));
		this.creator = new JTextField(this.currentProject.getCreator());
		this.name = new JTextField(this.currentProject.getName());
		this.date = new JTextField(this.currentProject.getCreationDate()
				.toString());
		this.date.setEditable(false);

		ArrayList typesArray = currentProject.getModelTypesArrayList();
		Collections.sort(typesArray);
		Object[] types = typesArray.toArray();
		this.type = new JComboBox(types);
		this.type.setSelectedItem(currentProject.getCurrentModelType());

		this.type.setEditable(false);

		this.okButton
				.addActionListener(new ProjectPropertiesDialog_okButton_actionAdapter(
						this));
		this.cancelButton
				.addActionListener(new ProjectPropertiesDialog_cancelButton_actionAdapter(
						this));
		this.genInfoLabels.setToolTipText("");
		this.genInfoLabels.setText("General Information Fields:");
		this.genInfoLabels.setBounds(new Rectangle(16, 13, 178, 22));
		this.fieldLabel.setBounds(new Rectangle(223, 13, 187, 20));
		this.jScrollPane1
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.jScrollPane1.setBounds(new Rectangle(230, 48, 185, 156));
		this.textarea.setLineWrap(true);
		this.textarea.setText(this.informations[0].getValue());
		this.jScrollPane2
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.jScrollPane2.setBounds(new Rectangle(15, 48, 194, 156));

		this.genList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.genList
				.addListSelectionListener(new ProjectPropertiesDialog_genList_listSelectionAdapter(
						this));
		this.logoLabel.setBounds(new Rectangle(25, 42, 39, 43));

		this.basic.add(this.creator, null);
		// basic.add(client, null);
		this.basic.add(this.date, null);
		this.basic.add(this.logoLabel, null);
		this.basic.add(this.name, null);
		this.basic.add(this.type);

		this.basic.add(this.jLabel3, null);
		this.basic.add(this.jLabel1, null);
		this.basic.add(this.jLabel2, null);
		this.basic.add(this.jLabel4, null);
		this.basic.add(this.jLabelType, null);
		this.tabs.add(this.basic, " Basic ");
		this.tabs.add(this.advanced, " Advanced ");

		// by eran toch - Handling import tab
		this.importedLibraries = new LibrariesImportsDialog(this.currentProject
				.getMetaManager(), this.currentProject, this.parentFrame);
		this.tabs.add(this.importedLibraries, " Libraries ");

		// importedReq = new
		// LibrariesImportsDialog(currentProject.getMetaManager(),
		// currentProject, parentFrame);
		// tabs.add(importedReq, " Requirements ");

		this.jLabelType.setText("Model Type:");
		this.jLabelType.setBounds(new Rectangle(84, 160, 119, 25));
		this.jLabel4.setText("Creation Date:");
		this.jLabel4.setBounds(new Rectangle(84, 120, 119, 25));
		// jLabel3.setText("System Client:");
		// jLabel3.setBounds(new Rectangle(84, 120, 119, 25));
		this.jLabel2.setText("System Creator:");
		this.jLabel2.setBounds(new Rectangle(84, 80, 119, 25));
		this.jLabel1.setText("System Name:");
		this.jLabel1.setBounds(new Rectangle(84, 42, 119, 25));
		this.getContentPane().setLayout(null);
		this.advanced.setLayout(null);
		this.tabs.setBounds(new Rectangle(3, 4, 435, 258));
		this.cancelButton.setBounds(new Rectangle(343, 278, 96, 28));
		this.cancelButton.setText("Cancel");
		this.okButton.setBounds(new Rectangle(235, 278, 96, 28));
		this.okButton.setText("OK");
		this.basic.setLayout(null);
		this.name.setBounds(new Rectangle(220, 42, 167, 25));
		this.creator.setBounds(new Rectangle(220, 80, 167, 25));
		// client.setBounds(new Rectangle(220, 120, 167, 25));
		this.date.setEditable(false);
		this.date.setBounds(new Rectangle(220, 120, 167, 25));
		this.type.setBounds(220, 160, 167, 25);
		this.getContentPane().add(this.tabs, null);
		this.advanced.add(this.genInfoLabels, null);
		this.advanced.add(this.jScrollPane1, null);
		this.advanced.add(this.fieldLabel, null);
		this.advanced.add(this.jScrollPane2, null);
		this.jScrollPane2.getViewport().add(this.genList, null);
		this.jScrollPane1.getViewport().add(this.textarea, null);
		this.getContentPane().add(this.cancelButton, null);
		this.getContentPane().add(this.okButton, null);
		this.setBounds(0, 0, 460, 360);
		this.setLocationRelativeTo(this.getParent());
		this.genList.setListData(this.informations);
		this.genList.setSelectedIndex(0);
	}

	/**
	 * Shows the dialog.
	 * 
	 * @return <code>true</code> if the OK was pressed.
	 */
	public boolean showDialog() {
		this.setVisible(true);
		if (Opcat2.getCurrentProject() != null)
			Opcat2.getCurrentProject().setCanClose(false);
		return this.isOkPressed();
	}

	/**
	 * Shows the dialog at the meta-libraries state.
	 * 
	 * @return <code>true</code> if the OK was pressed.
	 */
	public boolean showDialogAtLibraries() {
		this.tabs.setSelectedComponent(this.importedLibraries);
		return this.showDialog();
	}

	/**
	 * Shows the dialog at the meta-libraries state.
	 * 
	 * @return <code>true</code> if the OK was pressed.
	 */
	public boolean showDialogAtLibrariesClasification() {
		this.tabs.setSelectedComponent(this.importedLibraries);
		return this.showDialog();
	}

	/**
	 * Changes the selection of the general information entry. The method saves
	 * the last entry value in the <code>informations</code> array. Afterwards,
	 * the method changes the <code>textarea</code> input field, according to
	 * the selection.
	 * 
	 * @see #informations
	 */
	void genList_valueChanged(ListSelectionEvent e) {
		// Getting the current selection
		this.saveCurrentInfo();

		// changing the textarea to reflect the change
		this.current = (GenInfoEntry) this.genList.getSelectedValue();
		if (this.current == null) {
			return;
		}
		this.textarea.setText(this.current.getValue());
		this.fieldLabel.setText(this.current.toString() + ":");
	}

	private void saveCurrentInfo() {
		if (this.current != null) {
			GenInfoEntry old = this.searchEntry(this.current.getKey());
			if (old != null) {
				if ((this.textarea.getText() != null)
						&& !this.textarea.getText().equals("")) {
					old.setValue(this.textarea.getText());
				}
			}
		}
		return;
	}

	/**
	 * Returns the GenInfoEntry, according to the given key.
	 */
	GenInfoEntry searchEntry(String key) {
		for (int i = 0; i < this.informations.length; i++) {
			if (this.informations[i].getKey().equals(key)) {
				return this.informations[i];
			}
		}
		return null;
	}

	void okButton_actionPerformed(ActionEvent e) {
		this.doOK();
	}

	void doOK() {
		this.saveCurrentInfo();
		try {
			if (!this.currentProject.getName().equalsIgnoreCase(
					this.name.getText())) {
				for (OpcatExposeEntity i : currentProject.getExposeManager()
						.getExposedList(null, true)) {
					// if (i.getKey().isPrivate()) {
					Entry entry = currentProject.getSystemStructure().getEntry(
							i.getKey().getOpmEntityID());
					if (entry != null) {
						if (entry.getLogicalEntity().isPublicExposed()) {
							currentProject.getExposeManager()
									.addPublicExposeChange(
											entry.getInstances().nextElement(),
											OPCAT_EXPOSE_OP.UPDATE);
						}
						if (entry.getLogicalEntity().isPrivateExposed()) {
							currentProject.getExposeManager()
									.addPrivateExposeChange(
											entry.getInstances().nextElement(),
											OPCAT_EXPOSE_OP.UPDATE);
						}
					}
					// }
				}

			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		this.currentProject.setName(this.name.getText());
		this.currentProject.setCreator(this.creator.getText());
		this.currentProject.setModelType((String) this.type.getSelectedItem());
		if (currentProject.getPath() != null) {
			Opcat2.getFrame().setTitle(
					"Opcat II - " + currentProject.getName() + " : "
							+ currentProject.getPath());
		} else {
			Opcat2.getFrame().setTitle(
					"Opcat II - " + currentProject.getName() + " : "
							+ "warning - not saved yet");
		}
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		this.currentProject.setInfoValue("Client", "");
		GenInfoEntry entry;
		for (int i = 0; i < this.informations.length; i++) {
			entry = this.informations[i];
			if ((entry != null) & (entry.getKey() != null)
					&& (entry.getValue() != null)) {
				this.currentProject.setInfoValue(entry.getKey(), entry
						.getValue());
			}
		}
		this.okPressed = true;
		this.dispose();
	}

	/**
	 * Returns the meta Copies the list of references to the local
	 * <code>importedLibs</code> variable.
	 * 
	 * @author Eran Toch
	 */
	public Vector getMetaLibraries() {
		return this.importedLibraries.getMetaManager().getVectorClone();
	}

	public boolean isOkPressed() {
		return this.okPressed;
	}

	void cancelButton_actionPerformed(ActionEvent e) {
		this.okPressed = false;
		this.dispose();
	}
}

class ProjectPropertiesDialog_okButton_actionAdapter implements
		java.awt.event.ActionListener {
	private ProjectPropertiesDialog adaptee;

	ProjectPropertiesDialog_okButton_actionAdapter(
			ProjectPropertiesDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.okButton_actionPerformed(e);
	}
}

class ProjectPropertiesDialog_cancelButton_actionAdapter implements
		java.awt.event.ActionListener {
	private ProjectPropertiesDialog adaptee;

	ProjectPropertiesDialog_cancelButton_actionAdapter(
			ProjectPropertiesDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.cancelButton_actionPerformed(e);
	}
}

class ProjectPropertiesDialog_genList_listSelectionAdapter implements
		javax.swing.event.ListSelectionListener {
	ProjectPropertiesDialog adaptee;

	ProjectPropertiesDialog_genList_listSelectionAdapter(
			ProjectPropertiesDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void valueChanged(ListSelectionEvent e) {
		this.adaptee.genList_valueChanged(e);
	}
}