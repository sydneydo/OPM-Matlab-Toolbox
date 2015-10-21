package exportedAPI.opcatAPIx;

/**
 * IXLinkEntry is an interface to logical representation of OPM links. It allows
 * retrieving and altering attribute values.
 */
public interface IXLinkEntry extends IXEntry {

	/**
	 * Returns link type as specified in OpcatConstants
	 * 
	 * @return link type
	 */
	public int getLinkType();

	/**
	 * Returns the ID of destination IXConnectionEdgeEntry that link connected
	 * to.
	 * 
	 * @return the ID of destination IXConnectionEdgeEntry
	 */

	public long getSourceId();

	/**
	 * Returns the ID of destination IXConnectionEdgeEntry that link connected
	 * to.
	 * 
	 * @return the ID of destination IXConnectionEdgeEntry
	 */
	public long getDestinationId();

	/**
	 * Returns String representing minimum reaction time of this IXLinkEntry.
	 * This String contains non-negative integer X 7 (msec, sec, min, hours,
	 * days, months, years) with semi-colons separation.
	 * 
	 * @return minimum link reaction type as string
	 */
	public String getMinReactionTime();

	/**
	 * Sets minimum reaction time of this IXLinkEntry. This String contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation. <strong>Important</strong>: Legality of
	 * given string is not checked when using this method.
	 * 
	 * @param minimum
	 *            link reaction type as string
	 */
	public void setMinReactionTime(String minimumReactionTime);

	/**
	 * Returns String representing maximum reaction time of this IXLinkEntry.
	 * This String contains non-negative integer X 7 (msec, sec, min, hours,
	 * days, months, years) with semi-colons separation.
	 * 
	 * @return maximum link reaction type as string
	 */
	public String getMaxReactionTime();

	/**
	 * Sets maximum reaction time of this IXLinkEntry. This String contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation. <strong>Importent</strong>: Legality of
	 * given string is not checked when using this method.
	 * 
	 * @param maximum
	 *            link reaction type as string
	 */
	public void setMaxReactionTime(String maximumReactionTime);

	/**
	 * Returns condition of this link
	 * 
	 * @return condition of this link
	 */
	public String getCondition();

	/**
	 * Sets condition of this link
	 * 
	 * @param new
	 *            value for condition
	 */
	public void setCondition(String condition);

	/**
	 * Returns path of this link
	 * 
	 * @return path of this link
	 */
	public String getPath();

	/**
	 * Sets path of this link
	 * 
	 * @param new
	 *            value for path
	 */
	public void setPath(String path);

}