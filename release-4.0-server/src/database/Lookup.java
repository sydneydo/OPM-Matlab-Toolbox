package database;

import gui.util.OpcatLogger;

import java.sql.SQLException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Lookup<T extends Enum<T>> {
	private List<? extends Lookupable> all; // for holding all of the values
	// returned from the DAO
	private Map<T, Lookupable> map; // for holding the mapping between the two

	public Lookup(LookupDAO dao, Class clazz) throws SQLException {
		map = new EnumMap<T, Lookupable>(clazz);
		// map the lookups together
		all = dao.getAll();
		for (Lookupable item : all) {
			try {
				T key = (T) Enum.valueOf(clazz, item.getName().toUpperCase());
				map.put(key, item);
			} catch (IllegalArgumentException e) {
				/**
				 * TODO: remove this as an error
				 */
				OpcatLogger.logError(new Exception(item.getName()
						+ ": missing from enum"));
			}
		}
	}

	public String getName(T item) {
		return map.get(item).getName();
	}

	public int getColor(T item) {
		return map.get(item).getColor();
	}

	public int getId(T item) {
		return map.get(item).getId();
	}

	public String getDescription(T item) {
		return map.get(item).getDescription();
	}

	public Collection<? extends Lookupable> getAllEnums() {
		return map.values();
	}

	public boolean isFullMapping() {
		return all.size() == map.size();
	}

}
