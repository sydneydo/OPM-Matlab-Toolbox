package exportedAPI.opcatAPI;

/**
 * IThingEntry is an interface to logical representation of OPM Thing
 */

 public interface IThingEntry extends IConnectionEdgeEntry
{

	/**
	* Checks if thing is phisical
	* @return true if thing is phisical, false if informational
	*/
	public boolean isPhysical();

	/**
    * Returns the scope of OpmThing. The scope can be one of constant values
    * specified in interface OpcatConstants.
	*
	* @return  a String containing thing's scope
	*/
	public String getScope();



	/**
	 * Returns the number of MAS instances of the thing
	 * @return int specifying number of MAS instances of the OPM Thing
	 */
	public int getNumberOfMASInstances();

	  /**
	   * Returns the OPD that zooming in this ThingEntry. null returned if there is no
	   * such OPD.
	   *
	   */
	  public IOpd getZoomedInIOpd();
	  

	    /**
	     * returns the Thing URL
	     * @return
	     */
	    public String getUrl() ;	  
}
