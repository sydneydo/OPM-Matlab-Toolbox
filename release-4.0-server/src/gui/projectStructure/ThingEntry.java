package gui.projectStructure;

import exportedAPI.opcatAPI.IOpd;
import exportedAPI.opcatAPI.IThingEntry;
import exportedAPI.opcatAPIx.IXOpd;
import exportedAPI.opcatAPIx.IXThingEntry;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmAggregation;
import gui.opmEntities.OpmExhibition;
import gui.opmEntities.OpmSpecialization;
import gui.opmEntities.OpmStructuralRelation;
import gui.opmEntities.OpmThing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * <p>
 * This class represents entry of OPM thing. In this class we additionally hold
 * information about OPD which zooms in this thing.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class ThingEntry extends ConnectionEdgeEntry implements IXThingEntry,
		IThingEntry {
	private Opd zoomedInOpd;

	private ThingEntry parentThing;

	/**
	 * Creates ThingEntry that holds all information about specified pThing.
	 * 
	 * @param pEdge
	 *            object of OpmThing class.
	 */
	public ThingEntry(OpmThing pThing, OpdProject project) {
		super(pThing, project);
		this.zoomedInOpd = null;
		this.parentThing = null;
	}

	public ThingEntry(OpmThing pThing, ThingEntry parentThing,
			OpdProject project) {
		super(pThing, project);
		this.zoomedInOpd = null;
		this.parentThing = parentThing;
	}

	public ThingEntry getParentThing() {
		return this.parentThing;
	}

	/**
	 * Returns the OPD that zooming in this ThingEntry. null returned if there
	 * is no such OPD.
	 * 
	 */
	public Opd getZoomedInOpd() {
		return this.zoomedInOpd;
	}

	public Opd getUnfoldingOpd() {
		Iterator iter = Collections.list(getInstances()).iterator();
		while (iter.hasNext()) {
			Instance ins = (Instance) iter.next();
			if (ins instanceof ThingInstance) {
				if (((ThingInstance) ins)._getUnfoldingOpd() != null) {
					return ((ThingInstance) ins)._getUnfoldingOpd();
				}
			}
		}
		return null;
	}

	/**
	 * Returns the IOPD that zooming in this ThingEntry. null returned if there
	 * is no such OPD.
	 */
	public IOpd getZoomedInIOpd() {
		return this.zoomedInOpd;
	}

	/**
	 * Returns the IXOPD that zooming in this ThingEntry. null returned if there
	 * is no such OPD.
	 */
	public IXOpd getZoomedInIXOpd() {
		return this.zoomedInOpd;
	}

	/**
	 * Sets pZoomedInOpd to be zooming in for this ThingEntry.
	 * 
	 */

	public void setZoomedInOpd(Opd pZoomedInOpd) {
		this.zoomedInOpd = pZoomedInOpd;
		this.updateInstances();
	}

	/**
	 * Removes ZoomedInOpd from this ThingEntry.
	 * 
	 */
	public void removeZoomedInOpd() {
		this.zoomedInOpd = null;
		this.updateInstances();
	}

	public void updateInstances() {
		super.updateInstances();

		if (this.zoomedInOpd != null) {
			StringTokenizer st = new StringTokenizer(
					this.zoomedInOpd.getName(), " ");
			this.zoomedInOpd.setName(st.nextToken() + " - "
					+ this.getLogicalEntity().getName().replace('\n', ' ')
					+ " in-zoomed");
		}
	}

	/**
	 * get the direct Ancector of the thing. i.e the Thing in which this is
	 * inside it's inZoomed Thing
	 * 
	 * @return
	 */
	public ThingEntry getAncesctor() {
		ThingEntry retValue = null;
		ThingEntry curEntry = this;

		while (curEntry.getParentThing() != null) {
			curEntry = curEntry.getParentThing();
			retValue = curEntry;
		}

		return retValue;
	}

	public Enumeration getAllAncestors() {
		Hashtable ancestors = new Hashtable();

		ThingEntry curEntry = this;

		while (curEntry != null) {
			ancestors.put(curEntry, curEntry);
			curEntry = curEntry.getParentThing();
		}

		return ancestors.elements();
	}

	/**
	 * returns direct sons of an entry.
	 * 
	 * @return sons hash map <entry, link>
	 */
	public ArrayList<Entry> getDirectSons() {

		ArrayList<Entry> ret = new ArrayList<Entry>();

		Iterator<OpmStructuralRelation> iter = Collections.list(
				getSourceRelations()).iterator();

		while (iter.hasNext()) {
			OpmStructuralRelation rel = (OpmStructuralRelation) iter.next();

			ConnectionEdgeEntry conncted = (ConnectionEdgeEntry) myProject
					.getSystemStructure().getEntry(rel.getDestinationId());
			ret.add(conncted);
		}

		return ret;
	}



	public ArrayList<Entry> getAllSons() {
		ArrayList<Entry> ret = new ArrayList<Entry>();

		ArrayList<Entry> directSons = getDirectSons();

		if (directSons.size() > 0)
			ret.addAll(directSons);

		for (int i = 0; i < directSons.size(); i++) {
			ThingEntry entry = (ThingEntry) directSons.get(i);
			ret.addAll(entry.getAllSons());
		}

		return ret;
	}

	/**
	 * returns all the direct (ThingEntry,FundamentalRelationEntry) couples of
	 * this Thing. If this Thing is a Property or a Part OF another Thing
	 * (Parent), the (Parent,Relation) couple is returned, If this Thing has no
	 * parents an empty HashMap is returned.
	 * 
	 * @return
	 */
	public HashMap getParent() {
		HashMap ret = new HashMap();
		Enumeration relationEnum = getRelationByDestination();
		while (relationEnum.hasMoreElements()) {
			RelationEntry relation = (RelationEntry) relationEnum.nextElement();
			if (relation instanceof FundamentalRelationEntry) {
				Entry parent = (Entry) myProject.getSystemStructure().getEntry(
						relation.getSourceId());
				if (parent instanceof ConnectionEdgeEntry) {
					if (!(parent instanceof ThingEntry)) {
						StateEntry state = (StateEntry) parent;
						parent = state.getParentObject();
					}
					ret.put(parent, relation);
				}
			}
		}

		return ret;
	}

	/**
	 * returns all the direct (ThingEntry,FundamentalRelationEntry) couples of
	 * this Thing. If this Thing is a Property or a Part OF another Thing
	 * (Parent), the (Parent,Relation) couple is returned, If this Thing has no
	 * parents an empty HashMap is returned. Only a perant with Instansation
	 * relation is returned
	 * 
	 * @return
	 */
	public HashMap getInharetenceParent() {
		HashMap ret = new HashMap();
		Enumeration relationEnum = getRelationByDestination();
		while (relationEnum.hasMoreElements()) {
			RelationEntry relation = (RelationEntry) relationEnum.nextElement();
			if (relation.getLogicalEntity() instanceof OpmSpecialization) {
				Entry parent = (Entry) myProject.getSystemStructure().getEntry(
						relation.getSourceId());
				if (parent instanceof ConnectionEdgeEntry) {
					if (!(parent instanceof ThingEntry)) {
						StateEntry state = (StateEntry) parent;
						parent = state.getParentObject();
					}
					ret.put(parent, relation);
				}
			}
		}

		return ret;
	}

	/**
	 * returns all (ThingEntry,FundamentalRelationEntry) couples of this Thing.
	 * If this Thing is a Property or a Part OF another Thing (Parent), the
	 * (Parent,Relation) couple is returned, all the way up the relation tree.
	 * If this Thing has no parents an empty HashMap is returned.
	 * 
	 * @return
	 */
	public HashMap getAllIneratenceParents() {

		HashMap ret = getInharetenceParent();
		Iterator parentIter = ret.keySet().iterator();
		while (parentIter.hasNext()) {
			ThingEntry parent = (ThingEntry) parentIter.next();
			ret.putAll(parent.getAllIneratenceParents());
		}
		return ret;
	}

	public HashMap<ThingEntry, FundamentalRelationEntry> getIneratenceOneLevelSonsIncludingRoles() {
		HashMap<ThingEntry, FundamentalRelationEntry> ret = new HashMap<ThingEntry, FundamentalRelationEntry>();

		/**
		 * first get all parents including rules
		 */
		Enumeration<Entry> entries = myProject.getSystemStructure()
				.getElements();
		ArrayList<Entry> directParents = new ArrayList<Entry>();

		return ret;
	}

	public HashMap getAllIneratenceRoles() {

		HashMap temp = getAllIneratenceParents();
		HashMap ret = new HashMap();

		Iterator parentsIter = temp.keySet().iterator();
		while (parentsIter.hasNext()) {
			Iterator iter = ((ThingEntry) parentsIter.next())
					.getLogicalEntity().getRolesManager().getLoadedRolesVector(
							MetaLibrary.TYPE_POLICY).iterator();
			while (iter.hasNext()) {
				Role role = (Role) iter.next();
				ret.put(role, role);
			}
		}
		return ret;
	}

	/**
	 * return ALL the properties of this thig. Including the ones inhareted from
	 * parents and whih are not yet directly conected to this thif
	 * 
	 * @return HashMap<ConectionEdgeEntry,RelationEntry>
	 */
	public HashMap<Entry, FundamentalRelationEntry> getAllFundemantulRelationSons() {
		HashMap<Entry, FundamentalRelationEntry> ret = new HashMap<Entry, FundamentalRelationEntry>();
		// first get all real properties
		Iterator thingsIter = Collections.list(getSourceRelations()).iterator();
		while (thingsIter.hasNext()) {
			OpmStructuralRelation opm = (OpmStructuralRelation) thingsIter
					.next();
			if ((opm instanceof OpmExhibition)
					|| (opm instanceof OpmAggregation)) {
				// now find the relation instance in which this thing is the
				// source
				Entry entry = myProject.getSystemStructure().getEntry(
						opm.getId());
				Iterator instances = Collections.list(entry.getInstances())
						.iterator();
				while (instances.hasNext()) {
					FundamentalRelationInstance ins = (FundamentalRelationInstance) instances
							.next();
					if (ins.getSourceInstance().getEntry().getId() == getId()) {
						ret.put(ins.getDestinationInstance().getEntry(),
								(FundamentalRelationEntry) ins.getEntry());
					}
				}
			}
		}

		return ret;
	}

	/**
	 * return properties from the given type of this thig. Including the ones
	 * inhareted from parents and whih are not yet directly conected to this
	 * thif
	 * 
	 * @return HashMap<ConectionEdgeEntry,RelationEntry>
	 */
	public HashMap<Entry, FundamentalRelationEntry> getFundemantulRelationSons(
			int type) {
		HashMap<Entry, FundamentalRelationEntry> all = new HashMap<Entry, FundamentalRelationEntry>();
		HashMap<Entry, FundamentalRelationEntry> ret = new HashMap<Entry, FundamentalRelationEntry>();
		all = getAllFundemantulRelationSons();
		Iterator<Entry> iter = all.keySet().iterator();
		while (iter.hasNext()) {
			Entry entry = iter.next();
			FundamentalRelationEntry rel = all.get(entry);
			if (rel.getRelationType() == type) {
				ret.put(entry, rel);
			}
		}
		return ret;
	}

	public HashMap getAllInharatedProperties() {
		HashMap ret = new HashMap();

		// now get all inhreted properties
		HashMap inhareted = getAllIneratenceParents();
		// for each inhreted parent get all it's prpperties
		Iterator parents = inhareted.keySet().iterator();
		while (parents.hasNext()) {
			ThingEntry parent = (ThingEntry) parents.next();
			HashMap parntProperties = parent.getAllFundemantulRelationSons();

			Iterator parentPropertiesIter = parntProperties.keySet().iterator();
			while (parentPropertiesIter.hasNext()) {
				ThingEntry parentProperty = (ThingEntry) parentPropertiesIter
						.next();
				FundamentalRelationEntry parentRelation = (FundamentalRelationEntry) parntProperties
						.get(parentProperty);
				ret.put(parentProperty, parentRelation);
			}
		}

		// get all properties from roles of this thing

		// remove duplicate ?.

		return ret;
	}

	/**
	 * returns all (ThingEntry,FundamentalRelationEntry) couples of this Thing.
	 * If this Thing is a Property or a Part OF another Thing (Parent), the
	 * (Parent,Relation) couple is returned, all the way up the relation tree.
	 * If this Thing has no parents an empty HashMap is returned.
	 * 
	 * @return
	 */
	public HashMap getAllParents() {

		HashMap ret = getParent();
		Iterator parentIter = ret.keySet().iterator();
		while (parentIter.hasNext()) {
			ThingEntry parent = (ThingEntry) parentIter.next();
			/**
			 * the recursion
			 */
			HashMap parentMap = parent.getAllParents();
			// /////

			Iterator parents = parentMap.keySet().iterator();
			Iterator relations = parentMap.values().iterator();
			while (parents.hasNext()) {
				ThingEntry locParent = (ThingEntry) parents.next();
				RelationEntry locRelation = (RelationEntry) relations.next();
				ret.put(locParent, locRelation);
			}
		}
		return ret;
	}

	/**
	 * Sets the physical/informational property of OpmThing. If value of
	 * physical is true it's a physical thing, otherwise informational
	 * 
	 */

	public void setPhysical(boolean physical) {
		((OpmThing) this.logicalEntity).setPhysical(physical);
	}

	/**
	 * Returns true if this OpmThing is physical. If it's informational returns
	 * false
	 * 
	 */

	public boolean isPhysical() {
		return ((OpmThing) this.logicalEntity).isPhysical();
	}

	/**
	 * Sets the scope of OpmThing.
	 * 
	 * @param scope
	 *            a String specifying the scope of the Thing
	 */

	public void setScope(String scope) {
		((OpmThing) this.logicalEntity).setScope(scope);
	}

	/**
	 * Returns the scope of OpmThing.
	 * 
	 * @return a String containing thing's scope
	 */

	public String getScope() {
		return ((OpmThing) this.logicalEntity).getScope();
	}

	/**
	 * Returns the number of MAS instances of the thing
	 * 
	 * @return int specifying number of MAS instances of the OPM Thing
	 */
	public int getNumberOfMASInstances() {
		return ((OpmThing) this.logicalEntity).getNumberOfInstances();
	}

	/**
	 * Sets the number of MAS instances of the thing
	 * 
	 * @param int specifying number of MAS instances of the OPM Thing
	 */
	public void setNumberOfMASInstances(int numOfInstances) {
		((OpmThing) this.logicalEntity).setNumberOfInstances(numOfInstances);
	}

	public boolean isExternalFunction() {
		return getInstances().nextElement().isPointerInstance();
	}

	public String getTypeString() {
		return "Thing";
	}

}