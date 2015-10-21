package gui.opdGraphics.draggers;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.dialogs.LinkPropertiesDialog;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmInvocationLink;

import java.awt.Font;
import java.awt.FontMetrics;


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
public class OpdInvocationLink extends ArrowLink
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
	public OpdInvocationLink(Connectable pEdge, RelativeConnectionPoint pPoint, OpmInvocationLink pLink,
											 long pOpdId, long pEntityInOpdId, OpdProject pProject)
	{
		super(pEdge, pPoint, pLink,pOpdId, pEntityInOpdId, pProject);
	}


	/**
	 *  Creates and shows Exception Link properties dialog for given link.
	 */
	public void callPropertiesDialog()
	{
		LinkPropertiesDialog ld = new LinkPropertiesDialog(this.myLink, new OpdKey(this.getOpdId(), this.getEntityInOpdId()), this.myProject, "Invocation");
		ld.setVisible(true);
	}

}
