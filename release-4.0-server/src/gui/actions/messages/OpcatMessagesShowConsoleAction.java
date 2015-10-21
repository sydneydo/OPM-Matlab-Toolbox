package gui.actions.messages;

import gui.Opcat2;
import gui.actions.svn.SvnAction;
import gui.controls.GuiControl;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import messages.OpcatMessagesManager;
import messages.OpcatMessagesFilter;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SYSTEMS;

public class OpcatMessagesShowConsoleAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpcatMessagesShowConsoleAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		try {

			OPCAT_MESSAGES_SYSTEMS[] systems = null; // {
														// OPCAT_MESSAGES_SYSTEMS.FILES
														// };

			GridPanel.RemovePanel(OpcatMessagesManager.getMessagesGridName());
			if (GuiControl.getInstance().isShowMessagesConsole()) {
				OpcatMessagesFilter filter = new OpcatMessagesFilter(null,
						null, null, null, null, null, systems, null);
				OpcatMessagesManager.getInstance().getMessages(filter)
						.AddToExtensionToolsPanel();
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
