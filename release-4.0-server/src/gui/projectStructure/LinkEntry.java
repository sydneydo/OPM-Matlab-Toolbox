package gui.projectStructure;

import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPIx.IXLinkEntry;
import gui.opdProject.OpdProject;
import gui.opmEntities.Constants;
import gui.opmEntities.OpmProceduralLink;

import java.util.Enumeration;

/**
 * <p>
 * This class represents entry of OPM procedural link.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class LinkEntry extends Entry implements IXLinkEntry, ILinkEntry {

	/**
	 * Creates LinkEntry that holds all information about specified pLink.
	 * 
	 * @param pLink
	 *            object of OpmProceduralLink class.
	 */

	private double resourceConsumption = 0;

	private boolean resourceConsumptionAccumolated = false;

	public LinkEntry(OpmProceduralLink pLink, OpdProject project) {
		super(pLink, project);
	}

	public void updateInstances() {
		for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
			LinkInstance tempInstance = (LinkInstance) (e.nextElement());
			tempInstance.update();
		}
	}

	public int getLinkType() {
		return Constants.getType4Link((OpmProceduralLink) this.logicalEntity);
	}

	/**
	 * Returns the source id of this OpmProceduralLink.
	 * 
	 */
	public long getSourceId() {
		return ((OpmProceduralLink) this.logicalEntity).getSourceId();
	}

	/**
	 * Returns the destination id of this OpmProceduralLink.
	 * 
	 */
	public long getDestinationId() {
		return ((OpmProceduralLink) this.logicalEntity).getDestinationId();
	}

	/**
	 * Returns String representing minimum reaction time of this
	 * OpmProceduralLink. This String contains non-negative integer X 7 (msec,
	 * sec, min, hours, days, months, years) with semi-colons separation.
	 * 
	 */
	public String getMinReactionTime() {
		return ((OpmProceduralLink) this.logicalEntity).getMinReactionTime();
	}

	/**
	 * Sets minimum reaction time of this OpmProceduralLink. This field contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation.
	 * 
	 */
	public void setMinReactionTime(String minimumReactionTime) {
		((OpmProceduralLink) this.logicalEntity)
				.setMinReactionTime(minimumReactionTime);
	}

	/**
	 * Returns String representing maximum reaction time of this
	 * OpmProceduralLink. This String contains non-negative integer X 7 (msec,
	 * sec, min, hours, days, months, years) with semi-colons separation.
	 * 
	 */
	public String getMaxReactionTime() {
		return ((OpmProceduralLink) this.logicalEntity).getMaxReactionTime();
	}

	/**
	 * Sets maximum reaction time of this OpmProceduralLink. This field contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation.
	 * 
	 */
	public void setMaxReactionTime(String maximumReactionTime) {
		((OpmProceduralLink) this.logicalEntity)
				.setMaxReactionTime(maximumReactionTime);
	}

	/**
	 * Returns condition of this OpmProceduralLink
	 * 
	 */
	public String getCondition() {
		return ((OpmProceduralLink) this.logicalEntity).getCondition();
	}

	/**
	 * Sets condition of this OpmProceduralLink
	 * 
	 */
	public void setCondition(String condition) {
		((OpmProceduralLink) this.logicalEntity).setCondition(condition);
	}

	/**
	 * Returns path of this OpmProceduralLink
	 * 
	 */
	public String getPath() {
		return ((OpmProceduralLink) this.logicalEntity).getPath();
	}

	/**
	 * Sets path of this OpmProceduralLink
	 * 
	 */
	public void setPath(String path) {
		((OpmProceduralLink) this.logicalEntity).setPath(path);
	}

	/**
	 * Returns description of this OpmProceduralLink
	 * 
	 */
	public String getDescription() {
		return ((OpmProceduralLink) this.logicalEntity).getDescription();
	}

	/**
	 * Sets path of this OpmProceduralLink
	 * 
	 */
	public void setDescription(String path) {
		((OpmProceduralLink) this.logicalEntity).setDescription(path);
	}

	public double getResourceConsumption() {
		return resourceConsumption;
	}

	public void setResourceConsumption(double resourceConsumption) {
		this.resourceConsumption = resourceConsumption;
	}

	public boolean isResourceConsumptionAccumolated() {
		return resourceConsumptionAccumolated;
	}

	public void setResourceConsumptionAccumolated(
			boolean resourceConsumptionAccumolated) {
		this.resourceConsumptionAccumolated = resourceConsumptionAccumolated;
	}

	public String getTypeString() {
		return "Link";
	}

}
