package extensionTools.opcatLayoutManager.Constraints;

import java.util.*;
import extensionTools.opcatLayoutManager.Line;
import extensionTools.opcatLayoutManager.Springs.Spring;

/**
 * Defines a common interface and provides basic implementation for various constraint classes.
 */
public abstract class Constraint
{
    /**
     * Initializes an instance of the class.
     *
     * @param direction   Specifies the direction of the constraint, if aplicable.
     *                    The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#directionHorizontal Constraints.direction* constants}.
     *
     * @param isSyntactic Specifies whether the constraint is syntactic or not (semantic).
     *
     * @param comparator  A comparator used to sort nodes. May be null.
     */
    protected Constraint(int direction, boolean isSyntactic, Comparator comparator)
    {
        this.m_direction   = direction;
        this.m_isSyntactic = isSyntactic;
        this.m_comparator  = comparator;
    }

    /**
     * Checks whether the constraint is syntactic or not (semantic).
     *
     * @return true if the constraint is syntactic, false otherwise (semantic)
     */
    public boolean IsSyntactic()
    {
        return this.m_isSyntactic;
    }

    /**
     * Creates contstraint specific springs and attaches them to the nodes.
     * Must be implemented in the derives classes.
     *
     * @return a list of created springs
     */
    public abstract LinkedList Attach();

    /**
     * Detaches the springs from the nodes.
     */
    public void Detach()
    {
        this.Hide();

        if (null == this.m_springs)
        {
            return;
        }

        for (ListIterator iterSprings = this.m_springs.listIterator(); iterSprings.hasNext(); /* inside loop */)
        {
            Spring spring = (Spring)iterSprings.next();
            spring.DetachFromNodes();
        }

        this.m_springs = null;
    }

    /**
     * Returns the constraint specific spring constant.
     * Can be overridden by derived classes to provide constraint specific
     * spring constant values. The values can be static or dynamic.
     *
     * @return The constraint specific spring constant.
     */
    public double GetSpringConstant(Spring spring)
    {
        return this.m_isSyntactic ? 1.2 : 0.4;
    }

    /**
     * Highlights the nodes, affected by this constraint.
     */
    public abstract void Show();

    /**
     * Removes highlighting from the nodes, affected by this constraint.
     */
    public abstract void Hide();

    /**
     * Performs constraint specific adjustment.
     * Can be overridden by derived classes to provide constraint specific behaviuor.
     */
    public void Adjust()
    {
    }

    /**
     * Returns the string representation of a given direction.
     *
     * @param direction The direction. The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#directionHorizontal Constraints.direction* constants}.
     *
     * @return the string representation of a given direction.
     */
    public static String GetDirectionString(int direction)
    {
        switch (direction)
        {
            case Constraint.directionHorizontal:
                return "Horizontal";
            case Constraint.directionVertical:
                return "Vertical";
            case Constraint.directionOther:
                return "";
            default:
                return "";
        }
    }

    /**
     * Returns a direction orthogonal to a given direction.
     *
     * @param direction Specifies a direction to get the orthogonal direction of.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#directionHorizontal Constraints.direction* constants}.
     *
     * @return the direction orthogonal to the given direction
     */
    protected static int FlipDirection(int direction)
    {
        switch (direction)
        {
            case directionHorizontal:
                return directionVertical;
            case directionVertical:
                return directionHorizontal;
            case directionOther:
                return directionOther;
            default:
                return directionOther;
        }
    }

    /**
     * Returns an axis aligned line.
     *
     * @param direction Specifies the direction of the line.
     *                  The valid values for this parameter are
     *                  {@link extensionTools.opcatLayoutManager.Constraints#directionHorizontal Constraints.directionHorizontal and Constraint.directionVertical}.
     *
     * @param coordinate Specifies the line coordinate.
     *
     * @return an axis alilgned line
     */
    protected static Line GetAxisAlignedLine(int direction, int coordinate)
    {
        switch(direction)
        {
            case directionHorizontal:
                return new Line(0, coordinate, 100, coordinate);

            case directionVertical:
                return new Line(coordinate, 0, coordinate, 100);

            default:
                return null;
        }
    }

    protected LinkedList m_springs;
    protected int        m_direction;
    protected Comparator m_comparator;

    private boolean m_isSyntactic;

    /**
     * Direction values.
     */
    public static final int directionHorizontal = 0;
    public static final int directionVertical   = 1;
    public static final int directionOther      = 2;

    /**
     * Range description values.
     */
    public static final int rangeStartToStart   = 0;
    public static final int rangeEndToEnd       = 1;
    public static final int rangeCenterToCenter = 2;
    public static final int rangeEndToStart     = 3;
    public static final int rangeStartToEnd     = 4;

    /**
     * Origin description values.
     */
    public static final int originStart  = 0;
    public static final int originCenter = 1;
    public static final int originEnd    = 2;
}
