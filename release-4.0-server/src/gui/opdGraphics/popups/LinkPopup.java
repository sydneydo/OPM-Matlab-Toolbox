package gui.opdGraphics.popups;

import gui.images.standard.StandardImages;
import gui.opdProject.OpdProject;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkInstance;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class LinkPopup extends DefaultPopup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	LinkInstance myInstance;

	public LinkPopup(OpdProject prj, Instance inst) {
		super(prj, inst);
		this.myInstance = (LinkInstance) inst;// (LinkInstance)(myProject.getCurrentOpd().getSelectedItem());

		JMenuItem jmi = new JMenuItem(this.arrangeAction);
		if (this.myInstance.isAutoArranged()) {
			jmi.setIcon(StandardImages.CHECKED);
		} else {
			jmi.setIcon(StandardImages.UNCHECKED);
		}

		this.add(jmi);
		// cutAction.setEnabled(false);
		// add(cutAction);
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
			(myInstance).getSourceDragger().callPropertiesDialog();
			LinkPopup.this.myProject.getCurrentOpd().getDrawingArea().repaint();
		}
	};

	Action arrangeAction = new AbstractAction("Auto Arrange") {

		/**
		 * 
		 */
		private static final long serialVersionUID = -110507180858758455L;

		public void actionPerformed(ActionEvent e) {
			LinkPopup.this.myInstance
					.setAutoArranged(!LinkPopup.this.myInstance
							.isAutoArranged());
		}
	};

	Action straightAction = new AbstractAction("Straight") {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3916362963012542758L;

		public void actionPerformed(ActionEvent e) {
			LinkPopup.this.myInstance.getLine().makeStraight();
		}
	};

	Action orthogonalAction = new AbstractAction("Orthogonal") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3550626806875649845L;

		public void actionPerformed(ActionEvent e) {
		}
	};
}