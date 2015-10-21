package gui.undo;

import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableAddProcess extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private OpdProject myProject;

	private ProcessInstance tempProcess;

	private Container myContainer;

	private ProcessEntry myEntry;

	double originalSize;

	private Point location;

	private Dimension size;

	public UndoableAddProcess(OpdProject project, ProcessInstance pInstance) {
		this.myProject = project;
		this.tempProcess = pInstance;
		this.myContainer = this.tempProcess.getThing().getParent();
		this.myEntry = (ProcessEntry) pInstance.getEntry();
		GenericTable config = this.myProject.getConfiguration();
		this.originalSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		this.location = this.tempProcess.getConnectionEdge().getLocation();
		this.size = this.tempProcess.getConnectionEdge().getSize();
	}

	public String getPresentationName() {
		return "Process Addition";
	}

	public void undo() {
		super.undo();
		this.performUndoJob();
	}

	void performUndoJob() {
		this.myProject.deleteProcess(this.tempProcess);
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

		this.myEntry.addInstance(this.tempProcess.getKey(), this.tempProcess);
		this.tempProcess.add2Container(this.myContainer);

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double factor = currentSize / this.originalSize;

		this.tempProcess.getConnectionEdge().setLocation(
				(int) Math.round(this.location.getX() * factor),
				(int) Math.round(this.location.getY() * factor));
		this.tempProcess.getConnectionEdge().setSize(
				(int) Math.round(this.size.getWidth() * factor),
				(int) Math.round(this.size.getHeight() * factor));
	}
}
