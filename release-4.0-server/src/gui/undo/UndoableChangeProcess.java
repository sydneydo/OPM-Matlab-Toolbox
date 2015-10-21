package gui.undo;

import gui.opdProject.OpdProject;
import gui.opmEntities.OpmProcess;
import gui.projectStructure.ProcessEntry;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableChangeProcess extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private ProcessEntry myEntry;

	private OpmProcess originalProcess;

	private OpmProcess changedProcess;

	public UndoableChangeProcess(OpdProject project, ProcessEntry pEntry,
			OpmProcess originalProcess, OpmProcess changedProcess) {
		this.myEntry = pEntry;
		this.originalProcess = new OpmProcess(-1, "");
		this.changedProcess = new OpmProcess(-1, "");

		this.originalProcess.copyPropsFrom(originalProcess);
		this.changedProcess.copyPropsFrom(changedProcess);
	}

	public String getPresentationName() {
		return "Process Change";
	}

	public void undo() {
		super.undo();
		((OpmProcess) this.myEntry.getLogicalEntity())
				.copyPropsFrom(this.originalProcess);
		this.myEntry.updateInstances();
	}

	public void redo() {
		super.redo();
		((OpmProcess) this.myEntry.getLogicalEntity()).copyPropsFrom(this.changedProcess);
		this.myEntry.updateInstances();

	}
}
