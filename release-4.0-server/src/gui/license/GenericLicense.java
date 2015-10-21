package gui.license;

/**
 * A generic super class for license classes.
 * @author Eran Toch
 */
public abstract class GenericLicense implements ILicense {

	/* (non-Javadoc)
	 * @see gui.license.ILicense#isLimitedLicense()
	 */
	public boolean isLimitedLicense() {
		return false;
	}

	/* (non-Javadoc)
	 * @see gui.license.ILicense#getMaxNumberOfThings()
	 */
	public int getMaxNumberOfThings() {
		return -1;
	}

	/* (non-Javadoc)
	 * @see gui.license.ILicense#getLicenseTitle()
	 */
	public String getLicenseTitle() {
		return null;
	}
	
	public String getLicenseCaption()	{
		return "";
	}

	/* (non-Javadoc)
	 * @see gui.license.ILicense#getLicenseError()
	 */
	public String getLicenseError() {
		return null;
	}

	/* (non-Javadoc)
	 * @see gui.license.ILicense#getPolicyOfficer()
	 */
	public PolicyChecker getPolicyOfficer() {
		return new PolicyChecker(this);
	}

	public String toString()	{
		return this.getLicenseTitle();
	}
}
