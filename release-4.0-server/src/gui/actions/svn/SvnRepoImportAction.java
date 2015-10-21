package gui.actions.svn;

import gui.Opcat2;
import gui.controls.GuiControl;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JFileChooser;

import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.SVNURL;
import user.OpcatUser;

public class SvnRepoImportAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SVNURL entry;

	public SvnRepoImportAction(SVNURL entry, String name, Icon icon) {
		super(name, icon);
		this.entry = entry;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		super.actionPerformed(e);
		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		try {
			JFileChooser myFileChooser = new JFileChooser();
			myFileChooser.setSelectedFile(new File(""));
			myFileChooser.resetChoosableFileFilters();
			myFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int retVal = myFileChooser.showOpenDialog(Opcat2.getFrame());
			if (retVal == JFileChooser.APPROVE_OPTION) {
				OpcatMCManager.getInstance().doImport(
						myFileChooser.getSelectedFile(),
						entry,
						"Importing from Opcat user - "
								+ OpcatUser.getCurrentUser().getName());
				GuiControl.getInstance().getRepository().getModelsView()
						.repaintKeepOpen();
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		Opcat2.getGlassPane().setVisible(false);
		Opcat2.getGlassPane().stop();

	}
}
