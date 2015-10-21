package expose.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

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
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.Instance;
import gui.projectStructure.ThingEntry;
import gui.util.OpcatLogger;

public class OpcatExposeFromPropertiesAdvisor implements
		OpcatExposeAdvisorInterface {

	private ConnectionEdgeInstance instance;

	public OpcatExposeFromPropertiesAdvisor(ConnectionEdgeInstance instance) {
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
		meta.setHidden(true);
		meta.load();
		try {
			Role role = new Role(instance.getEntry().getId(), meta);
			roleSons.addAll(getAdvices(role, false));
		} catch (MetaException e) {
			OpcatLogger.logError(e);
		}

		// clean(roleSons);
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
			directSons = ((ThingEntry) thing).getDirectSonsWithOutInharatence();
		} else {
			directSons = new HashMap<Entry, OpmStructuralRelation>();
		}
		ArrayList<OpcatExposeInterfaceItem> tempRoles = new ArrayList<OpcatExposeInterfaceItem>();

		for (Entry i : directSons.keySet()) {
			try {

				if (!i.getInstances().nextElement().isAdvisor()) {
					Role localRole = new Role(directSons.get(i)
							.getDestinationId(), role.getOntology());
					OpcatExposeInterfaceItem item = new OpcatExposeInterfaceItem(
							instance, localRole, directSons.get(i),
							OPCAT_EXPOSE_LINK_DIRECTION.FROM);
					tempRoles.add(item);
				}

			} catch (MetaException e) {
				OpcatLogger.logError(e);
			}
		}

		sons.add(new OpcatExposeAdvice(role, tempRoles, instance));

		HashMap<ConnectionEdgeEntry, FundamentalRelationEntry> parents;
		if (thing instanceof ThingEntry) {
			parents = ((ThingEntry) thing).getAllIneratenceParents();
		} else {
			parents = new HashMap<ConnectionEdgeEntry, FundamentalRelationEntry>();
		}

		for (ConnectionEdgeEntry parent : parents.keySet()) {

			MetaLibrary meta = project.getMetaManager()
					.createNewMetaLibraryReference(MetaLibrary.TYPE_POLICY,
							new Object[] { project.getPath(), project },
							DataCreatorType.DATA_TYPE_OPCAT_LIBRARAY,
							DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE);
			try {
				meta.load();
				Role parentRole = new Role(parent.getId(), meta);
				sons.addAll(getAdvices(parentRole, true));
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}

		Collection<Role> roles = thing.getLogicalEntity().getRolesManager()
				.getRolesCollection();

		for (Role i : roles) {
			if (i.getOntology() != null) {
				if (i.getOntology().getType() == MetaLibrary.TYPE_POLICY) {
					// if (addRoles) {
					sons.addAll(getAdvices(i, true));
					// }
				}
			} else {
				// library not loaded for some reason.
				// System.out.println("Lib is null: " + i.getLibraryName());
			}
		}

		return sons;
	}

	private void clean(ArrayList<OpcatExposeAdvice> advices) {
		for (OpcatExposeAdvice advice : advices) {
			for (OpcatExposeInterfaceItem inter : advice.getProperties()) {
				for (OpcatExposeAdvice i : advices) {
					if (i.key().equalsIgnoreCase(advice.key()))
						continue;
					OpcatExposeInterfaceItem remove = null;
					for (OpcatExposeInterfaceItem j : i.getProperties()) {
						if ((inter.getRole().getThingId() == i.getSourceEntry()
								.getId())
								&& (((OpdProject) inter.getRole().getOntology()
										.getProjectHolder()).getGlobalID()
										.equalsIgnoreCase(i.getSourceEntry()
												.getMyProject().getGlobalID()))) {
							// remove j
							remove = j;
							break;
						}
					}
					if (remove != null) {
						i.getProperties().remove(remove);
					}
				}
			}
		}
	}

	@Override
	public void changeSourceInstance(ConnectionEdgeInstance newSourceInstance) {
		this.instance = newSourceInstance;
	}
}
