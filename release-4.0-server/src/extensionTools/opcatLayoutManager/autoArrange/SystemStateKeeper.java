
package extensionTools.opcatLayoutManager.autoArrange;

import java.util.*;

import javax.swing.JComponent;

/**
 * The class that represents the specific state of the system, diagram that is the result 
 * of some operation appliance. Holds the diagram weight and the list of operations that may
 * be performed in order to fix the situation 
 */
public class SystemStateKeeper implements Comparable
{
    private static int number_of_nodes_ = 0;
    
    private static class NodeKeeper
    {
        private JComponent node_ = null;
        
        private int newX_ = 0;
        
        private int newY_ = 0;
        
        public NodeKeeper(JComponent node)
        {
            node_ = node;
            newX_ = node.getX();
            newY_ = node.getY();
        }
        
        public JComponent getNode()
        {
            return node_;
        }
        
        public int getX()
        {
            return newX_;
        }
        
        public int getY()
        {
            return newY_;
        }
    }
    
    private List nodes_ = null;
    
    private long weight_ = 0;
    
    private List operations_ = null;
    
    public static void setNumberOfNodes(int n)
    {
        if (n <= 0)
            throw new IllegalArgumentException("number of nodes being set must be positive");
        number_of_nodes_ = n;
    }
    
    public SystemStateKeeper(Iterator nodeIterator, long weight, List operations)
    {
        if ((nodeIterator == null) || (weight < 0))
            throw new IllegalArgumentException();
        weight_ = weight;
        
        operations_ = operations;
            
        nodes_ = new ArrayList((number_of_nodes_ > 0) ? number_of_nodes_ : 10);
        
        while (nodeIterator.hasNext())
            nodes_.add(new NodeKeeper((JComponent) nodeIterator.next())); 
    }
    
    public long getWeight()
    {
        return weight_;
    }
    
    public List getOperations()
    {
        return operations_;
    }
    
    public void restoreState()
    {
        Iterator iter = nodes_.iterator();
        while (iter.hasNext()) {
            NodeKeeper nk = (NodeKeeper) iter.next();
            nk.getNode().setLocation(nk.getX(), nk.getY());
        }
    }

    public boolean equals(Object o)
    {
        return (((SystemStateKeeper) o).weight_ == weight_);
    }
    
    public int compareTo(Object o)
    {
        SystemStateKeeper ssk = (SystemStateKeeper) o;
        
        //do all these tedious comparisons - we cannot just cast the difference
        //  to (int), because the sign may be lost this way. E.g.
        //      ((int) Long.MIN_VALUE == 0)
        if (weight_ < ssk.weight_)
            return -1;
        else if (weight_ > ssk.weight_)
            return 1;
        return 0;
    }
}
