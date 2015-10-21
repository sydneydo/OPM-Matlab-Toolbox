package expose;

import java.io.File;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_ENTITY_TYPES;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_STATUS;
import gui.opmEntities.OpmObject;
import gui.projectStructure.Entry;
import gui.util.OpcatLogger;

public class OpcatExposeEntity {

	OpcatExposeKey key;
	private OPCAT_EXPOSE_ENTITY_TYPES opmEntityType;
	private String opmEntityDescription;
	private String modelID;
	private String modelName;
	private String userID;

	protected void setUserID(String userID) {
		this.userID = userID;
	}

	private String description;
	private String opmEntityName;
	private OPCAT_EXPOSE_STATUS status;
	private OPCAT_EXPOSE_OP op;

	protected void setOp(OPCAT_EXPOSE_OP op) {
		this.op = op;
	}

	public OpcatExposeKey getKey() {
		return key;
	}

	public OPCAT_EXPOSE_ENTITY_TYPES getOpmEntityType() {
		return opmEntityType;
	}

	public String getOpmEntityDescription() {
		return opmEntityDescription;
	}

	public String getModelID() {
		return modelID;
	}

	public String getModelName() {
		return modelName;
	}

	public String getUserID() {
		return userID;
	}

	public String getDescription() {
		return description;
	}

	public String getOpmEntityName() {
		return opmEntityName;
	}

	public OPCAT_EXPOSE_STATUS getStatus() {
		return status;
	}

	public OPCAT_EXPOSE_OP getOp() {
		return op;
	}

	public OpcatExposeEntity(OpcatExposeKey key,
			OPCAT_EXPOSE_ENTITY_TYPES opmEntityType,
			String opmEntityDescription, String modelID, String modelName,
			String userID, String description, String opmEntityName,
			OPCAT_EXPOSE_STATUS status, OPCAT_EXPOSE_OP op) {
		super();
		this.key = key;
		this.opmEntityType = opmEntityType;
		this.opmEntityDescription = opmEntityDescription;
		this.modelID = modelID;
		this.modelName = modelName;
		this.userID = userID;
		this.description = description;
		this.opmEntityName = opmEntityName;
		this.status = status;
		this.op = op;
	}

	public OpcatExposeEntity(Entry myEntity, String userID) {
		super();

		try {

			OPCAT_EXPOSE_ENTITY_TYPES type = (myEntity.getLogicalEntity() instanceof OpmObject ? OPCAT_EXPOSE_ENTITY_TYPES.OBJECT
					: OPCAT_EXPOSE_ENTITY_TYPES.PROCESS);

			OPCAT_EXPOSE_OP op = OPCAT_EXPOSE_OP.ADD;

			OPCAT_EXPOSE_STATUS status = OPCAT_EXPOSE_STATUS.NORMAL;

			OpcatExposeKey key;
			key = new OpcatExposeKey(
					new File(myEntity.getMyProject().getPath()), myEntity
							.getId(), myEntity.getLogicalEntity()
							.isPrivateExposed());

			String modelID = myEntity.getMyProject().getGlobalID();

			String modelName = myEntity.getMyProject().getName();

			String description = "description ";

			this.key = key;
			this.opmEntityType = type;
			this.opmEntityDescription = myEntity.getDescription();
			this.modelID = modelID;
			this.modelName = modelName;
			this.userID = userID;
			this.description = description;
			this.opmEntityName = myEntity.getName();
			this.status = status;
			this.op = op;

		} catch (Exception e) {
			OpcatLogger.logError(e);
		}

	}

}
