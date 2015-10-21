package gui.actions.svn;

import expose.OpcatExposeKey;
import expose.OpcatExposeManager;
import expose.OpcatExposeUser;
import gui.Opcat2;
import gui.controls.GuiControl;
import gui.util.OpcatFile;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.tmatesoft.svn.core.SVNURL;

import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;

public class SvnLocalDeleteAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OpcatFile file;

	public SvnLocalDeleteAction(OpcatFile file, String name, Icon icon) {
		super(name, icon);
		this.file = file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();
		try {
			OpcatMCDirEntry entry = OpcatMCManager.getInstance().getEntry(file);

			if (entry == null) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"Not a MC file");
				Opcat2.getGlassPane().setVisible(false);
				Opcat2.getGlassPane().stop();
				return;
			}

			ArrayList<OpcatExposeUser> users = new ArrayList<OpcatExposeUser>();
			if (entry != null) {
				users = OpcatExposeManager.getExposeUsage(entry.getURL()
						.getURIEncodedPath());
			}

			if (users.size() > 0) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"File has used exports, delete canceled");
				Opcat2.getGlassPane().setVisible(false);
				Opcat2.getGlassPane().stop();
				return;
			} else {
				// remove expose as there is no users
				ArrayList<OpcatExposeKey> keys = OpcatExposeManager
						.getExposeKey(entry.getURL().getURIEncodedPath());
				for (OpcatExposeKey key : keys) {
					OpcatExposeManager.removeExposeFromDB(key);
				}

				// remove usage
				SVNURL url = OpcatMCManager.getInstance().getFileURL(file);
				OpcatExposeManager.removeModelUsage(url.getPath());

			}

			int ret = JOptionPane.showOptionDialog(Opcat2.getFrame(),
					"Are you sure?", "Delete", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (ret == JOptionPane.YES_OPTION) {
				OpcatMCManager.getInstance().doLocalDelete(file, true);
				GuiControl.getInstance().getRepository().getModelsView()
						.repaintKeepOpen();

			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		GuiControl.getInstance().getRepository().getModelsView().rebuildTree(
				null, null);
		Opcat2.getGlassPane().setVisible(false);
		Opcat2.getGlassPane().stop();

	}
}
