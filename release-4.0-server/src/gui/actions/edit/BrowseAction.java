package gui.actions.edit;

import java.awt.event.ActionEvent;

import gui.Opcat2;
import gui.opdProject.Opd;
import gui.opdProject.OpdMap;
import gui.util.JToolBarButton;
import gui.util.OpcatLogger;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.w3c.dom.events.EventException;

public class BrowseAction extends EditAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8702799996492550346L;

	public BrowseAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			super.actionPerformed(arg0);
		} catch (EventException e) {
			JOptionPane.showMessageDialog(this.gui.getFrame(), e.getMessage()
					.toString(), "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			JToolBarButton src = (JToolBarButton) arg0.getSource();
			long opdid = -1;
			if (src.getToolTipText().equalsIgnoreCase("up")) {

				Opd opd = Opcat2.getCurrentProject().getCurrentOpd();
				if (opd.getMainEntry() != null) {
					Opcat2.getCurrentProject().showOPD(opd.getParentOpd().getOpdId());
					OpdMap.UpdateOpdMap(opd);
				}
			}

			if (src.getToolTipText().equalsIgnoreCase("back")) {
				opdid = OpdMap.getBackOpd();
			}
			if (src.getToolTipText().equalsIgnoreCase("forward")) {
				opdid = OpdMap.getForwordOpd();
			}
			if (opdid >= 0)
				Opcat2.getCurrentProject().showOPD(opdid, false);
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

}
