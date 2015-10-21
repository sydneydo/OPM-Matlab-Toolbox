package gui.actions.expose;

import java.awt.event.ActionEvent;
import javax.swing.Icon;

import expose.OpcatExposeKey;

import gui.actions.OpcatAction;
import gui.controls.FileControl;
import gui.metaLibraries.logic.Role;
import gui.opmEntities.OpmEntity;
import gui.projectStructure.Instance;
import gui.util.OpcatLogger;

public class OpcatExposeUNUSEExposeAction extends OpcatAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	OpmEntity myEntity;
	Instance myInstance;

	public OpcatExposeUNUSEExposeAction(String name, Icon icon,
			Instance instance) {
		super(name, icon);
		this.myEntity = instance.getEntry().getLogicalEntity();
		this.myInstance = instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			super.actionPerformed(e);

			OpcatExposeKey myKey = myInstance.getPointer();
			
			myInstance.getEntry().getLogicalEntity().getRolesManager()
					.removeRole(myKey.convertKeyToRole());

			myInstance.setPointer(null);

			FileControl.getInstance().getCurrentProject().setCanClose(false);

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}
}
