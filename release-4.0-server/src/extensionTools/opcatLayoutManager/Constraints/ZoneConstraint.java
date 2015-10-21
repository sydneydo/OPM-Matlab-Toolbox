package extensionTools.opcatLayoutManager.Constraints;

import java.text.MessageFormat;
import java.util.*;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Springs.*;

/**
 * Provides means for limitation of nodes positions to the interior of another node.
 */
public class ZoneConstraint extends MultiNodeConstraint
{
    /**
     * Initializes an instance of the class.
     *
     * @param zoneNode The node that defines the limit.
     *
     * @param nodes Nodes to add to the constraint.
     */
    public ZoneConstraint(NodeContext zoneNode, Iterator nodes)
    {
        super(Constraint.directionOther, zoneNode, nodes, null);
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return MessageFormat.format( "[{0} in zone of {1}]",
                                     new Object[] {this.GetNodesString(), this.m_mainNodeConnection.m_node});
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

            for (ListIterator iterNodes = this.m_nodes.listIterator(); iterNodes.hasNext(); /* inside loop */)
            {
                SpringConnectionPoint nodeConnection = ((SpringConnectionPoint)iterNodes.next());
                Spring spring = new NodeNodeSpring(this, 0, this.m_mainNodeConnection, nodeConnection);
                spring.AttachToNodes();
                this.m_springs.add(spring);
            }
        }

        return this.m_springs;
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#GetSpringConstant(Spring) the overridden method}.
     */
    public double GetSpringConstant(Spring spring)
    {
        NodeContext   zoneNode = this.m_mainNodeConnection.m_node;
        TwoNodeSpring spring2  = (TwoNodeSpring)spring;

        return zoneNode.Contains(spring2.GetPeerNode(zoneNode)) ?
               0                                                :
               5 * super.GetSpringConstant(spring);
    }
}
