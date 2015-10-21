package gui.metaLibraries.logic;

import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXObjectInstance;
import expose.OpcatExposeKey;
import gui.Opcat2;
import gui.controls.FileControl;
import gui.dataProject.DataCreatorType;
import gui.dataProject.DataProject;
import gui.dataProject.DataAbstractItem;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmState;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import org.tmatesoft.svn.core.SVNURL;

import modelControl.OpcatMCManager;

/**
 * The class represents a a knowledge about a domain - captured as an OPM model.
 * Meta-libraries can be used to define the structure of OPM models by importing
 * the meta-libraries into the OPM model and assigning things from the
 * meta-libraries as roles of things from the OPM models. Roles are represtned
 * using the {@link Role} class. The OPM model can be validated against the
 * meta-library, insuring that the structure of the meta-library is reflected as
 * the relations between roles in the model.
 * <p>
 * The meta-library class is used to reference and contain an OPM model project.
 * It allows access to the elements of the model, and exposes methods that
 * retrieve the possible roles of the ontology.
 * <p>
 * Each meta-libraryis is identified by a reference ID (<code>mlibID</code>)
 * which identifies the ontology in the context of the current project.
 * 
 * @see Role
 * @creator Eran Toch
 */
public class MetaLibrary implements Comparable {

	private boolean hidden = false;

	public static final int TYPE_POLICY = 1;
	public static final int TYPE_CLASSIFICATION = 2;

	// ************** Possible states for the library *************
	/**
	 * State initialized is a default state, which does not carry a special
	 * operation.
	 */
	public static final int STATE_INIT = 0;

	/**
	 * The meta-library was not loaded and serves as a reference. A path and
	 * type variables should have been set. A call to
	 * <code>MetaManager.refresh()</code> should load all the libraries with
	 * this state. Note that the meta-library is in a transient state, and
	 * cannot serve for roles.
	 */
	public static final int STATE_REFERENCE = 1;

	/**
	 * The meta-library was loaded successfully, and can serve as a basis for
	 * roles.
	 */
	public static final int STATE_LOADED = 2;

	/**
	 * The meta-library had failed to load, and cannot serve as a basis for
	 * roles.
	 */
	public static final int STATE_LOAD_FAILED = 3;

	/**
	 * The meta-library should be removed by the next
	 * <code>MetaManager.refresh()</code>. Note that the meta-library is in a
	 * transient state, and cannot serve for roles.
	 */
	public static final int STATE_REMOVED = 4;

	// /**
	// * The meta-library was edited and should be removed (after the
	// assigned roles removed)
	// * by the next <code>MetaManager.refresh()</code> call.
	// * Note that the meta-library is in a transient state, and cannot
	// serve for roles.
	// */
	// public static final int STATE_EDITED = 5;

	/**
	 * A reference to the project that contains the meta-library's OPM model.
	 */
	private DataProject dataProject = null;

	/**
	 * Stores the state of the library (<code>STATE_REFERENCE</code>,
	 * <code>STATE_LOADED</code> etc.
	 */
	private int state = STATE_INIT;

	/**
	 * The ontology data type
	 */
	private int type = TYPE_POLICY;

	/**
	 * The ontology recieves a projet holder and an internal ontology ID as
	 * input.
	 * 
	 */
	public MetaLibrary(int libraryType, Object param, int dataType,
			int dataReferenceType) {
		super();

		DataCreatorType creator = new DataCreatorType(dataType,
				dataReferenceType);

		this.dataProject = new DataProject(param, creator);

		this.type = libraryType;
		this.state = STATE_REFERENCE;
	}

	public long getID() {
		return dataProject.getID().hashCode();
	}

	public String getDataID() {
		return dataProject.getID();
	}

	/**
	 * Sets a new state for this library.
	 * 
	 * @param newState
	 *            The new state. Must be one of the constant states listed in
	 *            the class reference.
	 */
	protected void setState(int newState) {
		this.state = newState;
	}

	/**
	 * Retrieves all the Objects of the ontology as Role elements.
	 * 
	 * @return An Enumeration object containing Role objects.
	 */
	public Enumeration getAllObjectRoles() {
		return null;
	}

	/**
	 * Retrieves all the Process of the ontology as Role elements.
	 * 
	 * @return An Enumeration object containing Role objects.
	 */
	public Enumeration getAllProcessRoles() {
		return null;
	}

	/**
	 * Returns the ISystemStructure interface of the project holder.
	 */
	public ISystemStructure getStructure() {
		return dataProject.getDataHolder().getISystemStructure();
	}

	/**
	 * Returns a collection of the roles.
	 */
	public Collection<Role> getRolesCollection() throws MetaException {
		if (dataProject.getDataHolder() == null) {
			throw new MetaException("Library is not loaded", this.getPath());
		}

		Collection<Role> things = new ArrayList<Role>();

		Iterator iter = dataProject.getDataHolder().getDataComponents()
				.iterator();
		while (iter.hasNext()) {
			DataAbstractItem req = (DataAbstractItem) iter.next();
			things.add(new Role(req, this));
		}
		// }

		return things;
	}

	/**
	 * Returns a collection of the roles.
	 */
	public ArrayList<Role> getRolesInProject(OpdProject project)
			throws MetaException {

		if (dataProject.getDataHolder() == null) {
			this.load();
		}

		ArrayList<Role> ret = new ArrayList<Role>();
		Collection<Role> things = getRolesCollection();

		Enumeration<Entry> entries = project.getSystemStructure()
				.getAllElements();

		while (entries.hasMoreElements()) {
			Entry entry = entries.nextElement();
			for (Role i : entry.getLogicalEntity().getRolesManager()
					.getRolesCollection()) {
				for (Role myRole : things) {
					if (myRole.equals(i))
						ret.add(i);
				}
			}
		}

		return ret;
	}

	/**
	 * returns the thing which it's id is passed as itemID. the thing could be
	 * the opmEntity for a metalibrary of a dataitem for classification
	 * libraries
	 * 
	 * @param itemID
	 * @return
	 * @throws MetaException
	 */
	public DataAbstractItem getMEtaDataItem(int itemID) throws MetaException {
		if (dataProject.getDataHolder() == null) {
			throw new MetaException("Library is not loaded", this.getPath());
		}

		Collection<Role> things = new ArrayList<Role>();

		Iterator iter = dataProject.getDataHolder().getDataComponents()
				.iterator();
		while (iter.hasNext()) {
			DataAbstractItem req = (DataAbstractItem) iter.next();
			if (req.getId() == itemID) {
				return req;
			}
		}
		// }

		return null;
	}

	/**
	 * Checks if another ontology is the same one as this one. The comparison is
	 * carried out according to the name of the ontology. This method is not
	 * correct, of course. Any ideas?
	 * 
	 * @param otherOnto
	 *            the MetaLibrary to be compared.
	 * @return <code>true</code> if they are equals, <code>false</code>
	 *         otherwise.
	 */
	public boolean equals(MetaLibrary otherOnto) {
		if (otherOnto.getName() == this.getName()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the name of the ontology - the name of the project holder.
	 */
	public String getName() {
		if (dataProject == null) {
			return "null-ontology";
		}
		return dataProject.getName();
	}

	/**
	 * Returns the project holder (the OPM model of the ontology) as an
	 * <code>OpdProject</code>.
	 */
	public Object getProjectHolder() {
		if (dataProject.getDataHolder() == null) {
			return null;
		}
		return dataProject.getDataHolder().getData();
	}

	/**
	 * Returns the project holder (the OPM model of the ontology) as an
	 * <code>ISystem</code>.
	 */
	public ISystem getISystem() {
		return dataProject.getDataHolder().getISystem();
	}

	/**
	 * Gets the reference type.
	 * 
	 * @return int The type of the reference (TYPE_FILE or TYPE_URL).
	 * @see #FILE_TYPE_FILE
	 * @see #REFERENCE_TYPE_URL
	 */
	public int getReferenceType() {
		return dataProject.getSourceType().getReferenceType();
	}

	public DataProject getDataProject() {
		return dataProject;
	}

	/**
	 * Sets the reference type.
	 * 
	 * @param referenceType
	 *            int
	 */
	public void setReferenceType(int referenceType) {
		dataProject.getSourceType().setReferenceType(referenceType);
	}

	/**
	 * Gets the path of the meta-library reference.
	 * 
	 * @return String
	 */
	public String getPath() {
		return dataProject.getPath();
	}

	/**
	 * Returns the state of the Meta-Library.
	 * 
	 * @return One of the states of Meta-libraries.
	 */
	public int getState() {
		return this.state;
	}

	/**
	 * Finalizing a <code>MetaLibrary</code>, nulls the attached
	 * <code>OpdProject</code>.
	 */
	public void finalize() {
		dataProject = null;
	}

	/**
	 * Checks whether the project that represent the library is
	 * <code>null</code>.
	 * 
	 * @return
	 */
	public boolean projectIsNull() {
		if (dataProject == null) {
			return true;
		}
		return false;
	}

	public int getType() {
		return this.type;
	}

	public boolean isPolicy() {
		return (getType() == TYPE_POLICY);
	}

	public String toString() {
		return this.getName();
	}

	public int compareTo(Object o) {
		return this.toString().compareTo(o.toString());
	}

	private static ProcessInstance insertProcessInstance(Role insertedRole,
			boolean keepConnected, OpdProject targetProject, Opd targetOpd,
			int x, int y, ConnectionEdgeInstance parent,
			boolean doExistingChecks) {

		ProcessInstance newInstance = null;

		OpdProject metaProject = (OpdProject) insertedRole.getOntology()
				.getProjectHolder();

		ConnectionEdgeEntry roleThing = (ConnectionEdgeEntry) metaProject
				.getSystemStructure().getEntry(insertedRole.getThingId());

		ArrayList<Instance> instancesofRoleInOPD = new ArrayList<Instance>();
		if (doExistingChecks) {
			try {
				instancesofRoleInOPD = targetOpd
						.getInstancesInOPDOfRole(insertedRole);
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}
		if (!keepConnected || instancesofRoleInOPD.size() == 0) {
			if (parent != null) {
				newInstance = (ProcessInstance) targetProject.addProcess(
						targetOpd, roleThing.getName(), x, y,
						(ThingInstance) parent);
			} else {
				newInstance = (ProcessInstance) targetProject.addProcess(
						roleThing.getName(), x, y, targetOpd.getOpdId());
			}

			((OpmProcess) ((ProcessEntry) ((ProcessInstance) newInstance)
					.getEntry()).getLogicalEntity())
					.copyPropsFrom((OpmProcess) ((ProcessEntry) roleThing)
							.getLogicalEntity());

			if (keepConnected) {
				try {
					newInstance.getEntry().getLogicalEntity().getRolesManager()
							.addRole(insertedRole);
				} catch (Exception ex) {
					OpcatLogger.logError(ex);
				}
			} else {
				newInstance.getEntry().getLogicalEntity().getRolesManager()
						.clear();
			}

		} else {
			newInstance = (ProcessInstance) instancesofRoleInOPD.get(0);
		}

		return newInstance;
	}

	private static StateInstance insertStateInstance(Role insertedRole,
			boolean keepConnected, OpdProject targetProject, Opd targetOpd,
			int x, int y, ConnectionEdgeInstance parent,
			boolean doExistingChecks) {

		StateInstance newInstance = null;

		OpdProject metaProject = (OpdProject) insertedRole.getOntology()
				.getProjectHolder();

		ConnectionEdgeEntry roleThing = (ConnectionEdgeEntry) metaProject
				.getSystemStructure().getEntry(insertedRole.getThingId());

		IXConnectionEdgeInstance newObjectInstance = null;

		ObjectEntry object = ((StateEntry) roleThing).getParentObject();

		/**
		 * see if the object with this role is in the targetOPD
		 */
		ArrayList<Instance> instancesofRoleInOPD = new ArrayList<Instance>();
		if (doExistingChecks) {
			try {
				instancesofRoleInOPD = targetOpd
						.getInstancesInOPDOfRole(new Role(object.getId(),
								insertedRole.getOntology()));
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}

		boolean insertToExsitingObject = false;
		if (keepConnected && instancesofRoleInOPD.size() > 0) {
			newObjectInstance = (IXObjectInstance) instancesofRoleInOPD.get(0);
			insertToExsitingObject = true;

		} else {
			if (parent != null) {
				newObjectInstance = targetProject.addObject(targetOpd, x, y,
						parent.getGraphicalRepresentation(), -1, -1, true);
			} else {
				newObjectInstance = targetProject.addObject(targetOpd, x, y,
						targetOpd.getDrawingArea(), -1, -1, true);
			}

			/**
         * 
         */

			((OpmObject) ((ObjectInstance) newObjectInstance).getEntry()
					.getLogicalEntity()).copyPropsFrom((OpmObject) object
					.getLogicalEntity());

			if (keepConnected) {
				try {
					((ObjectInstance) newObjectInstance).getEntry()
							.getLogicalEntity().getRolesManager().addRole(
									new Role(object.getId(), insertedRole
											.getOntology()));
				} catch (MetaException e) {
					OpcatLogger.logError(e);
				}
			} else {
				((ObjectInstance) newObjectInstance).getEntry()
						.getLogicalEntity().getRolesManager().clear();
			}
		}

		Iterator iter = Collections.list(object.getStateEnum()).iterator();

		while (iter.hasNext()) {
			OpmState state = (OpmState) iter.next();

			StateEntry stateEntry = null;

			try {
				Role stateRole = new Role(state.getId(), insertedRole
						.getOntology());

				ArrayList<Instance> stateInstances = new ArrayList<Instance>();
				if (doExistingChecks) {
					stateInstances = targetOpd
							.getInstancesInOPDOfRole(stateRole);
				}
				if (!keepConnected || stateInstances.size() == 0) {
					stateEntry = (StateEntry) targetProject.addState(state
							.getName(), ((ObjectInstance) newObjectInstance));

					if (keepConnected) {
						stateEntry.addRole(new Role(state.getId(), insertedRole
								.getOntology()));
					} else if (!insertToExsitingObject) {
						stateEntry.getLogicalEntity().getRolesManager().clear();
					}

				} else {
					stateEntry = (StateEntry) stateInstances.get(0).getEntry();
				}

			} catch (MetaException e) {
				OpcatLogger.logError(e);
			}
		}

		ObjectEntry locObject = (ObjectEntry) newObjectInstance.getIXEntry();

		StateEntry mystate = locObject.getState(roleThing.getName());

		newInstance = (StateInstance) mystate.getInstanceInOPD(targetOpd);

		((ObjectInstance) newObjectInstance).getConnectionEdge().fitToContent();

		return newInstance;
	}

	private static ObjectInstance insertObjectInstance(Role insertedRole,
			boolean keepConnected, OpdProject targetProject, Opd targetOpd,
			int x, int y, ConnectionEdgeInstance parent,
			boolean doExistingChecks) {

		ObjectInstance newInstance = null;

		OpdProject metaProject = (OpdProject) insertedRole.getOntology()
				.getProjectHolder();

		ConnectionEdgeEntry roleThing = (ConnectionEdgeEntry) metaProject
				.getSystemStructure().getEntry(insertedRole.getThingId());

		ArrayList<Instance> instancesofRoleInOPD = new ArrayList<Instance>();
		if (doExistingChecks) {
			try {
				instancesofRoleInOPD = targetOpd
						.getInstancesInOPDOfRole(insertedRole);
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}

		/**
		 * ugly but does the job
		 */
		ObjectEntry object = (ObjectEntry) roleThing;
		/**
         * 
         */

		if (!keepConnected || instancesofRoleInOPD.size() == 0) {
			if (parent != null) {
				newInstance = targetProject.addObject(targetOpd, x, y, parent
						.getGraphicalRepresentation(), -1, -1, true);
			} else {
				newInstance = targetProject.addObject(targetOpd, x, y,
						targetOpd.getDrawingArea(), -1, -1, true);
			}

			((OpmObject) ((ObjectInstance) newInstance).getEntry()
					.getLogicalEntity()).copyPropsFrom((OpmObject) object
					.getLogicalEntity());

			if (keepConnected) {
				newInstance.getEntry().getLogicalEntity().getRolesManager()
						.addRole(insertedRole);
			} else {
				newInstance.getEntry().getLogicalEntity().getRolesManager()
						.clear();
			}
		} else {
			newInstance = (ObjectInstance) instancesofRoleInOPD.get(0);
		}

		Iterator iter = Collections.list(object.getStateEnum()).iterator();

		while (iter.hasNext()) {

			OpmState state = (OpmState) iter.next();

			try {
				Role stateRole = new Role(state.getId(), insertedRole
						.getOntology());

				if (doExistingChecks) {
					instancesofRoleInOPD = targetOpd
							.getInstancesInOPDOfRole(stateRole);
				}

				StateEntry stateEntry;
				if (!keepConnected || instancesofRoleInOPD.size() == 0) {
					stateEntry = (StateEntry) targetProject.addState(state
							.getName(), ((ObjectInstance) newInstance));
				} else {
					stateEntry = (StateEntry) instancesofRoleInOPD.get(0)
							.getEntry();

				}

				if (keepConnected) {
					stateEntry.getLogicalEntity().getRolesManager().addRole(
							stateRole);
				} else {
					stateEntry.getLogicalEntity().getRolesManager().clear();
				}
			} catch (Exception ex) {
				continue;
			}

		}

		return newInstance;
	}

	public static ConnectionEdgeInstance insertConnectionEdge(
			Role insertedRole, OpdProject targetProject, Opd targetOpd,
			boolean keepConnected, ConnectionEdgeInstance relativeInstance,
			ConnectionEdgeInstance parent, boolean doExistingChecks, int x,
			int y) throws Exception {

		if (targetProject.getOpdByID(targetOpd.getOpdId()) == null) {
			throw (new Exception("Opd does not exist in target model"));
		}

		if (relativeInstance != null) {
			x = relativeInstance.getX() + relativeInstance.getWidth() + 30;
			y = relativeInstance.getY() + relativeInstance.getHeight() + 30;
		}
		ConnectionEdgeInstance newInstance = null;

		OpdProject metaProject = (OpdProject) insertedRole.getOntology()
				.getProjectHolder();

		ConnectionEdgeEntry roleThing = (ConnectionEdgeEntry) metaProject
				.getSystemStructure().getEntry(insertedRole.getThingId());

		if (roleThing instanceof ObjectEntry) {

			newInstance = insertObjectInstance(insertedRole, keepConnected,
					targetProject, targetOpd, x, y, parent, doExistingChecks);

		} else if (roleThing instanceof StateEntry) {

			newInstance = insertStateInstance(insertedRole, keepConnected,
					targetProject, targetOpd, x, y, parent, doExistingChecks);

		} else {

			newInstance = insertProcessInstance(insertedRole, keepConnected,
					targetProject, targetOpd, x, y, parent, doExistingChecks);
		}

		return newInstance;

	}

	public static ConnectionEdgeInstance insertConnectionEdge(
			Role insertedRole, Opd targetOpd, boolean keepConnected,
			ConnectionEdgeInstance relativeInstance,
			ConnectionEdgeInstance parent, boolean doExistingChecks, int x,
			int y) throws Exception {

		return insertConnectionEdge(insertedRole, FileControl.getInstance()
				.getCurrentProject(), targetOpd, keepConnected,
				relativeInstance, parent, doExistingChecks, x, y);

	}

	public static ConnectionEdgeInstance insertConnectionEdge(
			Role insertedRole, Opd targetOpd, boolean keepConnected,
			ConnectionEdgeInstance relativeInstance,
			ConnectionEdgeInstance parent, boolean doExistingChecks)
			throws Exception {
		return insertConnectionEdge(insertedRole, targetOpd, keepConnected,
				relativeInstance, parent, doExistingChecks, 100, 100);

	}

	/**
	 * Create a new ConnectionEgdeInstance in targetOpd from the Role. (i.e.
	 * insert from metalib action) if keepConected is true the new object will
	 * be assigned the insertedRole.
	 * 
	 * @param insertedRole
	 * @param targetOpd
	 * @param keepConnected
	 * @param relativeInstance
	 *            if adding a process the this is used also as the parent of the
	 *            process the place of the new instance is calculated relative
	 *            to this instance if null the 0, 0 is taken
	 * @return
	 * @throws Exception
	 */
	public static ConnectionEdgeInstance insertConnectionEdge(
			Role insertedRole, Opd targetOpd, boolean keepConnected,
			ConnectionEdgeInstance relativeInstance,
			ConnectionEdgeInstance parent) throws Exception {

		return insertConnectionEdge(insertedRole, targetOpd, keepConnected,
				relativeInstance, parent, true);
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isTemplate() {
		return !hidden;
	}

	public boolean load() {
		if (dataProject == null) {
			this.setState(MetaLibrary.STATE_LOAD_FAILED);
			return false;
		}
		dataProject.load();
		try {
			if (dataProject.getStatus().isLoadFail()) {
				this.setState(MetaLibrary.STATE_LOAD_FAILED);
				if (!isHidden())
					OpcatLogger.logError(dataProject.getStatus()
							.getFailReason());
			} else {
				this.setState(MetaLibrary.STATE_LOADED);
			}
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}

		return true;
	}

}