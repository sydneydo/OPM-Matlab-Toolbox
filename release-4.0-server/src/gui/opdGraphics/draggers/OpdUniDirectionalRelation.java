package gui.opdGraphics.draggers;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.dialogs.GeneralUniDirRelationPropertiesDialog;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmUniDirectionalRelation;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * <p>
 * This class is graphic representation of Opd Uni Directional Relation, due to
 * its look and meaning, its graphically very close to OPD link.
 * </p>
 * <p>
 * Actually it is an <a href = "AroundDragger.html><code>AroundDragger</code></a>
 * with implemented <code>paint()<code> method.
 *  Graphicaly general relation, like link, consist of three components: two subclasses of <a href = "AroundDragger.html><code>AroundDragger</code></a> and one subclass of
 *  <a href = "ObdLine.html"><code>OpdLine</code></a> connecting them.
 *  Both edges of the link has same graphic drawing.<br></p>
 *  <p>UniDirecional Relation consist of <code>OpdUniDirectionalRelation</code> on both edges,
 *  and <a href = "OpdSimpleLine.html><code>OpdSimpleLine</code></a> connecting them.
 */
public class OpdUniDirectionalRelation extends OpdGeneralRelation {
	 

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
	 * <strong>No default constructor for this class.</strong><br>
	 * Constructs OpdAgentLink (subclass of <a href = "AroundDragger.html><code>AroundDragger</code></a>).
	 * 
	 * @param <code>Edge1 <a href = "Connetable.html">Connectable</a></code>,
	 *            <a href = "OpdProcess.html"><code>OpdProcess</code></a>
	 *            this link linked to.
	 * @param <code>pSide1 int</code>, the side of the component as spacified in
	 *            <a href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 * @param <code>pParam1 double</code>, connection parametr -- the position
	 *            of connection point on component's side.
	 * @param <code>pProject OpdProject</code>, OPD project this link belongs
	 *            to.
	 * @param <code>pRelation OpmUniDirectionalRelation</code>, logical
	 *            representation of the link, for more information
	 *            <code>opmEntities.OpmUniDirectionalRelation</code>.
	 */
	public OpdUniDirectionalRelation(Connectable pEdge,
			RelativeConnectionPoint pPoint,
			OpmUniDirectionalRelation pRelation, long pOpdId,
			long pEntityInOpdId, OpdProject pProject) {
		super(pEdge, pPoint, pRelation, pOpdId, pEntityInOpdId, pProject);
		this.updateDragger();
	}

	/**
	 * Draws <code>OpdUniDirectionalRelation</code>.
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2nc = (Graphics2D) g;
		AffineTransform at = (g2nc.getTransform());

		Graphics2D g2 = this.getRotatedGraphics2D(g);

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		g2.setColor(this.borderColor);
		g2.setStroke(new BasicStroke((float) factor * 1.5f));
		g2.drawLine(this.getWidth() * 2 / 6, this.getHeight() / 9, this.getWidth() / 2,
				this.getHeight() / 2);
		g2.drawLine(this.getWidth() * 4 / 6, this.getHeight() / 9, this.getWidth() / 2,
				this.getHeight() / 2);
		if (this.isSelected()) {
			g2.setStroke(new BasicStroke(1.0f));
			g2.setTransform(at);
			this.drawSelection(g2);
		}
	}

	/**
	 * Creates and shows Uni Directional Relation properties dialog for given
	 * relation.
	 */
	public void callPropertiesDialog() {
		GeneralUniDirRelationPropertiesDialog gd = new GeneralUniDirRelationPropertiesDialog(
				(OpmUniDirectionalRelation) this.getEntity(), new OpdKey(this.getOpdId(),
						this.getEntityInOpdId()), this.myProject);
		gd.setVisible(true);
	}

}
