package gui.opdGraphics.opdBaseComponents;

import exportedAPI.OpcatConstants;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.OpcatContainer;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opdProject.Selection;
import gui.opdProject.StateMachine;
import gui.projectStructure.GraphicalRelationRepresentation;
import gui.projectStructure.Instance;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * <p>
 * This is a superclass for graphic representations of all four fundamental
 * relations. It is an abstract class and cannot be instantinated directly,
 * direct instantination of this class has no meaning in OPM.\ Fundamental
 * Relation is a absract term in OPM. <code>OpdFundamentalRelation</code>
 * differs in its meaning from OPM fundamental relation. One OPM relation
 * represents relation between one OPM object to another, while one OPD relation
 * can represent several relations from one type with one source object and
 * several destination objects. Such difference is for better visual cognition
 * of inter-object relations.
 * </p>
 * <p>
 * Singele source connected to <code>OpdFundamentalRelation</code> with line,
 * and <code>OpdFundamentalRelation</code> is connected to several sources
 * with lines.
 * </p>
 */

public abstract class OpdFundamentalRelation extends OpdBaseComponent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1664685703958158853L;

	protected GraphicalRelationRepresentation graphicalEntry;

    protected static final double SELECTION_OFFSET = 3.0;

    /**
         * <strong>No default constructor for this class.</strong><br>
         * <p>
         * This is an abstruct class and cannot be instantinated directely. It
         * calls <code>super()</code> with given parameters and in such way
         * initializes default values, all subclasses have to call
         * <code>super()</code> in their constructors to initialize the
         * defaults and set parameters given as arguments
         * </p>
         * 
         * @param <code>pOpdId long</code>, uniqe identifier of the OPD this
         *                component is added to.
         * @param <code>pEntityInOpdId long</code>, uniqe identifier of the
         *                component within OPD it is added to.
         * @param <code>pProject OpdProject</code>, project this component
         *                is added to.
         */
    public OpdFundamentalRelation(long pOpdId, long pEntityInOpdId,
	    OpdProject pProject) {
	super(pOpdId, pEntityInOpdId, pProject);
	GenericTable config = pProject.getConfiguration();
	double normalSize = ((Integer) config.getProperty("NormalSize"))
		.doubleValue();
	double currentSize = ((Integer) config.getProperty("CurrentSize"))
		.doubleValue();
	double factor = currentSize / normalSize;
	double width = ((Integer) config
		.getProperty("FundamentalRelationWidth")).intValue()
		* factor;
	double height = ((Integer) config
		.getProperty("FundamentalRelationHeight")).intValue()
		* factor;

	this.setSize((int) width, (int) height);
    }

    public void setSize(int width, int height) {
	super.setSize(width, (int) Math.round(width * 1.73205080 / 2));
    }

    public void setSize(Dimension d) {
	this.setSize((int) Math.round(d.getWidth()), (int) Math.round(d
		.getHeight()));
    }

    /**
         * <p>
         * As said above all <code>OpdFundamentalRelation</code>s have
         * different logical meaning then OPM fundamental relation definition.
         * Thus <a href =
         * "../projectStructure/GraphicalRelationRepresentationEntry.html"><code>GraphicalRelationRepresentationEntry</code></a>
         * stored in dynamic database. Each <code>OpdFundamentalRelation</code>
         * has reference of its
         * <code>GraphicalRelationRepresentationEntry</code> in <a href =
         * "../projectStructure/MainStructure.html"><code>MainStructure</code></a>.
         * </p>
         * 
         * @return reference to relation's <a href =
         *         "../projectStructure/GraphicalRelationRepresentationEntry.html"><code>GraphicalRelationRepresentationEntry</code></a>.
         */
    public GraphicalRelationRepresentation getGraphicalEntry() {
	return this.graphicalEntry;
    }

    /**
         * <p>
         * As said above all <code>OpdFundamentalRelation</code>s have
         * different logical meaning then OPM fundamental relation definition.
         * Thus <a href =
         * "../projectStructure/GraphicalRelationRepresentationEntry.html"><code>GraphicalRelationRepresentationEntry</code></a>
         * stored in dynamic database. Each <code>OpdFundamentalRelation</code>
         * has reference of its
         * <code>GraphicalRelationRepresentationEntry</code> in <a href =
         * "../projectStructure/MainStructure.html"><code>MainStructure</code></a>.
         * </p>
         * 
         * @param reference
         *                to <a href =
         *                "../projectStructure/GraphicalRelationRepresentationEntry.html"><code>GraphicalRelationRepresentationEntry</code></a>
         *                this <code>OpdFundamentalRelation</code> will
         *                represent.
         */
    public void setGraphicalEntry(
	    GraphicalRelationRepresentation pGraphicalEntry) {
	this.graphicalEntry = pGraphicalEntry;
    }

    /**
         * Displays properties dialog for given fundamental relation.
         */

    /**
         * @return <code>true</code> if the point (pX, pY) that given in
         *         coordinates of this component is in area, that clicking and
         *         dragging mouse will move the componenet, otherwise
         *         <code>false</code>.
         */
    public boolean inMove(int pX, int pY) {
	return true;
    }

    /**
         * @return <code>true</code> if the point (pX, pY) that given in
         *         coordinates of this component is in area, that clicking and
         *         dragging mouse will resize the componenet, otherwise
         *         <code>false</code>.
         */
    public boolean inResize(int pX, int pY) {
	return false;
    }

    /**
         * @return the border (as defined in <a href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>
         *         where the point (pX, pY) given in this component coordinates
         *         is, or <code>NOT_IN_BORDER</code>.
         */
    public int whichBorder(int pX, int pY) {
	return OpcatConstants.NOT_IN_BORDER;
    }

    /**
         * @return A point of connection according to <code>pSide</code> and
         *         <code>parem</code> arguments
         * @param <code>pSide</code> -- the side of OPD graphic component
         * @param <code>param</code> -- The relation of connection point to
         *                length of the given side
         */
    public Point getAbsoluteConnectionPoint(RelativeConnectionPoint pPoint) {
	Point retPoint = new Point(0, 0);
	GenericTable config = this.myProject.getConfiguration();
	double currentSize = ((Integer) config.getProperty("CurrentSize"))
		.doubleValue();
	double normalSize = ((Integer) config.getProperty("NormalSize"))
		.doubleValue();
	double factor = currentSize / normalSize;
	int selOffset = Math.round((float) (SELECTION_OFFSET * factor));
	int dSelOffset = 2 * selOffset;

	switch (pPoint.getSide()) {
	case OpcatConstants.N_BORDER:
	    retPoint.setLocation((this.getWidth() - 1) * pPoint.getParam(),
		    selOffset);
	    break;
	case OpcatConstants.S_BORDER:
	    retPoint.setLocation((this.getWidth() - 1) * pPoint.getParam(),
		    this.getHeight() - selOffset);
	    break;
	case OpcatConstants.E_BORDER:
	    retPoint.setLocation(this.getWidth() - selOffset, selOffset
		    + (this.getHeight() - dSelOffset) * pPoint.getParam());
	    break;
	case OpcatConstants.W_BORDER:
	    retPoint.setLocation(selOffset, selOffset
		    + (this.getHeight() - dSelOffset) * pPoint.getParam());
	    break;
	}

	return retPoint;
    }

    public boolean isInAdjacentArea(int x, int y) {
	return this.contains(x, y);
    }

    protected void addObjects2Move() {
    }

    public void mouseReleased(MouseEvent e) {
	GraphicsUtils.setLastMouseEvent(e);
	// if (StateMachine.isWaiting() || StateMachine.isAnimated()) {
	// return;
	// }
	//
	// if (e.isPopupTrigger() || e.isMetaDown()) {
	// GraphicalRelationRepresentation gr = this.getGraphicalEntry();
	//
	// JPopupMenu jpm;
	// jpm = new FundamentalRelationPopup(this.myProject, gr
	// .getInstance(getOpdKey()));
	// jpm.show(this, e.getX(), e.getY());
	// return;
	// }
    }

    public void mousePressed(MouseEvent e) {
	if (StateMachine.isWaiting()) {
	    return;
	}

	super.mousePressed(e);
	Selection sel = ((OpcatContainer) this.getParent()).getSelection();

	if (e.isShiftDown()) {
	    if (this.isSelected()) {
		sel.removeSelection(this);
	    } else {
		sel.addSelection(this, !e.isShiftDown());
	    }
	} else {
	    if (this.isSelected()) {
		sel.addSelection(this, false);
	    } else {
		sel.addSelection(this, true);
	    }
	}

	for (Enumeration locenum = this.graphicalEntry.getInstances(); locenum
		.hasMoreElements();) {
	    Instance currInst = (Instance) locenum.nextElement();
	    if (e.isShiftDown() && currInst.isSelected()) {
		sel.removeSelection(currInst);
	    } else {
		if (currInst.isVisible()) {
		    sel.addSelection(currInst, false);
		}
	    }
	}

    }

    public void drawSelection(Graphics2D g) {
	Stroke oldStroke = g.getStroke();

	g.setStroke(new BasicStroke());

	GenericTable config = this.myProject.getConfiguration();
	double currentSize = ((Integer) config.getProperty("CurrentSize"))
		.doubleValue();
	double normalSize = ((Integer) config.getProperty("NormalSize"))
		.doubleValue();
	double factor = currentSize / normalSize;
	int selOffset = Math.round((float) (SELECTION_OFFSET * factor));
	int dSelOffset = 2 * selOffset;

	g.setColor(Color.white);
	g.fillRect(this.getActualWidth() / 2 - selOffset, 0, dSelOffset,
		dSelOffset);
	g.fillRect(0, this.getActualHeight() - 1 - dSelOffset, dSelOffset,
		dSelOffset);
	g.fillRect(this.getActualWidth() - 1 - dSelOffset, this
		.getActualHeight()
		- 1 - dSelOffset, dSelOffset, dSelOffset);

	g.setColor(Color.black);
	g.drawRect(this.getActualWidth() / 2 - selOffset, 0, dSelOffset,
		dSelOffset);
	g.drawRect(0, this.getActualHeight() - 1 - dSelOffset, dSelOffset,
		dSelOffset);
	g.drawRect(this.getActualWidth() - 1 - dSelOffset, this
		.getActualHeight()
		- 1 - dSelOffset, dSelOffset, dSelOffset);

	g.setStroke(oldStroke);
    }

}
