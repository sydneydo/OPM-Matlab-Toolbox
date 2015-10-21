package gui.metaLibraries.dialogs;

import gui.dataProject.DataAbstractItem;
import gui.metaLibraries.logic.MetaException;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.metaLibraries.logic.RolesManager;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmThing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * A dialog for handling roles from a {@link MetaLibrary} for an
 * <code>OpmThing</code>.
 * 
 * @author Eran Toch Created: 01/05/2004
 */
public class RolesDialog extends JPanel {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
         * 
         */

	private Vector<Role> selectedRoles;

	private Vector<Role> avaliableRoles;

	JPanel jPanel1 = new JPanel();

	Border border1;

	JButton addButton = new JButton();

	JButton removeButton = new JButton();

	JButton cancelButton = new JButton();

	JScrollPane selectedScroll = new JScrollPane();

	JScrollPane avaliableScroll = new JScrollPane();

	JList selectedList = new JList();

	JList avaliableList = new JList();

	JButton okButton = new JButton();

	private OpmThing theThing;

	private OpdProject myProject;

	private JTextField roleText, noOfInstancesText;

	private JPanel p1, p2; // tmp panels

	public RolesDialog(OpmThing theThing, OpdProject project)
			throws HeadlessException {
		try {
			this.theThing = theThing;
			this.myProject = project;
			this.build();
			this.initLists();
			this.addListeners();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void build() {
		// Handling buttons
		int buttonWidth = 60;
		// addButton.setSize(new Dimension(buttonWidth, 25));
		this.addButton.setAlignmentX(CENTER_ALIGNMENT);
		this.addButton.setHorizontalTextPosition(SwingConstants.TRAILING);
		this.addButton.setText(" >> ");
		// removeButton.setSize(new Dimension(buttonWidth, 25));
		this.removeButton.setAlignmentX(CENTER_ALIGNMENT);
		this.removeButton.setText(" << ");

		// Creating button pane
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
		buttons.add(Box.createRigidArea(new Dimension(buttonWidth, 80)));
		buttons.add(this.addButton, BorderLayout.PAGE_START);
		buttons.add(Box.createRigidArea(new Dimension(buttonWidth, 10)));
		buttons.add(this.removeButton, BorderLayout.PAGE_START);
		buttons.setAlignmentY(Component.CENTER_ALIGNMENT);

		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		// pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
		// " Roles Selection "), BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		this.avaliableScroll.setPreferredSize(new Dimension(160, 250));
		this.avaliableScroll.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory
						.createEtchedBorder(), " Avaliable Roles "),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		this.selectedScroll.setPreferredSize(new Dimension(160, 250));
		this.selectedScroll.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory
						.createEtchedBorder(), " Selected Roles "),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		pane.add(this.avaliableScroll, BorderLayout.WEST);
		pane.add(buttons, BorderLayout.CENTER);
		pane.add(this.selectedScroll, BorderLayout.EAST);
		JPanel bigPanel = new JPanel(new BorderLayout());
		bigPanel.add(pane, BorderLayout.CENTER);
		bigPanel.add(this._constructMasTab(), BorderLayout.SOUTH);

		// pane.setSize(new Dimension(250, 400));
		this.add(bigPanel, null);
		this.avaliableScroll.getViewport().add(this.avaliableList, null);
		this.selectedScroll.getViewport().add(this.selectedList, null);

		// add(cancelButton, null);
		// add(okButton, null);
		// jPanel1.add(selectedScroll, null);

		// this.add(jPanel1, null);
	}

	private JPanel _constructMasTab() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		JLabel roleLabel = new JLabel("Free Text Role: ");
		this.roleText = new JTextField();
		this.roleText.setText(this.theThing.getFreeTextRole());

		JLabel noOfInstancesLabel = new JLabel("Number of instances: ");
		this.noOfInstancesText = new JTextField(String.valueOf(this.theThing
				.getNumberOfInstances()));

		this.p1 = new JPanel();
		this.p1.setLayout(new BoxLayout(this.p1, BoxLayout.X_AXIS));
		this.p1.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		this.p2 = new JPanel();
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
		this.p2.add(roleLabel);
		this.p2.add(Box.createVerticalStrut(15));
		this.p2.add(noOfInstancesLabel);
		this.p1.add(this.p2);

		this.p1.add(Box.createHorizontalStrut(15));

		this.p2 = new JPanel();
		this.p2.setLayout(new BoxLayout(this.p2, BoxLayout.Y_AXIS));
		this.p2.add(this.roleText);
		this.p2.add(Box.createVerticalStrut(15));
		this.p2.add(this.noOfInstancesText);
		this.p1.add(this.p2);

		p.add(this.p1);
		p.add(Box.createVerticalStrut(10));
		return p;
	}

	private void initLists() {
		this.selectedRoles = new Vector<Role>();
		Vector<Role> tempRoles = new Vector<Role>();

		for (Role i : tempRoles) {
			if ((i.getOntology() != null) && !(i.getOntology().isHidden())) {
				this.selectedRoles.add(i);
			}
		}

		RolesListModel model = new RolesListModel(this.selectedRoles);
		this.selectedList.setModel(model);
		this.selectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		Vector<Role> allRoles = new Vector<Role>();
		Enumeration ontologies = this.myProject.getMetaManager()
				.getMetaLibraries(MetaLibrary.TYPE_POLICY);
		try {
			while (ontologies.hasMoreElements()) {
				MetaLibrary ont = (MetaLibrary) ontologies.nextElement();
				if (ont.isPolicy()) {
					allRoles.addAll(ont.getRolesCollection());
				}
			}
		} catch (MetaException E) {
			// JOptionPane.showMessageDialog(this, E.getMessage(),
			// "Message", JOptionPane.ERROR_MESSAGE);
			// OpcatLogger.logError(E);
		}

		this.avaliableRoles = new Vector<Role>();
		for (int i = 0; i < allRoles.size(); i++) {
			Role role = (Role) allRoles.get(i);
			DataAbstractItem thing = role.getThing();
			if (this.theThing instanceof OpmObject) {
				if (thing instanceof OpmObject) {
					this.avaliableRoles.add(role);
				}
			} else if (this.theThing instanceof OpmProcess) {
				if (thing instanceof OpmProcess) {
					this.avaliableRoles.add(role);
				}
			}
		}
		// avaliableRoles = Opcat2.getOntology().getRolesCollection();
		// Iterator it = avaliableRoles.iterator();

		// clean list
		this.avaliableList.setModel(new RolesListModel(this.avaliableRoles));
		this.selectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Enumeration locenum = this.avaliableRoles.elements();
		while (locenum.hasMoreElements()) {
			Role runner = (Role) locenum.nextElement();
			if (this.theThing.getRolesManager().contains(runner)) {
				this.avaliableRoles.remove(runner);
			}
		}

	}

	private void addListeners() {

		this.addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RolesDialog.this.addPressed();
			}
		});

		this.removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RolesDialog.this.removePressed();
			}
		});

		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RolesDialog.this.selectedRoles = ((RolesListModel) (RolesDialog.this.selectedList
						.getModel())).getRoles();
				RolesDialog.this.done();
			}
		});

		// cancel - close Dialog without doint anything
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RolesDialog.this.done();
			}
		});

	}

	private void done() {
		this.setVisible(false);
	}

	private void addPressed() {
		RolesListModel avaliableModel = (RolesListModel) (this.avaliableList
				.getModel());
		Role theRole = (Role) (this.avaliableList.getSelectedValue());
		if ((this.avaliableList.getSelectedValue() == null)
				|| (this.avaliableList.getSelectedIndex() == avaliableModel
						.getSize())) {
			JOptionPane.showMessageDialog(null, "Please select a role to add");
			return;
		}
		if (!((RolesListModel) (this.selectedList.getModel())).addRole(theRole)) {
			JOptionPane.showMessageDialog(null, "Role Already Selected");
		}
		RolesListModel selectedModel = (RolesListModel) (this.selectedList
				.getModel());
		avaliableModel.removeRole(theRole);
		selectedModel.addRole(theRole);
	}

	private void removePressed() {
		RolesListModel selectedModel = (RolesListModel) (this.selectedList
				.getModel());
		if ((this.selectedList.getSelectedValue() == null)
				|| (this.selectedList.getSelectedIndex() == selectedModel
						.getSize())) {
			JOptionPane.showMessageDialog(null,
					"Please select a role to remove");
			return;
		}
		Role theRole = (Role) (this.selectedList.getSelectedValue());
		RolesListModel avaliableModel = (RolesListModel) (this.avaliableList
				.getModel());
		avaliableModel.addRole(theRole);
		selectedModel.removeRole(theRole);
	}

	public Vector<Role> getRoles() {
		return this.selectedRoles;
	}

	public String getRoleText() {
		return this.roleText.getText();
	}

	public String getNoOfInstances() {
		return this.noOfInstancesText.getText();
	}

	public RolesDialog() {
		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setMinimumSize(new Dimension(200, 300));
	}
}
