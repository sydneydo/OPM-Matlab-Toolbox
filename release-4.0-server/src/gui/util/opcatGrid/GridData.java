package gui.util.opcatGrid;

import javax.swing.table.AbstractTableModel;
import com.sciapp.table.ListTableModel;

import java.util.ArrayList;
import java.util.List;

public class GridData extends AbstractTableModel implements ListTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public static final int UPDATE_TYPE_DELETE = 1;

	public static final int UPDATE_TYPE_UPDATE = 2;

	public static final int UPDATE_TYPE_INSERT = 3;

	private int columnCount = 0;

	private ArrayList columnNames;

	private ArrayList gridData;

	private ArrayList tags;

	private ArrayList tagsHash = new ArrayList();

	private boolean duplicateRows = true;

	private boolean[] keyFields;

	public ArrayList GetColumnsNames() {
		return this.columnNames;
	}

	public GridData(ArrayList columnsNames) {
		super();
		this.columnCount = columnsNames.size();
		this.gridData = new ArrayList();
		this.tags = new ArrayList();
		this.keyFields = new boolean[this.columnCount];
		for (int i = 0; i < this.keyFields.length; i++) {
			this.keyFields[i] = true;
		}
		this.columnNames = columnsNames;
	}

	private Long GetKeyFromRowandTag(Object[] row, Object[] rowTag) {
		String str = "";
		if (!(rowTag == null)) {
			for (int i = 0; i < rowTag.length; i++) {
				str = str + rowTag[i].toString();
			}
		}
		for (int i = 0; i < this.getColumnCount(); i++) {
			if (row[i] != null) {
				if (this.keyFields[i]) {
					str = str + row[i].toString();
				}
			}
		}
		return new Long(str.hashCode());
	}

	private void AddRowandTagstoHash(Object[] row, Object[] tags) {
		Long key = this.GetKeyFromRowandTag(row, tags);
		this.tagsHash.add(key);
	}

	public int addRow(Object[] row, Object[] rowTag) {
		if (this.isDuplicateRows()
				|| !this.tagsHash.contains(this.GetKeyFromRowandTag(row,
						rowTag))) {
			this.gridData.add(row);
			this.tags.add(rowTag);
			this.AddRowandTagstoHash(row, rowTag);
		}
		return this.getRowCount();
	}

	protected Object[] GetTag(int rowNumber) {
		return (Object[]) this.tags.get(rowNumber);
	}

	public int getRowCount() {
		if (!(this.gridData == null)) {
			return this.gridData.size();
		} else {
			return 0;
		}
	}

	public String getColumnName(int col) {
		return this.columnNames.get(col).toString();
	}

	public int getColumnCount() {
		return this.columnCount;
	}

	public Object getValueAt(int row, int column) {
		if ((row < this.gridData.size()) && (column < this.columnCount)) {
			Object[] oRow = (Object[]) this.gridData.get(row);
			return oRow[column];
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the duplicateRows.
	 */
	public boolean isDuplicateRows() {
		return this.duplicateRows;
	}

	/**
	 * @param duplicateRows
	 *            The duplicateRows to set.
	 */
	public void setDuplicateRows(boolean duplicateRows) {
		this.duplicateRows = duplicateRows;
	}

	public void setKeyFieldsOff(int field) {
		this.keyFields[field] = false;
	}

	public void addRow(Object row) {
		this.gridData.add(row);

	}

	public void addRows(List rows) {
		gridData.addAll(rows);

	}

	public void clear() {
		int count = getRowCount();
		this.tags.clear();
		this.tagsHash.clear();
		this.gridData.clear();
		fireTableRowsDeleted(0, count);
	}

	public Object getCellValue(Object row, int index) {
		return ((Object[]) row)[index];
	}

	public List getRows() {
		return gridData;
	}

	public void removeRow(int index) {
		this.gridData.remove(index);
	}

	public void removeRows(int[] indexes) {
		for (int i = 0; i < indexes.length; i++) {
			removeRow(i);
		}
	}
}
