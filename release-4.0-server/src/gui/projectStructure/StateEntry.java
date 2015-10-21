package gui.projectStructure;

import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IStateEntry;
import exportedAPI.opcatAPIx.IXObjectEntry;
import exportedAPI.opcatAPIx.IXStateEntry;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmState;

import java.util.Enumeration;

public class StateEntry extends ConnectionEdgeEntry implements IXStateEntry,
		IStateEntry {

	private OpmObject parentObject;

	public StateEntry(OpmState pState, OpmObject pObject, OpdProject project) {
		super(pState, project);
		this.parentObject = pObject;
	}

	public ObjectEntry getParentObject() {
		return (ObjectEntry) this.myProject.getComponentsStructure().getEntry(
				this.parentObject.getId());
	}

	public IObjectEntry getParentIObjectEntry() {
		return this.getParentObject();
	}

	public IXObjectEntry getParentIXObjectEntry() {
		return this.getParentObject();
	}

	public long getParentObjectId() {
		return this.parentObject.getId();
	}

	public boolean isRelated() {
		for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
			StateInstance si = (StateInstance) e.nextElement();
			if (si.isRelated()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns String representing minimum activation time of this OpmState.
	 * This String contains non-negative integer X 7 (msec, sec, min, hours,
	 * days, months, years) with semi-colons separation.
	 * 
	 */
	public String getMinTime() {
		return ((OpmState) this.logicalEntity).getMinTime();
	}

	/**
	 * Sets minimum activation time of this OpmState. This field contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation.
	 * 
	 */
	public void setMinTime(String minTime) {
		((OpmState) this.logicalEntity).setMinTime(minTime);
	}

	/**
	 * Returns String representing maximum activation time of this OpmState.
	 * This String contains non-negative integer X 7 (msec, sec, min, hours,
	 * days, months, years) with semi-colons separation.
	 * 
	 */
	public String getMaxTime() {
		return ((OpmState) this.logicalEntity).getMaxTime();
	}

	/**
	 * Sets maximum activation time of this OpmState. This field contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation.
	 * 
	 */
	public void setMaxTime(String maxTime) {
		((OpmState) this.logicalEntity).setMaxTime(maxTime);
	}

	/**
	 * Sets the initialState property of OpmState.
	 * 
	 */
	public void setInitial(boolean initialState) {
		if (initialState) {
			/**
			 * turn off all other states
			 */
			if (this.getParentObject() != null) {
				Enumeration<StateEntry> states = this.getParentObject()
						.getStates();
				while (states.hasMoreElements()) {
					StateEntry state = states.nextElement();
					if (state.getId() != this.getId()) {
						state.setInitial(false);
					}
				}
			}
		}

		((OpmState) this.logicalEntity).setInitial(initialState);
	}

	/**
	 * Returns true if this State is initial.
	 * 
	 */
	public boolean isInitial() {
		return ((OpmState) this.logicalEntity).isInitial();
	}

	/**
	 * Sets the finalState property of OpmState.
	 * 
	 */
	public void setFinal(boolean finalState) {
		if (finalState) {
			/**
			 * turn off all other states
			 */
			if (this.getParentObject() != null) {
				Enumeration<StateEntry> states = this.getParentObject()
						.getStates();
				while (states.hasMoreElements()) {
					StateEntry state = states.nextElement();
					if (state.getId() != this.getId()) {
						state.setFinal(false);
					}
				}
			}
		}
		((OpmState) this.logicalEntity).setFinal(finalState);
	}

	/**
	 * Returns true if this State is final.
	 * 
	 */
	public boolean isFinal() {
		return ((OpmState) this.logicalEntity).isFinal();
	}

	public boolean isDefault() {
		return ((OpmState) this.logicalEntity).isDefault();
	}

	public void setDefault(boolean defaultState) {
		if (defaultState) {
			/**
			 * turn off all other states
			 */
			if (this.getParentObject() != null) {
				Enumeration<StateEntry> states = this.getParentObject()
						.getStates();
				while (states.hasMoreElements()) {
					StateEntry state = states.nextElement();
					if (state.getId() != this.getId()) {
						state.setDefault(false);
					}
				}
			}
		}
		((OpmState) this.logicalEntity).setDefault(defaultState);
	}

	public boolean hasVisibleInstancesInOpd(long opdNumber) {
		for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
			Instance currInstance = (Instance) e.nextElement();
			if ((currInstance.getKey().getOpdId() == opdNumber)
					&& (currInstance.isVisible())) {
				return true;
			}
		}

		return false;

	}

	public String getTypeString() {
		return "State";
	}

	public static String getTypeString_() {
		return "State";
	}
}