package gui.repository.rpopups;

import exportedAPI.opcatAPIx.IXCheckResult;
import gui.Opcat2;
import gui.checkModule.CheckModule;
import gui.checkModule.CheckResult;
import gui.controls.EditControl;
import gui.images.standard.StandardImages;
import gui.opdGraphics.DrawingArea;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.projectStructure.Instance;
import gui.projectStructure.ThingInstance;
import gui.repository.BaseView;
import gui.undo.CompoundUndoAction;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;

public class RThingPopup extends RDefaultPopup {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
         * 
         */

	ThingInstance myInstance;

	OpdThing myThing;

	public RThingPopup(BaseView view, OpdProject prj) {
		super(view, prj);

		this.myInstance = (ThingInstance) this.userObject;
		this.myThing = this.myInstance.getThing();

		boolean env = myInstance.getEntry().getLogicalEntity().isEnviromental();
		boolean hasRoles = (myInstance.getEntry().getLogicalEntity()
				.getRolesManager().getLoadedPoliciesRolesVector().size() > 0);
		boolean ext = (env && hasRoles) || (myInstance.isPointerInstance());

		this.add(this.showAction);
		this.add(new JSeparator());
		this.add(this.deleteAction);
		if (!ext)
			this.add(new JSeparator());
		if (!ext)
			this.add(this.zoomInAction);
		if (!ext)
			this.add(this.unfoldingAction);
		if (!ext)
			this.add(this.createViewAction);
		this.add(new JSeparator());
		this.add(this.propertiesAction);
		this.add(new JSeparator());
		this.add(this.copyAction);
		this.addCollapseExpand();
	}

	Action showAction = new AbstractAction("Focus On Thing",
			StandardImages.EMPTY) {

		/**
         * 
         */
		private static final long serialVersionUID = 1L;

		/**
         * 
         */

		public void actionPerformed(ActionEvent e) {
			long opdId = RThingPopup.this.myThing.getOpdId();
			Opd myOpd = RThingPopup.this.myProject.getComponentsStructure()
					.getOpd(opdId);
			RThingPopup.this.showOpd(myOpd);
			RThingPopup.this.myProject.removeSelection();
			RThingPopup.this.myProject.addSelection(
					RThingPopup.this.myInstance, true);
			RThingPopup.this.myThing.setSelected(true);
			myProject.setCanClose(false);
			// center view to Thing
			RThingPopup.this.centerView(myOpd, RThingPopup.this.myInstance);

		}
	};

	Action copyAction = new AbstractAction("Copy", StandardImages.COPY) {

		/**
         * 
         */
		private static final long serialVersionUID = -9011119202463736832L;

		public void actionPerformed(ActionEvent e) {
			EditControl edit = EditControl.getInstance();
			edit.removeSelection(true);
			myInstance.getOpd().addSelection(myInstance, true);
			edit.copy(RThingPopup.this.myInstance.getOpd());
		}
	};

	Action pasteAction = new AbstractAction("Paste", StandardImages.PASTE) {

		/**
         * 
         */
		private static final long serialVersionUID = 928378074421546471L;

		public void actionPerformed(ActionEvent e) {
			EditControl edit = EditControl.getInstance();
			edit.paste(null);
		}
	};

	Action deleteAction = new AbstractAction("Delete", StandardImages.DELETE) {

		/**
         * 
         */
		private static final long serialVersionUID = -6362589581436229540L;

		public void actionPerformed(ActionEvent e) {

			CheckResult cr = CheckModule.checkDeletion(
					RThingPopup.this.myInstance, RThingPopup.this.myProject);
			if (cr.getResult() == IXCheckResult.WRONG) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(), cr
						.getMessage(), "Opcat2 - Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (cr.getResult() == IXCheckResult.WARNING) {
				int retVal = JOptionPane.showConfirmDialog(Opcat2.getFrame(),
						cr.getMessage(), "Opcat2 - Warning",
						JOptionPane.YES_NO_OPTION);

				if (retVal != JOptionPane.YES_OPTION) {
					return;
				}
			}

			CompoundUndoAction undoAction = new CompoundUndoAction();

			for (Enumeration e1 = RThingPopup.this.myInstance
					.getRelatedInstances(); e1.hasMoreElements();) {
				Instance currInstance = (Instance) e1.nextElement();
				undoAction.addAction(RThingPopup.this.myProject
						.deleteInstance(currInstance));
			}

			undoAction.addAction(RThingPopup.this.myProject
					.deleteInstance(RThingPopup.this.myInstance));

			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(this, undoAction));
			Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
			Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());

			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
			RThingPopup.this.myProject.getCurrentOpd().getDrawingArea()
					.repaint();

			myProject.setCanClose(false);
		}
	};

	Action zoomInAction = new AbstractAction("In-Zooming", StandardImages.EMPTY) {

		/**
         * 
         */
		private static final long serialVersionUID = -661820317481924192L;

		public void actionPerformed(ActionEvent e) {
			try {
				// Opd myOpd =
				// RThingPopup.this.myProject.getComponentsStructure()
				// .getOpd(RThingPopup.this.myThing.getOpdId());
				RThingPopup.this.myProject.removeSelection();
				RThingPopup.this.myProject.addSelection(
						RThingPopup.this.myInstance, true);
				RThingPopup.this.myThing.setSelected(true);
				// RThingPopup.this.centerView(myOpd,
				// RThingPopup.this.myInstance);

				RThingPopup.this.myProject.zoomIn();
			} catch (Exception e1) {
				System.out.println(e1);
			}
			myProject.setCanClose(false);
		}
	};

	Action unfoldingAction = new AbstractAction("Unfolding",
			StandardImages.EMPTY) {

		/**
         * 
         */
		private static final long serialVersionUID = -6745841788742182231L;

		public void actionPerformed(ActionEvent e) {
			try {
				// Opd myOpd =
				// RThingPopup.this.myProject.getComponentsStructure()
				// .getOpd(RThingPopup.this.myThing.getOpdId());
				RThingPopup.this.myProject.removeSelection();
				RThingPopup.this.myProject.addSelection(
						RThingPopup.this.myInstance, true);
				RThingPopup.this.myThing.setSelected(true);
				// RThingPopup.this.centerView(myOpd,
				// RThingPopup.this.myInstance);

				RThingPopup.this.myProject.unfolding();
			} catch (Exception e1) {
				System.out.println(e1);
			}
			myProject.setCanClose(false);
		}
	};

	Action createViewAction = new AbstractAction("Create View",
			StandardImages.EMPTY) {

		/**
         * 
         */
		private static final long serialVersionUID = -6745841788742182231L;

		public void actionPerformed(ActionEvent e) {
			try {
				// Opd myOpd =
				// RThingPopup.this.myProject.getComponentsStructure()
				// .getOpd(RThingPopup.this.myThing.getOpdId());
				RThingPopup.this.myProject.removeSelection();
				RThingPopup.this.myProject.addSelection(
						RThingPopup.this.myInstance, true);
				RThingPopup.this.myThing.setSelected(true);
				// RThingPopup.this.centerView(myOpd,
				// RThingPopup.this.myInstance);

				RThingPopup.this.myProject.createView();
			} catch (Exception e1) {
				System.out.println(e1);
			}
			myProject.setCanClose(false);
		}
	};

	Action propertiesAction = new AbstractAction("Properties",
			StandardImages.PROPERTIES) {

		/**
         * 
         */
		private static final long serialVersionUID = 4806047327580173904L;

		public void actionPerformed(ActionEvent e) {
			RThingPopup.this.myThing.callPropertiesDialog(
					BaseGraphicComponent.SHOW_ALL_TABS,
					BaseGraphicComponent.SHOW_ALL_BUTTONS);
			RThingPopup.this.myProject.getComponentsStructure().getOpd(
					RThingPopup.this.myThing.getOpdId()).getDrawingArea()
					.repaint();
		}
	};

	private void centerView(Opd myOpd, ThingInstance inst) {
		DrawingArea da = myOpd.getDrawingArea();
		JViewport jv = (JViewport) da.getParent();
		OpdThing thing = inst.getThing();
		Dimension visiblePort = jv.getExtentSize();

		int newX = (thing.getX() + thing.getWidth() / 2);
		int newY = (thing.getY() + thing.getHeight() / 2);

		Point thingCenter = SwingUtilities.convertPoint(thing.getParent(),
				newX, newY, da);

		newX = (int) (thingCenter.getX() - visiblePort.getWidth() / 2);
		newY = (int) (thingCenter.getY() - visiblePort.getHeight() / 2);

		int maxX = da.getWidth() - jv.getWidth();
		int maxY = da.getHeight() - jv.getHeight();

		if (newX < 0) {
			newX = 0;
		}
		if (newX > maxX) {
			newX = maxX;
		}
		if (newY < 0) {
			newY = 0;
		}
		if (newY > maxY) {
			newY = maxY;
		}

		jv.setViewPosition(new Point(newX, newY));
	}

	private void showOpd(Opd myOpd) {
		// myOpd.setVisible(true);
		myProject.showOPD(myOpd.getOpdId());
		// try {
		// myOpd.getOpdContainer().setSelected(true);
		// myOpd.getOpdContainer().setMaximum(true);
		// } catch (java.beans.PropertyVetoException pve) {
		// pve.printStackTrace();
		// }
	}

}