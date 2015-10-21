package gui.opdGraphics.draggers;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.dialogs.LinkPropertiesDialog;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmExceptionLink;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;


/**
 *  Graphic represenrartion of OPM Exception Link.<br>
 *  <p>
 *  Actually it is an <a href = "AroundDragger.html><code>AroundDragger</code></a> with implemented <code>paint()<code> method.
 *  Graphicaly every link consist of three components: two subclasses of <a href = "AroundDragger.html><code>AroundDragger</code></a> and one subclass of
 *  <a href = "ObdLine.html"><code>OpdLine</code></a> connecting them.<br></p>
 *  <p>If graphic representation has one edge of the link mark, like arrow or disk or disk with letter inside, the second edge is
 *  <a href = "TransparentAroundDragger.html"><code>TransparentAroundDragger</code></a> that has no any graphics. Otherwise
 *  both adges of the link has some graphic drawing.<br></p>
 *  <p>Exception link consist of <code>OpdExceptionLink</code> on one edge, <a href = "TransparentAroundDragger.html"><code>TransparentAroundDragger</code></a>
 *  on second edge and <a href = "OpdSimpleLine.html><code>OpdSimpleLine</code></a> connecting them.
 */
public class OpdExceptionLink extends OpdLink
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 
	protected Font currentFont;
	protected FontMetrics currentMetrics;

	/**
	 *  <strong>No default constructor for this class.</strong><br>
	 *  Constructs OpdExceptionLink (subclass of <a href = "AroundDragger.html><code>AroundDragger</code></a>).
	 *  @param <code>Edge1 <a href = "Connetable.html">Connectable</a></code>, <a href = "OpdProcess.html"><code>OpdProcess</code></a> this link linked to.
	 *  @param <code>pSide1 int</code>, the side of the component as spacified in <a href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 *  @param <code>pParam1 double</code>, connection parametr -- the position of connection point on component's side.
	 *  @param <code>pProject OpdProject</code>, OPD project this link belongs to.
	 *  @param <code>pLink OpmException</code>, logical representation of the link, for more information see <code>opmEntities.OpmExceptionLink</code>.
	 */
	public OpdExceptionLink(Connectable pEdge, RelativeConnectionPoint pPoint, OpmExceptionLink pLink,
											long pOpdId, long pEntityInOpdId, OpdProject pProject)
	{
		super(pEdge, pPoint, pLink, pOpdId, pEntityInOpdId, pProject);
		this.currentFont = new Font("OurFont",Font.PLAIN,10);
		this.currentMetrics = this.getFontMetrics(this.currentFont);
		this.updateDragger();
	}

	/**
	 *  Draws link.
	 */
	public void  paintComponent(Graphics g)
	{
		Point p, head, tail;
		Graphics2D g2nc = (Graphics2D)g;
		AffineTransform at = (g2nc.getTransform());

		Graphics2D g2 = (Graphics2D)g;
		Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);
		g2.setStroke(new BasicStroke(1.5f));

		p = this.line.getUpperPoint();
		if ( ((this.line.getX() + p.getX()) >= this.getX()) && ((this.line.getX() + p.getX()) <= (this.getX() + this.getWidth())) &&
			((this.line.getY() + p.getY()) >= this.getY()) && ((this.line.getY() + p.getY()) <= (this.getY() + this.getHeight())) )
		{
			head = p;
			tail = this.line.getLowerPoint();
		}
		else
		{
			tail = p;
			head = this.line.getLowerPoint();
		}

		double angle = GraphicsUtils.calcutateRotationAngle(head, tail);

		g2.rotate(angle, this.getWidth()/2, this.getHeight()/2);

		g2.setColor(this.borderColor);
		g2.drawLine(this.getWidth()*3/14, this.getHeight()/7, this.getWidth()*11/14, this.getHeight()*2/7);
		if(this.isSelected())
		{
			g2.setStroke(new BasicStroke(1.0f));
			g2.setTransform(at);
			this.drawSelection(g2);
		}
	}

	/**
	 *  Creates and shows Exception Link properties dialog for given link.
	 */
		public void callPropertiesDialog()
		{
		  LinkPropertiesDialog ld = new LinkPropertiesDialog(this.myLink, new OpdKey(this.getOpdId(), this.getEntityInOpdId()), this.myProject, "Exception");
		  ld.setVisible(true);
		}
}
