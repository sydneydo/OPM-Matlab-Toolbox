package gui.repository;

import gui.images.standard.StandardImages;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.projectStructure.Entry;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class OpdThingsView extends BaseView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public OpdThingsView(String invisibleRoot) {
		super(invisibleRoot);
		this.tip = new String("Things in OPD");
		this.viewName = "Things in OPD";
		this.setType(Repository.OPDThingsVIEW);

		// icon = RepositoryImages.OPD;
		this.icon = StandardImages.System_Icon;
	}

	public void rebuildTree(MainStructure s, OpdProject rootComponent) {

		super.rebuildTree(s, rootComponent);
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		DefaultMutableTreeNode prjRoot = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) model
				.getRoot()).getChildAt(0);

		DefaultMutableTreeNode tmpOpdNode;
		TreeMap opdSorter = new TreeMap();
		Hashtable sorterHt = new Hashtable();
		for (Enumeration e = s.getOpds(); e.hasMoreElements();) {
			Object o = e.nextElement();
			opdSorter.put(o.toString(), o);
			sorterHt.put(new Long(((Opd) o).getOpdId()), new TreeMap());
		}

		// add OPDs we now that this is an OPD
		for (Iterator i = opdSorter.values().iterator(); i.hasNext();) {
			tmpOpdNode = new DefaultMutableTreeNode(i.next());
			model.insertNodeInto(tmpOpdNode, prjRoot, prjRoot.getChildCount());
			// LERA
			TreePath nodes = new TreePath(tmpOpdNode.getPath());
			this.addSelectionPath(nodes);
			// END
			this.nodesHash.put(new HashKey(tmpOpdNode.getUserObject()),
					tmpOpdNode);
		}

		// separate things by OPD and sort it
		for (Enumeration e = s.getElements(); e.hasMoreElements();) {
			Entry ent = (Entry) e.nextElement();
			if (ent instanceof ThingEntry) {
				this.put2sorter(sorterHt, ent);
			}
		}

		// put things into tree
		long opdId;
		TreeMap sortedThings;
		DefaultMutableTreeNode currFather;

		for (int j = 0; j < prjRoot.getChildCount(); j++) {
			currFather = (DefaultMutableTreeNode) prjRoot.getChildAt(j);
			opdId = ((Opd) (currFather.getUserObject())).getOpdId();
			sortedThings = (TreeMap) (sorterHt.get(new Long(opdId)));

			// put things into needed OPD tree entry
			for (Iterator i = sortedThings.values().iterator(); i.hasNext();) {
				model.insertNodeInto(new DefaultMutableTreeNode(i.next()),
						currFather, currFather.getChildCount());
			}
		}
		this.setExpandedDescendants();
	}

	private void put2sorter(Hashtable sorter, Entry ent) {
		long opdId;
		for (Enumeration e = ent.getInstances(); e.hasMoreElements();) {
			ThingInstance inst = (ThingInstance) e.nextElement();
			opdId = inst.getKey().getOpdId();
			try {
				if (opdId != OpdProject.CLIPBOARD_ID) {
					if (inst instanceof ObjectInstance) {
						((TreeMap) (sorter.get(new Long(opdId)))).put(
								new String("O" + inst.getThingName()), inst);
						// ((TreeMap)(sorter.get(new Long(opdId)))).put(key,
						// inst);
					} else // ProcessInstance
					{
						((TreeMap) (sorter.get(new Long(opdId)))).put(
								new String("P" + inst.getThingName()), inst);
					}
				}
			} catch (Exception ex) {
				if (inst != null) {
					if (inst.getOpd() != null) {
						OpcatLogger.logError(inst.getOpd().getName() + " "
								+ inst.getThingName() + " ");
					} else {
						OpcatLogger.logError("id -" + inst.getKey()
								+ " name - " + inst.getThingName()
								+ " OPD is null ");
					}
				} else {
					OpcatLogger.logError(opdId + " " + "Inst IS NULL "
							+ ent.getName());
				}
			}
		}
	}
}
