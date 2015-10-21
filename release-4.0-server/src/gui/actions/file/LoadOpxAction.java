package gui.actions.file;

import gui.Opcat2;
import gui.controls.GuiControl;
import gui.opdProject.StateMachine;
import gui.util.CustomFileFilter;
import gui.util.OpcatFile;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;

/**
 * Loads a project from an OPX file, allowing the user to chose the file.
 * Updates last used files list, and recieves and updates the last directory.
 * 
 * @author Eran Toch
 * @see gui.controls.file#loadOpxFile()
 */
public class LoadOpxAction extends FileAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public LoadOpxAction(String name, Icon icon) {
		super(name, icon);
	}

	public void open(OpcatFile file) {

		if (!this.file.handleOpenedSystem()) {
			return;
		}

		String ld = file.getParent();
		LoadOpxAction.this.file.setLastDirectory(ld);
		StateMachine.setWaiting(true);
		GuiControl.getInstance().setCursor4All(Cursor.WAIT_CURSOR);

		String fileName = file.getAbsolutePath();
		Opcat2.setLoadedfilename(fileName);
		LoadOpxAction.this.file.loadOpxFile(fileName, true);

	}

	public void actionPerformed(ActionEvent arg0) {
		if (!this.file.handleOpenedSystem()) {
			return;
		}
		Thread runner = new Thread() {
			public void run() {

				JFileChooser myFileChooser = new JFileChooser();
				myFileChooser.setSelectedFile(new File(""));
				myFileChooser.resetChoosableFileFilters();

				// Setting the last directory
				String ld = LoadOpxAction.this.file.getLastDirectory();
				if (!ld.equals("")) {
					myFileChooser.setCurrentDirectory(new File(ld));
				}
				String[] exts = { "opx", "opz" };
				CustomFileFilter myFilter = new CustomFileFilter(exts,
						"Opcat2 Files");
				myFileChooser.addChoosableFileFilter(myFilter);

				int retVal = myFileChooser.showDialog(Opcat2.getFrame(),
						"Load System");

				if (retVal != JFileChooser.APPROVE_OPTION) {
					return;
				}
				ld = myFileChooser.getSelectedFile().getParent();
				LoadOpxAction.this.file.setLastDirectory(ld);
				StateMachine.setWaiting(true);
				GuiControl.getInstance().setCursor4All(Cursor.WAIT_CURSOR);

				String fileName = myFileChooser.getSelectedFile().getPath();
				Opcat2.setLoadedfilename(fileName);
				LoadOpxAction.this.file.loadOpxFile(fileName, true);

			}

		};

		runner.start();

	}

}
