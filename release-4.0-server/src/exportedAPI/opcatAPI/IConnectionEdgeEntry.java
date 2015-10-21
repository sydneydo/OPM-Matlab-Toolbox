package exportedAPI.opcatAPI;
import java.util.Enumeration;

/**
 * IConnectionEdgeEntry is an interface to all IEntries that could be connected by
 * procedural links or structural relations (objects, processes and states).
*/
public interface IConnectionEdgeEntry extends IEntry
{
	/**
	* Returns Enumeration of ILinkEntry - all procedural links having this
    * IConnectionEdgeEntry as source. Note that only directly related
    * links will be returned (For example - for object containing states
    * only links related directly to object will be returned and not links
    * related to its states). Use the Enumeration methods on the
    * returned object to fetch the ILinkEntry sequentially
	*/

    public Enumeration getLinksBySource();

	/**
	* Returns Enumeration of ILinkEntry - all procedural links having this
    * IConnectionEdgeEntry as destination. Note that only directly related
    * links will be returned (For example - for object containing states
    * only links related directly to object will be returned and not links
    * related to its states). Use the Enumeration methods on the
    * returned object to fetch the ILinkEntry sequentially
	*/

    public Enumeration getLinksByDestination();

	/**
	* Returns Enumeration of IRelationEntry - all structural relations having
    * this IConnectionEdgeEntry as source. Note that only directly related
    * relations will be returned (For example - for object containing states
    * only relations related directly to object will be returned and not relations
    * related to its states). Use the Enumeration methods on the
    * returned object to fetch the IRelationEntry sequentially
	*/

    public Enumeration getRelationBySource();

    /**
	* Returns Enumeration of IRelationEntry - all structural relations having
    * this IConnectionEdgeEntry as destination. Note that only directly related
    * relations will be returned (For example - for object containing states
    * only relations related directly to object will be returned and not relations
    * related to its states). Use the Enumeration methods on
    * the returned object to fetch the IRelationEntry sequentially
	*/

    public Enumeration getRelationByDestination();
}