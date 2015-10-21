package extensionTools.validator.gui;

import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.ISystem;
import extensionTools.validator.ValidationException;
import extensionTools.validator.Validator;
import extensionTools.validator.algorithm.Offence;
import extensionTools.validator.algorithm.VAlgorithm;
import extensionTools.validator.recommender.PerformAction;
import gui.Opcat2;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.OpdProject;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.StateEntry;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class ValidatorPanel extends GridPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private Opcat2 o2;

	private JFrame parentFrame = null;

	public ValidatorPanel(ISystem opcatSystem, Opcat2 myOpcat2,
			VAlgorithm algorithm, ArrayList<String> columnNames, JFrame parent) {
		super(columnNames);

		getGrid().addMouseListener(new MouseListner(this));

		this.o2 = myOpcat2;
		this.parentFrame = parent;
		Vector warnings = null;
		// Validating the model
		try {
			warnings = algorithm.validate(opcatSystem);
		} catch (ValidationException vex) {
			JOptionPane.showMessageDialog(this.parentFrame,
					"An Error occured while validating the model",
					"Validation Error", JOptionPane.ERROR_MESSAGE);
		}
		/**
		 * add the data from warnings
		 */
		this.setEntryTag();
		this.setTabName(Validator.VALIDATION_TAG_NAME);
		for (int i = 0; i < warnings.size(); i++) {

			Object[] row = new Object[5];
			Offence offence = (Offence) ((Vector) warnings.get(i)).toArray()[1];

			MetaLibrary meta = Opcat2.getCurrentProject().getMetaManager()
					.getMetaLibrary(offence.getRole().getMetaLibID());

			OpdProject metaProject = (OpdProject) meta.getProjectHolder();

			Entry metaApplicant = metaProject.getSystemStructure().getEntry(
					offence.getLaw().getApplicant().getRole().getThingID());

			Entry metaEntry = metaProject.getSystemStructure().getEntry(
					offence.getRole().getThingID());

			row[0] = meta.getName();

			if (offence.getOriginalThing() instanceof IProcessEntry) {
				row[1] = "Process";
			} else {
				row[1] = "Object";
			}

			row[2] = offence.getOriginalThingName().replaceAll("\n", " ");

			if (metaApplicant instanceof StateEntry) {
				metaApplicant = ((StateEntry) metaApplicant).getParentObject();
			}

			if (!metaEntry.GetInZoomedIncludedEntries().contains(metaApplicant)) {
				row[3] = "Interface";
			} else {
				row[3] = "Internal";
			}

			row[4] = (((Vector) warnings.get(i)).toArray()[2]).toString()
					.replaceAll("\n", " ");

			Object[] rowTag = new Object[2];
			rowTag[0] = offence.getOriginalThing();
			rowTag[1] = offence;

			getGrid().addRow(row, rowTag);
		}

		JTable table = this.getGrid();
		TableColumn columnIcon = table.getColumnModel().getColumn(0);
		columnIcon.setMaxWidth(300);

		JButton revalidateButton = new JButton();
		revalidateButton.setText("Revalidate");
		getButtonPane().add(revalidateButton);

		JButton propertiesButton = new JButton();
		propertiesButton.setText("Properties");
		getButtonPane().add(propertiesButton);

		revalidateButton.addActionListener(new RevalidateAdapter(this));
		propertiesButton.addActionListener(new PropertiesAdapter(this));
	}

	/**
	 * Revaldiates the panel
	 * 
	 * @param e
	 */
	public void revalidate(ActionEvent e) {
		if (this.o2 != null) {
			this.o2.clearValidationWindow();
			this.o2.validate();
		}
	}

	/**
	 * Opens up properties window.
	 * 
	 * @param e
	 */
	public void properties(ActionEvent e) {
		ConfigurationWindow window = new ConfigurationWindow(
				"Validation Properties");
		window.showDialog();
	}

	private void rightClickEvent(GridPanel panel, MouseEvent e) {
		JPopupMenu metaDataPopupMenu = panel.getRMenu();

		if (panel.getGrid().getSelectedRow() >= 0) {

			Object[] tag = new Object[2];
			tag = panel.getGrid().GetTag(panel.getGrid().getSelectedRow());
			Offence offence = (Offence) tag[1];

			JMenuItem fixWarnings = new JMenuItem("Fix Warning");
			getRMenu().add(fixWarnings);
			fixWarnings.addActionListener(new actionFixWarning(offence));

			metaDataPopupMenu.add(fixWarnings);

		}
		metaDataPopupMenu.show(e.getComponent(), e.getX(), e.getY());

	}

	class MouseListner extends MouseAdapter {
		ValidatorPanel adaptee;

		public MouseListner(ValidatorPanel adaptee) {
			super();
			this.adaptee = adaptee;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				dblClickEvent(null);
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				rightClickEvent(adaptee, e);
			}
		}
	}

	class actionFixWarning implements java.awt.event.ActionListener {
		Offence offence;

		actionFixWarning(Offence offence) {
			this.offence = offence;
		}

		public void actionPerformed(ActionEvent e) {

			MetaLibrary meta = Opcat2.getCurrentProject().getMetaManager()
					.getMetaLibrary(offence.getRole().getMetaLibID());

			ConnectionEdgeInstance instance = (ConnectionEdgeInstance) Opcat2
					.getCurrentProject().getSystemStructure().getInstanceInOpd(
							Opcat2.getCurrentProject().getCurrentOpd(),
							offence.getOriginalThing().getId()).nextElement();

			PerformAction doAction = new PerformAction(offence, instance,
					Opcat2.getCurrentProject(), meta);

			doAction.actionPerformed(null);

			revalidate(null);

			// JOptionPane.showInputDialog(metaApplicant.getName());
		}
	}
}

class RevalidateAdapter implements java.awt.event.ActionListener {
	ValidatorPanel adaptee;

	RevalidateAdapter(ValidatorPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.revalidate(e);
	}
}

class PropertiesAdapter implements java.awt.event.ActionListener {
	ValidatorPanel adaptee;

	PropertiesAdapter(ValidatorPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.properties(e);
	}
}
