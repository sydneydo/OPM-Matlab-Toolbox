package gui.actions.file;

import gui.Opcat2;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JDialog;

/**
 * Creates a new project, and prompts the user for properties.
 * 
 * @author Eran Toch
 * @see gui.controls.file#createNewProject()
 */
public class NewProjectAction extends FileAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public NewProjectAction(String name, Icon icon) {
		super(name, icon);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (!this.file.handleOpenedSystem()) {
			return;
		}
		JDialog diag = new NewProjectDialog(Opcat2.getFrame());
		diag.setLocationRelativeTo(Opcat2.getFrame());
		diag.setVisible(true);
		// this.file.createNewProject();
	}
	/**
	* This method should return an instance of this class which does 
	* NOT initialize it's GUI elements. This method is ONLY required by
	* Jigloo if the superclass of this class is abstract or non-public. It 
	* is not needed in any other situation.
	 */
	public static Object getGUIBuilderInstance() {
		return new NewProjectAction(Boolean.FALSE);
	}
	
	/**
	 * This constructor is used by the getGUIBuilderInstance method to
	 * provide an instance of this class which has not had it's GUI elements
	 * initialized (ie, initGUI is not called in this constructor).
	 */
	public NewProjectAction(Boolean initGUI) {
		super();
	}
}
