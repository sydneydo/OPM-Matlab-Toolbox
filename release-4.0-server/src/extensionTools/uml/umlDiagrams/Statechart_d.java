package extensionTools.uml.umlDiagrams;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.IStateEntry;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;

/**
 * creates the xml code for statechart diagram part
 */
public class Statechart_d {

	public Statechart_d() {
	}

	ISystem mySys;

	boolean initialFirstFlag = false;// first initial state in object

	// -----------------------------------G E T - F E A T U R
	// E----------------------------------------
	/**
	 * Recursive function, calculates all the objects which has states and are
	 * connected to given objectd(obj) by an Exibition relation as a
	 * destination.
	 * 
	 * @param IObjectEntry
	 *            obj
	 * @param Vector
	 *            obj_w_states_list - the result Vector
	 * @param ISystemStructure
	 *            MyStructure
	 */
	private void getFeature(IObjectEntry obj, Vector obj_w_states_list,
			ISystemStructure MyStructure) {
		Enumeration e;
		Vector obj_list = new Vector(4, 2);
		IEntry abstractEntry, destination;
		IRelationEntry rel;
		IObjectEntry son_obj;

		if (obj.getStates().hasMoreElements()) {
			obj_w_states_list.addElement(obj); // adds the object to the vector
		}
		e = MyStructure.getElements();
		while (e.hasMoreElements()) {
			abstractEntry = (IEntry) e.nextElement();
			if (abstractEntry instanceof IRelationEntry) {
				rel = (IRelationEntry) abstractEntry;
				if ((rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION)
						&& (rel.getSourceId() == obj.getId())) {
					// if the relation is an exhibition relation and obj is the
					// source-->we find the destination
					destination = (IEntry) MyStructure.getIEntry(rel
							.getDestinationId());
					if (destination instanceof IObjectEntry) {
						obj_list.addElement(destination); // if the
						// destination is an
						// object we need to
						// check if it also
						// has states, and
						// so on
						// so we need to go over the destination to.
					}
				}
			}
		}
		// going through the destinations
		e = obj_list.elements();
		while (e.hasMoreElements()) {
			son_obj = ((IObjectEntry) e.nextElement());
			this.getFeature(son_obj, obj_w_states_list, MyStructure);
		}
		return;
	}

	/**
	 * Recursive function, returns true if the given object(IObjectEntry) has
	 * states or if the given object has an "feature" with states.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param IObjectEntry
	 *            obj
	 * @return true - if the given Object has states; flase - otherwise
	 */
	private boolean hasStates(ISystemStructure MyStructure, IObjectEntry obj) {
		IRelationEntry rel;
		IObjectEntry obj2;
		Enumeration e;
		IEntry entry;

		e = obj.getRelationBySource();
		while (e.hasMoreElements()) {
			rel = (IRelationEntry) e.nextElement();
			if (rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION) {
				entry = MyStructure.getIEntry(rel.getDestinationId());
				if (entry instanceof IObjectEntry) {
					obj2 = (IObjectEntry) entry;
					if (obj2.getStates().hasMoreElements()) {
						return true;
					} else {
						if (this.hasStates(MyStructure, obj2)) {
							return true;
						}
					}

				}
			}
		}
		return false;
	}

	/**
	 * Calculates all the IObjectEntrys to appear in the list provided to the
	 * user, to choose from . The result stored is in VecObj vector.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param Vector
	 *            vecObj - the clculated list
	 */
	public void getObjectsForList(ISystemStructure MyStructure, Vector vecObj) {
		Enumeration e, relList;
		IEntry entry;
		int flag = 0;
		IObjectEntry obj;
		IRelationEntry rel;

		e = MyStructure.getElements();
		while (e.hasMoreElements()) {
			entry = (IEntry) e.nextElement();
			if (entry instanceof IObjectEntry) {
				flag = 0;
				obj = (IObjectEntry) entry;
				relList = obj.getRelationByDestination();
				while (relList.hasMoreElements()) {
					rel = (IRelationEntry) relList.nextElement();
					if (rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION) {
						flag = 1;
					}
				}
				if (flag == 0) {
					if (obj.getStates().hasMoreElements()) {
						vecObj.addElement(obj);
					} else {
						if (this.hasStates(MyStructure, obj)) {
							vecObj.addElement(obj);
						}
					}
				}
			}// if
		}// while

	}

	/**
	 * Calculates if the given state has a do activity (do:). If it does, the
	 * function prints the needed parts in the XML file.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param FileOutputStream
	 *            file
	 * @param IStateEntry
	 *            state
	 */
	private void StateDoActivityNeeded(ISystemStructure MyStructure,
			XmiWriter file, IStateEntry state) throws IOException {
		
			Enumeration e;
			int flag = 0;
			ILinkEntry link;
			IProcessEntry proc;
			String temp = "";

			e = state.getLinksBySource();
			flag = 0;
			while (e.hasMoreElements()) {
				link = (ILinkEntry) e.nextElement();
				if (link.getLinkType() == OpcatConstants.INSTRUMENT_LINK) {// instrument
					// link
					// from
					// state-->Do:
					if (flag == 0) {
						flag = 1;
						temp = "<UML:State.doActivity>";
						file.write(temp.getBytes());
						temp = "<UML:ActionSequence name=\"\" visibility=\"public\" isSpecification=\"false\" isAsynchronous=\"false\">";
						file.write(temp.getBytes());
						temp = "<UML:Action.recurrence>";
						file.write(temp.getBytes());
						temp = "<UML:IterationExpression language=\"\" body=\"\" /> ";
						file.write(temp.getBytes());
						temp = "</UML:Action.recurrence>";
						file.write(temp.getBytes());
						temp = "<UML:Action.target>";
						file.write(temp.getBytes());
						temp = "<UML:ObjectSetExpression language=\"\" body=\"\" /> ";
						file.write(temp.getBytes());
						temp = "</UML:Action.target>";
						file.write(temp.getBytes());
						temp = "<UML:Action.script>";
						file.write(temp.getBytes());
						temp = "<UML:ActionExpression language=\"\" body=\"\" /> ";
						file.write(temp.getBytes());
						temp = "</UML:Action.script>";
						file.write(temp.getBytes());
						temp = "<UML:ActionSequence.action>";
						file.write(temp.getBytes());
					}
					proc = (IProcessEntry) MyStructure.getIEntry(link
							.getDestinationId());
					temp = "<UML:UninterpretedAction xmi.id=\"XX."
							+ link.getId()
							+ ".4\" name=\""
							+ proc.getName()
							+ "\" visibility=\"public\" isSpecification=\"false\" isAsynchronous=\"false\">";
					file.write(temp.getBytes());
					temp = "<UML:Action.recurrence>";
					file.write(temp.getBytes());
					temp = "<UML:IterationExpression language=\"\" body=\"1\" /> ";
					file.write(temp.getBytes());
					temp = "</UML:Action.recurrence>";
					file.write(temp.getBytes());
					temp = "<UML:Action.target>";
					file.write(temp.getBytes());
					temp = "<UML:ObjectSetExpression language=\"\" body=\"\" /> ";
					file.write(temp.getBytes());
					temp = "</UML:Action.target>";
					file.write(temp.getBytes());
					temp = "<UML:Action.script>";
					file.write(temp.getBytes());
					temp = "<UML:ActionExpression language=\"\" body=\"\" /> ";
					file.write(temp.getBytes());
					temp = "</UML:Action.script>";
					file.write(temp.getBytes());
					temp = "</UML:UninterpretedAction>";
					file.write(temp.getBytes());
				}
			}
			if (flag == 1) {
				temp = "</UML:ActionSequence.action>";
				file.write(temp.getBytes());
				temp = "</UML:ActionSequence>";
				file.write(temp.getBytes());
				temp = "</UML:State.doActivity>";
				file.write(temp.getBytes());
			}
		
	}

	/**
	 * Calculates if the given state has a entry activity (entry:). If it does,
	 * the function prints the needed parts in the XML file.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param FileOutputStream
	 *            file
	 * @param IStateEntry
	 *            state
	 */
	private void StateEntryNeeded(ISystemStructure MyStructure,
			XmiWriter file, IStateEntry state) throws IOException {
		
			Enumeration e;
			int flag = 0;
			ILinkEntry link;
			IProcessEntry proc;
			String temp = "";

			e = state.getLinksBySource();
			flag = 0;
			while (e.hasMoreElements()) {
				link = (ILinkEntry) e.nextElement();
				if (link.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK) {
					if (flag == 0) {
						flag = 1;
						temp = "<UML:State.entry>";
						file.write(temp.getBytes());
						temp = "<UML:ActionSequence name=\"\" visibility=\"public\" isSpecification=\"false\" isAsynchronous=\"false\">";
						file.write(temp.getBytes());
						temp = "<UML:Action.recurrence>";
						file.write(temp.getBytes());
						temp = "<UML:IterationExpression language=\"\" body=\"\" /> ";
						file.write(temp.getBytes());
						temp = "</UML:Action.recurrence>";
						file.write(temp.getBytes());
						temp = "<UML:Action.target>";
						file.write(temp.getBytes());
						temp = "<UML:ObjectSetExpression language=\"\" body=\"\" /> ";
						file.write(temp.getBytes());
						temp = "</UML:Action.target>";
						file.write(temp.getBytes());
						temp = "<UML:Action.script>";
						file.write(temp.getBytes());
						temp = "<UML:ActionExpression language=\"\" body=\"\" /> ";
						file.write(temp.getBytes());
						temp = "</UML:Action.script>";
						file.write(temp.getBytes());
						temp = "<UML:ActionSequence.action>";
						file.write(temp.getBytes());
					}
					proc = (IProcessEntry) MyStructure.getIEntry(link
							.getDestinationId());
					temp = "<UML:UninterpretedAction xmi.id=\"XX."
							+ link.getId()
							+ ".4\" name=\""
							+ proc.getName()
							+ "\" visibility=\"public\" isSpecification=\"false\" isAsynchronous=\"false\">";
					file.write(temp.getBytes());
					temp = "<UML:Action.recurrence>";
					file.write(temp.getBytes());
					temp = "<UML:IterationExpression language=\"\" body=\"1\" /> ";
					file.write(temp.getBytes());
					temp = "</UML:Action.recurrence>";
					file.write(temp.getBytes());
					temp = "<UML:Action.target>";
					file.write(temp.getBytes());
					temp = "<UML:ObjectSetExpression language=\"\" body=\"\" /> ";
					file.write(temp.getBytes());
					temp = "</UML:Action.target>";
					file.write(temp.getBytes());
					temp = "<UML:Action.script>";
					file.write(temp.getBytes());
					temp = "<UML:ActionExpression language=\"\" body=\"\" /> ";
					file.write(temp.getBytes());
					temp = "</UML:Action.script>";
					file.write(temp.getBytes());
					temp = "</UML:UninterpretedAction>";
					file.write(temp.getBytes());
				}
			}
			if (flag == 1) {
				temp = "</UML:ActionSequence.action>";
				file.write(temp.getBytes());
				temp = "</UML:ActionSequence>";
				file.write(temp.getBytes());
				temp = "</UML:State.entry>";
				file.write(temp.getBytes());
			}
		
	}

	int flagCondInstEvnt = 0;

	/**
	 * Calculates if there is an outgoing instrument link or instrument event
	 * link from the given state to a process and if there is also a condition
	 * link to this process
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param IStateEntry
	 *            state
	 * @param Vector
	 *            TransStateVec - Vector of transitions, used in
	 *            CreateTransitions functions.
	 * @param Vector
	 *            TransitionStatePrint - Vector of transitions, used in
	 *            Diagram_Creator class.
	 * @param Vector
	 *            signalEventStateVec - Vector contains the signalEvent part,
	 *            used for printing in StateChartDiagramCreate function
	 * @return String: transitiond id if found, else "-1".
	 */
	private String hasConditionAndInstrument(ISystemStructure MyStructure,
			IStateEntry state, Vector TransStateVec,
			Vector TransitionStatePrint, Vector signalEventStateVec) {
		ILinkEntry link = null, linkp;
		IProcessEntry proc = null;
		Enumeration locenum, enup;
		int flag = 0;
		String id = "";
		Vector conditionVec = new Vector(2, 1);

		locenum = state.getLinksBySource();
		while (locenum.hasMoreElements()) {
			link = (ILinkEntry) locenum.nextElement();
			if ((link.getLinkType() == OpcatConstants.INSTRUMENT_LINK)
					|| (link.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK)) {
				proc = (IProcessEntry) MyStructure.getIEntry(link
						.getDestinationId());
				enup = proc.getLinksByDestination();
				while (enup.hasMoreElements()) {
					linkp = (ILinkEntry) enup.nextElement();
					if (linkp.getLinkType() == OpcatConstants.CONDITION_LINK) {
						conditionVec.addElement("" + linkp.getCondition() + ""); // if
						// the
						// process
						// has
						// condition
						// Link
						// and
						// an
						// instrument
						// link(from
						// state)
						flag = 1;
					}
				}
			}
		}
		if (flag == 1) {// both instrument and condition
			id = "" + state.getId() + "." + link.getId() + "." + proc.getId()
					+ ".4";
			TransitionStatePrint.addElement(id);
			TransStateVec.addElement(id);
			TransStateVec.addElement("" + state.getId() + "." + link.getId()
					+ ".4");// trigger
			TransStateVec.addElement("" + state.getId() + ".4");// source
			TransStateVec.addElement("" + state.getId() + ".4");// destination
			TransStateVec.addElement("" + state.getId() + "." + state.getId()
					+ ".4");// gaurd id
			TransStateVec.addElement(conditionVec);// cond vec
			signalEventStateVec.addElement("" + link.getId() + "."
					+ proc.getId() + ".4");
			signalEventStateVec.addElement("" + proc.getName() + "");
			return id;
		}
		return "-1";
	}

	/**
	 * Caculates the simple state part in the XML file and prints it.
	 * 
	 * @param ISystem
	 *            sys
	 * @param FileOutputStream
	 *            file
	 * @param Vector
	 *            stateDiagramsOut - Includes all the "diagrams" needs to be
	 *            printed in Diagram_Creator.
	 * @param IObjectEntry
	 *            father_obj - where the given state belongs to, father_obj is
	 *            the composite state.
	 * @param IObjectEntry
	 *            obj - Object, the state is created for.
	 * @param Vector
	 *            StateSimplePrint - Includes all the simple states needs to be
	 *            printed in Diagram_Creator.
	 * @param Vector
	 *            TransitionStatePrint - Includes all the transitions needs to
	 *            be printed in Diagram_Creator.
	 * @param Vector
	 *            TransStateVec - Contains all transitons found while creating
	 *            states, used in the CreateTransitions function
	 * @param Vector
	 *            TransStateVec2 - Contains all transitons found while creating
	 *            states, used in the CreateTransitions2 function
	 * @param Vector
	 *            signalEventStateVec - Contains all SignalEvent found while
	 *            creating states, used in the main function
	 *            StateChartDiagramCreate
	 * @param Vector
	 *            finalStatePrint - Includes all the final states needs to be
	 *            printed in Diagram_Creator.
	 * @param Vector
	 *            initialStatePrint - Includes all the initial states needs to
	 *            be printed in Diagram_Creator.
	 * 
	 */
	private void simpleStatesCreate(ISystem sys, XmiWriter file,
			Vector stateDiagramsOut, IObjectEntry father_obj, IObjectEntry obj,
			Vector StateSimplePrint, Vector TransitionStatePrint,
			Vector TransStateVec, Vector TransStateVec2,
			Vector signalEventStateVec, Vector finalStatePrint,
			Vector initialStatePrint) throws IOException {
		try {
			ISystemStructure MyStructure;
			Enumeration stateList, list, locenum;
			IStateEntry state;
			String temp = "", ans = "", exp = "";
			boolean hasStates = false;
			boolean hasInitial = false;
			boolean hasFinal = false;
			Vector transitionVec = new Vector(3, 3);
			Vector finalVec = new Vector(3, 3);
			Vector initialVec = new Vector(3, 3);
			this.mySys = sys;
			MyStructure = this.mySys.getISystemStructure();

			stateList = obj.getStates();
			if (stateList.hasMoreElements()) {
				stateDiagramsOut.addElement(obj);
				stateDiagramsOut.addElement(father_obj);
				hasStates = true;
			}
			while (stateList.hasMoreElements()) {
				this.flagCondInstEvnt = 0;
				state = (IStateEntry) stateList.nextElement();
				StateSimplePrint.addElement("" + state.getId() + ".4");// for
				// outside
				// printing
				// of
				// simple
				// states
				temp = "<UML:SimpleState xmi.id=\"G." + state.getId()
						+ ".4\" name=\"" + state.getName()
						+ "\" visibility=\"public\" isSpecification=\"false\" ";
				file.write(temp.getBytes());
				ans = this.hasConditionAndInstrument(MyStructure, state,
						TransStateVec, TransitionStatePrint,
						signalEventStateVec);
				temp = "outgoing=\"";
				file.write(temp.getBytes());
				if (state.isFinal()) {
					temp = "G." + state.getId() + ".46 ";
					file.write(temp.getBytes());
					TransitionStatePrint.addElement("" + state.getId() + ".46");
					TransStateVec2.addElement("" + state.getId() + ".46");
					TransStateVec2.addElement("" + state.getId() + ".4");
					TransStateVec2.addElement("" + obj.getId() + ".46");
					finalVec.addElement("" + state.getId() + ".46");
					hasFinal = true;
				}
				if (ans != "-1") {
					temp = "G." + ans + " ";
					file.write(temp.getBytes());
				}
				this.TransitionNeededOutgoing(MyStructure, obj, state,
						transitionVec, TransitionStatePrint, TransStateVec,
						signalEventStateVec);
				list = transitionVec.elements();
				while (list.hasMoreElements()) {
					temp = "G." + list.nextElement() + " ";
					file.write(temp.getBytes());
				}
				transitionVec.removeAllElements();
				exp = this.ExceptionTransitionOutgoing(MyStructure, obj, state,
						transitionVec, TransitionStatePrint, TransStateVec,
						signalEventStateVec);
				list = transitionVec.elements();
				while (list.hasMoreElements()) {
					temp = "G." + list.nextElement() + " ";
					file.write(temp.getBytes());
				}
				transitionVec.removeAllElements();
				temp = "\" incoming=\"";
				file.write(temp.getBytes());
				if (state.isInitial()) {
					temp = "G." + state.getId() + ".45 ";
					file.write(temp.getBytes());
					TransitionStatePrint.addElement("" + state.getId() + ".45");
					TransStateVec2.addElement("" + state.getId() + ".45");
					TransStateVec2.addElement("" + obj.getId() + ".45");
					TransStateVec2.addElement("" + state.getId() + ".4");
					initialVec.addElement("" + state.getId() + ".45");
					hasInitial = true;
				}
				if (ans != "-1") {
					temp = "G." + ans + " ";
					file.write(temp.getBytes());
				}
				this.TransitionNeededIncoming(MyStructure, obj, state,
						transitionVec);
				list = transitionVec.elements();
				while (list.hasMoreElements()) {
					temp = "G." + list.nextElement() + " ";
					file.write(temp.getBytes());
				}
				transitionVec.removeAllElements();
				if (exp != "-1") {
					temp = "G." + exp + " ";
					file.write(temp.getBytes());
				} else {
					this.ExceptionTransitionIncoming(MyStructure, obj, state,
							transitionVec);
					list = transitionVec.elements();
					while (list.hasMoreElements()) {
						temp = "G." + list.nextElement() + " ";
						file.write(temp.getBytes());
					}
					transitionVec.removeAllElements();
				}
				temp = "\" > ";
				file.write(temp.getBytes());
				if (ans == "-1") {
					this.StateDoActivityNeeded(MyStructure, file, state);
					this.StateEntryNeeded(MyStructure, file, state);
				}
				temp = "</UML:SimpleState>";
				file.write(temp.getBytes());
			}
			if (hasStates) {
				StateSimplePrint.addElement("-1");// next state diagram
				TransitionStatePrint.addElement("-1");
				if (hasInitial) {
					if (!this.initialFirstFlag) {
						initialStatePrint.addElement("" + obj.getId() + ".45");
						this.initialFirstFlag = true;
						temp = "<UML:Pseudostate xmi.id=\"G."
								+ obj.getId()
								+ ".45\" name=\"\" visibility=\"public\" isSpecification=\"false\" kind=\"initial\" outgoing=\"";
						file.write(temp.getBytes());
						locenum = initialVec.elements();
						while (locenum.hasMoreElements()) {
							temp = "G." + (String) locenum.nextElement() + " ";
							file.write(temp.getBytes());
						}
						temp = "\" />";
						file.write(temp.getBytes());
					}
				}
				if (hasFinal) {
					finalStatePrint.addElement("" + obj.getId() + ".46");
					temp = "<UML:FinalState xmi.id=\"G."
							+ obj.getId()
							+ ".46\" name=\"\" visibility=\"public\" isSpecification=\"false\" incoming=\"";
					file.write(temp.getBytes());
					locenum = finalVec.elements();
					while (locenum.hasMoreElements()) {
						temp = "G." + (String) locenum.nextElement() + " ";
						file.write(temp.getBytes());
					}
					temp = "\" /> ";
					file.write(temp.getBytes());
				}
				finalStatePrint.addElement("-1");
				initialStatePrint.addElement("-1");
			}
		}// end of try
		catch (IOException e) {
			System.out.println("error");
			return;
		}// end of catch

	}

	/**
	 * Prints the "states" part. While doing so the function calculates another
	 * information used in other functions as defined in the parametrs.
	 * 
	 * @param ISystem
	 *            sys
	 * @param FileOutputStream
	 *            file
	 * @param Vector
	 *            stateDiagramsOut - Includes all the "diagrams" needs to be
	 *            printed in Diagram_Creator.
	 * @param IObjectEntry
	 *            obj - Object, the state is created for.
	 * @param Vector
	 *            StateSimplePrint - Includes all the simple states needs to be
	 *            printed in Diagram_Creator.
	 * @param Vector
	 *            TransitionStatePrint - Includes all the transitions needs to
	 *            be printed in Diagram_Creator.
	 * @param Vector
	 *            TransStateVec - Contains all transitons found while creating
	 *            states, used in the CreateTransitions function
	 * @param Vector
	 *            TransStateVec2 - Contains all transitons found while creating
	 *            states, used in the CreateTransitions2 function
	 * @param Vector
	 *            signalEventStateVec - Contains all SignalEvent found while
	 *            creating states, used in the main function
	 *            StateChartDiagramCreate
	 * @param Vector
	 *            finalStatePrint - Includes all the final states needs to be
	 *            printed in Diagram_Creator.
	 * @param Vector
	 *            initialStatePrint - Includes all the initial states needs to
	 *            be printed in Diagram_Creator.
	 */
	private void CreateStates(ISystem sys, XmiWriter file,
			Vector stateDiagramsOut, IObjectEntry obj, Vector StateSimplePrint,
			Vector TransitionStatePrint, Vector TransStateVec,
			Vector TransStateVec2, Vector signalEventStateVec,
			Vector finalStatePrint, Vector initialStatePrint)
			throws IOException {
		
			ISystemStructure MyStructure;
			Enumeration list;
			IObjectEntry obj2;
			IRelationEntry rel;
			Vector compsVec = new Vector(2, 1);// contains all objects which
			// has states for a given
			// diagram(object)
			this.mySys = sys;
			MyStructure = this.mySys.getISystemStructure();

			this.initialFirstFlag = false;
			this.simpleStatesCreate(sys, file, stateDiagramsOut, obj, obj,
					StateSimplePrint, TransitionStatePrint, TransStateVec,
					TransStateVec2, signalEventStateVec, finalStatePrint,
					initialStatePrint);

			list = obj.getRelationBySource();
			while (list.hasMoreElements()) {
				rel = (IRelationEntry) list.nextElement();
				if (rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION) {
					if (MyStructure.getIEntry(rel.getDestinationId()) instanceof IObjectEntry) {
						obj2 = (IObjectEntry) MyStructure.getIEntry(rel
								.getDestinationId());
						this.getFeature(obj2, compsVec, MyStructure);// all
						// object
						// that
						// has
						// states
						// and
						// are
						// attribute
						// of
						// obj
					}
				}
			}
			list = compsVec.elements();
			while (list.hasMoreElements()) {
				obj2 = (IObjectEntry) list.nextElement();
				this.simpleStatesCreate(sys, file, stateDiagramsOut, obj, obj2,
						StateSimplePrint, TransitionStatePrint, TransStateVec,
						TransStateVec2, signalEventStateVec, finalStatePrint,
						initialStatePrint);
			}

		
	}

	/**
	 * Checks if there are outgoing consumption event link, instrument event
	 * link, consumtion link from the current state to a process and there is a
	 * result link from this process to another state of the same object. It
	 * also calculates if there is condition link to the process. All calculated
	 * data it stores in vectors that were passed as parameters.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param IObjectEntry
	 *            obj
	 * @param IStateEntry
	 *            state
	 * @param Vector
	 *            transitionVec
	 * @param Vector
	 *            TransitionStatePrint - Includes all the transitions needs to
	 *            be printed in Diagram_Creator.
	 * @param Vector
	 *            TransStateVec - Contains all transitons found, used in the
	 *            CreateTransitions function.
	 * @param Vector
	 *            signalEventStateVec - Contains all SignalEvent found, used in
	 *            the main function StateChartDiagramCreate.
	 */
	private void TransitionNeededOutgoing(ISystemStructure MyStructure,
			IObjectEntry obj, IStateEntry state, Vector transitionVec,
			Vector TransitionStatePrint, Vector TransStateVec,
			Vector signalEventStateVec) {
		IProcessEntry proc;
		ILinkEntry link1, link2, condLink;
		Enumeration linkListOut, procLinkOut, condE;
		IStateEntry state2;
		IEntry ent;
		IObjectEntry obj2;
		int flag = 0;
		Vector condVec = new Vector(1, 1);
		String path1 = "", path2 = "";

		linkListOut = state.getLinksBySource(); // all links outgoing from the
		// state
		while (linkListOut.hasMoreElements()) {
			link1 = (ILinkEntry) linkListOut.nextElement(); // link-outgoing
			if ((link1.getLinkType() == OpcatConstants.CONSUMPTION_LINK)
					|| (link1.getLinkType() == OpcatConstants.CONSUMPTION_EVENT_LINK)
					|| (link1.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK)) {// outgoing
				// consumption
				// link
				path1 = link1.getPath();
				proc = (IProcessEntry) MyStructure.getIEntry(link1
						.getDestinationId());// destination of the link
				procLinkOut = proc.getLinksBySource(); // all links outgoing
				// from the process
				while (procLinkOut.hasMoreElements()) {
					link2 = (ILinkEntry) procLinkOut.nextElement();
					path2 = link2.getPath();
					if (link2.getLinkType() == OpcatConstants.RESULT_LINK) {// out
						// of
						// the
						// same
						// process
						// -result
						// link
						ent = MyStructure.getIEntry(link2.getDestinationId()); // destination
						// of
						// the
						// link
						if (ent instanceof IStateEntry) {
							state2 = (IStateEntry) ent;
							obj2 = state2.getParentIObjectEntry();
							if ((obj.getId() == obj2.getId())
									&& ((path1.compareTo(path2)) == 0)) {
								transitionVec.addElement("" + link1.getId()
										+ "." + link2.getId() + ".4");// id of
								// a
								// transiton
								TransStateVec.addElement("" + link1.getId()
										+ "." + link2.getId() + ".4");
								TransStateVec.addElement("" + link1.getId()
										+ "." + link2.getId() + "."
										+ proc.getId() + ".4");// trigger
								TransStateVec.addElement("" + state.getId()
										+ ".4"); // source
								TransStateVec.addElement("" + state2.getId()
										+ ".4"); // destination
								// --------------------------guard id-->for
								// condition if there is.-----------------------
								condE = proc.getLinksByDestination();
								while (condE.hasMoreElements()) {
									condLink = (ILinkEntry) condE.nextElement();
									if (condLink.getLinkType() == OpcatConstants.CONDITION_LINK) {
										flag = 1;
										condVec.addElement(""
												+ condLink.getCondition() + "");
									}
								}
								if (flag == 1) {
									TransStateVec.addElement("" + state.getId()
											+ "." + proc.getId() + ".4");
									TransStateVec.addElement(condVec);
								} else {
									TransStateVec.addElement("-1");
									TransStateVec.addElement("-1");
								}
								// -----------------------------------------------------------------------------------------------------------
								TransitionStatePrint.addElement(""
										+ link1.getId() + "." + link2.getId()
										+ ".4");
								signalEventStateVec.addElement(""
										+ link1.getId() + "." + link2.getId()
										+ "." + proc.getId() + ".4");
								signalEventStateVec.addElement(""
										+ proc.getName() + "");
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Checks if there is outgoing exception link from the current state to a
	 * process and if there is a result link from this process to another state
	 * of the same object. It also calculates if there is condition link to the
	 * process. All calculated data it stores in vectors that were passed as
	 * parameters.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param IObjectEntry
	 *            obj - object to which the state belongs to
	 * @param IStateEntry
	 *            state - state to check
	 * @param Vector
	 *            transitionVec - vector used in order to print transitions
	 * @param Vector
	 *            TransitionStatePrint - outside printing of transitions
	 * @param Vector
	 *            TransStateVec - outside printing of transitions
	 * @paramVector signalEventStateVec - vector used to print the text on
	 *              transitions
	 */
	private String ExceptionTransitionOutgoing(ISystemStructure MyStructure,
			IObjectEntry obj, IStateEntry state, Vector transitionVec,
			Vector TransitionStatePrint, Vector TransStateVec,
			Vector signalEventStateVec) {
		IProcessEntry proc;
		ILinkEntry link1 = null, link2, condLink;
		Enumeration linkListOut, procLinkOut, condE;
		IStateEntry state2;
		IEntry ent;
		IObjectEntry obj2;
		int flag = 0, resultFlag = 0, exceptionFlag = 0;
		Vector condVec = new Vector(1, 1);

		linkListOut = state.getLinksBySource(); // all links outgoing from the
		// state
		while (linkListOut.hasMoreElements()) {
			link1 = (ILinkEntry) linkListOut.nextElement(); // link-outgoing
			if (link1.getLinkType() == OpcatConstants.EXCEPTION_LINK) {
				exceptionFlag = 1;
				proc = (IProcessEntry) MyStructure.getIEntry(link1
						.getDestinationId());// destination of the link
				procLinkOut = proc.getLinksBySource(); // all links outgoing
				// from the process
				while (procLinkOut.hasMoreElements()) {
					link2 = (ILinkEntry) procLinkOut.nextElement();
					if (link2.getLinkType() == OpcatConstants.RESULT_LINK) {
						ent = MyStructure.getIEntry(link2.getDestinationId()); // destination
						// of
						// the
						// link
						if (ent instanceof IStateEntry) {
							state2 = (IStateEntry) ent;
							obj2 = state2.getParentIObjectEntry();
							if (obj.getId() == obj2.getId()) {
								resultFlag = 1;
								transitionVec.addElement("" + link1.getId()
										+ "." + link2.getId() + ".4");// id of
								// a
								// transiton
								TransStateVec.addElement("" + link1.getId()
										+ "." + link2.getId() + ".4");
								TransStateVec.addElement("" + link1.getId()
										+ "." + link2.getId() + "."
										+ proc.getId() + ".4");// trigger
								TransStateVec.addElement("" + state.getId()
										+ ".4"); // source
								TransStateVec.addElement("" + state2.getId()
										+ ".4"); // destination
								// --------------------------guard id-->for
								// condition if there is.-----------------------
								condE = proc.getLinksByDestination();
								while (condE.hasMoreElements()) {
									condLink = (ILinkEntry) condE.nextElement();
									if (condLink.getLinkType() == OpcatConstants.CONDITION_LINK) {
										flag = 1;
										condVec.addElement(""
												+ condLink.getCondition() + "");
									}
								}
								if (flag == 1) {
									TransStateVec.addElement("" + state.getId()
											+ "." + proc.getId() + ".4");
									TransStateVec.addElement(condVec);
								} else {
									TransStateVec.addElement("-1");
									TransStateVec.addElement("-1");
								}
								// -----------------------------------------------------------------------------------------------------------
								TransitionStatePrint.addElement(""
										+ link1.getId() + "." + link2.getId()
										+ ".4");
								signalEventStateVec.addElement(""
										+ link1.getId() + "." + link2.getId()
										+ "." + proc.getId() + ".4");
								signalEventStateVec.addElement("tm("
										+ proc.getName() + ")");
							}
						}
					}
				}
			}
		}
		if ((exceptionFlag == 1) && (resultFlag == 0)) {
			flag = 0;
			linkListOut = state.getLinksBySource(); // all links outgoing from
			// the state
			while (linkListOut.hasMoreElements()) {
				link1 = (ILinkEntry) linkListOut.nextElement(); // link-outgoing
				if (link1.getLinkType() == OpcatConstants.EXCEPTION_LINK) {
					proc = (IProcessEntry) MyStructure.getIEntry(link1
							.getDestinationId());// destination of the link
					transitionVec.addElement("" + link1.getId() + "."
							+ link1.getId() + ".4");// id of a transiton
					TransStateVec.addElement("" + link1.getId() + "."
							+ link1.getId() + ".4");
					TransStateVec.addElement("" + link1.getId() + "."
							+ link1.getId() + "." + proc.getId() + ".4");// trigger
					TransStateVec.addElement("" + state.getId() + ".4"); // source
					TransStateVec.addElement("" + state.getId() + ".4"); // destination
					condE = proc.getLinksByDestination();
					while (condE.hasMoreElements()) {
						condLink = (ILinkEntry) condE.nextElement();
						if (condLink.getLinkType() == OpcatConstants.CONDITION_LINK) {
							flag = 1;
							condVec.addElement("" + condLink.getCondition()
									+ "");
						}
					}
					if (flag == 1) {
						TransStateVec.addElement("" + state.getId() + "."
								+ proc.getId() + ".4");
						TransStateVec.addElement(condVec);
					} else {
						TransStateVec.addElement("-1");
						TransStateVec.addElement("-1");
					}
					TransitionStatePrint.addElement("" + link1.getId() + "."
							+ link1.getId() + ".4");
					signalEventStateVec.addElement("" + link1.getId() + "."
							+ link1.getId() + "." + proc.getId() + ".4");
					signalEventStateVec
							.addElement("tm(" + proc.getName() + ")");
				}
			}
			String result = "" + link1.getId() + "." + link1.getId() + ".4";
			return result;
		}
		String tmp = "-1";
		return tmp;
	}

	/**
	 * Checks if there is incoming result link to this state from a process and
	 * if there is consumption link, consumption event link, instrument event
	 * link from the same object to this process.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param IObjectEntry
	 *            obj- the object to which the state belongs to
	 * @param IStateEntry
	 *            state - state to check
	 * @param Vector
	 *            transitionVec - vector which contains list of transition
	 *            incoming to this state
	 */
	private void TransitionNeededIncoming(ISystemStructure MyStructure,
			IObjectEntry obj, IStateEntry state, Vector transitionVec) {
		IProcessEntry proc;
		ILinkEntry link1, link2;
		Enumeration linkListOut, procLinkOut;
		IStateEntry state2;
		IEntry ent;
		IObjectEntry obj2;
		String path1 = "", path2 = "";

		linkListOut = state.getLinksByDestination(); // all links incoming to
		// the state
		while (linkListOut.hasMoreElements()) {
			link1 = (ILinkEntry) linkListOut.nextElement(); // link
			path1 = link1.getPath();
			if (link1.getLinkType() == OpcatConstants.RESULT_LINK) {
				proc = (IProcessEntry) MyStructure.getIEntry(link1
						.getSourceId());
				procLinkOut = proc.getLinksByDestination(); // all links
				// outgoing from the
				// process
				while (procLinkOut.hasMoreElements()) {
					link2 = (ILinkEntry) procLinkOut.nextElement();
					path2 = link2.getPath();
					if ((link2.getLinkType() == OpcatConstants.CONSUMPTION_LINK)
							|| (link2.getLinkType() == OpcatConstants.CONSUMPTION_EVENT_LINK)
							|| (link2.getLinkType() == OpcatConstants.INSTRUMENT_EVENT_LINK)) {
						ent = MyStructure.getIEntry(link2.getSourceId()); // destination
						// of
						// the
						// link
						if (ent instanceof IStateEntry) {
							state2 = (IStateEntry) ent;
							obj2 = state2.getParentIObjectEntry();
							if ((obj.getId() == obj2.getId())
									&& ((path1.compareTo(path2)) == 0)) {
								transitionVec.addElement("" + link2.getId()
										+ "." + link1.getId() + ".4");// id of
								// a
								// transiton
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Checks if there is result link from a process to the given state and if
	 * there is an exception link from the same object to the process. Also
	 * check for conditon links to the process.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param IObjectEntry
	 *            obj - object to which the state belongs to
	 * @param IStateEntry
	 *            state - state needed to be check
	 * @param Vector
	 *            transitionVec - contains all transitions incoming to this
	 *            state.
	 */
	private void ExceptionTransitionIncoming(ISystemStructure MyStructure,
			IObjectEntry obj, IStateEntry state, Vector transitionVec) {
		IProcessEntry proc;
		ILinkEntry link1, link2;
		Enumeration linkListOut, procLinkOut;
		IStateEntry state2;
		IEntry ent;
		IObjectEntry obj2;

		linkListOut = state.getLinksByDestination(); // all links incoming to
		// the state
		while (linkListOut.hasMoreElements()) {
			link1 = (ILinkEntry) linkListOut.nextElement(); // link
			if (link1.getLinkType() == OpcatConstants.RESULT_LINK) {
				proc = (IProcessEntry) MyStructure.getIEntry(link1
						.getSourceId());
				procLinkOut = proc.getLinksByDestination(); // all links
				// outgoing from the
				// process
				while (procLinkOut.hasMoreElements()) {
					link2 = (ILinkEntry) procLinkOut.nextElement();
					if (link2.getLinkType() == OpcatConstants.EXCEPTION_LINK) {
						ent = MyStructure.getIEntry(link2.getSourceId()); // destination
						// of
						// the
						// link
						if (ent instanceof IStateEntry) {
							state2 = (IStateEntry) ent;
							obj2 = state2.getParentIObjectEntry();
							if (obj.getId() == obj2.getId()) {
								transitionVec.addElement("" + link2.getId()
										+ "." + link1.getId() + ".4");// id of
								// a
								// transiton
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Prints the transiton part to the file
	 * 
	 * @param ISystem
	 *            sys
	 * @param FileOutputStream
	 *            file
	 * @param Vector
	 *            TransStateVec2 - the transition vector to be printed
	 */
	private void CreateTransitions2(ISystem sys, XmiWriter file,
			Vector TransStateVec2) throws IOException {
		
			Enumeration locenum;
			String temp = "";

			locenum = TransStateVec2.elements();
			while (locenum.hasMoreElements()) {
				temp = "<UML:Transition xmi.id=\"G."
						+ locenum.nextElement()
						+ "\" name=\"\" visibility=\"public\" isSpecification=\"false\" source=\"G."
						+ locenum.nextElement() + "\" target=\"G."
						+ locenum.nextElement() + "\" />";
				file.write(temp.getBytes());
			}

		
	}

	/**
	 * Prints the transiton part to the file
	 * 
	 * @param ISystem
	 *            sys
	 * @param FileOutputStream
	 *            file
	 * @param Vector
	 *            TransStateVec - the transition vector to be printed
	 */
	private void CreateTransitions(ISystem sys, XmiWriter file,
			Vector TransStateVec) throws IOException {
		
			Enumeration e, e2;
			String temp = "", temp2 = "";

			e = TransStateVec.elements();
			while (e.hasMoreElements()) {
				temp = "<UML:Transition xmi.id=\"G."
						+ e.nextElement()
						+ "\" name=\"\" visibility=\"public\" isSpecification=\"false\" trigger=\"S."
						+ e.nextElement() + "\" source=\"G." + e.nextElement()
						+ "\" target=\"G." + e.nextElement() + "\" ";
				file.write(temp.getBytes());
				temp2 = (String) e.nextElement();
				if (temp2 != "-1") {
					temp = ">";
					file.write(temp.getBytes());
					temp = "<UML:Transition.guard>";
					file.write(temp.getBytes());
					temp = "<UML:Guard xmi.id=\"XX."
							+ temp2
							+ "\" name=\"\" visibility=\"public\" isSpecification=\"false\">";
					file.write(temp.getBytes());
					temp = "<UML:Guard.expression>";
					file.write(temp.getBytes());
					temp = "<UML:BooleanExpression language=\"\" body=\"";
					file.write(temp.getBytes());
					e2 = ((Vector) (e.nextElement())).elements();// conditions
					// vector
					boolean hasCond = false;
					while (e2.hasMoreElements()) {
						temp2 = (String) e2.nextElement();
						if ((temp2.length()) > 0) {
							if (hasCond) {
								temp = " || ";
								file.write(temp.getBytes());
							}
							temp = temp2;
							file.write(temp.getBytes());
							hasCond = true;
						}
					}
					temp = "\" /> ";
					file.write(temp.getBytes());
					temp = "</UML:Guard.expression>";
					file.write(temp.getBytes());
					temp = "</UML:Guard>";
					file.write(temp.getBytes());
					temp = "</UML:Transition.guard>";
					file.write(temp.getBytes());
					temp = "</UML:Transition>";
					file.write(temp.getBytes());
				} else {
					temp2 = (String) e.nextElement();
					temp = "/>";
					file.write(temp.getBytes());
				}
			}
		
	}

	/**
	 * Main function activated in order to generate a STATECHART DIAGRAM
	 * 
	 * @param ISystem
	 *            sys
	 * @param FileOutputStream
	 *            file
	 * @param Vector
	 *            vecSt - all the objects(IObjectEntry) that the user have
	 *            chosen.
	 * @param Vector
	 *            stateDiagramsOut - Includes all the "diagrams" needs to be
	 *            printed in Diagram_Creator.
	 * @param Vector
	 *            StateSimplePrint - Includes all the simple states needs to be
	 *            printed in Diagram_Creator.
	 * @param Vector
	 *            TransitionStatePrint - Includes all the transitions needs to
	 *            be printed in Diagram_Creator.
	 * @param Vector
	 *            finalStatePrint - Includes all the final states needs to be
	 *            printed in Diagram_Creator.
	 * @param Vector
	 *            initialStatePrint - Includes all the initial states needs to
	 *            be printed in Diagram_Creator.
	 */
	public void StateChartDiagramCreate(ISystem sys, XmiWriter file,
			Vector vecSt, Vector stateDiagramsOut, Vector StateSimplePrint,
			Vector TransitionStatePrint, Vector finalStatePrint,
			Vector initialStatePrint) throws IOException {
		this.mySys = sys;

		String temp = "", sig = "", tempId = "";
		Enumeration e, locenum;
		IObjectEntry obj;

		Vector TransStateVec = new Vector(4, 2); // vector for
		// stateDiagram use
		Vector TransStateVec2 = new Vector(4, 2); // vector for
		// stateDiagram use
		Vector signalEventStateVec = new Vector(2, 1);

		e = vecSt.elements(); // the user selection object
		while (e.hasMoreElements()) {
			obj = (IObjectEntry) e.nextElement(); // object that the user
			// have selected-->a new
			// diagram for it
			temp = "<UML:StateMachine xmi.id=\"S."
					+ obj.getId()
					+ ".44\" name=\"State/Activity Model\" visibility=\"public\" isSpecification=\"false\" context=\"S."
					+ obj.getId() + "\">";
			file.write(temp.getBytes());
			temp = "<UML:StateMachine.top>";
			file.write(temp.getBytes());

			temp = "<UML:CompositeState xmi.id=\"XX."
					+ obj.getId()
					+ ".44\" name=\"{top}\" visibility=\"public\" isSpecification=\"false\" isConcurrent=\"false\">";
			file.write(temp.getBytes());
			temp = "<UML:CompositeState.subvertex>";
			file.write(temp.getBytes());

			// printouts for state
			this.CreateStates(sys, file, stateDiagramsOut, obj,
					StateSimplePrint, TransitionStatePrint, TransStateVec,
					TransStateVec2, signalEventStateVec, finalStatePrint,
					initialStatePrint);

			temp = "</UML:CompositeState.subvertex>";
			file.write(temp.getBytes());
			temp = "</UML:CompositeState>";
			file.write(temp.getBytes());

			temp = "</UML:StateMachine.top>";
			file.write(temp.getBytes());
			temp = "<UML:StateMachine.transitions>";
			file.write(temp.getBytes());

			this.CreateTransitions(sys, file, TransStateVec);
			this.CreateTransitions2(sys, file, TransStateVec2);

			temp = "</UML:StateMachine.transitions>";
			file.write(temp.getBytes());
			temp = "</UML:StateMachine>";
			file.write(temp.getBytes());

			locenum = signalEventStateVec.elements();// signals from
			// statechart
			while (locenum.hasMoreElements()) {
				tempId = (String) locenum.nextElement();
				sig = (String) locenum.nextElement();
				if (sig.startsWith("tm")) {
					temp = "<UML:SignalEvent xmi.id=\"S."
							+ tempId
							+ "\" name=\""
							+ sig
							+ "\" visibility=\"public\" isSpecification=\"false\" signal=\"S."
							+ tempId + ".1\" /> ";
					file.write(temp.getBytes());
					temp = "<UML:Signal xmi.id=\"S."
							+ tempId
							+ ".1\" name=\""
							+ sig
							+ "\" visibility=\"public\" isSpecification=\"false\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" /> ";
					file.write(temp.getBytes());
				} else {
					temp = "<UML:SignalEvent xmi.id=\"S."
							+ tempId
							+ "\" name=\"stopped("
							+ sig
							+ ")\" visibility=\"public\" isSpecification=\"false\" signal=\"S."
							+ tempId + ".1\" /> ";
					file.write(temp.getBytes());
					temp = "<UML:Signal xmi.id=\"S."
							+ tempId
							+ ".1\" name=\"stopped("
							+ sig
							+ ")\" visibility=\"public\" isSpecification=\"false\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" /> ";
					file.write(temp.getBytes());
				}
			}

		}
	}// end of func

}