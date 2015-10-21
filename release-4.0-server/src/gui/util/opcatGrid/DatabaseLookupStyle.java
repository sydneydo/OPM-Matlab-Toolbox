package gui.util.opcatGrid;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.JTable;

import com.sciapp.table.styles.Style;

public class DatabaseLookupStyle implements Style {

	private HashMap<String, Color> map;
	private int colorLookupColomn;

	public DatabaseLookupStyle(int colorLookupColomn,
			HashMap<String, Color> colValue_colorsMap) {
		this.map = colValue_colorsMap;
		this.colorLookupColomn = colorLookupColomn;
	}

	@Override
	public void apply(Component c, JTable table, int row, int column) {
		String value = table.getModel().getValueAt(row, colorLookupColomn)
				.toString();
		Color color = map.get(value);
		if (color != null) {
			c.setBackground(color);
		}
	}
}
