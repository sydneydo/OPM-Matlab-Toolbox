package gui.undo;

import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableAddObject extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private OpdProject myProject;

	private ObjectInstance tempObject;

	private Container myContainer;

	private ObjectEntry myEntry;

	private Vector stateVector;

	private Vector statesSizes;

	private Vector statesLocations;

	double originalSize;

	private Point location;

	private Dimension size;

	public UndoableAddObject(OpdProject project, ObjectInstance pInstance) {
		this.myProject = project;
		this.tempObject = pInstance;
		this.myContainer = this.tempObject.getThing().getParent();
		this.myEntry = (ObjectEntry) pInstance.getEntry();

		GenericTable config = this.myProject.getConfiguration();
		this.originalSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		this.location = this.tempObject.getConnectionEdge().getLocation();
		this.size = this.tempObject.getConnectionEdge().getSize();

		this.stateVector = new Vector();
		this.statesSizes = new Vector();
		this.statesLocations = new Vector();

		for (Enumeration e = this.tempObject.getStateInstances(); e
				.hasMoreElements();) {
			StateInstance si = (StateInstance) e.nextElement();
			this.stateVector.add(si);
			this.statesSizes.add(si.getState().getSize());
			this.statesLocations.add(si.getState().getLocation());
		}
	}

	public String getPresentationName() {
		return "Object Addition";
	}

	public void undo() {
		super.undo();
		this.performUndoJob();
	}

	void performUndoJob() {
		this.myProject.deleteObject(this.tempObject);
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

		this.myEntry.addInstance(this.tempObject.getKey(), this.tempObject);
		this.tempObject.add2Container(this.myContainer);

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double factor = currentSize / this.originalSize;

		this.tempObject.getConnectionEdge().setLocation(
				(int) Math.round(this.location.getX() * factor),
				(int) Math.round(this.location.getY() * factor));
		this.tempObject.getConnectionEdge().setSize(
				(int) Math.round(this.size.getWidth() * factor),
				(int) Math.round(this.size.getHeight() * factor));

		int counter = 0;
		for (Enumeration e = this.stateVector.elements(); e.hasMoreElements();) {

			StateInstance currInstance = (StateInstance) e.nextElement();
			StateEntry currEntry = (StateEntry) currInstance.getEntry();

			Point stLocation = (Point) this.statesLocations.get(counter);
			Dimension stSize = (Dimension) this.statesSizes.get(counter);

			if (ms.getEntry(currEntry.getId()) == null) {
				ms.put(currEntry.getId(), currEntry);
			}

			currEntry.addInstance(currInstance.getKey(), currInstance);
			this.tempObject.getThing().add(currInstance.getConnectionEdge());

			currInstance.getState().setLocation(
					(int) Math.round(stLocation.getX() * factor),
					(int) Math.round(stLocation.getY() * factor));
			currInstance.getState().setSize(
					(int) Math.round(stSize.getWidth() * factor),
					(int) Math.round(stSize.getHeight() * factor));
			counter++;
		}
	}

}
