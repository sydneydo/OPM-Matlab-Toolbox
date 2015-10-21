package extensionTools.opcatLayoutManager;

import java.util.Comparator;
import extensionTools.opcatLayoutManager.NodeContext;
import extensionTools.opcatLayoutManager.Constraints.Constraint;

/**
 * Implements comparison of two nodes by coordinate.
 */
public class NodesCoordinateComparator implements Comparator
{
    /**
     * Initializes an instance of the class.
     *
     * @param direction Specifies the direction of the comparison.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#directionHorizontal Constraints#directionHorizontal and Constraints#directionVertical constants}.
     *
     * @param range     Specifies how the comparison should be performed.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#rangeStartToStart Constraints#range constants}.
     */
    public NodesCoordinateComparator(int direction, int range)
    {
        this.m_direction = direction;
        this.m_range     = range;
    }

    /**
     * Compares two nodes by coordinate in the constraint direction.
     *
     * @param o1 the first node to compare
     *
     * @param o2 the second node to compare
     *
     * @return <p>
     *         -1 if the first node coordinate in the comparator direction
     *            is smaller than that of the second node
     *         <p>
     *          0 if the first node coordinate in the comparator direction
     *            is the same as that of the second node
     *         <p>
     *          1 if the first node coordinate in the comparator direction
     *            is greater than that of the second node
     */
    public int compare(Object o1, Object o2)
    {
        return compare(o1, o2, this.m_direction, this.m_range);
    }

    /**
     * Compares two nodes by coordinate in given direction and range.
     *
     * @param o1        the first node to compare
     *
     * @param o2        the second node to compare
     *
     * @param direction Specifies the direction of the comparison.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#directionHorizontal Constraints#directionHorizontal and Constraints#directionVertical constants}.
     *
     * @param range     Specifies how the comparison should be performed.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#rangeStartToStart Constraints#range constants}.
     * @return <p>
     *         -1 if the first node coordinate in the comparator direction
     *            is smaller than that of the second node
     *         <p>
     *          0 if the first node coordinate in the comparator direction
     *            is the same as that of the second node
     *         <p>
     *          1 if the first node coordinate in the comparator direction
     *            is greater than that of the second node
     */
    public static int compare(Object o1, Object o2, int direction, int range)
    {
        NodeContext node1 = (NodeContext)o1;
        NodeContext node2 = (NodeContext)o2;

        double coord1 = 0;
        double coord2 = 0;

        switch (range)
        {
            case Constraint.rangeStartToStart:
                coord1 = node1.GetStart(direction);
                coord2 = node2.GetStart(direction);
                break;

            case Constraint.rangeCenterToCenter:
                coord1 = node1.GetCenter(direction);
                coord2 = node2.GetCenter(direction);
                break;

            case Constraint.rangeEndToStart:
                coord1 = node1.GetEnd(direction);
                coord2 = node2.GetStart(direction);
                break;

                default:
                    
        }

        if (coord1 < coord2)
        {
            return -1;
        }

        if (coord1 > coord2)
        {
            return 1;
        }

        return 0;
    }

    private int m_direction;
    private int m_range;
}

