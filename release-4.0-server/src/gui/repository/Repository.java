package gui.repository;

import gui.Opcat2;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import java.awt.BorderLayout;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import gui.repository.OpcatOutLookBar;

;

public class Repository extends JPanel implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	Vector<BaseView> trees;

	OpdProject project = null;

	BaseView currentView;

	TreeSelectionListener opdSelectionEvent = null;

	TreeSelectionListener thingSelectionEvent = null;

	public final static int ModelsVIEW = 0;

	public final static int SVNView = 1;

	public final static int OPDVIEW = 2;

	public final static int OPDThingsVIEW = 3;

	public final static int ThingsVIEW = 4;

	public final static int MetaVIEW = 5;

	public final static int TestingVIEW = 6;

	public final static int ViewsView = 7;

	protected Opcat2 myOpcat2;

	// known views
	OpdThingsView opdThingsView;

	OpdView opdView;

	ThingView thingView;

	MetaView metaView;

	TestingView testingView;

	ModelsView modelsView;

	SVNTreeView svnView;

	ViewsView viewsView;

	OpcatOutLookBar outlookBar;

	public Repository(OpdProject prj, Opcat2 opcat2) {
		this.project = prj;

		this.setLayout(new BorderLayout());

		this.trees = new Vector<BaseView>();
		this.myOpcat2 = opcat2;

		outlookBar = new OpcatOutLookBar(this);
		this.add(outlookBar);

		DefaultMutableTreeNode p = null;

		// creating opdThingView
		if (this.project == null) {
			this.opdThingsView = new OpdThingsView("root"); // this is not
			// visible
			this.opdView = new OpdView("root"); // this is not visible
			this.thingView = new ThingView("root"); // this is not visible
			this.metaView = new MetaView("root"); // this is not visible
			// this.rolesView = new RolesView("root"); // this is not visible
			this.testingView = new TestingView("root");
			this.modelsView = new ModelsView("root");
			this.svnView = new SVNTreeView("root");
			this.viewsView = new ViewsView("root");

		} else {
			this.opdThingsView = new OpdThingsView("root"); // this is not
			// visible
			DefaultTreeModel model = (DefaultTreeModel) this.opdThingsView
					.getModel();
			model.insertNodeInto(new DefaultMutableTreeNode(this.project),
					(MutableTreeNode) model.getRoot(), 0);

			this.thingView = new ThingView("root"); // this is not visible
			model = (DefaultTreeModel) this.thingView.getModel();
			model.insertNodeInto(new DefaultMutableTreeNode(this.project),
					(MutableTreeNode) model.getRoot(), 0);

			this.opdView = new OpdView("root"); // this is not visible
			this.metaView = new MetaView("root");
			// this.rolesView = new RolesView("root");
			model = (DefaultTreeModel) this.opdView.getModel();
			p = new DefaultMutableTreeNode(this.project);
			model.insertNodeInto(p, (MutableTreeNode) model.getRoot(), 0);
			this.testingView = new TestingView("root");
			this.modelsView = new ModelsView("root");
			this.svnView = new SVNTreeView("root");
			this.viewsView = new ViewsView("root");

		}

		this.opdThingsView.addMouseListener(new RepositoryMouseListener(
				this.opdThingsView, this));
		this.opdView.addMouseListener(new RepositoryMouseListener(this.opdView,
				this));
		this.thingView.addMouseListener(new RepositoryMouseListener(
				this.thingView, this));
		this.metaView.addMouseListener(new RepositoryMouseListener(
				this.metaView, this));

		// this.rolesView.addMouseListener(new RepositoryMouseListener(
		// this.rolesView, this));

		this.testingView.addMouseListener(new RepositoryMouseListener(
				this.testingView, this));

		this.modelsView.addMouseListener(new RepositoryMouseListener(
				this.modelsView, this));

		this.svnView.addMouseListener(new RepositoryMouseListener(this.svnView,
				this));

		this.viewsView.addMouseListener(new RepositoryMouseListener(
				this.viewsView, this));

		this.trees.add(this.modelsView);
		this.trees.add(this.svnView);
		this.trees.add(this.opdView);
		// this.trees.add(this.opdThingsView);
		this.trees.add(this.thingView);
		this.trees.add(this.metaView);
		this.trees.add(this.testingView);
		// this.trees.add(this.viewsView) ;

		// this.trees.add(this.rolesView);

		// end creating opdThingView

		JScrollPane scroller;
		for (int i = 0; i < trees.size(); i++) {
			scroller = new JScrollPane((BaseView) this.trees.elementAt(i),
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			outlookBar.addBar(((BaseView) this.trees.elementAt(i)).viewName,
					scroller);
		}

		if (project == null) {
			this.changeView(ModelsVIEW);
		}
	}

	public void changeView(int i) {
		// JScrollPane scroller;

		// index is the order inwhich Views where added to the trees vector.
		// need to refactor this.

		int index = 0;

		// this.tabbedPane.removeAll();
		switch (i) {
		case ModelsVIEW:
			index = 0;
			break;
		case SVNView:
			index = 1;
			break;
		case OPDVIEW:
			index = 2;
			break;
		case OPDThingsVIEW:
			index = 3;
			break;
		case ThingsVIEW:
			index = 4;
			break;
		case MetaVIEW:
			index = 5;
			break;
		// case RolesVIEW:
		// index = 4;
		// break;
		case TestingVIEW:
			index = 6;
			break;
		case ViewsView:
			index = 7;
			break;

		}

		this.currentView = (BaseView) this.trees.elementAt(index);

		if (((this.project != null) || (currentView.isCanViewWitoutProject())))
			this.rebuildTrees(true);

		// this.tabbedPane.updateUI();

	}

	public void rebuildTrees(boolean current) {
		if (!(currentView.isCanViewWitoutProject()) && (this.project == null)) {
			for (Enumeration<BaseView> e = this.trees.elements(); e
					.hasMoreElements();) {
				BaseView view = (BaseView) e.nextElement();
				view.clearTree();
			}
			return;
		}

		// rescan current view only !!!!
		if (this.currentView == null) {
			this.currentView = this.opdView;
		}

		if (current) {
			if (this.project == null) {
				this.currentView.rebuildTree(null, null);
			} else {
				this.currentView.rebuildTree(this.project
						.getComponentsStructure(), this.project);
			}
		} else {
			for (Enumeration<BaseView> e = this.trees.elements(); e
					.hasMoreElements();) {
				BaseView view = (BaseView) e.nextElement();
				// view.clearTree();
				if (this.project == null) {
					view.rebuildTree(null, null);
				} else {
					view.rebuildTree(this.project.getComponentsStructure(),
							this.project);
				}
			}
		}

	}

	public void setProject(OpdProject prj) {

		this.project = prj;
		if (prj != null)
			this.rebuildTrees(true);
		changeView(OPDVIEW);
		outlookBar.setVisibleBar(OPDVIEW);
	}

	public OpdProject getProject() {
		return this.project;
	}

	public void clearHistory() {
		for (Enumeration<BaseView> e = this.trees.elements(); e
				.hasMoreElements();) {
			BaseView view = (BaseView) e.nextElement();
			view.clearHistory();
		}
	}

	public void stateChanged(ChangeEvent ce) {
	}

	public OpdThingsView getOpdThingView() {
		return this.opdThingsView;
	}

	public OpdView getOpdTreeView() {
		return this.opdView;
	}

	public MetaView getMetaView() {
		return this.metaView;
	}

	public TestingView getTestingView() {
		return testingView;
	}

	public ViewsView getViewsView() {
		return viewsView;
	}

	public ModelsView getModelsView() {
		return modelsView;
	}

	public SVNTreeView getSVNView() {
		return svnView;
	}

	public OpcatOutLookBar getOutlookBar() {
		return outlookBar;
	}
}

class SelectionEvent implements TreeSelectionListener {

	Repository repository = null;

	SelectionEvent(BaseView tree, Repository rep) {
		repository = rep;
	}

	public void valueChanged(TreeSelectionEvent event) {

		TreePath newSelPath = event.getNewLeadSelectionPath();

		DefaultMutableTreeNode newNode;

		if (newSelPath != null) {
			newNode = (DefaultMutableTreeNode) (newSelPath
					.getLastPathComponent());
			Object obj = newNode.getUserObject();
			if (obj instanceof Opd) {
				this.showOpd((Opd) obj);
			}
		}

		// IconCellRenderer renderer = myTree.getCellRenderer() ;

	}

	private void showOpd(Opd myOpd) {
		// myOpd.setVisible(true);
		repository.getProject().showOPD(myOpd.getOpdId());
		// try {
		// myOpd.getOpdContainer().setSelected(true);
		// myOpd.getOpdContainer().setMaximum(true);
		// } catch (java.beans.PropertyVetoException pve) {
		// pve.printStackTrace();
		// }
	}
}