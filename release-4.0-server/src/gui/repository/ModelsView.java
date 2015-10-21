package gui.repository;

import java.io.File;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import util.Configuration;

import gui.images.standard.StandardImages;
import gui.opdProject.OpdProject;
import gui.projectStructure.MainStructure;
import gui.util.OpcatFile;
import gui.util.OpcatFileNameFilter;
import gui.util.OpcatLogger;

public class ModelsView extends BaseView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2888245865859717591L;
	private boolean allowServerAccess = false;

	public ModelsView(String invisibleRoot) {
		super(invisibleRoot);

		setRootVisible(true);
		this.tip = new String("Manage models");
		this.viewName = "Models";
		this.icon = StandardImages.System_Icon;
		setCanViewWitoutProject(true);
		this.setType(Repository.ModelsVIEW);
	}

	public void rebuildTree(MainStructure s, OpdProject opdProject) {

		if (refrashState == View_RefreshState.NONE) {
			return;
		}

		if (refrashState == View_RefreshState.KEEP_OPEN) {
			repaintKeepOpen();
			refrashState = View_RefreshState.NONE;
			return;
		}

		refrashState = View_RefreshState.NONE;

		clearTree();

		DefaultTreeModel model = (DefaultTreeModel) this.getModel();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Models");
		model.setRoot(root);

		try {
			ListIterator<String> iter = Configuration.getInstance()
					.getUserDirectories();

			while (iter.hasNext()) {
				String path = iter.next();
				File file = new File(path);
				if (file.exists() && file.isDirectory()) {
					addNodes(root, file, path.equalsIgnoreCase(Configuration
							.getInstance().getProperty("models_directory")));
				} else {
					Configuration.getInstance()
							.removeDirectoryToUserDirectories(path);
				}
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		expandRoot();

	}

	/** Add nodes from under "dir" into curTop. Highly recursive. */
	DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir,
			boolean isWorkingCopyDir) {

		try {
			String curPath = dir.getPath();

			DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(
					new OpcatFile(curPath, isWorkingCopyDir));

			if (curTop != null) { // should only be null at root
				curTop.add(curDir);
			}
			Vector<String> ol = new Vector<String>();
			// It is also possible to filter the list of returned files.
			// This example does not return any files that start with `.'.
			OpcatFileNameFilter filter = new OpcatFileNameFilter(true, true);

			String[] tmp = dir.list(filter);

			if (tmp != null) {

				for (int i = 0; i < tmp.length; i++)
					ol.addElement(tmp[i]);
				Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
			}

			File f;
			Vector<OpcatFile> files = new Vector<OpcatFile>();
			// Make two passes, one for Dirs and one for Files. This is #1.
			for (int i = 0; i < ol.size(); i++) {
				String thisObject = ol.elementAt(i);
				String newPath;
				if (curPath.equals("."))
					newPath = thisObject;
				else
					newPath = curPath + File.separator + thisObject;
				if ((f = new File(newPath)).isDirectory())
					addNodes(curDir, f, isWorkingCopyDir);
				else {
					OpcatFile file = new OpcatFile(curPath + File.separator
							+ thisObject, isWorkingCopyDir);
					files.addElement(file);
				}
			}
			// Pass two: for files.
			for (int fnum = 0; fnum < files.size(); fnum++)
				curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
			return curDir;
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
			return null;
		}
	}

	public void repaintKeepOpen() {
		// now rebuild the tree keeping all open nodes expended
		super.repaintKeepOpen(View_RefreshState.NONE);
	}

	public void setAllowServerAccess(boolean allowServerAccess) {
		this.allowServerAccess = allowServerAccess;
	}

	public boolean isAllowServerAccess() {
		return allowServerAccess;
	}

}
