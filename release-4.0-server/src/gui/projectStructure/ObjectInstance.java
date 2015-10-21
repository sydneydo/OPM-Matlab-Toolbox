package gui.projectStructure;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPI.IObjectInstance;
import exportedAPI.opcatAPIx.IXObjectInstance;
import gui.Opcat2;
import gui.opdGraphics.opdBaseComponents.OpdBaseComponent;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdObject;
import gui.opdGraphics.opdBaseComponents.OpdState;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmState;

import java.awt.Component;
import java.awt.Container;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * <p>
 * This class represents instance of OPM object. In this class we additionally
 * hold information about all states instance belonging to this object's
 * instance .
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class ObjectInstance extends ThingInstance implements IObjectInstance,
		IXObjectInstance {
	Hashtable stateInstances;

	/**
	 * Creates ThingInstance with specified myKey, which holds pThing that
	 * represents this ObjectInstance graphically.
	 * 
	 * @param key
	 *            OpdKey - key of graphical instance of this entity.
	 * @param pThing
	 *            OpdThing that represents this ObjectInstance graphically.
	 */
	public ObjectInstance(ObjectEntry myEntry, OpdKey myKey,
			Container container, OpdProject project, boolean bringStates) {
		super(myKey, project);
		this.myEntry = myEntry;
		this.stateInstances = new Hashtable();

		if (myEntry.getLogicalEntity() instanceof OpmObject) {
			this.myConnectionEdge = new OpdObject(myEntry, this.myProject,
					myKey.getOpdId(), myKey.getEntityInOpdId());
		}

		if (container instanceof OpdThing) {
			this.setParent((OpdThing) container);
		}

		this.myConnectionEdge.addMouseListener(this.myConnectionEdge);
		this.myConnectionEdge.addMouseMotionListener(this.myConnectionEdge);
		// container.add(myConnectionEdge);
		// container.repaint();

		if (bringStates) {

			for (Enumeration e = myEntry.getStateEnum(); e.hasMoreElements();) {
				OpmState lState = (OpmState) e.nextElement();
				Opd tmpOpd = null;
				if (this.myConnectionEdge.getOpdId() == OpdProject.CLIPBOARD_ID) {
					tmpOpd = this.myProject.getClipBoard();
				} else {
					tmpOpd = this.myProject.getComponentsStructure().getOpd(
							this.myConnectionEdge.getOpdId());
				}
				long stateInOpdId = tmpOpd._getFreeEntityInOpdEntry();
				OpdKey stateKey = new OpdKey(tmpOpd.getOpdId(), stateInOpdId);
				StateEntry sEntry = (StateEntry) (this.myProject
						.getComponentsStructure().getEntry(lState.getId()));
				StateInstance tmpStateInstance = new StateInstance(sEntry,
						stateKey, (OpdObject) this.myConnectionEdge,
						this.myProject);
				sEntry.addInstance(stateKey, tmpStateInstance);
			}

		}

		this.graphicalRepresentation = myConnectionEdge;

	}

	public StateInstance getAnimatedState() {
		Enumeration<StateInstance> states = this.getStateInstances();
		while (states.hasMoreElements()) {
			StateInstance state = states.nextElement();
			if (state.isAnimated()) {
				return state;
			}
		}
		return null;
	}

	public boolean isRelated() {
		if (this.myConnectionEdge.isRelated()) {
			return true;
		}

		Component components[] = this.myConnectionEdge.getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof OpdBaseComponent) {
				if (((OpdBaseComponent) components[i]).isRelated()) {
					return true;
				}
			}
		}

		return false;

	}

	public Enumeration getStateInstances() {
		Vector instances = new Vector();
		MainStructure ms = this.myProject.getComponentsStructure();

		Component components[] = this.myConnectionEdge.getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof OpdState) {
				OpdState tempState = (OpdState) components[i];
				Entry stEntry = ms.getEntry(tempState.getEntity().getId());
				Instance stInstance = stEntry.getInstance(new OpdKey(tempState
						.getOpdId(), tempState.getEntityInOpdId()));
				instances.add(stInstance);
			}
		}

		return instances.elements();

	}

	public void copyPropsFrom(ObjectInstance origin) {
		super.copyPropsFrom(origin);
		this.setStatesAutoArranged(origin.isStatesAutoArranged());

	}

	public void update() {
		super.update();
	}

	public boolean isStatesAutoArranged() {
		return ((OpdObject) this.myConnectionEdge).isStatesAutoarrange();
	}

	public void setStatesAutoArranged(boolean isArranged) {
		((OpdObject) this.myConnectionEdge).setStatesAutoarrange(isArranged);
	}

	public boolean isStateInstaceExists(String stateName) {

		Iterator iter = Collections.list(this.getStateInstances()).iterator();
		while (iter.hasNext()) {
			StateInstance state = (StateInstance) iter.next();
			if (state.getEntry().getName().equalsIgnoreCase(stateName)) {
				return true;
			}
		}

		return false;
	}

	public StateInstance getState(String stateName) {

		Iterator iter = Collections.list(this.getStateInstances()).iterator();
		while (iter.hasNext()) {
			StateInstance state = (StateInstance) iter.next();
			if (state.getEntry().getName().equalsIgnoreCase(stateName)) {
				return state;
			}
		}

		return null;
	}

	public void showMeasurementUnit() {

		int ret = JOptionPane
				.showOptionDialog(
						Opcat2.getFrame(),
						"In Diagram Measurement Unit is not connected to the "
								+ "\n"
								+ "Properties dialog, and is for viewing only. Continue ?",
						"Opcat2 - Warning", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (ret != JOptionPane.YES_OPTION)
			return;

		OpmObject opm = (OpmObject) myEntry.getLogicalEntity();
		ObjectEntry entry = (ObjectEntry) myEntry;
		// get states and see if there is one named value.
		if (!(opm.isMesurementUnitExists())) {
			JOptionPane.showMessageDialog(Opcat2.getFrame(),
					"Measurement Unit not set", "Opcat2 - Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		/**
		 * add measurmentunit here
		 */
		Container cont = this.getParent();
		if (cont == null)
			cont = myProject.getCurrentOpd().getDrawingArea();

		// firs create the value as a state
		StateEntry stateEntry = entry.getState(String.valueOf(opm
				.getMesurementUnitInitialValue()));
		if (stateEntry == null) {
			StateInstance state = addState(this);
			state.getEntry().setName(
					Double.toString(opm.getMesurementUnitInitialValue()));
			this.getThing().fitToContent();
		} else {
			JOptionPane.showMessageDialog(Opcat2.getFrame(),
					"State Value is active, not added", "Opcat2 - Warning",
					JOptionPane.INFORMATION_MESSAGE);
		}
		// end state

		// now create the mes unit object
		HashMap<Entry, FundamentalRelationEntry> sons = ((ObjectEntry) this
				.getEntry())
				.getFundemantulRelationSons(OpcatConstants.EXHIBITION_RELATION);
		Iterator<Entry> sonsIter = sons.keySet().iterator();
		Entry mesUnitEntry = null;
		while (sonsIter.hasNext()) {
			ObjectEntry son = (ObjectEntry) sonsIter.next();
			if (son.getName().equalsIgnoreCase("Measurement Unit")) {
				mesUnitEntry = son;
				break;
			}
		}
		if (mesUnitEntry == null) {
			ObjectInstance measurment = myProject.addObject(this.getX() + 150,
					this.getY() + 150, cont, -1, -1, false);
			measurment.getEntry().setName("Measurement Unit");
			StateInstance state = addState(measurment);
			state.getEntry().setName(opm.getMesurementUnit());

			measurment.getThing().fitToContent();
			RelativeConnectionPoint sourcePoint = new RelativeConnectionPoint(
					OpcatConstants.S_BORDER, 0.5);
			RelativeConnectionPoint targetPoint = new RelativeConnectionPoint(
					OpcatConstants.N_BORDER, 0.5);

			myProject.addRelation((OpdConnectionEdge) this.getThing(),
					sourcePoint, (OpdConnectionEdge) measurment.getThing(),
					targetPoint, OpcatConstants.EXHIBITION_RELATION, this
							.getThing().getParent(), -1, -1);

		} else {
			JOptionPane.showMessageDialog(Opcat2.getFrame(),
					"Measurement Unit active, not added or changed",
					"Opcat2 - Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		myProject.getCurrentOpd().refit();
	}

	private StateInstance addState(ObjectInstance toInstance) {
		StateInstance retVal = null;
		OpmState sampleOpmState = new OpmState(0, "");

		StateEntry myEntry = myProject.addState((OpdObject) toInstance
				.getThing());

		OpmState nState = (OpmState) myEntry.getLogicalEntity();
		nState.copyPropsFrom(sampleOpmState);
		/**
		 * make sure only one is initial or final ;
		 */
		myEntry.setInitial(myEntry.isInitial());
		myEntry.setFinal(myEntry.isFinal());
		myEntry.setDefault(myEntry.isDefault());

		for (Enumeration en = toInstance.getEntry().getInstances(); en
				.hasMoreElements();) {
			ObjectInstance parentInstance = (ObjectInstance) en.nextElement();
			OpdObject pObj = (OpdObject) parentInstance.getThing();

			pObj.repaint();
			StateInstance stateInstance = ((ObjectInstance) pObj.getInstance())
					.getState(nState.getName());
			if (!pObj.getInstance().getKey().toString().equalsIgnoreCase(
					toInstance.getThing().getInstance().getKey().toString())) {
				stateInstance.getGraphicalRepresentation().setVisible(false);
				pObj.repaint();
			} else {
				retVal = stateInstance;
			}

		}
		return retVal;
	}

	public String getTypeString() {
		return "Object";
	}
}