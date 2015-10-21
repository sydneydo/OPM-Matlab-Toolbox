package exportedAPI;

import javax.swing.JPanel;

/**
 * <p><code>OpcatExtensionToolBase</code> - is a base interface for OpcatExtensionTool interfaces that
 * contains common methods.</p>
 */

public interface OpcatExtensionToolBase
{
	/**
	 * <p>Returns the name of Extension Tool. This method must be properly implemented. Opcat2 system uses
	 * it to identify Extension Tool and as a name for menu and about menu.</p>
	 *
	 * @return String that represents the name of Extension Tool.
	 */
	public String getName();

	/**
	 * <p>Returns <code>JPanel</code> that is about box. This method could be have
	 * empty implementation, in other words just return <code>null</code></p>.
	 *
	 * @return JPanel <code>JPanel</code> that is about panel box.
	 */
	public JPanel getAboutBox();

	/**
	 * <p>Returns <code>String</code> that is URL of the help .html file. This method could be have
	 * empty implementation, in other words just return <code>null</code></p>.
	 *
	 * @return <code>String</code> that is URL of the help .html file.
	 */
	public String getHelpURL();
}