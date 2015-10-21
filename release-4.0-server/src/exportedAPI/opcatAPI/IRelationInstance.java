package exportedAPI.opcatAPI;
/**
 * IRelationInstance is an interface to graphical representation of OPM Relation
 */

public interface IRelationInstance extends IInstance
{

    /**
     * Checks if autoArranged mode is on.
     */
    public boolean isAutoArranged();

    /**
     * Checks if the line of this IXRelationInstance is straight.
     */
    public boolean isStraight();

    /**
     * Gets IConnectionEdgeInstance which is source of this IRelationInstance.
     */

    public IConnectionEdgeInstance getSourceIInstance();

    /**
     * Gets IConnectionEdgeInstance which is destination of this IRelationInstance.
     */
    public IConnectionEdgeInstance getDestinationIInstance();
}