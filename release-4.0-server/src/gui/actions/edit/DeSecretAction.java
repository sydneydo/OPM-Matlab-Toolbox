package gui.actions.edit;

import gui.projectStructure.Entry;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.w3c.dom.events.EventException;

public class DeSecretAction extends EditAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public DeSecretAction(String name, Icon icon) {
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

		Iterator iter = Collections.list(
				edit.getCurrentProject().getSystemStructure().getElements())
				.iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();

			entry.setName(String.valueOf(entry.getId()));

			if (!entry.getDescription().equalsIgnoreCase("none")
					&& !entry.getDescription().equalsIgnoreCase(""))
				entry.setDescription(String.valueOf(entry.getId()));

			if (!entry.getUrl().equalsIgnoreCase("none")
					&& !entry.getUrl().equalsIgnoreCase(""))
				entry.setUrl(String.valueOf(entry.getId()));
		}

		edit.getCurrentProject().setCanClose(false);
		edit.getCurrentProject().getCurrentOpd().getDrawingArea().repaint() ; 

	}
}
