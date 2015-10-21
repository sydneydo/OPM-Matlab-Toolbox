package extensionTools.Testing;

/**
 This class implements the functions of Testing Entry, which is an extension
 of IXEntry. The class holds testing functions, such as animated and
 stopTesting. It also stores the events scheduled for the entry for future
 execution and keeps the log of the steps that were executed
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import util.Configuration;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPIx.IXConnectionEdgeEntry;
import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXEntry;
import exportedAPI.opcatAPIx.IXInstance;
import exportedAPI.opcatAPIx.IXLinkEntry;
import exportedAPI.opcatAPIx.IXLinkInstance;
import exportedAPI.opcatAPIx.IXObjectEntry;
import exportedAPI.opcatAPIx.IXProcessEntry;
import exportedAPI.opcatAPIx.IXRelationEntry;
import exportedAPI.opcatAPIx.IXStateEntry;
import exportedAPI.opcatAPIx.IXThingEntry;
import exportedAPI.opcatAPIx.IXThingInstance;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.StateEntry;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;

class TestingEntry extends Object {

	public static final int MaxResourceExeption = 1;

	public static final int MinResourceExeption = -1;

	public static final int NoResourceExeption = 0;

	private IXEntry iEntry = null;

	private StateEntry savedState;

	private double measurementUnitAcommulatedValue = 0;

	// current event stores differents setting that external functions
	// pass to each other.
	private TestingEvent currentEvent = null;

	private int defaultNumberOfInstances;

	private String taxtualInfo = "";

	// private int sevirityIndex = 0;

	private boolean isEventLinkActivated = false;

	private boolean online = false;

	// activation events scheduler
	// it keeps a list of events scheduled for each step
	TestingEventsScheduler actvScheduler;

	// termination events scheduler
	TestingEventsScheduler termScheduler;

	// Log table
	TestingEventsScheduler log;

	// current events list
	// usually testing entry has one current event
	// list of current events is required when more than one execution runs
	Hashtable currentEvents; // key execution Id, value TestingEvent

	// constructor
	public TestingEntry(IXEntry iEntry) {
		this.iEntry = iEntry;
		this.currentEvent = new TestingEvent();
		this.currentEvent.setNumberOfInstances(0);
		this.taxtualInfo = "";
		// set default number of instances
		if (TestingAuxiliary.isThing(iEntry)) {
			IXThingEntry iThingEntry = (IXThingEntry) iEntry;
			this.defaultNumberOfInstances = iThingEntry
					.getNumberOfMASInstances();
		} else {
			this.defaultNumberOfInstances = 0;
		}
		if (TestingAuxiliary.isLink(iEntry)) {
			this.currentEvent.setPath(((IXLinkEntry) iEntry).getPath());
		} else {
			this.currentEvent.setPath(new String());
		}
		this.actvScheduler = new TestingEventsScheduler();
		this.termScheduler = new TestingEventsScheduler();
		this.log = new TestingEventsScheduler();
		this.currentEvents = new Hashtable();
		if (iEntry instanceof IXObjectEntry) {
			measurementUnitAcommulatedValue = ((OpmObject) ((ObjectEntry) iEntry)
					.getLogicalEntity()).getMesurementUnitInitialValue();
		}
	}

	public IXEntry getIXEntry() {
		return this.iEntry;
	}

	// this function activates the entry - increases the number of instances
	// by one and applies testing
	public void activate(TestingEvent aEvent) {
		int insNum = this.getNumberOfInstances();
		// generate new executionId if it was not passed
		if (aEvent.getExecutionId() == 0) {
			aEvent.setExecutionId();
		}
		// store the path
		if (!this.currentEvent.getPath().equals("")) {
			aEvent.setPath(this.currentEvent.getPath());
		}

		// store step
		this.currentEvent.setStep(aEvent.getStep());

		// store current event
		this.addCurrentEvent(new TestingEvent(aEvent));

		this.currentEvent.setParentProcess(aEvent.getParentProcess());
		// remove all future events scheduled for it for this execution
		if (aEvent.getExecutionId() != 0) {
			this.actvScheduler.removeAllScheduledEvents(aEvent.getStep() - 1,
					aEvent.getExecutionId());
		}
		// write the activation to the log
		// set scheduling step to before writing to the log
		TestingEvent logEvent = new TestingEvent(aEvent);
		logEvent.setEventType(TestingSettings.getInstance()
				.getACTIVATION_EVENT());
		logEvent.setSchedulingStep(aEvent.getStep());
		this.log.scheduleEvent(logEvent);

		this.setNumberOfInstances(insNum + aEvent.getNumberOfInstances());
		if ((insNum > 0) || (insNum + aEvent.getNumberOfInstances() == 0)) {
			return;
		}
		this.animate(aEvent);
		this.online = true;
	}

	public int getNumberOfInstances() {
		return this.currentEvent.getNumberOfInstances();
	}

	// this function sets the number of instances and updated the related
	// entries
	private void setNumberOfInstances(int num) {
		// if (TestingAuxiliary.isThing(iEntry) && !this.isFeature()) {
		if (TestingAuxiliary.isThing(this.iEntry)) {
			IXThingEntry iThingEntry = (IXThingEntry) this.iEntry;
			if ((num + this.getNumberOfSpecializations()) > 0) {
				iThingEntry.setNumberOfMASInstances(num
						+ this.getNumberOfSpecializations());
			} else {
				iThingEntry.setNumberOfMASInstances(1);
			}
		}
		this.iEntry.updateInstances();
		// 0 is invalid number of instances for OPCAT, therefore
		// I don't set MAS instances to 0, I store it in local variable
		// currentEvent.numberOfInstances
		this.currentEvent.setNumberOfInstances(num);
	}

	// this function restores the number of instances to the default value
	// it is used when testing terminates to restore the number of MAS
	// instances
	private void setDefaultNumberOfInstances() {
		// if (TestingAuxiliary.isThing(iEntry) && !this.isFeature()) {
		if (TestingAuxiliary.isThing(this.iEntry)) {
			IXThingEntry iThingEntry = (IXThingEntry) this.iEntry;
			if (this.defaultNumberOfInstances > 0) {
				iThingEntry
						.setNumberOfMASInstances(this.defaultNumberOfInstances);
			} else {
				iThingEntry.setNumberOfMASInstances(1);
			}
		}
		this.iEntry.updateInstances();
	}

	public int getDefaultNumberOfInstances() {
		return this.defaultNumberOfInstances;
	}

	// this function deactivates the entry - decreases number of instances
	// and stops the testing
	public void deactivate(TestingEvent aEvent) {

		if (aEvent == null) {
			aEvent = new TestingEvent();
			aEvent.setEventType(TestingSettings.getInstance()
					.getTERMINATION_EVENT());
		}

		// restore path
		this.currentEvent.setPath(this.getPath(aEvent.getExecutionId()));

		// store step
		Long execId = new Long(aEvent.getExecutionId());
		if (execId.longValue() != 0) {
			if (((TestingEvent) this.currentEvents.get(execId)) != null) {
				try {
					this.currentEvent
							.setStep(((TestingEvent) this.currentEvents
									.get(execId)).getStep());
				} catch (Exception e) {
					OpcatLogger.logError(e);
				}
			}
		}

		// write the deactivation to the log
		TestingEvent logEvent = new TestingEvent(aEvent);
		logEvent.setEventType(TestingSettings.getInstance()
				.getTERMINATION_EVENT());
		logEvent.setSchedulingStep(aEvent.getStep());
		this.log.scheduleEvent(logEvent);

		// remove current event
		this.removeCurrentEvent(aEvent);

		int insNum = this.getNumberOfInstances(), numberOfInstances = aEvent
				.getNumberOfInstances();
		if (insNum >= numberOfInstances) {
			insNum -= numberOfInstances;
		} else {
			insNum = 0;
		}
		this.setNumberOfInstances(insNum);
		// ??
		if (insNum + this.currentEvent.getNumberOfSpecializations() != 0) {
			return;
		}
		this.stopTesting(aEvent);
		this.online = false;
	}

	public ArrayList<TestingEntry> getStatesTestingEntreies() {

		ArrayList<TestingEntry> al = new ArrayList<TestingEntry>();
		if (this.getIXEntry() instanceof IXProcessEntry) {
			return al;
		}

		ObjectEntry object = (ObjectEntry) this.getIXEntry();
		Enumeration<StateEntry> states = object.getStates();

		while (states.hasMoreElements()) {
			StateEntry state = states.nextElement();
			TestingEntry testingState = TestingSystem
					.getTestingElementByIXEntryID(state.getId());
			if (testingState != null) {
				al.add(testingState);
			}
		}
		return al;
	}

	// check if the entry is a feature of another entry
	public boolean isFeature() {
		return this.getRelationByDestionation(
				exportedAPI.OpcatConstants.EXHIBITION_RELATION).elements()
				.hasMoreElements();
	}

	public Vector getRelationByDestionation(int relationType) {
		Vector v = new Vector();
		if (TestingAuxiliary.isThing(this.iEntry)
				|| TestingAuxiliary.isState(this.iEntry)) {
			IXConnectionEdgeEntry iEdge = (IXThingEntry) this.iEntry;
			Enumeration en = iEdge.getRelationByDestination();
			for (; en.hasMoreElements();) {
				IXRelationEntry relation = (IXRelationEntry) en.nextElement();
				if (relation.getRelationType() == relationType) {
					v.add(relation);
				}
			}
		}
		return v;
	}

	// check if the entry is a root of a relation with the given type
	public Vector getRelationBySource(int relationType) {
		Vector v = new Vector();
		if (TestingAuxiliary.isThing(this.iEntry)
				|| TestingAuxiliary.isState(this.iEntry)) {
			IXConnectionEdgeEntry iEdge = (IXThingEntry) this.iEntry;
			Enumeration en = iEdge.getRelationBySource();
			for (; en.hasMoreElements();) {
				IXRelationEntry relation = (IXRelationEntry) en.nextElement();
				if (relation.getRelationType() == relationType) {
					v.add(relation);
				}
			}
		}
		return v;
	}

	public boolean isActive() {
		return this.currentEvent.getNumberOfInstances() > 0;
	}

	// this function checks whether the entry is active
	// in the given execution context
	public boolean isActive(long executionId) {
		return this.currentEvents.containsKey(new Long(executionId));
	}

	public void setPath(String path) {
		this.currentEvent.setPath(path);
	}

	public void setPath(String path, long execId) {
		TestingEvent aEvent = (TestingEvent) this.currentEvents.get(new Long(
				execId));
		if (aEvent != null) {
			aEvent.setPath(path);
		}
	}

	public String getPath() {
		return this.currentEvent.getPath();
	}

	public String getPath(long execId) {
		TestingEvent aEvent = (TestingEvent) this.currentEvents.get(new Long(
				execId));
		if (aEvent != null) {
			return aEvent.getPath();
		}
		return "";
	}

	public void setNumberOfSpecializations(int numberOfSpecialization) {
		int oldIns = this.getNumberOfInstances()
				+ this.getNumberOfSpecializations();
		if (numberOfSpecialization < 0) {
			this.currentEvent.setNumberOfSpecializations(0);
		} else {
			this.currentEvent
					.setNumberOfSpecializations(numberOfSpecialization);
		}
		try {
			IXThingEntry iThingEntry = (IXThingEntry) this.iEntry;
			if ((this.getNumberOfInstances() + this.currentEvent
					.getNumberOfSpecializations()) > 0) {
				iThingEntry.setNumberOfMASInstances(this.getNumberOfInstances()
						+ this.currentEvent.getNumberOfSpecializations());
			} else {
				iThingEntry.setNumberOfMASInstances(1);
			}
			if ((oldIns == 0) && (numberOfSpecialization > 0)) {
				this.animate(new TestingEvent());
			}
			if ((oldIns > 0)
					&& ((this.getNumberOfInstances() + this
							.getNumberOfSpecializations()) == 0)) {
				this.stopTesting(new TestingEvent());
			}
			this.iEntry.updateInstances();
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}
	}

	public int getNumberOfSpecializations() {
		return this.currentEvent.getNumberOfSpecializations();
	}

	// get any execution id
	public long getExecutionId() {
		Enumeration en = this.currentEvents.elements();
		if (en.hasMoreElements()) {
			return ((TestingEvent) en.nextElement()).getExecutionId();
		}
		return 0;
	}

	public TestingEvent getCurrentEvent() {
		return this.currentEvent;
	}

	public TestingEvent getCurrentEvent(long executionId) {
		return (TestingEvent) this.currentEvents.get(new Long(executionId));
	}

	public void addCurrentEvent(TestingEvent aEvent) {
		// store current event
		Long execId = new Long(aEvent.getExecutionId());
		TestingEvent aEventTemp = null;
		try {
			aEventTemp = (TestingEvent) this.currentEvents.get(execId);
			aEvent.setNumberOfInstances(aEventTemp.getNumberOfInstances()
					+ aEvent.getNumberOfInstances());
			this.currentEvents.put(execId, aEvent);
		} catch (NullPointerException ex) {
			this.currentEvents.put(execId, aEvent);
		}
	}

	public void removeCurrentEvent(TestingEvent aEvent) {
		// remove current event
		TestingEvent aEventTemp = null;
		Long execId = new Long(aEvent.getExecutionId());
		try {
			aEventTemp = (TestingEvent) this.currentEvents.get(execId);
			if (TestingAuxiliary.isProcess(this.iEntry) && (aEventTemp != null)
					&& (aEventTemp.getNumberOfInstances() > 1)) {
				aEventTemp.setNumberOfInstances(aEventTemp
						.getNumberOfInstances() - 1);
				this.currentEvents.put(execId, aEventTemp);
				aEvent.setNumberOfInstances(1);
			} else {
				this.currentEvents.remove(execId);
			}
		} catch (NullPointerException ex) {
			OpcatLogger.logError(ex);
		}
	}

	public void scheduleActivation(TestingEvent actvEvent) {
		if (actvEvent.getExecutionId() == 0) {
			actvEvent.setExecutionId();
		}
		this.actvScheduler.scheduleEvent(actvEvent);
	}

	public void scheduleActivation(long currentStep, long eventStep) {
		this.actvScheduler.scheduleEvent(currentStep, eventStep);
	}

	public void scheduleTermination(TestingEvent termEvent) {
		this.termScheduler.scheduleEvent(termEvent);
	}

	public void scheduleTermination(long currentStep, long eventStep) {
		this.termScheduler.scheduleEvent(currentStep, eventStep);
	}

	public Enumeration getScheduledActivations(long step) {
		return this.actvScheduler.getScheduledEvents(step);
	}

	public Enumeration getScheduledTerminations(long step) {
		return this.termScheduler.getScheduledEvents(step);
	}

	public void removeScheduledActivation(long step) {
		this.actvScheduler.removeScheduledEvent(step);
	}

	// this function checks whether there are activations
	// scheduled after given step
	public boolean futureActivationScheduled(long currentStep, long executionId) {
		return (this.actvScheduler.futureEventsScheduled(currentStep,
				executionId) != null);
	}

	/**
	 * TODO: here you change to take the process duration from the actual OPM
	 * process or from the settings if it is not set.
	 * 
	 */
	public long getDuration() {
		// temporary function - it should be part of OPCAT interface
		if (TestingAuxiliary.isProcess(this.iEntry)) {
			if (TestingSettings.getInstance().isMIN_TIME__PROCESS_DURATION()
					&& this.isLowLevel()
					&& (java.lang.Math.round(java.lang.Math.ceil(getMinTime())) > 0)) {
				return java.lang.Math.round(java.lang.Math.ceil(getMinTime())) - 1;
			} else if (!TestingSettings.getInstance()
					.isFIXED_PROCESS_DURATION()
					&& !TestingSettings.getInstance()
							.isRANDOM_PROCESS_DURATION()) {
				return TestingSettings.getInstance().getPROCESS_DURATION()
						* TestingSettings.getInstance().getSTEP_DURATION();
			}
			if (TestingSettings.getInstance().isFIXED_PROCESS_DURATION()) {
				return TestingSettings.getInstance().getPROCESS_DURATION()
						* TestingSettings.getInstance().getSTEP_DURATION();
			}

			if (TestingSettings.getInstance().isRANDOM_PROCESS_DURATION()) {
				long temp = TestingSettings.getInstance()
						.getPROCESS_DURATION_RANGE_START()
						+ TestingAuxiliary.getRandomNumber(TestingSettings
								.getInstance().getPROCESS_DURATION_RANGE_END()
								- TestingSettings.getInstance()
										.getPROCESS_DURATION_RANGE_START());
				temp = temp * TestingSettings.getInstance().getSTEP_DURATION();
				return temp;

			}
		}

		if (TestingAuxiliary.isEvent(this.iEntry)) {

			if (TestingSettings.getInstance().isFIXED_REACTION_TIME()) {
				return TestingSettings.getInstance().getREACTION_TIME()
						* TestingSettings.getInstance().getSTEP_DURATION();
			} else {
				long temp = TestingSettings.getInstance()
						.getREACTION_TIME_RANGE_START()
						+ TestingAuxiliary.getRandomNumber(TestingSettings
								.getInstance().getREACTION_TIME_RANGE_END()
								- TestingSettings.getInstance()
										.getREACTION_TIME_RANGE_START());
				temp = temp * TestingSettings.getInstance().getSTEP_DURATION();
				return temp;
			}
		}
		return 0;
	}

	// temporary function - it should be part of OPCAT interface
	public long getMinTime() {
		if (TestingAuxiliary.isProcess(this.iEntry)) {
			IXProcessEntry iEntry = (IXProcessEntry) this.getIXEntry();
			Enumeration<Role> roles = iEntry.getRoles(MetaLibrary.TYPE_POLICY);
			long time = 0;
			for (Role role : Collections.list(roles)) {
				if (role.getOntology().isPolicy()) {
					time = translateTime(((OpmProcess) role.getThing())
							.getMinTimeActivation());
					if (time != 0)
						break;
				}
			}

			if (time != 0) {
				return time;
			} else {
				return this.translateTime(iEntry.getMinTimeActivation());
			}
		}
		if (TestingAuxiliary.isLink(this.iEntry)) {
			IXLinkEntry iEntry = (IXLinkEntry) this.getIXEntry();
			return this.translateTime(iEntry.getMinReactionTime());
		}

		if (TestingAuxiliary.isState(this.iEntry)) {
			IXStateEntry iEntry = (IXStateEntry) this.getIXEntry();
			return this.translateTime(iEntry.getMinTime());
		}
		return 0;
	}

	public long getMaxTime() {
		if (TestingAuxiliary.isProcess(this.iEntry)) {
			IXProcessEntry iEntry = (IXProcessEntry) this.getIXEntry();
			Enumeration<Role> roles = iEntry.getRoles(MetaLibrary.TYPE_POLICY);
			long time = 0;
			for (Role role : Collections.list(roles)) {
				if (role.getOntology().isPolicy()) {
					time = translateTime(((OpmProcess) role.getThing())
							.getMaxTimeActivation());
					if (time != 0)
						break;
				}
			}

			if (time != 0) {
				return time;
			} else {
				return this.translateTime(iEntry.getMaxTimeActivation());
			}
		}
		if (TestingAuxiliary.isLink(this.iEntry)) {
			IXLinkEntry iEntry = (IXLinkEntry) this.getIXEntry();
			return this.translateTime(iEntry.getMaxReactionTime());
		}

		if (TestingAuxiliary.isState(this.iEntry)) {
			IXStateEntry iEntry = (IXStateEntry) this.getIXEntry();
			return this.translateTime(iEntry.getMaxTime());
		}
		return java.lang.Long.MAX_VALUE;
	}

	private long translateTime(String timeString) {
		if (timeString.equals("infinity")) {
			return java.lang.Long.MAX_VALUE;
		}
		long l[] = { 1, 1000, 60000, 3600000, 86400000 };
		int i = 0;
		long time = 0;
		StringTokenizer st = new StringTokenizer(timeString, ";");
		for (i = 0; st.hasMoreTokens(); i++) {
			try {
				String temp = st.nextToken();
				long timeToken;
				timeToken = new Long(temp).longValue();
				if (i < 5) {
					time += timeToken * l[i];
				} else {
					time += timeToken * java.lang.Long.MAX_VALUE;
				}
			} catch (Exception e) {
				// invalid MaxTimeActivation or MinTimeActivation defined for
				// the process
				OpcatLogger.logError(e);
			}
		}
		return time;
	}

	public void clear() {
		this.actvScheduler.clear();
		this.termScheduler.clear();
		this.log.clear();
		this.currentEvent.setNumberOfInstances(0);
		this.setDefaultNumberOfInstances();
		this.currentEvents.clear();
		this.currentEvent.setNumberOfSpecializations(0);
		this.stopTesting(new TestingEvent());
	}

	public void animate(TestingEvent aEvent) {
		if (!(TestingSettings.getInstance().isSHOW_GRAPHICS())) {
			return;
		}
		Enumeration eTemp = this.iEntry.getInstances();
		for (; eTemp.hasMoreElements();) {
			IXInstance i = (IXInstance) eTemp.nextElement();
			if (TestingAuxiliary.isLink(this.iEntry)) {
				IXLinkInstance iLinkInstance = (IXLinkInstance) i;
				if (TestingAuxiliary.isEvent(this.iEntry)) {
					// TestingAuxiliary.debug(this,"Event: before
					// animateAsFlash");
					iLinkInstance.animateAsFlash();
					// TestingAuxiliary.debug(this,"Event: after
					// animateAsFlash");
				} else {
					// link, but not event
					if (aEvent.getDuration() > 0) {
						// TestingAuxiliary.debug(this,"Link: before
						// animate");
						iLinkInstance.animate(aEvent.getDuration());
						// TestingAuxiliary.debug(this,"Link: after animate");
					}
				}
			} else {
				// object, process or state
				if (TestingAuxiliary.isState(this.iEntry)
						|| TestingAuxiliary.isThing(this.iEntry)) {
					// object or process
					IXConnectionEdgeInstance iEdge = (IXConnectionEdgeInstance) i;
					// iThingInstance.getBackgroundColor());
					iEdge.animate(aEvent.getDuration());

					/**
					 * TODO: here ask the conf if to show th eurl or not?
					 */
					iEdge.animateUrl(aEvent.getDuration());
				}
			}
		}

	}

	public void stopTesting(TestingEvent aEvent) {
		Enumeration eTemp = this.iEntry.getInstances();
		for (; eTemp.hasMoreElements();) {

			if (TestingAuxiliary.isThing(this.iEntry)
					|| TestingAuxiliary.isState(this.iEntry)) {
				IXConnectionEdgeInstance i = (IXConnectionEdgeInstance) eTemp
						.nextElement();
				i.stopTesting(aEvent.getDuration());
			} else {
				if (TestingAuxiliary.isLink(this.iEntry)) {
					// stopTesting for link was not supplied.
					IXLinkInstance i = (IXLinkInstance) eTemp.nextElement();
					i.stopTesting();
				}
			}
		}
	}

	public void pauseTesting() {
		Enumeration en = this.iEntry.getInstances();
		for (; en.hasMoreElements();) {
			if (TestingAuxiliary.isLink(this.iEntry)) {
				IXLinkInstance i = (IXLinkInstance) en.nextElement();
				i.pauseTesting();
			} else {
				if (TestingAuxiliary.isState(this.iEntry)
						|| TestingAuxiliary.isThing(this.iEntry)) {
					IXConnectionEdgeInstance i = (IXConnectionEdgeInstance) en
							.nextElement();
					// temporary remarked
					i.pauseTesting();
				}
			}
		}
	}

	public void continueTesting() {
		Enumeration en = this.iEntry.getInstances();
		for (; en.hasMoreElements();) {
			if (TestingAuxiliary.isLink(this.iEntry)) {
				IXLinkInstance i = (IXLinkInstance) en.nextElement();
				i.continueTesting();
			} else {
				if (TestingAuxiliary.isState(this.iEntry)
						|| TestingAuxiliary.isThing(this.iEntry)) {
					IXConnectionEdgeInstance i = (IXConnectionEdgeInstance) en
							.nextElement();
					i.continueTesting();
				}
			}
		}
	}

	// //////////////////////////
	// CHILD-PARENT functions//
	// //////////////////////////

	// this function returns a list of parents processes (not objects)
	public Enumeration getParents() {
		Enumeration en = this.iEntry.getInstances();
		Vector<IXEntry> parents = new Vector<IXEntry>();
		if (!TestingAuxiliary.isThing(this.iEntry)) {
			// return empty enumeration
			return parents.elements();
		}
		for (; en.hasMoreElements();) {
			ThingInstance i = (ThingInstance) en.nextElement();
			ThingInstance parentInstance = i.getParentThingInstance();

			if (i.getOpd() == null)
				continue;
			if (!(i.getOpd().isView())) {
				if (parentInstance != null) {
					if (TestingAuxiliary.isProcess(parentInstance.getIXEntry())) {
						IXEntry parentEntry = parentInstance.getIXEntry();
						if (!parents.contains(parentEntry)) {
							parents.add(parentEntry);
						}
					}
				}
			}
		}
		return parents.elements();
	}

	public long getNextLevelY(IXThingInstance parentInstance, long levelY) {
		Enumeration en = parentInstance.getChildThings();
		long minY = java.lang.Long.MAX_VALUE;

		for (; en.hasMoreElements();) {
			IXThingInstance tempChildInstance = (IXThingInstance) en
					.nextElement();
			long tempChildY = tempChildInstance.getY();
			// check that this is a process
			if ((tempChildInstance.getIXEntry() instanceof IXProcessEntry)
					&& (tempChildY <= minY) && (tempChildY > (levelY + 7))) {
				if (tempChildY < minY) {
					// children.clear();
					minY = tempChildY;
				}
				// children.add(tempChildInstance);
			}
		}
		return minY;
	}

	public Enumeration getChildrenByLevel(IXThingInstance parentInstance,
			long levelY) {
		Enumeration en = parentInstance.getChildThings();
		Vector children = new Vector();
		long minY = java.lang.Long.MAX_VALUE;

		// find the Y coordinate of the next level
		// this is the minimal Y that is greater than levelY
		minY = this.getNextLevelY(parentInstance, levelY);

		// obtain all processes from this level
		// this is a temporary solution, because it is not possible
		// to set several entries exactly on the same level
		en = parentInstance.getChildThings();
		for (; en.hasMoreElements();) {
			IXThingInstance tempChildInstance = (IXThingInstance) en
					.nextElement();
			long tempChildY = tempChildInstance.getY();

			// check that this is a process

			if (!(tempChildInstance.getIXEntry() instanceof IXProcessEntry)) {
				continue;
			}

			// IXProcessEntry temp = null;
			// try {
			// temp = (IXProcessEntry) tempChildInstance.getIXEntry();
			// } catch (ClassCastException e) {
			// // if not a process continue to a next child
			// continue;
			// }
			int range = Integer.parseInt(Configuration.getInstance()
					.getProperty("simulation_level_range"));

			if ((tempChildY <= (minY + range)) && (tempChildY >= minY)) {
				children.add(tempChildInstance);
			}
		}
		return children.elements();
	}

	// Returns all childern located on the next level after given child
	// on the zoomed in process
	public Enumeration getNextLevelChildren(IXThingInstance childInstance) {
		return this.getChildrenByLevel(
				childInstance.getParentIXThingInstance(), childInstance.getY());
	}

	public Enumeration getFirstLevelChildren(IXThingInstance parentInstance) {
		return this
				.getChildrenByLevel(parentInstance, java.lang.Long.MIN_VALUE);
	}

	// return true if the following conditions hold:
	// 1. process has no children processes
	// (i.e. is not zoomed in, or zoomed in without children processes)
	// 2. process has parent and it was activated in parent's context
	public boolean isLowLevel() {
		if (TestingAuxiliary.isProcess(this.iEntry)) {
			/*
			 * TestingAuxiliary.debug(this,"this.getParents().hasMoreElements() = " +
			 * this.getParents().hasMoreElements());
			 * TestingAuxiliary.debug(this,"currentEvent.getParentProcess() = " +
			 * currentEvent.getParentProcess());
			 * TestingAuxiliary.debug(this,"isLowLevel returns " +
			 * (!this.getChildrenProcesses().hasMoreElements() &&
			 * (!this.getParents().hasMoreElements() ||
			 * currentEvent.getParentProcess() != null))); return
			 * (!this.getChildrenProcesses().hasMoreElements() &&
			 * (!this.getParents().hasMoreElements() ||
			 * currentEvent.getParentProcess() != null));
			 */

			return (this.getChildrenProcesses().isEmpty());
		}
		return true;
	}

	public Vector getChildrenProcesses() {
		// return this.getChildrenByClass("gui.projectStructure.ProcessEntry");
		Vector<Entry> results = new Vector<Entry>();
		if (!TestingAuxiliary.isProcess(this.iEntry)) {
			return results;
		}
		String className = "gui.projectStructure.ProcessEntry";
		// get all sub processes
		HashMap<Entry, FundamentalRelationEntry> aggChildren = ((ProcessEntry) iEntry)
				.getFundemantulRelationSons(OpcatConstants.AGGREGATION_RELATION);
		Iterator<Entry> entries = aggChildren.keySet().iterator();

		while (entries.hasNext()) {
			Entry entry = entries.next();
			if (entry.getClass().getName().equalsIgnoreCase(className)) {
				results.add(entry);
			}
		}
		return results;

	}

	public Vector getChildrenObjects() {
		// return this.getChildrenByClass("gui.projectStructure.ObjectEntry");
		Vector<Entry> results = new Vector<Entry>();
		if (!TestingAuxiliary.isProcess(this.iEntry)) {
			return results;
		}
		String className = "gui.projectStructure.ObjectEntry";
		// get all sub objects
		HashMap<Entry, FundamentalRelationEntry> aggChildren = ((ProcessEntry) iEntry)
				.getFundemantulRelationSons(OpcatConstants.EXHIBITION_RELATION);
		Iterator<Entry> entries = aggChildren.keySet().iterator();

		while (entries.hasNext()) {
			Entry entry = entries.next();
			if (entry.getClass().getName().equalsIgnoreCase(className)) {
				results.add(entry);
			}
		}
		return results;
	}

	// private Vector getChildrenByClass(String className) {
	// Vector<Entry> results = new Vector<Entry>();
	// if (!TestingAuxiliary.isProcess(this.iEntry)) {
	// return results;
	// }
	//
	// // get all sub processes
	// HashMap<Entry, FundamentalRelationEntry> aggChildren = ((ProcessEntry)
	// iEntry)
	// .getFundemantulRelationSons(OpcatConstants.AGGREGATION_RELATION);
	// Iterator<Entry> entries = aggChildren.keySet().iterator();
	//
	// while (entries.hasNext()) {
	// Entry entry = entries.next();
	// if (entry.getClass().getName().equalsIgnoreCase(className)) {
	// results.add(entry);
	// }
	// }
	// return results;
	// }

	// this function is used for undo - it restores the entry from log
	// it updates the number of instances and the testing status
	public void restore(long fromStep, long toStep) {
		if (TestingAuxiliary.isLink(this.iEntry)) {
			if (fromStep > toStep) {
				// remove from the log all events that occured after toStep
				this.log.removeAllScheduledEvents(toStep, 0);
				// remove scheduled future events
				this.actvScheduler.removeAllScheduledEvents(toStep, 0);
				this.termScheduler.removeAllScheduledEvents(toStep, 0);
				return;
			}
		}
		// fromStep should be greater than toStep
		if (fromStep > toStep) {
			// find all events that occured after the new step
			int numOfInstances = this.getNumberOfInstances();
			for (long step = fromStep; step > toStep; step--) {
				Enumeration enEvents = this.log.getScheduledEvents(step);
				for (; enEvents.hasMoreElements();) {
					TestingEvent aEvent = (TestingEvent) enEvents.nextElement();
					// restore number of instances
					// if event is of activation type
					if (aEvent.getEventType() == TestingSettings.getInstance()
							.getACTIVATION_EVENT()) {
						numOfInstances -= aEvent.getNumberOfInstances();
						// remove the corresponding entry from currentEvents
						// this.currentEvents.remove(new
						// Long(aEvent.getExecutionId()));
						this.removeCurrentEvent(aEvent);
					} else {
						numOfInstances += aEvent.getNumberOfInstances();
						// find the corresponding activation event and add it to
						// currentEvents list
						TestingEvent actvEvent = this.log
								.futureEventsScheduled(0, aEvent
										.getExecutionId());
						if (actvEvent != null) {
							// this.currentEvents.put(new
							// Long(aEvent.getExecutionId()), new
							// TestingEvent(actvEvent));
							this.addCurrentEvent(new TestingEvent(actvEvent));
						}
					}
				}
			}

			// restore the testing in accordance with number of instances
			this.setNumberOfInstances(numOfInstances);
			if (numOfInstances > 0) {
				this.animate(new TestingEvent());
			} else {
				this.stopTesting(new TestingEvent());
			}
			// remove from the log all events that occured after toStep
			this.log.removeAllScheduledEvents(toStep, 0);
			// remove scheduled future events
			this.actvScheduler.removeAllScheduledEvents(toStep, 0);
			this.termScheduler.removeAllScheduledEvents(toStep, 0);
		}
	}

	public String getTaxtualInfo() {
		return this.taxtualInfo;
	}

	/**
	 * sets the text info, if sevirity is less then current and if there is no
	 * info the same we try to set
	 * 
	 * @param taxtualInfo
	 * @param sevirityIndex
	 */
	public void setTaxtualInfo(String taxtualInfo, int sevirityIndex) {
		// if (this.sevirityIndex < sevirityIndex) {
		if (this.taxtualInfo.indexOf(taxtualInfo) < 0) {
			this.taxtualInfo = this.taxtualInfo + " " + taxtualInfo;
		}
		// }
	}

	public boolean isEventLinkActivated() {
		return this.isEventLinkActivated;
	}

	public void setEventLinkActivated(boolean isEventLinkActivated) {
		this.isEventLinkActivated = isEventLinkActivated;
	}

	/**
	 * 
	 * return 0 if no exeption return 1 if abuve max return -1 if below min
	 * 
	 * @return
	 */

	public int isMeasurementUnitExeption() {
		OpmObject opm = (OpmObject) ((Entry) this.getIXEntry())
				.getLogicalEntity();

		if (getMeasurementUnitAcommulatedValue() >= opm
				.getMesurementUnitMaxValue()) {
			return MaxResourceExeption;
		}

		if (getMeasurementUnitAcommulatedValue() <= opm
				.getMesurementUnitMinValue()) {
			return MinResourceExeption;
		}

		return NoResourceExeption;
	}

	public void setSavedState(StateEntry state) {
		this.savedState = state;
	}

	public StateEntry getSavedState() {
		return this.savedState;
	}

	public double getMeasurementUnitAcommulatedValue() {
		return measurementUnitAcommulatedValue;
	}

	public void setMeasurementUnitAcommulatedValue(
			double measurementUnitAcommulatedValue) {
		this.measurementUnitAcommulatedValue = measurementUnitAcommulatedValue;
	}

	public boolean isOnLine() {
		return online;
	}

	public void setOnLine(boolean animated) {
		this.online = animated;
	}
}
