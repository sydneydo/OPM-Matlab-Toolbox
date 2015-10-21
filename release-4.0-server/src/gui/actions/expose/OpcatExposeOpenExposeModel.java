package gui.actions.expose;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Icon;

import expose.OpcatExposeKey;
import expose.OpcatExposeManager;
import gui.actions.OpcatAction;
import gui.controls.FileControl;
import gui.util.OpcatLogger;

public class OpcatExposeOpenExposeModel extends OpcatAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	OpcatExposeKey key;

	public OpcatExposeOpenExposeModel(String name, Icon icon, OpcatExposeKey key) {
		super(name, icon);
		this.key = key;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			super.actionPerformed(e);

			File model = OpcatExposeManager.getModelIntoOpcatTemp(
					key.getModelEncodedURI());

			FileControl.getInstance().runNewOPCAT(model, key.getOpmEntityID());

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}
}
