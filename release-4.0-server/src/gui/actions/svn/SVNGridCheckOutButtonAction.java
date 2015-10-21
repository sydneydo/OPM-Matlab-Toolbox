package gui.actions.svn;

import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;

import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.wc.SVNRevision;


public class SVNGridCheckOutButtonAction extends SvnAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	GridPanel panel;

	public SVNGridCheckOutButtonAction(GridPanel panel) {
		super();
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		// get the selected rows SVNDirEntry
		// and check those out.

		int rows[] = panel.getGrid().getSelectedRows();
		OpcatMCManager svn = OpcatMCManager.getInstance(); 
		
		for (int i = 0; i < rows.length; i++) {
			OpcatMCDirEntry file = (OpcatMCDirEntry) panel.getGrid().GetTag(rows[i])[0];
			
			//get parent
			
			
			//inform user 
			
			
			//checkout parent directories
			svn.doCheckOut(file, SVNRevision.HEAD);
		}

	}
}