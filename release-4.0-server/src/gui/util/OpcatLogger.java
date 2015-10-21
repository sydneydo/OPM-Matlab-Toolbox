package gui.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import util.Configuration;
import exportedAPI.util.OpcatApiException;
import gui.Opcat2;

/**
 * Logs messages into a designated log file.
 * 
 * @author Eran Toch
 * 
 */
public class OpcatLogger {

	/**
	 * Logs an error message to the system.err output.
	 */

	private static String logFilePath = Configuration.getInstance()
			.getProperty("error_log");

	public static void logError(String message, boolean showGUI) {
		try {
			FileOutputStream errorLog = new FileOutputStream(logFilePath, true);
			PrintStream ps = new PrintStream(errorLog);
			ps.println("---------------------------------------------");
			ps.println(getFormattedTime());
			ps.println("Error :" + message);
			ps.println("----------------------------------------------");
			ps.flush();
			ps.close();
		} catch (IOException ioe) {

		}

		try {
			if (showGUI) {

				String msg = "An error occured"
						+ "\n\n\"error.txt\" file was created in Opcat2 directory. Please send the error details and the error file to bugs@opcat.com ";

				JXErrorPane error = new JXErrorPane();
				ErrorInfo info = new ErrorInfo("OPCAT Error", msg, message,
						"Error", null, Level.SEVERE, null);
				error.setErrorInfo(info);
				JXErrorPane.createDialog(Opcat2.getFrame(), error).setVisible(
						true);
			}

		} catch (Exception e) {

		}

		System.err.println("---------------------------------------------");
		System.err.println(getFormattedTime());
		System.err.println("Error :" + message);
		System.err.println("---------------------------------------------");

	}

	public static void logError(String message) {
		logError(message, true);
	}

	private static String getFormattedTime() {
		GregorianCalendar rightNow = new GregorianCalendar();
		Date d = rightNow.getTime();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM);
		String thetime = df.format(d);
		return thetime;
	}

	public static void logError(Exception ex) {
		logError(ex, Level.SEVERE, true);
	}

	public static void logError(Exception ex, boolean showDialog) {
		logError(ex, Level.SEVERE, showDialog);
	}

	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}

	/**
	 * Logs an error by printing it's message and the stack trace to an
	 * "error log" file and to the standard error output.
	 */
	private static void logError(Exception ex, Level level, boolean showDialog) {

		try {
			FileOutputStream errorLog = new FileOutputStream(logFilePath, true);
			PrintStream ps = new PrintStream(errorLog);
			ps.println("---------------------------------------------");
			ps.println(getFormattedTime());
			ps.println(ex);
			ex.printStackTrace(ps);
			ps.println("----------------------------------------------");
			ps.flush();
			ps.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		try {

			if (showDialog) {
				String errorMessage = ex.toString();
				int place = ex.toString().indexOf(":");
				if (place > 0) {
					errorMessage = errorMessage.substring(place + 1,
							errorMessage.length());
				}
				String msg = "An error occured:\n\n" + errorMessage;
				errorMessage = "<pre>" + getStackTrace(ex) + "</pre>";
				// errorMessage.replace("\n", "<br>");
				// errorMessage.replace("\t", "<br>");

				JXErrorPane error = new JXErrorPane();
				ErrorInfo info = new ErrorInfo("OPCAT Error", msg,
						errorMessage, "Error", ex, level, null);
				error.setErrorInfo(info);

				JXErrorPane.createDialog(Opcat2.getFrame(), error).setVisible(
						true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.err.println("---------------------------------------------");
		System.err.println(getFormattedTime());
		System.err.println(ex);
		ex.printStackTrace(System.err);
		System.err.println("----------------------------------------------");
	}

	/**
	 * Logs an OPM error by printing it's message and the stack trace to an
	 * "error log" file and to the standard error output.
	 */
	public static void logError(OpcatApiException ex) {
		logError((Exception) ex);
	}

	/**
	 * Logs a general message.
	 */
	public static void logMessage(String message) {
		// try {
		// if (Debug.getDebugLevel() > 0) {
		// Debug debug = Debug.getInstance();
		// debug.Print(message, message, "1");
		// }
		// } catch (Exception e) {
		//
		// }

		try {
			FileOutputStream errorLog = new FileOutputStream(logFilePath, true);
			PrintStream ps = new PrintStream(errorLog);
			ps.println("---------------------------------------------");
			ps.println(getFormattedTime());
			ps.println("Message :" + message);
			ps.println("----------------------------------------------");
			ps.flush();
			ps.close();
		} catch (IOException ioe) {
		}

		System.out.println("---------------------------------------------");
		System.out.println(getFormattedTime());
		System.out.println("Message :" + message);
		System.out.println("---------------------------------------------");
	}

}
