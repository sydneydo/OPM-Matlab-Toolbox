package gui.opdGraphics.popups;

import gui.images.standard.StandardImages;
import gui.opdProject.OpdProject;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.Instance;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class GeneralRelationPopup extends DefaultPopup {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	GeneralRelationInstance myInstance;

	public GeneralRelationPopup(OpdProject prj, Instance inst) {
		super(prj, inst);
		this.myInstance = (GeneralRelationInstance) inst;// (GeneralRelationInstance)(myProject.getCurrentOpd().getSelectedItem());

		JMenuItem jmi = new JMenuItem(this.arrangeAction);
		if (this.myInstance.isAutoArranged()) {
			jmi.setIcon(StandardImages.CHECKED);
		} else {
			jmi.setIcon(StandardImages.UNCHECKED);
		}
		this.add(jmi);

		if (this.multiSelected) {
			this.add(this.copyAction);
			this.add(this.cutAction);
		}
		this.add(this.deleteAction);
		this.add(new JSeparator());
		JMenu lineMenu = new JMenu("Line Shape");
		lineMenu.setIcon(StandardImages.EMPTY);
		lineMenu.add(this.straightAction);
		this.orthogonalAction.setEnabled(false);
		lineMenu.add(this.orthogonalAction);
		this.add(lineMenu);
		this.add(new JSeparator());
		this.add(this.propertiesAction);
	}

	Action propertiesAction = new AbstractAction("Properties",
			StandardImages.PROPERTIES) {
		 

		/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

		/**
				 * 
				 */
				 

		public void actionPerformed(ActionEvent e) {
			(myInstance).getLine().callPropertiesDialog();
			GeneralRelationPopup.this.myProject.getCurrentOpd().getDrawingArea().repaint();
		}
	};

	Action arrangeAction = new AbstractAction("Auto Arrange") {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = -6444517176074108259L;

		public void actionPerformed(ActionEvent e) {
			GeneralRelationPopup.this.myInstance.setAutoArranged(!GeneralRelationPopup.this.myInstance.isAutoArranged());
		}
	};

	Action straightAction = new AbstractAction("Straight") {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 2167027092007242357L;

		public void actionPerformed(ActionEvent e) {
			GeneralRelationPopup.this.myInstance.getLine().makeStraight();
		}
	};

	Action orthogonalAction = new AbstractAction("Orthogonal") {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = -4558920174990796043L;

		public void actionPerformed(ActionEvent e) {
		}
	};
}