package gui.projectStructure;

import exportedAPI.OpdKey;
import exportedAPI.opcatAPI.IProcessInstance;
import exportedAPI.opcatAPIx.IXProcessInstance;
import gui.opdGraphics.opdBaseComponents.OpdProcess;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.OpdProject;

import java.awt.Container;

/**
 * <p>
 * This class represents instance of OPM process.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class ProcessInstance extends ThingInstance implements IProcessInstance,
		IXProcessInstance {
	/**
	 * Creates ProcessInstance with specified myKey, which holds pThing that
	 * represents this ProcessInstance graphically.
	 * 
	 * @param key
	 *            OpdKey - key of graphical instance of this entity.
	 * @param pThing
	 *            OpdThing that represents this ProcessInstance graphically.
	 */

	public ProcessInstance(ProcessEntry myEntry, OpdKey myKey,
			Container container, OpdProject project) {
		super(myKey, project);
		this.myEntry = myEntry;

		this.myConnectionEdge = new OpdProcess(myEntry, this.myProject, myKey
				.getOpdId(), myKey.getEntityInOpdId());

		if (container instanceof OpdThing) {
			this.setParent((OpdThing) container);
		}

		this.myConnectionEdge.addMouseListener(this.myConnectionEdge);
		this.myConnectionEdge.addMouseMotionListener(this.myConnectionEdge);
		/*
		 * TODO: as i get it there is a need to add the key listner to all
		 * things and not only the drawingarea. Ask eran about this. as i don't
		 * know the order in which events are handled. Raanan.
		 */
		// myConnectionEdge.addKeyListener(myConnectionEdge);
		this.graphicalRepresentation = myConnectionEdge;
	}

	public void copyPropsFrom(ProcessInstance origin) {
		super.copyPropsFrom(origin);
	}

	public String getTypeString() {
		return "Process";
	}
}
