package exportedAPI.opcatAPIx;


/**
 * IXThingEntry is an interface to logical representation of OPM Thing.
 */

public interface IXThingEntry extends IXConnectionEdgeEntry
{

	/**
	* Sets the physical/informational property of OpmThing. If value of
	* physical is true it's a physical thing, otherwise informational.
	* @param physical if true thing is phisical otherwise informational.
	*
	*/
	public void setPhysical(boolean physical);

	/**
	* Checks if thing is physical
	* @return true if thing is physical, false if informational
	*/
	public boolean isPhysical();

	/**
	* Sets the scope of OPM Thing. The scope can be one of constant values specified
    * in interface OpcatConstants.
	* @param scope a String specifying the scope of the Thing
	*/
	public void setScope(String scope);


	/**
    * Returns the scope of OpmThing.The scope can be one of constant values specified
    * in interface OpcatConstants.
	* @return  a String containing thing's scope
	*/
	public String getScope();

	
	/**
	 * Returns the number of MAS instances of the thing
	 * @return int specifying number of MAS instances of the OPM Thing
	 */
	public int getNumberOfMASInstances();

	/**
	 * Sets the number of MAS instances of the thing
	 * @param int specifying number of MAS instances of the OPM Thing
	 */
	public void setNumberOfMASInstances(int numOfInstances);

	/**
	   * Returns the OPD that zooming in this ThingEntry. null returned if there is no
	   * such OPD.
	   *
	   */
	public IXOpd getZoomedInIXOpd();
	
    /**
     * returns the Thing URL
     * @return
     */
    public String getUrl() ;
    
    /**
     * Sets the Thing URL
     * @return
     */
    public void setUrl(String url) ;	
}
