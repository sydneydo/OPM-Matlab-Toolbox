package gui.dataProject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;

import com.sciapp.renderers.BooleanRenderer;
import com.sciapp.table.GroupTableColumn;
import com.sciapp.table.GroupTableHeader;
import gui.Opcat2;
import gui.metaLibraries.logic.Role;
import gui.opmEntities.OpmConnectionEdge;
import gui.opmEntities.OpmEntity;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmStructuralRelation;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.Entry;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.RelationEntry;
import gui.projectStructure.StateEntry;
import gui.projectStructure.ThingEntry;
import gui.util.opcatGrid.Grid;
import gui.util.opcatGrid.GridPanel;

public class MetaDataItemAnalisysPanel extends GridPanel implements
		ActionListener {
	/**
	 * 
	 */

	// cols.add("Thing");
	// cols.add("Connected Thing");
	// cols.add("Is Connected");
	// cols.add("Connection Type");
	private static final long serialVersionUID = 1L;

	ArrayList things;

	DataAbstractItem item;

	ArrayList headers;

	Role role;

	public void actionPerformed(ActionEvent e) {
		Iterator iter = things.iterator();
		HashMap connctedMap = new HashMap();
		this.ClearData();
		this.RemoveFromExtensionToolsPanel();

		while (iter.hasNext()) {

			Object obj;
			obj = iter.next();

			OpmEntity opmThing = (OpmEntity) obj;
			Entry thing;
			thing = (ConnectionEdgeEntry) Opcat2.getCurrentProject()
					.getSystemStructure().getEntry(opmThing.getId());

			connctedMap = thing.getConnectedThings();
			Iterator connectedIter = connctedMap.values().iterator();

			while (connectedIter.hasNext()) {
				Object[] array = (Object[]) connectedIter.next();
				ConnectionEdgeEntry connected = (ConnectionEdgeEntry) (array[0]);
				OpmProceduralLink link = (OpmProceduralLink) (array[1]);

				Object row[] = new Object[headers.size()];
				Object rowTag[] = new Object[2];

				String hed = "";
				row[0] = thing.getName();
				if (thing instanceof StateEntry) {
					hed = " (State of - "
							+ ((StateEntry) thing).getParentObject().getName()
							+ ")";
					row[0] = thing.getName() + hed;
				}

				hed = "";
				if (connected instanceof StateEntry) {
					hed = " (State of - "
							+ ((StateEntry) connected).getParentObject()
									.getName() + ")";
				}
				row[1] = connected.getName() + hed;

				Vector roles = connected.getLogicalEntity().getRolesManager()
						.getRolesVector(role.getOntology().getID());

				row[2] = "";
				for (int i = 0; i < roles.size(); i++) {
					Role locRole = (Role) roles.elementAt(i);
					if (i < roles.size() - 1) {
						row[2] = row[2] + locRole.getThingName() + ", ";
					} else {
						row[2] = row[2] + locRole.getThingName();
					}
				}

				boolean isConnected = false;
				Iterator roleIter;
				if (connected instanceof StateEntry) {
					roleIter = Collections.list(
							((StateEntry) connected).getParentObject()
									.getRoles()).iterator();
				} else {
					roleIter = Collections.list(
							((ThingEntry) connected).getRoles()).iterator();
				}
				while (roleIter.hasNext()) {
					Role tempRole = (Role) roleIter.next();

					if (tempRole.getThing() == null) {
						continue;
					}
					long tempID = tempRole.getThing().getId();

					if ((role.getLibraryId() == tempRole.getLibraryId())
							&& (item.getId() == tempID)) {
						isConnected = true;
						break;
					}
				}

				JCheckBox chk = new JCheckBox();
				BooleanRenderer cellRenderer = new BooleanRenderer(chk);
				getGrid().getColumnModel().getColumn(3).setCellRenderer(
						cellRenderer);
				row[3] = new Boolean(isConnected);
				row[4] = link.getName().substring(0,
						link.getName().indexOf(" "));

				/**
				 * a patch
				 */
				List allTthings = Collections.list(Opcat2.getCurrentProject()
						.getComponentsStructure().getElements());
				Iterator thingsIter = allTthings.iterator();
				while (thingsIter.hasNext()) {
					Entry entry = (Entry) thingsIter.next();
					OpmEntity opm = (OpmEntity) ((Entry) entry)
							.getLogicalEntity();

					// System.out.println(opm.getName() + ", " + opm.getId() + "
					// - " + link.getName() + ", " + link.getId());

					/**
					 * TODO: the loop over the entries is a patch as it seems
					 * that there are many entries to the same link. the bug
					 * seems to be when you change a link destination from a
					 * thing outside an inzoomed thing to a thing inside an
					 * inzoomed thing. is copy the same ? a new entry with the
					 * same name is created. .
					 * 
					 */
					if (opm.getName().equalsIgnoreCase(link.getName())) {
						roles = opm.getRolesManager().getRolesVector(
								role.getOntology().getID());
						row[5] = "";
						for (int i = 0; i < roles.size(); i++) {
							Role locRole = (Role) roles.elementAt(i);
							if (i < roles.size() - 1) {
								row[5] = row[5] + locRole.getThingName() + ", ";
							} else {
								row[5] = row[5] + locRole.getThingName();
							}
						}
						// if ( !((String) row[5]).equalsIgnoreCase("")) break ;
					}
				}

				rowTag[0] = connected.getLogicalEntity();
				rowTag[1] = " ";
				this.getGrid().addRow(row, rowTag);
			}
		}
		AddToExtensionToolsPanel();

	}

	public MetaDataItemAnalisysPanel(ArrayList cols, DataAbstractItem req,
			ArrayList connctedThings, Role role) {
		super(cols);
		this.RemoveFromExtensionToolsPanel();
		this.ClearData();
		setTabName("Analysys : " + req.getName() + "(" + req.getExtID() + ")");

		GroupTableHeader gth = (GroupTableHeader) getGrid().getTableHeader();
		GroupTableColumn gc = new GroupTableColumn("Connected Thing Details");
		gc.addColumn(GetColumnsModel().getColumn(1));
		gc.addColumn(GetColumnsModel().getColumn(2));
		gth.addGroupColumn(gc);
		gc = new GroupTableColumn("Connection Details");
		gc.addColumn(GetColumnsModel().getColumn(4));
		gc.addColumn(GetColumnsModel().getColumn(5));
		gth.addGroupColumn(gc);

		item = req;
		things = connctedThings;
		headers = cols;
		this.role = role;
		getGrid().addMouseListener(new MouseListner(this));
		this.getGrid().setDuplicateRows(false);
		cleanThingsVector();
	}

	/**
	 * removes the links from the vector and adds thier connection edges to it
	 * 
	 */
	private void cleanThingsVector() {

		Vector loc = new Vector();
		Vector removed = new Vector();

		for (int i = 0; i < things.size(); i++) {
			OpmEntity opm = (OpmEntity) things.get(i);
			if (!(opm instanceof OpmConnectionEdge)) {
				if (opm instanceof OpmProceduralLink) {
					OpmProceduralLink link = (OpmProceduralLink) opm;
					LinkEntry entry = (LinkEntry) Opcat2.getCurrentProject()
							.getSystemStructure().getEntry(link.getId());
					Enumeration insEnum = entry.getInstances();
					while (insEnum.hasMoreElements()) {
						LinkInstance ins = (LinkInstance) insEnum.nextElement();
						loc.add(ins.getSourceInstance().getEntry()
								.getLogicalEntity());
						loc.add(ins.getDestinationInstance().getEntry()
								.getLogicalEntity());
					}
				} else if (opm instanceof OpmStructuralRelation) {
					OpmStructuralRelation rel = (OpmStructuralRelation) opm;
					RelationEntry entry = (RelationEntry) Opcat2
							.getCurrentProject().getSystemStructure().getEntry(
									rel.getId());
					Enumeration insEnum = entry.getInstances();
					while (insEnum.hasMoreElements()) {
						GeneralRelationInstance ins = (GeneralRelationInstance) insEnum
								.nextElement();
						loc.add(ins.getSourceInstance().getEntry()
								.getLogicalEntity());
						loc.add(ins.getDestinationInstance().getEntry()
								.getLogicalEntity());
					}
				}
				removed.add(new Integer(i));

			}
		}

		for (int i = removed.size() - 1; i >= 0; i--) {
			Integer integer = (Integer) removed.elementAt(i);
			things.remove(integer.intValue());
		}

		for (int i = 0; i < loc.size(); i++) {
			things.add(loc.elementAt(i));
		}

	}

	private void showThings() {
		Grid table = getGrid();
		if (table == null)
			return;
		Vector things = new Vector();
		// table is the TreeTable model
		int[] selectedRows = table.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			Object[] tag = table.GetTag(selectedRows[i]);
			// OpmThing theThing = (OpmThing) tag[0];
			things.add(tag[0]);
		}
		Entry entry = (Entry) Opcat2.getCurrentProject().getSystemStructure()
				.getElements().nextElement();
		entry.ShowInstances(things);
	}

	class MouseListner extends MouseAdapter {
		GridPanel panel;

		public MouseListner(GridPanel panel) {
			super();
			this.panel = panel;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				showThings();
			}
		}
	}
}
