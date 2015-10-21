package exportedAPI.opcatAPIx;

/**
 * IXRelationEntry - an interface to any of structural relations.
 * Use getRelationType() to know what is exectly the tha type.
 */

public interface IXRelationEntry extends IXEntry
{

	/**
	 * Returns type of relation as defined in <code>exportedAPI.OpcatConstants</code> class
	 * @return integer that represents the relation type.
	 */
	public int getRelationType();

	/**
	* Returns the ID of source IXConnectionEdgeEntry that relation connected to.
	* @return the ID of source IXConnectionEdgeEntry
	*/
	public long getSourceId();


	/**
	* Returns the ID of destination IXConnectionEdgeEntry that relation connected to.
	* @return the ID of destination IXConnectionEdgeEntry
	*/
	public long getDestinationId();


	/**
	* Returns String representing cardinality of the source IXConnectionEdgeEntry.
	* @return cardinality of the source IXConnectionEdgeEntry as String
	*/
	public String getSourceCardinality();

	/**
	* Sets cardinality of the source IXConnectionEdgeEntry.
	* <strong>Importent</strong>: Legality of given string is not checked when using this method.
	* @param new source cardinality as string
	*/
	public void setSourceCardinality(String cardinality);

	/**
	* Returns String representing cardinality of the destination IXConnectionEdgeEntry.
	* @return cardinality of the destination IXConnectionEdgeEntry as String
	*/
	public String getDestinationCardinality();

	/**
	* Sets cardinality of the destination IXConnectionEdgeEntry.
	* <strong>Importent</strong>: Legality of given string is not checked when using this method.
	* @param new destination cardinality as string
	*/
	public void setDestinationCardinality(String cardinality);


	/**
	* Returns forward relation meaning of this OPM Relation.
	* @return forward relation meaning of this OPM Relation.
	*/
	public String getForwardRelationMeaning();

	/**
	* Sets forward relation meaning of this OPM Relation.
	* @param new forward relation meaning of this OPM Relation.
	*/
	public void setForwardRelationMeaning(String meaning);

	/**
	* Returns backward relation meaning of this OPM Relation.
	* @return backward relation meaning of this OPM Relation.
	*/
	public String getBackwardRelationMeaning();

	/**
	* Sets backward relation meaning of this OPM Relation.
	* @param new backward relation meaning of this OPM Relation.
	*/
	public void setBackwardRelationMeaning(String meaning);

}