package extensionTools.opcatLayoutManager.Springs;

import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Constraints.Constraint;


/**
 * Defines a common interface and provides basic implementation for various
 * single-node spring classes. These springs connect to a node and an additional,
 * spring specific point.
 */
public abstract class SingleNodeSpring extends Spring
{
    /**
     * Initializes an instance of the class.
     *
     * @param constraint       Specifies the constraint, that creates this spring.
     * @param springRestLength Specifies the rest length of this spring.
     * @param nodeConnection   Specifies the node connection point for this spring.
     */
    protected SingleNodeSpring( Constraint            constraint,
                                double                springRestLength,
                                SpringConnectionPoint nodeConnection)
    {
        super(constraint, springRestLength);
        this.m_nodeConnection = nodeConnection;
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.Spring#AttachToNodes() the overridden method}.
     */
    public void AttachToNodes()
    {
        Debug.Print(25, "Attaching spring {0}", new Object[] {this});
        this.m_nodeConnection.Attach(this);
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.Spring#DetachFromNodes() the overridden method}.
     */
    public void DetachFromNodes()
    {
        this.m_nodeConnection.m_node.RemoveSpring(this);
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.Spring#GetForce(NodeContext) the overridden method}.
     */
    public Vector2D GetForce(NodeContext node)
    {
        if ((null != node) && !node.equals(this.m_nodeConnection.m_node))
        {
            
            return new Vector2D();
        }

        if (!this.m_nodeConnection.m_applyForce)
        {
            return new Vector2D();
        }

        Debug.Print( 25,
                     "Calculating force on node {0} by constraint {1}",
                     new Object[] {this.m_nodeConnection.m_node, this.m_constraint});

        return this.InternalGetForce(node);
    }

    /**
     * Return the node, this spring is connected to.
     *
     * @return the node, this spring is connected to.
     */
    public NodeContext GetNode()
    {
        return this.m_nodeConnection.m_node;
    }

    /**
     * An internal routine for force calculation. To be overridden by derived classes.
     *
     * @param node the node
     *
     * @return the force
     */
    protected abstract Vector2D InternalGetForce(NodeContext node);

    protected SpringConnectionPoint m_nodeConnection;
}
