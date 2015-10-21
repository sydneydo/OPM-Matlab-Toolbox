package expose;

import gui.projectStructure.Instance;

public class OpcatExposeUser {

	OpcatExposeKey key;
	String modelName;
	String userID;
	String modelPath;
	Instance usingInstance;

	/**
	 * returns the using {@link Instance}, null if the instance was not set.
	 * 
	 * @return
	 */
	public Instance getUsingInstance() {
		return usingInstance;
	}

	public String getModelPath() {
		return modelPath;
	}

	public OpcatExposeUser(OpcatExposeKey key, String modelName,
			String modelPath, String userID) {
		super();
		this.key = key;
		this.modelName = modelName;
		this.userID = userID;
		this.modelPath = modelPath;
	}

	public OpcatExposeUser(Instance usingInstance, String userID) {
		super();
		this.key = usingInstance.getPointer();
		this.modelName = usingInstance.getEntry().getMyProject().getName();
		this.userID = userID;
		this.modelPath = usingInstance.getEntry().getMyProject().getPath();
		this.usingInstance = usingInstance;
	}

	public String getUserID() {
		return userID;
	}

	public OpcatExposeKey getKey() {
		return key;
	}

	public String getModelName() {
		return modelName;
	}

}
