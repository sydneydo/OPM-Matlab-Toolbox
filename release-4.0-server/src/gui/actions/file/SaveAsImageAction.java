package gui.actions.file;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

/**
 * Saves the current OPD as a JPG image.
 * 
 * @author Eran Toch
 * @see gui.controls.file#saveAsImage()
 */
public class SaveAsImageAction extends FileAction {
	 

	public SaveAsImageAction(String name, Icon icon) {
		super(name, icon);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		super.actionPerformed(arg0);
		this.file.saveAsImage();

	}

}
