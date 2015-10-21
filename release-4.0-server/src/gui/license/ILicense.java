package gui.license;

/**
 * Represents the licensing policy for the current instance of Opcat.
 * @author Eran Toch
 */
public interface ILicense {

	/**
	 * Specifies whether the licensing policy limits the user actions in any way.
	 * If the result is <code>false</code>, then no special actions will be done
	 * in order to limit user (even of other method dictate it).
	 * @return	<code>true</code> if the license limits the actions of the user. 
	 * <code>false</code> otherwise.
	 */
	public boolean isLimitedLicense();
	
	/**
	 * Returns the maximal number of things which opcat is allowed to create. 
	 */
	public int getMaxNumberOfThings();
	
	
	/**
	 * @return	A longer text, describing the license charasteristics. May be displayed
	 * in the "splash welcome screen".
	 */
	public String getLicenseCaption();

	/**
	 * @return	A longer text, describing the license charasteristics. May be displayed
	 * in the "about window".
	 */
	public String getLicenseDescription();

	/**
	 * @return A text which is displayed to the user if she goes over the license
	 * policy (for instance, if more than X things were added to the model). 
	 */
	public String getLicenseError();
	
	public PolicyChecker getPolicyOfficer();
}
