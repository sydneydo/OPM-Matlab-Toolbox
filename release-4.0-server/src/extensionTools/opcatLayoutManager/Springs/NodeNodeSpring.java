package extensionTools.opcatLayoutManager.Springs;

import java.text.MessageFormat;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Constraints.Constraint;


/**
 * Represents a spring connecting two nodes.
 */
public class NodeNodeSpring extends TwoNodeSpring
{
    /**
     * Initializes an instance of the class.
     *
     * @param constraint       Specifies the constraint, that creates this spring.
     * @param springRestLength Specifies the rest length of this spring.
     * @param nodeConnection1  Specifies the first node connection point for this spring.
     * @param nodeConnection2  Specifies the second node connection point for this spring.
     */
    public NodeNodeSpring( Constraint            constraint,
                           double                springRestLength,
                           SpringConnectionPoint nodeConnection1,
                           SpringConnectionPoint nodeConnection2)
    {
        super(constraint, springRestLength, nodeConnection1, nodeConnection2);
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
                                     new Object[] {this.m_nodeConnection1.m_node, this.m_nodeConnection2.m_node, this.m_constraint});
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.TwoNodeSpring#InternalGetForce(NodeContext) the overridden method}.
     */
    protected Vector2D InternalGetForce(NodeContext node)
    {
        Vector2D intermidiateResult = new Vector2D( this.m_nodeConnection1.m_node.GetCenterX() + this.m_nodeConnection1.m_xOffset,
                                                    this.m_nodeConnection1.m_node.GetCenterY() + this.m_nodeConnection1.m_yOffset,
                                                    this.m_nodeConnection2.m_node.GetCenterX() + this.m_nodeConnection2.m_xOffset,
                                                    this.m_nodeConnection2.m_node.GetCenterY() + this.m_nodeConnection2.m_yOffset);

        double springActualLength = intermidiateResult.GetModule();

        intermidiateResult.Multiply((springActualLength - this.m_springRestLength) / springActualLength);
        intermidiateResult.Multiply(this.GetSpringConstant());

        if (node.equals(this.m_nodeConnection2.m_node))
        {
            intermidiateResult.Multiply(-1);
        }

        return intermidiateResult;
    }
}
