package gui.opdGraphics.draggers;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.Connectable;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;

import java.awt.Graphics;

/**
 *  This class implements <a href = "AroundDragger.html"><code>AroundDragger</code></a>
 *  It's <code>paint()</code> method does nothing, it means, that <code>TransparentAroundDragger</code> exist, and acts like
 *  any other subclass of <a href = "AroundDragger.html"><code>AroundDragger</code></a>, but it is not visible.
 */
public class TransparentAroundDragger extends AroundDragger
{

	/**
	 * 
	 */
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *  <strong>There is no default constructor</strong>
	 *  @param <code>pEdge1</code> -- OPD graphic component this dragger connected to.
	 *  @param <code>pSide1</code> -- The side OPD graphic component this dragger connected to.
	 *  @param <code>pParam1</code> -- The relation of coordinates of a <code>mouseClick</code> event to length of the side.
	 */
	public TransparentAroundDragger(Connectable pEdge, RelativeConnectionPoint pPoint, OpdProject pProject)
	{
		super(pEdge, pPoint, pProject);

		GenericTable config = pProject.getConfiguration();
		double normalSize = ((Integer)config.getProperty("NormalSize")).doubleValue();
		double currentSize = ((Integer)config.getProperty("CurrentSize")).doubleValue();
		double factor = currentSize/normalSize;
		double size = ((Integer)config.getProperty("DraggerSize")).intValue()*factor;


		this.setSize((int)size,(int)size);

		super.updateDragger();
	}



	/**
	 *  <code>paint()</code> method that does nothing.
	 */
	public void paintComponent(Graphics g)
	{
		if(this.isSelected()) {
			this.drawSelection(g);
		}
	}

	public void callPropertiesDialog() {}
	public void showPopupMenu(int pX, int pY){}

}
