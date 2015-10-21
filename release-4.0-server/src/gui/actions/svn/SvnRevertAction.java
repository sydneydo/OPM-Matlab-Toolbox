package gui.actions.svn;

import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import gui.Opcat2;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.util.OpcatFile;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import modelControl.OpcatMCManager;

public class SvnRevertAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OpcatFile file;

	public SvnRevertAction(OpcatFile file, String name, Icon icon) {
		super(name, icon);
		this.file = file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		try {

			int ret = 0;
			if (file.isDirectory()) {
				ret = JOptionPane
						.showConfirmDialog(
								Opcat2.getFrame(),
								"Are you sure?\nAll local copy fils are going to be overridden",
								"Opcat II", JOptionPane.YES_NO_OPTION);
			} else {
				ret = JOptionPane
						.showConfirmDialog(
								Opcat2.getFrame(),
								"Are you sure?\nYour local copy is going to be overridden",
								"Opcat II", JOptionPane.YES_NO_OPTION);
			}
			if (ret != JOptionPane.YES_OPTION) {
				return;
			}
			OpcatMCManager.getInstance().doRevert(file);

			GuiControl.getInstance().getRepository().getModelsView()
					.repaintKeepOpen();

			GridPanel.RemoveALLPanels();

			if (file.getName().endsWith(".opx")
					|| file.getName().endsWith(".opz"))
				FileControl.getInstance().loadFileWithOutFilesPrompt(
						file.getPath());

			if (Opcat2.getCurrentProject() != null)
				Opcat2.getCurrentProject().setCanClose(false);

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		GuiControl.getInstance().getRepository().getModelsView()
				.repaintKeepOpen();
		Opcat2.getGlassPane().setVisible(false);
		Opcat2.getGlassPane().stop();

	}
}
