package extensionTools.validator.finder;

import java.util.Vector;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;
import extensionTools.validator.algorithm.Neighbor;

/**
 * Handles the case with bi-directional general relations. Bi-directional
 * relations do not have a semantic direction, but they do have an "Opcat"
 * direction which is the result of the way they were drawn. This finder solves
 * this problem by duplicating bi-directional general relations and reversing
 * the direction of the duplicate relation. Note that this is useful only for
 * the model constraints, not for the meta-library constraints.
 * @author Eran Toch
 *
 */
public class HandleBiDirectionalRelations implements NeighborFinder {
	
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
			if (neighbor.getConnectionType() == OpcatConstants.BI_DIRECTIONAL_RELATION)	{
				int otherSizeDirection = 0;
				if (neighbor.getDirection() == Finder.SOURCE_DIRECTION)	{
					otherSizeDirection = Finder.DESTINATION_DIRECTION;
				}
				else	{
					otherSizeDirection = Finder.SOURCE_DIRECTION;
				}
				Neighbor otherSide = new Neighbor(neighbor.getConnectedThing(), neighbor.getConnectionType(), neighbor.getConnectionObject(), otherSizeDirection);
				output.add(otherSide);
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
