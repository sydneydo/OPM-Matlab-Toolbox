package exportedAPI.opcatAPIx;

/**
 * IXProcessEntry is an interface to logical representation of Process in OPM meaning
 */

public interface IXProcessEntry extends IXThingEntry
{

	/**
	 * Returns process body of this IXProcessEntry
	 * @return process body of this IXProcessEntry
	 */
	public String getProcessBody();

	/**
	* Sets process body of this OpmProcess
	* @param processBody - string contains body of of the OPM process
	*/
	public void setProcessBody(String processBody);

	/**
	* Returns String representing maximum activation time
	* of this Process. This String contains non-negative integer X 7
	* (msec, sec, min, hours, days, months, years) with semi-colons
	* separation.
	* @return maximum activation time in string representation
	*/
	public String getMaxTimeActivation();

	/**
	* Sets  maximum activation time
	* of this Process. This field contains non-negative integer X 7
	* (msec, sec, min, hours, days, months, years) with semi-colons
	* separation.<br>
	* <strong>Importent</strong>: Legality of given string is not checked when using this method.
	* @param time - maximum activation time in string representation
	*/
	public void setMaxTimeActivation(String time);

	/**
	* Returns String representing minimum activation time
	* of this Process. This String contains non-negative integer X 7
	* (msec, sec, min, hours, days, months, years) with semi-colons
	* separation.
	* @return minimum activation time in string representation
	*/
	public String getMinTimeActivation();


	/**
	* Sets  minimum activation time
	* of this Process. This field contains non-negative integer X 7
	* (msec, sec, min, hours, days, months, years) with semi-colons
	* separation.<br>
	* <strong>Importent</strong>: Legality of given string is not checked when using this method
	* @param time - minimum activation time in string representation
	*/
	public void setMinTimeActivation(String time);
	

}