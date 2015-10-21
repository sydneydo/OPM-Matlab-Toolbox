package gui.opdGraphics.dialogs;

import gui.Opcat2;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.opdBaseComponents.OpdAggregation;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdExhibition;
import gui.opdGraphics.opdBaseComponents.OpdFundamentalRelation;
import gui.opdGraphics.opdBaseComponents.OpdInstantination;
import gui.opdGraphics.opdBaseComponents.OpdSpecialization;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GraphicalRelationRepresentation;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * This class is properties dialog box for relation.
 * <p>
 * It displays the properties for relation of one type and with one source
 * thing. It's possible for <a href = "OpdFundamentalRelation.html><code>OpdFundamentalRelation</code></a>
 * to have several destination things, in this case all destination things are
 * displaied.
 * </p>
 */
public class RelationPropertiesDialog extends JDialog {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    /**
         * 
         */

    // private JTextArea description;
    private JTextField sourceThing, sourceCardinality;

    private DestinationsTable destinationsTable;

    private OpdFundamentalRelation opdRelation;

    private OpdConnectionEdge opdSourceThing;

    private FundamentalRelationInstance[] relationInstances;

    private JPanel p1, p2; // tmp panels

    private GraphicalRelationRepresentation myGraphicalRelation;

    private JButton okButton, cancelButton, applyButton;

    private JButton bgColor, textColor, borderColor;

    private String sourceName, tmpCardinality;

    private boolean isCardinalityEditable;

    // ===============================================================

    /**
         * Constructor:
         * 
         * @param
         * <code>graphicalRelation GraphicalRelationRepresentationEntry</code>
         * The entry of this OpdRelation.
         * @see <a href =
         *      "../projectStructure/GraphicalRelationRepresentationEntry.html"><code>GraphicalRelationRepresentationEntry</code></a>.
         */
    public RelationPropertiesDialog(
	    GraphicalRelationRepresentation graphicalRelation,
	    String dialogName, boolean isCardinalityEditable) {
	super(Opcat2.getFrame(), true);
	// super((JFrame)null, true);
	this.opdRelation = graphicalRelation.getRelation();
	this.opdSourceThing = graphicalRelation.getSource();
	this.myGraphicalRelation = graphicalRelation;
	this.isCardinalityEditable = isCardinalityEditable;

	this.relationInstances = new FundamentalRelationInstance[graphicalRelation
		.getInstancesNumber()];

	int i = 0;
	for (Enumeration e = graphicalRelation.getInstances(); e
		.hasMoreElements(); i++) {
	    this.relationInstances[i] = (FundamentalRelationInstance) e
		    .nextElement();
	}

	if (this.opdRelation instanceof OpdAggregation) {
	    dialogName = "Aggregation-Participation";
	}
	if (this.opdRelation instanceof OpdExhibition) {
	    dialogName = "Featuring-Characterization";
	}
	if (this.opdRelation instanceof OpdSpecialization) {
	    dialogName = "Generalization-Specialization";
	}
	if (this.opdRelation instanceof OpdInstantination) {
	    dialogName = "Classification-Instantination";
	}
	this.setTitle(dialogName + " Relation Properties");

	// init all variables
	this._initVariables();

	Container contPane = this.getContentPane();
	contPane.setLayout(new BoxLayout(contPane, BoxLayout.Y_AXIS));

	JTabbedPane tabs = new JTabbedPane();

	tabs.add("General", this._constructGeneralTab());
	tabs.add("Misc.", this._constructMiscTab());

	contPane.add(tabs);

	// -----------------------------------------------------------------
	// add buttons
	this.okButton = new JButton("OK");
	this.okButton.addActionListener(new OkListener());
	this.getRootPane().setDefaultButton(this.okButton);
	this.cancelButton = new JButton("Cancel");
	this.cancelButton.addActionListener(new CancelListener());
	this.applyButton = new JButton("Apply");
	// applyButton.setEnabled(false);
	this.applyButton.addActionListener(new ApplyListener());
	this.p1 = new JPanel();
	this.p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	GridLayout layout = new GridLayout(1, 5);
	layout.setHgap(3);
	this.p1.setLayout(layout);
	this.p1.add(Box.createGlue());
	this.p1.add(this.okButton);
	this.p1.add(this.cancelButton);
	this.p1.add(this.applyButton);
	this.p1.add(Box.createGlue());
	contPane.add(this.p1);
	// -----------------------------------------------------------------
	// Escape & Enter listener
	KeyListener escapeAndEnterListener = new KeyAdapter() {
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		    if (!RelationPropertiesDialog.this.destinationsTable
			    .hasFocus()) {
			(RelationPropertiesDialog.this).dispose();
		    }
		    return;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    if (!RelationPropertiesDialog.this.destinationsTable
			    .hasFocus()) {
			if ((RelationPropertiesDialog.this)
				._updateRelationData()) {
			    (RelationPropertiesDialog.this).dispose();
			}
		    }
		    return;
		}
	    }
	};

	this.addKeyListener(escapeAndEnterListener);

	this.pack();
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	int fX = Opcat2.getFrame().getX();
	int fY = Opcat2.getFrame().getY();
	int pWidth = Opcat2.getFrame().getWidth();
	int pHeight = Opcat2.getFrame().getHeight();

	this.setLocation(fX + Math.abs(pWidth / 2 - this.getWidth() / 2), fY
		+ Math.abs(pHeight / 2 - this.getHeight() / 2));

	this.setResizable(false);
    }

    public void showDialog() {
	this.setVisible(true);
    }

    // ===============================================================

    private void _initVariables() {
	try {
	    // description = new JTextArea("Here will be description", 3,
                // 30);
	    // description.setEditable(false);

	    this.sourceName = (this.opdSourceThing.getEntity()).getName()
		    .replace('\n', ' ');

	    this.sourceThing = new JTextField(this.sourceName);
	    this.sourceThing.setSelectedTextColor(new Color(0, 255, 0));
	    this.sourceThing.setEditable(false);
	    this.sourceThing.setHorizontalAlignment(SwingConstants.CENTER);

	    this.sourceCardinality = new JTextField("1");
	    this.sourceCardinality.setEditable(false);
	    this.sourceCardinality
		    .setHorizontalAlignment(SwingConstants.CENTER);

	    this.destinationsTable = new DestinationsTable(
		    this.relationInstances.length, this.isCardinalityEditable);
	    for (int j = 0; j < this.relationInstances.length; j++) {
		if (((this.relationInstances[j].getDestination()).getEntity()) instanceof OpmObject) {
		    this.destinationsTable.setIsObject(true, j);
		}
		if (((this.relationInstances[j].getDestination()).getEntity()) instanceof OpmProcess) {
		    this.destinationsTable.setIsObject(false, j);
		}
		this.destinationsTable.setValueAt(((this.relationInstances[j]
			.getDestination()).getEntity()).getName().replace('\n',
			' '), j, 0);
		this.tmpCardinality = (this.relationInstances[j]
			.getLogicalRelation()).getDestinationCardinality();
		if (this.tmpCardinality.compareTo("1") == 0) {
		    this.destinationsTable
			    .setValueAt(this.tmpCardinality, j, 1);
		} else if (this.tmpCardinality.compareTo("m") == 0) {
		    this.destinationsTable.setValueAt("many", j, 1);
		} else {
		    this.destinationsTable.setValueAt("custom", j, 1);
		    this.destinationsTable
			    .setValueAt(this.tmpCardinality, j, 2);
		}
	    }
	    this.destinationsTable.setVisible(true);

	} catch (Exception e) {
	    System.out.println("Problem2!!");
	}

	this.bgColor = new JButton("    ");
	this.bgColor.setBackground(this.opdRelation.getBackgroundColor());
	this.textColor = new JButton("    ");
	this.textColor.setBackground(this.opdRelation.getTextColor());
	this.borderColor = new JButton("    ");
	this.borderColor.setBackground(this.opdRelation.getBorderColor());
    }

    // ===============================================================

    private JPanel _constructGeneralTab() {
	JPanel tab = new JPanel();
	tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
	tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

	// Relation description
	// p1 = new JPanel();
	// p1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
        // " Description "), BorderFactory.createEmptyBorder(5,5,5,5)));
	// p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
	// description.setBackground(p1.getBackground());
	// p1.add(description);
	// tab.add(p1);

	// source and destinations pref
	this.p1 = new JPanel();
	this.p1.setLayout(new GridLayout(1, 2));
	this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
		.createEtchedBorder(), " Source "));
	// source
	this.p2 = new JPanel();
	this.p2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));
	this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.X_AXIS));
	JLabel source1 = new JLabel("Name: ");
	this.p2.add(source1);
	this.p2.add(Box.createHorizontalStrut(10));
	this.p2.add(this.sourceThing);
	this.p1.add(this.p2);

	this.p2 = new JPanel();
	this.p2.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
	this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.X_AXIS));
	JLabel source2 = new JLabel("Cardinality: ");
	this.p2.add(source2);
	this.p2.add(Box.createHorizontalStrut(10));
	this.p2.add(this.sourceCardinality);
	this.p1.add(this.p2);
	tab.add(this.p1);

	tab.add(Box.createVerticalStrut(10));

	// destination
	this.destinationsTable.setBorder(BorderFactory.createTitledBorder(
		BorderFactory.createEtchedBorder(), " Destinations "));
	tab.add(this.destinationsTable);
	return tab;
    }

    // ===============================================================

    private JPanel _constructMiscTab() {
	JPanel tab = new JPanel();
	tab.setLayout(new BoxLayout(tab, BoxLayout.X_AXIS));
	tab.setBorder(BorderFactory.createCompoundBorder(BorderFactory
		.createEmptyBorder(8, 8, 8, 8), BorderFactory
		.createEtchedBorder()));
	JLabel bgColorLabel = new JLabel("Background Color:");
	JLabel textColorLabel = new JLabel("Text Color:");
	JLabel borderColorLabel = new JLabel("Border Color:");
	this.p1 = new JPanel();
	this.p1.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.Y_AXIS));
	this.p2 = new JPanel();
	this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
	this.p2.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	this.p1.add(bgColorLabel);
	this.p1.add(Box.createVerticalStrut(20));
	this.p1.add(textColorLabel);
	this.p1.add(Box.createVerticalStrut(20));
	this.p1.add(borderColorLabel);
	this.p1.add(Box.createVerticalStrut(112));
	this.bgColor.addActionListener(new BgColorButtonListener());
	this.textColor.addActionListener(new TextColorButtonListener());
	this.borderColor.addActionListener(new BorderColorButtonListener());
	this.p2.add(this.bgColor);
	this.p2.add(Box.createVerticalStrut(8));
	this.p2.add(this.textColor);
	this.p2.add(Box.createVerticalStrut(8));
	this.p2.add(this.borderColor);
	this.p2.add(Box.createVerticalStrut(112));
	tab.add(this.p1);
	tab.add(this.p2);
	tab.add(Box.createHorizontalStrut(140));
	return tab;
    }

    // ===============================================================

    private boolean _updateRelationData() {

	this.destinationsTable.updateTable();

	for (int k = 0; k < this.relationInstances.length; k++) {
	    this.tmpCardinality = (this.destinationsTable.getValueAt(k, 1))
		    .toString();
	    if (this.tmpCardinality.compareTo("1") == 0) {
		(this.relationInstances[k].getLogicalRelation())
			.setDestinationCardinality(this.tmpCardinality);
	    } else if (this.tmpCardinality.compareTo("many") == 0) {
		(this.relationInstances[k].getLogicalRelation())
			.setDestinationCardinality("m");
	    }

	    else if (this.tmpCardinality.compareTo("custom") == 0) {
		String str = (this.destinationsTable.getValueAt(k, 2))
			.toString();

		if (GraphicsUtils.checkCustomCardinalityLegality(str)) {
		    (this.relationInstances[k].getLogicalRelation())
			    .setDestinationCardinality(str);
		} else {
		    JOptionPane
			    .showMessageDialog(
				    null,
				    "Custom cardinality should be of the form min..max or v1, v2, v3 or combination of them.",
				    "Opcat 2 - Error",
				    JOptionPane.ERROR_MESSAGE);
		    JTable table = this.destinationsTable.getTable();
		    DefaultCellEditor de = (DefaultCellEditor) table
			    .getCellEditor(table.getSelectedRow(), 2);
		    table.editCellAt(table.getSelectedRow(), 2);
		    JTextField tf = (JTextField) de.getComponent();
		    tf.setSelectionStart(0);
		    tf.setSelectionEnd(tf.getText().length());
		    tf.requestFocus();
		    return false;
		}
	    }

	}

	this.myGraphicalRelation.setBackgroundColor(this.bgColor
		.getBackground());
	this.myGraphicalRelation.setTextColor(this.textColor.getBackground());
	this.myGraphicalRelation.setBorderColor(this.borderColor
		.getBackground());

	this.myGraphicalRelation.updateInstances();
	Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);

	return true;
    }

    // ===============================================================

    class BgColorButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Color newColor = JColorChooser.showDialog(
		    RelationPropertiesDialog.this, "Choose Relation Color",
		    RelationPropertiesDialog.this.bgColor.getBackground());
	    if (newColor != null) {
		(RelationPropertiesDialog.this).bgColor.setBackground(newColor);
	    }
	}
    }

    // ===============================================================

    class TextColorButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Color newColor = JColorChooser.showDialog(
		    RelationPropertiesDialog.this, "Choose Text Color",
		    RelationPropertiesDialog.this.textColor.getBackground());
	    if (newColor != null) {
		(RelationPropertiesDialog.this).textColor
			.setBackground(newColor);
	    }
	}
    }

    // ===============================================================

    class BorderColorButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Color newColor = JColorChooser.showDialog(
		    RelationPropertiesDialog.this, "Choose Border Color",
		    RelationPropertiesDialog.this.borderColor.getBackground());
	    if (newColor != null) {
		(RelationPropertiesDialog.this).borderColor
			.setBackground(newColor);
	    }
	}
    }

    // ===============================================================

    class OkListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if ((RelationPropertiesDialog.this)._updateRelationData()) {
		(RelationPropertiesDialog.this).dispose();
	    }
	    if (Opcat2.getCurrentProject() != null)
		Opcat2.getCurrentProject().setCanClose(false);
	    return;
	}
    }

    // ===============================================================

    class ApplyListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    (RelationPropertiesDialog.this)._updateRelationData();
	    RelationPropertiesDialog.this.opdRelation.repaint();
	    RelationPropertiesDialog.this.opdRelation.repaintLines();
	}
    }

    // ===============================================================

    class CancelListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    (RelationPropertiesDialog.this).dispose();
	}
    }

}
