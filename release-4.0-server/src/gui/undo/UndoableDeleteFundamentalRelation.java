package gui.undo;

import gui.opdProject.OpdProject;
import gui.projectStructure.FundamentalRelationInstance;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableDeleteFundamentalRelation extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	private UndoableAddFundamentalRelation antagonist;

	public UndoableDeleteFundamentalRelation(OpdProject project,
			FundamentalRelationInstance pInstance) {
		this.antagonist = new UndoableAddFundamentalRelation(project, pInstance);
	}

	public String getPresentationName() {
		return "Fundamental Relation Deletion";
	}

	public void redo() {
		super.redo();
		this.antagonist.performUndoJob();
	}

	public void undo() {
		super.undo();
		this.antagonist.performRedoJob();
	}
}
