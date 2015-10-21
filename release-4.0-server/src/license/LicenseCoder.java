package license;

import license.StringEncrypter.EncryptionException;
/**
 * Codes a new encryption code for the Opcat license. The code should
 * be placed within the <code>license.lic</code> file. The decryptor accepts
 * two parameters:<br>
 * 1. The string to encrypt, that should reflect the type of the license. For instance,
 * the strings <code>restricted</code> and <code>unlimited</code> will be encoded and
 * retrieved by the license factory class. <br>
 * 2. Encryption key, that will be matched by the by the <code>LicenseFactory</code>
 * class. <code>gfjdl8d9ejd7j36djugdhd6d5dhj3o29dkdn39</code>, is the
 * current encryption key.
 * @author Eran Toch
 */
public class LicenseCoder {

	/**
	 * See class documentation. 
	 * @param args The license type and the encryption key.
	 */
	public static void main(String[] args) {
		String stringToEncrypt = args[0];
		String encryptionKey = args[1];

		try {
			StringEncrypter encrypter = new StringEncrypter(
					StringEncrypter.DESEDE_ENCRYPTION_SCHEME, encryptionKey);
			String encryptedString = encrypter.encrypt(stringToEncrypt);
			System.out.println(encryptedString);
		} catch (EncryptionException e) {
			e.printStackTrace();
		}
	}
}