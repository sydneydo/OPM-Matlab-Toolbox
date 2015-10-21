package gui.opdGraphics.dialogs;

import gui.Opcat2;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdState;
import gui.opdProject.OpdProject;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;

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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * This class is a properties dialog box for <a href = "OpdState.html"><code>OpdState</code></a>.
 * <p>
 * Constructs and shows properties dialog box for one of the OPD States
 * 
 * <br>
 * Shown when <code>callPropertiesDialog()</code> is called.
 */

public class StatePropertiesDialog extends JDialog implements OpcatDialog,
		ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private JTextArea name;

	private JTextArea description, iconPath;

	private JScrollPane nameScrollPane, iconScrollPane;

	private JScrollPane descriptionScrollPane;

	private JCheckBox isInitial, isFinal, isDefault;

	private TimeSpecifier maxTime, minTime;

	private JButton okButton, cancelButton, applyButton, bgColor, textColor,
			borderColor, iconButton;

	private JPanel p1, p2; // tmp panels

	// private OpmState opmState;
	private StateEntry stateEntry;

	private OpdState opdState;

	private boolean okPressed = false;

	private StateInstance myInstance;

	private JTabbedPane tabs;

	/**
	 * Constructor:
	 * 
	 * @param <code>parent</code> -- parent frame, Opcat2 application window or
	 *            <code>null</code>
	 * @param <code>pState OpdState</code> -- the OPD State to show properties
	 *            dialog for.
	 */
	public StatePropertiesDialog(StateInstance instance, OpdProject project,
			boolean showTabs, int showButtons) {
		super(Opcat2.getFrame(), "State Properties", true);

		this.addComponentListener(this);

		this.myInstance = instance;
		this.opdState = (OpdState) this.myInstance.getConnectionEdge();

		this.stateEntry = (StateEntry) myInstance.getEntry();
		// myProject = project;
		// this.showTabs = showTabs;
		// this.showButtons = showButtons;

		// parentDialogBox = (StatePanel)parent;

		// init all variables
		this._initVariables();

		Container contPane = this.getContentPane();
		contPane.setLayout(new BoxLayout(contPane, BoxLayout.Y_AXIS));

		this.tabs = new JTabbedPane();

		this.tabs.add("General", this._constructGeneralTab());
		this.tabs.add("Preferences", this._constructPreferencesTab());
		this.tabs.add("Misc.", this._constructMiscTab());

		contPane.add(this.tabs);

		// -----------------------------------------------------------------
		// add buttons
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(1, 5);
		layout.setHgap(3);
		this.p1.setLayout(layout);
		this.p1.add(Box.createGlue());

		if ((showButtons & BaseGraphicComponent.SHOW_OK) != 0) {
			this.okButton = new JButton("OK");
			this.okButton.addActionListener(new OkListener());
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
			// general
			this.name = new JTextArea(this.stateEntry.getName(), 3, 30);
			this.nameScrollPane = new JScrollPane(this.name,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			this.name.setLineWrap(true);
			this.name.setWrapStyleWord(true);

			this.description = new JTextArea(this.stateEntry.getDescription(),
					5, 30);
			this.descriptionScrollPane = new JScrollPane(this.description,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			this.description.setLineWrap(true);
			this.description.setWrapStyleWord(true);

			// end general

			// 2 tab
			this.isInitial = new JCheckBox("Initial", this.stateEntry
					.isInitial());
			this.isFinal = new JCheckBox("Final", this.stateEntry.isFinal());
			this.isDefault = new JCheckBox("Default", this.stateEntry
					.isDefault());

			this.maxTime = new TimeSpecifier(SwingConstants.HORIZONTAL,
					SwingConstants.TOP, 3, 3);
			this.maxTime.setTime(this.stateEntry.getMaxTime());
			this.minTime = new TimeSpecifier(SwingConstants.HORIZONTAL,
					SwingConstants.TOP, 3, 3);
			this.minTime.setTime(this.stateEntry.getMinTime());

			// end 2 tab

		} catch (NullPointerException e) {
			System.out.println("No object passed");
		}

		// misc
		this.bgColor = new JButton("          ");
		this.bgColor.setBackground(this.opdState.getBackgroundColor());
		this.textColor = new JButton("          ");
		this.textColor.setBackground(this.opdState.getTextColor());
		this.borderColor = new JButton("          ");
		this.borderColor.setBackground(this.opdState.getBorderColor());

		// set icon
		this.iconPath = new JTextArea(myInstance.getEntry().getIconPath()
				.trim(), 2, 24);
		this.iconPath.setLineWrap(true);
		this.iconPath.setWrapStyleWord(true);
		this.iconScrollPane = new JScrollPane(this.iconPath,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// end misc

	}

	private JPanel _constructGeneralTab() {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Object Name
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "State Name"));
		// p1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
		// BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		// p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		// JLabel l1 = new JLabel("State Name:");
		// p1.add(l1);
		// p1.add(Box.createHorizontalStrut(10));
		this.p1.add(this.nameScrollPane);
		tab.add(this.p1);
		tab.add(Box.createVerticalStrut(5));

		// State Description
		this.p1 = new JPanel();
		this.p1.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Description "));
		this.p1.add(this.descriptionScrollPane);
		// tab.add(Box.createVerticalStrut(10));
		tab.add(this.p1);
		tab.add(Box.createVerticalStrut(12));

		this.p1 = new JPanel();
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));
		// p1.add(Box.createHorizontalStrut(12));
		this.p1.add(this.isInitial);
		this.p1.add(Box.createHorizontalStrut(40));
		this.p1.add(this.isFinal);
		this.p1.add(Box.createHorizontalStrut(40));
		this.p1.add(this.isDefault);

		tab.add(this.p1);

		return tab;
	}

	public JPanel _constructPreferencesTab() {
		JPanel tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		this.minTime.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Minimum Activation Time "));
		this.maxTime.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), " Maximum Activation Time "));
		tab.add(this.minTime);
		tab.add(Box.createVerticalStrut(5));
		tab.add(this.maxTime);
		tab.add(Box.createVerticalStrut(10));

		// initial/final checkboxes
		// tab.add(Box.createVerticalStrut(10));
		return tab;
	}

	private JPanel _constructMiscTab() {
		// JPanel tab = new JPanel();
		// tab.setLayout(new BoxLayout(tab, BoxLayout.X_AXIS));
		// tab.setBorder(BorderFactory.createCompoundBorder(BorderFactory
		// .createEmptyBorder(8, 8, 8, 8), BorderFactory
		// .createEtchedBorder()));
		// JLabel bgColorLabel = new JLabel("Background Color:");
		// JLabel textColorLabel = new JLabel("Text Color:");
		// JLabel borderColorLabel = new JLabel("Border Color:");
		// this.p1 = new JPanel();
		// this.p1.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		// this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.Y_AXIS));
		// this.p2 = new JPanel();
		// this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
		// this.p2.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		// this.p1.add(bgColorLabel);
		// this.p1.add(Box.createVerticalStrut(20));
		// this.p1.add(textColorLabel);
		// this.p1.add(Box.createVerticalStrut(20));
		// this.p1.add(borderColorLabel);
		// this.p1.add(Box.createVerticalStrut(112));
		// this.bgColor.addActionListener(new BgColorButtonListener());
		// this.textColor.addActionListener(new TextColorButtonListener());
		// this.borderColor.addActionListener(new BorderColorButtonListener());
		// this.p2.add(this.bgColor);
		// this.p2.add(Box.createVerticalStrut(8));
		// this.p2.add(this.textColor);
		// this.p2.add(Box.createVerticalStrut(8));
		// this.p2.add(this.borderColor);
		// this.p2.add(Box.createVerticalStrut(112));
		// tab.add(this.p1);
		// tab.add(this.p2);
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
		this.iconButton.addActionListener(new IconListner(
				myInstance.getEntry(), this));
		this.p1.add(this.iconScrollPane);
		this.p1.add(this.iconButton);
		tab.add(this.p1);

		tab.add(Box.createHorizontalStrut(140));
		return tab;
	}

	private boolean _updateStateData() {
		if (this.name.getText().trim().equals("")) {
			this.tabs.setSelectedIndex(0);
			this.name.requestFocus();
			JOptionPane.showMessageDialog(Opcat2.getFrame(),
					"You should provide a name for the State",
					"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (GraphicsUtils.getMsec4TimeString(this.minTime.getTime()) > GraphicsUtils
				.getMsec4TimeString(this.maxTime.getTime())) {
			this.tabs.setSelectedIndex(1);
			JOptionPane
					.showMessageDialog(
							Opcat2.getFrame(),
							"Min activation time can't be larger than max activation time ",
							"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		this.stateEntry.setName(this.name.getText());
		this.stateEntry.setDescription(this.description.getText());
		this.stateEntry.setInitial(this.isInitial.isSelected());
		this.stateEntry.setFinal(this.isFinal.isSelected());
		this.stateEntry.setDefault(this.isDefault.isSelected());
		this.stateEntry.setMinTime(this.minTime.getTime());
		this.stateEntry.setMaxTime(this.maxTime.getTime());

		this.opdState.setBackgroundColor(this.bgColor.getBackground());
		this.opdState.setTextColor(this.textColor.getBackground());
		this.opdState.setBorderColor(this.borderColor.getBackground());

		this.myInstance.getEntry().setIcon(this.iconPath.getText().trim());

		this.myInstance.getEntry().updateInstances();

		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		return true;
	}

	public boolean showDialog() {
		this.setVisible(true);
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
					StatePropertiesDialog.this, "Choose Background Color",
					StatePropertiesDialog.this.bgColor.getBackground());
			if (newColor != null) {
				(StatePropertiesDialog.this).bgColor.setBackground(newColor);
			}
		}
	}

	class TextColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					StatePropertiesDialog.this, "Choose Text Color",
					StatePropertiesDialog.this.textColor.getBackground());
			if (newColor != null) {
				(StatePropertiesDialog.this).textColor.setBackground(newColor);
			}
		}
	}

	class BorderColorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color newColor = JColorChooser.showDialog(
					StatePropertiesDialog.this, "Choose Border Color",
					StatePropertiesDialog.this.borderColor.getBackground());
			if (newColor != null) {
				(StatePropertiesDialog.this).borderColor
						.setBackground(newColor);
			}
		}
	}

	class OkListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((StatePropertiesDialog.this)._updateStateData()) {
				StatePropertiesDialog.this.okPressed = true;
				(StatePropertiesDialog.this).dispose();
			}
			if (Opcat2.getCurrentProject() != null)
				Opcat2.getCurrentProject().setCanClose(false);
			return;
		}
	}

	class ApplyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			StatePropertiesDialog.this.okPressed = true;
			(StatePropertiesDialog.this)._updateStateData();
		}
	}

	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			StatePropertiesDialog.this.okPressed = false;
			(StatePropertiesDialog.this).dispose();
		}
	}

	@Override
	public JTextArea getIconPath() {
		return iconPath;
	}

}
