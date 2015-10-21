package database;

import gui.util.OpcatLogger;
import java.sql.Connection;
import java.sql.DriverManager;

import util.Configuration;

public class OpcatDatabaseConnection {

	private static OpcatDatabaseConnection instance = null;
	private Connection connection = null;
	private static boolean valid = false;

	public static boolean isOnLine() {
		return valid;
	}

	private OpcatDatabaseConnection() {
		if (connection == null) {
			try {
				Class.forName(
						"com."
								+ Configuration.getInstance().getProperty(
										"DBtype") + ".jdbc.Driver")
						.newInstance();
				connection = DriverManager.getConnection("jdbc:"
						+ Configuration.getInstance().getProperty("DBtype")
						+ "://"
						+ Configuration.getInstance().getProperty("DBserver")
						+ "/"
						+ Configuration.getInstance().getProperty(
								"DBdatabasename"), "opcat", "0545224014");
				valid = true;
			} catch (Exception ex) {
				valid = false;
				OpcatLogger.logError(ex);
			}
		}
	}

	public static OpcatDatabaseConnection getInstance() {
		if ((instance == null) || (!isOnLine())) {
			instance = new OpcatDatabaseConnection();
		}
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}

}
