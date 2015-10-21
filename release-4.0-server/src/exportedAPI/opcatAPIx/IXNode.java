package exportedAPI.opcatAPIx;

import java.awt.Point;
import java.util.Enumeration;

import exportedAPI.RelativeConnectionPoint;

/**
 * IXConnectionEdgeInstance is an interface to graphical representation of elements that could
 * be connected by links/relations
 */

public interface IXNode {

  /**
   * Returns parent IXNode (IXNode which contains graphically
   * this IXNode) or null if this IThingInstance drawn directly on OPD
   * (no parent IXNode).
   * @return IXNode which contains graphically this IXNode
   * or null if this IXNode drawn directly on OPD
   * (no parent IXNode).
   */
  public IXNode getParentIXNode();

  /**
   * Converts a RelativeConnectionPoint from a relative coordinate to
   * a IXConnectionEdgeInstance's coordinate system.
   * @see RelativeConnectionPoint
   */

  public Point getAbsoluteConnectionPoint(RelativeConnectionPoint relPoint);

  /**
   * Moves this component to a new location. The top-left corner of the new location
   * is specified by the x and y parameters in the coordinate space of this component's parent.
   * @param x - The x-coordinate of the new location's top-left corner in the parent's coordinate space.
   * @param y - The y-coordinate of the new location's top-left corner in the parent's coordinate space.
   */
  public void setLocation(int x, int y);

  /**
   * Resizes this component so that it has <code>w</code> width and height <code>h</code>
   * @param w - The new width of this component in pixels.
   * @param h - The new height of this component in pixels.
   */
  public void setSize(int w, int h);

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
   * Returns an Enumeration of all IXLines that are
   * directly related to current IXNode
   * @return Enumeration of related IXLines
   */

  public Enumeration getRelatedIXLines();

}