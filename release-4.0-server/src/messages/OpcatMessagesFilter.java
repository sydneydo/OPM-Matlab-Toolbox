package messages;

import java.sql.SQLException;

import database.Lookup;
import database.OpcatDatabaseLookupDAO;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SEVIRITY;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SUBSYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_TYPE;

public class OpcatMessagesFilter {

	String[] sourceID;
	String[] destinationID;
	OPCAT_MESSAGES_SUBSYSTEMS[] destinationSubsystems;
	OPCAT_MESSAGES_SUBSYSTEMS[] senderSubsystems;
	OPCAT_MESSAGES_TYPE[] destinationTypes;
	OPCAT_MESSAGES_TYPE[] senderTypes;
	OPCAT_MESSAGES_SYSTEMS[] systems;
	OPCAT_MESSAGES_SEVIRITY[] severity;

	/**
	 * 
	 * @param sourceID
	 *            if null, will get all sources
	 * @param destinationID
	 *            if null will get all destinations
	 * @param subsystems
	 *            null, will get all
	 * @param types
	 *            null, will get all
	 */
	public OpcatMessagesFilter(String[] sourceID, String[] destinationID,
			OPCAT_MESSAGES_SUBSYSTEMS[] destinationSubsystems,
			OPCAT_MESSAGES_SUBSYSTEMS[] senderSubsystems,
			OPCAT_MESSAGES_TYPE[] destinationTypes,
			OPCAT_MESSAGES_TYPE[] senderTypes,
			OPCAT_MESSAGES_SYSTEMS[] systems, OPCAT_MESSAGES_SEVIRITY[] severity) {
		super();
		this.sourceID = sourceID;
		this.destinationID = destinationID;
		this.destinationSubsystems = destinationSubsystems;
		this.senderSubsystems = senderSubsystems;
		this.destinationTypes = destinationTypes;
		this.senderTypes = senderTypes;
		this.systems = systems;
		this.severity = severity;
	}

	private String buildSubQuery(String fieldName, Object[] filter,
			Lookup lookup) {

		String dest = null;

		if (filter != null) {
			for (int i = 0; i < filter.length; i++) {
				dest = (dest == null ? "( "
						+ fieldName
						+ " = '"
						+ (lookup != null ? lookup.getId((Enum) filter[i])
								: filter[i]) + "')" : dest
						+ " OR ( "
						+ fieldName
						+ " = '"
						+ (lookup != null ? lookup.getId((Enum) filter[i])
								: filter[i]) + "')");
			}
			dest = "(" + dest + ")";
		}

		return dest;
	}

	protected String getQuery() throws SQLException {

		String query = null;
		String dest = null;

		if (destinationID != null) {
			dest = buildSubQuery(
					OpcatMessagesConstants.FIELD_NAME_DESTINATION_ID,
					destinationID, null);
			if (dest != null) {
				query = " WHERE " + dest;
			}
		}

		if (sourceID != null) {
			dest = buildSubQuery(OpcatMessagesConstants.FIELD_NAME_SENDER_ID,
					sourceID, null);
			if (dest != null) {
				if (query != null) {
					query = query + " AND " + dest;
				} else {
					query = " WHERE " + dest;
				}
			}
		}

		Lookup lookup = null;
		if (destinationTypes != null) {
			lookup = new Lookup(new OpcatDatabaseLookupDAO(
					OpcatMessagesConstants.type_table),
					OPCAT_MESSAGES_TYPE.class);
			dest = buildSubQuery(
					OpcatMessagesConstants.FIELD_NAME_DESTINATION_TYPE,
					destinationTypes, lookup);
			if (dest != null) {
				if (query != null) {
					query = query + " AND " + dest;
				} else {
					query = " WHERE " + dest;
				}
			}
		}

		if (senderTypes != null) {
			lookup = new Lookup(new OpcatDatabaseLookupDAO(
					OpcatMessagesConstants.type_table),
					OPCAT_MESSAGES_TYPE.class);
			dest = buildSubQuery(OpcatMessagesConstants.FIELD_NAME_SENDER_TYPE,
					senderTypes, lookup);
			if (dest != null) {
				if (query != null) {
					query = query + " AND " + dest;
				} else {
					query = " WHERE " + dest;
				}
			}
		}

		if (destinationSubsystems != null) {
			lookup = new Lookup(new OpcatDatabaseLookupDAO(
					OpcatMessagesConstants.subsystems_table),
					OPCAT_MESSAGES_SUBSYSTEMS.class);
			dest = buildSubQuery(
					OpcatMessagesConstants.FIELD_NAME_DESTINATION_SUBSYSTEM,
					destinationSubsystems, lookup);
			if (dest != null) {
				if (query != null) {
					query = query + " AND " + dest;
				} else {
					query = " WHERE " + dest;
				}
			}
		}

		if (senderSubsystems != null) {
			lookup = new Lookup(new OpcatDatabaseLookupDAO(
					OpcatMessagesConstants.subsystems_table),
					OPCAT_MESSAGES_SUBSYSTEMS.class);
			dest = buildSubQuery(
					OpcatMessagesConstants.FIELD_NAME_SENDER_SUBSYSTEM,
					senderSubsystems, lookup);
			if (dest != null) {
				if (query != null) {
					query = query + " AND " + dest;
				} else {
					query = " WHERE " + dest;
				}
			}
		}

		if (systems != null) {
			lookup = new Lookup(new OpcatDatabaseLookupDAO(
					OpcatMessagesConstants.systems_table),
					OPCAT_MESSAGES_SYSTEMS.class);
			dest = buildSubQuery(OpcatMessagesConstants.FIELD_NAME_SYSTEM,
					systems, lookup);
			if (dest != null) {
				if (query != null) {
					query = query + " AND " + dest;
				} else {
					query = " WHERE " + dest;
				}
			}
		}

		if (severity != null) {
			lookup = new Lookup(new OpcatDatabaseLookupDAO(
					OpcatMessagesConstants.sevirity_table),
					OPCAT_MESSAGES_SYSTEMS.class);
			dest = buildSubQuery(OpcatMessagesConstants.FIELD_NAME_SEVIRITY,
					severity, lookup);
			if (dest != null) {
				if (query != null) {
					query = query + " AND " + dest;
				} else {
					query = " WHERE " + dest;
				}
			}
		}

		return query;

	}
}
