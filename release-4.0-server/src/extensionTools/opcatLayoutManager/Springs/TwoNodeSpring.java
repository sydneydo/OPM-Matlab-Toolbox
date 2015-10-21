package extensionTools.opcatLayoutManager.Springs;


import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Constraints.Constraint;


/**
 * Defines a common interface and provides basic implementation for various
 * two-node spring classes.
 */
public abstract class TwoNodeSpring extends Spring
{
    /**
     * Initializes an instance of the class.
     *
     * @param constraint       Specifies the constraint, that creates this spring.
     * @param springRestLength Specifies the rest length of this spring.
     * @param nodeConnection1  Specifies the first node connection point for this spring.
     * @param nodeConnection2  Specifies the second node connection point for this spring.
     */
    public TwoNodeSpring( Constraint            constraint,
                          double                springRestLength,
                          SpringConnectionPoint nodeConnection1,
                          SpringConnectionPoint nodeConnection2)
    {
        super(constraint, springRestLength);
        this.m_nodeConnection1 = nodeConnection1;
        this.m_nodeConnection2 = nodeConnection2;
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.Spring#AttachToNodes() the overridden method}.
     */
    public void AttachToNodes()
    {
        Debug.Print(25, "Attaching spring {0}", new Object[] {this});
        this.m_nodeConnection1.Attach(this);
        this.m_nodeConnection2.Attach(this);
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.Spring#DetachFromNodes() the overridden method}.
     */
    public void DetachFromNodes()
    {
        this.m_nodeConnection1.m_node.RemoveSpring(this);
        this.m_nodeConnection2.m_node.RemoveSpring(this);
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.Spring#GetForce(NodeContext) the overridden method}.
     */
    public Vector2D GetForce(NodeContext node)
    {
        if ((null == node) || (!node.equals(this.m_nodeConnection1.m_node) && !node.equals(this.m_nodeConnection2.m_node)))
        {
            
            return new Vector2D();
        }

        boolean isSecondNode = node.equals(this.m_nodeConnection2.m_node);

        if ( (!isSecondNode && !this.m_nodeConnection1.m_applyForce) ||
             (isSecondNode && !this.m_nodeConnection2.m_applyForce))
        {
            return new Vector2D();
        }

        Debug.Print( 25,
                     "Calculating force on node {0} by constraint {1}",
                     new Object[] {node, this.m_constraint});

        return this.InternalGetForce(node);
    }

    /**
     * Return the first node, this spring is connected to.
     *
     * @return the first node, this spring is connected to.
     */
    public NodeContext GetNode1()
    {
        return this.m_nodeConnection1.m_node;
    }

    /**
     * Return the second node, this spring is connected to.
     *
     * @return the second node, this spring is connected to.
     */
    public NodeContext GetNode2()
    {
        return this.m_nodeConnection2.m_node;
    }

    /**
     * Return the "peer" of a given node.
     * The peer is the node at another end of the spring.
     *
     * @param node The node to get the peer of.
     *
     * @return the "peer" of a given node.
     */
    public NodeContext GetPeerNode(NodeContext node)
    {
        if (this.m_nodeConnection1.m_node.equals(node))
        {
            return this.m_nodeConnection2.m_node;
        }
        else if (this.m_nodeConnection2.m_node.equals(node))
        {
            return this.m_nodeConnection1.m_node;
        }
        else
        {
            
            return null;
        }
    }

    /**
     * An internal routine for force calculation. To be overridden by derived classes.
     *
     * @param node the node
     *
     * @return the force
     */
    protected abstract Vector2D InternalGetForce(NodeContext node);

    protected SpringConnectionPoint m_nodeConnection1;
    protected SpringConnectionPoint m_nodeConnection2;
}
