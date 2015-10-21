package gui.undo;

import exportedAPI.OpdKey;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmFundamentalRelation;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GraphicRepresentationKey;
import gui.projectStructure.GraphicalRelationRepresentation;
import gui.projectStructure.MainStructure;

import java.awt.Container;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableAddFundamentalRelation extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private OpdProject myProject;

	private FundamentalRelationInstance tempRelation;

	private Container myContainer;

	private FundamentalRelationEntry myEntry;

	private GraphicalRelationRepresentation gRepr;

	public UndoableAddFundamentalRelation(OpdProject project,
			FundamentalRelationInstance pInstance) {
		this.myProject = project;
		this.tempRelation = pInstance;
		this.myContainer = this.tempRelation.getLine(0).getParent();
		this.myEntry = (FundamentalRelationEntry) pInstance.getEntry();
		this.gRepr = pInstance.getGraphicalRelationRepresentation();
	}

	public String getPresentationName() {
		return "Fundamental Relation Addition";
	}

	public void undo() {
		super.undo();
		this.performUndoJob();
	}

	void performUndoJob() {
		this.myProject.deleteFundamentalRelation(this.tempRelation);
	}

	public void redo() {
		super.redo();
		this.performRedoJob();
	}

	void performRedoJob() {
		MainStructure ms = this.myProject.getComponentsStructure();

		OpdKey sKey = new OpdKey(this.gRepr.getSource().getOpdId(), this.gRepr
				.getSource().getEntityInOpdId());
		GraphicRepresentationKey myKey = new GraphicRepresentationKey(sKey,
				this.gRepr.getType());

		if (ms.getGraphicalRepresentation(myKey) == null) {
			ms.put(myKey, this.gRepr);
			this.gRepr.add2Container(this.myContainer);
		}

		if (ms.getEntry(this.myEntry.getId()) == null) {
			ms.put(this.myEntry.getId(), this.myEntry);
		}

		OpmFundamentalRelation lRelation = (OpmFundamentalRelation) this.myEntry
				.getLogicalEntity();

		ConnectionEdgeEntry sourceEntry = (ConnectionEdgeEntry) tempRelation
				.getSourceInstance().getEntry(); // ms.getSourceEntry(lRelation);
		ConnectionEdgeEntry destinationEntry = (ConnectionEdgeEntry) tempRelation
				.getDestinationInstance().getEntry(); // ms.getDestinationEntry(lRelation);

		sourceEntry.addRelationSource(lRelation);
		destinationEntry.addRelationDestination(lRelation);

		this.gRepr.addInstance(this.tempRelation.getKey(), this.tempRelation);
		this.myEntry.addInstance(this.tempRelation.getKey(), this.tempRelation);
		this.tempRelation.add2Container(this.myContainer);
		this.tempRelation.update();

	}

}
