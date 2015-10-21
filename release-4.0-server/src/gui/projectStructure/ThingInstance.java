package gui.projectStructure;

import exportedAPI.OpdKey;
import exportedAPI.opcatAPI.IThingInstance;
import exportedAPI.opcatAPIx.IXOpd;
import exportedAPI.opcatAPIx.IXThingInstance;
import gui.opdGraphics.opdBaseComponents.OpdState;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmThing;

import java.awt.Component;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JLayeredPane;

/**
 * <p>
 * This class is super class for ObjectInstance and ProcessInstance. In this
 * class we additionally hold information about OPD unfolding this thing
 * instance.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public abstract class ThingInstance extends ConnectionEdgeInstance implements
		IThingInstance, IXThingInstance {
	private Opd unfoldingOpd;

	private boolean zoomedIn;

	/**
	 * Creates ThingInstance with specified myKey, which holds pThing that
	 * represents this ThingInstance graphically.
	 * 
	 * @param key
	 *            OpdKey - key of graphical instance of this entity.
	 * @param pThing
	 *            OpdThing that represents this ThingInstance graphically.
	 */
	public ThingInstance(OpdKey myKey, OpdProject project) {
		super(myKey, project);
		this.unfoldingOpd = null;
		this.zoomedIn = false;
	}

	/**
	 * Returns OpdThing which represents this ThingInstance graphically.
	 * 
	 */
	public OpdThing getThing() {
		return (OpdThing) this.myConnectionEdge;
	}

	/**
	 * Returns OPD unfolding this ThingInstance. null returned if there is no
	 * such OPD.
	 * 
	 */

	public Opd getUnfoldingOpd() {
		return ((ThingEntry) this.getEntry()).getUnfoldingOpd();
	}

	/**
	 * used only by ThingEntry to patch the Unfolded = Instance bug
	 * 
	 * @return
	 */
	public Opd _getUnfoldingOpd() {
		return this.unfoldingOpd;
	}

	/**
	 * Returns OPD unfolding this ThingInstance. null returned if there is no
	 * such OPD. Added by YG
	 * 
	 */

	public IXOpd getUnfoldingIXOpd() {
		return this.getUnfoldingOpd();
	}

	/**
	 * Sets pUnfoldingOpd to be unfolding for this ThingInstance.
	 * 
	 */
	public void setUnfoldingOpd(Opd pUnfoldingOpd) {
		Iterator iter = Collections.list(
				((ThingEntry) this.getEntry()).getInstances()).iterator();
		while (iter.hasNext()) {
			ThingInstance ins = (ThingInstance) iter.next();
			ins._setUnfoldingOpd(pUnfoldingOpd);
		}
		if (pUnfoldingOpd != null) {
			((OpdThing) this.myConnectionEdge).setBoldBorder(true);
		} else {
			((OpdThing) this.myConnectionEdge).setBoldBorder(false);
		}
	}

	public void _setUnfoldingOpd(Opd pUnfoldingOpd) {
		unfoldingOpd = pUnfoldingOpd;
	}

	/**
	 * Removes UnfoldingOpd from this ThingInstance.
	 * 
	 */
	public void removeUnfoldingOpd() {
		this.unfoldingOpd = null;
		if (((ThingEntry) this.getEntry()).getUnfoldingOpd() == null)
			((OpdThing) this.myConnectionEdge).setBoldBorder(false);
	}

	public boolean isZoomedIn() {
		return this.zoomedIn;
	}

	// public IThingInstance getParentIThingInstance() {
	// Container tCont = this.myConnectionEdge.getParent();
	//
	// if (tCont == null) {
	// return null;
	// }
	//
	// if (tCont instanceof OpdThing) {
	// OpdThing tThing = (OpdThing) tCont;
	// return (IThingInstance) tThing.getEntry().getInstance(
	// tThing.getOpdKey());
	// }
	//
	// return null;
	// }

	// public IXThingInstance getParentIXThingInstance() {
	// Container tCont = this.myConnectionEdge.getParent();
	//
	// if (tCont == null) {
	// return null;
	// }
	//
	// if (tCont instanceof OpdThing) {
	// OpdThing tThing = (OpdThing) tCont;
	// return (IXThingInstance) tThing.getEntry().getInstance(
	// tThing.getOpdKey());
	// }
	//
	// return null;
	// }

	/**
	 * Sets if this ThingInstance is zoomed in, i.e. if it can contain another
	 * entities graphically.
	 */
	public void setZoomedIn(boolean pZoomedIn) {
		this.zoomedIn = pZoomedIn;
		OpdThing myThing = (OpdThing) this.myConnectionEdge;
		myThing.setZoomedIn(this.zoomedIn);
		myThing.setBoldBorder(this.zoomedIn);
	}

	public boolean isContainsChilds() {
		if (!this.myConnectionEdge.isZoomedIn()) {
			return false;
		}

		Component components[] = this.myConnectionEdge.getComponents();

		for (int i = 0; i < components.length; i++) {
			if (!(components[i] instanceof OpdState)) {
				return true;
			}
		}

		return false;
	}

	public Enumeration getChildThings() {
		Vector things = new Vector();
		if (!this.myConnectionEdge.isZoomedIn()) {
			return things.elements();
		}

		Component components[] = this.myConnectionEdge.getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof OpdThing) {
				OpdThing tThing = (OpdThing) components[i];
				things.add(tThing.getEntry().getInstance(tThing.getOpdKey()));
			}
		}

		return things.elements();

	}

	public void setTextPosition(String tp) {
		((OpdThing) this.myConnectionEdge).setTextPosition(tp);
	}

	public String getTextPosition() {
		return ((OpdThing) this.myConnectionEdge).getTextPosition();
	}

	protected void copyPropsFrom(ThingInstance origin) {
		super.copyPropsFrom(origin);
		this.setTextPosition(origin.getTextPosition());
	}

	public void update() {
		OpmThing logThing = (OpmThing) this.getEntry().getLogicalEntity();
		OpdThing grThing = (OpdThing) this.myConnectionEdge;

		grThing.setShadow(logThing.isPhysical());
		grThing.setDashed(logThing.isEnviromental());

		if (getUnfoldingOpd() != null) {
			this.unfoldingOpd = getUnfoldingOpd();
			StringTokenizer st = new StringTokenizer(this.unfoldingOpd
					.getName(), " ");
			this.unfoldingOpd.setName(st.nextToken() + " - "
					+ logThing.getName().replace('\n', ' ') + " unfolded");
		}

		if ((((ThingEntry) this.getEntry()).getZoomedInOpd() != null)
				|| (this.unfoldingOpd != null)) {
			grThing.setBoldBorder(true);
		} else {
			grThing.setBoldBorder(false);
		}

		super.update();
	}

	public int getLayer() {
		return ((JLayeredPane) this.myConnectionEdge.getParent())
				.getPosition(this.myConnectionEdge);
	}

	public void setLayer(int position) {
		((JLayeredPane) this.myConnectionEdge.getParent()).setPosition(
				this.myConnectionEdge, position);
	}

	// private method !!! uses local constants with no 'defines'
	// Gets 1 - things
	// 2 - links
	// 3 - gen rel
	// 4 - fund rel
	private Enumeration _getIncludedSomethingInstances(int what) {

		Vector retVector = new Vector();
		if (this.isZoomedIn()) {
			return retVector.elements();
		}

		Enumeration e = this.myProject.getComponentsStructure()
				.getInstancesInOpd(this.getKey().getOpdId());
		Instance inst = null;
		while (e.hasMoreElements()) {
			inst = (Instance) e.nextElement();

			// Attantion: the follwing statment means there is
			// only one OpdThing in OPD that can hold other OPD things
			if (inst.getParent() != null) {
				retVector.add(inst);
			}
		}

		for (int i = 0; i < retVector.size(); i++) {
			switch (what) {
			case 1:
				if (!(retVector.elementAt(i) instanceof ThingInstance)) {
					retVector.remove(i);
				}
				break;
			case 2:
				if (!(retVector.elementAt(i) instanceof LinkInstance)) {
					retVector.remove(i);
				}
				break;
			case 3:
				if (!(retVector.elementAt(i) instanceof GeneralRelationInstance)) {
					retVector.remove(i);
				}
				break;
			case 4:
				if (!(retVector.elementAt(i) instanceof FundamentalRelationInstance)) {
					retVector.remove(i);
				}
				break;
			}
		}
		return retVector.elements();
	}

	public IXOpd createInzoomedOpd() {
		return this.myProject.zoomIn(this);
	}

	public IXOpd createUnfoldedOpd(boolean bringRelatedThings,
			boolean bringRoleRelatedThings) {
		return this.myProject.unfolding(this, bringRelatedThings,
				bringRelatedThings, true, bringRoleRelatedThings);
	}

	// The following 4 methods returns OpdXxx.. not Xxx..Instances
	public Enumeration getIncludedThingInstances() {
		return this._getIncludedSomethingInstances(1);
	}

	public Enumeration getIncludedLinkInstances() {
		return this._getIncludedSomethingInstances(2);
	}

	public Enumeration getIncludedGeneralRelationInstances() {
		return this._getIncludedSomethingInstances(3);
	}

	public Enumeration getIncludedFundamentalRelationInstances() {
		return this._getIncludedSomethingInstances(4);
	}

	public boolean isIncluding(Instance ins) {
		Opd inzoom = ((ThingEntry) getEntry()).getZoomedInOpd();
		if (inzoom == null) {
			return false;
		}

		return ((getOpd().getOpdId() == inzoom.getOpdId()) && (ins.getParent() != null));
	}

	public String toString() {
		return this.getThing().getName();
	}

	public String getThingName() {
		return this.getThing().getName();
	}

	public String getTypeString() {
		return "Thing";
	}

	public HashMap<ThingInstance, FundamentalRelationInstance> getFundemantulRelationInstancesSons(
			int type) {
		HashMap<Entry, FundamentalRelationEntry> entries = ((ThingEntry) getEntry())
				.getFundemantulRelationSons(type);
		HashMap<ThingInstance, FundamentalRelationInstance> ret = new HashMap<ThingInstance, FundamentalRelationInstance>(
				0);
		Iterator<FundamentalRelationEntry> relationsEntries = entries.values()
				.iterator();
		while (relationsEntries.hasNext()) {
			FundamentalRelationEntry relEntry = relationsEntries.next();
			FundamentalRelationInstance ins = (FundamentalRelationInstance) relEntry
					.getInstanceInOPD(getOpd());
			if (ins != null) {
				ret.put((ThingInstance) ins.getDestinationInstance(), ins);
			}
		}
		return ret;

	}

}
