package gui.opdGraphics.dialogs;

import exportedAPI.OpcatConstants;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import gui.opmEntities.OpmInstantination;
import gui.opmEntities.OpmObject;
import gui.projectStructure.Entry;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ThingEntry;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

public class ObjectTypePanel extends JPanel {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private OpdProject myProject;

	private ThingEntry myEntry;

	private final String basic[] = { "Boolean", "char", "short", "integer",
			"unsigned integer", "long", "float", "double" };

	private final String compound[] = { "char[]", "date", "time", "integer" };

	private TreeMap<String,OpmEntity> sorted = new TreeMap<String,OpmEntity>(); // structure to hold the custom

	// types

	private MyComboBox typeCombo = new MyComboBox();

	private JTextField enumText = new JTextField("");

	private JTextField charText = new JTextField("50");

	private Component strut = Box.createHorizontalStrut(200);

	private JRadioButton basicTypes = new JRadioButton("Basic Types", true);

	private JRadioButton compoundTypes = new JRadioButton("Advanced Types");

	private JRadioButton customTypes = new JRadioButton("Custom Types");

	private ButtonGroup types = new ButtonGroup();

	private JLabel label = new JLabel();

	private MyCheckBox definition = new MyCheckBox("Compound Object");

	// private final static int MAX_LENGTH = 256;
	// private final static int MIN_LENGTH = 0;

	public ObjectTypePanel(ThingEntry pEntry, OpdProject prj) {
		this.myEntry = pEntry;
		this.myProject = prj;
		try {
			this._jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}

	private void _jbInit() throws Exception {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Object Type "), BorderFactory.createEmptyBorder(4, 8,
				4, 8)));
		this.enumText.setPreferredSize(new Dimension(140, 22));
		this.enumText.setMaximumSize(new Dimension(140, 22));
		this.enumText.setVisible(false);
		this.enumText
				.setToolTipText("Should be a coma separated strings. All white spaces will be ommited");

		this.charText.setPreferredSize(new Dimension(140, 22));
		this.charText.setMaximumSize(new Dimension(140, 22));
		this.charText.setVisible(false);
		this.charText.setToolTipText("Data length, 0 or more");

		this.label.setPreferredSize(new Dimension(60, 22));
		this.label.setMaximumSize(new Dimension(60, 22));
		this.label.setVisible(false);

		this.typeCombo.setRenderer(new ComboBoxRenderer());
		this.basicTypes.setActionCommand("basic");
		this.basicTypes.addActionListener(this.typeCombo);
		this.types.add(this.basicTypes);
		this.compoundTypes.setActionCommand("compound");
		this.compoundTypes.addActionListener(this.typeCombo);
		this.types.add(this.compoundTypes);
		this.customTypes.setActionCommand("custom");
		this.customTypes.addActionListener(this.typeCombo);
		this.types.add(this.customTypes);
		this._initContol();

		this.typeCombo.addActionListener(this.typeCombo);
		this.definition.addActionListener(this.definition);

		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(this.definition);
		this.add(p);
		this.add(Box.createVerticalStrut(5));

		p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(Box.createHorizontalStrut(10));
		p.add(this.basicTypes);
		p.add(this.compoundTypes);
		p.add(this.customTypes);
		this.add(p);
		this.add(Box.createVerticalStrut(5));

		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(Box.createHorizontalStrut(20));
		p.add(this.typeCombo);
		p.add(Box.createHorizontalStrut(10));
		p.add(this.strut);
		p.add(this.label);
		p.add(this.enumText);
		p.add(this.charText);
		this.add(p);

		this._setControlData();
	}

	private boolean _checkCharVectorType() {
		int val;
		try {
			val = Integer.parseInt(this.charText.getText());
		} catch (NumberFormatException nfe) {
			return false;
		}
		if (val < 0) {
			return false;
		}
		return true;
	}

	private boolean _checkEnumerationType() {
		StringTokenizer tokens = new StringTokenizer(this.enumText.getText(), ",");
		String trimmed = new String();
		String tmp;
		for (; tokens.hasMoreTokens();) {
			tmp = tokens.nextToken();
			tmp = tmp.trim();
			if (!trimmed.equals("")) {
				trimmed += ",";
			}
			trimmed += tmp;
		}
		this.enumText.setText(trimmed);
		return true;
	}

	public String getType() {
		if (this.definition.isSelected()) {
			return "";
		}

		if ((this.typeCombo.getSelectedItem().toString()).equals("char[]")) {
			if (!this._checkCharVectorType()) {
				return null;
			}
			return "char[" + this.charText.getText() + "]";
		}

		if ((this.typeCombo.getSelectedItem().toString()).equals("integer")) {
			if (!this._checkCharVectorType()) {
				return null;
			}
			return "INT" +  this.charText.getText();
		}
		
		if ((this.typeCombo.getSelectedItem().toString()).equals("enumeration")) {
			if (!this._checkEnumerationType()) {
				return null;
			}
			return "enumeration{" + this.enumText.getText() + "}";
		}

		return this.typeCombo.getSelectedItem().toString();
	}

	public long getTypeObjectId() {
		if (this.customTypes.isSelected()) {
			return ((OpmObject) this.typeCombo.getSelectedItem()).getId();
		}
		return -1;
	}

	private class ComboBoxRenderer extends JLabel implements ListCellRenderer {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 

		public ComboBoxRenderer() {
			this.setOpaque(true);
			this.setHorizontalAlignment(LEFT);
			this.setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			// setFont(getFont().deriveFont(Font.PLAIN));
			if (value == null) {
				return this;
			}
			String txt = value.toString();

			if (isSelected) {
				this.setBackground(list.getSelectionBackground());
				this.setForeground(list.getSelectionForeground());
			} else {
				this.setBackground(list.getBackground());
				this.setForeground(list.getForeground());
			}

			this.setText(txt);
			return this;
		}
	};

	private void _initContol() {
		MainStructure ms = this.myProject.getComponentsStructure();

		for (Enumeration els = ms.getElements(); els.hasMoreElements();) {
			Entry ent = (Entry) els.nextElement();
			if ((ent instanceof ObjectEntry) && (ent != this.myEntry)) {
				this.sorted.put(ent.getLogicalEntity().toString(), ent
						.getLogicalEntity() /* .toString() */);
			}
		}

		if (this.sorted.values().size() <= 0) {
			this.customTypes.setEnabled(false);
		}
	}

	/** *********************************************************************** */
	//
	/** *********************************************************************** */
	private class MyComboBox extends JComboBox {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 

		public void actionPerformed(ActionEvent e) {
			Component eventSource = (Component) e.getSource();

			if (eventSource instanceof JComboBox) { // changing combo selection
				ObjectTypePanel.this._showHideTextBox();
			}

			if (eventSource instanceof JRadioButton) { // changing type of
				// types selection
				String actCmd = e.getActionCommand();
				ObjectTypePanel.this._switchComboContence(actCmd);
			}
		}
	};

	/** *********************************************************************** */

	/** *********************************************************************** */
	//
	/** *********************************************************************** */
	private class MyCheckBox extends JCheckBox implements ActionListener {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 

		MyCheckBox(String s) {
			super(s);
		}

		public void actionPerformed(ActionEvent e) {
			ObjectTypePanel.this._enableControl(!this.isSelected());
			if (ObjectTypePanel.this.typeCombo.getSelectedItem() == null) {
				for (int i = 0; i < ObjectTypePanel.this.basic.length; i++) {
					ObjectTypePanel.this.typeCombo.addItem(ObjectTypePanel.this.basic[i]);
				}
			}
		}
	}

	/** *********************************************************************** */

	private void _showHideTextBox() {
		Object o = this.typeCombo.getSelectedItem();
		if (o != null) {
			String selItem = o.toString();
			if ((selItem.equals("char[]")) ) {
				this.strut.setVisible(false);
				this.enumText.setVisible(false);
				this.label.setText("  Length:");
				this.label.setVisible(true);
				this.charText.setVisible(true);
			} else if((selItem.equals("integer"))) {
				this.strut.setVisible(false);
				this.enumText.setVisible(false);
				this.label.setText("  Length:");
				this.label.setVisible(true);
				this.charText.setVisible(true);
				this.charText.setText("10") ; 
			} else if (selItem.equals("enumeration")) {
				this.strut.setVisible(false);
				this.charText.setVisible(false);
				this.label.setText("  Values:");
				this.label.setVisible(true);
				this.enumText.setVisible(true);
			} else {
				this.charText.setVisible(false);
				this.enumText.setVisible(false);
				this.label.setVisible(false);
				this.strut.setVisible(true);
			}
		}
	}

	private void _enableControl(boolean b) {
		if (b) {
			this.typeCombo.setEnabled(true);
			this.charText.setEnabled(true);
			this.enumText.setEnabled(true);
			this.basicTypes.setEnabled(true);
			this.compoundTypes.setEnabled(true);
			if (this.sorted.size() > 0) {
				this.customTypes.setEnabled(true);
			}
			this.label.setEnabled(true);
		}

		else {
			this.typeCombo.setEnabled(false);
			this.enumText.setEnabled(false);
			this.charText.setEnabled(false);
			this.basicTypes.setEnabled(false);
			this.compoundTypes.setEnabled(false);
			this.customTypes.setEnabled(false);
			this.label.setEnabled(false);
		}
	}

	private void _setControlData() {
		Enumeration locenum = this.myEntry
				.getDestinationRelations(OpcatConstants.INSTANTINATION_RELATION);
		if (locenum.hasMoreElements()) {
			OpmInstantination opmRel = null;
			try {
				opmRel = (OpmInstantination) locenum.nextElement();
			} catch (ClassCastException cce) {
				cce.printStackTrace(System.err);
			}
			// long opmRel.getSourceId();
			ObjectEntry source = (ObjectEntry) this.myProject
					.getComponentsStructure().getSourceEntry(opmRel);
			this.customTypes.setSelected(true);
			this._switchComboContence("custom");
			this.typeCombo.setSelectedItem(source.getLogicalEntity());
			this.definition.setEnabled(false);
			this._enableControl(false);
			return;
		}

		String typeString = ((OpmObject) this.myEntry.getLogicalEntity()).getType();

		if ((typeString == null) || typeString.equals("")) {
			this.definition.setSelected(true);
			this._enableControl(false);
			return;
		}
		this._enableControl(true);

		// if basic type
		for (int i = 0; i < this.basic.length; i++) {
			if (typeString.equals(this.basic[i])) {
				this.basicTypes.setSelected(true);
				this._switchComboContence("basic");
				this.typeCombo.setSelectedItem(typeString);
				return;
			}
		}

		// compound types expanded
		if (typeString.startsWith("char[")) {
			this.compoundTypes.setSelected(true);
			this._switchComboContence("compound");
			this.typeCombo.setSelectedItem("char[]");
			this.charText.setText(typeString.substring(5, typeString.length() - 1));
			return;
		}

		if (typeString.startsWith("enumeration")) {
			this.compoundTypes.setSelected(true);
			this._switchComboContence("compound");
			this.typeCombo.setSelectedItem("enumeration");
			this.enumText.setText(typeString.substring(12, typeString.length() - 1));
			return;
		}

		if (typeString.equals("date")) {
			this.compoundTypes.setSelected(true);
			this._switchComboContence("compound");
			this.typeCombo.setSelectedItem("date");
			return;
		}

		if (typeString.equals("time")) {
			this.compoundTypes.setSelected(true);
			this._switchComboContence("compound");
			this.typeCombo.setSelectedItem("time");
			return;
		}
		
		if (typeString.startsWith("INT")) {
			this.compoundTypes.setSelected(true);
			this._switchComboContence("compound");
			this.typeCombo.setSelectedItem("integer");
			this.charText.setText(typeString.substring(3, typeString.length()));
			return;
		}		

		// custom types
		for (Iterator iter = this.sorted.values().iterator(); iter.hasNext();) {
			OpmObject lEntity = (OpmObject) iter.next();
			if (lEntity.getId() == ((OpmObject) this.myEntry.getLogicalEntity())
					.getTypeOriginId()) {
				this.customTypes.setSelected(true);
				this._switchComboContence("custom");
				this.typeCombo.setSelectedItem(lEntity);
				return;
			}
		}
		JOptionPane
				.showMessageDialog(
						this,
						"Internal error in ObjectTypePanel._setControlData()\nPlease contact developers",
						"Opcat2 - ERROR", JOptionPane.ERROR_MESSAGE);
		return;
	}

	private void _switchComboContence(String actCmd) {
		if (actCmd.equals("basic")) {
			this.typeCombo.removeAllItems();
			for (int i = 0; i < this.basic.length; i++) {
				this.typeCombo.addItem(this.basic[i]);
			}
		}

		if (actCmd.equals("compound")) {
			this.typeCombo.removeAllItems();
			for (int i = 0; i < this.compound.length; i++) {
				this.typeCombo.addItem(this.compound[i]);
			}
		}

		if (actCmd.equals("custom")) {
			this.typeCombo.removeAllItems();
			for (Iterator iter = this.sorted.values().iterator(); iter.hasNext();) {
				this.typeCombo.addItem(iter.next());
			}
		}
		this._showHideTextBox();
	}


}