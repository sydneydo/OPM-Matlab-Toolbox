package extensionTools.opcatLayoutManager.Constraints;

import java.text.MessageFormat;
import java.util.*;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Springs.*;

/**
 * Provides means for horizontal or vertical spacing of nodes.
 */
public class EqualSpacingConstraint extends MultiNodeConstraint
{
    /**
     * Initializes an instance of the class.
     *
     * @param direction             Specifies the direction of the nodes distribution.
     *                              The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     *
     * @param nodes                 Nodes to add to the constraint.
     *
     * @param minimumSpace          Specifies the minimum space between nodes.
     *
     * @param ignoreInitialPosition Specifies whether the initial nodes position should be taken
     *                              into account or the nodes can be completely rearranged.
     */
    public EqualSpacingConstraint(int direction, Iterator nodes, int minimumSpace, boolean ignoreInitialPosition)
    {
        super( direction,
               null,
               nodes,
               ignoreInitialPosition ? null : new NodesCoordinateComparator(direction, rangeCenterToCenter));

        this.m_minimumSpace          = minimumSpace;
        this.m_ignoreInitialPosition = ignoreInitialPosition;
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return MessageFormat.format( "[{0} equal spacing of nodes {1}]",
                                     new Object[] {GetDirectionString(this.m_direction), this.GetNodesString()});

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

            if (this.m_nodes.size() > 1)
            {
                double space = this.CalculateSpace();

                ListIterator iterNodes = this.m_nodes.listIterator();
                SpringConnectionPoint nodeConnection1 = (SpringConnectionPoint)iterNodes.next();
                SpringConnectionPoint nodeConnection2;

                while (iterNodes.hasNext())
                {
                    nodeConnection2 = (SpringConnectionPoint)iterNodes.next();

                    double springLength = nodeConnection1.m_node.GetSize(this.m_direction) / 2 +
                                          nodeConnection2.m_node.GetSize(this.m_direction) / 2 +
                                          space;

                    Spring spring = new NodeNodeAxisAlignedSpring( this,
                                                                   springLength,
                                                                   nodeConnection1,
                                                                   nodeConnection2,
                                                                   this.m_direction);
                    spring.AttachToNodes();
                    this.m_springs.add(spring);

                    nodeConnection1 = nodeConnection2;
                }
            }
        }

        return this.m_springs;
    }

    /**
     * Calculates the space between adjacent nodes edges.
     *
     * @return the space between adjacent nodes edges.
     */
    private double CalculateSpace()
    {
        if (this.m_ignoreInitialPosition)
        {
            return this.m_minimumSpace;
        }

        //
        // Calculate the space so that the nodes will be distributed equally between the outer nodes.
        //

        //
        // Calculate the available free space and distribute it equally between nodes.
        // If there is no way to position nodes without overlapping,
        // the free space value will be negaive.
        //

        MinMax minMax         = this.GetMinMaxPositions(this.m_direction, Constraint.rangeStartToEnd);
        double availableSpace = minMax.max - minMax.min;
        double usedSpace      = 0;

        for (ListIterator iterNodes = this.m_nodes.listIterator(); iterNodes.hasNext(); /* inside loop */)
        {
            NodeContext node = ((SpringConnectionPoint)iterNodes.next()).m_node;
            usedSpace += node.GetSize(this.m_direction);
        }

        double calculatedSpace = (availableSpace - usedSpace) / (this.m_nodes.size() - 1);

        return Math.max(calculatedSpace, this.m_minimumSpace);
    }

    private int     m_minimumSpace;
    private boolean m_ignoreInitialPosition;
}
