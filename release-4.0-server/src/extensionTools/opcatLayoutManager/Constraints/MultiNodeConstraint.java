package extensionTools.opcatLayoutManager.Constraints;

import java.util.*;
import extensionTools.opcatLayoutManager.*;

/**
 * Defines a common interface and provides basic implementation for various
 * multi-node constraint classes.
 */
public abstract class MultiNodeConstraint extends Constraint
{
    /**
     * A helper class for holding min-max ranges.
     */
    protected class MinMax
    {
        public MinMax(double _min, double _max)
        {
            this.min = _min;
            this.max = _max;
        }

        public double min;
        public double max;
    }

    /**
     * Initializes an instance of the class.
     *
     * @param direction  Specifies the direction of the constraint, if aplicable.
     *                   The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints#directionHorizontal Constraints.direction* constants}.
     *
     * @param nodes      NodeContext or SpringConnectionPoint object to add to the constraint.
     *
     * @param comparator A comparator used to sort nodes. May be null.
     */
    protected MultiNodeConstraint(int direction, NodeContext mainNode, Iterator nodes, Comparator comparator)
    {
        super(direction, false, comparator);

        if (null != mainNode)
        {
            this.m_mainNodeConnection = new SpringConnectionPoint(mainNode, false);
        }

        this.AddNodes(nodes);
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Show() the overridden method}.
     */
    public void Show()
    {
        if (null != this.m_mainNodeConnection)
        {
            this.m_mainNodeConnection.m_node.Select(true);
        }

        for (ListIterator iterNodes = this.m_nodes.listIterator(); iterNodes.hasNext(); /* inside loop */)
        {
            NodeContext node = ((SpringConnectionPoint)iterNodes.next()).m_node;
            node.Select(true);
        }
    }

    /**
     * Refer to {@linkplain extensionTools.opcatLayoutManager.Constraints.Constraint#Hide() the overridden method}.
     */
    public void Hide()
    {
        if (null != this.m_mainNodeConnection)
        {
            this.m_mainNodeConnection.m_node.Select(false);
        }

        for (ListIterator iterNodes = this.m_nodes.listIterator(); iterNodes.hasNext(); /* inside loop */)
        {
            NodeContext node = ((SpringConnectionPoint)iterNodes.next()).m_node;
            node.Select(false);
        }
    }

    /**
     * Adds a node to the constraint.
     *
     * @param node the node to add
     */
    protected void AddNode(NodeContext node)
    {
        this.AddNode(new SpringConnectionPoint(node, true));
    }

    /**
     * Adds a node connection to the constraint.
     *
     * @param nodeConnection the node connection to add
     */
    protected void AddNode(SpringConnectionPoint nodeConnection)
    {
        if ((null != this.m_mainNodeConnection) && this.m_mainNodeConnection.m_node.equals(nodeConnection.m_node))
        {
            return;
        }

        Debug.Print(25, "Adding node {0} to constraint {1}", new Object[] {nodeConnection.m_node, this});

        if (null != this.m_comparator)
        {
            ListIterator iterNodes = this.m_nodes.listIterator();
            while(iterNodes.hasNext())
            {
                NodeContext currentNode = ((SpringConnectionPoint)iterNodes.next()).m_node;

                if (this.m_comparator.compare(nodeConnection.m_node, currentNode) <= 0)
                {
                    iterNodes.previous();
                    break;
                }
            }

            iterNodes.add(nodeConnection);
        }
        else
        {
            this.m_nodes.add(nodeConnection);
        }
    }

    /**
     * Adds nodes to the constraint.
     *
     * @param The nodes to add.
     */
    protected void AddNodes(Iterator nodes)
    {
        if (null != nodes)
        {
            while (nodes.hasNext())
            {
                Object element = nodes.next();

                if (element instanceof NodeContext)
                {
                    this.AddNode((NodeContext)element);
                }
                else if (element instanceof SpringConnectionPoint)
                {
                    this.AddNode((SpringConnectionPoint)element);
                }
            }
        }
    }

    /**
     * Gets the spring connection point for a given node.
     *
     * @param node The node.
     *
     * @return the spring connection point for a given node.
     */
    protected SpringConnectionPoint GetNodeConnection(NodeContext node)
    {
        for (ListIterator iterNodes = this.m_nodes.listIterator(); iterNodes.hasNext(); /* inside loop */)
        {
            SpringConnectionPoint nodeConnection = (SpringConnectionPoint)iterNodes.next();
            if (nodeConnection.m_node.equals(node))
            {
                return nodeConnection;
            }
        }

        return null;
    }

    /**
     * Removes the spring connection point for a given node.
     *
     * @param node The node.
     */
    protected void RemoveNodeConnection(NodeContext node)
    {
        SpringConnectionPoint nodeConnection = this.GetNodeConnection(node);
        if (null != nodeConnection)
        {
            this.m_nodes.remove(nodeConnection);
        }
    }

    /**
     * Gets a string representation of the constraint nodes.
     *
     * @return a string representation of the constraint nodes.
     */
    protected String GetNodesString()
    {
        StringBuffer nodesString      = new StringBuffer();
        boolean      isFirstIteration = true;

        for (ListIterator iterNodes = this.m_nodes.listIterator(); iterNodes.hasNext(); /* inside loop */)
        {
            if (isFirstIteration)
            {
                isFirstIteration = false;
            }
            else
            {
                nodesString.append(", ");
            }

            SpringConnectionPoint nodeConnection = (SpringConnectionPoint)iterNodes.next();
            nodesString.append(nodeConnection.m_node.toString());
            if (!nodeConnection.m_applyForce)
            {
                nodesString.append(" (no force)");
            }
        }

        return nodesString.toString();
    }

    /**
     * Returns a range spanned by the constraint nodes in a given direction.
     *
     * @param direction Specifies the direction of the range.
     *
     * @param range Specifies how the range should be calculated.
     *        For {@link #rangeStartToStart rangeStartToStart}   the range is (a, d)
     *        For {@link #rangeStartToStart rangeEndToEnd}       the range is (c, f)
     *        For {@link #rangeStartToStart rangeCenterToCenter} the range is (b, e)
     *        For {@link #rangeStartToStart rangeEndToStart}     the range is (c, d) (not implemented)
     *        For {@link #rangeStartToStart rangeStartToEnd}     the range is (a, f)
     *        +-----+     +-----+             +-----+    +-----+
     *        |  1  |     |  2  |   .......   | n-1 |    |  n  |
     *        +--+--+     +--+--+             +--+--+    +--+--+
     *        a  b  c                                    d  e  f
     *
     * @return a range spanned by the constraint nodes in a given direction.
     */
    protected MinMax GetMinMaxPositions(int direction, int range)
    {
        double minStart  = Double.MAX_VALUE;
        double maxStart  = 0;
        double minEnd    = Double.MAX_VALUE;
        double maxEnd    = 0;
        double minCenter = Double.MAX_VALUE;
        double maxCenter = 0;

        for (ListIterator iterNodes = this.m_nodes.listIterator(); iterNodes.hasNext(); /* inside loop */)
        {
            NodeContext node = ((SpringConnectionPoint)iterNodes.next()).m_node;

            switch(direction)
            {
                case directionHorizontal:
                    minStart  = Math.min(minStart, node.GetX());
                    maxStart  = Math.max(maxStart, node.GetX());
                    minEnd    = Math.min(minEnd, node.GetX() + node.GetWidth());
                    maxEnd    = Math.max(maxEnd, node.GetX() + node.GetWidth());
                    minCenter = Math.min(minCenter, node.GetCenterX());
                    maxCenter = Math.max(maxCenter, node.GetCenterX());
                    break;

                case directionVertical:
                    minStart  = Math.min(minStart, node.GetY());
                    maxStart  = Math.max(maxStart, node.GetY());
                    minEnd    = Math.min(minEnd, node.GetY() + node.GetHeight());
                    maxEnd    = Math.max(maxEnd, node.GetY() + node.GetHeight());
                    minCenter = Math.min(minCenter, node.GetCenterY());
                    maxCenter = Math.max(maxCenter, node.GetCenterY());
                    break;

                case directionOther:
                default:
                    return new MinMax(0, 0);
            }
        }

        switch (range)
        {
            case rangeStartToStart:
                return new MinMax(minStart, maxStart);

            case rangeEndToEnd:
                return new MinMax(minEnd, maxEnd);

            case rangeCenterToCenter:
                return new MinMax(minCenter, maxCenter);

            case rangeStartToEnd:
                return new MinMax(minStart, maxEnd);

            case rangeEndToStart:
            default:
                return new MinMax(0, 0);
        }
    }

    protected LinkedList            m_nodes              = new LinkedList();
    protected SpringConnectionPoint m_mainNodeConnection = null;
}
