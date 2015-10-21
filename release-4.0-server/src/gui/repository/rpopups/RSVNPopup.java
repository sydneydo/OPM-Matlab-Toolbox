package gui.repository.rpopups;

import gui.actions.svn.SvnAddServerDirectoryAction;
import gui.actions.svn.SvnCheckOutAction;
import gui.actions.svn.SvnRepoDeleteAction;
import gui.actions.svn.SvnRepoImportAction;
import gui.actions.svn.SvnRepoRevisionManagerAction;
import gui.actions.svn.SvnShowFilesInCheckoutGrid;
import gui.images.standard.StandardImages;
import gui.images.svn.SvnImages;
import gui.opdProject.OpdProject;
import gui.repository.BaseView;
import gui.repository.BaseView.View_RefreshState;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JSeparator;

import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;

public class RSVNPopup extends RDefaultPopup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SVNURL url;

	public RSVNPopup(BaseView view, OpdProject prj) {
		super(view, prj);

		add(RefrashAction);

		url = null;
		OpcatMCDirEntry entry = null;

		if (userObject instanceof SVNURL) {
			url = (SVNURL) userObject;
		} else {
			entry = (OpcatMCDirEntry) userObject;
			url = entry.getURL();
		}

		if (userObject instanceof OpcatMCDirEntry) {
			add(new JSeparator());
			// if (entry.getKind() == SVNNodeKind.DIR) {
			add(new SvnCheckOutAction(entry, true, "Checkout",
					SvnImages.ACTION_CHECKOUT));
			add(new JSeparator());
			// }

			if (entry.getKind() == SVNNodeKind.DIR) {
				add(new SvnCheckOutAction(entry, false, "Get", null));
				add(new JSeparator());
			}
			// if (entry.getKind() == SVNNodeKind.FILE) {

			add(new SvnRepoDeleteAction(entry, "Delete", null));
			add(new JSeparator());
			// }

		}
		if (((userObject instanceof OpcatMCDirEntry) && (entry.getKind() == SVNNodeKind.DIR))
				|| (userObject instanceof SVNURL)) {
			add(new SvnAddServerDirectoryAction(url, "Add Directory",
					SvnImages.ACTION_ADD_DIRECTORY));
		}

		add(new JSeparator());
		add(new SvnRepoImportAction(url, "Import", SvnImages.ACTION_IMPORT));

		add(new JSeparator());
		add(new SvnShowFilesInCheckoutGrid(url, "Show Properties",
				SvnImages.ACTION_SHOW_GRID));

		add(new SvnRepoRevisionManagerAction(url, (entry == null ? "root"
				: entry.getName()), "Revisions",
				SvnImages.ACTION_REVISION_EDITOR));

		JMenu admin = new JMenu("Admin");
		admin.add(setNeedsLock);
		admin.add(removeNeedsLock);
		admin.add(forceUnlock);

		// add(new JSeparator());
		// add(admin);

	}

	Action forceUnlock = new AbstractAction("Force unlock",
			SvnImages.ACTION_UNLOCK) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {

			OpcatMCManager.getInstance().adminForceUnlock(url);
		}
	};

	Action setNeedsLock = new AbstractAction("set lock property",
			SvnImages.ACTION_UPDATE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {

			OpcatMCManager.getInstance().adminAddNeedsLockProperty(url);
		}
	};
	Action removeNeedsLock = new AbstractAction("remove lock property",
			SvnImages.ACTION_UPDATE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {

			OpcatMCManager.getInstance().adminRemoveNeedsLockProperty(url);
		}
	};

	Action RefrashAction = new AbstractAction("Refresh", StandardImages.REFRESH) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {

			view.repaintKeepOpen(View_RefreshState.KEEP_OPEN);
		}
	};

}
