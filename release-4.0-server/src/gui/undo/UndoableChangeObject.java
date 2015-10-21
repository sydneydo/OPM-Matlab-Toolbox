package gui.undo;

import gui.opdProject.OpdProject;
import gui.opmEntities.OpmObject;
import gui.projectStructure.Entry;
import gui.projectStructure.ObjectEntry;

import java.util.Enumeration;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableChangeObject extends AbstractUndoableEdit {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private OpdProject myProject;

	private ObjectEntry myEntry;

	private OpmObject originalObject;

	private OpmObject changedObject;

	public UndoableChangeObject(OpdProject project, ObjectEntry pEntry,
			OpmObject originalObject, OpmObject changedObject) {
		this.myProject = project;
		this.myEntry = pEntry;
		this.originalObject = new OpmObject(-1, "");
		this.changedObject = new OpmObject(-1, "");

		this.originalObject.copyPropsFrom(originalObject);
		this.changedObject.copyPropsFrom(changedObject);
	}

	public String getPresentationName() {
		return "Object Change";
	}

	public void undo() {
		super.undo();
		((OpmObject) this.myEntry.getLogicalEntity()).copyPropsFrom(this.originalObject);
		this.myEntry.updateInstances();

		if (!this.originalObject.getName().equals(this.changedObject.getName())) {
			this._updateObjectsTypes(this.originalObject.getName());
		}
	}

	public void redo() {
		super.redo();
		((OpmObject) this.myEntry.getLogicalEntity()).copyPropsFrom(this.changedObject);
		this.myEntry.updateInstances();

		if (!this.originalObject.getName().equals(this.changedObject.getName())) {
			this._updateObjectsTypes(this.changedObject.getName());
		}

	}

	private void _updateObjectsTypes(String newName) {
		for (Enumeration e = this.myProject.getComponentsStructure().getElements(); e
				.hasMoreElements();) {
			Entry currEntry = (Entry) e.nextElement();

			if (currEntry instanceof ObjectEntry) {
				OpmObject currObject = (OpmObject) currEntry.getLogicalEntity();
				if (currObject.getTypeOriginId() == this.myEntry.getId()) {
					currObject.setType(newName);
					currEntry.updateInstances();
				}
			}
		}

		return;
	}

}
