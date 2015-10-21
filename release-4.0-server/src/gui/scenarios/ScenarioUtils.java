package gui.scenarios;

import gui.Opcat2;
import gui.projectStructure.ConnectionEdgeEntry;

import java.util.ArrayList;

public class ScenarioUtils {
	
	public static TestingScenariosPanel getScenaioPanel(Scenario scen, ConnectionEdgeEntry filterThing) {
		ArrayList cols = new ArrayList();
		cols.add("Object");
		cols.add("Connected Processes");
		cols.add("Description");
		cols.add("URL");
		cols.add("Initial State");
		cols.add("Final State");

		TestingScenariosPanel locPanel;
		locPanel = new TestingScenariosPanel(scen, cols, Opcat2
				.getCurrentProject(), false, filterThing);
		locPanel.doLayout();
		locPanel.setEntryTag() ; 
		return locPanel;
	}	

}
