package gui.actions.expose;

import java.awt.event.ActionEvent;
import javax.swing.Icon;

import gui.actions.OpcatAction;
import gui.opdProject.Opd;
import gui.opmEntities.OpmEntity;
import gui.projectStructure.Instance;
import gui.util.OpcatLogger;

public class OpcatExposeShowEntity extends OpcatAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	OpmEntity myEntity;
	Instance myInstance;

	public OpcatExposeShowEntity(String name, Icon icon, Instance showedInstance) {
		super(name, icon);
		this.myEntity = showedInstance.getEntry().getLogicalEntity();
		this.myInstance = showedInstance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			super.actionPerformed(e);

			// if (source != null) {
			Opd opd = myInstance.getEntry().getMyProject().getOpdByID(
					myInstance.getKey().getOpdId());
			myInstance.getEntry().getMyProject().showOPD(
					myInstance.getKey().getOpdId());
			opd.getSelection().resetSelection();
			opd.getSelection().addSelection(myInstance, true);

			// } else {
			// OpcatLogger.logError(new Exception("Error retriving source"));
			// }
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}
}
