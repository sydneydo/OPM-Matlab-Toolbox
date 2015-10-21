package gui.actions.edit;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

/**
 * Carries out a copy operation.
 * 
 * @author Eran Toch
 */
public class RedoAction extends EditAction {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	public RedoAction(String name, Icon icon) {
		super(name, icon);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		this.edit.redo();
	}
}
