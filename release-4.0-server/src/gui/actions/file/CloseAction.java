package gui.actions.file;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

/**
 * Closes the current project.
 * 
 * @see gui.controls.FileControl#closeSystem()
 * @author Eran Toch
 */
public class CloseAction extends FileAction {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	public CloseAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent arg0) {
		this.file.closeSystem();
	}

}
