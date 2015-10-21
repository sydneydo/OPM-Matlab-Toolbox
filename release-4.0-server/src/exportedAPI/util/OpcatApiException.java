package exportedAPI.util;

import gui.util.OpcatException;

/**
 * A general exception used by Opcat core and extension tools. All exceptions
 * thrown by Opcat components should extend the <code>OPMException</code>.
 * 
 * @author Eran Toch
 */
public class OpcatApiException extends OpcatException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5406293125229991687L;

	public OpcatApiException(String msg) {
		super(msg);
	}
}
