package expose.gui;

import expose.OpcatExposeEntity;
import expose.OpcatExposeKey;
import gui.Opcat2;
import gui.actions.OpcatAction;
import gui.controls.FileControl;
import gui.opdProject.OpdProject;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.sciapp.renderers.BooleanRenderer;
import com.sciapp.renderers.NumberRenderer;

public class OpcatExposeShowModelUsersAction extends OpcatAction {

	GridPanel myPanel;
	OpcatExposeKey key;
	ArrayList<String> cols;
	OpdProject project;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpcatExposeShowModelUsersAction(String name, Icon icon,
			OpdProject project, OpcatExposeKey key) {
		super(name, icon);
		this.key = key;
		this.project = project;
		cols = new ArrayList<String>();
		cols.add("ID");
		cols.add("Current Model Name");
		cols.add("OPD Name");
		cols.add("Source Model Name");
		cols.add("Private");

		myPanel = new GridPanel(cols);
		myPanel.setInstanceTag(FileControl.getInstance().getCurrentProject());
		myPanel.GetColumnsModel().getColumn(4).setCellRenderer(
				new BooleanRenderer());
		myPanel.GetColumnsModel().getColumn(0).setCellRenderer(
				new NumberRenderer());

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			super.actionPerformed(e);

			if (key == null) {
				return;
			}

			key.refreshID();

			ArrayList<OpcatExposeEntity> exposed = project.getExposeManager()
					.getExposedList(key, true);

			if (exposed == null) {
				JOptionPane
						.showMessageDialog(Opcat2.getFrame(),
								"There are no users of this expose in the current model");
				return;
			}

			if (exposed.size() == 0) {
				JOptionPane
						.showMessageDialog(Opcat2.getFrame(),
								"There are no users of this expose in the current model");
				return;
			}

			for (OpcatExposeEntity expose : exposed) {
				String tabName = expose.getOpmEntityName();
				myPanel.setTabName(tabName + " Current Model Users ");

				Enumeration<Entry> entries = FileControl.getInstance()
						.getCurrentProject().getSystemStructure().getElements();

				while (entries.hasMoreElements()) {

					Entry entry = entries.nextElement();

					Enumeration<Instance> instances = entry.getInstances();
					while (instances.hasMoreElements()) {
						Instance ins = instances.nextElement();
						if (ins.isPointerInstance()) {
							if (ins.getPointer().equals(expose.getKey())) {
								addRow(ins, entry, expose);
							}
						}
					}
				}
				myPanel.AddToExtensionToolsPanel();
			}

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}

	private void addRow(Instance ins, Entry entry, OpcatExposeEntity expose)
			throws Exception {
		Object[] rowTag = new Object[2];
		rowTag[0] = ins.getKey();
		rowTag[1] = entry.getId();

		Object[] row = new Object[cols.size()];

		row[0] = entry.getId();
		row[1] = ins.getEntry().getName();
		row[2] = ins.getOpd().getName();
		row[3] = (expose == null ? " " : expose.getOpmEntityName());
		row[4] = ins.getPointer().isPrivate();

		myPanel.getGrid().addRow(row, rowTag);
	}

}
