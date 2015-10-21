package gui.opdGraphics.opdBaseComponents;

import exportedAPI.OpcatConstants;
import gui.Opcat2;
import gui.dataProject.DataColors;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.DrawingArea;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.MoveUpdatable;
import gui.opdGraphics.OpcatContainer;
import gui.opdGraphics.draggers.AroundDragger;
import gui.opdGraphics.lines.OpdLine;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.undo.CompoundUndoAction;
import gui.undo.UndoableMoveResizeComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;

public abstract class BaseGraphicComponent extends JLayeredPane implements
		KeyListener, MouseListener, MouseMotionListener, Connectable, Movable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 480957748609220252L;

	protected OpdProject myProject;

	protected Color backgroundColor;

	protected Color textColor;

	protected Color borderColor;

	protected boolean selected;

	private boolean zoomedIn;

	private boolean isPresented = true;

	private Color currentForColor = null;

	DataColors colors = new DataColors();

	boolean sentToBAck = false;

	/**
	 * The font, if this component contains some text information, like name, it
	 * will be drawn using this font
	 */
	// protected Font currentFont;
	/**
	 * The metrics for the font.
	 */
	protected FontMetrics currentMetrics;

	/*
	 * Lines of the links or relations connected to this component
	 */
	private Hashtable lines;

	/*
	 * AroundDraggers or its subclasses of the links or relations connected to
	 * this component
	 */
	private Hashtable draggers;

	private int tempX;

	private int tempY;

	// private int prevX, prevY;
	private int initialX, initialY;

	private int initialWidth;

	private int initialHeight;

	private int whichResize;

	protected boolean resize;

	protected boolean inMove;

	protected JLayeredPane tempContainer;

	private boolean isDragged = false;

	private boolean moveable = true;

	protected Hashtable objects2Move;

	protected Dimension initialSize;

	// protected Hashtable multiMoveComponents;

	// -------------------------------------------------------------
	// constants are defined here

	public static final double BORDER_SENSITIVITY = 8.0;

	/**
	 * Center of the component<br>
	 * <strong>Note:</strong> <code>CENTER</code> is not the same as
	 * <code>NOT_IN_BOREDR</code> when thecomponent is drawn with shadow there
	 * are areas that not in border niether in center
	 */
	public static final int CENTER = 9;

	// show tabs ind dialogs
	public static final int SHOW_1 = 1;

	public static final int SHOW_2 = 2;

	public static final int SHOW_3 = 4;

	public static final int SHOW_4 = 8;

	public static final int SHOW_5 = 16;

	public static final int SHOW_6 = 32;

	public static final int SHOW_MISC = 64;

	public static final int SHOW_7 = 128;

	public static final int SHOW_ALL_TABS = SHOW_1 | SHOW_2 | SHOW_3 | SHOW_4
			| SHOW_5 | SHOW_6 | SHOW_7 | SHOW_MISC;

	// show buttons in dialog
	public static final int SHOW_OK = 1;

	public static final int SHOW_CANCEL = 2;

	public static final int SHOW_APPLY = 4;

	public static final int SHOW_ALL_BUTTONS = SHOW_OK | SHOW_CANCEL
			| SHOW_APPLY;

	private Hashtable updateListeners;

	/**
	 * <strong>No default constructor for this class.</strong><br>
	 * <p>
	 * This is an abstruct class and cannot be instantinated directely. It
	 * initializes default values, all subclasses have to call
	 * <code>super()</code> in their constructors to initialize the defaults
	 * and set parameters given as arguments
	 * </p>
	 * 
	 * @param <code>pOpdId long</code>, uniqe identifier of the OPD this
	 *            component is added to.
	 * @param <code>pOpdId long</code>, uniqe identifier of the component within
	 *            OPD it is added to.
	 * @param <code>pProject OpdProject</code>, project this component is added
	 *            to.
	 */
	public BaseGraphicComponent(OpdProject pProject) {
		this.lines = new Hashtable();
		this.draggers = new Hashtable();
		this.updateListeners = new Hashtable();
		this.objects2Move = new Hashtable();
		this.setLayout(null);
		this.zoomedIn = false;
		this.myProject = pProject;
		// this.setDoubleBuffered(true);
	}

	public void addUpdateListener(MoveUpdatable ls) {
		this.updateListeners.put(ls, ls);
	}

	public void removeUpdateListener(MoveUpdatable ls) {
		this.updateListeners.remove(ls);
	}

	/**
	 * Sets this compomponent to be zoomed in, i.e. this components is
	 * graphically capable to contain another object (for zooming in operation).
	 */
	public void setZoomedIn(boolean yn) {
		this.zoomedIn = yn;
	}

	/**
	 * Tests if this compomponent is zoomed in, i.e. this components is
	 * graphically capable to contain another object (for zooming in operation).
	 */
	public boolean isZoomedIn() {
		return this.zoomedIn;
	}

	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	public void setBackgroundColor(Color bColor) {
		this.backgroundColor = bColor;
	}

	public Color getTextColor() {
		return this.textColor;
	}

	public void setTextColor(Color tColor) {
		this.textColor = tColor;
	}

	public Color getBorderColor() {
		return this.borderColor;
	}

	public void setBorderColor(Color bColor) {
		this.borderColor = bColor;
	}

	// public Font getCurrentFont()
	// {
	// return currentFont;
	// }
	//
	// public void setCurrentFont(Font cFont)
	// {
	// currentFont = cFont;
	// }

	public void setSize(int w, int h) {
		super.setSize(w, h);
		this.repaintLines();
	}

	public void setLocation(int x, int y) {
		super.setLocation(x, y);

		this.repaintLines();
	}

	/**
	 * Adds line to the <a href = OpdBaseComponent.html#lines><code>lines</code></a>
	 * Hashtable.
	 */
	public void addLine(OpdLine pLine) {
		this.lines.put(pLine, pLine);
	}

	public void removeLine(OpdLine pLine) {
		this.lines.remove(pLine);
	}

	/**
	 * Adds dragger to the <a href = OpdBaseComponent.html#draggers><code>lines</code></a>
	 * Hashtable.
	 */
	public void addDragger(AroundDragger pDrag) {
		this.draggers.put(pDrag, pDrag);
	}

	public void removeDragger(AroundDragger pDrag) {
		this.draggers.remove(pDrag);
	}

	/**
	 * Repaint all lines of the OPD links and OPD relations connected to this
	 * component
	 */
	public void repaintLines() {
		OpdLine tempLine;
		AroundDragger tempDrag;
		for (Enumeration e = this.lines.elements(); e.hasMoreElements();) {
			tempLine = (OpdLine) (e.nextElement());
			tempLine.update();
			tempLine.repaint();
		}

		for (Enumeration e = this.draggers.elements(); e.hasMoreElements();) {
			tempDrag = (AroundDragger) (e.nextElement());
			tempDrag.updateDragger();
		}

		Component components[] = this.getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof BaseGraphicComponent) {
				((BaseGraphicComponent) components[i]).repaintLines();
			}
		}

		for (Enumeration e = this.updateListeners.elements(); e
				.hasMoreElements();) {
			((MoveUpdatable) e.nextElement()).updateMove(this);
		}

	}

	public boolean isRelated() {
		return !(this.lines.isEmpty() && this.draggers.isEmpty() && this.updateListeners
				.isEmpty());
	}

	/**
	 * Empty implementation of
	 * <code>FocusListener.focusGained(FocusEvent e)</code> for adapting
	 * purposes
	 */
	public void focusGained(FocusEvent e) {
	}

	/**
	 * Empty implementation of
	 * <code>FocusListener.focusLost(FocusEvent e)</code> for adapting
	 * purposes
	 */
	public void focusLost(FocusEvent e) {
	}

	/**
	 * <code>MouseEvent.mouseClicked(MouseEvent e)</code> event handler
	 * Behavior depends on state of <a href =
	 * "../opdProject.OpdStateMachine.html"><code>OpdStateMachine</code></a>
	 * state. Also sets the mouse cursor.
	 */
	public void mouseClicked(MouseEvent e) {

		if (StateMachine.isAnimated() && (e.getClickCount() == 2)) {
			if (this instanceof OpdThing) {
				OpdThing opdThing = (OpdThing) this;
				opdThing.getEntry().ShowInstances();

			}
		}

		if (StateMachine.isWaiting() || StateMachine.isAnimated()) {
			return;
		}

		if (SwingUtilities.isRightMouseButton(e)) {
			return;
		}

		if (e.getClickCount() == 2) {
			this.callPropertiesDialog(SHOW_ALL_TABS, SHOW_ALL_BUTTONS);
			return;
		}
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	/**
	 * <code>MouseEvent.mouseEntered(MouseEvent e)</code> event handler
	 * Behavior depends on state of <a href =
	 * "../opdProject.OpdStateMachine.html"><code>OpdStateMachine</code></a>
	 * state. Also sets the mouse cursor.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * <code>MouseEvent.mousePressed(MouseEvent e)</code> event handler
	 * Behavior depends on state of <a href =
	 * "../opdProject.OpdStateMachine.html"><code>OpdStateMachine</code></a>
	 * state. Also sets the mouse cursor. The method decides if the element is
	 * to be selected, moved or resized.
	 */
	public void mousePressed(MouseEvent e) {
		if (StateMachine.isWaiting()) {
			return;
		}

		this.isDragged = false;
		this.tempContainer = (JLayeredPane) this.getParent();
		this.tempX = e.getX();
		this.tempY = e.getY();
		this.initialX = this.getX();
		this.initialY = this.getY();
		this.initialWidth = this.getWidth();
		this.initialHeight = this.getHeight();

		this.whichResize = this.whichBorder(e.getX(), e.getY());

		this.resize = false;
		this.inMove = false;
		if (this.inMove(e.getX(), e.getY())) {
			this.inMove = true;
		}

		// Checks if mouse is on the resize points or on the boundery
		if (this.inResize(e.getX(), e.getY())) {
			this.resize = true;
			this.objects2Move.clear();
			this.addObjects2Move();
		}

	}

	/**
	 * <code>MouseEvent.mouseReleased(MouseEvent e)</code> event handler
	 * Behavior depends on state of <a href =
	 * "../opdProject.OpdStateMachine.html"><code>OpdStateMachine</code></a>
	 * state. Also sets the mouse cursor.
	 */
	public void mouseReleased(MouseEvent e) {
		GraphicsUtils.setLastMouseEvent(e);
		if (StateMachine.isWaiting() || StateMachine.isAnimated()) {
			return;
		}

		this.myProject.copyFormat();

		this.tempContainer = (JLayeredPane) this.getParent();
		// tempContainer.setLayer(this, myLayer, myPosition);
		// //((int)JLayeredPane.DRAG_LAYER));

		if (e.isPopupTrigger() || e.isMetaDown()) {
			this.showPopupMenu(e.getX(), e.getY());
			return;
		}

		if (this.isDragged) {
			CompoundUndoAction undoAction = new CompoundUndoAction();
			// undoAction.addAction(new UndoableMoveResizeComponent(this,
			// new
			// Point(initialX, initialY), new Dimension(initialWidth,
			// initialHeight),
			// new Point(getX(), getY()), new Dimension(getWidth(),
			// getHeight())));

			// VERY, VERY, VERY dirty solution
			BaseGraphicComponent bgc = null;
			Rectangle initRect = null;
			Point p = null;
			Dimension d = null;

			Hashtable ht = ((OpcatContainer) this.getParent()).getSelection()
					.getGraphicalSelectionHash();
			for (Enumeration e1 = ht.keys(); e1.hasMoreElements();) {
				bgc = (BaseGraphicComponent) e1.nextElement();
				initRect = (Rectangle) ht.get(bgc);
				p = new Point((int) Math.round(initRect.getX()), (int) Math
						.round(initRect.getY()));
				d = new Dimension((int) Math.round(initRect.getWidth()),
						(int) Math.round(initRect.getHeight()));
				undoAction.addAction(new UndoableMoveResizeComponent(bgc, p, d,
						new Point(bgc.getX(), bgc.getY()), new Dimension(bgc
								.getWidth(), bgc.getHeight()), this.myProject));

			}

			for (Enumeration locenum = this.objects2Move.keys(); locenum
					.hasMoreElements();) {
				OpdConnectionEdge t = (OpdConnectionEdge) locenum.nextElement();
				Rectangle oldSize = (Rectangle) this.objects2Move.get(t);
				undoAction.addAction(new UndoableMoveResizeComponent(t,
						new Point((int) oldSize.getX(), (int) oldSize.getY()),
						new Dimension((int) oldSize.getWidth(), (int) oldSize
								.getHeight()), new Point(t.getX(), t.getY()),
						new Dimension(t.getWidth(), t.getHeight()),
						this.myProject));
			}

			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(this, undoAction));
			Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
			Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
		}

		this.objects2Move.clear();

		for (Enumeration locenum = this.updateListeners.elements(); locenum
				.hasMoreElements();) {
			((MoveUpdatable) locenum.nextElement()).updateRelease(this);
		}
	}

	/**
	 * <code>MouseMotionEvent.mouseMoved(MouseMotionEvent e)</code> event
	 * handler Behavior depends on state of <a href =
	 * "../opdProject.OpdStateMachine.html"><code>OpdStateMachine</code></a>
	 * state. The method might resize the element or move it, according to the
	 * <code>resize</code> variable state. The method contains a disabled
	 * feature, in which things which reside within the zoomed-in thing, are
	 * resized along with it. Whenever an element get dragged, it is first of
	 * all pressed, using the <code>mousePressed</code> method.
	 * 
	 * @see #mouseReleased(MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (StateMachine.isWaiting() || SwingUtilities.isRightMouseButton(e)
				|| StateMachine.isAnimated()) {
			return;
		}

		this.isDragged = true;
		this.tempContainer = (JLayeredPane) this.getParent();

		// Handling resize of the element
		if (this.resize) {
			int rWidth = 10;
			int rHeight = 10;
			int rX = 10;
			int rY = 10;

			switch (this.whichResize) {
			case OpcatConstants.N_BORDER:
				rWidth = this.initialWidth;
				rHeight = this.initialY + this.initialHeight
						- (this.getY() - this.tempY + e.getY());
				rX = this.initialX;
				rY = this.getY() - this.tempY + e.getY();
				break;
			case OpcatConstants.NE_BORDER:
				rWidth = this.initialX + this.initialWidth
						- (this.getX() + this.tempX - e.getX());
				rHeight = this.initialY + this.initialHeight
						- (this.getY() - this.tempY + e.getY());
				rX = this.initialX;
				rY = this.getY() - this.tempY + e.getY();
				break;
			case OpcatConstants.NW_BORDER:
				rWidth = this.initialX + this.initialWidth
						- (this.getX() - this.tempX + e.getX());
				rHeight = this.initialY + this.initialHeight
						- (this.getY() - this.tempY + e.getY());
				rX = this.getX() - this.tempX + e.getX();
				rY = this.getY() - this.tempY + e.getY();
				break;
			case OpcatConstants.S_BORDER:
				rWidth = this.initialWidth;
				rHeight = this.initialHeight + e.getY() - this.tempY;
				rX = this.initialX;
				rY = this.initialY;
				break;
			case OpcatConstants.SE_BORDER:
				rWidth = this.initialWidth + e.getX() - this.tempX;
				rHeight = this.initialHeight + e.getY() - this.tempY;
				rX = this.initialX;
				rY = this.initialY;
				break;
			case OpcatConstants.SW_BORDER:
				rWidth = this.initialX + this.initialWidth
						- (this.getX() - this.tempX + e.getX());
				rHeight = this.initialHeight + e.getY() - this.tempY;
				rX = this.getX() - this.tempX + e.getX();
				rY = this.initialY;
				break;
			case OpcatConstants.W_BORDER:
				rWidth = this.initialX + this.initialWidth
						- (this.getX() - this.tempX + e.getX());
				rHeight = this.initialHeight;
				rX = this.getX() - this.tempX + e.getX();
				rY = this.initialY;
				break;
			case OpcatConstants.E_BORDER:
				rWidth = this.initialX + this.initialWidth
						- (this.getX() + this.tempX - e.getX());
				;
				rHeight = this.initialHeight;
				rX = this.initialX;
				rY = this.initialY;
				break;
			}

			boolean insideContainer = (this.tempContainer.contains(rX, rY)
					&& this.tempContainer.contains(rX + rWidth, rY)
					&& this.tempContainer.contains(rX, rY + rHeight) && this.tempContainer
					.contains(rX + rWidth, rY + rHeight));

			if ((rWidth > this.getMinimumSize().getWidth()) && insideContainer) {
				this.setLocation(rX, this.getY());
				this.setSize(rWidth, this.getHeight());
			}

			if ((rHeight > this.getMinimumSize().getHeight())
					&& insideContainer) {
				this.setLocation(this.getX(), rY);
				this.setSize(this.getWidth(), rHeight);
			}

			return;
		}

		// Handling movement of the element
		if (this.inMove) {
			//this.moveComp(e, this.tempX, this.tempY) ; 			
			Container c = this.getParent();
			OpcatContainer oc = null;
			if (c instanceof OpcatContainer) {
				oc = (OpcatContainer) c;
				BaseGraphicComponent[] bgcs = oc.graphicalSelectionComponents();
				for (int i = 0; i < bgcs.length; i++) {
					if (bgcs[i].isMoveable() || (bgcs[i] == this)) {

						bgcs[i].moveComp(e, this.tempX, this.tempY);

					}
				}

			}
			return;
		}
	}

	public boolean isMoveable() {
		return this.moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public void moveComp(MouseEvent e, int initX, int initY) {
		JLayeredPane tempContainer = (JLayeredPane) this.getParent();
		int rX, rY;

		rX = this.getX() - initX + e.getX();
		rY = this.getY() - initY + e.getY();
		if (tempContainer.contains(rX, rY)
				&& tempContainer.contains(rX + this.getWidth(), rY)
				&& tempContainer.contains(rX, rY + this.getHeight())
				&& tempContainer.contains(rX + this.getWidth(), rY
						+ this.getHeight())) {
			this.setLocation(this.getX() - initX + e.getX(), this.getY()
					- initY + e.getY());
			this.repaintLines();
		}
		return;
	}

	/**
	 * <code>MouseEvent.mouseEntered(MouseEvent e)</code> event handler
	 * Behavior depends on state of <a href =
	 * "../opdProject.OpdStateMachine.html"><code>OpdStateMachine</code></a>
	 * state. Also sets the mouse cursor.
	 */

	public void mouseEntered(MouseEvent e) {
		if (StateMachine.isWaiting() || StateMachine.isAnimated()) {
			return;
		}
	}

	/**
	 * <code>MouseMotionEvent.mouseMoved(MouseMotionEvent e)</code> event
	 * handler Behavior depends on state of <a href =
	 * "../opdProject.OpdStateMachine.html"><code>OpdStateMachine</code></a>
	 * state.
	 */

	public void mouseMoved(MouseEvent e) {
		if (StateMachine.isWaiting() || StateMachine.isAnimated()) {
			return;
		}

		this.setCursorForLocation(e);
	}

	protected void setCursorForLocation(MouseEvent e) {
		if (this.inMove(e.getX(), e.getY())) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			return;
		}

		if (this.inResize(e.getX(), e.getY())) {
			switch (this.whichBorder(e.getX(), e.getY())) {
			case OpcatConstants.N_BORDER:
				this.setCursor(Cursor
						.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				return;
			case OpcatConstants.NE_BORDER:
				this.setCursor(Cursor
						.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
				return;
			case OpcatConstants.NW_BORDER:
				this.setCursor(Cursor
						.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
				return;
			case OpcatConstants.S_BORDER:
				this.setCursor(Cursor
						.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
				return;
			case OpcatConstants.SE_BORDER:
				this.setCursor(Cursor
						.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
				return;
			case OpcatConstants.SW_BORDER:
				this.setCursor(Cursor
						.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
				return;
			case OpcatConstants.W_BORDER:
				this.setCursor(Cursor
						.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				return;
			case OpcatConstants.E_BORDER:
				this.setCursor(Cursor
						.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				return;
			}
		}

	}

	public int getActualWidth() {
		return this.getWidth();
	}

	public int getActualHeight() {
		return this.getHeight();
	}

	/**
	 * Paints the component. Abstract method, defines interface for subclasses
	 */
	protected abstract void paintComponent(Graphics g);

	/**
	 * Calls the Dialog Window which allows to edit component's properties. This
	 * method is invoked when user performs double click on the
	 * component.Abstract method, defines interface for subclasses
	 */
	public abstract void callPropertiesDialog(int showTabs, int showButtons);

	/**
	 * Test if the point in resizing area i.e. if user drags mouse in this area
	 * it resizes the component. Abstract method, defines interface for
	 * subclasses.
	 */
	public abstract boolean inResize(int pX, int pY);

	/**
	 * Test if the point in moving area i.e. if user drags mouse in this area it
	 * moves the component. Abstract method, defines interface for subclasses
	 */
	public abstract boolean inMove(int pX, int pY);

	/**
	 * Returns one of constants which determines on which border point (pX, pY)
	 * lays. Abstract method, defines interface for subclasses.
	 */
	public abstract int whichBorder(int pX, int pY);

	public abstract void showPopupMenu(int pX, int pY);

	protected abstract void addObjects2Move();

	/**
	 * Sets the component state. Sets
	 * <code>OpdProject.setSystemChangeProbability</code> to true. If a
	 * component was selected, there is high probability that something in the
	 * system was changed.
	 * 
	 * @param <code>yn</code> <code>true</code> to select the component
	 *            <code>false</code> to deselect
	 */
	public void setSelected(boolean yn) {
		this.selected = yn;
	}

	/**
	 * Tests component for selection
	 * 
	 * @return <code>true</code> if component is selected, otherwise
	 *         <code>false</code>
	 */
	public boolean isSelected() {
		return this.selected;
	}

	public void bringToFront() {
		this.tempContainer = (JLayeredPane) this.getParent();
		if (this.tempContainer != null) {
			this.tempContainer.moveToFront(this);
			this.sentToBAck = false;
		}
	}

	public void sendToBack() {
		this.tempContainer = (JLayeredPane) this.getParent();
		if (this.tempContainer != null) {
			this.tempContainer.moveToBack(this);
			this.sentToBAck = true;
		}

	}

	// private void setNewSizes(double wFactor, double hFactor)
	// {
	// for (Enumeration e = objects2Move.keys() ; e.hasMoreElements();)
	// {
	// OpdConnectionEdge t = (OpdConnectionEdge)e.nextElement();
	// Rectangle oldSize = (Rectangle)objects2Move.get(t);
	// t.setLocation((int)(oldSize.getX()*wFactor),
	// (int)(oldSize.getY()*hFactor));
	// t.setSize((int)(oldSize.getWidth()*wFactor),
	// (int)(oldSize.getHeight()*hFactor));
	// }
	// }

	/**
	 * @return Returns the isPresented.
	 */
	public boolean isPresented() {
		return this.isPresented;
	}

	/**
	 * @param isPresented
	 *            The isPresented to set.
	 */
	public void setPresented(boolean isPresented) {
		this.isPresented = isPresented;
	}

	public void changeToMetaColor(int color) {

		if (!colors.isUsed())
			currentForColor = getBackgroundColor();
		colors.setUsed(true);
		setBackgroundColor(colors.getColor(color));
		// colors.setColor(this.getComponentGraphics(this.getGraphics()), color)
		// ;
	}

	public void restoreFromMetaColor() {
		if ((currentForColor != null) && (colors.isUsed()))
			setBackgroundColor(currentForColor);
		colors.setUsed(false);
		currentForColor = null;
	}

	public boolean isSentToBAck() {
		return sentToBAck;
	}

	public void setSentToBAck(boolean sentToBAck) {
		this.sentToBAck = sentToBAck;
		if (sentToBAck) {
			sendToBack();
		} else {
			bringToFront();
		}
		repaint();
	}

	public DrawingArea getDrawingArea() {
		return myProject.getCurrentOpd().getDrawingArea();
	}

}