package extensionTools.opcatLayoutManager.Springs;

import java.text.MessageFormat;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Constraints.Constraint;

/**
 * Represents a spring connecting a node and a line (normal to the line).
 */
public class NodeOrthogonalToLineSpring extends SingleNodeSpring
{
    /**
     * Initializes an instance of the class.
     *
     * @param constraint       Specifies the constraint, that creates this spring.
     * @param springRestLength Specifies the rest length of this spring.
     * @param nodeConnection   Specifies the node connection point for this spring.
     * @param line             Specifies the line for this spring.
     */
    public NodeOrthogonalToLineSpring( Constraint            constraint,
                                       double                springRestLength,
                                       SpringConnectionPoint nodeConnection,
                                       Line                  line)
    {
        super(constraint, springRestLength, nodeConnection);
        this.m_line = line;
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
                                     new Object[] {this.m_nodeConnection.m_node, this.m_line, this.m_constraint});
    }

    /**
     * Sets the line.
     *
     * @param the line to set
     */
    public void SetLine(Line line)
    {
        this.m_line = line;
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Springs.SingleNodeSpring#InternalGetForce(NodeContext) the overridden method}.
     */
    protected Vector2D InternalGetForce(NodeContext node)
    {
        //
        // Get the the node coordinates for shorthand reference.
        //
        double nodeX = this.m_nodeConnection.m_node.GetCenterX() + this.m_nodeConnection.m_xOffset;
        double nodeY = this.m_nodeConnection.m_node.GetCenterY() + this.m_nodeConnection.m_yOffset;

        //
        // Calculate the spring actual length.
        //
        double springActualLength = this.m_line.ptLineDist(nodeX, nodeY);
        if (!Util.IsValidDouble(springActualLength))
        {
            return new Vector2D();
        }

        //
        // Calculate the force module: F = dL * K.
        //
        double forceModule = Math.abs((springActualLength - this.m_springRestLength) * this.GetSpringConstant());
        if (!Util.IsValidDouble(forceModule))
        {
            return new Vector2D();
        }

        //
        // Get the line angle.
        //
        double alpha = Math.atan((this.m_line.getY2() - this.m_line.getY1()) / (this.m_line.getX2() - this.m_line.getX1()));
        if (!Util.IsValidDouble(springActualLength))
        {
            return new Vector2D();
        }

        Debug.Print( 25,
                     "springActualLength={0}, forceModule={1}, alpha={2}",
                     new Object[] {new Double(springActualLength), new Double(forceModule), new Double(alpha)});

        //
        // Get the line and the node relative position.
        //
        int relativePosition = this.m_line.relativeCCW(nodeX, nodeY);
        Debug.Print( 25,
                     "Relative position for line ({0},{1})-({2},{3}) and point ({4},{5}) is {6}.",
                     new Object[] {new Double(this.m_line.x1),
                                   new Double(this.m_line.y1),
                                   new Double(this.m_line.x2),
                                   new Double(this.m_line.y2),
                                   new Double(nodeX),
                                   new Double(nodeY),
                                   new Integer(relativePosition)});
        if (0 == relativePosition)
        {
            //
            // Unlikely with floating point calulcation but happens.
            // The point is exactly on the line.
            //
            return new Vector2D();
        }

        //
        // "Roughly" calculate the force vector, based on its module and angle.
        // After this calculation the coordinates have correct absolute values
        // but their signs should be additionally adjusted.
        //
        Vector2D intermidiateResult = new Vector2D( forceModule * Math.abs(Math.sin(alpha)),
                                                    forceModule * Math.abs(Math.cos(alpha)));

        //
        // Fix up the signs, according to the relative line and node positions.
        // In this calculation we assume the force is "push". If the force
        // is actually "pull" we'll just revert its direction.
        //
        // There are four distinct cases here.
        //

        //
        // Case #1
        //   +----------------------------------------> x
        //   |    (x1, y1)
        //   |      * -  -   -  -  -  -  -  -
        //   |          *   ) alpha
        //   |              *            (x, y)
        //   |                 *         *
        //   |                     *
        //   |                         *
        //   |                             *
        //   |                              (x2, y2)
        // y v
        //
        //   alpha > 0
        //   relativePosition = 1
        //
        //   Expected vector: Fx > 0, Fy < 0.
        //
        if ((alpha >= 0) && (relativePosition == 1))
        {
            intermidiateResult.y *= -1;
        }

        //
        // Case #2
        //   +----------------------------------------> x
        //   |    (x1, y1)
        //   |      * -  -   -  -  -  -  -  -
        //   |          *   ) alpha
        //   |              *
        //   |                 *
        //   |                     *
        //   |            *            *
        //   |           (x, y)            *
        //   |                              (x2, y2)
        // y v
        //
        //   alpha > 0
        //   relativePosition = -1
        //
        //   Expected vector: Fx < 0, Fy > 0.
        //
        if ((alpha >= 0) && (relativePosition == -1))
        {
            intermidiateResult.x *= -1;
        }

        //
        // Case #3
        //   +----------------------------------------> x
        //   |
        //   |                             (x2, y2)
        //   |                             *
        //   |        *                *
        //   |     (x, y)           *
        //   |                  *
        //   |              *
        //   |          *  ) alpha
        //   |      * -  -  -  -  -  -  -  -  -
        // y v      (x1, y1)
        //
        //   alpha < 0
        //   relativePosition = 1
        //
        //   Expected vector: Fx < 0, Fy < 0.
        //
        if ((alpha < 0) && (relativePosition == 1))
        {
            intermidiateResult.x *= -1;
            intermidiateResult.y *= -1;
        }

        //
        // Case #4
        //   +----------------------------------------> x
        //   |
        //   |                             (x2, y2)
        //   |                             *
        //   |                         *
        //   |                     *
        //   |                  *
        //   |              *            *
        //   |          *  ) alpha       (x, y)
        //   |      * -  -  -  -  -  -  -  -  -  -
        // y v      (x1, y1)
        //
        //   alpha < 0
        //   relativePosition = -1
        //
        //   Expected vector: Fx > 0, Fy > 0.
        //
        if ((alpha < 0) && (relativePosition == -1))
        {
            // do nothing
        }

        if (springActualLength > this.m_springRestLength)
        {
            //
            // The force is actually "pull". Revert its direction.
            //
            intermidiateResult.Multiply(-1);
        }

        return intermidiateResult;
    }

    private Line m_line;
}
