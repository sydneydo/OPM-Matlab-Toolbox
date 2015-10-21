package extensionTools.opcatLayoutManager.Springs;

import java.text.MessageFormat;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Constraints.Constraint;


/**
 * Represents a spring connecting a node and an edge (normal to the edge).
 */
public class NodeOrthogonalToEdgeSpring extends SingleNodeSpring
{
    /**
     * Initializes an instance of the class.
     *
     * @param constraint       Specifies the constraint, that creates this spring.
     * @param springRestLength Specifies the rest length of this spring.
     * @param nodeConnection   Specifies the node connection point for this spring.
     * @param line             Specifies the edge for this spring.
     */
    public NodeOrthogonalToEdgeSpring( Constraint            constraint,
                                       double                springRestLength,
                                       SpringConnectionPoint nodeConnection,
                                       EdgeContext           edge)
    {
        super(constraint, springRestLength, nodeConnection);

        this.m_edge   = edge;
        this.m_spring = new NodeOrthogonalToLineSpring(constraint, springRestLength, this.m_nodeConnection, edge.GetLine());
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return MessageFormat.format( "[{0} - {1} by constraint {2}]",
                                     new Object[] {this.m_nodeConnection.m_node, this.m_edge, this.m_constraint});
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.Spring#GetForce(NodeContext) the overridden method}.
     */
    public Vector2D GetForce(NodeContext node)
    {
        this.m_spring.SetLine(this.m_edge.GetLine());
        return this.m_spring.GetForce(node);
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.SingleNodeSpring#InternalGetForce(NodeContext) the overridden method}.
     */
    protected Vector2D InternalGetForce(NodeContext node)
    {
        //
        // This function should never be called.
        //
        
        return new Vector2D();
    }

    private EdgeContext                m_edge;
    private NodeOrthogonalToLineSpring m_spring;
}
