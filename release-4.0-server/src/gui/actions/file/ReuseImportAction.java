package gui.actions.file;

import extensionTools.reuse.ImportDialog;
import extensionTools.reuse.ReuseCommand;
import gui.Opcat2;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.OpdProject;
import gui.opx.LoadException;
import gui.util.OpcatException;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 * Opens the import dialog, which imports a model into the existing model, using
 * the <code>reuse</code> package.
 * 
 * @author Eran Toch
 * @see extensionTools.reuse.ImportDialog
 */
public class ReuseImportAction extends FileAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private int typeOfReuse = ReuseCommand.NO_REUSE;

	private String importFileName = "";

	private MetaLibrary myMeta = null;

	private boolean showConnectType = false;

	public ReuseImportAction(String name, Icon icon, MetaLibrary meta) {
		super(name, icon);
		myMeta = meta;
		showConnectType = true;
	}

	public ReuseImportAction(String name, Icon icon) {
		super(name, icon);
		showConnectType = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		super.actionPerformed(arg0);

		ImportDialog dialog = new ImportDialog(this.file
				.getCurrentProject(), this.gui.getDesktop(),
				showConnectType);
		dialog.setVisible(true);
		if (!dialog.isOperated()) {
			return;
		}
		this.typeOfReuse = dialog.getTypeOfReuse();
		this.importFileName = dialog.getImportFileName();
		runImport();
	}

	private void runImport() {
		Thread runner = new Thread() {
			public void run() {

				if (Opcat2.getCurrentProject() != null)
					Opcat2.getCurrentProject().setCanClose(true);
				ReuseImportAction.this.file._save(false);
				ReuseImportAction.this.gui.startWaitingMode(
						"Importing Model...", true);
				try {
					ReuseImportAction.this.doImport();
					ReuseImportAction.this.file.reloadProject();
				} catch (OpcatException e) {
					ReuseImportAction.this.gui.stopWaitingMode();
					OpcatLogger.logError(e);
					JOptionPane.showMessageDialog(
							ReuseImportAction.this.file
									.getCurrentProject().getMainFrame(),
							"Import had failed becasue of the following error:\n"
									+ e.getMessage(), "Message",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					ReuseImportAction.this.gui.stopWaitingMode();
				}
				Opcat2.getCurrentProject().setCanClose(false);
			}

		};
		runner.start();
	}

	public void doImport(String fileName) throws OpcatException {
		importFileName = fileName;
		runImport();
	}

	public void doImport() throws OpcatException {
		OpdProject reusedProject = null;
		try {
			reusedProject = this.file.loadOpxFileIntoProject(
					this.importFileName, null);
		} catch (LoadException e) {
			throw new OpcatException("Imported project was not loaded\n"
					+ e.getMessage());
		}
		if (reusedProject == null) {
			throw new OpcatException(
					"Imported project is null. Check the file name and try again\n"
							+ this.importFileName);
		}
		OpdProject curr = this.file.getCurrentProject();
		int reuseType = ReuseCommand.NO_REUSE;
		if (this.typeOfReuse == 0) {
			reuseType = ReuseCommand.SIMPLE_WHITE;
		}

		if (this.typeOfReuse == 1) {
			reuseType = ReuseCommand.SIMPLE_BLACK;
		}
		// if (this.typeOfReuse == 2) {
		// // check if the current OPD is already open reused
		// // if so don't allow open reuse
		// if (curr.getCurrentOpd().isOpenReused()) {
		// throw new OpcatException(
		// "The current Opd is already open reused");
		// }
		// // check if the current opd is already open reused
		// // in the case of open reuse we need to
		// // mark the system as open reused
		// // mark the Current Opd as open reused and specify the path of
		// // hte
		// // file
		// reuseType = ReuseCommand.OPEN;
		//
		// curr.setProjectType(OpdProject.OPEN_REUSED_SYSTEM);
		// curr.addopenResuedOpdToList(curr.getCurrentOpd(),
		// this.importFileName);
		// curr.getCurrentOpd().setOpenReused(true);
		// curr.getCurrentOpd().setReusedSystemPath(this.importFileName);
		// }

		if ((this.typeOfReuse > 2) || (reusedProject == null)) {
			throw new OpcatException("Reuse type was not determined");
		}
		ReuseCommand command;
		if (myMeta == null) {
			command = new ReuseCommand(curr, reusedProject, reuseType);
		} else {
			command = new ReuseCommand(curr, myMeta);
		}
		try {
			command.commitReuse();
		} catch (OpcatException e1) {
			throw e1;
		} finally {
			reusedProject.close();
			// reusedProject = null ;
		}
		return;
	}

}