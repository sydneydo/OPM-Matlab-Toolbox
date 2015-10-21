package gui.actions.svn;

import gui.actions.OpcatAction;

import java.awt.event.ActionEvent;
import javax.swing.Icon;

public class SvnAction extends OpcatAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A general constructor, initiates a name and an icon.
	 * 
	 * @param name
	 *            The name of the action (e.g. "Save")
	 * @param icon
	 *            The icon of the action (presented by menus, toolbars etc)
	 */
	public SvnAction(String name, Icon icon) {
		super(name, icon);
	}

	/**
	 * A general constructor, takes a name, but not an icon.
	 * 
	 * @param name
	 */
	public SvnAction(String name) {
		super(name);
	}

	/**
	 * A simple constructor - does not take a name.
	 */
	public SvnAction() {
		super();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
