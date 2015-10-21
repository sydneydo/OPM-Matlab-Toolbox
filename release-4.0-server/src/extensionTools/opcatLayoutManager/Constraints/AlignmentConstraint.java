package extensionTools.opcatLayoutManager.Constraints;

import java.text.MessageFormat;
import java.util.*;
import extensionTools.opcatLayoutManager.*;
import extensionTools.opcatLayoutManager.Springs.NodeOrthogonalToLineSpring;
import extensionTools.opcatLayoutManager.Springs.Spring;

/**
 * Provides means for horizontal or vertical alignment of nodes.
 */
public class AlignmentConstraint extends MultiNodeConstraint
{
    /**
     * Initializes an instance of the class.
     *
     * @param direction Specifies the direction of the alignment.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     *
     * @param origin    Specifies how to align nodes.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.MultiNodeConstraint#originStart MultiNodeConstraint.origin* constants}.
     *
     * @param mainNode  Specifies the node to align to.
     *                  If node specified, nodes are aligned to the center line.
     *
     * @param           The nodes to add to the constraint.
     */
    public AlignmentConstraint(int direction, int origin, NodeContext mainNode, Iterator nodes)
    {
        super( FlipDirection(direction), // The direction of the spring is orthogonal to the direction of alignment.
               mainNode,
               null,                     // We must add node after calling SetOrigin()
               null);                    // We don't care about the ordering of the nodes

        this.SetOrigin(origin);
        this.AddNodes(nodes);
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        String originString;
        switch(this.m_origin)
        {
            case Constraint.originStart:
                originString = Constraint.directionHorizontal == this.m_direction ? "left" : "top";
                break;

            case Constraint.originCenter:
                originString = "center";
                break;

            case Constraint.originEnd:
                originString = Constraint.directionHorizontal == this.m_direction ? "right" : "bottom";
                break;

            default:
                originString = "???";
        }

        return MessageFormat.format( "[{0} alignment to {1} of nodes {2} to {3}]",
                                     new Object[] { GetDirectionString(FlipDirection(this.m_direction)),
                                                    originString,
                                                    this.GetNodesString(),
                                                    null != this.m_mainNodeConnection ? this.m_mainNodeConnection.m_node.toString() : "the center line"});

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

            Line line = this.GetAllignmentLine();

            Debug.Print(30, "The alignment line is {0}", new Object[] {line});

            for (ListIterator iterNodes = this.m_nodes.listIterator(); iterNodes.hasNext(); /* inside loop */)
            {
                SpringConnectionPoint nodeConnection = ((SpringConnectionPoint)iterNodes.next());
                Spring spring = new NodeOrthogonalToLineSpring(this, 0, nodeConnection, line);
                spring.AttachToNodes();
                this.m_springs.add(spring);
            }
        }

        return this.m_springs;
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Adjust() the overridden method}.
     * Updates the the alignment line as the nodes move around.
     */
    public void Adjust()
    {
        if (null == this.m_springs)
        {
            return;
        }

        Line line = this.GetAllignmentLine();

        Debug.Print(25, "Adjusting the alignment line of constraint {0} to {1}", new Object[] {this, line});

        for (ListIterator iterSprings = this.m_springs.listIterator(); iterSprings.hasNext(); /* inside loop */)
        {
            NodeOrthogonalToLineSpring spring = (NodeOrthogonalToLineSpring)iterSprings.next();
            spring.SetLine(line);
        }

    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.MultiNodeConstraint#AddNode(NodeContext) the overridden method}.
     */
    public void AddNode(NodeContext node)
    {
        SpringConnectionPoint nodeConnection;
        switch(this.m_direction)
        {
            case Constraint.directionHorizontal:
                nodeConnection = new SpringConnectionPoint( node,
                                                            true,
                                                            (int)(node.GetWidth() * this.m_offsetFactor),
                                                            0);
                break;

            case Constraint.directionVertical:
                nodeConnection = new SpringConnectionPoint( node,
                                                            true,
                                                            0,
                                                            (int)(node.GetHeight() * this.m_offsetFactor));
                break;

            case Constraint.directionOther:
            default:
                nodeConnection = new SpringConnectionPoint(node, true);
        }

        this.AddNode(nodeConnection);
    }

    /**
     * Returns the line, the nodes should be aligned to.
     *
     * @return the line, the nodes should be aligned to.
     */
    private Line GetAllignmentLine()
    {
        double coordinate;
        if (null != this.m_mainNodeConnection)
        {
            //
            // Align to the main node.
            //

            NodeContext node = this.m_mainNodeConnection.m_node;
            coordinate = Constraint.directionHorizontal == this.m_direction    ?
                         node.GetCenterX() + node.GetWidth() * this.m_offsetFactor   :
                         node.GetCenterY() + node.GetHeight() * this.m_offsetFactor;

        }
        else
        {
            //
            // Align to the center line.
            //
            MinMax minMax = this.GetMinMaxPositions(this.m_direction, this.m_minMaxOrigin);
            coordinate = (minMax.min + minMax.max) / 2;
        }

        //
        // The alignment line is orthogonal to springs.
        //
        return GetAxisAlignedLine(FlipDirection(this.m_direction), (int)coordinate);
    }

    private void SetOrigin(int origin)
    {
        this.m_origin = origin;

        switch(this.m_origin)
        {
            case Constraint.originStart:
                this.m_offsetFactor =  -0.5;
                this.m_minMaxOrigin = Constraint.rangeStartToStart;
                break;

            case Constraint.originCenter:
                this.m_offsetFactor = 0;
                this.m_minMaxOrigin = Constraint.rangeCenterToCenter;
                break;

            case Constraint.originEnd:
                this.m_offsetFactor = 0.5;
                this.m_minMaxOrigin = Constraint.rangeEndToEnd;
                break;

            default:
        }
    }

    private int    m_origin;
    private double m_offsetFactor;
    private int    m_minMaxOrigin;
}
