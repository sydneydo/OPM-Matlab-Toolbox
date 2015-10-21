package gui.opdGraphics.popups;

import expose.OpcatExposeConstants.OPCAT_EXOPSE_ADVISOR_TYPE;
import expose.gui.OpcatExposeAdvisorGUI;
import gui.Opcat2;
import gui.actions.edit.CompleteLinksForConnectionEdgeEntry;
import gui.images.standard.StandardImages;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdProject.OpdProject;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Instance;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class StatePopup extends DefaultPopup {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
         * 
         */

	StateEntry stateEntry;

	CompleteLinksForConnectionEdgeEntry completeLinksActionWithOutAddMissing = null;

	CompleteLinksForConnectionEdgeEntry completeLinksActionWithAddMissing = null;

	public StatePopup(OpdProject prj, Instance inst) {
		super(prj, inst);

		completeLinksActionWithAddMissing = new CompleteLinksForConnectionEdgeEntry(
				"Add Missing Things", StandardImages.COMPLETE_LINKS,
				(ConnectionEdgeInstance) inst, true);
		completeLinksActionWithOutAddMissing = new CompleteLinksForConnectionEdgeEntry(
				"Do Not Add Missing Things",
				StandardImages.COMPLETE_LINKS_NO_THING,
				(ConnectionEdgeInstance) inst, false);
		JMenuItem jmi;
		// StateInstance stInstance = (StateInstance)
		// (this.myProject.getCurrentOpd()
		// .getSelectedItem());

		StateInstance stInstance = (StateInstance) inst;

		this.stateEntry = (StateEntry) stInstance.getEntry();

		if (((StateEntry) stInstance.getEntry()).isRelated()) {
			this.deleteAction.setEnabled(false);
		}
		if (stInstance.isRelated()) {
			this.hideAction.setEnabled(false);
		}
		this.add(this.deleteAction);
		this.add(this.hideAction);
		this.add(new JSeparator());
		jmi = new JMenuItem(this.setInitialAction);
		if (this.stateEntry.isInitial()) {
			jmi.setIcon(StandardImages.CHECKED);
		} else {
			jmi.setIcon(StandardImages.UNCHECKED);
		}
		this.add(jmi);
		jmi = new JMenuItem(this.setFinalAction);
		if (this.stateEntry.isFinal()) {
			jmi.setIcon(StandardImages.CHECKED);
		} else {
			jmi.setIcon(StandardImages.UNCHECKED);
		}
		this.add(jmi);

		JMenuItem item;
		if (stInstance.getEntry().getMyProject().isCanClose()) {
			if (stInstance.isPointerInstance()
					|| (stInstance.getEntry().getLogicalEntity()
							.getRolesManager().getRolesCollection().size() > 0)) {

				OpcatExposeAdvisorGUI inter = new OpcatExposeAdvisorGUI(
						stInstance, OPCAT_EXOPSE_ADVISOR_TYPE.INTERFACE_LOCAL);
				item = this.add(inter.init());

			} else {
				item = new JMenuItem("Advisor - not a used thing");
				item.setEnabled(false);
				this.add(item);
			}
		} else {
			item = new JMenuItem("Advisor - model not saved");
			item.setEnabled(false);
			this.add(item);
		}

		this.add(new JSeparator());
		JMenu completeLinks = new JMenu("Complete Links");
		completeLinks.setIcon(StandardImages.COMPLETE_LINKS);
		completeLinks.add(completeLinksActionWithOutAddMissing);
		completeLinks.add(completeLinksActionWithAddMissing);
		this.add(completeLinks);
		this.add(new JSeparator());
		// /////////////////////////////
		// JMenu alingMenu = new JMenu("Align");
		// alingMenu.setIcon(StandardImages.EMPTY);
		// alingMenu.add(this.alignLeftAction);
		// alingMenu.add(this.alignRightAction);
		// alingMenu.add(this.alignTopAction);
		// alingMenu.add(this.alignBottomAction);
		// alingMenu.add(this.alignCenterVerticallyAction);
		// alingMenu.add(this.alignCenterHorizontallyAction);
		// alingMenu.add(new JSeparator());
		// alingMenu.add(this.alignAsTreeAction);
		//
		// this.add(alingMenu);
		// if ((this.myProject.getCurrentOpd() == null)
		// || (this.myProject.getCurrentOpd().getSelectedItemsHash()
		// .size() < 2)) {
		// alingMenu.setEnabled(false);
		// }
		// this.add(new JSeparator());
		// ////////////////////////

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
			// StatePopup.this.myProject.getCurrentOpd().getSelectedItem()
			((StateInstance) (myInstance)).getState().callPropertiesDialog(
					BaseGraphicComponent.SHOW_ALL_TABS,
					BaseGraphicComponent.SHOW_ALL_BUTTONS);
			StatePopup.this.myProject.getCurrentOpd().getDrawingArea()
					.repaint();
		}
	};

	Action setInitialAction = new AbstractAction("Initial") {

		/**
         * 
         */
		private static final long serialVersionUID = 7188983139912268967L;

		public void actionPerformed(ActionEvent e) {
			StatePopup.this.stateEntry.setInitial(!StatePopup.this.stateEntry
					.isInitial());
			StatePopup.this.myProject.getCurrentOpd().getDrawingArea()
					.repaint();
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		}
	};

	Action setFinalAction = new AbstractAction("Final") {

		/**
         * 
         */
		private static final long serialVersionUID = 270733455132914076L;

		public void actionPerformed(ActionEvent e) {
			StatePopup.this.stateEntry.setFinal(!StatePopup.this.stateEntry
					.isFinal());
			StatePopup.this.myProject.getCurrentOpd().getDrawingArea()
					.repaint();
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		}
	};

	Action hideAction = new AbstractAction("Hide state", StandardImages.EMPTY) {

		/**
         * 
         */
		private static final long serialVersionUID = -9051346513385775950L;

		public void actionPerformed(ActionEvent e) {
			// StatePopup.this.myProject.getCurrentOpd().getSelectedItem()
			((StateInstance) (myInstance)).getState().setVisible(false);
			StatePopup.this.myProject.getCurrentOpd().getDrawingArea()
					.repaint();
		}
	};

	// //////////////////////////////
	Action alignLeftAction = new AbstractAction("Left") {

		/**
         * 
         */
		private static final long serialVersionUID = -5483421693041278633L;

		public void actionPerformed(ActionEvent e) {
			StatePopup.this.myProject.getLayoutManager().align2Left();
		}
	};

	Action alignRightAction = new AbstractAction("Right") {

		/**
         * 
         */
		private static final long serialVersionUID = 239503285663982807L;

		public void actionPerformed(ActionEvent e) {
			StatePopup.this.myProject.getLayoutManager().align2Right();
		}
	};

	Action alignTopAction = new AbstractAction("Top") {

		/**
         * 
         */
		private static final long serialVersionUID = 2305020403623966419L;

		public void actionPerformed(ActionEvent e) {
			StatePopup.this.myProject.getLayoutManager().align2Top();
		}
	};

	Action alignBottomAction = new AbstractAction("Bottom") {

		/**
         * 
         */
		private static final long serialVersionUID = 7814303177070480931L;

		public void actionPerformed(ActionEvent e) {
			StatePopup.this.myProject.getLayoutManager().align2Bottom();
		}
	};

	Action alignCenterVerticallyAction = new AbstractAction("Center Vertically") {

		/**
         * 
         */
		private static final long serialVersionUID = 1316613334680687140L;

		public void actionPerformed(ActionEvent e) {
			StatePopup.this.myProject.getLayoutManager().evenSpaceVertically();
			// .align2CenterVertically();

		}
	};

	Action alignCenterHorizontallyAction = new AbstractAction(
			"Center Horizontally") {

		/**
         * 
         */
		private static final long serialVersionUID = 6662070932673168893L;

		public void actionPerformed(ActionEvent e) {
			StatePopup.this.myProject.getLayoutManager()
					.align2CenterHorizontally();
		}
	};

	Action alignAsTreeAction = new AbstractAction("As Tree") {

		/**
         * 
         */
		private static final long serialVersionUID = -5193633177133700618L;

		public void actionPerformed(ActionEvent e) {
			StatePopup.this.myProject.getLayoutManager().alignAsTree();
		}
	};
	// /////////////////////////

}