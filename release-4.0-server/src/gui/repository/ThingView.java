package gui.repository;

import exportedAPI.OpdKey;
import exportedAPI.opcatAPIx.IXInstance;
import exportedAPI.opcatAPIx.IXSystem;
import exportedAPI.util.OpcatApiException;
import extensionTools.reuse.MergeCommand;
import gui.Opcat2;
import gui.images.standard.StandardImages;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import gui.opmEntities.OpmThing;
import gui.projectStructure.Entry;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.util.opcatGrid.GridPanel;

import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ThingView extends BaseView implements DragSourceListener,
	DragGestureListener, TreeModelListener {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    /**
         * 
         */

    OpdProject myProject;

    Object[] row = { "", "", "", "", "" };

    Object[] tag = { "", "" };

    GridPanel results = null;

    public ThingView(String invisibleRoot) {
	super(invisibleRoot);
	this.tip = new String("Things List");
	this.viewName = "Things List";

	this.icon = StandardImages.System_Icon;
	this.setType(Repository.ThingsVIEW);
	// this.setDragEnabled(true);

	// Make this JTree a drag source
	DragSource dragSource = DragSource.getDefaultDragSource();
	dragSource.createDefaultDragGestureRecognizer(this,
		DnDConstants.ACTION_COPY_OR_MOVE, this);

	// Also, make this JTree a drag target
	DropTarget dropTarget = new DropTarget(this, new CDropTargetListener());
	dropTarget.setDefaultActions(DnDConstants.ACTION_COPY_OR_MOVE);

	ArrayList<String> colNames = new ArrayList<String>();
	colNames.add("OPD");
	colNames.add("Thing");
	colNames.add("Merge Done?");
	colNames.add("Dragged");
	colNames.add("Target");
	this.results = new GridPanel(colNames);
	this.results.getGrid().setDuplicateRows(false);
	this.results.getButtonPane().add(new JLabel(""));
	this.results.getButtonPane().add(new JLabel(""));
	this.results.getButtonPane().add(new JLabel(""));
	this.results.getButtonPane().add(new JLabel(""));
	this.results.getButtonPane().add(new JLabel(""));
	this.results.setTabName("Merge Results");
    }

    public void rebuildTree(MainStructure s, OpdProject rootComponent) {

	super.rebuildTree(s, rootComponent);
	this.myProject = rootComponent;
	this.results.setInstanceTag(this.myProject);

	DefaultTreeModel model = (DefaultTreeModel) this.getModel();
	DefaultMutableTreeNode prjRoot = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) model
		.getRoot()).getChildAt(0);
	TreeMap<String, OpmEntity> sorterHt = new TreeMap<String, OpmEntity>();

	DefaultMutableTreeNode tmpOpdNode;
	tmpOpdNode = new DefaultMutableTreeNode("Things");
	model.insertNodeInto(tmpOpdNode, prjRoot, prjRoot.getChildCount());
	// LERA
	TreePath nodes = new TreePath(tmpOpdNode.getPath());
	this.addSelectionPath(nodes);
	// END
	this.nodesHash.put(new HashKey(tmpOpdNode.getUserObject()), tmpOpdNode);

	// sort things
	long i = 0;
	for (Enumeration e = s.getElements(); e.hasMoreElements();) {
	    Entry ent = (Entry) e.nextElement();
	    if (ent instanceof ThingEntry) {
		if (ent instanceof ProcessEntry) {
		    sorterHt.put("P" + ent.getLogicalEntity().getName() + i,
			    ent.getLogicalEntity());
		} else {
		    sorterHt.put("O" + ent.getLogicalEntity().getName() + i,
			    ent.getLogicalEntity());
		}
		i++;
	    }
	}

	// put things into tree
	DefaultMutableTreeNode currFather;

	currFather = (DefaultMutableTreeNode) prjRoot.getChildAt(0);

	// put things into needed OPD tree entry
	for (Iterator iter = sorterHt.values().iterator(); iter.hasNext();) {
	    model.insertNodeInto(new DefaultMutableTreeNode(iter.next()),
		    currFather, currFather.getChildCount());
	}
	this.setExpandedDescendants();
    }

    // Interface: DragGestureListener
    public void dragGestureRecognized(DragGestureEvent e) {

	Point ptDragOrigin = e.getDragOrigin();
	TreePath path = this.getPathForLocation(ptDragOrigin.x, ptDragOrigin.y);
	if (path == null) {
	    return;
	}
	if (this.isRootPath(path)) {
	    return; // Ignore user trying to drag the root node
	}

	this.setSelectionPath(path); // Select this path in the tree

	// System.out.println("DRAGGING: " + path.getLastPathComponent());

	// Wrap the path being transferred into a Transferable object
	Transferable transferable = new TransferableTreePath(path);

	// We pass our drag image just in case it IS supported by the platform
	e.startDrag(null, transferable, this);
    }

    // Interface: DragSourceListener
    public void dragEnter(DragSourceDragEvent e) {
    }

    public void dragOver(DragSourceDragEvent e) {
    }

    public void dragExit(DragSourceEvent e) {
    }

    public void dropActionChanged(DragSourceDragEvent e) {
    }

    public void dragDropEnd(DragSourceDropEvent e) {
	if (e.getDropSuccess()) {
	    int nAction = e.getDropAction();
	    if (nAction == DnDConstants.ACTION_MOVE) { // The dragged item

	    }
	}
    }

    // DropTargetListener interface object...
    class CDropTargetListener implements DropTargetListener {

	// Constructor...
	public CDropTargetListener() {
	}

	// DropTargetListener interface
	public void dragEnter(DropTargetDragEvent e) {
	    if (!this.isDragAcceptable(e)) {
		e.rejectDrag();
	    } else {
		e.acceptDrag(e.getDropAction());
	    }
	}

	public void dragExit(DropTargetEvent e) {
	}

	/**
         * This is where the ghost image is drawn
         */
	public void dragOver(DropTargetDragEvent e) {
	}

	public void dropActionChanged(DropTargetDragEvent e) {
	    if (!this.isDragAcceptable(e)) {
		e.rejectDrag();
	    } else {
		e.acceptDrag(e.getDropAction());
	    }
	}

	public void drop(DropTargetDropEvent e) {
	    int nAction = e.getDropAction();
	    Object obj = ((DefaultMutableTreeNode) ThingView.this
		    .getLastSelectedPathComponent()).getUserObject();

	    if ((obj instanceof OpmThing)
		    && (nAction == DnDConstants.ACTION_MOVE)) { // The dragged
		// item

		Point dropPoint = e.getLocation();
		TreePath dropPath = ThingView.this.getPathForLocation(
			dropPoint.x, dropPoint.y);

		OpmThing dragged = (OpmThing) obj;

		OpmThing target = (OpmThing) ((DefaultMutableTreeNode) dropPath
			.getLastPathComponent()).getUserObject();

		if (!dragged.getClass().getCanonicalName().equalsIgnoreCase(
			target.getClass().getCanonicalName())) {
		    return;
		}
		Opd curOPD = ThingView.this.myProject.getCurrentOpd();

		int retValue = JOptionPane
			.showConfirmDialog(
				Opcat2.getFrame(),
				"Merge \""
					+ ThingView.this
						.getLastSelectedPathComponent()
					+ "\" with \""
					+ dropPath.getLastPathComponent()
					+ "\" ?\nMerge will be done only in Opd's \n where both source and traget are present",
				"Merge", JOptionPane.YES_NO_OPTION);

		if (retValue != JOptionPane.YES_OPTION) {
		    return;
		}

		ThingView.this.results.AddToExtensionToolsPanel();
		ThingView.this.results.ClearData();
		int counter = 0;
		// merga all instances of dragged with one instance of target
		Enumeration dragedEnum = Opcat2.getCurrentProject()
			.getSystemStructure().getEntry(dragged.getId())
			.getInstances();
		while (dragedEnum.hasMoreElements()) {
		    ThingInstance dragedIns = (ThingInstance) dragedEnum
			    .nextElement();
		    if (dragedIns.getKey().getOpdId() == OpdProject.CLIPBOARD_ID) {
			continue;
		    }
		    Enumeration targetEnum = Opcat2.getCurrentProject()
			    .getSystemStructure().getEntry(target.getId())
			    .getInstances();
		    ThingInstance targetIns = null;
		    boolean found = false;
		    while (targetEnum.hasMoreElements()) {
			targetIns = (ThingInstance) targetEnum.nextElement();
			// while (targetIns.getKey().getOpdId() ==
			// OpdProject.CLIPBOARD_ID) {
			// targetIns = (ThingInstance) targetEnum
			// .nextElement();
			// }
			if (targetIns.getKey().getOpdId() == OpdProject.CLIPBOARD_ID) {
			    continue;
			}
			if ((targetIns.getOpd().getOpdId() == dragedIns
				.getOpd().getOpdId())
				&& (targetIns.getKey().getOpdId() != OpdProject.CLIPBOARD_ID)) {
			    found = true;
			    break;
			}
		    }

		    ThingView.this.row = new Object[5];
		    ThingView.this.tag = new Object[2];

		    ThingView.this.row[0] = dragedIns.getOpd().getName();
		    ThingView.this.row[3] = dragged.getName();
		    ThingView.this.row[4] = target.getName();

		    if (found) {
			MergeCommand merge = new MergeCommand(
				ThingView.this.myProject);
			try {
			    boolean addStates = (counter == 0);
			    boolean ret = false;
			    ThingView.this.myProject.setCurrentOpd(targetIns
				    .getOpd());
			    ret = merge.commitMerge(dragedIns, targetIns,
				    addStates);
			    counter++;
			    if (Opcat2.getCurrentProject() != null)
				Opcat2.getCurrentProject().setCanClose(false);
			    if (!ret) {
				ThingView.this.row[1] = dragedIns.getEntry()
					.getName();
				ThingView.this.row[2] = "Could not Merge";
				ThingView.this.tag[0] = dragedIns.getKey();
				ThingView.this.tag[1] = new Long(dragedIns
					.getEntry().getId());
			    } else {
				ThingView.this.row[1] = targetIns.getEntry()
					.getName();
				ThingView.this.row[2] = "Yes";
				ThingView.this.tag[0] = targetIns.getKey();
				ThingView.this.tag[1] = new Long(targetIns
					.getEntry().getId());
			    }
			} catch (OpcatApiException ex) {
			    ThingView.this.row[1] = dragedIns.getEntry()
				    .getName();
			    ThingView.this.row[2] = ex.getMessage();
			    ThingView.this.tag[0] = dragedIns.getKey();
			    ThingView.this.tag[1] = new Long(dragedIns
				    .getEntry().getId());
			}

		    } else {
			ThingView.this.row[1] = dragedIns.getEntry().getName();
			ThingView.this.row[2] = "No Target in OPD";
			ThingView.this.tag[0] = dragedIns.getKey();
			ThingView.this.tag[1] = new Long(dragedIns.getEntry()
				.getId());
		    }
		    ThingView.this.results.getGrid().addRow(ThingView.this.row,
			    ThingView.this.tag);
		}

		ThingView.this.myProject.setCurrentOpd(curOPD);
		// ThingView.this.myProject.merge(source, target) ;

	    }

	    e.dropComplete(true);
	    ThingView.this.rebuildTree(myProject.getComponentsStructure(),
		    myProject);
	} // Helpers...

	public boolean isDragAcceptable(DropTargetDragEvent e) {

	    Object obj = ((DefaultMutableTreeNode) ThingView.this
		    .getLastSelectedPathComponent()).getUserObject();
	    Point dropPoint = e.getLocation();

	    // Only accept COPY or MOVE gestures (ie LINK is not supported)
	    if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
		return false;
	    }

	    // Only accept this particular flavor
	    if (!e.isDataFlavorSupported(TransferableTreePath.TREEPATH_FLAVOR)) {
		return false;
	    }

	    if (!(obj instanceof OpmThing)) {
		return false;
	    }

	    OpmThing dragged = (OpmThing) obj;

	    TreePath dropPath = ThingView.this.getPathForLocation(dropPoint.x,
		    dropPoint.y);

	    OpmThing target = (OpmThing) ((DefaultMutableTreeNode) dropPath
		    .getLastPathComponent()).getUserObject();

	    if (!dragged.getClass().getCanonicalName().equalsIgnoreCase(
		    target.getClass().getCanonicalName())) {
		return false;
	    }

	    /*
                 * // Do this if you want to prohibit dropping onto the drag
                 * source... Point pt = e.getLocation(); TreePath path =
                 * getClosestPathForLocation(pt.x, pt.y); if
                 * (path.equals(_pathSource)) return false;
                 * 
                 */

	    /*
                 * // Do this if you want to select the best flavor on offer...
                 * DataFlavor[] flavors = e.getCurrentDataFlavors(); for (int i =
                 * 0; i < flavors.length; i++ ) { DataFlavor flavor =
                 * flavors[i]; if
                 * (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
                 * return true; }
                 */
	    return true;
	}

	public boolean isDropAcceptable(DropTargetDropEvent e) {
	    // Only accept COPY or MOVE gestures (ie LINK is not supported)
	    if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
		return false;
	    }

	    // Only accept this particular flavor
	    if (!e.isDataFlavorSupported(TransferableTreePath.TREEPATH_FLAVOR)) {
		return false;
	    }

	    /*
                 * // Do this if you want to prohibit dropping onto the drag
                 * source... Point pt = e.getLocation(); TreePath path =
                 * getClosestPathForLocation(pt.x, pt.y); if
                 * (path.equals(_pathSource)) return false;
                 */

	    /*
                 * // Do this if you want to select the best flavor on offer...
                 * DataFlavor[] flavors = e.getCurrentDataFlavors(); for (int i =
                 * 0; i < flavors.length; i++ ) { DataFlavor flavor =
                 * flavors[i]; if
                 * (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
                 * return true; }
                 */
	    return true;
	}

    }

    // TreeModelListener interface...
    public void treeNodesChanged(TreeModelEvent e) {
	System.out.println("treeNodesChanged");
	this.sayWhat(e);
	// We dont need to reset the selection path, since it has not moved
    }

    public void treeNodesInserted(TreeModelEvent e) {
	System.out.println("treeNodesInserted ");
	this.sayWhat(e);

	// We need to reset the selection path to the node just inserted
	int nChildIndex = e.getChildIndices()[0];
	TreePath pathParent = e.getTreePath();
	this.setSelectionPath(this.getChildPath(pathParent, nChildIndex));
    }

    public void treeNodesRemoved(TreeModelEvent e) {
	System.out.println("treeNodesRemoved ");
	this.sayWhat(e);
    }

    public void treeStructureChanged(TreeModelEvent e) {
	System.out.println("treeStructureChanged ");
	this.sayWhat(e);
    }

    // More helpers...
    private TreePath getChildPath(TreePath pathParent, int nChildIndex) {
	TreeModel model = this.getModel();
	return pathParent.pathByAddingChild(model.getChild(pathParent
		.getLastPathComponent(), nChildIndex));
    }

    private boolean isRootPath(TreePath path) {
	return this.isRootVisible() && (this.getRowForPath(path) == 0);
    }

    private void sayWhat(TreeModelEvent e) {
	System.out.println(e.getTreePath().getLastPathComponent());
	int[] nIndex = e.getChildIndices();
	for (int i = 0; i < nIndex.length; i++) {
	    System.out.println(i + ". " + nIndex[i]);
	}
    }

}

class MergeGridPanel extends GridPanel {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    /**
         * 
         */

    public static String PanelName = "";

    private IXSystem mySys;

    private IXInstance lastIns = null;

    private Color lastColor = Color.black;

    public MergeGridPanel(int ColumnsNumber, ArrayList ColumnsNames,
	    IXSystem opcatSystem) {
	super(ColumnsNames);
	this.getGrid().addMouseListener(new MouseListner(this));
	this.mySys = opcatSystem;
	this.getGrid().setDuplicateRows(true);
	this.getGrid().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public void AddRow(Object[] row, Object[] rowTag) {
	this.getGrid().addRow(row, rowTag);
    }

    public void RemoveFromExtensionToolsPanel() {
	super.RemoveFromExtensionToolsPanel();
	this.RestoreThingOrigColor();
    }

    public void RestoreThingOrigColor() {
	if (this.lastIns != null) {
	    this.lastIns.setBorderColor(this.lastColor);
	}
    }

    public void dblClickEvent() {
	Object[] tag = new Object[2];
	IXInstance ins;
	tag = this.getGrid().GetTag(this.getGrid().getSelectedRow());
	OpdKey key = (OpdKey) tag[0];
	Long entityID = (Long) tag[1];
	ins = this.mySys.getIXSystemStructure()
		.getIXEntry(entityID.longValue()).getIXInstance(key);

	if ((this.lastIns != null) && (this.lastIns != ins)) {
	    this.lastIns.setBorderColor(this.lastColor);
	    this.lastIns.update();
	}
	if (this.lastIns != ins) {
	    this.lastIns = ins;
	    this.lastColor = this.lastIns.getBorderColor();
	    ins.setBorderColor(Color.red);
	    ins.update();
	}
	this.mySys.showOPD(key.getOpdId());

    }

    public class MouseListner extends MouseAdapter {
	private MergeGridPanel panel;

	public MouseListner(MergeGridPanel Panel) {
	    this.panel = Panel;
	}

	public void mouseClicked(MouseEvent e) {
	    if (e.getClickCount() == 2) {
		this.panel.dblClickEvent();
	    }
	}
    }

    public void setMySys(IXSystem mySys) {
	this.mySys = mySys;
    }
}
