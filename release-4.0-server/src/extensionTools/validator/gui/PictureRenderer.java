package extensionTools.validator.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * 
 * @author Eran Toch
 * Created: 14/05/2004
 */
public class PictureRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
		ImageIcon icon = (ImageIcon)value;
		this.setText ("Type");
		this.setIcon ( icon );
		return this;
	}

}
