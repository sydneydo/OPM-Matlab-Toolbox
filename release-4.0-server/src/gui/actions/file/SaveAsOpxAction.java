package gui.actions.file;

import gui.controls.GuiControl;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

/**
 * Perfoms a save-as operation, prompting the user for a file location.
 * 
 * @author Eran Toch
 * @see gui.controls.file#_saveAs()
 */
public class SaveAsOpxAction extends FileAction {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	public SaveAsOpxAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Thread runner = new Thread() {
			public void run() {
				SaveAsOpxAction.this.file._saveAs();
				GuiControl.getInstance().getRepository().getModelsView().repaintKeepOpen(); 
			}
		};

		runner.start();
	}

}
