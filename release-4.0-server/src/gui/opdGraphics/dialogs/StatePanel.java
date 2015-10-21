package gui.opdGraphics.dialogs;

import exportedAPI.OpdKey;
import exportedAPI.opcatAPIx.IXCheckResult;
import gui.Opcat2;
import gui.checkModule.CheckModule;
import gui.checkModule.CheckResult;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdObject;
import gui.opdGraphics.opdBaseComponents.OpdState;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmState;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;
import gui.undo.UndoableAddState;
import gui.undo.UndoableDeleteState;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.UndoableEditEvent;

class StatePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private JList statesList;
	DefaultListModel listModel;
	private JButton addButton, removeButton, propertiesButton;
	private OpdObject parentOpdObject;
	private JScrollPane scrollPane;
	private JComboBox viewBox;
	private JCheckBox autoarrange;
	private OpdProject myProject;

	public StatePanel(OpdObject o, OpdProject project) {
		super();
		this.parentOpdObject = o;
		this.myProject = project;
		this.listModel = new DefaultListModel();
		this.statesList = new JList(this.listModel);
		this.statesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.statesList.setCellRenderer(new CheckListCellRenderer());
		CheckListener lst = new CheckListener(this.statesList, project, this);
		this.statesList.addMouseListener(lst);
		this.statesList.addKeyListener(lst);
		// statesList.setSize(200, 100);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(15, 8, 8, 8));

		this.scrollPane = new JScrollPane(this.statesList);
		this.add(this.scrollPane);

		this.add(Box.createVerticalStrut(15));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 4, 3, 3));
		buttonPanel.add(Box.createGlue());
		this.addButton = new JButton("Add");
		this.addButton.addActionListener(new AddButtonListener());
		this.removeButton = new JButton("Remove");
		this.removeButton.addActionListener(new RemoveButtonListener());
		this.propertiesButton = new JButton("Props");
		this.propertiesButton.addActionListener(new PropertiesButtonListener());
		buttonPanel.add(this.addButton);
		buttonPanel.add(this.removeButton);
		buttonPanel.add(this.propertiesButton);
		this.add(buttonPanel);

		this.add(Box.createVerticalStrut(15));
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(new JLabel("   View:  "));
		p.add(Box.createHorizontalStrut(10));
		String[] views = { "None", "Implicit", "Explicit" };
		this.viewBox = new JComboBox(views);
		this.viewBox.setSelectedIndex(o.getStatesDrawingStyle());
		p.add(this.viewBox);
		p.add(Box.createHorizontalStrut(30));
		this.autoarrange = new JCheckBox("  Autoarrange");
		// if (o.isStatesAutoarrange())
		// {
		// this.autoarrange.setSelected(true);
		// }

		this.autoarrange.setSelected(o.isStatesAutoarrange());
		p.add(this.autoarrange);
		p.add(Box.createHorizontalStrut(40));
		this.add(p);
		this.add(Box.createVerticalStrut(75));
		this._createListModel();
	}

	private void _createListModel() {
		this.listModel.clear();
		Component[] comps = this.parentOpdObject.getComponents();

		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof OpdState) {
				OpdState tmpState = (OpdState) comps[i];
				this.listModel.addElement(new StateListCell(tmpState, tmpState
						.isVisible()));
			}
		}

	}

	public boolean isAutoarrange() {
		return this.autoarrange.isSelected();
	}

	public int getStatesView() {
		return this.viewBox.getSelectedIndex();
	}

	// Inner classes -- listeners
	class AddButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			OpmObject sampleObj = new OpmObject(0, "");
			ObjectEntry sampleObjEntry = new ObjectEntry(sampleObj,
					StatePanel.this.myProject);
			OpdObject sampleObjOpd = new OpdObject(sampleObjEntry,
					StatePanel.this.myProject, 0, 0);

			OpmState sampleOpmState = new OpmState(0, "");
			StateEntry sampleStateEntry = new StateEntry(sampleOpmState,
					sampleObj, StatePanel.this.myProject);
			StateInstance sampleSI = new StateInstance(sampleStateEntry,
					new OpdKey(0, 0), sampleObjOpd, StatePanel.this.myProject);

			StatePropertiesDialog sd = new StatePropertiesDialog(sampleSI,
					StatePanel.this.myProject, false,
					BaseGraphicComponent.SHOW_OK
							| BaseGraphicComponent.SHOW_CANCEL);

			if (sd.showDialog()) {
				StateEntry myEntry = StatePanel.this.myProject
						.addState(StatePanel.this.parentOpdObject);

				OpmState nState = (OpmState) myEntry.getLogicalEntity();
				nState.copyPropsFrom(sampleOpmState);

				/**
				 * make sure only one is initial or final ;
				 */
				myEntry.setInitial(myEntry.isInitial());
				myEntry.setFinal(myEntry.isFinal());
				myEntry.setDefault(myEntry.isDefault());

				ObjectEntry parentEntry = (ObjectEntry) StatePanel.this.myProject
						.getComponentsStructure().getEntry(
								StatePanel.this.parentOpdObject.getEntity()
										.getId());

				for (Enumeration en = parentEntry.getInstances(); en
						.hasMoreElements();) {
					ObjectInstance parentInstance = (ObjectInstance) en
							.nextElement();
					OpdObject pObj = (OpdObject) parentInstance.getThing();

					pObj.repaint();
					if (!pObj.getInstance().getKey().toString()
							.equalsIgnoreCase(
									parentOpdObject.getInstance().getKey()
											.toString())) {

						StateInstance stateInstance = ((ObjectInstance) pObj
								.getInstance()).getState(nState.getName());
						stateInstance.getGraphicalRepresentation().setVisible(
								false);
						pObj.repaint();
					}

				}

				if (parentOpdObject.isStatesAutoarrange()) {
					parentOpdObject.fitToContent();
				}

				Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
				Opcat2.getUndoManager().undoableEditHappened(
						new UndoableEditEvent(StatePanel.this.myProject,
								new UndoableAddState(StatePanel.this.myProject,
										myEntry)));
				Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
				Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());

				StatePanel.this._createListModel();
				OpdState newState = null;
				StateInstance stateInst;
				for (Enumeration en = myEntry.getInstances(); en
						.hasMoreElements();) {
					stateInst = (StateInstance) en.nextElement();
					if (stateInst.getKey().getOpdId() == StatePanel.this.parentOpdObject
							.getOpdId()) {
						newState = stateInst.getState();
					}
				}
				if (newState == null) {
					JOptionPane
							.showMessageDialog(
									StatePanel.this,
									"Internal Error in\nStatePanel.AddButtonListener.actionPerformed\nPlease contact developers",
									"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}
	}

	class RemoveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = (StatePanel.this).statesList.getSelectedIndex();
			if (index == -1) {
				return;
			}

			OpdState opdState = (((StateListCell) (StatePanel.this).listModel
					.get(index)).getOpdState());
			MainStructure ms = StatePanel.this.myProject
					.getComponentsStructure();
			StateEntry se = (StateEntry) ms.getEntry(opdState.getEntity()
					.getId());
			StateInstance si = (StateInstance) (se.getInstance(new OpdKey(
					opdState.getOpdId(), opdState.getEntityInOpdId())));

			CheckResult cr = CheckModule.checkDeletion(si,
					StatePanel.this.myProject);
			if (cr.getResult() == IXCheckResult.WRONG) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(), cr
						.getMessage(), "Opcat2 - Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (cr.getResult() == IXCheckResult.WARNING) {
				int retValue = JOptionPane.showConfirmDialog(Opcat2.getFrame(),
						cr.getMessage() + "\n Do you want to continue?",
						"Opcat2 - Warning", JOptionPane.YES_NO_OPTION);

				if (retValue == JOptionPane.NO_OPTION) {
					return;
				}
			}

			UndoableDeleteState ud = new UndoableDeleteState(
					StatePanel.this.myProject, se);

			StatePanel.this.myProject.deleteState(si);

			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(StatePanel.this.myProject, ud));
			Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
			Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
			if (StatePanel.this.isAutoarrange()) {
				StatePanel.this.parentOpdObject.fitToContent();
			}

			(StatePanel.this).listModel.remove(index);
		}
	}

	class PropertiesButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = (StatePanel.this).statesList.getSelectedIndex();
			if (index != -1) {
				OpdState opdState = (((StateListCell) (StatePanel.this).listModel
						.get(index)).getOpdState());
				opdState.callPropertiesDialog(
						BaseGraphicComponent.SHOW_ALL_TABS,
						BaseGraphicComponent.SHOW_ALL_BUTTONS);
				StatePanel.this.statesList.repaint();
			}
		}
	}

	public DefaultListModel getListModel() {
		return this.listModel;
	}
}

class CheckListCellRenderer extends JCheckBox implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	protected Border m_noFocusBorder = new EmptyBorder(1, 3, 1, 1);

	public CheckListCellRenderer() {
		super();
		this.setOpaque(true);
		this.setBorder(this.m_noFocusBorder);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		this.setText(value.toString());
		this.setBackground(isSelected ? list.getSelectionBackground() : list
				.getBackground());
		this.setForeground(isSelected ? list.getSelectionForeground() : list
				.getForeground());
		StateListCell s = (StateListCell) value;
		this.setSelected(s.isSelected());
		this.setFont(list.getFont());
		this.setBorder((cellHasFocus) ? UIManager
				.getBorder("List.focusCellHighlightBorder")
				: this.m_noFocusBorder);
		return this;
	}
}

class CheckListener implements MouseListener, KeyListener {
	protected JList m_list;
	protected DefaultListModel lm;
	protected OpdProject myProject;
	protected StatePanel parentWin;

	public CheckListener(JList parent, OpdProject prj, StatePanel pWin) {
		this.m_list = parent;
		this.myProject = prj;
		this.parentWin = pWin;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getX() < 20) {
			this.doCheck();
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ') {
			this.doCheck();
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	protected void doCheck() {
		int index = this.m_list.getSelectedIndex();
		if (index < 0) {
			return;
		}
		StateListCell choosedState = (StateListCell) this.m_list.getModel()
				.getElementAt(index);
		if (choosedState.isSelected()) {
			OpdState opdState = (((StateListCell) (this.parentWin).listModel
					.get(index)).getOpdState());
			MainStructure ms = this.myProject.getComponentsStructure();
			StateEntry se = (StateEntry) ms.getEntry(opdState.getEntity()
					.getId());
			StateInstance si = (StateInstance) (se.getInstance(new OpdKey(
					opdState.getOpdId(), opdState.getEntityInOpdId())));
			if (si.isRelated()) {
				JOptionPane
						.showMessageDialog(
								this.parentWin,
								"You can't hide this state instance\nbecause it related to another thing.",
								"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		choosedState.invertSelected();
		this.m_list.repaint();
	}

}