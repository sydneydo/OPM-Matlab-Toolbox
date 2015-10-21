package extensionTools.opcatLayoutManager.Springs;

import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Constraints.Constraint;

/**
 * Defines a common interface and provides basic implementation for various spring classes.
 */
public abstract class Spring
{
    /**
     * Initializes an instance of the class.
     *
     * @param constraint       Specifies the constraint, that creates this spring.
     * @param springRestLength Specifies the rest length of this spring.
     */
    protected Spring(Constraint constraint, double springRestLength)
    {
        this.m_constraint       = constraint;
        this.m_springRestLength = springRestLength;
    }

    /**
     * Attaches this spring to the nodes.
     */
    public abstract void AttachToNodes();

    /**
     * Detaches this spring from the nodes.
     */
    public abstract void DetachFromNodes();

    /**
     * Returns the constraint that created this spring.
     *
     * @return the constraint that created this spring.
     */
    public Constraint GetConstraint()
    {
        return this.m_constraint;
    }

    /**
     * Returns the elastic constant of this spring.
     *
     * @return the elastic constant of this spring.
     */
    public double GetSpringConstant()
    {
        return this.m_constraint.GetSpringConstant(this);
    }

    /**
     * Returns the rest length of this spring.
     *
     * @return the rest length of this spring.
     */
    public double GetSpringRestLength()
    {
        return this.m_springRestLength;
    }

    /**
     * Calculates a force, applied to a given node.
     *
     * @param node the node
     *
     * @return the force
     */
    public abstract Vector2D GetForce(NodeContext node);

    protected Constraint m_constraint;
    protected double     m_springRestLength;
}
