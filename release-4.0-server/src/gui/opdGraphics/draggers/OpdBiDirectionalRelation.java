package gui.opdGraphics.draggers;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.dialogs.GeneralBiDirRelationPropertiesDialog;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmBiDirectionalRelation;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


/**
 *  <p>This class is graphic representation of Opd Bi Directional Relation, due to its look
 *  and meaning, its graphically very close to OPD link. </p>
 *  <p>Actually it is an <a href = "AroundDragger.html><code>AroundDragger</code></a> with implemented <code>paint()<code> method.
 *  Graphicaly general relation, like link, consist of three components: two subclasses of <a href = "AroundDragger.html><code>AroundDragger</code></a> and one subclass of
 *  <a href = "OpdLine.html"><code>OpdLine</code></a> connecting them.
 *  Both edges of the link has same graphic drawing.<br></p>
 *  <p>BiDirecional Relation consist of <code>OpdBiDirectionalRelation</code> on both edges,
 *  and <a href = "OpdSimpleLine.html><code>OpdSimpleLine</code></a> connecting them.
 */

public class OpdBiDirectionalRelation extends OpdGeneralRelation
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
	 *  @param <code>pRelation OpmBiDirectionalRelation</code>, logical representation of the link, for more information <code>opmEntities.OpmBiDirectionalRelation</code>.
	 */
	public OpdBiDirectionalRelation(Connectable pEdge, RelativeConnectionPoint pPoint, OpmBiDirectionalRelation pRelation,
							long pOpdId, long pEntityInOpdId, OpdProject pProject)
	{
		super(pEdge, pPoint, pRelation, pOpdId, pEntityInOpdId, pProject);
		this.updateDragger();
	}


	/**
	 *  Draws <code>OpdBiDirectionalRelation</code>.
	 */
	public void  paintComponent(Graphics g)
	{
		Graphics2D g2nc = (Graphics2D)g;
		AffineTransform at = (g2nc.getTransform());

		Graphics2D g2 = this.getRotatedGraphics2D(g);

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer)config.getProperty("CurrentSize")).doubleValue();
		double normalSize = ((Integer)config.getProperty("NormalSize")).doubleValue();
		double factor = currentSize/normalSize;


		g2.setColor(this.borderColor);
		g2.setStroke(new BasicStroke((float)factor*1.5f));
		//g2.drawLine(getWidth()*2/6, getHeight()/9, getWidth()/2, getHeight()/2);
		g2.drawLine(this.getWidth()*4/6, this.getHeight()/9, this.getWidth()/2, this.getHeight()/2);
		if(this.isSelected())
		{
			g2.setStroke(new BasicStroke(1.0f));
			g2.setTransform(at);
			this.drawSelection(g2);
		}
	}


	/**
	 *  Creates and shows Bi Directional Relation properties dialog for given relation.
	 */
	public void callPropertiesDialog()
	{
		GeneralBiDirRelationPropertiesDialog gd = new GeneralBiDirRelationPropertiesDialog((OpmBiDirectionalRelation)this.getEntity(), new OpdKey(this.getOpdId(), this.getEntityInOpdId()), this.myProject);
		gd.setVisible(true);
	}
}
