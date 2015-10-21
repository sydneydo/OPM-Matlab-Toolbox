package gui.undo;

import gui.opdProject.OpdProject;
import gui.projectStructure.LinkInstance;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableDeleteLink extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	private UndoableAddLink antagonist;

	public UndoableDeleteLink(OpdProject project, LinkInstance pInstance) {
		this.antagonist = new UndoableAddLink(project, pInstance);
	}

	public String getPresentationName() {
		return "Link Deletion";
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
