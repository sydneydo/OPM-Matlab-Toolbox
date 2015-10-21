package extensionTools.Testing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import gui.util.opcatGrid.GridPanel;

public class TestingBreakPointsGrid extends GridPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TestingBreakPointsGrid(ArrayList cols, TestingSystem testingSystem) {
		super(cols);
		this.setTabName("Breakpoints");
		this.setEntryTag();
		this.getGrid().addMouseListener(new BreakPointsGridListner(this)); 
	}
	
	
	public void rightClickEvent(MouseEvent e) {

	}	

}

class BreakPointsGridListner extends MouseAdapter {
	private TestingBreakPointsGrid panel;

	public BreakPointsGridListner(TestingBreakPointsGrid Panel) {
		this.panel = Panel;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			panel.dblClickEvent(e);
		}

		if (e.getButton() == MouseEvent.BUTTON3) {
			if (panel.getGrid().getSelectedRow() != -1)
				panel.rightClickEvent(e);
		}
	}
}