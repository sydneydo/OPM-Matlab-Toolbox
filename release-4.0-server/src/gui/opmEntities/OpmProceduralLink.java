package gui.opmEntities;

/**
 * The Base Class for all procedural links existing in OPM. For better
 * understanding of this class you should be familiar with OPM.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 */

public abstract class OpmProceduralLink extends OpmEntity {
	// ---------------------------------------------------------------------------
	// The private attributes/members are located here
	private long sourceId;
	private long destinationId;
	private String minReactionTime;
	private String maxReactionTime;
	private String condition;
	private String path;
	private String description;

	/**
	 * Creates an OpmProceduralLink with specified id and name. Id of created
	 * OpmProceduralLink must be unique in OPCAT system
	 * 
	 * @param id
	 *            OpmProceduralLink id
	 * @param name
	 *            OpmProceduralLink name
	 */

	public OpmProceduralLink(long linkId, String linkName, long sourceId,
			long destinationId) {
		super(linkId, linkName);
		this.maxReactionTime = "infinity";
		this.minReactionTime = "0;0;0;0;0;0;0";
		this.condition = "";
		this.path = "";
		this.sourceId = sourceId;
		this.destinationId = destinationId;
		this.description = "";
	}

	public void copyPropsFrom(OpmProceduralLink origin) {
		super.copyPropsFrom(origin);
		this.maxReactionTime = origin.getMaxReactionTime();
		this.minReactionTime = origin.getMinReactionTime();
		this.condition = origin.getCondition();
		this.path = origin.getPath();
	}

	/**
	 * Returns the source id of this OpmProceduralLink.
	 * 
	 */

	public long getSourceId() {
		return this.sourceId;
	}

	/**
	 * Returns the destination id of this OpmProceduralLink.
	 * 
	 */
	public long getDestinationId() {
		return this.destinationId;
	}

	/**
	 * Returns String representing minimum reaction time of this
	 * OpmProceduralLink. This String contains non-negative integer X 7 (msec,
	 * sec, min, hours, days, months, years) with semi-colons separation.
	 * 
	 */
	public String getMinReactionTime() {
		return this.minReactionTime;
	}

	/**
	 * Sets minimum reaction time of this OpmProceduralLink. This field contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation.
	 * 
	 */
	public void setMinReactionTime(String minimumReactionTime) {
		this.minReactionTime = minimumReactionTime;
	}

	/**
	 * Returns String representing maximum reaction time of this
	 * OpmProceduralLink. This String contains non-negative integer X 7 (msec,
	 * sec, min, hours, days, months, years) with semi-colons separation.
	 * 
	 */
	public String getMaxReactionTime() {
		return this.maxReactionTime;
	}

	/**
	 * Sets maximum reaction time of this OpmProceduralLink. This field contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation.
	 * 
	 */
	public void setMaxReactionTime(String maximumReactionTime) {
		this.maxReactionTime = maximumReactionTime;
	}

	/**
	 * Returns condition of this OpmProceduralLink
	 * 
	 */
	public String getCondition() {
		return this.condition;
	}

	/**
	 * Sets condition of this OpmProceduralLink
	 * 
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * Returns path of this OpmProceduralLink
	 * 
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Sets path of this OpmProceduralLinkgetPath
	 * 
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		if (description.equalsIgnoreCase("none")) {
			return "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}