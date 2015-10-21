package exportedAPI.opcatAPIx;
import java.util.Enumeration;
/**
 * IXLinkInstance is an interface for graphical instance of the IXLinkEntry.
 */
public interface IXLinkInstance extends IXInstance
{

    /**
     * Gets IXConnectionEdgeInstance which is source of this IXLinkInstance.
     */

    public IXConnectionEdgeInstance getSourceIXInstance();

    /**
     * Gets IXConnectionEdgeInstance which is destination of this IXLinkInstance.
     */
    public IXConnectionEdgeInstance getDestinationIXInstance();


    /**
     * Animates this link for time milliseconds. In current implementation
     * red point runs on line for time milliseconds. Testing stops automaticaly
     * after time milliseconds.
     */
    public void animate(long time);

    /**
     * Animates this link for remainedTime milliseconds, when starting at
     * (1 - remainedTime/totalTime) point. In current implementation
     * red point runs on line for remainedTime milliseconds. Testing stops automaticaly
     * after reamainedTime milliseconds.
     */

    public void animate(long totalTime, long remainedTime);

    /**
     * Returns time (long milliseconds) remained until end of testing.
     * @return time (long milliseconds) remained until end of testing.
     */

    public long getRemainedTestingTime();

    /**
     * Returns total time (long milliseconds) of testing if this IXLinkInstance
     * is animated. Otherwise returns 0.
     * @return total time (long milliseconds) of testing.
     */

    public long getTotalTestingTime();

    /**
     * Animates this link for very short period of time. In current implementation
     * line's color becomes red for 350 milliseconds.
     */
    public void animateAsFlash();

    /**
     * Stops the testing immediately.
     */
    public void stopTesting();

    /**
     * Checks if this IXLinkInstance is animated.
     * @return true if this IXLinkInstance is animated.
     */
    public boolean isAnimated();

    /**
     * Pauses testing. If this IXLinkInstance is not animated
     * this command will be ignored.
     */
    public void pauseTesting();

    /**
     * Continues paused testing. If this IXLinkInstance is not animated
     * and not paused this command will be ignored.
     */
    public void continueTesting();

    /**
     * Returns Enumeration of IXLinkInstance - all procedural links having
     * Xor/Or logical relation with this IXLinkInstance at source point.
     * Returns null if no neighbour link by source is found.
     * @param isOr defines needed type of logical relation,
     * true/false means Or/Xor.
     */
    public Enumeration<IXLinkInstance> getOrXorSourceNeighbours(boolean isOr);

    /**
     * Returns Enumeration of IXLinkInstance - all procedural links having
     * Xor/Or logical relation with this IXLinkInstance at destination point.
     * Returns null if no neighbour link by destination is found.
     * @param isOr defines needed type of logical relation,
     * true/false means Or/Xor.
     */
    public Enumeration<IXLinkInstance> getOrXorDestinationNeighbours(boolean isOr);


}