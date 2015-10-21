package exportedAPI.opcatAPIx;
import java.awt.Color;

import exportedAPI.OpdKey;
/**
 * IXInstance is an iterface to graphical representation of IXEntry.
 * This is base class for all Instances as you can see from class hierachy.
 *
 */
public interface IXInstance
{

/**
* Returns OpdKey of this Instance.
* @return <code>OpdKey of IXInstance</code>
*/
	public OpdKey getKey();

	/**
	 * Returns IXEntry which IXInstance belongs to, shortly returns logical part
	 * of IXInstance
	 * @return IXEntry which IXInstance belongs to.
	 */
	public IXEntry getIXEntry();

	/**
	 * Returns an ID of IXEntry which IXInstance belongs to.
	 * This is identical to getIXEntry().getId()
	 * @return an ID of which IXInstance belongs to.
	 */
	public long getLogicalId();

	/**
	 * Returns background color of graphical representation
	 * @return background color of IXInstance
	 */
	public Color getBackgroundColor();

	/**
	 * Sets the background color of graphical representation to <code>backgroundColor</code>
	 * @param new background color to IXInstance
	 */
	public void setBackgroundColor(Color backgroundColor);

	/**
	 * Returns border color of graphical representation
	 * @return border color of IXInstance
	 */
	public Color getBorderColor();

	/**
	 * Sets the border color of graphical representation to <code>borderColor</code>
	 * @param new border color to IXInstance
	 */
	public void setBorderColor(Color borderColor);

	/**
	 * Returns text color of graphical representation
	 * @return text color of IXInstance
	 */
	public Color getTextColor();

	/**
	 * Sets the text color of graphical representation to <code>textColor</code>
	 * @param new text color to IXInstance
	 */
	public void setTextColor(Color textColor);

	/**
	 * Updates IXInstance. You should not call this function when altering pro
	 */
	public void update();
}