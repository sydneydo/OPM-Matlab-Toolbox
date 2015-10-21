package gui.projectStructure;

import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPI.IConnectionEdgeInstance;
import exportedAPI.opcatAPI.IThingInstance;
import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXNode;
import exportedAPI.opcatAPIx.IXThingInstance;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JLayeredPane;

/**
 * <p>
 * This class is super class for ThingInstance and StateInstance, both of them
 * have common property - can be connected by links or relations, therefore they
 * should have a common superclass. Holds graphical component of
 * OpdConnectionEdge type that represents this ConnectionEdgeInstance
 * graphically.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public abstract class ConnectionEdgeInstance extends Instance implements
		IXConnectionEdgeInstance, IConnectionEdgeInstance {
	protected OpdConnectionEdge myConnectionEdge;

	protected Entry myEntry;

	/**
	 * Creates ConnectionEdgeInstance with specified pKey, which holds
	 * pConnectionEdge that represents this ConnectionEdgeInstance graphically.
	 * 
	 * @param key
	 *            OpdKey - key of graphical instance of some entity.
	 * @param pConnectionEdge
	 *            OpdConnectionEdge that represents this ConnectionEdgeInstance
	 *            graphically.
	 */
	public ConnectionEdgeInstance(OpdKey myKey, OpdProject project) {
		super(myKey, project);
		this.myConnectionEdge = null;
	}

	/**
	 * Returns OpdConnectionEdge which represents this ConnectionEdgeInstance
	 * graphically.
	 * 
	 */
	public OpdConnectionEdge getConnectionEdge() {
		return this.myConnectionEdge;
	}

	public IXThingInstance getParentIXThingInstance() {
		Container tCont = this.myConnectionEdge.getParent();

		if (tCont == null) {
			return null;
		}

		if (tCont instanceof OpdThing) {
			OpdThing tThing = (OpdThing) tCont;
			return (IXThingInstance) tThing.getEntry().getInstance(
					tThing.getOpdKey());
		}

		return null;
	}

	public ThingInstance getParentThingInstance() {
		Container tCont = this.myConnectionEdge.getParent();

		if (tCont == null) {
			return null;
		}

		if (tCont instanceof OpdThing) {
			OpdThing tThing = (OpdThing) tCont;
			return (ThingInstance) tThing.getEntry().getInstance(
					tThing.getOpdKey());
		}

		return null;
	}

	public IThingInstance getParentIThingInstance() {
		Container tCont = this.myConnectionEdge.getParent();

		if (tCont == null) {
			return null;
		}

		if (tCont instanceof OpdThing) {
			OpdThing tThing = (OpdThing) tCont;
			return (IThingInstance) tThing.getEntry().getInstance(
					tThing.getOpdKey());
		}

		return null;
	}

	public Entry getEntry() {
		return this.myEntry;
	}

	public Color getBackgroundColor() {
		return this.myConnectionEdge.getBackgroundColor();
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.myConnectionEdge.setBackgroundColor(backgroundColor);
		// pending
		this.myConnectionEdge.repaint();
	}

	public Color getBorderColor() {
		return this.myConnectionEdge.getBorderColor();
	}

	public void setBorderColor(Color borderColor) {
		this.myConnectionEdge.setBorderColor(borderColor);
	}

	public Color getTextColor() {
		return this.myConnectionEdge.getTextColor();
	}

	public void setTextColor(Color textColor) {
		this.myConnectionEdge.setTextColor(textColor);
	}

	public void setLocation(int x, int y) {
		this.myConnectionEdge.setLocation(x, y);
	}

	public void setSize(int w, int h) {
		this.myConnectionEdge.setSize(w, h);
	}

	public int getX() {
		return this.myConnectionEdge.getX();
	}

	public int getY() {
		return this.myConnectionEdge.getY();
	}

	public int getWidth() {
		return this.myConnectionEdge.getWidth();
	}

	public int getHeight() {
		return this.myConnectionEdge.getHeight();
	}

	public void setVisible(boolean isVisible) {
		this.myConnectionEdge.setVisible(isVisible);
	}

	public boolean isVisible() {
		return this.myConnectionEdge.isVisible();
	}

	public void setSelected(boolean isSelected) {
		this.myConnectionEdge.setSelected(isSelected);
		this.myConnectionEdge.repaint();
		this.selected = isSelected;
	}

	public void removeFromContainer() {
		Container cn = this.myConnectionEdge.getParent();
		cn.remove(this.myConnectionEdge);
		cn.repaint();
	}

	protected void copyPropsFrom(ConnectionEdgeInstance origin) {
		super.copyPropsFrom(origin);
		this.setSize(origin.getWidth(), origin.getHeight());
	}

	protected void copyLocationFrom(ConnectionEdgeInstance origin) {
		this.setLocation(origin.getX(), origin.getY());
	}

	public void add2Container(Container cn) {
		cn.add(this.myConnectionEdge, JLayeredPane.DEFAULT_LAYER);
	}

	public long getLogicalId() {
		return this.myConnectionEdge.getEntity().getId();
	}

	public boolean isRelated() {
		return this.myConnectionEdge.isRelated();
	}

	public String toString() {
		return this.myConnectionEdge.getEntity().getName().replace('\n', ' ');
	}

	public void update() {
		this.myConnectionEdge.repaint();
		this.myConnectionEdge.repaintLines();
	}

	// public void autoArrange() {}

	public Enumeration getRelatedSourceLinks() {
		Vector retInstances = new Vector();

		for (Enumeration e = this.getRelatedInstances(); e.hasMoreElements();) {
			Object relInstance = e.nextElement();
			if (relInstance instanceof LinkInstance) {
				LinkInstance li = (LinkInstance) relInstance;
				if (li.getSource().getOpdKey().equals(this.key)) {
					retInstances.add(li);
					continue;
				}
			}
		}

		return retInstances.elements();
	}

	/**
	 * returns an arraylist of all BiDirectional GeneralRelationInstances which
	 * source is this instance (all the general rel which where started from
	 * this instance)
	 * 
	 * @return
	 */
	public ArrayList<GeneralRelationInstance> getRelatedSourceGeneralRelations() {
		ArrayList<GeneralRelationInstance> retInstances = new ArrayList<GeneralRelationInstance>();

		for (Enumeration e = this.getRelatedInstances(); e.hasMoreElements();) {
			Object relInstance = e.nextElement();
			if (relInstance instanceof GeneralRelationInstance) {
				GeneralRelationInstance li = (GeneralRelationInstance) relInstance;
				if (li.getSource().getOpdKey().equals(this.key)) {
					retInstances.add(li);
					continue;
				}
			}
		}

		return retInstances;
	}

	/**
	 * returns an arraylist of all BiDirectional GeneralRelationInstances which
	 * source is this instance (all the general rel which where destination )
	 * 
	 * @return
	 */
	public ArrayList<GeneralRelationInstance> getRelatedDestinationGeneralRelations() {
		ArrayList<GeneralRelationInstance> retInstances = new ArrayList<GeneralRelationInstance>();

		for (Enumeration e = this.getRelatedInstances(); e.hasMoreElements();) {
			Object relInstance = e.nextElement();
			if (relInstance instanceof GeneralRelationInstance) {
				GeneralRelationInstance li = (GeneralRelationInstance) relInstance;
				if (li.getDestination().getOpdKey().equals(this.key)) {
					retInstances.add(li);
					continue;
				}
			}
		}

		return retInstances;
	}

	public Enumeration getRelatedSourceFundamentalRelation() {
		Vector retInstances = new Vector();

		for (Enumeration e = this.getRelatedInstances(); e.hasMoreElements();) {
			Object relInstance = e.nextElement();
			if (relInstance instanceof FundamentalRelationInstance) {
				FundamentalRelationInstance li = (FundamentalRelationInstance) relInstance;
				if (li.getSourceInstance().getKey().equals(this.key)) {
					retInstances.add(li);
					continue;
				}
			}
		}

		return retInstances.elements();
	}

	public Enumeration getRelatedDestinationFundamentalRelation() {
		Vector retInstances = new Vector();

		for (Enumeration e = this.getRelatedInstances(); e.hasMoreElements();) {
			Object relInstance = e.nextElement();
			if (relInstance instanceof FundamentalRelationInstance) {
				FundamentalRelationInstance li = (FundamentalRelationInstance) relInstance;
				if (li.getDestinationInstance().getKey().equals(this.key)) {
					retInstances.add(li);
					continue;
				}
			}
		}

		return retInstances.elements();
	}

	public Enumeration getRelatedDestinationLinks() {
		Vector retInstances = new Vector();

		for (Enumeration e = this.getRelatedInstances(); e.hasMoreElements();) {
			Object relInstance = e.nextElement();
			if (relInstance instanceof LinkInstance) {
				LinkInstance li = (LinkInstance) relInstance;
				if (li.getDestination().getOpdKey().equals(this.key)) {
					retInstances.add(li);
					continue;
				}
			}
		}

		return retInstances.elements();
	}

	public Enumeration getRelatedIXLines() {
		return this.getRelatedInstances();
	}

	public Enumeration getRelatedInstances() {
		Vector relatedInstances = new Vector();

		ConnectionEdgeEntry myEntry = (ConnectionEdgeEntry) this.getEntry();

		for (Enumeration e1 = myEntry.getAllRelatedEntities(); e1
				.hasMoreElements();) {
			OpmEntity relEntity = (OpmEntity) e1.nextElement();
			Entry relEntry = this.myProject.getComponentsStructure().getEntry(
					relEntity.getId());
			if (relEntry == null)
				continue;
			for (Enumeration e2 = relEntry.getInstances(); e2.hasMoreElements();) {
				Instance relInstance = (Instance) e2.nextElement();

				if (relInstance instanceof LinkInstance) {
					LinkInstance li = (LinkInstance) relInstance;
					if (li.getSource().getOpdKey().equals(this.key)
							|| li.getDestination().getOpdKey().equals(this.key)) {
						relatedInstances.add(li);
						continue;
					}
				}

				if (relInstance instanceof GeneralRelationInstance) {
					GeneralRelationInstance gri = (GeneralRelationInstance) relInstance;
					if (gri.getSource().getOpdKey().equals(this.key)
							|| gri.getDestination().getOpdKey()
									.equals(this.key)) {
						relatedInstances.add(gri);
						continue;
					}
				}

				if (relInstance instanceof FundamentalRelationInstance) {
					FundamentalRelationInstance fri = (FundamentalRelationInstance) relInstance;
					if (fri.getGraphicalRelationRepresentation().getSource()
							.getOpdKey().equals(this.key)
							|| fri.getDestination().getOpdKey()
									.equals(this.key)) {
						relatedInstances.add(fri);
						continue;
					}
				}

			}
		}

		if (this instanceof ObjectInstance) {
			ObjectInstance me = (ObjectInstance) this;

			for (Enumeration e1 = me.getStateInstances(); e1.hasMoreElements();) {
				StateInstance si = (StateInstance) e1.nextElement();

				for (Enumeration e2 = si.getRelatedInstances(); e2
						.hasMoreElements();) {
					relatedInstances.add(e2.nextElement());
				}
			}
		}

		return relatedInstances.elements();
	}

	public BaseGraphicComponent[] getGraphicComponents() {
		BaseGraphicComponent[] bgcs = new BaseGraphicComponent[1];
		bgcs[0] = this.myConnectionEdge;
		return bgcs;
	}

	public Point getAbsoluteConnectionPoint(RelativeConnectionPoint relPoint) {
		return this.myConnectionEdge.getAbsoluteConnectionPoint(relPoint);
	}

	public void animate(long time) {
		this.myConnectionEdge.animate(time);
	}

	public void animateUrl(long time) {
		this.myConnectionEdge.animateUrl(time);
	}

	public void stopTesting(long time) {
		this.myConnectionEdge.stopTesting(time);
	}

	public boolean isAnimated() {
		return this.myConnectionEdge.isAnimated();
	}

	public void pauseTesting() {
		this.myConnectionEdge.pauseAnimaition();
	}

	public void continueTesting() {
		this.myConnectionEdge.continueAnimaition();
	}

	public long getRemainedTestingTime() {
		return this.myConnectionEdge.getRemainedTestingTime();
	}

	public long getTotalTestingTime() {
		return this.myConnectionEdge.getTotalTestingTime();
	}

	public boolean isAnimatedUp() {
		return this.myConnectionEdge.isAnimatedUp();
	}

	public boolean isAnimatedDown() {
		return this.myConnectionEdge.isAnimatedDown();
	}

	public IXNode getParentIXNode() {
		Container tCont = this.myConnectionEdge.getParent();

		if (tCont == null) {
			return null;
		}

		if (tCont instanceof OpdConnectionEdge) {
			OpdConnectionEdge tEdge = (OpdConnectionEdge) tCont;
			return (IXConnectionEdgeInstance) tEdge.getEntry().getInstance(
					tEdge.getOpdKey());
		}

		return null;

	}

	public String getTypeString() {
		return "Connection Edge";
	}

}
