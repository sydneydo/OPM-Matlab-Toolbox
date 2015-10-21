package exportedAPI.opcatAPIx;
import java.util.Enumeration;
/**
 * IXObjectInstance is an interface to graphical representation of OPM objects
 */

public interface IXObjectInstance extends IXThingInstance
{
	/**
	 * This method returns an Enumeration of <code>IXStateInstance</code>es for this IXObjectInstance.
	 * @return Enumeration of <code>IXStateInstance</code>es.\
	 * @see IXStateInstance
	 */
    public Enumeration getStateInstances();
}