package extensionTools.Testing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TestingLifeSpanCellRenderer extends JButton implements
	TableCellRenderer {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    private TableCellRenderer __defaultRenderer;

    public TestingLifeSpanCellRenderer(TableCellRenderer renderer) {
	__defaultRenderer = renderer;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {

	if (value instanceof TestingColor) {
	    Color color = ((TestingColor) value).getColor() ; 
	    int red = color.getRed() ; 
	    int blue = color.getBlue() ; 
	    int green = color.getGreen(); 
	    
	    int lowRed = red - 20 ; 
	    int lowBlue = blue - 20 ; 
	    int lowGreen = green - 20 ; 
	    
	    int highRed = red + 20 ; 
	    int highBlue = blue + 20 ; 
	    int highGreen = green + 20 ; 

	    if(lowRed <= 0 ) lowRed = 0  ;
	    if(lowGreen <= 0 ) lowGreen = 0  ;
	    if(lowBlue <= 0 ) lowBlue = 0  ;
	    
	    
	    if(highRed > 254 ) highRed = 254 ;
	    if(highBlue > 254 ) highBlue = 254 ;
	    if(highGreen > 254 ) highGreen = 254 ;	    
	    
	    putClientProperty("gradientStart", new Color(lowRed, lowGreen , lowBlue )) ; 
	    putClientProperty("gradientEnd", new Color(highRed, highGreen , highBlue)) ; 
	    setBackground(((TestingColor) value).getColor());
	    setText(value.toString());
	    return this;
	} else {
	    return __defaultRenderer.getTableCellRendererComponent(table,
		    value, isSelected, hasFocus, row, column);
	}

    }

}
