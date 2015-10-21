package exportedAPI.opcatAPI;
import java.util.Enumeration;
/**
 * IObjectInstance is an interface to graphical representation of OPM objects
 */

public interface IObjectInstance extends IThingInstance
{
	/**
	 * This method returns an Enumeration of <code>IStateInstance</code>es for this IObjectInstance.
	 * @return Enumeration of <code>IStateInstance</code>es.\
	 * @see IStateInstance
	 */
	public Enumeration getStateInstances();
}