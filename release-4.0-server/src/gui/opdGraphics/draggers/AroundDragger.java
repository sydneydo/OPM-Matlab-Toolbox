/*
 *  opcat2
 *  package: opdGraphics
 *  file:    AroundDragger.java
 */


package gui.opdGraphics.draggers;
import exportedAPI.OpcatConstants;
import exportedAPI.RelativeConnectionPoint;
//import extensionTools.hio.CursorFactory;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.DoublePoint;
import gui.opdGraphics.DrawingArea;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.MoveUpdatable;
import gui.opdGraphics.lines.OpdLine;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdProcess;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *  <p>Abstract class AroundDragger is a superclass for all classes which represents
 *  items that can be dragged around Objects, Processes or States in OPD.
 *  Actually this class provides dragging functionality for edges of all kinds
 *  of OPD links and relations.</p>
 */

public abstract class AroundDragger extends JComponent implements MouseListener, MouseMotionListener, Connectable
{

/**
	 * 
	 */
	private static final long serialVersionUID = -295585044768623321L;
/**
 *  OPD thing that this dragger connected to
 */
	protected static final double SELECTION_OFFSET = 3.2;
	protected Connectable edge;
	protected Connectable originalEdge;
	protected OpdProject myProject;

/**
 *  The line that this dragger connects to <code>edge</code>
 */
	protected OpdLine line;

/**
 *  Side of the OPD thing tis dragger connected to
 *  @see <a href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a> for possible values
 */
	protected RelativeConnectionPoint cPoint;
	protected RelativeConnectionPoint originalPoint;
	private int tempX;
	private int tempY;


	protected Color backgroundColor;
	protected Color textColor;
	protected Color borderColor;

/**
 *  Shift in X coordinate when dragger is inside one of the OPD graphic components, not in <a href = "DrawingArea.html"><code>DrawingArea</code></a>
 */

	protected int shiftX;  // shifts for connection to state (state not in Drawing
									// area like others but inside some Object)
/**
 *  Shift in Y coordinate when dragger is inside one of the OPD graphic components, not in <a href = "DrawingArea.html"><code>DrawingArea</code></a>
 */
	protected int shiftY;
	private boolean canLeave;
	protected boolean moveable;

	private Container dArea;

	private boolean selected;
	Hashtable updateListeners;
	protected boolean wasDragged;

/**
 *  <strong>There is no default constructor</strong>
 *  <p>All extending classes have to call <code>super()</code> in their constructors.
 *  @param <code>pEdge1</code> -- OPD graphic component this dragger connected to.
 *  @param <code>pSide1</code> -- The side OPD graphic component this dragger connected to.
 *  @param <code>pParam1</code> -- The relation of coordinates of a <code>mouseClick</code> event to length of the side.
 */
	public AroundDragger(Connectable pEdge1, RelativeConnectionPoint cPoint, OpdProject project)

	{
		this.myProject = project;
		this.edge = pEdge1;
		this.cPoint = cPoint;
		this.backgroundColor = new Color(230, 230, 230);
		this.textColor = Color.black;
		this.borderColor = Color.black;

		this._countShift();
		this.selected = false;
		this.updateListeners = new Hashtable();
		this.originalPoint = new RelativeConnectionPoint(cPoint.getSide(),cPoint.getParam());
		this.canLeave = false;
		this.moveable = true;
	}

/**
 * @return background color of the dragger
 */


	public boolean isCanLeave()
	{
		return this.canLeave;
	}

	public void setCanLeave(boolean canLeave)
	{
		this.canLeave = canLeave;
	}

	public boolean isMoveable()
	{
		return this.moveable;
	}

	public void setMoveable(boolean moveable)
	{
		this.moveable = moveable;
	}

	  public void addUpdateListener(MoveUpdatable ls)
	  {
		this.updateListeners.put(ls, ls);
	  }

	  public void removeUpdateListener(MoveUpdatable ls)
	  {
		this.updateListeners.remove(ls);
	  }



	  public void setRelativeConnectionPoint(RelativeConnectionPoint cPoint)
	  {
		this.cPoint = cPoint;
	  }

	public RelativeConnectionPoint getRelativeConnectionPoint()
	{
		return this.cPoint;
	}
	public Color getBackgroundColor()
	{
		return this.backgroundColor;
	}

/**
 *  Sets background color of the dragger
 *  @param bColor -- new background color
 */
	public void setBackgroundColor(Color bColor)
	{
		this.backgroundColor = bColor;
	}

/**
 * @return text color of the dragger
 */
	public Color getTextColor()
	{
		return this.textColor;
	}

/**
 *  Sets text color of the dragger
 *  @param tColor -- new text color
 */
	public void setTextColor(Color tColor)
	{
		this.textColor = tColor;
	}

/**
 * @return border color of the dragger
 */
	public Color getBorderColor()
	{
		return this.borderColor;
	}

/**
 *  Sets border color of the dragger
 *  @param bColor -- new boreder color
 */
	public void setBorderColor(Color bColor)
	{
		this.borderColor = bColor;
	}

/**
 * Sets the line that this dragger connects to <code>edge</code>
 */

	public void setLine(OpdLine pLine)
	{
		this.line = pLine;
	}

	private void _countShift()
	{

		this.dArea = ((OpdConnectionEdge)this.edge).getParent();

		while (!(this.dArea instanceof DrawingArea))
		{
			this.dArea = this.dArea.getParent();
		}


		if ( ((OpdConnectionEdge)this.edge).getParent() == this.getParent())
		{
			this.shiftX = 0;
			this.shiftY = 0;
		}
		else
		{

			Point shiftPoint;

			this.shiftX = ((OpdConnectionEdge)this.edge).getParent().getX();
			this.shiftY = ((OpdConnectionEdge)this.edge).getParent().getY();

			shiftPoint = SwingUtilities.convertPoint(
							((OpdConnectionEdge)this.edge).getParent().getParent(),
							this.shiftX, this.shiftY, this.dArea);

			this.shiftX = (int)shiftPoint.getX();
			this.shiftY = (int)shiftPoint.getY();

		}
	}

/**
 *  Recalculates dragger position repaints it and repaints the line this dragger connected to.
 */
	public synchronized void updateDragger()
	{
		Point p1;

		this._countShift();
		p1 = this.edge.getAbsoluteConnectionPoint(this.cPoint);
		this.setLocation(this.shiftX+this.edge.getX()+(int)(p1.getX() - this.getWidth()/2) ,this.shiftY+this.edge.getY()+(int)(p1.getY())-this.getHeight()/2);

		if (this.line != null)
		{
			this.line.update();
			this.updateNeighbor();
		}


	}

	protected void updateListeners()
	{
		for (Enumeration e = this.updateListeners.elements();e.hasMoreElements();)
		{
			MoveUpdatable mu = (MoveUpdatable)e.nextElement();
			mu.updateMove(this);
//            ((MoveUpdatable)e.nextElement()).updateMove();
		}

	}

/**
 *  <code>mouseClicked</code> event handler
 */
	public void mouseClicked(MouseEvent e)
	{
		if (StateMachine.isAnimated() || StateMachine.isWaiting())
		{
			return;
		}

		if (SwingUtilities.isRightMouseButton(e))
		{
			return;
		}

		if (e.getClickCount() == 2)
		{
			this.callPropertiesDialog();
		}
	}

/**
 *  <code>mouseDragged</code> event handler
 */
	public void mouseDragged(MouseEvent e)
	{
		if (StateMachine.isAnimated() || StateMachine.isWaiting())
		{
			return;
		}

		if (SwingUtilities.isRightMouseButton(e) || !this.moveable)
		{
			return;
		}

		this.wasDragged = true;

		Point convPoint;
		convPoint = SwingUtilities.convertPoint(this, e.getX(), e.getY(), (JComponent)this.edge);

		if (this.canLeave && !this.edge.isInAdjacentArea((int)convPoint.getX(), (int)convPoint.getY()))
		{
			this.tempX=this.getWidth()/2;
			this.tempY=this.getHeight()/2;

			this.setLocation(this.getX()+e.getX()-this.getWidth()/2, this.getY()+e.getY()-this.getHeight()/2);

			if (this.line != null)
			{
				this.line.update();
				this.updateNeighbor();
			}

			convPoint = SwingUtilities.convertPoint(this, e.getX(), e.getY(), this.dArea);

			OpdConnectionEdge te = GraphicsUtils.findAdjacentComponent(this.dArea, (int)convPoint.getX(), (int)convPoint.getY());


			if (te != null)
			{
				this.edge = te;
			}
			else
			{
				this.edge = this.originalEdge;
			}

			this._countShift();

			return;
		}


		if (this.edge instanceof OpdProcess)
		{
			this._setPointForOval(e);
		}
		else
		{
			this._setPointForRectangle(e);
		}

		if (this.edge == this.originalEdge)
		{
			this.originalPoint.setParam(this.cPoint.getParam());
			this.originalPoint.setSide(this.cPoint.getSide());
		}

		this.updateDragger();
		this.updateListeners();
	}


		private void _setPointForOval(MouseEvent e)
		{
			int eWidth  = this.edge.getActualWidth();
			int eHeight = this.edge.getActualHeight();
			this.cPoint.setParam((double)(this.getX()+this.getWidth()/2+e.getX()-this.tempX-this.edge.getX()-this.shiftX) / (double)(eWidth));

			if ( e.getY() + this.getHeight()/2 + this.getY() - this.tempY - this.shiftY  < this.edge.getY() + eHeight/2)
			{
				this.cPoint.setSide(OpcatConstants.N_BORDER);
				return;
			}

			if ( e.getY() + this.getHeight()/2 + this.getY() -this.tempY - this.shiftY > this.edge.getY() + eHeight/2)
			{
				this.cPoint.setSide(OpcatConstants.S_BORDER);
				return;
			}

		}

	/**
	 *  According to place <code>MouseEvent e</code> occured desides if this drugger should jump to another side of the component it connected to,
	 *  and jupms to this side.
	 */


	private void _setPointForRectangle(MouseEvent e)
	{
		int eWidth  = this.edge.getActualWidth();
		int eHeight = this.edge.getActualHeight();

		GenericTable config = this.myProject.getConfiguration();
		Integer nSize = (Integer)config.getProperty("NormalSize");
		Integer cSize = (Integer)config.getProperty("CurrentSize");

		double proximity = 12.0*cSize.doubleValue()/nSize.doubleValue();

		double xLocation = e.getX() + this.getX() + this.getWidth()/2 - this.tempX - this.shiftX;
		double yLocation = e.getY() + this.getY() +this.getHeight()/2 - this.tempY - this.shiftY;



		if ( ((int)xLocation <= this.edge.getX()+3+(int)proximity) && ((int)yLocation > this.edge.getY())
										&& ((int)yLocation < this.edge.getY() + eHeight))
		{
			this.cPoint.setSide(OpcatConstants.W_BORDER);
			this.cPoint.setParam((yLocation - this.edge.getY()) / (eHeight));
			return;
		}

		if ( ((int)xLocation >= this.edge.getX() + eWidth - 3- (int)proximity) && ((int)yLocation > this.edge.getY())
										&& ((int)yLocation < this.edge.getY() + eHeight))
		{
			this.cPoint.setSide(OpcatConstants.E_BORDER);
			this.cPoint.setParam((yLocation - this.edge.getY()) / (eHeight));
			return;
		}

		if (((int)yLocation > this.edge.getY() + eHeight/2) && (e.getX() + this.getX() - this.shiftX < this.edge.getX() + eWidth) && (xLocation  > this.edge.getX()))
		{
			this.cPoint.setSide(OpcatConstants.S_BORDER);
			this.cPoint.setParam((xLocation - this.edge.getX()) / (eWidth));
			return;
		}

		if (((int)yLocation < this.edge.getY() + eHeight/2) && (e.getX() + this.getX() - this.shiftX < this.edge.getX() + eWidth) && (xLocation > this.edge.getX()))
		{
			this.cPoint.setSide(OpcatConstants.N_BORDER);
			this.cPoint.setParam((xLocation - this.edge.getX()) / (eWidth));
			return;
		}

	}

/**
 *  <code>mouseMoved</code> event handler
 *  Empty implementation for adapting
 */
	public void mouseMoved(MouseEvent e){}

/**
 *  <code>mousePressed</code> event handler
 */
	public void mousePressed(MouseEvent e)
	{
		if (StateMachine.isAnimated() || StateMachine.isWaiting())
		{
			return;
		}

		if (!this.moveable)
		{
			return;
		}
		this._countShift();
		this.tempX=e.getX();
		this.tempY=e.getY();
		this.originalEdge = this.edge;
		this.wasDragged = false;
	}

/**
 *  <code>mouseReleased</code> event handler
 *  Empty implementation for adapting
 */
	public void mouseReleased(MouseEvent e)
	{
		if (StateMachine.isAnimated() || StateMachine.isWaiting())
		{
			return;
		}

		if(e.isPopupTrigger() || e.isMetaDown())
		{
			this.showPopupMenu(e.getX(), e.getY());
		}

		if (!this.moveable)
		{
			return;
		}
		else
		{
			this.updateDragger();
			this.updateListeners();
		}

	}

/**
 *  <code>mouseEntered</code> event handler
 */
public void mouseEntered(MouseEvent e)
{
  if (StateMachine.isWaiting() || StateMachine.isAnimated())
  {
	return;
  }

  this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
//  /** ***********************HIOTeam************** */
//  if (StateMachine.getCurrentState() == StateMachine.USUAL_DRAW) {
//  	//this.setCursor(CursorFactory.getDrawCursor());
//  }
//  /*************************HIOTeam***************/

}

/**
 *  <code>mouseExited</code> event handler<br>
 *  Empty implementation for adapting
 */
	public void mouseExited(MouseEvent e){}

/**
 *  Calls properties dialog box<br>
 *  Empty implementation for adapting
 */
	public abstract void callPropertiesDialog();

/**
 *  @return Point that this dragger connected to in coordinates of dragger
 */
	public  Point getAbsoluteConnectionPoint(RelativeConnectionPoint rPoint)
	{
		Point returnedPoint = new Point(0,0);

		switch (rPoint.getSide())
		{
			case OpcatConstants.N_BORDER:
				returnedPoint.setLocation(this.getWidth()*rPoint.getParam(), 0);
				break;
			case OpcatConstants.S_BORDER:
				returnedPoint.setLocation(this.getWidth()*rPoint.getParam(), this.getHeight());
				break;
			case OpcatConstants.E_BORDER:
				returnedPoint.setLocation(this.getWidth(), this.getHeight()*rPoint.getParam());
				break;
			case OpcatConstants.W_BORDER:
				returnedPoint.setLocation(0, this.getHeight()*rPoint.getParam());
				break;
			case BaseGraphicComponent.CENTER:
				returnedPoint.setLocation(this.getWidth()/2, this.getHeight()/2);
				break;
		}

				return returnedPoint;
	}

	public abstract void paintComponent(Graphics g);

/**
 *  Updates the dragger on the other edge of the line
 */
	private void updateNeighbor()
	{
		if (this.line == null) {
			return;
		}

		Connectable neighbor = this.line.getNeighbor(this);

		if (neighbor instanceof AroundDragger)
		{
				  ((AroundDragger)neighbor).repaint();
		}
	}

/**
 *  @return OPD graphic component this dragger connected to
 */
	public Connectable getEdge()
	{
		return this.edge;
	}

/**
 *  @return The relation of coordinates of a <code>mouseClick</code> event to length of the side.
 */
	public double getParam()
	{
		return this.cPoint.getParam();
	}

	public void setParam(double param)
	{
		this.cPoint.setParam(param);
	}

/**
 *  @return The side OPD graphic component this dragger connected to.
 */
	public int getSide()
	{
		return this.cPoint.getSide();
	}

	public void setSide(int side)
	{
		this.cPoint.setSide(side);
	}

	public double getRotationAngle()
	{
//		Point p, head, tail;
//
//		p = line.getUpperPoint();
//		if ( (line.getX() + p.getX()) >= getX() && (line.getX() + p.getX()) <= (getX() + getWidth()) &&
//			(line.getY() + p.getY()) >= getY() && (line.getY() + p.getY()) <= (getY() + getHeight()) )
//		{
//			head = p;
//			tail = line.getLowerPoint();
//		}
//		else
//		{
//			tail = p;
//			head = line.getLowerPoint();
//		}
//
//		return GraphicsUtils.calcutateRotationAngle(head, tail);

		return this.line.getRotationAngle(this);
	}

	protected Graphics2D getRotatedGraphics2D(Graphics g)
	{
		double angle = this.getRotationAngle();

		Graphics2D g2 = (Graphics2D)g;
		Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);

		g2.rotate(angle, this.getWidth()/2, this.getHeight()/2);

		return g2;
	}

		public int getActualWidth()
		{
		  return this.getWidth();
		}

	public int getActualHeight()
	{
		return this.getHeight();
	}

	public boolean contains(int x, int y)
	{
		if(this.line == null)
		{
			return false;
		}
		double angle = this.getRotationAngle();
		DoublePoint center = new DoublePoint(this.getWidth()/2, this.getWidth()/4);
		center.rotate(angle, new Point(this.getWidth()/2, this.getHeight()/2));
		double click = (x-center.getX())*(x-center.getX())+(y-center.getY())*(y-center.getY());
		if(click < this.getWidth()*this.getWidth()/4)
		{
			return true;
		}
		return false;
	}

	/**
	 * Sets the component state. Sets <code>OpdProject.setSystemChangeProbability</code>
	 * to true. If a component was selected, there is high probability that something
	 * in the system was changed.
	 * @see OpdProject#setSystemChangeProbability
	 * */
	public void setSelected(boolean newSelected)
	{
		this.selected = newSelected;
	}

	public boolean isSelected()
	{
		return this.selected;
	}

	protected void drawSelection(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		Stroke oldStroke = g2.getStroke();

		g2.setStroke(new BasicStroke());

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer)config.getProperty("CurrentSize")).doubleValue();
		double normalSize = ((Integer)config.getProperty("NormalSize")).doubleValue();
		double factor = currentSize/normalSize;
		int selOffset = Math.round((float)(SELECTION_OFFSET*factor));
		int dSelOffset = 2*selOffset;

		g2.setColor(Color.white);
		g2.fillRect(this.getWidth()/2-selOffset, this.getHeight()/2-selOffset, dSelOffset, dSelOffset);
		g2.setColor(Color.black);
		g2.drawRect(this.getWidth()/2-selOffset, this.getHeight()/2-selOffset, dSelOffset, dSelOffset);

		g2.setStroke(oldStroke);
	}

	public boolean isInAdjacentArea(int x, int y)
	{
		return this.contains(x,y);
	}


	public abstract void showPopupMenu(int pX, int pY);

}
