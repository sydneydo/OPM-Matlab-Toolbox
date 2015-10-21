package exportedAPI.opcatAPIx;
import java.util.Enumeration;

import exportedAPI.OpdKey;
import gui.metaLibraries.logic.Role;

/**
 * This interface is an interface to an entry in mainStructure. It provides method for getting information about any entry or altering entity's properties.
 * This is base interface for all Entries as you can see in class hiearachy.
 */


public interface IXEntry
{
    
    
	/**
	 * Returns the role of the thing
	 * @return String representing the role of the OPM Thing
	 */

	public String getRole();

	/**
	 * Returns the free text role string, without the meta-libraries roles.
	 * @return	String representing the role.
	 */
	public String getFreeTextRole();
	
	/**
	 * Sets the role of OPM Thing
	 * @param String representing new role of the OPM Thing
	 */
	public void setRole(String role);

	/**
	 * Returns an enumeration of ontology roles objects. Each object represent
	 * a role from an imported ontology.
	 * @return Enumeration Contains Role objects.
	 * @author Eran Toch
	 */
	public Enumeration<Role>  getRoles();
	
	public Enumeration<Role> getRoles(int type);

	
	/**
	 * Adds a role to the roles list of the OPM Thing.
	 * @param role	The role to be added
	 * @author Eran Toch
	 */
	public void addRole(IXRole role);
	
	/**
	 * Removes a role from the roles list. 
	 * @param role	The role to be removed.
	 */
	public void removeRole(IXRole role);
	
/**
* Returns the id of the entity.
*
* @return a long number represents id of the entity.
*/
	public long getId();

/**
* Returns the name of the entity.
*
* @return	a String represents entity name
*/
	public String getName();

/**
* Sets the string to be entity name.
*
* @param	name entity name
*/
	public void setName(String name);

/**
* Retrieves entity description.
*
* @return a String with the entity description
*/
	public String getDescription();

/**
* Sets the string to be entity description.
*
* @param description description of the entity
**/
	public void setDescription(String description);


/**
* Sets the environmental/systemic property of the Entity. If value of
* Environmental is true it's an environmental entity, otherwise systemic
*
* @param environmental if true thing is environmental otherwise systemic
*/
	public void setEnvironmental(boolean environmental);


/**
* Returns true if Entity is environmental. If it's systemic
* returns false
*
* @return true if environmental otherwise false
*/
	public boolean isEnvironmental();

/**
 * Compares current IXEntry with one given in <code>obj</code> parameter
 *
 * @param obj IXEntry to compare the current entry to
 */
	public boolean equals(Object obj);

/**
* Returns an enumeration of the IXInstances in this IXEntry.
* @use the Enumeration methods on the returned object to fetch the
* Instances sequentially.
* @see IXSystemStructure
*
* @return an enumeration of the IXInstances in this SystemStructure
*/
	public Enumeration getInstances();

/**
* Returns the IXInstance - an interface to one of the graphical representations
* according to given OpdKey
*
* @param key OpdKey - key of graphical instance of some entity.
* @return the IXInstance - an interface to one of the graphical representations
* according to given OpdKey
*/
	public IXInstance getIXInstance(OpdKey pKey);


/**
* Returns the number of graphical instances of this Entry's entity.
*
* @return number of graphical representations of IXEntry
*/
	public int getInstancesNumber();


/**
 * Checks if IXEntry has graphical representation in Opd specified by <code>opdNumber</code> parameter
 * @param opdNumber specifies an OPD to check graphical representation in
 * @return true if IXEntry has an graphical representation in specified OPD, otherwise false
 */
	public boolean hasInstanceInOpd(long opdNumber);

/**
 * When one of the properties of IXEntry is changed, it may affect graphical representations
 * of IXEntry, this method updates all <code>IXInstance</code>'s belongs to IXEntry.<br>
 * Call this method each time you altering IXEntry property
 */
	public void updateInstances();
}
