package extensionTools.opcatLayoutManager.Constraints;

import java.text.MessageFormat;
import java.util.*;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Springs.*;


/**
 * Provides means for avoiding of node-edge overlapping.
 */
public class NodeEdgeOverlapConstraint extends Constraint
{
    /**
     * Initializes an instance of the class.
     *
     * @param node The node.
     * @param edge The edge.
     */
    public NodeEdgeOverlapConstraint(NodeContext node, EdgeContext edge)
    {
        super(Constraint.directionOther, true, null);
        this.m_nodeConnection = new SpringConnectionPoint(node, true);
        this.m_edge           = edge;
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return MessageFormat.format( "[Node-edge overlapping for {0} and {1}]",
                                     new Object[] {this.m_nodeConnection.m_node, this.m_edge});
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

            double springRestLength = this.m_nodeConnection.m_node.GetRadius();

            //
            // Add three springs:
            //
            //                                      +-----+
            //                                  ~~~~|     |
            //                                      +--+--+
            //                                         |
            //   +-------------------------+  main     |
            //   | m_nodeConnection.m_node |~~~~~~~~   |
            //   +-------------------------+           | m_edge
            //                                         |
            //                                         |
            //                                      +--+--+
            //                                  ~~~~|     |
            //                                      +-----+
            //

            NodeOrthogonalToEdgeSpring mainSpring = new NodeOrthogonalToEdgeSpring(this, springRestLength, this.m_nodeConnection, this.m_edge);
            mainSpring.AttachToNodes();
            this.m_springs.add(mainSpring);

            Spring spring1 = new EdgeNodeOrthogonalToNodeSpring( this,
                                                                 new SpringConnectionPoint(this.m_edge.GetNode1(), true),
                                                                 mainSpring);
            spring1.AttachToNodes();
            this.m_springs.add(spring1);

            Spring spring2 = new EdgeNodeOrthogonalToNodeSpring( this,
                                                                 new SpringConnectionPoint(this.m_edge.GetNode2(), true),
                                                                 mainSpring);
            spring2.AttachToNodes();
            this.m_springs.add(spring2);
        }

        return this.m_springs;
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#GetSpringConstant(Spring) the overridden method}.
     */
    public double GetSpringConstant(Spring spring)
    {
        return this.m_nodeConnection.m_node.Overlaps(this.m_edge) ? super.GetSpringConstant(spring) : 0;
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Show() the overridden method}.
     */
    public void Show()
    {
        this.m_nodeConnection.m_node.Select(true);
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Hide() the overridden method}.
     */
    public void Hide()
    {
        this.m_nodeConnection.m_node.Select(false);
    }

    private SpringConnectionPoint m_nodeConnection;
    private EdgeContext           m_edge;
}
