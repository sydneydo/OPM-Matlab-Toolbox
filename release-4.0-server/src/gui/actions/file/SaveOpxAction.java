package gui.actions.file;

import gui.controls.GuiControl;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

/**
 * Perfoms a save operation, saving the file in the last location which it was
 * saved. Before that, it performs an OpcatTeam operation.
 * 
 * @author Eran Toch
 * @see gui.controls.file#_save()
 */
public class SaveOpxAction extends FileAction {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	public SaveOpxAction(String name, Icon icon) {
		super(name, icon);		
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		Thread runner = new Thread() {
			public void run() {
				SaveOpxAction.this.file._save(true);
				GuiControl.getInstance().getRepository().getModelsView().repaintKeepOpen(); 
			}
		};

		runner.start();

	}

}
