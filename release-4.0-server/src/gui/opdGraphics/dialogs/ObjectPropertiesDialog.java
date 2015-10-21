package gui.opdGraphics.dialogs;

import exportedAPI.OpcatConstants;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import gui.Opcat2;
import gui.dataProject.DataCreatorType;
import gui.metaLibraries.dialogs.LibraryLocationDialog;
import gui.metaLibraries.dialogs.RolesDialog;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdObject;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmObject;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.ObjectEntry;
import gui.undo.UndoableChangeObject;
import gui.util.CustomFileFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container; //import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
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
import javax.swing.event.UndoableEditEvent;

/**
 * Importing ontologies package.
 * @author Eran Toch
 */

/**
 * This class is a properties dialog box for <a href = "OpdObject.html">
 * <code>OpdObject</code></a>.
 * <p>
 * Constructs and shows properties dialog box for one of the OPD Objects
 * 
 * <br>
 * Shown when <code>callPropertiesDialog()</code> is called.
 */
public class ObjectPropertiesDialog extends JDialog implements OpcatDialog,
		ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	JComboBox measuermentUnitdrop = null;
	private JCheckBox showIcon = null;

	private JTextField indexName, initialValue, indexOrder, mesUnit[],
			mesUnitInitialValue[], mesUnitMinValue[], mesUnitMaxValue[];

	private ItemListener measurementUnitListnerA = new measurementUnitListner();

	int resourceTabIndex = -1;

	private JTextArea name, description, url, iconPath,
			generaldescription /*
								 * , type
								 */;

	private JRadioButton physical, informational, environmental, system,
			additionHelpOff, additionHelpON;

	private JComboBox scope;

	private JCheckBox isPersistent, isKey;

	private JButton okButton, cancelButton, applyButton, bgColor, textColor,
			borderColor, resourceButton, iconButton;

	private ButtonGroup piBg, esBg, addBg;

	private JScrollPane /* typeScrollPane, */
	generaldescriptionScrollPane, nameScrollPane, urlScrollPane,
			iconScrollPane;

	private JPanel p1, p2; // tmp panels

	private OpmObject object;

	private OpdObject opdObject;

	private ObjectEntry myEntry;

	protected JList stateList;

	private StatePanel statesPanel;

	private OpdProject myProject;

	private ObjectTypePanel objTypePanel;

	private JTabbedPane tabs;

	private DirectionPanel dp;

	private OpdObject sampleObject;

	private String defaultName;

	private String defaultUrl;

	// private final int numTabs = 3;
	private int showTabs;

	private boolean okPressed = false;

	private boolean isCreation;

	private boolean addHelpON = true;

	/**
	 * A JPanel containing roles selection.
	 * 
	 * @author Eran Toch
	 */
	// private RolesDialog rolesTab;
	/**
	 * Constructor:
	 * 
	 * @param <code>parent</code> -- parent frame, Opcat2 application window or
	 *        <code>null</code>
	 * @param <code>OpdObject pObject</code> -- the OPD object to show
	 *        properties dialog for.
	 * @param <code>ThingEntry pEntry</code> -- the
	 *        <code>projectStructure.ThingEntry</code> class, entry of this OPD
	 *        Object in project dynamic structure.
	 * @see <a href = "ThingEntry.html"><code>ThingEntry</code></a> class
	 *      documentation for more information.
	 */
	public ObjectPropertiesDialog(OpdObject pObject, ObjectEntry pEntry,
			OpdProject project, boolean isCreation) {
		super(Opcat2.getFrame(), "OPD Object Properties", true);

		this.addComponentListener(this);
		this.opdObject = pObject;
		this.object = (OpmObject) pObject.getEntity();
		this.myEntry = pEntry;
		this.myProject = project;
		this.defaultName = "";
		this.defaultUrl = "";

		this.isCreation = isCreation;

		int showButtons;
		if (!isCreation) {
			this.showTabs = BaseGraphicComponent.SHOW_ALL_TABS;
			showButtons = BaseGraphicComponent.SHOW_ALL_BUTTONS;
		} else {
			this.showTabs = BaseGraphicComponent.SHOW_ALL_TABS
					- BaseGraphicComponent.SHOW_3;
			showButtons = BaseGraphicComponent.SHOW_OK
					| BaseGraphicComponent.SHOW_CANCEL;
		}

		// init all variables
		this._initVariables();

		Container contPane = this.getContentPane();
		contPane.setLayout(new BoxLayout(contPane, BoxLayout.Y_AXIS));

		this.tabs = new JTabbedPane();

		if ((this.showTabs & BaseGraphicComponent.SHOW_1) != 0) {
			this.tabs.add("General", this._constructGeneralTab());
		}

		if ((this.showTabs & BaseGraphicComponent.SHOW_7) != 0) {
			this.tabs.add("Details", this._constructDescriptionTab());
		}

		if ((this.showTabs & BaseGraphicComponent.SHOW_2) != 0) {
			/*
			 * TODO: this is the keys and indices tab. i removed this as it has
			 * no real porpose and use. we can add this later when we create a
			 * real DB creation tools. raanan
			 */
			// tabs.add("Keys & Indices", _constructKeysAndIndecesTab());
		}
		if ((this.showTabs & BaseGraphicComponent.SHOW_3) != 0) {
			this.tabs.add("States", this._constructStatesTab());
		} else {
			this.tabs.add("States", this._constructDummyStatesTab());
		}

		// Adding roles tab to the general tabs
		// By Eran Toch
		if ((this.showTabs & BaseGraphicComponent.SHOW_4) != 0) {
			// JPanel role = this._constructRolesTab();
			// this.tabs.add("Roles", role);
		}

		if ((this.showTabs & BaseGraphicComponent.SHOW_MISC) != 0) {
			this.tabs.add("Misc.", this._constructMiscTab());
		}

		if ((this.showTabs & BaseGraphicComponent.SHOW_MISC) != 0) {
			this.tabs.add("Resources", this._constructResourceTab(null));
			resourceTabIndex = tabs.getTabCount() - 1;
		}

		contPane.add(this.tabs);

		// -----------------------------------------------------------------
		// add buttons
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(1, 4);
		layout.setHgap(3);
		this.p1.setLayout(layout);
		this.p1.add(Box.createGlue());
		if ((showButtons & BaseGraphicComponent.SHOW_OK) != 0) {
			this.okButton = new JButton("OK");
			this.okButton.addActionListener(new OkListener());
			this.getRootPane().setDefaultButton(this.okButton);
			this.p1.add(this.okButton);
		}
		if ((showButtons & BaseGraphicComponent.SHOW_CANCEL) != 0) {
			this.cancelButton = new JButton("Cancel");
			this.cancelButton.addActionListener(new CancelListener());
			this.p1.add(this.cancelButton);
		}
		if ((showButtons & BaseGraphicComponent.SHOW_APPLY) != 0) {
			this.applyButton = new JButton("Apply");
			this.applyButton.addActionListener(new ApplyListener());
			this.p1.add(this.applyButton);
		}
		this.p1.add(Box.createGlue());
		contPane.add(this.p1);
		// -----------------------------------------------------------------

		// Escape & Enter listener
		KeyListener escapeAndEnterListener = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					(ObjectPropertiesDialog.this).dispose();
				}
				if ((e.getKeyCode() == KeyEvent.VK_ENTER)
						&& !ObjectPropertiesDialog.this.description.hasFocus() /*
																				 * &&
																				 * !
																				 * type
																				 * .
																				 * hasFocus
																				 * (
																				 * )
																				 */
				) {
					if ((ObjectPropertiesDialog.this)._updateObjectData()) {
						(ObjectPropertiesDialog.this).dispose();
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

	private void _initVariables() {
		try {
			if ((this.showTabs & BaseGraphicComponent.SHOW_1) != 0) {
				// general tab
				if (this.object.getName().trim().equals("")) {
					this.name = new JTextArea(this.defaultName, 3, 15);
				} else {
					this.name = new JTextArea(this.object.getName(), 3, 15);
				}
				this.name.setRequestFocusEnabled(true);
				this.name.setLineWrap(true);
				this.name.setWrapStyleWord(true);

				// set the url to be presented
				if (this.object.getUrl().trim().equals("")) {
					this.url = new JTextArea(this.defaultUrl, 4, 24);
				} else {
					this.url = new JTextArea(this.object.getUrl(), 4, 24);
				}
				this.url.setLineWrap(true);
				this.url.setWrapStyleWord(true);
				this.urlScrollPane = new JScrollPane(this.url,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

				// set icon
				this.iconPath = new JTextArea(
						this.myEntry.getIconPath().trim(), 2, 24);
				this.iconPath.setLineWrap(true);
				this.iconPath.setWrapStyleWord(true);
				this.iconScrollPane = new JScrollPane(this.iconPath,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

				this.showIcon = new JCheckBox(
						"Show Icon for this appearance ?", opdObject
								.isIconVisible());

				this.initialValue = new JTextField(this.object
						.getInitialValue());

				this.physical = new JRadioButton("Physical");
				this.physical.addActionListener(this.positionAction);
				this.informational = new JRadioButton("Informatical");
				this.informational.addActionListener(this.positionAction);
				this.piBg = new ButtonGroup();
				this.piBg.add(this.physical);
				this.piBg.add(this.informational);
				if (this.object.isPhysical()) {
					this.physical.setSelected(true);
				} else {
					this.informational.setSelected(true);
				}
				// type = new JTextArea(object.getType(), 3, 30);
				this.environmental = new JRadioButton("Environmental");
				this.environmental.addActionListener(this.positionAction);
				this.system = new JRadioButton("Systemic");
				this.system.addActionListener(this.positionAction);
				this.esBg = new ButtonGroup();
				this.esBg.add(this.environmental);
				this.esBg.add(this.system);
				if (this.object.isEnviromental()) {
					this.environmental.setSelected(true);
				} else {
					this.system.setSelected(true);
				}

				this.scope = new JComboBox();
				this.scope.addItem("Public");
				this.scope.addItem("Protected");
				this.scope.addItem("Private");
				this.scope.setSelectedIndex(Character.digit((this.object
						.getScope()).charAt(0), 3));
				// end general tab

				this.additionHelpON = new JRadioButton("Enable");
				this.additionHelpON.addActionListener(this.positionAction);
				this.additionHelpOff = new JRadioButton("Disable");
				this.additionHelpOff.addActionListener(this.positionAction);
				this.addBg = new ButtonGroup();
				this.addBg.add(this.additionHelpOff);
				this.addBg.add(this.additionHelpON);
				this.additionHelpON.setSelected(true);
				this.addHelpON = true;
			}

			if ((this.showTabs & BaseGraphicComponent.SHOW_2) != 0) {
				// keys & indices
				this.isPersistent = new JCheckBox("Persistent");
				if (this.object.isPersistent()) {
					this.isPersistent.setSelected(true);
				}

				this.isKey = new JCheckBox("Key");
				if (this.object.isKey()) {
					this.isKey.setSelected(true);
				}

				this.indexOrder = new JTextField((new Integer(this.object
						.getIndexOrder())).toString());
				this.indexName = new JTextField(this.object.getIndexName());

				/*
				 * FIXME: the idiots used the description generic attribute of
				 * the opmEntity for the keys description and now when i want to
				 * add a real description i am fucked. need to replace this by a
				 * KeysDescription attribute of an object class and add this to
				 * th esave proc. save it and use this description as it was
				 * intended. raanan
				 */
				this.description = new JTextArea(this.object.getDescription(),
						8, 30);
				// descriptionScrollPane =
				// new JScrollPane(
				// description,
				// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				// JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				this.description.setLineWrap(true);
				this.description.setWrapStyleWord(true);
				// end keys & indices

				// description
				this.generaldescription = new JTextArea(this.object
						.getDescription(), 8, 30);
				// generaldescription = new JTextArea("NOT IMPLEMENTED YET, do
				// not enter anything here", 8, 30);
				this.generaldescriptionScrollPane = new JScrollPane(
						this.generaldescription,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				this.generaldescription.setLineWrap(true);
				this.generaldescription.setWrapStyleWord(true);
			}

			this.stateList = new JList();

		} catch (Exception e) {
			System.out.println("Problem in ObjectPropertiesDialog!!");
		}

		// typeScrollPane = new JScrollPane(type,
		// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		// JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// type.setBorder(BorderFactory.createEtchedBorder());
		// type.setLineWrap(true);
		// type.setWrapStyleWord(true);

		if ((this.showTabs & BaseGraphicComponent.SHOW_MISC) != 0) {
			this.bgColor = new JButton("          ");
			this.bgColor.setBackground(this.opdObject.getBackgroundColor());
			this.textColor = new JButton("          ");
			this.textColor.setBackground(this.opdObject.getTextColor());
			this.borderColor = new JButton("          ");
			this.borderColor.setBackground(this.opdObject.getBorderColor());
		}
	}

	private JPanel _constructGeneralTab() {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Object Name / Init Value
		this.p2 = new JPanel();
		this.p2.setLayout(new GridLayout(1, 2));
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Object Name "), BorderFactory.createEmptyBorder(4, 8,
				4, 8)));
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));

		this.nameScrollPane = new JScrollPane(this.name,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.p1.add(this.nameScrollPane);
		this.p2.add(this.p1);

		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Initial Value "), BorderFactory.createEmptyBorder(4,
				8, 4, 8)));
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.Y_AXIS));
		// p1.add(Box.createVerticalStrut(30));
		this.p1.add(this.initialValue);
		this.p1.add(Box.createVerticalStrut(30));

		this.p2.add(this.p1);
		tab.add(this.p2);

		// type
		this.objTypePanel = new ObjectTypePanel(this.myEntry, this.myProject);
		tab.add(this.objTypePanel);

		// Radio buttons panel group
		this.p1 = new JPanel();
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));

		// Essence group
		this.p2 = new JPanel();
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
		this.p2.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Essence "), BorderFactory.createEmptyBorder(4, 8, 4,
				8)));
		this.p2.add(this.physical);
		this.p2.add(Box.createVerticalStrut(5));
		this.p2.add(this.informational);
		this.p1.add(this.p2);

		this.p1.add(Box.createHorizontalStrut(10));

		// Affilation group
		this.p2 = new JPanel();
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
		this.p2.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Affilation "), BorderFactory.createEmptyBorder(4, 8,
				4, 8)));
		this.p2.add(this.environmental);
		this.p2.add(Box.createVerticalStrut(5));
		this.p2.add(this.system);
		this.p1.add(this.p2);

		tab.add(this.p1);

		// scope ComboBox
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEtchedBorder(), BorderFactory.createEmptyBorder(8, 8, 8,
				8)));
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));
		JLabel l2 = new JLabel("Scope:   ");
		this.p1.add(l2);
		this.p1.add(Box.createHorizontalStrut(10));
		this.p1.add(this.scope);
		this.p2 = new JPanel();
		this.p2.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.X_AXIS));
		this.p2.add(this.p1);

		tab.add(this.p2);

		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "   Addition Helper   "));
		this.p1.setLayout(new GridLayout(2, 1));
		this.p1.add(this.additionHelpON);
		this.p1.add(this.additionHelpOff);

		tab.add(Box.createVerticalStrut(10));
		tab.add(this.p1);

		return tab;
	}

	private JPanel _constructDescriptionTab() {
		JPanel tab = new JPanel();
		JPanel p1;

		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Process Description
		p1 = new JPanel();
		p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Description "));
		p1.add(this.generaldescriptionScrollPane);
		// p1.setVisible(false) ;
		tab.add(p1);

		// URL
		// Process Body
		p1 = new JPanel();
		p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " URL "));
		this.resourceButton = new JButton("URL");
		this.resourceButton.addActionListener(new resourceListner());
		p1.add(this.urlScrollPane);
		p1.add(this.resourceButton);
		tab.add(p1);

		// // Icon
		// this.p1 = new JPanel();
		// this.iconButton = new JButton("Icon");
		// this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
		// .createEtchedBorder(), " Icon "));
		// this.iconButton.addActionListener(new IconListner(myEntry, this));
		// this.p1.add(this.iconScrollPane);
		// this.p1.add(this.iconButton);
		// tab.add(this.p1);

		return tab;
	}

	// private JPanel _constructKeysAndIndecesTab() {
	// JPanel tab = new JPanel();
	// tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
	// tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	//
	// // pesist/key checkboxes
	// p1 = new JPanel();
	// p1.setBorder(
	// BorderFactory.createCompoundBorder(
	// BorderFactory.createEtchedBorder(),
	// BorderFactory.createEmptyBorder(4, 8, 4, 8)));
	// p1.setLayout(new FlowLayout(FlowLayout.CENTER));
	// p1.add(isPersistent);
	// p1.add(Box.createHorizontalStrut(40));
	// p1.add(isKey);
	//
	// tab.add(p1);
	//
	// // index name
	// p1 = new JPanel();
	// p1.setBorder(
	// BorderFactory.createCompoundBorder(
	// BorderFactory.createTitledBorder(
	// BorderFactory.createEtchedBorder(),
	// " Index Name "),
	// BorderFactory.createEmptyBorder(4, 8, 4, 8)));
	// p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
	// p1.add(indexName);
	// tab.add(p1);
	//
	// // index order
	// p1 = new JPanel();
	// p1.setBorder(
	// BorderFactory.createCompoundBorder(
	// BorderFactory.createTitledBorder(
	// BorderFactory.createEtchedBorder(),
	// " Index Order "),
	// BorderFactory.createEmptyBorder(4, 8, 4, 8)));
	// p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
	// p1.add(indexOrder);
	// tab.add(p1);
	//
	// //tab.add(Box.createVerticalStrut(160));
	//
	// //description
	// p1 = new JPanel();
	// p1.setBorder(
	// BorderFactory.createCompoundBorder(
	// BorderFactory.createTitledBorder(
	// BorderFactory.createEtchedBorder(),
	// " Description "),
	// BorderFactory.createEmptyBorder(4, 8, 4, 8)));
	// p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
	// JScrollPane sp =
	// new JScrollPane(
	// description,
	// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	// JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	// p1.add(sp);
	// tab.add(p1);
	// tab.add(Box.createVerticalStrut(30));
	//
	// return tab;
	// }

	private JPanel _constructStatesTab() {
		this.statesPanel = new StatePanel(this.opdObject, this.myProject);
		this.statesPanel.add(Box.createVerticalStrut(45));
		return this.statesPanel;
	}

	private JPanel _constructDummyStatesTab() {
		JPanel p = new JPanel();
		// p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		JLabel l = new JLabel(
				"You have to confirm the Object addition before adding States.",
				gui.images.misc.MiscImages.LOGO_BIG_ICON, SwingConstants.CENTER);
		p.add(Box.createVerticalStrut(320));
		p.add(l);
		return p;
	}

	private JPanel _constructResourceTab(JComboBox dropdown) {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));

		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(0, 2));
		p2.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Measurement Unit & Measurement values "),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		HashMap<Entry, FundamentalRelationEntry> sons = myEntry
				.getFundemantulRelationSons(OpcatConstants.EXHIBITION_RELATION);
		Object[] entries = sons.keySet().toArray();

		if (dropdown == null) {
			measuermentUnitdrop = new JComboBox();
			for (int i = 0; i < entries.length; i++) {
				if ((entries[i] instanceof ObjectEntry)
						&& !(((Entry) entries[i]).getName().trim()
								.equalsIgnoreCase("Measurement Unit"))) {
					measuermentUnitdrop.addItem(entries[i]);
				}
			}
		}

		p2.add(Box.createVerticalGlue());
		p2.add(Box.createVerticalGlue());
		p2.add(new JLabel("Value Name:"));
		p2.add(measuermentUnitdrop);
		p2.add(Box.createVerticalStrut(10));
		p2.add(Box.createVerticalStrut(10));
		p2.add(new JLabel("Measurement Unit :"));

		if (measuermentUnitdrop.getItemCount() > 0) {
			if ((mesUnit == null)
					|| (mesUnit.length != measuermentUnitdrop.getItemCount())) {
				mesUnit = new JTextField[measuermentUnitdrop.getItemCount()];
			}
			for (int i = 0; i < measuermentUnitdrop.getItemCount(); i++) {
				ObjectEntry obj = (ObjectEntry) measuermentUnitdrop
						.getItemAt(i);
				if (mesUnit[i] == null) {
					mesUnit[i] = new JTextField();
					mesUnit[i].setText(((OpmObject) obj.getLogicalEntity())
							.getMesurementUnit());
				}
			}
			p2.add(mesUnit[measuermentUnitdrop.getSelectedIndex()]);
		} else {
			p2.add(new JLabel("Empty"));
			measuermentUnitdrop.setEnabled(false);
		}

		p2.add(Box.createVerticalStrut(10));
		p2.add(Box.createVerticalStrut(10));
		p2.add(new JLabel("Initial Value :"));
		if (measuermentUnitdrop.getItemCount() > 0) {
			if ((mesUnitInitialValue == null)
					|| (mesUnitInitialValue.length != measuermentUnitdrop
							.getItemCount())) {
				mesUnitInitialValue = new JTextField[measuermentUnitdrop
						.getItemCount()];
			}
			for (int i = 0; i < measuermentUnitdrop.getItemCount(); i++) {
				ObjectEntry obj = (ObjectEntry) measuermentUnitdrop
						.getItemAt(i);
				if (mesUnitInitialValue[i] == null) {
					mesUnitInitialValue[i] = new JTextField();
					mesUnitInitialValue[i].setText(String
							.valueOf(((OpmObject) obj.getLogicalEntity())
									.getMesurementUnitInitialValue()));
				}
			}
			p2.add(mesUnitInitialValue[measuermentUnitdrop.getSelectedIndex()]);
		} else {
			p2.add(new JLabel("Empty"));
		}

		p2.add(new JLabel("Min Value :"));
		if (measuermentUnitdrop.getItemCount() > 0) {
			if ((mesUnitMinValue == null)
					|| (mesUnitMinValue.length != measuermentUnitdrop
							.getItemCount())) {
				mesUnitMinValue = new JTextField[measuermentUnitdrop
						.getItemCount()];
			}
			for (int i = 0; i < measuermentUnitdrop.getItemCount(); i++) {
				ObjectEntry obj = (ObjectEntry) measuermentUnitdrop
						.getItemAt(i);
				if (mesUnitMinValue[i] == null) {
					mesUnitMinValue[i] = new JTextField();
					mesUnitMinValue[i].setText(String.valueOf(((OpmObject) obj
							.getLogicalEntity()).getMesurementUnitMinValue()));
				}
			}
			p2.add(mesUnitMinValue[measuermentUnitdrop.getSelectedIndex()]);
		} else {
			p2.add(new JLabel("Empty"));
		}

		p2.add(new JLabel("Max Value :"));
		if (measuermentUnitdrop.getItemCount() > 0) {
			if ((mesUnitMaxValue == null)
					|| (mesUnitMaxValue.length != measuermentUnitdrop
							.getItemCount())) {
				mesUnitMaxValue = new JTextField[measuermentUnitdrop
						.getItemCount()];
			}
			for (int i = 0; i < measuermentUnitdrop.getItemCount(); i++) {
				ObjectEntry obj = (ObjectEntry) measuermentUnitdrop
						.getItemAt(i);
				if (mesUnitMaxValue[i] == null) {
					mesUnitMaxValue[i] = new JTextField();
					mesUnitMaxValue[i].setText(String.valueOf(((OpmObject) obj
							.getLogicalEntity()).getMesurementUnitMaxValue()));
				}
			}
			p2.add(mesUnitMaxValue[measuermentUnitdrop.getSelectedIndex()]);
		} else {
			p2.add(new JLabel("Empty"));
		}

		p2.add(Box.createVerticalGlue());
		p2.add(Box.createVerticalGlue());
		tab.add(p2);

		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(0, 2));
		p3.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), " "
						+ myEntry.getName() + " "), BorderFactory
				.createEmptyBorder(4, 8, 4, 8)));
		p3.add(new JLabel("Measurement Unit :"));
		p3.add(new JLabel(((OpmObject) myEntry.getLogicalEntity())
				.getMesurementUnit()));

		p3.add(new JLabel("Initial Value :"));
		p3.add(new JLabel(Double.toString(((OpmObject) myEntry
				.getLogicalEntity()).getMesurementUnitInitialValue())));

		p3.add(new JLabel("Min Value :"));
		p3.add(new JLabel(Double.toString(((OpmObject) myEntry
				.getLogicalEntity()).getMesurementUnitMinValue())));

		p3.add(new JLabel("Max Value :"));
		p3.add(new JLabel(Double.toString(((OpmObject) myEntry
				.getLogicalEntity()).getMesurementUnitMaxValue())));
		tab.add(p3);
		// tab.add(Box.createVerticalStrut(100));
		measuermentUnitdrop.removeItemListener(measurementUnitListnerA);
		measuermentUnitdrop.addItemListener(measurementUnitListnerA);
		return tab;
	}

	private JPanel _constructMiscTab() {
		JPanel retTab = new JPanel();
		retTab.setLayout(new BoxLayout(retTab, BoxLayout.Y_AXIS));

		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.X_AXIS));
		tab.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Colors "), BorderFactory
				.createEmptyBorder(4, 8, 4, 8)));
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
		// p1.add(Box.createVerticalStrut(112));
		this.bgColor.addActionListener(new BgColorButtonListener());
		this.textColor.addActionListener(new TextColorButtonListener());
		this.borderColor.addActionListener(new BorderColorButtonListener());
		this.p2.add(this.bgColor);
		this.p2.add(Box.createVerticalStrut(8));
		this.p2.add(this.textColor);
		this.p2.add(Box.createVerticalStrut(8));
		this.p2.add(this.borderColor);
		// p2.add(Box.createVerticalStrut(112));
		tab.add(this.p1);
		tab.add(this.p2);
		// tab.add(Box.createHorizontalStrut(140));
		retTab.add(tab);

		JPanel textPositionPanel = new JPanel();
		textPositionPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory
						.createEtchedBorder(), " Text Position "),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		textPositionPanel.setLayout(new BorderLayout());
		this.dp = new DirectionPanel(true, "try str", this.positionAction);
		this.dp.setSelection(this.opdObject.getTextPosition());
		textPositionPanel.add(this.dp, BorderLayout.CENTER);
		textPositionPanel.add(Box.createVerticalStrut(40), BorderLayout.NORTH);
		textPositionPanel.add(Box.createVerticalStrut(40), BorderLayout.SOUTH);

		JPanel previewPanel = new JPanel();
		previewPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Preview "), BorderFactory.createEmptyBorder(4, 8, 4,
				8)));
		ObjectEntry sampleEntry = new ObjectEntry(new OpmObject(0, "Sample"),
				this.myProject);
		previewPanel.setLayout(new BorderLayout());
		// previewPanel.setLayout(new BoxLayout(previewPanel,
		// BoxLayout.X_AXIS));
		this.sampleObject = new OpdObject(sampleEntry, this.myProject, 0, 0);
		this.sampleObject.setTextPosition(this.opdObject.getTextPosition());
		this.sampleObject.setBackgroundColor(this.opdObject
				.getBackgroundColor());
		this.sampleObject.setBorderColor(this.opdObject.getBorderColor());
		this.sampleObject.setTextColor(this.opdObject.getTextColor());
		if (this.opdObject.isDashed()) {
			this.sampleObject.setDashed(true);
		}
		if (this.opdObject.isShadow()) {
			this.sampleObject.setShadow(true);
		}

		previewPanel.add(this.sampleObject, BorderLayout.CENTER);
		previewPanel.add(Box.createVerticalStrut(40), BorderLayout.NORTH);
		previewPanel.add(Box.createVerticalStrut(40), BorderLayout.SOUTH);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		bottomPanel.add(textPositionPanel);
		bottomPanel.add(previewPanel);

		retTab.add(bottomPanel);

		// Icon
		this.p1 = new JPanel();
		this.p1.setLayout(new BorderLayout(15, 4));
		this.iconButton = new JButton("Icon");
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Icon "));
		this.iconButton.addActionListener(new IconListner(myEntry, this));
		this.p1.add(this.iconScrollPane, BorderLayout.LINE_START);
		this.p1.add(this.iconButton, BorderLayout.CENTER);
		this.p1.add(showIcon, BorderLayout.PAGE_END);
		retTab.add(this.p1);

		return retTab;
	}

	/**
	 * Construct a roles tab, using the RolesDialog class.
	 * 
	 * @return The roles JPanel class.
	 * @author Eran Toch
	 */
	// private JPanel _constructRolesTab() {
	// this.rolesTab = new RolesDialog(this.object, this.myProject);
	// return this.rolesTab;
	// }
	private boolean _updateObjectData() {
		OpmObject oldObject = new OpmObject(-1, "");
		oldObject.copyPropsFrom(this.object);

		// if (!myEntry.canBeGeneric() && !this.rolesTab.getRoles().isEmpty()
		// && this.environmental.isSelected()) {
		// JOptionPane
		// .showMessageDialog(
		// this,
		// "Can not change to Generic Object, \nObject have Role and is Environmental\nFirst delete the in-zoom or Unfold OPD",
		// "OPCAT II", JOptionPane.WARNING_MESSAGE);
		// return false;
		// }

		if ((!this.object.getType().equalsIgnoreCase(
				this.objTypePanel.getType()))
				&& this.object.isExposed()) {
			int ret = JOptionPane.showOptionDialog(this,
					"About to change Exposed object.\nAre you sure ?",
					"OPCAT II", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, null, null, null);
			if (ret != JOptionPane.YES_OPTION) {
				return false;
			}
			myProject.getExposeManager().treatExpsoeChange(
					this.opdObject.getInstance());

		}
		/**
		 * as this is an update it will not replace the interfacechange if it
		 * was entered
		 */
		if (this.object.isPublicExposed()) {
			myProject.getExposeManager().addPublicExposeChange(
					this.opdObject.getInstance(), OPCAT_EXPOSE_OP.UPDATE);
		}

		if (this.object.isPrivateExposed()) {
			myProject.getExposeManager().addPrivateExposeChange(
					this.opdObject.getInstance(), OPCAT_EXPOSE_OP.UPDATE);
		}

		// general
		if ((this.showTabs & BaseGraphicComponent.SHOW_1) != 0) {
			if (this.name.getText().equals(this.defaultName)
					|| this.name.getText().trim().equals("")) {
				this.tabs.setSelectedIndex(0);
				this.name.requestFocus();
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"You should provide a name for the Object",
						"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			// if (this.name.getText().trim().toLowerCase().endsWith("ing"))
			// {
			// String[] options = { "OK", "Edit Name" };
			// JOptionPane pane =
			// new JOptionPane(
			// "If the object name ends with \"ing\" the suffix
			// \"Object\"\n"
			// + "will be added to it in the OPL, so it will be called \""
			// + GraphicsUtils.capitalizeFirstLetters(
			// this.name.getText().trim())
			// + " Object\"",
			// JOptionPane.WARNING_MESSAGE,
			// JOptionPane.YES_NO_OPTION,
			// null,
			// options,
			// options[0]);
			//
			// pane.createDialog(Opcat2.getFrame(), "Opcat2 -
			// Warning").setVisible(true);
			//
			// if (!options[0].equals(pane.getValue())) {
			// this.tabs.setSelectedIndex(0);
			// this.name.requestFocus();
			// return false;
			// }
			// }

			try {
				Integer.decode(this.indexOrder.getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this,
						"Index Order is not an integer", "Opcat 2 - Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (!this.object.equals(this.name.getText())) {
				// object.setName(
				// GraphicsUtils.capitalizeFirstLetters(
				// name.getText().trim()));
				this.object.setName(this.name.getText().trim());
				this._updateObjectsTypes(this.name.getText());
			}

			if (!this.object.equals(this.url.getText())) {
				this.object.setUrl(this.url.getText().trim());
			}

			this.myEntry.setIcon(this.iconPath.getText().trim());

			if (!this.object.equals(this.generaldescription.getText())) {
				this.object.setDescription(this.generaldescription.getText()
						.trim());
			}

			this.object.setInitialValue(this.initialValue.getText());
			// object.setType(type.getText());
			this.object.setType(this.objTypePanel.getType());

			this.object.setTypeOriginId(this.objTypePanel.getTypeObjectId());
			this.object.setPhysical(this.physical.isSelected());
			this.object.setEnviromental(this.environmental.isSelected());
			this.object.setScope(String.valueOf(this.scope.getSelectedIndex()));
		}
		// end general

		// keys & indicies
		if ((this.showTabs & BaseGraphicComponent.SHOW_2) != 0) {
			this.object.setKey(this.isKey.isSelected());
			this.object.setPersistent(this.isPersistent.isSelected());
			this.object.setIndexName(this.indexName.getText());
			try {
				this.object.setIndexOrder((Integer.decode(this.indexOrder
						.getText())).intValue());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this,
						"Index Order is not an integer", "Opcat 2 - Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			/*
			 * TODO: this should be returned to keys description when key are
			 * returned
			 */
			// object.setDescription(description.getText());
		}
		// end keys & indicies

		// misc
		if ((this.showTabs & BaseGraphicComponent.SHOW_MISC) != 0) {
			if (measuermentUnitdrop.getItemCount() > 0) {
				for (int i = 0; i < measuermentUnitdrop.getItemCount(); i++) {
					ObjectEntry entry = (ObjectEntry) measuermentUnitdrop
							.getItemAt(i);
					double initial = 0;
					double min = 0;
					double max = 0;
					try {
						initial = Double
								.parseDouble(this.mesUnitInitialValue[i]
										.getText());
					} catch (NumberFormatException nfe) {
						JOptionPane
								.showMessageDialog(
										Opcat2.getFrame(),
										"Measurement Unit Initial value  must be a number",
										"Opcat2 - Error",
										JOptionPane.ERROR_MESSAGE);
						return false;
					}
					try {
						min = Double.parseDouble(this.mesUnitMinValue[i]
								.getText());
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(Opcat2.getFrame(),
								"Measurement Unit Min value  must be a number",
								"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					try {
						max = Double.parseDouble(this.mesUnitMaxValue[i]
								.getText());
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(Opcat2.getFrame(),
								"Measurement Unit value  must be a number",
								"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					if (((initial != 0) || (min != -999999999) || (max != 999999999))
							&& this.mesUnit[i].getText().trim()
									.equalsIgnoreCase("")) {
						JOptionPane
								.showMessageDialog(
										Opcat2.getFrame(),
										"Measurement Unit values set but Measurement Unit is empty",
										"Opcat2 - Error",
										JOptionPane.ERROR_MESSAGE);
						return false;
					}
					((OpmObject) entry.getLogicalEntity())
							.setMesurementUnitInitialValue(initial);
					((OpmObject) entry.getLogicalEntity())
							.setMesurementUnitMinValue(min);
					((OpmObject) entry.getLogicalEntity())
							.setMesurementUnitMaxValue(max);
					((OpmObject) entry.getLogicalEntity())
							.setMesurementUnit(mesUnit[i].getText());
				}
			}

			opdObject.setIconVisible(showIcon.isSelected());
			this.opdObject.setBackgroundColor(this.bgColor.getBackground());
			this.opdObject.setTextColor(this.textColor.getBackground());
			this.opdObject.setBorderColor(this.borderColor.getBackground());

		}
		// end misc

		// states
		if ((this.showTabs & BaseGraphicComponent.SHOW_3) != 0) {
			this.opdObject.setStatesAutoarrange(this.statesPanel
					.isAutoarrange());
			this.opdObject.setStatesDrawingStyle(this.statesPanel
					.getStatesView());
			this.opdObject.setTextPosition(this.dp.getSelection());

			DefaultListModel listModel = this.statesPanel.getListModel();
			StateListCell cell;
			for (Enumeration e = listModel.elements(); e.hasMoreElements();) {
				cell = (StateListCell) (e.nextElement());
				if (cell.isSelected() && !(cell.getOpdState().isVisible())) {
					cell.getOpdState().setVisible(true);
				}

				if (!(cell.isSelected()) && cell.getOpdState().isVisible()) {
					cell.getOpdState().setVisible(false);
				}
			}
		}
		// ens states

		if ((this.showTabs & BaseGraphicComponent.SHOW_4) != 0) {
			// this.object.setRole(this.rolesTab.getRoleText());
			// int noi = 0;
			// try {
			// // get the .. in the number
			//
			// int firstIndex = this.rolesTab.getNoOfInstances().indexOf("..");
			// int lastIndex = this.rolesTab.getNoOfInstances().lastIndexOf(
			// "..");
			//
			// if ((firstIndex < 0) || (firstIndex != lastIndex)) {
			// noi = Integer.parseInt(this.rolesTab.getNoOfInstances());
			// } else {
			// noi = Integer.parseInt(this.rolesTab.getNoOfInstances()
			// .substring(0, firstIndex));
			// noi = Integer.parseInt(this.rolesTab.getNoOfInstances()
			// .substring(firstIndex + 2));
			// }
			//
			// } catch (NumberFormatException nfe) {
			// JOptionPane
			// .showMessageDialog(
			// Opcat2.getFrame(),
			// "Number Of Instances must be a positive integer or in the form of min..max ",
			// "Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
			// return false;
			// }
			// if (noi < 1) {
			// JOptionPane.showMessageDialog(Opcat2.getFrame(),
			// "Number Of Instances must be a positive integer",
			// "Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
			// return false;
			// }
			// this.object.setNumberOfInstances(noi);
		}

		/** ******* Handling Roles ************* */
		// Handling roles selection, when the OK button is pressed.
		// by Eran Toc
		// changed to update only policies roles
		// this.object.getRolesManager().replacePoliciesRoles(
		// this.rolesTab.getRoles());
		this.myEntry.updateInstances();

		if (!this.isCreation && !this.object.hasSameProps(oldObject)) {
			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(this.myProject,
							new UndoableChangeObject(this.myProject,
									this.myEntry, oldObject, this.object)));
			Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
			Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());

			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		}

		myEntry.updateDefaultURL();
		return true;
	}

	private void _updateObjectsTypes(String newName) {
		for (Enumeration e = this.myProject.getComponentsStructure()
				.getElements(); e.hasMoreElements();) {
			Entry currEntry = (Entry) e.nextElement();

			if (currEntry instanceof ObjectEntry) {
				OpmObject currObject = (OpmObject) currEntry.getLogicalEntity();
				if (currObject.getTypeOriginId() == this.object.getId()) {
					currObject.setType(newName);
					currEntry.updateInstances();
				}
			}
		}

		return;
	}

	public boolean showDialog() {
		this.setVisible(true);
		if (Opcat2.getCurrentProject() != null)
			Opcat2.getCurrentProject().setCanClose(false);
		return this.okPressed;
	}

	public void componentShown(ComponentEvent e) {
		this.name.requestFocus();
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
	}

	class BgColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					ObjectPropertiesDialog.this, "Choose Background Color",
					ObjectPropertiesDialog.this.bgColor.getBackground());
			if (newColor != null) {
				(ObjectPropertiesDialog.this).bgColor.setBackground(newColor);
				(ObjectPropertiesDialog.this).sampleObject
						.setBackgroundColor(newColor);
				(ObjectPropertiesDialog.this).sampleObject.repaint();
			}
		}
	}

	class TextColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					ObjectPropertiesDialog.this, "Choose Text Color",
					ObjectPropertiesDialog.this.textColor.getBackground());
			if (newColor != null) {
				(ObjectPropertiesDialog.this).textColor.setBackground(newColor);
				(ObjectPropertiesDialog.this).sampleObject
						.setTextColor(newColor);
				(ObjectPropertiesDialog.this).sampleObject.repaint();
			}

		}
	}

	class BorderColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					ObjectPropertiesDialog.this, "Choose Border Color",
					ObjectPropertiesDialog.this.borderColor.getBackground());
			if (newColor != null) {
				(ObjectPropertiesDialog.this).borderColor
						.setBackground(newColor);
				(ObjectPropertiesDialog.this).sampleObject
						.setBorderColor(newColor);
				(ObjectPropertiesDialog.this).sampleObject.repaint();
			}
		}
	}

	class OkListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((ObjectPropertiesDialog.this)._updateObjectData()) {
				ObjectPropertiesDialog.this.okPressed = true;
				(ObjectPropertiesDialog.this).dispose();
			}
			return;
		}
	}

	class ApplyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ObjectPropertiesDialog.this.okPressed = true;
			(ObjectPropertiesDialog.this)._updateObjectData();
		}
	}

	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ObjectPropertiesDialog.this.okPressed = false;
			(ObjectPropertiesDialog.this).dispose();
		}
	}

	Action positionAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public void actionPerformed(ActionEvent e) {
			if ((ObjectPropertiesDialog.this.showTabs & BaseGraphicComponent.SHOW_MISC) == 0) {
				return;
			}
			String position = (ObjectPropertiesDialog.this).dp.getSelection();
			(ObjectPropertiesDialog.this).sampleObject
					.setTextPosition(position);
			(ObjectPropertiesDialog.this).sampleObject
					.setDashed((ObjectPropertiesDialog.this).environmental
							.isSelected());
			(ObjectPropertiesDialog.this).sampleObject
					.setShadow((ObjectPropertiesDialog.this).physical
							.isSelected());
			ObjectPropertiesDialog.this.addHelpON = ObjectPropertiesDialog.this.additionHelpON
					.isSelected();
			(ObjectPropertiesDialog.this).sampleObject.repaint();

		}
	};

	class resourceListner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			LibraryLocationDialog addLocation = new LibraryLocationDialog(
					Opcat2.getFrame());
			addLocation.setTitle("URL");
			addLocation.setFileChooserTitle("Choose File");
			addLocation.setResourceLabel("URL Or File Location :");
			HashMap newRef = addLocation.showDialog();
			if (newRef != null) {
				String path = (String) newRef.get("path");
				int type = ((Integer) newRef.get("type")).intValue();
				if (type == DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE) {
					ObjectPropertiesDialog.this.url.setText("file:///"
							.concat(path));
				} else {
					ObjectPropertiesDialog.this.url.setText(path);
				}
			}
			return;
		}
	}

	class measurementUnitListner implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			tabs.removeTabAt(resourceTabIndex);
			tabs.add("Resources", _constructResourceTab(measuermentUnitdrop));
			tabs.setSelectedIndex(resourceTabIndex);
		}

	}

	public boolean isAddHelpON() {
		return this.addHelpON;
	}

	class iconListner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser myFileChooser = new JFileChooser();
			if (myEntry.isIconSet()) {
				myFileChooser.setSelectedFile(new File(myEntry.getIconPath()));

			} else {
				myFileChooser.setSelectedFile(new File(""));
			}

			myFileChooser.resetChoosableFileFilters();
			myFileChooser.removeChoosableFileFilter(myFileChooser
					.getAcceptAllFileFilter());

			CustomFileFilter myFilter;

			String[] exts = { "jpeg", "jpg", "png", "bmp" };
			myFilter = new CustomFileFilter(exts, "Image Files");
			myFileChooser.addChoosableFileFilter(myFilter);

			int retVal = myFileChooser.showDialog(Opcat2.getFrame(), "Load");

			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			} else {
				ObjectPropertiesDialog.this.iconPath.setText(myFileChooser
						.getSelectedFile().getPath());
			}
			return;
		}
	}

	@Override
	public JTextArea getIconPath() {
		return iconPath;
	}
}
