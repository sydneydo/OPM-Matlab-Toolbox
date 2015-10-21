package gui.actions.svn;

import gui.Opcat2;
import gui.controls.GuiControl;
import gui.util.OpcatFile;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import modelControl.OpcatMCManager;


public class SvnUnlockAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OpcatFile file;

	public SvnUnlockAction(OpcatFile file, String name, Icon icon) {
		super(name, icon);
		this.file = file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		super.actionPerformed(e);
		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		try {
			boolean done = false;
			while (!done) {
				String msg = (String) JOptionPane.showInputDialog(Opcat2
						.getFrame(), "Enter unlock note", "Unlock",
						JOptionPane.PLAIN_MESSAGE, null, null, "Unlock note");

				if ((msg != null) && (msg.length() > 0)) {
					OpcatMCManager.getInstance().doUnLock(file, msg);
					GuiControl.getInstance().getRepository().getModelsView()
							.repaintKeepOpen();
				}

				done = true;
			}

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		GuiControl.getInstance().getRepository().getModelsView().repaintKeepOpen(); 

		Opcat2.getGlassPane().setVisible(false);
		Opcat2.getGlassPane().stop();
	}
}
