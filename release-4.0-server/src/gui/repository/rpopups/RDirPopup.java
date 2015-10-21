package gui.repository.rpopups;

import gui.Opcat2;
import gui.actions.svn.SvnAddAction;
import gui.actions.svn.SvnAddLocalDirectoryAction;
import gui.actions.svn.SvnCleanupAction;
import gui.actions.svn.SvnCommitAction;
import gui.actions.svn.SvnDeleteLocalNoRepoDirectoryAction;
import gui.actions.svn.SvnLocalDeleteAction;
import gui.actions.svn.SvnLockAction;
import gui.actions.svn.SvnRepoRevisionManagerAction;
import gui.actions.svn.SvnRevertAction;
import gui.actions.svn.SvnUnlockAction;
import gui.actions.svn.SvnUpdateAction;
import gui.controls.FileControl;
import gui.dataProject.DataCreatorType;
import gui.dataProject.DataProject;
import gui.images.standard.StandardImages;
import gui.images.svn.SvnImages;
import gui.metaLibraries.logic.MetaException;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.OpdProject;
import gui.repository.BaseView;
import gui.repository.ModelsView;
import gui.repository.BaseView.View_RefreshState;
import gui.util.OpcatFile;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultMutableTreeNode;

import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.SVNURL;

import util.Configuration;

public class RDirPopup extends RDefaultPopup {

	OpcatFile file;
	private JCheckBoxMenuItem allowServer;

	public RDirPopup(BaseView view, OpdProject prj, MouseEvent event) {
		super(view, prj);

		if (userObject instanceof OpcatFile) {

			this.file = (OpcatFile) this.userObject;

			boolean readonlyModelsdir = file.getPath()
					.equalsIgnoreCase(
							Configuration.getInstance().getProperty(
									"models_directory"));

			if (!file.isDirectory()) {
				add(OpeninNewWindow);
				add(OpeninCurrentWindow);
				add(new JSeparator());
				JMenuItem item = add(AddasPolicy);
				if (myProject == null)
					item.setEnabled(false);
				item = add(AddasClassification);
				if (myProject == null)
					item.setEnabled(false);
			}

			if (file.isDirectory()) {

				int i = 0;
				i = ((DefaultMutableTreeNode) view.getModel().getRoot())
						.getIndex(((DefaultMutableTreeNode) view
								.getPathForLocation(event.getX(), event.getY())
								.getPathComponent(1)));

				if (i != 0 && (selectedPath.getPathCount() <= 2))
					add(RemoveDirectory);

				if (file.isWorkinCopyFile()) {

				}
			}

			if (file.isWorkinCopyFile()) {
				allowServer = new JCheckBoxMenuItem("Lock status refrash");
				allowServer.setSelected(((ModelsView) view)
						.isAllowServerAccess());
				allowServer.addActionListener(AllowServerAccess);
				JMenuItem item;

				boolean online = OpcatMCManager.isOnline();

				boolean noUrl = (OpcatMCManager.getInstance(true).getFileURL(
						file) == null);

				// SVNStatus status =
				// OpcatMCManager.getInstance(true).getStatus(
				// file, false);
				// boolean zeroRevision = true;
				// if (status != null)
				// zeroRevision = (status.getRevision().getNumber() == 0);
				// boolean canPreformMCActions = !noUrl && !zeroRevision;
				// boolean canPreformDBActions =
				// OpcatMCManager.getInstance(true)
				// .isConnectedToerver();

				if (online) {

					// add(allowServer);
					if (!readonlyModelsdir && online && !noUrl) {

						add(new JSeparator());

						item = add(new SvnCommitAction(file, "Commit",
								SvnImages.ACTION_COMMIT));
						item
								.setToolTipText("Commit changes to the repository (network access needed)");
					}
					add(new JSeparator());
				} else if (!online) {
					item = add(new JMenuItem("Commit: off-line"));
					item.setEnabled(false);
				} else if (noUrl) {
					item = add(new JMenuItem("Commit: not MC File"));
					item.setEnabled(false);
				}

				if (!readonlyModelsdir && online) {
					item = add(new SvnLockAction(file, "Lock",
							SvnImages.ACTION_LOCK));
					item
							.setToolTipText("Lock files in the repository (network access needed)");

					item = add(new SvnUnlockAction(file, "Unlock",
							SvnImages.ACTION_UNLOCK));
					item
							.setToolTipText("Unlock files in the repository (network access needed)");

					add(new JSeparator());

					if (file.isFile() && !noUrl) {
						item = add(new SvnLocalDeleteAction(file,
								"Delete File", null));
						item.setToolTipText("Mark item for deletion");
					}

					item = add(new SvnUpdateAction(file, "Update",
							SvnImages.ACTION_UPDATE));
					item
							.setToolTipText("Get changes from repository of files changed by other users");

					item = add(new SvnRevertAction(file, "Revert",
							SvnImages.ACTION_REVERT));
					item
							.setToolTipText("Revert changes of local file and replace it by latest revision from the repository");

				}

				if (!readonlyModelsdir) {
					item = add(new SvnCleanupAction(file, "Cleanup",
							SvnImages.ACTION_CLEANUP));

					add(new JSeparator());

					// if (file.isFile()) {
					if (selectedPath.getPath().length > 3) {
						item = add(new SvnAddAction(file, "Add",
								SvnImages.ACTION_ADD));
						item
								.setToolTipText("Mark files to be added to the repository later by commit action");
					}
					// }

					// item = add(new SvnAddDirectoryAction(file,
					// "Make Repository Directory",
					// SvnImages.ACTION_ADD_DIRECTORY));
					// item
					// .setToolTipText("Add folder to local copy and commit it to repository");

					item = add(new SvnAddLocalDirectoryAction(file,
							"Make Directory", SvnImages.ACTION_ADD_DIRECTORY));
					item
							.setToolTipText("Make a local directory which will be added to the repository later");

					if (!readonlyModelsdir) {

						item = add(new SvnDeleteLocalNoRepoDirectoryAction(
								file, "Delete Locally",
								SvnImages.ACTION_ADD_DIRECTORY));
						item
								.setToolTipText("Delete a file locally, repository is not effected, update action will recreate the file");

						// item.setEnabled(OpcatMCManager.getInstance(true)
						// .getStatus(file, false).getRevision()
						// .getNumber() != 0);
					}

					if (OpcatMCManager.isOnline()
							&& (selectedPath.getPath().length > 3)) {

						OpcatMCDirEntry entry = OpcatMCManager.getInstance()
								.getEntry(file, false);
						if (entry != null) {
							add(new JSeparator());
							item = add(new SvnRepoRevisionManagerAction(entry
									.getURL(), entry.getName(), "Revisions",
									SvnImages.ACTION_REVISION_EDITOR));
							item.setToolTipText("Revisions editor");
						}
					}
				}

				// SVN Opps
			}

		}

		if (userObject instanceof String) {
			add(RefrashAction);
			add(new JSeparator());
			add(AddDirectory);
		}
	}

	Action AllowServerAccess = new AbstractAction("Allow Server Access",
			StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			((ModelsView) view).setAllowServerAccess(allowServer.isSelected());

			((ModelsView) view).setRefrashState(View_RefreshState.ALL);
			view.rebuildTree(null, null);
		}
	};

	Action RemoveDirectory = new AbstractAction("Remove Directory",
			StandardImages.DELETE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {

			Configuration.getInstance().removeDirectoryToUserDirectories(
					file.getAbsolutePath());
			((ModelsView) view).setRefrashState(View_RefreshState.ALL);
			;
			view.rebuildTree(null, null);
		}
	};

	Action DummyWorkingCopyFileOP = new AbstractAction(
			"DummyWorkingCopyFileOP", StandardImages.DELETE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {

		}
	};

	Action DummyWorkingCopyDirectoryOP = new AbstractAction(
			"DummyWorkingCopyDirectoryOP", StandardImages.DELETE) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {

		}
	};

	// Action SVNCheckOut = new AbstractAction("Checkout",
	// SvnImages.CHECKOUT) {
	//
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// public void actionPerformed(ActionEvent e) {
	//
	// }
	// };

	Action AddDirectory = new AbstractAction("Add directory to local list",
			StandardImages.SAVE_AS) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JFileChooser myFileChooser = new JFileChooser();
			myFileChooser.setSelectedFile(new File(""));
			myFileChooser.resetChoosableFileFilters();
			myFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int retVal = myFileChooser.showOpenDialog(Opcat2.getFrame());
			if (retVal == JFileChooser.APPROVE_OPTION) {
				Configuration.getInstance().addDirectoryToUserDirectories(
						myFileChooser.getSelectedFile().getAbsolutePath());
				((ModelsView) view).setRefrashState(View_RefreshState.ALL);
				;
				view.rebuildTree(null, null);
			}
		}
	};

	Action AddasClassification = new AbstractAction("Add as Classification",
			StandardImages.Classification) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {

			String path = file.getAbsolutePath();
			int type = DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE;
			try {
				if (myProject != null) {
					MetaLibrary newLib = myProject.getMetaManager()
							.createNewMetaLibraryReference(
									MetaLibrary.TYPE_CLASSIFICATION, path,
									DataCreatorType.DATA_TYPE_OPCAT_FILE_OPZ,
									type);
					myProject.getMetaManager().addMetaLibrary(newLib);
					newLib.load();
					myProject.setCanClose(false);
				}
			} catch (MetaException E) {
				OpcatLogger.logError(E);
			}
		}
	};

	Action AddasPolicy = new AbstractAction("Add Template",
			StandardImages.Policies) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {

			String path = file.getAbsolutePath();

			try {
				SVNURL url = OpcatMCManager.getInstance(true).getFileURL(
						new File(path));
				if (url == null) {
					throw (new Exception(
							"Model not commited to MC, can not add as template"));
				}
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}

			try {
				if (myProject != null) {
					MetaLibrary newLib = myProject
							.getMetaManager()
							.createNewMetaLibraryReference(
									MetaLibrary.TYPE_POLICY,
									path,
									DataCreatorType.DATA_TYPE_OPCAT_LIBRARAY,
									DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE);
					newLib.load();
					myProject.getMetaManager().addMetaLibrary(newLib);
					myProject.setCanClose(false);
				}
			} catch (MetaException E) {
				OpcatLogger.logError(E);
			}
		}
	};

	public Action OpeninCurrentWindow = new AbstractAction(
			"Open in Current Window", StandardImages.REFRESH) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			try {
				GridPanel.RemoveALLPanels();

				FileControl.getInstance().loadFileWithOutFilesPrompt(
						file.getPath());

				if (myProject != null)
					myProject.setCanClose(false);
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}
	};

	Action OpeninNewWindow = new AbstractAction("Open in new Window",
			StandardImages.OPEN) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			try {
				FileControl.getInstance().runNewOPCAT(file);
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}
	};

	Action RefrashAction = new AbstractAction("Refresh View",
			StandardImages.REFRESH) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			((ModelsView) view).setRefrashState(View_RefreshState.ALL);
			;
			view.rebuildTree(null, null);
		}
	};

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
