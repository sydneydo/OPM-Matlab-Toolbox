/*
 * Created on Jan 24, 2007
 */
package extensionTools.opcatLayoutManager.autoArrange;

//import extensionTools.opcatLayoutManager.Debug;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;

import java.awt.Rectangle;
import java.util.List;

/**
 * Class that handles the issue of the node intersecting with another node. If the intersection is
 * detected the handler adds the set of operations for possible fixes
 */
public class IssueHandlerOverpappingNodes extends IssueHandler
{
    private BaseGraphicComponent node1 = null, node2 = null;
    
    private long value = -1;
    
    /**
     * @see IssueHandler#getId()
     */
    public String getId()
    {
        return StandardIssueHandlers.OVERLAPPING_NODES;
    }
    
    /**
     * @see IssueHandler#evaluateIssue(Object, Object)
     */
    public long evaluateIssue(Object inst1, Object inst2)
    {
        value = 0;
        if ((! (inst1 instanceof BaseGraphicComponent)) || 
                (! (inst2 instanceof BaseGraphicComponent)))
            return value;
        
        node1 = (BaseGraphicComponent) inst1;
        Rectangle r1 = node1.getBounds();
        node2 = (BaseGraphicComponent) inst2;
        Rectangle r2 = node2.getBounds();
        
        if ((node1.getParent() == node2) || (node1 == node2.getParent()) || 
                (! r1.intersects(r2)))
            return 0;
        value = getWeight();
        
        //Debug.Print("[n X n] " + node1.getName() + "-" + node2.getName() + "(" + r1 + "-" + r2 + ")");
            
        return value;
    }
    
    /**
     * @see IssueHandler#accumulateOperations(List)
     */
    public void accumulateOperations(List operations)
    {
        //handle once and for all the cases when we don't need to do anything
        if ((operations == null) || (value == 0))
            return;
        
        //the problematic case - an attempt to retrieve operations before
        //  evaluating the issue
        if (value < 0)
            throw new IllegalStateException("\"evaluateIssue\" not yet called");

        //
        // accumulate all the operations for the nodes
        //
        
        //store the dimensions of the nodes
        int w1 = node1.getWidth();
        int h1 = node1.getHeight();
        int w2 = node2.getWidth();
        int h2 = node2.getHeight();
        
        double D_orig = node2.getX() + w2 / 2 - node1.getX() - w1 / 2;
        double D_new1 = 0.75 * (w1 + w2);
        double D_new2 = 0.60 * (w1 + w2);
        
        //push both nodes horizontally
        //  New D = 0.75 * (w1 + w2)
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                (byte) (DoubleShiftingOperation.LEFT | DoubleShiftingOperation.RIGHT),
                Utils.round((D_new1 - D_orig) / 2)) });
        
        //push both nodes horizontally
        //  New D = 0.60 * (w1 + w2)
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                (byte) (DoubleShiftingOperation.LEFT | DoubleShiftingOperation.RIGHT),
                Utils.round((D_new2 - D_orig) / 2)) });
        
        //push the left-most node horizontally to the left 
        //  New D = 0.75 * (w1 + w2)
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                DoubleShiftingOperation.LEFT,
                Utils.round(D_new1 - D_orig)) });
        
        //push the left-most node horizontally to the left 
        //  New D = 0.60 * (w1 + w2);
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                DoubleShiftingOperation.LEFT,
                Utils.round(D_new2 - D_orig)) });
        
        //push the right-most node horizontally to the right 
        //  New D = 0.75 * (w1 + w2)
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                DoubleShiftingOperation.RIGHT,
                Utils.round(D_new1 - D_orig)) });
        
        //push the right-most node horizontally to the right 
        //  New D = 0.60 * (w1 + w2);
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                DoubleShiftingOperation.RIGHT,
                Utils.round(D_new2 - D_orig)) });
        
        //the same thing vertically
        D_orig = node2.getY() + h2 / 2 - node1.getY() - h1 / 2;
        D_new1 = 0.75 * (h1 + h2);
        D_new2 = 0.60 * (h1 + h2);

        //push both nodes vertically
        //  New D = 0.75 * (h1 + h2);
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                (byte) (DoubleShiftingOperation.LOWER | DoubleShiftingOperation.UPPER),
                Utils.round((D_new1 - D_orig) / 2)) });
        //new distance minus old distance
        
        //push both nodes vertically
        //  New D =  0.6 * (h1 + h2);
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                (byte) (DoubleShiftingOperation.LOWER | DoubleShiftingOperation.UPPER),
                Utils.round((D_new2 - D_orig) / 2)) });
        //new distance minus old distance
        
        //push the left-most node vertically 
        //  New D = 0.75 * (h1 + h2);
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                DoubleShiftingOperation.LOWER,
                Utils.round(D_new1 - D_orig)) });
        
        //push the left-most node vertically to the left 
        //  New D = 0.6 * (h1 + h2);
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                DoubleShiftingOperation.LOWER,
                Utils.round(D_new2 - D_orig)) });
        
        //push the right-most node vertically to the right 
        //  New D =  0.75 * (h1 + h2);
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                DoubleShiftingOperation.UPPER,
                Utils.round(D_new1 - D_orig)) });
        
        //push the right-most node vertically to the right 
        //  New D = 0.6 * (h1 + h2);
        operations.add(new IssueOperation[] { new DoubleShiftingOperation(node1, node2, 
                DoubleShiftingOperation.UPPER,
                Utils.round(D_new2 - D_orig)) });
    }

    /**
     * @see IssueHandler#getWeight()
     */
    public long getWeight()
    {
        return 1500;
    }
}
