
package extensionTools.opcatLayoutManager.autoArrange;

import javax.swing.JComponent;

/**
 * An implementation of the IssueOperation class that acts on
 * two nodes and swaps them on their locations.
 */
public class SwappingOperation extends IssueOperation
{
    private JComponent node1 = null, node2 = null;
    
    public SwappingOperation(JComponent node1, JComponent node2)
    {
        this.node1 = node1;
        this.node2 = node2;
    }

    public void apply()
    {
        int x1 = node2.getX();
        int y1 = node2.getY();
        
        //if not a valid location, cancel the operation
        if (! isWithinBoundaries(node1, x1, y1))
            return;
        
        int x2 = node1.getX();
        int y2 = node1.getY();
        
        //if not a valid location, cancel the operation
        if (! isWithinBoundaries(node2, x2, y2))
            return;
        
        //ready to perform the swap
        node1.setLocation(x1, y1);
        node2.setLocation(x2, y2);
    }
}
