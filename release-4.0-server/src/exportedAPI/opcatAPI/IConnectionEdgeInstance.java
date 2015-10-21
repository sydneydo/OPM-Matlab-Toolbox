package exportedAPI.opcatAPI;
import java.awt.Point;
import java.util.Enumeration;

import exportedAPI.RelativeConnectionPoint;

/**
 * IXConnectionEdgeInstance is an interface to graphical representation of elements that could
 * be connected by links/relations
 */

public interface IConnectionEdgeInstance extends IInstance
{
    /**
     * Converts a RelativeConnectionPoint from a relative coordinate to
     * a IXConnectionEdgeInstance's coordinate system.
     * @see RelativeConnectionPoint
     */

    public Point getAbsoluteConnectionPoint(RelativeConnectionPoint relPoint);

	/**
	 * Return the current x coordinate of the components origin.
	 * @return the current x coordinate of the components origin.
	 */
	public int getX();

	/**
	 * Return the current y coordinate of the components origin.
	 * @return the current y coordinate of the components origin.
	 */
	public int getY();

	/**
	 * Return the current width of this component.
	 * @return the current width of this component.
	 */
	public int getWidth();

	/**
	 * Return the current height of this component.
	 * @return the current height of this component.
	 */
	public int getHeight();

	/**
     * Returns an Enumeration of all ILinkInstances and IRelationInstances that are
     * directly related to current IInstance
	 * @return Enumeration of related IXInstances
	 */
	public Enumeration getRelatedInstances();
	
	
	/**
	 * Returns parent IThingInstance (IThingInstance which contains graphically
     * this IThingInstance) or null if this IThingInstance drawn directly on OPD
     * (no parent IThingInstance).
	 * @return IThingInstance which contains graphically this IThingInstance
     * or null if this IThingInstance drawn directly on OPD
     * (no parent IThingInstance).
	 */

    public IThingInstance getParentIThingInstance();	
   
}