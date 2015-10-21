package extensionTools.reuse;

import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPIx.IXRelationInstance;
import exportedAPI.util.APILogger;
import gui.Opcat2;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.opdBaseComponents.OpdFundamentalRelation;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.GraphicalRelationRepresentation;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.RelationEntry;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatException;
import gui.util.OpcatLogger;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * The class contains all actions for the model import process. In order to use
 * the class, it is necessary to create a <code>ReuseCommand</code> object and
 * to call the <code>commitReuse</code> method.
 */
public class ReuseCommand {
    /* reuse types */
    public final static int SIMPLE_WHITE = 3;

    public static final int SIMPLE_BLACK = 2;

    public final static int OPEN = 1;

    public final static int NO_REUSE = 0;

    public final static int REGULAR_SYSTEM = 0;

    public final static int OPEN_REUSED_SYSTEM = 1;

    public final static int MOVE = 10;

    OpdProject reusedProject = null;

    OpdProject currentProject = null;

    MetaLibrary reusedLib = null;

    Opd reusedBaseOpd = null;

    int reuseType = 0;

    // reuseTables

    /**
         * Table: reusedEntriesTable + reusedInstancesTable two hash tables that
         * save the tuples of reused and added instances/entires were created in
         * order to maintain correct entry/instance relations
         */
    private Hashtable reusedEntriesTable = new Hashtable();

    private Hashtable reusedInstancesTable = new Hashtable();

    /**
         * saves for each OPD which is open reused the correct path/link to the
         * the system it is open reused to
         */

    private LinkedList reusedInstancesGlobalList = new LinkedList();

    /**
         * a list of OPD's that were already open reused
         */
    private LinkedList reusedOpdList = new LinkedList();

    /**
         * Constructor for the reuse operation - sets the properties for the
         * reuse, and fires the reuse action.
         * 
         * @param _current
         *                the current system that the reused model will be
         *                imported into it
         * @param _reused
         *                the reused system - the external model that will be
         *                imported into the current one
         * @param _reuseType
         *                The type of the reuse commited, can be NO_REUSE, OPEN,
         *                SIMPLE_WHITE, SIMPLE-BLACK.
         */

    public ReuseCommand(OpdProject _current, MetaLibrary _reusedLib) {

	// clearing undo history
	Opcat2.getUndoManager().discardAllEdits();
	Opcat2.setRedoEnabled(false);
	Opcat2.setUndoEnabled(false);

	this.reusedProject = (OpdProject) _reusedLib.getProjectHolder();
	this.currentProject = _current;
	this.reuseType = SIMPLE_BLACK;
	this.reusedLib = _reusedLib;
    }

    public ReuseCommand(OpdProject _current, OpdProject _reused, int _reuseType) {

	// clearing undo history
	Opcat2.getUndoManager().discardAllEdits();
	Opcat2.setRedoEnabled(false);
	Opcat2.setUndoEnabled(false);

	this.reusedProject = _reused;
	this.currentProject = _current;
	this.reuseType = _reuseType;
    }

    public ReuseCommand(OpdProject _current, OpdProject _reused,
	    Opd _reusedBase, int _reuseType) {
	// clearing undo history
	Opcat2.getUndoManager().discardAllEdits();
	Opcat2.setRedoEnabled(false);
	Opcat2.setUndoEnabled(false);

	this.reusedProject = _reused;
	this.currentProject = _current;
	this.reuseType = _reuseType;
	this.reusedBaseOpd = _reusedBase;
    }

    /**
         * The method commits all types of reuse. First, the method commits a
         * simple reuse, afterwards it implies the black or open reuse means, if
         * necessary. At the end, the method sets the current OPD to do original
         * one, and presents the OPD to the user. Finally, the method updates
         * the model with the new changes.
         * 
         * @see #emptyAndLockReusedInstancesGlobalList
         */
    public void commitReuse() throws OpcatException {

	this.currentProject.setPresented(false);

	if (this.reuseType == NO_REUSE) {
	    throw new OpcatException("No reuse type was determined");
	}
	// remember the Opd we started the reuse at so we will come to him at
	// the end
	Opd current = this.currentProject.getCurrentOpd();
	ThingInstance designatedContainer = null;
	boolean forceContainer = false;
	try {
	    ThingInstance selectedContainer = (ThingInstance) this.currentProject
		    .getCurrentOpd().getSelectedItem();
	    if ((selectedContainer != null) && selectedContainer.isZoomedIn()) {
		designatedContainer = selectedContainer;
		forceContainer = true;
	    }
	} catch (ClassCastException ex) {
	    APILogger.Print(this, ex.getMessage());
	}
	current.removeSelection();

	if (this.reusedBaseOpd == null) {
	    this.reusedBaseOpd = this.reusedProject.getCurrentOpd();
	}

	this.addReusedOpd(this.reusedBaseOpd, this.currentProject
		.getCurrentOpd(), designatedContainer, forceContainer);

	// //Handling black and open reuse
	// if ((this.reuseType == SIMPLE_BLACK) || (this.reuseType == OPEN)) {
	// this.emptyAndLockReusedInstancesGlobalList();
	// }
	this.currentProject.setPresented(true);
	this.currentProject.updateChanges();
	// Returning to the original OPD
	this.currentProject.refresh();
    }

    /**
         * Copying elements from a reused OPD to a target OPD recursively.
         * general algorithm:
         * <p>
         * going over the entire elements of the resued system in two main loops
         * <br>
         * First Loop - reuse all the things (object/processes). For each thing,
         * a new entry and instance are created. If the thing has inner levels,
         * the inner levels are created using <code>createInnerLevels()</code>
         * method. <br>
         * Second loop - reuse all the links relations <br>
         * 
         * @param reusedOpd
         *                The OPD that the elements are copied from
         * @param targetOpd
         *                The OPD that the elements are copied to
         * @param container
         *                The instance that the elements will be placed within
         *                it. If it is null, then the elements will not be
         *                contained within an inzommed entity.
         * @param forceContainer
         *                A flag indicating whether a container which is not
         *                part of the reused model will be forced on elements of
         *                the model. The method is used mainly for the first
         *                level of reusing, when reusing inside a zoomed-in
         *                thing in the current model
         */
    public boolean addReusedOpd(Opd reusedOpd, Opd targetOpd,
	    ThingInstance container, boolean forceContainer)
	    throws OpcatException {
	int x = 0, y = 0; // coordinates for objects/processes at first
	// level
	if (GraphicsUtils.getLastMouseEvent() != null) {
	    x = GraphicsUtils.getLastMouseEvent().getX();
	    y = GraphicsUtils.getLastMouseEvent().getY();
	}
	Point showPoint = new Point(x, y);
	x = (int) showPoint.getX();
	y = (int) showPoint.getY();

	Instance generalInstance = null;
	ThingEntry myEntry;
	ConnectionEdgeInstance conInstance;

	try {
	    // first loop - running on all the elements of the reused OPD
	    Enumeration e = this.reusedProject.getIXSystemStructure()
		    .getInstancesInOpd(reusedOpd.getOpdId());
	    while (e.hasMoreElements()) {
		generalInstance = (Instance) e.nextElement();
		this.currentProject.setCurrentOpd(targetOpd);
		if ((generalInstance instanceof ObjectInstance)
			|| (generalInstance instanceof ProcessInstance)) {

		    // determining the location of the new objects
		    conInstance = (ConnectionEdgeInstance) generalInstance;
		    // if (first) {
		    // first = false ;
		    // } else {
		    y = conInstance.getY();
		    x = conInstance.getX();
		    // }

		    // object section in loop - also in charge of states
		    // Adding the actual objects/processes to the target OPD
		    // Note that the addign methods may return
		    // <code>null</code> if
		    // the
		    // thing was alrady added as a main instance
		    ThingInstance addedReusedInstance = null;
		    ThingInstance theContainer = null;
		    if (((container != null) && forceContainer)
			    || (generalInstance.getParent() != null)) {
			theContainer = container;
		    }
		    if (generalInstance instanceof ObjectInstance) {
			addedReusedInstance = this.addReusedObject(
				generalInstance, x, y, targetOpd, theContainer);
			// Adding process
		    } else if (generalInstance instanceof ProcessInstance) {
			addedReusedInstance = this.addReusedProcess(
				generalInstance, x, y, targetOpd, theContainer);
		    } else {
			// In this case we got something which is not an object
			// or a
			// process
			OpcatLogger.logError("instance wrong in reuse - "
				+ generalInstance);
		    }

		    myEntry = (ThingEntry) generalInstance.getEntry();
		    ThingInstance thingInstance = (ThingInstance) generalInstance;
		    // Creates zooming levels
		    if ((myEntry.getZoomedInOpd() != null)
			    && !this.isOpdReused(myEntry.getZoomedInOpd())) {
			this.createZoominLevels(
				(ThingInstance) generalInstance,
				addedReusedInstance, targetOpd);
		    }
		    // Creates unfolding levels
		    if (thingInstance.getUnfoldingOpd() != null) {
			this.createUnfoldingLevels(
				(ThingInstance) generalInstance,
				addedReusedInstance, targetOpd);
		    }
		}
	    }
	    // end of first loop
	    // this function arranges the father-son relations between
	    // Things
	    // second loop
	    // this loop goes over all the elements in the Opd
	    // add's all the relations and the links
	    e = this.reusedProject.getSystemStructure().getInstancesInOpd(
		    reusedOpd.getOpdId());
	    this.currentProject.setCurrentOpd(targetOpd);
	    while (e.hasMoreElements()) {
		generalInstance = (Instance) e.nextElement();
		if (generalInstance instanceof LinkInstance) {
		    this.addReusedLink(generalInstance, targetOpd);
		}

		if (generalInstance instanceof GeneralRelationInstance) {
		    this.addReusedRelation(generalInstance, true);
		}

		if (generalInstance instanceof FundamentalRelationInstance) {
		    this.addReusedRelation(generalInstance, false);
		}
	    }
	} catch (OpcatException oex) {
	    throw oex;
	} catch (Exception ex) {
	    throw new OpcatException(ex.getMessage());
	}
	return true;
    }

    /**
         * Adds an object from the reused model to the current model. If the
         * thing has a container, then it is added under this container. The
         * thing might not be added if it was added before as a main instance.
         * 
         * @param reusedProcessInstance
         * @param x
         *                The x coordinate of the new thing
         * @param y
         *                The y coordinate of the new thing
         * @param current_opd
         *                The opd in which the thing is added
         * @param container
         *                The container object of the thing. Can be null, if the
         *                thing is not added under a container
         * @return The new instance, or null if the thing was not added
         */
    protected ObjectInstance addReusedObject(Instance reusedObjectInstance,
	    int x, int y, Opd targetOpd, ThingInstance container)
	    throws OpcatException {

	if (this.reusedInstancesTable.containsKey(reusedObjectInstance)) {
	    return null;
	}
	ObjectInstance addedObjectInstance;
	ObjectInstance ins = (ObjectInstance) reusedObjectInstance;

	// If the thing was already copied from the reused model.
	// It can happen, for instance, when things from inner levels were
	// copied
	// before things from upper levels
	try {
	    if (this.existReusedEntry(reusedObjectInstance.getEntry())) {
		ThingEntry originalEntry = (ThingEntry) this.reusedEntriesTable
			.get(reusedObjectInstance.getEntry());
		// Handling things that have a container
		if (container != null) {
		    addedObjectInstance = this.currentProject
			    .addObjectInstanceToContainer(ins.getEntry()
				    .getName(), x, y, originalEntry, container
				    .getThing());
		} else {
		    addedObjectInstance = this.currentProject
			    .addObjectInstance(ins.getEntry().getName(), x, y,
				    originalEntry);
		}

		// Adding things for the first time
	    } else {
		if (container != null) {
		    // Adding the current object to a container
		    addedObjectInstance = this.currentProject.addObject(x, y,
			    container.getThing(), -1, -1, false);
		} else {
		    // Adding the current object without a container
		    addedObjectInstance = (ObjectInstance) this.currentProject
			    .addObject(reusedObjectInstance.getEntry()
				    .getName(), x, y, targetOpd.getOpdId());
		}
		this.copyReusedObjectEntryProperties(
			(ObjectEntry) reusedObjectInstance.getEntry(),
			(ObjectEntry) addedObjectInstance.getEntry());
	    }

	    ObjectInstance i = (ObjectInstance) reusedObjectInstance;
	    addedObjectInstance.setStatesAutoArranged(i.isStatesAutoArranged());
	    // addedObjectInstance.setStatesAutoArranged(false);
	    addedObjectInstance.copyPropsFrom(i);
	    addedObjectInstance.setSize(i.getConnectionEdge().getWidth(), i
		    .getConnectionEdge().getHeight());
	    addedObjectInstance.setLocation(i.getConnectionEdge().getX(), i
		    .getConnectionEdge().getY());
	    // addedObjectInstance.update();

	    // Adding the states
	    this.addReusedStates(addedObjectInstance,
		    (ObjectInstance) reusedObjectInstance, targetOpd);

	    // OpdConnectionEdge oce =
	    // addedObjectInstance.getConnectionEdge();
	    // OpdConnectionEdge reoce = ins.getConnectionEdge();

	    // oce.setSize(reoce.getWidth(), reoce.getHeight()) ;
	    // oce.setLocation(reoce.getX(), reoce.getY()) ;

	    // if (this.reuseType == OPEN) {
	    // this.currentProject.addInstanceToOpenReuseList(addedObjectInstance);
	    // addedObjectInstance.getEntry().setOpenReused(true);
	    // }

	    // if (addedObjectInstance.getStateInstances().hasMoreElements()
	    // && addedObjectInstance.isStatesAutoArranged()) {
	    // oce.fitToContent();
	    // }

	    this.reuseTablesUpdate(reusedObjectInstance, addedObjectInstance);
	} catch (RuntimeException e) {
	    String message = "Importing process had failed in object: "
		    + reusedObjectInstance + ", in Opd " + targetOpd;
	    throw new OpcatException(message);
	}

	if (reuseType == SIMPLE_BLACK) {
	    try {
		ObjectEntry addedEntry = (ObjectEntry) addedObjectInstance
			.getEntry();
		ObjectEntry reusedEntry = (ObjectEntry) reusedObjectInstance
			.getEntry();

		addedEntry.addRole(new Role(reusedEntry.getId(), reusedLib));

		// now the states.

		Enumeration<StateEntry> addedStates = addedEntry.getStates();

		while (addedStates.hasMoreElements()) {
		    StateEntry addedState = addedStates.nextElement();
		    StateEntry rusedState = reusedEntry.getState(addedState
			    .getName());
		    if (reusedEntry != null) {
			addedState.addRole(new Role(rusedState.getId(),
				reusedLib));
		    }

		}
	    } catch (Exception ex) {
		OpcatLogger.logError(ex);
	    }
	}
	return addedObjectInstance;
    }

    /**
         * Adds a process from the reused model to the current model. If the
         * thing has a container, then it is added under this container. The
         * thing might not be added if it was added before as a main instance.
         * 
         * @param reusedProcessInstance
         * @param x
         *                The x coordinate of the new thing
         * @param y
         *                The y coordinate of the new thing
         * @param current_opd
         *                The opd in which the thing is added
         * @param container
         *                The container object of the thing. Can be null, if the
         *                thing is not added under a container
         * @param forceContainer
         *                If sets to <code>true</code>, then the new instance
         *                will be placed within the given <code>container</code>,
         *                even though, in the reused model, the thing was not
         *                contained originally.
         * @return The new instance, or null if the thing was not added
         */
    protected ProcessInstance addReusedProcess(Instance reusedProcessInstance,
	    int x, int y, Opd current_opd, ThingInstance container)
	    throws OpcatException {
	if (this.reusedInstancesTable.containsKey(reusedProcessInstance)) {
	    return null;
	}
	ProcessInstance addedProcessInstance = null;
	try {
	    if (this.existReusedEntry(reusedProcessInstance.getEntry())) {
		ThingEntry entry = (ThingEntry) this.reusedEntriesTable
			.get(reusedProcessInstance.getEntry());
		// Handling things that have a container
		if (container != null) {
		    addedProcessInstance = this.currentProject
			    .addProcessInstanceToContainer(
				    reusedProcessInstance.getEntry().getName(),
				    x, y, entry, container.getThing());
		}
		// Handling things without a container
		else {
		    addedProcessInstance = this.currentProject
			    .addProcessInstance(reusedProcessInstance
				    .getEntry().getName(), x, y, entry);
		}
		// addedProcessInstance.setZoomedIn(true);
	    } else {
		if (container != null) {
		    // Creating process with a container
		    addedProcessInstance = this.currentProject.addProcess(x, y,
			    container.getThing(), -1, -1);
		} else {
		    // Adding the process without a container
		    addedProcessInstance = (ProcessInstance) this.currentProject
			    .addProcess(reusedProcessInstance.getEntry()
				    .getName(), x, y, current_opd.getOpdId());
		}
		addedProcessInstance.getEntry().setName(
			reusedProcessInstance.getEntry().getName());
	    }
	    ConnectionEdgeInstance resuedConInstance = (ConnectionEdgeInstance) reusedProcessInstance;
	    addedProcessInstance.setSize(resuedConInstance.getWidth(),
		    resuedConInstance.getHeight());
	    addedProcessInstance
		    .copyPropsFrom((ProcessInstance) reusedProcessInstance);
	    addedProcessInstance.setLocation(x, y);
	    // addedProcessInstance.update();
	    // if (this.reuseType == OPEN) {
	    // this.currentProject.addInstanceToOpenReuseList(addedProcessInstance);
	    // addedProcessInstance.getEntry().setOpenReused(true);
	    // }
	    this.copyReusedProcessEntryProperties(
		    (ProcessEntry) reusedProcessInstance.getEntry(),
		    (ProcessEntry) addedProcessInstance.getEntry());
	    this.reuseTablesUpdate(reusedProcessInstance, addedProcessInstance);
	} catch (RuntimeException e) {
	    String message = "Importing process had failed in process: "
		    + reusedProcessInstance + ", in Opd " + current_opd;
	    throw new OpcatException(message);
	}

	if (reuseType == SIMPLE_BLACK) {
	    try {
		ProcessEntry addedEntry = (ProcessEntry) addedProcessInstance
			.getEntry();
		ProcessEntry reusedEntry = (ProcessEntry) reusedProcessInstance
			.getEntry();

		addedEntry.addRole(new Role(reusedEntry.getId(), reusedLib));

	    } catch (Exception ex) {
		OpcatLogger.logError(ex);
	    }
	}
	return addedProcessInstance;
    }

    /**
         * Copies an object's states from the reused object to the new object.
         */
    protected void addReusedStates(ObjectInstance addedObject,
	    ObjectInstance reusedFrom, Opd targetOpd) {
	ObjectInstance reusedObjectFrom = reusedFrom;
	StateInstance addedReusedState, instance = null;

	Entry addedState;
	Enumeration g;
	Enumeration e = reusedObjectFrom.getStateInstances();
	while (e.hasMoreElements()) {
	    addedReusedState = (StateInstance) e.nextElement();
	    StateInstance targetStateInstance;
	    // if this state has not been entered yet
	    if (!this.existReusedEntry(addedReusedState.getEntry())) {
		addedState = (Entry) this.currentProject.addState(
			addedReusedState.getEntry().getName(), addedObject);

		this.reusedEntriesTable.put(addedReusedState.getEntry(),
			addedState);
		// Copy entry properties
		this.copyReusedStateEntryProperties(
			(StateEntry) addedReusedState.getEntry(),
			(StateEntry) addedState);
		g = addedObject.getStateInstances();
		// Find the instance of the new state we added.
		while (g.hasMoreElements()) {
		    instance = (StateInstance) g.nextElement();
		    if (instance.getEntry().getName().equals(
			    addedState.getName())) {
			break;
		    }
		}
		this.reusedInstancesTable.put(addedReusedState, instance);

		targetStateInstance = instance;
		// if (this.reuseType == OPEN) {
		// targetStateInstance.getEntry().setOpenReused(true);
		// this.currentProject
		// .addInstanceToOpenReuseList(targetStateInstance);
		// }
		this.reusedInstancesGlobalList.add(targetStateInstance);
	    }
	    // if this state has been already entered
	    else {
		// For some strange reason, the instance comes already with the
		// states. We delete the old ones and copy the new ones.
		g = addedObject.getStateInstances();
		StateInstance killed;
		while (g.hasMoreElements()) {
		    killed = (StateInstance) g.nextElement();
		    if (killed.getEntry().getName().equals(
			    addedReusedState.getEntry().getName())) {
			killed.removeFromContainer();
		    }
		}

		instance = this.currentProject.addStateInstance(targetOpd,
			addedObject, (StateEntry) this.reusedEntriesTable
				.get(addedReusedState.getEntry()));
		this.reusedInstancesTable.put(addedReusedState, instance);
		targetStateInstance = instance;
		// if ((this.reuseType == SIMPLE_BLACK) || (this.reuseType ==
		// OPEN)) {
		// targetStateInstance.getEntry().commitLock();
		//
		// if (this.reuseType == OPEN) {
		// targetStateInstance.getEntry().setOpenReused(true);
		// }
		// this.currentProject
		// .addInstanceToOpenReuseList(targetStateInstance);
		// }
		this.reusedInstancesGlobalList.add(targetStateInstance);
	    }

	    targetStateInstance.copyPropsFrom(addedReusedState);
	    targetStateInstance.getConnectionEdge().setSize(
		    addedReusedState.getConnectionEdge().getWidth(),
		    addedReusedState.getConnectionEdge().getHeight());
	    targetStateInstance.getConnectionEdge().setLocation(
		    addedReusedState.getConnectionEdge().getX(),
		    addedReusedState.getConnectionEdge().getY());
	    // targetStateInstance.setSize(addedReusedState.getWidth(),
	    // addedReusedState.getHeight());
	    // targetStateInstance.setLocation(300, 300);
	    // targetStateInstance.update();
	}
    }

    /**
         * The method creates a new OPD with the zoomin levels of the main
         * instance.
         * 
         * @param reusedInstance
         *                The original instance in the reused model
         * @param addedInstance
         *                The instance that was added to the base model
         * @param target
         *                The OPD in which the addedInstance was added to
         */
    protected void createZoominLevels(ThingInstance reusedInstance,
	    ThingInstance addedInstance, Opd target) throws OpcatException {

	ThingEntry myEntry = (ThingEntry) reusedInstance.getEntry();

	try {
	    // Performing the creation of inner levels
	    Opd zoomIn = myEntry.getZoomedInOpd(); // Retrieving the OPD of
	    // the
	    if (zoomIn == null) {
		if (this.reuseType == MOVE) {
		    this.reusedProject.deleteOpd(reusedInstance.getOpd(), true);
		}
		return;
	    }
	    Opd newOpd = this.currentProject.createNewOpd(true, addedInstance,
		    target);
	    this.reusedOpdList.add(zoomIn); // Updating the reuse opd list

	    ThingEntry tEntry = (ThingEntry) addedInstance.getEntry();
	    tEntry.setZoomedInOpd(newOpd);
	    // adding another object / process
	    // get the reused system main instance
	    ThingInstance mainReusedIns = (ThingInstance) zoomIn
		    .getMainIInstance();
	    this.currentProject.setCurrentOpd(newOpd);
	    // reusedProject.setCurrentOpd(zoomIn);
	    ThingInstance newMainIns = null;
	    if (mainReusedIns instanceof ObjectInstance) {
		newMainIns = this.addReusedObject(mainReusedIns, mainReusedIns
			.getX(), mainReusedIns.getY(), newOpd, null);
	    } else if (mainReusedIns instanceof ProcessInstance) {
		newMainIns = this.addReusedProcess(mainReusedIns, mainReusedIns
			.getX(), mainReusedIns.getY(), newOpd, null);
	    }
	    newMainIns.setLocation(mainReusedIns.getX(), mainReusedIns.getY());
	    newMainIns.setZoomedIn(true);
	    // newMainIns.update();

	    // Reusing the inner level OPD
	    this.addReusedOpd(zoomIn, newOpd, newMainIns, false);
	    Entry newInsEntry = newMainIns.getEntry();
	    // newInsEntry.updateInstances();
	    newOpd.setMainEntry((ThingEntry) newInsEntry);
	    newOpd.setMainInstance(newMainIns);
	    this.currentProject.getParentDesktop().getDesktopManager()
		    .deiconifyFrame(newOpd.getOpdContainer());
	    // if ((this.reuseType == SIMPLE_BLACK) || (this.reuseType ==
	    // OPEN)) {
	    // newOpd.setLocked(true);
	    // }
	    // if (this.reuseType == OPEN) {
	    // newOpd.setOpenReused(true);
	    // }
	    if (this.reuseType == MOVE) {
		this.reusedProject.deleteOpd(zoomIn, true);
	    }
	} catch (OpcatException oe) {
	    throw oe;
	} catch (RuntimeException ex) {
	    String errorMessage = "Import had failed in inzooming Opd for thing: "
		    + reusedInstance;
	    throw new OpcatException(errorMessage);
	}
    }

    /**
         * The method creates a new OPD with the unfolding levels of the main
         * instance.
         * 
         * @param reusedInstance
         *                The original instance in the reused model
         * @param addedInstance
         *                The instance that was added to the base model
         * @param target
         *                The OPD in which the addedInstance was added to
         */
    protected void createUnfoldingLevels(ThingInstance reusedInstance,
	    ThingInstance addedInstance, Opd target) throws OpcatException {
	Opd unfold = reusedInstance.getUnfoldingOpd(); // The unfolded OPD from
	// the reused model
	if (unfold == null) {
	    if (this.reuseType == MOVE) {
		this.reusedProject.deleteOpd(reusedInstance.getOpd(), true);
	    }
	    return;
	}
	try {
	    ThingInstance newMainIns = null;
	    ThingInstance mainReusedIns = (ThingInstance) unfold
		    .getMainIInstance();
	    // If the OPD was already reused, we don't reuse it again
	    if (!this.isOpdReused(unfold)) {
		// Performing the creation of inner levels
		Opd newOpd = this.currentProject.createNewOpd(false,
			addedInstance, target);
		this.reusedOpdList.add(unfold);
		// adding the original instance objects/process
		this.currentProject.setCurrentOpd(newOpd);
		// reusedProject.setCurrentOpd(unfold);
		newMainIns = null;
		int x = mainReusedIns.getX();
		int y = mainReusedIns.getY();
		if (mainReusedIns instanceof ObjectInstance) {
		    newMainIns = this.addReusedObject(mainReusedIns, x, y,
			    newOpd, null);
		} else {
		    if (mainReusedIns instanceof ProcessInstance) {
			newMainIns = this.addReusedProcess(mainReusedIns, x, y,
				newOpd, null);
		    }
		}
		newMainIns.setLocation(x, y);
		// newMainIns.update();
		newOpd.setMainInstance(newMainIns);

		// Adding the elements of the inner levels to the new OPD
		this.addReusedOpd(unfold, newOpd, newMainIns, false);

		// if ((this.reuseType == SIMPLE_BLACK) || (this.reuseType ==
		// OPEN)) {
		// newOpd.setLocked(true);
		// }
		// if (this.reuseType == OPEN) {
		// newOpd.setOpenReused(true);
		// }
		if (this.reuseType == MOVE) {
		    this.reusedProject.deleteOpd(unfold, true);
		}
	    }
	} catch (OpcatException e) {
	    throw e;
	} catch (RuntimeException e) {
	    String errorMessage = "Import had failed in unfolding Opd for thing: "
		    + reusedInstance;
	    throw new OpcatException(errorMessage);
	}
    }

    /**
         * Adds a link, according to the given instance, to the target OPD.
         * 
         * @param reusedLinkInstance
         *                The link to be reused
         * @param target
         *                The OPD that the link is copied to.
         */
    protected LinkInstance addReusedLink(Instance reusedLinkInstance, Opd target)
	    throws OpcatException {
	LinkInstance linkInstance = (LinkInstance) reusedLinkInstance;
	Instance source, destination;
	Instance n_source, n_destination;
	source = linkInstance.getSourceInstance();
	destination = linkInstance.getDestinationInstance();
	n_source = (Instance) this.reusedInstancesTable.get(source);
	n_destination = (Instance) this.reusedInstancesTable.get(destination);
	LinkInstance myLinkInstance = null;
	try {
	    LinkEntry lEntry = (LinkEntry) linkInstance.getEntry();
	    myLinkInstance = this.currentProject.addLink(n_source,
		    n_destination, lEntry.getLinkType(), target);

	    LinkEntry newEntry = (LinkEntry) myLinkInstance.getEntry();
	    newEntry.setPath(lEntry.getPath());
	    newEntry.setCondition(lEntry.getCondition());
	    newEntry.setDescription(lEntry.getDescription());
	    newEntry.setEnvironmental(lEntry.isEnvironmental());
	    newEntry.setMaxReactionTime(lEntry.getMaxReactionTime());
	    newEntry.setMinReactionTime(lEntry.getMinReactionTime());

	    if (myLinkInstance == null) {
		return null;
	    }
	    myLinkInstance.copyPropsFrom(linkInstance);
	    myLinkInstance.setDestinationConnectionPoint(linkInstance
		    .getDestinationConnectionPoint());
	    myLinkInstance.setSourceConnectionPoint(linkInstance
		    .getSourceConnectionPoint());
	    myLinkInstance.setAutoArranged(linkInstance.isAutoArranged());
	    // myLinkInstance.makeStraight();
	    // myLinkInstance.update();
	    // if (this.reuseType == OPEN) {
	    // this.currentProject.addInstanceToOpenReuseList(myLinkInstance);
	    // myLinkInstance.getEntry().setOpenReused(true);
	    // }
	    this.reusedInstancesGlobalList.add(myLinkInstance);
	} catch (RuntimeException e) {
	    String errorMessage = "Import had failed in creating link: "
		    + reusedLinkInstance + ", in Opd " + target;
	    throw new OpcatException(errorMessage);
	}
	return myLinkInstance;
    }

    /**
         * Copies a relation from the reused model to the current project.
         */
    protected void addReusedRelation(Instance reusedRelationInstance,
	    boolean isGeneral) throws OpcatException {
	GeneralRelationInstance relationInstance;
	FundamentalRelationInstance funRelationInstance;
	Instance source, destination;
	ConnectionEdgeInstance n_source, n_destination;
	RelativeConnectionPoint destPoint = null, sourcePoint = null;
	RelationEntry rEntry = null;
	try {
	    if (isGeneral) {
		relationInstance = (GeneralRelationInstance) reusedRelationInstance;
		source = relationInstance.getSourceInstance();
		destination = relationInstance.getDestinationInstance();
		rEntry = (RelationEntry) relationInstance.getEntry();
		destPoint = ((GeneralRelationInstance) reusedRelationInstance)
			.getDestinationConnectionPoint();
		sourcePoint = ((GeneralRelationInstance) reusedRelationInstance)
			.getSourceConnectionPoint();

	    } else {
		funRelationInstance = (FundamentalRelationInstance) reusedRelationInstance;
		source = funRelationInstance.getSourceInstance();
		destination = funRelationInstance.getDestinationInstance();
		rEntry = (RelationEntry) funRelationInstance.getEntry();
		destPoint = ((FundamentalRelationInstance) reusedRelationInstance)
			.getDestinationConnectionPoint();
		sourcePoint = ((FundamentalRelationInstance) reusedRelationInstance)
			.getSourceConnectionPoint();
	    }
	    try {
		n_source = (ConnectionEdgeInstance) this.reusedInstancesTable
			.get(source);
		n_destination = (ConnectionEdgeInstance) this.reusedInstancesTable
			.get(destination);
	    } catch (Exception e) {
		n_source = null;
		n_destination = null;
	    }

	    IXRelationInstance myRelationInstance = this.currentProject
		    .addRelation(n_source, n_destination, rEntry
			    .getRelationType());
	    myRelationInstance.setDestinationConnectionPoint(destPoint);
	    myRelationInstance.setSourceConnectionPoint(sourcePoint);
	    // myRelationInstance.setAutoArranged(reusedRelationInstance.isAutoArranged())
	    // ;

	    RelationEntry newEntry = (RelationEntry) myRelationInstance
		    .getIXEntry();
	    newEntry.setBackwardRelationMeaning(rEntry
		    .getBackwardRelationMeaning());
	    newEntry.setForwardRelationMeaning(rEntry
		    .getForwardRelationMeaning());
	    newEntry.setDestinationCardinality(rEntry
		    .getDestinationCardinality());
	    newEntry.setSourceCardinality(rEntry.getSourceCardinality());
	    newEntry.setDescription(rEntry.getDescription());
	    newEntry.setEnvironmental(rEntry.isEnvironmental());
	    // newEntry.updateInstances();
	    if (isGeneral) {
		relationInstance = (GeneralRelationInstance) myRelationInstance;
		relationInstance
			.copyPropsFrom((GeneralRelationInstance) reusedRelationInstance);
		// relationInstance.update();

	    } else {
		FundamentalRelationInstance locMyRel = (FundamentalRelationInstance) myRelationInstance;
		FundamentalRelationInstance locReuseRel = (FundamentalRelationInstance) reusedRelationInstance;

		GraphicalRelationRepresentation repr = locMyRel
			.getGraphicalRelationRepresentation();
		RelativeConnectionPoint locsourcePoint = new RelativeConnectionPoint(
			locReuseRel.getSourceConnectionPoint().getSide(),
			locReuseRel.getSourceConnectionPoint().getParam());

		repr.getSourceDragger().setRelativeConnectionPoint(
			locsourcePoint);
		repr.getSourceDragger().updateDragger();

		OpdFundamentalRelation triangle = repr.getRelation();
		OpdFundamentalRelation reusetriangle = locReuseRel
			.getGraphicalRelationRepresentation().getRelation();

		triangle
			.setLocation(reusetriangle.getX(), reusetriangle.getY());
		triangle.setSize(reusetriangle.getWidth(), reusetriangle
			.getHeight());
		repr.arrangeLines();

		// locMyRel.setGraphicalRelationRepresentation(locReuseRel) ;
		// locMyRel.arrangeLines();

		// funRelationInstance.update();

	    }

	    this.reusedInstancesGlobalList.add(myRelationInstance);
	    // if (this.reuseType == OPEN) {
	    // this.currentProject.addInstanceToOpenReuseList(instance);
	    // instance = (Instance) myRelationInstance;
	    // instance.getEntry().setOpenReused(true);
	    // }
	} catch (RuntimeException e) {
	    e.printStackTrace();
	    String errorMessage = "Import had failed in creating relation: "
		    + reusedRelationInstance;
	    throw new OpcatException(errorMessage);

	}
    }

    /** ********************************************************************* */
    /* function: copyReusedThingEntryProperties */
    /* purpose: */
    /* copying properties between entires */
    /** ********************************************************************* */
    // TODO: add this command to the Entry class
    protected void copyReusedEntryProperties(Entry reusedEntry,
	    Entry targetEntry) {
	targetEntry.setDescription(reusedEntry.getDescription());
	targetEntry.setName(reusedEntry.getName());
	targetEntry.setEnvironmental(reusedEntry.isEnvironmental());
	targetEntry.setUrl(reusedEntry.getUrl());
	// targetEntry.updateInstances();
    }

    /** ********************************************************************* */
    /* function: copyReusedThingEntryProperties */
    /* purpose: */
    /* copying properties between thing entires */
    /** ********************************************************************* */
    protected void copyReusedThingEntryProperties(ThingEntry reusedThingEntry,
	    ThingEntry targetThingEntry) {
	targetThingEntry.setPhysical(reusedThingEntry.isPhysical());
	targetThingEntry.setScope(reusedThingEntry.getScope());
	targetThingEntry.setRole(reusedThingEntry.getRole());
	targetThingEntry.setNumberOfMASInstances(reusedThingEntry
		.getNumberOfMASInstances());
	targetThingEntry.setDescription(reusedThingEntry.getDescription());
	targetThingEntry.setUrl(reusedThingEntry.getUrl());
	// targetThingEntry.updateInstances();
    }

    protected void copyReusedProcessEntryProperties(ProcessEntry reused,
	    ProcessEntry target) {
	target.setDescription(reused.getDescription());
	target.setEnvironmental(reused.isEnvironmental());
	target.setMaxTimeActivation(reused.getMaxTimeActivation());
	target.setMinTimeActivation(reused.getMinTimeActivation());
	target.setName(reused.getName());
	target.setPhysical(reused.isPhysical());
	target.setProcessBody(reused.getProcessBody());
	target.setRole(reused.getRole());
	target.setScope(reused.getScope());
	target.setUrl(reused.getUrl());
	// target.setZoomedInOpd(reused.getZoomedInOpd()) ;
    }

    /** ********************************************************************* */
    /* function: copyReusedObjectEntryProperties */
    /* purpose: */
    /* copying properties between object entires */
    /** ********************************************************************* */
    protected void copyReusedObjectEntryProperties(
	    ObjectEntry reusedObjectEntry, ObjectEntry targetObjectEntry) {
	targetObjectEntry.setPersistent(reusedObjectEntry.isPersistent());
	targetObjectEntry.setEnvironmental(reusedObjectEntry.isEnvironmental());
	targetObjectEntry.setDescription(reusedObjectEntry.getDescription());
	targetObjectEntry.setName(reusedObjectEntry.getName());
	targetObjectEntry.setNumberOfMASInstances(reusedObjectEntry
		.getNumberOfMASInstances());
	targetObjectEntry.setRole(reusedObjectEntry.getRole());
	targetObjectEntry.setScope(reusedObjectEntry.getScope());
	targetObjectEntry.setType(reusedObjectEntry.getType());
	targetObjectEntry.setInitialValue(reusedObjectEntry.getInitialValue());
	targetObjectEntry.setKey(reusedObjectEntry.isKey());
	targetObjectEntry.setIndexOrder(reusedObjectEntry.getIndexOrder());
	targetObjectEntry.setPhysical(reusedObjectEntry.isPhysical());
	targetObjectEntry.setPersistent(reusedObjectEntry.isPersistent());
	targetObjectEntry.setIndexName(reusedObjectEntry.getIndexName());
	targetObjectEntry.setTypeOriginId(reusedObjectEntry.getTypeOriginId());
	targetObjectEntry.setUrl(reusedObjectEntry.getUrl());

	// targetObjectEntry.updateInstances();
    }

    /** ********************************************************************* */
    /* function: copyReusedStateEntryProperties */
    /* purpose: */
    /* copying properties between state entires */
    /** ********************************************************************* */
    protected void copyReusedStateEntryProperties(StateEntry reusedStateEntry,
	    StateEntry targetStateEntry) {
	targetStateEntry.setDefault(reusedStateEntry.isDefault());
	targetStateEntry.setFinal(reusedStateEntry.isFinal());
	targetStateEntry.setInitial(reusedStateEntry.isInitial());
	targetStateEntry.setEnvironmental(reusedStateEntry.isEnvironmental());
	targetStateEntry.setDescription(reusedStateEntry.getDescription());
	// targetStateEntry.updateInstances();
    }

    /**
         * Checks whether an entry was already reused by the process.
         * 
         * @param specialEntry
         * @return
         */
    protected boolean existReusedEntry(Entry specialEntry) {
	return this.reusedEntriesTable.containsKey(specialEntry);
    }

    /**
         * Updates the reuse hash tables with the new instance that was reused.
         * 
         * @param reusedInstance
         *                The instance of the external model
         * @param targetInstance
         *                The instance of the current model, which was added
         */
    public void reuseTablesUpdate(Instance reusedInstance,
	    Instance targetInstance) {
	this.reusedInstancesTable.put(reusedInstance, targetInstance);
	this.reusedEntriesTable.put(reusedInstance.getEntry(), targetInstance
		.getEntry());
	this.reusedInstancesGlobalList.add(targetInstance);
    }

    /**
         * Returns whether a given OPD was reused until now.
         */
    protected boolean isOpdReused(Opd aOpd) {
	if (aOpd == null) {
	    return true;
	}
	return this.reusedOpdList.contains(aOpd);
    }

}
