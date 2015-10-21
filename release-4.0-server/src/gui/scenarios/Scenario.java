package gui.scenarios;

import gui.metaLibraries.logic.MetaManager;

import java.util.HashMap;

public class Scenario {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name = "";

	private String id;

	private HashMap<Long,Long> initialObjects;

	private HashMap<Long,Long> finalObjects;

	private MetaManager metalibreries = new MetaManager();

	public Scenario(String name) {
		this.name = name;
		initialObjects = new HashMap<Long,Long>();
		finalObjects = new HashMap<Long,Long>();
		id = name;
	}

	public HashMap<Long,Long> getFinalObjectsHash() {
		return finalObjects;
	}

	public void setFinalObjectsHash(HashMap<Long,Long> finalObjects) {
		this.finalObjects = finalObjects;
	}

	public HashMap<Long,Long> getInitialObjectsHash() {
		return initialObjects;
	}

	public void setInitialObjectsHash(HashMap<Long,Long> initialObjects) {
		this.initialObjects = initialObjects;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.id = name;
	}

	public String getId() {
		return id;
	}

	public String toString() {
		return getName();
	}

	/**
	 * metalibs arraylist
	 * 
	 * @return
	 */
	public MetaManager getSettings() {
		return metalibreries;
	}

}
