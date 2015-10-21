package gui.opdGraphics.lines;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.OpcatContainer;
import gui.opdGraphics.draggers.AroundDragger;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.opmEntities.OpmEntity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import util.Configuration;

public class TextLine extends OpdSimpleLine {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private static final double ANIMATION_SIZE = 7.0;

	private String upperText;
	private String lowerText;
	private boolean inDrag;
	private Point dragPoint;
	private StyledLine parentLine;
	private boolean animated;
	private double testingParametr;

	public TextLine(Connectable pEdge1, RelativeConnectionPoint cPoint1,
			Connectable pEdge2, RelativeConnectionPoint cPoint2,
			OpmEntity pEntity, OpdKey key, OpdProject pProject) {
		super(pEdge1, cPoint1, pEdge2, cPoint2, pEntity, key, pProject);
		this.upperText = null;
		this.lowerText = null;
		// shiftLine = 15;
		this.inDrag = false;
		this.dragPoint = new Point(0, 0);
		this.parentLine = null;
		this.animated = false;
		// animated = true;
		// testingParametr = 0.75;
	}

	public void setParentLine(StyledLine parentLine) {
		this.parentLine = parentLine;
	}

	public void setAnimated(boolean isAnimated) {
		this.animated = isAnimated;
	}

	public boolean isAnimated() {
		return this.animated;
	}

	public void setTestingParametr(double parametr) {
		this.testingParametr = parametr;
	}

	public double getTestingParametr() {
		return this.testingParametr;
	}

	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		this.inDrag = false;
	}

	public void mouseReleased(MouseEvent e) {
		if (StateMachine.isWaiting() || StateMachine.isAnimated()) {
			return;
		}

		this.myProject.copyFormat();

		if (!this.inDrag) {
			super.mouseReleased(e);
			this.inDrag = false;
			return;
		}

		if (this.contains(SwingUtilities.convertPoint(this.getParent(),
				this.dragPoint, this))) {
			this.inDrag = false;
			super.mouseReleased(e);
			this.repaint();
			return;
		}

		if (this.parentLine != null) {
			this.parentLine.breakLine(this, this.dragPoint);
		}
		this.inDrag = false;
	}

	public void mouseDragged(MouseEvent e) {
		if (StateMachine.isAnimated() || StateMachine.isWaiting()) {
			return;
		}

		// following if determines if there is multiple selection, dirty!!!
		if (((OpcatContainer) (this.getParent())).getSelection()
				.getSelectedItemsHash().size() > 1) {
			super.mouseDragged(e);
			return;
		}
		this.inDrag = true;
		this.dragPoint.setLocation(e.getX(), e.getY());
		this.dragPoint = SwingUtilities.convertPoint(this, this.dragPoint, this
				.getParent());

		if (this.edge1 instanceof AroundDragger) {
			((AroundDragger) this.edge1).updateDragger();
		}

		if (this.edge2 instanceof AroundDragger) {
			((AroundDragger) this.edge2).updateDragger();
		}

		Point p1, p2;

		p1 = this.edge1.getAbsoluteConnectionPoint(this.cPoint1);
		p1.setLocation(this.edge1.getX() + p1.getX(), this.edge1.getY()
				+ p1.getY());
		p1 = SwingUtilities.convertPoint(((JComponent) this.edge1).getParent(),
				p1, this.getParent());

		p2 = this.edge2.getAbsoluteConnectionPoint(this.cPoint2);
		p2.setLocation(this.edge2.getX() + p2.getX(), this.edge2.getY()
				+ p2.getY());
		p2 = SwingUtilities.convertPoint(((JComponent) this.edge2).getParent(),
				p2, this.getParent());

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double shiftLine = (currentSize / normalSize) * MIN_SIZE;

		int x = (int) (Math.min(this.dragPoint.getX(), Math.min(p1.getX(), p2
				.getX())) - shiftLine);
		int y = (int) (Math.min(this.dragPoint.getY(), Math.min(p1.getY(), p2
				.getY())) - shiftLine);

		int width = (int) (Math.max(this.dragPoint.getX(), Math.max(p1.getX(),
				p2.getX()))
				- x + shiftLine * 2);
		int height = (int) (Math.max(this.dragPoint.getY(), Math.max(p1.getY(),
				p2.getY()))
				- y + shiftLine * 2);

		this.setBounds(x, y, width, height);
		// repaint();
	}

	public void paintComponent(Graphics g) {
		int x1;
		int y1;
		int x2;
		int y2;
		Point p1, p2;

		int stringX;
		GenericTable config = this.myProject.getConfiguration();
		Font currentFont = (Font) this.myProject.getConfiguration()
				.getProperty("LineFont");
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		Graphics2D g2 = (Graphics2D) g;
		Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);

		if (this.isDashed()) {
			g2.setStroke(new BasicStroke((float) factor * 1.8f,
					BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f,
					new float[] { 6, 4 }, 0.0f));
		} else {
			g2.setStroke(new BasicStroke((float) factor * 1.8f));
		}

		// g2.setStroke(new BasicStroke(1.8f));

		g2.setColor(this.lineColor);

		p1 = this.getUpperPoint();
		p2 = this.getLowerPoint();
		x1 = (int) p1.getX();
		y1 = (int) p1.getY();
		x2 = (int) p2.getX();
		y2 = (int) p2.getY();

		if (!this.inDrag) {
			g2.drawLine(x1, y1, x2, y2);
		} else {
			Point tPoint = SwingUtilities.convertPoint(this.getParent(),
					this.dragPoint, this);
			g2.drawLine(x1, y1, (int) tPoint.getX(), (int) tPoint.getY());
			g2.drawLine((int) tPoint.getX(), (int) tPoint.getY(), x2, y2);
		}

		java.awt.geom.AffineTransform at = g2.getTransform();

		double angle = GraphicsUtils.calcutateRotationAngle(new Point(x1, y1),
				new Point(x2, y2));
		g2.rotate((angle + Math.PI / 2) % Math.PI - Math.PI / 120, this
				.getWidth() / 2, this.getHeight() / 2);

		if ((angle > 0) && (angle < Math.PI / 2)) {
			g2.rotate(Math.PI - Math.PI / 120, this.getWidth() / 2, this
					.getHeight() / 2);
		}

		currentFont = currentFont
				.deriveFont((float) (currentFont.getSize2D() * factor));

		g2.setFont(currentFont);
		g2.setColor(this.textColor);
		FontMetrics currentMetrics = this.getFontMetrics(currentFont);

		if (this.upperText != null) {
			stringX = (this.getWidth()
					- currentMetrics.stringWidth(this.upperText) - (int) ((MIN_SIZE + 10) * factor)) / 2;
			g2.drawString(this.upperText, stringX, this.getHeight() / 2
					- currentMetrics.getAscent() + 2);
		}

		if (this.lowerText != null) {
			stringX = (this.getWidth() - currentMetrics
					.stringWidth(this.lowerText)) / 2;
			g2.drawString(this.lowerText, stringX, this.getHeight() / 2
					+ currentMetrics.getAscent());
		}

		if (this.animated) {
			g2.setTransform(at);
			Point sPoint = this.edge1.getAbsoluteConnectionPoint(this.cPoint1);
			sPoint = SwingUtilities.convertPoint((Container) this.edge1,
					sPoint, this);

			Point dPoint = this.edge2.getAbsoluteConnectionPoint(this.cPoint2);
			dPoint = SwingUtilities.convertPoint((Container) this.edge2,
					dPoint, this);

			Point resPoint = new Point();
			resPoint.setLocation(dPoint.getX() - sPoint.getX(), dPoint.getY()
					- sPoint.getY());
			resPoint.setLocation(resPoint.getX() * this.testingParametr
					+ sPoint.getX(), resPoint.getY() * this.testingParametr
					+ sPoint.getY());

			double aSize = ANIMATION_SIZE * factor;
			g2.setColor(Color.red);
			g2.fillOval((int) Math.round(resPoint.getX() - aSize / 2),
					(int) Math.round(resPoint.getY() - aSize / 2), (int) Math
							.round(aSize), (int) Math.round(aSize));

		}

		drawIcon(g2);

	}

	private void drawIcon(Graphics2D g2) {

		boolean showIcons = Boolean.parseBoolean(Configuration.getInstance()
				.getProperty("show_icons"));
		
		if (!showIcons)
			return;

		
		if (getIcon() != null) {

			BufferedImage buf = getIcon().getAsBufferedImage();
			Image image = new ImageIcon(buf).getImage();

			Point sPoint = this.edge1.getAbsoluteConnectionPoint(this.cPoint1);
			sPoint = SwingUtilities.convertPoint((Container) this.edge1,
					sPoint, this);

			Point dPoint = this.edge2.getAbsoluteConnectionPoint(this.cPoint2);
			dPoint = SwingUtilities.convertPoint((Container) this.edge2,
					dPoint, this);

			Point resPoint = new Point();
			resPoint.setLocation(dPoint.getX() - sPoint.getX(), dPoint.getY()
					- sPoint.getY());
			resPoint.setLocation(resPoint.getX() * 0.5 + sPoint.getX(),
					resPoint.getY() * 0.5 + sPoint.getY());

			g2.drawImage(image, (int) Math.round(resPoint.getX() / 2),
					(int) Math.round(resPoint.getY() / 2), null);

		}
		return;
	}

	public String getLowerText() {
		return this.lowerText;
	}

	public void setLowerText(String lowerText) {
		this.lowerText = lowerText;
	}

	public String getUpperText() {
		return this.upperText;
	}

	public void setUpperText(String upperText) {
		this.upperText = upperText;
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

		if (this.inDrag) {
			tail = SwingUtilities.convertPoint(this.getParent(),
					this.dragPoint, this);
		}

		return GraphicsUtils.calcutateRotationAngle(head, tail);
	}

}
