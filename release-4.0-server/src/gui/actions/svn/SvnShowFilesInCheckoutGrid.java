package gui.actions.svn;

import gui.Opcat2;
import gui.util.OpcatLogger;

import java.awt.event.ActionEvent;
import javax.swing.Icon;

import modelControl.gui.OpcatCheckoutGrid;

import org.tmatesoft.svn.core.SVNURL;

public class SvnShowFilesInCheckoutGrid extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SVNURL url;

	public SvnShowFilesInCheckoutGrid(SVNURL url, String name, Icon icon) {
		super(name, icon);
		this.url = url;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		Opcat2.getGlassPane().setVisible(true);
		Opcat2.getGlassPane().start();

		try {
			OpcatCheckoutGrid grid = new OpcatCheckoutGrid(url);
			grid.show();

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		Opcat2.getGlassPane().setVisible(false);
		Opcat2.getGlassPane().stop();

	}

}
