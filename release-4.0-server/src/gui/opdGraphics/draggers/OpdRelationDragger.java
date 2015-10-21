package gui.opdGraphics.draggers;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPIx.IXCheckResult;
import gui.Opcat2;
import gui.checkModule.CheckResult;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.OpdCardinalityLabel;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.popups.GeneralRelationPopup;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.opmEntities.OpmGeneralRelation;
import gui.projectStructure.Entry;
import gui.projectStructure.GeneralRelationEntry;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.Instance;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;


public abstract class  OpdRelationDragger extends AroundDragger
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4787065027648159363L;
	protected OpdCardinalityLabel label;
	private int dX;
	private int dY;

	protected OpmGeneralRelation myRelation;

/**
 *  An ID of the OPD this dragger belongs to
 */
	protected long opdId;
	protected long entityInOpdId;


	public OpdRelationDragger(Connectable pEdge, RelativeConnectionPoint pPoint, OpmGeneralRelation pRelation,
											  long pOpdId, long pEntityInOpdId, OpdProject pProject)
	{
		super(pEdge, pPoint, pProject);
		this.myRelation = pRelation;
		this.myProject = pProject;
		this.opdId = pOpdId;
		this.entityInOpdId = pEntityInOpdId;
		this.setCanLeave(true);

		super.updateDragger();
		this.label = new OpdCardinalityLabel(null, pProject);
		this.dX = 20;
		this.dY = 20;
		this.label.addMouseListener(this.label);
		this.label.addMouseMotionListener(this.label);
		this.label.setLocation(this.getX()+this.dX, this.getY() + this.dY);

		GenericTable config = this.myProject.getConfiguration();
		double normalSize = ((Integer)config.getProperty("NormalSize")).doubleValue();
		double currentSize = ((Integer)config.getProperty("CurrentSize")).doubleValue();
		double factor = currentSize/normalSize;
		double size = ((Integer)config.getProperty("DraggerSize")).intValue()*factor;
		this.setSize((int)size,(int)size);

	}

		public OpdCardinalityLabel getOpdCardinalityLabel()
		{
		  return this.label;
		}

		public void setText(String text)
		{
		  this.label.setText(text);
		}

		public String getText()
		{
		  return this.label.getText();
		}

	public void mouseDragged(MouseEvent e)
	{
        if (StateMachine.isAnimated() || StateMachine.isWaiting())
        {
            return;
        }

		this.dX = this.label.getX() - this.getX();
		this.dY = this.label.getY() - this.getY();
		super.mouseDragged(e);
		Point convPoint;
		convPoint = SwingUtilities.convertPoint(this, e.getX(), e.getY(), (JComponent)this.edge);
		if (this.isCanLeave() && !this.edge.isInAdjacentArea((int)convPoint.getX(), (int)convPoint.getY()))
		{
		  this.label.setLocation(this.getX() + this.dX, this.getY() + this.dY);
		}
	}

	public void updateDragger()
	{
		this.dX = this.label.getX() - this.getX();
		this.dY = this.label.getY() - this.getY();
		super.updateDragger();
		this.label.setLocation(this.getX() + this.dX, this.getY() + this.dY);
	}

		public OpmGeneralRelation getEntity()
	{
		  return this.myRelation;
	}

	public long getOpdId()
	{
		  return this.opdId;
	}

	public long getEntityInOpdId()
	{
		  return this.entityInOpdId;
	}

	//NOTE: I think this code should move to AroundDragger
	public void mousePressed(MouseEvent e)
	{
        if (StateMachine.isWaiting())
        {
            return;
        }

		  Entry myEntry = this.myProject.getComponentsStructure().getEntry(this.myRelation.getId());
		  Instance myInstance = myEntry.getInstance(new OpdKey(this.opdId, this.entityInOpdId));

		  if(e.isShiftDown())
		  {
			  if(myInstance.isSelected())
			  {
				  this.myProject.removeSelection(myInstance);
			  }
			  else
			  {
				  this.myProject.addSelection(myInstance, !e.isShiftDown());
			  }
		  }
		  else
		  {
			  if(myInstance.isSelected())
			  {
				  this.myProject.addSelection(myInstance, false);
			  }
			  else
			  {
				  this.myProject.addSelection(myInstance, true);
			  }
		  }
		  this.repaint();

          if (StateMachine.isAnimated())
          {
              return;
          }


		  super.mousePressed(e);
		  return;

	}

	public void mouseReleased(MouseEvent e)
	{
        if (StateMachine.isAnimated() || StateMachine.isWaiting())
        {
            return;
        }

		super.mouseReleased(e);

		if (!this.moveable)
		{
			return;
		}

		if (this.edge!=this.originalEdge)
		{
			GeneralRelationEntry gre = (GeneralRelationEntry)this.myProject.getComponentsStructure().getEntry(this.myRelation.getId());
			GeneralRelationInstance gri = (GeneralRelationInstance)gre.getInstance(new OpdKey(this.opdId, this.entityInOpdId));

			CheckResult cr = this.myProject.switchGeneralRelationEdge(gri, (OpdConnectionEdge)this.originalEdge, (OpdConnectionEdge)this.edge, this.cPoint);
			if (cr.getResult()== IXCheckResult.WRONG)
			{
				this.edge = this.originalEdge;
				this.cPoint.setParam(this.originalPoint.getParam());
				this.cPoint.setSide(this.originalPoint.getSide());
				this.updateDragger();
				JOptionPane.showMessageDialog(Opcat2.getFrame(),cr.getMessage(),"Opcat2 - Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
        else
        {
            if (this.wasDragged)
            {
                ((GeneralRelationInstance)this.myProject.getComponentsStructure().getEntry(this.myRelation.getId()).getInstance(new OpdKey(this.opdId, this.entityInOpdId))).setAutoArranged(false);
            }
        }

		// restore original values for undo/redo purposes
		// even if that this instance deleted on switchLinkEdge
		this.edge = this.originalEdge;
		this.cPoint.setParam(this.originalPoint.getParam());
		this.cPoint.setSide(this.originalPoint.getSide());
		this.updateDragger();
	}


	public void showPopupMenu(int pX, int pY)
	{
		JPopupMenu jpm;
		jpm = new GeneralRelationPopup(this.myProject, this.myProject.getComponentsStructure().getEntry(this.myRelation.getId()).getInstance(new OpdKey(this.opdId, this.entityInOpdId)));
		jpm.show(this, pX, pY);
		return;
	}



}