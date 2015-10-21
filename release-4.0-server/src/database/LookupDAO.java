package database;

import java.sql.SQLException;
import java.util.List;

public interface LookupDAO {
	public abstract List<? extends Lookupable> getAll() throws SQLException;
}
