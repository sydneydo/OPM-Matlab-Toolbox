/*
 * Created on 01/05/2004
 */
package exportedAPI.opcatAPI;

/**
 * Represent a Role - an OPM Thing from an imported ontology, which can
 * be played by an OPM Thing in the local model.
 * @author Eran Toch
 * Created: 01/05/2004
 */
public interface IRole {
	/**
	 * Returns the name of the role's thing in the meta-library.
	 * @return
	 */
	public String getThingName();
	
	/**
	 * Returns the name of the meta-library of the role.
	 * @return
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
