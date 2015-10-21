package gui.opdGraphics.opdBaseComponents;

import gui.opdGraphics.dialogs.RelationPropertiesDialog;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmAggregation;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;



/**
 *  <p>This class defines Aggregation relation as specified in OPM documentation
 *  Graphicaly each fundamental relation is a triangle (with a mark inside) connected with lines
 *  to <strong>one</strong> source and <strong>number</strong> of destinations (may be 1 or more).
 *  Logicaly each pair source - destination constructs relation.</p><br>
 *  <p>The difference in logic and graphic representattion is for better visual cognition.</p>
 */
public class OpdAggregation extends OpdFundamentalRelation
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
	 *  Constructs OpdAggregation relation between objects.
	 *  @param <code>mAgg, <a href = "OpmAggregation.html">OpmAggregation</a></code> logical representation of OPM Aggregation link
	 *  @param <code>pProject OpdProject</code>, OPD project this relation belongs to.
	 *  @param <code>pOpdId long</code>, uniqe identifier for the <a href = "../opdProject/Opd.html"><code>Opd</code></a> this Aggregation relation added to.
	 *  @param <code>pEntity InOpdIdlong</code>, uniqe identifier for the component withing current <a href = "../opdProject/Opd.html"><code>Opd</code></a>
	 */
	public	OpdAggregation(OpmAggregation mAgg, OpdProject pProject, long pOpdId, long pEntityInOpdId)
	{
		  super(pOpdId, pEntityInOpdId, pProject);
	}
	/**
	 * paints this component.
	 */
	public void paintComponent(Graphics g)
	{
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer)config.getProperty("CurrentSize")).doubleValue();
		double normalSize = ((Integer)config.getProperty("NormalSize")).doubleValue();
		double factor = currentSize/normalSize;

        int selOffset = Math.round((float)(SELECTION_OFFSET*factor));


		int xS[] = {selOffset,(int)Math.round((double)(this.getWidth()-1)/2), this.getWidth()-selOffset-1};
		int yS[] = {this.getHeight()-selOffset-1, selOffset, this.getHeight()-selOffset-1};

		Graphics2D g2 = (Graphics2D)g;
		Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);
		g2.setStroke(new BasicStroke((float)factor*1.8f));

		g2.setColor(this.borderColor);
		g2.fillPolygon(xS,yS,3);
		g2.drawPolygon(xS,yS,3);
        if(this.isSelected()) {
			this.drawSelection(g2);
		}
	}

	public  void callPropertiesDialog(int showTabs, int showButtons)
	{
		RelationPropertiesDialog rd = new RelationPropertiesDialog(this.graphicalEntry, "Aggregation-Participation", true);
		rd.showDialog();
	}
	
	public void callShowUrl()
	{
		
	}
}
