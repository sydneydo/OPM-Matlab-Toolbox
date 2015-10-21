package extensionTools.opcatLayoutManager;

import java.util.*;
import exportedAPI.opcatAPIx.*;
import extensionTools.opcatLayoutManager.Constraints.*;

/**
 * Represents the constraint based force directed system for automatic diagrams layout.
 * For more information on the model refer to:
 *     K. Ryall, L. Marks, S. Shieber.
 *     An Interactive Constraint-Based System for Drawing Graphs.
 *     Proceedings of User Interface Software and Technology, pp. 97-104, 1997
 */
public class ConstraintBasedForceDirectedLayoutSystem
{
    /**
     * Initializes a new instance of the class.
     */
    public ConstraintBasedForceDirectedLayoutSystem()
    {
    }

    /**
     * Adds a diagram node to the system.
     *
     * @param node Specifies the node to be added.
     */
    public void addNode(IXNode node)
    {
        if (this.m_nodes.containsKey(node))
        {
            return;
        }

        //
        // Create a new node context and add it to the model.
        //
        NodeContext nodeContext = new NodeContext(node);
        Debug.Print(10, "Adding node context for {0}.", new Object[] {nodeContext});
        this.m_nodes.put(node, nodeContext);
    }

    /**
     * Adds a diagram edge to the system.
     * If the edge node are not in the system yet, they are added automatically.
     *
     * @param edge Specifies the edge to be added.
     */
    public void addEdge(IXLine edge)
    {
        if (this.m_edges.containsKey(edge))
        {
            return;
        }

        NodeContext sourceNode      = this.getNodeContext(edge.getSourceIXNode());
        NodeContext destinationNode = this.getNodeContext(edge.getDestinationIXNode());

        //
        // Create a new edge context and add it to the model.
        //
        EdgeContext edgeContext = new EdgeContext(edge, sourceNode, destinationNode);
        Debug.Print(10, "Adding edge context for {0}.", new Object[] {edgeContext});
        this.m_edges.put(edge, edgeContext);
    }

    /**
     * Adds a constraint to the system.
     * Related nodes and edges are added automatically.
     *
     * @param constraint Specifies the constraint to be added.
     */
    public void addConstraint(Constraint constraint)
    {
        this.m_constraints.add(constraint);
        Debug.Print( 10,
                     "Adding {0} constraint {1}.",
                     new Object[] {constraint.IsSyntactic() ? "syntactic" : "semantic", constraint});

    }

    /**
     * Removes a constraint from the system.
     *
     * @param constraint Specifies the constraint to be removed.
     */
    public void removeConstraint(Constraint constraint)
    {
        constraint.Detach();
        Debug.Print(10, "Removing constraint {0}.", new Object[] {constraint});
        this.m_constraints.remove(constraint);
    }

    /**
     * Removes all constraints from the system.
     */
    public void removeAllConstraints()
    {
        Debug.Print(10, "Removing all constraints.");
        this.m_constraints.clear();
    }

    /**
     * Anchors a node for all future arrangment operations.
     *
     * @param node Specifies the node to anchor.
     */
    public void anchorNode(IXNode node)
    {
        NodeContext nodeContext = this.getNodeContext(node);
        Debug.Print(10, "Setting anchor for node {0}.", new Object[] {nodeContext});
        nodeContext.SetAnchor(true);
    }

    /**
     * Removes a previously added anchor.
     *
     * @param node Specifies the node to remove an anchor from.
     */
    public void removeAnchor(IXNode node)
    {
        NodeContext nodeContext = this.getNodeContext(node);
        Debug.Print(10, "Removing anchor for node {0}.", new Object[] {nodeContext});
        nodeContext.SetAnchor(false);
    }

    /**
     * Removes all previously added anchors.
     */
    public void removeAllAnchors()
    {
        Debug.Print(10, "Removing all anchors.");

        for (Enumeration enumNodes = this.m_nodes.elements(); enumNodes.hasMoreElements(); /* inside loop */)
        {
            NodeContext node = (NodeContext)enumNodes.nextElement();
            node.SetAnchor(false);
        }
    }

    /**
     * Adds syntactic constraints: node-node and node-edge overlapping and zone.
     *
     * @return Collection of constraints.
     */
    public Iterator addSyntacticConstraints()
    {
        Debug.Print(10, "Adding syntactic constraints.");

        int        outerNodesIndex          = 0;
        Hashtable  zoneConstraintNodesLists = new Hashtable();
        LinkedList newConstraints           = new LinkedList();

        for (Enumeration enumOuterNodes = this.m_nodes.elements(); enumOuterNodes.hasMoreElements(); /* inside loop */)
        {
            NodeContext outerNode = (NodeContext)enumOuterNodes.nextElement();
            ++outerNodesIndex;

            //
            // Add node-node overlapping constraints.
            // Prepare lists for addition of zone constraints.
            //

            Enumeration enumInnerNodes = this.m_nodes.elements();

            //
            // Skip inner nodes.
            //
            for (int i = 0; i < outerNodesIndex; ++i)
            {
                enumInnerNodes.nextElement();
            }

            while (enumInnerNodes.hasMoreElements())
            {
                NodeContext innerNode = (NodeContext)enumInnerNodes.nextElement();

                if (outerNode.IsParentOf(innerNode))
                {
                    this.addNodeToConstraintList(zoneConstraintNodesLists, outerNode, innerNode);
                }
                else if (innerNode.IsParentOf(outerNode))
                {
                    this.addNodeToConstraintList(zoneConstraintNodesLists, innerNode, outerNode);
                }
                else
                {
                    Constraint  constraint = new NodeNodeOverlapConstraint(outerNode, innerNode);
                    this.addConstraint(constraint);
                    newConstraints.add(constraint);
                }
            }

            //
            // Add node-edge overlapping constraints.
            //
            for (Enumeration enumEdges = this.m_edges.elements(); enumEdges.hasMoreElements(); /* inside loop */)
            {
                EdgeContext edge      = (EdgeContext)enumEdges.nextElement();
                NodeContext edgeNode1 = edge.GetNode1();
                NodeContext edgeNode2 = edge.GetNode2();

                if ( outerNode.equals(edgeNode1)     || // A node can intersect with its own edge
                     outerNode.equals(edgeNode1)     || // A node can intersect with its own edge
                     outerNode.IsParentOf(edgeNode1) || // A parent node can intersect with its child's edge
                     outerNode.IsParentOf(edgeNode2) || // A parent node can intersect with its child's edge
                     edgeNode1.IsParentOf(outerNode) || // Parent's constraint works for child nodes too
                     edgeNode2.IsParentOf(outerNode))   // Parent's constraint works for child nodes too
                {
                    continue;
                }

                Constraint  constraint = new NodeEdgeOverlapConstraint(outerNode, edge);
                this.addConstraint(constraint);
                newConstraints.add(constraint);
            }
        }

        //
        // Create zone constraints.
        //
        for (Enumeration enumZones = zoneConstraintNodesLists.keys(); enumZones.hasMoreElements(); /* inside loop */)
        {
            NodeContext zone       = (NodeContext)enumZones.nextElement();
            LinkedList  list       = (LinkedList)zoneConstraintNodesLists.get(zone);
            Constraint  constraint = new ZoneConstraint(zone, list.iterator());

            this.addConstraint(constraint);
            newConstraints.add(constraint);
        }

        return newConstraints.iterator();
    }

    /**
     * Clears the system, removing all nodes, edges and constraints.
     */
    public void clear()
    {
        Debug.Print(10, "Clearing the system.");
        this.m_nodes.clear();
        this.m_edges.clear();
        this.m_constraints.clear();
    }

    /**
     * Prepares the layout system for an arrangment operation.
     * An arrangment operation can be performed in one or two phases.
     * In two-phase mode in phase 1 the system semantic constraints are ignored.
     * In phase 2 all constraints are taken into account.
     * Using two-phase mode helps reducing local optima effect.
     *
     * @param arrangeInTwoPhases Specifies whether an arrangment should be done
     *                           in two phases.
     */
    public void initArrange(boolean arrangeInTwoPhases)
    {
        Debug.Print(10, "Initializing arrangement operations.");

        //
        // Restore all edges to auto-arrangment.
        //
        for (Enumeration enumEdges = this.m_edges.elements(); enumEdges.hasMoreElements(); /* inside loop */)
        {
            EdgeContext edge = (EdgeContext)enumEdges.nextElement();
            edge.RestoreAutoArrange();
        }

        //
        // (Re)Create springs.
        //
        this.updateSprings();

        this.m_currentIteration = 0;

        //
        // Reset nodes.
        //
        for (Enumeration enumNodes = this.m_nodes.elements(); enumNodes.hasMoreElements(); /* inside loop */)
        {
            NodeContext node = (NodeContext)enumNodes.nextElement();
            node.ResetPositionCalculation();
        }

        //
        // Decide whether to execute or skip phase 1.
        //
        this.m_currentPhase = arrangeInTwoPhases ? 1 : 2;

        Debug.Print( 10,
                     "The system is ready for arrangment operation with {0} nodes, {1} edges and {2} constraints",
                     new Object[] { new Integer(this.m_nodes.size()),
                                    new Integer(this.m_edges.size()),
                                    new Integer(this.m_constraints.size())});

    }

    /**
     * Arranges all known nodes and edges according to active constraints.
     * The operation must be initialized by a call to {@link #initArrange(boolean) initArrange()} method.
     *
     * @param maximumIterations Specifies the maximum number of iterations.
     *
     * @return true if the arrangment succeeded (i.e. the system reached a low energy state)
     *         false if the arrangment failed (i.e. the system exceeded the maximum number of iterations
     *         but didn't reach a low energy state)
     */
    public boolean arrange(int maximumIterations)
    {
        Debug.Print(10, "Starting iterative arrangement operation.");

        boolean isInLowEnergyState = false;

        int startPhase = this.m_currentPhase;

        //
        // Execute the selected phases.
        //
        while (this.m_currentPhase <= 2)
        {
            isInLowEnergyState = false;

            for (;;)
            {
                ++this.m_currentIteration;

                Debug.Print( 10,
                             "***** Starting iteration {0} *****.",
                             new Object[] {new Integer(this.m_currentIteration)});

                if (this.step())
                {
                    //
                    // We've reached a low energy state.
                    //
                    isInLowEnergyState = true;
                    break;
                }

                if (this.m_currentIteration > maximumIterations)
                {
                    ++this.m_currentPhase;

                    Debug.Print( 20,
                                 "***** Phase raised to {0} after {1} iterations. *****",
                                 new Object[] {new Integer(this.m_currentPhase), new Integer(this.m_currentIteration)});
                    break;
                }
            }
        }

        //
        // Restore phase to enable subsequent arrange operations.
        //
        this.m_currentPhase = startPhase;

        return isInLowEnergyState;
    }

    /**
     * Performs a single step in arrangment operation.
     * The operation must be initialized by a call to {@link #initArrange(boolean) initArrange()} method.
     * After performing a single step, the operation can be continued by a call to this method
     * or {@link #arrange(int) arrange()} method.
     *
     * @return true if the arrangment succeeded (i.e. the system reached a low energy state)
     *         false if the arrangment failed (i.e. the system exceeded the maximum number of iterations
     *         but didn't reach a low energy state)
     */
    public boolean step()
    {
        Debug.Print(10, "Starting arrangment step.");

        //
        // At phase 1 we ignore syntactic constraints trying to avoid local optima.
        // At phase 2 we take them into account doing the final arrangment.
        //
        boolean ignoreSyntacticConstraints = (1 == this.m_currentPhase);

        if (this.adjustPosition(ignoreSyntacticConstraints) || this.isLowEnergyState())
        {
            //
            // After reaching a low energy state we raise the phase.
            //
            ++this.m_currentPhase;

            return true;
        }

        return false;
    }



    /**
     * Creates a node-node overlapping constraint.
     *
     * @param node1 The first node.
     * @param node2 The second node.
     *
     * @return The created constraint.
     */
    public Constraint createNodeNodeOverlappingConstraint(IXNode node1, IXNode node2)
    {
        return new NodeNodeOverlapConstraint(this.getNodeContext(node1), this.getNodeContext(node2));
    }

    /**
     * Creates a node-edge overlapping constraint.
     *
     * @param node The node.
     * @param edge The edge.
     *
     * @return The created constraint.
     */
    public Constraint createNodeEdgeOverlappingConstraint(IXNode node, IXLine edge)
    {
        return new NodeEdgeOverlapConstraint(this.getNodeContext(node), this.getEdgeContext(edge));
    }

    /**
     * Creates a spacing constraint.
     *
     * @param direction    Specifies the direction of the nodes distribution.
     *                     The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     *
     * @param minimumSpace Specifies the minimum space between nodes.
     *
     * @param nodes        The nodes to apply the constraint to.
     *
     * @return The created constraint.
     */
    public Constraint createSpacingConstraint(int direction, int minimumSpace, Enumeration nodes)
    {
        EqualSpacingConstraint constraint = new EqualSpacingConstraint( direction,
                                                                        this.getNodesList(nodes).iterator(),
                                                                        minimumSpace,
                                                                        false);
        return constraint;
    }

    /**
     * Creates an alignment constraint.
     *
     * @param direction       Specifies the direction of the nodes alignment.
     *                        The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     *
     * @param origin           Specifies how to align nodes.
     *                         The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.MultiNodeConstraint#originStart MultiNodeConstraint.origin* constants}.
     *
     * @param mainNode        The node to align to.
     *
     * @param nodes           The nodes to apply the constraint to.
     *
     * @return The created constraint.
     */
    public Constraint createAlignmentConstraint(int direction, int origin, IXNode mainNode, Enumeration nodes)
    {
        AlignmentConstraint constraint = new AlignmentConstraint( direction,
                                                                  origin,
                                                                  this.getNodeContext(mainNode),
                                                                  this.getNodesList(nodes).iterator());
        return constraint;
    }

    /**
     * Creates an order constraint.
     *
     * @param direction Specifies the direction of the order.
     *                  The valid values for this parameter are {@link extensionTools.opcatLayoutManager.Constraints.Constraint#directionHorizontal Constraint.directionHorizontal and Constraint.directionVertical constants}.
     *
     * @param node1     The first node.
     * @param node2     The second node.
     *
     * @return The created constraint.
     */
    public Constraint createOrderConstraint(int direction, IXNode node1, IXNode node2)
    {
        return new SequenceConstraint(this.getNodeContext(node1), this.getNodeContext(node2), direction, false);
    }

    /**
     * Creates a zone constraint.
     *
     * @param containerNode Specifies the node that its interior should be used as a limit
     *                      for positions of other nodes.
     *
     * @param nodes         The nodes to limit their positions.
     *
     * @return The created constraint.
     */
    public Constraint createZoneConstraint(IXNode containerNode, Enumeration nodes)
    {
        ZoneConstraint constraint = new ZoneConstraint( this.getNodeContext(containerNode),
                                                        this.getNodesList(nodes).iterator());
        return constraint;
    }

    /**
     * Creates a tree (T-shape) constraint.
     *
     * @param rootNode     The root node of the T-shape.
     *
     * @param relationNode The node relation node.
     *
     * @param childNodes The childNodes nodes.
     *
     * @return The created constraint.
     */
    public Constraint createTreeConstraint(IXNode rootNode, IXNode relationNode, Enumeration childNodes)
    {
        TShapeConstraint constraint = new TShapeConstraint( this.getNodeContext(rootNode),
                                                            this.getNodeContext(relationNode),
                                                            this.getNodesList(childNodes).iterator());
        return constraint;
    }

    /**
     * Finds a node context in the system. If not exists, a new context is created
     * and added to the system.
     *
     * @param the node
     *
     * @returm the node context
     */
    private NodeContext getNodeContext(IXNode node)
    {
        if (null == node)
        {
            return null;
        }

        if (!this.m_nodes.containsKey(node))
        {
            this.addNode(node);
        }
        return (NodeContext)this.m_nodes.get(node);
    }

    /**
     * Finds an edge context in the system. If not exists, a new context is created
     * and added to the system.
     *
     * @param the node
     *
     * @returm the edge context
     */
    private EdgeContext getEdgeContext(IXLine edge)
    {
        if (null == edge)
        {
            return null;
        }

        if (!this.m_edges.containsKey(edge))
        {
            this.addEdge(edge);
        }
        return (EdgeContext)this.m_edges.get(edge);
    }

    /**
     * Returns a list of node context objects for a given enumeration.
     *
     * @param nodes The enumeration of nodes.
     *
     * @return a list of node context objects for a given enumeration.
     */
    private LinkedList getNodesList(Enumeration nodes)
    {
        LinkedList list = new LinkedList();

        while (nodes.hasMoreElements())
        {
            Object element = nodes.nextElement();
            if (element instanceof IXNode)
            {
              IXNode node = (IXNode)element;
              list.add(this.getNodeContext(node));
            }
        }

        return list;
    }

    /**
     * Adds a specified node to the list of nodes that should be added to a constraint
     * with a given main node.
     *
     * @param constraintList A collection of lists, keyed by a main node.
     *
     * @param mainNode       A key to the collecion.
     *
     * @param node           A node to add.
     */
    private void addNodeToConstraintList(Hashtable constraintList, NodeContext mainNode, NodeContext node)
    {
        LinkedList list;
        if (constraintList.containsKey(mainNode))
        {
            list = (LinkedList)constraintList.get(mainNode);
        }
        else
        {
            list = new LinkedList();
            constraintList.put(mainNode, list);
        }

        list.add(node);
    }


    /**
     * Updates springs according to existing constraints.
     */
    private void updateSprings()
    {
        Debug.Print(20, "Updating springs.");

        //
        // Remove all existing springs.
        //
        for (Enumeration enumNodes = this.m_nodes.elements(); enumNodes.hasMoreElements(); /* inside loop */)
        {
            NodeContext node = (NodeContext)enumNodes.nextElement();
            node.RemoveAllSprings();
        }

        //
        // Create new springs.
        //
        for (ListIterator iterConstraints = this.m_constraints.listIterator(); iterConstraints.hasNext(); /* inside loop */)
        {
            Constraint constraint = (Constraint)iterConstraints.next();
            constraint.Detach();
            constraint.Attach();
        }
    }

    /**
     * Adjusts a position of nodes, optionally ignoring syntactic constraints.
     *
     * @param ignoreSyntacticConstraints Specifies whether syntactic constraints should be ignored.
     *
     * @return true if this operation brought the system to a low energy state, false otherwise
     */
    private boolean adjustPosition(boolean ignoreSyntacticConstraints)
    {
        //
        // Adjust constraints.
        //
        for (ListIterator iterConstraints = this.m_constraints.listIterator(); iterConstraints.hasNext(); /* inside loop */)
        {
            Constraint constraint = (Constraint)iterConstraints.next();
            constraint.Adjust();
        }

        //
        // Calculate the damping constant: gamma = 2 * SQRT (max(K)).
        //

        double maxSpringConstant = 0;
        for (Enumeration enumNodes = this.m_nodes.elements(); enumNodes.hasMoreElements(); /* inside loop */)
        {
            NodeContext node = (NodeContext)enumNodes.nextElement();
            double springConstant = node.GetMaxSpringConstant(ignoreSyntacticConstraints);
            if (Math.abs(springConstant) > Math.abs(maxSpringConstant))
            {
                maxSpringConstant = springConstant;
            }
        }

        if (0 == maxSpringConstant)
        {
            //
            // This may happen for example if only syntactic constraints
            // are defined and the diagram is already properly aligned.
            // Practically this means that no spring in the system can produce
            // forces and therefore there is no reason to continue the cycle.
            //
            Debug.Print(10, "***** No active springs in the system. *****");
            return true;
        }

        double dampingConstant = 2 * Math.sqrt(Math.abs(maxSpringConstant));
        Debug.Print( 15,
                     "The damping constant is {0} (for spring constant {1}).",
                     new Object[] { new Double(dampingConstant),
                                    new Double(maxSpringConstant)});

        //
        // Adjust nodes positions in two passes:
        //    * calculate new positions for all nodes
        //    * apply the calculation to all nodes
        // We cannot do this in one pass because all calculations should be
        // done based on a static state.
        //

        for (Enumeration enumNodes = this.m_nodes.elements(); enumNodes.hasMoreElements(); /* inside loop */)
        {
            NodeContext node = (NodeContext)enumNodes.nextElement();
            node.CalculateNewPosition(dampingConstant, this.m_timeInterval, ignoreSyntacticConstraints);
        }

        for (Enumeration enumNodes = this.m_nodes.elements(); enumNodes.hasMoreElements(); /* inside loop */)
        {
            NodeContext node = (NodeContext)enumNodes.nextElement();
            node.SetNewPosition();
        }

        return false;
    }

    /**
     * Checks whether the system has already reached a low energy state.
     *
     * @return true if the has already reached a low energy state, false otherwise
     */
    private boolean isLowEnergyState()
    {
        //
        // Compare the the system energy E = SUM(P^2 / 2) against the allowed energy.
        // To improve performance we calculate E * 2.
        //

        double energy        = 0;
        double allowedEnergy = this.m_nodes.size() * 0.01 * 2;

        for (Enumeration enumNodes = this.m_nodes.elements(); enumNodes.hasMoreElements(); /* inside loop */)
        {
            NodeContext node = (NodeContext)enumNodes.nextElement();
            energy += node.GetMomentum().GetModuleSquare();
if (!Debug.IsDebug())
{
            if (energy > allowedEnergy)
            {
                return false;
            }
}
        }

if (Debug.IsDebug())
{
        Debug.Print( 10,
                     "***** The system energy is {0}, the allowed energy is {1} *****.",
                     new Object[] {new Double(energy), new Double(allowedEnergy)});

        if (energy > allowedEnergy)
        {
            return false;
        }
}

        return true;
    }

    private LinkedList        m_constraints          = new LinkedList();
    private Hashtable         m_nodes                = new Hashtable();
    private Hashtable         m_edges                = new Hashtable();
    private int               m_currentIteration     = 0;
    private double            m_timeInterval         = 0.2;
    private int               m_currentPhase         = 1;
};
