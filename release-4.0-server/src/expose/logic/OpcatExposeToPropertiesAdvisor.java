package expose.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import expose.OpcatExposeConstants.OPCAT_EXPOSE_LINK_DIRECTION;

import gui.dataProject.DataCreatorType;
import gui.dataProject.DataProject;
import gui.metaLibraries.logic.MetaException;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmStructuralRelation;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.util.OpcatLogger;

public class OpcatExposeToPropertiesAdvisor implements
		OpcatExposeAdvisorInterface {

	private ConnectionEdgeInstance instance;

	public OpcatExposeToPropertiesAdvisor(ConnectionEdgeInstance instance) {
		super();
		this.instance = instance;
	}

	public ArrayList<OpcatExposeAdvice> getAdvices() {

		ArrayList<OpcatExposeAdvice> roleSons = new ArrayList<OpcatExposeAdvice>();

		MetaLibrary meta = instance.getEntry().getMyProject().getMetaManager()
				.createNewMetaLibraryReference(
						MetaLibrary.TYPE_POLICY,
						new Object[] {
								instance.getEntry().getMyProject().getPath(),
								instance.getEntry().getMyProject() },
						DataCreatorType.DATA_TYPE_OPCAT_LIBRARAY,
						DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE);
		meta.load();
		try {
			Role role = new Role(instance.getEntry().getId(), meta);
			roleSons.addAll(getAdvices(role, false));
		} catch (MetaException e) {
			OpcatLogger.logError(e);
		}

		return roleSons;

	}

	private ArrayList<OpcatExposeAdvice> getAdvices(Role role,
			boolean getDirectSons) {

		ArrayList<OpcatExposeAdvice> sons = new ArrayList<OpcatExposeAdvice>();

		OpdProject project = (OpdProject) role.getOntology().getProjectHolder();

		Entry entry = project.getSystemStructure().getEntry(role.getThingId());

		project.getMetaManager().refresh(project, null);

		ConnectionEdgeEntry thing = (ConnectionEdgeEntry) entry;

		if (thing == null) {
			return sons;
		}

		HashMap<Entry, OpmStructuralRelation> directSons;
		if (getDirectSons) {
			directSons = ((ConnectionEdgeEntry) thing).getDirectParents();
		} else {
			directSons = new HashMap<Entry, OpmStructuralRelation>();
		}

		// HashMap<Role, OpmStructuralRelation> tempRoles = new HashMap<Role,
		// OpmStructuralRelation>();
		ArrayList<OpcatExposeInterfaceItem> tempRoles = new ArrayList<OpcatExposeInterfaceItem>();

		for (Entry i : directSons.keySet()) {
			try {

				Role localRole = new Role(directSons.get(i).getSourceId(), role
						.getOntology());
				OpcatExposeInterfaceItem item = new OpcatExposeInterfaceItem(
						instance, localRole, directSons.get(i),
						OPCAT_EXPOSE_LINK_DIRECTION.TO);
				tempRoles.add(item);

			} catch (MetaException e) {
				OpcatLogger.logError(e);
			}
		}
		sons.add(new OpcatExposeAdvice(role, tempRoles, instance));

		Collection<Role> roles = thing.getLogicalEntity().getRolesManager()
				.getRolesCollection();

		for (Role i : roles) {
			if (i.getOntology() != null) {
				if (i.getOntology().getType() == MetaLibrary.TYPE_POLICY) {
					sons.addAll(getAdvices(i, true));

				}
			} else {
				// library not loaded for some reason.
				// System.out.println("Lib is null: " + i.getLibraryName());
			}
		}

		return sons;
	}

	@Override
	public void changeSourceInstance(ConnectionEdgeInstance newSourceInstance) {
		this.instance = newSourceInstance;
	}
}
