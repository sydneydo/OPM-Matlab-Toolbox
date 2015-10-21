package gui.util;

import gui.util.opcatGrid.*;

import java.util.ArrayList;

/**
 * This class contains debug helpers. it is a singleton and the output is
 * wrriten to a grid in the extension tools panel.
 * 
 * @usage Debug debug = Debug.getInstance() ; <br>
 *        debug.Print ;
 * 
 * @author raanan
 * 
 */
public class Debug {

	private static Debug instance = null;
	GridPanel panel;
	ArrayList<String> columnsNames;
	private long startTime = System.currentTimeMillis();
	public static final int DebugLevelExtensionTools = 100;
	public static final int DebugLevelCore = 1;
	public static final int DebugLevelOFF = 0;

	private Debug() {

		if (LEVEL > 0) {
			this.columnsNames = this.initColumnsNames();
			this.panel = new GridPanel(this.columnsNames);
			this.panel.GetColumnsModel().getColumn(0).setMaxWidth(10000);
			this.panel.setTabName(tabName);
			try {
				this.panel.AddToExtensionToolsPanel();
			} catch (Exception ex) {
			}
		}

	}

	public static Debug getInstance() {
		if (instance == null) {
			instance = new Debug();
		}
		return instance;
	}

	public void Print(Object obj, String Message, String Key) {
		if (LEVEL > 0) {
			Object[] row = new Object[ColumnsNumber];
			row[0] = new Long(this.panel.getGrid().getRowCount() + 1);
			row[1] = obj.getClass().getCanonicalName();
			row[2] = Message;
			Long time = new Long(System.currentTimeMillis() - this.startTime);
			row[3] = time;
			Object[] tag = new Object[1];
			tag[0] = Key;
			this.panel.getGrid().addRow(row, tag);
		}
	}

	/**
	 * Checks whether the system is operating in the debug mode.
	 * 
	 * @return true if the system is operating in the debug mode, false
	 *         otherwise.
	 */
	public boolean IsDebug() {
		return LEVEL > 0;
	}

	/**
	 * @param level
	 *            The lEVEL to set.
	 */
	public static void setLEVEL(int level) {
		LEVEL = level;
	}

	private ArrayList<String> initColumnsNames() {
		ArrayList<String> columnsNames = new ArrayList<String>();
		columnsNames.add("ID");
		columnsNames.add("Class");
		columnsNames.add("Message");
		columnsNames.add("Time");
		return columnsNames;
	}

	public static void setDebugLevelToExtensionTools() {
		LEVEL = DebugLevelExtensionTools;
	}

	public static void setDebugLevelToCore() {
		LEVEL = DebugLevelCore;
	}

	private static int LEVEL = 0;
	private static final String tabName = "Debug";
	private static final int ColumnsNumber = 4;

	/**
	 * @return Returns the debug lEVEL.
	 */
	public static int getDebugLevel() {
		return LEVEL;
	}
}