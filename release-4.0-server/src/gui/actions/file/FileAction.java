package gui.actions.file;

import gui.actions.OpcatAction;
import gui.controls.FileControl;
import gui.controls.GuiControl;

import javax.swing.Icon;

/**
 * A superclass for file-related actions of Opcat. All operations that access
 * the file system can inherit from this class, and have access to file
 * operations.
 * 
 * @author Eran Toch
 */
public abstract class FileAction extends OpcatAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * A singleton class handling file operation.
	 */
	protected FileControl file = FileControl.getInstance();
	
	protected GuiControl gui = GuiControl.getInstance() ; 

	/**
	 * A general constructor, initiates a name and an icon.
	 * 
	 * @param name
	 *            The name of the action (e.g. "Save")
	 * @param icon
	 *            The icon of the action (presented by menus, toolbars etc)
	 */
	public FileAction(String name, Icon icon) {
		super(name, icon);
	}

	public FileAction() {
		super();
	}
	
	public FileAction(String name) {
		super(name);
	}

}
