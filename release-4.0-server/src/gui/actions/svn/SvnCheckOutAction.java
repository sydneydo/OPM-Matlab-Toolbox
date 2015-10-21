package gui.actions.svn;

import gui.Opcat2;
import gui.controls.GuiControl;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import javax.swing.Icon;

import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.wc.SVNRevision;

public class SvnCheckOutAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OpcatMCDirEntry entry;
	private boolean lock = true;

	public SvnCheckOutAction(OpcatMCDirEntry entry, boolean lock, String name,
			Icon icon) {
		super(name, icon);
		this.entry = entry;
		this.lock = lock;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		try {

			// SwingUtilities.invokeAndWait(new Runnable() {
			//
			// @Override
			// public void run() {
			// OpcatSvnProgressReporter progress = new
			// OpcatSvnProgressReporter();
			// progress.setVisible(true);
			// }
			//
			// });

			OpcatMCManager svn = OpcatMCManager.getInstance();
			svn.doCheckOut(entry, SVNRevision.HEAD, lock);

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		Opcat2.getGlassPane().setVisible(false);
		Opcat2.getGlassPane().stop();
		GuiControl.getInstance().getRepository().getModelsView()
				.repaintKeepOpen();

	}

}
