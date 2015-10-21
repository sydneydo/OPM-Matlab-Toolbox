package gui.repository.rpopups;

import gui.controls.EditControl;
import gui.images.standard.StandardImages;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmThing;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.repository.BaseView;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JSeparator;

public class REntryPopup extends RDefaultPopup {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    /**
         * 
         */

    OpmThing myOpmThing;

    ThingEntry myEntry;

    public REntryPopup(BaseView view, OpdProject prj) {
	super(view, prj);
	this.myOpmThing = (OpmThing) this.userObject;
	myEntry = (ThingEntry) prj.getSystemStructure().getEntry(
		myOpmThing.getId());
	this.add(this.showAction);
	this.add(new JSeparator());
	this.add(this.deleteAction);
	this.add(new JSeparator());
	this.add(this.zoomInAction);
	this.add(this.unfoldingAction);
	this.add(this.createViewAction);
	this.add(new JSeparator());
	this.add(this.propertiesAction);
	this.add(new JSeparator());
	this.add(this.copyAction);
	this.addCollapseExpand();
    }

    Action showAction = new AbstractAction("Show all appearances",
	    StandardImages.EMPTY) {

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
         * 
         */

	public void actionPerformed(ActionEvent e) {
	    REntryPopup.this.myEntry.ShowInstances();
	}
    };

    Action copyAction = new AbstractAction("Copy", StandardImages.COPY) {

	/**
         * 
         */
	private static final long serialVersionUID = -2130296981968671447L;

	public void actionPerformed(ActionEvent e) {
	    if (myEntry != null) {
		Enumeration insEnum = myEntry.getInstances();
		ThingInstance local = (ThingInstance) insEnum.nextElement();
		EditControl edit = EditControl.getInstance();
		edit.removeSelection(true);
		local.getOpd().addSelection(local, true);
		edit.copy(local.getOpd());
	    }
	}
    };

    Action deleteAction = new AbstractAction("Delete", StandardImages.DELETE) {

	/**
         * 
         */
	private static final long serialVersionUID = -1586427737686549765L;

	public void actionPerformed(ActionEvent e) {
	    ThingEntry entry;
	    entry = (ThingEntry) REntryPopup.this.myEntry;
	    if (entry != null) {
		Enumeration insEnum = entry.getInstances();
		ThingInstance local = (ThingInstance) insEnum.nextElement();
		REntryPopup.this.myProject.showOPD(local.getOpd().getOpdId());
		REntryPopup.this.myProject.removeSelection();
		REntryPopup.this.myProject.addSelection(local, true);
		local.setSelected(true);
		REntryPopup.this.myProject.delete();
	    }

	}
    };

    Action zoomInAction = new AbstractAction("In-Zooming", StandardImages.EMPTY) {

	/**
         * 
         */
	private static final long serialVersionUID = 4601416826417432940L;

	public void actionPerformed(ActionEvent e) {
	    ThingEntry entry;
	    entry = (ThingEntry) REntryPopup.this.myEntry;
	    if ((entry != null) && (entry instanceof ProcessEntry)) {
		Enumeration insEnum = entry.getInstances();
		ThingInstance local = (ThingInstance) insEnum.nextElement();
		REntryPopup.this.myProject.showOPD(local.getOpd().getOpdId());
		REntryPopup.this.myProject.removeSelection();
		REntryPopup.this.myProject.addSelection(local, true);
		local.setSelected(true);
		REntryPopup.this.myProject.zoomIn();
	    }
	}
    };

    Action createViewAction = new AbstractAction("Create View",
	    StandardImages.EMPTY) {

	/**
         * 
         */
	private static final long serialVersionUID = -6745841788742182231L;

	public void actionPerformed(ActionEvent e) {
	    try {
		ThingEntry entry;
		entry = (ThingEntry) REntryPopup.this.myEntry;
		Enumeration insEnum = entry.getInstances();
		ThingInstance local = (ThingInstance) insEnum.nextElement();
		REntryPopup.this.myProject.showOPD(local.getOpd().getOpdId());
		REntryPopup.this.myProject.removeSelection();
		REntryPopup.this.myProject.addSelection(local, true);
		local.setSelected(true);
		REntryPopup.this.myProject.createView();
	    } catch (Exception e1) {
		System.out.println(e1);
	    }
	    myProject.setCanClose(false);
	}
    };

    Action unfoldingAction = new AbstractAction("Unfolding",
	    StandardImages.EMPTY) {

	/**
         * 
         */
	private static final long serialVersionUID = -5811445809081408803L;

	public void actionPerformed(ActionEvent e) {
	    ThingEntry entry;
	    entry = (ThingEntry) REntryPopup.this.myEntry;
	    if ((entry != null) && (entry instanceof ObjectEntry)) {
		Enumeration insEnum = entry.getInstances();
		ThingInstance local = (ThingInstance) insEnum.nextElement();
		REntryPopup.this.myProject.showOPD(local.getOpd().getOpdId());
		REntryPopup.this.myProject.removeSelection();
		REntryPopup.this.myProject.addSelection(local, true);
		local.setSelected(true);
		REntryPopup.this.myProject.unfolding();
	    }
	}
    };

    Action propertiesAction = new AbstractAction("Properties",
	    StandardImages.PROPERTIES) {

	/**
         * 
         */
	private static final long serialVersionUID = -7956680214857803796L;

	public void actionPerformed(ActionEvent e) {
	    ThingEntry entry;
	    entry = (ThingEntry) REntryPopup.this.myEntry;
	    if ((entry != null)) {
		Enumeration insEnum = entry.getInstances();
		ThingInstance local = (ThingInstance) insEnum.nextElement();
		REntryPopup.this.myProject.showOPD(local.getOpd().getOpdId());
		REntryPopup.this.myProject.removeSelection();
		REntryPopup.this.myProject.addSelection(local, true);
		local.setSelected(true);
		local.getThing().callPropertiesDialog(
			BaseGraphicComponent.SHOW_ALL_TABS,
			BaseGraphicComponent.SHOW_ALL_BUTTONS);
		REntryPopup.this.myProject.getComponentsStructure().getOpd(
			local.getThing().getOpdId()).getDrawingArea().repaint();
	    }
	}
    };

}