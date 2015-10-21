package gui.projectStructure;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;
import exportedAPI.opcatAPIx.IXConnectionEdgeEntry;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_LINK_DIRECTION;
import gui.Opcat2;
import gui.controls.EditControl;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.Constants;
import gui.opmEntities.OpmConnectionEdge;
import gui.opmEntities.OpmFundamentalRelation;
import gui.opmEntities.OpmGeneralRelation;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmSpecialization;
import gui.opmEntities.OpmStructuralRelation;
import gui.undo.UndoableAddLink;

import java.awt.Container;
import java.awt.Point;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;

/**
 * <p>
 * This class is super class for ThingEntry and StateEntry, both of them have
 * common property - can be connected by links or relations, therefore they
 * should have a common superclass.
 * <p>
 * In this class we hold information (in special data structures) about links
 * and relations in which this ConnectionEdgeEntry participates as source or
 * destination. This information is very useful for Unfolding and Zooming in
 * operations.
 * 
 * @version 2.0
 * @author Stanislav Obydionnov
 * @author Yevgeny Yaroker
 * 
 */

public class ConnectionEdgeEntry extends Entry implements IConnectionEdgeEntry,
		IXConnectionEdgeEntry {
	private Hashtable<RelationKey, OpmProceduralLink> linkSources;

	private Hashtable<RelationKey, OpmProceduralLink> linkDestinations;

	private Hashtable relationSources;

	private Hashtable relationDestinations;

	private LinkedList generalRelationSources;

	private LinkedList generalRelationDestinations;

	/**
	 * Creates ConnectionEdgeEntry that holds all information about specified
	 * pEdge.
	 * 
	 * @param pEdge
	 *            object of OpmConnectionEdge class.
	 */
	public ConnectionEdgeEntry(OpmConnectionEdge pEdge, OpdProject project) {
		super(pEdge, project);
		this.linkSources = new Hashtable();
		this.linkDestinations = new Hashtable();
		this.relationSources = new Hashtable();
		this.relationDestinations = new Hashtable();
		this.generalRelationSources = new LinkedList();
		this.generalRelationDestinations = new LinkedList();
	}

	/**
	 * This method adds pRelation to the data structure holding all structural
	 * relations in which this ConnectionEdgeEntry participates as source.
	 * 
	 */

	/**
	 * This method adds pRelation to the data structure holding all structural
	 * relations in which this ConnectionEdgeEntry participates as source.
	 * 
	 */

	public void addRelationSource(OpmFundamentalRelation pRelation) {
		this.relationSources.put(new RelationKey(Constants
				.getType4Relation(pRelation), pRelation.getDestinationId()),
				pRelation);
		return;
	}

	public void removeRelationSource(OpmFundamentalRelation pRelation) {
		this.relationSources.remove(new RelationKey(Constants
				.getType4Relation(pRelation), pRelation.getDestinationId()));
		return;
	}

	public void addGeneralRelationSource(OpmGeneralRelation pRelation) {
		if (!this.generalRelationSources.contains(pRelation)) {
			this.generalRelationSources.add(pRelation);
		}
		return;
	}

	public void removeGeneralRelationSource(OpmGeneralRelation pRelation) {
		this.generalRelationSources.remove(pRelation);
		return;
	}

	/**
	 * Returns OpmStructuralRelation of pRelationType(as it specified in
	 * Interface Constants in opmEntities package) in which entity with
	 * pEntityId participates as destination and this ConnectionEdgeEntry
	 * participates as source. null is returned if there is no such
	 * OpmStructuralRelation in data structure.
	 */

	public OpmStructuralRelation getRelationByDestination(long pEntityId,
			int pRelationType) {
		return (OpmStructuralRelation) (this.relationSources
				.get(new RelationKey(pRelationType, pEntityId)));
	}

	/**
	 * This method adds pRelation to the data structure holding all structural
	 * relations in which this ConnectionEdgeEntry participates as destination.
	 * 
	 */
	public void addRelationDestination(OpmFundamentalRelation pRelation) {
		this.relationDestinations.put(new RelationKey(Constants
				.getType4Relation(pRelation), pRelation.getSourceId()),
				pRelation);
		return;
	}

	/**
	 * returns interface of an entry.
	 * 
	 * @return sons hash map <entry, link>
	 */
	public HashMap<OpmProceduralLink, OPCAT_EXPOSE_LINK_DIRECTION> getDirectInterface() {

		HashMap<OpmProceduralLink, OPCAT_EXPOSE_LINK_DIRECTION> ret = new HashMap<OpmProceduralLink, OPCAT_EXPOSE_LINK_DIRECTION>();

		Iterator<OpmProceduralLink> iter = Collections.list(getSourceLinks())
				.iterator();

		while (iter.hasNext()) {
			OpmProceduralLink rel = (OpmProceduralLink) iter.next();

			ConnectionEdgeEntry conncted = (ConnectionEdgeEntry) myProject
					.getSystemStructure().getEntry(rel.getDestinationId());
			ret.put(rel, OPCAT_EXPOSE_LINK_DIRECTION.FROM);
		}

		iter = Collections.list(getDestinationLinks()).iterator();

		while (iter.hasNext()) {
			OpmProceduralLink rel = (OpmProceduralLink) iter.next();

			ConnectionEdgeEntry conncted = (ConnectionEdgeEntry) myProject
					.getSystemStructure().getEntry(rel.getSourceId());
			ret.put(rel, OPCAT_EXPOSE_LINK_DIRECTION.TO);
		}

		return ret;
	}

	public void removeRelationDestination(OpmFundamentalRelation pRelation) {
		this.relationDestinations.remove(new RelationKey(Constants
				.getType4Relation(pRelation), pRelation.getSourceId()));
		return;
	}

	public void addGeneralRelationDestination(OpmGeneralRelation pRelation) {
		if (!this.generalRelationDestinations.contains(pRelation)) {
			this.generalRelationDestinations.add(pRelation);
		}
		return;
	}

	public void removeGeneralRelationDestination(OpmGeneralRelation pRelation) {
		this.generalRelationDestinations.remove(pRelation);
		return;
	}

	/**
	 * Returns OpmStructuralRelation of pRelationType(as it specified in
	 * Interface Constants in opmEntities package) in which entity with
	 * pEntityId participates as source and this ConnectionEdgeEntry
	 * participates as destination. null is returned if there is no such
	 * OpmStructuralRelation in data structure.
	 */

	public OpmStructuralRelation getRelationBySource(long pEntityId,
			int pRelationType) {
		return (OpmStructuralRelation) (this.relationDestinations
				.get(new RelationKey(pRelationType, pEntityId)));
	}

	/**
	 * This method adds pLink to the data structure holding all procedural links
	 * in which this ConnectionEdgeEntry participates as source.
	 * 
	 */

	public void addLinkSource(OpmProceduralLink pLink) {
		this.linkSources.put(new RelationKey(Constants.getType4Link(pLink),
				pLink.getDestinationId()), pLink);
		return;
	}

	public void removeLinkSource(OpmProceduralLink pLink) {
		this.linkSources.remove(new RelationKey(Constants.getType4Link(pLink),
				pLink.getDestinationId()));
		return;
	}

	/**
	 * Returns OpmProceduralLink of pLinkType(as it specified in Interface
	 * Constants in opmEntities package) in which entity with pEntityId
	 * participates as destination and this ConnectionEdgeEntry participates as
	 * source. null is returned if there is no such OpmProceduralLink in data
	 * structure.
	 */

	public OpmProceduralLink getLinkByDestination(long pEntityId, int pLinkType) {
		return (OpmProceduralLink) (this.linkSources.get(new RelationKey(
				pLinkType, pEntityId)));
	}

	/**
	 * This method adds pLink to the data structure holding all procedural links
	 * in which this ConnectionEdgeEntry participates as destination.
	 * 
	 */

	public void addLinkDestination(OpmProceduralLink pLink) {
		this.linkDestinations.put(new RelationKey(
				Constants.getType4Link(pLink), pLink.getSourceId()), pLink);
		return;
	}

	public void removeLinkDestination(OpmProceduralLink pLink) {
		this.linkDestinations.remove(new RelationKey(Constants
				.getType4Link(pLink), pLink.getSourceId()));
		return;
	}

	/**
	 * Returns OpmProceduralLink of pLinkType(as it specified in Interface
	 * Constants in opmEntities package) in which entity with pEntityId
	 * participates as source and this ConnectionEdgeEntry participates as
	 * destination. null is returned if there is no such OpmProceduralLink in
	 * data structure.
	 */
	public OpmProceduralLink getLinkBySource(long pEntityId, int pLinkType) {
		return (OpmProceduralLink) (this.linkDestinations.get(new RelationKey(
				pLinkType, pEntityId)));
	}

	/**
	 * Returns an enumeration of the OpmStructuralRelation in which this
	 * ConnectionEdgeEntry paricipates as source. Use the Enumeration methods on
	 * the returned object to fetch the OpmStructuralRelation sequentially
	 * 
	 */
	public Enumeration<OpmStructuralRelation> getSourceRelations() {
		return this.relationSources.elements();
	}

	public Iterator<OpmGeneralRelation> getSourceGeneralRelations() {
		return ((LinkedList) generalRelationSources.clone()).iterator();
	}

	/**
	 * Returns Enumeration of IRelationEntry - all srtructural relations having
	 * this IConnectionEdgeEntry as source. Use the Enumeration methods on the
	 * returned object to fetch the IRelationEntry sequentially
	 */

	public Enumeration getRelationBySource() {
		Vector tVect = new Vector();
		for (Enumeration e = this.getSourceRelations(); e.hasMoreElements();) {
			OpmStructuralRelation tRel = (OpmStructuralRelation) e
					.nextElement();
			tVect.add(this.myProject.getComponentsStructure().getEntry(
					tRel.getId()));
		}

		for (Iterator i = this.getSourceGeneralRelations(); i.hasNext();) {
			OpmGeneralRelation tRel = (OpmGeneralRelation) i.next();
			tVect.add(this.myProject.getComponentsStructure().getEntry(
					tRel.getId()));
		}

		return tVect.elements();
	}

	/**
	 * Returns an enumeration of the OpmStructuralRelation in which this
	 * ConnectionEdgeEntry paricipates as destination. Use the Enumeration
	 * methods on the returned object to fetch the OpmStructuralRelation
	 * sequentially
	 * 
	 */
	public Enumeration getDestinationRelations() {
		return this.relationDestinations.elements();
	}

	public Iterator getDestinationGeneralRelations() {
		return this.generalRelationDestinations.iterator();
	}

	/**
	 * returns direct sons of an entry.
	 * 
	 * @return sons hash map <entry, link>
	 */
	public HashMap<Entry, OpmStructuralRelation> getDirectSonsWithOutInharatence() {

		HashMap<Entry, OpmStructuralRelation> ret = new HashMap<Entry, OpmStructuralRelation>();

		Enumeration<Entry> entries = myProject.getSystemStructure()
				.getElements();

		while (entries.hasMoreElements()) {
			Entry entry = entries.nextElement();
			if (entry instanceof FundamentalRelationEntry) {
				FundamentalRelationEntry fun = (FundamentalRelationEntry) entry;
				if (fun.getRelationType() != OpcatConstants.SPECIALIZATION_RELATION) {
					if (fun.getSourceId() == this.getId()) {
						ConnectionEdgeEntry conncted = (ConnectionEdgeEntry) myProject
								.getSystemStructure().getEntry(
										fun.getDestinationId());
						ret.put(conncted, (OpmStructuralRelation) fun
								.getLogicalEntity());
					}
				}
			} else if (entry instanceof GeneralRelationEntry) {
				GeneralRelationEntry fun = (GeneralRelationEntry) entry;
				if (fun.getSourceId() == this.getId()) {
					ConnectionEdgeEntry conncted = (ConnectionEdgeEntry) myProject
							.getSystemStructure().getEntry(
									fun.getDestinationId());
					ret.put(conncted, (OpmStructuralRelation) fun
							.getLogicalEntity());
				}
			}
		}

		// Iterator<OpmStructuralRelation> iter = Collections.list(
		// getSourceRelations()).iterator();
		//
		// while (iter.hasNext()) {
		// OpmStructuralRelation rel = (OpmStructuralRelation) iter.next();
		//
		// if ((rel instanceof OpmSpecialization)) {
		//
		// } else {
		// ConnectionEdgeEntry conncted = (ConnectionEdgeEntry) myProject
		// .getSystemStructure().getEntry(rel.getDestinationId());
		// ret.put(conncted, rel);
		// }
		// }
		//
		// Iterator<OpmGeneralRelation> gens = getSourceGeneralRelations();
		// while (gens.hasNext()) {
		// OpmStructuralRelation rel = (OpmStructuralRelation) gens.next();
		//
		// ConnectionEdgeEntry conncted = (ConnectionEdgeEntry) myProject
		// .getSystemStructure().getEntry(rel.getDestinationId());
		// ret.put(conncted, rel);
		// }

		return ret;
	}

	/**
	 * returns direct parents of an entry.
	 * 
	 * @return sons hash map <entry, link>
	 */
	public HashMap<Entry, OpmStructuralRelation> getDirectParents() {

		HashMap<Entry, OpmStructuralRelation> ret = new HashMap<Entry, OpmStructuralRelation>();

		Iterator<OpmStructuralRelation> iter = Collections.list(
				getDestinationRelations()).iterator();

		while (iter.hasNext()) {
			OpmStructuralRelation rel = (OpmStructuralRelation) iter.next();

			ConnectionEdgeEntry conncted = (ConnectionEdgeEntry) myProject
					.getSystemStructure().getEntry(rel.getSourceId());
			ret.put(conncted, rel);

		}

		Iterator<OpmGeneralRelation> gens = getDestinationGeneralRelations();
		while (gens.hasNext()) {
			OpmStructuralRelation rel = (OpmStructuralRelation) gens.next();

			ConnectionEdgeEntry conncted = (ConnectionEdgeEntry) myProject
					.getSystemStructure().getEntry(rel.getSourceId());
			ret.put(conncted, rel);
		}

		return ret;
	}

	/**
	 * Returns Enumeration of RelationEntry - all structural relations having
	 * this ConnectionEdgeEntry as destination. Use the Enumeration methods on
	 * the returned object to fetch the RelationEntry sequentially
	 */

	public Enumeration getRelationByDestination() {
		Vector tVect = new Vector();
		for (Enumeration e = this.getDestinationRelations(); e
				.hasMoreElements();) {
			OpmStructuralRelation tRel = (OpmStructuralRelation) e
					.nextElement();
			tVect.add(this.myProject.getComponentsStructure().getEntry(
					tRel.getId()));
		}

		for (Iterator i = this.getDestinationGeneralRelations(); i.hasNext();) {
			OpmStructuralRelation tRel = (OpmStructuralRelation) i.next();
			tVect.add(this.myProject.getComponentsStructure().getEntry(
					tRel.getId()));
		}

		return tVect.elements();
	}

	/**
	 * Returns an enumeration of the OpmProceduralLink in which this
	 * ConnectionEdgeEntry paricipates as source. Use the Enumeration methods on
	 * the returned object to fetch the OpmProceduralLink sequentially
	 * 
	 */
	public Enumeration<OpmProceduralLink> getSourceLinks() {
		return this.linkSources.elements();
	}

	/**
	 * Returns Enumeration of LinkEntry - all links having this
	 * ConnectionEdgeEntry as source. Use the Enumeration methods on the
	 * returned object to fetch the LinkEntry sequentially
	 */

	public Enumeration getLinksBySource() {

		Vector tVect = new Vector();
		for (Enumeration e = this.getSourceLinks(); e.hasMoreElements();) {
			OpmProceduralLink tLink = (OpmProceduralLink) e.nextElement();
			tVect.add(this.myProject.getComponentsStructure().getEntry(
					tLink.getId()));
		}

		return tVect.elements();
	}

	/**
	 * Returns an enumeration of the OpmProceduralLink in which this
	 * ConnectionEdgeEntry paricipates as destination. Use the Enumeration
	 * methods on the returned object to fetch the OpmProceduralLink
	 * sequentially
	 * 
	 */
	public Enumeration<OpmProceduralLink> getDestinationLinks() {
		return this.linkDestinations.elements();
	}

	/**
	 * Returns Enumeration of LinkEntry - all links having this
	 * ConnectionEdgeEntry as destination. Use the Enumeration methods on the
	 * returned object to fetch the LinkEntry sequentially
	 */

	public Enumeration getLinksByDestination() {

		Vector tVect = new Vector();
		for (Enumeration e = this.getDestinationLinks(); e.hasMoreElements();) {
			OpmProceduralLink tLink = (OpmProceduralLink) e.nextElement();
			tVect.add(this.myProject.getComponentsStructure().getEntry(
					tLink.getId()));
		}

		return tVect.elements();
	}

	public Enumeration getSourceRelations(int relType) {
		OpmStructuralRelation tmpRel;
		Vector v = new Vector();
		for (Enumeration e = this.getSourceRelations(); e.hasMoreElements();) {
			tmpRel = (OpmStructuralRelation) e.nextElement();
			if (Constants.getType4Relation(tmpRel) == relType) {
				v.addElement(tmpRel);
			}
		}
		return v.elements();
	}

	public Enumeration getDestinationRelations(int relType) {
		OpmStructuralRelation tmpRel;
		Vector v = new Vector();
		for (Enumeration e = this.relationDestinations.elements(); e
				.hasMoreElements();) {
			tmpRel = (OpmStructuralRelation) e.nextElement();
			if (Constants.getType4Relation(tmpRel) == relType) {
				v.addElement(tmpRel);
			}
		}
		return v.elements();
	}

	public Enumeration getSourceLinks(int linkType) {
		OpmProceduralLink tmpLink;
		Vector v = new Vector();
		for (Enumeration e = this.linkSources.elements(); e.hasMoreElements();) {
			tmpLink = (OpmProceduralLink) e.nextElement();
			if (Constants.getType4Link(tmpLink) == linkType) {
				v.addElement(tmpLink);
			}
		}
		return v.elements();
	}

	public Enumeration getDestinationLinks(int linkType) {
		OpmProceduralLink tmpLink;
		Vector v = new Vector();
		for (Enumeration e = this.linkDestinations.elements(); e
				.hasMoreElements();) {
			tmpLink = (OpmProceduralLink) e.nextElement();
			if (Constants.getType4Link(tmpLink) == linkType) {
				v.addElement(tmpLink);
			}
		}
		return v.elements();
	}

	/**
	 * Returns an enumeration of OpmEntity (all relations and links) in which
	 * this ConnectionEdgeEntry paricipates. Use the Enumeration methods on the
	 * returned object to fetch the OpmStructuralRelation sequentially
	 * 
	 */

	public Enumeration getAllRelatedEntities() {
		Vector v = new Vector();

		v.addAll(this.linkSources.values());
		v.addAll(this.linkDestinations.values());
		v.addAll(this.relationSources.values());
		v.addAll(this.relationDestinations.values());

		Object tempArray[] = this.generalRelationSources.toArray();

		for (int i = 0; i < tempArray.length; i++) {
			v.add(tempArray[i]);
		}

		tempArray = this.generalRelationDestinations.toArray();

		for (int i = 0; i < tempArray.length; i++) {
			v.add(tempArray[i]);
		}

		return v.elements();
	}

	public void updateInstances() {
		for (Enumeration e = this.getInstances(); e.hasMoreElements();) {
			((ConnectionEdgeInstance) (e.nextElement())).update();
		}
	}

	public void completeLinks(Opd opd, boolean entryIsSource,
			boolean addMisingThings, ConnectionEdgeInstance myInstance) {

		long opdID = opd.getOpdId();
		Enumeration<OpmProceduralLink> linksEnum = null;
		MainStructure system = myProject.getSystemStructure();

		if (entryIsSource) {
			if (this instanceof ObjectEntry) {
				linksEnum = ((ObjectEntry) this).getSourceLinks(true);
			} else {
				linksEnum = getSourceLinks();
			}
		} else {
			if (this instanceof ObjectEntry) {
				linksEnum = ((ObjectEntry) this).getDestinationLinks(true);
			} else {
				linksEnum = getDestinationLinks();
			}
		}

		while (linksEnum.hasMoreElements()) {

			OpmProceduralLink opmLink = linksEnum.nextElement();

			LinkEntry linkEntry = (LinkEntry) system.getEntry(opmLink.getId());

			ConnectionEdgeEntry myEdge = null;

			ConnectionEdgeEntry otherEdge = null;
			if (entryIsSource) {
				otherEdge = (ConnectionEdgeEntry) system.getEntry(opmLink
						.getDestinationId());
				myEdge = (ConnectionEdgeEntry) system.getEntry(opmLink
						.getSourceId());
			} else {
				otherEdge = (ConnectionEdgeEntry) system.getEntry(opmLink
						.getSourceId());
				myEdge = (ConnectionEdgeEntry) system.getEntry(opmLink
						.getDestinationId());
			}

			if (this instanceof ObjectEntry) {

			}

			if (!otherEdge.hasInstanceInOpd(opdID)) {
				/**
				 * if other instance is not in the OPD then add it if the user
				 * marked the addMisingThings param
				 */
				if (addMisingThings) {
					Instance otherInstance = otherEdge.getInstances()
							.nextElement();
					EditControl.getInstance().getCurrentProject().deselectAll();
					EditControl.getInstance().getCurrentProject()
							.clearClipBoard();

					Container parent = opd.getDrawingArea();
					int x = 1;
					int y = 1;
					if ((myInstance != null)
							&& (myInstance.getParent() != null)) {
						int ret = JOptionPane.showConfirmDialog(opd
								.getDrawingArea(), "Insert into main Entity ?",
								"Complete Link", JOptionPane.YES_NO_OPTION);
						if (ret == JOptionPane.YES_OPTION) {
							parent = myInstance.getParent();
							x = myInstance.getX() + 30;
							y = myInstance.getY();
						}
					}
					if (otherInstance instanceof StateInstance) {
						ObjectInstance objectIns = ((StateInstance) otherInstance)
								.getParentObjectInstance();
						otherInstance.getOpd().getSelection().addSelection(
								objectIns, true);
						myProject._copy(otherInstance.getOpd(), opd, x, y,
								parent, true);
					} else {
						otherInstance.getOpd().getSelection().addSelection(
								otherInstance, true);
						myProject._copy(otherInstance.getOpd(), opd, x, y,
								parent, true);
					}

					Instance insertedInstance = otherInstance;
					Enumeration<Instance> insertedInstances = system
							.getInstanceInOpd(opd, otherInstance.getEntry()
									.getId());

					if (insertedInstances != null) {
						while (insertedInstances.hasMoreElements()) {
							insertedInstance = insertedInstances.nextElement();
							Point location = insertedInstance
									.getGraphicalRepresentation().getLocation();
							((ConnectionEdgeInstance) insertedInstance)
									.getConnectionEdge().fitToContent();
							insertedInstance.getGraphicalRepresentation()
									.setLocation(location);
						}
					}
				}
			}
			/**
			 * now search instances of this entry in this OPD
			 */
			if (otherEdge.hasInstanceInOpd(opdID)
					&& myEdge.hasInstanceInOpd(opdID)) {
				Enumeration<Instance> otherInstancesEnum = system
						.getInstanceInOpd(opd, otherEdge.getId());
				/**
				 * we have the connected instances in this OPD, see if they are
				 * alrady connected. if not then conect them.
				 * 
				 */
				boolean found = false;
				while (otherInstancesEnum.hasMoreElements()) {
					found = false;
					ConnectionEdgeInstance connectionEdgeInstance = (ConnectionEdgeInstance) otherInstancesEnum
							.nextElement();

					Enumeration<Instance> instances = myEdge.getInstances();
					Enumeration<Instance> sourceLinkInstances = linkEntry
							.getInstances();

					while (instances.hasMoreElements()) {
						Instance instance = instances.nextElement();

						if (opd.isContaining(instance)) {
							while (sourceLinkInstances.hasMoreElements()) {
								LinkInstance sourceLinkInstance = (LinkInstance) sourceLinkInstances
										.nextElement();

								if (opd.isConnected(instance,
										connectionEdgeInstance,
										sourceLinkInstance)) {
									found = true;
									break;
								}
							}
							if (!found) {
								/**
								 * add the link here,
								 */
								LinkInstance li = null;
								if (entryIsSource) {
									li = myProject.addLink(instance,
											connectionEdgeInstance, linkEntry
													.getLinkType(), opd);
								} else {
									li = myProject.addLink(
											connectionEdgeInstance, instance,
											linkEntry.getLinkType(), opd);
								}
								if (li != null) {
									Opcat2
											.getUndoManager()
											.undoableEditHappened(
													new UndoableEditEvent(
															this,
															new UndoableAddLink(
																	myProject,
																	li)));
									Opcat2.setUndoEnabled(Opcat2
											.getUndoManager().canUndo());
									Opcat2.setRedoEnabled(Opcat2
											.getUndoManager().canRedo());
								}

							}
							break;
						}
					}
				}
			}
		}

	}

	public String getTypeString() {
		return "Connection Edge";
	}
}
