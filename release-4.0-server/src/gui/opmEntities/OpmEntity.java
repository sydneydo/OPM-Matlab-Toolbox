package gui.opmEntities;

import java.util.Enumeration;
import java.util.Vector;

import gui.dataProject.DataAbstractItem;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.metaLibraries.logic.RolesManager;
import gui.projectStructure.Instance;

//-----------------------------------------------------------------
/**
 * The base class for all OPM logical entities. This class represents
 * logicaly(not graphically) all entities which exist in OPM methodology, and
 * its (and its subclasses) purpose is only to store all general information
 * about this entity. For better understanding of this class you should be
 * familiar with OPM.
 * 
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */
// -----------------------------------------------------------------
public abstract class OpmEntity extends DataAbstractItem {

	/**
	 * A roles manager that manage {@link Role} objects for the current
	 * <code>OpmThing</code>. The manager is set to <code>null</code> and is
	 * initialized only if called by the <code>getRolesManager</code> method.
	 * 
	 * @author Eran Toch
	 */
	protected RolesManager rolesManager = null;

	/**
	 * the freetext role
	 */
	protected String role;

	private boolean exposed = false;

	private boolean privateExposed = false;

	private boolean exposedChanged = false;

	public boolean isExposed() {
		return exposed || privateExposed;
	}

	public boolean isPublicExposed() {
		return exposed;
	}

	public boolean isExposedChanged() {
		return exposedChanged;
	}

	public void setExposedChanged(boolean changed) {
		this.exposedChanged = changed;
	}

	/**
	 * Makes this {@link OpmEntity} exposed, thus making it visible to other
	 * projects for reuse. the exposed entity could be used only after
	 * committing the project to the server.
	 * 
	 * @param exposed
	 */
	public void setPublicExposed(boolean exposed) {
		this.exposed = exposed;
	}

	// ---------------------------------------------------------------------------
	// The private attributes/members are located here
	private long entityId;

	private String entityName;

	private String entityUrl = "";

	private String description;

	private boolean enviromental = false; // true if enviromental, false if
	// system

	private boolean physical = false; // true if enviromental, false if system

	/**
	 * Creates an OpmEntity with specified id and name. Id of created OpmEntity
	 * must be unique in OPCAT system
	 * 
	 * @param id
	 *            entity id
	 * @param name
	 *            entity name
	 */

	public OpmEntity(long id, String name) {
		super(name, id);
		this.entityId = id;
		this.entityName = name;
		this.description = "none";
		this.enviromental = false;
		this.entityUrl = "none";
		this.setAdditionalData(new Long(id));
	}

	public OpmEntity(long id, String name, String url) {
		super(name, id);
		this.entityId = id;
		this.entityName = name;
		this.description = "none";
		this.enviromental = false;
		this.entityUrl = url;
		this.setAdditionalData(new Long(id));
	}

	protected void copyPropsFrom(OpmEntity origin) {
		this.entityName = origin.getName();
		this.description = origin.getDescription();
		this.enviromental = origin.isEnviromental();
		this.entityUrl = origin.getUrl();
	}

	protected boolean hasSameProps(OpmEntity pEntity) {
		return (this.entityName.equals(pEntity.getName())
				&& this.description.equals(pEntity.getDescription())
				&& this.entityUrl.equals(pEntity.getUrl()) && (this.enviromental == pEntity
				.isEnviromental()));
	}

	// ---------------------------------------------------------------------------
	// The public methods are located here

	// --------------------------------------------------------------------------
	/**
	 * Returns the id of the entity.
	 * 
	 * @return a long number represents id of the entity.
	 */

	public long getId() {
		return this.entityId;
	}

	/***********************************************************************
	 * No setter function for entityId - you set the ID in the constructor * and
	 * you can't change it *
	 **********************************************************************/

	// --------------------------------------------------------------------------
	/**
	 * Returns the name of the entity.
	 * 
	 * @return a String represnts entity name
	 */

	public String getName() {
		return this.entityName;
	}

	// --------------------------------------------------------------------------
	/**
	 * Sets the string to be entity name.
	 * 
	 * @param name
	 *            entity name
	 */

	public void setName(String name) {
		this.entityName = name;
	}

	// --------------------------------------------------------------------------
	/**
	 * Returns entity description.
	 * 
	 * @return a String with the entity description
	 */

	public String getDescription() {
		return this.description;
	}

	// --------------------------------------------------------------------------
	/**
	 * Sets the string to be entity description.
	 * 
	 * @param description
	 *            description of the entity
	 */

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the enviromental/system property of OpmEntity. If value of
	 * enviromental is true it's a enviromental thing, otherwise system
	 * 
	 */

	public void setEnviromental(boolean enviromental) {
		this.enviromental = enviromental;
	}

	/**
	 * Returns true if this OpmEntity is enviromental. If it's system returns
	 * false
	 * 
	 */

	public boolean isEnviromental() {
		return this.enviromental;
	}

	// --------------------------------------------------------------------------
	/**
	 * Returns a string representation of the entity.
	 * 
	 * @return a string representation of the entity
	 */

	public String toString() {
		return this.entityName.replace('\n', ' ');
	}

	public boolean equals(Object obj) {

		OpmEntity tempEntity;
		if (!(obj instanceof OpmEntity)) {
			return false;
		}

		tempEntity = (OpmEntity) obj;

		if (tempEntity.getId() == this.getId()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return Returns the entityUrl.
	 */
	public String getUrl() {
		return this.entityUrl;
	}

	/**
	 * @param entityUrl
	 *            The entityUrl to set.
	 */
	public void setUrl(String entityUrl) {
		this.entityUrl = entityUrl;
	}

	/**
	 * Returns the {@link RolesManager}, which manages roles for this
	 * <code>OpmThing</code>. The method creates a new <code>RolesManager</code>
	 * if it was <code>null</code>. Each <code>Thing</code> has one
	 * <code>RolesManager</code>.
	 * 
	 * @return The <code>RolesManager</code> of this Thing.
	 * @author Eran Toch
	 */
	public RolesManager getRolesManager() {
		if (this.rolesManager == null) {
			this.rolesManager = new RolesManager(this);
		}
		return this.rolesManager;
	}

	/**
	 * Returns a a <code>Vector</code> object containing pairs of Thing name /
	 * Library Name.
	 * 
	 * @return A <code>Vector</code> object holding <code>Property</code>
	 *         object, in which the thing name has the key of
	 *         <code>RolesManager.THING_NAME</code> and the meta-library name is
	 *         keyed under <code>RolesManager.LIBRARY_NAME</code>.
	 *         <p>
	 *         The following code demonstrates how to print the role reference:
	 *         <p>
	 *         <code>
	 * Vector allRoles = theThing.getRolesRepresentation();<br>
	 * if (allRoles.size() > 0)	{<br>
	 * 		Properties aRole = (Properties)allRoles.get(0);<br>
	 * 		String roleThingName = aRole.getProperty(RolesManager.THING_NAME);<br>
	 * 		String roleLibraryName = aRole.getProperty(RolesManager.LIBRARY_NAME);<br>
	 * 		System.out.println(roleThingName + ":" + roleLibraryName);<br>
	 * }<br>
     * </code>
	 * @author Eran Toch
	 */
	public Vector getRolesRepresentation() {
		Vector vec = this.getRolesManager().getOPLRepresentation();
		return vec;
	}

	/**
	 * Returns the full graphical representation of the roles, including free
	 * text <code>String</code> roles and meta-libraries based roles.
	 * 
	 * @author Eran Toch
	 * @return
	 */
	public String getRole() {
		String output = "";
		if ((this.role != null) && !this.role.equals("")) {
			output += this.role;
		}
		if (this.rolesManager != null) {
			if (this.getRolesManager().hasRoles()) {
				if (!output.equals("")) {
					output += ", ";
				}
				output += this.getRolesManager().getRolesText(
						MetaLibrary.TYPE_POLICY);
			}
		}
		return output;
	}

	/**
	 * Returns the full graphical representation of the roles which library is
	 * of a given type (policy, not policy) , including free text
	 * <code>String</code> roles and meta-libraries based roles.
	 * 
	 * @author Eran Toch
	 * @return
	 */
	public String getPoliciesRole() {
		String output = "";
		if ((this.role != null) && !this.role.equals("")) {
			output += this.role;
		}
		if (this.rolesManager != null) {
			if (this.getRolesManager().hasRoles()) {
				if (!output.equals("")) {
					output += ", ";
				}
				output += this.getRolesManager().getPoliciesRolesText();
			}
		}
		return output;
	}

	/**
	 * Returns the text of the free text <code>String</code> role (without
	 * meta-libraries roles).
	 * 
	 * @author Eran Toch
	 */
	public String getFreeTextRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * return the type of an entity as a String. Object Process State etc...
	 * 
	 * @return
	 */
	public String getTypeString() {

		if (this instanceof OpmObject) {
			return "Object";
		} else if (this instanceof OpmProcess) {
			return "Process";
		} else if (this instanceof OpmState) {
			return "State";
		} else if (this instanceof OpmAgent) {
			return "Agent Link";
		} else if (this instanceof OpmConditionLink) {
			return "Condition Link";
		} else if (this instanceof OpmSpecialization) {
			return "Specialization";
		} else if (this instanceof OpmResultLink) {
			return "Result Link";
		} else if (this instanceof OpmUniDirectionalRelation) {
			return "UniDirectional Link";
		} else if (this instanceof OpmAggregation) {
			return "Aggregation Link";
		} else if (this instanceof OpmBiDirectionalRelation) {
			return "BiDirectional Link";
		} else if (this instanceof OpmConsumptionEventLink) {
			return "Consumption Event";
		} else if (this instanceof OpmConsumptionLink) {
			return "Consumption Link";
		} else if (this instanceof OpmEffectLink) {
			return "Effect Link";
		} else if (this instanceof OpmExceptionLink) {
			return "Exception Link";
		} else if (this instanceof OpmExhibition) {
			return "Exibition";
		} else if (this instanceof OpmInstantination) {
			return "Instantination";
		} else if (this instanceof OpmInstrumentEventLink) {
			return "Instrument Event";
		} else if (this instanceof OpmInstrument) {
			return "Instrument Link";
		} else {
			return "Link";
		}

	}

	public Vector getAllData() {
		Vector vec = new Vector();
		vec.add(new Long(this.getId()));

		vec.add(getTypeString());
		vec.add(this.getName().replaceAll("\n", " "));

		if (this instanceof OpmGeneralRelation) {
			if (this.getName().equalsIgnoreCase("")) {
				vec.add(" ");
			} else {
				String forword = ((OpmGeneralRelation) this)
						.getForwardRelationMeaning();
				String back = ((OpmGeneralRelation) this)
						.getBackwardRelationMeaning();
				String str = "";
				if (forword.equalsIgnoreCase("")) {
					str = back;
				} else {
					str = forword;
					if (!back.equalsIgnoreCase("")) {
						str = str + "--" + back;
					}
				}
				vec.add(str);
			}
		} else {
			if (this.getDescription().equalsIgnoreCase("")) {
				vec.add(" ");
			} else {
				vec.add(this.getDescription());
			}
		}
		return vec;
	}

	public int getColoringLevel(MetaLibrary meta) {

		return (int) getId();
		//
		// Iterator roleIter = getRolesManager().getRolesVector(meta.getType(),
		// meta.getGlobalID()).iterator();
		//
		// HashMap colored = new HashMap();
		//
		// int level = -1 ;
		// while (roleIter.hasNext()) {
		// Role role = (Role) roleIter.next();
		// Long id = new Long(role.getThingId());
		// if (!colored.containsKey(id)) {
		// colored.put(id, new Long(level));
		// level++;
		// }
		//			
		// }
		// return level;
	}

	/**
	 * Returns true if this OpmThing is physical. If it's informational returns
	 * false
	 * 
	 */

	public boolean isPhysical() {
		return physical;
	}

	/**
	 * Sets the physical/informational property of OpmThing. If value of
	 * physical is true it's a physical thing, otherwise informational
	 * 
	 */
	public void setPhysical(boolean physical) {
		this.physical = physical;
	}

	public void setPrivateExposed(boolean privateExposed) {
		this.privateExposed = privateExposed;
	}

	public boolean isPrivateExposed() {
		return privateExposed;
	}

}
