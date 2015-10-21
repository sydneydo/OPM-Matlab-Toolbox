package gui.actions;

import java.awt.event.ActionEvent;

import gui.controls.EditControl;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.w3c.dom.events.EventException;

public abstract class OpcatAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpcatAction(String name, Icon icon) {
		super(name, icon);
	}

	public OpcatAction() {
		super();
	}

	public OpcatAction(String name) {
		super(name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!EditControl.getInstance().isProjectOpened()) {
			short code = 1;
			throw new EventException(code,
					"This Action can not be executed if there is no open model");
		}
	}

}
