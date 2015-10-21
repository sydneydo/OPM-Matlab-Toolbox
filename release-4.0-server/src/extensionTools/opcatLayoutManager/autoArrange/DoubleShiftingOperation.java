
package extensionTools.opcatLayoutManager.autoArrange;

import java.awt.*;

import javax.swing.JComponent;

/**
 * An implementation of the IssueOperation class that acts on
 * two nodes and shifts them away from each other in a number of ways.
 */
public class DoubleShiftingOperation extends IssueOperation
{
    /**
     * A "direction" constant 
     */
    public static final byte LEFT   = 0x01;
    
    /**
     * A "direction" constant 
     */
    public static final byte RIGHT  = 0x02;
    
    /**
     * A "direction" constant 
     */
    public static final byte UPPER  = 0x04;
    
    /**
     * A "direction" constant 
     */
    public static final byte LOWER  = 0x08;
    
    /**
     * The schema components to which to apply the operation
     */
    private JComponent n1 = null, n2 = null;
    
    private byte which2move = 0;
    
    private int moveBy = 0;
    /**
     * Class constructor:
     * @param n1 - first component to switch (object of node)
     * @param n2 - second component to switch (object of node)
     * @param which2move - one of "direction" constants LEFT, RIGHT, UPPER, LOWER
     * 					or some logical operation between them
     * @param moveBy - new x or y distance between the centers of shefted components 
     */
    
    public DoubleShiftingOperation(JComponent n1, JComponent n2, byte which2move, int moveBy)
    {
        this.n1 = n1;
        this.n2 = n2;
        this.which2move = which2move;
        this.moveBy = moveBy;
    }
    
    /**
     * Perform the operation
     */
    public void apply()
    {
        //Debug.Print("{D-Sh} " + n1.getName() + "+" + n2.getName() + "|" + which2move + "| += " + moveBy);
        
        //move them horisontally, if required 
        if (((which2move & LEFT) != 0) || ((which2move & RIGHT) != 0)) {
            //find the left and right nodes
            JComponent left = null;
            JComponent right = null;
            if (n1.getX() < n2.getX()) {
                left = n1;
                right = n2;
            } else if (n1.getX() > n2.getX()) {
                left = n2;
                right = n1;
            } else { //(n1.getX() == n2.getX())
                //take the lower for the "lefter"
                if (n1.getY() < n2.getY()) {
                    left = n1;
                    right = n2;
                } else {
                    left = n2;
                    right = n1;
                }
            }
            
            Point left_new =  (Point)  left.getLocation().clone();
            Point right_new = (Point) right.getLocation().clone();
            
            if ((which2move & LEFT) != 0)
                left_new.x  -= moveBy;
            if ((which2move & RIGHT) != 0)
                right_new.x += moveBy;
            
            //check the validity of the new position
            if (isWithinBoundaries(left, left_new.x,  left_new.y))
                left.setLocation(left_new);
            if (isWithinBoundaries(right,right_new.x, right_new.y))
                right.setLocation(right_new);
        }
        
        //move them vertically, if required 
        if (((which2move & LOWER) != 0) || ((which2move & UPPER) != 0)) {
            //find the left and right nodes
            JComponent lower = null;
            JComponent upper = null;
            if (n1.getY() < n2.getY()) {
                lower = n1;
                upper = n2;
            } else if (n1.getY() > n2.getY()) {
                lower = n2;
                upper = n1;
            } else { //(n1.getY() == n2.getY())
                //take the left-most for the lower
                if (n1.getX() < n2.getX()) {
                    lower = n1;
                    upper = n2;
                } else {
                    lower = n2;
                    upper = n1;
                }
            }
            
            Point lower_new = (Point) lower.getLocation().clone();
            Point upper_new = (Point) upper.getLocation().clone();
            
            if ((which2move & LOWER) != 0)
                lower_new.y -= moveBy;
            if ((which2move & UPPER) != 0)
                upper_new.y += moveBy;
            
            //check the validity of the new position
            if (isWithinBoundaries(lower, lower_new.x, lower_new.y))
                lower.setLocation(lower_new);
            if (isWithinBoundaries(upper, upper_new.x, upper_new.y))
                upper.setLocation(upper_new);
        }
    }
}
