package gui.repository;

import gui.controls.GuiControl;
import gui.images.opcaTeam.OPCATeamImages;
import gui.images.repository.RepositoryImages;
import gui.images.standard.StandardImages;
import gui.images.svn.SvnImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.scenarios.Scenario;
import gui.scenarios.Scenarios;
import gui.util.OpcatFile;
import gui.util.OpcatLogger;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.SVNLock;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;

import user.OpcatUser;
import util.Configuration;

public class IconCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public IconCellRenderer() {
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

		this.setToolTipText(obj.toString());

		if (obj instanceof Opd) {
			if (expanded) {
				this.setIcon(RepositoryImages.OPD_OPEN);
			} else {
				this.setIcon(RepositoryImages.OPD);
			}
			return this;
		}

		if (obj instanceof ObjectInstance) {
			this.setIcon(RepositoryImages.OBJECT_MID);
			return this;
		}

		if (obj instanceof OpmObject) {
			this.setIcon(RepositoryImages.OBJECT_MID);
			return this;
		}

		if (obj instanceof ObjectEntry) {
			this.setIcon(RepositoryImages.OBJECT_MID);
			return this;
		}

		if (obj instanceof ProcessInstance) {
			this.setIcon(RepositoryImages.PROCESS_MID);
			return this;
		}

		if (obj instanceof OpmProcess) {
			this.setIcon(RepositoryImages.PROCESS_MID);
			return this;
		}

		if (obj instanceof ProcessEntry) {
			this.setIcon(RepositoryImages.PROCESS_MID);
			return this;
		}

		if (obj instanceof Scenario) {
			this.setIcon(OPCATeamImages.MODEL);
			return this;
		}

		if (obj instanceof Scenarios) {
			this.setIcon(OPCATeamImages.WORKGROUP);
			return this;
		}

		if (obj instanceof SVNURL) {
			this.setText(((SVNURL) obj).getHost());
			return this;
		}

		if ((obj instanceof OpcatFile)) {
			OpcatFile file = (OpcatFile) obj;

			if (file.isFile()) {
				this.setIcon(SvnImages.FILE_NORMAL);
			} else {
			}

			if (file.isWorkinCopyFile()) {
				try {

					if (file.getPath().equalsIgnoreCase(
							Configuration.getInstance().getProperty(
									"models_directory"))) {
						return this;
					}

					boolean allowRemote = GuiControl.getInstance()
							.getRepository().getModelsView()
							.isAllowServerAccess();

					OpcatMCManager svn;
					svn = OpcatMCManager.getInstance(true);
					SVNStatus status = svn.getStatus(file, allowRemote);

					if (file.isFile()) {
						getFileStatus(file, status, allowRemote);
						return this;
					} else {
						getLocalDirectoryStatus(status);
						return this;
					}
				} catch (Exception ex) {
					OpcatLogger.logError(ex);
				}
			}
			return this;
		}

		if (obj instanceof MetaLibrary) {
			if (((MetaLibrary) obj).isPolicy()) {
				this.setText("<html><b>" + ((MetaLibrary) obj).getName()
						+ "</b></html>");
				this.setIcon(StandardImages.Policies);
			} else {
				this.setText(((MetaLibrary) obj).getName());
				this.setIcon(StandardImages.Classification);
			}
			return this;
		}

		if ((obj instanceof OpdProject) && (tree instanceof BaseView)) {
			this.setIcon(((BaseView) tree).getIcon());
		}

		return this;
	}

	private void getFileStatus(OpcatFile file, SVNStatus status, boolean remote) {
		try {
			if (status.getContentsStatus() != SVNStatusType.STATUS_UNVERSIONED) {
				if (remote) {
					OpcatMCManager svn = OpcatMCManager.getInstance(false);
					SVNLock lock = svn.getRemoteLock(file);
					if (lock != null) {
						if ((lock.getOwner() != null)
								&& lock.getOwner().equalsIgnoreCase(
										OpcatUser.getCurrentUser().getName())) {
							this.setIcon(SvnImages.FILE_LOCKED_BYME);
							return;
						} else {
							this.setIcon(SvnImages.FILE_LOCKED);
							return;
						}
					}
				} else {
					boolean locked = !file.canWrite();
					if (locked) {
						this.setIcon(SvnImages.FILE_LOCKED);
						return;
					}

				}
			}

			if (status != null) {
				if (status.getContentsStatus() == SVNStatusType.STATUS_UNVERSIONED) {
					this.setIcon(SvnImages.FILE_NEW);
					return;
				}

				if (status.getContentsStatus() == SVNStatusType.STATUS_ADDED) {
					this.setIcon(SvnImages.FILE_ADDED);
					return;
				}

				if (status.getContentsStatus() == SVNStatusType.STATUS_MODIFIED) {
					this.setIcon(SvnImages.FILE_CHANGED);
					return;
				}

				if (status.getContentsStatus() == SVNStatusType.STATUS_DELETED) {
					this.setIcon(SvnImages.FILE_DELETED);
					return;
				}
			}

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

	}

	private void getLocalDirectoryStatus(SVNStatus status) {
		try {
			if (status != null) {
				if (status.getContentsStatus() == SVNStatusType.STATUS_UNVERSIONED) {
					this.setIcon(SvnImages.FOLDER_NEW);
					return;
				}

				if (status.getContentsStatus() == SVNStatusType.STATUS_ADDED) {
					this.setIcon(SvnImages.FOLDER_ADDED);
					return;
				}

				if (status.getContentsStatus() == SVNStatusType.STATUS_MODIFIED) {
					this.setIcon(SvnImages.FOLDER_CHANGED);
					return;
				}

				if (status.getContentsStatus() == SVNStatusType.STATUS_DELETED) {
					this.setIcon(SvnImages.FOLDER_DELETED);
					return;
				}
			}

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}
}
