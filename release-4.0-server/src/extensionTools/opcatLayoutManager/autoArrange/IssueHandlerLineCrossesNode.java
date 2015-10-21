/*
 * Created on Jan 24, 2007
 */
package extensionTools.opcatLayoutManager.autoArrange;

//import extensionTools.opcatLayoutManager.Debug;
import gui.opdGraphics.lines.OpdLine;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.List;
import javax.swing.JComponent;
/**
 * Class that handles the issue of the node intersecting with edge. If the intersection is
 * detected the handler adds the set of operations for possible fixes
 */
class IssueHandlerLineCrossesNode extends IssueHandler
{
    private BaseGraphicComponent node = null;
    
    private Object lineObj = null;
    
    private long value = -1;
    /**
     * Detects whether the input line intersects with the input rectangle
     * @param lineObj - input line or the array of line parts (if not straight)
     * @param r - input rectangle
     */  
    private boolean lineIntersectsRectangle(Object lineObj, Rectangle r)
    {
        if (lineObj instanceof OpdLine[]) {
            OpdLine[] lineArr = (OpdLine[]) lineObj;
            
            for (int i = 0; i < lineArr.length; i++)
                if (lineIntersectsRectangle(lineArr[i], r))
                    return true;
            return false;
        }
        
        OpdLine line = (OpdLine) lineObj;
        
        Point src = new Point(line.getX(), line.getY());
        Point dst = new Point(line.getX() + line.getWidth(), line.getY() + line.getHeight());
        
        //handle a trivial case - zero-length line. In this case we will not do anything,
        //  because all the arrangement will be done via adjacent components
        if (src.distance(dst) > 5) {
            if (r.intersectsLine(src.x, src.y, dst.x, dst.y))
                return true;
        }
        
        return false;
    }

    /**
     * @see IssueHandler#getId()
     */
    public String getId()
    {
        return StandardIssueHandlers.LINE_CROSSES_NODE;
    }
    
    /**
     * @see IssueHandler#evaluateIssue(JComponent, JComponent)
     */
    public long evaluateIssue(Object inst1, Object inst2)
    {
        value = 0;
        
        if (! (((inst1 instanceof BaseGraphicComponent) && isLine(inst2)) ||
                (isLine(inst1) && (inst2 instanceof BaseGraphicComponent))))
            return value;
        
        if (inst1 instanceof BaseGraphicComponent) {
            node = (BaseGraphicComponent) inst1;
            lineObj = inst2;
        } else {
            node = (BaseGraphicComponent) inst2;
            lineObj = inst1;
        }
        
        //exclude the case when the node is actually one of the edges of the line
        if ((node == extractEdgeNode(lineObj, 1)) || 
                (node == extractEdgeNode(lineObj, 2)))
            return value;
        
        if (lineIntersectsRectangle(lineObj, node.getBounds())) {
            value = getWeight();
            //Debug.Print("[n X L] " + node.getName() + "-" + Utils.showClass(line) + "(" + node.getBounds() + "-" + Utils.showClass(line) + ")");
        }

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
        
        Rectangle nodeRect = node.getBounds();
        Point nodeCenter = new Point(
                nodeRect.x + nodeRect.width / 2, nodeRect.y + nodeRect.height / 2);
        int nodeRadius = Utils.round(Math.sqrt(
                Utils.square(nodeRect.getWidth() / 2) + Utils.square(nodeRect.getHeight() / 2)));
        
        Point src = null, dst = null;
        if (lineObj instanceof OpdLine[]) {
            OpdLine[] lineArr = (OpdLine[]) lineObj;
            src = new Point(lineArr[0].getX(), lineArr[0].getY());
            dst = new Point(
                    lineArr[lineArr.length - 1].getX() + lineArr[lineArr.length - 1].getWidth(), 
                    lineArr[lineArr.length - 1].getY() + lineArr[lineArr.length - 1].getHeight());
        } else {
            OpdLine line = (OpdLine) lineObj;
            src = new Point(line.getX(), line.getY());
            dst = new Point(line.getX() + line.getWidth(), line.getY() + line.getHeight());
        }
        
        JComponent node1 = extractEdgeNode(lineObj, 1);
        JComponent node2 = extractEdgeNode(lineObj, 2);
        
        int centerToLineDist = Utils.round(Line2D.ptLineDist(
                src.x, src.y, dst.x, dst.y, nodeCenter.x, nodeCenter.y));
        int newCenterToLineDist = 4 * nodeRadius;
        int lineLength = Utils.round(src.distance(dst));
        int relCCW = (new Line2D.Double(src, dst)).relativeCCW(nodeCenter);
        if (relCCW == 0)
            relCCW = 1;
        
        double sin = relCCW * ((double) (dst.y - src.y)) / lineLength;
        double cos = relCCW * ((double) (dst.x - src.x)) / lineLength;

        //for each of the below operations try a few options: first try to move
        //  the operations the farthest; if it causes problems, move it closer
        
        //move the node farther away from the line, and then across the line and then away
        IssueOperation[] ops1 = new IssueOperation[2];
        IssueOperation[] ops2 = new IssueOperation[2];
        
        int xShiftStraight = Utils.round((newCenterToLineDist - centerToLineDist) * sin);
        int yShiftStraight = Utils.round((newCenterToLineDist - centerToLineDist) * (-cos));
        int xShiftAcross =   Utils.round((newCenterToLineDist + centerToLineDist) * (-sin));
        int yShiftAcross =   Utils.round((newCenterToLineDist + centerToLineDist) * cos);
        
        ops1[0] = new SingleShiftingOperation(node, xShiftStraight, yShiftStraight);
        ops2[0] = new SingleShiftingOperation(node, xShiftAcross, yShiftAcross);
        
        xShiftStraight = Utils.round((newCenterToLineDist / 2 - centerToLineDist) * sin);
        yShiftStraight = Utils.round((newCenterToLineDist / 2 - centerToLineDist) * (-cos));
        xShiftAcross   = Utils.round((newCenterToLineDist / 2 + centerToLineDist) * (-sin));
        yShiftAcross   = Utils.round((newCenterToLineDist / 2 + centerToLineDist) * cos);
        
        ops1[1] = new SingleShiftingOperation(node, xShiftStraight, yShiftStraight);
        ops2[1] = new SingleShiftingOperation(node, xShiftAcross, yShiftAcross);
        
        operations.add(ops1);
        operations.add(ops2);
        
        
        //move the line (by both ends) farther away from the node, and then across 
        //  the node and then away
        ops1 = new IssueOperation[2];
        ops2 = new IssueOperation[2];
        
        xShiftStraight = Utils.round((newCenterToLineDist - centerToLineDist) * sin);
        yShiftStraight = Utils.round((newCenterToLineDist - centerToLineDist) * (-cos));
        xShiftAcross   = Utils.round((newCenterToLineDist + centerToLineDist) * (-sin));
        yShiftAcross   = Utils.round((newCenterToLineDist + centerToLineDist) * cos);
        
        //move both ends of the line away from the node
        ops1[0] = new SingleShiftingOperation(node1, - xShiftStraight, - yShiftStraight);
        ops1[0].setNextOperation(
                new SingleShiftingOperation(node2, - xShiftStraight, - yShiftStraight));
        
        //move both ends of the line across the node and then away
        ops2[0] = new SingleShiftingOperation(node1, - xShiftAcross, - yShiftAcross);
        ops2[0].setNextOperation(
                new SingleShiftingOperation(node2, - xShiftAcross, - yShiftAcross));

        xShiftStraight = Utils.round((newCenterToLineDist / 2 - centerToLineDist) * sin);
        yShiftStraight = Utils.round((newCenterToLineDist / 2 - centerToLineDist) * (-cos));
        xShiftAcross   = Utils.round((newCenterToLineDist / 2 + centerToLineDist) * (-sin));
        yShiftAcross   = Utils.round((newCenterToLineDist / 2 + centerToLineDist) * cos);
        
        //move both ends of the line away from the node
        ops1[1] = new SingleShiftingOperation(node1, - xShiftStraight, - yShiftStraight);
        ops1[1].setNextOperation(
                new SingleShiftingOperation(node2, - xShiftStraight, - yShiftStraight));
        
        //move both ends of the line across the node and then away
        ops2[1] = new SingleShiftingOperation(node1, - xShiftAcross, - yShiftAcross);
        ops2[1].setNextOperation(
                new SingleShiftingOperation(node2, - xShiftAcross, - yShiftAcross));

        operations.add(ops1);
        operations.add(ops2);
    }

    /**
     * @see IssueHandler#getWeight()
     */
    public long getWeight()
    {
        return 1000;
    }
}
