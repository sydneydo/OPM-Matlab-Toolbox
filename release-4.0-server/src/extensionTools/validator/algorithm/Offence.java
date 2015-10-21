package extensionTools.validator.algorithm;

import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;

//import exportedAPI.opcatAPI.IConnectionEdgeEntry;
/**
 * A validation offence which was found 
 * @author Eran Toch
 * Created: 06/05/2004
 */
public class Offence {
	private Law law;
	private ModelVRole role;
	
	public Offence(ModelVRole _role, Law _law)	{
		this.law =_law;
		this.role = _role;
	}
	
	/**
	 * @return
	 */
	public Law getLaw() {
		return this.law;
	}

	/**
	 * @return
	 */
	public VRole getRole() {
		return this.role;
	}

	/**
	 * @param law
	 */
	public void setLaw(Law law) {
		this.law = law;
	}
	
	/**
	 * Returns the name of the original thing the role was set to.
	 */
	public String getOriginalThingName()	{
		return this.role.getThingName();
	}
	
	/**
	 * Returns the IConnectionEdgeEntry of the original thing the role was set to.
	 */
	public IConnectionEdgeEntry getOriginalThing()	{
		return this.role.getOriginalThing();
	}	
	
	/**
	 * @param role
	 */
	public void setRole(ModelVRole role) {
		this.role = role;
	}
	
	/**
	 * Renders the offence warning text - specific warnings should be implemented
	 * by descendent classes.
	 * @param structure	The structure of the model - is used to render the names
	 * of the things.
	 * @return	Warning text.
	 */
	public String renderOffenceText(ISystemStructure structure)	{
		return "No specific offence was created";
	}
	
	public String toString()	{
		return this.role + "["+ this.law +"]";
	}

}
