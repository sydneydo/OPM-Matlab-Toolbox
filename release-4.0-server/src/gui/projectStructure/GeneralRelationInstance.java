package gui.projectStructure;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPI.IConnectionEdgeInstance;
import exportedAPI.opcatAPI.IRelationInstance;
import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXNode;
import exportedAPI.opcatAPIx.IXRelationInstance;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.draggers.OpdBiDirectionalRelation;
import gui.opdGraphics.draggers.OpdRelationDragger;
import gui.opdGraphics.draggers.OpdUniDirectionalRelation;
import gui.opdGraphics.draggers.TransparentRelationDragger;
import gui.opdGraphics.lines.StyledLine;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmBiDirectionalRelation;
import gui.opmEntities.OpmGeneralRelation;
import gui.opmEntities.OpmUniDirectionalRelation;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JOptionPane;

/**
 * <p>
 * This class represents instance of OPM general structural relation. This
 * GeneralRelationInstance compound from several graphical elements.<br>
 * sourceDragger - AroundDragger that connected to source.<br>
 * destinationDragger - AroundDragger that connected to destination.<br>
 * line - OpdLine that connects AroundDraggers.<br>
 * Additionaly this class holds information about source and destination
 * instances.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class GeneralRelationInstance extends Instance implements
		IRelationInstance, IXRelationInstance {
	private OpdRelationDragger sourceDragger;

	private OpdRelationDragger destinationDragger;

	private OpdConnectionEdge source;

	private OpdConnectionEdge destination;

	private StyledLine line;

	private OpmGeneralRelation myRelation;

	private boolean autoArranged;

	/**
	 * Creates GeneralRelationInstance with specified parameters.
	 * 
	 * @param pSource
	 *            OpdConnectionEdge that represents the source graphically.
	 * @param pSourceDragger
	 *            AroundDragger that connected to source.
	 * @param pDestination
	 *            OpdConnectionEdge that represents the destination graphically.
	 * @param pDestinationDragger
	 *            AroundDragger that connected to destination.
	 * @param pLine
	 *            OpdLine that connects AroundDraggers.
	 * @param myKey
	 *            OpdKey - key of graphical instance of this
	 *            GeneralRelationInstance.
	 */

	public GeneralRelationInstance(OpdConnectionEdge source,
			RelativeConnectionPoint pPoint1, OpdConnectionEdge destination,
			RelativeConnectionPoint pPoint2, OpmGeneralRelation pRelation,
			OpdKey myKey, OpdProject project, Container container) {
		super(myKey, project);
		this.source = source;
		this.destination = destination;
		this.myRelation = pRelation;

		this.destinationDragger = this.createOpdRelation(destination, pPoint2);

		if (this.myRelation instanceof OpmBiDirectionalRelation) {
			this.sourceDragger = this.createOpdRelation(source, pPoint1);
		} else {
			this.sourceDragger = this.createTransparentDragger(source, pPoint1);
		}

		this.line = new StyledLine(source, this.sourceDragger, destination,
				this.destinationDragger, this.myRelation, this.getKey(),
				this.myProject);

		this.add2Container(container);
		this.setAutoArranged(true);
		// this.graphicalRepresentation = myRelation.get ;
	}

	private OpdRelationDragger createOpdRelation(Connectable edge,
			RelativeConnectionPoint pPoint) {

		OpdRelationDragger nRelation = null;

		if (this.myRelation instanceof OpmUniDirectionalRelation) {
			nRelation = new OpdUniDirectionalRelation(edge, pPoint,
					(OpmUniDirectionalRelation) this.myRelation, this.getKey()
							.getOpdId(), this.getKey().getEntityInOpdId(),
					this.myProject);
		}

		if (this.myRelation instanceof OpmBiDirectionalRelation) {
			nRelation = new OpdBiDirectionalRelation(edge, pPoint,
					(OpmBiDirectionalRelation) this.myRelation, this.getKey()
							.getOpdId(), this.getKey().getEntityInOpdId(),
					this.myProject);
		}

		if (nRelation == null) {
			JOptionPane
					.showMessageDialog(
							null,
							" Serious internal bug occured in AddLink function \n Please contact software developers.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		nRelation.addMouseListener(nRelation);
		nRelation.addMouseMotionListener(nRelation);

		return nRelation;
	}

	private TransparentRelationDragger createTransparentDragger(
			Connectable edge, RelativeConnectionPoint pPoint) {
		TransparentRelationDragger gDragger;

		gDragger = new TransparentRelationDragger(edge, pPoint,
				(OpmUniDirectionalRelation) this.myRelation, this.getKey()
						.getOpdId(), this.getKey().getEntityInOpdId(),
				this.myProject);
		gDragger.addMouseListener(gDragger);
		gDragger.addMouseMotionListener(gDragger);
		return gDragger;
	}

	public OpmGeneralRelation getGeneralRelation() {
		return this.myRelation;
	}

	/**
	 * Returns OpdConnectionEdge that represents the source graphically.
	 * 
	 */
	public OpdConnectionEdge getSource() {
		return this.source;
	}

	public Instance getSourceInstance() {
		return this.source.getEntry().getInstance(this.source.getOpdKey());
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

	/**
	 * Returns OpdLine connecting source and destination AroundDragger.
	 * 
	 */
	public StyledLine getLine() {
		return this.line;
	}

	/**
	 * Returns AroundDragger that connected to the source.
	 * 
	 */
	public OpdRelationDragger getSourceDragger() {
		return this.sourceDragger;
	}

	/**
	 * Returns AroundDragger that connected to the destination.
	 * 
	 */
	public OpdRelationDragger getDestinationDragger() {
		return this.destinationDragger;
	}

	public Color getBackgroundColor() {
		return this.sourceDragger.getBackgroundColor();
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.sourceDragger.setBackgroundColor(backgroundColor);
		this.destinationDragger.setBackgroundColor(backgroundColor);
		this.sourceDragger.repaint();
		this.destinationDragger.repaint();
	}

	public Color getBorderColor() {
		return this.sourceDragger.getBorderColor();
	}

	public void setBorderColor(Color borderColor) {
		this.line.setLineColor(borderColor);
		this.line.repaint();
	}

	public Color getTextColor() {
		return this.line.getTextColor();
	}

	public void setTextColor(Color textColor) {
		this.line.setTextColor(textColor);
		this.line.repaint();
	}

	public void update() {
		this.sourceDragger.updateDragger();
		this.destinationDragger.updateDragger();
		this.sourceDragger.setText(this.myRelation.getSourceCardinality());
		this.destinationDragger.setText(this.myRelation
				.getDestinationCardinality());
		this.line.setUpperText(this.myRelation.getForwardRelationMeaning());
		this.line.setLowerText(this.myRelation.getBackwardRelationMeaning());
		this.line.setDashed(this.myRelation.isEnviromental());
		this.line.repaint();
	}

	public void copyPropsFrom(GeneralRelationInstance origin) {
		super.copyPropsFrom(origin);
		this.sourceDragger.setSide(origin.getSourceDragger().getSide());
		this.sourceDragger.setParam(origin.getSourceDragger().getParam());
		this.destinationDragger.setSide(origin.getDestinationDragger()
				.getSide());
		this.destinationDragger.setParam(origin.getDestinationDragger()
				.getParam());
		this.line.copyShapeFrom(origin.getLine());
		this.setAutoArranged(origin.isAutoArranged());
		if (this.isAutoArranged()) {
			this.line.arrange();
		}

		int dX = origin.getSourceDragger().getOpdCardinalityLabel().getX()
				- origin.getSourceDragger().getX();
		int dY = origin.getSourceDragger().getOpdCardinalityLabel().getY()
				- origin.getSourceDragger().getY();
		;
		this.sourceDragger.getOpdCardinalityLabel().setLocation(
				this.sourceDragger.getX() + dX, this.sourceDragger.getY() + dY);

		dX = origin.getDestinationDragger().getOpdCardinalityLabel().getX()
				- origin.getDestinationDragger().getX();
		dY = origin.getDestinationDragger().getOpdCardinalityLabel().getY()
				- origin.getDestinationDragger().getY();
		this.destinationDragger.getOpdCardinalityLabel().setLocation(
				this.destinationDragger.getX() + dX,
				this.destinationDragger.getY() + dY);
	}

	public void setSelected(boolean isSelected) {
		this.line.setSelected(isSelected);
		this.selected = isSelected;
	}

	public void removeFromContainer() {
		this.line.removeFromContainer();
	}

	public void add2Container(Container cn) {
		this.line.add2Container(cn);
	}

	public long getLogicalId() {
		return this.myRelation.getId();
	}

	public void makeStraight() {
		this.line.makeStraight();
	}

	public boolean isStraight() {
		return this.line.isStraight();
	}

	public void setAutoArranged(boolean yn) {
		this.autoArranged = yn;
		this.line.setAutoArranged(yn);
	}

	public boolean isAutoArranged() {
		return this.autoArranged;
	}

	public RelativeConnectionPoint getSourceConnectionPoint() {
		return this.sourceDragger.getRelativeConnectionPoint();
	}

	/**
	 * Sets source RelativeConnectionPoint - the point on source of this
	 * IXRelationInstance to which line of this link is connected.
	 * 
	 * @see RelativeConnectionPoint
	 */
	public void setSourceConnectionPoint(RelativeConnectionPoint point) {
		this.sourceDragger.setRelativeConnectionPoint(point);
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
		return (IXConnectionEdgeInstance) this.source.getInstance();
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
		return (IConnectionEdgeInstance) this.source.getInstance();
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
		return (IXConnectionEdgeInstance) this.getSourceInstance();
	}

	/**
	 * Gets IXNode which is destination of this IXLine.
	 */
	public IXNode getDestinationIXNode() {
		return (IXConnectionEdgeInstance) this.getDestinationInstance();
	}

	public void autoArrange() {
	}

	public void setVisible(boolean isVisible) {
		this.line.setVisible(isVisible);
		this.sourceDragger.setVisible(isVisible);
		this.destinationDragger.setVisible(isVisible);
	}

	public boolean isVisible() {
		return this.sourceDragger.isVisible();
	}

	public BaseGraphicComponent[] getGraphicComponents() {
		return this.line.getPointsArray();
	}

	public String getTypeString() {
		return "General Relation";
	}
}
