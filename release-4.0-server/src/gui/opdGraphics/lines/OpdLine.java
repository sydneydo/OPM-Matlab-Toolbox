package gui.opdGraphics.lines;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.dataProject.DataColors;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.DoublePoint;
import gui.opdGraphics.GraphicsUtils;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.opmEntities.OpmEntity;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.jai.PlanarImage;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public abstract class OpdLine extends JComponent implements MouseListener,
		MouseMotionListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3936241539001395556L;

	private Color currentForColor = null;
	
	private PlanarImage icon = null;

	DataColors colors = new DataColors();

	protected static final double MIN_SIZE = 18.0;

	protected static final double LINE_SENSITIVITY = 5.00;

	/**
	 * First edge of the line
	 */
	protected Connectable edge1;

	/**
	 * Second edge of the line
	 */
	protected Connectable edge2;

	// protected int shiftLine;

	/**
	 * the side of the component as spacified in <a href =
	 * "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 */
	protected RelativeConnectionPoint cPoint1;

	protected RelativeConnectionPoint cPoint2;

	/**
	 * If line is arranged.
	 * <p>
	 * If true, line is drawn number of lines which are perpendicular each to
	 * other. If false, line is drawn as stright line connecting two points.
	 * </p>
	 */
	protected boolean arranged; // if true line arranged, otherwise the line is

	// simply connects two points

	/**
	 * Color the line is drawn with.
	 */
	protected Color lineColor;

	/**
	 * If line has some kind of text mark, text will be rendered using this
	 * color.
	 */
	protected Color textColor;

	protected OpmEntity myEntity;

	protected OpdProject myProject;

	protected OpdKey myKey;

	private boolean dashed;

	private boolean vertical;

	int tempX, tempY;

	/**
	 * Constructs an <code>OpdLine</code> component. It's an abstract class
	 * and cannot be instantinated directly.
	 * 
	 * @param <code>pEdge1 Connectable</code>, one edge of Opd Link a subclass
	 *            of <a href = "AroundDragger.html"><code>AroundDragger</code></a>.
	 * @param <code>pSide1 int</code>, the border of first edge as defined in <a
	 *            href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 * @param <code>pParam1 double</code>, connection parameter for positioning
	 *            the connection point on the component's border.
	 * @param <code>pEdge2 Connectable</code>, second edge of Opd Link a
	 *            subclass of <a href = "AroundDragger.html"><code>AroundDragger</code></a>.
	 * @param <code>pSide2 int</code>, the border of first edge as defined in <a
	 *            href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 * @param <code>pParam2 double</code>, connection parameter for positioning
	 *            the connection point on the component's border.
	 */
	public OpdLine(Connectable pEdge1, RelativeConnectionPoint cPoint1,
			Connectable pEdge2, RelativeConnectionPoint cPoint2,
			OpmEntity pEntity, OpdKey key, OpdProject pProject) {
		this.edge1 = pEdge1;
		this.edge2 = pEdge2;
		this.cPoint1 = cPoint1;
		this.cPoint2 = cPoint2;
		this.myEntity = pEntity;
		this.myProject = pProject;
		this.myKey = key;
		this.arranged = false;
		this.vertical = false;
		this.textColor = Color.black;
		this.lineColor = Color.black;

		if (pEntity != null) {
			this.dashed = pEntity.isEnviromental();
		}
		this.update();
		colors = new DataColors();
	}

	public void changeToMetaColor(int color) {

		if (!colors.isUsed())
			currentForColor = getLineColor();
		colors.setUsed(true);
		setLineColor(colors.getColor(color));
		// colors.setColor(this.getComponentGraphics(this.getGraphics()), color)
		// ;
	}

	public void restoreFromMetaColor() {
		if ((currentForColor != null) && (colors.isUsed()))
			setLineColor(currentForColor);
		colors.setUsed(false);
		currentForColor = null;
	}

	/**
	 * Returns whether the line is arranged
	 * 
	 * @retun <code>true</code> if line is arranged, otherwise
	 *        <code>false</code>
	 */
	public boolean isArranged() {
		return this.arranged;
	}

	boolean isVertical() {
		return this.vertical;
	}

	void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

	/**
	 * Arranges and dearranges line. Arranged line is drawn as number of lines
	 * which are perpendicular each to other. Not arranged line is drawn as
	 * stright line connecting two points.
	 * 
	 * @param <code>yn boolean</code>, if <code>true</code> arranges the line,
	 *            if <code>false</code> dearranges the line
	 */
	public void setArranged(boolean yn) {
		this.arranged = yn;
	}

	/**
	 * @return the color the line is rendered with.
	 */
	public Color getLineColor() {
		return this.lineColor;
	}

	/**
	 * Sets the color the line is rendered with.
	 * 
	 * @param <code>pLineColor Color</code>, the new color the line will be
	 *            rendered with.
	 */
	public void setLineColor(Color pLineColor) {
		this.lineColor = pLineColor;
	}

	/**
	 * @return the color the text is rendered with, if line has some text marks.
	 */
	public Color getTextColor() {
		return this.textColor;
	}

	/**
	 * Sets the color the text is rendered with, if line has some text marks.
	 * 
	 * @param <code>pTextColor Color</code>, the new color the text will be
	 *            rendered with.
	 */
	public void setTextColor(Color pTextColor) {
		this.textColor = pTextColor;
	}

	/**
	 * the first side of the component as spacified in <a href =
	 * "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 */
	public void setSide1(int pSide) {
		this.cPoint1.setSide(pSide);
	}

	/**
	 * the second side of the component as spacified in <a href =
	 * "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 */
	public void setSide2(int pSide) {
		this.cPoint2.setSide(pSide);
	}

	public void setEdge1(Connectable sourceEdge) {
		this.edge1 = sourceEdge;
	}

	public void setEdge2(Connectable destEdge) {
		this.edge2 = destEdge;
	}

	/**
	 * @return the component on the one edge of
	 *         <code>OpdLine</line> it's one of subclasses of <a href = "AroundDragger.html"><code>AroundDragger</code></a>.
	 */
	public Connectable getEdge1() {
		return this.edge1;
	}

	/**
	 * @return the component on the second edge of
	 *         <code>OpdLine</line> it's one of subclasses of <a href = "AroundDragger.html"><code>AroundDragger</code></a>.
	 */
	public Connectable getEdge2() {
		return this.edge2;
	}

	public void setDashed(boolean dashed) {
		this.dashed = dashed;
		this.repaint();
	}

	public boolean isDashed() {
		return this.dashed;
	}

	/**
	 * updates line, used to avoid infinite loop created by calling
	 * <code>repaint()</code> function by components trying to update each
	 * other, when they are neighbors, and moving or resizing of one affects
	 * another.
	 */
	public synchronized void update() {
		Point p1, p2;

		Container parent = this.getParent();

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double shiftLine = (currentSize / normalSize) * MIN_SIZE;

		if (parent == null) {
			return;
		}

		p1 = this.edge1.getAbsoluteConnectionPoint(this.cPoint1);
		p2 = this.edge2.getAbsoluteConnectionPoint(this.cPoint2);

		p1 = SwingUtilities.convertPoint((Container) this.edge1, p1, parent);
		p2 = SwingUtilities.convertPoint((Container) this.edge2, p2, parent);

		this.setLocation((int) (Math.min(p1.getX(), p2.getX()) - shiftLine),
				(int) (Math.min(p1.getY(), p2.getY()) - shiftLine));

		this.setSize((int) (Math.abs(p1.getX() - p2.getX()) + shiftLine * 2),
				(int) (Math.abs(p1.getY() - p2.getY()) + shiftLine * 2));

	}

	/**
	 * Abstract method, to define interface for subclasses
	 */
	public abstract void paintComponent(Graphics g);

	public abstract void callPropertiesDialog();

	private boolean _isFirstPointUpper() {
		Point p1, p2;

		p1 = this.edge1.getAbsoluteConnectionPoint(this.cPoint1);
		p2 = this.edge2.getAbsoluteConnectionPoint(this.cPoint2);

		if ((this.edge1.getY() + p1.getY() - this.edge2.getY() - p2.getY()) >= 0) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * @return the upper point of the line in the component coordinates system.
	 */
	public Point getUpperPoint() {
		Point p1, p2, retPoint;
		Container parent = this.getParent();
		if (parent == null) {
			return new Point(0, 0);
		}

		retPoint = new Point(0, 0);

		p1 = this.edge1.getAbsoluteConnectionPoint(this.cPoint1);
		p2 = this.edge2.getAbsoluteConnectionPoint(this.cPoint2);

		if (this._isFirstPointUpper()) {
			retPoint.setLocation(this.edge1.getX() + p1.getX(), this.edge1
					.getY()
					+ p1.getY());
		} else {
			retPoint.setLocation(this.edge2.getX() + p2.getX(), this.edge2
					.getY()
					+ p2.getY());

		}

		retPoint = SwingUtilities.convertPoint(parent, retPoint, this);
		return retPoint;
	}

	/**
	 * @return the lower point of the line in the component coordinates system.
	 */
	public Point getLowerPoint() {
		Point p1, p2, retPoint;

		Container parent = this.getParent();
		if (parent == null) {
			return new Point(0, 0);
		}

		retPoint = new Point(0, 0);

		p1 = this.edge1.getAbsoluteConnectionPoint(this.cPoint1);
		p2 = this.edge2.getAbsoluteConnectionPoint(this.cPoint2);

		if (this._isFirstPointUpper()) {
			retPoint.setLocation(this.edge2.getX() + p2.getX(), this.edge2
					.getY()
					+ p2.getY());
		} else {
			retPoint.setLocation(this.edge1.getX() + p1.getX(), this.edge1
					.getY()
					+ p1.getY());
		}

		retPoint = SwingUtilities.convertPoint(parent, retPoint, this);
		return retPoint;
	}

	/**
	 * <p>
	 * Gets the component on one edge of the line as parameter to the method,
	 * and returns component on another. Such way each <a href =
	 * "Connectable.html><code>Connectable</code></a> can know its neighbor.
	 * This method should be called by <code>Connectable</code>s to know who
	 * is its partner on the second edge of the line.
	 * 
	 * @param <code>me Connectable</code> the Connectable on one edge of the
	 *            link.
	 * @return The connectable on the second edge of the line.
	 */
	public Connectable getNeighbor(Connectable me) {
		if (this.edge1 == me) {
			return this.edge2;
		} else {
			return this.edge1;
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		if (StateMachine.isAnimated() || StateMachine.isWaiting()) {
			return;
		}

		if (e.getClickCount() == 2) {
			this.callPropertiesDialog();
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (StateMachine.isWaiting()) {
			return;
		}

		this.tempX = e.getX();
		this.tempY = e.getY();

		// myProject.removeSelection();
		Entry myEntry = this.myProject.getComponentsStructure().getEntry(
				this.myEntity.getId());
		Instance myInstance = myEntry.getInstance(this.myKey);
		// myProject.addSelection(myInstance, !e.isShiftDown());
		if (e.isShiftDown()) {
			if (myInstance.isSelected()) {
				this.myProject.removeSelection(myInstance);
			} else {
				this.myProject.addSelection(myInstance, !e.isShiftDown());
			}
		} else {
			if (myInstance.isSelected()) {
				this.myProject.addSelection(myInstance, false);
			} else {
				this.myProject.addSelection(myInstance, true);
			}
		}
		this.repaint();
		return;
	}

	public OpmEntity getLogicalentity() {
		return myEntity;
	}

	public void mouseReleased(MouseEvent e) {
		if (StateMachine.isAnimated() || StateMachine.isWaiting()) {
			return;
		}

		if (e.isPopupTrigger() || e.isMetaDown()) {
			this.showPopupMenu(e.getX(), e.getY());
		}
	}

	public void mouseDragged(MouseEvent e) {
		// Container c = this.getParent();
		// OpcatContainer oc = null;
		// if(c instanceof OpcatContainer)
		// {
		// oc = (OpcatContainer)c;
		// BaseGraphicComponent[] bgcs = oc.graphicalSelectionComponents();
		// for(int i = 0; i < bgcs.length; i++)
		// {
		// bgcs[i].moveComp(e, tempX, tempY);
		// }
		//
		// }
		// return;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public boolean contains(int x, int y) {
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();

		double lineSensitivity = LINE_SENSITIVITY * (currentSize / normalSize);

		DoublePoint pl = new DoublePoint(this.getLowerPoint());
		DoublePoint pu = new DoublePoint(this.getUpperPoint());
		DoublePoint p = new DoublePoint(x, y);

		double deltaX = pu.getX() - pl.getX();
		if (deltaX == 0.0) {
			deltaX = 0.0001;
		}

		double angle = Math.atan((pu.getY() - pl.getY()) / deltaX);

		if (angle <= 0.0) {
			angle += Math.PI;
		}
		p.rotate(-angle, pu);
		pl.rotate(-angle, pu);
		if ((p.getX() >= pu.getX() - lineSensitivity)
				&& (p.getX() <= pl.getX() + lineSensitivity)
				&& (p.getY() <= pu.getY() + lineSensitivity)
				&& (p.getY() >= pu.getY() - lineSensitivity)) {
			// System.err.println("Contains "+this);
			return true;
		}
		return false;
	}

	public double getRotationAngle(Connectable ad) {
		Point p1, p2, head, tail;

		p1 = this.edge1.getAbsoluteConnectionPoint(this.cPoint1);
		p1.setLocation(this.edge1.getX() + p1.getX(), this.edge1.getY()
				+ p1.getY());
		p1 = SwingUtilities.convertPoint(this.getParent(), p1, this);

		p2 = this.edge2.getAbsoluteConnectionPoint(this.cPoint2);
		p2.setLocation(this.edge2.getX() + p2.getX(), this.edge2.getY()
				+ p2.getY());
		p2 = SwingUtilities.convertPoint(this.getParent(), p2, this);

		// p = getUpperPoint();
		if (ad == this.edge1) {
			head = p1;
			tail = p2;
		} else {
			head = p2;
			tail = p1;
		}

		return GraphicsUtils.calcutateRotationAngle(head, tail);
	}

	public abstract void showPopupMenu(int pX, int pY);
	
	public PlanarImage getIcon() {
		return icon;
	}

	public void setIcon(PlanarImage icon) {
		this.icon = icon;
	}
}
