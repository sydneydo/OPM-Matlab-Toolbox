package gui.actions.svn;

import gui.Opcat2;
import gui.controls.GuiControl;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import modelControl.OpcatMCManager;
import modelControl.gui.OpcatSvnGridReporter;

import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNFileRevision;

public class SvnRepoRevisionManagerAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SVNURL url;
	private String entryName;

	public SvnRepoRevisionManagerAction(SVNURL url, String entryName,
			String name, Icon icon) {
		super(name, icon);
		this.url = url;
		this.entryName = entryName;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		super.actionPerformed(e);
		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		try {

			Collection<SVNFileRevision> logs = OpcatMCManager.getInstance().doLog(
					url, entryName);

			if (logs == null) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"No revisions", "Revisions", JOptionPane.YES_OPTION,
						null);
				Opcat2.getGlassPane().setVisible(false);
				Opcat2.getGlassPane().stop();				
				return;
			}

			Collection<String> data = new LinkedList<String>();
			data.add("Number;Name;Revision;Date;Message");
			int i = 1;
			for (SVNFileRevision log : logs) {
				SVNProperties revisionProperties = log.getRevisionProperties();
				data.add(i + ";" + entryName + ";" + log.getRevision() + ";"
						+ null + ";"
						+ revisionProperties.getStringValue("svn:log"));
				i++;
			}

			OpcatSvnGridReporter report = new OpcatSvnGridReporter(true, ";",
					entryName + " Revisions");
			report.setData(data);
			report.show(true);

			GuiControl.getInstance().getRepository().getModelsView()
					.repaintKeepOpen();

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		Opcat2.getGlassPane().setVisible(false);
		Opcat2.getGlassPane().stop();
	}
}
