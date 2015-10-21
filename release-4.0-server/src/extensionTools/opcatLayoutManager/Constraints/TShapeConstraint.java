package extensionTools.opcatLayoutManager.Constraints;

import java.text.MessageFormat;
import java.util.*;
import extensionTools.opcatLayoutManager.*;

/**
 * Provides means for T-shape (tree) alignment of nodes.
 * <p>
 * <pre>
 *                              +-----+
 *                              |     |
 *                              +--+--+
 *                                 |
 *                                /_\
 *                                 |
 *           +-----------+---------+---------+----------+
 *           |           |                   |          |
 *        +--+--+     +--+--+             +--+--+    +--+--+
 *        |  1  |     |  2  |   .......   | n-1 |    |  n  |
 *        +--+--+     +--+--+             +--+--+    +--+--+
 * </pre>
 */
public class TShapeConstraint extends MultiNodeConstraint
{
    /**
     * Initializes an instance of the class.
     *
     * @param root         The root node of the T-shape.
     * @param relationNode The node representing a relation, may be null.
     */
    public TShapeConstraint(NodeContext root, NodeContext relationNode, Iterator nodes)
    {
        super(directionHorizontal, root, nodes, null);
        this.m_mainNodeConnection.m_applyForce = true;

        if (null != relationNode)
        {
            //
            // Make sure the relation node is not included in the nodes list.
            //
            this.RemoveNodeConnection(relationNode);

            //
            // Align the relation node with the parent node.
            //
            LinkedList list = new LinkedList();
            list.add(relationNode);
            this.m_relationVerticallyAlignedWithParent = new AlignmentConstraint( directionVertical,
                                                                             originCenter,
                                                                             root,
                                                                             list.iterator());

            //
            // Make sure the relation node is below the parent node.
            //
            this.m_relationBelowParent = new SequenceConstraint( root,
                                                            relationNode,
                                                            directionVertical,
                                                            true);
        }

        if (this.m_nodes.size() > 0)
        {
            //
            // Make sure the children nodes are below the parent and the relation nodes.
            //
            this.m_childrenBelowParentAndRelation = new SequenceConstraint( null != relationNode ? relationNode : root,
                                                                       ((SpringConnectionPoint)this.m_nodes.getFirst()).m_node,
                                                                       directionVertical,
                                                                       true);

            if (this.m_nodes.size() > 1)
            {
                //
                // Horizontally align child nodes.
                //
                this.m_childrenHorizontallyAligned = new AlignmentConstraint( directionHorizontal,
                                                                         originStart,
                                                                         null,
                                                                         this.m_nodes.iterator());
                //
                // Horizontally space child nodes.
                //
                this.m_childrenHorizontallyDistributed = new EqualSpacingConstraint( directionHorizontal,
                                                                                this.m_nodes.iterator(),
                                                                                10,
                                                                                true);
                //
                // Center the parent node.
                // Note that the first and the last child node are used as reference but
                // are not affected themselves.
                //

                SpringConnectionPoint firstChildConnection = new SpringConnectionPoint(((SpringConnectionPoint) this.m_nodes.getFirst()).m_node, false);
                SpringConnectionPoint lastChildConnection  = new SpringConnectionPoint(((SpringConnectionPoint) this.m_nodes.getLast()).m_node, false);

                LinkedList list = new LinkedList();

                list.add(firstChildConnection);
                list.add(this.m_mainNodeConnection);
                list.add(lastChildConnection);

                this.m_parentHorizontallyCentered = new EqualSpacingConstraint( directionHorizontal,
                                                                           list.iterator(),
                                                                           10,
                                                                           true);
            }
            else
            {
                //
                // Just align the single child node with the parent.
                //
                this.m_parentHorizontallyCentered = new AlignmentConstraint( directionVertical,
                                                                        originCenter,
                                                                        root,
                                                                        this.m_nodes.iterator());
            }
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
        return MessageFormat.format( "[TShape constraint for {0} as root and nodes {1}.]",
                                     new Object[] {this.m_mainNodeConnection.m_node, this.GetNodesString()});
    }

    /**
     * Refer to  {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Attach() the overridden method}.
     */
    public LinkedList Attach()
    {
        Debug.Print(25, "Attaching springs for constraint {0}", new Object[] {this});

        if (null == this.m_springs)
        {
            //
            // Attach springs for all constraints.
            //

            this.m_springs = new LinkedList();

            if (null != this.m_relationVerticallyAlignedWithParent)
            {
                this.m_springs.addAll(this.m_relationVerticallyAlignedWithParent.Attach());
            }

            if (null != this.m_parentHorizontallyCentered)
            {
                this.m_springs.addAll(this.m_parentHorizontallyCentered.Attach());
            }

            if (null != this.m_childrenHorizontallyAligned)
            {
                this.m_springs.addAll(this.m_childrenHorizontallyAligned.Attach());
            }

            if (null != this.m_childrenHorizontallyDistributed)
            {
                this.m_springs.addAll(this.m_childrenHorizontallyDistributed.Attach());
            }

            if (null != this.m_relationBelowParent)
            {
                this.m_springs.addAll(this.m_relationBelowParent.Attach());
            }

            if (null != this.m_childrenBelowParentAndRelation)
            {
                this.m_springs.addAll(this.m_childrenBelowParentAndRelation.Attach());
            }
        }

        return this.m_springs;
    }

    private Constraint m_relationVerticallyAlignedWithParent = null;
    private Constraint m_parentHorizontallyCentered          = null;
    private Constraint m_childrenHorizontallyAligned         = null;
    private Constraint m_childrenHorizontallyDistributed     = null;
    private Constraint m_relationBelowParent                 = null;
    private Constraint m_childrenBelowParentAndRelation      = null;
}
