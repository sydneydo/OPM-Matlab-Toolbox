/*
 *  opcat2
 *  package: opdGraphics
 *  file:    AroundDragger.java
 */

package gui.opdGraphics.draggers;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPIx.IXCheckResult;
import gui.Opcat2;
import gui.checkModule.CheckResult;
import gui.opdGraphics.Connectable;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.popups.LinkPopup;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.opmEntities.OpmProceduralLink;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;


/**
 *  <p>Abstract class AroundDragger is a superclass for all classes which represents
 *  items that can be dragged around Objects, Processes or States in OPD.
 *  Actually this class provides dragging functionality for edges of all kinds
 *  of OPD links and relations.</p>
 */

public abstract class OpdLink extends AroundDragger
{

/**
	 * 
	 */
	private static final long serialVersionUID = -6984575681796690010L;


/**
 *  Logical entity represents link this dragger belongs to
 */
	protected OpmProceduralLink  myLink;


/**
 *  An ID of the OPD this dragger belongs to
 */
	private long opdId;
	private long entityInOpdId;


/**
 *  <strong>There is no default constructor</strong>
 *  <p>All extending classes have to call <code>super()</code> in their constructors.
 *  @param <code>pEdge1</code> -- OPD graphic component this dragger connected to.
 *  @param <code>pSide1</code> -- The side OPD graphic component this dragger connected to.
 *  @param <code>pParam1</code> -- The relation of coordinates of a <code>mouseClick</code> event to length of the side.
 */
	public OpdLink(Connectable pEdge1, RelativeConnectionPoint cPoint, OpmProceduralLink pLink,
								   long pOpdId, long pEntityInOpdId, OpdProject pProject)

	{
		super(pEdge1, cPoint, pProject);
		this.myLink = pLink;
		this.myProject = pProject;
		this.opdId = pOpdId;
		this.entityInOpdId = pEntityInOpdId;

		GenericTable config = this.myProject.getConfiguration();

		double normalSize = ((Integer)config.getProperty("NormalSize")).doubleValue();
		double currentSize = ((Integer)config.getProperty("CurrentSize")).doubleValue();
		double factor = currentSize/normalSize;
		double size = ((Integer)config.getProperty("DraggerSize")).intValue()*factor;
		this.setSize((int)size,(int)size);
		this.setCanLeave(true);
	}


/**
 *  @return Logical entity represents link or line this dragger belongs to.
 */
	public OpmProceduralLink getEntity()
	{
		  return this.myLink;
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

		  Entry myEntry = this.myProject.getComponentsStructure().getEntry(this.myLink.getId());
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
			LinkEntry le = (LinkEntry)this.myProject.getComponentsStructure().getEntry(this.myLink.getId());
			LinkInstance li = (LinkInstance)le.getInstance(new OpdKey(this.opdId, this.entityInOpdId));

			CheckResult cr = this.myProject.switchLinkEdge(li, (OpdConnectionEdge)this.originalEdge, (OpdConnectionEdge)this.edge, this.cPoint);
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
                ((LinkInstance)this.myProject.getComponentsStructure().getEntry(this.myLink.getId()).getInstance(new OpdKey(this.opdId, this.entityInOpdId))).setAutoArranged(false);
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
		jpm = new LinkPopup(this.myProject, this.myProject.getComponentsStructure().getEntry(this.myLink.getId()).getInstance(new OpdKey(this.opdId, this.entityInOpdId)));
		jpm.show(this, pX, pY);
		return;
	}

	public boolean equals(Object obj)
	{

		OpdLink tempLink;
		if (!(obj instanceof OpdLink))
		{
			return false;
		}

		tempLink = (OpdLink)obj;
		if ((tempLink.getOpdId() == this.opdId) && (tempLink.getEntityInOpdId() == this.entityInOpdId))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


}
