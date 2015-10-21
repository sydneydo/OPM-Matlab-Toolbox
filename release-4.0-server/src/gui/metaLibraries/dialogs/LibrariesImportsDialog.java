package gui.metaLibraries.dialogs;

import gui.dataProject.DataCreatorType;
import gui.dataProject.DataProject;
import gui.metaLibraries.logic.MetaException;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.MetaManager;
import gui.opdProject.OpdProject;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A dialog for library reference import selection. The dialog enable users to
 * manage their referene library.
 * 
 * @author Eran Toch
 * @version 1.0
 */

public class LibrariesImportsDialog extends JPanel {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
         * 
         */

	JList importedList = new JList();

	JLabel jLabel1 = new JLabel();

	JButton add = new JButton();

	JButton edit = new JButton();

	JButton remove = new JButton();

	JScrollPane importedScroll = new JScrollPane();

	protected MetaManager metaManager = null;

	protected OpdProject currentProject = null;

	protected JFrame parentFrame = null;

	private boolean showIsPolicyCheckBox = false;

	public LibrariesImportsDialog(MetaManager meta, OpdProject _project,
			JFrame parent) throws Exception {
		this.metaManager = (MetaManager) meta.clone();
		this.currentProject = _project;
		this.parentFrame = parent;
		this.showIsPolicyCheckBox = false;
		if (this.metaManager == null) {
			throw new Exception("Templates Manager is null");
		}
		try {
			this.jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public LibrariesImportsDialog(MetaManager meta, OpdProject _project,
			JFrame parent, boolean showIsPolicyCheckBox) throws Exception {

		this.metaManager = (MetaManager) meta.clone();
		this.currentProject = _project;
		this.parentFrame = parent;
		this.showIsPolicyCheckBox = showIsPolicyCheckBox;
		if (this.metaManager == null) {
			throw new Exception("Templates Manager is null");
		}
		try {
			this.jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setLayout(null);

		Rectangle firstButtonPosition = new Rectangle(313, 54, 100, 29);
		Rectangle secondButtonPosition = new Rectangle(313, 97, 100, 29);
		Rectangle thirdButtonPosition = new Rectangle(313, 140, 100, 28);
		this.importedList.setBorder(BorderFactory.createEtchedBorder());
		this.jLabel1.setPreferredSize(new Dimension(124, 15));
		this.jLabel1.setText(this.getTitleText());
		this.jLabel1.setBounds(new Rectangle(24, 26, 350, 19));
		this.add.setBounds(firstButtonPosition);
		this.add.setPreferredSize(new Dimension(70, 23));
		this.add.setRequestFocusEnabled(true);
		this.add.setText("Add...");
		this.add.addActionListener(new OntologyImportsDialog_add_actionAdapter(
				this));
		this.edit.setBounds(secondButtonPosition);
		this.edit.setPreferredSize(new Dimension(60, 23));
		this.edit.setText("Edit...");
		this.edit
				.addActionListener(new OntologyImportsDialog_edit_actionAdapter(
						this));
		this.remove.setBounds(thirdButtonPosition);
		this.remove.setMaximumSize(new Dimension(60, 23));
		this.remove.setMinimumSize(new Dimension(60, 23));
		this.remove.setPreferredSize(new Dimension(60, 23));
		this.remove.setMnemonic('0');
		this.remove.setText("Remove");
		this.remove
				.addActionListener(new OntologyImportsDialog_remove_actionAdapter(
						this));
		this.setOpaque(true);
		this.importedScroll.setBounds(new Rectangle(24, 54, 276, 157));
		this.add(this.jLabel1, null);
		if (this.displayAddButton()) {
			this.add(this.add, null);
		} else {
			this.edit.setBounds(firstButtonPosition);
			this.remove.setBounds(secondButtonPosition);
		}
		this.add(this.edit, null);
		this.add(this.remove, null);
		this.add(this.importedScroll, null);
		this.importedScroll.getViewport().add(this.importedList, null);

		// Adding current ontologies to the list
		PathObject[] references = this.getPathsArray(this.getLibReferences());
		this.importedList.setListData(references);
	}

	/**
	 * Returns a Vector containing the Libraries to be displayed in the list.
	 */
	protected Vector getLibReferences() {
		return this.metaManager.getDisplayLibraries();
	}

	/**
	 * The method decides if to display an "add" button.
	 * 
	 * @return <code>true</code> if the button should be displayed.
	 *         <code>false</code> if not.
	 */
	protected boolean displayAddButton() {
		return true;
	}

	/**
	 * Returns the text that would be displayed to the user, above the libraries
	 * list.
	 * 
	 * @return
	 */
	protected String getTitleText() {
		return "Imported Templates:";
	}

	/**
	 * Builds an array containing the paths.
	 * 
	 * @param libsList
	 *            The Vector of <code>MetaLibrary</code> objects
	 * @return
	 */
	private PathObject[] getPathsArray(Vector libsList) {
		PathObject[] references = new PathObject[libsList.size()];
		MetaLibrary runner = null;
		for (int i = 0; i < libsList.size(); i++) {
			runner = (MetaLibrary) libsList.get(i);
			if (runner != null) {
				PathObject pathObj = new PathObject(runner);
				references[i] = pathObj;
			}
		}
		return references;
	}

/**
         * Retruns the
         * {@link MetaManager) of the current dialog. The dialog creates a  copy
         * of the project's <code>MetaManager</code> and changes the copy.
         * When closing the dialog, the <code>MetaManager</code> can be
         * returned in order to change the original one.
         * 
         * @return The local copy of the <code>MetaManager</code>.
         */
	public MetaManager getMetaManager() {
		return this.metaManager;
	}

	/**
	 * Adding a new library to the system. The new library is added using the
	 * <code>LibraryLocationDialog</code> which allows users to browse for
	 * libraries using a file chooser or a URL.
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void add_actionPerformed(ActionEvent e) {
		LibraryLocationDialog addLocation = new LibraryLocationDialog(
				this.parentFrame, "",
				DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE,
				showIsPolicyCheckBox, false);
		addLocation.setTitle("Location of Imported Template");
		HashMap newRef = addLocation.showDialog();
		if (newRef != null) {
			String path = (String) newRef.get("path");
			int type = ((Integer) newRef.get("type")).intValue();
			boolean isPolicy = ((Boolean) newRef.get("policy")).booleanValue();

			MetaLibrary newLib;
			if (isPolicy) {
				newLib = this.metaManager.createNewMetaLibraryReference(
						MetaLibrary.TYPE_POLICY, path,
						DataCreatorType.DATA_TYPE_OPCAT_LIBRARAY, type);
			} else {
				newLib = this.metaManager.createNewMetaLibraryReference(
						MetaLibrary.TYPE_CLASSIFICATION, path,
						DataCreatorType.DATA_TYPE_OPCAT_FILE_OPZ, type);

			}

			try {
				this.metaManager.addMetaLibrary(newLib);
				this.currentProject.setCanClose(false);
			} catch (MetaException E) {
				JOptionPane.showMessageDialog(this, E.getMessage(), "Message",
						JOptionPane.ERROR_MESSAGE);
			}
			PathObject[] references = this.getPathsArray(this.metaManager
					.getDisplayLibraries());
			this.importedList.setListData(references);
			this.importedList.repaint();
		}
	}

	/**
	 * Edits a selected imported library reference. The selected library is
	 * replaced with the one defined by the user if the new values are different
	 * from existing ones.
	 * 
	 * @param e
	 */
	public void edit_actionPerformed(ActionEvent e) {
		if (this.importedList.getSelectedValue() != null) {
			PathObject selectedPathValue = (PathObject) this.importedList
					.getSelectedValue();
			MetaLibrary edited = this.metaManager
					.findByReferenceID(selectedPathValue.getMeta().getID());
			if (edited == null) {
				return;
			}
			LibraryLocationDialog addLocation = new LibraryLocationDialog(
					this.parentFrame, edited.getPath(), edited
							.getReferenceType(), edited.isPolicy(), edited
							.isPolicy());
			addLocation.setTitle("Location of Imported Template");
			HashMap ref = addLocation.showDialog();
			if (ref != null) {
				String path = (String) ref.get("path");
				int type = ((Integer) ref.get("type")).intValue();
				// If the values are different, the library state is changed
				if ((!edited.getPath().equals(path))
						|| (edited.getReferenceType() != type)) {
					try {
						this.metaManager.changeLibraryState(edited.getID(),
								path, type);
					} catch (MetaException E) {
						JOptionPane.showMessageDialog(this, E.getMessage(),
								"Message", JOptionPane.ERROR_MESSAGE);
					}
				}
				PathObject[] references = this.getPathsArray(this.metaManager
						.getDisplayLibraries());
				this.importedList.setListData(references);
				this.importedList.repaint();
			}
		}
	}

	/**
	 * Removes an ontology reference from the list.
	 */
	public void remove_actionPerformed(ActionEvent e) {
		if (this.importedList.getSelectedValue() != null) {
			PathObject selectedPathValue = (PathObject) this.importedList
					.getSelectedValue();
			MetaLibrary candidate = this.metaManager
					.findByReferenceID(selectedPathValue.getMeta().getID());
			// String selectedValue =
			// (String)importedList.getSelectedValue();
			try {
				if (this.metaManager.projectHasRolesOfLib(candidate,
						this.currentProject)) {
					int retValue = 0;
					retValue = JOptionPane
							.showConfirmDialog(
									this.parentFrame,
									"The model contains usage of the Template that was removed\n"
											+ "Are you sure you want to remove the Template?",
									"Template remove",
									JOptionPane.YES_NO_OPTION);
					if (retValue == JOptionPane.NO_OPTION) {
						return;
					}
				}
				this.metaManager.markForRemoval(selectedPathValue.getMeta()
						.getID());
			} catch (MetaException E) {
				JOptionPane.showMessageDialog(this, E.getMessage(), "Message",
						JOptionPane.ERROR_MESSAGE);
			}
			PathObject[] references = this.getPathsArray(this.metaManager
					.getDisplayLibraries());
			this.importedList.setListData(references);
			this.importedList.repaint();
		}
	}

} // End of class

class OntologyImportsDialog_add_actionAdapter implements
		java.awt.event.ActionListener {
	LibrariesImportsDialog adaptee;

	OntologyImportsDialog_add_actionAdapter(LibrariesImportsDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.add_actionPerformed(e);
	}
}

class OntologyImportsDialog_remove_actionAdapter implements
		java.awt.event.ActionListener {
	LibrariesImportsDialog adaptee;

	OntologyImportsDialog_remove_actionAdapter(LibrariesImportsDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.remove_actionPerformed(e);
	}
}

class OntologyImportsDialog_edit_actionAdapter implements
		java.awt.event.ActionListener {
	LibrariesImportsDialog adaptee;

	OntologyImportsDialog_edit_actionAdapter(LibrariesImportsDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.edit_actionPerformed(e);
	}
}
