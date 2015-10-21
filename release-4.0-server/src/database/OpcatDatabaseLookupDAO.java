package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import database.LookupDAO;
import database.LookupImpl;
import database.Lookupable;
import database.OpcatDatabaseConnection;

public class OpcatDatabaseLookupDAO implements LookupDAO {

	String tableName;

	public OpcatDatabaseLookupDAO(String tableName) {
		super();
		this.tableName = tableName;
	}

	@Override
	public List<? extends Lookupable> getAll() throws SQLException {
		List<Lookupable> list = new ArrayList<Lookupable>();
		ResultSet rs;
		PreparedStatement ps = OpcatDatabaseConnection.getInstance()
				.getConnection().prepareStatement("select * from " + tableName);
		rs = ps.executeQuery();

		while (rs.next()) {
			list.add(new LookupImpl(rs
					.getInt(OpcatDatabaseConstants.FIELD_NAME_ID), rs
					.getString(OpcatDatabaseConstants.FIELD_NAME_NAME), rs
					.getString(OpcatDatabaseConstants.FIELD_NAME_DESCRIPTION),
					rs.getInt(OpcatDatabaseConstants.FIELD_NAME_COLOR)));
		}
		rs.close();
		return list;

	}
}
