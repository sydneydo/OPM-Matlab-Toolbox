package expose;

public class OpcatExposeConstants {

	protected final static String TABLE_EXPOSE = "expose";
	protected final static String TABLE_EXPOSE_USAGE = "expose_usage";
	protected final static String TABLE_EXPOSE_OPM_ENTITY_TYPE = "opm_entity_type";
	protected final static String TABLE_EXPOSE_OP = "expose_op";
	protected final static String TABLE_EXPOSE_DATA = "expose_data";
	protected final static String TABLE_EXPOSE_STATUS = "expose_status";
	protected final static String TABLE_EXPOSE_ALLOWED = "expose_allowed";

	protected static final String FIELD_EXPOSE_ENTITY_MODEL_URI = "MODEL_URI";
	protected static final String FIELD_EXPOSE_ENTITY_ID = "ENTITY_ID";

	protected static final String FIELD_EXPOSE_ALLOWED_EXPOSE_ID = "EXPOSE_ID";
	protected static final String FIELD_EXPOSE_ALLOWED_MODEL_URI = "MODEL_URI";

	protected static final String FIELD_EXPOSE_DATA_EXPOSE_ID = "EXPOSE_ID";
	protected static final String FIELD_EXPOSE_DATA_MODEL_ID = "MODEL_ID";
	protected static final String FIELD_EXPOSE_DATA_MODEL_NAME = "MODEL_NAME";
	protected static final String FIELD_EXPOSE_DATA_ENTITY_TYPE = "ENTITY_TYPE_ID";
	protected static final String FIELD_EXPOSE_DATA_ENTITY_DESCRIPTION = "ENTITY_DESCRIPTION";
	protected static final String FIELD_EXPOSE_DATA_DESCRIPTION = "DESCRIPTION";
	protected static final String FIELD_EXPOSE_DATA_ENTITY_NAME = "ENTITY_NAME";
	protected static final String FIELD_EXPOSE_DATA_EXPOSE_OP = "EXPOSE_OP_ID";
	protected static final String FIELD_EXPOSE_DATA_OP_USER = "OP_USER_ID";
	protected static final String FIELD_EXPOSE_DATA_EXPOSE_STATUS = "EXPOSE_STATUS_ID";
	protected static final String FIELD_EXPOSE_DATA_EXPOSE_DATETIME = "EXPOSE_DATETIME";

	protected static final String FIELD_EXPOSE_USAGE_USING_MODEL_NAME = "USING_MODEL_NAME";
	protected static final String FIELD_EXPOSE_USAGE_USING_MODEL_URI = "USING_MODEL_URI";
	protected static final String FIELD_EXPOSE_USAGE_EXPOSE_ID = "EXPOSE_ID";
	protected static final String FIELD_EXPOSE_USAGE_USING_ENTITY_IN_OPD_ID = "USING_INSTANCE_IN_OPD_ID";
	protected static final String FIELD_EXPOSE_USAGE_USING_ENTITY_OPD_ID = "USING_INSTANCE_OPD_ID";
	protected static final String FIELD_EXPOSE_USAGE_USING_ENTITY_NAME = "USING_ENTITY_NAME";

	public static final long EXPOSE_UNDEFINED_ID = -99999;

	public static enum OPCAT_EXPOSE_ENTITY_TYPES {
		PROCESS, OBJECT, STATE, LINK
	}

	public static enum OPCAT_EXPOSE_OP {
		UPDATE, DELETE, ADD, CHANGE_INTERFACE, NONE
	}

	public static enum OPCAT_EXPOSE_STATUS {
		NORMAL_CHANGE_REQUEST, PRIVATE_CHANGE_REQUEST, NORMAL, PRIVATE
	}

	public static enum OPCAT_EXPOSE_CHANGE_TYPE {
		ZOOM_IN, UNFOLD, ADD, COPY, DELETE, DELETE_THING, DELETE_LINK, DELETE_REALTION, DELETE_STATE, ADD_LINK, ADD_RELATION, ADD_STATE, ADD_THING, CUT_LINK, CUT_RELATION, CUT_STATE, CUT_THING, DELETE_OPD
	}

	public static enum OPCAT_EXPOSE_LINK_DIRECTION {
		FROM, TO
	}

	public static enum OPCAT_EXOPSE_ADVISOR_TYPE {
		INTERFACE_LOCAL, INTERFACE_ROOT, PROPERTIES_FROM, PROERTIES_TO
	}

}
