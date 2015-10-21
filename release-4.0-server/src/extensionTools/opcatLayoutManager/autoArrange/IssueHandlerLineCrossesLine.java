/*
 * Created on Feb 10, 2007
 */
package extensionTools.opcatLayoutManager.autoArrange;

//import extensionTools.opcatLayoutManager.Debug;
import gui.opdGraphics.lines.OpdLine;

import java.awt.geom.Line2D;
import java.util.List;

import javax.swing.JComponent;

/**
 * Class that handles the issue of links intersection. If two intersected links are 
 * detected the handler adds the swapping operations for the edges' sources or destination nodes
 */
public class IssueHandlerLineCrossesLine extends IssueHandler
{
    private JComponent node1_1 = null, node1_2 = null, node2_1 = null, node2_2 = null;
    
    public String getId()
    {
        return StandardIssueHandlers.LINE_CROSSES_LINE;
    }
    /**
     * If two input instances are interesecting links, then the issue
     * is evaluated by getWeight(), else - returns 0
     * @param inst1 - first instance
     * @param inst2 - second instance
     * @return issue weight
     */
    public long evaluateIssue(Object inst1, Object inst2)
    {
        if ((! isLine(inst1)) || (! isLine(inst2)))
            return 0;
        
        long weight = 0;
        if ((inst1 instanceof OpdLine) && (inst2 instanceof OpdLine)) {
            OpdLine line1 = (OpdLine) inst1;
            OpdLine line2 = (OpdLine) inst2;
            
            boolean answer = Line2D.linesIntersect(line1.getX(), line1.getY(), 
                    line1.getX() + line1.getWidth(), line1.getY() + line1.getHeight(),
                    line2.getX(), line2.getY(), 
                    line2.getX() + line2.getWidth(), line2.getY() + line2.getHeight());
            
            if (! answer)
                return 0;
            
            weight = getWeight();
        }

        if (inst1 instanceof OpdLine[]) {
            OpdLine[] lineArr = (OpdLine[]) inst1;
            for (int i = 0; i < lineArr.length; i++)
                weight += evaluateIssue(lineArr[i], inst2);
        }
        
        if (inst2 instanceof OpdLine[]) {
            OpdLine[] lineArr = (OpdLine[]) inst2;
            for (int i = 0; i < lineArr.length; i++)
                weight += evaluateIssue(inst1, lineArr[i]);
        }
        
        node1_1 = extractEdgeNode(inst1, 1);
        node1_2 = extractEdgeNode(inst1, 2);
        node2_1 = extractEdgeNode(inst2, 1);
        node2_2 = extractEdgeNode(inst2, 2);
        
        return weight;
    }

    /**
     * Adds to the given operations list the relevant operations for this issue type
     * and for this issue participant instances
     * @param operations - list of operations to add entries to
     */
    public void accumulateOperations(List operations)
    {
        if ((node1_1 == null) || (operations == null))
            return;
        
        operations.add(new IssueOperation[] { new SwappingOperation(node1_2, node2_2) });
        operations.add(new IssueOperation[] { new SwappingOperation(node1_2, node2_1) });
    }
    /**
     * Returns constant value - issue weight.
     * @return non-zero weight if the issue takes place and 0 otherwise
     */
    public long getWeight()
    {
        return 500;
    }
}
