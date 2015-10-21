package expose;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.internal.util.SVNPathUtil;

import com.sciapp.renderers.BooleanRenderer;

import expose.OpcatExposeConstants.OPCAT_EXPOSE_ENTITY_TYPES;
import extensionTools.validator.gui.PictureRenderer;
import gui.actions.expose.OpcatExposeGridActions;
import gui.controls.FileControl;
import gui.images.opm.OPMImages;
import gui.images.standard.StandardImages;
import gui.util.opcatGrid.GridPanel;

public class OpcatExposeList {

	public static final String tab_name_text = "Expose List";
	public static final String refresh_text = " Refresh";
	public static final String useExpose_text = "Use";
	public static final String usageReport_text = "Show Local Successors";
	public static final String usageGlobalReport_text = "Show Successors";
	public static final String messages_menu_text = "Messages";
	public static final String reports_menu_text = "Reports";
	public static final String interface_change_text = "Interface need Changes";
	public static final String non_interface_change_text = "Non Interface changes";

	private static OpcatExposeList list = null;
	private GridPanel panel = null;

	private OpcatExposeList() {

		ArrayList<String> cols = initColumns();
		panel = new GridPanel(cols);

		panel.getGrid().getColumnModel().getColumn(2).setCellRenderer(
				new PictureRenderer());

		panel.getGrid().getColumnModel().getColumn(5).setCellRenderer(
				new BooleanRenderer());

		panel.getGrid().getColumnModel().getColumn(6).setCellRenderer(
				new BooleanRenderer());

		JButton refresh = new JButton(refresh_text);
		refresh.addActionListener(new OpcatExposeGridActions(this));
		panel.getButtonPane().add(refresh);

		JButton useExpose = new JButton(useExpose_text);
		useExpose.addActionListener(new OpcatExposeGridActions(this));
		panel.getButtonPane().add(useExpose);

		panel.getGrid().addMouseListener(new MouseListner(panel));

		panel.setTabName(tab_name_text);

	}

	public static OpcatExposeList getInstance() {
		if (list == null) {
			list = new OpcatExposeList();
		}

		return list;
	}

	public OpcatExposeEntity getSelectedEntity() {
		Object[] tag = panel.getSelectedTag();
		if (tag == null)
			return null;
		return (OpcatExposeEntity) tag[1];

	}

	private ArrayList<String> initColumns() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("ID");
		ret.add("Name");
		ret.add("Type");
		ret.add("Exposure Information");
		ret.add("Thing Description");
		ret.add("Public");
		ret.add("Private");
		ret.add("Model Name");
		ret.add("Model Repository path");

		return ret;

	}

	public void refresh(ArrayList<OpcatExposeEntity> entities) {
		init(entities);
	}

	public void init(ArrayList<OpcatExposeEntity> entities) {

		if (FileControl.getInstance().getCurrentProject() == null) {
			return;
		}

		if (panel != null) {
			panel.ClearData();
		}

		if (OpcatMCManager.getInstance(true).getSVNURL(
				new File(FileControl.getInstance().getCurrentProject()
						.getPath())) != null) {
			fillData(entities);
		} else {
			entities = new ArrayList<OpcatExposeEntity>();
		}
	}

	private void fillData(ArrayList<OpcatExposeEntity> data) {
		for (OpcatExposeEntity entity : data) {

			Object[] row = new Object[panel.getGrid().getColumnCount()];
			Object[] rowTag = new Object[2];

			boolean pub = false;
			if (entity.getKey().getId() == OpcatExposeConstants.EXPOSE_UNDEFINED_ID) {
				row[0] = new Long(0);
			} else {
				pub = true;
				row[0] = new Long(entity.getKey().getId());
			}

			row[1] = entity.getOpmEntityName();
			if (entity.getOpmEntityType() == OPCAT_EXPOSE_ENTITY_TYPES.OBJECT) {
				row[2] = OPMImages.OBJECT;
			} else if (entity.getOpmEntityType() == OPCAT_EXPOSE_ENTITY_TYPES.PROCESS) {
				row[2] = OPMImages.PROCESS;
			} else {
				row[2] = null;
			}

			row[3] = entity.getDescription();
			row[4] = entity.getOpmEntityDescription();
			row[5] = pub;
			row[6] = entity.getKey().isPrivate();
			row[7] = entity.getModelName();
			row[8] = SVNPathUtil.getAbsolutePath(entity.getKey()
					.getModelDecodedURI());

			rowTag[0] = entity.getKey();
			rowTag[1] = entity;

			// if (entity.getKey().getId() !=
			// OpcatExposeConstants.EXPOSE_UNDEFINED_ID) {
			panel.getGrid().addRow(row, rowTag);
			// }
		}
	}

	private void rightClickEvent(GridPanel panel, MouseEvent e) {
		JPopupMenu popup = panel.getRMenu();

		if (panel.getGrid().getSelectedRow() >= 0) {

			Object[] tag = panel.getSelectedTag();
			OpcatExposeEntity entity = (OpcatExposeEntity) tag[1];

			JMenu reports = new JMenu(reports_menu_text);
			JMenuItem localreport = new JMenuItem(usageReport_text);
			localreport.addActionListener(new OpcatExposeGridActions(this));
			reports.add(localreport);
			JMenuItem globalreport = new JMenuItem(usageGlobalReport_text);
			globalreport.addActionListener(new OpcatExposeGridActions(this));
			reports.add(globalreport);
			popup.add(reports);
			popup.add(new JSeparator());

			if (entity.getModelID()
					.equalsIgnoreCase(
							FileControl.getInstance().getCurrentProject()
									.getGlobalID())) {
				JMenu messages = new JMenu(messages_menu_text);
				JMenuItem interface_message = new JMenuItem(
						interface_change_text);
				interface_message.addActionListener(new OpcatExposeGridActions(
						this));
				messages.add(interface_message);
				JMenuItem non_interface_message = new JMenuItem(
						non_interface_change_text);
				non_interface_message
						.addActionListener(new OpcatExposeGridActions(this));
				messages.add(non_interface_message);
				popup.add(messages);
				popup.add(new JSeparator());
			}

			JMenuItem item = new JMenuItem(useExpose_text);
			item.addActionListener(new OpcatExposeGridActions(this));
			popup.add(item);
			popup.add(new JSeparator());

		}
		popup.show(e.getComponent(), e.getX(), e.getY());

	}

	public void show(boolean show) {

		if (panel == null)
			return;

		GridPanel.RemovePanel(panel.getTabName());
		if (!show) {
			panel.RemoveFromExtensionToolsPanel();
		} else {
			panel.AddToExtensionToolsPanel();
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
				// showThings();
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				rightClickEvent(panel, e);
			}
		}
	}

}
