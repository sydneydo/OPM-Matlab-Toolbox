package gui.opdGraphics.dialogs;

import exportedAPI.OpdKey;
import gui.Opcat2;
import gui.opdGraphics.GraphicsUtils;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmBiDirectionalRelation;
import gui.projectStructure.GeneralRelationEntry;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.MainStructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * <p>
 * Constructs and shows properties dialog box for General Bi-directional
 * Relation represented by class <a href = "OpdBiDirectionalRelation.html"><code>OpdBiDirectionalRelation</code></a>.
 * Shown when <code>callPropertiesDialog()</code> is called.
 */

public class GeneralBiDirRelationPropertiesDialog extends JDialog implements
		ComponentListener {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	// private JTextArea description;
	private JTextField sourceThing, destinationThing;

	private JComboBox sourceCardinality, destinationCardinality;

	private JTextField customSourceCardinality, customDestinationCardinality;

	private JTextField forwardRelationMeaning;

	private JTextField backwardRelationMeaning;

	private OpmBiDirectionalRelation biDirGeneralRelation;

	// private OpdBiDirectionalRelation opdBiDirGeneral;

	private JPanel p1, p2; // tmp panels

	private JRadioButton environmental, system;

	private ButtonGroup esBg;

	private JButton okButton, cancelButton, applyButton;

	private JButton bgColor, textColor, borderColor;

	private String sourceName, destinationName;

	private MainStructure myStructure;

	private GeneralRelationEntry myEntry;

	private GeneralRelationInstance myInstance;

	// reuseCommnet

	// this file has been changed by the reused project team
	// the changes aim to support locking of items
	// the change disbles all the GUI changing for elements which are locked
	// private JCheckBox lockStatus;
	// private boolean isLocked;
	// endReuseCommnet

	// ===============================================================

	/**
	 * Constructor:
	 * 
	 * @param <code>parent</code> -- parent frame, Opcat2 application window
	 * @param <code>biDirGeneral</code> -- the
	 *            <code>OpdBiDirectionalRelation</code> to show properies for
	 * @param <code>sName</code> -- The name of the source OPD component, OPD
	 *            Object
	 * @param <code>dName</code> -- The name of the destination OPD component,
	 *            OPD Object
	 */

	public GeneralBiDirRelationPropertiesDialog(
			OpmBiDirectionalRelation relation, OpdKey key, OpdProject myProject) {
		super(Opcat2.getFrame(), "Bi-Directional General Relation Properties",
				true);

		this.addComponentListener(this);
		this.biDirGeneralRelation = relation;
		// reuseCommnet
		// isLocked=myInstance.getEntry().getLocked();
		// endReuseCommnet
		this.myStructure = myProject.getComponentsStructure();
		this.myEntry = (GeneralRelationEntry) this.myStructure
				.getEntry(this.biDirGeneralRelation.getId());
		this.myInstance = (GeneralRelationInstance) this.myEntry.getInstance(key);

		long sId, dId;

		sId = myInstance.getSourceInstance().getEntry().getId();
		dId = myInstance.getDestinationInstance().getEntry().getId();

		this.sourceName = this.myStructure.getEntry(sId).getLogicalEntity().getName()
				.replace('\n', ' ');
		this.destinationName = this.myStructure.getEntry(dId).getLogicalEntity()
				.getName().replace('\n', ' ');

		// init all variables

		this._initVariables();

		Container contPane = this.getContentPane();
		contPane.setLayout(new BoxLayout(contPane, BoxLayout.Y_AXIS));

		JTabbedPane tabs = new JTabbedPane();

		tabs.add("General", this._constructGeneralTab());
		tabs.add("Web", this._constructWebTab());
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

	// ===============================================================

	public void componentShown(ComponentEvent e) {
		this.forwardRelationMeaning.requestFocus();
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
	}

	private void _initVariables() {
		try {
			// description = new JTextArea("Here will be description", 3, 30);
			// description.setEditable(false);

			this.forwardRelationMeaning = new JTextField(this.biDirGeneralRelation
					.getForwardRelationMeaning());
			this.backwardRelationMeaning = new JTextField(this.biDirGeneralRelation
					.getBackwardRelationMeaning());

			this.sourceThing = new JTextField(this.sourceName);
			this.sourceThing.setEditable(false);
			this.destinationThing = new JTextField(this.destinationName);
			this.destinationThing.setEditable(false);

			this.sourceCardinality = new JComboBox();
			this.sourceCardinality.addItem("1");
			this.sourceCardinality.addItem("many");
			this.sourceCardinality.addItem("custom");
			this.customSourceCardinality = new JTextField();
			this.customSourceCardinality.setEditable(false);

			if (this.biDirGeneralRelation.getSourceCardinality().compareTo("1") == 0) {
				this.sourceCardinality.setSelectedItem("1");
			} else {
				if (this.biDirGeneralRelation.getSourceCardinality().compareTo("m") == 0) {
					this.sourceCardinality.setSelectedItem("many");
				} else {
					this.sourceCardinality.setSelectedItem("custom");
					this.customSourceCardinality.setEditable(true);
					this.customSourceCardinality.setText(this.biDirGeneralRelation
							.getSourceCardinality());
				}
			}

			this.destinationCardinality = new JComboBox();
			this.destinationCardinality.addItem("1");
			this.destinationCardinality.addItem("many");
			this.destinationCardinality.addItem("custom");
			this.customDestinationCardinality = new JTextField();
			this.customDestinationCardinality.setEditable(false);

			if (this.biDirGeneralRelation.getDestinationCardinality().compareTo("1") == 0) {
				this.destinationCardinality.setSelectedItem("1");
			} else {
				if (this.biDirGeneralRelation.getDestinationCardinality().compareTo(
						"m") == 0) {
					this.destinationCardinality.setSelectedItem("many");
				} else {
					this.destinationCardinality.setSelectedItem("custom");
					this.customDestinationCardinality.setEditable(true);
					this.customDestinationCardinality.setText(this.biDirGeneralRelation
							.getDestinationCardinality());
				}
			}

			this.sourceCardinality
					.addActionListener(new SourceCardinalityListener());
			this.destinationCardinality
					.addActionListener(new DestinationCardinalityListener());
		} catch (Exception e) {
			System.out.println("Problem2!!");
		}

		this.environmental = new JRadioButton("Environmental");
		this.system = new JRadioButton("Systemic");
		this.esBg = new ButtonGroup();
		this.esBg.add(this.environmental);
		this.esBg.add(this.system);

		if (this.myEntry.getLogicalEntity().isEnviromental()) {
			this.environmental.setSelected(true);
		} else {
			this.system.setSelected(true);
		}

		this.bgColor = new JButton("    ");
		this.bgColor.setBackground(this.myInstance.getBackgroundColor());
		this.textColor = new JButton("    ");
		this.textColor.setBackground(this.myInstance.getTextColor());
		this.borderColor = new JButton("    ");
		this.borderColor.setBackground(this.myInstance.getBorderColor());

	}

	// ===============================================================

	private JPanel _constructGeneralTab() {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Relation description
		// p1 = new JPanel();
		// p1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
		// "Description"), BorderFactory.createEmptyBorder(5,5,5,5)));
		// p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		// description.setBackground(p1.getBackground());
		// p1.add(description);
		// tab.add(p1);

		// forward relation meaning
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "  Forward Tag  "));
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));
		this.p1.add(this.forwardRelationMeaning);
		tab.add(this.p1);

		tab.add(Box.createVerticalStrut(10));

		// backward relation meaning
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "  Backward Tag  "));
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));
		this.p1.add(this.backwardRelationMeaning);
		tab.add(this.p1);

		tab.add(Box.createVerticalStrut(10));

		// source and destination pref
		// source
		this.p2 = new JPanel();
		this.p2.setLayout(new GridLayout(2, 3, 3, 0));
		this.p2.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "  Source  "));
		JLabel source1 = new JLabel("           Name");
		JLabel source2 = new JLabel("Participation Constraint");
		JLabel source3 = new JLabel("           Custom");
		this.p2.add(source1);
		this.p2.add(source2);
		this.p2.add(source3);
		this.sourceThing.setBorder(BorderFactory.createEtchedBorder());
		this.customSourceCardinality.setBorder(BorderFactory.createEtchedBorder());
		this.p2.add(this.sourceThing);
		this.p2.add(this.sourceCardinality);
		this.p2.add(this.customSourceCardinality);
		tab.add(this.p2);

		tab.add(Box.createVerticalStrut(5));

		// destination
		this.p2 = new JPanel();
		this.p2.setLayout(new GridLayout(2, 3, 3, 0));
		this.p2.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "  Destination  "));
		JLabel destination1 = new JLabel("           Name");
		JLabel destination2 = new JLabel("Participation Constraint");
		JLabel destination3 = new JLabel("           Custom");
		this.p2.add(destination1);
		this.p2.add(destination2);
		this.p2.add(destination3);
		this.destinationThing.setBorder(BorderFactory.createEtchedBorder());
		this.customDestinationCardinality.setBorder(BorderFactory
				.createEtchedBorder());
		this.p2.add(this.destinationThing);
		this.p2.add(this.destinationCardinality);
		this.p2.add(this.customDestinationCardinality);

		tab.add(this.p2);
		return tab;
	}

	private JPanel _constructWebTab() {
		JPanel wp = new JPanel();
		wp.setLayout(new BorderLayout());
		JPanel p2 = new JPanel();

		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		p2.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Origin "));
		p2.add(this.environmental);

		p2.add(this.system);
		wp.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
		wp.add(p2);
		wp.add(Box.createVerticalStrut(200), BorderLayout.SOUTH);
		wp.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		wp.add(Box.createHorizontalStrut(10), BorderLayout.WEST);

		return wp;
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
		JLabel borderColorLabel = new JLabel("Line Color:");
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

		if ((String.valueOf(this.sourceCardinality.getSelectedItem()))
				.compareTo("custom") == 0) {
			if (GraphicsUtils
					.checkCustomCardinalityLegality(this.customSourceCardinality
							.getText())) {
				this.biDirGeneralRelation
						.setSourceCardinality(this.customSourceCardinality.getText());
			} else {
				JOptionPane
						.showMessageDialog(
								this,
								"Custom cardinality should be of the form min..max or v1, v2, v3 or combination of them.",
								"Opcat 2 - Error", JOptionPane.ERROR_MESSAGE);
				this.customSourceCardinality.setSelectionStart(0);
				this.customSourceCardinality.setSelectionEnd(this.customSourceCardinality
						.getText().length());
				this.customSourceCardinality.requestFocus();
				return false;
			}
		} else {
			if ((String.valueOf(this.sourceCardinality.getSelectedItem()))
					.compareTo("many") == 0) {
				this.biDirGeneralRelation.setSourceCardinality("m");
			} else {
				this.biDirGeneralRelation.setSourceCardinality("1");
			}
		}

		if ((String.valueOf(this.destinationCardinality.getSelectedItem()))
				.compareTo("custom") == 0) {
			if (GraphicsUtils
					.checkCustomCardinalityLegality(this.customDestinationCardinality
							.getText())) {
				this.biDirGeneralRelation
						.setDestinationCardinality(this.customDestinationCardinality
								.getText());
			} else {
				JOptionPane
						.showMessageDialog(
								this,
								"Custom cardinalit,y should be of the form min..max or v1, v2, v3 or combination of them.",
								"Opcat 2 - Error", JOptionPane.ERROR_MESSAGE);
				this.customDestinationCardinality.setSelectionStart(0);
				this.customDestinationCardinality
						.setSelectionEnd(this.customDestinationCardinality.getText()
								.length());
				this.customDestinationCardinality.requestFocus();
				return false;
			}

		}

		else {
			if ((String.valueOf(this.destinationCardinality.getSelectedItem()))
					.compareTo("many") == 0) {
				this.biDirGeneralRelation.setDestinationCardinality("m");
			} else {
				this.biDirGeneralRelation.setDestinationCardinality("1");
			}
		}

		this.biDirGeneralRelation.setForwardRelationMeaning(this.forwardRelationMeaning
				.getText());
		this.biDirGeneralRelation.setBackwardRelationMeaning(this.backwardRelationMeaning
				.getText());
		this.biDirGeneralRelation.setEnviromental(this.environmental.isSelected());

		this.myEntry.updateInstances();

		this.myInstance.setBackgroundColor(this.bgColor.getBackground());
		this.myInstance.setTextColor(this.textColor.getBackground());
		this.myInstance.setBorderColor(this.borderColor.getBackground());
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);

		return true;
	}

	// ===============================================================

	class BgColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					GeneralBiDirRelationPropertiesDialog.this,
					"Choose Relation Color", GeneralBiDirRelationPropertiesDialog.this.bgColor.getBackground());
			if (newColor != null) {
				(GeneralBiDirRelationPropertiesDialog.this).bgColor
						.setBackground(newColor);
			}
		}
	}

	// ===============================================================

	class TextColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					GeneralBiDirRelationPropertiesDialog.this,
					"Choose Text Color", GeneralBiDirRelationPropertiesDialog.this.textColor.getBackground());
			if (newColor != null) {
				(GeneralBiDirRelationPropertiesDialog.this).textColor
						.setBackground(newColor);
			}
		}
	}

	// ===============================================================

	class BorderColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					GeneralBiDirRelationPropertiesDialog.this,
					"Choose Border Color", GeneralBiDirRelationPropertiesDialog.this.borderColor.getBackground());
			if (newColor != null) {
				(GeneralBiDirRelationPropertiesDialog.this).borderColor
						.setBackground(newColor);
			}
		}
	}

	// ===============================================================

	class SourceCardinalityListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (((String.valueOf(GeneralBiDirRelationPropertiesDialog.this.sourceCardinality.getSelectedItem()))
					.compareTo("custom")) == 0) {
				GeneralBiDirRelationPropertiesDialog.this.customSourceCardinality.setEditable(true);
				GeneralBiDirRelationPropertiesDialog.this.customSourceCardinality.requestFocus();
			} else {
				GeneralBiDirRelationPropertiesDialog.this.customSourceCardinality.setEditable(false);
				GeneralBiDirRelationPropertiesDialog.this.customSourceCardinality.setText("");
			}
			return;
		}
	}

	// ===============================================================

	class DestinationCardinalityListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (((String.valueOf(GeneralBiDirRelationPropertiesDialog.this.destinationCardinality.getSelectedItem()))
					.compareTo("custom")) == 0) {
				GeneralBiDirRelationPropertiesDialog.this.customDestinationCardinality.setEditable(true);
				GeneralBiDirRelationPropertiesDialog.this.customDestinationCardinality.requestFocus();
			} else {
				GeneralBiDirRelationPropertiesDialog.this.customDestinationCardinality.setEditable(false);
				GeneralBiDirRelationPropertiesDialog.this.customDestinationCardinality.setText("");
			}
			return;
		}
	}

	// ===============================================================

	class OkListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((GeneralBiDirRelationPropertiesDialog.this)
					._updateRelationData()) {
				(GeneralBiDirRelationPropertiesDialog.this).dispose();
			}
			if (Opcat2.getCurrentProject() != null) Opcat2.getCurrentProject().setCanClose(false); 
			return;
		}
	}

	// ===============================================================

	class ApplyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			(GeneralBiDirRelationPropertiesDialog.this)._updateRelationData();
		}
	}

	// ===============================================================

	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			(GeneralBiDirRelationPropertiesDialog.this).dispose();
		}
	}

}
