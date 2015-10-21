package exportedAPI.opcatAPIx;
import exportedAPI.RelativeConnectionPoint;
/**
 * IXLine is an interface for graphical line.
 */
public interface IXLine
{
    /**
     * Turns autoArranged mode on/off.
     */
    public void setAutoArranged(boolean isAutorranged);

    /**
     * Checks if autoArranged mode is on.
     */
    public boolean isAutoArranged();

    /**
     * Makes the line of this IXLine straight.
     */
    public void makeStraight();

    /**
     * Checks if the line of this IXLine is straight.
     */
    public boolean isStraight();

    /**
     * Gets source RelativeConnectionPoint - the point on
     * source of this IXLine to which line the link is connected.
     * You can translate the RelativeConnectionPoint to regular Point by
     * getAbsoluteConnectionPoint of IXNode method.
     * @see RelativeConnectionPoint
     * @see IXNode#getAbsoluteConnectionPoint(RelativeConnectionPoint relPoint)
     */
    public RelativeConnectionPoint getSourceConnectionPoint();

    /**
     * Sets source RelativeConnectionPoint - the point on
     * source of this IXLine to which line of this link is connected.
     * @see RelativeConnectionPoint
     */
    public void setSourceConnectionPoint(RelativeConnectionPoint point);

    /**
     * Gets destination RelativeConnectionPoint - the point on
     * destination of this IXLine to which line of this link is connected.
     * You can translate the RelativeConnectionPoint to regular Point by
     * getAbsoluteConnectionPoint of IXNode method.
     * @see RelativeConnectionPoint
     * @see IXNode#getAbsoluteConnectionPoint(RelativeConnectionPoint relPoint)
     */
    public RelativeConnectionPoint getDestinationConnectionPoint();

    /**
     * Sets destination RelativeConnectionPoint - the point on
     * destination of this IXLine to which line of this link is connected.
     * @see RelativeConnectionPoint
     */

    public void setDestinationConnectionPoint(RelativeConnectionPoint point);

    /**
     * Gets IXNode which is source of this IXNode.
     */

    public IXNode getSourceIXNode();

    /**
     * Gets IXNode which is destination of this IXLine.
     */
    public IXNode getDestinationIXNode();
}
