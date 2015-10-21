package gui.undo;

import gui.opdProject.OpdProject;
import gui.opmEntities.OpmProceduralLink;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.MainStructure;

import java.awt.Container;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableAddLink extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private OpdProject myProject;

	private LinkInstance tempLink;

	private Container myContainer;

	private LinkEntry myEntry;

	public UndoableAddLink(OpdProject project, LinkInstance pInstance) {
		this.myProject = project;
		this.tempLink = pInstance;
		this.myContainer = this.tempLink.getSourceDragger().getParent();
		this.myEntry = (LinkEntry) pInstance.getEntry();
	}

	public String getPresentationName() {
		return "Link Addition";
	}

	public void undo() {
		super.undo();
		this.performUndoJob();
	}

	void performUndoJob() {
		this.myProject.deleteLink(this.tempLink);
	}

	public void redo() {
		super.redo();
		this.performRedoJob();
	}

	void performRedoJob() {
		MainStructure ms = this.myProject.getComponentsStructure();

		if ((ms.getEntry(this.myEntry.getId()) == null)) {
			ms.put(this.myEntry.getId(), this.myEntry);
		}

		OpmProceduralLink lLink = (OpmProceduralLink) this.myEntry
				.getLogicalEntity();
		ConnectionEdgeEntry sourceEntry = (ConnectionEdgeEntry) tempLink
				.getSourceInstance().getEntry(); // ms.getSourceEntry(lLink);
		ConnectionEdgeEntry destinationEntry = (ConnectionEdgeEntry) tempLink
				.getDestinationInstance().getEntry(); // ms.getDestinationEntry(lLink);
		
		sourceEntry.addLinkSource(lLink);
		destinationEntry.addLinkDestination(lLink);

		this.myEntry.addInstance(this.tempLink.getKey(), this.tempLink);
		this.tempLink.add2Container(this.myContainer);
		this.tempLink.update();
	}

}
