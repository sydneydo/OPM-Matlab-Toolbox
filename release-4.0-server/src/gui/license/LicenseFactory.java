package gui.license;

/**
 * Returns the license class for the current instance of Opcat, according to the
 * code supplied. The class uses an encryption key, which is hard coded into the code
 * and should match the key supplied to the code decryptor.  
 * 
 * @author Eran Toch
 *
 */
public class LicenseFactory {
	
	private static final String KEY = "gfjdl8d9ejd7j36djugdhd6d5dhj3o29dkdn39";

	/**
	 * A factory method that creates and retrieves an implementation of the 
	 * <code>ILicense</code> interface, according to the given code.
	 * @param code	The license code, supplied by the client of the factory. 
	 * probably extracted from a license file, or given by the user.
	 * @return	An instance (implementation) of the <code>ILicense</code> interface,
	 * specifying the abilities of the instance.
	 */
	public static ILicense createLicense(String code)	{
		ILicense lic = null;
		try {
			StringEncrypter encrypter = new StringEncrypter(
					StringEncrypter.DESEDE_ENCRYPTION_SCHEME, KEY);
			String licenseType = encrypter.decrypt(code);
			lic = decide(licenseType);
		} catch (Exception ex)	{
			ex.printStackTrace();
			lic = createDefaultLicense();
		}
		return lic;
	}
	
	/**
	 * Returns the default license - i.e., the license which is returned if no license
	 * file was found.
	 */
	public static ILicense createDefaultLicense()	{
		return new UnlimitedLicense();
	}
	
	/**
	 * Decides which license class to load, according to the given code.
	 * @return	The license class
	 * @param licenseType	The result of the decryption of the code.
	 * @throws Exception if no license match the licenseType
	 */
	private static ILicense decide(String licenseType) throws Exception	{
		if (licenseType.equals("restricted"))	{
			return new RestrictedLicense();
		}
		else if (licenseType.equals("unlimited"))	{
			return new UnlimitedLicense();
		}
		else {
			throw new Exception("No suitable license class was found for"+ licenseType);
		}
	}
}
