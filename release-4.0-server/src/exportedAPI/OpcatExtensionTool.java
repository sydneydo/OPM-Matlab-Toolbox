package exportedAPI;

import javax.swing.JPanel;

import exportedAPI.opcatAPI.ISystem;
/**
 * <p>OpcatExtensionTool - is an interface that Extension Tool actually implements.
 * When any Extension Tool implements this interface, it commits not to change
 * anything in Opcat2 system.</p>
 *
 */
public interface OpcatExtensionTool extends OpcatExtensionToolBase
{
	/**
	 * <p>This method is actually the main function for any Extension Tool. All algorithms, processings of Opcat2
	 * structure etc. done here. <code>execute()</code> method can return <code>JPanel</> that will be inseted
	 * into Opcat2 GUI, this panel can have different controls or any Java components. If you do not want any
	 * graphical output or feedback, you can return <code>null</code> from this method.
	 * You can see examples for more information.</p>
	 * @return <code>JPanel</code> or <code>null</code> no GUI feedback required.
	 */
	public JPanel execute(ISystem opcatSystem);
}