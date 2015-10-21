package extensionTools.uml.umlDiagrams;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IInstance;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IProcessInstance;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.IStateEntry;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IThingInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;

/**
 * creates the xml code for sequence diagram part
 */
public class sequence_d {

	public sequence_d() {
	}

	ISystemStructure MyStructure;

	FileOutputStream file;

	// ----------------------------isActor----------------------------------------
	/**
	 * Checks if the given object is enviormenatal, meaning an "Actor".
	 * 
	 * @param IObjectEntry
	 *            obj
	 * @return true-if the given object is an "Actor"; false - otherwise
	 */
	private boolean isActor(IObjectEntry obj) {
		if (obj.isEnvironmental()) {
			return true;
		}
		return false;
	}

	// -----------------------------isClass----------------------------------------
	/**
	 * Checks if the given object is informatical and systematic, meaning a
	 * "Class".
	 * 
	 * @param IObjectEntry
	 *            obj
	 * @return true-if the given object is an "Class"; false - otherwise
	 */
	private boolean isClass(IObjectEntry obj) {
		Enumeration e;
		IRelationEntry rel;
		int relType;

		if ((obj.isEnvironmental()) || (obj.isPhysical())) {
			return false;
		}
		e = obj.getRelationByDestination();
		while (e.hasMoreElements()) {
			rel = (IRelationEntry) e.nextElement();
			relType = rel.getRelationType();
			if ((relType == OpcatConstants.EXHIBITION_RELATION)
					|| (relType == OpcatConstants.INSTANTINATION_RELATION)) {
				return false;
			}
		}
		return true;
	}

	// ----------------------------isInVec-----------------------------------
	/**
	 * Checks if the given entry is already in the given Vector
	 * 
	 * @param IEntry
	 *            entry
	 * @param Vector
	 *            vec
	 * @return true-if the given entry is already in the given vector vec; false -
	 *         otherwise
	 */
	private boolean isInVec(IEntry entry, Vector vec) {
		Enumeration e;
		IEntry AbstractEntry;
		e = vec.elements();
		while (e.hasMoreElements()) {
			AbstractEntry = (IEntry) e.nextElement();
			if (AbstractEntry.getId() == entry.getId()) {
				return true;
			}
		}
		return false;
	}

	// ----------------------------getPathsVector-----------------------------------
	/**
	 * Finds all the paths of a given process--> the result returns in vector
	 * pathVec
	 * 
	 * @param IProcessEntry
	 *            proc - checks the paths of links connected to this process
	 * @param Vector
	 *            pathVec - contains al the found paths (strings)
	 */
	private void getPathsVector(IProcessEntry proc, Vector pathVec) {

		Enumeration locenum;
		ILinkEntry link;
		String path;
		Vector linkVec = new Vector(4, 2);

		this.getLinkVec(proc, linkVec);
		locenum = linkVec.elements();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			path = link.getPath();
			if (!(path == "") && (!this.isInVec(path, pathVec))) {
				pathVec.addElement(path);
			}
		}
	}

	// ----------------------------isInVec-----------------------------------
	/**
	 * Checks if the given String is already in the given Vector
	 * 
	 * @param String
	 *            path
	 * @param Vector
	 *            vec
	 * @return true-if the given path (string) is already in the given vector
	 *         Vec; false - otherwise
	 */
	private boolean isInVec(String path, Vector vec) {
		Enumeration locenum = vec.elements();
		while (locenum.hasMoreElements()) {
			if (path.compareTo((String) locenum.nextElement()) == 0) {
				return true;
			}
		}
		return false;
	}

	// ----------------------------getLinkVec----------------------------------------
	/**
	 * Finds all connected links to the given process
	 * 
	 * @param IProcessEntry
	 *            proc - process we want to find the links for
	 * @param Vector
	 *            linkVec - contains all links connected to the given process
	 */
	private void getLinkVec(IProcessEntry proc, Vector linkVec) {
		this.getIncomeLinks(proc, linkVec);
		this.getOutgoingLinks(proc, linkVec);
	}

	/**
	 * Finds all link connected to the given proc, when proc is a destination
	 * 
	 * @param IProcessEntry
	 *            proc - process we want to find the links for
	 * @param Vector
	 *            linkVec - contains the links found
	 */
	private void getIncomeLinks(IProcessEntry proc, Vector linkVec) {
		Enumeration locenum;
		ILinkEntry link;

		locenum = proc.getLinksByDestination();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			linkVec.addElement(link);
		}
	}

	/**
	 * Finds all link connected to the given proc, when proc is a source
	 * 
	 * @param IProcessEntry
	 *            proc - process we want to find the links for
	 * @param Vector
	 *            linkVec - contains the links found
	 */
	private void getOutgoingLinks(IProcessEntry proc, Vector linkVec) {
		Enumeration locenum;
		ILinkEntry link;

		locenum = proc.getLinksBySource();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			linkVec.addElement(link);
		}
	}

	// ---------------------isBasic-------------------------------------------------
	/**
	 * Function check if the given proc is in its lowest level (no InZooming and
	 * no Unfolding).
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @return true if the given proc has no InZooming and no Unfolding ;false -
	 *         otherwise
	 */
	private boolean isBasic(IProcessEntry proc) {
		Enumeration locenum;
		IProcessInstance procInst;
		Vector vec = new Vector(2, 2);

		this.getUnfoldChildVec(proc, vec);
		locenum = proc.getInstances();
		while (locenum.hasMoreElements()) {
			procInst = (IProcessInstance) locenum.nextElement();
			if ((procInst.isZoomedIn()) || (!vec.isEmpty())) {
				return false;
			}
		}
		return true;
	}

	// -----------------------------C L A S
	// S--------------------------------------
	/**
	 * structure used to define what kind of invocation link does the proc has.
	 * flag can be 0,1,2 as defined in function hasInvocation.
	 */
	public class ProcInvocOutgoing {
		IProcessInstance inst;

		int flag;

		ProcInvocOutgoing() {
		}

		ProcInvocOutgoing(IProcessInstance proc, int flag1) {
			this.inst = proc;
			this.flag = flag1;
		}

		public IProcessInstance getProcInst() {
			return this.inst;
		}

		public int getFlag() {
			return this.flag;
		}
	}

	/**
	 * Function fills vec with items of procs that has no invocation or has only
	 * outgoing invocation
	 * 
	 * @param Enumeration
	 *            thingsList - contains IInstance elements
	 * @param Vector
	 *            vec - contains the needed procInst.
	 */
	private void createItemsVec(Enumeration thingsList, Vector vec) {
		IInstance inst;
		int flag = -1;
		while (thingsList.hasMoreElements()) {
			inst = (IInstance) thingsList.nextElement();
			if (inst instanceof IProcessInstance) {
				flag = this
						.hasInvocation((IProcessEntry) ((IProcessInstance) inst)
								.getIEntry());
				if ((flag == 0) || (flag == 1)) {
					ProcInvocOutgoing procInst = new ProcInvocOutgoing(
							(IProcessInstance) inst, flag);
					vec.addElement(procInst);
				}
			}
		}
	}

	/**
	 * Function creates an ordered vector(by vertical position Y, or by
	 * invocation link) from all process in InZooming of the given proc.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            orderVec - contains the ordered procs.
	 */
	private void sortByOrder(IProcessEntry proc, Vector orderVec) {
		Vector vec = new Vector(4, 2);
		ProcInvocOutgoing item, item1;
		Enumeration locenum, thingsList;
		IProcessInstance procInst;

		locenum = proc.getInstances();
		procInst = (IProcessInstance) locenum.nextElement();// first inst of
		// proc
		thingsList = procInst.getChildThings();// list of children of first
		// inst
		while (locenum.hasMoreElements()) {// if there are more insts of proc
			if (thingsList.hasMoreElements()) {
				// privios inst
				break;
			}
			procInst = (IProcessInstance) locenum.nextElement();
			thingsList = procInst.getChildThings();
		}
		this.createItemsVec(thingsList, vec);

		while (!(vec.isEmpty())) {
			locenum = vec.elements();
			item1 = (ProcInvocOutgoing) locenum.nextElement();
			while (locenum.hasMoreElements()) { // finds minimum item by Y
				item = (ProcInvocOutgoing) locenum.nextElement();
				if (item1.inst.getY() >= item.inst.getY()) {
					item1 = item;
				}
			}
			vec.removeElement(item1);
			if (item1.flag == 0) {
				orderVec.addElement(item1.inst.getIEntry());
			}
			if (item1.flag == 1) {
				this.insertByInvocation((IProcessEntry) item1.inst.getIEntry(),
						proc, orderVec);
			}
		}
	}

	/**
	 * Function inserts the given proc to the given vector orderVec, and
	 * recursevly activeted on all process connected to the given process proc
	 * by an invocation link.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param IProcessEntry
	 *            fatherProc
	 * @param Vector
	 *            orderVec
	 */
	private void insertByInvocation(IProcessEntry proc,
			IProcessEntry fatherProc, Vector orderVec) {
		Enumeration locenum;
		IProcessEntry destProc;
		ILinkEntry link;

		orderVec.addElement(proc);
		locenum = proc.getLinksBySource();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
				destProc = (IProcessEntry) this.MyStructure.getIEntry(link
						.getDestinationId());
				if (destProc.getId() != fatherProc.getId()) {
					this.insertByInvocation(destProc, fatherProc, orderVec);
				}
			}
		}
		return;
	}

	/**
	 * Checks if there is an invocation link incoming/outgoing from the given
	 * proc.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @return int - 0: there is not incoming or outgoing invocation link from
	 *         the proc 1: there is outgoing invocation link from the proc but
	 *         not incoming 2: other
	 */
	private int hasInvocation(IProcessEntry proc) {
		ILinkEntry link;
		Enumeration e;

		e = proc.getLinksByDestination();
		while (e.hasMoreElements()) {
			link = (ILinkEntry) e.nextElement();
			if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
				return 2;// other
			}
		}
		e = proc.getLinksBySource();
		while (e.hasMoreElements()) {
			link = (ILinkEntry) e.nextElement();
			if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
				return 1;// out
			}
		}
		return 0;// no out and no in;
	}

	// *******************************getProcForList*********************************
	/**
	 * Class used to define a position of a given proc.
	 */
	public class ObjPosition {
		int y;

		int height;

		IProcessEntry proc;

		public ObjPosition() {
		}

		public ObjPosition(int y1, int height1, IProcessEntry proc1) {
			this.y = y1;
			this.height = height1;
			this.proc = proc1;
		}

		public int getY() {
			return this.y;
		}

		public int getHeight() {
			return this.height;
		}

		public IProcessEntry getProcEntry() {
			return this.proc;
		}
	}

	/**
	 * Check if the given proc has order in all its levels.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @return true - if there are not paralel procs in all levels inside the
	 *         given proc; false - otherwise.
	 */
	private boolean existProcOrder(IProcessEntry proc) {
		
		Enumeration<ThingInstance>  childrenList = null ; 
		IProcessEntry child = null ;
		IEntry abstractEntry = null ;
		boolean flag = true ;
		
		childrenList = Collections.enumeration((((ProcessEntry) proc).GetInZoomedIncludedInstances())) ; 
		
		if (!this.hasParalelChildren(proc)) {
			while (childrenList.hasMoreElements()) {
				abstractEntry = ((IInstance) childrenList.nextElement())
						.getIEntry();
				if (abstractEntry instanceof IProcessEntry) {
					child = (IProcessEntry) abstractEntry;
					flag = this.existProcOrder(child);
					if (!flag) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Function fills the given vec with procs connected to the given proc by
	 * aggregation relation, when proc is a source of a relation.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            vec
	 */
	private void getUnfoldChildVec(IProcessEntry proc, Vector<IEntry> vec) {
		Enumeration locenum;
		IRelationEntry rel;
		IEntry abstrEntry;

		locenum = proc.getRelationBySource();
		while (locenum.hasMoreElements()) {
			rel = (IRelationEntry) locenum.nextElement();
			if (rel.getRelationType() == OpcatConstants.AGGREGATION_RELATION) {
				abstrEntry = this.MyStructure.getIEntry(rel.getDestinationId());
				if ((abstrEntry instanceof IProcessEntry)
						&& !(((ProcessEntry) proc)
								.isIncludedInZoomedIn((ThingEntry) abstrEntry))) {
					vec.addElement(abstrEntry);
				}
			}
		}
	}

	/**
	 * Checks if there are parlel procs in InZooming or Unfolding of the given
	 * proc.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Enumeration
	 *            childrenList - all procs in InZooming of proc.
	 * @return true - if there are paralel procs in InZooming or Unfolding of
	 *         the given proc; false - otherwise.
	 */
	private boolean hasParalelChildren(IProcessEntry proc) {
		
		Enumeration childrenList = Collections.enumeration((((ProcessEntry) proc).GetInZoomedIncludedInstances())) ;
		Vector vec = new Vector(4, 2);

		this.getUnfoldChildVec(proc, vec);
		if ((!childrenList.hasMoreElements()) && (vec.isEmpty())) {
			return false;
		}
		if ((childrenList.hasMoreElements()) && (!vec.isEmpty())) {
			return true;
		}
		if ((this.hasInzoomedParalelChildren(childrenList))
				|| (this.hasUnfoldParalelChildren(proc, vec))) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if there are paralel procs in the given vector "vec".
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            vec - vector of procs.
	 * @return true - if there are paralel procs ; false - otherwise.
	 */
	private boolean hasUnfoldParalelChildren(IProcessEntry proc, Vector vec) {
		Enumeration locenum;
		IProcessEntry child;
		int count = 0;
		int status;

		locenum = vec.elements();
		while (locenum.hasMoreElements()) {
			child = (IProcessEntry) locenum.nextElement();
			status = this.hasInvocation(child);
			if ((status == 0) && (vec.size() > 1)) {
				// without incoming/outgoing
				// invocation
				return true;
			}
			if (status == 1) {
				count++;
			}
			if (count > 1) {
				// invocation
				return true;
			}
		}
		return false; // if we are here there is sequence between all the
		// procs
	}

	/**
	 * Checks if the are paralel procs in the given enumeration.
	 * 
	 * @param Enumeration
	 *            childrenList - enumeraton of procs.
	 * @return true - if there are paralel procs in the given enumeration; false -
	 *         otherwise.
	 */
	private boolean hasInzoomedParalelChildren(Enumeration childrenList) {
		Vector<ObjPosition> vec = new Vector<ObjPosition>(4, 2);
		IInstance abstractInst;
		IProcessEntry proc;
		IProcessInstance procInst;
		Object[] arr1;
		Object[] arr2;

		while (childrenList.hasMoreElements()) {
			abstractInst = (IInstance) childrenList.nextElement();
			if (abstractInst instanceof IProcessInstance) {
				procInst = (IProcessInstance) abstractInst;
				proc = (IProcessEntry) procInst.getIEntry();
				ObjPosition objPos = new ObjPosition(procInst.getY(), procInst
						.getHeight(), proc);
				vec.addElement(objPos);
			}
		}
		arr1 = vec.toArray();
		arr2 = vec.toArray();
		for (int i = 0; i < arr1.length; i++) {
			for (int j = i + 1; j < arr2.length; j++) {
				if (this
						.isParalel((ObjPosition) arr1[i], (ObjPosition) arr2[j])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Function checks if the two given positions of procs are paralel.
	 * 
	 * @param ObjPosition
	 *            pos1
	 * @param ObjPosition
	 *            pos2
	 * @return true - if the two given positions of procs are paralel; false -
	 *         otherwise.
	 */
	private boolean isParalel(ObjPosition pos1, ObjPosition pos2) {
		if (this.hasInvocLink(pos1.proc, pos2.proc)) {
			return false;
		}
		if (((pos1.getY() >= pos2.getY()) && (pos1.getY() < (pos2.getY() + pos2
				.getHeight())))
				|| ((pos2.getY() >= pos1.getY()) && (pos2.getY() < (pos1.getY() + pos1
						.getHeight())))) {
			return true;
		}
		return false;
	}

	/**
	 * Function checks if there is an invocation link between two given procs.
	 * 
	 * @param IProcessEntry
	 *            proc1
	 * @param IProcessEntry
	 *            proc2
	 * @return true - if there is an invocation link between two given procs;
	 *         false - otherwise.
	 */
	private boolean hasInvocLink(IProcessEntry proc1, IProcessEntry proc2) {
		Enumeration e;
		ILinkEntry link;

		e = proc1.getLinksBySource();
		while (e.hasMoreElements()) {
			link = (ILinkEntry) e.nextElement();
			if ((link.getLinkType() == OpcatConstants.INVOCATION_LINK)
					&& (link.getDestinationId() == proc2.getId())) {
				return true;
			}
		}
		e = proc1.getLinksByDestination();
		while (e.hasMoreElements()) {
			link = (ILinkEntry) e.nextElement();
			if ((link.getLinkType() == OpcatConstants.INVOCATION_LINK)
					&& (link.getSourceId() == proc2.getId())) {
				return true;
			}
		}
		return false;
	}

	// --------------------getProcForList----------------------------------------------
	/**
	 * Calculates all process that will appear in the list presented to the user
	 * to choose from, when he asks to generate a sequence diagram.
	 * 
	 * @param ISystemStructure
	 *            myStructure
	 * @param Vector
	 *            vecProc - contains all found procs that need to appear in the
	 *            list.
	 */
	public void getProcForList(ISystemStructure myStructure, Vector vecProc) {
		Enumeration locenum;
		IEntry abstractEntry;
		IProcessEntry proc;
		this.MyStructure = myStructure;
		locenum = this.MyStructure.getElements();
		while (locenum.hasMoreElements()) {
			abstractEntry = (IEntry) locenum.nextElement();
			if (abstractEntry instanceof IProcessEntry) {
				proc = (IProcessEntry) abstractEntry;
				if ((!this.isBasic(proc)) && (this.existProcOrder(proc))) {
					vecProc.addElement(proc);
				}
			}
		}
	}

	// ----------------------------getChildrenProcs----------------------------------
	/**
	 * Function creates vector with all the childPcocs of the proc from all
	 * levels down
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            ChildProcsVec - contains the found procs.
	 */
	private void getChildProcs(IProcessEntry proc, Vector ChildProcsVec) {
		Enumeration locenum, childrenList;
		IProcessInstance procInst;
		IEntry abstractEntry;
		IProcessEntry child;

		locenum = proc.getInstances();
		procInst = (IProcessInstance) locenum.nextElement();
		childrenList = procInst.getChildThings();
		while (locenum.hasMoreElements()) {
			if (childrenList.hasMoreElements()) {
				break;
			}
			procInst = (IProcessInstance) locenum.nextElement();
			childrenList = procInst.getChildThings();
		}
		while (childrenList.hasMoreElements()) {
			abstractEntry = ((IInstance) childrenList.nextElement())
					.getIEntry();
			if (abstractEntry instanceof IProcessEntry) {
				child = (IProcessEntry) abstractEntry;
				ChildProcsVec.addElement(child);
				if (!this.isBasic(child)) {
					this.getChildProcs(child, ChildProcsVec);
				}
			}
		}
	}

	// ---------------------------getLinkedObjects-----------------------------------
	/**
	 * Function creates vector which contains all objects that linked to the
	 * given proc or its chilren (all levels down).
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            ObjVec
	 * @param String
	 *            path
	 */
	private void getLinkedObjects(IProcessEntry proc, Vector ObjVec, String path) {
		Vector childrenVec = new Vector(4, 2);
		Enumeration locenum;
		IProcessEntry child;

		this.addObjects(proc, ObjVec, path);
		this.getChildProcs(proc, childrenVec);
		locenum = childrenVec.elements();
		while (locenum.hasMoreElements()) {
			child = (IProcessEntry) locenum.nextElement();
			this.addObjects(child, ObjVec, path);
		}
	}

	/**
	 * Functions finds all connected Objects to the given proc by links with a
	 * given path. The found objects are at their meaning "Actors" or
	 * "UseCases".
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            ObjVec - contains the found objects
	 * @param String
	 *            path
	 */
	private void addObjects(IProcessEntry proc, Vector ObjVec, String path) {
		Enumeration locenum;
		IEntry abstractEntry;
		ILinkEntry link;
		IObjectEntry obj;

		locenum = proc.getLinksByDestination(); /* all links proc=destination */
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement(); /* link itself */
			if ((path == "") || (path.compareTo(link.getPath()) == 0)) {
				abstractEntry = this.MyStructure.getIEntry(link.getSourceId());
				if ((abstractEntry instanceof IObjectEntry)
						|| (abstractEntry instanceof IStateEntry)) { /*
																		 * if
																		 * source=IObjectEntry
																		 */
					if (abstractEntry instanceof IObjectEntry) {
						obj = (IObjectEntry) abstractEntry;
					} else {
						obj = ((IStateEntry) abstractEntry)
								.getParentIObjectEntry();
					}
					if (((this.isActor(obj)) || (this.isClass(obj)))
							&& (!this.isInVec(obj, ObjVec))) {
						ObjVec.addElement(obj);
					} else {
						obj = this.whoseFeature(obj);
						if ((obj != null) && (!this.isInVec(obj, ObjVec))) {
							ObjVec.addElement(obj);
						}
					}

				}
			}
		}
		locenum = proc.getLinksBySource(); /* all links proc=source */
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement(); /* link itself */
			if ((path == "") || (path.compareTo(link.getPath()) == 0)) {
				abstractEntry = this.MyStructure.getIEntry(link
						.getDestinationId());
				if ((abstractEntry instanceof IObjectEntry)
						|| (abstractEntry instanceof IStateEntry)) {
					if (abstractEntry instanceof IObjectEntry) {
						obj = (IObjectEntry) abstractEntry; /* destination */
					} else {
						obj = ((IStateEntry) abstractEntry)
								.getParentIObjectEntry();
					}
					if (((this.isActor(obj)) || (this.isClass(obj)))
							&& (!this.isInVec(obj, ObjVec))) {
						ObjVec.addElement(obj);
					} else {
						obj = this.whoseFeature(obj);
						if ((obj != null) && (!this.isInVec(obj, ObjVec))) {
							ObjVec.addElement(obj);
						}
					}
				}
			}
		}
	}

	// *****************************************************************************
	// ----------------------------C L A S
	// S----------------------------------------
	/**
	 * class used to define transitions in sequence diagram and it`s order.
	 */
	public class SequenceEntry {
		IProcessEntry headProc;

		IProcessEntry msgProc;

		String path;

		IObjectEntry srcObj;

		IObjectEntry destObj;

		SequenceEntry() {
		}

		SequenceEntry(IProcessEntry headProc1, IProcessEntry msgProc1,
				String path1, IObjectEntry srcObj1, IObjectEntry destObj1) {
			this.headProc = headProc1;
			this.msgProc = msgProc1;
			this.path = path1;
			this.srcObj = srcObj1;
			this.destObj = destObj1;
		}

		public IProcessEntry getHeadProc() {
			return this.headProc;
		}

		public IProcessEntry getMsgProc() {
			return this.msgProc;
		}

		public String getPath() {
			return this.path;
		}

		public IObjectEntry getSrcObj() {
			return this.srcObj;
		}

		public IObjectEntry getDestObj() {
			return this.destObj;
		}
	}

	// --------------------------isDestType------------------------------------------
	/**
	 * Checks if links type is a "destination type", meaning if the type is one
	 * of the following: consumtion link, effect link, instrument link, result
	 * link.
	 * 
	 * @param ILinkEntry
	 *            link
	 * @return true - if links type is on of destinations types; fulse -
	 *         otherwise
	 */
	private boolean isDestType(ILinkEntry link) {
		if ((link.getLinkType() == OpcatConstants.CONSUMPTION_LINK)
				|| (link.getLinkType() == OpcatConstants.EFFECT_LINK)
				|| (link.getLinkType() == OpcatConstants.INSTRUMENT_LINK)
				|| (link.getLinkType() == OpcatConstants.RESULT_LINK)) {
			return true;
		}
		return false;
	}

	/**
	 * This function returns true if the given IObjectEntry obj is a feature of
	 * another IObjectEntry, (that is connected as a destination by
	 * EXHIBITION_RELATION to another IObjectEntry) else return false.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param IObjectEntry
	 *            obj
	 * @return true-in case the given object is an feature, otherwise false.
	 */
	private IObjectEntry whoseFeature(IObjectEntry obj) {
		Enumeration e;
		IEntry abstractEntry;
		IRelationEntry rel;
		IObjectEntry newObj, newObj2;

		e = obj.getRelationByDestination();
		while (e.hasMoreElements()) {
			rel = (IRelationEntry) e.nextElement();
			if (rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION) {
				abstractEntry = (IEntry) this.MyStructure.getIEntry(rel
						.getSourceId());
				if (abstractEntry instanceof IObjectEntry) {
					newObj = (IObjectEntry) abstractEntry;
					if ((this.isActor(newObj)) || (this.isClass(newObj))) {
						return newObj;
					} else {
						newObj2 = this.whoseFeature(newObj);
						if (newObj2 != null) {
							return newObj2;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Finds all the objects linked by the given path to the given proc
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param String
	 *            path
	 * @param Vector
	 *            vec - contains all found objects
	 */
	private void getDestObjsLinkedDirect(IProcessEntry proc, String path,
			Vector vec) {
		Enumeration locenum;
		ILinkEntry link;
		IEntry abstrEntry;
		IObjectEntry obj;

		locenum = proc.getLinksByDestination();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			if ((link.getPath().compareTo(path) == 0)
					&& (this.isDestType(link))) {
				abstrEntry = this.MyStructure.getIEntry(link.getSourceId());
				if ((abstrEntry instanceof IObjectEntry)
						|| (abstrEntry instanceof IStateEntry)) {
					if (abstrEntry instanceof IObjectEntry) {
						obj = (IObjectEntry) abstrEntry;
					} else {
						obj = ((IStateEntry) abstrEntry)
								.getParentIObjectEntry();
					}
					if (this.isActor(obj) || this.isClass(obj)) {
						vec.addElement(obj);
					} else {
						obj = this.whoseFeature(obj);
						if (obj != null) {
							vec.addElement(obj);
						}
					}
				}
			}
		}
		locenum = proc.getLinksBySource();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			if ((link.getPath().compareTo(path) == 0)
					&& (this.isDestType(link))) {
				abstrEntry = this.MyStructure
						.getIEntry(link.getDestinationId());
				if ((abstrEntry instanceof IObjectEntry)
						|| (abstrEntry instanceof IStateEntry)) {
					if (abstrEntry instanceof IObjectEntry) {
						obj = (IObjectEntry) abstrEntry;
					} else {
						obj = ((IStateEntry) abstrEntry)
								.getParentIObjectEntry();
					}
					if (this.isActor(obj) || this.isClass(obj)) {
						vec.addElement(obj);
					} else {
						obj = this.whoseFeature(obj);
						if (obj != null) {
							vec.addElement(obj);
						}
					}
				}
			}
		}

	}

	/**
	 * Finds all the procs from one level lower - all procs in InZooming of the
	 * given proc and those that connected as a destinations in aggregation
	 * relation to this proc.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            childVec - contains all found procs
	 */
	private void getSonProcs(IProcessEntry proc, Vector childVec) {
		Enumeration locenum, childrenList;
		IProcessInstance procInst;
		IEntry abstractEntry;
		IProcessEntry child;
		IRelationEntry rel;

		locenum = proc.getInstances();
		procInst = (IProcessInstance) locenum.nextElement();
		childrenList = procInst.getChildThings();
		while (childrenList.hasMoreElements()) {
			abstractEntry = ((IInstance) childrenList.nextElement())
					.getIEntry();
			if (abstractEntry instanceof IProcessEntry) {
				child = (IProcessEntry) abstractEntry;
				childVec.addElement(child);
			}
		}
		locenum = proc.getRelationBySource();
		while (locenum.hasMoreElements()) {
			rel = (IRelationEntry) locenum.nextElement();
			if (rel.getRelationType() == OpcatConstants.AGGREGATION_RELATION) {
				child = (IProcessEntry) this.MyStructure.getIEntry(rel
						.getDestinationId());
				childVec.addElement(child);
			}
		}
	}

	/**
	 * Function returns true if this is the last level that the given
	 * IObjectEntry obj appears in.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param String
	 *            path
	 * @param IObjectEntry
	 *            obj
	 * @return true-if it is the last OPD level the obj appears on;false -
	 *         otherwise.
	 */
	private boolean isLastInst(IProcessEntry proc, String path, IObjectEntry obj) {
		Vector childVec = new Vector(4, 2);
		Enumeration locenum, linksEnum;
		IProcessEntry child;
		ILinkEntry link;
		boolean flag = true;

		this.getSonProcs(proc, childVec);
		locenum = childVec.elements();
		while ((locenum.hasMoreElements()) && (flag)) {
			child = (IProcessEntry) locenum.nextElement();
			linksEnum = child.getLinksBySource();
			while (linksEnum.hasMoreElements()) {
				link = (ILinkEntry) linksEnum.nextElement();
				if ((link.getPath().compareTo(path) == 0)
						&& (link.getDestinationId() == obj.getId())) {
					return false;
				}
			}
			linksEnum = child.getLinksByDestination();
			while (linksEnum.hasMoreElements()) {
				link = (ILinkEntry) linksEnum.nextElement();
				if ((link.getPath().compareTo(path) == 0)
						&& (link.getSourceId() == obj.getId())) {
					return false;
				}
			}
			flag = this.isLastInst(child, path, obj);
		}
		return flag;
	}

	// -------------------------getFirstLink-----------------------------------------
	/**
	 * The function returns the first link of wanted types according to typeArr,
	 * with the given path and the given proc as a destination. The search is
	 * from down to top.
	 * 
	 * @param IProcessEntry
	 *            topProc - the "hiest" process, can not go over it (the process
	 *            that the user choosed from the list)
	 * @param IProcessEntry
	 *            proc
	 * @param String
	 *            path
	 * @param int[]
	 *            typeArr - array of different link types
	 * @return IObjectEntry - the source of the link, when the object is with a
	 *         meaning of an "Actor" or a "Use Case"
	 */
	private IObjectEntry getFirstLink(IProcessEntry topProc,
			IProcessEntry proc, String path, int[] typeArr) {
		IProcessInstance procInst;
		Enumeration locenum;
		IProcessEntry father;
		IEntry abstractEntry;
		IThingInstance thingInst;
		IObjectEntry obj;

		obj = this.getMyLink(proc, path, typeArr);// finds an object
													// connencted to
		// the proc
		if (obj != null) {
			return obj;
		}

		if (proc.getId() != topProc.getId()) {// if it is not the hiest proc
			// in diagram
			locenum = proc.getInstances();
			procInst = (IProcessInstance) locenum.nextElement();
			thingInst = procInst.getParentIThingInstance();// process parent
			while ((locenum.hasMoreElements()) && (thingInst == null)) {// finding
				// parentThing
				procInst = (IProcessInstance) locenum.nextElement();
				thingInst = procInst.getParentIThingInstance();
			}
			if (thingInst == null) {
				abstractEntry = topProc;
			} else {
				abstractEntry = thingInst.getIEntry();// up in level opd
			}
			if (abstractEntry instanceof IProcessEntry) { // can be that
				// father of proc is
				// obj and reverse
				father = (IProcessEntry) abstractEntry; // we should check
				// influance on links
				// meaning
				return this.getFirstLink(topProc, father, path, typeArr);
			}
		}
		return null;
	}

	// ------------------------getMyLink-----------------------------------------------------
	/**
	 * Searches for an object connected to the given process(process is
	 * destination and object is a source) by a link with type from typeArr and
	 * with the given path. The found object have to be an invironmental meaning
	 * an "Actor" or informatical and systematic meaning a "Class".
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param String
	 *            path
	 * @param int[]
	 *            typeArr - array of different link types
	 * @return IObjectEntry - object connected to the given process with link of
	 *         a type found in typeArr and with given path, if found; null
	 *         otherwise.
	 */
	private IObjectEntry getMyLink(IProcessEntry proc, String path,
			int[] typeArr) {
		Enumeration locenum;
		ILinkEntry link;
		IObjectEntry obj = null;

		for (int i = 0; i < typeArr.length; i++) {
			locenum = proc.getLinksByDestination();
			while (locenum.hasMoreElements()) {
				link = (ILinkEntry) locenum.nextElement();
				if ((link.getLinkType() == typeArr[i])
						&& (link.getPath().compareTo(path) == 0)) {
					IEntry entry = this.MyStructure.getIEntry(link
							.getSourceId());
					if (entry instanceof IObjectEntry) {
						obj = (IObjectEntry) entry;
					} else if (entry instanceof IStateEntry) {
						obj = ((IStateEntry) entry).getParentIObjectEntry();
					}
					if ((this.isActor(obj)) || (this.isClass(obj))) {
						return obj;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Finds srcObject for OPD level of the proc, it will be the source of all
	 * transitions in the diagram.
	 * 
	 * @param ProcessEntry
	 *            headProc - the top proc, from the selected by the user.
	 * @param IProcessEntry
	 *            proc
	 * @param String
	 *            path
	 */
	private IObjectEntry findSourceForProc(IProcessEntry headProc,
			IProcessEntry proc, String path) {
		int[] arr1 = new int[4];
		int[] arr2 = new int[2];
		IObjectEntry obj = null;

		arr1[0] = OpcatConstants.AGENT_LINK;
		arr1[1] = OpcatConstants.INSTRUMENT_EVENT_LINK;
		arr1[2] = OpcatConstants.EXCEPTION_LINK;
		arr1[3] = OpcatConstants.CONSUMPTION_EVENT_LINK;

		arr2[0] = OpcatConstants.INSTRUMENT_LINK;
		arr2[1] = OpcatConstants.EFFECT_LINK;

		obj = this.getFirstLink(headProc, proc, path, arr1);
		if (obj != null) {
			return obj;
		}

		obj = this.getFirstLink(headProc, proc, path, arr2);
		if (obj != null) {
			return obj;
		}
		return null;
	}

	/**
	 * Function creats vector of sequenceEntries which will be used for prints
	 * 
	 * @param ProcessEntry
	 *            headProc
	 * @param IProcessEntry
	 *            proc
	 * @param String
	 *            path
	 * @param IObjectEntry
	 *            fatherSrc
	 * @param Vector
	 *            seqEntryVec
	 */
	private void createSeqEntryVec(IProcessEntry headProc, IProcessEntry proc,
			String path, IObjectEntry fatherSrc, Vector seqEntryVec) {
		Vector objVec = new Vector(4, 2);
		Vector childVec = new Vector(4, 2);
		Enumeration locenum;
		IObjectEntry obj;
		IObjectEntry src;

		this.getDestObjsLinkedDirect(proc, path, objVec); // all direct
															// objects to
		// the proc that can be
		// destination
		src = this.findSourceForProc(headProc, proc, path); // finds a source
															// for
		// the proc
		if ((src == null) && (proc.getId() != headProc.getId())) {
			src = fatherSrc;
		}
		if (src != null) {
			fatherSrc = src;
			locenum = objVec.elements();// all objects that can be destinations
			while (locenum.hasMoreElements()) {// going throw all "patential"
				// destinations
				obj = (IObjectEntry) locenum.nextElement();
				if (this.isLastInst(proc, path, obj)) { // if retured
					// false-->found;true->not
					// found
					SequenceEntry entry = new SequenceEntry(headProc, proc,
							path, src, obj);
					seqEntryVec.addElement(entry);
				}
			}
		}
		this.sortByOrder(proc, childVec);

		locenum = childVec.elements();
		while (locenum.hasMoreElements()) {
			this.createSeqEntryVec(headProc, (IProcessEntry) locenum
					.nextElement(), path, fatherSrc, seqEntryVec);
		}
		return;
	}

	/**
	 * Function prints ClassifierRoles xml part for objects of each diagram (by
	 * headProc and path)
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param String
	 *            path
	 * @param Vector
	 *            vec - contains all seqEntries relavent to current diagram.
	 * @param Vector
	 *            ClassifierRolePrint - for outside printing of Classifaer Role
	 *            part in xml file.
	 */
	private void createProcRoles(IProcessEntry proc, String path, Vector vec,
			Vector ClassifierRolePrint) throws IOException {
		try {
			Enumeration objEnum, locenum;
			IObjectEntry obj;
			SequenceEntry seqEntry;
			String msg, temp;
			Vector objVec = new Vector(4, 2); // objVec = all obj connected to
			// this proc(from all levels)
			Vector msg1Vec = new Vector(2, 2); // all msg1 vec for proc
			Vector msg2Vec = new Vector(2, 2); // all msg2 vec for proc

			this.getLinkedObjects(proc, objVec, path); // objVec = all obj
			// connected to this
			// proc(from all levels)
			objEnum = objVec.elements();
			while (objEnum.hasMoreElements()) {
				obj = (IObjectEntry) objEnum.nextElement();
				if (vec != null) {
					locenum = vec.elements(); // part of seqEntryVec relevant
					// to this diagram
					while (locenum.hasMoreElements()) { // for source entries
						seqEntry = (SequenceEntry) locenum.nextElement();
						if (seqEntry.getSrcObj().getId() == obj.getId()) {
							msg = "" + proc.getId() + ".3" + path + "."
									+ seqEntry.getSrcObj().getId() + "."
									+ seqEntry.getDestObj().getId() + "";
							msg2Vec.addElement(msg);
						}
						if (seqEntry.getDestObj().getId() == obj.getId()) {
							msg = "" + proc.getId() + ".3" + path + "."
									+ seqEntry.getSrcObj().getId() + "."
									+ seqEntry.getDestObj().getId() + "";
							msg1Vec.addElement(msg);
						}
					}
				}
				ClassifierRolePrint.addElement("" + proc.getId() + ".3" + path
						+ "." + obj.getId() + "");
				temp = "<UML:ClassifierRole xmi.id=\"G."
						+ proc.getId()
						+ ".3"
						+ path
						+ "."
						+ obj.getId()
						+ "\" name=\"\" visibility=\"public\" isSpecification=\"false\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" base=\"S."
						+ obj.getId() + "\" ";
				this.file.write(temp.getBytes());
				if (!msg2Vec.isEmpty()) {
					temp = "message2=\"";
					this.file.write(temp.getBytes());
					locenum = msg2Vec.elements();
					while (locenum.hasMoreElements()) {
						msg = (String) locenum.nextElement();
						temp = "G." + msg + " ";
						this.file.write(temp.getBytes());
					}
					temp = "\"";
					this.file.write(temp.getBytes());
				}
				if (!msg1Vec.isEmpty()) {
					temp = " message1=\"";
					this.file.write(temp.getBytes());
					locenum = msg1Vec.elements();
					while (locenum.hasMoreElements()) {
						msg = (String) locenum.nextElement();
						temp = "G." + msg + " ";
						this.file.write(temp.getBytes());
					}
					temp = "\"";
					this.file.write(temp.getBytes());
				}
				temp = " >";
				this.file.write(temp.getBytes());

				temp = "<UML:ClassifierRole.multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity.range>";
				this.file.write(temp.getBytes());
				temp = "<UML:MultiplicityRange lower=\"1\" upper=\"1\" />";
				this.file.write(temp.getBytes());
				temp = "</UML:Multiplicity.range>";
				this.file.write(temp.getBytes());
				temp = "</UML:Multiplicity>";
				this.file.write(temp.getBytes());
				temp = "</UML:ClassifierRole.multiplicity>";
				this.file.write(temp.getBytes());
				temp = "</UML:ClassifierRole>";
				this.file.write(temp.getBytes());

				msg1Vec.removeAllElements();
				msg2Vec.removeAllElements();
			}

		} catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch
	}

	/**
	 * Function prints all ClassifierRoles xml part for all needed to be
	 * generated diagrams.
	 * 
	 * @param Vector
	 *            seqEntryVec - contains all seqEntries.
	 * @param Vector
	 *            notSeqEntryVec - all objects needs to be a Classifaer Role and
	 *            are not in ClassifierRolePrint Vector
	 * @param Vector
	 *            ClassifierRolePrint - for outside printing of Classifaer Role
	 *            part in xml file
	 */
	private void createRoles(Vector seqEntryVec, Vector notSeqEntryVec,
			Vector ClassifierRolePrint) throws IOException {
		try {
			Enumeration locenum;
			SequenceEntry seqEntry = null;
			IProcessEntry headProc;
			String procPath;
			Vector tempseqEntryVec = new Vector(2, 2);

			int flag = 0;
			locenum = seqEntryVec.elements();
			if (!seqEntryVec.isEmpty()) {
				seqEntry = (SequenceEntry) locenum.nextElement(); // first out
				flag = 1;
				while ((locenum.hasMoreElements()) || (flag == 1)) {
					flag = 0;
					headProc = seqEntry.getHeadProc();
					procPath = seqEntry.getPath();
					tempseqEntryVec.addElement(seqEntry);
					while ((locenum.hasMoreElements()) && (flag == 0)) {
						seqEntry = (SequenceEntry) locenum.nextElement();
						if ((seqEntry.getHeadProc().getId() == headProc.getId())
								&& ((seqEntry.getPath()).compareTo(procPath) == 0)) {
							tempseqEntryVec.addElement(seqEntry);
						} else {
							flag = 1;
						}
					}
					ClassifierRolePrint.addElement("" + headProc.getId() + ".3"
							+ procPath + ""); // diagramId
					ClassifierRolePrint.addElement("" + headProc.getName() + ""
							+ procPath + ""); // diagram name
					this.createProcRoles(headProc, procPath, tempseqEntryVec,
							ClassifierRolePrint);
					ClassifierRolePrint.addElement("-1");
					tempseqEntryVec.removeAllElements();
				}
			}
			locenum = notSeqEntryVec.elements();
			while (locenum.hasMoreElements()) {
				headProc = (IProcessEntry) locenum.nextElement();
				procPath = (String) locenum.nextElement();
				ClassifierRolePrint.addElement("" + headProc.getId() + ".3"
						+ procPath + ""); // diagramId
				ClassifierRolePrint.addElement("" + headProc.getName() + ""
						+ procPath + ""); // diagram name
				this.createProcRoles(headProc, procPath, null,
						ClassifierRolePrint);
				ClassifierRolePrint.addElement("-1");
			}

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch

	}// end of func

	/**
	 * Prints AssociationRole xml part.
	 * 
	 * @param Vector
	 *            seqEntryVec - contains the information to be printed.
	 */
	private void createAssocRole(Vector seqEntryVec) throws IOException {
		try {
			Enumeration locenum;
			String temp, str = "", str1 = "";
			SequenceEntry seqEntry;

			locenum = seqEntryVec.elements();
			while (locenum.hasMoreElements()) {
				seqEntry = (SequenceEntry) locenum.nextElement();
				str = seqEntry.getHeadProc().getId() + ".3"
						+ seqEntry.getPath() + "."
						+ seqEntry.getSrcObj().getId() + "."
						+ seqEntry.getDestObj().getId() + ".3";
				temp = "<UML:AssociationRole xmi.id=\"G."
						+ str
						+ "\" name=\"\" visibility=\"public\" isSpecification=\"false\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\">";
				this.file.write(temp.getBytes());
				temp = "<UML:AssociationRole.multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity.range>";
				this.file.write(temp.getBytes());
				temp = "<UML:MultiplicityRange lower=\"1\" upper=\"1\" />";
				this.file.write(temp.getBytes());
				temp = "</UML:Multiplicity.range>";
				this.file.write(temp.getBytes());
				temp = "</UML:Multiplicity>";
				this.file.write(temp.getBytes());
				temp = "</UML:AssociationRole.multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Association.connection>";
				this.file.write(temp.getBytes());
				str1 = "" + seqEntry.getHeadProc().getId() + ".3"
						+ seqEntry.getPath() + "."
						+ seqEntry.getDestObj().getId() + "";
				temp = "<UML:AssociationEndRole xmi.id=\"XX."
						+ str
						+ ".1\" name=\"\" visibility=\"public\" isSpecification=\"false\" isNavigable=\"false\" ordering=\"unordered\" aggregation=\"none\" targetScope=\"instance\" changeability=\"changeable\" type=\"G."
						+ str1 + "\">";
				this.file.write(temp.getBytes());
				temp = "<UML:AssociationEnd.multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity />";
				this.file.write(temp.getBytes());
				temp = "</UML:AssociationEnd.multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:AssociationEndRole.collaborationMultiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity /> ";
				this.file.write(temp.getBytes());
				temp = "</UML:AssociationEndRole.collaborationMultiplicity>";
				this.file.write(temp.getBytes());
				temp = "</UML:AssociationEndRole>";
				this.file.write(temp.getBytes());
				str1 = seqEntry.getHeadProc().getId() + ".3"
						+ seqEntry.getPath() + "."
						+ seqEntry.getSrcObj().getId();
				temp = "<UML:AssociationEndRole xmi.id=\"XX."
						+ str
						+ ".2\" name=\"\" visibility=\"public\" isSpecification=\"false\" isNavigable=\"false\" ordering=\"unordered\" aggregation=\"none\" targetScope=\"instance\" changeability=\"changeable\" type=\"G."
						+ str1 + "\">";
				this.file.write(temp.getBytes());
				temp = "<UML:AssociationEnd.multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity />";
				this.file.write(temp.getBytes());
				temp = "</UML:AssociationEnd.multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:AssociationEndRole.collaborationMultiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity /> ";
				this.file.write(temp.getBytes());
				temp = "</UML:AssociationEndRole.collaborationMultiplicity>";
				this.file.write(temp.getBytes());
				temp = "</UML:AssociationEndRole>";
				this.file.write(temp.getBytes());
				temp = "</UML:Association.connection>";
				this.file.write(temp.getBytes());
				temp = "</UML:AssociationRole>";
				this.file.write(temp.getBytes());
			}
		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch

	}// end of func

	/**
	 * Prints the logical view xml part for sequence diagram.
	 * 
	 * @param Vector
	 *            seqEntryVec - contains all seqEntries created.
	 */
	private void createLogView1(Vector seqEntryVec) throws IOException {
		try {
			int flag;
			boolean first1 = true, first2 = false, last, exSeq2;
			Enumeration locenum;
			String temp, str, msgName;
			String sender, receiver, msg3, pred = "";
			SequenceEntry seqEntry = null, seqEntry2 = null;

			locenum = seqEntryVec.elements();
			flag = 0;
			if (!seqEntryVec.isEmpty()) {
				seqEntry = (SequenceEntry) locenum.nextElement(); // first
				// enetry in
				// vec
				flag = 1;
				first1 = true; // first in sequense in the current logical view
			}

			while ((locenum.hasMoreElements()) || (flag == 1)) { // going
				// through
				// entries
				// in one
				// diagram
				flag = 0;

				str = "" + seqEntry.getHeadProc().getId() + ".3"
						+ seqEntry.getPath() + ""; // logical view id
				if (first1) { // first in diagram-->new diagram-->new logical
					// view
					temp = "<UML:Interaction xmi.id=\"G."
							+ str
							+ "\" name=\"{Logical View}"
							+ seqEntry.getHeadProc().getName()
							+ "-"
							+ seqEntry.getPath()
							+ "\" visibility=\"public\" isSpecification=\"false\">";
					this.file.write(temp.getBytes());
					temp = "<UML:Interaction.message>";
					this.file.write(temp.getBytes());
				}

				str = "" + seqEntry.getHeadProc().getId() + ".3"
						+ seqEntry.getPath() + "."
						+ seqEntry.getSrcObj().getId() + "."
						+ seqEntry.getDestObj().getId() + "";
				sender = "" + seqEntry.getHeadProc().getId() + ".3"
						+ seqEntry.getPath() + "."
						+ seqEntry.getSrcObj().getId() + "";
				receiver = "" + seqEntry.getHeadProc().getId() + ".3"
						+ seqEntry.getPath() + "."
						+ seqEntry.getDestObj().getId() + "";
				msgName = "" + seqEntry.getMsgProc().getName() + "";

				if (locenum.hasMoreElements()) {
					seqEntry2 = (SequenceEntry) locenum.nextElement(); // after
					// seqEntry
					exSeq2 = true;
				} else {
					exSeq2 = false;
				}

				if (!exSeq2) {
					last = true;
					if (first1) {
						temp = "<UML:Message xmi.id=\"G."
								+ str
								+ "\" name=\""
								+ msgName
								+ "\" visibility=\"public\" isSpecification=\"false\" sender=\"G."
								+ sender + "\" receiver=\"G." + receiver
								+ "\" communicationConnection=\"G." + str
								+ ".3\" action=\"XX." + str + "\" />";
						this.file.write(temp.getBytes());
					} else {
						temp = "<UML:Message xmi.id=\"G."
								+ str
								+ "\" name=\""
								+ msgName
								+ "\" visibility=\"public\" isSpecification=\"false\" sender=\"G."
								+ sender + "\" receiver=\"G." + receiver
								+ "\" predecessor=\"G." + pred
								+ "\" communicationConnection=\"G." + str
								+ ".3\" action=\"XX." + str + "\" />";
						this.file.write(temp.getBytes());
					}
					temp = "</UML:Interaction.message>";
					this.file.write(temp.getBytes());
					temp = "</UML:Interaction>";
					this.file.write(temp.getBytes());
				} else {
					if ((seqEntry.getHeadProc().getId() == seqEntry2
							.getHeadProc().getId())
							&& (seqEntry.getPath().compareTo(
									seqEntry2.getPath()) == 0)) {
						// seqEntry1 and SeqEntry2 are in the same
						// diagram-->seqEnrty is not last
						last = false;
						msg3 = "" + seqEntry2.getHeadProc().getId() + ".3"
								+ seqEntry2.getPath() + "."
								+ seqEntry2.getSrcObj().getId() + "."
								+ seqEntry2.getDestObj().getId() + "";
						if (first1) { // if seqEntry is the first in this
							// diagram
							temp = "<UML:Message xmi.id=\"G."
									+ str
									+ "\" name=\""
									+ msgName
									+ "\" visibility=\"public\" isSpecification=\"false\" sender=\"G."
									+ sender + "\" receiver=\"G." + receiver
									+ "\" message3=\"G." + msg3
									+ "\" communicationConnection=\"G." + str
									+ ".3\" action=\"XX." + str + "\" />";
							this.file.write(temp.getBytes());
							first2 = false;// next msg is not the first one
						}
						if (!first1) {// seqEntry is not first-->has a pred
							temp = "<UML:Message xmi.id=\"G."
									+ str
									+ "\" name=\""
									+ msgName
									+ "\" visibility=\"public\" isSpecification=\"false\" sender=\"G."
									+ sender + "\" receiver=\"G." + receiver
									+ "\" message3=\"G." + msg3
									+ "\" predecessor=\"G." + pred
									+ "\" communicationConnection=\"G." + str
									+ ".3\" action=\"XX." + str + "\" />";
							this.file.write(temp.getBytes());
						}
					} else {// not in the same diagram-->seq1 is last
						last = true;
						first2 = true;// seqEntry2 is first in next diagram
						if (!first1) {// not first but last
							temp = "<UML:Message xmi.id=\"G."
									+ str
									+ "\" name=\""
									+ msgName
									+ "\" visibility=\"public\" isSpecification=\"false\" sender=\"G."
									+ sender + "\" receiver=\"G." + receiver
									+ "\" predecessor=\"G." + pred
									+ "\" communicationConnection=\"G." + str
									+ ".3\" action=\"XX." + str + "\" />";
							this.file.write(temp.getBytes());
						} else { // first and last
							temp = "<UML:Message xmi.id=\"G."
									+ str
									+ "\" name=\""
									+ msgName
									+ "\" visibility=\"public\" isSpecification=\"false\" sender=\"G."
									+ sender + "\" receiver=\"G." + receiver
									+ "\" communicationConnection=\"G." + str
									+ ".3\" action=\"XX." + str + "\" />";
							this.file.write(temp.getBytes());
						}
					}
					if (last) {
						temp = "</UML:Interaction.message>";
						this.file.write(temp.getBytes());
						temp = "</UML:Interaction>";
						this.file.write(temp.getBytes());
					}

					pred = str;
					first1 = first2;
					seqEntry = seqEntry2;
				}

				if ((!locenum.hasMoreElements()) && (exSeq2)) {
					flag = 1;
				}
			}

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch

	}// end of func

	// --------------------------------------------------------------------------------------------

	/**
	 * Prints a logical view xml part.
	 * 
	 * @param Vector
	 *            notSeqEntryVec - all clasifiers roles that has no transitions
	 *            incoming and no transitions outgoing.
	 */
	private void createLogView2(Vector notSeqEntryVec) throws IOException {
		try {
			Enumeration locenum;
			String temp, str, procPath;
			IProcessEntry headProc;

			locenum = notSeqEntryVec.elements();
			while (locenum.hasMoreElements()) {
				headProc = (IProcessEntry) locenum.nextElement();
				procPath = (String) locenum.nextElement();
				str = "" + headProc.getId() + ".3" + procPath + ""; // logical
				// view id
				temp = "<UML:Interaction xmi.id=\"G." + str
						+ "\" name=\"{Logical View}" + headProc.getName() + "-"
						+ procPath
						+ "\" visibility=\"public\" isSpecification=\"false\">";
				this.file.write(temp.getBytes());
				temp = "<UML:Interaction.message>";
				this.file.write(temp.getBytes());
				temp = "</UML:Interaction.message>";
				this.file.write(temp.getBytes());
				temp = "</UML:Interaction>";
				this.file.write(temp.getBytes());
			}
		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch

	}// end of func

	/**
	 * Prints the UninterpretedAction xml part.
	 * 
	 * @param Vector
	 *            seqEntryVec - contains all seqEntries created.
	 */
	private void createUninterpretedAction(Vector seqEntryVec)
			throws IOException {
		try {
			Enumeration locenum;
			String temp, str, name;
			SequenceEntry seqEntry;

			locenum = seqEntryVec.elements();
			while (locenum.hasMoreElements()) {
				seqEntry = (SequenceEntry) locenum.nextElement();
				str = "" + seqEntry.getHeadProc().getId() + ".3"
						+ seqEntry.getPath() + "."
						+ seqEntry.getSrcObj().getId() + "."
						+ seqEntry.getDestObj().getId() + "";
				name = "" + seqEntry.getMsgProc().getName() + "";
				temp = "<UML:UninterpretedAction xmi.id=\"XX."
						+ str
						+ "\" name=\""
						+ name
						+ "\" visibility=\"public\" isSpecification=\"false\" isAsynchronous=\"false\">";
				this.file.write(temp.getBytes());
				temp = "<UML:Action.recurrence>";
				this.file.write(temp.getBytes());
				temp = "<UML:IterationExpression language=\"\" body=\"\" /> ";
				this.file.write(temp.getBytes());
				temp = "</UML:Action.recurrence>";
				this.file.write(temp.getBytes());
				temp = "<UML:Action.target>";
				this.file.write(temp.getBytes());
				temp = "<UML:ObjectSetExpression language=\"\" body=\"\" /> ";
				this.file.write(temp.getBytes());
				temp = "</UML:Action.target>";
				this.file.write(temp.getBytes());
				temp = "<UML:Action.script>";
				this.file.write(temp.getBytes());
				temp = "<UML:ActionExpression language=\"\" body=\"\" /> ";
				this.file.write(temp.getBytes());
				temp = "</UML:Action.script>";
				this.file.write(temp.getBytes());
				temp = "</UML:UninterpretedAction>";
				this.file.write(temp.getBytes());
			}

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch

	}// end of func

	/**
	 * Creates a AssociationPrint vector from the given seqEntryVec vector.
	 * Aranges the associations acording to diagrams.
	 * 
	 * @param Vector
	 *            seqEntryVec - contains all seqEntries found previosly.
	 * @param Vector
	 *            AssociationPrint - created vector, used in outside printing.
	 */
	private void createAssociationPrintVec(Vector seqEntryVec,
			Vector AssociationPrint) {
		Enumeration locenum;
		SequenceEntry seqEntry1 = null, seqEntry2;

		locenum = seqEntryVec.elements();
		if (locenum.hasMoreElements()) {
			seqEntry1 = (SequenceEntry) locenum.nextElement();
			AssociationPrint.addElement("" + seqEntry1.getHeadProc().getId()
					+ ".3" + seqEntry1.getPath() + "");// diagramId
			AssociationPrint.addElement("" + seqEntry1.getHeadProc().getId()
					+ ".3" + seqEntry1.getPath() + "."
					+ seqEntry1.getSrcObj().getId() + "."
					+ seqEntry1.getDestObj().getId() + "");// assId
		}
		while (locenum.hasMoreElements()) {
			seqEntry2 = (SequenceEntry) locenum.nextElement();
			if ((seqEntry1.getHeadProc().getId() == seqEntry2.getHeadProc()
					.getId())
					&& (seqEntry1.getPath().compareTo(seqEntry2.getPath()) == 0)) {
				AssociationPrint.addElement(""
						+ seqEntry2.getHeadProc().getId() + ".3"
						+ seqEntry2.getPath() + "."
						+ seqEntry2.getSrcObj().getId() + "."
						+ seqEntry2.getDestObj().getId() + "");// assId
			} else {
				AssociationPrint.addElement("-1");
				AssociationPrint.addElement(""
						+ seqEntry2.getHeadProc().getId() + ".3"
						+ seqEntry2.getPath() + "");
				AssociationPrint.addElement(""
						+ seqEntry2.getHeadProc().getId() + ".3"
						+ seqEntry2.getPath() + "."
						+ seqEntry2.getSrcObj().getId() + "."
						+ seqEntry2.getDestObj().getId() + "");// assId
			}
			seqEntry1 = seqEntry2;
		}
		AssociationPrint.addElement("-1");

	}

	// ----------------------------SequenceDiagramCreate-------------------------------------------
	/**
	 * The main function called to create the sequence diagram part in xml.
	 * 
	 * @param System
	 *            sys
	 * @param FileOutputStream
	 *            file1
	 * @param Vector
	 *            vecSq - contains the process that the user choosen
	 * @param Vector
	 *            seqEntryVec - will contain all seqEntries
	 * @param Vector
	 *            ClassifierRolePrint - will contains all classifier roles for
	 *            outside printing
	 * @param Vector
	 *            AssociationPrint - will contains all association roles for
	 *            outside printing
	 */
	public void SequenceDiagramCreate(ISystem sys, XmiWriter file1,
			Vector vecSq, Vector seqEntryVec, Vector ClassifierRolePrint,
			Vector AssociationPrint) throws IOException {

		String temp, path;
		Enumeration locenum, enum1;
		IProcessEntry proc = null;
		Vector pathVec = new Vector(2, 2);
		Vector notSeqEntryVec = new Vector(4, 2);

		this.file = file1.file;

		this.MyStructure = sys.getISystemStructure();
		temp = "<!--  ==================== SeqDExampl::Collaboration    [Collaboration] ==================== --> ";
		this.file.write(temp.getBytes());
		temp = "<UML:Collaboration xmi.id=\"S.0000.3\" name=\"Logical View-Collaboration\" visibility=\"public\" isSpecification=\"false\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\">";
		this.file.write(temp.getBytes());
		temp = "<UML:Namespace.ownedElement>";
		this.file.write(temp.getBytes());

		locenum = vecSq.elements(); // locenum=list of all processes that
		// the user have chosen to create a
		// siquense diagram for
		while (locenum.hasMoreElements()) {
			proc = (IProcessEntry) locenum.nextElement(); // a specific
			// process
			this.getPathsVector(proc, pathVec); // a vector which contains all
			// the paths of the spesific
			// process
			enum1 = pathVec.elements(); // a enumeration which contains all
			// the paths of the spesific process
			if (pathVec.isEmpty()) { // no paths
				this.createSeqEntryVec(proc, proc, "", null, seqEntryVec);
				if (seqEntryVec.isEmpty()) {
					notSeqEntryVec.addElement(proc);
					notSeqEntryVec.addElement("");
				}
			} else { // if there are paths
				while (enum1.hasMoreElements()) {
					path = (String) enum1.nextElement(); // specific path
					this.createSeqEntryVec(proc, proc, path, null,/* tempObjVec, */
					seqEntryVec);
					if (seqEntryVec.isEmpty()) {
						notSeqEntryVec.addElement(proc);
						notSeqEntryVec.addElement(path);
					}
				}
			}
			pathVec.removeAllElements(); // clear the vector
		}
		this.createRoles(seqEntryVec, notSeqEntryVec, ClassifierRolePrint); // createRoles
		// function
		// called
		this.createAssocRole(seqEntryVec);
		this.createAssociationPrintVec(seqEntryVec, AssociationPrint);

		temp = "</UML:Namespace.ownedElement>";
		this.file.write(temp.getBytes());
		temp = "<UML:Collaboration.interaction>";
		this.file.write(temp.getBytes());
		if (!notSeqEntryVec.isEmpty()) {
			this.createLogView2(notSeqEntryVec);
		}
		this.createLogView1(seqEntryVec);

		temp = "</UML:Collaboration.interaction>";
		this.file.write(temp.getBytes());
		temp = "</UML:Collaboration>";
		this.file.write(temp.getBytes());

		this.createUninterpretedAction(seqEntryVec);

	}// end of func
}
