package expose.logic;

import expose.OpcatExposeConstants.OPCAT_EXPOSE_LINK_DIRECTION;
import gui.controls.FileControl;
import gui.dataProject.DataCreatorType;
import gui.dataProject.DataProject;
import gui.metaLibraries.logic.MetaException;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdGraphics.opdBaseComponents.OpdObject;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmProceduralLink;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.ThingEntry;
import gui.util.OpcatLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class OpcatExposeGlobalInterfaceAdvisor implements
		OpcatExposeAdvisorInterface {

	private ConnectionEdgeInstance instance;

	public OpcatExposeGlobalInterfaceAdvisor(ConnectionEdgeInstance instance) {
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

		HashMap<OpmProceduralLink, OPCAT_EXPOSE_LINK_DIRECTION> directSons;
		if (getDirectSons) {
			directSons = thing.getDirectInterface();
		} else {
			directSons = new HashMap<OpmProceduralLink, OPCAT_EXPOSE_LINK_DIRECTION>();
		}

		ArrayList<OpcatExposeInterfaceItem> tempRoles = new ArrayList<OpcatExposeInterfaceItem>();
		for (OpmProceduralLink i : directSons.keySet()) {
			try {

				if (directSons.get(i) == OPCAT_EXPOSE_LINK_DIRECTION.FROM) {
					// Entry destination = ((OpdProject) (role.getOntology()
					// .getProjectHolder())).getSystemStructure()
					// .getEntry(i.getDestinationId());
					// if (destination.getLogicalEntity().getRolesManager()
					// .getRolesVector(MetaLibrary.TYPE_POLICY).size() == 0) {
					Role localRole = new Role(i.getDestinationId(), role
							.getOntology());
					OpcatExposeInterfaceItem item = new OpcatExposeInterfaceItem(
							instance, localRole, i, directSons.get(i));
					tempRoles.add(item);
					// }
				} else {
					// Entry source = ((OpdProject) (role.getOntology()
					// .getProjectHolder())).getSystemStructure()
					// .getEntry(i.getSourceId());
					// if (source.getLogicalEntity().getRolesManager()
					// .getRolesVector(MetaLibrary.TYPE_POLICY).size() == 0) {
					Role localRole = new Role(i.getSourceId(), role
							.getOntology());
					OpcatExposeInterfaceItem item = new OpcatExposeInterfaceItem(
							instance, localRole, i, directSons.get(i));
					tempRoles.add(item);
					// }
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
				meta.setHidden(true);
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
					Role source = (getSourceRole(i) == null ? i
							: getSourceRole(i));
					sons.addAll(getAdvices(source, true));

				}
			} else {
				// library not loaded for some reason.
				// System.out.println("Lib is null: " + i.getLibraryName());
			}
		}

		// for (Role i : roles) {
		// MetaLibrary m = project.getMetaManager().getMetaLibrary(
		// i.getLibraryId());
		// try {
		// if (m != null) {
		// m.load();
		// i.setOntology(m);
		// // i.getOntology().load();
		// sons.addAll(getAdvices(i, true));
		// } else {
		// // throw (new Exception("Connected model not found : "
		// // + i.getLibraryName()));
		// }
		// } catch (Exception ex) {
		// OpcatLogger.logError(ex);
		// }
		// }

		return sons;
	}

	private Role getSourceRole(Role role) {

		OpdProject project = (OpdProject) role.getOntology().getProjectHolder();

		Entry entry = project.getSystemStructure().getEntry(role.getThingId());

		project.getMetaManager().refresh(project, null);

		ConnectionEdgeEntry thing = (ConnectionEdgeEntry) entry;

		if (thing == null) {
			return null;
		}

		Collection<Role> roles = thing.getLogicalEntity().getRolesManager()
				.getRolesCollection();
		if (roles.size() > 0) {
			// should be only one role
			Role iter = roles.iterator().next();
			if (iter.getOntology() != null) {
				if (iter.getOntology().getType() == MetaLibrary.TYPE_POLICY) {
					return getSourceRole(iter);
				}
			}
		} else {
			return role;
		}

		return null;
	}

	@Override
	public void changeSourceInstance(ConnectionEdgeInstance newSourceInstance) {
		this.instance = newSourceInstance;
	}
}
