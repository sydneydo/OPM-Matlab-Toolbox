package exportedAPI.opcatAPI;

import java.util.Enumeration;

import exportedAPI.OpdKey;

/**
 * This interface is an interface to an entry in mainStructure. It provides
 * method for getting information about any entry. This is base interface for
 * all Entries as you can see in class hierarchy.
 */

public interface IEntry {
    
    
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
	 * Returns an enumeration of ontology roles objects. Each object represent
	 * a role from an imported ontology.
	 * @return Enumeration Contains Role objects.
	 * @author Eran Toch
	 */
	public Enumeration getRoles();
	
	public Enumeration getRoles(int type);
	
	public String getRolesString(int type);	    

    /**
         * Returns the id of the entity.
         * 
         * @return a long number represents id of the entity.
         */
    public long getId();

    /**
         * Returns the name of the entity.
         * 
         * @return a String represents entity name
         */
    public String getName();

    /**
         * Returns entity description.
         * 
         * @return a String with the entity description
         */
    public String getDescription();

    /**
         * Returns true if Entity is environmental. If it's system returns
         * false
         * 
         * @return true if environmental otherwise false
         */
    public boolean isEnvironmental();

    /**
         * Compares current IEntry with one given in <code>obj</code>
         * parameter
         * 
         * @param obj
         *                IEntry to compare the current entry to
         */
    public boolean equals(Object obj);

    /**
         * Returns an enumeration of the IInstances in this IEntry.
         * 
         * @use the Enumeration methods on the returned object to fetch the
         *      Instances sequentially.
         * @see ISystemStructure
         * 
         * @return an enumeration of the IInstances in this SystemStructure
         */
    public Enumeration getInstances();

    /**
         * Returns the IInstance - an interface to one of the graphical
         * representations according to given OpdKey
         * 
         * @param key
         *                OpdKey - key of graphical instance of some entity.
         * @return the IInstance - an interface to one of the graphical
         *         representations according to given OpdKey
         */
    public IInstance getIInstance(OpdKey pKey);

    /**
         * Returns the number of graphical instances of this Entry's entity.
         * 
         * @return number of graphical representations of IXEntry
         */
    public int getInstancesNumber();

    /**
         * Checks if IXEntry has graphical representation in Opd specified by
         * <code>opdNumber</code> parameter
         * 
         * @param opdNumber
         *                specifies an OPD to check graphical representation in
         * @return true if IXEntry has an graphical representation in specified
         *         OPD, otherwise false
         */
    public boolean hasInstanceInOpd(long opdNumber);

    /**
         * returns a textual representation of this Entry type
         * 
         * @return
         */
    public String getTypeString();
}
