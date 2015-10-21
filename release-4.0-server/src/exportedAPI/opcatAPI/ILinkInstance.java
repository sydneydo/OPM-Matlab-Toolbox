package exportedAPI.opcatAPI;
import java.util.Enumeration;

/**
 * ILinkInstance is an interface for graphical instance of the ILinkEntry
 */
public interface ILinkInstance extends IInstance
{
    /**
     * Checks if autoArranged mode is on.
     */
    public boolean isAutoArranged();

    /**
     * Checks if the line of this IXLinkInstance is straight.
     */
    public boolean isStraight();

    /**
     * Gets IConnectionEdgeInstance which is source of this ILinkInstance.
     */

    public IConnectionEdgeInstance getSourceIInstance();

    /**
     * Gets IXConnectionEdgeInstance which is destination of this IXLinkInstance.
     */
    public IConnectionEdgeInstance getDestinationIInstance();

    /**
     * Returns Enumeration of IXLinkInstance - all procedural links having
     * Xor/Or logical relation with this IXLinkInstance at source point.
     * Returns null if no neighbor link by source is found.
     * @param isOr defines needed type of logical relation,
     * true/false means Or/Xor.
     */
    public Enumeration getOrXorSourceNeighbours(boolean isOr);

    /**
     * Returns Enumeration of IXLinkInstance - all procedural links having
     * Xor/Or logical relation with this IXLinkInstance at destination point.
     * Returns null if no neighbor link by destination is found.
     * @param isOr defines needed type of logical relation,
     * true/false means Or/Xor.
     */
    public Enumeration getOrXorDestinationNeighbours(boolean isOr);

}