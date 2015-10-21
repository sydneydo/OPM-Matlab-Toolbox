package gui.opmEntities;

import exportedAPI.OpcatConstants;
import gui.metaLibraries.logic.RolesManager;

/**
 * This class represents Thing in OPM
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 */

public abstract class OpmThing extends OpmConnectionEdge {
	// ---------------------------------------------------------------------------
	// The private attributes/members are located here

	private String scope; // can be 0 - public,1 - protected ,2 - private

	private int numberOfInstances;

	/**
	 * Creates an OpmThing with specified id and name. Id of created OpmThing
	 * must be unique in OPCAT system
	 * 
	 * @param id
	 *            OpmThing id
	 * @param name
	 *            OpmThing name
	 */

	public OpmThing(long thingId, String thingName) {
		super(thingId, thingName);
		this.setPhysical(false);
		this.scope = OpcatConstants.PUBLIC;
		this.numberOfInstances = 1;
		this.role = "";
	}

	protected void copyPropsFrom(OpmThing origin) {
		super.copyPropsFrom(origin);
		this.setPhysical(origin.isPhysical());
		this.scope = origin.getScope();
		this.role = origin.getFreeTextRole();
		this.numberOfInstances = origin.getNumberOfInstances();

		// Copying the roles - using the roles manager
		// By Eran Toch
		this.rolesManager = (RolesManager) origin.getRolesManager().clone();

	}

	protected boolean hasSameProps(OpmThing pThing) {
		return (super.hasSameProps(pThing)
				&& (this.isPhysical() == pThing.isPhysical())
				&& this.scope.equals(pThing.getScope())
				&& this.role.equals(pThing.getRole())
				&& (this.numberOfInstances == pThing.getNumberOfInstances()) && (this
				.getRolesManager().equals(pThing.getRolesManager())));
	}


	/**
	 * Sets the scope of OpmThing.
	 * 
	 * @param scope
	 *            a String specifying the scope of the Thing
	 */

	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * Returns the scope of OpmThing.
	 * 
	 * @return a String containing thing's scope
	 */

	public String getScope() {
		return this.scope;
	}

	public int getNumberOfInstances() {
		return this.numberOfInstances;
	}

	public void setNumberOfInstances(int nOfIns) {
		this.numberOfInstances = nOfIns;
	}
	


}