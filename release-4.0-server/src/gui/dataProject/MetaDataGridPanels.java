package gui.dataProject;

import gui.Opcat2;
import gui.controls.FileControl;
import gui.controls.GuiControl;

import com.sciapp.renderers.BooleanRenderer;
import com.sciapp.renderers.ProgressBarRenderer;
import com.sciapp.table.SortTableModel;
import com.sciapp.treetable.*;

import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaException;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.util.opcatGrid.Grid;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;

public class MetaDataGridPanels {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 
	 */

	private HashMap gridPanels = new HashMap();

	private static MetaDataGridPanels instance = new MetaDataGridPanels();

	private MetaDataGridPanels() {
	}

	private MetaDataGridPanel InitGrid(DataProject prj) {

		if ((gridPanels != null) && gridPanels.containsKey(prj.getName())) {
			return (MetaDataGridPanel) gridPanels.get(prj.getName());
		}

		MetaDataGridPanel gridPanel;

		// gridPanel.AllignButtons() ;
		ArrayList<String> colNames = new ArrayList<String>();
		for (int i = 0; i < prj.getHeaders().size(); i++) {
			colNames.add(prj.getHeaders().elementAt(i).toString());
		}
		colNames.add("Connected?");
		colNames.add("Things");

		gridPanel = new MetaDataGridPanel(colNames, prj);

		// gridPanel.getGrid().getSortedModel().setComparator(0,
		// new ReqComparator());
		JButton btnConnect = new JButton("   Connect   ");
		btnConnect.setIcon(StandardImages.META_CONNECT);
		btnConnect.addActionListener(new ConnectButton(this));
		gridPanel.getButtonPane().add(btnConnect);

		gridPanel.getGrid().addMouseListener(new MouseListner(gridPanel));

		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setIcon(StandardImages.META_DISCONNECT);
		gridPanel.getButtonPane().add(btnDisconnect);
		gridPanel.getButtonPane().add(new JLabel(""));
		btnDisconnect.addActionListener(new DisconnectButton(this));
		JButton btnUnconnected = new JButton("Not Assigned");
		gridPanel.getButtonPane().add(btnUnconnected);
		btnUnconnected.addActionListener(new UnconnectedButton(this));

		JButton btnMissing = new JButton("Missing");
		gridPanel.getButtonPane().add(btnMissing);
		btnMissing.addActionListener(new MissingButton(this));

		gridPanel.setTabName(prj.getName());

		gridPanels.put(prj.getName(), gridPanel);
		return gridPanel;

	}

	public void actionPerformed(ActionEvent action) {
		if (!FileControl.getInstance().isProjectOpened()) {
			JOptionPane
					.showMessageDialog(GuiControl.getInstance().getFrame(),
							"No system is opened", "Message",
							JOptionPane.ERROR_MESSAGE);
			return;
		}
		showMetaDataProjects();

	}

	public void showMeta(MetaLibrary ont, DataProject metadata) {

		MetaDataGridPanel gridPanel = InitGrid(metadata);

		gridPanel.ClearData();
		gridPanel.AddToExtensionToolsPanel();

		// try {
		Vector allRoles = new Vector();
		try {
			allRoles.addAll(ont.getRolesCollection());
		} catch (MetaException ex) {
			System.out.println(ex.getMessage());
		}

		boolean coloring = (metadata).isColoring();
		int colorIdx = -1;
		if (coloring) {
			colorIdx = (metadata).getColoringIndex();
			if (metadata.isShowColorValueAsPrograssBar()) {
				JProgressBar bar = new JProgressBar(0, (metadata)
						.getMaxColoringLevelValue());
				ProgressBarRenderer cellRenderer = new ProgressBarRenderer(bar);
				gridPanel.getGrid().getColumnModel().getColumn(colorIdx)
						.setCellRenderer(cellRenderer);
			}
		}

		int connectedIndex = (metadata).getHeaders().size();
		JCheckBox chk = new JCheckBox();
		BooleanRenderer cellRenderer = new BooleanRenderer(chk);
		gridPanel.getGrid().getColumnModel().getColumn(connectedIndex)
				.setCellRenderer(cellRenderer);

		// get a hash map of (req, thingsVector connected to it)

		Iterator iter = allRoles.iterator();
		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			DataAbstractItem req = role.getThing();
			// MetaDataItem req;
			// if (dataIns.getDataInstance() instanceof MetaDataItem) {
			// req = (MetaDataItem) dataIns.getDataInstance();
			// } else {
			// OpmEntity opm = (OpmEntity) dataIns.getDataInstance();
			// req = metadata.getMetaDataItem(opm.getId());
			// }

			if (req != null) {
				// get connected thing.

				// first we need to add an aggragator row for the thig
				// coloumn
				Object[] row = new Object[req.getAllData().size() + 2];
				Object[] rowTag = new Object[2];
				for (int i = 0; i < req.getAllData().size(); i++) {
					row[i] = req.getAllData().get(i);
				}

				String things = req.connectedThingstoString(role, Opcat2
						.getCurrentProject());

				if (things.equalsIgnoreCase("")) {
					row[req.getAllData().size()] = new Boolean(false);
				} else {
					row[req.getAllData().size()] = new Boolean(true);
				}

				row[req.getAllData().size() + 1] = things;
				rowTag[0] = role; // set to thing real tag if it exists
				rowTag[1] = req; // //set to thing real tag if it exists

				gridPanel.getGrid().addRow(row, rowTag);
			}

		}

		gridPanel.getGrid().getSortedModel().sort(0, SortTableModel.ADD_SORT);

		// } catch (MetaException E) {
		// JOptionPane.showMessageDialog(this, E.getMessage(), "Message",
		// JOptionPane.ERROR_MESSAGE);
		// OpcatLogger.logError(E);
		// }
	}

	public void showMetaDataProjects() {
		// set a style for header and footer rows

		// file.getCurrentProject().getMetaManager().loadDummyReqProject();

		Enumeration ontologies = FileControl.getInstance().getCurrentProject()
				.getMetaManager().getMetaLibraries();
		while (ontologies.hasMoreElements()) {

			MetaLibrary ont = (MetaLibrary) ontologies.nextElement();
			if (!ont.isPolicy()) {
				showMeta(ont, ont.getDataProject());
			}

		}

	}

	private Grid getCurrentTable() {

		Object panel = GuiControl.getInstance().getExtensionToolsPane()
				.getSelectedComponent();
		if (panel instanceof GridPanel) {
			Grid table = ((GridPanel) panel).getGrid();
			return table;
		} else {
			return null;
		}

	}

	private void connect() {

		Grid table = getCurrentTable();
		if (table == null)
			return;
		// table is the TreeTable model
		int[] selectedRows = table.getSelectedRows();
		TreeTableModelAdapter ttm = (TreeTableModelAdapter) table.getModel();

		for (int row = 0; row < selectedRows.length; row++) {

			TreeTableRow treeRow = (TreeTableRow) ttm
					.nodeForRow(selectedRows[row]);

			if (treeRow.isLeaf() && !treeRow.isFooter()) {
				List thingsList = Collections
						.list(FileControl.getInstance().getCurrentProject()
								.getCurrentOpd().getSelectedItems());

				Iterator selectedIter = thingsList.iterator();

				Object[] tag = table.GetTag(selectedRows[row]);
				Role role = (Role) tag[0];

				while (selectedIter.hasNext()) {
					Object obj = selectedIter.next();
					if ((obj instanceof Instance)) {
						OpmEntity theThing = (OpmEntity) ((Instance) obj)
								.getEntry().getLogicalEntity();
						theThing.getRolesManager().addRole(role);
					}
				}
				if (thingsList.size() > 0) {
					DataAbstractItem req = (DataAbstractItem) tag[1];
					Object[] data = (Object[]) treeRow.getUserObject();
					data[data.length - 2] = new Boolean(true);
					data[data.length - 1] = req.connectedThingstoString(role,
							Opcat2.getCurrentProject());
				}
			}
		}
		getCurrentTable().getSortedModel().fireTableDataChanged();
		Opcat2.getCurrentProject().setCanClose(false);
		Opcat2.getFrame().repaint();
	}

	private void disconnect() {

		Grid table = getCurrentTable();
		if (table == null)
			return;
		// table is the TreeTable model
		int[] selectedRows = table.getSelectedRows();
		TreeTableModelAdapter ttm = (TreeTableModelAdapter) table.getModel();
		// List treeList = ttm.getRows();

		for (int row = 0; row < selectedRows.length; row++) {
			TreeTableRow treeRow = (TreeTableRow) ttm
					.nodeForRow(selectedRows[row]);
			Object[] tag = table.GetTag(selectedRows[row]);
			Role role = (Role) tag[0];
			try {
				role.load(role.getOntology());
			} catch (Exception e) {

			}

			List selectedThings = Collections.list(FileControl.getInstance()
					.getCurrentProject().getCurrentOpd().getSelectedItems());
			Iterator selectedIter = selectedThings.iterator();
			while (selectedIter.hasNext()) {
				Object obj = selectedIter.next();
				if ((obj instanceof Instance)) {
					OpmEntity theThing = (OpmEntity) ((Instance) obj)
							.getEntry().getLogicalEntity();
					theThing.getRolesManager().removeRole(role);
				}
			}
			if (selectedThings.size() > 0) {
				DataAbstractItem req = (DataAbstractItem) tag[1];
				Object[] data = (Object[]) treeRow.getUserObject();
				String str = req.connectedThingstoString(role, Opcat2
						.getCurrentProject());
				data[data.length - 1] = str;
				if (str.equalsIgnoreCase("")) {
					data[data.length - 2] = new Boolean(false);
				}
			}
		}
		getCurrentTable().getSortedModel().fireTableDataChanged();
		Opcat2.getCurrentProject().setCanClose(false);
		Opcat2.getFrame().repaint();
	}

	/**
	 * returns a map of (req, Conected Things)
	 * 
	 * @param reqExtID
	 * @return
	 */

	private void unconnected() {

		Vector things = new Vector();

		Iterator elementsIter = Collections.list(
				FileControl.getInstance().getCurrentProject()
						.getSystemStructure().getElements()).iterator();

		Entry ent = null;
		while (elementsIter.hasNext()) {
			Object obj = elementsIter.next();
			if ((obj instanceof Entry)) {
				ent = (Entry) obj;
				if (!ent.getRoles(MetaLibrary.TYPE_CLASSIFICATION).hasMoreElements()) {
					things.add(ent.getLogicalEntity());
				}
			}
		}

		if (ent != null)
			ent.ShowInstances(things);
	}

	private void missing() {
		Vector things = new Vector();

		Iterator elementsIter = Collections.list(
				FileControl.getInstance().getCurrentProject()
						.getSystemStructure().getElements()).iterator();
		Entry ent = null;
		while (elementsIter.hasNext()) {
			Object obj = elementsIter.next();
			if ((obj instanceof Entry)) {
				ent = (Entry) obj;
				OpmEntity opm = (OpmEntity) ent.getLogicalEntity();
				if (opm.getRolesManager().getNotLoadedRolesVector(
						MetaLibrary.TYPE_CLASSIFICATION).size() > 0) {
					things.add(ent.getLogicalEntity());
				}
			}
		}

		if (ent != null)
			ent.ShowInstances(things);

	}

	private void rightClickEvent(GridPanel panel, MouseEvent e) {
		JPopupMenu metaDataPopupMenu = panel.getRMenu();

		if (panel.getGrid().getSelectedRow() >= 0) {
			Object[] tag = new Object[2];
			tag = panel.getGrid().GetTag(panel.getGrid().getSelectedRow());
			Role role = (Role) tag[0];
			DataAbstractItem req = (DataAbstractItem) tag[1];
			JMenuItem analyze = new JMenuItem("Analyze : " + req.getName());
			metaDataPopupMenu.add(analyze);
			ArrayList<String> cols = new ArrayList<String>();
			cols.add("Thing");
			cols.add("Name");
			cols.add("Connected To");
			cols.add("Is Connected");
			cols.add("Type");
			cols.add("Connected To");
			analyze
					.addActionListener(new MetaDataItemAnalisysPanel(cols, req,
							req.connectedThings(role, Opcat2
									.getCurrentProject()), role));

			if (role.getOntology().getProjectHolder() instanceof OpdProject) {

				OpdProject meta = (OpdProject) role.getOntology()
						.getProjectHolder();
				Long entryID = (Long) role.getThing().getAdditionalData();
				Entry entry = meta.getSystemStructure().getEntry(
						entryID.longValue());

				JMenuItem menuItem = new JMenuItem("Show Appearances");
				menuItem.addActionListener(new ShowInstancesAction(entry));
				metaDataPopupMenu.add(new JSeparator());
				metaDataPopupMenu.add(menuItem);
			}
		}

		metaDataPopupMenu.show(e.getComponent(), e.getX(), e.getY());

	}

	private void showThings() {

		Grid table = getCurrentTable();
		if (table == null)
			return;
		Vector things = new Vector();
		// table is the TreeTable model
		int[] selectedRows = table.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {

			Object[] tag = table.GetTag(selectedRows[i]);
			DataAbstractItem req = (DataAbstractItem) tag[1];
			ArrayList connectedThings = req.connectedThings((Role) tag[0],
					Opcat2.getCurrentProject());
			Iterator thingsIter = connectedThings.iterator();
			while (thingsIter.hasNext()) {
				OpmEntity theThing = (OpmEntity) thingsIter.next();
				things.add(theThing);
			}

		}
		// get an entry to show the instances.
		if (Opcat2.getCurrentProject().getSystemStructure() != null) {
			Entry entry = (Entry) Opcat2.getCurrentProject()
					.getSystemStructure().getElements().nextElement();
			entry.ShowInstances(things);
		}
	}

	class ConnectButton implements java.awt.event.ActionListener {
		private MetaDataGridPanels panel;

		public ConnectButton(MetaDataGridPanels action) {
			this.panel = action;
		}

		public void actionPerformed(ActionEvent e) {
			this.panel.connect();
		}
	}

	class DisconnectButton implements java.awt.event.ActionListener {
		private MetaDataGridPanels panel;

		public DisconnectButton(MetaDataGridPanels action) {
			this.panel = action;
		}

		public void actionPerformed(ActionEvent e) {
			this.panel.disconnect();
		}
	}

	class UnconnectedButton implements java.awt.event.ActionListener {
		private MetaDataGridPanels panel;

		public UnconnectedButton(MetaDataGridPanels action) {
			this.panel = action;
		}

		public void actionPerformed(ActionEvent e) {
			this.panel.unconnected();
		}
	}

	class MissingButton implements java.awt.event.ActionListener {
		private MetaDataGridPanels panel;

		public MissingButton(MetaDataGridPanels action) {
			this.panel = action;
		}

		public void actionPerformed(ActionEvent e) {
			this.panel.missing();
		}
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
			if (e.getButton() == MouseEvent.BUTTON3) {
				rightClickEvent(panel, e);
			}
		}
	}

	public static MetaDataGridPanels getInstance() {
		return instance;
	}
}

class ShowInstancesAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Entry entry;

	public ShowInstancesAction(Entry entry) {
		this.entry = entry;
	}

	public void actionPerformed(ActionEvent e) {
		entry.ShowInstances();
	}

}
