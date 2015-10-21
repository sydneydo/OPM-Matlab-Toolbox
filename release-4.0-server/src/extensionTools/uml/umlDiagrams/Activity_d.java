package extensionTools.uml.umlDiagrams;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Vector;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IInstance;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IOpd;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IProcessInstance;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.IStateEntry;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;

/**
 * creats the activity xml part in xml file.
 */
public class Activity_d {

	public Activity_d() {
	}

	FileOutputStream file;

	ISystemStructure MyStructure;

	// ----------------------------------GET_ALL_INZOOMED_PROCESS-----------------------------------------------
	/**
	 * Finds all process that not in the last level. (inzoomed) All process
	 * found will appear in the list, for the user to choose from.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param Vector
	 *            ProcessVec - contains all found procs.
	 */
	public void GetAll_InZoomedProcess(ISystemStructure MyStructure,
			Vector ProcessVec) {
		IEntry abstractEntry, abstractEntry1;
		IProcessEntry proc;
		IProcessInstance procInst;
		Enumeration e, e1, childrenList;
		int flag = 0;

		e = MyStructure.getElements(); // all elements in the system
		while (e.hasMoreElements()) {
			abstractEntry = (IEntry) e.nextElement(); // element
			flag = 0;
			if (abstractEntry instanceof IProcessEntry) {// if element is a
															// process
				proc = (IProcessEntry) abstractEntry; // found a process
				e1 = proc.getInstances(); // all instances of a given process
				while ((e1.hasMoreElements()) && (flag == 0)) {
					procInst = (IProcessInstance) e1.nextElement();
					childrenList = procInst.getChildThings(); // inzoomed
																// tings of a
																// given process
					while ((childrenList.hasMoreElements()) && (flag == 0)) {
						abstractEntry1 = ((IInstance) childrenList
								.nextElement()).getIEntry();
						if (abstractEntry1 instanceof IProcessEntry) {
							ProcessVec.addElement(proc); // adding to the
															// vector
							flag = 1;
						}
					}
				}
			}
		}// end of while
	}

	// ---------------------------------------------F I N D - R O O T - O P
	// D-----------------------------------------------
	/**
	 * Function finds and returns the root Opd of the system
	 * 
	 * @param ISystemStructure
	 *            myStr
	 * @return IOpd - the root IOpd of the system.
	 */
	public IOpd findRootOpd(ISystemStructure myStr) {
		Enumeration e;
		IOpd opd, root = null;

		e = myStr.getOpds();
		while (e.hasMoreElements()) {
			opd = (IOpd) e.nextElement();
			if (opd.getParentIOpd() == null) {
				root = opd; // the root opd found
				break;
			}
		}
		return root;
	}

	// ---------------------------------------------ProcessInRootIOpd------------------------------------------------
	/**
	 * Creates an ordered vector of lists containing procs.
	 * 
	 * @param IOpd
	 *            rootIOpd - id of a root OPD
	 * @param Vector
	 *            orderProcVec -
	 */
	private void procsInRootOpd(IOpd rootIOpd, Vector orderProcVec) {
		Enumeration list;
		IInstance inst;
		IProcessInstance procInst;
		Vector sortedProcVec = new Vector(4, 2);
		Vector childVec = new Vector(4, 2);

		list = this.MyStructure.getInstancesInOpd(rootIOpd.getOpdId()); // all
																		// IInstances
																		// in
																		// the
																		// root
																		// Opd;
		while (list.hasMoreElements()) {
			inst = (IInstance) list.nextElement();
			if (inst instanceof IProcessInstance) { // if it is a process
													// instance
				procInst = (IProcessInstance) inst;
				if ((!this.hasIncomIvoc((IProcessEntry) procInst.getIEntry()))
						&& (!this.hasIncomAggr((IProcessEntry) procInst
								.getIEntry()))) {
					childVec.addElement(procInst);
				}
			}
		}
		this.SortVectorByY(childVec, sortedProcVec);
		this.createLists(sortedProcVec, orderProcVec);

	}

	// ***************************SortChildProcs**********************************************************

	/**
	 * Checks if the given process has incoming invocation link.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @return true - if there is incoming invocation link to the given proc ;
	 *         false - otherwise.
	 */
	private boolean hasIncomIvoc(IProcessEntry proc) {
		Enumeration enumLoc;
		ILinkEntry link;

		enumLoc = proc.getLinksByDestination();
		while (enumLoc.hasMoreElements()) {
			link = (ILinkEntry) enumLoc.nextElement();
			if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the given process has incoming aggregation relation.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @return true - if there is incoming aggregation relation to the given
	 *         proc ; false - otherwise.
	 */
	private boolean hasIncomAggr(IProcessEntry proc) {
		Enumeration enumLoc;
		IRelationEntry rel;

		enumLoc = proc.getRelationByDestination();
		while (enumLoc.hasMoreElements()) {
			rel = (IRelationEntry) enumLoc.nextElement();
			if (rel.getRelationType() == OpcatConstants.AGGREGATION_RELATION) {
				return true;
			}
		}
		return false;
	}

	// -------------------------------------------orderChildProcs---------------------------------------------------------
	/**
	 * Function gets a process and returns an order process vector (by
	 * coordinata y) of lists. If the process are paralel in orderedProcVector
	 * they appear in separate LinkedLists.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            orderProcVec
	 */
	private void orderChildProcs(IProcessEntry proc, Vector orderProcVec) {
		Enumeration enumLoc, childList;
		IInstance inst;
		IProcessInstance procInst;
		Vector childVec = new Vector(4, 2);
		Vector sortedProcVec = new Vector(4, 2);
		IProcessEntry child;

		enumLoc = proc.getInstances();
		while (enumLoc.hasMoreElements()) {
			procInst = (IProcessInstance) enumLoc.nextElement();
			childList = procInst.getChildThings();
			while (childList.hasMoreElements()) {
				inst = (IInstance) childList.nextElement();
				if (inst instanceof IProcessInstance) {
					child = (IProcessEntry) inst.getIEntry();
					if ((!this.hasIncomIvoc(child))
							&& (!this.hasIncomAggr(child))) {
						childVec.addElement(inst);
					}
				}
			}
		}
		this.SortVectorByY(childVec, sortedProcVec);
		this.createLists(sortedProcVec, orderProcVec);
	}

	/**
	 * Sorts a vector from highest process to the lowest one.
	 * 
	 * @param Vector
	 *            childVec - the given unsorted vector.
	 * @param Vector
	 *            sortedProcVec - the created sorted vector.
	 */
	private void SortVectorByY(Vector childVec, Vector sortedProcVec) {
		IProcessInstance proc;
		while (!childVec.isEmpty()) {
			proc = this.findMin(childVec);
			childVec.removeElement(proc);
			sortedProcVec.addElement(proc);
		}
	}

	/**
	 * Finds the process with maximal Y value (location) in the vector
	 * 
	 * @param Vector
	 *            vec - process to choose the minimum from
	 * @return IProcessInstance of the minimum Y value.
	 */
	private IProcessInstance findMin(Vector vec) {
		Enumeration enumLoc;
		IProcessInstance procInstMin, procInst;

		enumLoc = vec.elements();
		procInstMin = (IProcessInstance) enumLoc.nextElement();
		while (enumLoc.hasMoreElements()) {
			procInst = (IProcessInstance) enumLoc.nextElement();
			if (procInstMin.getY() > procInst.getY()) {
				procInstMin = procInst;
			}
		}
		return procInstMin;
	}

	/**
	 * Function creates lists of paralel procs and stores them in orderProcVec.
	 * Each list contains paralel procs.
	 * 
	 * @param sortedProcVec-
	 *            sorted vector of processInstances.
	 * @oaram Vector orderProcVec - contains the ordered lists.
	 */
	private void createLists(Vector sortedProcVec, Vector orderProcVec) {
		LinkedList list = new LinkedList();
		Enumeration enumLoc;
		IProcessInstance procInst1 = null, procInst2 = null;

		if (sortedProcVec.isEmpty()) {
			return;
		}

		enumLoc = sortedProcVec.elements();// proc Enumeration

		if (!sortedProcVec.isEmpty()) { // if nor empty --> get first procInst
			procInst1 = (IProcessInstance) enumLoc.nextElement();
		}

		if (!enumLoc.hasMoreElements()) {// if there are no more elements in
											// enum-->no more procs
			list.addLast(procInst1.getIEntry());// adding the last proc to the
												// last list
			orderProcVec.addElement(list);
		}

		while (enumLoc.hasMoreElements()) {// if there are more procs in sorted
											// vec
			list.addLast(procInst1.getIEntry());// adding the proc to the list
			procInst2 = (IProcessInstance) enumLoc.nextElement();// next proc
			if (this.isParalel(procInst1, procInst2)) {// if proc1 and proc 2
														// are paralel-->in the
														// same list
				if (!enumLoc.hasMoreElements()) {
					list.addLast(procInst2.getIEntry());
					orderProcVec.addElement(list);
				} else {
					procInst1 = procInst2;
				}
			} else {// not paralel
				orderProcVec.addElement(list);// close the list
				list = new LinkedList();// create new one
				if (!enumLoc.hasMoreElements()) {// if there are no more
													// elemnts in sorted vec
					list.addLast(procInst2.getIEntry());
					orderProcVec.addElement(list);
				} else {
					procInst1 = procInst2;
				}
			}

		}// end of while

	}

	// ---------------------------------isParalel--------------------------------------------------------------------------
	/**
	 * Checks if the two given procs are paralel.
	 * 
	 * @param IProcessInstance
	 *            inst1
	 * @param IProcessInstance
	 *            inst2
	 * @return true - if the two given procs are paralel ; false - otherwise.
	 */
	private boolean isParalel(IProcessInstance inst1, IProcessInstance inst2) {

		if (((inst1.getY() >= inst2.getY()) && (inst1.getY() < (inst2.getY() + inst2
				.getHeight())))
				|| ((inst2.getY() >= inst1.getY()) && (inst2.getY() < (inst1
						.getY() + inst1.getHeight())))) {
			return true;
		}
		return false;
	}

	/**
	 * Class structure for transitions
	 */
	public class Transition {
		String id;

		String source;

		String destination;

		String trigger;

		public Transition() {
			// empty stub
		}

		public Transition(String id1, String source1, String destination1,
				String trigger1) {
			this.id = id1;
			this.source = source1;
			this.destination = destination1;
			this.trigger = trigger1;
		}

		public String getTransitionId() {
			return this.id;
		}

		public String getTransitionSource() {
			return this.source;
		}

		public String getTransitionDestination() {
			return this.destination;
		}

		public String getTransitionTrigger() {
			return this.trigger;
		}

	}

	/**
	 * Class structure for InOutStructure - used in activity diagram.
	 */
	public class InOutStructure {

		String id;

		Vector inVec = new Vector(2, 2);

		Vector outVec = new Vector(2, 2);

		public InOutStructure() {
		}

		public InOutStructure(String id1) {
			this.id = id1;
		}

		public void setId(String id1) {
			this.id = id1;
		}

		public void addInTrans(String trans) {
			this.inVec.addElement(trans);
		}

		public void addOutTrans(String trans) {
			this.outVec.addElement(trans);
		}

		public String getId() {
			return this.id;
		}

		public Enumeration getInVec() {
			return this.inVec.elements();
		}

		public Enumeration getOutVec() {
			return this.outVec.elements();
		}

		public void cleanStructure() {
			this.inVec.removeAllElements();
			this.outVec.removeAllElements();
		}
	}

	/**
	 * Class structure for outside printings.
	 */
	public class PrintStructure {

		String diagramId;

		String elementId;

		public PrintStructure() {
		}

		public PrintStructure(String dId, String eId) {
			this.diagramId = dId;
			this.elementId = eId;
		}

		public void setDiagramId(String dId) {
			this.diagramId = dId;
		}

		public void setElementId(String eId) {
			this.elementId = eId;
		}

		public String getDiagramId() {
			return this.diagramId;
		}

		public String getElementId() {
			return this.elementId;
		}

	}

	// ************************************ActivityDiagramCreate************************************************

	/**
	 * Checks if the given proc is InZoomed.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @return true - in case the given process has InZoom; false - otherwise.
	 */
	private boolean isInzoomed(IProcessEntry proc) {
		Enumeration locenum;
		IProcessInstance procInst;

		locenum = proc.getInstances();
		while (locenum.hasMoreElements()) {
			procInst = (IProcessInstance) locenum.nextElement();
			if (procInst.getChildThings().hasMoreElements()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the given proc is Unfolded.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @return true - in case the given process has outgoing aggregation
	 *         relation - unfolding; false - otherwise.
	 */
	private boolean isUnfolded(IProcessEntry proc) {
		Enumeration locenum;
		IRelationEntry rel;

		locenum = proc.getRelationBySource();
		while (locenum.hasMoreElements()) {
			rel = (IRelationEntry) locenum.nextElement();
			if (rel.getRelationType() == OpcatConstants.AGGREGATION_RELATION) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds all outgoing relations from the given proc that are from type
	 * aggregation relation.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            vec - contains all found procs.
	 */
	private void getUnfoldedProcs(IProcessEntry proc, Vector vec) {
		Enumeration locenum;
		IRelationEntry rel;

		locenum = proc.getRelationBySource();
		while (locenum.hasMoreElements()) {
			rel = (IRelationEntry) locenum.nextElement();
			if (rel.getRelationType() == OpcatConstants.AGGREGATION_RELATION) {
				vec.addElement(rel);
			}
		}
	}

	/**
	 * Creates a vector of invoced process from the given proc.
	 * 
	 * @param String
	 *            headProcId - Id of the diagram (process that the user choose
	 *            from the list)
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            invocedProcVec - will contain all invoce procs from the given
	 *            proc.
	 * @param Vector
	 *            transitionsVec - for inside printing of transitions.
	 * @param Vector
	 *            TransitionPrint - for outside printing of transitions.
	 */
	private void createInvoceVec(String headProcId, IProcessEntry proc,
			Vector invocedProcVec, Vector transitionsVec, Vector TransitionPrint) {
		Enumeration locenum;
		ILinkEntry link;
		IProcessEntry procInvoc;
		Vector condVec = new Vector(2, 2);
		Vector exVec = new Vector(2, 2);
		Vector exProcVec = new Vector(2, 2);

		locenum = proc.getLinksBySource();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
				procInvoc = (IProcessEntry) this.MyStructure.getIEntry(link
						.getDestinationId());
				if (headProcId.compareTo("" + procInvoc.getId() + ".5") != 0) {
					if (!this.branchIsNeeded(procInvoc, condVec, exVec,
							exProcVec)) {// if branch is not needed
						Transition trans = new Transition("" + link.getId()
								+ ".5", "" + proc.getId() + ".5", ""
								+ link.getDestinationId() + ".5", null);
						transitionsVec.addElement(trans);
						PrintStructure transPrintStructure = new PrintStructure(
								headProcId, "" + link.getId() + ".5");
						TransitionPrint.addElement(transPrintStructure);
					} else {
						Transition trans = new Transition("" + link.getId()
								+ ".5", "" + proc.getId() + ".5", ""
								+ headProcId + "." + link.getDestinationId()
								+ ".5.branch", null);
						transitionsVec.addElement(trans);
						PrintStructure transPrintStructure = new PrintStructure(
								headProcId, "" + link.getId() + ".5");
						TransitionPrint.addElement(transPrintStructure);
					}
					invocedProcVec.addElement(procInvoc); // destination
															// process
					this.createInvoceVec(headProcId, procInvoc, invocedProcVec,
							transitionsVec, TransitionPrint);
				}
			}
		}

	}

	/**
	 * Function finds all exception and condtion links incoming to the given
	 * proc.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            condLinkVec
	 * @param Vector
	 *            exptLinkVec
	 * @param Vector
	 *            exptLinkProcVec
	 */
	private void findConditonExeptionLink(IProcessEntry proc,
			Vector condLinkVec, Vector exptLinkVec, Vector exptLinkProcVec) {
		Enumeration locenum;
		ILinkEntry link;
		IEntry absEntry;

		locenum = proc.getLinksByDestination();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			if (link.getLinkType() == OpcatConstants.CONDITION_LINK) {
				absEntry = this.MyStructure.getIEntry(link.getSourceId()); // source
																			// of
																			// the
																			// link;
				if (absEntry instanceof IStateEntry) {
					condLinkVec.addElement(link);
				}
			}
			if (link.getLinkType() == OpcatConstants.EXCEPTION_LINK) {
				absEntry = this.MyStructure.getIEntry(link.getSourceId()); // source
																			// of
																			// the
																			// link;
				if (absEntry instanceof IStateEntry) {
					exptLinkVec.addElement(link);
				}
				if (absEntry instanceof IProcessEntry) {
					exptLinkProcVec.addElement(link);
				}
			}
		}
	}

	/**
	 * Creates branch if it needed-->before the given proc
	 * 
	 * @param String
	 *            prevId - Id of a previos element.
	 * @param String
	 *            headProcId - Id of a diagram.
	 * @param IProcessEntry
	 *            proc
	 * @param InOutStructure
	 *            procStructure - structure of given proc.
	 * @param Vector
	 *            transitionVec - inside printing of transitions.
	 * @param Vector
	 *            TransitionPrint - outside printing of transitions.
	 * @param Vector
	 *            BranchPrint - outside printing of branches.
	 * @return InOutStructure of the branch.
	 */
	private InOutStructure createBranch(String prevId, String headProcId,
			IProcessEntry proc, InOutStructure procStructure,
			Vector transitionVec, Vector TransitionPrint, Vector BranchPrint) {

		InOutStructure branchStructure = new InOutStructure("" + headProcId
				+ "." + proc.getId() + ".5.branch");
		branchStructure.addInTrans(prevId + "." + branchStructure.getId());
		branchStructure.addOutTrans("" + branchStructure.getId() + "."
				+ proc.getId() + ".5");
		procStructure.addInTrans("" + branchStructure.getId() + "."
				+ proc.getId() + ".5");
		Transition trans = new Transition("" + branchStructure.getId() + "."
				+ proc.getId() + ".5", branchStructure.getId(), ""
				+ proc.getId() + ".5", "" + proc.getId() + ".5.triggerIn");
		transitionVec.addElement(trans);
		PrintStructure printTrans = new PrintStructure(headProcId, ""
				+ branchStructure.getId() + "." + proc.getId() + ".5");
		TransitionPrint.addElement(printTrans);
		PrintStructure printBranchStructure = new PrintStructure(headProcId,
				branchStructure.getId());
		BranchPrint.addElement(printBranchStructure);
		return branchStructure;

	}

	/**
	 * Checks if needed a branch before the given proc.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            condLinkVec - will contain condition links from states.
	 * @param Vector
	 *            exptLinkVec - will contain exception links from states.
	 * @param Vector
	 *            exptLinkProcVec - will contain exception links from processes.
	 * @return true - in case needed a branh before the given proc; false -
	 *         otherwise.
	 */
	private boolean branchIsNeeded(IProcessEntry proc, Vector condLinkVec,
			Vector exptLinkVec, Vector exptLinkProcVec) {

		this.findConditonExeptionLink(proc, condLinkVec, exptLinkVec,
				exptLinkProcVec);// creates vecs which contains all links of
									// type exeption/conditon and they from
									// state

		if ((condLinkVec.isEmpty()) && (exptLinkVec.isEmpty())
				&& (exptLinkProcVec.isEmpty())) {
			return false;// branch not needed
		}
		return true;
	}

	/**
	 * Function adds the signal event of a transition to a signal event vector.
	 * 
	 * @param IProcessEntry
	 *            proc
	 * @param Vector
	 *            signalEventVec - signal event vector, contains all signal
	 *            events.
	 * @param Vector
	 *            condLinkVec - contains all conditon links from states to the
	 *            given proc.
	 * @param Vector
	 *            exptLinkVec - contains all exception links from states to the
	 *            given proc.
	 * @param Vector
	 *            exptLinkProcVec - contains all exception links from processes
	 *            to the given proc.
	 */
	private void addSignalEvents(IProcessEntry proc, Vector signalEventVec,
			Vector condLinkVec, Vector exptLinkVec, Vector exptLinkProcVec) {
		Enumeration locenum;
		ILinkEntry link;
		IObjectEntry obj;
		IStateEntry state;
		IProcessEntry exptProc;
		String outToProc = "", outToEnd = "";
		String temp;

		if (!condLinkVec.isEmpty()) {// has condition links

			locenum = condLinkVec.elements();
			while (locenum.hasMoreElements()) {
				link = (ILinkEntry) locenum.nextElement();// condition link
				state = ((IStateEntry) (this.MyStructure.getIEntry(link
						.getSourceId())));
				obj = state.getParentIObjectEntry();
				temp = "" + obj.getName() + " in state " + state.getName() + "";
				outToProc = "" + outToProc + "" + temp + "";
				temp = " " + obj.getName() + " not in state " + state.getName()
						+ "";
				outToEnd = "" + outToEnd + "" + temp + "";
				if (locenum.hasMoreElements()) {
					outToProc = "" + outToProc + " || ";
					outToEnd = "" + outToEnd + " || ";
				}
			}
		}

		if (!exptLinkVec.isEmpty()) {// has exeption links to states
			if (!condLinkVec.isEmpty()) {
				outToProc = "" + outToProc + " || ";
				outToEnd = "" + outToEnd + " || ";
			}
			locenum = exptLinkVec.elements();
			while (locenum.hasMoreElements()) {
				link = (ILinkEntry) locenum.nextElement();// exeption link
				state = ((IStateEntry) (this.MyStructure.getIEntry(link
						.getSourceId())));
				obj = state.getParentIObjectEntry();
				temp = "" + obj.getName() + "-" + state.getName() + " on time";
				outToProc = "" + outToProc + "" + temp + "";
				temp = "" + obj.getName() + "-" + state.getName()
						+ " not on time";
				outToEnd = "" + outToEnd + "" + temp + "";
				if (locenum.hasMoreElements()) {
					outToProc = "" + outToProc + " || ";
					outToEnd = "" + outToEnd + " || ";
				}
			}
		}

		if (!exptLinkProcVec.isEmpty()) {// has exeption link to a process
			if ((!condLinkVec.isEmpty()) || (!exptLinkVec.isEmpty())) {
				outToProc = "" + outToProc + " || ";
				outToEnd = "" + outToEnd + " || ";
			}
			locenum = exptLinkProcVec.elements();
			while (locenum.hasMoreElements()) {
				link = (ILinkEntry) locenum.nextElement();// exeption link
				exptProc = ((IProcessEntry) (this.MyStructure.getIEntry(link
						.getSourceId())));
				temp = "" + exptProc.getName() + " on time";
				outToProc = "" + outToProc + "" + temp + "";
				temp = "" + exptProc.getName() + " not on time";
				outToEnd = "" + outToEnd + "" + temp + "";
				if (locenum.hasMoreElements()) {
					outToProc = "" + outToProc + " || ";
					outToEnd = "" + outToEnd + " || ";
				}
			}
		}

		signalEventVec.addElement("" + proc.getId() + ".5.triggerIn");
		signalEventVec.addElement(outToProc);// event enter the proc
		signalEventVec.addElement("" + proc.getId() + ".5.triggerOut");
		signalEventVec.addElement(outToEnd);// event enter the proc
	}

	/**
	 * Creates InOutStructure for a given proc, adds all it`s incoming and
	 * outgoing(to invoced procs) transitions (besides the one which goes to
	 * join).
	 * 
	 * @param String
	 *            headProcId - Id of the diagram (process which the user choose
	 *            from the list).
	 * @param IProcessEntry
	 *            proc
	 * @param int
	 *            forkIndex - current index of fork.
	 * @param LinkedList
	 *            nextList - next linked list.
	 * @param InOutStructure
	 *            forkStructure - current fork structure.
	 * @param Vector
	 *            transitionVec - inside pronting of tansitions.
	 * @param String
	 *            lastId - Id of previos element.
	 * @param Vector
	 *            TransitionPrint - outside printing of transitions.
	 * @param Vector
	 *            BranchPrint - outside printing of branches.
	 * @param Vector
	 *            signalEventVec - the text on transitions
	 * @param Vector
	 *            branchVec - branch vector.
	 * @return InOutStructure - created proc strucutre.
	 */
	private InOutStructure createInOutStructureProc(String headProcId,
			IProcessEntry proc, int forkIndex, LinkedList nextList,
			InOutStructure forkStructure, Vector transitionVec, String lastId,
			Vector TransitionPrint, Vector BranchPrint, Vector signalEventVec,
			Vector branchVec) {
		Enumeration locenum;
		ILinkEntry link;
		String dest;
		Vector condLinkVec = new Vector(4, 2);
		Vector exptLinkVec = new Vector(4, 2);
		Vector exptLinkProcVec = new Vector(4, 2);
		InOutStructure branchStruct = null;
		boolean branchNeeded = false;

		InOutStructure procStructure = new InOutStructure("" + proc.getId()
				+ ".5");

		// check if branch needed before this proc
		if (this
				.branchIsNeeded(proc, condLinkVec, exptLinkVec, exptLinkProcVec)) {
			branchStruct = this.createBranch(lastId, headProcId, proc,
					procStructure, transitionVec, TransitionPrint, BranchPrint);
			if (forkStructure == null) {
				branchVec.addElement(branchStruct);
				branchVec.addElement("" + proc.getId() + ".5.triggerOut");
			}
			this.addSignalEvents(proc, signalEventVec, condLinkVec,
					exptLinkVec, exptLinkProcVec);
			branchNeeded = true;
		} else if (lastId != "-1") {// no branch needed
			procStructure.addInTrans("" + lastId + "." + proc.getId() + ".5");
		}

		if (forkStructure != null) {// lastId was fork-->need to add this
									// transition to fork and to transitionsVec
			if (branchNeeded) {// transition between fork and branch
				dest = branchStruct.getId();
				branchStruct.addInTrans(forkStructure.getId() + "."
						+ branchStruct.getId());
				branchVec.addElement(branchStruct);
				branchVec.addElement("" + proc.getId() + ".5.triggerOut");
			} else {// transition between fork and proc
				dest = "" + proc.getId() + ".5";
			}
			forkStructure.addOutTrans("" + lastId + "." + dest);
			Transition trans = new Transition("" + lastId + "." + dest, lastId,
					dest, null);
			transitionVec.addElement(trans);
			PrintStructure transPrintStructure = new PrintStructure(headProcId,
					"" + lastId + "." + dest);
			TransitionPrint.addElement(transPrintStructure);
		}
		// all invocation links that goes out prom the current proc
		locenum = proc.getLinksBySource();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
				procStructure.addOutTrans("" + link.getId() + ".5");
			}
		}
		return procStructure;
	}

	/**
	 * Creates branch if it needed-->before the given invocProc
	 * 
	 * @param String
	 *            headProcId - Id of a diagram.
	 * @param IProcessEntry
	 *            invocProc
	 * @param InOutStructure
	 *            invocStructure - structure of given invocProc.
	 * @param Vector
	 *            transitionVec - inside printing of transitions.
	 * @param Vector
	 *            TransitionPrint - outside printing of transitions.
	 * @param Vector
	 *            BranchPrint - outside printing of branches.
	 * @param Vector
	 *            branchVec -vector of branches.
	 * @return InOutStructure of the branch.
	 */
	private InOutStructure createBranchInvoc(String headProcId,
			IProcessEntry invocProc, InOutStructure invocStructure,
			Vector transitionVec, Vector TransitionPrint, Vector BranchPrint,
			Vector branchVec) {
		Enumeration locenum;
		ILinkEntry link;
		IProcessEntry srcProc;

		InOutStructure branchStructure = new InOutStructure("" + headProcId
				+ "." + invocProc.getId() + ".5.branch");
		locenum = invocProc.getLinksByDestination();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
				srcProc = (IProcessEntry) this.MyStructure.getIEntry(link
						.getSourceId());
				branchStructure.addInTrans("" + link.getId() + ".5");
				Transition trans = new Transition("" + link.getId() + ".5", ""
						+ srcProc.getId() + ".5", branchStructure.getId(), null);
				transitionVec.addElement(trans);
				PrintStructure transPrintStructure = new PrintStructure(
						headProcId, "" + link.getId() + ".5");
				TransitionPrint.addElement(transPrintStructure);
			}
		}
		branchStructure.addOutTrans(branchStructure.getId() + "."
				+ invocProc.getId() + ".5");
		invocStructure.addInTrans(branchStructure.getId() + "."
				+ invocProc.getId() + ".5");
		Transition trans = new Transition(branchStructure.getId() + "."
				+ invocProc.getId() + ".5", branchStructure.getId(), invocProc
				.getId()
				+ ".5", invocProc.getId() + ".5.triggerIn");
		transitionVec.addElement(trans);
		PrintStructure transPrintStructure = new PrintStructure(headProcId,
				branchStructure.getId() + "." + invocProc.getId() + ".5");
		TransitionPrint.addElement(transPrintStructure);
		PrintStructure branchPrintStructure = new PrintStructure(headProcId,
				branchStructure.getId());
		BranchPrint.addElement(branchPrintStructure);
		branchVec.addElement(branchStructure);
		branchVec.addElement(invocProc.getId() + ".5.triggerOut");
		return branchStructure;
	}

	/**
	 * Creates InOutStructure for a given invocProc, adds all it`s incoming and
	 * outgoing(to invoced procs) transitions.
	 * 
	 * @param String
	 *            headProcId - Id of the diagram (process which the user choose
	 *            from the list).
	 * @param IProcessEntry
	 *            invocProc.
	 * @param Vector
	 *            branchVec - branch vector.
	 * @param Vector
	 *            transitionVec - inside pronting of tansitions.
	 * @param Vector
	 *            TransitionPrint - outside printing of transitions.
	 * @param Vector
	 *            BranchPrint - outside printing of branches.
	 * @param Vector
	 *            signalEventVec - the text on transitions
	 * @return InOutStructure - created procInvoc strucutre.
	 */
	private InOutStructure createInvocProcStructure(String headProcId,
			IProcessEntry invocProc, Vector branchVec, Vector transitionVec,
			Vector TransitionPrint, Vector BranchPrint, Vector signalEventVec) {
		Enumeration locenum;
		ILinkEntry link;
		InOutStructure invocStructure = new InOutStructure(""
				+ invocProc.getId() + ".5");
		Vector condLinkVec = new Vector(4, 2);
		Vector exptLinkVec = new Vector(4, 2);
		Vector exptLinkProcVec = new Vector(4, 2);

		if (this.branchIsNeeded(invocProc, condLinkVec, exptLinkVec,
				exptLinkProcVec)) {
			this.createBranchInvoc(headProcId, invocProc, invocStructure,
					transitionVec, TransitionPrint, BranchPrint, branchVec);
			this.addSignalEvents(invocProc, signalEventVec, condLinkVec,
					exptLinkVec, exptLinkProcVec);
		} else {// branch not needed to the invoced proc
			locenum = invocProc.getLinksByDestination();
			while (locenum.hasMoreElements()) {
				link = (ILinkEntry) locenum.nextElement();
				if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
					invocStructure.addInTrans("" + link.getId() + ".5");
				}
			}
		}

		locenum = invocProc.getLinksBySource();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
				invocStructure.addOutTrans("" + link.getId() + ".5");
			}
		}
		return invocStructure;
	}

	/**
	 * Prints "join" part in XML file.
	 * 
	 * @param InOutStructure
	 *            join - strucutre of the join to print.
	 */
	private void printJoin(InOutStructure join) throws IOException {
		try {
			String temp;
			Enumeration locenum;

			temp = "<UML:Pseudostate xmi.id=\"G."
					+ join.getId()
					+ "\" name=\"\" visibility=\"public\" kind=\"join\" outgoing=\"";
			this.file.write(temp.getBytes());
			locenum = join.getOutVec();
			while (locenum.hasMoreElements()) {
				temp = "G." + (String) locenum.nextElement() + " ";
				this.file.write(temp.getBytes());
			}
			temp = "\" incoming=\"";
			this.file.write(temp.getBytes());
			locenum = join.getInVec();
			while (locenum.hasMoreElements()) {
				temp = "G." + (String) locenum.nextElement() + " ";
				this.file.write(temp.getBytes());
			}
			temp = "\" /> ";
			this.file.write(temp.getBytes());

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch
	}

	/**
	 * Prints "fork" part in XML file.
	 * 
	 * @param InOutStructure
	 *            fork - strucutre of the fork to print.
	 */
	private void printFork(InOutStructure fork) throws IOException {
		try {
			String temp;
			Enumeration locenum;

			temp = "<UML:Pseudostate xmi.id=\"G."
					+ fork.getId()
					+ "\" name=\"\" visibility=\"public\" kind=\"fork\" outgoing=\"";
			this.file.write(temp.getBytes());
			locenum = fork.getOutVec();
			while (locenum.hasMoreElements()) {
				temp = "G." + (String) locenum.nextElement() + " ";
				this.file.write(temp.getBytes());
			}
			temp = "\" incoming=\"";
			this.file.write(temp.getBytes());
			locenum = fork.getInVec();
			while (locenum.hasMoreElements()) {
				temp = "G." + (String) locenum.nextElement() + " ";
				this.file.write(temp.getBytes());
			}
			temp = "\" /> ";
			this.file.write(temp.getBytes());

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch
	}

	/**
	 * Prints final state part in XML file.
	 * 
	 * @param InOutStructure
	 *            finalState - strucutre of the final state to print.
	 */
	private void printFinalState(InOutStructure finalState) throws IOException {
		try {
			String temp = "";

			temp = "<UML:FinalState xmi.id=\"G."
					+ finalState.getId()
					+ "\" name=\"\" visibility=\"public\" incoming=\"G."
					+ finalState.getInVec().nextElement() + "\" /> ";
			this.file.write(temp.getBytes());

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch
	}

	// ---------------------------------------------------------------------------------------------------------------------
	/**
	 * Prints initial state part in XML file
	 * 
	 * @param ring
	 *            headProcId - diagrram Id (process which the user chose)
	 * @param LinkedList
	 *            list - next list.
	 * @param int
	 *            forkIndex - the current index of fork.
	 * @param Vector
	 *            transitionsVec - for inside printing of transitions.
	 * @param Vector
	 *            TransitionPrint - for outside printing of transitions.
	 * @param Vector
	 *            InitialPrint - for outside printing of initial states.
	 * @param String
	 *            branchId - branch id in case next element is a branch.
	 */
	private void printInitialState(String headProcId, LinkedList list,
			int forkIndex, Vector transitionsVec, Vector TransitionPrint,
			Vector InitialPrint, String branchId) throws IOException {
		try {
			String temp = "", out = "", dest = "";
			if (branchId != null) {
				out = "" + headProcId + ".initial." + branchId;
				dest = branchId;
			} else {
				if (list.size() == 1) {
					out = "" + headProcId + ".initial."
							+ ((IProcessEntry) list.getFirst()).getId() + ".5";
					dest = "" + ((IProcessEntry) list.getFirst()).getId()
							+ ".5";
				}
				if (list.size() > 1) {
					out = "" + headProcId + ".initial." + headProcId + "."
							+ (forkIndex + 1) + ".fork";
					dest = "" + headProcId + "." + (forkIndex + 1) + ".fork";
				}
			}
			Transition trans = new Transition(out,
					"" + headProcId + ".initial", dest, null);
			transitionsVec.addElement(trans);
			PrintStructure transPrint = new PrintStructure(headProcId, out);
			TransitionPrint.addElement(transPrint);
			PrintStructure initPrint = new PrintStructure(headProcId, ""
					+ headProcId + ".initial");
			InitialPrint.addElement(initPrint);

			temp = "<UML:Pseudostate xmi.id=\"G."
					+ headProcId
					+ ".initial\" name=\"\" visibility=\"public\" kind=\"initial\" outgoing=\"G."
					+ out + "\" />";
			this.file.write(temp.getBytes());

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch
	}

	/**
	 * Prints an xml code part for branch.
	 * 
	 * @param InOutStructure
	 *            branchStructure - stucture of the given branch.
	 */
	private void printBranch(InOutStructure branchStructure) throws IOException {
		try {
			String temp;
			Enumeration locenum;

			temp = "<UML:Pseudostate xmi.id=\"G."
					+ branchStructure.getId()
					+ "\" name=\"\" visibility=\"public\" kind=\"branch\" outgoing=\"";
			this.file.write(temp.getBytes());
			locenum = branchStructure.getOutVec();
			while (locenum.hasMoreElements()) {
				temp = "G." + (String) locenum.nextElement() + " ";
				this.file.write(temp.getBytes());
			}
			temp = "\" incoming=\"";
			this.file.write(temp.getBytes());
			locenum = branchStructure.getInVec();
			while (locenum.hasMoreElements()) {
				temp = "G." + (String) locenum.nextElement() + " ";
				this.file.write(temp.getBytes());
			}
			temp = "\" /> ";
			this.file.write(temp.getBytes());

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch

	}

	// ------------------------copyVec------------------------------------------------------------
	/**
	 * Function copies elements from srcVec to destVec and clears srcVec.
	 * 
	 * @param Vector
	 *            srcVec - source vector (copy from)
	 * @param Vector
	 *            destVec - destination vector (copy to)
	 */
	private void copyVec(Vector srcVec, Vector destVec) {
		Enumeration locenum;

		locenum = srcVec.elements();
		while (locenum.hasMoreElements()) {
			destVec.addElement(locenum.nextElement());
		}
		srcVec.removeAllElements();
	}

	// ---------------------------------------------------------------------------------------------------------------------
	/**
	 * Treats one proc from list with all the process that invoced by it.
	 * 
	 * @param nextList==null
	 *            if there is no nextList, otherwise it is a nextList in procVec
	 *            structure
	 * @param join=null
	 *            if no join and fork needed, otherwise it is ajoin that needed.
	 * @param transitionsVec
	 *            contains all transitions. If more transitions found in the
	 *            function they are added.
	 * @param level=number
	 *            of levels needed to go down (from the given proc).
	 * @param prevId=the
	 *            previos Id that appeared before the given proc, and the proc
	 *            need to be connected to it.
	 * @param headProc=id
	 *            of the process that the given process is inzoomed from.
	 * @return String newPrevId= if new prevId needed.
	 */
	private String procTreatment(IProcessEntry proc, String headProc,
			LinkedList nextList, int level, InOutStructure fork,
			InOutStructure join, String prevId, int joinIndex, int forkIndex,
			Vector transitionsVec, Vector actDiagramsVec, Vector ActivityPrint,
			Vector ForkPrint, Vector JoinPrint, Vector InitialPrint,
			Vector FinalPrint, Vector BranchPrint, Vector TransitionPrint,
			Vector signalEventVec, Vector branchVec) throws IOException {
		try {
			Enumeration invocEnum, locenum;
			IProcessEntry invocProc;
			Vector vec1 = new Vector(2, 2);
			Vector vec2 = new Vector(2, 2);
			Vector invocedProcVec = new Vector(2, 2);
			Vector newBranchVec = new Vector(2, 2);
			InOutStructure oldBranch = null, newBranch = null;
			String triggerId, destId, newTriggerId = null;
			InOutStructure procStructure, invocStructure;
			InOutStructure finalState = new InOutStructure();
			InOutStructure newJoin = new InOutStructure();
			boolean needNewJoin = false, needFinalState = false;
			String newPrevId = "";

			if (nextList == null) {
				if (join == null) {
					finalState.setId(headProc + ".final");
				}
			}

			if (join == null) {// in current list one proc
				procStructure = this.createInOutStructureProc(headProc, proc,
						forkIndex, nextList, fork, transitionsVec, prevId,
						TransitionPrint, BranchPrint, signalEventVec,
						newBranchVec);// structure created for the proc
			} else {
				procStructure = this
						.createInOutStructureProc(headProc, proc, forkIndex,
								nextList, fork, transitionsVec, prevId,
								TransitionPrint, BranchPrint, signalEventVec,
								branchVec);// structure created for the proc
			}
			this.createInvoceVec(headProc, proc, invocedProcVec,
					transitionsVec, TransitionPrint);// creating a vector of
														// all invoced process
														// from proc

			if (invocedProcVec.isEmpty()) {// no invoced process at all
				if (join != null) {// there is a big join already --> conecting
									// the Proc to join
					procStructure.addOutTrans("" + proc.getId() + ".5."
							+ headProc + "." + joinIndex + ".join");
					join.addInTrans("" + proc.getId() + ".5." + headProc + "."
							+ joinIndex + ".join");
					Transition trans = new Transition("" + proc.getId() + ".5."
							+ headProc + "." + joinIndex + ".join", ""
							+ proc.getId() + ".5", "" + headProc + "."
							+ joinIndex + ".join", null);
					transitionsVec.addElement(trans);
					PrintStructure transPrintStructure = new PrintStructure(
							headProc, "" + proc.getId() + ".5." + headProc
									+ "." + joinIndex + ".join");
					TransitionPrint.addElement(transPrintStructure);
				}
				if (join == null) {// one proc in the list-->no join outside
					if (!newBranchVec.isEmpty()) {
						locenum = newBranchVec.elements();
						newBranch = (InOutStructure) locenum.nextElement();// new
																			// branch
						newTriggerId = (String) locenum.nextElement();
					}
					locenum = branchVec.elements();
					while (locenum.hasMoreElements()) {
						oldBranch = (InOutStructure) locenum.nextElement();
						triggerId = (String) locenum.nextElement();
						if (newBranchVec.isEmpty()) {
							destId = "" + proc.getId() + ".5";
							procStructure.addInTrans(oldBranch.getId() + "."
									+ destId);
						} else {
							destId = newBranch.getId();
							newBranch.addInTrans(oldBranch.getId() + "."
									+ destId);
							newBranch.addOutTrans(destId + "." + proc.getId()
									+ ".5");
							procStructure.addInTrans(destId + "."
									+ proc.getId() + ".5");
							Transition trans = new Transition(destId + "."
									+ proc.getId() + ".5", destId, proc.getId()
									+ ".5", newTriggerId);
							transitionsVec.addElement(trans);
							PrintStructure printTrans = new PrintStructure(
									headProc, destId + "." + proc.getId()
											+ ".5");
							TransitionPrint.addElement(printTrans);
						}
						oldBranch.addOutTrans(oldBranch.getId() + "." + destId);
						this.printBranch(oldBranch);
						Transition trans = new Transition(oldBranch.getId()
								+ "." + destId, oldBranch.getId(), destId,
								triggerId);
						transitionsVec.addElement(trans);
						PrintStructure printTrans = new PrintStructure(
								headProc, oldBranch.getId() + "." + destId);
						TransitionPrint.addElement(printTrans);
					}
					branchVec.removeAllElements();
					this.copyVec(newBranchVec, branchVec);
					newBranchVec.removeAllElements();
					if (nextList == null) {// it is the last list-->final state
											// creation
						procStructure.addOutTrans("" + proc.getId() + ".5."
								+ headProc + ".final");
						Transition trans = new Transition("" + proc.getId()
								+ ".5." + headProc + ".final", ""
								+ proc.getId() + ".5",
								"" + headProc + ".final", null);
						transitionsVec.addElement(trans);
						PrintStructure transPrintStructure = new PrintStructure(
								headProc, "" + proc.getId() + ".5." + headProc
										+ ".final");
						TransitionPrint.addElement(transPrintStructure);
						finalState.addInTrans("" + proc.getId() + ".5."
								+ headProc + ".final");
						PrintStructure finalPrintStructure = new PrintStructure(
								headProc, "" + headProc + ".final");
						FinalPrint.addElement(finalPrintStructure);
						locenum = branchVec.elements();
						while (locenum.hasMoreElements()) {
							oldBranch = (InOutStructure) locenum.nextElement();
							triggerId = (String) locenum.nextElement();
							finalState.addInTrans(oldBranch.getId() + "."
									+ headProc + ".final");
							oldBranch.addOutTrans(oldBranch.getId() + "."
									+ headProc + ".final");
							trans = new Transition(oldBranch.getId() + "."
									+ headProc + ".final", oldBranch.getId(),
									"" + headProc + ".final", triggerId);
							transitionsVec.addElement(trans);
							transPrintStructure = new PrintStructure(headProc,
									oldBranch.getId() + "." + headProc
											+ ".final");
							TransitionPrint.addElement(transPrintStructure);
							this.printBranch(oldBranch);
						}
						branchVec.removeAllElements();
						this.printFinalState(finalState);
					} else {// nextList is not null-->proc need to be connected
							// to the next List
						newPrevId = "" + proc.getId() + ".5";
						if (nextList.size() == 1) {// in next list only one
													// process -->connecting
													// directly to it
							IProcessEntry nextproc = (IProcessEntry) nextList
									.getFirst();
							String nextId;
							Vector condVec = new Vector(2, 2);
							Vector exVec = new Vector(2, 2);
							Vector exProcVec = new Vector(2, 2);
							if (this.branchIsNeeded(nextproc, condVec, exVec,
									exProcVec)) {
								nextId = headProc + "." + nextproc.getId()
										+ ".5.branch";
							} else {
								nextId = nextproc.getId() + ".5";
							}
							procStructure.addOutTrans("" + proc.getId() + ".5."
									+ nextId);
							Transition trans = new Transition("" + proc.getId()
									+ ".5." + nextId, "" + proc.getId() + ".5",
									nextId, null);
							transitionsVec.addElement(trans);
							PrintStructure transPrintStructure = new PrintStructure(
									headProc, "" + proc.getId() + ".5."
											+ nextId);
							TransitionPrint.addElement(transPrintStructure);
						}
						if (nextList.size() > 1) {// in next list has to be
													// fork and join
													// -->connecting this proc
													// to the future fork
							if (!branchVec.isEmpty()) {
								needNewJoin = true;
								locenum = branchVec.elements();
								while (locenum.hasMoreElements()) {
									oldBranch = (InOutStructure) locenum
											.nextElement();
									triggerId = (String) locenum.nextElement();
									newJoin.addInTrans(oldBranch.getId() + "."
											+ headProc + "." + (joinIndex + 1)
											+ ".join");
									oldBranch.addOutTrans(oldBranch.getId()
											+ "." + headProc + "."
											+ (joinIndex + 1) + ".join");
									Transition trans = new Transition(oldBranch
											.getId()
											+ "."
											+ headProc
											+ "."
											+ (joinIndex + 1) + ".join",
											oldBranch.getId(), "" + headProc
													+ "." + (joinIndex + 1)
													+ ".join", triggerId);
									transitionsVec.addElement(trans);
									PrintStructure transPrintStructure = new PrintStructure(
											headProc, oldBranch.getId() + "."
													+ headProc + "."
													+ (joinIndex + 1) + ".join");
									TransitionPrint
											.addElement(transPrintStructure);
									this.printBranch(oldBranch);
									newJoin.addInTrans("" + proc.getId()
											+ ".5." + headProc + "."
											+ (joinIndex + 1) + ".join");
									procStructure.addOutTrans("" + proc.getId()
											+ ".5." + headProc + "."
											+ (joinIndex + 1) + ".join");
									trans = new Transition("" + proc.getId()
											+ ".5." + headProc + "."
											+ (joinIndex + 1) + ".join", ""
											+ proc.getId() + ".5", ""
											+ headProc + "." + (joinIndex + 1)
											+ ".join", triggerId);
									transitionsVec.addElement(trans);
									transPrintStructure = new PrintStructure(
											headProc, "" + proc.getId() + ".5."
													+ headProc + "."
													+ (joinIndex + 1) + ".join");
									TransitionPrint
											.addElement(transPrintStructure);
								}
								branchVec.removeAllElements();
							} else {
								procStructure.addOutTrans("" + proc.getId()
										+ ".5." + headProc + "."
										+ (forkIndex + 1) + ".fork");
								Transition trans = new Transition(""
										+ proc.getId() + ".5." + headProc + "."
										+ (forkIndex + 1) + ".fork", ""
										+ proc.getId() + ".5", "" + headProc
										+ "." + (forkIndex + 1) + ".fork", null);
								transitionsVec.addElement(trans);
								PrintStructure transPrintStructure = new PrintStructure(
										headProc, "" + proc.getId() + ".5."
												+ headProc + "."
												+ (forkIndex + 1) + ".fork");
								TransitionPrint.addElement(transPrintStructure);
							}
						}
					}// end of else
				}// end if join==null

			}// end if invocVec is empty
			else {// if there are invoced procs from the given proc-->
					// continue with the branch collection in branch vec
				this.copyVec(newBranchVec, branchVec);
				newBranchVec.removeAllElements();
			}

			// printing
			this.printAct(proc, procStructure, level, transitionsVec,
					actDiagramsVec, ActivityPrint, ForkPrint, JoinPrint,
					InitialPrint, FinalPrint, BranchPrint, TransitionPrint,
					signalEventVec);
			PrintStructure printProcStructure = new PrintStructure(headProc, ""
					+ proc.getId() + "");
			ActivityPrint.addElement(printProcStructure);
			// invocation procs part
			invocEnum = invocedProcVec.elements();// list of envoced procs
			while (invocEnum.hasMoreElements()) {
				invocProc = (IProcessEntry) invocEnum.nextElement();
				invocStructure = this.createInvocProcStructure(headProc,
						invocProc, branchVec, transitionsVec, TransitionPrint,
						BranchPrint, signalEventVec);// creating structure
														// for invocProc
				if (!invocStructure.getOutVec().hasMoreElements()) {// no
																	// invocations
																	// outgoing
																	// from the
																	// invocProc
					if (join != null) {// go into given join (created outside)
						Transition trans = new Transition(""
								+ invocProc.getId() + ".5." + headProc + "."
								+ joinIndex + ".join", "" + invocProc.getId()
								+ ".5", "" + headProc + "." + joinIndex
								+ ".join", null);
						transitionsVec.addElement(trans);
						PrintStructure transPrintStructure = new PrintStructure(
								headProc, "" + invocProc.getId() + ".5."
										+ headProc + "." + joinIndex + ".join");
						TransitionPrint.addElement(transPrintStructure);
						invocStructure.addOutTrans("" + invocProc.getId()
								+ ".5." + headProc + "." + joinIndex + ".join");
						join.addInTrans("" + invocProc.getId() + ".5."
								+ headProc + "." + joinIndex + ".join");
					}
					if (join == null) {// need to check if new join needed
						if (this.joinNeeded(invocedProcVec)) {// need to
																// create new
																// join
							needNewJoin = true;
							invocStructure.addOutTrans("" + invocProc.getId()
									+ ".5." + headProc + "." + (joinIndex + 1)
									+ ".join");
							newJoin.addInTrans("" + invocProc.getId() + ".5."
									+ headProc + "." + (joinIndex + 1)
									+ ".join");
							Transition trans = new Transition(""
									+ invocProc.getId() + ".5." + headProc
									+ "." + (joinIndex + 1) + ".join", ""
									+ invocProc.getId() + ".5", "" + headProc
									+ "." + (joinIndex + 1) + ".join", null);
							transitionsVec.addElement(trans);
							PrintStructure transPrintStructure = new PrintStructure(
									headProc, "" + invocProc.getId() + ".5."
											+ headProc + "." + (joinIndex + 1)
											+ ".join");
							TransitionPrint.addElement(transPrintStructure);
							locenum = branchVec.elements();
							while (locenum.hasMoreElements()) {
								oldBranch = (InOutStructure) locenum
										.nextElement();
								triggerId = (String) locenum.nextElement();
								oldBranch.addOutTrans(oldBranch.getId() + "."
										+ headProc + "." + (joinIndex + 1)
										+ ".join");
								this.printBranch(oldBranch);
								newJoin.addInTrans(oldBranch.getId() + "."
										+ headProc + "." + (joinIndex + 1)
										+ ".join");
								trans = new Transition(oldBranch.getId() + "."
										+ headProc + "." + (joinIndex + 1)
										+ ".join", oldBranch.getId(), ""
										+ headProc + "." + (joinIndex + 1)
										+ ".join", triggerId);
								transitionsVec.addElement(trans);
								transPrintStructure = new PrintStructure(
										headProc, oldBranch.getId() + "."
												+ headProc + "."
												+ (joinIndex + 1) + ".join");
								TransitionPrint.addElement(transPrintStructure);
							}
							branchVec.removeAllElements();
							if (nextList == null) {// there is no next list,
													// finalState needed
								needFinalState = true;// need to add
														// transition from the
														// new join to final
														// state
								// transToFinal=""+headProc+"."+(joinIndex+1)+".join."+headProc+".final";//from
								// join to final state
							}
						} else {// join not needed--->meaning there will be only
								// one invoced proc going in to next list or to
								// final state
							if (nextList == null) {// this is the last
													// proc-->need to be
													// connected to a final
													// state
								invocStructure.addOutTrans(""
										+ invocProc.getId() + ".5." + headProc
										+ ".final");
								Transition trans = new Transition(""
										+ invocProc.getId() + ".5." + headProc
										+ ".final", "" + invocProc.getId()
										+ ".5", "" + headProc + ".final", null);
								transitionsVec.addElement(trans);
								PrintStructure transPrintStructure = new PrintStructure(
										headProc, "" + invocProc.getId()
												+ ".5." + headProc + ".final");
								TransitionPrint.addElement(transPrintStructure);
								finalState.addInTrans("" + invocProc.getId()
										+ ".5." + headProc + ".final");
								PrintStructure finalPrintStructure = new PrintStructure(
										headProc, "" + headProc + ".final");
								FinalPrint.addElement(finalPrintStructure);
								if (!branchVec.isEmpty()) {
									locenum = branchVec.elements();
									while (locenum.hasMoreElements()) {
										oldBranch = (InOutStructure) locenum
												.nextElement();
										triggerId = (String) locenum
												.nextElement();
										oldBranch.addOutTrans(oldBranch.getId()
												+ "." + headProc + ".final");
										finalState.addInTrans(oldBranch.getId()
												+ "." + headProc + ".final");
										trans = new Transition(oldBranch
												.getId()
												+ "." + headProc + ".final",
												oldBranch.getId(), ""
														+ headProc + ".final",
												triggerId);
										transitionsVec.addElement(trans);
										transPrintStructure = new PrintStructure(
												headProc, oldBranch.getId()
														+ "." + headProc
														+ ".final");
										TransitionPrint
												.addElement(transPrintStructure);
									}
									branchVec.removeAllElements();
									this.printBranch(oldBranch);
								}
								this.printFinalState(finalState);
							} else {// next list is not null, newPrevId
								newPrevId = "" + invocProc.getId() + ".5";
								if (nextList.size() == 1) {
									IProcessEntry nextProc = ((IProcessEntry) nextList
											.getFirst());
									if (this.branchIsNeeded(nextProc, vec1,
											vec2, vec1)) {// if for new next
															// proc needed
															// branch
										vec1.removeAllElements();
										vec2.removeAllElements();
									} else {// for new next proc branch is not
											// needed
										invocStructure.addOutTrans(""
												+ invocProc.getId() + ".5."
												+ nextProc.getId() + ".5");
										Transition trans = new Transition(""
												+ invocProc.getId()
												+ ".5."
												+ ((IProcessEntry) nextList
														.getFirst()).getId()
												+ ".5", "" + invocProc.getId()
												+ ".5", ""
												+ ((IProcessEntry) nextList
														.getFirst()).getId()
												+ ".5", null);
										transitionsVec.addElement(trans);
										PrintStructure transPrintStructure = new PrintStructure(
												headProc,
												""
														+ invocProc.getId()
														+ ".5."
														+ ((IProcessEntry) nextList
																.getFirst())
																.getId() + ".5");
										TransitionPrint
												.addElement(transPrintStructure);
										locenum = branchVec.elements();
										while (locenum.hasMoreElements()) {
											oldBranch = (InOutStructure) locenum
													.nextElement();
											triggerId = (String) locenum
													.nextElement();
											oldBranch.addOutTrans(oldBranch
													.getId()
													+ "."
													+ nextProc.getId()
													+ ".5");
											this.printBranch(oldBranch);
											invocStructure.addInTrans(oldBranch
													.getId()
													+ "."
													+ nextProc.getId()
													+ ".5");
											trans = new Transition(oldBranch
													.getId()
													+ "."
													+ nextProc.getId()
													+ ".5", oldBranch.getId(),
													nextProc.getId() + ".5",
													triggerId);
											transitionsVec.addElement(trans);
											transPrintStructure = new PrintStructure(
													headProc, oldBranch.getId()
															+ "."
															+ nextProc.getId()
															+ ".5");
											TransitionPrint
													.addElement(transPrintStructure);
										}
										branchVec.removeAllElements();
									}
								}
								if (nextList.size() > 1) {
									if (!branchVec.isEmpty()) {// new branch
																// needed!
										needNewJoin = true;
										invocStructure.addOutTrans(""
												+ invocProc.getId() + ".5."
												+ headProc + "."
												+ (joinIndex + 1) + ".join");
										newJoin.addInTrans(""
												+ invocProc.getId() + ".5."
												+ headProc + "."
												+ (joinIndex + 1) + ".join");
										Transition trans = new Transition(""
												+ invocProc.getId() + ".5."
												+ headProc + "."
												+ (joinIndex + 1) + ".join", ""
												+ invocProc.getId() + ".5", ""
												+ headProc + "."
												+ (joinIndex + 1) + ".join",
												null);
										transitionsVec.addElement(trans);
										PrintStructure transPrintStructure = new PrintStructure(
												headProc, ""
														+ invocProc.getId()
														+ ".5." + headProc
														+ "." + (joinIndex + 1)
														+ ".join");
										TransitionPrint
												.addElement(transPrintStructure);
										locenum = branchVec.elements();
										while (locenum.hasMoreElements()) {
											oldBranch = (InOutStructure) locenum
													.nextElement();
											triggerId = (String) locenum
													.nextElement();
											oldBranch
													.addOutTrans(oldBranch
															.getId()
															+ "."
															+ headProc
															+ "."
															+ (joinIndex + 1)
															+ ".join");
											this.printBranch(oldBranch);
											newJoin
													.addInTrans(oldBranch
															.getId()
															+ "."
															+ headProc
															+ "."
															+ (joinIndex + 1)
															+ ".join");
											trans = new Transition(
													oldBranch.getId() + "."
															+ headProc + "."
															+ (joinIndex + 1)
															+ ".join",
													oldBranch.getId(), ""
															+ headProc + "."
															+ (joinIndex + 1)
															+ ".join",
													triggerId);
											transitionsVec.addElement(trans);
											transPrintStructure = new PrintStructure(
													headProc, oldBranch.getId()
															+ "." + headProc
															+ "."
															+ (joinIndex + 1)
															+ ".join");
											TransitionPrint
													.addElement(transPrintStructure);
										}
										branchVec.removeAllElements();
									} else {
										invocStructure.addOutTrans(""
												+ invocProc.getId() + ".5."
												+ headProc + "."
												+ (forkIndex + 1) + ".fork");
										Transition trans = new Transition(""
												+ invocProc.getId() + ".5."
												+ headProc + "."
												+ (forkIndex + 1) + ".fork", ""
												+ invocProc.getId() + ".5", ""
												+ headProc + "."
												+ (forkIndex + 1) + ".fork",
												null);
										transitionsVec.addElement(trans);
										PrintStructure transPrintStructure = new PrintStructure(
												headProc, ""
														+ invocProc.getId()
														+ ".5." + headProc
														+ "." + (forkIndex + 1)
														+ ".fork");
										TransitionPrint
												.addElement(transPrintStructure);
									}
								}
							}// end else
						}// end else

					}// end if join==null
				}
				this.printAct(invocProc, invocStructure, level, transitionsVec,
						actDiagramsVec, ActivityPrint, ForkPrint, JoinPrint,
						InitialPrint, FinalPrint, BranchPrint, TransitionPrint,
						signalEventVec);
				PrintStructure printProc1Structure = new PrintStructure(
						headProc, "" + invocProc.getId() + "");
				ActivityPrint.addElement(printProc1Structure);

			}// end while invocEnum

			if (needNewJoin) {
				joinIndex++;
				newJoin.setId("" + headProc + "." + joinIndex + ".join");
				prevId = "" + headProc + "." + joinIndex + ".join";
				newPrevId = "" + headProc + "." + joinIndex + ".join";
				if (needFinalState) {
					newJoin.addOutTrans("" + headProc + "." + joinIndex
							+ ".join." + headProc + "final");
					Transition trans = new Transition("" + headProc + "."
							+ joinIndex + ".join." + headProc + ".final", ""
							+ headProc + "." + joinIndex + ".join", ""
							+ headProc + ".final", null);
					transitionsVec.addElement(trans);
					PrintStructure transPrintStructure = new PrintStructure(
							headProc, "" + headProc + "." + joinIndex
									+ ".join." + headProc + ".final");
					TransitionPrint.addElement(transPrintStructure);
					finalState.addInTrans("" + headProc + "." + joinIndex
							+ ".join." + headProc + "final");
					PrintStructure finalPrintStructure = new PrintStructure(
							headProc, "" + headProc + ".final");
					FinalPrint.addElement(finalPrintStructure);
					this.printFinalState(finalState);
				} else {// final state not needed--->nextList<>null
					if (nextList.size() == 1) {
						newJoin.addOutTrans("" + headProc + "." + joinIndex
								+ ".join."
								+ ((IProcessEntry) nextList.getFirst()).getId()
								+ ".5");
						Transition trans = new Transition("" + headProc + "."
								+ joinIndex + ".join."
								+ ((IProcessEntry) nextList.getFirst()).getId()
								+ ".5", "" + headProc + "." + joinIndex
								+ ".join", ""
								+ ((IProcessEntry) nextList.getFirst()).getId()
								+ ".5", null);
						transitionsVec.addElement(trans);
						PrintStructure transPrintStructure = new PrintStructure(
								headProc, ""
										+ headProc
										+ "."
										+ joinIndex
										+ ".join."
										+ ((IProcessEntry) nextList.getFirst())
												.getId() + ".5");
						TransitionPrint.addElement(transPrintStructure);
					}
					if (nextList.size() > 1) {
						newJoin.addOutTrans("" + headProc + "." + joinIndex
								+ ".join." + headProc + "." + (forkIndex + 1)
								+ ".fork");
						Transition trans = new Transition("" + headProc + "."
								+ joinIndex + ".join." + headProc + "."
								+ (forkIndex + 1) + ".fork", "" + headProc
								+ "." + joinIndex + ".join", "" + headProc
								+ "." + (forkIndex + 1) + ".fork", null);
						transitionsVec.addElement(trans);
						PrintStructure transPrintStructure = new PrintStructure(
								headProc, "" + headProc + "." + joinIndex
										+ ".join." + headProc + "."
										+ (forkIndex + 1) + ".fork");
						TransitionPrint.addElement(transPrintStructure);
					}
				}
				this.printJoin(newJoin);
				PrintStructure joinPrintStructure = new PrintStructure(
						headProc, newJoin.getId());
				JoinPrint.addElement(joinPrintStructure);
			}

			return newPrevId;
		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return prevId;
		}// end of catch

	}

	/**
	 * Checks if a new join is needed.
	 * 
	 * @param Vector
	 *            vec - contains processes.
	 * @return true - in case join needed; false - otherwise.
	 */
	private boolean joinNeeded(Vector vec) {
		Enumeration locenum, linksEnum;
		IProcessEntry proc;
		ILinkEntry link;
		int cnt = 0;
		boolean found;

		locenum = vec.elements();
		while (locenum.hasMoreElements()) {
			proc = (IProcessEntry) locenum.nextElement();
			linksEnum = proc.getLinksBySource();
			found = false;
			while ((linksEnum.hasMoreElements()) && (!found)) {
				link = (ILinkEntry) linksEnum.nextElement();
				if (link.getLinkType() == OpcatConstants.INVOCATION_LINK) {
					found = true;
				}
			}
			if (found) {
				cnt++;
			}
			if (cnt > 1) {
				return true;
			}
		}
		return false;
	}

	// -----------------------------------createActForProc------------------------------------------------------
	/**
	 * Prints the "Activites" in XML file, and calculates the needed transition,
	 * branches, initials and final states.
	 * 
	 * @param String
	 *            headProcId - diagram ID.
	 * @param String
	 *            headProcName - diagram name.
	 * @param boolean
	 *            initialNeeded - indicates if needed a initial state.
	 * @param int
	 *            level - current level.
	 * @param Vector
	 *            procVec - contains ordered (by Y) lists of paralel processes
	 *            of the current level.
	 * @param Vector
	 *            transitionsVec - inside printing of transitions.
	 * @param Vector
	 *            actDiagramsVec - diagrams vector.
	 * @param Vector
	 *            ActivityPrint - outside printing of activities.
	 * @param Vector
	 *            ForkPrint - outside printing of forks.
	 * @param Vector
	 *            JoinPrint - outside printing of joins.
	 * @param Vector
	 *            InitialPrint - outside printing of initial states.
	 * @param Vector
	 *            FinalPrint - outside printing of final states.
	 * @param Vector
	 *            BranchPrint - outside printing of branches.
	 * @param Vector
	 *            TransitionPrint - outside printing of transitions.
	 * @param Vector
	 *            signalEventVec - printing of text on transitions.
	 */
	private void createActForProc(String headProcId, String headProcName,
			boolean initialNeeded, int level, Vector procVec,
			Vector transitionsVec, Vector actDiagramsVec, Vector ActivityPrint,
			Vector ForkPrint, Vector JoinPrint, Vector InitialPrint,
			Vector FinalPrint, Vector BranchPrint, Vector TransitionPrint,
			Vector signalEventVec) throws IOException {
		try {
			Enumeration locenum;
			Enumeration branchEnum;
			IProcessEntry proc;
			LinkedList list = null, nextList = null;
			String tempId;
			int listSize;
			Vector branchVec = new Vector(2, 2);// contains all branchs which
												// need to be connected to
												// current list from previos
			Vector condVec = new Vector(2, 2);
			Vector exepVec = new Vector(2, 2);
			Vector exepProcVec = new Vector(2, 2);
			// --------------------------------------------------
			InOutStructure branchStructure = new InOutStructure();
			InOutStructure forkStructure = new InOutStructure();
			InOutStructure joinStructure = new InOutStructure();
			InOutStructure finalStateStructure = new InOutStructure();
			// --------------------------------------------------
			boolean flag = true;
			String prevId = "-1";
			String branchID = null;

			int forkIndex = 0;
			int joinIndex = 0;
			if (level == 0) {
				return;
			}

			locenum = procVec.elements(); // all elements in the procVec
											// structure
			if (!procVec.isEmpty()) { // if there are elemnets in the procVec
										// sturcture-->Diagram Begins!-->initial
										// State
				list = (LinkedList) locenum.nextElement(); // geting
															// out--->first list
				actDiagramsVec.addElement(headProcId);// adding a new diagram
														// for printing outside
				actDiagramsVec.addElement(headProcName);
				if (initialNeeded) {
					if ((list.size() == 1)
							&& (this.branchIsNeeded((IProcessEntry) list
									.getFirst(), condVec, exepVec, exepProcVec))) {
						condVec.removeAllElements();
						exepVec.removeAllElements();
						branchID = headProcId + "."
								+ ((IProcessEntry) list.getFirst()).getId()
								+ ".5.branch";
					}
					prevId = "" + headProcId + ".initial";
					this.printInitialState(headProcId, list, forkIndex,
							transitionsVec, TransitionPrint, InitialPrint,
							branchID);

				}
			} else {
				flag = false; // if the there no elemnts in the procVec
								// structure --> flag=false
			}

			// if the structure is empty we will not enter the "while"
			while ((locenum.hasMoreElements()) || (flag)) { // list out of
															// vector
				if (flag) {
					flag = false;
				}
				if (locenum.hasMoreElements()) {
					nextList = (LinkedList) locenum.nextElement();// next List
					if (!locenum.hasMoreElements()) {
						flag = true;
					}
				} else {
					nextList = null;// no nextList
				}

				listSize = list.size(); // num of procs in list-> if more then
										// one fork and join needed
				if (listSize > 1) {// fork and join needed
					forkIndex++;
					joinIndex++;
					forkStructure.setId("" + headProcId + "." + forkIndex
							+ ".fork");// fork id
					joinStructure.setId("" + headProcId + "." + joinIndex
							+ ".join");// join id
					// branch Vec connected-->from previos list -->if it has one
					// proc end (no join)----------------------------
					branchEnum = branchVec.elements();
					while (branchEnum.hasMoreElements()) {
						branchStructure = (InOutStructure) branchEnum
								.nextElement();
						tempId = (String) branchEnum.nextElement();
						Transition trans = new Transition(branchStructure
								.getId()
								+ "." + forkStructure.getId(), branchStructure
								.getId(), forkStructure.getId(), tempId);
						transitionsVec.addElement(trans);
						PrintStructure printTrans = new PrintStructure(
								headProcId, branchStructure.getId() + "."
										+ forkStructure.getId());
						TransitionPrint.addElement(printTrans);
						branchStructure.addOutTrans(branchStructure.getId()
								+ "." + forkStructure.getId());
						this.printBranch(branchStructure);
						forkStructure.addInTrans(branchStructure.getId() + "."
								+ forkStructure.getId());
					}
					branchVec.removeAllElements();

					if (prevId != "-1") {// there will be a connection from
											// prev to the fork
						forkStructure.addInTrans("" + prevId + "." + headProcId
								+ "." + forkIndex + ".fork");// transition
																// going inside
																// fork
					}

					prevId = "" + headProcId + "." + forkIndex + ".fork";
					while (!list.isEmpty()) {// while there are things in
												// list
						proc = (IProcessEntry) list.getFirst();
						list.removeFirst();
						this.procTreatment(proc, headProcId, nextList, level,
								forkStructure, joinStructure, prevId,
								joinIndex, forkIndex, transitionsVec,
								actDiagramsVec, ActivityPrint, ForkPrint,
								JoinPrint, InitialPrint, FinalPrint,
								BranchPrint, TransitionPrint, signalEventVec,
								branchVec);
					}

					// connected all branch vec to the join!!!
					branchEnum = branchVec.elements();
					while (branchEnum.hasMoreElements()) {
						branchStructure = (InOutStructure) branchEnum
								.nextElement();
						tempId = (String) branchEnum.nextElement();
						joinStructure.addInTrans("" + branchStructure.getId()
								+ "." + headProcId + "." + joinIndex + ".join");
						branchStructure.addOutTrans(""
								+ branchStructure.getId() + "." + headProcId
								+ "." + joinIndex + ".join");
						this.printBranch(branchStructure);
						Transition trans = new Transition(branchStructure
								.getId()
								+ "." + headProcId + "." + joinIndex + ".join",
								branchStructure.getId(), "" + headProcId + "."
										+ joinIndex + ".join", tempId);
						transitionsVec.addElement(trans);
						PrintStructure printTrans = new PrintStructure(
								headProcId, branchStructure.getId() + "."
										+ headProcId + "." + joinIndex
										+ ".join");
						TransitionPrint.addElement(printTrans);
					}
					branchVec.removeAllElements();

					if (nextList != null) {// tere is a next list
						if (nextList.size() == 1) {// if next list has only one
													// process in
													// it-->connecting the join
													// to it, and adding a
													// transition
							joinStructure.addOutTrans(""
									+ headProcId
									+ "."
									+ joinIndex
									+ ".join."
									+ ((IProcessEntry) nextList.getFirst())
											.getId() + ".5");
							Transition trs = new Transition(""
									+ headProcId
									+ "."
									+ joinIndex
									+ ".join."
									+ ((IProcessEntry) nextList.getFirst())
											.getId() + ".5", "" + headProcId
									+ "." + joinIndex + ".join", ""
									+ ((IProcessEntry) nextList.getFirst())
											.getId() + ".5", null);
							transitionsVec.addElement(trs);
							PrintStructure transPrintStructure = new PrintStructure(
									headProcId, ""
											+ headProcId
											+ "."
											+ joinIndex
											+ ".join."
											+ ((IProcessEntry) nextList
													.getFirst()).getId() + ".5");
							TransitionPrint.addElement(transPrintStructure);
						}

						if (nextList.size() > 1) {
							joinStructure.addOutTrans("" + headProcId + "."
									+ joinIndex + ".join." + headProcId + "."
									+ (forkIndex + 1) + ".fork");
							Transition trs = new Transition("" + headProcId
									+ "." + joinIndex + ".join." + headProcId
									+ "." + (forkIndex + 1) + ".fork", ""
									+ headProcId + "." + joinIndex + ".join",
									"" + headProcId + "." + (forkIndex + 1)
											+ ".fork", null);
							transitionsVec.addElement(trs);
							PrintStructure transPrintStructure = new PrintStructure(
									headProcId, "" + headProcId + "."
											+ joinIndex + ".join." + headProcId
											+ "." + (forkIndex + 1) + ".fork");
							TransitionPrint.addElement(transPrintStructure);
						}

						prevId = "" + headProcId + "." + joinIndex + ".join";
					}// end if nextList!=null
					else {// if nextList==null--> there is no nextList -->
							// need to connect the join to a final state
						joinStructure.addOutTrans("" + headProcId + "."
								+ joinIndex + ".join." + headProcId + ".final");
						Transition trs = new Transition("" + headProcId + "."
								+ joinIndex + ".join." + headProcId + ".final",
								"" + headProcId + "." + joinIndex + ".join", ""
										+ headProcId + ".final", null);
						transitionsVec.addElement(trs);
						PrintStructure transPrintStructure = new PrintStructure(
								headProcId, "" + headProcId + "." + joinIndex
										+ ".join." + headProcId + ".final");
						TransitionPrint.addElement(transPrintStructure);
						finalStateStructure.setId("" + headProcId + ".final");
						finalStateStructure.addInTrans("" + headProcId + "."
								+ joinIndex + ".join." + headProcId + ".final");
						PrintStructure finalPrintStructure = new PrintStructure(
								headProcId, "" + headProcId + ".final");
						FinalPrint.addElement(finalPrintStructure);
						this.printFinalState(finalStateStructure);
						finalStateStructure.cleanStructure();
					}

					this.printFork(forkStructure);
					PrintStructure forkPrintStructure = new PrintStructure(
							headProcId, forkStructure.getId());
					ForkPrint.addElement(forkPrintStructure);
					this.printJoin(joinStructure);
					PrintStructure joinPrintStructure = new PrintStructure(
							headProcId, joinStructure.getId());
					JoinPrint.addElement(joinPrintStructure);
					forkStructure.cleanStructure();
					joinStructure.cleanStructure();

				}// end if listSize>1

				if (listSize == 1) {// there is one process in the list
					proc = (IProcessEntry) list.getFirst();
					list.removeFirst();
					prevId = this.procTreatment(proc, headProcId, nextList,
							level, null, null, prevId, joinIndex, forkIndex,
							transitionsVec, actDiagramsVec, ActivityPrint,
							ForkPrint, JoinPrint, InitialPrint, FinalPrint,
							BranchPrint, TransitionPrint, signalEventVec,
							branchVec);
					if (prevId.endsWith("join")) {
						joinIndex++;
						// connected all branch vec to the join!!!
					}
				}// //end of if(size=1)

				list = nextList;

			}// end of while-vector

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch

	}// end of func

	/**
	 * Prints xml part for an activity of given proc.
	 * 
	 * @param IProcessEntry
	 *            proc -
	 * @param InOutStructure
	 *            procStructure - structure for a given proc.
	 * @param int
	 *            level - the current level.
	 * @param Vector
	 *            transitionsVec - inside printing of transitions.
	 * @param Vector
	 *            actDiagramsVec - contains all diagrams Id.
	 * @param Vector
	 *            ActivityPrint - outside printing of activities.
	 * @param Vector
	 *            ForkPrint - outside printing of forks.
	 * @param Vector
	 *            JoinPrint - outside printing of joins.
	 * @param Vector
	 *            InitialPrint - outside printing of initial states.
	 * @param Vector
	 *            FinalPrint - outside printing of final states.
	 * @param Vector
	 *            BranchPrint - outside printing of branches.
	 * @param Vector
	 *            TransitionPrint - outside printing of transitions.
	 * @param Vector
	 *            signalEventVec - the text on the transitions.
	 */
	private void printAct(IProcessEntry proc, InOutStructure procStructure,
			int level, Vector transitionsVec, Vector actDiagramsVec,
			Vector ActivityPrint, Vector ForkPrint, Vector JoinPrint,
			Vector InitialPrint, Vector FinalPrint, Vector BranchPrint,
			Vector TransitionPrint, Vector signalEventVec) throws IOException {
		try {
			Enumeration tranEnum;
			String temp;
			Vector orderProcVec = new Vector(4, 2);
			Vector unfoldProcVec = new Vector(4, 2);
			Vector sortedProcVec = new Vector(4, 2);
			if ((this.isInzoomed(proc) || this.isUnfolded(proc)) && (level > 1)) {
				temp = "<UML:SubactivityState xmi.id=\"G."
						+ proc.getId()
						+ ".5\" name=\""
						+ proc.getName()
						+ "\" visibility=\"public\" isConcurrent=\"false\" isDynamic=\"false\" outgoing=\"";
				this.file.write(temp.getBytes());
				tranEnum = procStructure.getOutVec();
				while (tranEnum.hasMoreElements()) {
					temp = "G." + (String) tranEnum.nextElement() + " ";
					this.file.write(temp.getBytes());
				}
				temp = "\" incoming=\"";
				this.file.write(temp.getBytes());
				tranEnum = procStructure.getInVec();
				while (tranEnum.hasMoreElements()) {
					temp = "G." + (String) tranEnum.nextElement() + " ";
					this.file.write(temp.getBytes());
				}
				temp = "\">";
				this.file.write(temp.getBytes());
				temp = "<UML:SubactivityState.dynamicArguments>";
				this.file.write(temp.getBytes());
				temp = "<UML:ArgListsExpression language=\"\" body=\"\" /> ";
				this.file.write(temp.getBytes());
				temp = "</UML:SubactivityState.dynamicArguments>";
				this.file.write(temp.getBytes());
				temp = "<UML:SubactivityState.dynamicMultiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity.range>";
				this.file.write(temp.getBytes());
				temp = "<UML:MultiplicityRange lower=\"0\" upper=\"0\" /> ";
				this.file.write(temp.getBytes());
				temp = "</UML:Multiplicity.range>";
				this.file.write(temp.getBytes());
				temp = "</UML:Multiplicity>";
				this.file.write(temp.getBytes());
				temp = "</UML:SubactivityState.dynamicMultiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:CompositeState.subvertex>";
				this.file.write(temp.getBytes());

				// inside to the children -both inzoom and both agg
				if (this.isInzoomed(proc)) {
					this.orderChildProcs(proc, orderProcVec);
					this.createActForProc("" + proc.getId() + "", proc
							.getName(), true, (level - 1), orderProcVec,
							transitionsVec, actDiagramsVec, ActivityPrint,
							ForkPrint, JoinPrint, InitialPrint, FinalPrint,
							BranchPrint, TransitionPrint, signalEventVec);
				} else if (this.isUnfolded(proc)) {
					this.getUnfoldedProcs(proc, unfoldProcVec);// needd to be
																// implemented
					this.SortVectorByY(unfoldProcVec, sortedProcVec);
					this.createLists(sortedProcVec, orderProcVec);
					this.createActForProc("" + proc.getId() + "", proc
							.getName(), true, (level - 1), orderProcVec,
							transitionsVec, actDiagramsVec, ActivityPrint,
							ForkPrint, JoinPrint, InitialPrint, FinalPrint,
							BranchPrint, TransitionPrint, signalEventVec);
				}

				temp = "</UML:CompositeState.subvertex>";
				this.file.write(temp.getBytes());
				temp = "</UML:SubactivityState>";
				this.file.write(temp.getBytes());

			} else {
				temp = "<UML:ActionState xmi.id=\"G."
						+ proc.getId()
						+ ".5\" name=\""
						+ proc.getName()
						+ "\" visibility=\"public\" isDynamic=\"false\" outgoing=\"";
				this.file.write(temp.getBytes());
				tranEnum = procStructure.getOutVec();
				while (tranEnum.hasMoreElements()) {
					temp = "G." + (String) tranEnum.nextElement() + " ";
					this.file.write(temp.getBytes());
				}
				temp = "\" incoming=\"";
				this.file.write(temp.getBytes());
				tranEnum = procStructure.getInVec();
				while (tranEnum.hasMoreElements()) {
					temp = "G." + (String) tranEnum.nextElement() + " ";
					this.file.write(temp.getBytes());
				}
				temp = "\">";
				this.file.write(temp.getBytes());
				temp = "<UML:ActionState.dynamicArguments>";
				this.file.write(temp.getBytes());
				temp = "<UML:ArgListsExpression language=\"\" body=\"\" /> ";
				this.file.write(temp.getBytes());
				temp = "</UML:ActionState.dynamicArguments>";
				this.file.write(temp.getBytes());
				temp = "<UML:ActionState.dynamicMultiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity>";
				this.file.write(temp.getBytes());
				temp = "<UML:Multiplicity.range>";
				this.file.write(temp.getBytes());
				temp = "<UML:MultiplicityRange lower=\"0\" upper=\"0\" /> ";
				this.file.write(temp.getBytes());
				temp = "</UML:Multiplicity.range>";
				this.file.write(temp.getBytes());
				temp = "</UML:Multiplicity>";
				this.file.write(temp.getBytes());
				temp = "</UML:ActionState.dynamicMultiplicity>";
				this.file.write(temp.getBytes());
				temp = "</UML:ActionState>";
				this.file.write(temp.getBytes());
			}

		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch

	}// end of func

	// -----------------------------------ActivityDiagramCreate--------------------------------------------------
	/**
	 * Main function call in order to generate an ACTIVITY DIAGAM.
	 * 
	 * @param ISystem
	 *            sys
	 * @param FileOutputStream
	 *            _file
	 * @param int
	 *            opA - the option in which the user selected to generate the
	 *            activity diagram.
	 * @param Vector
	 *            vecA - processes that were selected by the user.
	 * @param int
	 *            level - level that was selected by the user.
	 * @param Vector
	 *            actDiagramsVec - contains the id`s of all activity diagrams.
	 * @param Vector
	 *            ActivityPrint - used for outside printing of activities.
	 * @param Vector
	 *            ForkPrint - used for outside printing of forks.
	 * @param Vector
	 *            JoinPrint - used for outside printing of joins.
	 * @param Vector
	 *            InitialPrint - used for outside printing of initial states.
	 * @param Vector
	 *            FinalPrint - used for outside printing of final states.
	 * @param Vector
	 *            BranchPrint - used for outside printing of branches.
	 * @param Vector
	 *            TransitionPrint - used for outside printing of transitions.
	 */
	public void ActivityDiagramCreate(ISystem sys, XmiWriter _file, int opA,
			Vector vecA, int level, Vector actDiagramsVec,
			Vector ActivityPrint, Vector ForkPrint, Vector JoinPrint,
			Vector InitialPrint, Vector FinalPrint, Vector BranchPrint,
			Vector TransitionPrint) throws IOException {

		// ------------initialization------------------
		this.MyStructure = sys.getISystemStructure();

		// --------------------------------------------
		Vector transitionsVec = new Vector(4, 2); // for transition printing
		Vector procVec = new Vector(4, 2);
		Vector signalEventVec = new Vector(2, 2);
		Enumeration locenum;
		IProcessEntry proc;
		String temp;
		Transition trans;
		this.file = _file.file ; 

		temp = "<!--  ==================== State/Activity Model    [ActivityGraph] ====================  --> ";
		this.file.write(temp.getBytes());
		temp = "<UML:ActivityGraph xmi.id=\"S.0000.5\" name=\"State/Activity Model\" isSpecification=\"false\" visibility=\"public\" context=\"G.0000\">";
		this.file.write(temp.getBytes());
		temp = "<UML:StateMachine.top>";
		this.file.write(temp.getBytes());
		temp = "<!--  ==================== State/Activity Model::{top}    [CompositeState] ====================  --> ";
		this.file.write(temp.getBytes());
		temp = "<UML:CompositeState xmi.id=\"XX.0000.5\" name=\"{top}\" visibility=\"public\" isConcurrent=\"false\">";
		this.file.write(temp.getBytes());
		temp = "<UML:CompositeState.subvertex>";
		this.file.write(temp.getBytes());
		if (opA == 1) { //from top level down
			this.procsInRootOpd(this.findRootOpd(this.MyStructure), procVec);
			if (!procVec.isEmpty()) {
				this.createActForProc("00.555", "RootOpd", true, level,
						procVec, transitionsVec, actDiagramsVec, ActivityPrint,
						ForkPrint, JoinPrint, InitialPrint, FinalPrint,
						BranchPrint, TransitionPrint, signalEventVec);//define  id=00.555 as id of the top level
			}
		}

		if (opA == 2) { //from proc down
			boolean initialNeeded = true;
			locenum = vecA.elements();
			while (locenum.hasMoreElements()) {
				proc = (IProcessEntry) locenum.nextElement();
				this.orderChildProcs(proc, procVec);
				if (!procVec.isEmpty()) {
					this.createActForProc("" + proc.getId() + ".5", proc
							.getName(), initialNeeded, level, procVec,
							transitionsVec, actDiagramsVec, ActivityPrint,
							ForkPrint, JoinPrint, InitialPrint, FinalPrint,
							BranchPrint, TransitionPrint, signalEventVec);
					initialNeeded = false;
				}
			}
		}
		temp = "</UML:CompositeState.subvertex>";
		this.file.write(temp.getBytes());
		temp = "</UML:CompositeState>";
		this.file.write(temp.getBytes());
		temp = "</UML:StateMachine.top>";
		this.file.write(temp.getBytes());
		temp = "<UML:StateMachine.transitions>";
		this.file.write(temp.getBytes());
		//prints of transitions
		locenum = transitionsVec.elements();
		while (locenum.hasMoreElements()) {
			trans = (Transition) locenum.nextElement();
			temp = "<UML:Transition xmi.id=\"G."
					+ trans.getTransitionId()
					+ "\" name=\"\" visibility=\"public\" ";
			this.file.write(temp.getBytes());
			if (trans.getTransitionTrigger() != null) {
				temp = "trigger=\"S." + trans.getTransitionTrigger() + "\" ";
				this.file.write(temp.getBytes());
			}
			temp = "source=\"G." + trans.getTransitionSource()
					+ "\" target=\"G." + trans.getTransitionDestination()
					+ "\" /> ";
			this.file.write(temp.getBytes());
		}

		temp = "</UML:StateMachine.transitions>";
		this.file.write(temp.getBytes());
		temp = "</UML:ActivityGraph>";
		this.file.write(temp.getBytes());

		//  SignalEvent
		String sinalEventId, signalEventName;
		locenum = signalEventVec.elements();
		while (locenum.hasMoreElements()) {
			sinalEventId = (String) locenum.nextElement();
			signalEventName = (String) locenum.nextElement();
			temp = "<UML:SignalEvent xmi.id=\"S."
					+ sinalEventId
					+ "\" name=\""
					+ signalEventName
					+ "\" visibility=\"public\" signal=\"S."
					+ sinalEventId + ".sig\" /> ";
			this.file.write(temp.getBytes());
			temp = "<UML:Signal xmi.id=\"S."
					+ sinalEventId
					+ ".sig\" name=\""
					+ signalEventName
					+ "\" visibility=\"public\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" /> ";
			this.file.write(temp.getBytes());
		}

	}//end of func

}