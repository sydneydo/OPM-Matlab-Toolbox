package gui.actions.expose;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import expose.OpcatExposeKey;
import expose.OpcatExposeList;
import gui.Opcat2;
import gui.actions.OpcatAction;
import gui.controls.FileControl;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import gui.util.OpcatLogger;

public class OpcatExposeUpdateExposedThingAction extends OpcatAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	OpmEntity myEntity;

	public OpcatExposeUpdateExposedThingAction(String name, Icon icon,
			OpmEntity opmEntity) {
		super(name, icon);
		this.myEntity = opmEntity;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			super.actionPerformed(e);

			String description = JOptionPane.showInputDialog(Opcat2.getFrame(),
					"Expose description");
			if (description == null) {
				return;
			}

			OpcatExposeKey key = new OpcatExposeKey(new File(FileControl
					.getInstance().getCurrentProject().getFileName()), myEntity
					.getId(), false);

			OpdProject prj = FileControl.getInstance().getCurrentProject();

			prj.getExposeManager().updateExposeDescription(key, description);

			OpcatExposeList.getInstance().refresh(
					prj.getExposeManager().getExposedList(null, true));

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}
}
