package exportedAPI.opcatAPI;
/**
 * IRelationEntry - an interface to any of structural relations.
 * Use getRelationType() to know what is exectly the tha type.
 */

public interface IRelationEntry extends IEntry
{

	/**
	 * Returns type of relation as defined in <code>exportedAPI.OpcatConstants</code> class
	 * @return integer that represents the relation type.
	 */
	public int getRelationType();

	/**
	* Returns the ID of source IConnectionEdgeEntry that relation connected to.
	* @return the ID of source IConnectionEdgeEntry
	*/
	public long getSourceId();

	/**
	* Returns the ID of destination IConnectionEdgeEntry that relation connected to.
	* @return the ID of destination IConnectionEdgeEntry
	*/
	public long getDestinationId();

	/**
	* Returns String representing cardinality of the source IConnectionEdgeEntry.
	* @return cardinality of the source IConnectionEdgeEntry as String
	*/
	public String getSourceCardinality();

	/**
	* Returns String representing cardinality of the destination IConnectionEdgeEntry.
	* @return cardinality of the destination IConnectionEdgeEntry as String
	*/
	public String getDestinationCardinality();


	/**
	* Returns forward relation meaning of this OPM Relation.
	* @return forward relation meaning of this OPM Relation.
	*/
	public String getForwardRelationMeaning();


	/**
	* Returns backward relation meaning of this OPM Relation.
	* @return backward relation meaning of this OPM Relation.
	*/
	public String getBackwardRelationMeaning();

}