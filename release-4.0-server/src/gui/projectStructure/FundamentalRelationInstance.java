package gui.projectStructure;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPI.IConnectionEdgeInstance;
import exportedAPI.opcatAPI.IRelationInstance;
import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXNode;
import exportedAPI.opcatAPIx.IXRelationInstance;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.DraggedPoint;
import gui.opdGraphics.MoveUpdatable;
import gui.opdGraphics.draggers.TextDragger;
import gui.opdGraphics.lines.OpdSimpleLine;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdFundamentalRelation;
import gui.opdGraphics.opdBaseComponents.OpdProcess;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmFundamentalRelation;

import java.awt.Color;
import java.awt.Container;
import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

/**
 * <p>
 * This class represents instance of OPM fundamental relation. This
 * FundamentalRelationInstance compound from several graphical elements.<br>
 * destinationDragger - AroundDragger that connected to destination.<br>
 * line - OpdLine that connect destination AroundDragger to "triangle"
 * representing graphically this fundamental relation.<br>
 * Additionaly this class holds information about source and destination
 * instances, and logical entity - OpmFundamentalRelation representing logically
 * this fundamental relation, OpdFundamentalRelation representing graphically
 * this fundamental relation.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class FundamentalRelationInstance extends Instance implements
		MoveUpdatable, IXRelationInstance, IRelationInstance {
	private OpmFundamentalRelation logicalRelation;

	private boolean visible = true;

	private GraphicalRelationRepresentation myRepresentation;

	private TextDragger destinationDragger;

	private OpdConnectionEdge destination;

	private OpdSimpleLine lines[];

	private DraggedPoint points[];

	private OpdProject myProject;

	private final static int BYPASS_DISTANCE = 15;

	/**
	 * Creates FundamentalRelationInstance with specified parameters.
	 * 
	 * @param pSource
	 *            OpdConnectionEdge that represents the source graphically.
	 * @param pRelation
	 *            OpdFundamentalRelation representing graphically this
	 *            fundamental relation
	 * @param pDestination
	 *            OpdConnectionEdge that represents the destination graphically.
	 * @param pDestinationDragger
	 *            AroundDragger that connected to destination.
	 * @param pLine
	 *            OpdLine that connects destination AroundDragger to "triangle"
	 *            representing graphically this fundamental relation.
	 * @param myKey
	 *            OpdKey - key of graphical instance of this LinkInstance.
	 * @param pLogicalRelation
	 *            OpmFundamentalRelation representing this fundamental relation.
	 */
	public FundamentalRelationInstance(OpmFundamentalRelation pLogicalRelation,
			OpdConnectionEdge pDestination, RelativeConnectionPoint pPoint,
			GraphicalRelationRepresentation representation, OpdKey myKey,
			OpdProject project, Container cn) {
		super(myKey, project);

		this.myProject = project;
		this.lines = new OpdSimpleLine[5];
		this.points = new DraggedPoint[4];

		this.myRepresentation = representation;
		this.graphicalRepresentation = representation.getRelation();
		this.destination = pDestination;
		this.logicalRelation = pLogicalRelation;

		this.destinationDragger = this.addTextDragger(this.destination, pPoint,
				this.logicalRelation.getDestinationCardinality());
		// cn.add(destinationDragger, JLayeredPane.MODAL_LAYER);
		// cn.add(destinationDragger.getOpdCardinalityLabel(),JLayeredPane.MODAL_LAYER);

		this.createPoints(cn);
		this.createLines(cn);

		this.destinationDragger.setLine(this.lines[4]);
		this.destination.addDragger(this.destinationDragger);

		this.addLines2Points();
		representation.getRelation().addLine(this.lines[0]);

		representation.getRelation().repaintLines();
		this.destination.repaintLines();
		this.setBackgroundColor(representation.getBackgroundColor());
		this.setBorderColor(representation.getBorderColor());
		this.setTextColor(representation.getTextColor());
		this.add2Container(cn);
		this.arrangeLines();
		this.update();
		this.destination.addUpdateListener(this);
		this.destinationDragger.addUpdateListener(this);
		this.getGraphicalRelationRepresentation().getRelation()
				.addUpdateListener(this);

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

		this.lines[0] = this.createLine(this.myRepresentation.getRelation(),
				new RelativeConnectionPoint(OpcatConstants.S_BORDER, 0.5),
				this.points[0], centerPoint);
		this.lines[1] = this.createLine(this.points[0], centerPoint,
				this.points[1], centerPoint);
		this.lines[2] = this.createLine(this.points[1], centerPoint,
				this.points[2], centerPoint);
		this.lines[3] = this.createLine(this.points[2], centerPoint,
				this.points[3], centerPoint);
		this.lines[4] = this.createLine(this.points[3], centerPoint,
				this.destinationDragger, centerPoint);

		// cn.add(lines[0], JLayeredPane.PALETTE_LAYER);
		// cn.add(lines[1], JLayeredPane.PALETTE_LAYER);
		// cn.add(lines[2], JLayeredPane.PALETTE_LAYER);
		// cn.add(lines[3], JLayeredPane.PALETTE_LAYER);
		// cn.add(lines[4], JLayeredPane.PALETTE_LAYER);
	}

	private void createPoints(Container cn) {
		this.points[0] = new DraggedPoint(this.myProject);
		this.points[1] = new DraggedPoint(this.myProject);
		this.points[2] = new DraggedPoint(this.myProject);
		this.points[3] = new DraggedPoint(this.myProject);

		for (int i = 0; i < this.points.length; i++) {
			this.points[i].setMoveable(false);
		}
		// cn.add(points[0], JLayeredPane.PALETTE_LAYER);
		// cn.add(points[1], JLayeredPane.PALETTE_LAYER);
		// cn.add(points[2], JLayeredPane.PALETTE_LAYER);
		// cn.add(points[3], JLayeredPane.PALETTE_LAYER);

		OpdFundamentalRelation rel = this.myRepresentation.getRelation();

		int x1 = rel.getX() + rel.getWidth() / 2 - this.points[0].getWidth()
				/ 2 - 5;
		int y1 = rel.getY() + rel.getHeight();

		int x2 = this.destinationDragger.getX()
				+ this.destinationDragger.getWidth() / 2
				- this.points[0].getWidth() / 2;
		int y2 = this.destinationDragger.getY();

		if (rel.getY() + rel.getWidth() < this.destination.getY()) {
			int diff = (this.destination.getY() - rel.getY() - rel.getWidth()) / 2;

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
		//
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
		OpdSimpleLine gLine;
		gLine = new OpdSimpleLine(edge1, pPoint1, edge2, pPoint2,
				this.logicalRelation, this.getKey(), this.myProject);
		gLine.addMouseListener(gLine);
		return gLine;
	}

	public TextDragger addTextDragger(Connectable edge,
			RelativeConnectionPoint pPoint, String text) {
		TextDragger gDragger;

		gDragger = new TextDragger(edge, pPoint, text, this.myProject);
		gDragger.addMouseListener(gDragger);
		gDragger.addMouseMotionListener(gDragger);

		// container.add(gDragger, JLayeredPane.MODAL_LAYER);
		// container.repaint();
		return gDragger;
	}

	public void setAutoArranged(boolean isAutorranged) {
		//
	}

	public boolean isAutoArranged() {
		return true;
	}

	public void makeStraight() {
		//
	}

	public boolean isStraight() {
		return false;
	}

	/**
	 * Gets source RelativeConnectionPoint - the point on source of this
	 * IXRelationInstance to which line the link is connected. You can translate
	 * the RelativeConnectionPoint to regular Point by
	 * getAbsoluteConnectionPoint of IXConnectionEdgeInstance method.
	 * 
	 * @see RelativeConnectionPoint
	 * @see IXConnectionEdgeInstance#getAbsoluteConnectionPoint(RelativeConnectionPoint
	 *      relPoint)
	 */
	public RelativeConnectionPoint getSourceConnectionPoint() {
		return this.myRepresentation.getSourceDragger()
				.getRelativeConnectionPoint();
	}

	/**
	 * Sets source RelativeConnectionPoint - the point on source of this
	 * IXRelationInstance to which line of this link is connected.
	 * 
	 * @see RelativeConnectionPoint
	 */
	public void setSourceConnectionPoint(RelativeConnectionPoint point) {
		this.myRepresentation.getSourceDragger().setRelativeConnectionPoint(
				point);
	}

	/**
	 * Gets destination RelativeConnectionPoint - the point on destination of
	 * this IXRelationInstance to which line of this link is connected. You can
	 * translate the RelativeConnectionPoint to regular Point by
	 * getAbsoluteConnectionPoint of IXConnectionEdgeInstance method.
	 * 
	 * @see RelativeConnectionPoint
	 * @see IXConnectionEdgeInstance#getAbsoluteConnectionPoint(RelativeConnectionPoint
	 *      relPoint)
	 */
	public RelativeConnectionPoint getDestinationConnectionPoint() {
		return this.destinationDragger.getRelativeConnectionPoint();
	}

	/**
	 * Sets destination RelativeConnectionPoint - the point on destination of
	 * this IXRelationInstance to which line of this link is connected.
	 * 
	 * @see RelativeConnectionPoint
	 */

	public void setDestinationConnectionPoint(RelativeConnectionPoint point) {
		this.destinationDragger.setRelativeConnectionPoint(point);
	}

	/**
	 * Gets IXConnectionEdgeInstance which is source of this IXRelationInstance.
	 */

	public IXConnectionEdgeInstance getSourceIXInstance() {
		return (IXConnectionEdgeInstance) this.myRepresentation
				.getSourceInstance();
	}

	/**
	 * Gets IXConnectionEdgeInstance which is destination of this
	 * IXRelationInstance.
	 */
	public IXConnectionEdgeInstance getDestinationIXInstance() {
		return (IXConnectionEdgeInstance) this.destination.getInstance();
	}

	/**
	 * Gets IConnectionEdgeInstance which is source of this IRelationInstance.
	 */

	public IConnectionEdgeInstance getSourceIInstance() {
		return (IConnectionEdgeInstance) this.myRepresentation
				.getSourceInstance();
	}

	/**
	 * Gets IConnectionEdgeInstance which is destination of this
	 * IRelationInstance.
	 */
	public IConnectionEdgeInstance getDestinationIInstance() {
		return (IConnectionEdgeInstance) this.destination.getInstance();
	}

	/**
	 * Gets IXNode which is source of this IXNode.
	 */

	public IXNode getSourceIXNode() {
		return this.getSourceIXInstance();
	}

	/**
	 * Gets IXNode which is destination of this IXLine.
	 */
	public IXNode getDestinationIXNode() {
		return (IXConnectionEdgeInstance) this.getDestinationInstance();
	}

	/**
	 * Returns OpdConnectionEdge that represents the destination graphically.
	 * 
	 */

	public OpdConnectionEdge getDestination() {
		return this.destination;
	}

	public Instance getDestinationInstance() {
		return this.destination.getEntry().getInstance(
				this.destination.getOpdKey());
	}

	public GraphicalRelationRepresentation getGraphicalRelationRepresentation() {
		return this.myRepresentation;
	}

	/**
	 * Returns OpdLine that connects destination AroundDragger to "triangle"
	 * representing graphically this fundamental relation.
	 * 
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
	 * Returns AroundDragger that connected to the destination.
	 * 
	 */
	public TextDragger getDestinationDragger() {
		return this.destinationDragger;
	}

	/**
	 * Returns OpmFundamentalRelation that represents this fundamental relation
	 * logically.
	 * 
	 */
	public OpmFundamentalRelation getLogicalRelation() {
		return this.logicalRelation;
	}

	public Color getBackgroundColor() {
		return this.destinationDragger.getBackgroundColor();
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.destinationDragger.setBackgroundColor(backgroundColor);
	}

	public Color getBorderColor() {
		return this.destinationDragger.getBorderColor();
	}

	public void setBorderColor(Color borderColor) {
		this.destinationDragger.setBorderColor(borderColor);
		for (int i = 0; i < this.lines.length; i++) {
			this.lines[i].setLineColor(borderColor);
			this.lines[i].repaint();
		}

		for (int i = 0; i < this.points.length; i++) {
			this.points[i].setBorderColor(borderColor);
			this.points[i].repaint();
		}
	}

	public Color getTextColor() {
		return this.destinationDragger.getTextColor();
	}

	public void setTextColor(Color textColor) {
		this.destinationDragger.setTextColor(textColor);
		this.destinationDragger.repaint();
		this.destinationDragger.getOpdCardinalityLabel()
				.setTextColor(textColor);
		this.destinationDragger.getOpdCardinalityLabel().repaint();
	}

	public void setSelected(boolean isSelected) {

		boolean selectDragger = true;
		for (int i = 0; i < this.points.length; i++) {
			if (this.points[i].isVisible()) {
				this.points[i].setSelected(isSelected);
				this.points[i].repaint();
			} else if (selectDragger) {
				selectDragger = false;
			}
		}

		if (selectDragger) {
			this.destinationDragger.setSelected(isSelected);
			this.destinationDragger.repaint();
			this.myRepresentation.incdecSelections(isSelected);
			this.selected = isSelected;
		}

	}

	// public void setVisibleEntry(boolean isVisible) {
	// this.destinationDragger.setVisible(isVisible);
	// for (int i = 0; i < this.points.length; i++) {
	// this.points[i].setVisible(isVisible);
	// }
	// for (int i = 0; i < this.lines.length; i++) {
	// this.lines[i].setVisible(isVisible);
	// }
	// this.myRepresentation.setVisible(isVisible);
	// }

	public void setVisible(boolean isVisible) {

		// this.destinationDragger.setVisible(isVisible);

		for (int i = 0; i < this.points.length; i++) {
			this.points[i].setVisible(isVisible);
		}
		for (int i = 0; i < this.lines.length; i++) {
			this.lines[i].setVisible(isVisible);
		}

		Enumeration<FundamentalRelationInstance> instances = this.myRepresentation
				.getInstances();

		boolean visiable = isVisible;
		while (instances.hasMoreElements()) {
			FundamentalRelationInstance ins = instances.nextElement();
			if (!visiable
					&& !(ins.getKey().toString().equalsIgnoreCase(this.getKey()
							.toString())) && ins.isVisible()) {
				visiable = true;
			}
		}
		this.myRepresentation.setVisible(visiable);
		this.visible = isVisible;

	}

	public boolean isVisible() {
		// return this.destinationDragger.isVisible();
		return this.visible;
	}

	public void removeFromContainer() {
		Container cn = this.destinationDragger.getParent();

		cn.remove(this.destinationDragger);
		cn.remove(this.destinationDragger.getOpdCardinalityLabel());
		this.destination.removeDragger(this.destinationDragger);

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
			cn.add(this.points[i], JLayeredPane.PALETTE_LAYER);
		}

		for (int i = 0; i < this.lines.length; i++) {
			cn.add(this.lines[i], new Integer(JLayeredPane.PALETTE_LAYER
					.intValue() - 1));
		}

		cn.add(this.destinationDragger, new Integer(JLayeredPane.PALETTE_LAYER
				.intValue() + 1));
		this.destination.addDragger(this.destinationDragger);
		cn.add(this.destinationDragger.getOpdCardinalityLabel(),
				JLayeredPane.MODAL_LAYER);
		this.destinationDragger.updateDragger();
		cn.repaint();

	}

	public void update() {
		this.destinationDragger.setText(this.logicalRelation
				.getDestinationCardinality());
		this.destination.repaintLines();
		for (int i = 0; i < this.points.length; i++) {
			this.points[i].repaintLines();
		}

	}

	public long getLogicalId() {
		return this.logicalRelation.getId();
	}

	public void updateMove(Object origin) {
		this.arrangeLines();

		// for (int i = 0; i < points.length; i++)
		// {
		// points[i].repaintLines();
		// }
		//

		// System.err.println(points[0]+" "+points[1]+" "+points[2]+"
		// "+points[3]);
	}

	public void updateRelease(Object origin) {
	}

	public void arrangeLines() {
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		int bypassDistance = (int) Math.round(BYPASS_DISTANCE * factor);

		JComponent rel = this.getGraphicalRelationRepresentation()
				.getRelation();
		int sX = rel.getX() + rel.getWidth() / 2 - this.points[0].getWidth()
				/ 2;
		int sY = rel.getY() + rel.getHeight() - this.points[3].getHeight() / 2;
		int dX = this.destinationDragger.getX()
				+ this.destinationDragger.getWidth() / 2
				- this.points[3].getWidth() / 2;
		int dY = this.destinationDragger.getY()
				+ this.destinationDragger.getHeight() / 2
				- this.points[3].getHeight() / 2;

		int draggerSide = 0;

		if (this.destination instanceof OpdProcess) {
			int side = this.destinationDragger.getSide();
			double param = this.destinationDragger.getParam();

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
			draggerSide = this.destinationDragger.getSide();
		}

		if ((dX < sX) && (dY > sY)) // 1
		{
			if (draggerSide == OpcatConstants.N_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[1].setAbsolutesLocation(sX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[2].setAbsolutesLocation(sX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[3].setAbsolutesLocation(dX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				return;
			}

			if (draggerSide == OpcatConstants.E_BORDER) {
				this.points[0].setAbsolutesLocation(sX, dY);
				this.points[1].setAbsolutesLocation(sX, dY);
				this.points[2].setAbsolutesLocation(sX, dY);
				this.points[3].setAbsolutesLocation(sX, dY);
				return;
			}

			if (draggerSide == OpcatConstants.S_BORDER) {
				this.points[0].setAbsolutesLocation(sX, dY + bypassDistance);
				this.points[1].setAbsolutesLocation(sX, dY + bypassDistance);
				this.points[2].setAbsolutesLocation(sX, dY + bypassDistance);
				this.points[3].setAbsolutesLocation(dX, dY + bypassDistance);
				return;
			}

			if (draggerSide == OpcatConstants.W_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[1].setAbsolutesLocation(dX - bypassDistance, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[2].setAbsolutesLocation(dX - bypassDistance, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[3].setAbsolutesLocation(dX - bypassDistance, dY);
				return;
			}
		}

		if ((dX > sX) && (dY > sY)) // 2
		{
			if (draggerSide == OpcatConstants.N_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[1].setAbsolutesLocation(sX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[2].setAbsolutesLocation(sX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[3].setAbsolutesLocation(dX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				return;
			}

			if (draggerSide == OpcatConstants.E_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[1].setAbsolutesLocation(dX + bypassDistance, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[2].setAbsolutesLocation(dX + bypassDistance, sY
						+ (int) Math.round((dY - sY) / 2.0));
				this.points[3].setAbsolutesLocation(dX + bypassDistance, dY);
				return;
			}

			if (draggerSide == OpcatConstants.S_BORDER) {
				this.points[0].setAbsolutesLocation(sX, dY + bypassDistance);
				this.points[1].setAbsolutesLocation(sX, dY + bypassDistance);
				this.points[2].setAbsolutesLocation(sX, dY + bypassDistance);
				this.points[3].setAbsolutesLocation(dX, dY + bypassDistance);
				return;
			}

			if (draggerSide == OpcatConstants.W_BORDER) {
				this.points[0].setAbsolutesLocation(sX, dY);
				this.points[1].setAbsolutesLocation(sX, dY);
				this.points[2].setAbsolutesLocation(sX, dY);
				this.points[3].setAbsolutesLocation(sX, dY);
				return;
			}
		}

		if ((dX < sX) && (dY < sY)) // 3
		{
			if (draggerSide == OpcatConstants.N_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY + bypassDistance);
				this.points[1].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), sY
						+ bypassDistance);
				this.points[2].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), dY
						- bypassDistance);
				this.points[3].setAbsolutesLocation(dX, dY - bypassDistance);
				return;
			}

			if (draggerSide == OpcatConstants.E_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY + bypassDistance);
				this.points[1].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), sY
						+ bypassDistance);
				this.points[2].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), sY
						+ bypassDistance);
				this.points[3].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), dY);
				return;
			}

			if (draggerSide == OpcatConstants.S_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY + bypassDistance);
				this.points[1].setAbsolutesLocation(dX, sY + bypassDistance);
				this.points[2].setAbsolutesLocation(dX, sY + bypassDistance);
				this.points[3].setAbsolutesLocation(dX, sY + bypassDistance);
				return;
			}

			if (draggerSide == OpcatConstants.W_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY + bypassDistance);
				this.points[1].setAbsolutesLocation(dX - bypassDistance, sY
						+ bypassDistance);
				this.points[2].setAbsolutesLocation(dX - bypassDistance, sY
						+ bypassDistance);
				this.points[3].setAbsolutesLocation(dX - bypassDistance, dY);
				return;
			}
		}

		if ((dX > sX) && (dY < sY)) // 4
		{
			if (draggerSide == OpcatConstants.N_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY + bypassDistance);
				this.points[1].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), sY
						+ bypassDistance);
				this.points[2].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), dY
						- bypassDistance);
				this.points[3].setAbsolutesLocation(dX, dY - bypassDistance);
				return;
			}

			if (draggerSide == OpcatConstants.E_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY + bypassDistance);
				this.points[1].setAbsolutesLocation(dX + bypassDistance, sY
						+ bypassDistance);
				this.points[2].setAbsolutesLocation(dX + bypassDistance, sY
						+ bypassDistance);
				this.points[3].setAbsolutesLocation(dX + bypassDistance, dY);
				return;
			}

			if (draggerSide == OpcatConstants.S_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY + bypassDistance);
				this.points[1].setAbsolutesLocation(dX, sY + bypassDistance);
				this.points[2].setAbsolutesLocation(dX, sY + bypassDistance);
				this.points[3].setAbsolutesLocation(dX, sY + bypassDistance);
				return;
			}

			if (draggerSide == OpcatConstants.W_BORDER) {
				this.points[0].setAbsolutesLocation(sX, sY + bypassDistance);
				this.points[1].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), sY
						+ bypassDistance);
				this.points[2].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), sY
						+ bypassDistance);
				this.points[3].setAbsolutesLocation(dX
						+ (int) Math.round((sX - dX) / 2.0), dY);
				return;

			}
		}

	}

	public BaseGraphicComponent[] getGraphicComponents() {
		return this.points;
	}

	public Instance getSourceInstance() {
		return this.myRepresentation.getSourceInstance();
	}

	public OpdSimpleLine[] getLines() {
		return lines;
	}

	public String getTypeString() {
		return "Fundamental Relation";
	}
}
