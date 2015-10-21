package gui.actions.expose;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import expose.OpcatExposeList;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import gui.actions.OpcatAction;
import gui.controls.FileControl;
import gui.opmEntities.OpmEntity;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.util.OpcatLogger;

public class OpcatExposeThingAction extends OpcatAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Instance myEntity;

	public OpcatExposeThingAction(String name, Icon icon, Instance opmEntity) {
		super(name, icon);
		this.myEntity = opmEntity;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			super.actionPerformed(e);

			myEntity.getEntry().getMyProject().getExposeManager()
					.addPublicExposeChange(myEntity, OPCAT_EXPOSE_OP.ADD);

			OpcatExposeList.getInstance().refresh(
					FileControl.getInstance().getCurrentProject()
							.getExposeManager().getExposedList(null, true));

			FileControl.getInstance().getCurrentProject().setCanClose(false);

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}
}
