package gui.actions.reports;

import exportedAPI.opcatAPIx.IXSystem;
import gui.actions.edit.EditAction;
import gui.dataProject.DataProject;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmThing;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessEntry;
import gui.util.opcatGrid.GridPanel;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.w3c.dom.events.EventException;

import com.sciapp.treetable.AggregateRow;
import com.sciapp.treetable.DefaultCellAggregator;
import com.sciapp.treetable.DynamicTreeTableModel;
import com.sciapp.treetable.TreeTableRow;

public class ProjectSummaryAction extends EditAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int metaColIndexStart;

	private int metaColIndexEnd;

	Object[] row = { "", "", "", "", "", "" };

	Object[] tag = { "", "" };

	GridPanel results = null;

	private ArrayList<String> colNames = new ArrayList<String>();

	public ProjectSummaryAction() {
		super("Project Summary", StandardImages.REPORT);
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			super.actionPerformed(arg0);
		} catch (EventException e) {
			JOptionPane.showMessageDialog(this.gui.getFrame(), e.getMessage()
					.toString(), "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}

		initGrid();

		Iterator<Entry> iter = Collections.list(
				edit.getCurrentProject().getSystemStructure().getElements())
				.iterator();

		while (iter.hasNext()) {
			Object obj = iter.next();

			if ((obj instanceof ObjectEntry) || (obj instanceof ProcessEntry)) {

				Entry ent = (Entry) obj;

				Iterator<Instance> insIter = Collections.list(ent.getInstances())
						.iterator();

				while (insIter.hasNext()) {
					Instance ins = (Instance) insIter.next();

					if ((ins.getOpd() != null)
							&& (ins.getOpd().getOpdId() != OpdProject.CLIPBOARD_ID)) {
						Object[] tag = new Object[2];
						tag[0] = ins.getKey();
						tag[1] = new Long(ins.getLogicalId());

						Object[] addedRow = new Object[colNames.size()];

						addedRow[0] = ins.getOpd().getName().replaceAll("\n",
								" ");
						addedRow[1] = ins.getEntry().getName().replaceAll("\n",
								" ");
						addedRow[2] = " ";
						if (ent instanceof ObjectEntry)
							addedRow[2] = "Object";
						if (ent instanceof ProcessEntry)
							addedRow[2] = "Process";
						addedRow[colNames.size() - 2] = Boolean.toString(ent
								.isEnvironmental());
						addedRow[colNames.size() - 1] = Boolean
								.toString(((OpmThing) ent.getLogicalEntity())
										.isPhysical());

						try {
							URL url;
							url = new URL(ins.getEntry().getUrl());
							addedRow[colNames.size() - 3] = url.toString();
						} catch (Exception ex) {
							addedRow[colNames.size() - 3] = " ";
							// OpcatLogger.logError(ex);
						}

						if ((ins instanceof ObjectInstance)) {
							OpmObject objEntry = (OpmObject) ins.getEntry()
									.getLogicalEntity();
							String mesUnit = objEntry.getMesurementUnit();
							Double minVal = objEntry
									.getMesurementUnitMinValue();
							Double maxUnit = objEntry
									.getMesurementUnitMaxValue();
							Double initialUnit = objEntry
									.getMesurementUnitInitialValue();
							String res = " ";
							if ((mesUnit != null)
									&& !mesUnit.trim().equalsIgnoreCase("")) {
								res = "[" + minVal + "," + maxUnit + "] "
										+ mesUnit + " (" + initialUnit + ") ";
							}
							addedRow[colNames.size() - 5] = res;
						} else {
							addedRow[colNames.size() - 5] = " ";
						}

						if (ent instanceof ProcessEntry) {
							OpmProcess process = (OpmProcess) ent.getLogicalEntity() ; 
							
							addedRow[colNames.size() - 4] = "[" + process.getMinTimeActivationString() + " : " + process.getMaxTimeActivationString()+ "]" ;
						} else {
							addedRow[colNames.size() - 4] = " ";
						}

						OpmThing opmThing = (OpmThing) ent.getLogicalEntity();

						Iterator<Role> roleIter = Collections.list(
								opmThing.getRolesManager().getLoadedRoles())
								.iterator();

						if (roleIter.hasNext()) {
							while (roleIter.hasNext()) {
								Role role = (Role) roleIter.next();
								MetaLibrary meta = edit.getCurrentProject()
										.getMetaManager().getMetaLibrary(
												role.getLibraryId());
								int i;
								for (i = metaColIndexStart; i <= metaColIndexEnd; i++) {
									if (meta.getProjectHolder() instanceof DataProject) {
										if (((String) colNames.get(i))
												.equalsIgnoreCase(((DataProject) meta
														.getProjectHolder())
														.getName())) {
											break;
										}
									}
									if (meta.getProjectHolder() instanceof OpdProject) {
										if (((String) colNames.get(i))
												.equalsIgnoreCase(((OpdProject) meta
														.getProjectHolder())
														.getCurrentModelType())) {
											break;
										}
									}
								}
								if ((addedRow[i] != null)
										&& !((String) addedRow[i])
												.equalsIgnoreCase(" ")) {
									addedRow[i] = addedRow[i]
											+ ","
											+ role.getThingName().replaceAll(
													"\n", " ");
								} else {
									addedRow[i] = role.getThingName()
											.replaceAll("\n", " ");
								}
							}

						}
						for (int j = 0; j < colNames.size(); j++) {
							if (addedRow[j] == null) {
								addedRow[j] = " ";
							}
						}
						results.getGrid().addRow(addedRow, tag);
					}
				}
			}
		}
		results.AddToExtensionToolsPanel();

	}

	private void initGrid() {
		colNames = new ArrayList<String>();
		colNames.add("OPD");
		colNames.add("Thing");
		colNames.add("Type");
		metaColIndexStart = 3;
		Iterator iter = Collections.list(
				edit.getCurrentProject()
						.getMetaLibraries(MetaLibrary.TYPE_CLASSIFICATION)).iterator();
		if (iter.hasNext()) {
			while (iter.hasNext()) {
				MetaLibrary meta = (MetaLibrary) iter.next();
				if (meta.getState() == MetaLibrary.STATE_LOADED) {
					colNames.add(((DataProject) meta.getProjectHolder())
							.getName());
				}
			}
		}
		Object[] types = edit.getCurrentProject().getModelTypesArray();
		for (int i = 0; i < types.length; i++) {
			colNames.add((String) types[i]);
		}
		colNames.add("Resources");
		colNames.add("Activation Time");
		colNames.add("URL");
		colNames.add("Env?");
		colNames.add("Physical?");
		metaColIndexEnd = colNames.size() - 6;
		this.results = new GridPanel(colNames);
		// results.setName("Project Summary") ;
		this.results.getGrid().setDuplicateRows(false);
		this.results.getButtonPane().add(new JLabel(""));
		this.results.getButtonPane().add(new JLabel(""));
		results.setInstanceTag((IXSystem) edit.getCurrentProject());
		results.getGrid().AddDefaultAggragator(true, true);
		results.setTabName("Project Summary");
		results.RemoveFromExtensionToolsPanel();
		results.ClearData();
	}

}

class HeaderAggregator extends DefaultCellAggregator {

	public Object getAggregateValue(AggregateRow row, int colIndex) {

		if (row.isAggregate()) {
			TreeTableRow child = (TreeTableRow) model.getChild(row, 0);
			String val = (String) ((Object[]) child.getUserObject())[colIndex];
			return val;
		}

		return super.getAggregateValue(row, colIndex);
	}

	DynamicTreeTableModel dtt;

	public HeaderAggregator(DynamicTreeTableModel model) {
		super(model);
		dtt = model;
	}
}