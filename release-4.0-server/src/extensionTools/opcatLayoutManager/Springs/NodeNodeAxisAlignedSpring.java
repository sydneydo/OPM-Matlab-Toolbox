package extensionTools.opcatLayoutManager.Springs;

import java.text.MessageFormat;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Constraints.Constraint;

/**
 * Represents a spring connecting two nodes and applying force
 * in either horizontal or vertical direction.
 *
 * In order to support reordering of nodes, we allow this type of spring
 * to be "compressed beyond zero length", as shown in the figure below.
 * In such a case the spring actual length is considered negative.
 * <p>
 * <pre>
 * +----+        +----+        +----+    +----+        +----+    +----+
 * | N1 +--------+ N2 |        | N1 +----+ N2 |        | N2 +----+ N1 |
 * +----+        +----+        +----+    +----+        +----+    +----+
 *
 *   The spring is in            The spring is     The spring is compressed
 *     a rest state               compressed           beyond zero length
 * </pre>
 */
public class NodeNodeAxisAlignedSpring extends TwoNodeSpring
{
    /**
     * Initializes an instance of the class.
     *
     * @param constraint       Specifies the constraint, that creates this spring.
     * @param springRestLength Specifies the rest length of this spring.
     * @param nodeConnection1  Specifies the first node connection point for this spring.
     * @param nodeConnection2  Specifies the second node connection point for this spring.
     * @param direction        Specifies the direction of the force.
     *                         The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     */
    public NodeNodeAxisAlignedSpring( Constraint            constraint,
                                      double                springRestLength,
                                      SpringConnectionPoint nodeConnection1,
                                      SpringConnectionPoint nodeConnection2,
                                      int                   direction)
    {
        super(constraint, springRestLength, nodeConnection1, nodeConnection2);
        this.m_direction = direction;
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return MessageFormat.format( "[{0} spring between nodes {1} and {2} by constraint {3}]",
                                     new Object[] { Constraint.GetDirectionString(this.m_direction),
                                                    this.m_nodeConnection1.m_node,
                                                    this.m_nodeConnection2.m_node,
                                                    this.m_constraint});
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.TwoNodeSpring#InternalGetForce(NodeContext) the overridden method}.
     */
    protected Vector2D InternalGetForce(NodeContext node)
    {
        Vector2D intermidiateResult;
        boolean  compressedBeyondZeroLength;

        switch (this.m_direction)
        {
            case Constraint.directionHorizontal:
                intermidiateResult = new Vector2D( this.m_nodeConnection1.GetConnectionX(),
                                                   0,
                                                   this.m_nodeConnection2.GetConnectionX(),
                                                   0);
                compressedBeyondZeroLength = intermidiateResult.x <= 0;
                break;

            case Constraint.directionVertical:
                intermidiateResult = new Vector2D( 0,
                                                   this.m_nodeConnection1.GetConnectionY(),
                                                   0,
                                                   this.m_nodeConnection2.GetConnectionY());
                compressedBeyondZeroLength = intermidiateResult.y <= 0;
                break;

            case Constraint.directionOther:
            default:
                
                intermidiateResult = new Vector2D();
                compressedBeyondZeroLength = false;

        }

        double springActualLength = intermidiateResult.GetModule();

        if (compressedBeyondZeroLength)
        {
            springActualLength *= -1;
        }

        intermidiateResult.Multiply((springActualLength - this.m_springRestLength) / springActualLength);
        intermidiateResult.Multiply(this.GetSpringConstant());

        if (node.equals(this.m_nodeConnection2.m_node))
        {
            intermidiateResult.Multiply(-1);
        }

        return intermidiateResult;
    }

    private int m_direction;
}
