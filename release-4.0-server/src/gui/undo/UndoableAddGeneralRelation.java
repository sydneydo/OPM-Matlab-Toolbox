package gui.undo;

import gui.opdProject.OpdProject;
import gui.opmEntities.OpmGeneralRelation;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.GeneralRelationEntry;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.MainStructure;

import java.awt.Container;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableAddGeneralRelation extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private OpdProject myProject;

	private GeneralRelationInstance tempRelation;

	private Container myContainer;

	private GeneralRelationEntry myEntry;

	public UndoableAddGeneralRelation(OpdProject project,
			GeneralRelationInstance pInstance) {
		this.myProject = project;
		this.tempRelation = pInstance;
		this.myContainer = this.tempRelation.getSourceDragger().getParent();
		this.myEntry = (GeneralRelationEntry) pInstance.getEntry();
	}

	public String getPresentationName() {
		return "General Relation Addition";
	}

	public void undo() {
		super.undo();
		this.performUndoJob();
	}

	void performUndoJob() {
		this.myProject.deleteGeneralRelation(this.tempRelation);
	}

	public void redo() {
		super.redo();
		this.performRedoJob();
	}

	void performRedoJob() {
		MainStructure ms = this.myProject.getComponentsStructure();

		if (ms.getEntry(this.myEntry.getId()) == null) {
			ms.put(this.myEntry.getId(), this.myEntry);
		}

		OpmGeneralRelation lRelation = (OpmGeneralRelation) this.myEntry
				.getLogicalEntity();

		ConnectionEdgeEntry sourceEntry = (ConnectionEdgeEntry) tempRelation
				.getSourceInstance().getEntry(); // ms.getSourceEntry(lRelation);
		ConnectionEdgeEntry destinationEntry = (ConnectionEdgeEntry) tempRelation
				.getDestinationInstance().getEntry(); // ms.getDestinationEntry(lRelation);
		sourceEntry.addGeneralRelationSource(lRelation);
		destinationEntry.addGeneralRelationDestination(lRelation);

		this.myEntry.addInstance(this.tempRelation.getKey(), this.tempRelation);
		this.tempRelation.add2Container(this.myContainer);
		this.tempRelation.update();

	}

}
