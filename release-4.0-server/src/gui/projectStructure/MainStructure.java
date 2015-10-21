package gui.projectStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import exportedAPI.OpdKey;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IOpd;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPIx.IXEntry;
import exportedAPI.opcatAPIx.IXOpd;
import exportedAPI.opcatAPIx.IXSystemStructure;
import gui.controls.EditControl;
import gui.controls.GuiControl;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmStructuralRelation;

/**
 * <p>
 * This class actually is a data structure that contains all information about
 * all entities existing in user's project. Each Entry in this data structure
 * contains some entity and in this Entry we hold all logical information about
 * this entity and information about all of its graphical representations.
 * <p>
 * This Class also contains information about all OPDs existing in user's
 * project.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 */

public class MainStructure implements ISystemStructure, IXSystemStructure {
	private Hashtable<Long, Entry> mStructure;

	private Hashtable<Long, Opd> opdsHash;

	private Hashtable<GraphicRepresentationKey, GraphicalRelationRepresentation> graphicalRepresentations;

	/**
	 * Creates a MainStructure class and initializes its internal data
	 * structures.
	 * 
	 */

	public MainStructure() {
		this.mStructure = new Hashtable<Long, Entry>();
		this.opdsHash = new Hashtable<Long, Opd>();
		this.graphicalRepresentations = new Hashtable();
	}

	/**
	 * Maps the specified key to the specified Entry in this MainStructure.
	 * Neither the key nor the Entry can be null. The Entry can be retrieved by
	 * calling the getElement method with a key that is equal to the original
	 * key.
	 * 
	 * @param pKey
	 *            entity id of entity which represented by Entry
	 * @param pEntry
	 *            Entry which contains information about some entity in user's
	 *            project
	 * @return the previous value of the specified key in this MainStruscure, or
	 *         null if it did not have one.
	 */

	public Entry put(long pKey, Entry pEntry) {
		return (Entry) (this.mStructure.put(new Long(pKey), pEntry));
	}

	/**
	 * Maps the specified key to the specified Opd in this MainStructure.
	 * Neither the key nor the Opd can be null. The Opd can be retrieved by
	 * calling the getOpd method with a key that is equal to the original key.
	 * 
	 * @param pKey
	 *            opd id
	 * @param pOpd
	 *            instance of OPD class that represents some OPD in user's
	 *            project
	 * @return the previous value of the specified key in this MainStruscure, or
	 *         null if it did not have one.
	 */

	public Opd put(long pKey, Opd pOpd) {

		return (Opd) (this.opdsHash.put(new Long(pKey), pOpd));

	}

	public Instance getInstance(OpdKey key) {
		Enumeration<Entry> entries = getAllElements();
		while (entries.hasMoreElements()) {
			Enumeration<Instance> instances = entries.nextElement()
					.getInstances();
			while (instances.hasMoreElements()) {
				Instance instance = instances.nextElement();
				if (key.equals(instance.getKey()))
					return instance;
			}
		}
		return null;
	}

	/**
	 * Maps the specified key to the specified
	 * GraphicalRelationRepresentationEntry in this MainStructure. Neither the
	 * key nor the GraphicalRelationRepresentationEntry can be null.
	 * 
	 * @param pKey
	 *            opd id
	 * @param gRepresentation
	 *            GraphicalRelationRepresentationEntry - class representing
	 *            graphically some fundamental strctural relation
	 * @return the previous value of the specified key in this MainStruscure, or
	 *         null if it did not have one.
	 */

	public GraphicalRelationRepresentation put(GraphicRepresentationKey pKey,
			GraphicalRelationRepresentation gRepresentation) {
		return (GraphicalRelationRepresentation) (this.graphicalRepresentations
				.put(pKey, gRepresentation));
	}

	/**
	 * Returns the OPD to which the specified key is mapped in this
	 * MainStructure.
	 * 
	 * @param pKey
	 *            a key in this MainStructure
	 * @return the Opd to which the key is mapped in this MainStructure; null if
	 *         the key is not mapped to any Opd in this MainStructure.
	 */

	public Opd getOpd(long pKey) {
		if (pKey == -1) {
			return null;
		}

		return (Opd) (this.opdsHash.get(new Long(pKey)));
	}

	public IOpd getIOpd(long pKey) {
		return (IOpd) (this.opdsHash.get(new Long(pKey)));
	}

	public IXOpd getIXOpd(long pKey) {
		return (IXOpd) (this.opdsHash.get(new Long(pKey)));
	}

	public Opd deleteOpd(long pKey) {
		return (Opd) (this.opdsHash.remove(new Long(pKey)));
	}

	/**
	 * Returns the Entry to which the specified key is mapped in this
	 * MainStructure.
	 * 
	 * @param pKey
	 *            a key in this MainStructure
	 * @return the Entry to which the key is mapped in this MainStructure; null
	 *         if the key is not mapped to any Entry in this MainStructure.
	 */

	public Entry getEntry(long pKey) {
		return (Entry) this.mStructure.get(new Long(pKey));
	}

	public ArrayList<Entry> getEntry(String entryName) {

		Enumeration<Entry> entries = getElements();
		ArrayList<Entry> ret = new ArrayList<Entry>();

		while (entries.hasMoreElements()) {
			Entry entry = entries.nextElement();
			if (entry.getName().equalsIgnoreCase(entryName)) {
				ret.add(entry);
			}
		}
		return ret;
	}

	public IEntry getIEntry(long pKey) {
		return (IEntry) this.mStructure.get(new Long(pKey));
	}

	public IXEntry getIXEntry(long pKey) {
		return (IXEntry) this.mStructure.get(new Long(pKey));
	}

	public boolean removeEntry(long pKey) {
		Object result = this.mStructure.remove(new Long(pKey));
		if (result == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean removeGraphicalRelationRepresentation(
			GraphicRepresentationKey pKey) {
		GraphicalRelationRepresentation gRelR = (GraphicalRelationRepresentation) this.graphicalRepresentations
				.get(pKey);

		if (gRelR == null) {
			return false;
		}
		gRelR.removeFromContainer();
		this.graphicalRepresentations.remove(pKey);
		return true;
	}

	/**
	 * Returns an enumeration of the Entries representing entities in user's
	 * project in this MainStructure. Use the Enumeration methods on the
	 * returned object to fetch the Entries sequentially
	 * 
	 * @return an enumeration of the Entries in this MainStructure
	 */
	public Enumeration<Entry> getElements() {

		ArrayList<Entry> entries = new ArrayList<Entry>();
		Iterator<Entry> entIter = Collections.list(mStructure.elements())
				.iterator();

		while (entIter.hasNext()) {
			Entry ent = (Entry) entIter.next();
			Iterator<Instance> insIter = Collections.list(ent.getInstances())
					.iterator();
			boolean goodEnt = false;
			while (insIter.hasNext()) {
				Instance ins = (Instance) insIter.next();
				if ((ins.getOpd() != null)
						&& (ins.getOpd().getOpdId() != OpdProject.CLIPBOARD_ID)) {
					goodEnt = true;
					break;
				}
			}
			if (goodEnt) {
				entries.add(ent);
			}
		}

		return Collections.enumeration(entries);
	}

	public Enumeration<Entry> getAllElements() {
		return mStructure.elements();
	}

	public Iterator<Entry> GetObjectEntries() {

		ArrayList<Entry> objects = new ArrayList<Entry>();

		Iterator<Entry> iter = Collections.list(getElements()).iterator();
		while (iter.hasNext()) {
			Entry ent = (Entry) iter.next();
			if (ent instanceof ObjectEntry)
				objects.add(ent);
		}

		return objects.iterator();
	}

	public Iterator<Entry> GetProcessEntries() {

		ArrayList<Entry> processes = new ArrayList<Entry>();

		Iterator<Entry> iter = Collections.list(getElements()).iterator();
		while (iter.hasNext()) {
			Entry ent = (Entry) iter.next();
			if (ent instanceof ProcessEntry)
				processes.add(ent);
		}

		return processes.iterator();
	}

	/**
	 * Returns an enumeration of the OPDs in this MainStructure. Use the
	 * Enumeration methods on the returned object to fetch the OPDs sequentially
	 * 
	 * @return an enumeration of the OPDs in this MainStructure
	 */
	public Enumeration<Opd> getOpds() {

		ArrayList<Opd> opds = new ArrayList<Opd>();

		GuiControl gui = GuiControl.getInstance();
		Opd clipBoard = null;
		if (gui != null) {
			clipBoard = gui.getClipBoard();
		}
		EditControl edit = EditControl.getInstance();

		// if cut is pending all opd's which are
		// inzoom or unfold of entries in the clipboard should not be passed
		if (clipBoard != null)
			clipBoard.selectAll();
		Iterator opdIter = Collections.list(opdsHash.elements()).iterator();
		while (opdIter.hasNext()) {
			boolean remove = false;
			Opd opd = (Opd) opdIter.next();

			if (edit.IsCutPending()) {
				if (clipBoard != null) {
					Iterator iter = Collections.list(
							clipBoard.getSelectedItems()).iterator();
					while (iter.hasNext()) {
						Entry ent = ((Instance) iter.next()).getEntry();
						// opd.getMainEntry().getId() ;
						if ((opd.getMainEntry() != null)
								&& (opd.getMainEntry().getId() == ent.getId())) {
							remove = true;
							break;
						}
					}
				} else {
					remove = false;
				}
			}

			if (!remove) {
				opds.add(opd);
			}
		}
		return Collections.enumeration(opds);

		// return this.opdsHash.elements() ;
	}

	public Enumeration getAllOpds() {

		return this.opdsHash.elements();

	}

	/**
	 * Returns an enumeration of the GraphicalRelationRepresentationEntry in
	 * this MainStructure. Use the Enumeration methods on the returned object to
	 * fetch the GraphicalRelationRepresentationEntry sequentially
	 * 
	 * @return an enumeration of the GraphicalRelationRepresentationEntry in
	 *         this MainStructure
	 */
	public Enumeration<GraphicalRelationRepresentation> getGraphicalRepresentations() {
		return this.graphicalRepresentations.elements();
	}

	public Enumeration getGraphicalRepresentationsInOpd(long opdId) {
		Vector resultVector = new Vector();
		GraphicalRelationRepresentation tempIns;

		for (Enumeration e = this.getGraphicalRepresentations(); e
				.hasMoreElements();) {
			tempIns = (GraphicalRelationRepresentation) e.nextElement();
			if (tempIns.getSource().getOpdId() == opdId) {
				resultVector.add(tempIns);
			}
		}

		return resultVector.elements();

	}

	public GraphicalRelationRepresentation getGraphicalRepresentation(
			GraphicRepresentationKey pKey) {
		return (GraphicalRelationRepresentation) this.graphicalRepresentations
				.get(pKey);
	}

	public ConnectionEdgeEntry getSourceEntry(OpmStructuralRelation relation) {
		return (ConnectionEdgeEntry) this.mStructure.get(new Long(relation
				.getSourceId()));
	}

	public ConnectionEdgeEntry getSourceEntry(OpmProceduralLink link) {
		return (ConnectionEdgeEntry) (this.mStructure.get(new Long(link
				.getSourceId())));
	}

	public ConnectionEdgeEntry getDestinationEntry(
			OpmStructuralRelation relation) {
		return (ConnectionEdgeEntry) (this.mStructure.get(new Long(relation
				.getDestinationId())));
	}

	public ConnectionEdgeEntry getDestinationEntry(OpmProceduralLink link) {
		return (ConnectionEdgeEntry) (this.mStructure.get(new Long(link
				.getDestinationId())));
	}

	/**
	 * This function returns all instances in the given OPD
	 */
	public Enumeration<Instance> getInstancesInOpd(long opdId) {
		Vector<Instance> resultVector = new Vector<Instance>();
		Entry tempEnt;
		Instance tempIns;
		for (Enumeration ee = this.getAllElements(); ee.hasMoreElements();) {
			tempEnt = (Entry) ee.nextElement();
			for (Enumeration ie = tempEnt.getInstances(); ie.hasMoreElements();) {
				tempIns = (Instance) ie.nextElement();
				if (tempIns.getKey().getOpdId() == opdId) {
					resultVector.add(tempIns);
				}
			}
		}
		return resultVector.elements();
	}

	public Enumeration getEntriesInOpd(long opdId) {
		Vector resultVector = new Vector();
		Entry tempEnt;
		Instance tempIns;
		for (Enumeration ee = this.getAllElements(); ee.hasMoreElements();) {
			tempEnt = (Entry) ee.nextElement();
			for (Enumeration ie = tempEnt.getInstances(); ie.hasMoreElements();) {
				tempIns = (Instance) ie.nextElement();
				if (tempIns.getKey().getOpdId() == opdId) {
					resultVector.add(tempEnt);
					break;
				}
			}
		}
		return resultVector.elements();
	}

	/**
	 * This function returns all instances in the given OPD
	 */
	public Enumeration<Instance> getInstancesInOpd(Opd opd) {
		return this.getInstancesInOpd(opd.getOpdId());
	}

	/**
	 * This function returns all link instances in the given OPD
	 */
	public Enumeration getLinksInOpd(long opdId) {
		Vector resultVector = new Vector();
		Instance tempIns;
		for (Enumeration e = this.getInstancesInOpd(opdId); e.hasMoreElements();) {
			tempIns = (Instance) e.nextElement();
			if (tempIns instanceof LinkInstance) {
				resultVector.add(tempIns);
			}
		}

		return resultVector.elements();
	}

	/**
	 * This function returns all link instances in the given OPD
	 */
	public Enumeration getLinksInOpd(Opd opd) {
		return this.getLinksInOpd(opd.getOpdId());
	}

	/**
	 * This function returns all General Relations instances in the given OPD
	 */
	public Enumeration getGeneralRelationsInOpd(long opdId) {
		Vector resultVector = new Vector();
		Instance tempIns;
		for (Enumeration e = this.getInstancesInOpd(opdId); e.hasMoreElements();) {
			tempIns = (Instance) e.nextElement();
			if (tempIns instanceof GeneralRelationInstance) {
				resultVector.add(tempIns);
			}
		}

		return resultVector.elements();
	}

	/**
	 * This function returns all General Relations instances in the given OPD
	 */
	public Enumeration getGeneralRelationsInOpd(Opd opd) {
		return this.getGeneralRelationsInOpd(opd.getOpdId());
	}

	/**
	 * This function returns all Fundamental Relations instances in the given
	 * OPD
	 */
	public Enumeration<FundamentalRelationInstance> getFundamentalRelationsInOpd(
			long opdId) {
		Vector<FundamentalRelationInstance> resultVector = new Vector<FundamentalRelationInstance>();
		Instance tempIns;
		for (Enumeration<Instance> e = this.getInstancesInOpd(opdId); e
				.hasMoreElements();) {
			tempIns = (Instance) e.nextElement();
			if (tempIns instanceof FundamentalRelationInstance) {
				resultVector.add((FundamentalRelationInstance) tempIns);
			}
		}

		return resultVector.elements();
	}

	/**
	 * This function returns all Fundamental Relations instances in the given
	 * OPD
	 */
	public Enumeration<FundamentalRelationInstance> getFundamentalRelationsInOpd(
			Opd opd) {
		return this.getFundamentalRelationsInOpd(opd.getOpdId());
	}

	/**
	 * This function returns all Things instances in the given OPD
	 */
	public Enumeration getThingsInOpd(long opdId) {
		Vector resultVector = new Vector();
		Instance tempIns;
		for (Enumeration e = this.getInstancesInOpd(opdId); e.hasMoreElements();) {
			tempIns = (Instance) e.nextElement();
			if (tempIns instanceof ThingInstance) {
				resultVector.add(tempIns);
			}
		}

		return resultVector.elements();
	}

	/**
	 * This function returns all Connection Edge instances in the given OPD
	 */
	public ArrayList<ConnectionEdgeInstance> getConnetionEdgeInOpd(long opdId) {

		ArrayList<ConnectionEdgeInstance> resultVector = new ArrayList<ConnectionEdgeInstance>();
		Instance tempIns;
		for (Enumeration e = this.getInstancesInOpd(opdId); e.hasMoreElements();) {
			tempIns = (Instance) e.nextElement();
			if (tempIns instanceof ConnectionEdgeInstance) {
				resultVector.add((ConnectionEdgeInstance) tempIns);
			}
		}

		return resultVector;
	}

	/**
	 * This function returns all Connection Edge Entries in the given OPD
	 */
	public ArrayList<ConnectionEdgeEntry> getConnetionEdgeEntriesInOpd(
			long opdId) {

		ArrayList<ConnectionEdgeEntry> resultVector = new ArrayList<ConnectionEdgeEntry>();
		Instance tempIns;
		for (Enumeration e = this.getInstancesInOpd(opdId); e.hasMoreElements();) {
			tempIns = (Instance) e.nextElement();
			if (tempIns instanceof ConnectionEdgeInstance) {
				if (!resultVector.contains(tempIns))
					resultVector
							.add((ConnectionEdgeEntry) (tempIns.getEntry()));
			}
		}

		return resultVector;
	}

	/**
	 * This function returns all Things instances in the given OPD
	 */
	public Enumeration getThingsInOpd(Opd opd) {
		return this.getThingsInOpd(opd.getOpdId());
	}

	public Enumeration<Instance> getInstanceInOpd(Opd opd, long entityId) {
		Vector<Instance> v = new Vector<Instance>();
		Instance inst = null;
		for (Enumeration e = this.getInstancesInOpd(opd); e.hasMoreElements();) {
			inst = (Instance) e.nextElement();
			if (inst.getEntry().getId() == entityId) {
				v.add(inst);
			}
		}
		if (v.size() > 0) {
			return v.elements();
		}
		return null;
	}

	/**
	 * Returns the number of entries that reside in the model.
	 * 
	 * @return Number of entries.
	 */
	public int getNumOfEntries() {
		return this.mStructure.size();
	}

	public ArrayList<Instance> getPointerInstances(
			boolean includePrivatePointers) {

		ArrayList<Instance> ret = new ArrayList<Instance>();

		Enumeration<Entry> entries = getElements();
		while (entries.hasMoreElements()) {
			Entry entry = entries.nextElement();
			Enumeration<Instance> instances = entry.getInstances();
			while (instances.hasMoreElements()) {
				Instance instance = instances.nextElement();
				if (instance.isPointerInstance()) {

					if (instance.getPointer().isPrivate()) {
						if (includePrivatePointers) {
							ret.add(instance);
						}
					} else {
						ret.add(instance);
					}
				}
			}
		}

		return ret;
	}
}
