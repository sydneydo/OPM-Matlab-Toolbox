package gui.repository;

import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.OpdProject;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ProcessEntry;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class MetaView extends BaseView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	OpdProject myProject;

	Object[] row = { "", "", "" };

	Object[] tag = { "", "" };

	GridPanel results = null;

	public MetaView(String invisibleRoot) {
		super(invisibleRoot);
		this.tip = new String("Templates");
		this.viewName = "Templates";
		this.icon = StandardImages.LIBRARIES;
		this.setType(Repository.MetaVIEW);

		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("Name");
		colNames.add("Description");
		colNames.add("URL");
		this.results = new GridPanel(colNames);
		this.results.getGrid().setDuplicateRows(false);
		this.results.getButtonPane().add(new JLabel(""));
		this.results.getButtonPane().add(new JLabel(""));
		this.results.getButtonPane().add(new JLabel(""));
		this.results.getButtonPane().add(new JLabel(""));
		this.results.getButtonPane().add(new JLabel(""));

		this.setRowHeight(25);
	}

	public void rebuildTree(MainStructure s, OpdProject rootComponent) {

		super.rebuildTree(rootComponent);
		this.myProject = rootComponent;

		myProject.getMetaManager().setAllLibstoReferenced();
		// if (Opcat2.getLoadedfilename() != null) {
		myProject.getMetaManager().refresh(myProject, new JProgressBar());
		// }
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();

		DefaultMutableTreeNode prjRoot = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) model
				.getRoot()).getChildAt(0);

		Object[] types = new Object[myProject.getModelTypesArray().length];
		types = myProject.getModelTypesArray();

		DefaultMutableTreeNode[] typesNodes = new DefaultMutableTreeNode[types.length];
		for (int i = 0; i < types.length; i++) {
			typesNodes[i] = new DefaultMutableTreeNode((String) types[i]);
			model.insertNodeInto(typesNodes[i], prjRoot, prjRoot
					.getChildCount());
		}

		Iterator libreries = Collections.list(
				rootComponent.getMetaLibraries(MetaLibrary.TYPE_POLICY))
				.iterator();
		while (libreries.hasNext()) {
			try {
				MetaLibrary meta = (MetaLibrary) libreries.next();
				if (!meta.isHidden()) {

					// if(meta.isPolicyLibrary()) continue ;
					DefaultMutableTreeNode tmpOpdNode;
					tmpOpdNode = new DefaultMutableTreeNode(meta);

					if (meta.getState() == MetaLibrary.STATE_LOADED) {

						OpdProject metaPrj = (OpdProject) meta
								.getProjectHolder();
						DefaultMutableTreeNode father = new DefaultMutableTreeNode();
						father = prjRoot;

						String metaType = metaPrj.getCurrentModelType();

						// get type
						int j = 0;
						while (j < typesNodes.length) {
							String type = (String) typesNodes[j]
									.getUserObject();
							if (type.equalsIgnoreCase(metaType))
								break;
							j++;
						}

						if (j == typesNodes.length) {
							father = typesNodes[0];
						} else {
							father = typesNodes[j];
						}

						model.insertNodeInto(tmpOpdNode, father, father
								.getChildCount());

						// LERA
						TreePath nodes = new TreePath(tmpOpdNode.getPath());
						this.addSelectionPath(nodes);
						// END

						this.nodesHash.put(new HashKey(tmpOpdNode
								.getUserObject()), tmpOpdNode);
						TreeMap sorterHt = new TreeMap();
						// sort things
						try {
							Iterator e = null;
							e = meta.getRolesCollection().iterator();
							while (e.hasNext()) {
								Role role = (Role) e.next();

								Long entID = (Long) role.getThing()
										.getAdditionalData();
								Object ent = ((OpdProject) role.getOntology()
										.getProjectHolder())
										.getSystemStructure().getEntry(entID);
								// System.out.println(ent.getClass().getCanonicalName()
								// ) ;
								// if (ent instanceof ThingEntry) {
								if (ent instanceof ProcessEntry) {
									ProcessEntry process = (ProcessEntry) ent;
									sorterHt.put("P" + process.getName(), ent);
								}
								if (ent instanceof ObjectEntry) {
									ObjectEntry object = (ObjectEntry) ent;
									sorterHt.put("O" + object.getName(), ent);
								}

								// }
							}
						} catch (Exception ex) {
							OpcatLogger.logError(ex);
						}

						// put things into tree
						DefaultMutableTreeNode currFather;

						currFather = tmpOpdNode;

						for (Iterator i = sorterHt.values().iterator(); i
								.hasNext();) {
							DefaultMutableTreeNode curr = new DefaultMutableTreeNode(
									i.next());
							model.insertNodeInto(curr, currFather, currFather
									.getChildCount());
						}
					}
				}
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}
		// delete the empty types nodes.
		for (int i = 0; i < typesNodes.length; i++) {
			try {
				if (typesNodes[i].getChildCount() <= 0) {
					model.removeNodeFromParent(typesNodes[i]);
				}
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}
		//
		this.setExpandedDescendants();
	}
}
