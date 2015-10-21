package extensionTools.Testing;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JTabbedPane;
import javax.swing.table.TableCellRenderer;

import com.sciapp.table.AdvancedJTable;
import com.sciapp.table.styles.Style;

import gui.controls.GuiControl;
import gui.util.opcatGrid.GridPanel;

public abstract class TestingStepsRreportsAbstractGrid {

	private static final long serialVersionUID = 1L;

	protected GridPanel panel = null;

	protected ArrayList<TestingEntry> entries = new ArrayList<TestingEntry>();

	protected ArrayList<String> cols = new ArrayList<String>();

	protected String tabName = "No name set";

	public boolean isPanelNull() {
		return (panel == null);
	}

	public void RemoveFromExtensionToolsPanel() {
		panel.RemoveFromExtensionToolsPanel();
	}

	public TestingStepsRreportsAbstractGrid(TestingSystem testingSys, String tabName) {
		entries = new ArrayList<TestingEntry>();
		entries.addAll(testingSys.getTestingElements());
		
		this.tabName = tabName ; 
		JTabbedPane  tabs = GuiControl.getInstance().getExtensionToolsPane() ; 
		if ( tabs.indexOfTab(tabName) > 0 ) {
			tabs.removeTabAt(tabs.indexOfTab(tabName)) ;
		}
	}

	protected GridPanel init(long stepsNumber, ArrayList<String> firstCols) {

		GridPanel myPanel = null;

		this.cols = firstCols;

		for (int i = 1; i <= stepsNumber; i++) {
			cols.add(String.valueOf(i));
		}

		myPanel = new GridPanel(cols);
		myPanel.getGrid().setAutoResizeMode(AdvancedJTable.AUTO_RESIZE_OFF);

		myPanel.setTabName(tabName);
		if (myPanel.isOnExtensionToolsPane()) {
			myPanel.RemoveFromExtensionToolsPanel();
		}
		myPanel.setEntryTag();
		// panel.AddToExtensionToolsPanel();

		TableCellRenderer defRend = myPanel.getGrid().getDefaultRenderer(
				Object.class);
		TableCellRenderer rend = new TestingLifeSpanCellRenderer(defRend);

		myPanel.getGrid().setDefaultRenderer(Object.class, rend);

		Style[] style = myPanel.getGrid().getStyleModel().getStyles();
		for (int i = 0; i < style.length; i++) {
			myPanel.getGrid().getStyleModel().removeStyle(style[i]);
		}

		return myPanel;

	}

	protected GridPanel init(long stepsNumber) {

		GridPanel myPanel = null;

		this.cols = new ArrayList<String>();
		cols.add("Name");
		cols.add("Type");
		
		for (int i = 1; i <= stepsNumber; i++) {
			cols.add(String.valueOf(i));
		}

		myPanel = new GridPanel(cols);
		myPanel.getGrid().setAutoResizeMode(AdvancedJTable.AUTO_RESIZE_OFF);

		myPanel.setTabName(tabName);
		if (myPanel.isOnExtensionToolsPane()) {
			myPanel.RemoveFromExtensionToolsPanel();
		}
		myPanel.setEntryTag();
		// panel.AddToExtensionToolsPanel();

		TableCellRenderer defRend = myPanel.getGrid().getDefaultRenderer(
				Object.class);
		TableCellRenderer rend = new TestingLifeSpanCellRenderer(defRend);

		myPanel.getGrid().setDefaultRenderer(Object.class, rend);

		Style[] style = myPanel.getGrid().getStyleModel().getStyles();
		for (int i = 0; i < style.length; i++) {
			myPanel.getGrid().getStyleModel().removeStyle(style[i]);
		}

		return myPanel;

	}

	public void ClearData() {
		panel.ClearData();
	}

	protected String getStateString(TestingStepEntryData entryData) {

		String value = entryData.getStateName();

		if (entryData.isObject()) {

			if (value.equals("")) {
				if (entryData.isAnimated()) {
					value = "exists";
				} else {
					value = "not exists";
				}
			}
		} else {
			if (entryData.isAnimated()) {
				value = "active";
			} else {
				value = "not active";
			}
		}

		return value;
	}

	abstract public void showGrid(TreeMap<Long, TestingStepData> data);

}
