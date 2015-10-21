package expose.logic;

import expose.OpcatExposeConstants.OPCAT_EXPOSE_LINK_DIRECTION;
import gui.metaLibraries.logic.Role;
import gui.opmEntities.OpmEntity;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmStructuralRelation;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Instance;

/**
 * represents an interface for the advisor
 * 
 * @author raanan
 * 
 */
public class OpcatExposeInterfaceItem {

	OpmEntity link;
	ConnectionEdgeInstance surceInstance;

	public ConnectionEdgeInstance getSurceInstance() {
		return surceInstance;
	}

	public Role getRole() {
		return role;
	}

	public ConnectionEdgeInstance getSourceInsatance() {
		return sourceInsatance;
	}

	Role role;

	ConnectionEdgeInstance sourceInsatance;

	public OpmEntity getLink() {
		return link;
	}

	OPCAT_EXPOSE_LINK_DIRECTION direction;

	public boolean equals(Object obj) {
		if (obj instanceof OpcatExposeInterfaceItem) {
			OpcatExposeInterfaceItem that = (OpcatExposeInterfaceItem) obj;
			if (that.getDirection() == this.getDirection()) {
				if (that.getSourceInsatance().getKey().equals(
						this.getSourceInsatance().getKey())) {
					if (that.getLink().equals(this.getLink())) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * sourceInstance has a {@link Role role } which is connected with a
	 * {@link OpmProceduralLink link} to the interface (a in the remote model.
	 * 
	 * @param sourceInstance
	 *            local {@link Instance} on which the advisor is run
	 * @param interfaceRole
	 *            the remote representation of the local instance (i's Role)
	 * @param link
	 *            the lik in the emte repreesentation to a neigbor in the remote
	 *            model
	 * @param direction
	 *            direction of the lik in the remote model.
	 */
	public OpcatExposeInterfaceItem(ConnectionEdgeInstance sourceInstance,
			Role interfaceRole, OpmEntity link,
			OPCAT_EXPOSE_LINK_DIRECTION direction) {
		super();
		this.link = link;
		this.direction = direction;
		this.role = interfaceRole;
		this.sourceInsatance = sourceInstance;
	}

	public long getInterfaceEntryID() {
		if (link instanceof OpmProceduralLink) {
			if (direction == OPCAT_EXPOSE_LINK_DIRECTION.TO) {
				return ((OpmProceduralLink) link).getSourceId();

			} else {
				return ((OpmProceduralLink) link).getDestinationId();
			}
		} else if (link instanceof OpmStructuralRelation) {
			if (direction == OPCAT_EXPOSE_LINK_DIRECTION.TO) {
				return ((OpmStructuralRelation) link).getSourceId();

			} else {
				return ((OpmStructuralRelation) link).getDestinationId();
			}
		} else {
			return link.getId();
		}
	}

	public OPCAT_EXPOSE_LINK_DIRECTION getDirection() {
		return direction;
	}

}
