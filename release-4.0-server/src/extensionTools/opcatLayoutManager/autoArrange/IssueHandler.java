
package extensionTools.opcatLayoutManager.autoArrange;

import gui.opdGraphics.draggers.AroundDragger;
import gui.opdGraphics.lines.OpdLine;
import gui.opdGraphics.opdBaseComponents.OpdState;
//import gui.projectStructure.FundamentalRelationInstance;

import java.util.List;

import javax.swing.JComponent;


/**
 * This interface any issue handler class related to a specific 
 * issue should implement.
 */
public abstract class IssueHandler implements Comparable
{
    /**
     * A small auxiliary method that determines whether the component
     * passed is a "line" in terms of this issue handler class.
     * @return true if"f the component passed is an instance of either 
     * a OpdLine or FundamentalRelationInstance (which is also treated
     * as a line)
     */
    protected boolean isLine(Object inst)
    {
        return ((inst instanceof OpdLine) ||
                (inst instanceof OpdLine[]));
    }
    
    /**
     * @return the ID of this issue handler class, by which it can 
     * later be found (e.g., for removing) in a list of handlers
     */
    public abstract String getId();
    
    /**
     * Checks the given instances to see whether they constitute
     * an "issue".
     * @param inst1 the first instance to check for issue
     * @param inst2 the second instance to check for issue
     * @return the value of the heuristic function induced
     * by the given issue found (if any). When no issue has
     * been found, 0 is returned
     */
    public abstract long evaluateIssue(Object inst1, Object inst2);
    
    /**
     * The method to be used to retrieve the list of oprerations
     * that can be applied to "fix" the issue found in the last call
     * to "evaluateIssue" method. It should be treated as an error
     * to call this method before calling "evaluateIssue" method at
     * least once.
     * 
     *  NOTE: if the parameter is null, the method should do nothing
     */
    public abstract void accumulateOperations(List operations);
    
    /**
     * Returns the weight of the issue (used mainly to sort handlers
     * from the heaviest to lightest).
     * @return the weight of the issue
     */
    public abstract long getWeight();
    
    /**
     * Returns true if"f the object is an instance of this class
     */
    public boolean equals(Object o)
    {
        if (! (o instanceof IssueHandler))
            return false;
        
        IssueHandler iis = (IssueHandler) o;
        return (getWeight() == iis.getWeight());
    }

    /**
     * @see Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
        IssueHandler iis = (IssueHandler) o; //let it throw a ClassCastException
        
        //do all these tedious comparisons - we cannot just cast the difference
        //  to (int), because the sign may be lost this way. E.g.
        //      ((int) Long.MIN_VALUE == 0)
        if (getWeight() < iis.getWeight())
            return -1;
        else if (getWeight() > iis.getWeight())
            return 1;
        return 0;
    }
    
    /**
     * Extracts edge's source or destination node
     * @param lineObj - the edge object itself
     * @param num - what node to extract: 1=src; 2=dst
     * @return source or destination node
     */
    public JComponent extractEdgeNode(Object lineObj, int num)
    {
        JComponent node = null;
        
        if (lineObj instanceof OpdLine[]) {
            OpdLine[] lineArr = (OpdLine[]) lineObj;
            
            return ((num == 1) 
                    ? extractEdgeNode(lineArr[0], 1) 
                            : extractEdgeNode(lineArr[lineArr.length - 1], 2));
        }
        
        if (num == 1)
            node = (JComponent) ((OpdLine) lineObj).getEdge1();
        else
            node = (JComponent) ((OpdLine) lineObj).getEdge2();
        
        if (node instanceof AroundDragger)
            node = (JComponent) ((AroundDragger) node).getEdge();
        
        //if the given link is connected to a state rather than an object,
        //	replace it with the parent object (becuase we do not want to
        //	move states independently of their parent objects)
        if (node instanceof OpdState)
        	node = (JComponent) node.getParent();
        
        return node;
    }
}
