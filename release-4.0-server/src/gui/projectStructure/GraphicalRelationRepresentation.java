package gui.projectStructure;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.DraggedPoint;
import gui.opdGraphics.MoveUpdatable;
import gui.opdGraphics.draggers.AroundDragger;
import gui.opdGraphics.draggers.TransparentAroundDragger;
import gui.opdGraphics.lines.OpdSimpleLine;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdAggregation;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdExhibition;
import gui.opdGraphics.opdBaseComponents.OpdFundamentalRelation;
import gui.opdGraphics.opdBaseComponents.OpdInstantination;
import gui.opdGraphics.opdBaseComponents.OpdProcess;
import gui.opdGraphics.opdBaseComponents.OpdSpecialization;
import gui.opdProject.OpdProject;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JLayeredPane;

/**
 * <p>
 * This class describes some "triangle" (graphical component representing
 * several fundamental relations). We need this class because to one "triangle"
 * user connects fundamental relation that don't related logically. So this
 * class holds data structure that contains instances of all fundamental
 * relations connected via this "triangle". This class also holds the
 * informatiom about the "tringle" itself. It compound from following components -
 * <br>
 * relation - OpdFundamentalRelation that represents graphically "triangle"
 * itself.<br>
 * source - OpdConnectionEdge which is source for all fundamental relation
 * connected via this "triangle".<br>
 * sourceDragger - AroundDragger which is connected to the source.<br>
 * line - OpdLine which connects sourceDragger to the "triangle".
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class GraphicalRelationRepresentation implements MoveUpdatable {
    private OpdProject myProject;

    private AroundDragger sourceDragger;

    private OpdConnectionEdge source;

    private OpdSimpleLine lines[];

    private DraggedPoint points[];

    private OpdFundamentalRelation relation;

    private Hashtable<OpdKey, FundamentalRelationInstance> graphicalInstances;

    private Container myContainer;
    
    private Container parentContainer = null ; 

    private int myType;

    private final static int BYPASS_DISTANCE = 15;

    private int selectionsCount = 0;


    /**
         * Creates GraphicalRelationRepresentationEntry that holds all
         * information about specified pRelation ("triangle").
         * 
         * @param pSource
         *                OpdConnectionEdge which is source for all fundamental
         *                relation connected via this "triangle".
         * @param pSourceDragger
         *                AroundDragger which is connected to the source.
         * @param pLine
         *                OpdLine which connects sourceDragger to the
         *                "triangle".
         * @param pRelation
         *                OpdFundamentalRelation that represents graphically
         *                "triangle" itself.
         */

    public GraphicalRelationRepresentation(OpdConnectionEdge pSource,
	    RelativeConnectionPoint pPoint1, int relType, OpdProject project,
	    Point initLocation, Container cn)

    {

	this.parentContainer = cn ; 
	this.graphicalInstances = new Hashtable<OpdKey, FundamentalRelationInstance>();
	this.lines = new OpdSimpleLine[5];
	this.points = new DraggedPoint[4];
	this.myProject = project;
	this.source = pSource;
	this.myType = relType;

	this.relation = this.addRelation(relType, cn);
	cn.add(this.relation, JLayeredPane.MODAL_LAYER);

	this.relation.setLocation(initLocation);

	this.sourceDragger = this.addDragger(this.source, pPoint1,
		this.myContainer);
	cn.add(this.sourceDragger, JLayeredPane.MODAL_LAYER);
	this.createPoints(cn);
	this.createLines(cn);

	this.sourceDragger.setLine(this.lines[0]);
	this.source.addDragger(this.sourceDragger);

	this.addLines2Points();
	this.relation.addLine(this.lines[4]);
	this.source.repaintLines();

	cn.repaint();

	this.relation.setGraphicalEntry(this);
	this.source.addUpdateListener(this);
	this.sourceDragger.addUpdateListener(this);
	this.relation.addUpdateListener(this);

    }

    private void addLines2Points() {
	this.points[0].addLine(this.lines[0]);
	this.points[0].addLine(this.lines[1]);
	this.points[1].addLine(this.lines[1]);
	this.points[1].addLine(this.lines[2]);
	this.points[2].addLine(this.lines[2]);
	this.points[2].addLine(this.lines[3]);
	this.points[3].addLine(this.lines[3]);
	this.points[3].addLine(this.lines[4]);
    }

    private void createLines(Container cn) {
	RelativeConnectionPoint centerPoint = new RelativeConnectionPoint(
		BaseGraphicComponent.CENTER, 0);

	this.lines[0] = this.createLine(this.sourceDragger, centerPoint,
		this.points[0], centerPoint);
	this.lines[1] = this.createLine(this.points[0], centerPoint,
		this.points[1], centerPoint);
	this.lines[2] = this.createLine(this.points[1], centerPoint,
		this.points[2], centerPoint);
	this.lines[3] = this.createLine(this.points[2], centerPoint,
		this.points[3], centerPoint);
	this.lines[4] = this.createLine(this.points[3], centerPoint,
		this.relation, new RelativeConnectionPoint(
			OpcatConstants.N_BORDER, 0.5));

	cn.add(this.lines[0], JLayeredPane.PALETTE_LAYER);
	cn.add(this.lines[1], JLayeredPane.PALETTE_LAYER);
	cn.add(this.lines[2], JLayeredPane.PALETTE_LAYER);
	cn.add(this.lines[3], JLayeredPane.PALETTE_LAYER);
	cn.add(this.lines[4], JLayeredPane.PALETTE_LAYER);
    }

    private void createPoints(Container cn) {
	this.points[0] = new DraggedPoint(this.myProject);
	this.points[1] = new DraggedPoint(this.myProject);
	this.points[2] = new DraggedPoint(this.myProject);
	this.points[3] = new DraggedPoint(this.myProject);

	for (int i = 0; i < this.points.length; i++) {
	    this.points[i].setMoveable(false);
	}

	cn.add(this.points[0], JLayeredPane.PALETTE_LAYER);
	cn.add(this.points[1], JLayeredPane.PALETTE_LAYER);
	cn.add(this.points[2], JLayeredPane.PALETTE_LAYER);
	cn.add(this.points[3], JLayeredPane.PALETTE_LAYER);

	int x1 = this.sourceDragger.getX() + this.sourceDragger.getWidth() / 2
		- this.points[0].getWidth() / 2 - 5;
	int y1 = this.sourceDragger.getY() + this.sourceDragger.getHeight() / 2;
	int x2 = this.relation.getX() + this.relation.getWidth() / 2
		- this.points[0].getWidth() / 2;
	int y2 = this.relation.getY();

	if (this.source.getY() + this.source.getWidth() < this.relation.getY()) {

	    int diff = (this.relation.getY() - this.source.getY() - this.source
		    .getWidth()) / 2;

	    this.points[0].setAbsolutesLocation(x1, y1 + diff);
	    this.points[1].setAbsolutesLocation(x1, y1 + diff);
	    this.points[2].setAbsolutesLocation(x1, y1 + diff);
	    this.points[3].setAbsolutesLocation(x2, y1 + diff);

	} else {

	    this.points[0].setAbsolutesLocation(x1, y1 + 20);
	    this.points[1].setAbsolutesLocation(x2 + 50, y1 + 20);

	    this.points[2].setAbsolutesLocation(x2 + 50, y2 - 20);
	    this.points[3].setAbsolutesLocation(x2, y2 - 20);
	}

	// points[0].setYDirectionAllowed(true);
	// points[1].setYDirectionAllowed(true);
	// points[1].setXDirectionAllowed(true);
	// points[2].setYDirectionAllowed(true);
	// points[2].setXDirectionAllowed(true);
	// points[3].setYDirectionAllowed(true);

	// points[0].setVerticalNeighbor(points[1]);
	// points[1].setVerticalNeighbor(points[0]);
	// points[1].setHorizontalNeighbor(points[2]);
	// points[2].setHorizontalNeighbor(points[1]);
	// points[2].setVerticalNeighbor(points[3]);
	// points[3].setVerticalNeighbor(points[2]);

	// points[0].addMouseMotionListener(points[0]);
	// points[1].addMouseMotionListener(points[1]);
	// points[2].addMouseMotionListener(points[2]);
	// points[3].addMouseMotionListener(points[3]);

    }

    private OpdSimpleLine createLine(Connectable edge1,
	    RelativeConnectionPoint pPoint1, Connectable edge2,
	    RelativeConnectionPoint pPoint2) {

	return new OpdSimpleLine(edge1, pPoint1, edge2, pPoint2, null,
		new OpdKey(0, 0), this.myProject);
    }

    private OpdFundamentalRelation addRelation(int relType, Container container) {
	OpdFundamentalRelation gRelation = null;

	switch (relType) {
	case OpcatConstants.SPECIALIZATION_RELATION: {
	    gRelation = new OpdSpecialization(null, this.myProject, 0, 0);
	    break;
	}

	case OpcatConstants.INSTANTINATION_RELATION: {
	    gRelation = new OpdInstantination(null, this.myProject, 0, 0);
	    break;
	}

	case OpcatConstants.EXHIBITION_RELATION: {
	    gRelation = new OpdExhibition(null, this.myProject, 0, 0);
	    break;
	}

	case OpcatConstants.AGGREGATION_RELATION: {
	    gRelation = new OpdAggregation(null, this.myProject, 0, 0);
	    break;
	}
	}

	gRelation.addMouseListener(gRelation);
	gRelation.addMouseMotionListener(gRelation);

	container.add(gRelation);
	return gRelation;
    }

    public TransparentAroundDragger addDragger(Connectable edge,
	    RelativeConnectionPoint pPoint, Container container) {
	TransparentAroundDragger gDragger;

	gDragger = new TransparentAroundDragger(edge, pPoint, this.myProject);
	gDragger.addMouseListener(gDragger);
	gDragger.addMouseMotionListener(gDragger);

	return gDragger;
    }

    /**
         * Returns OpdConnectionEdge which is source for all fundamental
         * relation connected via this "triangle".
         */
    public OpdConnectionEdge getSource() {
	return this.source;
    }

    public Instance getSourceInstance() {
	return this.source.getEntry().getInstance(this.source.getOpdKey());
    }

    /**
         * Returns OpdLine which connects sourceDragger to the "triangle".
         */
    public OpdSimpleLine getLine(int lineNumber) {
	if (lineNumber < this.lines.length) {
	    return this.lines[lineNumber];
	}

	return null;
    }

    public DraggedPoint getPoint(int pointNumber) {
	if (pointNumber < this.points.length) {
	    return this.points[pointNumber];
	}

	return null;
    }

    /**
         * Returns AroundDragger which is connected to the source.
         */
    public AroundDragger getSourceDragger() {
	return this.sourceDragger;
    }

    /**
         * Returns OpdFundamentalRelation OpdFundamentalRelation that represents
         * graphically "triangle" itself.
         */
    public OpdFundamentalRelation getRelation() {
	return this.relation;
    }

    public int getType() {
	return this.myType;
    }

    /**
         * Tests if the specified pKey is a key in data structure containing
         * graphical instances of this GraphicalRelationRepresentationEntry.
         */

    public boolean containsKey(OpdKey pKey) {
	return this.graphicalInstances.containsKey(pKey);
    }

    /**
         * Adds the specified pInstance with the specified key to the data
         * structure containing graphical instances of this
         * GraphicalRelationRepresentationEntry.
         * 
         * @param key
         *                OpdKey - key of some FundamentalRelationInstance.
         * @param pInstance
         *                FundamentalRelationInstance.
         * @return true if specified key is a new key in data structure
         *         containing graphical instances of this
         *         GraphicalRelationRepresentationEntry. Returns false and does
         *         nothing if specified key already exist in the data structure.
         */

    public boolean addInstance(OpdKey key, FundamentalRelationInstance pInstance) {

	if (this.containsKey(key)) {
	    return false;
	}

	this.graphicalInstances.put(key, pInstance);
	return true;

    }

    public FundamentalRelationInstance getInstance(OpdKey pKey) {
	return (FundamentalRelationInstance) this.graphicalInstances.get(pKey);
    }

    /**
         * Removes from the data structure containing graphical instances of
         * this GraphicalRelationRepresentationEntry Instance with the specified
         * pKey
         * 
         * @param key
         *                OpdKey - key of some FundamentalRelationInstance.
         * @return true operation was successful . False if specified key
         *         doesn't exist in the data structure.
         */
    public boolean removeInstance(OpdKey pKey) {

	if (!this.containsKey(pKey)) {
	    return false;
	}

	this.graphicalInstances.remove(pKey);
	return true;
    }

    /**
         * Returns an enumeration of the FundamentalRelationInstance in this
         * GraphicalRelationRepresentationEntry. Use the Enumeration methods on
         * the returned object to fetch the Instances sequentially
         * 
         * @return an enumeration of the FundamentalRelationInstance in this
         *         MainStructure
         */
    public Enumeration<FundamentalRelationInstance> getInstances() {
	return this.graphicalInstances.elements();
    }

    public boolean isEmpty() {
	return this.graphicalInstances.isEmpty();
    }

    /**
         * Returns the number of graphical instances in this
         * GraphicalRelationRepresentationEntry.
         * 
         */

    public int getInstancesNumber() {
	return this.graphicalInstances.size();
    }

    public void setTextColor(Color textColor) {
	this.relation.setTextColor(textColor);

	for (int i = 0; i < this.lines.length; i++) {
	    this.lines[i].setTextColor(textColor);
	}

	for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
	    FundamentalRelationInstance tempInstance = (FundamentalRelationInstance) (e
		    .nextElement());
	    tempInstance.setTextColor(textColor);
	}

    }

    public Color getTextColor() {
	return this.relation.getTextColor();
    }

    public void setBorderColor(Color borderColor) {
	this.relation.setBorderColor(borderColor);

	for (int i = 0; i < this.lines.length; i++) {
	    this.lines[i].setLineColor(borderColor);
	    this.lines[i].repaint();
	}

	for (int i = 0; i < this.points.length; i++) {
	    this.points[i].setBorderColor(borderColor);
	    this.points[i].repaint();
	}

	for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
	    FundamentalRelationInstance tempInstance = (FundamentalRelationInstance) (e
		    .nextElement());
	    tempInstance.setBorderColor(borderColor);
	}

    }

    public Color getBorderColor() {
	return this.relation.getBorderColor();
    }

    public void setBackgroundColor(Color backgroundColor) {
	this.relation.setBackgroundColor(backgroundColor);
	this.relation.repaint();

	for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
	    FundamentalRelationInstance tempInstance = (FundamentalRelationInstance) (e
		    .nextElement());
	    tempInstance.setBackgroundColor(backgroundColor);
	}

    }

    public void removeFromContainer() {
	Container cn = this.sourceDragger.getParent();

	cn.remove(this.sourceDragger);
	this.source.removeDragger(this.sourceDragger);
	cn.remove(this.relation);

	for (int i = 0; i < this.points.length; i++) {
	    cn.remove(this.points[i]);
	}

	for (int i = 0; i < this.lines.length; i++) {
	    cn.remove(this.lines[i]);
	}

	cn.repaint();

    }

    public void add2Container(Container cn) {
	for (int i = 0; i < this.points.length; i++) {
	    cn.add(this.points[i], JLayeredPane.MODAL_LAYER);
	}

	for (int i = 0; i < this.lines.length; i++) {
	    cn.add(this.lines[i], JLayeredPane.MODAL_LAYER);
	}

	cn.add(this.sourceDragger, JLayeredPane.MODAL_LAYER);
	this.source.addDragger(this.sourceDragger);
	cn.add(this.relation, JLayeredPane.MODAL_LAYER);

	cn.repaint();

    }

    public Color getBackgroundColor() {
	return this.relation.getBackgroundColor();
    }

    public void updateInstances() {
	for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
	    FundamentalRelationInstance tempInstance = (FundamentalRelationInstance) (e
		    .nextElement());
	    FundamentalRelationEntry tempEntry = (FundamentalRelationEntry) tempInstance
		    .getEntry();
	    tempEntry.updateInstances();
	}
    }

    public void updateMove(Object origin) {
	this.arrangeLines();
	for (int i = 0; i < this.points.length; i++) {
	    this.points[i].repaintLines();
	}

    }

    public void updateRelease(Object origin) {
    }

    public void arrangeLines() {

	int sX = this.sourceDragger.getX() + this.sourceDragger.getWidth() / 2
		- this.points[0].getWidth() / 2;
	int sY = this.sourceDragger.getY() + this.sourceDragger.getHeight() / 2
		- this.points[0].getHeight() / 2;
	int dX = this.relation.getX() + this.relation.getWidth() / 2
		- this.points[3].getWidth() / 2;
	int dY = this.relation.getY() - this.points[3].getHeight() / 2;

	int draggerSide = 0;

	if (this.source instanceof OpdProcess) {
	    int side = this.sourceDragger.getSide();
	    double param = this.sourceDragger.getParam();

	    if ((side == OpcatConstants.N_BORDER)
		    && ((param >= 0.2) && (param <= 0.8))) {
		draggerSide = OpcatConstants.N_BORDER;
	    }

	    if ((side == OpcatConstants.S_BORDER)
		    && ((param >= 0.2) && (param <= 0.8))) {
		draggerSide = OpcatConstants.S_BORDER;
	    }

	    if (param >= 0.8) {
		draggerSide = OpcatConstants.E_BORDER;
	    }

	    if (param <= 0.2)

	    {
		draggerSide = OpcatConstants.W_BORDER;
	    }

	} else {
	    draggerSide = this.sourceDragger.getSide();
	}

	if ((dX < sX) && (dY > sY)) // 1
	{
	    if (draggerSide == OpcatConstants.N_BORDER) {
		this.points[0].setAbsolutesLocation(sX, sY - BYPASS_DISTANCE);
		this.points[1].setAbsolutesLocation(dX, sY - BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(dX, sY - BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, sY - BYPASS_DISTANCE);
		return;
	    }

	    if (draggerSide == OpcatConstants.E_BORDER) {
		this.points[0].setAbsolutesLocation(sX + BYPASS_DISTANCE, sY);
		this.points[1].setAbsolutesLocation(sX + BYPASS_DISTANCE, sY
			+ (dY - sY) / 2);
		this.points[2].setAbsolutesLocation(sX + BYPASS_DISTANCE, sY
			+ (dY - sY) / 2);
		this.points[3].setAbsolutesLocation(dX, sY + (dY - sY) / 2);
		return;
	    }

	    if (draggerSide == OpcatConstants.S_BORDER) {
		this.points[0].setAbsolutesLocation(sX, sY + (dY - sY) / 2);
		this.points[1].setAbsolutesLocation(sX, sY + (dY - sY) / 2);
		this.points[2].setAbsolutesLocation(sX, sY + (dY - sY) / 2);
		this.points[3].setAbsolutesLocation(dX, sY + (dY - sY) / 2);
		return;
	    }

	    if (draggerSide == OpcatConstants.W_BORDER) {
		this.points[0].setAbsolutesLocation(dX, sY);
		this.points[1].setAbsolutesLocation(dX, sY);
		this.points[2].setAbsolutesLocation(dX, sY);
		this.points[3].setAbsolutesLocation(dX, sY);
		return;
	    }
	}

	if ((dX > sX) && (dY > sY)) // 2
	{
	    if (draggerSide == OpcatConstants.N_BORDER) {
		this.points[0].setAbsolutesLocation(sX, sY - BYPASS_DISTANCE);
		this.points[1].setAbsolutesLocation(dX, sY - BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(dX, sY - BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, sY - BYPASS_DISTANCE);
		return;
	    }

	    if (draggerSide == OpcatConstants.E_BORDER) {
		this.points[0].setAbsolutesLocation(dX, sY);
		this.points[1].setAbsolutesLocation(dX, sY);
		this.points[2].setAbsolutesLocation(dX, sY);
		this.points[3].setAbsolutesLocation(dX, sY);
		return;

	    }

	    if (draggerSide == OpcatConstants.S_BORDER) {
		this.points[0].setAbsolutesLocation(sX, sY + (dY - sY) / 2);
		this.points[1].setAbsolutesLocation(sX, sY + (dY - sY) / 2);
		this.points[2].setAbsolutesLocation(sX, sY + (dY - sY) / 2);
		this.points[3].setAbsolutesLocation(dX, sY + (dY - sY) / 2);
		return;
	    }

	    if (draggerSide == OpcatConstants.W_BORDER) {
		this.points[0].setAbsolutesLocation(sX - BYPASS_DISTANCE, sY);
		this.points[1].setAbsolutesLocation(sX - BYPASS_DISTANCE, sY
			+ (dY - sY) / 2);
		this.points[2].setAbsolutesLocation(sX - BYPASS_DISTANCE, sY
			+ (dY - sY) / 2);
		this.points[3].setAbsolutesLocation(dX, sY + (dY - sY) / 2);
		return;
	    }
	}

	if ((dX > sX) && (dY < sY)) // 3
	{
	    if (draggerSide == OpcatConstants.N_BORDER) {
		this.points[0].setAbsolutesLocation(sX, dY - BYPASS_DISTANCE);
		this.points[1].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		return;
	    }

	    if (draggerSide == OpcatConstants.E_BORDER) {
		this.points[0].setAbsolutesLocation(sX + (dX - sX) / 2, sY);
		this.points[1].setAbsolutesLocation(sX + (dX - sX) / 2, dY
			- BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(sX + (dX - sX) / 2, dY
			- BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		return;
	    }

	    if (draggerSide == OpcatConstants.S_BORDER) {
		this.points[0].setAbsolutesLocation(sX, sY + BYPASS_DISTANCE);
		this.points[1].setAbsolutesLocation(sX + (dX - sX) / 2, sY
			+ BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(sX + (dX - sX) / 2, dY
			- BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		return;
	    }

	    if (draggerSide == OpcatConstants.W_BORDER) {

		this.points[0].setAbsolutesLocation(sX - BYPASS_DISTANCE, sY);
		this.points[1].setAbsolutesLocation(sX - BYPASS_DISTANCE, dY
			- BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(sX - BYPASS_DISTANCE, dY
			- BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		return;

	    }
	}

	if ((dX < sX) && (dY < sY)) // 4
	{
	    if (draggerSide == OpcatConstants.N_BORDER) {
		this.points[0].setAbsolutesLocation(sX, dY - BYPASS_DISTANCE);
		this.points[1].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		return;
	    }

	    if (draggerSide == OpcatConstants.E_BORDER) {
		this.points[0].setAbsolutesLocation(sX + BYPASS_DISTANCE, sY);
		this.points[1].setAbsolutesLocation(sX + BYPASS_DISTANCE, dY
			- BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(sX + BYPASS_DISTANCE, dY
			- BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		return;
	    }

	    if (draggerSide == OpcatConstants.S_BORDER) {
		this.points[0].setAbsolutesLocation(sX, sY + BYPASS_DISTANCE);
		this.points[1].setAbsolutesLocation(sX + (dX - sX) / 2, sY
			+ BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(sX + (dX - sX) / 2, dY
			- BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		return;
	    }

	    if (draggerSide == OpcatConstants.W_BORDER) {
		this.points[0].setAbsolutesLocation(sX + (dX - sX) / 2, sY);
		this.points[1].setAbsolutesLocation(sX + (dX - sX) / 2, dY
			- BYPASS_DISTANCE);
		this.points[2].setAbsolutesLocation(sX + (dX - sX) / 2, dY
			- BYPASS_DISTANCE);
		this.points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
		return;
	    }
	}

    }

    public void setVisible(boolean isVisible) {
	this.sourceDragger.setVisible(isVisible);
	this.relation.setVisible(isVisible);
	for (int i = 0; i < this.points.length; i++) {
	    this.points[i].setVisible(isVisible);
	}
	for (int i = 0; i < this.lines.length; i++) {
	    this.lines[i].setVisible(isVisible);
	}
    }

    public void incrementSelections() {
	this.selectionsCount++;
    }

    public void decrementSelections() {
	this.selectionsCount--;
    }

    public void incdecSelections(boolean incdec) {
	if (incdec) {
	    this.incrementSelections();
	} else {
	    this.decrementSelections();
	}
    }

    public int getSelectionsCount() {
	return this.selectionsCount;
    }

    public Container getParentContainer() {
        return parentContainer;
    }

}
