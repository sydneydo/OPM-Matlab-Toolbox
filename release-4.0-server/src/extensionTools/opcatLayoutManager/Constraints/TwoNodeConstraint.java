package extensionTools.opcatLayoutManager.Constraints;

import java.util.*;
import extensionTools.opcatLayoutManager.*;

/**
 * Defines a common interface and provides basic implementation for various
 * two-node constraint classes.
 */
public abstract class TwoNodeConstraint extends Constraint
{
    /**
     * Initializes an instance of the class.
     *
     * @param direction Specifies the direction of the constraint, if aplicable.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#directionHorizontal Constraints.direction* constants}.
     *
     * @param isSyntactic Specifies whether the constraint is syntactic or not (semantic).
     *
     * @param node1      The first node.
     *
     * @param node2      The second node.
     *
     * @param comparator The comparator to use to sort the nodes. If null, the nodes are not sorted.
     */
    protected TwoNodeConstraint( int         direction,
                                 boolean     isSyntactic,
                                 NodeContext node1,
                                 NodeContext node2,
                                 Comparator  comparator)
    {
        this( direction,
              isSyntactic,
              new SpringConnectionPoint(node1, true),
              new SpringConnectionPoint(node2, true),
              comparator);
    }

    /**
     * Initializes an instance of the class.
     *
     * @param direction       Specifies the direction of the constraint, if aplicable.
     *                        The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#directionHorizontal Constraints.direction* constants}.
     *
     * @param isSyntactic     Specifies whether the constraint is syntactic or not (semantic).
     *
     * @param nodeConnection1 The first node connection.
     *
     * @param nodeConnection2 The second node connection.
     *
     * @param comparator      The comparator to use to sort the nodes. If null, the nodes are not sorted.
     */
    protected TwoNodeConstraint( int                   direction,
                                 boolean               isSyntactic,
                                 SpringConnectionPoint nodeConnection1,
                                 SpringConnectionPoint nodeConnection2,
                                 Comparator            comparator)
    {
        super(direction, isSyntactic, comparator);

        this.m_nodeConnection1 = nodeConnection1;
        this.m_nodeConnection2 = nodeConnection2;

        if ((null != comparator) && (Constraint.directionOther != direction))
        {
            //
            // Make sure m_nodeConnection1 and m_nodeConnection2 are sorted in the constraint direction.
            //
            if (comparator.compare(nodeConnection1.m_node, nodeConnection2.m_node) > 0)
            {
                SpringConnectionPoint tmp = this.m_nodeConnection2;
                this.m_nodeConnection2 = this.m_nodeConnection1;
                this.m_nodeConnection1 = tmp;
            }
        }
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Show() the overridden method}.
     */
    public void Show()
    {
        this.m_nodeConnection1.m_node.Select(true);
        this.m_nodeConnection2.m_node.Select(true);
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Hide() the overridden method}.
     */
    public void Hide()
    {
        this.m_nodeConnection1.m_node.Select(false);
        this.m_nodeConnection2.m_node.Select(false);
    }

    protected SpringConnectionPoint m_nodeConnection1;
    protected SpringConnectionPoint m_nodeConnection2;
}
