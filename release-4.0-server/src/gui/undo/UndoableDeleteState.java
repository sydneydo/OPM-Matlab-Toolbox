package gui.undo;

import gui.opdProject.OpdProject;
import gui.projectStructure.StateEntry;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableDeleteState extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	private UndoableAddState antagonist;

	public UndoableDeleteState(OpdProject project, StateEntry entry) {
		this.antagonist = new UndoableAddState(project, entry);
	}

	public String getPresentationName() {
		return "State Deletion";
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
