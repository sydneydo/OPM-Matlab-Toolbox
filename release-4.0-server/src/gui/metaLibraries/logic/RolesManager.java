package gui.metaLibraries.logic;

import exportedAPI.opcatAPIx.IXRole;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import gui.projectStructure.Entry;
import gui.projectStructure.RoleEntry;
import gui.util.OpcatLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 * Manages a list of {@link Role} objects.
 * 
 * @author Eran Toch Created: 01/05/2004
 */
public class RolesManager {
	/**
	 * The key name for the thing name representation.
	 */
	public static final String THING_NAME = "thing";

	/**
	 * The key name for the library name representation.
	 */
	public static final String LIBRARY_NAME = "library";

	/**
	 * A list of roles that the current thing acts as.
	 */
	protected Vector<Role> ontoRoles = null;

	/**
	 * Empty Constructor for the class.
	 * 
	 */

	private OpmEntity myEntity;

	public RolesManager(OpmEntity opmEntity) {
		this.myEntity = opmEntity;
	}

	/**
	 * Creating the <code>ontoRoles</code> list, if it is null. The aim of this
	 * method is to avoid creating the list if no thing uses it.
	 * 
	 */
	protected void lazyInit() {
		if (this.ontoRoles == null) {
			this.ontoRoles = new Vector<Role>();
		}
	}

	/**
	 * Checks if the list contains roles.
	 * 
	 * @return <code>true</code> if there are any roles, <code>false</code>
	 *         otherwise.
	 */
	public boolean hasRoles() {
		if (this.ontoRoles == null) {
			return false;
		} else if (this.ontoRoles.size() == 0) {
			return false;
		}
		return true;
	}

	public boolean hasRoles(int metaLibType) {
		Iterator rolesIter = ontoRoles.iterator();
		while (rolesIter.hasNext()) {
			Role role = (Role) rolesIter.next();
			if (role.isLoaded()
					&& (role.getOntology().getType() == metaLibType)) {
				return true;
			}
		}
		return false;
	}

	public void clear() {
		lazyInit();
		ontoRoles.clear();
	}

	/**
	 * Sets an ontology role for the thing. A thing might have several roles,
	 * and calling the method multiple times will result in multiple roles being
	 * added. If the role is already in the list, it will not be added again.
	 * 
	 * @param role
	 *            The role to be added.
	 * @author Eran Toch
	 */
	public void addRole(Role role) {
		this.lazyInit();
		if (!this.ontoRoles.contains(role)) {
			this.ontoRoles.add(role);
		}
	}

	/**
	 * Adds an {@link IXRole} to the list. The method converts the role to the
	 * implemented <code>Role</code> or <code>RoleEntry</code> form and performs
	 * the original method.
	 * 
	 * @param _role
	 *            The role to be added.
	 */
	public void addRole(IXRole _role) {
		Role role;
		try {
			role = (Role) _role;
		} catch (ClassCastException ex) {
			RoleEntry entry = (RoleEntry) _role;
			role = entry.getLogicalRole();
		}
		this.addRole(role);
	}

	/**
	 * Removes a role from the list.
	 * 
	 * @param role
	 *            The role to be removed.
	 */
	public void removeRole(Role role) {
		if (this.ontoRoles != null) {
			this.ontoRoles.remove(role);
		}
	}

	/**
	 * Removes an {@link IXRole} from the list. The method converts the role to
	 * the implemented <code>Role</code> form and performs the original method.
	 * 
	 * @param role
	 *            The role to be removed.
	 */
	public void removeRole(IXRole _role) {
		Role role = (Role) _role;
		this.removeRole(role);
	}

	/**
	 * Returns an enumeration of all the roles of the thing.
	 * 
	 * @return Enumeration containing Role objects. If no roles exist, the
	 *         method returns an empty enumeration. The roles are ordered
	 *         according to the order of their insertion.
	 * @author Eran Toch
	 */
	public Enumeration<Role> getRoles() {
		this.lazyInit();
		return new Vector<Role>(this.ontoRoles).elements();
	}

	public Enumeration<Role> getLoadedRoles() {
		this.lazyInit();

		Iterator<Role> iter = this.ontoRoles.iterator();
		ArrayList<Role> roles = new ArrayList<Role>();

		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			if (role.isLoaded()) {
				roles.add(role);
			}
		}
		return new Vector<Role>(roles).elements();
	}

	/**
	 * Returns a collection of all the roles of the thing.
	 * 
	 * @return <code>Vector</code> containing Role objects. If no roles exist,
	 *         the method returns null. The roles are ordered according to the
	 *         order of their insertion.
	 * @author Eran Toch
	 */
	public Collection<Role> getRolesCollection() {

		this.lazyInit();

		return this.ontoRoles;

	}

	/**
	 * Returns a Vector of all the roles of the thing.
	 * 
	 * @return <code>Vector</code> containing Role objects. If no roles exist,
	 *         the method returns null. The roles are ordered according to the
	 *         order of their insertion.
	 * @author Eran Toch
	 */
	public Vector getRolesVector() {
		this.lazyInit();
		return this.ontoRoles;
	}

	public Vector<Role> getRolesVector(int ontoType) {
		this.lazyInit();
		Vector<Role> selectedRoles = new Vector<Role>();
		Iterator iter = this.ontoRoles.iterator();
		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			if (role.getOntology().getType() == ontoType) {
				selectedRoles.add(role);
			}
		}

		return selectedRoles;
	}

	public Vector<Role> getRolesVector(int ontoType, long ontGlobID) {
		this.lazyInit();
		Vector<Role> selectedRoles = new Vector<Role>();
		Iterator<Role> iter = this.ontoRoles.iterator();
		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			if ((role.getOntology() != null)
					&& (role.getOntology().getID() == ontGlobID)
					&& (role.getOntology().getType() == ontoType)) {
				selectedRoles.add(role);
			}
		}

		return selectedRoles;
	}

	public Vector<Role> getRolesVector(long ontGlobID) {
		this.lazyInit();
		Vector<Role> selectedRoles = new Vector<Role>();
		Iterator<Role> iter = this.ontoRoles.iterator();
		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			if (role.getOntology() == null) {
				continue;
			}
			try {
				if (role.getOntology().getID() == ontGlobID) {
					selectedRoles.add(role);
				}
			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}

		return selectedRoles;
	}

	public Vector<Role> getLoadedRolesVector(int ontoType) {
		this.lazyInit();
		Vector<Role> selectedRoles = new Vector<Role>();
		Iterator<Role> iter = this.ontoRoles.iterator();
		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			if (role.isLoaded()) {
				if (role.getOntology().getType() == ontoType) {
					selectedRoles.add(role);
				}
			}
		}

		return selectedRoles;
	}

	public Vector<Role> getLoadedPoliciesRolesVector() {
		this.lazyInit();
		Vector<Role> selectedRoles = new Vector<Role>();
		Iterator<Role> iter = this.ontoRoles.iterator();
		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			if (role.isLoaded()) {
				if ((role.getOntology().isPolicy())
						&& (role.getOntology().getType() == MetaLibrary.TYPE_POLICY)) {
					selectedRoles.add(role);
				}
			}
		}

		return selectedRoles;
	}

	public Vector<Role> getNotLoadedRolesVector(int ontoType) {
		this.lazyInit();
		Vector<Role> selectedRoles = new Vector<Role>();
		Iterator<Role> iter = this.ontoRoles.iterator();
		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			if (!role.isLoaded()) {
				// if ((role.getOntology().getType() == ontoType )) {
				selectedRoles.add(role);
				// }
			}
		}

		return selectedRoles;
	}

	/**
	 * Returns an enumeration of roles as {@link RoleEntry} objects.
	 * 
	 * @return An Enumeration containing <code>RoleEntry</code> objects.
	 */
	public Enumeration<RoleEntry> getRoleEntries() {
		this.lazyInit();
		Vector<RoleEntry> entries = new Vector<RoleEntry>();
		Role role;
		for (int i = 0; i < this.ontoRoles.size(); i++) {
			role = (Role) this.ontoRoles.get(i);
			RoleEntry entry = new RoleEntry(role);
			entries.add(entry);
		}
		return entries.elements();
	}

	/**
	 * Renders a string representation of all roles. The method tackles a
	 * situation in which there are multiple roles. In this case, the name of
	 * the meta-library is added to the name of each thing.
	 * 
	 * @author Eran Toch
	 */
	public String getRolesText(int type) {
		if (this.ontoRoles == null) {
			return "";
		}
		String output = "";
		Role role;
		String lastLibName = "";
		for (int i = 0; i < this.ontoRoles.size(); i++) {
			role = (Role) this.ontoRoles.get(i);
			if (role.isLoaded()) {
				if (role.getOntology().getType() == type) {
					if (this.ontoRoles.size() > 1) {
						if (!role.getLibraryName().equals(lastLibName)) {
							output += role.getLibraryName() + ":";
							lastLibName = role.getLibraryName();
						}
					}
					output += role.getThingName();
					if (i < (this.ontoRoles.size() - 1)) {
						output += ",";
					}
				}
			}
		}
		return output;
	}

	public String getPoliciesRolesText() {
		if (this.ontoRoles == null) {
			return "";
		}
		String output = "";
		Role role;
		String lastLibName = "";
		for (int i = 0; i < this.ontoRoles.size(); i++) {
			role = (Role) this.ontoRoles.get(i);
			if (role.isLoaded()) {

				if (role.getOntology().isPolicy()) {
					if (this.ontoRoles.size() > 1) {
						if (!role.getLibraryName().equals(lastLibName)) {
							output += role.getLibraryName() + ":";
							lastLibName = role.getLibraryName();
						}
					}
					output += role.getThingName();
					if (i < (this.ontoRoles.size() - 1)) {
						output += ",";
					}
				}
			}
		}
		return output;
	}

	public String getRolesText() {
		if (this.ontoRoles == null) {
			return "";
		}
		String output = "";
		Role role;
		String lastLibName = "";
		for (int i = 0; i < this.ontoRoles.size(); i++) {
			role = (Role) this.ontoRoles.get(i);
			if (role.isLoaded()) {
				if (this.ontoRoles.size() > 1) {
					if (!role.getLibraryName().equals(lastLibName)) {
						output += role.getLibraryName() + ":";
						lastLibName = role.getLibraryName();
					}
				}
				output += role.getThingName();
				if (i < (this.ontoRoles.size() - 1)) {
					output += ", ";
				}
			}
		}
		return output;
	}

	/**
	 * Checks if a thing has a certain role. If the list is not initialized,
	 * returns false.
	 * 
	 * @param id
	 *            The ID of the role.
	 * @return <code>true</code> if the list contains the role,
	 *         <code>false</code> otherwise.
	 * @author Eran Toch
	 */
	public boolean hasRole(long id) {
		if (this.ontoRoles == null) {
			return false;
		}
		Role runner;
		for (int i = 0; i < this.ontoRoles.size(); i++) {
			runner = (Role) this.ontoRoles.get(i);
			if (runner.getThingId() == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * roles collection including all roles from the roles entries. This is
	 * treating roles as intra-model inheritance.
	 * 
	 * @return
	 */
	public ArrayList<Role> getRolesCollectionIncludingInerated() {
		ArrayList<Role> ret = new ArrayList<Role>();

		Iterator<Role> roles = getRolesCollection().iterator();
		ret.addAll(getRolesCollection());

		while (roles.hasNext()) {
			Role role = roles.next();

			try {
				role.getOntology().load();
			} catch (Exception e) {
				OpcatLogger.logError(e);
				return ret;
			}

			Long entID = (Long) role.getThing().getAdditionalData();
			Entry ent = ((OpdProject) role.getOntology().getProjectHolder())
					.getSystemStructure().getEntry(entID);
			if (ent != null) {
				((OpdProject) role.getOntology().getProjectHolder())
						.getMetaManager().refresh(
								((OpdProject) role.getOntology()
										.getProjectHolder()), null);

				ret.addAll(ent.getLogicalEntity().getRolesManager()
						.getRolesCollectionIncludingInerated());
			}
		}
		return ret;
	}

	public String toString() {
		String output = "RolesManager:\n";
		if (this.ontoRoles != null) {
			Role runner;
			for (int i = 0; i < this.ontoRoles.size(); i++) {
				runner = (Role) this.ontoRoles.get(i);
				output += runner.getThingName() + " of "
						+ runner.getLibraryName() + "\n";
			}
		} else {
			return "null";
		}
		return output;
	}

	/**
	 * Returns a a <code>Vector</code> object containing pairs of Thing name /
	 * Library Name.
	 * 
	 * @return A <code>Vector</code> object holding <code>Property</code>
	 *         object, in which the thing name has the key of
	 *         <code>RolesManager.THING_NAME</code> and the meta-library name is
	 *         keyed under <code>RolesManager.LIBRARY_NAME</code>.
	 */
	public Vector<Properties> getOPLRepresentation() {
		Vector<Properties> output = new Vector<Properties>();
		if (this.ontoRoles != null) {
			Role runner;
			for (int i = 0; i < this.ontoRoles.size(); i++) {
				runner = (Role) this.ontoRoles.get(i);
				Properties prop = new Properties();
				prop.setProperty(THING_NAME, runner.getThingName());
				prop.setProperty(LIBRARY_NAME, runner.getLibraryName());
				output.add(prop);
			}
		}
		return output;
	}

	/**
	 * Checks if the manager contains a specific role.
	 * 
	 * @param role
	 *            The role to be looked for.
	 * @return <code>true</code> if a role exists, <code>false</code> otherwise.
	 */
	public boolean contains(Role role) {
		if (this.ontoRoles == null) {
			return false;
		}
		Iterator it = this.ontoRoles.iterator();
		boolean flag = false;
		while (it.hasNext()) {
			Role runner = (Role) it.next();
			if (runner.equals(role)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * Returns a deep copy of the object.
	 * 
	 * @return <code>RolesManager<ocde> object.
	 */
	public Object clone() {
		RolesManager copy = new RolesManager(myEntity);
		Enumeration locenum = this.getRoles();
		while (locenum.hasMoreElements()) {
			Role role = (Role) locenum.nextElement();
			Role roleCopy = (Role) role.clone();
			copy.addRole(roleCopy);
		}
		return copy;
	}

	/**
	 * Sets the visiable roles (not belonging to hidden libraries) according to
	 * a given <code>Vector</code>.
	 * 
	 * @param roles
	 *            Vector containing <code>Role</code> objects.
	 */
	public void replacePoliciesRoles(Vector<Role> roles) {
		Vector<Role> classificationRoles = new Vector<Role>();
		for (Role role : ontoRoles) {
			if (!(role.getOntology().isPolicy())) {
				classificationRoles.add(role);
			}
		}
		this.ontoRoles = roles;
		this.ontoRoles.addAll(classificationRoles);
	}

	/**
	 * Checks if two role managers are equals. Two role managers are equal if
	 * each of their roles are equals and the order in which the roles are
	 * arranged is the same.
	 */
	public boolean equals(Object otherManager) {
		if (!(otherManager instanceof RolesManager)) {
			return false;
		}
		RolesManager other;
		try {
			other = (RolesManager) otherManager;
		} catch (Exception ex) {
			return false;
		}
		Vector otherRoles = other.getRolesVector();
		if (otherRoles.size() != this.ontoRoles.size()) {
			return false;
		}
		for (int i = 0; i < this.ontoRoles.size(); i++) {
			Role thisRole = (Role) this.ontoRoles.get(i);
			Role otherRole = (Role) otherRoles.get(i);
			if (!thisRole.equals(otherRole)) {
				return false;
			}
		}
		return true;
	}

}
