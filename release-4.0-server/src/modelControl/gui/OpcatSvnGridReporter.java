package modelControl.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import javax.swing.table.TableColumn;

import com.sciapp.renderers.NumberRenderer;

import gui.util.opcatGrid.GridPanel;

public class OpcatSvnGridReporter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token = ",";
	private Collection<String> cols = new ArrayList<String>();
	private GridPanel mypanel = null;
	private String tabName = "Checkout Report";

	public OpcatSvnGridReporter(boolean firstRowNames, String token,
			String tabName) {
		if (token != null) {
			this.token = token;
		}
		this.tabName = tabName;
	}

	public long getRowsCount() {
		return mypanel.getGrid().getRowCount();
	}

	/**
	 * The first colomn must be an integer marking the id or row number.
	 * 
	 * @param data
	 */
	public void setData(Collection<String> data) {

		boolean head = true;
		for (String str : data) {
			if (head) {
				init(str);
				head = false;
			} else {
				addRow(str);
			}
		}
	}

	public void show(boolean visible) {
		if (mypanel != null) {
			if (visible) {
				mypanel.AddToExtensionToolsPanel();
			} else {
				mypanel.RemoveFromExtensionToolsPanel();
			}
		}
	}

	public void repaint() {
		mypanel.repaint();
	}

	public void addRow(String data) {

		StringTokenizer token = new StringTokenizer(data, this.token);
		int i = 0;
		Object[] row = new Object[mypanel.GetColumnsModel().getColumnCount()];
		Object[] rowTag = new Object[2];
		while (token.hasMoreElements()) {
			String inner = (String) token.nextElement();
			try {
				Integer.parseInt(inner);
				row[i] = new Integer(inner).intValue();
			} catch (Exception ex) {
				row[i] = inner;
			}
			i++;
			if (i >= mypanel.GetColumnsModel().getColumnCount())
				break;
		}
		rowTag[0] = "";
		rowTag[1] = "";
		mypanel.getGrid().addRow(row, rowTag);
	}

	public void init(String data) {
		StringTokenizer token = new StringTokenizer(data, this.token);

		while (token.hasMoreElements()) {
			String inner = (String) token.nextElement();
			cols.add(inner);

		}
		mypanel = null;
		mypanel = new GridPanel(cols);
		mypanel.setTabName(tabName);

		TableColumn column = mypanel.getGrid().getColumnModel().getColumn(0);
		column.setCellRenderer(new NumberRenderer());

	}
}
