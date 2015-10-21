/*
 * Created on 04/06/2004
 */
package extensionTools.validator.finder;


import java.util.Vector;

import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;

/**
 * Specifies a <b>finder</b> algorithm that finds neighbors (connected things) 
 * of a given thing, in a given model. The finder is used by 
 * @author Eran Toch
 * Created: 04/06/2004
 */
public interface NeighborFinder {
	
	/**
	 * Finds neigoboring things, according to some algorithm, and adds them 
	 * to a list.
	 * @param thing	The thing that we are looking it's neighbors for.
	 * @param struct	The model we are analyzing.
	 * @return	A vector containing <code>Neighbor</code> objects.
	 */
	public Vector findNeighbors(IConnectionEdgeEntry thing, ISystemStructure struct);
	
	/**
	 * The method enables the finder implementation to get a list of the exsiting 
	 * findings. It may be useful for algorithms that take into account the 
	 * neighbors that were found.
	 * @param findings A vector containing <code>Neighbor</code> objects.
	 */
	public void setExsitingFinding(Vector findings);
}
