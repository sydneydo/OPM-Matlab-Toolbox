package gui.scenarios;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import com.sciapp.filter.StringFilter;
import com.sciapp.filter.TableFilter;
import com.sciapp.table.FilterTableModel;

import gui.Opcat2;
import gui.controls.GuiControl;
import gui.opdProject.OpdProject;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.StateEntry;
import gui.util.opcatGrid.GridPanel;

public class TestingScenariosPanel extends GridPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3228741594759395297L;

	OpdProject project;

	ArrayList columnsNames;

	boolean onlyWithStates;

	Scenario scenario;

	String oldID;

	boolean newScenario;
	ConnectionEdgeEntry filteredThing = null ; 

	public TestingScenariosPanel(Scenario scen, ArrayList columnsNames,
			OpdProject prj, boolean onlyWithStates, ConnectionEdgeEntry thing) {
		super(columnsNames);
		this.filteredThing = thing ; 
		project = prj;
		this.columnsNames = columnsNames;
		this.onlyWithStates = onlyWithStates;

		this.getGrid().addMouseListener(new MyMouseListner(this));
		this.setEntryTag();
		init();

		if (scen == null) {
			scenario = new Scenario(".no name.");
			newScenario = true;
		} else {
			scenario = scen;
			newScenario = false;
		}
		oldID = scenario.getId();

		JButton save = new JButton("Save");
		save.addActionListener(new SaveScen(prj, this));
		getButtonPane().add(save);

		if (!newScenario) {
			JButton rename = new JButton("Rename");
			rename.addActionListener(new RenameScen(prj, this));
			getButtonPane().add(rename);
		} else {
			this.getButtonPane().add(new JLabel(""));
		}

		this.setTabName("Scenario " + scenario.getName() + " - " + "Objects");
		
		

	}
	
	protected void init() {
		// build the Grid
		ClearData();
		Iterator iter = project.getSystemStructure().GetObjectEntries();
		while (iter.hasNext()) {
			ObjectEntry ent = (ObjectEntry) iter.next();

			// if (ent.hasStates() || !onlyWithStates) {
			Object[] row = new Object[columnsNames.size()];
			for (int i = 0; i < row.length; i++) {
				row[i] = " ";
			}

			row[0] = ent.getName();

			row[1] = " ";
			HashMap processes = ent.getConnectedThings();
			Iterator connected = processes.values().iterator();
			while (connected.hasNext()) {
				Object[] array = (Object[]) connected.next();
				if (array[0] instanceof ProcessEntry) {
					ProcessEntry process = (ProcessEntry) (array[0]);
					row[1] = process.getName().replace("\n", " ") + ","
							+ row[1];
				}
			}
			row[2] = ent.getDescription();
			row[3] = ent.getUrl();

			Iterator states = Collections.list(ent.getStates()).iterator();
			row[4] = " ";
			row[5] = " ";
			while (states.hasNext()) {
				StateEntry state = (StateEntry) states.next();
				if (state.isInitial()) {
					row[4] = state.getName();
				}
				if (state.isFinal()) {
					row[5] = state.getName();
				}
			}

			Object[] rowTag = { " ", " " };
			rowTag[0] = ent;

			this.getGrid().addRow(row, rowTag);
		}
		if(filteredThing !=  null) {
		    FilterTableModel ftm = this.getGrid().getFilterModel();

		    StringFilter sf = new StringFilter();
		    sf.setMode(StringFilter.CONTAINS);
		    sf.setPattern(filteredThing.getName().replace("\n", " "));

		    TableFilter tf = new TableFilter(sf, 1);
		    ftm.setTableFilter(tf);		    
		}
		// }
	}

	public void rightClickEvent(MouseEvent e) {
		JPopupMenu statesPopupMenu = this.getRMenu();

		if (this.getGrid().getSelectedRow() >= 0) {
			Object[] tag = new Object[2];
			tag = this.getGrid().GetTag(this.getGrid().getSelectedRow());
			ObjectEntry obj = (ObjectEntry) tag[0];
			Iterator iter = Collections.list(obj.getStates()).iterator();
			if (iter.hasNext()) {
				JMenu states = new JMenu("Set Initial");
				JMenu finStates = new JMenu("Set Final");
				while (iter.hasNext()) {
					StateEntry state = (StateEntry) iter.next();
					JCheckBoxMenuItem stateItem = new JCheckBoxMenuItem(state
							.getName());
					JCheckBoxMenuItem finalStateItem = new JCheckBoxMenuItem(
							state.getName());
					if (state.isInitial()) {
						stateItem.setSelected(true);
					} else {
						stateItem.setSelected(false);
					}
					if (state.isFinal()) {
						finalStateItem.setSelected(true);
					} else {
						finalStateItem.setSelected(false);
					}

					stateItem.addActionListener(new StateAction(this, obj,
							state, true));
					finalStateItem.addActionListener(new StateAction(this, obj,
							state, false));
					states.add(stateItem);
					finStates.add(finalStateItem);
				}
				statesPopupMenu.add(states);
				statesPopupMenu.add(finStates);
			}
			statesPopupMenu.add(new JSeparator());
			JMenuItem randomInitial = new JMenuItem("All Initial Off");
			randomInitial.addActionListener(new RandomInitial(this, project,
					true));
			JMenuItem randomFinal = new JMenuItem("All Final off");
			randomFinal.addActionListener(new RandomInitial(this, project,
					false));
			statesPopupMenu.add(randomInitial);
			statesPopupMenu.add(randomFinal);
			statesPopupMenu.add(new JSeparator());

			statesPopupMenu.show(e.getComponent(), e.getX(), e.getY());

		}
	}
}

class RenameScen extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -861379811237840446L;

	TestingScenariosPanel panel;

	OpdProject project;

	public RenameScen(OpdProject project, TestingScenariosPanel panel) {
		this.panel = panel;
		this.project = project;

	}

	public void actionPerformed(ActionEvent e) {

		String name = panel.scenario.getName();
		while ((name != null)
				&& (name.equalsIgnoreCase(panel.scenario.getName()) || (project
						.getScen().get(name) != null))) {
			name = (String) JOptionPane.showInputDialog(panel, "Scenario Name",
					"OPCAT II", JOptionPane.INFORMATION_MESSAGE, null, null,
					name);

			if (project.getScen().get(name) != null) {
				JOptionPane.showMessageDialog(panel, "Name already exists",
						"Scenario Name", JOptionPane.ERROR_MESSAGE);
			}

		}

		if (name != null) {
			panel.scenario.setName(name);
		} else {
			JOptionPane.showMessageDialog(panel, "Rename Canceled",
					"Scenario Name", JOptionPane.ERROR_MESSAGE);
		}

	}
};

class SaveScen extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -861379811237840446L;

	TestingScenariosPanel panel;

	OpdProject project;

	public SaveScen(OpdProject project, TestingScenariosPanel panel) {
		this.panel = panel;
		this.project = project;

	}

	public void actionPerformed(ActionEvent e) {

		HashMap initialsce = new HashMap();
		HashMap finalsce = new HashMap();

		String name = "Enter Scenario name";
		if (panel.newScenario) {
			while ((name != null)
					&& (name.equalsIgnoreCase("Enter Scenario name") || (project
							.getScen().get(name) != null))) {
				name = (String) JOptionPane.showInputDialog(panel,
						"Scenario Name", "OPCAT II",
						JOptionPane.INFORMATION_MESSAGE, null, null, name);

				if (project.getScen().get(name) != null) {
					JOptionPane.showMessageDialog(panel, "Name already exists",
							"Scenario Name", JOptionPane.ERROR_MESSAGE);
				}

			}
		} else {
			name = panel.scenario.getName();
		}

		Iterator iter = project.getSystemStructure().GetObjectEntries();
		while (iter.hasNext()) {
			ObjectEntry ent = (ObjectEntry) iter.next();
			if (ent.hasStates()) {
				Iterator states = Collections.list(ent.getStates()).iterator();
				while (states.hasNext()) {
					StateEntry state = (StateEntry) states.next();
					if (state.isInitial()) {
						initialsce.put(new Long(ent.getId()), new Long(state
								.getId()));
					}
					if (state.isFinal()) {
						finalsce.put(new Long(ent.getId()), new Long(state
								.getId()));
					}
				}
			}
		}
		panel.scenario.setInitialObjectsHash(initialsce);
		panel.scenario.setFinalObjectsHash(finalsce);

		if (name != null) {
			panel.scenario.setName(name);
			panel.newScenario = false;

			project.getScen().remove(panel.oldID);
			project.getScen().add(panel.scenario);

			panel.RemoveFromExtensionToolsPanel();
			panel.setTabName("Scenario " + panel.scenario.getName() + " - "
					+ "Objects");
			panel.AddToExtensionToolsPanel();
		} else {
			JOptionPane.showMessageDialog(panel, "Save Canceled",
					"Scenario Name", JOptionPane.ERROR_MESSAGE);
		}

		project.setCanClose(false);

		GuiControl gui = GuiControl.getInstance();
		gui.getRepository().rebuildTrees(true);
	}

}

class StateAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9009284864238922559L;

	private ObjectEntry myObject;

	private StateEntry myState;

	private boolean setInit;

	private TestingScenariosPanel scen;

	public void actionPerformed(ActionEvent e) {
		Iterator iter = Collections.list(myObject.getStates()).iterator();
		while (iter.hasNext()) {
			StateEntry state = (StateEntry) iter.next();
			if (setInit) {
				if (state.getId() == myState.getId()) {
					state.setInitial(!state.isInitial());
				} else {
					state.setInitial(false);
				}
			} else {
				if (state.getId() == myState.getId()) {
					state.setFinal(!state.isFinal());
				} else {
					state.setFinal(false);
				}
			}
		}
		Opcat2.getFrame().repaint();
		scen.init();
	}

	public StateAction(TestingScenariosPanel source, ObjectEntry obj,
			StateEntry state, boolean setInitial) {
		super();
		myObject = obj;
		myState = state;
		setInit = setInitial;
		scen = source;
	}
}

class MyMouseListner extends MouseAdapter {
	private TestingScenariosPanel panel;

	public MyMouseListner(TestingScenariosPanel Panel) {
		this.panel = Panel;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			panel.dblClickEvent(e);
		}

		if (e.getButton() == MouseEvent.BUTTON3) {
			if (panel.getGrid().getSelectedRow() != -1)
				panel.rightClickEvent(e);
		}
	}
}


class RandomInitial extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	OpdProject project;

	TestingScenariosPanel panel;

	boolean initial = false;

	public RandomInitial(TestingScenariosPanel panel, OpdProject project,
			boolean setInitial) {
		this.panel = panel;
		this.project = project;
		this.initial = setInitial;
	}

	public void actionPerformed(ActionEvent e) {
		Iterator objectIter = project.getSystemStructure().GetObjectEntries();
		while (objectIter.hasNext()) {
			ObjectEntry ent = (ObjectEntry) objectIter.next();
			if (ent.hasStates()) {
				Iterator states = Collections.list(ent.getStates()).iterator();
				while (states.hasNext()) {
					StateEntry sta = (StateEntry) states.next();

					if (initial) {
						sta.setInitial(false);
					}

					if (!initial) {
						sta.setFinal(false);
					}
				}
			}

		}
		Opcat2.getFrame().repaint();
		panel.init();

	}

}