package gui.repository;

import gui.images.repository.RepositoryImages;
import gui.opdProject.OpdProject;
import gui.projectStructure.MainStructure;
import gui.util.OpcatFile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JTree; //import javax.swing.tree.TreeSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * This is the base class for all possible views
 */
public abstract class BaseView extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum View_RefreshState {
		ALL, KEEP_OPEN, NONE
	};

	DefaultTreeModel treeModel;

	protected View_RefreshState refrashState = View_RefreshState.ALL;

	private int type;

	public View_RefreshState getRefrashState() {
		return refrashState;
	}

	public void setRefrashState(View_RefreshState refrashState) {
		this.refrashState = refrashState;
	}

	protected String viewName;

	protected String tip;

	protected Icon icon;

	protected final String fileSeparator = System.getProperty("file.separator");

	protected final String javaImagesPath = "images" + this.fileSeparator
			+ "javaimages" + this.fileSeparator;

	protected boolean isSameProject = false;

	protected Enumeration expandedDescendants;

	// private JTree tmpTree; // for saving tree
	protected Hashtable<HashKey, DefaultMutableTreeNode> nodesHash; // another

	private boolean canViewWitoutProject = false;

	// representation
	// of
	// nodes
	// in
	// tree
	// holds only non-leaf nodes

	public boolean isCanViewWitoutProject() {
		return canViewWitoutProject;
	}

	public void setCanViewWitoutProject(boolean canViewWitoutProject) {
		this.canViewWitoutProject = canViewWitoutProject;
	}

	public BaseView(String invisibleRoot) {
		super(new DefaultTreeModel(new DefaultMutableTreeNode(invisibleRoot)));
		// getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setRootVisible(false);
		this.setBackground(new Color(230, 230, 230));
		ToolTipManager.sharedInstance().registerComponent(this);
		this.putClientProperty("JTree.lineStyle", "Angled");
		this.setShowsRootHandles(true);
		this.treeModel = (DefaultTreeModel) this.getModel();

		IconCellRenderer locCellRenderer = new IconCellRenderer();
		locCellRenderer.setBorderSelectionColor(Color.RED);
		this.setCellRenderer(locCellRenderer);

		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) this
				.getCellRenderer();

		renderer.setLeafIcon(RepositoryImages.FOLDER_EMPTY);
		renderer.setClosedIcon(RepositoryImages.FOLDER_CLOSE);
		renderer.setOpenIcon(RepositoryImages.FOLDER_OPEN);
		renderer.setBackgroundNonSelectionColor(new Color(230, 230, 230));

		this.nodesHash = new Hashtable<HashKey, DefaultMutableTreeNode>();

		// tmpTree = new JTree(new DefaultTreeModel(new
		// DefaultMutableTreeNode(invisibleRoot)));
		this.toggleClickCount = 0;
		this.setRowHeight(24);
	}

	public String getName() {
		return this.viewName;
	}

	public String getTip() {
		return this.tip;
	}

	public Icon getIcon() {
		return this.icon;
	}

	void clearHistory() {
		this.nodesHash.clear();
	}

	public void clearTree() {
		// treeModel = (DefaultTreeModel)this.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.treeModel
				.getRoot();
		if (!root.isLeaf() && (root.getChildCount() > 0)) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
					.getChildAt(0);
			int childIndexes[] = { 0 };
			Object removedChildren[] = { node.getUserObject() };
			node.removeFromParent();
			this.treeModel
					.nodesWereRemoved(root, childIndexes, removedChildren);
		}
	}

	public void setRootComponent(OpdProject c) {
		this.treeModel = (DefaultTreeModel) this.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.treeModel
				.getRoot();
		DefaultMutableTreeNode subRoot = new DefaultMutableTreeNode(c);
		this.treeModel.insertNodeInto(subRoot, root, 0);
		this.nodesHash.put(new HashKey(c), subRoot);
	}

	public void rebuildTree(MainStructure s, OpdProject rootComponent) {
		this.getExpandedDescendants(rootComponent);
		this.clearTree();
		this.setRootComponent(rootComponent);
	}

	public void rebuildTree(OpdProject rootComponent) {
		this.getExpandedDescendants(rootComponent);
		this.clearTree();
		this.setRootComponent(rootComponent);
	}

	/**
	 * Saves the expanded state in tmp tree
	 */
	protected void getExpandedDescendants(OpdProject rootComponent) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.treeModel
				.getRoot();

		if (!root.isLeaf()) {
			OpdProject prevProject = (OpdProject) ((DefaultMutableTreeNode) root
					.getChildAt(0)).getUserObject();
			if (prevProject.getProjectId() == rootComponent.getProjectId()) {
				this.isSameProject = true;
				this.expandedDescendants = super
						.getExpandedDescendants(new TreePath(root));
				this.clearToggledPaths();
			}
		}
	}

	protected void expandRoot() {
		if (this.treeModel.getRoot() == null) {
			return;
		}
		TreePath rootPath = new TreePath(this.treeModel.getRoot());
		this.expandPath(rootPath);
	}

	// call this method if you want tree to be expanded as before rescaning
	protected void setExpandedDescendants() {
		this.expandRoot();
		if (this.isSameProject) {
			this.isSameProject = false;
			DefaultMutableTreeNode tmpNode, newNode;
			for (; this.expandedDescendants.hasMoreElements();) {
				tmpNode = (DefaultMutableTreeNode) ((TreePath) this.expandedDescendants
						.nextElement()).getLastPathComponent();
				newNode = null;
				try {
					newNode = (DefaultMutableTreeNode) this.nodesHash
							.get(new HashKey(tmpNode.getUserObject()));
				} catch (Exception ex) {
					//
				}
				if ((newNode != null)
						&& !this.isExpanded(new TreePath(newNode.getPath()))) {
					// System.err.println("New node " + newNode);
					this.expandPath(new TreePath(newNode.getPath()));
				}
			}
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void repaintKeepOpen(View_RefreshState endstate) {
		// now rebuild the tree keeping all open nodes expended

		ArrayList<Object> expended = saveExpendedUserObjects();
		refrashState = View_RefreshState.ALL;
		rebuildTree(null, null);
		expend(expended);
		refrashState = endstate;

	}

	protected void expend(ArrayList<Object> expended) {
		// now expend what was expended
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel()
				.getRoot();
		Enumeration<DefaultMutableTreeNode> rootEnum = root
				.depthFirstEnumeration();

		while (rootEnum.hasMoreElements()) {
			DefaultMutableTreeNode node = rootEnum.nextElement();
			if (node.getUserObject() instanceof OpcatFile) {
				Object obj = node.getUserObject();
				if (expended.contains(obj)) {
					expandPath(new TreePath(node.getPath()));
				}
			}
		}
	}

	protected ArrayList<Object> saveExpendedUserObjects() {

		Enumeration<DefaultMutableTreeNode> rootEnum = ((DefaultMutableTreeNode) this
				.getModel().getRoot()).depthFirstEnumeration();

		ArrayList<Object> expended = new ArrayList<Object>();
		while (rootEnum.hasMoreElements()) {
			DefaultMutableTreeNode node = rootEnum.nextElement();
			if (!this.isCollapsed(getRowForPath(new TreePath(node.getPath())))) {
				expended.add(node.getUserObject());
			}
		}

		return expended;
	}

	// public static DefaultMutableTreeNode findNodeInTree(JTree tree,
	// DefaultMutableTreeNode n, Comparator c)
	// {
	// TreeModel model = tree.getModel();
	// return localFindNodeInTree((DefaultMutableTreeNode)model.getRoot(),
	// n, c,
	// model);
	// }
	//
	// private static DefaultMutableTreeNode
	// localFindNodeInTree(DefaultMutableTreeNode tree,
	// DefaultMutableTreeNode
	// n, Comparator c, TreeModel model)
	// {
	// DefaultMutableTreeNode tn = null;
	// for(Enumeration e = tree.children(); e.hasMoreElements();)
	// {
	// tn = (DefaultMutableTreeNode)e.nextElement();
	// if(c.compare(n, tn) == 0)
	// {
	// return tn;
	// }
	// if(!model.isLeaf(tn))
	// {
	// return localFindNodeInTree(tn, n, c, model);
	// }
	// }
	// return null;
	// }
	//
	// private class NodesByNameComparator implements Comparator
	// {
	// public int compare(Object o1, Object o2)
	// {
	// DefaultMutableTreeNode n1 = (DefaultMutableTreeNode)o1;
	// DefaultMutableTreeNode n2 = (DefaultMutableTreeNode)o2;
	// if(n1.getUserObject().toString().equals(n2.getUserObject().toString()))
	// {
	// return 0;
	// }
	// return -1;
	// }
	// }
}
