package gui.undo;

import gui.opdProject.OpdProject;
import gui.projectStructure.ObjectInstance;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableDeleteObject extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	private UndoableAddObject antagonist;

	public UndoableDeleteObject(OpdProject project, ObjectInstance pInstance) {
		this.antagonist = new UndoableAddObject(project, pInstance);
	}

	public String getPresentationName() {
		return "Object Deletion";
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
