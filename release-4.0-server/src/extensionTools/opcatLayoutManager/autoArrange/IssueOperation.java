
package extensionTools.opcatLayoutManager.autoArrange;

import java.awt.Container;

import javax.swing.JComponent;


/**
 * The interface that must be "implemented" by classes representing
 *  operations on "Issues".
 */
public abstract class IssueOperation
{
    protected IssueOperation nextOperation = null;
    
    /**
     * Identifies whether it is acceptable to move the node to the location,
     * in terms of staying within the parent area
     * @param node - the node proposed to be moved
     * @param newX - the proposed location - new X coordinate of the node center
     * @param newY - the proposed location - new Y coordinate of the node center
     * @return true, if"f the new location is safe
     */
    protected boolean isWithinBoundaries(JComponent node, int newX, int newY)
    {
        Container parent = node.getParent();
        
        return (parent.contains(newX, newY) && parent.contains(newX + node.getWidth(), newY) &&
                parent.contains(newX, newY + node.getHeight()) && 
                parent.contains(newX + node.getWidth(), newY + node.getHeight()));
    }
    
    /**
     * Applies the action to the relevant nodes. The latter should
     * be stored in the class' data members via a constructor or
     * setter functions.
     */
    public abstract void apply();
    
    /**
     * Returns the next IssueOperation in the list 
     */
    public IssueOperation getNextOperation()
    {
        return nextOperation;
    }
    
    /**
     * Sets the next IssueOperation in the list 
     */
    public void setNextOperation(IssueOperation nextOperation)
    {
        this.nextOperation = nextOperation;
    }
}
