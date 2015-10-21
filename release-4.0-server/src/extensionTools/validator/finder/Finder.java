/*
 * Created on 03/06/2004
 */
package extensionTools.validator.finder;

import java.util.Enumeration;
import java.util.Vector;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.ISystemStructure;
import extensionTools.validator.algorithm.AlgorithmConfiguration;
import extensionTools.validator.algorithm.Neighbor;

/**
 * Wraps up a <code>IConnectionEdgeEntry</code> object, allowing connected
 * things to be found.
 * 
 * @author Eran Toch Created: 03/06/2004 TODO: add a finder algorithm for
 *         bi-directional effect links
 */
public class Finder implements NeighborFinder {

    public final static int SOURCE_DIRECTION = 1;

    public final static int DESTINATION_DIRECTION = 2;

    public final static int ZOOMIN_RELATION = 401;

    public final static int STATE_OF_RELATION = 402;

    protected ISystemStructure structure;

    /**
         * A list of <code>Finder</code> objects that can add
         */
    protected Vector addins = new Vector();

    public Finder() {
    }

    /**
         * Allows other finder algorithms to be added to this finder.
         * 
         * @param finder
         */
    public void addFinder(NeighborFinder finder) {
	this.addins.add(finder);
    }

    /**
         * Finds all teh constraints for a given thing in the model.
         * 
         * @param thing
         * @return
         */
    public Vector findNeighbors(IConnectionEdgeEntry thing,
	    ISystemStructure struct) {
	this.structure = struct;
	Vector vec = new Vector();
	vec.addAll(this.getRelationsBySource(thing));
	vec.addAll(this.getRelationsByDestination(thing));
	vec.addAll(this.getLinksBySource(thing));
	vec.addAll(this.getLinksByDestination(thing));
	for (int i = 0; i < this.addins.size(); i++) {
	    NeighborFinder finder = (NeighborFinder) this.addins.get(i);
	    finder.setExsitingFinding(vec);
	    vec.addAll(finder.findNeighbors(thing, this.structure));
	}
	return vec;
    }

    /**
         * The method does not need this method.
         */
    public void setExsitingFinding(Vector findings) {
    }

    /**
         * Retrieves all relations which are on the destination side of the
         * given <code>IConnectionEdgeEntry</code>.
         */
    protected Vector getRelationsBySource(IConnectionEdgeEntry thing) {
	Vector vec = new Vector();
	Enumeration connections = thing.getRelationBySource();
	while (connections.hasMoreElements()) {
	    IRelationEntry rel = (IRelationEntry) connections.nextElement();
	    if (AlgorithmConfiguration.DO_IS_A) {
		if (rel.getRelationType() == OpcatConstants.SPECIALIZATION_RELATION) {
		    break;
		}
	    }
	    IConnectionEdgeEntry destinationThing = (IConnectionEdgeEntry) this.structure
		    .getIEntry(rel.getDestinationId());
	    // Building the neighbor object
	    if (rel != null) {
		Neighbor neighbor = new Neighbor(destinationThing, rel
			.getRelationType(), rel, DESTINATION_DIRECTION);
		vec.add(neighbor);
	    }
	}
	return vec;
    }

    /**
         * Retrieves all relations which are on the source side of the given
         * <code>IConnectionEdgeEntry</code>.
         */
    protected Vector getRelationsByDestination(IConnectionEdgeEntry thing) {
	Vector vec = new Vector();
	Enumeration connections = thing.getRelationByDestination();
	while (connections.hasMoreElements()) {
	    IRelationEntry rel = (IRelationEntry) connections.nextElement();
	    if (AlgorithmConfiguration.DO_IS_A) {
		if (rel.getRelationType() == OpcatConstants.SPECIALIZATION_RELATION) {
		    break;
		}
	    }
	    IConnectionEdgeEntry destinationThing = (IConnectionEdgeEntry) this.structure
		    .getIEntry(rel.getSourceId());
	    // Building the neighbor object
	    if (rel != null) {
		Neighbor neighbor = new Neighbor(destinationThing, rel
			.getRelationType(), rel, SOURCE_DIRECTION);
		vec.add(neighbor);
	    }
	}
	return vec;
    }

    /**
         * Retrieves all links which are on the destination side of the given
         * <code>IConnectionEdgeEntry</code>.
         */
    protected Vector getLinksBySource(IConnectionEdgeEntry thing) {
	Vector vec = new Vector();
	Enumeration connections = thing.getLinksBySource();
	while (connections.hasMoreElements()) {
	    ILinkEntry link = (ILinkEntry) connections.nextElement();
	    IConnectionEdgeEntry destinationThing = (IConnectionEdgeEntry) this.structure
		    .getIEntry(link.getDestinationId());
	    // Building the neighbor object
	    if (link != null) {
		Neighbor neighbor = new Neighbor(destinationThing, link
			.getLinkType(), link, DESTINATION_DIRECTION);
		vec.add(neighbor);
	    }
	}
	return vec;
    }

    /**
         * Retrieves all links which are on the source side of the given
         * <code>IConnectionEdgeEntry</code>.
         */
    protected Vector getLinksByDestination(IConnectionEdgeEntry thing) {
	Vector vec = new Vector();
	Enumeration connections = thing.getLinksByDestination();
	while (connections.hasMoreElements()) {
	    ILinkEntry link = (ILinkEntry) connections.nextElement();
	    IConnectionEdgeEntry destinationThing = (IConnectionEdgeEntry) this.structure
		    .getIEntry(link.getSourceId());
	    // Building the neighbor object
	    if (link != null) {
		Neighbor neighbor = new Neighbor(destinationThing, link
			.getLinkType(), link, SOURCE_DIRECTION);
		vec.add(neighbor);
	    }
	}
	return vec;
    }
}
