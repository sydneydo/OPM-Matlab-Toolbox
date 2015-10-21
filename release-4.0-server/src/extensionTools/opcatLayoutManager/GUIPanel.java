package extensionTools.opcatLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import exportedAPI.opcatAPIx.*;
import extensionTools.opcatLayoutManager.Constraints.*;

/**
 * This class implements UI for layout manager extension tool.
 * This UI is used primarily for debugging purposes.
 */
public class GUIPanel extends JPanel
{
    /**
	 * 
	 */
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Creates an instance of the class.
     */
    public GUIPanel(IXSystem system)
    {
        try
        {
            this.jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        this.m_system = system;

        this.LoadModel();
        this.FillTreeFirstLevel();
    }

    /**
     * Initializes UI controls.
     */
    private void jbInit() throws Exception
    {
        this.m_btnArrange.setBounds(new Rectangle(648, 77, 66, 23));
        this.m_btnArrange.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnArrange.setText("Arrange");
        this.m_btnArrange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnArrange_actionPerformed(e);
            }
        });
        this.m_btnStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnStep_actionPerformed(e);
            }
        });
        this.m_btnStep.setBounds(new Rectangle(648, 105, 66, 23));
        this.m_btnStep.setToolTipText("");
        this.m_btnStep.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnStep.setText("Step");
        this.setLayout(null);
        this.m_treeConstraints.setBounds(new Rectangle(60, 39, 274, 140));
        this.m_treeConstraints.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                GUIPanel.this.m_treeConstraints_valueChanged(e);
            }
        });
        this.m_btnAlignHorStart.setBounds(new Rectangle(359, 21, 85, 23));
        this.m_btnAlignHorStart.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnAlignHorStart.setText("Align to Top");
        this.m_btnAlignHorStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnAlignHorStart_actionPerformed(e);
            }
        });
        this.m_btnAlignVerStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnAlignVerStart_actionPerformed(e);
            }
        });
        this.m_btnAlignVerStart.setText("Align to Left");
        this.m_btnAlignVerStart.setBounds(new Rectangle(452, 21, 85, 23));
        this.m_btnAlignVerStart.setToolTipText("");
        this.m_btnAlignVerStart.setActionCommand("Align Vertically");
        this.m_btnAlignVerStart.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnSpaceHorizontally.setActionCommand("Space Equally  Horizontally");
        this.m_btnSpaceHorizontally.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnSpaceHorizontally.setText("Even Space");
        this.m_btnSpaceHorizontally.setToolTipText("");
        this.m_btnSpaceHorizontally.setBounds(new Rectangle(359, 104, 85, 23));
        this.m_btnSpaceHorizontally.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnSpaceHorizontally_actionPerformed(e);
            }
        });
        this.m_btnSpaceVertically.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnSpaceVertically_actionPerformed(e);
            }
        });
        this.m_btnSpaceVertically.setBounds(new Rectangle(452, 104, 85, 23));
        this.m_btnSpaceVertically.setToolTipText("");
        this.m_btnSpaceVertically.setText("Even Space");
        this.m_btnSpaceVertically.setActionCommand("Space Equally  Vertically");
        this.m_btnSpaceVertically.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnRemoveConstraint.setActionCommand("Remove Constraint");
        this.m_btnRemoveConstraint.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnRemoveConstraint.setText("Remove Constraint");
        this.m_btnRemoveConstraint.setToolTipText("");
        this.m_btnRemoveConstraint.setBounds(new Rectangle(359, 160, 178, 23));
        this.m_btnRemoveConstraint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnRemoveConstraint_actionPerformed(e);
            }
        });
        this.m_btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnReload_actionPerformed(e);
            }
        });
        this.m_btnReload.setText("Reload");
        this.m_btnReload.setBounds(new Rectangle(648, 21, 66, 23));
        this.m_btnReload.setMargin(new Insets(2, 2, 2, 2));
        this.m_chkArrangeInTwoPhases.setBounds(new Rectangle(648, 134, 66, 23));
        this.m_chkArrangeInTwoPhases.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                GUIPanel.this.m_chkArrangeInTwoPhases_itemStateChanged(e);
            }
        });
        this.m_chkArrangeInTwoPhases.setText("2 Phase");
        this.m_chkArrangeInTwoPhases.setSelected(true);
        this.m_chkArrangeInTwoPhases.setToolTipText("");
        this.m_btnOrderHorizontally.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnOrderHorizontally_actionPerformed(e);
            }
        });
        this.m_btnOrderHorizontally.setText("Order");
        this.m_btnOrderHorizontally.setBounds(new Rectangle(359, 131, 85, 23));
        this.m_btnOrderHorizontally.setActionCommand("Order Horizontally");
        this.m_btnOrderHorizontally.setMargin(new Insets(2, 2, 2, 2));
        this.m_lblHorozontal.setFont(new java.awt.Font("Dialog", 1, 12));
        this.m_lblHorozontal.setText("Horizontal");
        this.m_lblHorozontal.setBounds(new Rectangle(359, 0, 85, 20));
        this.m_lblVertical.setBounds(new Rectangle(452, 1, 85, 20));
        this.m_lblVertical.setFont(new java.awt.Font("Dialog", 1, 12));
        this.m_lblVertical.setOpaque(true);
        this.m_lblVertical.setText("Vertical");
        this.m_btnOrderVertically.setActionCommand("Order Vertically");
        this.m_btnOrderVertically.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnOrderVertically.setToolTipText("");
        this.m_btnOrderVertically.setBounds(new Rectangle(452, 131, 85, 23));
        this.m_btnOrderVertically.setText("Order");
        this.m_btnOrderVertically.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnOrderVertically_actionPerformed(e);
            }
        });
        this.m_btnAlignHorCenter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnAlignHorCenter_actionPerformed(e);
            }
        });
        this.m_btnAlignHorCenter.setText("Align to Center");
        this.m_btnAlignHorCenter.setBounds(new Rectangle(359, 49, 85, 23));
        this.m_btnAlignHorCenter.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnAlignHorEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnAlignHorEnd_actionPerformed(e);
            }
        });
        this.m_btnAlignHorEnd.setText("Align to Bottom");
        this.m_btnAlignHorEnd.setBounds(new Rectangle(359, 77, 85, 23));
        this.m_btnAlignHorEnd.setMaximumSize(new Dimension(83, 25));
        this.m_btnAlignHorEnd.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnAlignVerCenter.setActionCommand("Align Vertically");
        this.m_btnAlignVerCenter.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnAlignVerCenter.setToolTipText("");
        this.m_btnAlignVerCenter.setBounds(new Rectangle(452, 49, 85, 23));
        this.m_btnAlignVerCenter.setText("Align to Center");
        this.m_btnAlignVerCenter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnAlignVerCenter_actionPerformed(e);
            }
        });
        this.m_btnAlignVerEnd.setActionCommand("Align Vertically");
        this.m_btnAlignVerEnd.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnAlignVerEnd.setToolTipText("");
        this.m_btnAlignVerEnd.setBounds(new Rectangle(452, 77, 85, 23));
        this.m_btnAlignVerEnd.setText("Align to Right");
        this.m_btnAlignVerEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnAlignVerEnd_actionPerformed(e);
            }
        });

        this.m_scrollTreeConstraints = new JScrollPane(this.m_treeConstraints);
        this.m_scrollTreeConstraints.setBounds(new Rectangle(-1, 0, 352, 226));

        this.m_chkAlignToFirstSelected.setToolTipText("");
        this.m_chkAlignToFirstSelected.setSelected(true);
        this.m_chkAlignToFirstSelected.setText("Align to first selected");
        this.m_chkAlignToFirstSelected.setBounds(new Rectangle(359, 191, 178, 23));
        this.m_btnZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnZone_actionPerformed(e);
            }
        });
    this.m_btnZone.setText("Zone");
    this.m_btnZone.setMargin(new Insets(2, 2, 2, 2));
    this.m_btnZone.setBounds(new Rectangle(544, 49, 85, 23));
    this.m_btnTree.setBounds(new Rectangle(544, 77, 85, 23));
    this.m_btnTree.setMargin(new Insets(2, 2, 2, 2));
    this.m_btnTree.setSelected(false);
    this.m_btnTree.setText("Tree");
    this.m_btnTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnTree_actionPerformed(e);
            }
        });
    this.m_btnSyntactic.setBounds(new Rectangle(544, 21, 85, 23));
    this.m_btnSyntactic.setMargin(new Insets(2, 2, 2, 2));
    this.m_btnSyntactic.setText("Syntactic");
    this.m_btnSyntactic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnSyntactic_actionPerformed(e);
            }
        });
    this.m_btnAnchor.setBounds(new Rectangle(544, 104, 85, 23));
    this.m_btnAnchor.setMargin(new Insets(2, 2, 2, 2));
    this.m_btnAnchor.setText("Anchor");
    this.m_btnAnchor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnAnchor_actionPerformed(e);
            }
        });
    this.m_btnReload1.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnReload1.setBounds(new Rectangle(0, 0, 66, 23));
        this.m_btnReload1.setText("Reload");
        this.m_btnReload1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnReload1_actionPerformed(e);
            }
        });
        this.m_btnInitArrange.setMargin(new Insets(2, 2, 2, 2));
        this.m_btnInitArrange.setBounds(new Rectangle(648, 49, 66, 23));
        this.m_btnInitArrange.setText("Init");
        this.m_btnInitArrange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIPanel.this.m_btnInitArrange_actionPerformed(e);
            }
        });
        this.add(this.m_btnOrderHorizontally, null);
        this.add(this.m_btnAlignVerStart, null);
        this.add(this.m_btnSpaceVertically, null);
        this.add(this.m_btnOrderVertically, null);
        this.add(this.m_btnAlignHorStart, null);
        this.add(this.m_btnAlignHorCenter, null);
        this.add(this.m_btnAlignHorEnd, null);
        this.add(this.m_btnSpaceHorizontally, null);
        this.add(this.m_btnAlignVerCenter, null);
        this.add(this.m_btnAlignVerEnd, null);
        this.add(this.m_btnRemoveConstraint, null);
        this.add(this.m_scrollTreeConstraints, null);
        this.add(this.m_chkAlignToFirstSelected, null);
        this.add(this.m_lblHorozontal, null);
        this.add(this.m_lblVertical, null);
        this.add(this.m_btnZone, null);
        this.add(this.m_btnTree, null);
        this.add(this.m_btnSyntactic, null);
        this.add(this.m_btnAnchor, null);
        this.add(this.m_btnReload, null);
        this.add(this.m_btnArrange, null);
        this.add(this.m_btnStep, null);
        this.add(this.m_chkArrangeInTwoPhases, null);
        this.add(this.m_btnReload1, null);
        this.add(this.m_btnInitArrange, null);
    }

    /**
     * Loads the model.
     */
    private void LoadModel()
    {
        this.m_systemStructure = this.m_system.getIXSystemStructure();
        this.m_currentOpd      = this.m_system.getCurrentIXOpd();
        this.m_currentOpdId    = this.m_currentOpd.getOpdId();
        this.m_layoutSystem    = new ConstraintBasedForceDirectedLayoutSystem();
    }

    /**
     * Fills the first level of the constraints tree.
     */
    private void FillTreeFirstLevel()
    {
        DefaultMutableTreeNode rootNode  = new DefaultMutableTreeNode("Root");
        DefaultTreeModel       treeModel = new DefaultTreeModel(rootNode);

        //
        // Add constraint categories.
        //
        this.m_tnNodeNodeOverlap = new DefaultMutableTreeNode("Node-node ovelpaping");
        this.m_tnNodeEdgeOverlap = new DefaultMutableTreeNode("Node-edge ovelpaping");
        this.m_tnHorAlign        = new DefaultMutableTreeNode("Horizontal alignment");
        this.m_tnHorEqSpace      = new DefaultMutableTreeNode("Horizontal equal spacing");
        this.m_tnHorOrder        = new DefaultMutableTreeNode("Horizontal order");
        this.m_tnVerAlign        = new DefaultMutableTreeNode("Vertical alignment");
        this.m_tnVerEqSpace      = new DefaultMutableTreeNode("Vertical equal spacing");
        this.m_tnVerOrder        = new DefaultMutableTreeNode("Vertical order");
        this.m_tnZone            = new DefaultMutableTreeNode("Zone");
        this.m_tnTree            = new DefaultMutableTreeNode("Tree");
        this.m_tnAnchor          = new DefaultMutableTreeNode("Anchor");
        treeModel.insertNodeInto(this.m_tnNodeNodeOverlap, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnNodeEdgeOverlap, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnHorAlign, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnHorEqSpace, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnHorOrder, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnVerAlign, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnVerEqSpace, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnVerOrder, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnZone, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnTree, rootNode, 0);
        treeModel.insertNodeInto(this.m_tnAnchor, rootNode, 0);

        this.m_treeConstraints.setModel(treeModel);
        this.m_treeConstraints.setRootVisible(true);
        this.m_treeConstraints.setShowsRootHandles(true);
        this.m_treeConstraints.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
     * Adds all the nodes in the currend OPD.
     */
    private void AddNodesToModel()
    {
        for ( Enumeration enumInstances = this.m_systemStructure.getInstancesInOpd(this.m_currentOpdId);
              enumInstances.hasMoreElements();
              /* inside loop */)
        {
            IXInstance instance = (IXInstance)enumInstances.nextElement();

            if (instance instanceof IXNode)
            {
                this.m_layoutSystem.addNode((IXNode)instance);
            }
        }
    }

    /**
     * Adds all the edges in the cuttent OPD.
     */
    private void AddEdgesToModel()
    {
        for ( Enumeration enumInstances = this.m_systemStructure.getInstancesInOpd(this.m_currentOpdId);
              enumInstances.hasMoreElements();
              /* inside loop */)
        {
            IXInstance instance = (IXInstance)enumInstances.nextElement();

            if (instance instanceof IXLine)
            {
                this.m_layoutSystem.addEdge((IXLine)instance);
            }
        }
    }

    /**
     * Adds an alignment constraint.
     */
    private void AddAlignmentConstraint( int                    direction,
                                         int                    origin,
                                         boolean                useFirstSelectedAsMain,
                                         DefaultMutableTreeNode treeGroup)
    {
        Constraint constraint = this.m_layoutSystem.createAlignmentConstraint( direction,
                                                                          origin,
                                                                          useFirstSelectedAsMain ? this.GetFirstSelectedNode() : null,
                                                                          this.m_currentOpd.getSelectedItems());
        this.m_layoutSystem.addConstraint(constraint);
        this.AddConstraintToTree(constraint, treeGroup);
    }

    /**
     * Adds and equal spacing constraint.
     */
    private void AddEqualSpacingConstraint(int direction, DefaultMutableTreeNode treeGroup)
    {
        Constraint constraint = this.m_layoutSystem.createSpacingConstraint( direction,
                                                                        0,
                                                                        this.m_currentOpd.getSelectedItems());
        this.m_layoutSystem.addConstraint(constraint);
        this.AddConstraintToTree(constraint, treeGroup);
    }

    /**
     * Adds a sequence constraint.
     */
    private void AddSequenceConstraint(int direction, DefaultMutableTreeNode treeGroup)
    {
        IXNode[] nodes = new IXNode[2];

        int index = 0;

        for ( Enumeration enumSelected = this.m_currentOpd.getSelectedItems();
              enumSelected.hasMoreElements();
              /* inside loop */)
        {
            IXInstance instance = (IXInstance)enumSelected.nextElement();

            if (instance instanceof IXNode)
            {
                if (index > 1)
                {
                    //
                    // Too much elements.
                    //
                    return;
                }

                nodes[index++] = (IXNode)instance;
            }
        }

        if (index < 2)
        {
            //
            // Not enough elements.
            //
            return;
        }

        Constraint constraint = this.m_layoutSystem.createOrderConstraint(direction, nodes[0], nodes[1]);
        this.m_layoutSystem.addConstraint(constraint);
        this.AddConstraintToTree(constraint, treeGroup);
    }

    /**
     * Returns a constraint object, corresponding to the current selection
     * in the constraints tree control.
     *
     * @return the constraint object
     */
    private Object GetSelectedConstraint()
    {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)this.m_treeConstraints.getLastSelectedPathComponent();
        return null != treeNode ? treeNode.getUserObject() : null;
    }

    /**
     * Adds a constraint object to the constraints tree control
     */
    private DefaultMutableTreeNode AddConstraintToTree(Constraint constraint, DefaultMutableTreeNode parent)
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(constraint);
        DefaultTreeModel treeModel  = (DefaultTreeModel)this.m_treeConstraints.getModel();
        treeModel.insertNodeInto(node, parent, 0);
        this.m_treeConstraints.scrollPathToVisible(new TreePath(node.getPath()));
        return node;
    }

    /**
     * Returns the node that has been selected first.
     */
    private IXNode GetFirstSelectedNode()
    {
        IXNode node = null;

        //
        // The node that has been selected first appears as the last element
        // of the m_currentOpd.getSelectedItems() enumeration.
        //
        for ( Enumeration enumSelected = this.m_currentOpd.getSelectedItems();
              enumSelected.hasMoreElements();
              /* inside loop */)
        {
            Object element = enumSelected.nextElement();
            if (element instanceof IXNode)
            {
                node = (IXNode) element;
            }
        }

        return node;
    }

    /**
     * Handles click on the "Reload" button.
     */
    private void m_btnReload_actionPerformed(ActionEvent e)
    {
        this.LoadModel();
        this.FillTreeFirstLevel();
    }

    /**
     * Handles click on the "Init" button.
     */
    void m_btnInitArrange_actionPerformed(ActionEvent e)
    {
        this.m_layoutSystem.initArrange(this.m_chkArrangeInTwoPhases.isSelected());
    }

    /**
     * Handles click on the "Arrange" button.
     */
    private void m_btnArrange_actionPerformed(ActionEvent e)
    {
        this.m_layoutSystem.arrange(200);
    }

    /**
     * Hanles click on the "Step" button.
     */
    private void m_btnStep_actionPerformed(ActionEvent e)
    {
        this.m_layoutSystem.step();
    }

    /**
     * Handles changes of "2 phases" checkbox.
     */
    private void m_chkArrangeInTwoPhases_itemStateChanged(ItemEvent e)
    {
        if (null != this.m_layoutSystem)
        {
            this.m_layoutSystem.initArrange(this.m_chkArrangeInTwoPhases.isSelected());
        }
    }

    /**
     * Handles click on the "Align to top" button.
     */
    private void m_btnAlignHorStart_actionPerformed(ActionEvent e)
    {
        this.AddAlignmentConstraint( Constraint.directionHorizontal,
                                Constraint.originStart,
                                this.m_chkAlignToFirstSelected.isSelected(),
                                this.m_tnHorAlign);
    }

    /**
     * Handles click on the "Align to center horizontally" button.
     */
    private void m_btnAlignHorCenter_actionPerformed(ActionEvent e)
    {
        this.AddAlignmentConstraint( Constraint.directionHorizontal,
                                Constraint.originCenter,
                                this.m_chkAlignToFirstSelected.isSelected(),
                                this.m_tnHorAlign);
    }

    /**
     * Handles click on the "Align to bottom" button.
     */
    private void m_btnAlignHorEnd_actionPerformed(ActionEvent e)
    {
        this.AddAlignmentConstraint( Constraint.directionHorizontal,
                                Constraint.originEnd,
                                this.m_chkAlignToFirstSelected.isSelected(),
                                this.m_tnHorAlign);
    }

    /**
     * Handles click on the "Space equal horizontally" button.
     */
    private void m_btnSpaceHorizontally_actionPerformed(ActionEvent e)
    {
        this.AddEqualSpacingConstraint(Constraint.directionHorizontal, this.m_tnHorEqSpace);
    }

    /**
     * Handles click on the "Order horizontally" button.
     */
    void m_btnOrderHorizontally_actionPerformed(ActionEvent e)
    {
        this.AddSequenceConstraint(Constraint.directionHorizontal, this.m_tnHorOrder);
    }

    /**
     * Handles click on the "Align to left" button.
     */
    private void m_btnAlignVerStart_actionPerformed(ActionEvent e)
    {
        this.AddAlignmentConstraint( Constraint.directionVertical,
                                Constraint.originStart,
                                this.m_chkAlignToFirstSelected.isSelected(),
                                this.m_tnVerAlign);
    }

    /**
     * Handles click on the "Align to center vertically" button.
     */
    private void m_btnAlignVerCenter_actionPerformed(ActionEvent e)
    {
        this.AddAlignmentConstraint( Constraint.directionVertical,
                                Constraint.originCenter,
                                this.m_chkAlignToFirstSelected.isSelected(),
                                this.m_tnVerAlign);
    }

    /**
     * Handles click on the "Align to right" button.
     */
    private void m_btnAlignVerEnd_actionPerformed(ActionEvent e)
    {
        this.AddAlignmentConstraint( Constraint.directionVertical,
                                Constraint.originEnd,
                                this.m_chkAlignToFirstSelected.isSelected(),
                                this.m_tnVerAlign);
    }

    /**
     * Handles click on the "Space equal vertically" button.
     */
    private void m_btnSpaceVertically_actionPerformed(ActionEvent e)
    {
        this.AddEqualSpacingConstraint(Constraint.directionVertical, this.m_tnVerEqSpace);
    }

    /**
     * Handles click on the "Order vertically" button.
     */
    private void m_btnOrderVertically_actionPerformed(ActionEvent e)
    {
        this.AddSequenceConstraint(Constraint.directionVertical, this.m_tnVerOrder);
    }

    /**
     * Handles click on the "Zone" button.
     */
    void m_btnZone_actionPerformed(ActionEvent e)
    {
        IXNode mainNode = this.GetFirstSelectedNode();
        if (null == mainNode)
        {
            return;
        }

        Constraint constraint = this.m_layoutSystem.createZoneConstraint(mainNode, this.m_currentOpd.getSelectedItems());
        this.m_layoutSystem.addConstraint(constraint);
        this.AddConstraintToTree(constraint, this.m_tnZone);
    }

    /**
     * Handles click on the "Tree" button.
     */
    void m_btnTree_actionPerformed(ActionEvent e)
    {
        IXNode mainNode = this.GetFirstSelectedNode();
        if (null == mainNode)
        {
            return;
        }

        Constraint constraint = this.m_layoutSystem.createTreeConstraint( mainNode,
                                                                     null, // don't have access to the relation node
                                                                     this.m_currentOpd.getSelectedItems());
        this.m_layoutSystem.addConstraint(constraint);
        this.AddConstraintToTree(constraint, this.m_tnTree);
    }

    /**
     * Handles click on the "Syntactic" button.
     */
    void m_btnSyntactic_actionPerformed(ActionEvent e)
    {
        //
        // Make sure the model contains all nodes and edges in the current OPD.
        //
        this.AddNodesToModel();
        this.AddEdgesToModel();

        //
        // Add syntactic constraints.
        //
        for ( Iterator iterSyntacticConstraints = this.m_layoutSystem.addSyntacticConstraints();
              iterSyntacticConstraints.hasNext();
              /* inside loop */)
        {
            Constraint constraint = (Constraint)iterSyntacticConstraints.next();

            DefaultMutableTreeNode treeGroup;
            if (constraint instanceof NodeNodeOverlapConstraint)
            {
                treeGroup = this.m_tnNodeNodeOverlap;
            }
            else if (constraint instanceof NodeEdgeOverlapConstraint)
            {
                treeGroup = this.m_tnNodeEdgeOverlap;
            }
            else if (constraint instanceof ZoneConstraint)
            {
                treeGroup = this.m_tnZone;
            }
            else
            {
                continue;
            }

            this.AddConstraintToTree(constraint, treeGroup);
        }
    }

    /**
     * Handles click on the "Anchor" button.
     */
    void m_btnAnchor_actionPerformed(ActionEvent e)
    {
        IXNode nodeToAnchor = this.GetFirstSelectedNode();
        if (null == nodeToAnchor)
        {
            return;
        }
        this.m_layoutSystem.anchorNode(nodeToAnchor);

        DefaultMutableTreeNode treeNode  = new DefaultMutableTreeNode(nodeToAnchor);
        DefaultTreeModel       treeModel = (DefaultTreeModel)this.m_treeConstraints.getModel();

        treeModel.insertNodeInto(treeNode, this.m_tnAnchor, 0);
        this.m_treeConstraints.scrollPathToVisible(new TreePath(treeNode.getPath()));
    }

    /**
     * Handles selecting items in the constraints tree control.
     */
    private void m_treeConstraints_valueChanged(TreeSelectionEvent e)
    {
        if (null != this.m_currentlySelectedConstraint)
        {
            if (this.m_currentlySelectedConstraint instanceof Constraint)
            {
                ((Constraint)this.m_currentlySelectedConstraint).Hide();
            }
            else if (this.m_currentlySelectedConstraint instanceof IXNode)
            {
            }
            else
            {
                
            }
        }

        this.m_currentlySelectedConstraint = this.GetSelectedConstraint();

        if (null != this.m_currentlySelectedConstraint)
        {
            if (this.m_currentlySelectedConstraint instanceof Constraint)
            {
                ((Constraint)this.m_currentlySelectedConstraint).Show();
            }
            else if (this.m_currentlySelectedConstraint instanceof IXNode)
            {
            }
            else
            {
                
            }
        }
    }

    /**
     * Handles click on the "Remove coonstraint" button.
     */
    private void m_btnRemoveConstraint_actionPerformed(ActionEvent e)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.m_treeConstraints.getLastSelectedPathComponent();
        if (null == node)
        {
           return;
        }

        Object userObject = node.getUserObject();
        if (null == userObject)
        {
            return;
        }

        if (userObject instanceof Constraint)
        {
            this.m_layoutSystem.removeConstraint((Constraint)userObject);
        }
        else if (userObject instanceof IXNode)
        {
            this.m_layoutSystem.removeAnchor((IXNode)userObject);
        }
        else
        {
            
        }

        node.removeFromParent();
        this.m_treeConstraints.updateUI();
    }

    private JButton                m_btnArrange            = new JButton();
    private JButton                m_btnStep               = new JButton();
    private JCheckBox              m_chkArrangeInTwoPhases = new JCheckBox();
    private JTree                  m_treeConstraints       = new JTree();
    private JScrollPane            m_scrollTreeConstraints;
    private JLabel                 m_lblHorozontal         = new JLabel();
    private JLabel                 m_lblVertical           = new JLabel();
    private JButton                m_btnAlignHorStart      = new JButton();
    private JButton                m_btnAlignHorCenter     = new JButton();
    private JButton                m_btnAlignHorEnd        = new JButton();
    private JButton                m_btnSpaceHorizontally  = new JButton();
    private JButton                m_btnOrderHorizontally  = new JButton();
    private JButton                m_btnAlignVerStart      = new JButton();
    private JButton                m_btnAlignVerCenter     = new JButton();
    private JButton                m_btnAlignVerEnd        = new JButton();
    private JButton                m_btnSpaceVertically    = new JButton();
    private JButton                m_btnOrderVertically    = new JButton();
    private JButton                m_btnZone               = new JButton();
    private JButton                m_btnTree               = new JButton();
    private JButton                m_btnSyntactic          = new JButton();
    private JButton                m_btnAnchor             = new JButton();
    private JButton                m_btnRemoveConstraint   = new JButton();
    private JCheckBox              m_chkAlignToFirstSelected = new JCheckBox();
    private JButton                m_btnReload             = new JButton();
    private DefaultMutableTreeNode m_tnNodeNodeOverlap;
    private DefaultMutableTreeNode m_tnNodeEdgeOverlap;
    private DefaultMutableTreeNode m_tnHorAlign;
    private DefaultMutableTreeNode m_tnHorEqSpace;
    private DefaultMutableTreeNode m_tnHorOrder;
    private DefaultMutableTreeNode m_tnVerAlign;
    private DefaultMutableTreeNode m_tnVerEqSpace;
    private DefaultMutableTreeNode m_tnVerOrder;
    private DefaultMutableTreeNode m_tnZone;
    private DefaultMutableTreeNode m_tnTree;
    private DefaultMutableTreeNode m_tnAnchor;
    private Object                 m_currentlySelectedConstraint;

    private IXSystem                                 m_system;
    private IXSystemStructure                        m_systemStructure;
    private IXOpd                                    m_currentOpd;
    private long                                     m_currentOpdId;
    private ConstraintBasedForceDirectedLayoutSystem m_layoutSystem;
    JButton m_btnReload1 = new JButton();
    JButton m_btnInitArrange = new JButton();
    void m_btnReload1_actionPerformed(ActionEvent e) {

    }
}