package gui.license;

/**
 * Represents an unlimited license. 
 * 
 * @author Eran Toch
 */
public class UnlimitedLicense extends GenericLicense {

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
	 * @see gui.license.GenericLicense#getLicenseCaption()
	 */
	public String getLicenseCaption() {
		return "";
	}

	/* (non-Javadoc)
	 * @see gui.license.ILicense#getLicenseError()
	 */
	public String getLicenseError() {
		return "";
	}
	
    /* (non-Javadoc)
     * @see gui.license.ILicense#getLicenseDiscription()
     */
    public String getLicenseDescription() {
        return "Commercial Version";
    }
    
	public String toString()	{
		return this.getLicenseTitle();
	}
}
