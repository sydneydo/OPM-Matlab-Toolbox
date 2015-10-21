package gui.util.opcatGrid;

import java.awt.*;
import com.sciapp.table.styles.Style;
import com.sciapp.treetable.*;

/**
 * 
 */
public class AggregateStyle implements Style {
	public TreeTable table;

	public TreeTableModelAdapter model;

	/**
	 * 
	 */
	public AggregateStyle(TreeTable table) {
		this.table = table;

		model = (TreeTableModelAdapter) table.getModel();
	}

	/**
	 * 
	 */
	public void apply(Component c, javax.swing.JTable t, int row, int column) {
		if (model.isFooter(row)) {
			Font f = c.getFont().deriveFont(Font.BOLD);
			c.setFont(f);

			if (!t.isCellSelected(row, column)) {
				Object value = t.getValueAt(row, column);
				if ((value != null && !value.toString().equals(""))) {
					Color color = Color.yellow;
					c.setBackground(color);
				}
			}
		} else if (model.isHeader(row)) {
			Font f = c.getFont().deriveFont(Font.BOLD);
			c.setFont(f); 
		} //else if (row >= 0 ) {		
		//	Object obj = t.getModel().getValueAt(row, column) ;
		//	if(obj instanceof TestingColor ) {
		//		c.setBackground((TestingColor) obj) ; 
		//	}
		//}
	}
}
