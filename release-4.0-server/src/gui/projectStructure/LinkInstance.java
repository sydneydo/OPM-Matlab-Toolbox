package gui.projectStructure;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPI.IConnectionEdgeInstance;
import exportedAPI.opcatAPI.ILinkInstance;
import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXLinkInstance;
import exportedAPI.opcatAPIx.IXNode;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.MoveUpdatable;
import gui.opdGraphics.draggers.AroundDragger;
import gui.opdGraphics.draggers.OpdAgentLink;
import gui.opdGraphics.draggers.OpdConditionLink;
import gui.opdGraphics.draggers.OpdConsumptionEventLink;
import gui.opdGraphics.draggers.OpdConsumptionLink;
import gui.opdGraphics.draggers.OpdEffectLink;
import gui.opdGraphics.draggers.OpdExceptionLink;
import gui.opdGraphics.draggers.OpdInstrumentEventLink;
import gui.opdGraphics.draggers.OpdInstrumentLink;
import gui.opdGraphics.draggers.OpdInvocationLink;
import gui.opdGraphics.draggers.OpdLink;
import gui.opdGraphics.draggers.OpdResultLink;
import gui.opdGraphics.draggers.TransparentLinkDragger;
import gui.opdGraphics.lines.StyledLine;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmAgent;
import gui.opmEntities.OpmConditionLink;
import gui.opmEntities.OpmConsumptionEventLink;
import gui.opmEntities.OpmConsumptionLink;
import gui.opmEntities.OpmEffectLink;
import gui.opmEntities.OpmExceptionLink;
import gui.opmEntities.OpmInstrument;
import gui.opmEntities.OpmInstrumentEventLink;
import gui.opmEntities.OpmInvocationLink;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmResultLink;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * <p>
 * This class represents instance of OPM procedural link. This LinkInstance
 * compound from several graphical elements.<br>
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

public class LinkInstance extends Instance implements MoveUpdatable,
		IXLinkInstance, ILinkInstance {
	private AroundDragger sourceDragger;

	private AroundDragger destinationDragger;

	private OpdConnectionEdge source;

	private OpdConnectionEdge destination;

	private StyledLine line;

	private OpmProceduralLink myLink;

	private boolean autoArranged;

	private OpdProject myProject;

	private OrInstance destOr = null;

	private OrInstance sourceOr = null;

	/**
	 * Creates LinkInstance with specified parameters.
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
	 *            OpdKey - key of graphical instance of this LinkInstance.
	 */

	public LinkInstance(OpdConnectionEdge source,
			RelativeConnectionPoint pPoint1, OpdConnectionEdge destination,
			RelativeConnectionPoint pPoint2, OpmProceduralLink pLink,
			OpdKey myKey, OpdProject project, Container container) {
		super(myKey, project);

		this.myProject = project;
		this.source = source;
		this.destination = destination;
		this.myLink = pLink;

		this.destinationDragger = this.createOpdLink(destination, pPoint2);

		if (this.myLink instanceof OpmEffectLink) {
			this.sourceDragger = this.createOpdLink(source, pPoint1);
		} else {
			this.sourceDragger = this.createTransparentDragger(source, pPoint1);
		}

		this.line = new StyledLine(source, this.sourceDragger, destination,
				this.destinationDragger, this.myLink, this.getKey(),
				this.myProject);

		if (this.myLink instanceof OpmInvocationLink) {
			this.line.setStyle(StyledLine.INVOCATION);
		} else {
			this.line.setStyle(StyledLine.BREAKABLE);
		}

		this.add2Container(container);
		this.setAutoArranged(true);

	}

	private TransparentLinkDragger createTransparentDragger(Connectable edge,
			RelativeConnectionPoint pPoint) {
		TransparentLinkDragger gDragger;

		gDragger = new TransparentLinkDragger(edge, pPoint, this.myLink, this
				.getKey().getOpdId(), this.getKey().getEntityInOpdId(),
				this.myProject);
		gDragger.addMouseListener(gDragger);
		gDragger.addMouseMotionListener(gDragger);
		return gDragger;
	}

	private OpdLink createOpdLink(Connectable edge,
			RelativeConnectionPoint pPoint) {

		OpdLink nLink = null;

		if (this.myLink instanceof OpmAgent) {
			nLink = new OpdAgentLink(edge, pPoint, (OpmAgent) this.myLink, this
					.getKey().getOpdId(), this.getKey().getEntityInOpdId(),
					this.myProject);
		}

		if (this.myLink instanceof OpmInstrument) {
			nLink = new OpdInstrumentLink(edge, pPoint,
					(OpmInstrument) this.myLink, this.getKey().getOpdId(), this
							.getKey().getEntityInOpdId(), this.myProject);
		}

		if (this.myLink instanceof OpmConditionLink) {
			nLink = new OpdConditionLink(edge, pPoint,
					(OpmConditionLink) this.myLink, this.getKey().getOpdId(),
					this.getKey().getEntityInOpdId(), this.myProject);
		}

		if (this.myLink instanceof OpmInstrumentEventLink) {
			nLink = new OpdInstrumentEventLink(edge, pPoint,
					(OpmInstrumentEventLink) this.myLink, this.getKey()
							.getOpdId(), this.getKey().getEntityInOpdId(),
					this.myProject);
		}

		if (this.myLink instanceof OpmResultLink) {
			nLink = new OpdResultLink(edge, pPoint,
					(OpmResultLink) this.myLink, this.getKey().getOpdId(), this
							.getKey().getEntityInOpdId(), this.myProject);
		}

		if (this.myLink instanceof OpmConsumptionLink) {
			nLink = new OpdConsumptionLink(edge, pPoint,
					(OpmConsumptionLink) this.myLink, this.getKey().getOpdId(),
					this.getKey().getEntityInOpdId(), this.myProject);
		}

		if (this.myLink instanceof OpmEffectLink) {
			nLink = new OpdEffectLink(edge, pPoint,
					(OpmEffectLink) this.myLink, this.getKey().getOpdId(), this
							.getKey().getEntityInOpdId(), this.myProject);
		}

		if (this.myLink instanceof OpmExceptionLink) {
			nLink = new OpdExceptionLink(edge, pPoint,
					(OpmExceptionLink) this.myLink, this.getKey().getOpdId(),
					this.getKey().getEntityInOpdId(), this.myProject);
		}

		if (this.myLink instanceof OpmInvocationLink) {
			nLink = new OpdInvocationLink(edge, pPoint,
					(OpmInvocationLink) this.myLink, this.getKey().getOpdId(),
					this.getKey().getEntityInOpdId(), this.myProject);
		}

		if (this.myLink instanceof OpmConsumptionEventLink) {
			nLink = new OpdConsumptionEventLink(edge, pPoint,
					(OpmConsumptionEventLink) this.myLink, this.getKey()
							.getOpdId(), this.getKey().getEntityInOpdId(),
					this.myProject);
		}

		if (nLink == null) {
			JOptionPane
					.showMessageDialog(
							null,
							" Serious internal bug occured in AddLink function \n Please contact software developers.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		nLink.addMouseListener(nLink);
		nLink.addMouseMotionListener(nLink);

		return nLink;
	}

	/**
	 * Returns OpdConnectionEdge that represents the source graphically.
	 * 
	 */
	public OpdConnectionEdge getSource() {
		return this.source;
	}

	/**
	 * Returns OpdConnectionEdge that represents the destination graphically.
	 * 
	 */

	public OpdConnectionEdge getDestination() {
		return this.destination;
	}

	public OpmProceduralLink getLink() {
		return this.myLink;
	}

	public RelativeConnectionPoint getSourceConnectionPoint() {
		return this.sourceDragger.getRelativeConnectionPoint();
	}

	public void setSourceConnectionPoint(RelativeConnectionPoint point) {
		this.sourceDragger.setRelativeConnectionPoint(point);
	}

	public RelativeConnectionPoint getDestinationConnectionPoint() {
		return this.destinationDragger.getRelativeConnectionPoint();
	}

	public void setDestinationConnectionPoint(RelativeConnectionPoint point) {
		this.destinationDragger.setRelativeConnectionPoint(point);
	}

	public IXConnectionEdgeInstance getSourceIXInstance() {
		return (IXConnectionEdgeInstance) this.source.getInstance();
	}

	public IXConnectionEdgeInstance getDestinationIXInstance() {
		return (IXConnectionEdgeInstance) this.destination.getInstance();
	}

	public IConnectionEdgeInstance getSourceIInstance() {
		return (IConnectionEdgeInstance) this.source.getInstance();
	}

	public IConnectionEdgeInstance getDestinationIInstance() {
		return (IConnectionEdgeInstance) this.destination.getInstance();
	}

	public void animate(long time) {
		this.line.animate(time);
	}

	public void animate(long totalTime, long remainedTime) {
		this.line.animate(totalTime, remainedTime);
	}

	public long getRemainedTestingTime() {
		return this.line.getRemainedTestingTime();
	}

	public long getTotalTestingTime() {
		return this.line.getTotalTestingTime();
	}

	public void animateAsFlash() {
		this.line.animateAsFlash();
	}

	public boolean isAnimated() {
		return this.line.isAnimated();
	}

	public void stopTesting() {
		this.line.stopTesting();
	}

	public void pauseTesting() {
		this.line.pauseAnimaition();
	}

	public void continueTesting() {
		this.line.continueAnimaition();
	}

	public OrInstance getDestOr() {
		return this.destOr;
	}

	public void setDestOr(OrInstance destOr) {
		this.destOr = destOr;
	}

	public OrInstance getSourceOr() {
		return this.sourceOr;
	}

	public void setSourceOr(OrInstance sourceOr) {
		this.sourceOr = sourceOr;
	}

	/**
	 * Returns StyledLine connecting source and destination AroundDragger.
	 * 
	 */
	public StyledLine getLine() {
		return this.line;
	}

	/**
	 * Returns AroundDragger that connected to the source.
	 * 
	 */
	public AroundDragger getSourceDragger() {
		return this.sourceDragger;
	}

	/**
	 * Returns AroundDragger that connected to the destination.
	 * 
	 */
	public AroundDragger getDestinationDragger() {
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
		return this.line.getLineColor();
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
		this.line.setDashed(this.myLink.isEnviromental());
		this.line.setUpperText(this.myLink.getPath());
		this.line.setLowerText(myLink.getDescription());
		if (getEntry().isRoleIconSet()) {
			this.line.setIcon(getEntry().getRoleIcon());
		} else {
			this.line.setIcon(getEntry().getIcon());
		}

		this.line.repaint();
	}

	public void copyPropsFrom(LinkInstance origin) {
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
	}

	public void setSelected(boolean isSelected) {
		this.line.setSelected(isSelected);
		this.line.repaint();
		this.selected = isSelected;
	}

	public void setVisible(boolean isVisible) {
		this.line.setVisible(isVisible);
		this.sourceDragger.setVisible(isVisible);
		this.destinationDragger.setVisible(isVisible);
	}

	public boolean isVisible() {
		return this.sourceDragger.isVisible();
	}

	public void removeFromContainer() {
		if (this.destOr != null) {
			this.destOr.remove(this);
			this.destOr.update();
			this.destOr = null;
		}

		if (this.sourceOr != null) {
			this.sourceOr.remove(this);
			this.sourceOr.update();
			this.sourceOr = null;
		}

		this.line.removeFromContainer();
		this.source.removeUpdateListener(this);
		this.destination.removeUpdateListener(this);
		this.sourceDragger.removeUpdateListener(this);
		this.destinationDragger.removeUpdateListener(this);
	}

	public void add2Container(Container cn) {
		this.line.add2Container(cn);
		this.source.addUpdateListener(this);
		this.destination.addUpdateListener(this);
		this.sourceDragger.addUpdateListener(this);
		this.destinationDragger.addUpdateListener(this);
		this.updateOrConnections();
	}

	public long getLogicalId() {
		return this.myLink.getId();
	}

	public void updateMove(Object origin) {
		this.updateOrConnections();
	}

	public void updateRelease(Object origin) {
		// empty
	}

	public void updateOrConnections() {
		this.updateDestinationConnections();
		this.updateSourceConnections();
	}

	public void updateDestinationConnections() {
		Hashtable neighbors = this.findDestinationNeighbors();

		if (neighbors.size() > 0) {

			if (this.destOr == null) {
				for (Enumeration e = neighbors.elements(); e.hasMoreElements();) {
					this.destOr = ((LinkInstance) e.nextElement()).getDestOr();
				}

				if (this.destOr == null) {
					neighbors.put(this.getKey(), this);
					this.destOr = new OrInstance(neighbors, false,
							this.myProject);
					this.destOr.add2Container(this.sourceDragger.getParent());

					for (Enumeration e = neighbors.elements(); e
							.hasMoreElements();) {
						((LinkInstance) e.nextElement()).setDestOr(this.destOr);
					}
				}
			}

			this.destOr.add(this);
			this.destOr.update();
		} else {
			if (this.destOr == null) {
				return;
			}

			this.destOr.remove(this);
			this.destOr.update();
			this.destOr = null;
		}
	}

	public void updateSourceConnections() {
		Hashtable neighbors = this.findSourceNeighbors();

		if (neighbors.size() > 0) {
			if (this.sourceOr == null) {
				for (Enumeration e = neighbors.elements(); e.hasMoreElements();) {
					this.sourceOr = ((LinkInstance) e.nextElement())
							.getSourceOr();
				}

				if (this.sourceOr == null) {
					neighbors.put(this.getKey(), this);
					this.sourceOr = new OrInstance(neighbors, true,
							this.myProject);
					this.sourceOr.add2Container(this.sourceDragger.getParent());

					for (Enumeration e = neighbors.elements(); e
							.hasMoreElements();) {
						((LinkInstance) e.nextElement())
								.setSourceOr(this.sourceOr);
					}
				}
			} else {
				this.sourceOr.add(this);
				this.sourceOr.update();
			}
		} else {
			if (this.sourceOr == null) {
				return;
			}

			this.sourceOr.remove(this);
			this.sourceOr.update();
			this.sourceOr = null;
		}
	}

	private boolean couldBeORConencted(LinkEntry compare) {
		LinkEntry local = (LinkEntry) this.getEntry();

		if (local.getLinkType() == compare.getLinkType()) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.INVOCATION_LINK)
				|| (compare.getLinkType() == OpcatConstants.INVOCATION_LINK)) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.CONDITION_LINK)
				&& (compare.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK)) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.CONDITION_LINK)
				&& (compare.getLinkType() == OpcatConstants.CONSUMPTION_EVENT_LINK)) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.CONDITION_LINK)) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.CONSUMPTION_EVENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.CONDITION_LINK)) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.INSTRUMENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK)) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.INSTRUMENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.CONSUMPTION_EVENT_LINK)) {
			return true;
		}
		
		
		
		if ((local.getLinkType() == OpcatConstants.AGENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK)) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.AGENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.CONSUMPTION_EVENT_LINK)) {
			return true;
		}
		
		if ((local.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.AGENT_LINK)) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.CONSUMPTION_EVENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.AGENT_LINK)) {
			return true;
		}
		
		
		

		if ((local.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.INSTRUMENT_LINK)) {
			return true;
		}

		if ((local.getLinkType() == OpcatConstants.CONSUMPTION_EVENT_LINK)
				&& (compare.getLinkType() == OpcatConstants.INSTRUMENT_LINK)) {
			return true;
		}

		return false;
	}

	private boolean isDestinationNeighbour(LinkInstance li) {
		/**
		 * check if OR could be connected between the given links
		 */
		if (!couldBeORConencted((LinkEntry) li.getEntry())) {
			return false;
		}
		// if (((LinkEntry)this.getEntry()).getLinkType()!=
		// ((LinkEntry)li.getEntry()).getLinkType())
		// {
		// return false;
		// }
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		double epsilon = 8.0 * factor;

		if (li.getDestination() == this.destination) {
			RelativeConnectionPoint nPoint = li.getDestinationDragger()
					.getRelativeConnectionPoint();
			RelativeConnectionPoint myPoint = this.destinationDragger
					.getRelativeConnectionPoint();

			if (this.isClose(this.destination
					.getAbsoluteConnectionPoint(nPoint), this.destination
					.getAbsoluteConnectionPoint(myPoint), epsilon)) {
				return true;
			}
		}

		return false;

	}

	private boolean isClose(Point point1, Point point2, double epsilon) {
		double xDiff = point1.getX() - point2.getX();
		double yDiff = point1.getY() - point2.getY();
		return (xDiff * xDiff + yDiff * yDiff) < epsilon * epsilon;
	}

	private boolean isSourceNeighbour(LinkInstance li) {
		// if (((LinkEntry) this.getEntry()).getLinkType() != ((LinkEntry) li
		// .getEntry()).getLinkType()) {
		// return false;
		// }

		if (!couldBeORConencted((LinkEntry) li.getEntry())) {
			return false;
		}
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		double epsilon = 8.0 * factor;

		if (li.getSource() == this.source) {
			RelativeConnectionPoint nPoint = li.getSourceDragger()
					.getRelativeConnectionPoint();
			RelativeConnectionPoint myPoint = this.sourceDragger
					.getRelativeConnectionPoint();

			if (this.isClose(this.source.getAbsoluteConnectionPoint(nPoint),
					this.source.getAbsoluteConnectionPoint(myPoint), epsilon)) {
				return true;
			}
		}

		return false;

	}

	private Hashtable findDestinationNeighbors() {

		Hashtable neighbors = new Hashtable();

		ConnectionEdgeInstance dIns = (ConnectionEdgeInstance) this.destination
				.getEntry().getInstance(this.destination.getOpdKey());

		for (Enumeration e = dIns.getRelatedInstances(); e.hasMoreElements();) {
			Object tmp = e.nextElement();

			if ((tmp != this) && (tmp instanceof LinkInstance)) {
				LinkInstance li = (LinkInstance) tmp;
				if (this.isDestinationNeighbour(li)) {
					neighbors.put(li.getKey(), li);
				}
			}
		}

		return neighbors;
	}

	private Hashtable findSourceNeighbors() {

		Hashtable neighbors = new Hashtable();

		ConnectionEdgeInstance sIns = (ConnectionEdgeInstance) this.source
				.getEntry().getInstance(this.source.getOpdKey());

		for (Enumeration e = sIns.getRelatedInstances(); e.hasMoreElements();) {
			Object tmp = e.nextElement();

			if ((tmp != this) && (tmp instanceof LinkInstance)) {
				LinkInstance li = (LinkInstance) tmp;
				if (this.isSourceNeighbour(li)) {
					neighbors.put(li.getKey(), li);
				}
			}
		}

		return neighbors;
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

		if (yn) {
			this.updateOrConnections();
		}
	}

	public boolean isAutoArranged() {
		return this.autoArranged;
	}

	public void autoArrange() {
		// empty
	}

	public String toString() {
		return this.getEntry().getName();
	}

	public ConnectionEdgeInstance getSourceInstance() {
		return (ConnectionEdgeInstance) this.source.getEntry().getInstance(
				this.source.getOpdKey());
	}

	public ConnectionEdgeInstance getDestinationInstance() {
		return (ConnectionEdgeInstance) this.destination.getEntry()
				.getInstance(this.destination.getOpdKey());
	}

	/**
	 * Gets IXNode which is source of this IXNode.
	 */

	public IXNode getSourceIXNode() {
		return this.getSourceInstance();
	}

	/**
	 * Gets IXNode which is destination of this IXLine.
	 */
	public IXNode getDestinationIXNode() {
		return this.getDestinationInstance();
	}

	public BaseGraphicComponent[] getGraphicComponents() {
		return this.line.getPointsArray();
	}

	public Enumeration getOrXorSourceNeighbours(boolean isOr) {
		if ((this.sourceOr == null) || (this.sourceOr.isOr() == isOr)) {
			return null;
		}

		Vector neighbours = new Vector();
		for (Enumeration e = this.sourceOr.getInstances(); e.hasMoreElements();) {
			LinkInstance tIns = (LinkInstance) e.nextElement();
			if (!tIns.equals(this)) {
				neighbours.add(tIns);
			}
		}
		return neighbours.elements();
	}

	public Enumeration getOrXorDestinationNeighbours(boolean isOr) {
		if ((this.destOr == null) || (this.destOr.isOr() == isOr)) {
			return null;
		}

		Vector neighbours = new Vector();
		for (Enumeration e = this.destOr.getInstances(); e.hasMoreElements();) {
			LinkInstance tIns = (LinkInstance) e.nextElement();
			if (!tIns.equals(this)) {
				neighbours.add(tIns);
			}
		}
		return neighbours.elements();
	}

	public void setDestination(OpdConnectionEdge destination) {
		this.destination = destination;
	}

	public void setSource(OpdConnectionEdge source) {
		this.source = source;
	}

	public String getTypeString() {
		return "Link";
	}
}
