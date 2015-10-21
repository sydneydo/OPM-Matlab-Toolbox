package gui.undo;

import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmState;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableAddState extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private OpdProject myProject;

	private StateEntry myEntry;

	private Vector instances;

	private Vector statesSizes;

	private Vector statesLocations;

	double originalSize;

	public UndoableAddState(OpdProject project, StateEntry entry) {
		this.myProject = project;
		this.myEntry = entry;

		GenericTable config = this.myProject.getConfiguration();
		this.originalSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();

		this.instances = new Vector();
		this.statesSizes = new Vector();
		this.statesLocations = new Vector();

		for (Enumeration e = this.myEntry.getInstances(); e.hasMoreElements();) {
			StateInstance si = (StateInstance) e.nextElement();
			this.instances.add(si);
			this.statesSizes.add(si.getState().getSize());
			this.statesLocations.add(si.getState().getLocation());
		}

	}

	public String getPresentationName() {
		return "State Addition";
	}

	public void undo() {
		super.undo();
		this.performUndoJob();
	}

	void performUndoJob() {
		this.myProject.deleteState((StateInstance) this.instances.get(0));
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

		ObjectEntry parent = (ObjectEntry) ms.getEntry(this.myEntry
				.getParentObjectId());
		parent.addState((OpmState) this.myEntry.getLogicalEntity());

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double factor = currentSize / this.originalSize;

		int counter = 0;

		for (Enumeration e = this.instances.elements(); e.hasMoreElements();) {

			StateInstance currInstance = (StateInstance) e.nextElement();
			this.myEntry.addInstance(currInstance.getKey(), currInstance);
			currInstance.getParent().add(currInstance.getConnectionEdge());

			Point stLocation = (Point) this.statesLocations.get(counter);
			Dimension stSize = (Dimension) this.statesSizes.get(counter);
			currInstance.getState().setLocation(
					(int) Math.round(stLocation.getX() * factor),
					(int) Math.round(stLocation.getY() * factor));
			currInstance.getState().setSize(
					(int) Math.round(stSize.getWidth() * factor),
					(int) Math.round(stSize.getHeight() * factor));
			counter++;

			currInstance.getParent().repaint();
		}

	}

}
