package exportedAPI.opcatAPI;
/**
 * IXStateInstance is an interface to graphical representation of OPM state
 */
public interface IStateInstance extends IConnectionEdgeInstance
{
	/**
	 * Checks if this IStateInstance is visible
	 * @return true if IStateInstance is visible
	 */
	public boolean isVisible();
}
