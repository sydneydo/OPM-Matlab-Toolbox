package gui.dataProject;

import gui.metaLibraries.logic.Role;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import gui.projectStructure.Entry;
import gui.util.OpcatLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public abstract class DataAbstractItem implements ItemInterface {

	Object additionalData;

	private Vector data = new Vector();

	private String name;

	private long id;

	private String extID = "";

	public DataAbstractItem(String name, long id) {
		this.id = id;
		this.name = name;
		this.extID = Long.toString(id);
	}

	public DataAbstractItem(String name, String extID) {
		this.id = extID.hashCode();
		this.name = name;
		this.extID = extID;

	}

	public ArrayList connectedThings(Role myRole, OpdProject opdProject) {

		ArrayList things = new ArrayList();

		List allTthings = Collections.list(opdProject.getComponentsStructure()
				.getElements());
		Iterator iter = allTthings.iterator();
		while (iter.hasNext()) {
			Object entry = iter.next();
			if ((entry instanceof Entry)) {
				OpmEntity theThing = (OpmEntity) ((Entry) entry)
						.getLogicalEntity();
				Iterator rolesIter = Collections.list(
						theThing.getRolesManager().getLoadedRoles()).iterator();
				// .getLoadedRolesVector(ontoType).elements()).iterator();
				while (rolesIter.hasNext()) {
					Role role = (Role) rolesIter.next();
					// if (role.getThing().getDataInstance() != null) {
					DataAbstractItem tempReq = role.getThing();

					try {
						long roleProjectID = role.getOntology().getID();
						if (roleProjectID == myRole.getOntology().getID()) {
							if (tempReq.getId() == getId()) {
								things.add(theThing);
							}
						}
					} catch (Exception ex) {
						OpcatLogger.logError(ex);
					}

				}

			}

		}

		return things;
	}

	public void setAdditionalData(Object entry) {
		additionalData = entry;
	}

	public Object getAdditionalData() {
		return additionalData;
	}

	public Vector getAllData() {
		return data;
	}

	public void setAllData(Vector data) {
		this.data = data;
	}

	public String getName() {
		String str = " ";

		if (name != "") {
			str = name;
		}
		return str;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return this.id;
	}

	public String connectedThingstoString(Role myRole, OpdProject opdProject) {
		ArrayList connectedThings = connectedThings(myRole, opdProject);
		String names = "";
		for (int j = 0; j < connectedThings.size(); j++) {
			names = names + ((OpmEntity) connectedThings.get(j)).getName();
			if (j < connectedThings.size() - 1) {
				names = names + ",";
			}
		}
		return names;
	}

	public String getExtID() {
		return extID;
	}

}
