package gui.opdProject;

import exportedAPI.OpdKey;
import gui.opdGraphics.OpcatContainer;
import gui.opdGraphics.SelectionRectangle;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdObject;
import gui.opdGraphics.opdBaseComponents.OpdState;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.GraphicalRelationRepresentation;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ThingInstance;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * this class represents a selection of graphical elements. It is used as a variable
 * within the <code>Opd</code> class. 
 */

public class Selection// extends JComponent
{

	private OpdProject myProject;
	private Opd myOpd;
	private OpcatContainer containerSelectingFrom;
	private SelectionRectangle selRect;

	// we store selection information in different hashes
	/**
	 * graphical selection: all BaseGraphicComponents withing SelectionRectangle
	 */
	private Hashtable graphicalSelection = new Hashtable();

	/**
	 * selected: all selected Instances withing the SelectionRectangle
	 */
	private Hashtable selected = new Hashtable();

	/**
	 * thingsInContainer: ALL things in container selecting  from
	 */
	private Hashtable thingsInContainer = new Hashtable();

	Selection(OpdProject prj, Opd opd)
	{
		this.myProject = prj;
//		container = cont;
		this.myOpd = opd;
	}


	public void initSelection(OpcatContainer oc, OpdThing parent)
	{
		this.containerSelectingFrom = oc;
		this.selRect = oc.getSelectionRectangle();
		MainStructure cs = this.myProject.getComponentsStructure();
		Enumeration  enumLoc = cs.getThingsInOpd(this.myOpd);
		ThingInstance ti = null;
		this.removeSelection();
		while(enumLoc.hasMoreElements())
		{
			ti = (ThingInstance)enumLoc.nextElement();
			if(ti.getParent() == parent)
			{
				this.thingsInContainer.put(ti.getKey(), ti);
			}
		}
	}

	// this method checks if things are in selection rectngle
	// and if yes draws selection and adds to selection hash
	private void _checkSelectionThings()
	{
		Rectangle compRect;
		ThingInstance ti;
//		Rectangle selection = new Rectangle(selRect.getX(), selRect.getY(), selRect.getWidth(), selRect.getHeight());
//		System.err.println(selection);
		for(Enumeration e = this.thingsInContainer.elements(); e.hasMoreElements();)
		{
			ti = (ThingInstance)e.nextElement();

			compRect = new Rectangle(ti.getX(), ti.getY(), ti.getWidth(), ti.getHeight());

			// if selected area of component gte 2/3 of component,
			// we count it as selected
			if(this._xPercentIntersects(66.66666666, compRect, this.selRect.getBounds()))
			{
				if (!this.selected.containsKey(ti.getKey()))
				{
					this.selected.put(ti.getKey(), ti);
					ti.setSelected(true);
				}
			}
			else
			{
				if (this.selected.containsKey(ti.getKey()))
				{
					this.selected.remove(ti.getKey());
					ti.setSelected(false);
				}
			}
		}
//		System.err.println(selected);
//		System.err.println(thingsInContainer);
	}

	// after the things are selected we want to select all links and
	// relations that source and destination are selected
	private void _checkSelectionLinksAndRelations()
	{
		Hashtable selectedLinksAndRelations = new Hashtable();
		ThingInstance ti;
		Instance inst;
		LinkInstance linkI = null;
		GeneralRelationInstance genRelI = null;
		FundamentalRelationInstance fundRelI = null;
		GraphicalRelationRepresentation graphRelRep = null;
		for(Enumeration e = this.selected.elements(); e.hasMoreElements();)
		{
			Object obj = e.nextElement();
			if(obj instanceof ThingInstance ) {
				ti = (ThingInstance) obj ; 
			} else {
				continue ; 
			}
			

			for(Enumeration e1 = ti.getRelatedInstances(); e1.hasMoreElements();)
			{
				inst = (Instance)e1.nextElement();
				if(inst instanceof LinkInstance)
				{
					linkI = (LinkInstance)inst;
					if( (this.selected.get(linkI.getSource().getOpdKey()) != null) &&
						(this.selected.get(linkI.getDestination().getOpdKey()) != null))
					{
						linkI.setSelected(true);
						selectedLinksAndRelations.put(linkI.getKey(), linkI);
					}

					/////////////////////////////////////////////////
					// handle state to any and any to state links
					else
					{
						OpdKey parentObjectOpdK;
						if(linkI.getSource() instanceof OpdState)
						{
							parentObjectOpdK = ((OpdObject)((OpdState)linkI.getSource()).getParent()).getOpdKey();
							if(this.selected.get(parentObjectOpdK) != null)
							{
								linkI.setSelected(true);
								selectedLinksAndRelations.put(linkI.getKey(), linkI);
							}
						}

						if(linkI.getDestination() instanceof OpdState)
						{
							parentObjectOpdK = ((OpdThing)((OpdState)linkI.getDestination()).getParent()).getOpdKey();
							if(this.selected.get(parentObjectOpdK) != null)
							{
								linkI.setSelected(true);
								selectedLinksAndRelations.put(linkI.getKey(), linkI);
							}
						}

					}
				}

				else if(inst instanceof GeneralRelationInstance)
				{
					genRelI = (GeneralRelationInstance)inst;
					if( (this.selected.get(genRelI.getSource().getOpdKey()) != null) &&
						(this.selected.get(genRelI.getDestination().getOpdKey()) != null))
					{
						/////////////////////////////////////////////////
						// handle gen relation to any and any to state gen relations
						// eaven if it's not allowed now
						//////////////////////////////////////////////////
						genRelI.setSelected(true);
						selectedLinksAndRelations.put(genRelI.getKey(), genRelI);
					}
				}
				else if(inst instanceof FundamentalRelationInstance)
				{
					fundRelI = (FundamentalRelationInstance)inst;
					graphRelRep = fundRelI.getGraphicalRelationRepresentation();
						/////////////////////////////////////////////////
						// handle fund relation to any and any to state fund relations
						// eaven if it's not allowed now
						//////////////////////////////////////////////////
					if((this.selected.get(fundRelI.getDestination().getOpdKey()) != null) &&
						(this.selected.get(graphRelRep.getSource().getOpdKey()) != null))
					{
						fundRelI.setSelected(true);
						selectedLinksAndRelations.put(fundRelI.getKey(), fundRelI);
					}
				}
			}
		}
		this.selected.putAll(selectedLinksAndRelations);
		selectedLinksAndRelations.clear();
	}

	public void deselectAll()
	{
		this.resetSelection();
	}

	public void selectAll()
	{
		// select all Instances - logical
		this.initSelection(this.myOpd.getDrawingArea(), null);
		this.selected.putAll(this.thingsInContainer);
		for(Enumeration e = this.thingsInContainer.elements(); e.hasMoreElements();)
		{
			((Instance)e.nextElement()).setSelected(true);
		}
		this._checkSelectionLinksAndRelations();

		// select all BaseGraphicComponents - graphical
		Component[] allComps = this.containerSelectingFrom.getContainer().getComponents();
		BaseGraphicComponent currBgc;
		for(int i = 0; i < allComps.length; i++)
		{
			if(allComps[i] instanceof BaseGraphicComponent)
			{
				currBgc = (BaseGraphicComponent)allComps[i];
				this.graphicalSelection.put(currBgc, currBgc.getBounds());
			}
		}
	}

	public void finishSelection()
	{
		this._checkSelectionLinksAndRelations();
		this._checkGraphicalSelection();
        this.myOpd.getDrawingArea().repaint();
	}

	public void inSelection()
	{
		this._checkSelectionThings();
	}

	private void _checkGraphicalSelection()
	{
		if((this.containerSelectingFrom == null) || (this.containerSelectingFrom.getContainer() == null)) {
			return ; 
		}
		Component[] allComps = this.containerSelectingFrom.getContainer().getComponents();
		Rectangle sel = null;
		BaseGraphicComponent currBgc;
		for(int i = 0; i < allComps.length; i++)
		{
			if(allComps[i] instanceof BaseGraphicComponent)
			{
				currBgc = (BaseGraphicComponent)allComps[i];
				if(this._xPercentIntersects(66.66666, new Rectangle(currBgc.getX(), currBgc.getY(), currBgc.getWidth(), currBgc.getHeight()), sel))
				{
					this.graphicalSelection.put(currBgc, currBgc.getBounds());
                    currBgc.setSelected(true);
				}
			}
		}
	}

	/**
	 * checks if current selection rectangle sel has x percent intersection with
	 * given rectange r. We pass the sel rectangle to avoid allocation of mutiple
	 * rectangles when this method called secuentially. If sel is null new rectangle
	 * is allocated so we do not have to allocate before the method is called for the
	 * first time.
	 */
	private boolean _xPercentIntersects(double pc, Rectangle r, Rectangle sel) throws IllegalArgumentException
	{
		Rectangle intersection;
		if(sel == null)
		{
			sel = new Rectangle(this.selRect.getX(), this.selRect.getY(), this.selRect.getWidth(), this.selRect.getHeight());
		}
		if((pc < 0.0) || (pc >100.0))
		{
			throw new IllegalArgumentException("Percentage is less then 0 or greate then 100");
		}

		intersection = sel.intersection(r);

		if(((intersection.getWidth() > 0) && (intersection.getHeight() > 0)) &&
		   (intersection.getWidth()*intersection.getHeight() >= r.getWidth()*r.getHeight()*(pc/100)))
		{
			return true;
		}
		return false;
	}

	public BaseGraphicComponent[] graphicalSelectionComponents()
	{
		BaseGraphicComponent tmpBgc;
		BaseGraphicComponent[] bgcs = new BaseGraphicComponent[this.graphicalSelection.size()];
		int i = 0;
		for(Enumeration e = this.graphicalSelection.keys(); e.hasMoreElements();)
		{
			tmpBgc = (BaseGraphicComponent)e.nextElement();
			bgcs[i++] = tmpBgc;
		}
		return bgcs;
	}

	public Enumeration getGraphicalSelection()
	{
		return this.graphicalSelection.keys();
	}

	public Hashtable getGraphicalSelectionHash()
	{
		return this.graphicalSelection;
	}

	public void removeSelection(BaseGraphicComponent bgc)
    {
        bgc.setSelected(false);
        this.graphicalSelection.remove(bgc);
    }
    public void removeSelection(Instance inst)
	{
		if(this.selected.remove(inst.getKey()) != null)
		{
			inst.setSelected(false);
		}
		BaseGraphicComponent[] bgcs = inst.getGraphicComponents();
		for(int i = 0; i < bgcs.length; i++)
		{
            bgcs[i].setSelected(false);
			if(this.graphicalSelection.remove(bgcs[i]) == null)
			{
				//pending - Are we have to handle the error case
			}
		}
	}

	public void removeSelection()
	{
		for(Enumeration e = this.selected.elements(); e.hasMoreElements();)
		{
            ((Instance)e.nextElement()).setSelected(false);
		}
        this.selected.clear();
		this.thingsInContainer.clear();

        for(Enumeration e1 = this.graphicalSelection.keys(); e1.hasMoreElements();)
        {
            Object temp = e1.nextElement();
            if (temp instanceof BaseGraphicComponent)
            {
                ((BaseGraphicComponent)temp).setSelected(false);
            }
            else
            {
                System.err.println(temp.getClass()+" "+temp.toString());
            }
        }
		this.graphicalSelection.clear();
        this.myOpd.getDrawingArea().repaint();
	}

	public void addSelection(Instance sItem, boolean removePrevious)
	{
		if(removePrevious)
		{
			this.removeSelection();
		}

        this.selected.put(sItem.getKey(), sItem);
        sItem.setSelected(true);
        BaseGraphicComponent[] bgcs = sItem.getGraphicComponents();
        for(int i = 0; i < bgcs.length; i++)
        {
            this.graphicalSelection.put(bgcs[i], bgcs[i].getBounds());
        }
	}

	public void addSelection(BaseGraphicComponent bgc, boolean removePrevious)
	{

		if(removePrevious)
		{
			this.removeSelection();
		}

		// here we try to guess if there is only one graphic
		// component selected.
		// we suppose that if something else logically selected
		// there is multiple selection
		if(this.selected.size() >= 0 )
		{
//			graphicalSelection.clear();
//			removeSelection();
		}
		this.graphicalSelection.put(bgc, bgc.getBounds());
        bgc.setSelected(true);
	}

	public void resetSelection()
	{
		this.removeSelection();
	}

	public Enumeration getSelectedItems()
	{
		return this.selected.elements();
	}

	public Hashtable getSelectedItemsHash()
	{
		return this.selected;
	}

	/***************************************** OWL-S Start *****************************/
	public Instance selectOne(String name)
	{
			// select all Instances - logical
			Object temp = null;
			this.initSelection(this.myOpd.getDrawingArea(), null);
			for(Enumeration e = this.thingsInContainer.elements(); e.hasMoreElements();)
			{
			  temp = e.nextElement();
			  if (name.matches(temp.toString())) {
				this.selected.put(temp, temp);
				break;
			  }
			  if (e.hasMoreElements() == false) {
				return null;
			}
			}
			( (Instance) temp).setSelected(true);
			this._checkSelectionLinksAndRelations();
        
			// select all BaseGraphicComponents - graphical
			Component[] allComps = this.containerSelectingFrom.getContainer().getComponents();
			BaseGraphicComponent currBgc;
			for (int i = 0; i < allComps.length; i++) {
			  if (allComps[i] instanceof BaseGraphicComponent) {
				currBgc = (BaseGraphicComponent) allComps[i];
				this.graphicalSelection.put(currBgc, currBgc.getBounds());
			  }
			}
			return (Instance)temp;
	}
	/***************************************** OWL-S End *****************************/

}
