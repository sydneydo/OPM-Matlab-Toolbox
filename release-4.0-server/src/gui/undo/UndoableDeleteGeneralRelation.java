package gui.undo;

import gui.opdProject.OpdProject;
import gui.projectStructure.GeneralRelationInstance;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableDeleteGeneralRelation extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	private UndoableAddGeneralRelation antagonist;

	public UndoableDeleteGeneralRelation(OpdProject project,
			GeneralRelationInstance pInstance) {
		this.antagonist = new UndoableAddGeneralRelation(project, pInstance);
	}

	public String getPresentationName() {
		return "General Relation Deletion";
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
