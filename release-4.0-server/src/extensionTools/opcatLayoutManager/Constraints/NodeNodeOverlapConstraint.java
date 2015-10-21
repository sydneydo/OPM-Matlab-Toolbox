package extensionTools.opcatLayoutManager.Constraints;

import java.text.MessageFormat;
import java.util.*;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Springs.*;

/**
 * Provides means for avoiding of node-node overlapping.
 */
public class NodeNodeOverlapConstraint extends TwoNodeConstraint
{
    /**
     * Initializes an instance of the class.
     *
     * @param node1 The first node.
     * @param node2 The second node.
     */
    public NodeNodeOverlapConstraint(NodeContext node1, NodeContext node2)
    {
        super(Constraint.directionOther, true, node1, node2, null);
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return MessageFormat.format( "[Node-node overlapping for {0} and {1}]",
                                     new Object[] {this.m_nodeConnection1.m_node, this.m_nodeConnection2.m_node});
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Attach() the overridden method}.
     */
    public LinkedList Attach()
    {
        Debug.Print(25, "Attaching springs for constraint {0}", new Object[] {this});

        if (null == this.m_springs)
        {
            this.m_springs = new LinkedList();

            double springRestLength = (this.m_nodeConnection1.m_node.GetRadius() + this.m_nodeConnection2.m_node.GetRadius());
            Spring spring = new NodeNodeSpring(this, springRestLength, this.m_nodeConnection1, this.m_nodeConnection2);
            spring.AttachToNodes();
            this.m_springs.add(spring);
        }

        return this.m_springs;
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#GetSpringConstant(Spring) the overridden method}.
     */
    public double GetSpringConstant(Spring spring)
    {
        return this.m_nodeConnection1.m_node.Overlaps(this.m_nodeConnection2.m_node) ? super.GetSpringConstant(spring) : 0;
    }
}
