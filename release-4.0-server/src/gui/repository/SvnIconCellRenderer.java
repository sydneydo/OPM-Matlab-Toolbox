package gui.repository;

import gui.images.svn.SvnImages;
import gui.util.OpcatLogger;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.SVNLock;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import user.OpcatUser;

public class SvnIconCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public SvnIconCellRenderer() {
	}

	/**
	 * Returns the color to use for the background if node is selected. LERA
	 */

	public Color getBackgroundSelectionColor() {
		return this.backgroundNonSelectionColor;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		Object obj = node.getUserObject();

		// if ((this.expended != null) && (this.expended.isNodeAncestor(node)))
		// {
		// return this;
		// }

		this.setToolTipText(obj.toString());

		if (obj instanceof OpcatMCDirEntry) {

			OpcatMCDirEntry myEntry = (OpcatMCDirEntry) obj;

			if (myEntry.getIcon() != null)

			{
				setText(myEntry.getName());
				setIcon(myEntry.getIcon());
				return this;
			}

			this.setText(myEntry.getName());
			Icon icon = getIcon();

			try {
				OpcatMCManager svn = OpcatMCManager.getInstance();
				String username = OpcatUser.getCurrentUser().getName();
				SVNLock lock = svn.getEntryLock(myEntry);

				if (myEntry.getKind() == SVNNodeKind.FILE) {
					if ((lock != null) && (lock.getOwner() != null)
							&& !(lock.getOwner().equalsIgnoreCase(username))) {
						icon = SvnImages.FILE_LOCKED;
						this.setIcon(SvnImages.FILE_LOCKED);
					} else if ((lock != null) && (lock.getOwner() != null )
							&& lock.getOwner().equalsIgnoreCase(username)) {
						icon = SvnImages.FILE_LOCKED_BYME;
						this.setIcon(SvnImages.FILE_LOCKED_BYME);

					} else {
						icon = SvnImages.FILE_NORMAL;
						this.setIcon(SvnImages.FILE_NORMAL);
					}
				}
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}

			if (((OpcatMCDirEntry) obj).getKind() == SVNNodeKind.DIR) {

			}

			myEntry.setIcon(icon);
		}

		if (obj instanceof SVNURL) {
			this.setText(((SVNURL) obj).getHost());
			return this;
		}

		return this;
	}
}
