package gui.actions.edit;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.w3c.dom.events.EventException;

public class CloneAction extends EditAction {

	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 * @param icon
	 */
	public CloneAction(String name, Icon icon) {
		super(name, icon);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		try {
			super.actionPerformed(arg0);
		} catch (EventException e) {
			JOptionPane.showMessageDialog(this.gui.getFrame(), e.getMessage()
					.toString(), "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.edit.getCurrentProject().cloneSelected();
	}
}
