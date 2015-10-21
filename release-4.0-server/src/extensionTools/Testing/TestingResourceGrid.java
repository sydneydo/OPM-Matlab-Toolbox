package extensionTools.Testing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import exportedAPI.opcatAPIx.IXObjectEntry;
import gui.opmEntities.OpmObject;
import gui.projectStructure.ObjectEntry;

public class TestingResourceGrid extends TestingStepsRreportsAbstractGrid {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestingResourceGrid(TestingSystem testingSys) {
		super(testingSys, "Resource Graph");
	}

	public void showGrid(TreeMap<Long, TestingStepData> data) {
		
		//need to remove the pannel from the tab as there could be left one in there. 
		

		if ((panel != null) && (panel.isOnExtensionToolsPane())) {
			// panel.ClearData() ;
			panel.RemoveFromExtensionToolsPanel();
		}
		
		panel = null;

		ArrayList<String> cols = new ArrayList<String>();
		cols.add("Name");
		cols.add("Limits");

		panel = init(data.keySet().size(), cols);

		for (int i = 0; i < entries.size(); i++) {

			TestingEntry entry = entries.get(i);

			if (!(entry.getIXEntry() instanceof IXObjectEntry)) {
				continue;
			}

			Object[] row = new Object[cols.size()];
			row[0] = entry.getIXEntry().getName();
			OpmObject opm = (OpmObject) ((ObjectEntry) entry.getIXEntry())
					.getLogicalEntity();

			if (!opm.isMesurementUnitExists()) {
				continue;
			}

			row[1] = opm.getMesurementUnitInitialValue() + " ["
					+ opm.getMesurementUnitMinValue() + ","
					+ opm.getMesurementUnitMaxValue() + "] "
					+ opm.getMesurementUnit();

			Iterator<Long> iter = data.keySet().iterator();
			while (iter.hasNext()) {

				Long step = iter.next();
				TestingStepData stepData = data.get(step);
				TestingStepEntryData entryData = stepData.getEntryData(entry);

				row[step.intValue() + 1] = getStateString(entryData) + " ("
						+ entryData.getMeasurementUnitAcommulatedValue() + ")";
			}

			Object[] rowTag = new Object[2];
			rowTag[0] = entries.get(i);
			rowTag[1] = " ";
			panel.getGrid().addRow(row, rowTag);

		}

		panel.AddToExtensionToolsPanel();

	}

}
