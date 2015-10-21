package gui.opdGraphics.dialogs;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import gui.Opcat2;
import gui.opdGraphics.GraphicsUtils;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmProceduralLink;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ProcessEntry;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * <p>
 * Constructs and shows properties dialog box for one of the OPD links
 * 
 * <br>
 * Shown when <code>callPropertiesDialog()</code> is called.
 */
public class LinkPropertiesDialog extends JDialog implements OpcatDialog,
		ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private JTextArea description, condition, iconPath;

	private JScrollPane descriptionScrollPane, conditionScrollPane,
			iconScrollPane;

	private JTextField sourceThing, destinationThing;

	private JTextField path;

	private JTextField resourceConsumption;

	private TimeSpecifier maxReactionTime, minReactionTime;

	private OpmProceduralLink opmLink;

	// private OpdLink opdLink;

	private JPanel p1, p2; // tmp panels

	private JButton okButton, cancelButton, applyButton;

	private JButton bgColor, textColor, borderColor, iconButton;

	private JCheckBox accumolate;

	private String sourceName, destinationName;

	private MainStructure myStructure;

	private LinkEntry myEntry;

	private LinkInstance myInstance;

	private JTabbedPane tabs;

	private JRadioButton environmental, system;

	private ButtonGroup esBg;

	// ===============================================================
	/**
	 * Constructor:
	 * 
	 * @param <code>parent</code> -- parent frame, Opcat2 application window
	 * @param <code>opdLink</code> -- the one of the following OPD links to show
	 *            properies for, the link is a subclass of <a href =
	 *            "AroundDragger.html"><code>AroundDragger</code></a>
	 *            <ul>
	 *            <li><a href = "OpdEffectLink.html"><code>OpdEffectLink</code></a></li>
	 *            <li><a href = "OpdInvocationLink.html"><code>OpdInvocationLink</code></a></li>
	 *            <li><a href = "OpdResultLink.html"><code>OpdResultLink</code></a></li>
	 *            <li><a href = "OpdAgentLink.html"><code>OpdAgentLink</code></a></li>
	 *            <li><a href = "OpdConditionLink.html"><code>OpdConditionLink</code></a></li>
	 *            <li><a href = "OpdEventLink.html"><code>OpdEventLink</code></a></li>
	 *            <li><a href = "OpdExceptionLink.html"><code>OpdExceptionLink</code></a></li>
	 *            <li><a href = "OpdInstrumentLink.html"><code>OpdInstrumentLink</code></a></li>
	 *            <li><a href = "Opd...Link.html"><code>Opd...Link</code></a></li>
	 *            <li><a href = "Opd...Link.html"><code>Opd...Link</code></a></li>
	 *            </ul>
	 * @param <code>dialogName</code> -- The title of dialog box
	 * @param <code>sName</code> -- The name of the source OPD component, OPD
	 *            Object
	 * @param <code>dName</code> -- The name of the destination OPD component,
	 *            OPD Object
	 */
	public LinkPropertiesDialog(OpmProceduralLink link, OpdKey key,
			OpdProject myProject, String dialogName) {
		super(Opcat2.getFrame(), dialogName + " Link Properties", true);
		this.addComponentListener(this);
		this.opmLink = link;

		this.myStructure = myProject.getComponentsStructure();
		this.myEntry = (LinkEntry) this.myStructure.getEntry(this.opmLink
				.getId());
		this.myInstance = (LinkInstance) this.myEntry.getInstance(key);

		long sId, dId;

		sId = myInstance.getSourceInstance().getEntry().getId(); // .getSourceId();
		dId = myInstance.getDestinationInstance().getEntry().getId(); // this.opmLink.getDestinationId();

		this.sourceName = this.myStructure.getEntry(sId).getLogicalEntity()
				.getName().replace('\n', ' ');
		this.destinationName = this.myStructure.getEntry(dId)
				.getLogicalEntity().getName().replace('\n', ' ');

		// init all variables
		this._initVariables();

		Container contPane = this.getContentPane();
		contPane.setLayout(new BoxLayout(contPane, BoxLayout.Y_AXIS));

		this.tabs = new JTabbedPane();

		this.tabs.add("General", this._constructGeneralTab());
		if (OpcatConstants.isEventLink(this.myEntry.getLinkType())) {
			this.tabs.add("Reaction Time", this._constructTimingTab());
		}
		this.tabs.add("Web", this._constructWebTab());
		this.tabs.add("Misc.", this._constructMiscTab());

		if (OpcatConstants.EFFECT_LINK == this.myEntry.getLinkType()) {
			ConnectionEdgeEntry sourceEntry = (ConnectionEdgeEntry) this.myStructure
					.getEntry(sId);
			ConnectionEdgeEntry destinationEntry = (ConnectionEdgeEntry) this.myStructure
					.getEntry(dId);
			ProcessEntry proc = null;
			ObjectEntry obj = null;
			if (sourceEntry instanceof ProcessEntry) {
				proc = (ProcessEntry) sourceEntry;
				obj = (ObjectEntry) destinationEntry;
			} else {
				proc = (ProcessEntry) destinationEntry;
				obj = (ObjectEntry) sourceEntry;
			}

			if (proc.getZoomedInOpd() == null) {
				this.tabs.add("Resources", _constructResourceTab(obj, proc));
			}
		}

		contPane.add(this.tabs);

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
		this.p1.add(Box.createGlue());
		this.p1.add(this.okButton);
		this.p1.add(this.cancelButton);
		this.p1.add(this.applyButton);
		contPane.add(this.p1);
		// -----------------------------------------------------------------
		// Escape & Enter listener
		KeyListener escapeAndEnterListener = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					(LinkPropertiesDialog.this).dispose();
				}
				if ((e.getKeyCode() == KeyEvent.VK_ENTER)
						&& !LinkPropertiesDialog.this.condition.hasFocus()) {
					if ((LinkPropertiesDialog.this)._updateLinkData()) {
						(LinkPropertiesDialog.this).dispose();
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

	// ===============================================================

	public void componentShown(ComponentEvent e) {
		this.path.requestFocus();
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
	}

	private void _initVariables() {
		try {
			this.sourceThing = new JTextField(this.sourceName);
			this.sourceThing.setEditable(false);
			this.destinationThing = new JTextField(this.destinationName);
			this.destinationThing.setEditable(false);

			this.condition = new JTextArea(this.opmLink.getCondition(), 5, 0);

			this.description = new JTextArea(this.opmLink.getDescription(), 5,
					0);
			// condition.setEditable(true);

			this.path = new JTextField(this.opmLink.getPath());
			// path.setEditable(true);

			this.maxReactionTime = new TimeSpecifier(SwingConstants.HORIZONTAL,
					SwingConstants.TOP, 3, 3);
			this.maxReactionTime.setTime(this.opmLink.getMaxReactionTime());
			this.minReactionTime = new TimeSpecifier(SwingConstants.HORIZONTAL,
					SwingConstants.TOP, 3, 3);
			this.minReactionTime.setTime(this.opmLink.getMinReactionTime());

		} catch (Exception e) {
			System.out.println("Problem2!!");
		}

		this.descriptionScrollPane = new JScrollPane(this.description,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.description.setLineWrap(true);
		this.description.setWrapStyleWord(true);

		this.conditionScrollPane = new JScrollPane(this.condition,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.condition.setLineWrap(true);
		this.condition.setWrapStyleWord(true);

		this.environmental = new JRadioButton("Environmental");
		this.system = new JRadioButton("Systemic");
		this.esBg = new ButtonGroup();
		this.esBg.add(this.environmental);
		this.esBg.add(this.system);
		if (this.opmLink.isEnviromental()) {
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

		// set icon
		this.iconPath = new JTextArea(this.myEntry.getIconPath().trim(), 2, 24);
		this.iconPath.setLineWrap(true);
		this.iconPath.setWrapStyleWord(true);
		this.iconScrollPane = new JScrollPane(this.iconPath,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	}

	// ===============================================================

	private JPanel _constructGeneralTab() {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// source and destination pref :
		this.p1 = new JPanel();
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));
		// p1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
		// BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		// 1.source
		this.p2 = new JPanel();
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
		this.p2.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Source name :"));
		this.p2.add(this.sourceThing);
		this.p1.add(this.p2);
		// 2.destination
		this.p2 = new JPanel();
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
		this.p2.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Destination name :"));
		this.p2.add(this.destinationThing);
		this.p1.add(this.p2);
		tab.add(this.p1);

		tab.add(Box.createVerticalStrut(5));

		// description
		this.p1 = new JPanel();
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Description :"));
		this.p1.add(this.descriptionScrollPane);
		tab.add(this.p1);

		// condition area
		this.p1 = new JPanel();
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Condition :"));
		this.p1.add(this.conditionScrollPane);
		tab.add(this.p1);

		tab.add(Box.createVerticalStrut(5));

		// path field
		this.p1 = new JPanel();
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.Y_AXIS));
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Path :"));
		this.p1.add(this.path);
		tab.add(this.p1);

		return tab;
	}

	// ===============================================================

	private JPanel _constructTimingTab() {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		this.minReactionTime.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), " Minimum Reaction Time "));
		this.maxReactionTime.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), " Maximum Reaction Time "));
		tab.add(Box.createVerticalStrut(10));
		tab.add(this.minReactionTime);
		tab.add(Box.createVerticalStrut(10));
		tab.add(this.maxReactionTime);
		// tab.add(Box.createVerticalStrut(40));
		return tab;
	}

	private JPanel _constructResourceTab(ObjectEntry obj, ProcessEntry proc) {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		this.resourceConsumption = new JTextField(String.valueOf(myEntry
				.getResourceConsumption()));

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Consumption of "
				+ obj.getName().replace("\n", " ") + " by "
				+ proc.getName().replace("\n", " ")));
		p.add(new JLabel("Consumption : "));
		p.add(this.resourceConsumption);
		p.add(new JLabel("Accumulate ? "));
		accumolate = new JCheckBox();
		accumolate.setSelected(myEntry.isResourceConsumptionAccumolated());
		p.add(accumolate);
		tab.add(p);

		JTextArea text = new JTextArea();
		text.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Help "));
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		text.setBackground(tab.getBackground());
		text.setText("The number in the above field will be added to the "
				+ "Measurement Unit Value (defined in "
				+ obj.getName().replace("\n", " ") + " Resource tab)."
				+ "For example, If the Measurement Unit Value of "
				+ obj.getName().replace("\n", " ")
				+ " is 100, entering a -10 in the above field "
				+ "will decrease " + "the Measurement Unit Value "
				+ "by 10 every time " + proc.getName().replace("\n", " ")
				+ " is activated.");
		// text.setUI(new MultiLineLabelUI());
		tab.add(text);
		tab.add(Box.createVerticalStrut(10));

		return tab;
	}

	// ===============================================================

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
		wp.add(Box.createVerticalStrut(155), BorderLayout.SOUTH);
		wp.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		wp.add(Box.createHorizontalStrut(10), BorderLayout.WEST);

		return wp;
	}

	private JPanel _constructMiscTab() {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		// tab.setBorder(BorderFactory.createCompoundBorder(BorderFactory
		// .createEmptyBorder(8, 8, 8, 8), BorderFactory
		// .createEtchedBorder()));
		JLabel bgColorLabel = new JLabel("Background Color:");
		JLabel textColorLabel = new JLabel("Text Color:");
		JLabel borderColorLabel = new JLabel("Line Color:");

		JPanel colors = new JPanel();
		colors.setLayout(new BoxLayout(colors, BoxLayout.X_AXIS));
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
		this.p1.add(Box.createVerticalStrut(10));
		this.bgColor.addActionListener(new BgColorButtonListener());
		// bgColor.putClientProperty("gradientStart",
		// myInstance.getBackgroundColor());
		// bgColor.putClientProperty("gradientEnd",
		// myInstance.getBackgroundColor());
		// bgColor.setBackground(myInstance.getBackgroundColor());
		// bgColor.setForeground(myInstance.getBackgroundColor());
		this.textColor.addActionListener(new TextColorButtonListener());
		// textColor.setBackground(myInstance.getTextColor());
		// textColor.setForeground(myInstance.getTextColor());
		// textColor.putClientProperty("gradientStart",
		// myInstance.getBackgroundColor());
		// textColor.putClientProperty("gradientEnd",
		// myInstance.getBackgroundColor());
		this.borderColor.addActionListener(new BorderColorButtonListener());
		// borderColor.setBackground(myInstance.getBorderColor());
		// borderColor.setForeground(myInstance.getBorderColor());
		// borderColor.putClientProperty("gradientStart",
		// myInstance.getBackgroundColor());
		// borderColor.putClientProperty("gradientEnd",
		// myInstance.getBackgroundColor());
		this.p2.add(this.bgColor);
		this.p2.add(Box.createVerticalStrut(8));
		this.p2.add(this.textColor);
		this.p2.add(Box.createVerticalStrut(8));
		this.p2.add(this.borderColor);
		this.p2.add(Box.createVerticalStrut(10));
		colors.add(this.p1);
		colors.add(this.p2);
		colors.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Colors ")));
		tab.add(colors);

		// Icon
		this.p1 = new JPanel();
		this.iconButton = new JButton("Icon");
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Icon "));
		this.iconButton.addActionListener(new IconListner(myEntry, this));
		this.p1.add(this.iconScrollPane);
		this.p1.add(this.iconButton);
		tab.add(this.p1);

		tab.add(Box.createHorizontalStrut(140));
		return tab;
	}

	// ===============================================================

	private boolean _updateLinkData() {

		if (GraphicsUtils.getMsec4TimeString(this.minReactionTime.getTime()) > GraphicsUtils
				.getMsec4TimeString(this.maxReactionTime.getTime())) {
			this.tabs.setSelectedIndex(1);
			JOptionPane
					.showMessageDialog(
							Opcat2.getFrame(),
							"Min reaction time can't be larger than max reaction time ",
							"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ((resourceConsumption != null)
				&& (resourceConsumption.getText() != null)) {
			if ((myEntry.getLinkType() == OpcatConstants.EFFECT_LINK)
					&& !(resourceConsumption.getText().equalsIgnoreCase(""))) {
				double noi = 0.0;
				try {
					noi = Double.parseDouble(resourceConsumption.getText());
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(Opcat2.getFrame(),
							"Consumption must be a number", "Opcat2 - Error",
							JOptionPane.ERROR_MESSAGE);
					return false;
				}
				this.myEntry.setResourceConsumption(noi);
				this.myEntry.setResourceConsumptionAccumolated(accumolate
						.isSelected());
			}
		}

		this.opmLink.setCondition(this.condition.getText());
		this.opmLink.setDescription(this.description.getText());

		this.opmLink.setPath(this.path.getText());
		this.opmLink.setMinReactionTime(this.minReactionTime.getTime());
		this.opmLink.setMaxReactionTime(this.maxReactionTime.getTime());
		this.opmLink.setEnviromental(this.environmental.isSelected());

		this.myInstance.setBackgroundColor(this.bgColor.getBackground());
		this.myInstance.setTextColor(this.textColor.getBackground());
		this.myInstance.setBorderColor(this.borderColor.getBackground());

		this.myEntry.setIcon(this.iconPath.getText().trim());

		this.myEntry.updateInstances();

		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);

		return true;
	}

	// ===============================================================

	class BgColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					LinkPropertiesDialog.this, "Choose Link Color",
					LinkPropertiesDialog.this.bgColor.getBackground());
			if (newColor != null) {
				(LinkPropertiesDialog.this).bgColor.setBackground(newColor);
			}
		}
	}

	// ===============================================================

	class TextColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					LinkPropertiesDialog.this, "Choose Text Color",
					LinkPropertiesDialog.this.textColor.getBackground());
			if (newColor != null) {
				(LinkPropertiesDialog.this).textColor.setBackground(newColor);
			}
		}
	}

	// ===============================================================

	class BorderColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					LinkPropertiesDialog.this, "Choose Border Color",
					LinkPropertiesDialog.this.borderColor.getBackground());
			if (newColor != null) {
				(LinkPropertiesDialog.this).borderColor.setBackground(newColor);
			}
		}
	}

	// ===============================================================

	class OkListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((LinkPropertiesDialog.this)._updateLinkData()) {
				(LinkPropertiesDialog.this).dispose();
			}
			if (Opcat2.getCurrentProject() != null)
				Opcat2.getCurrentProject().setCanClose(false);
			return;
		}
	}

	// ===============================================================

	class ApplyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			(LinkPropertiesDialog.this)._updateLinkData();
		}
	}

	// ===============================================================

	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			(LinkPropertiesDialog.this).dispose();
		}
	}

	@Override
	public JTextArea getIconPath() {
		return iconPath;
	}


}