package extensionTools.opcatLayoutManager.Constraints;

import java.text.MessageFormat;
import java.util.*;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Springs.*;

/**
 * Provides means for preserving horizontal or vertical order of two nodes.
 */
public class SequenceConstraint extends TwoNodeConstraint
{
    /**
     * Initializes an instance of the class.
     *
     * @param node1 The first node.
     * @param node2 The second node.
     *
     * @param direction Specifies the direction of the order.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     */
    public SequenceConstraint( NodeContext node1,
                               NodeContext node2,
                               int         direction,
                               boolean     ignoreCurrentPosition)
    {
        super( direction,
               false,
               node1,
               node2,
               ignoreCurrentPosition ? null : new NodesCoordinateComparator(direction, rangeEndToStart));

        if (ignoreCurrentPosition)
        {
            this.m_comparator = new NodesCoordinateComparator(direction, rangeEndToStart);
        }
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return MessageFormat.format( "[{0} sequence of nodes {1} and {2}]",
                                     new Object[] { GetDirectionString(this.m_direction),
                                                    this.m_nodeConnection1.m_node,
                                                    this.m_nodeConnection2.m_node});

    }

    /**
     * Refer to  {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Attach() the overridden method}.
     */
    public LinkedList Attach()
    {
        Debug.Print(25, "Attaching springs for constraint {0}", new Object[] {this});

        if (null == this.m_springs)
        {
            this.m_springs = new LinkedList();

            //
            // We rely on the constructor to sort m_nodeConnection1 and m_nodeConnection2.
            //

            //
            // The ahchor line is orthogonal to the springs.
            //
            int anchorLineDirection = FlipDirection(this.m_direction);

            NodeOrthogonalToLineSpring negativeInfinitySpring = new NodeOrthogonalToLineSpring( this,
                                                                                                0,
                                                                                                this.m_nodeConnection1,
                                                                                                GetAxisAlignedLine(anchorLineDirection, -infinity));
            negativeInfinitySpring.AttachToNodes();
            this.m_springs.add(negativeInfinitySpring);

            NodeOrthogonalToLineSpring positiveInfinitySpring = new NodeOrthogonalToLineSpring( this,
                                                                                                0,
                                                                                                this.m_nodeConnection2,
                                                                                                GetAxisAlignedLine(anchorLineDirection, infinity));
            positiveInfinitySpring.AttachToNodes();
            this.m_springs.add(positiveInfinitySpring);
        }

        return this.m_springs;
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#GetSpringConstant(Spring) the overridden method}.
     */
    public double GetSpringConstant(Spring spring)
    {
        return (this.m_comparator.compare(this.m_nodeConnection1.m_node, this.m_nodeConnection2.m_node) > 0) ?
               super.GetSpringConstant(spring) * 0.2                                          :
               0;
    }

    private static final int infinity = 1000;
}