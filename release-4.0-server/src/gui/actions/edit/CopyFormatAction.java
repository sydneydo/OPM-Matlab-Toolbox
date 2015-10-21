package gui.actions.edit;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.w3c.dom.events.EventException;

public class CopyFormatAction extends EditAction {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	public CopyFormatAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent arg0) throws EventException {
		try {
			super.actionPerformed(arg0);
		} catch (EventException e) {
			JOptionPane.showMessageDialog(this.gui.getFrame(), e.getMessage()
					.toString(), "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.edit.getCurrentProject().setCopyFormatThing(
				this.edit.getCurrentProject().getCurrentOpd().getSelectedItem());
	}

}
