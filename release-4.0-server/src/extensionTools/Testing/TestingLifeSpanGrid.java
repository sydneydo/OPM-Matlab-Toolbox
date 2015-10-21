package extensionTools.Testing;

import java.util.Iterator;
import java.util.TreeMap;

import exportedAPI.opcatAPIx.IXThingEntry;
import gui.projectStructure.ObjectEntry;

public class TestingLifeSpanGrid extends TestingStepsRreportsAbstractGrid {

	public TestingLifeSpanGrid(TestingSystem testingSys) {
		super(testingSys, "Life-Span Graph") ; 
	}



	public void showGrid(TreeMap<Long, TestingStepData> data) {

		// synchronized (panel) {

		if ((panel != null) && (panel.isOnExtensionToolsPane())) {
			// panel.ClearData() ;
			panel.RemoveFromExtensionToolsPanel();
		}

		panel = null;

		panel = init(data.keySet().size());

		for (int i = 0; i < entries.size(); i++) {
			
			TestingEntry entry = entries.get(i);			
			
			if (!(entry.getIXEntry() instanceof IXThingEntry)) {
				continue;
			}

			Object[] row = new Object[cols.size()];
			row[0] = entry.getIXEntry().getName();

			if (entry.getIXEntry() instanceof ObjectEntry) {
				row[1] = "Object";
			} else {
				row[1] = "Process";
			}

			Iterator<Long> iter = data.keySet().iterator();
			while (iter.hasNext()) {
				Long step = iter.next();
				TestingStepData stepData = data.get(step);

				TestingStepEntryData entryData = stepData
						.getEntryData(entry);

				TestingColor testColor = new TestingColor(entryData
						.getEntryColor(), createTestinColorString(entryData));

				row[step.intValue() + 1] = testColor;
			}

			Object[] rowTag = new Object[2];
			rowTag[0] = entry.getIXEntry();
			rowTag[1] = " ";
			panel.getGrid().addRow(row, rowTag);

		}

		panel.AddToExtensionToolsPanel();
		// GuiControl.getInstance().hideWaitMessage();

		// }

	}

	private String createTestinColorString(TestingStepEntryData entryData) {

		String value = entryData.getStateName();

		value = getStateString(entryData) ; 

		if (entryData.isObject()) {
			if (!value.toString().trim().equalsIgnoreCase("")) {
				value = value.toString() + " ("
						+ entryData.getMeasurementUnitAcommulatedValue() + ")";
			}
		}

		value = value.toString() + " [" + entryData.getNumberOfInstances()
				+ "]";
		return value;
	}


}
