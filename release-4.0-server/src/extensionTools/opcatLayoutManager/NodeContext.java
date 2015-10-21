package extensionTools.opcatLayoutManager;

import java.awt.*;
import java.awt.geom.*;
import java.text.MessageFormat;
import java.util.*;
import exportedAPI.opcatAPIx.*;
import extensionTools.opcatLayoutManager.Constraints.Constraint;
import extensionTools.opcatLayoutManager.Springs.*;

/**
 * Encapsulates OPCAT node classes with some additional layout information.
 */
public class NodeContext
{
    /**
     * Initializes an instance of the class.
     *
     * @param node An instance of an OPCAT node class.
     */
    public NodeContext(IXNode node)
    {
        this.m_node = node;

        this.m_color = this.m_node instanceof IXInstance              ?
                  ((IXInstance)this.m_node).getBackgroundColor() :
                  Color.BLACK;

        this.ResetPositionCalculation();
    }

    /**
     * Returns the name of the OPCAT node instance.
     *
     * @return the name of the OPCAT node instance.
     */
    public String GetName()
    {
        if (this.m_node instanceof IXInstance)
        {
          return ((IXInstance)this.m_node).getIXEntry().getName();
        }
        else
        {
            return "?";
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
        return MessageFormat.format("[{0}]", new Object[] {this.GetName().replace('\n', ' ')});
    }

    /**
     * Returns the x coordinate of the top left corner of the node bounding rectangle.
     *
     * @return the x coordinate of the top left corner of the node bounding rectangle.
     */
    public double GetX()
    {
        return this.m_X;
    }

    /**
     * Returns the y coordinate of the top left corner of the node bounding rectangle.
     *
     * @return the y coordinate of the top left corner of the node bounding rectangle.
     */
    public double GetY()
    {
        return this.m_Y;
    }

    /**
     * Returns the x coordinate of the center of the node bounding rectangle.
     *
     * @return the x coordinate of the center of the node bounding rectangle.
     */
    public double GetCenterX()
    {
        return this.m_X + this.m_node.getWidth() / 2;
    }

    /**
     * Returns the y coordinate of the center of the node bounding rectangle.
     *
     * @return the y coordinate of the center of the node bounding rectangle.
     */
    public double GetCenterY()
    {
        return this.m_Y + this.m_node.getHeight() / 2;
    }

    /**
     * Returns the width of the node bounding rectangle.
     *
     * @return the width of the node bounding rectangle.
     */
    public double GetWidth()
    {
        return this.m_node.getWidth();
    }

    /**
     * Returns the height of the node bounding rectangle.
     *
     * @return the height of the node bounding rectangle.
     */
    public double GetHeight()
    {
        return this.m_node.getHeight();
    }

    /**
     * Returns the node bounding rectangle.
     *
     * @return the node bounding rectangle.
     */
    public Rectangle GetRectangle()
    {
        return new Rectangle((int)this.m_X, (int)this.m_Y, this.m_node.getWidth(), this.m_node.getHeight());
    }

    /**
     * Returns the legth of maximum diagonal of the node bounding rectangle.
     *
     * @return the legth of maximum diagonal of the node bounding rectangle.
     */
    public double GetRadius()
    {
        double diagonal = Math.sqrt( Util.Square(this.m_node.getWidth()) +
                                     Util.Square(this.m_node.getHeight()));
        return diagonal / 2;
    }

    /**
     * Returns the size of the node bounding rectangle in a given direction.
     *
     * @param direction Specifies in which direction the size should be measured.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     *
     * @return the size of the node bounding rectangle in a given direction.
     */
    public double GetSize(int direction)
    {
        switch(direction)
        {
            case Constraint.directionHorizontal:
                return this.m_node.getWidth();

            case Constraint.directionVertical:
                return this.m_node.getHeight();

            case Constraint.directionOther:
            default:
                
                return this.GetRadius();
        }
    }

    /**
     * Returns the start coordinate of the node bounding rectangle in a given direction.
     *
     * @param direction Specifies the start coordinate in which direction should be returned.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     *
     * @return the start coordinate of the node bounding rectangle in a given direction.
     */
    public double GetStart(int direction)
    {
        switch(direction)
        {
            case Constraint.directionHorizontal:
                return this.GetX();

            case Constraint.directionVertical:
                return this.GetY();

            case Constraint.directionOther:
            default:
                
                return 0;
        }
    }

    /**
     * Returns the center coordinate of the node bounding rectangle in a given direction.
     *
     * @param direction Specifies the center coordinate in which direction should be returned.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     *
     * @return the center coordinate of the node bounding rectangle in a given direction.
     */
    public double GetCenter(int direction)
    {
        switch(direction)
        {
            case Constraint.directionHorizontal:
                return this.GetCenterX();

            case Constraint.directionVertical:
                return this.GetCenterY();

            case Constraint.directionOther:
            default:
                
                return 0;
        }
    }

    /**
     * Returns the end coordinate of the node bounding rectangle in a given direction.
     *
     * @param direction Specifies the end coordinate in which direction should be returned.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     *
     * @return the end coordinate of the node bounding rectangle in a given direction.
     */
    public double GetEnd(int direction)
    {
        switch(direction)
        {
            case Constraint.directionHorizontal:
                return this.GetX() + this.GetWidth();

            case Constraint.directionVertical:
                return this.GetY() + this.GetHeight();

            case Constraint.directionOther:
            default:
                
                return 0;
        }
    }

    /**
     * Sets whether the node should be anchored.
     * Anchored nodes cannot be moved.
     *
     * @param isAnchored Specifies whether the node should be anchored.
     */
    public void SetAnchor(boolean isAnchored)
    {
        this.m_isAnchored = isAnchored;
    }

    /**
     * Resets the position calculation.
     */
    public void ResetPositionCalculation()
    {
        this.m_momentum.x = 0;
        this.m_momentum.y = 0;

        this.m_X = this.m_node.getX();
        this.m_Y = this.m_node.getY();

        IXNode parentInstance = this.m_node.getParentIXNode();
        if (null != parentInstance)
        {
            this.m_X += parentInstance.getX();
            this.m_Y += parentInstance.getY();
        }
    }

    /**
     * Removes all the springs, attached to the node.
     */
    public void RemoveAllSprings()
    {
        this.m_springs.clear();
    }

    /**
     * Adds a spring to the node.
     *
     * @param spring The spring to add.
     */
    public void AddSpring(Spring spring)
    {
        this.m_springs.add(spring);
    }

    /**
     * Removes a spring from the node.
     *
     * @param spring The spring to remove.
     */
    public void RemoveSpring(Spring spring)
    {
        this.m_springs.remove(spring);
    }

    /**
     * Returns the spring constant of the strongest (at this time) spring attached to this node.
     *
     * @param ignoreSyntacticSprings Specifies whether springs generated by syntactic constraints
     *                               should be ignored.
     *
     * @return the spring constant of the strongest (at this time) spring attached to this node.
     */
    public double GetMaxSpringConstant(boolean ignoreSyntacticSprings)
    {
        double maxSpringConstant = 0;
        for (ListIterator iterSprings = this.m_springs.listIterator(); iterSprings.hasNext(); /* inside loop */)
        {
            Spring spring = (Spring)iterSprings.next();

            if (ignoreSyntacticSprings && spring.GetConstraint().IsSyntactic())
            {
                continue;
            }

            double springConstant = spring.GetSpringConstant();

            if (Math.abs(springConstant) > Math.abs(maxSpringConstant))
            {
                maxSpringConstant = springConstant;
            }
        }
        return maxSpringConstant;
    }

    /**
     * Returns the momentum applied to this node.
     *
     * @return the momentum applied to this node.
     */
    public Vector2D GetMomentum()
    {
        return this.m_momentum;
    }

    /**
     * Calculates the new position.
     * The new position is not automatically applied to the node.
     * This should be done explicitly by calling {@link #SetNewPosition() SetNewPosition} method.
     *
     * @param springDampingConstant The damping spring constant in the system.
     * @param timeInterval Specifies the time interval for calculations.
     * @param ignoreSyntacticConstraint Specifies whether syntactic constraints should be ignored.
     */
    public void CalculateNewPosition(double springDampingConstant, double timeInterval, boolean ignoreSyntacticConstraints)
    {
        Debug.Print(10, "Calculating new position for node {0}.", new Object[] {this});

        //
        // Calculate the resultant force.
        //
        Vector2D force = new Vector2D();
        for (ListIterator iterSprings = this.m_springs.listIterator(); iterSprings.hasNext(); /* inside loop */)
        {
            Spring spring = (Spring)iterSprings.next();

            if (ignoreSyntacticConstraints && spring.GetConstraint().IsSyntactic())
            {
                continue;
            }

            Vector2D springForce = spring.GetForce(this);

            if ((0 != springForce.x) || (0 != springForce.y))
            {
                Debug.Print(20, "\tspring {0} yields force {1}", new Object[] {spring, springForce});
                force.Add(springForce);
            }
        }
        Debug.Print(15, "\tthe force is {0}", new Object[] {force});

        //
        // Calculate the current momentum: P(t) = P(t - dt) + (F - gamma * P(t - dt)) * dt.
        // gamma - the spring damping constant.
        //
        Vector2D intermidiateResult = new Vector2D(this.m_momentum);
        intermidiateResult.Multiply(springDampingConstant);
        intermidiateResult.Multiply(-1);
        intermidiateResult.Add(force);
        intermidiateResult.Multiply(timeInterval);
        this.m_momentum.Add(intermidiateResult);
        Debug.Print(15, "\tthe momentum is {0}", new Object[] {this.m_momentum});

        this.m_newX = this.m_X;
        this.m_newY = this.m_Y;

        if (this.m_isAnchored)
        {
            Debug.Print(15, "\tthe node is anchored, remains in the same position");
        }
        else
        {
            if ((this.m_momentum.x != 0) || (this.m_momentum.y != 0))
            {
                Debug.Print(15, "\told coordinates are ({0}, {1})", new Object[] {new Double(this.m_X), new Double(this.m_Y)});

                //
                // Calculate new coordiantes: X(t) = X(t - dt) + P(t)dt.
                //
                this.m_newX += (this.m_momentum.x * timeInterval);
                this.m_newY += (this.m_momentum.y * timeInterval);

                Debug.Print(15, "\tnew coordinates are ({0}, {1})", new Object[] {new Double(this.m_newX), new Double(this.m_newY)});
            }
        }
    }

    /**
     * Sets the previously calculated new positioin.
     */
    public void SetNewPosition()
    {
        this.m_X = this.m_newX;
        this.m_Y = this.m_newY;

        double x = this.m_X;
        double y = this.m_Y;

        IXNode parentInstance = this.m_node.getParentIXNode();
        if (null != parentInstance)
        {
            x -= parentInstance.getX();
            y -= parentInstance.getY();
        }

        this.m_node.setLocation((int)x, (int)y);
    }

    /**
     * Returns a distance between this node and a given node.
     *
     * @param node The node to measure the distance from.
     *
     * @return a distance between this node and a given node.
     */
    public double GetDistance(NodeContext node)
    {
        return Math.sqrt( Util.Square((node.GetCenterX() - this.GetCenterX())) +
                          Util.Square((node.GetCenterY() - this.GetCenterY())));
    }

    /**
     * Checks whether this node and a given node overlap.
     *
     * @param node The node to check overlapping with.
     *
     * @return true if this node a given node overlap, false otherwise.
     */
    public boolean Overlaps(NodeContext node)
    {
        boolean overlap = this.GetRectangle().intersects(node.GetRectangle());

        Debug.Print( 25,
                     "Checking overlapping of nodes {0} and {1}: {2}.",
                     new Object[] { this,
                                    node,
                                    overlap ? "overlap" : "don't overlap"});
        return overlap;
    }

    /**
     * Checks whether this node and a given edge overlap.
     *
     * @param edge The edge to check overlapping with.
     *
     * @return true if this node a given edge overlap, false otherwise.
     */
    public boolean Overlaps(EdgeContext edge)
    {
        boolean overlap = this.GetRectangle().intersectsLine(edge.GetLine());

        Debug.Print( 25,
                     "Checking overlapping of node {0} and edge {1}: {2}.",
                     new Object[] { this,
                                    edge,
                                    overlap ? "overlap" : "don't overlap"});
        return overlap;
    }

    /**
     * Checks whether this node fully contains a given node.
     *
     * @param node The node to check containment of.
     *
     * @return true if this node fully contains a given node, false otherwise.
     */
    public boolean Contains(NodeContext node)
    {
        RectangularShape shape;
        if (this.m_node instanceof IXProcessInstance)
        {
            shape = new Ellipse2D.Double(this.m_X, this.m_Y, this.m_node.getWidth(), this.m_node.getHeight());
        }
        else
        {
            shape = this.GetRectangle();
        }

        boolean isContained = shape.contains(node.GetRectangle());

        Debug.Print( 25,
                     "Checking containment of node {0} in {1}: {2}.",
                     new Object[] { node,
                                    this,
                                    isContained ? "contained" : "not contained"});

        return isContained;
    }

    /**
     * Checks whether this node is a parent of a given node.
     *
     * @param node The node to check.
     *
     * @return true if this node is a parent of a given node, false otherwise.
     */
    public boolean IsParentOf(NodeContext node)
    {
        return (this.m_node == node.m_node.getParentIXNode());
    }

    /**
     * Turns on/off highlightinh of this node.
     *
     * @param isSelected Specifies whether the node should be highlighted.
     */
    public void Select(boolean isSelected)
    {
        if (Debug.IsDebug() && (this.m_node instanceof IXInstance))
        {
            ((IXInstance)this.m_node).setBackgroundColor( isSelected                                      ?
                                                     (this.m_color != Color.red ? Color.red : Color.blue) :
                                                     this.m_color);
        }
    }

    private IXNode     m_node;
    private Vector2D   m_momentum = new Vector2D();
    private LinkedList m_springs  = new LinkedList();
    private double     m_X;
    private double     m_Y;
    private double     m_newX;
    private double     m_newY;
    private boolean    m_isAnchored = false;
    private Color      m_color;
}