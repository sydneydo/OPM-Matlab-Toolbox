package gui.opdGraphics.dialogs;

import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import gui.Opcat2;
import gui.dataProject.DataCreatorType;
import gui.metaLibraries.dialogs.LibraryLocationDialog;
import gui.metaLibraries.dialogs.RolesDialog;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdProcess;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmProcess;
import gui.projectStructure.ProcessEntry;
import gui.undo.UndoableChangeProcess;
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
import java.io.File;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.UndoableEditEvent;

/**
 * Importing ontologies package.
 * @author Eran Toch
 */

/**
 * This class is a properties dialog box for <a href = "OpdProcess.html">
 * <code>OpdProcess</code></a>.
 * <p>
 * Constructs and shows properties dialog box for one of the OPD Processes
 * 
 * <br>
 * Shown when <code>callPropertiesDialog()</code> is called.
 */
public class ProcessPropertiesDialog extends JDialog implements OpcatDialog,
		ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private JCheckBox showIcon = null;

	private JTextArea name, url, iconPath;

	private JTextArea description, body;

	private TimeSpecifier maxTimeActivation, minTimeActivation;

	private JRadioButton physical, informational, environmental, system,
			additionHelpOff, additionHelpON;

	private JComboBox scope;

	private JButton okButton, cancelButton, applyButton, bgColor, textColor,
			borderColor, resourceButton, iconButton;

	private ButtonGroup piBg, esBg, addBg;

	private JScrollPane bodyScrollPane, descriptionScrollPane, nameScrollPane,
			urlScrollPane, iconScrollPane;

	private JPanel p1, p2; // tmp panels

	private OpmProcess process;

	private OpdProcess opdProcess;

	private OpdProject myProject;

	private ProcessEntry myEntry;

	private DirectionPanel dp;

	private OpdProcess sampleProcess;

	private String defaultName = "";

	private String defaultUrl = "";

	private int showTabs;

	private JTabbedPane tabs;

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
	 * @param <code>OpdProcess pProcess</code> -- the OPD object to show
	 *        properties dialog for.
	 * @param <code>ThingEntry pEntry</code> -- the
	 *        <code>projectStructure.ThingEntry</code> class, entry of this OPD
	 *        Process in project dynamic structure.
	 * @see <a href = "ThingEntry.html"><code>ThingEntry</code></a> class
	 *      documentation for more information.
	 */
	public ProcessPropertiesDialog(OpdProcess pProcess, ProcessEntry pEntry,
			OpdProject project, boolean isCreation) {
		super(Opcat2.getFrame(), "OPD Process Properties", true);

		this.addComponentListener(this);
		this.myEntry = pEntry;
		this.opdProcess = pProcess;
		this.process = (OpmProcess) (this.myEntry.getLogicalEntity());
		this.myProject = project;

		this.isCreation = isCreation;
		int showButtons;
		this.showTabs = BaseGraphicComponent.SHOW_ALL_TABS;

		if (!isCreation) {
			showButtons = BaseGraphicComponent.SHOW_ALL_BUTTONS;
		} else {
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

		if ((this.showTabs & BaseGraphicComponent.SHOW_2) != 0) {
			this.tabs.add("Details", this._constructPreferences1Tab());
		}

		if ((this.showTabs & BaseGraphicComponent.SHOW_3) != 0) {
			this.tabs.add("Activation Time", this._constructTimingTab());
		}
		// Adding roles tab to the general tabs
		if ((this.showTabs & BaseGraphicComponent.SHOW_4) != 0) {
			// JPanel role = this._constructRolesTab();
			// this.tabs.add("Roles", role);
		}

		if ((this.showTabs & BaseGraphicComponent.SHOW_MISC) != 0) {
			this.tabs.add("Misc.", this._constructMiscTab());
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
		this.resourceButton.addActionListener(new resourceListner());
		this.iconButton.addActionListener(new IconListner(myEntry, this));

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
					(ProcessPropertiesDialog.this).dispose();
				}
				if ((e.getKeyCode() == KeyEvent.VK_ENTER)
						&& !ProcessPropertiesDialog.this.body.hasFocus()
						&& !ProcessPropertiesDialog.this.description.hasFocus()) {
					if ((ProcessPropertiesDialog.this)._updateProcessData()) {
						(ProcessPropertiesDialog.this).dispose();
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
			boolean env = myEntry.getLogicalEntity().isEnviromental();
			boolean hasRoles = (myEntry.getLogicalEntity().getRolesManager()
					.getLoadedPoliciesRolesVector().size() > 0);
			boolean ext = (env && hasRoles);

			// genaral
			if ((this.showTabs & BaseGraphicComponent.SHOW_1) != 0) {
				if (this.process.getName().trim().equals("")) {
					this.name = new JTextArea(this.defaultName, 3, 32);
				} else {
					this.name = new JTextArea(this.process.getName(), 3, 32);
				}
				this.name.setLineWrap(true);
				this.name.setWrapStyleWord(true);
				name.setEnabled(!ext);
				this.nameScrollPane = new JScrollPane(this.name,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

				/*
				 * TODO: this is ugly. the defurl and defname should be set in
				 * the thing object load and create. tis should be done to all
				 * those kind of properties.
				 */

				// set the url to be presented
				if (this.process.getUrl().trim().equals("")) {
					this.url = new JTextArea(this.defaultUrl, 4, 24);
				} else {
					this.url = new JTextArea(this.process.getUrl(), 4, 24);
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
						"Show Icon for this appearance ?", opdProcess
								.isIconVisible());

				this.scope = new JComboBox();
				this.scope.addItem("Public");
				this.scope.addItem("Protected");
				this.scope.addItem("Private");
				this.scope.setSelectedIndex(Character.digit((this.process
						.getScope()).charAt(0), 3));

				this.physical = new JRadioButton("Physical");
				this.physical.addActionListener(this.positionAction);
				this.informational = new JRadioButton("Informational");
				this.informational.addActionListener(this.positionAction);
				this.piBg = new ButtonGroup();
				this.piBg.add(this.physical);
				this.piBg.add(this.informational);
				if (this.process.isPhysical()) {
					this.physical.setSelected(true);
				} else {
					this.informational.setSelected(true);
				}

				this.environmental = new JRadioButton("Environmental");
				this.environmental.addActionListener(this.positionAction);
				this.system = new JRadioButton("Systemic");
				this.system.addActionListener(this.positionAction);

				this.additionHelpON = new JRadioButton("Enable");
				this.additionHelpON.addActionListener(this.positionAction);
				this.additionHelpOff = new JRadioButton("Disable");
				this.additionHelpOff.addActionListener(this.positionAction);
				this.addBg = new ButtonGroup();
				this.addBg.add(this.additionHelpOff);
				this.addBg.add(this.additionHelpON);
				this.additionHelpON.setSelected(true);
				this.addHelpON = true;

				this.esBg = new ButtonGroup();
				this.esBg.add(this.environmental);
				this.esBg.add(this.system);
				if (this.process.isEnviromental()) {
					this.environmental.setSelected(true);
				} else {
					this.system.setSelected(true);
				}
			}
			// end general

			// desc & body
			if ((this.showTabs & BaseGraphicComponent.SHOW_2) != 0) {
				this.body = new JTextArea(this.process.getProcessBody(), 10, 30);
				this.bodyScrollPane = new JScrollPane(this.body,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				this.body.setLineWrap(true);
				this.body.setWrapStyleWord(true);

				this.description = new JTextArea(this.process.getDescription(),
						4, 30);
				this.descriptionScrollPane = new JScrollPane(this.description,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				this.description.setLineWrap(true);
				this.description.setWrapStyleWord(true);

				this.url = new JTextArea(this.process.getUrl(), 4, 24);
				this.urlScrollPane = new JScrollPane(this.url,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				this.url.setLineWrap(true);
				this.url.setWrapStyleWord(true);

				this.iconPath = new JTextArea(myEntry.getIconPath(), 2, 24);
				this.iconScrollPane = new JScrollPane(this.iconPath,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				this.iconPath.setLineWrap(true);
				this.iconPath.setWrapStyleWord(true);
				this.showIcon = new JCheckBox(
						"Show Icon for this appearance ?", opdProcess
								.isIconVisible());

			}
			// end desc & body

			// activation time
			if ((this.showTabs & BaseGraphicComponent.SHOW_3) != 0) {
				this.maxTimeActivation = new TimeSpecifier(
						SwingConstants.HORIZONTAL, SwingConstants.TOP, 3, 3);
				this.maxTimeActivation.setTime(this.process
						.getMaxTimeActivation());
				this.minTimeActivation = new TimeSpecifier(
						SwingConstants.HORIZONTAL, SwingConstants.TOP, 3, 3);
				this.minTimeActivation.setTime(this.process
						.getMinTimeActivation());
			}
			// end activation time

		} catch (Exception e) {
			System.out.println("Error during process processing");
		}

		// misc
		if ((this.showTabs & BaseGraphicComponent.SHOW_MISC) != 0) {
			this.bgColor = new JButton("          ");
			this.bgColor.setBackground(this.opdProcess.getBackgroundColor());
			this.textColor = new JButton("          ");
			this.textColor.setBackground(this.opdProcess.getTextColor());
			this.borderColor = new JButton("          ");
			this.borderColor.setBackground(this.opdProcess.getBorderColor());
		}
		// end misc
	}

	private JPanel _constructGeneralTab() {

		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Process Name
		this.p1 = new JPanel();
		// p1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
		// BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Process Name"));
		// p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		// JLabel l1 = new JLabel("Process Name:");
		// p1.add(l1);
		// p1.add(Box.createHorizontalStrut(10));
		this.p1.add(this.nameScrollPane);
		this.p2 = new JPanel();
		this.p2.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.X_AXIS));
		this.p2.add(this.p1);
		tab.add(this.p2);

		// Radio buttons panel group
		this.p1 = new JPanel();
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));

		// Essence group
		this.p2 = new JPanel();
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
		this.p2.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Essence "));
		this.p2.add(this.physical);
		this.p2.add(Box.createVerticalStrut(10));
		this.p2.add(this.informational);
		this.p1.add(this.p2);

		this.p1.add(Box.createHorizontalStrut(10));

		// Affilation group
		this.p2 = new JPanel();
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
		this.p2.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Affilation "));
		this.p2.add(this.environmental);
		this.p2.add(Box.createVerticalStrut(10));
		this.p2.add(this.system);
		this.p1.add(this.p2);

		tab.add(Box.createVerticalStrut(8));
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

		tab.add(Box.createVerticalStrut(10));
		tab.add(this.p2);

		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "   Addition Helper   "));
		this.p1.setLayout(new GridLayout(2, 1));
		this.p1.add(this.additionHelpON);
		this.p1.add(this.additionHelpOff);

		tab.add(Box.createVerticalStrut(10));
		tab.add(this.p1);

		// tab.add(Box.createVerticalStrut(40));
		return tab;
	}

	private JPanel _constructPreferences1Tab() {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Process Description
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Description "));
		this.p1.add(this.descriptionScrollPane);
		tab.add(this.p1);

		// URL
		this.p1 = new JPanel();
		this.resourceButton = new JButton("URL");
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " URL "));
		this.p1.add(this.urlScrollPane);
		this.p1.add(this.resourceButton);
		tab.add(this.p1);

		// // Icon
		// this.p1 = new JPanel();
		// this.iconButton = new JButton("Icon");
		// this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
		// .createEtchedBorder(), " Icon "));
		// this.p1.add(this.iconScrollPane);
		// this.p1.add(this.iconButton);
		// tab.add(this.p1);

		// Process Body
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Body "));
		this.p1.add(this.bodyScrollPane);
		tab.add(this.p1);

		// tab.add(Box.createVerticalStrut(10));

		return tab;

	}

	private JPanel _constructTimingTab() {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		this.minTimeActivation.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Minimum Activation Time "));
		this.maxTimeActivation.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Maximum Activation Time "));
		tab.add(this.minTimeActivation);
		tab.add(Box.createVerticalStrut(5));
		tab.add(this.maxTimeActivation);
		tab.add(Box.createVerticalStrut(50));
		return tab;
	}

	/**
	 * Construct a roles tab, using the RolesDialog class.
	 * 
	 * @return The roles JPanel class.
	 * @author Eran Toch
	 */
	// private JPanel _constructRolesTab() {
	// this.rolesTab = new RolesDialog(this.process, this.myProject);
	// return this.rolesTab;
	// }
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
		this.dp.setSelection(this.opdProcess.getTextPosition());
		textPositionPanel.add(this.dp, BorderLayout.CENTER);

		JPanel previewPanel = new JPanel();
		previewPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(),
						" Preview "), BorderFactory.createEmptyBorder(4, 8, 4,
				8)));
		ProcessEntry sampleEntry = new ProcessEntry(
				new OpmProcess(0, "Sample"), this.myProject);
		previewPanel.setLayout(new BorderLayout());
		this.sampleProcess = new OpdProcess(sampleEntry, this.myProject, 0, 0);
		this.sampleProcess.setTextPosition(this.opdProcess.getTextPosition());
		this.sampleProcess.setBackgroundColor(this.opdProcess
				.getBackgroundColor());
		this.sampleProcess.setBorderColor(this.opdProcess.getBorderColor());
		this.sampleProcess.setTextColor(this.opdProcess.getTextColor());
		this.sampleProcess.setDashed(this.opdProcess.isDashed());
		this.sampleProcess.setShadow(this.opdProcess.isShadow());

		previewPanel.add(this.sampleProcess, BorderLayout.CENTER);

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

	private boolean _updateProcessData() {
		OpmProcess oldProcess = new OpmProcess(-1, "");
		oldProcess.copyPropsFrom(this.process);

		// if (!myEntry.canBeGeneric() && !this.rolesTab.getRoles().isEmpty()
		// && this.environmental.isSelected()) {
		// JOptionPane
		// .showMessageDialog(
		// this,
		// "Can not change to Generic Process, \nProcess have Role and is Environmental\nFirst delete the zoom-in OPD",
		// "OPCAT II", JOptionPane.WARNING_MESSAGE);
		// return false;
		// }

		if (this.process.isPublicExposed()) {
			this.myProject.getExposeManager().addPublicExposeChange(
					this.opdProcess.getInstance(), OPCAT_EXPOSE_OP.UPDATE);
		}

		if (this.process.isPrivateExposed()) {
			myProject.getExposeManager().addPrivateExposeChange(
					this.opdProcess.getInstance(), OPCAT_EXPOSE_OP.UPDATE);
		}

		// general
		if ((this.showTabs & BaseGraphicComponent.SHOW_1) != 0) {
			if (this.name.getText().equals(this.defaultName)
					|| this.name.getText().trim().equals("")) {
				this.tabs.setSelectedIndex(0);
				this.name.requestFocus();
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"You should provide a name for the Process",
						"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (GraphicsUtils.getMsec4TimeString(this.minTimeActivation
					.getTime()) > GraphicsUtils
					.getMsec4TimeString(this.maxTimeActivation.getTime())) {
				this.tabs.setSelectedIndex(2);
				JOptionPane
						.showMessageDialog(
								Opcat2.getFrame(),
								"Min activation time can't be larger than max activation time ",
								"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (Opcat2.getShowProcessNameMessage()) {
				// if (!name.getText().equals(process.getName()) &&
				// !name.getText().trim().toLowerCase().endsWith("ing") &&
				// !name.getText().trim().toLowerCase().endsWith("process"))
				// {
				// ProcessNameDialog doIng = new ProcessNameDialog(myProject,
				// name.getText());
				// int doWhat = doIng.showDialog();
				// if (doWhat == doIng.CANCEL) {
				// tabs.setSelectedIndex(0);
				// name.requestFocus();
				// return false;
				// }
				// else if (doWhat == doIng.OK_AND_DONT_ASK_AGAIN) {
				// Opcat2.setShowProcessNameMessage(false);
				// }
				// else if (doWhat == doIng.OK) {
				// Opcat2.setShowProcessNameMessage(true);
				// }
				// }
			}

			// process.setName(GraphicsUtils.capitalizeFirstLetters(name.getText().trim()));
			this.process.setName(this.name.getText().trim());
			this.process.setUrl(this.url.getText().trim());
			this.myEntry.setIcon(this.iconPath.getText().trim());
			this.process.setEnviromental(this.environmental.isSelected());
			this.process.setPhysical(this.physical.isSelected());
			this.process
					.setScope(String.valueOf(this.scope.getSelectedIndex()));

		}
		// end general

		// description & body
		if ((this.showTabs & BaseGraphicComponent.SHOW_2) != 0) {
			this.process.setDescription(this.description.getText());
			this.process.setProcessBody(this.body.getText());
		}
		// end description & body

		// activation time
		if ((this.showTabs & BaseGraphicComponent.SHOW_3) != 0) {
			this.process.setMinTimeActivation(this.minTimeActivation.getTime());
			this.process.setMaxTimeActivation(this.maxTimeActivation.getTime());
		}
		// end activation time

		// misc
		if ((this.showTabs & BaseGraphicComponent.SHOW_MISC) != 0) {
			opdProcess.setIconVisible(showIcon.isSelected());
			this.opdProcess.setBackgroundColor(this.bgColor.getBackground());
			this.opdProcess.setTextColor(this.textColor.getBackground());
			this.opdProcess.setBorderColor(this.borderColor.getBackground());
			this.opdProcess.setTextPosition(this.dp.getSelection());
		}
		// end misc

		if ((this.showTabs & BaseGraphicComponent.SHOW_4) != 0) {
			// this.process.setRole(this.rolesTab.getRoleText());
			// int noi = 0;
			// try {
			// noi = Integer.parseInt(this.rolesTab.getNoOfInstances());
			// } catch (NumberFormatException nfe) {
			// JOptionPane.showMessageDialog(Opcat2.getFrame(),
			// "Number Of Instances must be a positive integer",
			// "Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
			// return false;
			// }
			// if (noi < 1) {
			// JOptionPane.showMessageDialog(Opcat2.getFrame(),
			// "Number Of Instances must be a positive integer",
			// "Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
			// return false;
			// }
			// this.process.setNumberOfInstances(noi);
		}

		/** ******* Handling Roles ************* */
		// Handling roles selection, when the OK button is pressed.
		// by Eran Toch
		// this.process.getRolesManager().replacePoliciesRoles(
		// this.rolesTab.getRoles());
		// end roles
		this.myEntry.updateInstances();

		if (!this.isCreation && !this.process.hasSameProps(oldProcess)) {
			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(this.myProject,
							new UndoableChangeProcess(this.myProject,
									this.myEntry, oldProcess, this.process)));
			Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
			Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());

			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		}
		myEntry.updateDefaultURL();
		return true;
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

	// reuse
	public void commitLockForEdit() {
		this.body.setEditable(false);
		this.maxTimeActivation.setEnabled(false);
		this.maxTimeActivation.setFocusable(false);
		this.minTimeActivation.setEnabled(false);
		this.scope.setEditable(false);
		this.bodyScrollPane.setEnabled(false);
		this.descriptionScrollPane.setEnabled(false);
		this.nameScrollPane.setEnabled(false);
		this.name.setEditable(false);
		this.description.setEditable(false);
		this.physical.setEnabled(false);
		this.informational.setEnabled(false);
		this.environmental.setEnabled(false);
		this.system.setEnabled(false);
		this.scope.setEditable(false);
		this.scope.setEnabled(false);
		this.borderColor.setEnabled(false);

	}

	class BgColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					ProcessPropertiesDialog.this, "Choose Background Color",
					ProcessPropertiesDialog.this.bgColor.getBackground());
			if (newColor != null) {
				(ProcessPropertiesDialog.this).bgColor.setBackground(newColor);
				(ProcessPropertiesDialog.this).sampleProcess
						.setBackgroundColor(newColor);
				(ProcessPropertiesDialog.this).sampleProcess.repaint();
			}

		}
	}

	class TextColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					ProcessPropertiesDialog.this, "Choose Text Color",
					ProcessPropertiesDialog.this.textColor.getBackground());
			if (newColor != null) {
				(ProcessPropertiesDialog.this).textColor
						.setBackground(newColor);
				(ProcessPropertiesDialog.this).sampleProcess
						.setTextColor(newColor);
				(ProcessPropertiesDialog.this).sampleProcess.repaint();
			}

		}
	}

	class BorderColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					ProcessPropertiesDialog.this, "Choose Border Color",
					ProcessPropertiesDialog.this.borderColor.getBackground());
			if (newColor != null) {
				(ProcessPropertiesDialog.this).borderColor
						.setBackground(newColor);
				(ProcessPropertiesDialog.this).sampleProcess
						.setBorderColor(newColor);
				(ProcessPropertiesDialog.this).sampleProcess.repaint();
			}
		}
	}

	class resourceListner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser myFileChooser = new JFileChooser();
			if (myEntry.isIconSet()) {
				myFileChooser.setSelectedFile(new File(myEntry.getIconPath()));
			} else {
				myFileChooser.setSelectedFile(new File(""));
			}

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
					ProcessPropertiesDialog.this.url.setText("file:///"
							.concat(path));
				} else {
					ProcessPropertiesDialog.this.url.setText(path);
				}
			}
			return;
		}
	}

	class OkListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((ProcessPropertiesDialog.this)._updateProcessData()) {
				ProcessPropertiesDialog.this.okPressed = true;
				(ProcessPropertiesDialog.this).dispose();
			}
			return;
		}
	}

	class ApplyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ProcessPropertiesDialog.this.okPressed = true;
			(ProcessPropertiesDialog.this)._updateProcessData();
		}
	}

	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ProcessPropertiesDialog.this.okPressed = false;
			(ProcessPropertiesDialog.this).dispose();
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
			if ((ProcessPropertiesDialog.this.showTabs & BaseGraphicComponent.SHOW_MISC) == 0) {
				return;
			}
			String position = (ProcessPropertiesDialog.this).dp.getSelection();
			(ProcessPropertiesDialog.this).sampleProcess
					.setTextPosition(position);
			(ProcessPropertiesDialog.this).sampleProcess
					.setDashed((ProcessPropertiesDialog.this).environmental
							.isSelected());
			(ProcessPropertiesDialog.this).sampleProcess
					.setShadow((ProcessPropertiesDialog.this).physical
							.isSelected());
			ProcessPropertiesDialog.this.addHelpON = ProcessPropertiesDialog.this.additionHelpON
					.isSelected();
			(ProcessPropertiesDialog.this).sampleProcess.repaint();
		}
	};

	public boolean isAddHelpON() {
		return this.addHelpON;
	}

	@Override
	public JTextArea getIconPath() {
		return iconPath;
	}

}