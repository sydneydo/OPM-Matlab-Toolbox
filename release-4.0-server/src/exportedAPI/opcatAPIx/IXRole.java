package exportedAPI.opcatAPIx;

/**
 * Represent a Role - an OPM Thing from an imported ontology, which can
 * be played by an OPM Thing in the local model.
 * @author Eran Toch
 * Created: 01/05/2004
 */
public interface IXRole {
	/**
	 * Defines a new role, using a given 
	 * @param _thingID	The ID of the thing (in the original meta-library)
	 * @param _libID	The ID of the meta-library.
	 */
	public void define(long _thingID, long _libID);
	
	/**
	 * Returns the name of the role's thing in the meta-library.
	 */
	public String getThingName();
	
	/**
	 * Returns the name of the meta-library of the role.
	 */
	public String getLibraryName();
	
	/**
	 * Returns the role's thing ID.
	 */
	public long getThingId();
	
	/**
	 * Retruns the role's library ID.
	 */
	public long getLibraryId();
	
}
