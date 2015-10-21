package expose.logic;

import java.util.ArrayList;

import gui.metaLibraries.logic.Role;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ConnectionEdgeInstance;

public class OpcatExposeAdvice {

	private Role parent;
	private ArrayList<OpcatExposeInterfaceItem> properties;
	private ConnectionEdgeInstance sourceInstance;
	private ConnectionEdgeEntry sourceEntry;

	public OpcatExposeAdvice(Role parent,
			ArrayList<OpcatExposeInterfaceItem> properties,
			ConnectionEdgeInstance sourceInstance) {
		super();
		this.parent = parent;
		this.properties = properties;
		this.sourceInstance = sourceInstance;
		this.sourceEntry = (ConnectionEdgeEntry) sourceInstance.getEntry();
	}

	public String key() {
		return sourceEntry.getId() + "@"
				+ sourceEntry.getMyProject().getGlobalID();
	}

	public Role getParent() {
		return parent;
	}

	public void setParent(Role parent) {
		this.parent = parent;
	}

	public ArrayList<OpcatExposeInterfaceItem> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<OpcatExposeInterfaceItem> properties) {
		this.properties = properties;
	}

	public ConnectionEdgeInstance getSourceInstance() {
		return sourceInstance;
	}

	public void setSourceInstance(ConnectionEdgeInstance sourceInstance) {
		this.sourceInstance = sourceInstance;
	}

	public ConnectionEdgeEntry getSourceEntry() {
		return sourceEntry;
	}

	public void setSourceEntry(ConnectionEdgeEntry sourceEntry) {
		this.sourceEntry = sourceEntry;
	}

}
