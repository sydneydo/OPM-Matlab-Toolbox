package server.database;

import gui.util.OpcatLogger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Models {

	private Connection con = null;
	private static String getNamesStmt = "select name from Models";

	public Models(Connection con) {
		super();
		this.con = con;
	}

	public ArrayList<String> getNames() {

		ArrayList<String> ret = new ArrayList<String>();
		try {
			Statement stmt = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet srs = stmt.executeQuery(getNamesStmt);

			String name = "";
			while (srs.next()) {
				name = srs.getString("Name");
				ret.add(name);
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		return ret;
	}
}
