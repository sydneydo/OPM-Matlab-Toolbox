package messages;

import gui.util.opcatGrid.GridPanel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OpcatMessagesConstants {

	protected static final String messages_table = "messages";
	protected static final String type_table = "messages_type";
	protected static final String systems_table = "messages_systems";
	protected static final String subsystems_table = "messages_subsystems";
	protected static final String sevirity_table = "messages_severity";
	protected static final String op_table = "messages_operation";

	public static enum OPCAT_MESSAGES_OP {
		FILES_COMMIT_MODIFIED, FILES_COMMIT_COMPLETED, FILES_COMMIT_DELETED, FILES_COMMIT_REPLACED, FILES_COMMIT_DELTA_SENT, FILES_COMMIT_ADDED, FILES_UPDATE_NONE, FILES_UPDATE_ADD, FILES_UPDATE_DELETE, FILES_UPDATE_UPDATE_CHANGED, FILES_UPDATE_UPDATE_CONFLICTED, FILES_UPDATE_UPDATE_MERGED, FILES_UPDATE_EXTERNAL, FILES_RESTORE, FILES_UPDATE_COMPLETED, FILES_ADD, FILES_DELETE, FILES_LOCKED, FILES_LOCK_FAILED, FILES_UNLOCK_FAILED, FILES_UNLOCKED, FILES_REVERT, NONE, EXPOSE_INTERFACE_CHANGE_REQUEST, EXPOSE_CHANGE_DONE, EXPOSE_CHANGE_REQUEST, EXPOSE_UPDATED, EXPOSE_ADDED, EXPOSE_DELETED, EXPOSE_USED, EXPOSED_UN_USED, EXPOSE_RELEASE_REQUEST;
	};

	/**
	 * 
	 * an {@link Enum} which represents the messages_type table. each name in
	 * this table should be listed here. the description the table will be shown
	 * in the {@link OpcatMessage} {@link GridPanel}
	 */
	public static enum OPCAT_MESSAGES_TYPE {
		ALL, USER, MODEL, ENTITY
	};

	/**
	 * 
	 * an {@link Enum} which represents the messages_subsystems table. each name
	 * in this table should be listed here. the description the table will be
	 * shown in the {@link OpcatMessage} {@link GridPanel}
	 */
	public static enum OPCAT_MESSAGES_SUBSYSTEMS {
		ALL, COMMIT, CHECKOUT, EXPOSE
	};

	/**
	 * 
	 * an {@link Enum} which represents the messages_systems table. each name in
	 * this table should be listed here. the description the table will be shown
	 * in the {@link OpcatMessage} {@link GridPanel}
	 */
	public static enum OPCAT_MESSAGES_SYSTEMS {
		ALL, FILES, MODELS
	};

	/**
	 * 
	 * an {@link Enum} which represents the messages_severity table. each name
	 * in this table should be listed here. the description the table will be
	 * shown in the {@link OpcatMessage} {@link GridPanel}
	 */
	public static enum OPCAT_MESSAGES_SEVIRITY {
		INFORMATION, WARNING, ACTION_NEEDED
	};

	protected static final String FIELD_NAME_SYSTEM = "SYSTEM_ID";
	protected static final String FIELD_NAME_DESTINATION_TYPE = "DESTINATION_TYPE_ID";
	protected static final String FIELD_NAME_DESTINATION_ID = "DESTINATION_ID";
	protected static final String FIELD_NAME_DESTINATION_SUBSYSTEM = "DESTINATION_SUBSYSTEM_ID";
	protected static final String FIELD_NAME_SENDER_ID = "SENDER_ID";
	protected static final String FIELD_NAME_SENDER_TYPE = "SENDER_TYPE_ID";
	protected static final String FIELD_NAME_SENDER_SUBSYSTEM = "SENDER_SUBSYSTEM_ID";
	protected static final String FIELD_NAME_MESSAGE_DATE = "MESSAGE_DATE";
	protected static final String FIELD_NAME_SEVIRITY = "SEVIRITY_ID";
	protected static final String FIELD_NAME_MESSAGE = "MESSAGE_DESCRIPTION";
	/**
	 * message is about this object
	 */
	protected static final String FIELD_NAME_MESSAGE_SUBJECT = "MESSAGE_SUBJECT";

	/**
	 * message is the result of this operation
	 */
	protected static final String FIELD_NAME_MESSAGE_OP = "MESSAGE_OP_ID";

	protected static final String GRID_NAME_ID = "ID";
	protected static final String GRID_NAME_SYSTEM = "System";
	protected static final String GRID_NAME_DESTINATION_TYPE = "Destination Type";
	protected static final String GRID_NAME_DESTINATION_ID = "Destination";
	protected static final String GRID_NAME_DESTINATION_SUBSYSTEM = "Destination Subsystem";
	protected static final String GRID_NAME_SENDER_ID = "Sender ID";
	protected static final String GRID_NAME_SENDER_TYPE = "Sender Type";
	protected static final String GRID_NAME_SENDER_SUBSYSTEM = "Sender Subsystem";
	protected static final String GRID_NAME_MESSAGE_DATE = "Date";
	protected static final String GRID_NAME_SEVIRITY = "Severity";
	protected static final String GRID_NAME_MESSAGE = "Message";
	protected static final String GRID_NAME_NAME = "Name";
	protected static final String GRID_NAME_DESCRIPTION = "Description";
	protected static final String GRID_NAME_MESSAGE_OP = "Operation";
	protected static final String GRID_NAME_MESSAGE_SUBJECT = "Subject";

	protected static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

	protected static String getDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		return sdf.format(cal.getTime());

	}

}
