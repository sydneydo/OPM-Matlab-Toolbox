package messages;

import messages.OpcatMessagesConstants.OPCAT_MESSAGES_OP;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SEVIRITY;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SUBSYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_TYPE;

public class OpcatMessage {

	/**
	 * ID INTEGER PRIMARY KEY , TARGET_TYPE INTEGER, TARGET_ID VARCHAR(254),
	 * ORIGIN_TYPE INTEGER, ORIGIN_ID VARCHAR(254), DATE DATETIME ,
	 * OPCAT_SUBSYSTEM VARCHAR(254) , SEVIRITY VARCHAR(254), MESSAGE
	 * VARCHAR(254)) ");
	 */

	String senderID;
	String destinationID;
	OPCAT_MESSAGES_TYPE senderType;
	OPCAT_MESSAGES_TYPE destinationType;
	OPCAT_MESSAGES_SUBSYSTEMS destinationSubsystem;
	OPCAT_MESSAGES_SUBSYSTEMS senderSubsystem;
	OPCAT_MESSAGES_SEVIRITY sevirity;
	OPCAT_MESSAGES_SYSTEMS system;
	OPCAT_MESSAGES_OP op;
	String subject;
	String msg;
	long id;

	/**
	 * 
	 * @param senderID
	 *            system, Username, model name , entityname + @ + modelname
	 * @param targetID
	 *            system, Username, model name , entityname + @ + modelname
	 * @param type
	 * @param subsystem
	 * @param sevirity
	 * @param msg
	 * 
	 */
	public OpcatMessage(String senderID, String destinationID,
			OPCAT_MESSAGES_TYPE senderType,
			OPCAT_MESSAGES_TYPE destinationType,
			OPCAT_MESSAGES_SUBSYSTEMS destinationSubsystem,
			OPCAT_MESSAGES_SUBSYSTEMS senderSubsystem,
			OPCAT_MESSAGES_SYSTEMS system, OPCAT_MESSAGES_SEVIRITY sevirity,
			OPCAT_MESSAGES_OP op, String subject, String msg) {
		super();
		this.senderID = senderID;
		this.destinationID = destinationID;
		this.senderType = senderType;
		this.destinationType = destinationType;
		this.destinationSubsystem = destinationSubsystem;
		this.senderSubsystem = senderSubsystem;
		this.sevirity = sevirity;
		this.msg = msg;
		this.system = system;
		this.op = op;
		this.subject = subject;
	}

	public OPCAT_MESSAGES_SYSTEMS getSystem() {
		return system;
	}

	public String getSenderID() {
		return senderID;
	}

	public String getDestinationID() {
		return destinationID;
	}

	public OPCAT_MESSAGES_TYPE getSenderType() {
		return senderType;
	}

	public OPCAT_MESSAGES_TYPE getDestinationType() {
		return destinationType;
	}

	public OPCAT_MESSAGES_SUBSYSTEMS getDestinationSubsystem() {
		return destinationSubsystem;
	}

	public OPCAT_MESSAGES_SUBSYSTEMS getSenderSubsystem() {
		return senderSubsystem;
	}

	public OPCAT_MESSAGES_SEVIRITY getSevirity() {
		return sevirity;
	}

	public String getMsg() {
		return msg;
	}

	public OPCAT_MESSAGES_OP getOp() {
		return op;
	}

	public String getSubject() {
		return subject;
	}

}
