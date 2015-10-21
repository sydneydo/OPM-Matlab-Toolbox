package gui.projectStructure;

import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPIx.IXProcessEntry;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmProcess;

/**
 * <p>
 * This class represents entry of OPM process.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class ProcessEntry extends ThingEntry implements IXProcessEntry,
		IProcessEntry {

	/**
	 * Creates ProcessEntry that holds all information about specified pProcess.
	 * 
	 * @param pProcess
	 *            object of OpmProcess class.
	 */

	public ProcessEntry(OpmProcess pProcess, OpdProject project) {
		super(pProcess, project);
	}

	public ProcessEntry(OpmProcess pProcess, ThingEntry parentThing,
			OpdProject project) {
		super(pProcess, parentThing, project);
	}

	/**
	 * Returns process body of this OpmProcess
	 * 
	 */
	public String getProcessBody() {
		return ((OpmProcess) this.logicalEntity).getProcessBody();
	}

	/**
	 * Sets process body of this OpmProcess
	 * 
	 */
	public void setProcessBody(String processBody) {
		((OpmProcess) this.logicalEntity).setProcessBody(processBody);
	}

	/**
	 * Returns String representing maximum activation time of this OpmProcess.
	 * This String contains non-negative integer X 7 (msec, sec, min, hours,
	 * days, months, years) with semi-colons separation.
	 * 
	 */

	public String getMaxTimeActivation() {
		return ((OpmProcess) this.logicalEntity).getMaxTimeActivation();
	}

	/**
	 * Sets maximum activation time of this OpmProcess. This field contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation.
	 * 
	 */

	public void setMaxTimeActivation(String time) {
		((OpmProcess) this.logicalEntity).setMaxTimeActivation(time);
	}

	/**
	 * Returns String representing minimum activation time of this OpmProcess.
	 * This String contains non-negative integer X 7 (msec, sec, min, hours,
	 * days, months, years) with semi-colons separation.
	 * 
	 */

	public String getMinTimeActivation() {
		return ((OpmProcess) this.logicalEntity).getMinTimeActivation();
	}

	/**
	 * Sets minimum activation time of this OpmProcess. This field contains
	 * non-negative integer X 7 (msec, sec, min, hours, days, months, years)
	 * with semi-colons separation.
	 * 
	 */

	public void setMinTimeActivation(String time) {
		((OpmProcess) this.logicalEntity).setMinTimeActivation(time);
	}

	public String toString() {
		return this.getName().replaceAll("\n", " ");
	}

	public String getTypeString() {
		return "Process";
	}
}
