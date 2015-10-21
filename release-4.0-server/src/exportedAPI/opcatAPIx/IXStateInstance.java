package exportedAPI.opcatAPIx;
/**
 * IXStateInstance is an interface to graphical representation of OPM state
 */
public interface IXStateInstance extends IXConnectionEdgeInstance
{
	/**
	 * Checks if this IXStateInstance is visible
	 * @return true if IXStateInstance is visible
	 */
    public boolean isVisible();

	/**
	 * Sets the IXStateInstance to be visible or not according to given parameter.
	 * @param visible - true if IXStateInstance gouing to be visible.
	 */
    public void setVisible(boolean visible);

}
