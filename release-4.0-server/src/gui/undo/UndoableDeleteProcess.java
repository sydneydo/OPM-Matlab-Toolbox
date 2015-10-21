package gui.undo;

import gui.opdProject.OpdProject;
import gui.projectStructure.ProcessInstance;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableDeleteProcess extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	private UndoableAddProcess antagonist;

	public UndoableDeleteProcess(OpdProject project, ProcessInstance pInstance) {
		this.antagonist = new UndoableAddProcess(project, pInstance);
	}

	public String getPresentationName() {
		return "Process Deletion";
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
