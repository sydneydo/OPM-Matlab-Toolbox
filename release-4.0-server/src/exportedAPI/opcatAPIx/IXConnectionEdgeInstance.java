package exportedAPI.opcatAPIx;

import java.util.Enumeration;

/**
 * IXConnectionEdgeInstance is an interface to graphical representation of elements that could
 * be connected by links/relations
 */

public interface IXConnectionEdgeInstance
    extends IXInstance, IXNode {

  /**
   * Returns an Enumeration of all IXLinkInstances and IXRelationInstances that are
   * directly related to current IXInstance
   * @return Enumeration of related IXInstances
   */
  public Enumeration getRelatedInstances();

  /**
   * Animates this thing for time milliseconds. In current implementation
   * background color changes to brighter version of border color
   * progressively in time milliseconds.
   * animatedUp flag becames true for time milliseconds.
   */
  public void animate(long time);
  
  /** 
   * show the thing URL
   * @param time
   */
  public void animateUrl(long time) ;
  
  /**
   * Returns parent IXThingInstance (IXThingInstance which contains graphically
   * this IThingInstance) or null if this IThingInstance drawn directly on OPD
   * (no parent IXThingInstance).
   * @return IXThingInstance which contains graphically this IXThingInstance
   * or null if this IXThingInstance drawn directly on OPD
   * (no parent IXThingInstance).
   */
  public IXThingInstance getParentIXThingInstance();  

  /**
   * Stops the testing in time milliseconds. In current implementation
   * background color changes to original progressively in time milliseconds.
   * animatedDown flag becames true for time milliseconds.
   */
  public void stopTesting(long time);

  /**
   * Checks if this IXConnectionEdgeInstance is animated.
   * @return true if this IXConnectionEdgeInstance is animated.
   */
  public boolean isAnimated();

  /**
   * Pauses testing. If this IXConnectionEdgeInstance is not animated
   * this command will be ignored.
   */
  public void pauseTesting();

  /**
   * Continues paused testing. If this IXConnectionEdgeInstance is not animated
   * and not paused this command will be ignored.
   */
  public void continueTesting();

  /**
   * Returns time (long milliseconds) remained until enf of testing if this
   * IXConnectionEdgeInstance inside animate or stopTesting operations.
   * @return time (long milliseconds) remained until enf of testing.
   */
  public long getRemainedTestingTime();

  /**
   * Returns total time (long milliseconds) of testing if this IXConnectionEdgeInstance
   * is animatedUp or animatedDown. Otherwise returns 0.
   * @return total time (long milliseconds) of testing.
   */
  public long getTotalTestingTime();

  /**
   * Returns true if this IXConnectionEdgeInstance is inside of
   * animate operation.
   * @return true if this IXConnectionEdgeInstance is inside of
   * animate operation.
   */
  public boolean isAnimatedUp();

  /**
   * Returns true if this IXConnectionEdgeInstance is inside of
   * animate operation.
   * @return true if this IXConnectionEdgeInstance is inside of
   * stopTesting operation.
   */
  public boolean isAnimatedDown();

}