package gui.actions.svn;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.repository.BaseView.View_RefreshState;
import gui.util.OpcatFile;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNURL;

import modelControl.OpcatMCManager;

public class SvnAddServerDirectoryAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SVNURL url;

	public SvnAddServerDirectoryAction(SVNURL url, String name, Icon icon) {
		super(name, icon);
		this.url = url;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		try {
			Opcat2.getGlassPane().setVisible(true);
			Opcat2.getGlassPane().start();

			String msg = "Enter Directory Name";
			String title = "Create MC Directory";
			msg = JOptionPane.showInputDialog(Opcat2.getFrame(), title, msg);

			if (msg != null) {
				OpcatMCManager.getInstance().doMakeDirectory(url, msg);

				GuiControl.getInstance().getRepository().getSVNView()
						.repaintKeepOpen(View_RefreshState.KEEP_OPEN);

			}
		} catch (Exception ex) {

		} finally {

			Opcat2.getGlassPane().setVisible(false);
			Opcat2.getGlassPane().stop();
		}

	}

}
