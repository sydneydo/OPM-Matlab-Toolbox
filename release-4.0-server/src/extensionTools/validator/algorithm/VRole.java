package extensionTools.validator.algorithm;

/**
 * Represent a Validation Role which is used internally by the Validation
 * algorithm.
 * 
 * @author Eran Toch Created: 06/05/2004
 */
public class VRole {
	protected final long thingID;
	protected final long metaLibID;
	protected String thingType;

	/**
	 * A constructor method for a <code>VRole</code> object. Assignes the ID's.
	 * 
	 * @param _thingID
	 *            The ID of the thing in the meta-library
	 * @param _metaLibID
	 *            The ID of the meta-library
	 */
	public VRole(long _metaLibID, long _thingID, String _thingType) {
		this.thingID = _thingID;
		this.metaLibID = _metaLibID;
		this.thingType = _thingType;
	}

	/**
	 * Checks if both objects have the same thingID and metaLibID.
	 * 
	 * @param other
	 *            The other VRole to be compared.
	 * @return <code>true</code> if they are equal, <code>false</code>
	 *         otherwise.
	 */
	public boolean equals(VRole other) {
		if ((this.metaLibID == other.getMetaLibID())
				&& (this.thingID == other.getThingID())) {
			return true;
		}
		return false;
	}

	public boolean equals(Object obj) {
		try {
			VRole other = (VRole) obj;
			return this.equals(other);
		} catch (Exception E) {
			return false;
		}
	}

	/**
	 * Returns the ID of the meta-library of the role.
	 */
	public long getMetaLibID() {
		return this.metaLibID;
	}

	/**
	 * Returns the ID of the thing of the role (in the meta-library).
	 */
	public long getThingID() {
		return this.thingID;
	}

	public String toString() {
		return this.getMetaLibID() + "." + this.getThingID();
	}
}
