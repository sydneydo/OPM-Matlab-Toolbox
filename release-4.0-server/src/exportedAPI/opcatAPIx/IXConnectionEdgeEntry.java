package exportedAPI.opcatAPIx;

import java.util.Enumeration;

/**
 * IXConnectionEdgeEntry is an interface to all IXEntries that could be connected by
 * procedural links or structural relations (objects, processes and states).
 */
public interface IXConnectionEdgeEntry
    extends IXEntry {
  /**
   * Returns Enumeration of IXLinkEntry - all procedural links having this
   * IXConnectionEdgeEntry as source. Use the Enumeration methods on the
   * returned object to fetch the IXLinkEntry sequentially
   */

  public Enumeration getLinksBySource();

  /**
   * Returns Enumeration of IXLinkEntry - all procedural links having this
   * IXConnectionEdgeEntry as destination. Use the Enumeration methods on the
   * returned object to fetch the IXLinkEntry sequentially
   */

  public Enumeration getLinksByDestination();

  /**
   * Returns Enumeration of IXRelationEntry - all structural relations having
   * this IXConnectionEdgeEntry as source. Use the Enumeration methods on the
   * returned object to fetch the IXRelationEntry sequentially
   */

  public Enumeration getRelationBySource();

  /**
   * Returns Enumeration of IXRelationEntry - all structural relations having
   * this IXConnectionEdgeEntry as destination. Use the Enumeration methods on
   * the returned object to fetch the IXRelationEntry sequentially
   */

  public Enumeration getRelationByDestination();

}