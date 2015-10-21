package exportedAPI;

import javax.swing.JPanel;

import exportedAPI.opcatAPIx.IXSystem;
/**
 * <p>OpcatExtensionToolX - is an interface that Extension Tool actually implements.
 * When any Extension Tool implements this interface, it may make chages in Opcat2 system.</p>
 *
 */
public interface OpcatExtensionToolX extends OpcatExtensionToolBase
{
	/**
	 * <p>This method is actually the main function for any Extension Tool. All algorithms, processings of Opcat2
	 * structure, atering system etc. done here. <code>execute()</code> method can return <code>JPanel</> that will be inseted
	 * into Opcat2 GUI, this panel can have different controls or any Java components. If you do not want any
	 * graphical output or feedback, you can return <code>null</code> from this method.
	 * You can see examples for more information.</p>
	 * @return <code>JPanel</code> or <code>null</code> no GUI feedback required.
	 */
	public JPanel execute(IXSystem opcatSystem);
}