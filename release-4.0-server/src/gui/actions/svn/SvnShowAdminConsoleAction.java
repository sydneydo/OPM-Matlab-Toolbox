package gui.actions.svn;

import gui.Opcat2;
import gui.controls.GuiControl;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import javax.swing.Icon;

import modelControl.OpcatMCManager;

public class SvnShowAdminConsoleAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SvnShowAdminConsoleAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		try {

			GridPanel.RemovePanel(OpcatMCManager.getAdminGridName());
			if (GuiControl.getInstance().isShowAdminConsole()) {
				OpcatMCManager.getInstance(true).getAdminGrid().show(true);
			} 

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		GuiControl.getInstance().getRepository().getModelsView()
				.repaintKeepOpen();
		Opcat2.getGlassPane().setVisible(false);
		Opcat2.getGlassPane().stop();
	}
}
