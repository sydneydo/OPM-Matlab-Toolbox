package gui.opdGraphics.draggers;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmProceduralLink;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


/**
 *  <p>Arrow link is a class that represents link which graphic representation is an arrow.</p>
 *
 *
 */
public abstract class ArrowLink extends OpdLink
{

/**
	 * 
	 */
	private static final long serialVersionUID = -7605933455597134686L;


/**
 *  <strong>There is no default constructor</strong>
 *  <p>All extending classes have to call <code>super()</code> in their constructors.</p>
 *  @param <code>pEdge1</code> -- OPD graphic component this dragger connected to.
 *  @param <code>pSide1</code> -- The side OPD graphic component this dragger connected to.
 *  @param <code>pParam1</code> -- The relation of coordinates of a <code>mouseClick</code> event to length of the side.
 */
	public ArrowLink(Connectable pEdge, RelativeConnectionPoint pPoint, OpmProceduralLink pLink,
									 long pOpdId, long pEntityInOpdId, OpdProject pProject)

	{
		super(pEdge, pPoint, pLink, pOpdId, pEntityInOpdId, pProject);
		this.updateDragger();
	}


	public void paintComponent(Graphics g)
	{
		Graphics2D g2nc = (Graphics2D)g;
		AffineTransform at = (g2nc.getTransform());
		Graphics2D g2 = this.getRotatedGraphics2D(g);
		// the default arrow position (without any rotation) is DOWN
		//                 |
		//               \ |/
		//                \/

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer)config.getProperty("CurrentSize")).doubleValue();
		double normalSize = ((Integer)config.getProperty("NormalSize")).doubleValue();
		double factor = currentSize/normalSize;


		int Xs[] = {this.getWidth()*2/6, this.getWidth()/2, this.getWidth()*4/6, this.getWidth()/2};
		int Ys[] = {this.getHeight()/9, this.getHeight()/2, this.getHeight()/9, this.getHeight()*2/9};

		g2.setColor(this.backgroundColor);
		g2.fillPolygon(Xs, Ys, 4);
		g2.setColor(this.borderColor);
		g2.setStroke(new BasicStroke((float)factor*1.5f));
		g2.drawPolygon(Xs, Ys, 4);
		if(this.isSelected())
		{
			g2.setStroke(new BasicStroke(1.0f));
			g2.setTransform(at);
			this.drawSelection(g2);
		}
	}

}
