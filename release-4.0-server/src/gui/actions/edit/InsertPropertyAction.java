package gui.actions.edit;

import exportedAPI.OpcatConstants;
import exportedAPI.RelativeConnectionPoint;
import gui.Opcat2;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.Role;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmState;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.w3c.dom.events.EventException;

public class InsertPropertyAction extends EditAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ThingInstance parent;

	ThingEntry son;

	FundamentalRelationEntry relation;
	
	Role myRole = null; 

	boolean fromMeta = false;

	/**
	 * 
	 */

	public InsertPropertyAction(ThingInstance parent, ThingEntry son,
			FundamentalRelationEntry relation, boolean fromMeta, Role role) {

		super("Add properties", StandardImages.EMPTY);
		this.parent = parent;
		this.son = son;
		this.relation = relation;
		this.fromMeta = fromMeta;
		this.myRole = role ; 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		try {
			super.actionPerformed(arg0);
		} catch (EventException e) {
			JOptionPane.showMessageDialog(this.gui.getFrame(), e.getMessage()
					.toString(), "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (fromMeta) {
			fromMeta();
		} else {
			fromLocalProject();
		}
	}

	private void fromMeta() {

		int X = parent.getX() + parent.getWidth() + 10 ;  
		int Y = parent.getY() + parent.getHeight() + 10 ;
		ThingInstance newIns = null ; 
		if (son instanceof ProcessEntry) {
			ProcessEntry process = (ProcessEntry) son;

			ProcessInstance addedProcessInstance = parent.getEntry()
					.getMyProject().addProcess(
							X,
							Y,
							parent.getEntry().getMyProject().getCurrentOpd()
									.getDrawingArea(), -1, -1);
			((OpmProcess) addedProcessInstance.getEntry().getLogicalEntity())
					.copyPropsFrom((OpmProcess) process.getLogicalEntity());
			Iterator iter = Collections.list(process.getInstances()).iterator();
			ProcessInstance instance = (ProcessInstance) iter.next();

			if (iter.hasNext()) {
				instance = (ProcessInstance) iter.next();
			}

			addedProcessInstance.setBackgroundColor(instance
					.getBackgroundColor());
			addedProcessInstance.setBorderColor(instance.getBorderColor());
			addedProcessInstance.setSize(instance.getWidth(), instance
					.getHeight());
			addedProcessInstance.setTextColor(instance.getTextColor());
			addedProcessInstance.setTextPosition(instance.getTextPosition());

			Role role = new Role(instance.getEntry().getLogicalEntity(), myRole.getOntology());
			((ProcessEntry) addedProcessInstance.getEntry()).addRole(role);

			addedProcessInstance.getOpd().getDrawingArea().repaint();
			newIns = addedProcessInstance ; 

		}
		if (son instanceof ObjectEntry) {
			ObjectEntry object = (ObjectEntry) son;

			ObjectInstance addedObjectInstance = parent.getEntry()
					.getMyProject().addObject(
							X,
							Y,
							parent.getEntry().getMyProject().getCurrentOpd()
									.getDrawingArea(), -1, -1, true);
			((OpmObject) addedObjectInstance.getEntry().getLogicalEntity())
					.copyPropsFrom((OpmObject) object.getLogicalEntity());			
			Iterator iter = Collections.list(object.getStateEnum()).iterator();
			while (iter.hasNext()) {
				OpmState state = (OpmState) iter.next();
				parent.getEntry().getMyProject().addState(state.getName(),
						addedObjectInstance);
			}

			Iterator objIter = Collections.list(object.getInstances())
					.iterator();

			ObjectInstance instance = (ObjectInstance) objIter.next();

			if (objIter.hasNext()) {
				instance = (ObjectInstance) objIter.next();
			}

			addedObjectInstance.setBackgroundColor(instance
					.getBackgroundColor());
			addedObjectInstance.setBorderColor(instance.getBorderColor());
			addedObjectInstance.setSize(instance.getWidth(), instance
					.getHeight());
			addedObjectInstance.setTextColor(instance.getTextColor());
			addedObjectInstance.setTextPosition(instance.getTextPosition());

			Role role = new Role(instance.getEntry().getLogicalEntity(), myRole.getOntology());
			((ObjectEntry) addedObjectInstance.getEntry()).addRole(role);
			addedObjectInstance.getOpd().getDrawingArea().repaint();
			newIns = addedObjectInstance ; 

		}
		
		RelativeConnectionPoint sourcePoint = new RelativeConnectionPoint(
				OpcatConstants.S_BORDER, 0.5);
		RelativeConnectionPoint targetPoint = new RelativeConnectionPoint(
				OpcatConstants.N_BORDER, 0.5);
		parent.getEntry().getMyProject().addRelation(parent.getThing(),
				sourcePoint, newIns.getThing(), targetPoint,
				relation.getRelationType(), parent.getOpd().getDrawingArea(),
				-1, -1);
		
		newIns.getThing().fitToContent() ; 
		parent.getOpd().addSelection(newIns, true);
		parent.getOpd().refit();		

	}

	private void fromLocalProject() {
		//OpdProject myProject = parent.getEntry().getMyProject();
		// add thingInstance as son to ti as parent with a new instance of
		// relation

		// check if son entry is in the project as it can come from metalib ?

		// /end check entry
		ThingInstance newIns;
		//if (myProject.getSystemStructure().getInstanceInOpd(parent.getOpd(),
		//		son.getId()) == null) {
			if (son instanceof ObjectEntry) {
				newIns = Opcat2.getCurrentProject().addObjectInstance(
						son.getName(), parent.getX() + parent.getWidth() + 10,
						parent.getY() + parent.getHeight() + 10, son);
			} else {
				// process
				newIns = Opcat2.getCurrentProject().addProcessInstance(
						son.getName(), parent.getX() + parent.getWidth() + 10,
						parent.getY() + parent.getHeight() + 10, son);
			}

		//} else {
		//	// if the menu was shown and there was an instance in the OPD
		//	// then the link is not present and it is safe to connect
		//	newIns = (ThingInstance) myProject.getSystemStructure()
		//			.getInstanceInOpd(parent.getOpd(), son.getId())
		//			.nextElement();
		//}
		RelativeConnectionPoint sourcePoint = new RelativeConnectionPoint(
				OpcatConstants.S_BORDER, 0.5);
		RelativeConnectionPoint targetPoint = new RelativeConnectionPoint(
				OpcatConstants.N_BORDER, 0.5);
		parent.getEntry().getMyProject().addRelation(parent.getThing(),
				sourcePoint, newIns.getThing(), targetPoint,
				relation.getRelationType(), parent.getOpd().getDrawingArea(),
				-1, -1);

		parent.getOpd().addSelection(newIns, true);
		parent.getEntry().getMyProject().setCanClose(false) ; 
		parent.getOpd().refit();
		parent.getEntry().getMyProject().setCanClose(false) ; 

	}
}
