package gui.actions.edit;

import java.awt.event.ActionEvent;

import gui.actions.OpcatAction;
import gui.controls.EditControl;
import gui.controls.GuiControl;

import javax.swing.Icon;
import org.w3c.dom.events.EventException;

/**
 * A super class for all editing-related actions. Allows access to the
 * EditControl class.
 * 
 * @author Eran Toch
 */
public abstract class EditAction extends OpcatAction {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	protected EditControl edit = EditControl.getInstance();

	protected GuiControl gui = GuiControl.getInstance();

	private String locName;

	private boolean showCutMessage = true;

	/**
	 * A general constructor.
	 * 
	 * @param name
	 *            The name of the action (e.g. "Save")
	 * @param icon
	 *            The icon of the action (presented by menus, toolbars etc)
	 */
	public EditAction(String name, Icon icon) {
		super(name, icon);
		this.locName = name;
	}

	public EditAction(String name, Icon icon, boolean showCutMessage) {
		super(name, icon);
		this.locName = name;
		this.showCutMessage = showCutMessage;
	}

	public EditAction() {
		super();
	}

	/**
	 * this function is executed evry time an Edit action is preforemed. it
	 * checks and does general things common to all Edit actions. (like checkin
	 * if aproject is loded before preforming the action
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) throws EventException {
		super.actionPerformed(arg0);

		if (!this.locName.equalsIgnoreCase("Paste")) {
			if (this.edit.IsCutPending() && showCutMessage) {
				short code = 2;
				throw new EventException(code, EditControl.CUT_PENDING_MSG);
			}
		}

	}
}
