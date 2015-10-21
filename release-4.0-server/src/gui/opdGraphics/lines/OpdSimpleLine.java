package gui.opdGraphics.lines;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.Opcat2;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.dialogs.GeneralBiDirRelationPropertiesDialog;
import gui.opdGraphics.dialogs.GeneralUniDirRelationPropertiesDialog;
import gui.opdGraphics.dialogs.LinkPropertiesDialog;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.popups.FundamentalRelationPopup;
import gui.opdGraphics.popups.GeneralRelationPopup;
import gui.opdGraphics.popups.LinkPopup;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmAgent;
import gui.opmEntities.OpmBiDirectionalRelation;
import gui.opmEntities.OpmConditionLink;
import gui.opmEntities.OpmConsumptionEventLink;
import gui.opmEntities.OpmConsumptionLink;
import gui.opmEntities.OpmEffectLink;
import gui.opmEntities.OpmEntity;
import gui.opmEntities.OpmExceptionLink;
import gui.opmEntities.OpmFundamentalRelation;
import gui.opmEntities.OpmGeneralRelation;
import gui.opmEntities.OpmInstrument;
import gui.opmEntities.OpmInstrumentEventLink;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmResultLink;
import gui.opmEntities.OpmUniDirectionalRelation;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.FundamentalRelationInstance;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;



/**
 *  <p>This class is a simple line that can be arranged or not arranged.
 *  Arranged line is drawn as number of lines which are perpendicular each to other.
 *  Not arranged line is drawn as stright line connecting two points.
 */
public class OpdSimpleLine extends OpdLine
{
	/**
	 * 
	 */
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Constructs an <code>OpdSimpleLine</code> component.
	 *  It's an abstract class and cannot be instantinated directly.
	 *  @param <code>pEdge1 Connectable</code>, one edge of Opd Link a subclass of <a href = "AroundDragger.html"><code>AroundDragger</code></a>.
	 *  @param <code>pSide1 int</code>, the border of first edge as defined in <a href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 *  @param <code>pParam1 double</code>, connection parameter for positioning the connection point on the component's border.
	 *  @param <code>pEdge2 Connectable</code>, second edge of Opd Link a subclass of <a href = "AroundDragger.html"><code>AroundDragger</code></a>.
	 *  @param <code>pSide2 int</code>, the border of first edge as defined in <a href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>.
	 *  @param <code>pParam2 double</code>, connection parameter for positioning the connection point on the component's border.
	 */
	public OpdSimpleLine(Connectable pEdge1, RelativeConnectionPoint cPoint1,
				   Connectable pEdge2, RelativeConnectionPoint cPoint2,
				   OpmEntity pEntity, OpdKey key, OpdProject pProject)
	{
		super(pEdge1, cPoint1, pEdge2, cPoint2, pEntity, key ,pProject);
	}


	/**
	 *  Paints line component according to <code>arranged</code> property.
	 *  If arranged line is drawn as number of lines which are perpendicular each to other.
	 *  Otherwise line is drawn as stright line connecting two points.
	 */
	public void paintComponent(Graphics g)
	{
		int x1, y1, x2, y2;
		Point p1, p2;

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer)config.getProperty("CurrentSize")).doubleValue();
		double normalSize = ((Integer)config.getProperty("NormalSize")).doubleValue();
		double factor = currentSize/normalSize;


		Graphics2D g2 = (Graphics2D)g;
		Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);

		if (this.isDashed())
		{
			g2.setStroke(new BasicStroke((float)factor*1.7f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[]{6}, 0.0f));
		}
		else
		{
			g2.setStroke(new BasicStroke((float)factor*1.7f));
		}

//        System.err.println((float)factor*1.8f);

		g2.setColor(this.lineColor);

		p1 = this.getUpperPoint();
		p2 = this.getLowerPoint();
		x1 = (int)p1.getX();
		y1 = (int)p1.getY();
		x2 = (int)p2.getX();
		y2 = (int)p2.getY();
		g2.drawLine(x1, y1, x2, y2);


	}

	public void callPropertiesDialog()
	{
	  String title = " ";

	  if (this.myEntity instanceof OpmProceduralLink)
	  {
		if (this.myEntity instanceof OpmAgent) {
			title = "Agent";
		}
		if (this.myEntity instanceof OpmConditionLink) {
			title = "Condition";
		}
		if (this.myEntity instanceof OpmConsumptionLink) {
			title = "Consumption";
		}
		if (this.myEntity instanceof OpmEffectLink) {
			title = "Effect";
		}
		if (this.myEntity instanceof OpmInstrumentEventLink) {
			title = "Event";
		}
		if (this.myEntity instanceof OpmExceptionLink) {
			title = "Exception";
		}
		if (this.myEntity instanceof OpmResultLink) {
			title = "Result";
		}
		if (this.myEntity instanceof OpmInstrument) {
			title = "Instrument";
		}
		if (this.myEntity instanceof OpmConsumptionEventLink) {
			title = "Consumption Event Event";
		}

		LinkPropertiesDialog ld = new LinkPropertiesDialog((OpmProceduralLink)this.myEntity, this.myKey , this.myProject, title);
		ld.setVisible(true);
		return;
	  }

	  if (this.myEntity instanceof OpmUniDirectionalRelation)
	  {
		GeneralUniDirRelationPropertiesDialog gUniD = new GeneralUniDirRelationPropertiesDialog((OpmUniDirectionalRelation)this.myEntity, this.myKey, this.myProject);
		gUniD.setVisible(true);
		return;
	  }

	  if (this.myEntity instanceof OpmBiDirectionalRelation)
	  {
		GeneralBiDirRelationPropertiesDialog gBiD = new GeneralBiDirRelationPropertiesDialog((OpmBiDirectionalRelation)this.myEntity, this.myKey, this.myProject);
		gBiD.setVisible(true);
		return;
	  }

		if (this.myEntity instanceof OpmFundamentalRelation)
		{
			FundamentalRelationEntry myEntry = (FundamentalRelationEntry)this.myProject.getComponentsStructure().getEntry(this.myEntity.getId());
			FundamentalRelationInstance myIns = (FundamentalRelationInstance)myEntry.getInstance(this.myKey);
			myIns.getGraphicalRelationRepresentation().getRelation().callPropertiesDialog(
						BaseGraphicComponent.SHOW_ALL_TABS, BaseGraphicComponent.SHOW_ALL_BUTTONS);
		}

	}

	public void showPopupMenu(int x, int y)
	{
		JPopupMenu jpm = null;
		if (this.myEntity instanceof OpmProceduralLink)
		{
			jpm = new LinkPopup(this.myProject, this.myProject.getComponentsStructure().getEntry(this.myEntity.getId()).getInstance(this.myKey));
		}

		else if (this.myEntity instanceof OpmGeneralRelation)
		{
			jpm = new GeneralRelationPopup(this.myProject, this.myProject.getComponentsStructure().getEntry(this.myEntity.getId()).getInstance(this.myKey));
		}

		else if (this.myEntity instanceof OpmFundamentalRelation)
		{
			jpm = new FundamentalRelationPopup(this.myProject, this.myProject.getComponentsStructure().getEntry(this.myEntity.getId()).getInstance(this.myKey));
		}

		else
		{
			JOptionPane.showMessageDialog(Opcat2.getFrame(),
					"Internal error occured in\nopdGraphics.lines.TextLine.showPopupMenu()",
					"Opcat2 - Internal Error", JOptionPane.ERROR_MESSAGE);
		}
		jpm.show(this, x, y);
		return;
	}

}
