/*
 * Created on 06/12/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package extensionTools.validator.finder;

import java.util.Enumeration;
import java.util.Vector;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;
import extensionTools.validator.algorithm.Neighbor;

/**
 * @author eran
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FindSpecificNeighbors implements NeighborFinder {
	
	protected Vector found = null;
	/* (non-Javadoc)
	 * @see extensionTools.validator.algorithm.NeighborFinder#findNeighbors(exportedAPI.opcatAPI.IConnectionEdgeEntry, exportedAPI.opcatAPI.ISystemStructure)
	 */
	public Vector findNeighbors(IConnectionEdgeEntry thing, ISystemStructure structure) {
		Vector output = new Vector();
		if (this.found == null)	{
			return output;
		}
		for (int i=0; i<this.found.size(); i++)	{
			Neighbor neighbor = (Neighbor)this.found.get(i);
			IConnectionEdgeEntry theNeighborThing = neighbor.getConnectedThing();
			Enumeration connections = theNeighborThing.getRelationBySource();
			while (connections.hasMoreElements()) {
				IRelationEntry rel = (IRelationEntry) connections.nextElement();
				if (rel.getRelationType() == OpcatConstants.SPECIALIZATION_RELATION) {
					IConnectionEdgeEntry sourceThing = (IConnectionEdgeEntry) structure.getIEntry(rel.getDestinationId());
					Neighbor specificNeighbor = new Neighbor(sourceThing, neighbor.getConnectionType(), neighbor.getConnectionObject(), neighbor.getDirection());
					output.add(specificNeighbor);
				}
			}
			
		}
		return output;
	}

	/* (non-Javadoc)
	 * @see extensionTools.validator.algorithm.NeighborFinder#setExsitingFinding(java.util.Vector)
	 */
	public void setExsitingFinding(Vector _findings) {
		if (_findings == null)	{
			return;
		}
		this.found = _findings;

	}
}
