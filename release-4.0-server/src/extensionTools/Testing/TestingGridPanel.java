package extensionTools.Testing;

import java.awt.Color;
import java.awt.event.*;
import gui.util.opcatGrid.*;

import java.util.ArrayList;
import javax.swing.JLabel;

import exportedAPI.OpdKey;
import exportedAPI.opcatAPIx.IXInstance;
import exportedAPI.opcatAPIx.IXSystem;

public class TestingGridPanel extends GridPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7787975395110512L;

	private IXSystem mySys;

	private IXInstance lastIns = null;

	private Color lastColor = Color.black;

	public TestingGridPanel(ArrayList<String> columnsNames,
			IXSystem opcatSystem) {
		super(columnsNames);
		this.getGrid().addMouseListener(new MouseListner(this));
		this.mySys = opcatSystem;
		this.getGrid().setDuplicateRows(true);	
		
		this.getButtonPane().add(new JLabel(""));		
	}

	public int addRow(Object[] row, Object[] rowTag) {
		return this.getGrid().addRow(row, rowTag);
	}

	public void RemoveFromExtensionToolsPanel() {
		super.RemoveFromExtensionToolsPanel();
		this.RestoreThingOrigColor();
	}

	public void RestoreThingOrigColor() {
		if (this.lastIns != null) {
			this.lastIns.setBorderColor(this.lastColor);
		}
	}

	public void dblClickEvent() {
		Object[] tag = new Object[2];
		IXInstance ins;
		tag = this.getGrid().GetTag(this.getGrid().getSelectedRow());
		OpdKey key = (OpdKey) tag[0];
		Long entityID = (Long) tag[1];
		ins = this.mySys.getIXSystemStructure().getIXEntry(entityID.longValue())
				.getIXInstance(key);

		if ((this.lastIns != null) && (this.lastIns != ins)) {
			this.lastIns.setBorderColor(this.lastColor);
			this.lastIns.update();
		}
		if (this.lastIns != ins) {
			this.lastIns = ins;
			this.lastColor = this.lastIns.getBorderColor();
			ins.setBorderColor(Color.red);
			ins.update();
		}
		this.mySys.showOPD(key.getOpdId());

	}

	public class MouseListner extends MouseAdapter {
		private TestingGridPanel panel;

		public MouseListner(TestingGridPanel Panel) {
			this.panel = Panel;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				this.panel.dblClickEvent();
			}
		}
	}

}
