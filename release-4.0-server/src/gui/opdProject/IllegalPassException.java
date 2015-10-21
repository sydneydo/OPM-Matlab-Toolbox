package gui.opdProject;

/**
 * This class is Exception that <a href = "OpdStateMachne.html"><code>OpdStateMachne</code></a>
 * throws when the automata does illegal step.
 * 
 * @see <a href = "OpdStateMachne.html"><code>OpdStateMachne</code></a>
 *      </p>
 */

public class IllegalPassException extends Exception {
	 

	/**
	 * 
	 */
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Just constructs an exception with "Illegal pass
	 * Exception" message.
	 */
	public IllegalPassException() {
		super("Illegal pass Exception");
	}

	/**
	 * Constructs an Exception with the specified detail message.
	 * 
	 * @param str
	 *            the detail message.
	 */
	public IllegalPassException(String str) {
		super(str);
	}
}