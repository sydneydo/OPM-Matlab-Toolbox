package gui.actions.expose;

import expose.OpcatExposeEntity;
import expose.OpcatExposeList;
import gui.Opcat2;
import gui.actions.OpcatAction;
import gui.controls.FileControl;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import modelControl.OpcatMCManager;

public class OpcatExposeShowExposedList extends OpcatAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpcatExposeShowExposedList(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			super.actionPerformed(e);

			if (FileControl.getInstance().getCurrentProject().getPath() == null) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"Can not use expose on an un-saved model");
				return;
			}

			if (FileControl.getInstance().getCurrentProject().getMcURL() == null) {

				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"Add the model to MC, before using expose");
				return;
			}
			ArrayList<OpcatExposeEntity> exposed = new ArrayList<OpcatExposeEntity>();

			// if (OpcatMCManager.isOnline()) {
			exposed = FileControl.getInstance().getCurrentProject()
					.getExposeManager().getExposedList(null, true);
			// }

			OpcatExposeList.getInstance().init(exposed);
			OpcatExposeList.getInstance().show(true);

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}
}
