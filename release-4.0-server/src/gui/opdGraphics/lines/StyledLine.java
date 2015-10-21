package gui.opdGraphics.lines;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.dataProject.DataColors;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.DoublePoint;
import gui.opdGraphics.DraggedPoint;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.MoveUpdatable;
import gui.opdGraphics.draggers.AroundDragger;
import gui.opdGraphics.draggers.OpdRelationDragger;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkInstance;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.media.jai.PlanarImage;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class StyledLine implements MoveUpdatable {
	public static final int ORTHOGONAL = 1;

	public static final int BREAKABLE = 2;

	public static final int INVOCATION = 3;

	public static final double FRAMES_PER_SECOND = 12.0;

	private String upperText;

	private Vector points;

	private Vector lines;

	private int style;

	private OpmEntity myEntity;

	private OpdKey myKey;

	private OpdProject myProject;

	private OpdConnectionEdge source;

	private AroundDragger sourceDragger;

	private OpdConnectionEdge destination;

	private AroundDragger destinationDragger;

	private boolean autoArranged;

	private boolean selected;

	private javax.swing.Timer testingTimer;

	private boolean animated;

	private boolean animatedAsFlash;

	private boolean paused;

	private long startTime;

	private long totalTime;

	private long performedTime;

	private Color originalColor;

	private PlanarImage icon = null;

	private Container cn = null;

	private static final double LINE_SENSITIVITY = 5.0;

	private Color currentForColor = null;

	DataColors colors = new DataColors();

	public StyledLine(OpdConnectionEdge src, AroundDragger srcDragger,
			OpdConnectionEdge dest, AroundDragger destDragger,
			OpmEntity pEntity, OpdKey key, OpdProject pProject) {
		this.myProject = pProject;
		this.myKey = key;
		this.myEntity = pEntity;
		this.source = src;
		this.sourceDragger = srcDragger;
		this.destination = dest;
		this.destinationDragger = destDragger;

		this.points = new Vector();
		this.lines = new Vector();
		TextLine newLine = this.createTextLine(this.sourceDragger,
				this.destinationDragger);
		this.lines.add(newLine);

		this.sourceDragger.setLine(newLine);
		this.destinationDragger.setLine(newLine);

		this.setStyle(BREAKABLE);
		this.selected = false;
		// setStyle(ORTHOGONAL);
		// orthogonalArrange();

		this.animated = false;
		this.animatedAsFlash = false;
		this.paused = false;

		colors = new DataColors();
	}

	public void changeToMetaColor(int color) {

		if (!colors.isUsed())
			currentForColor = getLineColor();
		colors.setUsed(true);
		setLineColor(colors.getColor(color));
	}

	public void restoreFromMetaColor() {
		if ((currentForColor != null) && (colors.isUsed()))
			setLineColor(currentForColor);
		colors.setUsed(false);
		currentForColor = null;
	}

	public OpdConnectionEdge getSource() {
		return this.source;
	}

	public AroundDragger getSourceDragger() {
		return this.sourceDragger;
	}

	public OpdConnectionEdge getDestination() {
		return this.destination;
	}

	public AroundDragger getDestinationDragger() {
		return this.destinationDragger;
	}

	public Instance getMyInstance() {
		Entry myEntry = this.myProject.getComponentsStructure().getEntry(
				this.myEntity.getId());
		return myEntry.getInstance(this.myKey);
	}

	public boolean isStraight() {
		return (this.points.size() == 0);
	}

	public void makeStraight() {
		if (this.style != INVOCATION) {
			this.setNumOfPoints(0);
		}

		// updateOrConnections
		if (this.getMyInstance() instanceof LinkInstance) {
			((LinkInstance) this.getMyInstance()).updateMove(this);
		}

	}

	public void copyShapeFrom(StyledLine origin) {
		this.setStyle(origin.getStyle());

		if (this.style != BREAKABLE) {
			return;
		}

		Container originCn = origin.cn;
		Vector originPoints = origin.getPointsVector();
		this.setNumOfPoints(originPoints.size());

		for (int i = 0; i < originPoints.size(); i++) {
			DraggedPoint myPoint = (DraggedPoint) this.points.get(i);
			DraggedPoint originPoint = (DraggedPoint) originPoints.get(i);
			Point location = SwingUtilities.convertPoint(originCn, originPoint
					.getX(), originPoint.getY(), this.cn);
			myPoint.setLocation((int) location.getX(), (int) location.getY());
		}

	}

	public void addPoint(Point newPoint) {
		this.breakLastLine(newPoint);
	}

	public void setSelected(boolean isSelected) {
		this.selected = isSelected;
		this.sourceDragger.setSelected(this.selected);
		this.sourceDragger.repaint();
		this.destinationDragger.setSelected(this.selected);
		this.destinationDragger.repaint();

		for (int i = 0; i < this.points.size(); i++) {
			DraggedPoint tPoint = (DraggedPoint) this.points.get(i);
			tPoint.setSelected(this.selected);
			tPoint.repaint();
		}
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setAutoArranged(boolean autoArranged) {
		if (this.cn == null) {
			return;
		}

		if (autoArranged && !this.autoArranged) {
			this.arrange();
		}

		this.autoArranged = autoArranged;

		// sourceDragger.setMoveable(!autoArranged);
		// destinationDragger.setMoveable(!autoArranged);
		//
		// if (autoArranged)
		// {
		// ((JLayeredPane)cn).moveToBack(sourceDragger);
		// ((JLayeredPane)cn).moveToBack(destinationDragger);
		//
		// arrange();
		// }
		// else
		// {
		// ((JLayeredPane)cn).moveToFront(sourceDragger);
		// ((JLayeredPane)cn).moveToFront(destinationDragger);
		// }
	}

	public Vector getPointsVector() {
		return this.points;
	}

	public OpmEntity getEntity() {
		return this.myEntity;
	}

	public OpdKey getKey() {
		return this.myKey;
	}

	public boolean isAutoArranged() {
		return this.autoArranged;
	}

	public void arrange() {
		if ((this.style == BREAKABLE) || (this.style == INVOCATION)) {
			RelativeConnectionPoint sourcePoint;
			RelativeConnectionPoint destinationPoint;

			if (this.points.isEmpty()) {
				sourcePoint = GraphicsUtils.calculateConnectionPoint(
						this.source, this.destination);
				destinationPoint = GraphicsUtils.calculateConnectionPoint(
						this.destination, this.source);
			} else {
				sourcePoint = GraphicsUtils.calculateConnectionPoint(
						this.source, (DraggedPoint) this.points.get(0));
				destinationPoint = GraphicsUtils.calculateConnectionPoint(
						this.destination, (DraggedPoint) this.points
								.get(this.points.size() - 1));
			}

			this.sourceDragger.setRelativeConnectionPoint(sourcePoint);
			this.sourceDragger.updateDragger();
			this.destinationDragger
					.setRelativeConnectionPoint(destinationPoint);
			this.destinationDragger.updateDragger();

		}
	}

	public void setStyle(int style) {
		if (this.style == style) {
			return;
		}

		this.style = style;

		if (style == ORTHOGONAL) {
			for (int i = 0; i < this.lines.size(); i++) {
				TextLine tLine = (TextLine) this.lines.get(i);
				tLine.removeMouseMotionListener(tLine);
			}

			return;
		}

		if (style == BREAKABLE) {
			for (int i = 0; i < this.lines.size(); i++) {
				TextLine tLine = (TextLine) this.lines.get(i);
				tLine.addMouseMotionListener(tLine);
			}

			return;
		}

		if (style == INVOCATION) {
			this.points.clear();
			this.lines.clear();
			OpdInvocationLine newLine = this.createInvocationLine(
					this.sourceDragger, this.destinationDragger);
			this.lines.add(newLine);
			this.sourceDragger.setLine(newLine);
			this.destinationDragger.setLine(newLine);

		}

		return;
	}

	public int getStyle() {
		return this.style;
	}

	public void add2Container(Container cn) {
		if (this.cn != null) {
			System.err
					.println("Can't add one styled line to more than one container");
			return;
		}

		this.cn = cn;

		this.source.addUpdateListener(this);
		this.destination.addUpdateListener(this);

		for (int i = 0; i < this.points.size(); i++) {
			cn.add((DraggedPoint) this.points.get(i),
					JLayeredPane.PALETTE_LAYER);
		}

		for (int i = 0; i < this.lines.size(); i++) {
			cn.add((OpdLine) this.lines.get(i), new Integer(
					JLayeredPane.PALETTE_LAYER.intValue() - 1));
		}

		this.sourceDragger.addUpdateListener(this);
		this.destinationDragger.addUpdateListener(this);

		cn.add(this.sourceDragger, JLayeredPane.PALETTE_LAYER);
		cn.add(this.destinationDragger, JLayeredPane.PALETTE_LAYER);

		if (this.sourceDragger instanceof OpdRelationDragger) {
			cn.add(((OpdRelationDragger) this.sourceDragger)
					.getOpdCardinalityLabel(), JLayeredPane.MODAL_LAYER);
		}

		if (this.destinationDragger instanceof OpdRelationDragger) {
			cn.add(((OpdRelationDragger) this.destinationDragger)
					.getOpdCardinalityLabel(), JLayeredPane.MODAL_LAYER);
		}

		this.source.repaintLines();
		this.destination.repaintLines();

	}

	public void removeFromContainer() {
		this.cn.remove(this.sourceDragger);

		if (this.sourceDragger instanceof OpdRelationDragger) {
			this.cn.remove(((OpdRelationDragger) this.sourceDragger)
					.getOpdCardinalityLabel());
		}

		this.cn.remove(this.destinationDragger);

		if (this.destinationDragger instanceof OpdRelationDragger) {
			this.cn.remove(((OpdRelationDragger) this.destinationDragger)
					.getOpdCardinalityLabel());
		}
		for (int i = 0; i < this.lines.size(); i++) {
			this.cn.remove((OpdLine) this.lines.get(i));
		}

		for (int i = 0; i < this.points.size(); i++) {
			this.cn.remove((DraggedPoint) this.points.get(i));
		}

		this.sourceDragger.removeUpdateListener(this);
		this.destinationDragger.removeUpdateListener(this);

		this.source.removeUpdateListener(this);
		this.destination.removeUpdateListener(this);
		this.cn.repaint();

		this.cn = null;
	}

	private OpdInvocationLine createInvocationLine(Connectable source,
			Connectable destination) {
		OpdInvocationLine gLine;
		RelativeConnectionPoint centerPoint = new RelativeConnectionPoint(
				BaseGraphicComponent.CENTER, 0);

		gLine = new OpdInvocationLine(source, centerPoint,
				this.destinationDragger, centerPoint, this.myEntity,
				this.myKey, this.myProject);

		// gLine.setParentLine(this);
		gLine.addMouseListener(gLine);
		return gLine;
	}

	private TextLine createTextLine(Connectable source, Connectable destination) {
		TextLine gLine;
		RelativeConnectionPoint centerPoint = new RelativeConnectionPoint(
				BaseGraphicComponent.CENTER, 0);

		gLine = new TextLine(source, centerPoint, destination, centerPoint,
				this.myEntity, this.myKey, this.myProject);

		gLine.setParentLine(this);
		gLine.addMouseListener(gLine);
		if (this.style == BREAKABLE) {
			gLine.addMouseMotionListener(gLine);
		}
		return gLine;
	}

	public void breakLine(OpdLine brLine, Point location) {

		int index = this.lines.indexOf(brLine);
		if (index == -1) {
			return;
		}

		if (index == this.lines.size() - 1) {
			this.breakLastLine(location);
			this.removeRedundantLines();

			if ((this.style == BREAKABLE) && this.isAutoArranged()) {
				this.arrange();
			}

			// updateOrConnections
			if (this.getMyInstance() instanceof LinkInstance) {
				((LinkInstance) this.getMyInstance()).updateMove(this);
			}

			return;
		}

		DraggedPoint oldPoint = (DraggedPoint) this.points.get(index);
		Container cn = brLine.getParent();

		DraggedPoint newPoint = new DraggedPoint(this.myProject);
		newPoint.addUpdateListener(this);
		cn.add(newPoint, JLayeredPane.PALETTE_LAYER);
		newPoint.setLocation(location);

		newPoint.setSelected(this.selected);
		newPoint.addMouseListener(newPoint);
		newPoint.addMouseMotionListener(newPoint);
		brLine.setEdge2(newPoint);

		OpdLine newLine = this.createTextLine(newPoint, oldPoint);
		cn.add(newLine, new Integer(JLayeredPane.PALETTE_LAYER.intValue() - 1));
		newLine.setTextColor(this.getTextColor());
		newLine.setLineColor(this.getLineColor());

		oldPoint.removeLine(brLine);
		oldPoint.addLine(newLine);

		newPoint.addLine(newLine);
		newPoint.addLine(brLine);

		newLine.update();
		brLine.update();
		this.lines.add(index + 1, newLine);
		this.points.add(index, newPoint);

		this.removeRedundantLines();

		// updateOrConnections
		if (this.getMyInstance() instanceof LinkInstance) {
			((LinkInstance) this.getMyInstance()).updateMove(this);
		}

		if ((this.style == BREAKABLE) && this.isAutoArranged()) {
			this.arrange();
		}

	}

	private void breakLastLine(Point location) {

		OpdLine brLine = (OpdLine) this.lines.get(this.lines.size() - 1);
		Container cn = brLine.getParent();

		DraggedPoint newPoint = new DraggedPoint(this.myProject);

		if (cn != null) {
			cn.add(newPoint, JLayeredPane.PALETTE_LAYER);
		}
		newPoint.setLocation(location);

		newPoint.addMouseListener(newPoint);
		newPoint.addMouseMotionListener(newPoint);
		newPoint.addUpdateListener(this);
		newPoint.setSelected(this.selected);

		brLine.setEdge2(newPoint);

		OpdLine newLine = this
				.createTextLine(newPoint, this.destinationDragger);

		if (cn != null) {
			cn.add(newLine, new Integer(
					JLayeredPane.PALETTE_LAYER.intValue() - 1));
		}

		newLine.setTextColor(this.getTextColor());
		newLine.setLineColor(this.getLineColor());

		this.destinationDragger.setLine(newLine);

		newPoint.addLine(newLine);
		newPoint.addLine(brLine);

		newLine.update();
		brLine.update();
		this.lines.add(newLine);
		this.points.add(newPoint);
	}

	public synchronized void updateMove(Object origin) {
		if (origin instanceof OpdConnectionEdge) {
			if ((this.getStyle() == BREAKABLE)
					|| (this.getStyle() == INVOCATION)) {
				if (this.isAutoArranged()) {
					this.arrange();
				} else {
					this.sourceDragger.updateDragger();
					this.destinationDragger.updateDragger();
				}

				return;
			}

			return;

		}

		if (origin instanceof DraggedPoint) {
			int index = this.points.indexOf(origin);
			if (index == -1) {
				return;
			}

			if (this.getStyle() == BREAKABLE) {
				((TextLine) this.lines.get(index)).update();
				((TextLine) this.lines.get(index + 1)).update();

				if (this.isAutoArranged()) {
					this.arrange();
				}
			}

			if (this.getStyle() == ORTHOGONAL) {
				if (index == 0) {
					this.updateDragger4Point(this.sourceDragger);
				} else {
					this.updatePoint(index - 1, index);
				}

				if (index == this.points.size() - 1) {
					this.updateDragger4Point(this.destinationDragger);
				} else {
					this.updatePoint(index + 1, index);
				}

			}

			if (this.getMyInstance() instanceof LinkInstance) {
				((LinkInstance) this.getMyInstance()).updateMove(this);
			}

			return;
		}

		if (origin instanceof AroundDragger) {
			if (this.getStyle() == ORTHOGONAL) {
				this.updatePoint4Dragger((AroundDragger) origin);
				return;
			}
		}
	}

	private void updatePoint4Dragger(AroundDragger dragger) {
		DraggedPoint tPoint;
		TextLine tLine;

		if (dragger == this.sourceDragger) {
			tPoint = (DraggedPoint) this.points.get(0);
			tLine = (TextLine) this.lines.get(0);
		} else {
			tPoint = (DraggedPoint) this.points.get(this.points.size() - 1);
			tLine = (TextLine) this.lines.get(this.lines.size() - 1);
		}

		if (tLine.isVertical()) {
			tPoint.setLocation(dragger.getX() + dragger.getWidth() / 2
					- tPoint.getWidth() / 2, tPoint.getY());
		} else {

			tPoint.setLocation(tPoint.getX(), dragger.getY()
					+ dragger.getHeight() / 2 - tPoint.getHeight() / 2);
		}

	}

	private void updateDragger4Point(AroundDragger dragger) {
		DraggedPoint tPoint;
		TextLine tLine;

		if (dragger == this.sourceDragger) {
			tPoint = (DraggedPoint) this.points.get(0);
			tLine = (TextLine) this.lines.get(0);
		} else {
			tPoint = (DraggedPoint) this.points.get(this.points.size() - 1);
			tLine = (TextLine) this.lines.get(this.lines.size() - 1);
		}

		Connectable edge = dragger.getEdge();

		double param;
		if (tLine.isVertical()) {
			param = (double) (tPoint.getX() + tPoint.getWidth() / 2 - edge
					.getX())
					/ (double) edge.getActualWidth();
		} else {
			param = (double) (tPoint.getY() + tPoint.getHeight() / 2 - edge
					.getY())
					/ (double) edge.getActualHeight();
		}

		dragger.setParam(param);
		dragger.updateDragger();
	}

	private void updatePoint(int pointToChangeIndex, int originIndex) {
		TextLine tLine = (TextLine) this.lines.get(Math.min(pointToChangeIndex,
				pointToChangeIndex));
		DraggedPoint tPoint = (DraggedPoint) this.points
				.get(pointToChangeIndex);
		DraggedPoint origPoint = (DraggedPoint) this.points.get(originIndex);

		if (tLine.isVertical()) {
			tPoint.setLocation(origPoint.getX(), tPoint.getY());
		} else {
			tPoint.setLocation(tPoint.getX(), origPoint.getY());
		}
	}

	public void updateRelease(Object origin) {
		this.removeRedundantLines();
		if (this.style != INVOCATION) {
			this.setUpperText(this.upperText);
			this.setIcon(icon);
		}
	}

	public void removeRedundantLines() {
		for (int i = 0; i < this.points.size(); i++) {
			DraggedPoint tPoint = (DraggedPoint) this.points.get(i);
			if (this.onLine(tPoint)) {
				this.removePoint(tPoint);
			}
		}
	}

	public void removePoint(int index) {
		if ((index < 0) || (index >= this.points.size())) {
			return;
		}

		DraggedPoint removedPoint = (DraggedPoint) this.points.get(index);
		TextLine removedLine = (TextLine) this.lines.get(index + 1);

		TextLine tLine = (TextLine) this.lines.get(index);

		if (index == this.points.size() - 1) {
			this.destinationDragger.setLine((TextLine) this.lines.get(index));
			tLine.setEdge2(this.destinationDragger);
		} else {
			DraggedPoint tPoint = (DraggedPoint) this.points.get(index + 1);
			tLine.setEdge2(tPoint);
		}

		this.cn.remove(removedPoint);
		this.cn.remove(removedLine);

		tLine.update();

		this.points.remove(index);
		this.lines.remove(index + 1);

	}

	public void removePoint(DraggedPoint point) {
		this.removePoint(this.points.indexOf(point));
	}

	private boolean onLine(DraggedPoint point) {
		int index = this.points.indexOf(point);
		if (index == -1) {
			return false;
		}

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();

		int lineSensitivity = (int) Math.round(LINE_SENSITIVITY
				* (currentSize / normalSize));

		DoublePoint myPoint = new DoublePoint(point.getX() + point.getWidth()
				/ 2, point.getY() + point.getHeight() / 2);
		DoublePoint src, dest;
		DraggedPoint tPoint;
		if (index == 0) {
			src = new DoublePoint(this.sourceDragger.getX()
					+ this.sourceDragger.getWidth() / 2, this.sourceDragger
					.getY()
					+ this.sourceDragger.getHeight() / 2);
		} else {
			tPoint = (DraggedPoint) this.points.get(index - 1);
			src = new DoublePoint(tPoint.getX() + tPoint.getWidth() / 2, tPoint
					.getY()
					+ tPoint.getHeight() / 2);
		}

		if (index == this.points.size() - 1) {
			dest = new DoublePoint(this.destinationDragger.getX()
					+ this.destinationDragger.getWidth() / 2,
					this.destinationDragger.getY()
							+ this.destinationDragger.getHeight() / 2);
		} else {
			tPoint = (DraggedPoint) this.points.get(index + 1);
			dest = new DoublePoint(tPoint.getX() + tPoint.getWidth() / 2,
					tPoint.getY() + tPoint.getHeight() / 2);
		}

		if (src.getY() > dest.getY()) {
			DoublePoint temp = src;
			src = dest;
			dest = temp;
		}

		double deltaX = src.getX() - dest.getX();
		if (deltaX == 0.0) {
			deltaX = 0.0001;
		}

		double angle = Math.atan((src.getY() - dest.getY()) / deltaX);

		if (angle < 0) {
			angle += Math.PI;
		}
		myPoint.rotate(-angle, src);
		dest.rotate(-angle, src);
		if ((myPoint.getX() >= src.getX() - lineSensitivity)
				&& (myPoint.getX() <= dest.getX() + lineSensitivity)
				&& (myPoint.getY() <= src.getY() + lineSensitivity)
				&& (myPoint.getY() >= src.getY() - lineSensitivity)) {
			// System.err.println("Contains "+this);
			return true;
		}
		return false;

	}

	public void setLineColor(Color clr) {
		for (int i = 0; i < this.lines.size(); i++) {
			((OpdLine) this.lines.get(i)).setLineColor(clr);
		}

		this.sourceDragger.setBorderColor(clr);
		this.destinationDragger.setBorderColor(clr);
	}

	public Color getLineColor() {
		return ((OpdLine) this.lines.get(0)).getLineColor();
	}

	public void setTextColor(Color clr) {
		for (int i = 0; i < this.lines.size(); i++) {
			((OpdLine) this.lines.get(i)).setTextColor(clr);
		}

		if (this.sourceDragger instanceof OpdRelationDragger) {
			((OpdRelationDragger) this.sourceDragger).getOpdCardinalityLabel()
					.setTextColor(clr);
		}

		if (this.destinationDragger instanceof OpdRelationDragger) {
			((OpdRelationDragger) this.destinationDragger)
					.getOpdCardinalityLabel().setTextColor(clr);
		}
	}

	public Color getTextColor() {
		return ((OpdLine) this.lines.get(0)).getTextColor();
	}

	public void setDashed(boolean isDashed) {
		for (int i = 0; i < this.lines.size(); i++) {
			((OpdLine) this.lines.get(i)).setDashed(isDashed);
		}

	}

	public void setUpperText(String text) {
		if (this.style == INVOCATION) {
			return;
		}

		this.upperText = text;
		for (int i = 0; i < this.lines.size(); i++) {
			TextLine line = (TextLine) this.lines.get(i);
			line.setUpperText("");
		}

		TextLine line = (TextLine) this.lines.get(this.getLongestLine());
		line.setUpperText(text);
		this.repaint();
	}

	public void setIcon(PlanarImage icon) {
		this.icon = icon;
		OpdLine line = (OpdLine) this.lines.get(this.getLongestLine());
		line.setIcon(icon);
		this.repaint();
	}

	public void setLowerText(String text) {
		if (this.style == INVOCATION) {
			return;
		}

		for (int i = 0; i < this.lines.size(); i++) {
			TextLine line = (TextLine) this.lines.get(i);
			line.setLowerText("");
		}

		TextLine line = (TextLine) this.lines.get(this.getLongestLine());
		line.setLowerText(text);
		this.repaint();

	}

	public void repaint() {
		for (int i = 0; i < this.lines.size(); i++) {
			((OpdLine) this.lines.get(i)).repaint();
		}

		this.sourceDragger.repaint();
		this.destinationDragger.repaint();
	}

	public void callPropertiesDialog() {
		((OpdLine) this.lines.get(0)).callPropertiesDialog();
	}

	private int getLongestLine() {
		int index = 0;
		OpdLine line = (OpdLine) this.lines.get(0);
		double length = Math.sqrt(line.getWidth() * line.getWidth()
				+ line.getHeight() * line.getHeight());

		for (int i = 1; i < this.lines.size(); i++) {
			line = (TextLine) this.lines.get(i);
			double tLength = Math.sqrt(line.getWidth() * line.getWidth()
					+ line.getHeight() * line.getHeight());
			if (tLength > length) {
				index = i;
				length = tLength;
			}
		}

		return index;
	}

	private int getTotalLength() {
		double totalLength = 0;
		for (int i = 0; i < this.lines.size(); i++) {
			TextLine line = (TextLine) this.lines.get(i);
			totalLength += Math.sqrt(line.getWidth() * line.getWidth()
					+ line.getHeight() * line.getHeight());
		}

		return (int) totalLength;
	}

	private void setNumOfPoints(int num) {
		if (num == this.points.size()) {
			return;
		}

		if (num < this.points.size()) {
			for (int i = this.points.size() - 1; i > num - 1; i--) {
				this.removePoint(i);
			}

			return;
		}

		for (int i = this.points.size() - 1; i < num - 1; i++) {
			TextLine lastLine = (TextLine) this.lines
					.get(this.lines.size() - 1);
			this.breakLastLine(new Point(lastLine.getX() + lastLine.getWidth()
					/ 2, lastLine.getY() + lastLine.getHeight() / 2));
		}
	}

	// private void orthogonalArrange()
	// {
	// GenericTable config = myProject.getConfiguration();
	// double currentSize =
	// ((Integer)config.getProperty("CurrentSize")).doubleValue();
	// double normalSize =
	// ((Integer)config.getProperty("NormalSize")).doubleValue();
	// double factor = currentSize/normalSize;
	//
	// double pointSize = (8.0/2.0)*factor;
	//
	// setNumOfPoints(1);
	//
	// int x = (int)Math.round(sourceDragger.getX()+
	// (double)sourceDragger.getWidth()/2-pointSize);
	// int y =
	// (int)Math.round(destinationDragger.getY()+(double)destinationDragger.getHeight()/2-pointSize);
	//
	// ((DraggedPoint)points.get(0)).setLocation(x,y);
	// ((TextLine)lines.get(0)).setVertical(true);
	// ((TextLine)lines.get(1)).setVertical(false);
	// }

	// private void orthogonalArrange()
	// {
	// Configuration config = myProject.getConfiguration();
	// double currentSize =
	// ((Integer)config.getProperty("CurrentSize")).doubleValue();
	// double normalSize =
	// ((Integer)config.getProperty("NormalSize")).doubleValue();
	// double factor = currentSize/normalSize;
	//
	// int pointSize = (int)Math.round((8.0/2.0)*factor);
	// int bypassDistance = BYPASS_DISTANCE*factor;
	//
	// int sX = source.getX() + source.getWidth()/2 - pointSize;
	// int sY = source.getY() + source.getHeight()/2 - pointSize;
	// int dX = destination.getX() + destination.getWidth()/2 - pointSize;
	// int dY = destination.getY() + destination.getHeight()/2 - pointSize;
	//
	// int draggerSide = getSide(source);
	//
	// Connectable sEdge = source.getEdge();
	//
	//
	// if (dX < sX && dY > sY) //1
	// {
	// if (draggerSide == BaseGraphicComponent.N_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX, sY-BYPASS_DISTANCE);
	// points[1].setAbsolutesLocation(dX, sY-BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(dX, sY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, sY-BYPASS_DISTANCE);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.E_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX+BYPASS_DISTANCE, sY);
	// points[1].setAbsolutesLocation(sX+BYPASS_DISTANCE, sY+(dY-sY)/2);
	// points[2].setAbsolutesLocation(sX+BYPASS_DISTANCE, sY+(dY-sY)/2);
	// points[3].setAbsolutesLocation(dX, sY+(dY-sY)/2);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.S_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX, sY+(dY-sY)/2);
	// points[1].setAbsolutesLocation(sX, sY+(dY-sY)/2);
	// points[2].setAbsolutesLocation(sX, sY+(dY-sY)/2);
	// points[3].setAbsolutesLocation(dX, sY+(dY-sY)/2);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.W_BORDER)
	// {
	// points[0].setAbsolutesLocation(dX, sY);
	// points[1].setAbsolutesLocation(dX, sY);
	// points[2].setAbsolutesLocation(dX, sY);
	// points[3].setAbsolutesLocation(dX, sY);
	// return;
	// }
	// }
	//
	// if (dX > sX && dY > sY) //2
	// {
	// if (draggerSide == BaseGraphicComponent.N_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX, sY-BYPASS_DISTANCE);
	// points[1].setAbsolutesLocation(dX, sY-BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(dX, sY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, sY-BYPASS_DISTANCE);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.E_BORDER)
	// {
	// points[0].setAbsolutesLocation(dX, sY);
	// points[1].setAbsolutesLocation(dX, sY);
	// points[2].setAbsolutesLocation(dX, sY);
	// points[3].setAbsolutesLocation(dX, sY);
	// return;
	//
	// }
	//
	// if (draggerSide == BaseGraphicComponent.S_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX, sY+(dY-sY)/2);
	// points[1].setAbsolutesLocation(sX, sY+(dY-sY)/2);
	// points[2].setAbsolutesLocation(sX, sY+(dY-sY)/2);
	// points[3].setAbsolutesLocation(dX, sY+(dY-sY)/2);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.W_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX-BYPASS_DISTANCE, sY);
	// points[1].setAbsolutesLocation(sX-BYPASS_DISTANCE, sY+(dY-sY)/2);
	// points[2].setAbsolutesLocation(sX-BYPASS_DISTANCE, sY+(dY-sY)/2);
	// points[3].setAbsolutesLocation(dX, sY+(dY-sY)/2);
	// return;
	// }
	// }
	//
	//
	// if (dX > sX && dY < sY) //3
	// {
	// if (draggerSide == BaseGraphicComponent.N_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX, dY-BYPASS_DISTANCE);
	// points[1].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.E_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX+(dX-sX)/2, sY);
	// points[1].setAbsolutesLocation(sX+(dX-sX)/2, dY-BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(sX+(dX-sX)/2, dY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.S_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX, sY+BYPASS_DISTANCE);
	// points[1].setAbsolutesLocation(sX+(dX-sX)/2, sY+BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(sX+(dX-sX)/2, dY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.W_BORDER)
	// {
	//
	// points[0].setAbsolutesLocation(sX-BYPASS_DISTANCE, sY);
	// points[1].setAbsolutesLocation(sX-BYPASS_DISTANCE, dY-BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(sX-BYPASS_DISTANCE, dY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// return;
	//
	// }
	// }
	//
	//
	// if (dX < sX && dY < sY) //4
	// {
	// if (draggerSide == BaseGraphicComponent.N_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX, dY-BYPASS_DISTANCE);
	// points[1].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.E_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX+BYPASS_DISTANCE, sY);
	// points[1].setAbsolutesLocation(sX+BYPASS_DISTANCE, dY-BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(sX+BYPASS_DISTANCE, dY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.S_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX, sY+BYPASS_DISTANCE);
	// points[1].setAbsolutesLocation(sX+(dX-sX)/2, sY+BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(sX+(dX-sX)/2, dY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, dY-BYPASS_DISTANCE);
	// return;
	// }
	//
	// if (draggerSide == BaseGraphicComponent.W_BORDER)
	// {
	// points[0].setAbsolutesLocation(sX+(dX-sX)/2, sY);
	// points[1].setAbsolutesLocation(sX+(dX-sX)/2, dY-BYPASS_DISTANCE);
	// points[2].setAbsolutesLocation(sX+(dX-sX)/2, dY-BYPASS_DISTANCE);
	// points[3].setAbsolutesLocation(dX, dY - BYPASS_DISTANCE);
	// return;
	// }
	// }
	// }

	// private int getSide(AroundDragger dragger)
	// {
	// if (dragger.getEdge() instanceof OpdProcess)
	// {
	// double param = dragger.getParam();
	//
	// if (param <= 0.2)
	// {
	// return OpcatConstants.W_BORDER;
	// }
	//
	// if (param >= 0.8)
	// {
	// return OpcatConstants.E_BORDER;
	// }
	// }
	//
	// return dragger.getSide();
	// }

	public void setVisible(boolean isVisible) {
		for (int i = 0; i < this.lines.size(); i++) {
			((OpdLine) this.lines.elementAt(i)).setVisible(isVisible);
		}
	}

	public BaseGraphicComponent[] getPointsArray() {
		BaseGraphicComponent[] bgcs = new BaseGraphicComponent[this.points
				.size()];
		for (int i = 0; i < this.points.size(); i++) {
			bgcs[i] = (BaseGraphicComponent) this.points.get(i);
		}
		return bgcs;
	}

	public synchronized void animateAsFlash() {
		if (this.animated) {
			return;
		}

		this.animated = true;
		this.animatedAsFlash = true;

		this.originalColor = this.getLineColor();

		this.setLineColor(Color.red);
		this.repaint();
		this.testingTimer = new javax.swing.Timer(500, this.stopFlash);
		this.testingTimer.setInitialDelay(500);
		this.testingTimer.setRepeats(false);

		this.testingTimer.start();

		// try
		// {
		// Thread.currentThread().sleep(350);
		// }
		// catch (Exception e){}
		// setLineColor(originalColor);
	}

	public synchronized boolean isAnimated() {
		return this.animated;
	}

	public synchronized void pauseAnimaition() {
		if (!this.animated || this.paused) {
			return;
		}

		this.paused = true;
		this.testingTimer.stop();
		this.performedTime = (new GregorianCalendar()).getTime().getTime()
				- this.startTime;
	}

	public synchronized void continueAnimaition() {
		if (!this.animated || !this.paused) {
			return;
		}

		this.startTime = (new GregorianCalendar()).getTime().getTime()
				- this.performedTime;
		this.testingTimer.start();
		this.paused = false;
	}

	public synchronized void animate(long time) {
		if (this.animated) {
			if (time == 0) {
				this.totalTime = 0;
			}
			return;
		}

		if (time < (long) (1000 / FRAMES_PER_SECOND)) {
			return;
		}

		this.animated = true;

		if (this.style == INVOCATION) {
			((OpdInvocationLine) this.lines.get(0)).setAnimated(true);
			;
			((OpdInvocationLine) this.lines.get(0)).repaint();
			this.testingTimer = new javax.swing.Timer((int) time,
					this.stTesting);
			this.testingTimer.setInitialDelay((int) time);
			this.testingTimer.setRepeats(false);
			this.testingTimer.start();
			return;
		}

		this.testingTimer = new javax.swing.Timer(
				(int) (1000 / FRAMES_PER_SECOND), this.updateTesting);
		this.startTime = (new GregorianCalendar()).getTime().getTime();
		this.totalTime = time;
		this.testingTimer.start();
	}

	public synchronized void animate(long testingTime, long remainedTime) {
		if (this.animated) {
			if (testingTime == 0) {
				this.totalTime = 0;
			}
			return;
		}

		if (testingTime < (long) (1000 / FRAMES_PER_SECOND)) {
			return;
		}

		this.animated = true;

		if (this.style == INVOCATION) {
			((OpdInvocationLine) this.lines.get(0)).setAnimated(true);
			;
			((OpdInvocationLine) this.lines.get(0)).repaint();
			this.testingTimer = new javax.swing.Timer((int) remainedTime,
					this.stTesting);
			this.testingTimer.setInitialDelay((int) remainedTime);
			this.testingTimer.setRepeats(false);
			this.testingTimer.start();
			return;
		}

		this.testingTimer = new javax.swing.Timer(
				(int) (1000 / FRAMES_PER_SECOND), this.updateTesting);
		this.totalTime = testingTime;
		this.startTime = (new GregorianCalendar()).getTime().getTime()
				- (this.totalTime - remainedTime);
		this.testingTimer.start();
	}

	public synchronized long getRemainedTestingTime() {
		if (!this.animated || this.animatedAsFlash) {
			return 0;
		}

		if (this.paused) {
			return this.totalTime - this.performedTime;
		}

		return this.totalTime + this.startTime
				- (new GregorianCalendar()).getTime().getTime();
	}

	public synchronized long getTotalTestingTime() {
		if (!this.animated || this.animatedAsFlash) {
			return 0;
		}

		return this.totalTime;
	}

	public synchronized void stopTesting() {
		if (!this.animated) {
			return;
		}

		if (this.paused) {
			this.paused = false;
		}

		if (this.animatedAsFlash) {
			this.setLineColor(this.originalColor);
			this.repaint();
			this.animated = false;
			this.animatedAsFlash = false;
		} else {
			this._stpTesting();
		}
	}

	private void _stpTesting() {
		if (!this.animated) {
			return;
		}

		if (this.paused) {
			this.paused = false;
		}

		this.testingTimer.stop();

		if (this.style == INVOCATION) {
			((OpdInvocationLine) this.lines.get(0)).setAnimated(false);
			((OpdInvocationLine) this.lines.get(0)).repaint();
			this.animated = false;
			return;
		}

		for (int i = 0; i < this.lines.size(); i++) {
			TextLine line = (TextLine) this.lines.get(i);
			if (line.isAnimated()) {
				line.setAnimated(false);
				line.repaint();
			}
		}

		this.animated = false;

	}

	Action updateTesting = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public synchronized void actionPerformed(java.awt.event.ActionEvent e) {
			long timeLeft = StyledLine.this.totalTime
					+ StyledLine.this.startTime
					- (new GregorianCalendar()).getTime().getTime();

			if (timeLeft <= 0) {
				StyledLine.this._stpTesting();
				return;
			}

			double totalLength = StyledLine.this.getTotalLength();
			double currLength = totalLength
					* (StyledLine.this.totalTime - timeLeft)
					/ StyledLine.this.totalTime;

			// timeLeft = timeLeft - testingTimer.getDelay();
			TextLine tl = (TextLine) StyledLine.this.lines.get(0);
			double aParam = 0.0;

			double cLength = 0;
			for (int i = 0; i < StyledLine.this.lines.size(); i++) {
				TextLine line = (TextLine) StyledLine.this.lines.get(i);
				double myLength = Math.sqrt(line.getWidth() * line.getWidth()
						+ line.getHeight() * line.getHeight());
				cLength += myLength;
				if (currLength <= cLength) {
					tl = line;
					aParam = 1.0 - (cLength - currLength) / myLength;
					break;
				} else {
					if (line.isAnimated()) {
						line.setAnimated(false);
						line.repaint();
					}
				}
			}

			tl.setAnimated(true);
			tl.setTestingParametr(aParam);
			tl.repaint();

		}
	};

	Action stTesting = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7524820027095784410L;

		public synchronized void actionPerformed(java.awt.event.ActionEvent e) {
			StyledLine.this._stpTesting();
		}
	};

	Action stopFlash = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8444257196282947571L;

		public synchronized void actionPerformed(java.awt.event.ActionEvent e) {
			StyledLine.this.setLineColor(StyledLine.this.originalColor);
			StyledLine.this.repaint();
			StyledLine.this.animated = false;
			StyledLine.this.animatedAsFlash = false;
		}
	};

	public Vector getLinesVector() {
		return lines;
	}

}
