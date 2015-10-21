package exportedAPI.opcatAPIx;

import java.util.Enumeration;

/**
 * IXThingInstance is an interface to graphical representation of OPM Thing
 */
public interface IXThingInstance
    extends IXConnectionEdgeInstance {
  /**
   * Checks if this IXThingInstance is ZoomedIn
   * @return true if IXThingInstance is ZoomedIn, false if not
   */
  public boolean isZoomedIn();


  /**
   * Returns Enumeration of IXThingInstance which this IXThingInstance contains
   * graphically. Empty Enumeration is returned if this IXThingInstance contains
   * nothing. Only directly contained etities are returned.
   * @return Enumeration of IXThingInstance which this IXThingInstance contains
   * graphically. Empty Enumeration is returned if this IXThingInstance contains
   * nothing. Only directly contained etities are returned.
   */
  public Enumeration getChildThings();

  /**
   * Creates inzoomed IXOpd of this IXThingInstance. If inzoomed IXOpd already
   * exists returns it.
   */
  public IXOpd createInzoomedOpd();

  /**
   * Creates unfolded IXOpd of this IXThingInstance. If unfolded IXOpd already
   * exists returns it.
   * @param bringRelatedThings specifies whether to bring related entitities
   */

  public IXOpd createUnfoldedOpd(boolean bringRelatedEntities, boolean bringRolesRelatedThings);
  
  /**
  * Returns OPD unfolding this ThingInstance. null returned if there is no
  * such OPD.
  * @author Yossi Gozlan
  * @since	26/April/2004
  */
	public IXOpd getUnfoldingIXOpd();

}