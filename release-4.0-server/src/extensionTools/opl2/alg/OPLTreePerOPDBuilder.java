package extensionTools.opl2.alg;

import java.util.Enumeration;
import java.util.Vector;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IInstance;
import exportedAPI.opcatAPI.IStateEntry;
import exportedAPI.opcatAPI.ISystemStructure;
import extensionTools.opl2.generated.ObjectFactory;
import gui.util.OpcatLogger;

/**
 * <p>
 * Title: Extension Tools
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author OPCAT team
 * @version 1.0
 */

public final class OPLTreePerOPDBuilder extends OPLTreeBuilder {
	public OPLTreePerOPDBuilder(ISystemStructure myStructure_,
			String systemName_, long opd_, ObjectFactory ob) {
		super(myStructure_, systemName_, ob);
		this.opd = opd_;
		this.currOPD = opd_;
		if ((this.myStructure == null)
				|| (this.myStructure.getIOpd(this.opd) == null)
				|| (this.myStructure.getIOpd(this.opd).getParentIOpd() == null)) {
			this.mainElem = null;
		} else {
			this.mainElem = this.myStructure.getIOpd(this.opd).getMainIEntry();
		}
	}

	public boolean isMainElement(IEntry elem) {
		if (this.mainElem == null) {
			return false;
		}
		return (this.mainElem.getId() == elem.getId());

	}

	public IEntry getMainElement() {
		return this.mainElem;
	}

	protected Enumeration getSystemElements() {
		Enumeration e;
		IEntry entry;
		// java.util.Collection elems = new LinkedList();
		Vector elems = new Vector();
		/*
		 * if(mainElem!=null){
		 * elems.add(myStructure.getIOpd(opd).getMainIEntry()); }else{ }
		 */
		Enumeration el = this.myStructure.getInstancesInOpd(this.opd);
		while (el.hasMoreElements()) {
			entry = ((IInstance) el.nextElement()).getIEntry();
			if (entry != this.mainElem) {
				elems.add(entry);
			}
		}
		e = elems.elements();
		return e;
	}

	/**
	 * rule = 0 : all relations by source only tests if the relation in the opd
	 * rule = 1 : Bidirectional relations && Specialization + test if it is in
	 * the opd - relations by destination
	 */
	// protected boolean testRelation(IRelationEntry rel, int rule) {
	// if (!OPLGeneral.isByDestination(rule))
	// return rel.hasInstanceInOpd(opd);
	// int type = rel.getRelationType();
	// return (rel.hasInstanceInOpd(opd) &&
	// ( (type == OpcatConstants.BI_DIRECTIONAL_RELATION &&
	// OPLGeneral.isNotEmpty(rel.getBackwardRelationMeaning())
	// && !rel.getForwardRelationMeaning().equals("")) ||
	// (type == OpcatConstants.SPECIALIZATION_RELATION)));
	// }
	// protected boolean testLink(ILinkEntry link) {
	// return link.hasInstanceInOpd(opd);
	// }
	//
	// protected boolean testRelation(IRelationEntry link) {
	// return link.hasInstanceInOpd(opd);
	// }
	//
	// protected boolean testState(IStateEntry link) {
	// return link.hasInstanceInOpd(opd);
	// }
	protected boolean testEntity(IEntry entity) {
		try {
			if (entity instanceof IStateEntry) {
				return ((IStateEntry) entity)
						.hasVisibleInstancesInOpd(this.opd);
				// System.err.println(" My opd is:" + opd + " my name is " +
				// entity.getName());
			}
			// if(entity.hasInstanceInOpd(opd))
			//System.err.println(" " + entity.getName() + " has inctance in opd"
			//		+ opd);
			/**
			 * raanan fix this 
			 */
			if(entity == null) return false ; 
			return entity.hasInstanceInOpd(this.opd);
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
			return false;
		}
	}

	protected long opd;
}
