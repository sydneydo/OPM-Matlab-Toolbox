package gui.repository.rpopups;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JSeparator;

import com.sciapp.table.GroupTableColumn;
import com.sciapp.table.GroupTableHeader;

import gui.images.standard.StandardImages;
import gui.opdProject.OpdProject;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.StateEntry;
import gui.repository.BaseView;
import gui.scenarios.Scenario;
import gui.scenarios.ScenarioUtils;
import gui.scenarios.Scenarios;
import gui.scenarios.TestingScenariosPanel;
import gui.util.opcatGrid.GridPanel;

public class RScenariosPopup extends RDefaultPopup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Scenarios scenarios ; 

	public RScenariosPopup(BaseView view, OpdProject prj) {
		super(view, prj);
		
		this.add(this.addScenario);
		this.add(new JSeparator());
		this.add(this.compareScenarios);		

		scenarios = (Scenarios) userObject ; 
	}
	
	Action addScenario = new AbstractAction("Add",
			StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			
			TestingScenariosPanel panel = ScenarioUtils.getScenaioPanel(null, null) ; 
			panel.AddToExtensionToolsPanel() ; 

			
			view.rebuildTree(myProject.getSystemStructure(), myProject) ; 
		}
	};	
	
	Action compareScenarios = new AbstractAction("Compare",
			StandardImages.EMPTY) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {

			OpdProject  project = myProject ; 
			
			Iterator sceniter = project.getScen().keySet().iterator();
			ArrayList cols = new ArrayList();
			ArrayList scenNames = new ArrayList();
			cols.add("Object Name");

			while (sceniter.hasNext()) {
				// cols.add((String) sceniter.next());
				cols.add("Initial");
				cols.add("Final");
				String name = (String) sceniter.next();
				scenNames.add(name);
				scenNames.add(name);
			}
			GridPanel scenPanel = new GridPanel(cols);
			scenPanel.setEntryTag();

			for (int i = 1; i < cols.size(); i = i + 2) {
				GroupTableColumn grp = new GroupTableColumn((String) scenNames
						.get(i));
				grp.addColumn(scenPanel.getGrid().getColumnModel().getColumn(i));
				grp
						.addColumn(scenPanel.getGrid().getColumnModel().getColumn(
								i + 1));
				// grp.setShowChildren(false);
				((GroupTableHeader) scenPanel.getGrid().getTableHeader())
						.addGroupColumn(grp);
			}
			scenPanel.setTabName("Compare Scenarios");
			scenPanel.RemoveFromExtensionToolsPanel();
			// now get all objects and put in rows;
			Iterator iter = project.getSystemStructure().GetObjectEntries();
			while (iter.hasNext()) {
				ObjectEntry obj = (ObjectEntry) iter.next();
				Object[] row = new Object[cols.size()];
				row[0] = obj.getName();

				for (int i = 1; i < cols.size(); i = i + 2) {
					Scenario scen = (Scenario) project.getScen().get(
							(String) scenNames.get(i));
					HashMap initialSce = (HashMap) scen.getInitialObjectsHash();
					HashMap finalSce = (HashMap) scen.getFinalObjectsHash();

					String initialState = " ";
					Long StateID = (Long) initialSce.get(new Long(obj.getId()));
					if (StateID != null) {
						StateEntry initial = (StateEntry) project
								.getSystemStructure().getEntry(StateID.longValue());
						if (initial != null) {
							initialState = initial.getName();
						}
					}

					String finalState = " ";
					StateID = (Long) finalSce.get(new Long(obj.getId()));
					if (StateID != null) {
						StateEntry finalS = (StateEntry) project
								.getSystemStructure().getEntry(StateID.longValue());
						if (finalS != null) {
							finalState = finalS.getName();
						}
					}

					row[i] = initialState;
					row[i + 1] = finalState;

					// scenPanel.getGrid().getTableHeader()
				}

				Object[] rowTag = new Object[2];
				rowTag[0] = obj;
				rowTag[1] = " ";

				scenPanel.getGrid().addRow(row, rowTag);
			}

			scenPanel.AddToExtensionToolsPanel();			
			view.rebuildTree(myProject.getSystemStructure(), myProject) ; 
		}
	};		
	

}
