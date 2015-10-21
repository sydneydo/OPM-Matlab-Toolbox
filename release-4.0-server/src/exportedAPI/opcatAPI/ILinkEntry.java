package exportedAPI.opcatAPI;

/**
 * ILinkEntry is an interface to logical representation of OPM links.
 * It allows retrieving and altering attribute values.
 */

public interface ILinkEntry extends IEntry
{

	/**
	 * Returns link type as specified in OpcatConstants
	 * @return link type
	 */
	public int getLinkType();

	/**
	* Returns the ID of source IConnectionEdgeEntry that link connected to.
	* @return the ID of source IConnectionEdgeEntry
	*/
	public long getSourceId();

	/**
	* Returns the ID of destination IConnectionEdgeEntry that link connected to.
	* @return the ID of destination IConnectionEdgeEntry
	*/
	public long getDestinationId();

	/**
	* Returns String representing minimum reaction time
	* of this ILinkEntry. This String contains non-negative integer X 7
	* (msec, sec, min, hours, days, months, years) with semi-colons
	* separation.
	* @return minimum link reaction type as string
	*/
	public String getMinReactionTime();

	/**
	* Returns String representing maximum reaction time
	* of this ILinkEntry. This String contains non-negative integer X 7
	* (msec, sec, min, hours, days, months, years) with semi-colons
	* separation.
	* @return maximum link reaction type as string
	*/
	public String getMaxReactionTime();

	/**
	* Returns condition of this link
	* @return condition of this link
	*/
	public String getCondition();

	/**
	* Returns path of this link
	* @return path of this link
	*/
	public String getPath();

}