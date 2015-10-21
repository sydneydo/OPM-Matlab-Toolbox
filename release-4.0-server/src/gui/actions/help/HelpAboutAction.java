package gui.actions.help;

import java.awt.event.ActionEvent;
import gui.AboutDialog;
import gui.controls.GuiControl;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * Opens the Help contents menu, through an external browser.
 * 
 * @author Eran Toch
 */
public class HelpAboutAction extends AbstractAction {
	 

	/**
	 * 
	 */
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A singleton class handling GUI in Opcat.
	 */
	protected GuiControl myGuiControl = GuiControl.getInstance();

	/**
	 * A general constructor, initiates a name and an icon.
	 * 
	 * @param name
	 *            The name of the action (e.g. "Save")
	 * @param icon
	 *            The icon of the action (presented by menus, toolbars etc)
	 */
	public HelpAboutAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {

		new AboutDialog();

	}

}
