package gui.repository;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.OpdProject;
import gui.projectStructure.MainStructure;
import gui.scenarios.Scenario;
import gui.scenarios.Scenarios;

public class TestingView extends BaseView {

	public TestingView(String invisibleRoot) {
		super(invisibleRoot);
		this.tip = new String("Manage testing scnerios");
		this.viewName = "Testing";
		this.icon = StandardImages.System_Icon;
		this.setType(Repository.TestingVIEW);
	}

	public void rebuildTree(MainStructure s, OpdProject opdProject) {

		super.rebuildTree(opdProject);

		DefaultTreeModel model = (DefaultTreeModel) this.getModel();

		DefaultMutableTreeNode prjRoot = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) model
				.getRoot()).getChildAt(0);

		Scenarios sceneriosMap = opdProject.getScen();

		DefaultMutableTreeNode scenRoot = new DefaultMutableTreeNode(opdProject
				.getScen());
		model.insertNodeInto(scenRoot, prjRoot, prjRoot.getChildCount());

		// sortscenrios
		TreeMap sorterScen = new TreeMap();
		for (Iterator e = sceneriosMap.keySet().iterator(); e.hasNext();) {
			String scenName = (String) e.next();
			Scenario scen = sceneriosMap.get(scenName);
			sorterScen.put(scenName, scen);
		}

		for (Iterator scens = sorterScen.values().iterator(); scens.hasNext();) {

			Scenario scen = (Scenario) scens.next();

			DefaultMutableTreeNode tmpOpdNode;
			tmpOpdNode = new DefaultMutableTreeNode(scen);
			model
					.insertNodeInto(tmpOpdNode, scenRoot, scenRoot
							.getChildCount());

			// LERA
			TreePath nodes = new TreePath(tmpOpdNode.getPath());
			this.addSelectionPath(nodes);
			// END

			this.nodesHash.put(new HashKey(tmpOpdNode.getUserObject()
					.toString()), tmpOpdNode);

			// put things into tree
			DefaultMutableTreeNode currFather;

			currFather = tmpOpdNode;

			// sort objects
			TreeMap sorterHt = new TreeMap();
			for (Enumeration e = scen.getSettings().getMetaLibraries(); e
					.hasMoreElements();) {
				MetaLibrary ent = (MetaLibrary) e.nextElement();
				sorterHt.put(ent.getName(), ent);

			}

			// put things into needed OPD tree entry
			for (Iterator i = sorterHt.values().iterator(); i.hasNext();) {
				model.insertNodeInto(new DefaultMutableTreeNode(i.next()),
						currFather, currFather.getChildCount());
			}
			this.setExpandedDescendants();
		}
		this.setExpandedDescendants();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
