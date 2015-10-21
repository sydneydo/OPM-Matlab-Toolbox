
package extensionTools.opcatLayoutManager.autoArrange;

import javax.swing.JComponent;

//import extensionTools.opcatLayoutManager.Debug;

/**
 * An implementation of the IssueOperation class that acts on
 * one node and moves it to new location.
 */
public class SingleShiftingOperation extends IssueOperation
{
    private JComponent node = null;
    
    private int xMoveBy = 0;
    
    private int yMoveBy = 0;
    
    /**
     * Gets the node to move, the distance to move on x axis and the distance to move
     * on y axis. Moves the node to new location relatively to current node center
     * @param node to move
     * @param xMoveBy the distance to move on x axis
     * @param yMoveBy the distance to move on y axis
     */  
    public SingleShiftingOperation(JComponent node, int xMoveBy, int yMoveBy)
    {
        this.node = node;
        this.xMoveBy = xMoveBy;
        this.yMoveBy = yMoveBy;
    }

    public void apply()
    {
        //Debug.Print("{S-Sh} " + node.getName() + " += (" + xMoveBy + "," + yMoveBy + ")");
        
        int x_new = node.getX() + xMoveBy;
        int y_new = node.getY() + yMoveBy;
        
        //if a valid location, set the new position
        if (isWithinBoundaries(node, x_new, y_new))
            node.setLocation(x_new, y_new);
    }
}
