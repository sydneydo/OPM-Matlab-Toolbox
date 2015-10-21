package gui.opdGraphics.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * <p>
 * Class implementing table for view of destinantions of Fundamental relations
 * in <code>RelationPropertiesDialog</code>
 * </p>
 * 
 * @see <a href = "RelationPropertiesDilaog.html"><code>RelationPropertiesDialog</code></a>
 */

public class DestinationsTable extends JPanel {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private JTable table;

	MyTableModel myModel;

	private JScrollPane scrollPane;

	private int rows;

	// private int colomns;
	private boolean[] isObject;

	private JComboBox comboBox;

	private boolean isCardinalityEditable;

	public DestinationsTable(int myRows, boolean isCardinalityEditable) {
		super();
		this.rows = myRows;
		this.isCardinalityEditable = isCardinalityEditable;
		this.isObject = new boolean[myRows];
		this.myModel = new MyTableModel();
		this.myModel.initTable();

		this.table = new JTable(this.myModel);
		this.table.setPreferredScrollableViewportSize(new Dimension(350, 80));

		// Create the scroll pane and add the table to it.
		this.scrollPane = new JScrollPane(this.table);

		// Fiddle with the Cardinality column's cell editors/renderers.
		this.setUpCardinalityColumn(this.table.getColumnModel().getColumn(1));

		// Fiddle with the Custom column's cell editors/renderers.
		this.setUpCustomColumn(this.table.getColumnModel().getColumn(2));

		// Add the scroll pane to this panel.
		this.add(this.scrollPane);

		Action emptyAction = new AbstractAction() {
			 

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * 
			 */
			 

			public void actionPerformed(ActionEvent e) {
			}
		};
		this.table.registerKeyboardAction(emptyAction, KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

	}

	public JTable getTable() {
		return this.table;
	}

	public void setIsObject(boolean isObj, int row) {
		this.isObject[row] = isObj;
	}

	public void updateTable() {
		if (this.table.isEditing()) {
			(this.table.getCellEditor(this.table.getEditingRow(), this.table
					.getEditingColumn())).stopCellEditing();
		}
	}

	public void setValueAt(Object value, int row, int col) {
		this.table.setValueAt(value, row, col);
	}

	public Object getValueAt(int row, int col) {
		return this.table.getValueAt(row, col);
	}

	public void setUpCardinalityColumn(TableColumn cardinalityColumn) {
		// Set up the editor for the cardinality cells.
		this.comboBox = new JComboBox();
		this.comboBox.addActionListener(new ComboBoxAdditionalListener());
		this.comboBox.addItem("1");
		this.comboBox.addItem("many");
		this.comboBox.addItem("custom");

		cardinalityColumn.setCellEditor(new DefaultCellEditor(this.comboBox));

		// Set up tool tips for the cardinality cells.
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer
				.setToolTipText("Click for combo box (if destination is Object)");
		cardinalityColumn.setCellRenderer(renderer);
	}

	public void setUpCustomColumn(TableColumn customColumn) {
		// Set up the editor for the custom cells.
		JTextField textField = new JTextField();
		DefaultCellEditor ce = new DefaultCellEditor(textField);
		ce.setClickCountToStart(1);
		customColumn.setCellEditor(ce);

		// Set up tool tips for the cardinality cells.
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click to edit (if cardinality is \"custom\")");
		customColumn.setCellRenderer(renderer);
	}

	public boolean hasFocus() {
		return (this.table.hasFocus() || this.comboBox.hasFocus());
	}

	class ComboBoxAdditionalListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			// System.err.println(ae.getSource());
			int si = ((JComboBox) ae.getSource()).getSelectedIndex();
			if ((si == 0) || (si == 1)) {
				DestinationsTable.this.table.transferFocus();
			} else if (si == 2) {
				DefaultCellEditor de = (DefaultCellEditor) DestinationsTable.this.table.getCellEditor(
						DestinationsTable.this.table.getSelectedRow(), 2);
				DestinationsTable.this.table.editCellAt(DestinationsTable.this.table.getSelectedRow(), 2);
				JTextField tf = (JTextField) de.getComponent();
				tf.requestFocus();
			}
		}
	}

	class MyTableModel extends AbstractTableModel {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 

		final String[] columnNames = { "Name", "Participation Constraint",
				"Value" };

		Object[][] data = new Object[DestinationsTable.this.rows][3];

		public void initTable() {
			for (int i = 0; i < DestinationsTable.this.rows; i++) {
				for (int j = 0; j < 3; j++) {
					this.data[i][j] = "";
				}
			}

		}

		public int getColumnCount() {
			return this.columnNames.length;
		}

		public int getRowCount() {
			return this.data.length;
		}

		public String getColumnName(int col) {
			return this.columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return this.data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the last column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
			return this.getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			if (!DestinationsTable.this.isCardinalityEditable || (col < 1)) {
				return false;
			} else if (col == 1) {
				return DestinationsTable.this.isObject[row];
			} else if ((col == 2) && ("custom").equals(this.getValueAt(row, 1))) {
				return true;
			} else {
				return false;
			}
		}

		public void setValueAt(Object value, int row, int col) {

			if (col == 2) {
				this.data[row][col] = value.toString();
			} else if (col == 1) {
				if (("1").equals(value)) {
					this.data[row][col + 1] = "1";
				}
				if (("many").equals(value)) {
					this.data[row][col + 1] = "m";
				}
				if (("custom").equals(value)) {
					this.data[row][col + 1] = "";
				}

				this.data[row][col] = value;
				this.fireTableCellUpdated(row, col + 1);
			} else {
				this.data[row][col] = value;
			}
			this.fireTableCellUpdated(row, col);
		}
	}

	// ActionListener cardinalityListener = new AbstractAction("Cardinality
	// Listener"){
	// public void actionPerformed(ActionEvent e){
	// if
	// (((String.valueOf(destinationCardinality.getSelectedItem())).compareTo("custom"))
	// == 0)
	// {
	// customDestinationCardinality.setEditable(true);
	// customDestinationCardinality.requestFocus();
	// }
	// else
	// {
	// customDestinationCardinality.setEditable(false);
	// customDestinationCardinality.setText("");
	// }
	// return;
	// }
	// };

}