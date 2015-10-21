package gui.opdProject;

import gui.Opcat2;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;

public class OplColorsDialog extends JDialog {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    /**
         * 
         */

    private OplColorScheme colorScheme;

    private OplColorScheme originalScheme;

    JPanel topPanel = new JPanel();

    JPanel colorsPanel = new JPanel();

    TitledBorder titledBorder1;

    JCheckBox disableColors = new JCheckBox();

    JPanel miscPanel = new JPanel();

    Border border1;

    JComboBox sizesBox = new JComboBox();

    JLabel textSizeLabel = new JLabel();

    JButton applyButton = new JButton();

    JButton cancelButton = new JButton();

    JButton okButton = new JButton();

    JLabel oplElementsLabel = new JLabel();

    JScrollPane listScrollPane = new JScrollPane();

    JList elementsList = new JList();

    JLabel attributesLabel = new JLabel();

    JCheckBox boldBox = new JCheckBox();

    JCheckBox italicBox = new JCheckBox();

    JLabel textColorLabel = new JLabel();

    JButton colorButton = new JButton();

    JCheckBox underlinedBox = new JCheckBox();

    JButton defaultButton = new JButton();

    private final String fontSizes[] = { "Smallest", "Smaller", "Small", "Medium",
	    "Large", "Larger", "Largest" };

    public OplColorsDialog(JFrame owner, String title, OplColorScheme cScheme) {
	super(owner, title, true);
	this.originalScheme = cScheme;
	this.colorScheme = new OplColorScheme();

	try {
	    this.jbInit();
	    this.myInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(
		new Color(153, 153, 153), 2), "");
	this.border1 = BorderFactory.createEtchedBorder(Color.white, new Color(
		142, 142, 142));
	this.getContentPane().setLayout(null);
	this.topPanel.setBorder(BorderFactory.createRaisedBevelBorder());
	this.topPanel.setBounds(new Rectangle(8, 8, 370, 231));
	this.topPanel.setLayout(null);
	this.colorsPanel.setBorder(this.titledBorder1);
	this.colorsPanel.setBounds(new Rectangle(15, 74, 345, 145));
	this.colorsPanel.setLayout(null);
	this.titledBorder1.setTitle("Colors");
	this.titledBorder1.setBorder(BorderFactory.createEtchedBorder());
	this.disableColors.setText("Disable Colors   ");
	this.disableColors.setHorizontalTextPosition(SwingConstants.LEADING);
	this.disableColors.setActionCommand("disableColors");
	this.disableColors.setBounds(new Rectangle(200, 15, 130, 20));
	this.disableColors
		.addActionListener(new OplColorsDialog_disableColors_actionAdapter(
			this));
	this.miscPanel.setBorder(this.border1);
	this.miscPanel.setBounds(new Rectangle(15, 16, 341, 49));
	this.miscPanel.setLayout(null);
	this.sizesBox.setBounds(new Rectangle(102, 14, 79, 22));
	this.sizesBox
		.addActionListener(new OplColorsDialog_sizesBox_actionAdapter(
			this));
	this.textSizeLabel.setText("Text Size");
	this.textSizeLabel.setBounds(new Rectangle(37, 15, 60, 20));
	this.applyButton.setText("Apply");
	this.applyButton.setBounds(new Rectangle(219, 254, 70, 27));
	this.applyButton
		.addActionListener(new OplColorsDialog_applyButton_actionAdapter(
			this));
	this.cancelButton.setText("Cancel");
	this.cancelButton.setBounds(new Rectangle(296, 254, 80, 27));
	this.cancelButton
		.addActionListener(new OplColorsDialog_cancelButton_actionAdapter(
			this));
	this.okButton.setText("OK");
	this.okButton.setBounds(new Rectangle(145, 254, 66, 27));
	this.okButton
		.addActionListener(new OplColorsDialog_okButton_actionAdapter(
			this));
	this.oplElementsLabel.setText("OPL Element :");
	this.oplElementsLabel.setBounds(new Rectangle(15, 25, 110, 21));
	this.listScrollPane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	this.listScrollPane.setBounds(new Rectangle(16, 48, 115, 80));
	this.attributesLabel.setText("Attributes :");
	this.attributesLabel.setBounds(new Rectangle(151, 26, 72, 22));
	this.boldBox.setText("Bold");
	this.boldBox.setBounds(new Rectangle(232, 25, 97, 18));
	this.boldBox
		.addActionListener(new OplColorsDialog_boldBox_actionAdapter(
			this));
	this.italicBox.setText("Italic");
	this.italicBox.setBounds(new Rectangle(232, 48, 97, 18));
	this.italicBox
		.addActionListener(new OplColorsDialog_italicBox_actionAdapter(
			this));
	this.textColorLabel.setText("Text Color :");
	this.textColorLabel.setBounds(new Rectangle(151, 103, 81, 22));
	this.colorButton.setText("    ");
	this.colorButton.setBounds(new Rectangle(232, 105, 97, 22));
	this.colorButton
		.addActionListener(new OplColorsDialog_colorButton_actionAdapter(
			this));
	this.setResizable(false);
	this.elementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.elementsList
		.addListSelectionListener(new OplColorsDialog_elementsList_listSelectionAdapter(
			this));
	this.underlinedBox.setText("Underlined");
	this.underlinedBox.setBounds(new Rectangle(232, 71, 97, 18));
	this.underlinedBox
		.addActionListener(new OplColorsDialog_underlinedBox_actionAdapter(
			this));
	this.defaultButton.setText("Defaults");
	this.defaultButton.setBounds(new Rectangle(21, 254, 90, 27));
	this.defaultButton
		.addActionListener(new OplColorsDialog_defaultButton_actionAdapter(
			this));
	this.getContentPane().add(this.topPanel, null);
	this.topPanel.add(this.miscPanel, null);
	this.miscPanel.add(this.disableColors, null);
	this.miscPanel.add(this.textSizeLabel, null);
	this.miscPanel.add(this.sizesBox, null);
	this.topPanel.add(this.colorsPanel, null);
	this.colorsPanel.add(this.listScrollPane, null);
	this.colorsPanel.add(this.oplElementsLabel, null);
	this.colorsPanel.add(this.underlinedBox, null);
	this.colorsPanel.add(this.textColorLabel, null);
	this.colorsPanel.add(this.attributesLabel, null);
	this.colorsPanel.add(this.boldBox, null);
	this.colorsPanel.add(this.italicBox, null);
	this.colorsPanel.add(this.colorButton, null);
	this.listScrollPane.getViewport().add(this.elementsList, null);
	this.getContentPane().add(this.defaultButton, null);
	this.getContentPane().add(this.okButton, null);
	this.getContentPane().add(this.applyButton, null);
	this.getContentPane().add(this.cancelButton, null);

	this.setBounds(0, 0, 393, 330);
	this.setLocationRelativeTo(this.getParent());
	// this.setVisible(true);
    }

    private void myInit() {
	for (int i = 0; i < fontSizes.length; i++) {
	    this.sizesBox.addItem(fontSizes[i]);
	}

	this.colorScheme.getSchemeFrom(this.originalScheme);

	this.sizesBox.setSelectedIndex(colorScheme.getTextSize() - 1 );
	this.elementsList.setListData(this.colorScheme.getAttributes());
	this.elementsList.setSelectedIndex(0);
	this.disableColors.setSelected(!this.colorScheme.isColorsEnabled());
	this.enableColors(this.colorScheme.isColorsEnabled());

    }

    private void enableColors(boolean enabled) {
	this.colorsPanel.setEnabled(enabled);

	JScrollBar jsb = this.listScrollPane.getVerticalScrollBar();
	if (jsb != null) {
	    jsb.setEnabled(enabled);
	}
	this.attributesLabel.setEnabled(enabled);
	this.textColorLabel.setEnabled(enabled);
	this.oplElementsLabel.setEnabled(enabled);
	this.elementsList.setEnabled(enabled);
	this.colorButton.setEnabled(enabled);
	this.boldBox.setEnabled(enabled);
	this.italicBox.setEnabled(enabled);
	this.underlinedBox.setEnabled(enabled);
    }

    void cancelButton_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    void disableColors_actionPerformed(ActionEvent e) {
	this.enableColors(!this.disableColors.isSelected());
	this.colorScheme.enableColors(!this.disableColors.isSelected());
    }

    void elementsList_valueChanged(ListSelectionEvent e) {
	ColorAttribute att = (ColorAttribute) this.elementsList
		.getSelectedValue();
	if (att == null) {
	    return;
	}

	this.colorButton.setBackground(att.getColor());
	this.boldBox.setSelected(att.isBold());
	this.italicBox.setSelected(att.isItalic());
	this.underlinedBox.setSelected(att.isUnderlined());
    }

    void boldBox_actionPerformed(ActionEvent e) {
	ColorAttribute att = (ColorAttribute) this.elementsList
		.getSelectedValue();
	if (att == null) {
	    return;
	}

	att.setBold(this.boldBox.isSelected());
    }

    void italicBox_actionPerformed(ActionEvent e) {
	ColorAttribute att = (ColorAttribute) this.elementsList
		.getSelectedValue();
	if (att == null) {
	    return;
	}

	att.setItalic(this.italicBox.isSelected());
    }

    void colorButton_actionPerformed(ActionEvent e) {
	ColorAttribute att = (ColorAttribute) this.elementsList
		.getSelectedValue();
	if (att == null) {
	    return;
	}

	Color newColor = JColorChooser.showDialog(OplColorsDialog.this,
		"Choose " + att.toString() + " Color", this.colorButton
			.getBackground());
	if (newColor != null) {
	    att.setColor(newColor);
	    this.colorButton.setBackground(newColor);
	}

    }

    void okButton_actionPerformed(ActionEvent e) {
	this.originalScheme.getSchemeFrom(this.colorScheme);
	Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
	this.dispose();
    }

    void applyButton_actionPerformed(ActionEvent e) {
	this.originalScheme.getSchemeFrom(this.colorScheme);
	Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
    }

    void sizesBox_actionPerformed(ActionEvent e) {
	this.colorScheme.setTextSize(this.sizesBox.getSelectedIndex() + 1 );
    }

    void underlinedBox_actionPerformed(ActionEvent e) {
	ColorAttribute att = (ColorAttribute) this.elementsList
		.getSelectedValue();
	if (att == null) {
	    return;
	}

	att.setUnderlined(this.underlinedBox.isSelected());

    }

    void defaultButton_actionPerformed(ActionEvent e) {
	int selIndex = this.elementsList.getSelectedIndex();

	this.colorScheme.setDefault();
	this.sizesBox.setSelectedIndex(colorScheme.getTextSize() - 1);
	this.elementsList.setListData(this.colorScheme.getAttributes());

	if (selIndex == -1) {
	    this.elementsList.setSelectedIndex(0);
	} else {
	    this.elementsList.setSelectedIndex(selIndex);
	}

	ColorAttribute att = (ColorAttribute) this.elementsList
		.getSelectedValue();
	if (att == null) {
	    return;
	}

	this.colorButton.setBackground(att.getColor());
	this.boldBox.setSelected(att.isBold());
	this.italicBox.setSelected(att.isItalic());
	this.underlinedBox.setSelected(att.isUnderlined());

	this.disableColors.setSelected(false);
	this.enableColors(true);

    }

}

class OplColorsDialog_cancelButton_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_cancelButton_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.cancelButton_actionPerformed(e);
    }
}

class OplColorsDialog_disableColors_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_disableColors_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.disableColors_actionPerformed(e);
    }
}

class OplColorsDialog_elementsList_listSelectionAdapter implements
	javax.swing.event.ListSelectionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_elementsList_listSelectionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void valueChanged(ListSelectionEvent e) {
	this.adaptee.elementsList_valueChanged(e);
    }
}

class OplColorsDialog_boldBox_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_boldBox_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.boldBox_actionPerformed(e);
    }
}

class OplColorsDialog_italicBox_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_italicBox_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.italicBox_actionPerformed(e);
    }
}

class OplColorsDialog_colorButton_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_colorButton_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.colorButton_actionPerformed(e);
    }
}

class OplColorsDialog_okButton_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_okButton_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.okButton_actionPerformed(e);
    }
}

class OplColorsDialog_applyButton_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_applyButton_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.applyButton_actionPerformed(e);
    }
}

class OplColorsDialog_sizesBox_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_sizesBox_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.sizesBox_actionPerformed(e);
    }
}

class OplColorsDialog_underlinedBox_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_underlinedBox_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.underlinedBox_actionPerformed(e);
    }
}

class OplColorsDialog_defaultButton_actionAdapter implements
	java.awt.event.ActionListener {
    OplColorsDialog adaptee;

    OplColorsDialog_defaultButton_actionAdapter(OplColorsDialog adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
	this.adaptee.defaultButton_actionPerformed(e);
    }
}
