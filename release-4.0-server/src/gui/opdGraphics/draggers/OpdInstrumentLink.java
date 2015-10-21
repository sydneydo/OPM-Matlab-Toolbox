package gui.opdGraphics.draggers;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.DoublePoint;
import gui.opdGraphics.dialogs.LinkPropertiesDialog;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmInstrument;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;


/**
 *  Graphic represenrartion of OPM Instrument Link.<br>
 *  <p>
 *  Actually it is an <a href = "AroundDragger.html><code>AroundDragger</code></a> with implemented <code>paint()<code> method.
 *  Graphicaly every link consist of three components: two subclasses of <a href = "AroundDragger.html><code>AroundDragger</code></a> and one subclass of
 *  <a href = "OpdLine.html"><code>OpdLine</code></a> connecting them.<br></p>
 *  <p>If graphic representation has one edge of the link mark, like arrow or disk or disk with letter inside, the second edge is
 *  <a href = "TransparentAroundDragger.html"><code>TransparentAroundDragger</code></a> that has no any graphics. Otherwise
 *  both adges of the link has some graphic drawing.<br></p>
 *  <p>Instrument link consist of <code>OpdInstrumentLink</code> on one edge, <a href = "TransparentAroundDragger.html"><code>TransparentAroundDragger</code></a>
 *  on second edge and <a href = "OpdSimpleLine.html><code>OpdSimpleLine</code></a> connecting them.
 */
public class OpdInstrumentLink extends OpdLink
{

	/**
	 * 
	 */
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  <strong>No default constructor for this class.</strong><br>
	 *  Constructs OpdAgentLink (subclass of <a href = "AroundDragger.html><code>AroundDragger</code></a>).
	 *  @param <code>Edge1 <a href = "Connetable.html">Connectable</a></code>, <a href = "OpdProcess.html"><code>OpdProcess</code></a> this link linked to.
	 *  @param <code>pSide1 int</code>, the side of the component as spacified in <a href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 *  @param <code>pParam1 double</code>, connection parametr -- the position of connection point on component's side.
	 *  @param <code>pProject OpdProject</code>, OPD project this link belongs to.
	 *  @param <code>pLink OpmInstrumentLink</code>, logical representation of the link, for more information see <code>opmEntities.OpmInstrumentLink</code>.
	 */
	public OpdInstrumentLink(Connectable pEdge, RelativeConnectionPoint pPoint, OpmInstrument pLink,
												  long pOpdId, long pEntityInOpdId, OpdProject pProject)
	{
		super(pEdge, pPoint, pLink, pOpdId, pEntityInOpdId, pProject);
		this.updateDragger();
	}

	/**
	 *  Draws link.
	 */
	public void  paintComponent(Graphics g)
	{
		double angle = this.getRotationAngle();

		double normalSize = ((Integer)this.myProject.getConfiguration().getProperty("NormalSize")).doubleValue();
		double currentSize = ((Integer)this.myProject.getConfiguration().getProperty("CurrentSize")).doubleValue();
		double factor = currentSize/normalSize;
		double size = 10*factor;

		DoublePoint center = new DoublePoint( this.getWidth()/2, (this.getHeight()-size+1.0)/2 );
		center.rotate(angle, new Point(this.getWidth()/2, this.getHeight()/2));

		Graphics2D g2 = (Graphics2D)g;
		Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);

		g2.setColor(this.backgroundColor);
		g2.fillOval((int)Math.round(center.getX()-size/2), (int)Math.round(center.getY()-size/2), (int)Math.round(size), (int)Math.round(size));
		g2.setColor(this.borderColor);
		g2.setStroke(new BasicStroke(1.5f));
		g2.drawOval((int)Math.round(center.getX()-size/2), (int)Math.round(center.getY()-size/2), (int)Math.round(size), (int)Math.round(size));
		if(this.isSelected()) {
			this.drawSelection(g2);
		}
	}

	/**
	 *  Creates and shows Instrument Link properties dialog for given link.
	 */
		public void callPropertiesDialog()
		{
		  LinkPropertiesDialog ld = new LinkPropertiesDialog(this.myLink, new OpdKey(this.getOpdId(), this.getEntityInOpdId()), this.myProject, "Instrument");
		  ld.setVisible(true);
		}

}
