package gui.actions.svn;

import gui.Opcat2;
import gui.util.OpcatFile;
import java.awt.event.ActionEvent;
import javax.swing.Icon;

import modelControl.OpcatMCManager;

public class SvnCleanupAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OpcatFile file;

	public SvnCleanupAction(OpcatFile file, String name, Icon icon) {
		super(name, icon);
		this.file = file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		OpcatMCManager.getInstance().Cleanup(file);

		Opcat2.getGlassPane().setVisible(false);
		Opcat2.getGlassPane().stop();

	}

}
