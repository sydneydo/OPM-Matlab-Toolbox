package exportedAPI.opcatAPIx;
/**
 * IXRelationInstance is an interface to graphical representation of OPM Relation
 */

public interface IXRelationInstance extends IXInstance, IXLine
{

    public IXConnectionEdgeInstance getSourceIXInstance();

    /**
     * Gets IXConnectionEdgeInstance which is destination of this IXRelationInstance.
     */
    public IXConnectionEdgeInstance getDestinationIXInstance();
}