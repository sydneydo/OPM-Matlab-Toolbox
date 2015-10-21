package gui.opdProject;

import exportedAPI.opcatAPIx.IXNode;
import extensionTools.opcatLayoutManager.ConstraintBasedForceDirectedLayoutSystem;
import extensionTools.opcatLayoutManager.Constraints.Constraint;
import gui.Opcat2;
import gui.controls.EditControl;
import gui.opdGraphics.opdBaseComponents.OpdFundamentalRelation;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.ThingInstance;
import gui.undo.CompoundUndoAction;
import gui.undo.UndoableMoveResizeComponent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;

public class OpcatLayoutManager {
	OpdProject myProject;

	ConstraintBasedForceDirectedLayoutSystem myLayoutManager;

	ArrayList oldLocations = new ArrayList();

	public OpcatLayoutManager(OpdProject project) {
		this.myProject = project;
		this.myLayoutManager = new ConstraintBasedForceDirectedLayoutSystem();
	}

	private Opd getOPD() {
		return this.myProject.getCurrentOpd();
	}

	private void updateUndo() {
		CompoundUndoAction undoAction = new CompoundUndoAction();

		for (Iterator oldLocs = oldLocations.iterator(); oldLocs.hasNext();) {
			NodeOldLocation currNode = (NodeOldLocation) oldLocs.next();
			JComponent jNode = currNode.node;
			undoAction.addAction(new UndoableMoveResizeComponent(jNode,
					currNode.location, jNode.getSize(), jNode.getLocation(),
					jNode.getSize(), this.myProject));
		}

		Opcat2.getUndoManager().undoableEditHappened(
				new UndoableEditEvent(this, undoAction));
		Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
		Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
	}

	private void updateOLDLocationsForUndo() {

		Opd currentOpd = getOPD();
		if (currentOpd == null)
			return;

		oldLocations = new ArrayList();

		for (Enumeration enumSelected = currentOpd.getSelectedItems(); enumSelected
				.hasMoreElements();) {
			Object currItem = enumSelected.nextElement();
			if (currItem instanceof ConnectionEdgeInstance) {
				ConnectionEdgeInstance currInstance = (ConnectionEdgeInstance) currItem;
				oldLocations.add(new NodeOldLocation(currInstance
						.getConnectionEdge()));
			}

			if (currItem instanceof FundamentalRelationInstance) {
				FundamentalRelationInstance currInstance = (FundamentalRelationInstance) currItem;
				OpdFundamentalRelation triangle = currInstance
						.getGraphicalRelationRepresentation().getRelation();
				oldLocations.add(new NodeOldLocation(triangle));
			}
		}
	}

	private void alignDirections(int direction, int origin) {
		Opd currentOpd = getOPD();
		
		if (currentOpd == null) {
			return;
		}

		updateOLDLocationsForUndo();

		IXNode mainNode = null;

		for (Enumeration enumSelected = currentOpd.getSelectedItems(); enumSelected
				.hasMoreElements();) {
			Object currItem = enumSelected.nextElement();
			if (currItem instanceof ThingInstance) {
				ThingInstance currInstance = (ThingInstance) currItem;
				mainNode = currInstance;
			}
		}

		//
		// Clear any previously inserted elements from the system.
		//
		this.myLayoutManager.clear();

		//
		// Create an alignment constraint according to the given direction and
		// origin.
		// This function automatically adds any involved nodes and edges.
		//
		Constraint constraint = this.myLayoutManager.createAlignmentConstraint(
				direction, origin, mainNode, currentOpd.getSelectedItems());

		//
		// Add the constraint to the system.
		//
		this.myLayoutManager.addConstraint(constraint);

		//
		// Initialize an arrangment operation.
		// This function must be called before any arrangment operation.
		//
		this.myLayoutManager.initArrange(false /* one-phase arrangment */);

		//
		// Start an arangment operation.
		//
		this.myLayoutManager.arrange(100 /* maximum 100 iterations */);

		updateUndo();
		
		EditControl.getInstance().getCurrentProject().getCurrentOpd().refit() ; 

	}

	private void evenSpaceDirections(int direction, int origin) {
		Opd currentOpd = this.myProject.getCurrentOpd();
		if (currentOpd == null) {
			return;
		}

		updateOLDLocationsForUndo();

		IXNode mainNode = null;

		for (Enumeration enumSelected = currentOpd.getSelectedItems(); enumSelected
				.hasMoreElements();) {
			Object currItem = enumSelected.nextElement();
			if (currItem instanceof ConnectionEdgeInstance) {
				ConnectionEdgeInstance currInstance = (ConnectionEdgeInstance) currItem;
				mainNode = currInstance;
			}
		}

		//
		// Clear any previously inserted elements from the system.
		//
		this.myLayoutManager.clear();

		//
		// Create an alignment constraint according to the given direction and
		// origin.
		// This function automatically adds any involved nodes and edges.
		//
		Constraint constraint = this.myLayoutManager.createSpacingConstraint(
				direction, 20, currentOpd.getSelectedItems());

		//
		// Add the constraint to the system.
		//
		this.myLayoutManager.addConstraint(constraint);

		constraint = this.myLayoutManager.createAlignmentConstraint(direction,
				origin, mainNode, currentOpd.getSelectedItems());

		this.myLayoutManager.addConstraint(constraint);

		//
		// Initialize an arrangment operation.
		// This function must be called before any arrangment operation.
		//
		this.myLayoutManager.initArrange(false /* one-phase arrangment */);

		//
		// Start an arangment operation.
		//
		this.myLayoutManager.arrange(100 /* maximum 100 iterations */);

		updateUndo();
		
		EditControl.getInstance().getCurrentProject().getCurrentOpd().refit() ; 		

	}

	public void align2Left() {
		this.alignDirections(Constraint.directionVertical,
				Constraint.originStart);
	}

	public void align2CenterVertically() {
		this.alignDirections(Constraint.directionVertical,
				Constraint.originCenter);
	}

	public void evenSpaceVertically() {
		this.evenSpaceDirections(Constraint.directionVertical,
				Constraint.originStart);
	}

	public void align2Right() {
		this
				.alignDirections(Constraint.directionVertical,
						Constraint.originEnd);
	}

	public void align2Top() {
		this.alignDirections(Constraint.directionHorizontal,
				Constraint.originStart);
	}

	public void align2CenterHorizontally() {
		this.alignDirections(Constraint.directionHorizontal,
				Constraint.originCenter);
	}

	public void align2Bottom() {
		this.alignDirections(Constraint.directionHorizontal,
				Constraint.originEnd);
	}

	public void alignAsTree() {
		Opd currentOpd = this.myProject.getCurrentOpd();
		if (currentOpd == null) {
			return;
		}

		updateOLDLocationsForUndo() ; 
		
		
		IXNode mainNode = null;
		IXNode relationNode = null;

		for (Enumeration enumSelected = currentOpd.getSelectedItems(); enumSelected
				.hasMoreElements();) {
			Object currItem = enumSelected.nextElement();
			if (currItem instanceof ThingInstance) {
				ThingInstance currInstance = (ThingInstance) currItem;
				mainNode = currInstance;
			}

			if (currItem instanceof FundamentalRelationInstance) {
				FundamentalRelationInstance currInstance = (FundamentalRelationInstance) currItem;
				OpdFundamentalRelation triangle = currInstance
						.getGraphicalRelationRepresentation().getRelation();
				relationNode = triangle;
			}
		}

		// System.err.println("Main node
		// "+((ThingInstance)mainNode).getEntry().getName()+" rel
		// "+relationNode);
		this.myLayoutManager.clear();

		Constraint constraint = this.myLayoutManager.createTreeConstraint(
				mainNode, relationNode, currentOpd.getSelectedItems());
		this.myLayoutManager.addConstraint(constraint);
		this.myLayoutManager.initArrange(false);
		this.myLayoutManager.arrange(100);

		updateUndo();
		
		EditControl.getInstance().getCurrentProject().getCurrentOpd().refit() ; 		

	}

	class NodeOldLocation {
		JComponent node;

		Point location;

		NodeOldLocation(JComponent node) {
			this.node = node;
			this.location = new Point(node.getX(), node.getY());
		}

	}
}