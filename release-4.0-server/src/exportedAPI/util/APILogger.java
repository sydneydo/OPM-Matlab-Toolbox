package exportedAPI.util;

import gui.util.Debug;

/**
 * Enables logging utilities for extension tools. The class enable logging for
 * error handling and debugging. Opcat logging might be blocked in production
 * versions, but will always work in debug versions.
 * 
 * @author Eran Toch
 */
public class APILogger {

	/**
	 * Logs a text message to the system.err output.
	 * 
	 * @param message
	 *            The error message to be displayed.
	 */
	public static void logError(String message) {
		// OpcatLogger.logError(message);
	}

	/**
	 * Logs an OPM error by printing it's message and the stack trace to an
	 * "error log" file and to the standard error output.
	 */
	public static void logError(OpcatApiException oex) {
		// OpcatLogger.logError(oex);
	}

	/**
	 * Logs a Message to the Debug Pane on the extension tools tab. The Debug
	 * tab will be shown only if
	 * 
	 * @see SetDebug is used to turn on Debug pane. This pane is turned off in
	 *      production versions.
	 * @param obj
	 *            the Object source of the Message
	 * @param Message
	 *            the Message to print into the debug window.
	 */
	public static void Print(Object obj, String Message) {
		//OpcatLogger.logError(Message);
	}

	/**
	 * Used to turn Debug mode On
	 * 
	 * @see Print
	 * 
	 */
	public static void SetDebug(boolean status) {
		if (status) {
			Debug.setLEVEL(Debug.DebugLevelExtensionTools);
		} else {
			Debug.setLEVEL(Debug.DebugLevelOFF);
		}
	}

}
