package extensionTools.Testing;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.TimerTask;
import java.util.Vector;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import exportedAPI.opcatAPIx.IXConnectionEdgeEntry;
import exportedAPI.opcatAPIx.IXEntry;
import exportedAPI.opcatAPIx.IXInstance;
import exportedAPI.opcatAPIx.IXLinkEntry;
import exportedAPI.opcatAPIx.IXObjectEntry;
import exportedAPI.opcatAPIx.IXObjectInstance;
import exportedAPI.opcatAPIx.IXOpd;
import exportedAPI.opcatAPIx.IXProcessEntry;
import exportedAPI.opcatAPIx.IXProcessInstance;
import exportedAPI.opcatAPIx.IXRelationEntry;
import exportedAPI.opcatAPIx.IXStateEntry;
import exportedAPI.opcatAPIx.IXStateInstance;
import exportedAPI.opcatAPIx.IXSystem;
import exportedAPI.opcatAPIx.IXThingEntry;
import exportedAPI.opcatAPIx.IXThingInstance;
import gui.controls.GuiControl;
import gui.opdProject.Opd;
import gui.opmEntities.OpmObject;
import gui.projectStructure.Entry;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;
import gui.util.OpcatLogger;

public class TestingSystem extends Object implements Testing {

	private Hashtable debugEvents = new Hashtable();

	private boolean bManualActivated = false;

	private IXSystem mySys;

	private IXSystem bkpSys;

	private static Vector<TestingEntry> testingElements = new Vector<TestingEntry>();

	IXOpd lastShownOPD = null;

	public long currentStep = 0;

	TestingTimer testingTimer;

	TestingStatusBar guiPanel = null;

	Stack<Long> opdStack = new Stack<Long>();

	private TestingLifeSpanGrid lifeSpanGrid = null;

	private TestingResourceGrid resourceGrid = null;

	private TestingGridPanel testingPanel;

	// private TestingScenriosPanel objectsScenrios;

	static private boolean hasTerminations = false;

	static private boolean hasActivation = false;

	private TestingSystemData stepsData = new TestingSystemData();

	// ////////////////// initiation ////////////////////////

	public TestingSystem(IXSystem mySys, TestingStatusBar guiPanel) {
		this.mySys = mySys;
		this.bkpSys = mySys;
		this.guiPanel = guiPanel;
		guiPanel.showTestingStatus(0, 0);
		this.testingPanel = this.InitTestingGridPanel();
		testingPanel.setTabName(TestingAuxiliary.TestingPanelName);
		this.testingPanel.AddToExtensionToolsPanel();

		// objectsScenrios = InitObjectScnerios();
		// this.objectsScenrios.AddToExtensionToolsPanel();
	}

	private void createTestingElements() {
		Enumeration en;

		if (!TestingSystem.testingElements.isEmpty()) {
			TestingSystem.testingElements.clear();
		}
		en = this.mySys.getIXSystemStructure().getElements();
		for (; en.hasMoreElements();) {
			IXEntry iEntry = (IXEntry) en.nextElement();
			// create only things, states and links (but not relations)
			// since relations are not relevant for testing
			if (TestingAuxiliary.isThing(iEntry)
					|| TestingAuxiliary.isState(iEntry)
					|| TestingAuxiliary.isLink(iEntry)) {
				TestingSystem.testingElements.addElement(new TestingEntry(
						iEntry));
			}
		}
	}

	public boolean isTestingPaused() {
		if (testingTimer == null) {
			return false;
		} else {
			return testingTimer.isPause();
		}
	}

	public boolean canFarward() {
		return this.isTestingPaused();
	}

	public boolean canBackward() {
		return (this.isTestingPaused() && (this.currentStep > 0));
	}

	public synchronized void testingStart() {
		if (testingTimer == null) {
			this.currentStep = 0;
			this.createTestingElements();

			if (lifeSpanGrid == null) {
				lifeSpanGrid = new TestingLifeSpanGrid(this);
			} else {
				if (!lifeSpanGrid.isPanelNull()) {
					lifeSpanGrid.ClearData();
					lifeSpanGrid.RemoveFromExtensionToolsPanel();
				}
			}

			if (resourceGrid == null) {
				resourceGrid = new TestingResourceGrid(this);
			} else {
				if (!resourceGrid.isPanelNull()) {
					resourceGrid.ClearData();
					resourceGrid.RemoveFromExtensionToolsPanel();
				}
			}

			this.testingTimer = new TestingTimer(this);
		}
	}

	public synchronized void testingStop() {
		if (this.testingTimer != null) {
			this.testingTimer.cancel();
		}
		this.deactivate();
		this.guiPanel.showTestingStatus(0, 0);
		this.mySys = null;
		this.testingTimer = null;
		this.mySys = this.bkpSys;
		this.testingPanel.RestoreThingOrigColor();

		if (TestingSettings.getInstance().isSHOW_RESOURCE()) {
			resourceGrid.showGrid(stepsData);
		}

		if (TestingSettings.getInstance().isSHOW_LIFE_SPAN()) {
			lifeSpanGrid.showGrid(stepsData);
		}

		GuiControl.getInstance().setCursor4All(Cursor.DEFAULT_CURSOR);

	}

	public synchronized void testingPause() {
		if (this.testingTimer != null) {
			this.testingTimer.testingPause();
		}
		// pause all testing effects
		Enumeration en = TestingSystem.testingElements.elements();
		for (; en.hasMoreElements();) {
			((TestingEntry) en.nextElement()).pauseTesting();
		}

		if (TestingSettings.getInstance().isSHOW_RESOURCE()) {
			resourceGrid.showGrid(stepsData);
		}

		if (TestingSettings.getInstance().isSHOW_LIFE_SPAN()) {
			lifeSpanGrid.showGrid(stepsData);
		}

		GuiControl.getInstance().setCursor4All(Cursor.DEFAULT_CURSOR);

	}

	public synchronized void testingContinue() {

		GuiControl.getInstance().setCursor4All(Cursor.WAIT_CURSOR);

		try {
			this.testingTimer.testingContinue();
			// continue all testing effects
			Enumeration en = TestingSystem.testingElements.elements();
			for (; en.hasMoreElements();) {
				((TestingEntry) en.nextElement()).continueTesting();
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

	}

	public synchronized void testingActivate() {

		if (testingTimer == null) {
			return;
		}
		bManualActivated = true;

		if (isTestingPaused()) {
			// restart if we where in a pause
			this.testingContinue();
		}
		Enumeration en = this.mySys.getCurrentIXOpd().getSelectedItems();
		for (; en.hasMoreElements();) {
			// this.hasNotingToDO = false ;
			// activate selected items
			IXEntry iEntry = ((IXInstance) en.nextElement()).getIXEntry();
			TestingEntry aEntry = this.findEntryById(iEntry.getId());
			// objects can be activated without preconditions
			// other entries should be checked before the activation
			if (this.checkBeforeActivate(aEntry, null)
					|| TestingAuxiliary.isObject(aEntry.getIXEntry())
					|| TestingAuxiliary.isState(aEntry.getIXEntry())) {
				this.activate(aEntry);
				// this.hasNotingToDO = false ;
			}
		}
		// continue one step forward after manual activate
		if (TestingSettings.getInstance().isSTEP_BY_STEP_MODE()) {
			this.testingForward(1);
		}

		bManualActivated = false;
	}

	public synchronized void testingDeactivate() {

		Enumeration en = this.mySys.getCurrentIXOpd().getSelectedItems();
		for (; en.hasMoreElements();) {
			// deactivate selected items
			IXEntry iEntry = ((IXInstance) en.nextElement()).getIXEntry();
			this.deactivate(this.findEntryById(iEntry.getId()));
		}
	}

	public synchronized void testingForward(long numberOfSteps) {

		GuiControl.getInstance().setCursor4All(Cursor.WAIT_CURSOR);

		// if application is not running - return
		if ((this.testingTimer == null)) {
			return;
		}
		// exit pause mode
		this.testingContinue();

		// schedule pause after numberOfSteps steps
		// try {
		this.testingTimer.schedule(new TimerTask() {
			public void run() {
				TestingSystem.this.testingPause();
			}
		}, numberOfSteps * TestingSettings.getInstance().getSTEP_DURATION());
		// } catch (IllegalStateException e) {
		// this.testingTimer = new TestingTimer(this);
		// }
	}

	public synchronized void testingBackward(long numberOfSteps) {

		// validate number of steps
		if (numberOfSteps >= this.currentStep) {
			// return to the starting point
			numberOfSteps = this.currentStep;
			this.deactivate();
		} else {
			Enumeration en = TestingSystem.testingElements.elements();
			for (; en.hasMoreElements();) {
				TestingEntry aEntry = (TestingEntry) en.nextElement();
				int numOfIns = aEntry.getNumberOfInstances();
				// restore the number of instances and the testing status of
				// the entry itself
				aEntry.restore(this.currentStep, this.currentStep
						- numberOfSteps);
				// update the generalization
				// check how many instances were added/removed from this entry
				if (TestingAuxiliary.isThing(aEntry.getIXEntry())) {
					numOfIns = numOfIns - aEntry.getNumberOfInstances();
					if (numOfIns != 0) {
						TestingEvent aEventTemp = new TestingEvent();
						if (numOfIns > 0) {
							aEventTemp.setEventType(TestingSettings
									.getInstance().getTERMINATION_EVENT());
						} else {
							aEventTemp.setEventType(TestingSettings
									.getInstance().getACTIVATION_EVENT());
						}
						if (numOfIns < 0) {
							numOfIns = (-numOfIns);
						}
						aEventTemp.setNumberOfInstances(numOfIns);
						this.treatGeneralization((IXThingEntry) aEntry
								.getIXEntry(), aEventTemp);
					}
				}
				// if the restored entry is a process and it is active after the
				// restore
				// animate its consumption and result links.
				if (TestingAuxiliary.isProcess(aEntry.getIXEntry())
						&& (aEntry.getNumberOfInstances() > 0)) {
				}
			}
		}
		this.retreatStep(numberOfSteps);

		for (int i = 0; i < numberOfSteps; i++) {
			stepsData.removeStep(stepsData.keySet().size());
		}

		if (TestingSettings.getInstance().isSHOW_RESOURCE()) {
			resourceGrid.showGrid(stepsData);
		}

		if (TestingSettings.getInstance().isSHOW_LIFE_SPAN()) {
			lifeSpanGrid.showGrid(stepsData);
		}
	}

	// ////////////////// activate ////////////////////////

	public synchronized void activate(TestingEntry aEntry) {
		this.activate(aEntry, null);
	}

	public synchronized void activate(TestingEntry aEntry, TestingEvent aEvent) {
		// activate
		aEvent = this.activateWithoutConsequences(aEntry, aEvent);
		// treat consequences
		this.treatActivationConsequences(aEntry, aEvent);
	}

	public static TestingEntry getTestingElementByIXEntryID(long ixEntryID) {

		if (testingElements == null) {
			return null;
		}

		Iterator testingEnum = testingElements.iterator();
		while (testingEnum.hasNext()) {
			TestingEntry entry = (TestingEntry) testingEnum.next();
			if (entry.getIXEntry().getId() == ixEntryID) {
				return entry;
			}
		}
		return null;
	}

	// this function just activates the the entry
	// without treating consequences
	// this function is used when consequences should not be treated
	// immediately: in activateLevel function, where we first activate
	// all processes on certain level and then treat consequences of each
	// one.

	private TestingEvent activateWithoutConsequences(TestingEntry aEntry,
			TestingEvent aEvent) {
		if (aEntry != null) {
			if (aEvent == null) {
				aEvent = new TestingEvent();
			}
			// set scheduling step and step (this is requried for undo)
			aEvent.setSchedulingStep(this.currentStep);
			aEvent.setStep(this.currentStep);
			aEvent.setEventType(TestingSettings.getInstance()
					.getACTIVATION_EVENT());
			// for children processes: set parent and executionId, if it
			// still
			// empty
			this.setProcessContext(aEntry, aEvent);
			// activate
			aEntry.activate(aEvent);
		}
		return aEvent;
	}

	// this function activates child processes located on the same level
	// inside a zoomed in process. It implements the following flow:
	// For each process on the given level:
	// 1. check whether process can be activated
	// 2. if it can not be activated due to missing condition - continue
	// 3. if it can not be activated due to another reason -
	// schedule another activation to the next step (the system will try to
	// activate
	// this process until it will succeed.
	// 4. if the process passed the validations - activate it.
	// If no activations were performed or scheduled (this can happen if
	// all the processes on this level could not be activated due to
	// condition),
	// then the function recursively calls to itself to activate for the
	// next
	// level
	// the function returns true if it activates at least one child process
	// and false otherwise
	private boolean activateLevel(Enumeration enChildProcesses,
			TestingEvent aInputChildEvent) {
		boolean proceedToNextLevel = true;
		TestingEntry aChildProcess = null;
		Vector activatedProcesses = new Vector();
		Vector activatedEvents = new Vector();
		for (; enChildProcesses.hasMoreElements();) {
			TestingEvent aChildEvent = new TestingEvent(aInputChildEvent);
			IXProcessInstance childProcessInstance = (IXProcessInstance) enChildProcesses
					.nextElement();
			aChildProcess = this.findEntryById(childProcessInstance
					.getIXEntry().getId());
			// ignore instantion/feature/specialization/particulation
			// processes
			if (this.isRelatedProcess(aChildProcess, aChildEvent
					.getParentProcess())) {
				continue;
			}
			// set the indication that the process is to be activated in
			// parents
			// context
			TestingEntry oldParent = aChildProcess.getCurrentEvent()
					.getParentProcess();
			aChildProcess.getCurrentEvent().setParentProcess(
					aChildEvent.getParentProcess());
			// check general validations before process activation
			// child process can be activated automatically
			// only if there is no events linked to it
			/**
			 * Raanan : event is not a blocking thingy.
			 * 
			 */
			boolean eventsFound = false;
			// this.findEvents(aChildProcess, aChildEvent
			// .getParentProcess());

			aChildProcess.getCurrentEvent().setWaitForEvent(true);

			if (!eventsFound
					&& this.checkProcessBeforeActivate(aChildProcess, null)) {
				aChildProcess.getCurrentEvent().setWaitForEvent(false);
				// activate the process if it is valid
				this.activateWithoutConsequences(aChildProcess, aChildEvent);
				activatedProcesses.add(aChildProcess);
				activatedEvents.add(aChildEvent);
				proceedToNextLevel = false;
			} else {
				aChildProcess.getCurrentEvent().setWaitForEvent(false);
				// if there is an unavalaible condition link
				// other processes should not wait to this process
				// otherwise schedule future activation until first successful
				// activation
				if (!this.findUnavailableCondition(aChildProcess)) {
					// the process should be activated by event, not when
					// handling the
					// future request, therefore number of instances is 0
					// future event is scheduled only to indicate that
					// parent
					// process can not terminate yet
					if (eventsFound) {
						aChildEvent.setNumberOfInstances(0);
						// schedule future activation to current step (without
						// recurring ind)
						// this is necessary if event will activate the process
						// already
						// on current step
						TestingEvent aChildEventTemp = new TestingEvent(
								aChildEvent);
						aChildEventTemp.setRecurringInd(false);
						aChildEventTemp.setStep(this.currentStep);
						aChildEventTemp.setSchedulingStep(this.currentStep);
						aChildProcess.scheduleActivation(aChildEventTemp);
					}
					// schedule future activation to next step with
					// recurring
					// ind
					aChildEvent.setRecurringInd(true);
					aChildEvent.setStep(this.currentStep + 1);
					aChildEvent.setSchedulingStep(this.currentStep);
					// this was added to solve conditions links problem.
					// aChildEvent.setNumberOfInstances(aChildProcess.getNumberOfInstances());
					aChildProcess.scheduleActivation(aChildEvent);
					proceedToNextLevel = false;
				}
				// if process can not be activated restore its parent process
				aChildProcess.getCurrentEvent().setParentProcess(oldParent);
			}
		} // end for

		// go through the list of activated processes and
		// treatActivationConsequences
		// for each one. The separation between activate and treat consequences
		// here is required to ensure that all processes activated. Otherwise,
		// it can happen that one of activated process terminates immediately
		// (if
		// neither of its children can be executed due to unavailable condition)
		// and then parent might be cancelled before it tries to activate other
		// children.
		for (int i = 0; i < activatedProcesses.size(); i++) {
			this.treatProcessActivationConsequences(
					(TestingEntry) activatedProcesses.get(i),
					(TestingEvent) activatedEvents.get(i));
		}

		// if nothing was activated on this level, and no future activations
		// were scheduled, although the level is not empty -
		// proceed immediately to the next level
		if ((proceedToNextLevel) && (aChildProcess != null)) {
			this.treatChildProcessTerminationConsequences(aChildProcess,
					new TestingEvent(aInputChildEvent));
			return true;
		}

		// if there are no more children - the process is the last child process
		// terminate the parent
		if (aChildProcess == null) {
			return false;
		}

		return true;
	}

	// an auxiliary function that ascribes a child process activated by
	// event to some execution of its parent.
	private void setProcessContext(TestingEntry aEntry, TestingEvent aEvent) {
		if (TestingAuxiliary.isProcess(aEntry.getIXEntry())
				&& aEntry.getParents().hasMoreElements()
				&& (aEvent.getExecutionId() == 0)) {
			aEvent
					.setParentProcess(aEntry.getCurrentEvent()
							.getParentProcess());
			aEvent.setExecutionId(aEntry.getCurrentEvent().getExecutionId());
			// aEntry.getCurrentEvent(aEntry.getCurrentEvent().getExecutionId())
		}
	}

	// ////////////////// deactivate ////////////////////////

	// this function deactivates all testing entries
	public void deactivate() {

		Enumeration en = TestingSystem.testingElements.elements();
		for (; en.hasMoreElements();) {
			TestingEntry aEntry = (TestingEntry) en.nextElement();
			TestingEvent aEvent = new TestingEvent();
			aEvent.setNumberOfInstances(aEntry.getNumberOfInstances());
			aEvent.setStep(this.currentStep);
			aEvent.setEventType(TestingSettings.getInstance()
					.getTERMINATION_EVENT());
			aEntry.deactivate(aEvent);
			aEntry.clear();
		}
		this.opdStack.clear();
		TestingSettings.getInstance().setLAST_EXECUTION_ID(0);
	}

	// this function deactivates specific testing entry
	public void deactivate(TestingEntry aEntry) {
		if (aEntry == null) {
			return;
		}
		if (aEntry.isActive()) {
			this.deactivate(aEntry, new TestingEvent());
		}
	}

	// this function deactivates specific testing entry
	// according to the settings passed in TestingEvent object
	public void deactivate(TestingEntry aEntry, TestingEvent aEvent) {
		if (aEntry != null) {
			if (aEvent == null) {
				aEvent = new TestingEvent();
			}
			// set scheduling step and step (this is requried for undo)
			if (aEvent.getSchedulingStep() == 0) {
				aEvent.setSchedulingStep(this.currentStep);
			}
			if (aEvent.getStep() == 0) {
				aEvent.setStep(this.currentStep);
			}
			// deactivate
			aEntry.deactivate(aEvent);
			// treat consequences
			this.treatTerminationConsequences(aEntry, aEvent);

		}
	}

	// ////////////////// find ////////////////////////

	// this function searches for a entry with the given id
	private TestingEntry findEntryById(long entryId) {
		Enumeration en = TestingSystem.testingElements.elements();
		TestingEntry abstractEntry;
		long id;
		for (; en.hasMoreElements();) {
			abstractEntry = (TestingEntry) en.nextElement();
			id = abstractEntry.getIXEntry().getId();
			if (id == entryId) {
				return abstractEntry;
			}
		}
		return null;
	}

	private void advanceStep() {
		this.currentStep++;
		this.guiPanel.showTestingStatus(1, this.currentStep);
	}

	private void retreatStep(long numberOfSteps) {
		this.currentStep -= numberOfSteps;
		this.guiPanel.showTestingStatus(2, this.currentStep);
	}

	// ////////////////// first step ////////////////////////
	// this function is called when the testing starts running
	// it initiates the objects according to object initiation rules
	public void firstStep() {
		// return if pause mode
		if (isTestingPaused()) {
			return;
		}
		stepsData = new TestingSystemData();
		this.debugEvents = new Hashtable();
		Enumeration en = TestingSystem.testingElements.elements();
		TestingEntry aEntry;
		this.advanceStep();
		for (; en.hasMoreElements();) {
			aEntry = (TestingEntry) en.nextElement();
			// initially objects and states are activated without any
			// trigger
			if (TestingSettings.getInstance().isAUTOMATIC_INITIATION()) {
				if (TestingAuxiliary.isObject(aEntry.getIXEntry())) {
					// activate the object only if it is not inside a zoomed
					// in
					// process
					if (!aEntry.getParents().hasMoreElements()) {
						this.initiateObject(aEntry);
					}
				}
			}
		}

		stepsData.addStep(this, 1);

		// if (TestingSettings.SHOW_LIFE_SPAN)
		// lifeSpanGrid.NextStep(mySys, 1);
		//
		// if (TestingSettings.SHOW_RESOURCE)
		// resourceGrid.NextStep(mySys, 1);
		//
	}

	// this function checks whether the object can be activated
	// according to the initial activation rules
	// and activates it if it passes the validation
	private void initiateObject(TestingEntry aObject) {
		if (this.checkObjectBeforeActivate(aObject)) {
			// object is initiated with the default number of instances
			// which is specified in MAS folder
			int insNum = aObject.getDefaultNumberOfInstances();
			if (insNum <= 1) {
				if (TestingSettings.getInstance().isONE_OBJECT_INSTANCE()) {
					insNum = 1; // replace with default
				} else {
					insNum = TestingSettings.getInstance()
							.getMAX_OBJECT_INSTANCES(); // replace
					// with
					// default
				}
			}
			TestingEvent actvEvent = new TestingEvent();
			actvEvent.setStep(this.currentStep);
			actvEvent.setNumberOfInstances(insNum);
			this.activate(aObject, actvEvent);
		}
	}

	private void testingChecking(TestingEvent tempEvent,
			TestingEntry tempEntry, String testingAction) {
		try {
			if ((this.debugEvents.size() > 0)
					&& (this.debugEvents.get(new Long(tempEvent
							.getExecutionId())) != null)) {
				this.TestingGridAddRow(testingAction, tempEntry, tempEvent);
			} else {
				this.debugEvents.put(new Long(tempEvent.getExecutionId()),
						tempEvent);
			}
		} catch (Exception e) {
			// APILogger.SetDebug(true) ;
			OpcatLogger.logError(e);
		}
	}

	// ////////////////// next step ////////////////////////
	// this function is called whenever testing performes a recomputation
	// consistent with the configured step duration
	// it goes through the list of terminations scheduled to the current
	// step
	// and performs the events that pass the validations
	// then it treats the activations scheduled to the current step
	public void nextStep() {
		// if testing is paused - do nothing or there is nothing to do
		if (isTestingPaused()) {
			return;
		}

		// if (lifeSpanGrid.getPanel().isOnExtensionToolsPane())
		// lifeSpanGrid.NextStep(mySys, currentStep);

		// if (currentStep == 1 ) stepsData.addStep(this, currentStep);

		Enumeration en = TestingSystem.testingElements.elements();
		TestingEntry aEntry;
		this.advanceStep();
		hasTerminations = false;
		hasActivation = false;

		// go through the testing elements
		// and retrieve a list of scheduled events (first terminations and then
		// activations for each element
		for (; en.hasMoreElements();) {
			aEntry = (TestingEntry) en.nextElement();
			// get scheduled terminations
			Enumeration termEvents = aEntry
					.getScheduledTerminations(this.currentStep);
			if (!hasTerminations) {
				hasTerminations = termEvents.hasMoreElements();
			}
			for (; termEvents.hasMoreElements();) {
				// treat termination event
				TestingEvent currentEvent = (TestingEvent) termEvents
						.nextElement();

				// deactivate the entry if the event is not cancelled and the
				// entry
				// passes termination conditions
				if ((currentEvent.getCancelStep() == 0)
						&& this.checkBeforeTerminate(aEntry, currentEvent)) {
					this.deactivate(aEntry, currentEvent);
				} else {
					// check event if need to be printed in the log
					if (currentEvent.getCancelStep() != 0) {
						// event cancled
						aEntry.setTaxtualInfo(
								"Event Cancled by Time Limits in Step - "
										+ currentEvent.getCancelStep(), 5);
					}
					this.testingChecking(currentEvent, aEntry,
							TestingAuxiliary.ActionTerminate);
				}
			}
			// get scheduled activations
			Enumeration actvEvents = aEntry
					.getScheduledActivations(this.currentStep);
			if (!hasActivation) {
				hasActivation = actvEvents.hasMoreElements();
			}
			for (; actvEvents.hasMoreElements();) {
				TestingEvent currentEvent = (TestingEvent) actvEvents
						.nextElement();

				synchronized (currentEvent) {
					// set the indication that the process is to be
					// activated in
					// parents context
					TestingEntry oldParent = aEntry.getCurrentEvent()
							.getParentProcess();
					aEntry.getCurrentEvent().setParentProcess(
							currentEvent.getParentProcess());
					// activate the entry if the event is not cancelled and
					// the
					// entry
					// passes activation conditions
					// number of instances 0 indicates waiting for event
					// or we are in the wrong OPD
					// (child
					// process
					// inside a zoomed in OPD can be activated only by
					// event,
					// therefore
					// next step ignores it).
					Vector linkTypes = new Vector();
					linkTypes.add(new Integer(
							exportedAPI.OpcatConstants.RESULT_LINK));
					linkTypes.add(new Integer(
							exportedAPI.OpcatConstants.INVOCATION_LINK));
					linkTypes.add(new Integer(
							exportedAPI.OpcatConstants.CONSUMPTION_EVENT_LINK));
					linkTypes.add(new Integer(
							exportedAPI.OpcatConstants.CONSUMPTION_LINK));

					if ((currentEvent.getCancelStep() == 0)
							&& (currentEvent.getNumberOfInstances() > 0)
							&& this.checkBeforeActivate(aEntry, currentEvent)) {
						currentEvent.setWaitForEvent(false);
						this.activate(aEntry, currentEvent);
					} else {
						if (currentEvent.getCancelStep() != 0) {
							aEntry.setTaxtualInfo("Event cancled in Step - "
									+ currentEvent.getCancelStep(), 5);
						}

						if (currentEvent.getNumberOfInstances() == 0) {
							// /**
							// * find entry OPD
							// */
							// boolean found = false ;
							// Enumeration opdEnum =
							// mySys.getIXSystemStructure().getOpds() ;
							// IXOpd opd = null ; ;
							// while(opdEnum.hasMoreElements()) {
							// opd = (IXOpd) opdEnum.nextElement() ;
							// if((opd.getMainIXEntry() != null) &&
							// (opd.getMainIXEntry().getId() ==
							// aEntry.getIXEntry().getId())) {
							// found = true ;
							// break ;
							// }
							// }
							if (currentEvent.getEventType() == TestingSettings
									.getInstance().getACTIVATION_EVENT()) {
								if (aEntry.futureActivationScheduled(
										currentStep, currentEvent
												.getExecutionId())) {
									aEntry
											.setTaxtualInfo(
													"Could not activate "
															+ aEntry
																	.getIXEntry()
																	.getName()
															+ " which has registered activation in this step",
													5);
								} else {
									aEntry.setTaxtualInfo("Could not activate "
											+ aEntry.getIXEntry().getName(), 5);
								}
							} else {
								aEntry.setTaxtualInfo("Could not terminate "
										+ aEntry.getIXEntry().getName(), 5);
							}
						}
						// if the entry hasn't pass the validations restore
						// parent process
						aEntry.getCurrentEvent().setParentProcess(oldParent);
						// reschedule to the next step if the recurring ind is
						// checked
						if (currentEvent.getRecurringInd()) {
							TestingEvent aEvent = new TestingEvent(currentEvent);
							aEvent.setSchedulingStep(this.currentStep);
							aEvent.setStep(this.currentStep + 1);
							aEntry.scheduleActivation(aEvent);
							aEntry.setTaxtualInfo(", retry in steps ", 1);
							aEntry.setTaxtualInfo((this.currentStep + 1) + ",",
									1);
						}
						this.testingChecking(currentEvent, aEntry,
								TestingAuxiliary.ActionActivate);
					}
				} // end synchronized
			}
		}
		if (!hasActivation && !hasTerminations) {
			en = TestingSystem.testingElements.elements();
			for (; en.hasMoreElements();) {
				aEntry = (TestingEntry) en.nextElement();
				if (this.mySys.getCurrentIXOpd().getMainIXEntry() != null) {
					if (TestingAuxiliary.isProcess(aEntry.getIXEntry())) {
						if (aEntry.getIXEntry().getId() != this.mySys
								.getCurrentIXOpd().getMainIXEntry().getId()) {
							// if
							// (aEntry.getTaxtualInfo().equalsIgnoreCase(""))
							// {
							// aEntry
							// .setTaxtualInfo(
							// "Inconsistent Model, Parent Process does
							// nothing Or not consistent with upper levels
							// ?",
							// 1);
							// }
						} else {
							aEntry.setTaxtualInfo(
									"Waiting for agent or event or time limit",
									1);
							this
									.TestingGridAddRow(
											TestingAuxiliary.ActionGlobel,
											aEntry, null);
						}
					}
				}
			}
		}
		stepsData.addStep(this, currentStep);

		// if (TestingSettings.SHOW_LIFE_SPAN)
		// lifeSpanGrid.NextStep(mySys, currentStep);
		//
		// if (TestingSettings.SHOW_RESOURCE)
		// resourceGrid.NextStep(mySys, currentStep);
	}

	// ////////////////// validations before activation
	// ////////////////////////
	private boolean checkBeforeActivate(TestingEntry aEntry, TestingEvent aEvent) {
		if (TestingAuxiliary.isObject(aEntry.getIXEntry())) {
			return this.checkObjectBeforeActivate(aEntry);
		}

		if (TestingAuxiliary.isProcess(aEntry.getIXEntry())) {
			return this.checkProcessBeforeActivate(aEntry, aEvent);
		}

		if (TestingAuxiliary.isLink(aEntry.getIXEntry())) {
			return this.checkLinkBeforeActivate(aEntry, (IXLinkEntry) aEntry
					.getIXEntry());
		}
		return false;
	}

	// this function checks whether object is valid to be activated
	// it is called only by initiation procedure
	// An object can be activated if it is not a result
	// and if it is not a feature of another object
	private boolean checkObjectBeforeActivate(TestingEntry aEntry) {
		// result can't be active unless it is activated as a consequence
		IXObjectEntry iObject = (IXObjectEntry) aEntry.getIXEntry();
		if (this.isResult(iObject)) {
			aEntry.setTaxtualInfo("Object is a Result", 10);
			return false;
		}
		// if the object is a feature of another object it can not be activated
		// unless as a consequence of root object activation
		if (aEntry.isFeature()) {
			aEntry.setTaxtualInfo("Object is a Feature", 10);
			return false;
		}
		aEntry.setTaxtualInfo("", 100);
		return true;
	}

	// this function checks whether the given object is result
	// of some process. Object O is result if the following is true:
	// 1. There is a result link between a process P to O or to some state
	// of O
	// and there is no consumption link from O or one of its states to P
	// 2. A generalization of O is a result.
	private boolean isResult(IXObjectEntry iObject) {
		// initialize
		Vector allResultLinks = new Vector();
		Vector allConsumptionLinks = new Vector();
		Vector linkTypesResults = new Vector();
		linkTypesResults
				.add(new Integer(exportedAPI.OpcatConstants.RESULT_LINK));
		Vector linkTypesConsumption = new Vector();
		linkTypesConsumption.add(new Integer(
				exportedAPI.OpcatConstants.CONSUMPTION_EVENT_LINK));
		linkTypesConsumption.add(new Integer(
				exportedAPI.OpcatConstants.CONSUMPTION_LINK));
		boolean isResult = true;

		// get own incoming result links
		allResultLinks.addAll(this.getLinkByType(this
				.getLinksByDestination(iObject), linkTypesResults));

		// get own outgoing consumption links
		allConsumptionLinks.addAll(this.getLinkByType(this
				.getLinksBySource(iObject), linkTypesConsumption));

		// get incoming result links and outgoing consumption links of all
		// states
		Enumeration enStates = iObject.getStates();
		for (; enStates.hasMoreElements();) {
			IXStateEntry iState = (IXStateEntry) enStates.nextElement();
			// get list of result links incoming to the state
			allResultLinks.addAll(this.getLinkByType(this
					.getLinksByDestination(iState), linkTypesResults));
			allConsumptionLinks.addAll(this.getLinkByType(this
					.getLinksBySource(iState), linkTypesConsumption));
		}

		// go through both link lists
		for (int i = 0; i < allResultLinks.size(); i++) {
			IXLinkEntry iResultLink = (IXLinkEntry) allResultLinks.get(i);
			isResult = true;
			// for each result link find corresponding consumption link
			// if not found return true - the object is result
			for (int j = 0; (j < allConsumptionLinks.size()) && isResult; j++) {
				IXLinkEntry iConsumptionLink = (IXLinkEntry) allConsumptionLinks
						.get(j);
				if (iResultLink.getSourceId() == iConsumptionLink
						.getDestinationId()) {
					// found pair of consumption and result
					isResult = false;
				}
			}
			// found result link without corresponding consumption link -
			// return
			// true
			// the object is a result
			if (isResult) {
				return true;
			}
		}

		// check if object's generalization is a product
		Enumeration en = iObject.getRelationByDestination();
		for (; en.hasMoreElements();) {
			IXRelationEntry relation = (IXRelationEntry) en.nextElement();
			if (relation.getRelationType() == exportedAPI.OpcatConstants.SPECIALIZATION_RELATION) {
				// check if relations source is result, by this recursive call
				if (this.isResult((IXObjectEntry) this.findEntryById(
						relation.getSourceId()).getIXEntry())) {
					return true;
				}
			}
		}
		return false;
	}

	// this function checks whether process can be activated
	// it incorporates the following validations
	// ***Validations relevant for a child process inside zoomed in OPD***
	// 1. Parent Process should be active
	// 2. If process is activated by an external event, it is allowed to be
	// activated
	// only if parent process waits for this event
	// ***Validations relevant for all processes***
	// 1. If the process was triggered by event (instrument event link or
	// consumption event link) the source of this event should be active
	// and if this event is marked with a path label, this path should be
	// valid
	// 2. Incoming links (instrument, comnsumption, effect and condition)
	// should
	// constitute a valid combination consistent with defined paths and
	// logical conditions (not supported yet)
	// LERA!!! - adding logical conditions is here!!!!!!!!
	private boolean checkProcessBeforeActivate(TestingEntry aProcess,
			TestingEvent aEvent) {
		// if the process is a child process inside a zoomed in process
		// it can be activated only if the parent is active
		Enumeration enParents = aProcess.getParents();
		TestingEntry aParent = null;
		TestingEntry oldParent = aProcess.getCurrentEvent().getParentProcess();
		// future enhancement: support multiple parents
		if (enParents.hasMoreElements()) {
			IXProcessEntry parent = (IXProcessEntry) enParents.nextElement();
			aParent = this.findEntryById(parent.getId());
			// if found a not active parent
			// and process is not activated by event link then
			// return false
			if (!aProcess.isEventLinkActivated() && !aParent.isActive()) {
				aProcess.setTaxtualInfo("Parent, "
						+ aParent.getIXEntry().getName()
						+ ", which is a parent of "
						+ aProcess.getIXEntry().getName()
						+ " Is not active, can not execute event link", 20);
				return false;
			}

			// check if the process was triggered by internal event
			boolean isInternalEvent = this.isTriggeredByInternalEvent(aProcess,
					aParent, aEvent);

			// if the process activation was triggered from activateLevel
			// function
			// or from nextStep function the waitForEvent ind will be
			// switched
			// on
			// if it is not switched on - the process was triggered by an
			// external
			// event. In this case we should check whether the parent waits
			// for
			// this
			// event
			if (!aProcess.getCurrentEvent().getWaitForEvent()
					&& !isInternalEvent) {
				// if ((aEvent != null) && (aEvent.getEventLink() != null) &&
				// !isInternalEvent) {
				// process can be activated by event only if parent process
				// waits for
				// this event, i.e. a future activation is scheduled
				Enumeration enEvents = aProcess
						.getScheduledActivations(this.currentStep);
				boolean found = false;
				for (; enEvents.hasMoreElements();) {
					TestingEvent aEventTemp = (TestingEvent) enEvents
							.nextElement();
					// find the active parent according to the exceution id
					for (Enumeration enP = aProcess.getParents(); enP
							.hasMoreElements();) {
						IXEntry iParent = (IXEntry) enP.nextElement();
						TestingEntry aParentTemp = this.findEntryById(iParent
								.getId());
						if (aParentTemp.isActive(aEventTemp.getExecutionId())
								|| aProcess.isEventLinkActivated()) {
							// if (aEventTemp.getExecutionId() ==
							// aParent.getExecutionId()) {
							aParent = aParentTemp;
							found = true;
						}
					}
				}

				// parent process do not wait for this event - return false
				// unless the event is internal
				if (!found) {
					aProcess.setTaxtualInfo(
							"Could not find scheduled activation", 10);
					return false;
				}

				// Parent process should be set here, because it is used further
				// is this function
				// to obtain the links of the process, including those inherited
				// from parent
				aProcess.getCurrentEvent().setParentProcess(aParent);
				aProcess.getCurrentEvent().setExecutionId(
						aParent.getExecutionId());
			}

			// copy parent and execution id to current event
			// they are used further
			if (aEvent != null) {
				if ((aEvent.getEventLink() != null)
						&& (aProcess.getCurrentEvent().getParentProcess() != null)) {
					// copy parent and execution id from the process
					aEvent.setParentProcess(aProcess.getCurrentEvent()
							.getParentProcess());
					if (!isInternalEvent) {
						aEvent.setExecutionId(aProcess.getCurrentEvent()
								.getExecutionId());
					}

				}
			}
		}

		// check the incoming links, a valid combination should exist
		// in accordance with path and logical relations (if defined)
		// if no path/logical relations are defined
		// all links should be valid.
		boolean pathExist = false;
		Vector linkTypes = new Vector();

		linkTypes.add(new Integer(exportedAPI.OpcatConstants.INSTRUMENT_LINK));
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.CONDITION_LINK));
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.CONSUMPTION_LINK));
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.EFFECT_LINK));
		if (TestingSettings.getInstance().isSTOP_AT_AGENT()) {
			linkTypes.add(new Integer(exportedAPI.OpcatConstants.AGENT_LINK));
		}

		// check if the process was activated by event
		if (aEvent != null) {
			if (aEvent.getEventLink() != null) {
				IXLinkEntry triggerEvent = (IXLinkEntry) aEvent.getEventLink()
						.getIXEntry();
				int linkType = triggerEvent.getLinkType();
				// if the process was activated by consumption or instrument
				// event link
				// check that the object on the second end of this link is
				// active
				/**
				 * Raanan : events are none blocking
				 */
				if ((linkType == exportedAPI.OpcatConstants.INSTRUMENT_EVENT_LINK)
						|| (linkType == exportedAPI.OpcatConstants.CONSUMPTION_EVENT_LINK)) {
					// linkTypes.add(new Integer(linkType));
				}
				// if the process was activated by event
				// and there is a path defined for this event -
				// this path should be valid
				if (!triggerEvent.getPath().equals("")) {
					if (this.checkPath(triggerEvent.getPath(), this
							.getLinksByDestination((IXProcessEntry) aProcess
									.getIXEntry(), linkTypes))) {
						// store valid path on the process
						aProcess.setPath(triggerEvent.getPath());
						aProcess.setTaxtualInfo("", 100);
						return true;
					}
					// return false if the path of the trigger is not valid
					aProcess.setTaxtualInfo("Not valid Path ("
							+ triggerEvent.getPath() + ") of "
							+ triggerEvent.getName(), 20);
					return false;
				}
			}
		}

		// check if incoming links allow process activation
		// if a path is defined for at least one of the links - this path should
		// be valid
		// all links without path should be valid, unless there is a logical
		// condition
		Vector v = this.getProcessLinks((IXProcessEntry) aProcess.getIXEntry(),
				linkTypes, false);
		for (Enumeration en = v.elements(); en.hasMoreElements();) {
			IXLinkEntry iLinkEntry = (IXLinkEntry) en.nextElement();
			if (!iLinkEntry.getPath().equals("")) {
				// this variable indicates if atleast one of the links has path
				pathExist = true;
			}
			boolean retVal = this.checkLinkBeforeActivate(aProcess, iLinkEntry);
			// TestingGridAddRow(TestingAuxiliary.ActionActivate, aProcess,
			// retVal,iLinkEntry.getName() + " - " + str);

			if (retVal) {
				// At least one path should be valid
				if (!iLinkEntry.getPath().equals("")) {
					if (this.checkPath(iLinkEntry.getPath(), v.elements())) {
						// store valid path on the process
						aProcess.setPath(iLinkEntry.getPath());
						aProcess.setTaxtualInfo("", 100);
						return true;
					}
				}
			} else {
				// if link's path is empty - the link is mandatory
				// return false if it is not available
				// if link has a path - don't return false meanwhile
				// because may be another path will be found valid
				if (iLinkEntry.getPath().equals("")) {
					// revert old parent and return
					aProcess.getCurrentEvent().setParentProcess(oldParent);
					aProcess.setTaxtualInfo("Problem with link From ( "
							+ this.mySys.getIXSystemStructure().getIXEntry(
									iLinkEntry.getSourceId()).getName() + " )",
							30);
					return false;
				}
				// check logical operators. May be there is another link
				// is available
				// return false;
			}
		}
		// if atleast one link has a path - the process can be activated
		// only if one of the paths is valid.
		// if the execution acheives this point - no valid path exist
		// therefore the process can not be activated
		if (pathExist) {
			// revert ald parent and return
			aProcess.getCurrentEvent().setParentProcess(oldParent);
			aProcess.setTaxtualInfo("No valid path exists", 20);
			return false;
		}

		// there is no path and all links without path are valid
		// store empty path
		aProcess.setPath("");
		aProcess.setTaxtualInfo("", 100);
		return true;
	}

	// This function checks whether link can be activated
	// It incorporates the following validations:
	// 1. Link source should be active
	// 2. If there is a path stored on the source of the link, then the link
	// can be activated only if it has the same path
	private boolean checkLinkBeforeActivate(TestingEntry aEntry,
			IXLinkEntry iLinkEntry) {
		int linkType = iLinkEntry.getLinkType(); // link Type
		TestingEntry aSrc = this.findEntryById(iLinkEntry.getSourceId());
		// TestingEntry aDestination = this.findEntryById(iLinkEntry
		// .getDestinationId());
		// link source
		// if link is enabler its source should be active
		boolean linkTypeCondition = (linkType == exportedAPI.OpcatConstants.AGENT_LINK)
				|| (linkType == exportedAPI.OpcatConstants.CONDITION_LINK)
				|| (linkType == exportedAPI.OpcatConstants.CONSUMPTION_EVENT_LINK)
				|| (linkType == exportedAPI.OpcatConstants.CONSUMPTION_LINK)
				|| (linkType == exportedAPI.OpcatConstants.EFFECT_LINK)
				|| (linkType == exportedAPI.OpcatConstants.INSTRUMENT_EVENT_LINK)
				|| (linkType == exportedAPI.OpcatConstants.INSTRUMENT_LINK);

		if ((linkType == exportedAPI.OpcatConstants.AGENT_LINK)
				&& TestingSettings.getInstance().isSTOP_AT_AGENT()
				&& !bManualActivated) {
			return false;
		}

		if (linkTypeCondition) {
			if (!aSrc.isActive()) {
				// return false if source is not active - link can't be
				// activated
				if (aEntry != null) {
					aEntry.setTaxtualInfo("Source is not Active", 20);
				}
				return false;
			}
		}
		// link path should be same as source path (if source path is not empty)
		if ((aSrc.getPath().equals(""))
				|| (iLinkEntry.getPath().equals(aSrc.getPath()))) {
			//
		} else {
			if (aEntry != null) {
				aEntry.setTaxtualInfo("Source Path is not empty", 20);
			}
			return false;
		}
		if (aEntry != null) {
			aEntry.setTaxtualInfo("", 100);
		}
		return true;
	}

	// ////////////////// Treat activation Consequences
	// ////////////////////////

	private void treatActivationConsequences(TestingEntry activatedEntry,
			TestingEvent aEvent) {
		if (TestingAuxiliary.isEvent(activatedEntry.getIXEntry())) {
			this.treatEventActivationConsequences((IXLinkEntry) activatedEntry
					.getIXEntry(), aEvent);
			return;
		}
		if (TestingAuxiliary.isProcess(activatedEntry.getIXEntry())) {
			this.treatProcessActivationConsequences(activatedEntry, aEvent);
			return;
		}
		if (TestingAuxiliary.isObject(activatedEntry.getIXEntry())) {
			this.treatObjectActivationConsequences(activatedEntry);
			return;
		}
		if (TestingAuxiliary.isState(activatedEntry.getIXEntry())) {
			this.treatStateActivationConsequences((IXStateEntry) activatedEntry
					.getIXEntry());
			return;
		}
		if (TestingAuxiliary.isConditionLink(activatedEntry.getIXEntry())) {
			this.treatConditionLinkConsequences((IXLinkEntry) activatedEntry
					.getIXEntry());
			return;
		}
	}

	/**
	 * this function was added to try to solve a bug wiht conditions. condition
	 * links where not functioning properly.(didnt help)
	 * 
	 * @param entry
	 */
	private void treatConditionLinkConsequences(IXLinkEntry entry) {
		/*
		 * TestingEntry Destination = findEntryById(entry.getDestinationId());
		 * TestingEvent currentEvent = new TestingEvent();
		 * currentEvent.setStep(currentStep+1);
		 * currentEvent.setSchedulingStep(currentStep);
		 * Destination.futureActivationScheduled(currentStep,currentEvent.getExecutionId());
		 */
	}

	// this function handles consequences of event links activation
	// it goes through the list of testing elements and activates the
	// processes
	// connected to the event that pass the validations.
	// if reaction time is defined for the event, it schedules the
	// activation
	// after
	// the reaction time
	// the function also handles the reaction time out exception, which are
	// not
	// supported yet by OPCAT
	private void treatEventActivationConsequences(IXLinkEntry activatedEvent,
			TestingEvent currentEvent) {
		if (currentEvent == null) {
			currentEvent = new TestingEvent();
		}
		// try to activate all corresponding processes
		Enumeration en = TestingSystem.testingElements.elements();
		TestingEntry aProcess, aEventLink = this.findEntryById(activatedEvent
				.getId());
		long reactionTime = aEventLink.getDuration();
		for (; en.hasMoreElements();) {
			aProcess = (TestingEntry) en.nextElement();
			if (TestingAuxiliary.isProcess(aProcess.getIXEntry())) {
				if (activatedEvent.getDestinationId() == aProcess.getIXEntry()
						.getId()) {
					// if the event is connected to one of the descendant of
					// the
					// process
					// it should not activate the parent
					if (this.isDescendantLink(activatedEvent, aProcess, false)) {
						continue;
					}

					// if process that triggers this process is its child
					// nullify the parent passed from child
					if ((currentEvent != null)
							&& (currentEvent.getParentProcess() != null)
							&& currentEvent.getParentProcess().equals(aProcess)) {
						currentEvent.setParentProcess(null);
					}
					// if no reaction time is defined - try to activate the
					// process
					// immediately
					aProcess.setEventLinkActivated(true);
					if (reactionTime == 0) {
						// store the link that tries to activate the process
						currentEvent.setEventLink(aEventLink);
						// activate the process if it passes the validations

						if (this.checkProcessBeforeActivate(aProcess,
								currentEvent)) {
							/*
							 * // copy parent and execution id from the process
							 * currentEvent.setParentProcess(aProcess.getCurrentEvent().getParentProcess()); //
							 * if (currentEvent.getExecutionId() == 0) { if
							 * (!this.isTriggeredByInternalEvent(aProcess,
							 * currentEvent.getParentProcess(), currentEvent )) {
							 * currentEvent.setExecutionId(aProcess.getCurrentEvent().getExecutionId()); }
							 * MOVED TO checkProcessBeforeActivate
							 */
							// activate the process
							this.activate(aProcess, currentEvent);
						}
					} else {
						// schedule future activation of the process after the
						// reaction time
						// the system will try to activate the process even if
						// the reaction
						// time constraints do not hold.
						// TestingEvent actvEvent = new
						// TestingEvent((currentStep +
						// this.computeStep(reactionTime)),
						// aEventLink.getPath(), 0,1, currentStep);
						currentEvent.setStep(this.currentStep
								+ this.computeStep(reactionTime));
						currentEvent.setPath(aEventLink.getPath());
						currentEvent.setSchedulingStep(this.currentStep);

						// store the link that tries to activate the process
						currentEvent.setEventLink(aEventLink);
						// currentEvent.setNumberOfInstances(currentEvent.getNumberOfInstances()
						// + 1 ) ;
						aProcess.scheduleActivation(currentEvent);

						// treat reaction time out exceptions if they are
						// defined
						Enumeration exceptions = this
								.findTimeOutException(aEventLink);
						// if found exceptions compare reaction time to min and
						// max limits
						// Note: reaction time out exceptions are not supported
						// yet
						// therefore they have not been tested
						for (; exceptions.hasMoreElements();) {
							IXLinkEntry iLink = (IXLinkEntry) exceptions
									.nextElement();
							TestingEntry aExceptionLink = this
									.findEntryById(iLink.getId());
							// reaction time out exception link exists
							if (reactionTime < aEventLink.getMinTime()) {
								// if reaction time is less than minimum -
								// schedule exception activation after reaction
								// time
								aExceptionLink.scheduleActivation(
										this.currentStep, this
												.computeStep(reactionTime));
							}
							if (reactionTime > aEventLink.getMaxTime()) {
								// if reaction time is greater than maximum
								// schedule exception activation after max time
								aExceptionLink.scheduleActivation(
										this.currentStep, this
												.computeStep(aEventLink
														.getMaxTime()));
							}
						}
					}
				}
			}
		} // for

		// now deactivate the event
		this.findEntryById(activatedEvent.getId()).deactivate(
				new TestingEvent());
	}

	// this function handles process activation consequences:
	// 1. schedules future termination of the process, unless the process is
	// zoomed in
	// 2. deactivates consumed objects/states consistent with path rules
	// 3. animates result and effect links
	// 4. For zoomed in processes: activates the objects and first level
	// processes
	// inside zoomed in OPD.
	private void treatProcessActivationConsequences(TestingEntry aProcess,
			TestingEvent currentEvent) {

		IXProcessEntry activatedProcess = (IXProcessEntry) aProcess
				.getIXEntry();
		long duration = aProcess.getDuration();
		TestingEvent aEvent = new TestingEvent();
		aEvent.setDuration(duration);
		aEvent.setExecutionId(currentEvent.getExecutionId());

		// schedule future termination
		// if process has child process it will be terminated upon
		// termination of the last child
		if (aProcess.isLowLevel()) {
			try {
				TestingEvent aChildEvent = new TestingEvent(currentEvent);
				aChildEvent.setSchedulingStep(this.currentStep);
				aChildEvent.setStep(this.currentStep
						+ this.computeStep(duration));
				aProcess.scheduleTermination(aChildEvent);
			} catch (Exception e) {
				OpcatLogger.logError(e);
			}
		}

		// deactivate consumed objects/states
		Vector linkTypes = new Vector();
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.CONSUMPTION_LINK));
		linkTypes.add(new Integer(
				exportedAPI.OpcatConstants.CONSUMPTION_EVENT_LINK));
		DeactivateConsumedEntriesTask task1 = new DeactivateConsumedEntriesTask(
				aProcess, aEvent);
		this.validateAndTreatLinkByDestination(activatedProcess, linkTypes,
				task1);

		// animate results
		linkTypes.clear();
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.RESULT_LINK));
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.EFFECT_LINK));
		AnimateResultsTask task2 = new AnimateResultsTask(aProcess, aEvent);
		this.treatLinkBySource(activatedProcess, linkTypes, task2);

		// animate effect links
		linkTypes.clear();
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.EFFECT_LINK));
		this.treatLinkByDestination(activatedProcess, linkTypes, task2);

		linkTypes.clear();
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.EFFECT_LINK));
		this.treatResourceLinkBySource(activatedProcess, linkTypes, true);
		this.treatResourceLinkByDestination(activatedProcess, linkTypes, true);

		// if the process is invoked by internal event deactivate the
		// objects inside it and then initiate them again
		if (this.isTriggeredByInternalEvent(aProcess, currentEvent
				.getParentProcess(), currentEvent)) {
			this.deactivateChildObjects(aProcess);
		}

		// activate objects (not results) inside zoomed in process
		Enumeration en = (aProcess.getChildrenObjects()).elements();
		for (; en.hasMoreElements();) {
			IXObjectEntry childObject = (IXObjectEntry) en.nextElement();
			this.initiateObject(this.findEntryById(childObject.getId()));
		}

		// activate first descendant process (if exists) inside zoomed in
		// process
		TestingEvent aChildEvent = new TestingEvent();
		aChildEvent.setParentProcess(aProcess);
		aChildEvent.setExecutionId(currentEvent.getExecutionId());
		en = activatedProcess.getInstances();
		for (; en.hasMoreElements();) {
			IXProcessInstance activatedProcessInstance = (IXProcessInstance) en
					.nextElement();
			Enumeration firstLevelChildren = aProcess
					.getFirstLevelChildren(activatedProcessInstance);
			// show OPD that contains the child process
			if (TestingSettings.getInstance().isMOVE_BETWEEN_OPD()
					&& TestingSettings.getInstance().isSHOW_GRAPHICS()) {
				if (firstLevelChildren.hasMoreElements()) {
					long childOpdId = ((IXProcessInstance) firstLevelChildren
							.nextElement()).getKey().getOpdId();
					long currentOpd = this.mySys.getCurrentIXOpd().getOpdId();
					if (childOpdId != currentOpd) {
						lastShownOPD = this.mySys.getCurrentIXOpd();
						this.mySys.showOPD(childOpdId, false, false);
						// IXOpd parentOPD = this.mySys.getCurrentIXOpd()
						// .getParentIXOpd();
						IXOpd parentOPD = lastShownOPD;
						Opd current = (Opd) this.mySys.getCurrentIXOpd();
						if (parentOPD != null) {
							if (((Opd) parentOPD).getOpdContainer().isMaximum()) {
								try {
									current.getOpdContainer().setMaximum(true);
								} catch (java.beans.PropertyVetoException pve) {
									OpcatLogger.logError(pve);
								}
							} else {
								current.getOpdContainer().setSize(
										((Opd) parentOPD).getOpdContainer()
												.getSize());
							}
						} else {
							// if
							// (current.getOpdContainer().isMaximumSizeSet())
							// {
							try {
								current.getOpdContainer().setMaximum(true);
							} catch (Exception ex) {
								OpcatLogger.logError(ex);
							}
							// }
						}
						this.opdStack.push(new Long(currentOpd));
					}
				}
			}
			this.activateLevel(aProcess
					.getFirstLevelChildren(activatedProcessInstance),
					aChildEvent);
		}

		// treat timeouts
		this.treatTimeoutsUponActivation(aProcess);

		// if activated process is a specialization of another process
		// activate its generalization as well
		TestingEvent aEventTemp = new TestingEvent(currentEvent);
		aEventTemp.setEventType(TestingSettings.getInstance().getACTIVATION_EVENT());
		this.treatGeneralization(activatedProcess, aEventTemp);
	}

	// this function treats object activation consequences:
	// 1. activates the initial state
	// 2. if no initial state is defined - activates any state randomly
	// 3. if the object has no states treates the instrument/consumption
	// event
	// links
	// 4. activates related objects generalization and exhibitions
	private void treatObjectActivationConsequences(TestingEntry aObject) {
		IXObjectEntry activatedObject = (IXObjectEntry) aObject.getIXEntry();
		boolean hasElements = false;
		hasElements = activatedObject.getInstances().hasMoreElements();
		if (!(hasElements)) {
			return;
			// this is very strange has it means the instance
			// has no elements which he must have at least one - himself
		}
		IXObjectInstance objIns = (IXObjectInstance) (activatedObject
				.getInstances().nextElement());
		boolean hasActiveState = this.hasActiveState(activatedObject);
		boolean hasInitialState = false;
		Enumeration en = null;
		int statesCounter = 0;
		// activate initial state if there is no another active state
		if (!hasActiveState) {
			en = objIns.getStateInstances();
			if (en.hasMoreElements()) {
				IXStateEntry state = null;
				for (; en.hasMoreElements();) {
					state = (IXStateEntry) ((IXStateInstance) en.nextElement())
							.getIXEntry();

					if (((ObjectEntry) activatedObject)
							.getState(ObjectEntry.maxResExeptionStateName) != null) {
						if (state.getId() == ((ObjectEntry) activatedObject)
								.getState(ObjectEntry.maxResExeptionStateName)
								.getId()) {
							continue;
						}
					}

					if (((ObjectEntry) activatedObject)
							.getState(ObjectEntry.minResExeptionStateName) != null) {
						if (state.getId() == ((ObjectEntry) activatedObject)
								.getState(ObjectEntry.minResExeptionStateName)
								.getId()) {
							continue;
						}
					}

					statesCounter++;
					if (state.isInitial()) {
						TestingEntry aState = this.findEntryById(state.getId());
						this.activate(aState);
						hasInitialState = true;
					}
				}
				// activate some state randomly if no initial state is defined
				if (TestingSettings.getInstance().isRANDOM_STATE_SELECTION()) {
					if (!hasInitialState) {
						int randomStateIndex = 0;
						if (statesCounter > 1) {
							randomStateIndex = TestingAuxiliary
									.getRandomNumber(statesCounter);
						}
						en = objIns.getStateInstances();
						int j = 0;
						for (int i = 0; en.hasMoreElements(); i++) {
							state = (IXStateEntry) ((IXStateInstance) en
									.nextElement()).getIXEntry();

							if (((ObjectEntry) activatedObject)
									.getState(ObjectEntry.maxResExeptionStateName) != null) {
								if (state.getId() == ((ObjectEntry) activatedObject)
										.getState(
												ObjectEntry.maxResExeptionStateName)
										.getId()) {
									continue;
								}
							}

							if (((ObjectEntry) activatedObject)
									.getState(ObjectEntry.minResExeptionStateName) != null) {
								if (state.getId() == ((ObjectEntry) activatedObject)
										.getState(
												ObjectEntry.minResExeptionStateName)
										.getId()) {
									continue;
								}
							}

							if (j == randomStateIndex) {
								this
										.activate(this.findEntryById(state
												.getId()));
							}
							j++;
						}
					}
				}
			}
		}

		// if there are no states - treat event links (otherwise these is
		// treated by
		// state activation consequences function
		if (!activatedObject.getStates().hasMoreElements()) {
			Vector linkTypes = new Vector();
			linkTypes.add(new Integer(
					exportedAPI.OpcatConstants.CONSUMPTION_EVENT_LINK));
			linkTypes.add(new Integer(
					exportedAPI.OpcatConstants.INSTRUMENT_EVENT_LINK));
			this.activateLinksBySource(activatedObject, linkTypes);
		}

		// if activated object is a specialization of another object
		// activate its generalization as well
		TestingEvent aEventTemp = new TestingEvent();
		aEventTemp.setEventType(TestingSettings.getInstance().getACTIVATION_EVENT());
		this.treatGeneralization(activatedObject, aEventTemp);

		// treat exhibition relations
		this.treatExhibitions(aObject, true);
	}

	// this function treats state activation consequences:
	// 1. Activates the parent object if it is not active yet
	// 2. schedules state timeout events
	// 3. treats consumption and instrument event links linked to the state
	// 4. treats consumption and instrument event links linked to the parent
	// object
	// (state change)
	private void treatStateActivationConsequences(IXStateEntry activatedState) {
		// activate parent object (if not active)
		IXObjectEntry parentObject = activatedState.getParentIXObjectEntry();
		TestingEntry aParentObject = this.findEntryById(parentObject.getId());
		if ((aParentObject != null) && (!aParentObject.isActive())) {
			this.activate(aParentObject);
		}
		// schedule state time out exception
		TestingEntry aState = this.findEntryById(activatedState.getId());
		this.treatTimeoutsUponActivation(aState);

		// Treat state-entrance events
		Vector linkTypes = new Vector();
		linkTypes.add(new Integer(
				exportedAPI.OpcatConstants.CONSUMPTION_EVENT_LINK));
		linkTypes.add(new Integer(
				exportedAPI.OpcatConstants.INSTRUMENT_EVENT_LINK));
		// this one change to solve bug. condition links where not allways
		// working.
		/**
		 * ???? why is this added ?
		 */
		// linkTypes.add(new
		// Integer(exportedAPI.OpcatConstants.CONDITION_LINK));
		this.activateLinksBySource(activatedState, linkTypes);

		// treat state change events
		this.activateLinksBySource(parentObject, linkTypes);

		// deactivate all other states in object
		IXObjectEntry object = activatedState.getParentIXObjectEntry();
		Enumeration<IXStateEntry> states = object.getStates();
		while (states.hasMoreElements()) {
			IXStateEntry state = states.nextElement();
			if (state.getId() != activatedState.getId()) {
				TestingEntry tState = this.findEntryById(state.getId());
				if (tState.isActive()) {
					this.deactivate(tState);
				}
			}
		}

	}

	// this function synchonizes activation of exhibition relation root and
	// its
	// features
	// when the root is activated/deactivated all its features are
	// acticvated/deactivated
	// as well. Activation/deactivation of feature triggeres
	// activation/deactivation
	// of the root.
	private void treatExhibitions(TestingEntry aObject, boolean activateInd) {
		// if the object is a feature of another object activate/deactivate its
		// parent
		// in this case WaitForEvent indicates that the object was not
		// activated/deactivated
		// as a consequence of root process activation/deactivation

		if (!aObject.getCurrentEvent().getWaitForEvent()) {
			Vector relations = aObject
					.getRelationByDestionation(exportedAPI.OpcatConstants.EXHIBITION_RELATION);
			for (int i = 0; i < relations.size(); i++) {
				IXRelationEntry relation = (IXRelationEntry) relations.get(i);
				TestingEntry aParent = this.findEntryById(relation
						.getSourceId());
				/**
				 * only activate this rule for objects. not processes.
				 */
				if (aParent.isActive()) {
					continue;
				}
				aObject.getCurrentEvent().setWaitForEvent(true);
				if (activateInd) {
					this.activate(aParent);
				} else {
					this.deactivate(aParent);
				}
				aObject.getCurrentEvent().setWaitForEvent(false);
			}
		}

		// if the object is a root of an exhibition relation -
		// activate/deactivate all its features
		Vector relations = aObject
				.getRelationBySource(exportedAPI.OpcatConstants.EXHIBITION_RELATION);
		for (int i = 0; i < relations.size(); i++) {
			IXRelationEntry relation = (IXRelationEntry) relations.get(i);
			TestingEntry aFeature = this.findEntryById(relation
					.getDestinationId());
			if (!TestingAuxiliary.isProcess(aFeature.getIXEntry())) {
				if (!aFeature.getCurrentEvent().getWaitForEvent()) {
					aFeature.getCurrentEvent().setWaitForEvent(true); // temporary
					// indication

					// if (aFeature.isActive()) {
					// continue;
					// }

					if (activateInd) {
						this.activate(aFeature);
					} else {
						this.deactivate(aFeature);
					}
					aFeature.getCurrentEvent().setWaitForEvent(false); // temporary
					// indication
				}
			}
		}
	}

	// This function sets the number of generalization instances upon
	// activation/
	// deactivation of a specialization object. Generalization reflect the
	// total numebr of instances of all its specialization and of its own
	private void treatGeneralization(IXThingEntry iThing, TestingEvent aEvent) {
		Enumeration en = iThing.getRelationByDestination();
		for (; en.hasMoreElements();) {
			IXRelationEntry relation = (IXRelationEntry) en.nextElement();
			if (relation.getRelationType() == exportedAPI.OpcatConstants.SPECIALIZATION_RELATION) {
				// activate relation's source - this is the generalization
				// don't check before activate in this case
				// activateGeneralization is invoked when a specialization is
				// activated
				// in this case generalization should be valid
				TestingEntry aGenEntry = this.findEntryById(relation
						.getSourceId());
				if (aEvent.getEventType() == TestingSettings.getInstance().getACTIVATION_EVENT()) {
					aGenEntry.setNumberOfSpecializations(aGenEntry
							.getNumberOfSpecializations()
							+ aEvent.getNumberOfInstances());
				} else {
					aGenEntry.setNumberOfSpecializations(aGenEntry
							.getNumberOfSpecializations()
							- aEvent.getNumberOfInstances());
				}
			}
		}
	}

	// /////////// Validations before termination //////////////

	private boolean checkBeforeTerminate(TestingEntry aEntry,
			TestingEvent aEvent) {
		// Validations before termination are relevant mean while only for
		// process
		if (TestingAuxiliary.isProcess(aEntry.getIXEntry())) {
			boolean retVal = false;
			retVal = this.checkProcessBeforeTerminate(aEntry, aEvent);
			return retVal;
		}
		return true;
	}

	// process can be terminated only if all child processes have been
	// terminated
	// if child process could not be started because of any reason, except
	// insufficient condition link (e.g. missing instrument), it will have
	// a future activation event that will try to activate the child on the
	// next
	// step.
	private boolean checkProcessBeforeTerminate(TestingEntry aProcess,
			TestingEvent aEvent) {
		// check if any child is active or has future events
		Enumeration children = aProcess.getChildrenProcesses().elements();
		for (; children.hasMoreElements();) {
			IXEntry childEntry = (IXEntry) children.nextElement();
			TestingEntry aChildEntry = this.findEntryById(childEntry.getId());
			// can not terminate if there is active child
			// or there is a termination scheduled for child
			// or child waits for event
			if (aChildEntry.isActive(aEvent.getExecutionId())
					|| aChildEntry.futureActivationScheduled(this.currentStep,
							aEvent.getExecutionId())) {
				aProcess.setTaxtualInfo("Child - "
						+ aChildEntry.getIXEntry().getName()
						+ " Is/Will still/be Active", 20);
				return false;
			}
		}
		// check if the process itself waits for future activation by internal
		// event
		// this is requried to support internal activation with reaction time
		TestingEvent aEventTemp = aProcess.actvScheduler.futureEventsScheduled(
				this.currentStep, aEvent.getExecutionId());
		if (aEventTemp != null) {
			if (this.isTriggeredByInternalEvent(aProcess, aEventTemp
					.getParentProcess(), aEventTemp)) {
				aProcess.setTaxtualInfo("Triggered by Internal Event", 2);
				return false;
			}
		}
		aProcess.setTaxtualInfo("", 100);
		return true;
	}

	// ////////////////// Auxiliary Functions ////////////////////////

	// this function checks whether there is a link identical to the given
	// link
	// linked to one of the direct descendants of the given process.
	// srcInd specifies whether source or destination links should be
	// considered
	private boolean isDescendantLink(IXLinkEntry iLink, TestingEntry aProcess,
			boolean srcInd) {
		// get child processes
		// Vector descendants = getAllDescendantProcesses(aProcess);
		Vector descendants = aProcess.getChildrenProcesses();

		// go through the list of child processes
		for (int j = 0; j < descendants.size(); j++) {
			IXProcessEntry iDescProcess = (IXProcessEntry) descendants.get(j);
			Enumeration enDescLinks = null;

			// get descendant's links
			Vector linkTypes = new Vector();
			linkTypes.add(new Integer(iLink.getLinkType()));
			enDescLinks = this.getLinkByType(iDescProcess, linkTypes, srcInd)
					.elements();

			// look for a link identical to the process own link
			// among the links of descendant
			for (; enDescLinks.hasMoreElements();) {
				IXLinkEntry iDescLink = (IXLinkEntry) enDescLinks.nextElement();
				if (this.isIdentical(iLink, iDescLink, srcInd)) {
					aProcess
							.setTaxtualInfo(
									" This link is connected to a descendant of this process ",
									0);
					return true;
				}
			}
		}
		return false;
	}

	// this function checks whether childThing is a specialization of
	// parentThing
	// or of one of its specializations
	// it applies also for aggregation and instantiation relations
	private boolean isSpecialization(IXThingEntry parentThing,
			IXThingEntry childThing) {
		Enumeration enRelations = parentThing.getRelationBySource();
		for (; enRelations.hasMoreElements();) {
			IXRelationEntry relation = (IXRelationEntry) enRelations
					.nextElement();
			// if found that childThing is indeed a specialization of
			// parentThing
			// return true
			if ((relation.getRelationType() == exportedAPI.OpcatConstants.SPECIALIZATION_RELATION)
					|| (relation.getRelationType() == exportedAPI.OpcatConstants.AGGREGATION_RELATION)
					|| (relation.getRelationType() == exportedAPI.OpcatConstants.INSTANTINATION_RELATION)) {
				if (relation.getDestinationId() == childThing.getId()) {
					return true;
				}

				// call the function recusively for relation's destination
				TestingEntry temp = this.findEntryById(relation
						.getDestinationId());
				if (this.isSpecialization((IXThingEntry) temp.getIXEntry(),
						childThing)) {
					return true;
				}
			}
		}
		// return false if no specialization found
		return false;
	}

	// this function checks whether two links are identical.
	// it gets two links, one of them is connected to a parent process
	// and second to its child. The srcInd indicates the connection type
	// (it is true when child/parent are links' source, false otherwise
	// it will return true if one of the following conditions hold
	// 1. second end of both links is same entry
	// 2. second end of child is a specialization of second end of parent
	// 3. second end of child's link is a state of an object on the second
	// end
	// of
	// parent's link
	// 4. second end of child is a child process of second end of parent
	// 5. second end of child is a state of an object, which is a
	// specialization
	// of second end of parent
	// 6. on high level there is an effect link between parent and object
	// on low level there are result + consumption links between child and
	// object's states
	private boolean isIdentical(IXLinkEntry parentLink, IXLinkEntry childLink,
			boolean srcInd) {
		IXThingEntry parentThing = null, childThing = null;
		// if link type is different return false - identical links should have
		// link type
		if (parentLink.getLinkType() != childLink.getLinkType()) {
			return false;
		}

		// if parent and child are linked to the same entry
		// with the same link type remove this link
		if ((parentLink.getDestinationId() == childLink.getDestinationId())
				|| (parentLink.getSourceId() == childLink.getSourceId())) {
			return true;
		}

		// retrieve second end entries for child and parent
		try {
			IXEntry childEntry = null;
			if (srcInd) {
				parentThing = (IXThingEntry) this.findEntryById(
						parentLink.getDestinationId()).getIXEntry();
				childEntry = this.findEntryById(childLink.getDestinationId())
						.getIXEntry();
			} else {
				parentThing = (IXThingEntry) this.findEntryById(
						parentLink.getSourceId()).getIXEntry();
				childEntry = this.findEntryById(childLink.getSourceId())
						.getIXEntry();
			}
			// obtain the thing on child's second end
			if (TestingAuxiliary.isThing(childEntry)) {
				childThing = (IXThingEntry) childEntry;
			} else {
				// if child's second end is state - get the corresponding object
				if (TestingAuxiliary.isState(childEntry)) {
					childThing = ((IXStateEntry) childEntry)
							.getParentIXObjectEntry();
				}
			}
		} catch (Exception e) {
			return false;
		}

		// after getting object for state:
		// if parent and child are linked to the same entry
		// with the same link type remove this link
		if (parentThing.getId() == childThing.getId()) {
			return true;
		}

		// if second end of child's link is specialization of second end
		// of parent's link (direct or inherited) - return true
		if ((childThing != null) && (parentThing != null)) {
			if (this.isSpecialization(parentThing, childThing)) {
				return true;
			}
		}

		// if second end of child's link is a child process of second end
		// of parent's link - return true
		if ((TestingAuxiliary.isProcess(childThing))
				&& (TestingAuxiliary.isProcess(parentThing))) {
			IXProcessEntry childProcess = (IXProcessEntry) childThing;
			IXProcessEntry parentProcess = (IXProcessEntry) parentThing;
			TestingEntry aParentProcess = this.findEntryById(childProcess
					.getId());
			Enumeration en = aParentProcess.getChildrenProcesses().elements();
			for (; en.hasMoreElements();) {
				IXProcessEntry childProcessTemp = (IXProcessEntry) en
						.nextElement();
				if (childProcessTemp.getId() == parentProcess.getId()) {
					return true;
				}
			}
		}

		// if neither of conditions holds - return false
		return false;
	}

	// this function checks whether the given process is linked with a
	// fundamental
	// relation to another process under the same parent
	private boolean isRelatedProcess(TestingEntry aProcess, TestingEntry aParent) {
		IXProcessEntry iProcess = (IXProcessEntry) aProcess.getIXEntry();
		// get list of brothers
		Vector brothers = aParent.getChildrenProcesses();
		// go through the list of brothers
		for (int i = 0; i < brothers.size(); i++) {
			IXProcessEntry brother = (IXProcessEntry) brothers.get(i);
			long brotherId = brother.getId();
			// get relations of the current process
			Enumeration en = iProcess.getRelationByDestination();
			for (; en.hasMoreElements();) {
				IXRelationEntry relation = (IXRelationEntry) en.nextElement();
				if ((relation.getRelationType() == exportedAPI.OpcatConstants.AGGREGATION_RELATION)
						|| (relation.getRelationType() == exportedAPI.OpcatConstants.EXHIBITION_RELATION)
						|| (relation.getRelationType() == exportedAPI.OpcatConstants.INSTANTINATION_RELATION)
						|| (relation.getRelationType() == exportedAPI.OpcatConstants.SPECIALIZATION_RELATION)) {
					// check if the relation is linked to a brother
					if (relation.getSourceId() == brotherId) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// // This function checks whether there are incoming events
	// // linked to the given process. The function considers all events
	// // except internal events, i.e. events processes inside zoomed in
	// // process,
	// // such as brother invokes brother, or child invokes parent
	// private boolean findEvents(TestingEntry aProcess,
	// TestingEntry aParentProcess) {
	// // get all incoming events
	// Vector linkTypes = new Vector();
	// // linkTypes.add(new Integer(exportedAPI.OpcatConstants.AGENT_LINK));
	// linkTypes.add(new Integer(
	// exportedAPI.OpcatConstants.CONSUMPTION_EVENT_LINK));
	// linkTypes.add(new Integer(exportedAPI.OpcatConstants.EXCEPTION_LINK));
	// linkTypes.add(new Integer(
	// exportedAPI.OpcatConstants.INSTRUMENT_EVENT_LINK));
	// linkTypes.add(new Integer(exportedAPI.OpcatConstants.INVOCATION_LINK));
	// Enumeration en = this.getLinksByDestination((IXProcessEntry) aProcess
	// .getIXEntry(), linkTypes);
	// // go through incoming events, return true if found none internal event
	// for (; en.hasMoreElements();) {
	// IXLinkEntry iLink = (IXLinkEntry) en.nextElement();
	// if (!this.isInternalEvent(aProcess, aParentProcess, iLink)) {
	// return true;
	// }
	// }
	// // return false - no events found
	// return false;
	// }

	// this function checks whether the given link is internal for the given
	// process
	// internal link is a link inside a zoomed in process
	// such as brother invokes brother, or child invokes parent
	private boolean isInternalEvent(TestingEntry aProcess,
			TestingEntry aParentProcess, IXLinkEntry iLink) {

		// internal link can be only of invocation or exception type
		if ((iLink.getLinkType() != exportedAPI.OpcatConstants.EXCEPTION_LINK)
				&& (iLink.getLinkType() != exportedAPI.OpcatConstants.INVOCATION_LINK)) {
			// the link is not internal
			return false;
		}

		// Get children processes
		Vector internalProcesses = aProcess.getChildrenProcesses();
		// Add brother processes
		if (aParentProcess != null) {
			internalProcesses.addAll(aParentProcess.getChildrenProcesses());
		}

		// check if the event source is a child or brother processes
		for (int i = 0; i < internalProcesses.size(); i++) {
			IXEntry iProcess = (IXEntry) internalProcesses.get(i);
			if (iProcess.getId() == iLink.getSourceId()) {
				// the link is internal
				return true;
			}
		}
		return false;
	}

	// this function checks whether the process has been triggered by
	// internal
	// event, i.e by its child or brother.
	// it gets the process itself, its parent and the event that contains
	// activation parameters and calls isInternalEvent for the event link
	// that activated the process
	private boolean isTriggeredByInternalEvent(TestingEntry aProcess,
			TestingEntry aParent, TestingEvent aEvent) {
		boolean isInternalEvent = false; // initialize
		// obtain the event link that activated the process and check if it is
		// internal
		try {
			IXLinkEntry iLink = (IXLinkEntry) aEvent.getEventLink()
					.getIXEntry();
			isInternalEvent = this.isInternalEvent(aProcess, aParent, iLink);
		} catch (Exception e) {
			// the process was not activated by event - return false
			return false;
		}
		return isInternalEvent;
	}

	// this function checks whether there is an unavailable conditions, due
	// to
	// which
	// the process can not be activated. It is used for the child processes
	// inside
	// zoomed in process
	private boolean findUnavailableCondition(TestingEntry process) {
		// get all condition links linked to the process
		Vector linkTypes = new Vector();
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.CONDITION_LINK));
		Enumeration en = this.getLinksByDestination((IXProcessEntry) process
				.getIXEntry(), linkTypes);
		// go through condition links
		for (; en.hasMoreElements();) {
			IXLinkEntry iLinkEntry = (IXLinkEntry) en.nextElement();
			// check whether condition is available

			boolean retVal = this.checkLinkBeforeActivate(process, iLinkEntry);
			// TestingGridAddRow(TestingAuxiliary.ActionActivate, process,
			// retVal,iLinkEntry.getName() + " - " + str);

			if (!retVal) {
				// found unavailable condition
				return true;
			}
		}
		// all conditions are available
		return false;
	}

	// this is an auxiliary function that converts time value to steps
	private long computeStep(long timeOffset) {
		double time = (new Double(timeOffset)).doubleValue();
		double duration = (new Double(TestingSettings.getInstance().getSTEP_DURATION()))
				.doubleValue();
		double doubleTemp = time / duration;
		long temp = java.lang.Math.round(java.lang.Math.ceil(doubleTemp));
		return temp;
	}

	// this function searches time out exceptions linked to the given
	// testing
	// entry
	private Enumeration findTimeOutException(TestingEntry aEntry) {
		Vector linkTypes = new Vector();
		IXConnectionEdgeEntry iEntry;
		try {
			// currently it will not support reaction time outs
			// because link is not IXConnectionEdgeEntry
			iEntry = (IXConnectionEdgeEntry) aEntry.getIXEntry();
		} catch (java.lang.ClassCastException e) {
			return linkTypes.elements();
		}
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.EXCEPTION_LINK));
		return this.getLinksBySource(iEntry, linkTypes);
	}

	// this function gets a path and a list of links
	// it checks whether the path is valid
	// if inside given list of links there is a none active
	// link marked with the given path, or with no path
	// the function returns false
	// otherwise it returns true
	private boolean checkPath(String path, Enumeration en) {
		// Enumeration en = testingElements.elements();
		// String path = link.getPath();
		IXLinkEntry tempLink;
		// long destId = link.getDestinationId();

		for (; en.hasMoreElements();) {
			/*
			 * TestingEntry aEntry = (TestingEntry)en.nextElement(); if
			 * (!TestingAuxiliary.isLink(aEntry.getIXEntry())) { continue; }
			 * tempLink = (IXLinkEntry)aEntry.getIXEntry();
			 */
			tempLink = (IXLinkEntry) en.nextElement();
			/*
			 * if ((tempLink.getDestinationId() == destId) && (tempLink.getId() !=
			 * link.getId())) {
			 */
			String tempPath = tempLink.getPath();
			// if there is another link with same destination
			// marked with same path or not marked with any path
			// then it should be active as well
			if (tempPath.equals("") || tempPath.equals(path)) {

				boolean retVal = this.checkLinkBeforeActivate(null, tempLink);
				// TestingGridAddRow(TestingAuxiliary.ActionActivate,
				// tempLink, retVal,tempLink.getName() + "(Check Path) - " +
				// str);
				if (!retVal) {
					return false;
				}
			}
		}
		// path is valid
		return true;
	}

	// this recursive function returns all descendant processes
	// of the given process
	public Vector getAllDescendantProcesses(TestingEntry aProcess) {
		Vector v = new Vector();
		v.addAll(aProcess.getChildrenProcesses());
		Enumeration en = v.elements();
		for (; en.hasMoreElements();) {
			IXProcessEntry iChild = (IXProcessEntry) en.nextElement();
			TestingEntry aChild = this.findEntryById(iChild.getId());
			TestingAuxiliary.mergeVectors(v, aChild.getChildrenProcesses());
		}
		return v;
	}

	// this function checks whether the object has an active state
	private boolean hasActiveState(IXObjectEntry ixObjectEntry) {
		IXObjectInstance objIns = (IXObjectInstance) (ixObjectEntry
				.getInstances().nextElement());
		Enumeration en = objIns.getStateInstances();
		// check that there is no active state
		for (; en.hasMoreElements();) {
			IXStateEntry state = (IXStateEntry) ((IXStateInstance) en
					.nextElement()).getIXEntry();
			TestingEntry aState = this.findEntryById(state.getId());
			if (aState.isActive()) {
				return true;
			}
		}
		return false;
	}

	// this function deactivates objects inside a zoomed in process
	private void deactivateChildObjects(TestingEntry aProcess) {
		Enumeration childObjects = aProcess.getChildrenObjects().elements();
		for (; childObjects.hasMoreElements();) {
			IXObjectEntry childObject = (IXObjectEntry) childObjects
					.nextElement();
			TestingEvent aEvent = new TestingEvent();
			TestingEntry aChildObject = this.findEntryById(childObject.getId());
			aEvent.setNumberOfInstances(aChildObject.getNumberOfInstances());
			this.deactivate(aChildObject, aEvent);
		}
	}

	// ////////////////// Links Treatment Functions ////////////////////////

	// this function activates links with the given source and link type
	private void activateLinksBySource(IXConnectionEdgeEntry iEntry,
			Vector linkTypes) {
		Enumeration en = this.getLinksBySource(iEntry, linkTypes);
		IXLinkEntry link;
		while(en.hasMoreElements()) {
			link = (IXLinkEntry) en.nextElement();
			this.activate(this.findEntryById(link.getId()));
		}
	}

	// this link applies the specified task to the links with specified
	// source
	// and link type
	private void treatLinkBySource(IXConnectionEdgeEntry iEntry,
			Vector linkTypes, Task task) {
		Enumeration en = this.getLinksBySource(iEntry, linkTypes);
		this.treatLinks(en, task);
	}

	private void treatResourceLinkBySource(IXConnectionEdgeEntry iEntry,
			Vector linkTypes, boolean isActivation) {
		Enumeration en = this.getLinksBySource(iEntry, linkTypes);
		while (en.hasMoreElements()) {
			IXLinkEntry link = (IXLinkEntry) en.nextElement();
			if (link.getLinkType() == OpcatConstants.EFFECT_LINK) {
				LinkEntry effect = (LinkEntry) link;
				TestingEntry testingEntry = getTestingElementByIXEntryID(link
						.getDestinationId());
				if (testingEntry.getIXEntry() instanceof IXObjectEntry) {
					OpmObject opm = (OpmObject) ((Entry) testingEntry
							.getIXEntry()).getLogicalEntity();
					if ((opm.getMesurementUnitInitialValue() != 0)
							|| opm.isMesurementUnitExists()) {
						if (effect.getResourceConsumption() != 0) {
							if (isActivation) {
								testingEntry
										.setMeasurementUnitAcommulatedValue(testingEntry
												.getMeasurementUnitAcommulatedValue()
												+ ((LinkEntry) link)
														.getResourceConsumption());
							} else {
								if (!((LinkEntry) link)
										.isResourceConsumptionAccumolated()) {
									testingEntry
											.setMeasurementUnitAcommulatedValue(testingEntry
													.getMeasurementUnitAcommulatedValue()
													- ((LinkEntry) link)
															.getResourceConsumption());
								}

							}
						}
						/**
						 * treat resource exeption first get the active state of
						 * the object
						 */

						if (!isActivation) {

							ObjectEntry object = (ObjectEntry) testingEntry
									.getIXEntry();

							StateInstance activeState = ((ObjectInstance) object
									.getInstances().nextElement())
									.getAnimatedState();
							StateEntry maxState = object
									.getState(ObjectEntry.maxResExeptionStateName);
							StateEntry minState = object
									.getState(ObjectEntry.minResExeptionStateName);
							if (testingEntry.isMeasurementUnitExeption() != TestingEntry.NoResourceExeption) {
								boolean saveState = false;

								if (testingEntry.isMeasurementUnitExeption() == TestingEntry.MaxResourceExeption) {
									if (maxState != null) {
										activate(getTestingElementByIXEntryID(maxState
												.getId()));
										saveState = true;
									}
								}

								if (testingEntry.isMeasurementUnitExeption() == TestingEntry.MinResourceExeption) {
									if (minState != null) {
										activate(getTestingElementByIXEntryID(minState
												.getId()));
										saveState = true;
									}
								}

								if (saveState && activeState != null) {
									if (testingEntry.getSavedState() == null) {
										testingEntry
												.setSavedState((StateEntry) activeState
														.getEntry());
									}
								} else {

									testingEntry.setSavedState(null);
								}

							} else {
								if (minState != null) {
									deactivate(getTestingElementByIXEntryID(minState
											.getId()));
								}
								if (maxState != null) {
									deactivate(getTestingElementByIXEntryID(maxState
											.getId()));
								}

								if (testingEntry.getSavedState() != null) {
									activate(getTestingElementByIXEntryID(testingEntry
											.getSavedState().getId()));
								}
								testingEntry.setSavedState(null);
							}
						}

					}
				}
			}
		}
	}

	private void treatResourceLinkByDestination(IXConnectionEdgeEntry iEntry,
			Vector linkTypes, boolean isActivation) {
		Enumeration en = this.getLinksByDestination(iEntry, linkTypes);
		while (en.hasMoreElements()) {
			IXLinkEntry link = (IXLinkEntry) en.nextElement();
			if (link.getLinkType() == OpcatConstants.EFFECT_LINK) {
				LinkEntry effect = (LinkEntry) link;
				TestingEntry testingEntry = getTestingElementByIXEntryID(link
						.getSourceId());
				if (testingEntry.getIXEntry() instanceof IXObjectEntry) {
					OpmObject opm = (OpmObject) ((Entry) testingEntry
							.getIXEntry()).getLogicalEntity();
					if ((opm.getMesurementUnitInitialValue() != 0)
							|| opm.isMesurementUnitExists()) {
						if (effect.getResourceConsumption() != 0) {
							if (isActivation) {
								testingEntry
										.setMeasurementUnitAcommulatedValue(testingEntry
												.getMeasurementUnitAcommulatedValue()
												+ ((LinkEntry) link)
														.getResourceConsumption());
							} else {
								if (!((LinkEntry) link)
										.isResourceConsumptionAccumolated()) {
									testingEntry
											.setMeasurementUnitAcommulatedValue(testingEntry
													.getMeasurementUnitAcommulatedValue()
													- ((LinkEntry) link)
															.getResourceConsumption());
								}
							}
						}

						/**
						 * treat resource exeption first get the active state of
						 * the object
						 */

						if (!isActivation) {

							ObjectEntry object = (ObjectEntry) testingEntry
									.getIXEntry();

							StateInstance activeState = ((ObjectInstance) object
									.getInstances().nextElement())
									.getAnimatedState();
							StateEntry maxState = object
									.getState(ObjectEntry.maxResExeptionStateName);
							StateEntry minState = object
									.getState(ObjectEntry.minResExeptionStateName);
							if (testingEntry.isMeasurementUnitExeption() != TestingEntry.NoResourceExeption) {
								boolean saveState = false;

								if (testingEntry.isMeasurementUnitExeption() == TestingEntry.MaxResourceExeption) {
									if (maxState != null) {
										activate(getTestingElementByIXEntryID(maxState
												.getId()));
										saveState = true;
									}
								}

								if (testingEntry.isMeasurementUnitExeption() == TestingEntry.MinResourceExeption) {
									if (minState != null) {
										activate(getTestingElementByIXEntryID(minState
												.getId()));
										saveState = true;
									}
								}

								if (saveState && activeState != null) {
									if (testingEntry.getSavedState() == null) {
										testingEntry
												.setSavedState((StateEntry) activeState
														.getEntry());
									}
								} else {

									testingEntry.setSavedState(null);
								}

							} else {
								if (minState != null) {
									deactivate(getTestingElementByIXEntryID(minState
											.getId()));
								}
								if (maxState != null) {
									deactivate(getTestingElementByIXEntryID(maxState
											.getId()));
								}

								if (testingEntry.getSavedState() != null) {
									activate(getTestingElementByIXEntryID(testingEntry
											.getSavedState().getId()));
								}
								testingEntry.setSavedState(null);
							}
						}
					}
				}
			}
		}
	}

	// this link applies the specified task to the links with specified
	// destination and link type
	private void treatLinkByDestination(IXConnectionEdgeEntry iEntry,
			Vector linkTypes, Task task) {
		Enumeration en = this.getLinksByDestination(iEntry, linkTypes);
		this.treatLinks(en, task);
	}

	// this link applies the validation and the treatment of the specified
	// task
	// to
	// the links with specified source and link type
	// private void validateAndTreatLinkBySource(IXConnectionEdgeEntry
	// iEntry,
	// Vector linkTypes, Task task) {
	// Enumeration en = getLinksBySource(iEntry, linkTypes);
	// validateAndTreatLinks(en, task);
	// }

	// this link applies the validation and the treatment of the specified
	// task
	// to
	// the links with specified destination and link type
	private void validateAndTreatLinkByDestination(
			IXConnectionEdgeEntry iEntry, Vector linkTypes, Task task) {
		Enumeration en = this.getLinksByDestination(iEntry, linkTypes);
		this.validateAndTreatLinks(en, task);
	}

	// this function applies the given task to the given enumeration of
	// links
	private void treatLinks(Enumeration en, Task task) {
		IXLinkEntry link;
		for (; en.hasMoreElements();) {
			link = (IXLinkEntry) en.nextElement();
			// if (!processedLinks.containsKey(new Long(link.getId()))) {
			task.run(link);
			// processedLinks.put(new Long(link.getId()), link);
			// }

		}
	}

	// this function applies the given task and validation to the given
	// enumeration of links
	private void validateAndTreatLinks(Enumeration en, Task task) {
		Vector validLinks = new Vector();
		Vector allLinks = new Vector();

		for (; en.hasMoreElements();) {
			allLinks.add(en.nextElement());
		}

		en = allLinks.elements();

		for (; en.hasMoreElements();) {
			IXLinkEntry link = (IXLinkEntry) en.nextElement();
			if (task.validate(link, allLinks.elements())) {
				validLinks.add(link);
			}
		}
		this.treatLinks(validLinks.elements(), task);
	}

	// temporary functions written instead of
	// IXConnectionEdgeEntry.getLinksBySource,
	// which returns sometimes ancestors links
	private Enumeration getLinksBySource(IXConnectionEdgeEntry iEntry) {
		return this.getLinks(iEntry, true);
		// return iEntry.getLinksBySource();
	}

	private Enumeration getLinksByDestination(IXConnectionEdgeEntry iEntry) {
		return this.getLinks(iEntry, false);
		// return iEntry.getLinksByDestination();
	}

	private Enumeration getLinks(IXConnectionEdgeEntry iEntry, boolean srcInd) {
		// Hashtable<Long, IXLinkEntry> processedLinks = new Hashtable<Long,
		// IXLinkEntry>();
		Vector<IXLinkEntry> v = new Vector<IXLinkEntry>();
		Enumeration en = this.testingElements.elements();
		for (; en.hasMoreElements();) {
			TestingEntry aEntry = (TestingEntry) en.nextElement();
			if (TestingAuxiliary.isLink(aEntry.getIXEntry())) {
				IXLinkEntry iLink = (IXLinkEntry) aEntry.getIXEntry();
				// /**
				// * Start Or tretmant
				// */
				// // // if or conncted choose one.
				// IXLinkInstance linkInstance = (IXLinkInstance) iLink
				// .getInstances().nextElement();
				// if ((srcInd &&
				// findEntryById(iLink.getSourceId()).isLowLevel())
				// || (!srcInd && findEntryById(iLink.getDestinationId())
				// .isLowLevel())) {
				// Enumeration<IXLinkInstance> orEnum = linkInstance
				// .getOrXorSourceNeighbours(false);
				// // get a random number from 0 to numInstances ;
				// if (srcInd) {
				// orEnum = linkInstance.getOrXorSourceNeighbours(false);
				// } else {
				// orEnum = linkInstance
				// .getOrXorDestinationNeighbours(false);
				// }
				//
				// if (orEnum != null) {
				// for (int i = 0; orEnum.hasMoreElements(); i++) {
				// IXLinkInstance ins = orEnum.nextElement();
				// if (i != 0 ) {
				// processedLinks.put(new Long(ins.getIXEntry()
				// .getId()), (IXLinkEntry) ins
				// .getIXEntry());
				// }
				// }
				// }
				// }
				//
				// /**
				// * END Or tretment
				// */
				if (srcInd) {
					if ((iLink.getSourceId() == iEntry.getId())) {
						v.add(iLink);
					}
				} else {
					if ((iLink.getDestinationId() == iEntry.getId())) {
						v.add(iLink);
					}
				}
			}
		}
		return v.elements();
	}

	// this function returns list of links connected to the given entry
	// (source)
	// and having specified link types
	private Enumeration getLinksBySource(IXConnectionEdgeEntry iEntry,
			Vector linkTypes) {
		if (TestingAuxiliary.isProcess(iEntry)) {
			return this.getProcessLinks((IXProcessEntry) iEntry, linkTypes,
					true).elements();
		}
		return this.getLinkByType(this.getLinksBySource(iEntry), linkTypes)
				.elements();
	}

	// this function returns list of links connected to the given entry
	// (destination)
	// and having specified link types
	private Enumeration getLinksByDestination(IXConnectionEdgeEntry iEntry,
			Vector linkTypes) {
		if (TestingAuxiliary.isProcess(iEntry)) {
			return this.getProcessLinks((IXProcessEntry) iEntry, linkTypes,
					false).elements();
		} else {
			// return getLinkByType(iEntry.getLinksByDestination(),
			// linkTypes).elements();
			return this.getLinkByType(this.getLinksByDestination(iEntry),
					linkTypes).elements();
		}
	}

	private Vector getLinkByType(IXProcessEntry process, Vector linkTypes,
			boolean srcInd) {
		if (srcInd) {
			return this
					.getLinkByType(this.getLinksBySource(process), linkTypes);
		} else {
			return this.getLinkByType(this.getLinksByDestination(process),
					linkTypes);
		}
	}

	// this function derives links having given link types from the given
	// enumeration of links
	private Vector getLinkByType(Enumeration en, Vector linkTypes) {
		Vector<IXLinkEntry> temp = new Vector<IXLinkEntry>();
		IXLinkEntry link;
		for (; en.hasMoreElements();) {
			link = (IXLinkEntry) en.nextElement();
			if (linkTypes.contains(new Integer(link.getLinkType()))) {
				// mix links is some random order. This is necessary for
				// random selection when more than one link/path are available
				temp.add(TestingAuxiliary.getRandomNumber(temp.size() + 1),
						link);
			}
		}
		return temp;
	}

	// this is a recursive function that returns a list of links
	// of the given object and all its ancestors
	// according to the given link type and source/destination
	private Vector getProcessOwnAndAncestorsLinks(IXProcessEntry process,
			Vector linkTypes, boolean srcInd) {
		Vector allLinks = new Vector();
		Vector newLinks = null;
		TestingEntry aProcess = this.findEntryById(process.getId());

		// get own links
		newLinks = this.getLinkByType(process, linkTypes, srcInd);

		// add newLinks to allLinks
		TestingAuxiliary.mergeVectors(allLinks, newLinks);

		// if the process was not activated in parent's context return only
		// its own links (high level process)
		if (aProcess.getCurrentEvent().getParentProcess() == null) {
			return allLinks;
		}

		// get ancestors' links by this recursive call
		IXProcessEntry parentProcess = (IXProcessEntry) aProcess
				.getCurrentEvent().getParentProcess().getIXEntry();
		Vector parentLinks = this.getProcessOwnAndAncestorsLinks(parentProcess,
				linkTypes, srcInd);

		// remove identical links from the list of parent's links
		for (int i = parentLinks.size() - 1; i >= 0; i--) {
			IXLinkEntry parentLink = (IXLinkEntry) parentLinks.get(i);
			// remove identical links
			for (int j = 0; j < allLinks.size(); j++) {
				IXLinkEntry childLink = (IXLinkEntry) allLinks.get(j);
				if (this.isIdentical(parentLink, childLink, srcInd)) {
					parentLinks.remove(i);
					j = allLinks.size(); // exit from this loop after
					// removal
				}
			}
		}

		// remove brothers' links
		Vector brothers = this.findEntryById(parentProcess.getId())
				.getChildrenProcesses();

		// go through the brothers
		for (int j = 0; j < brothers.size(); j++) {
			IXProcessEntry iBrotherProcess = (IXProcessEntry) brothers.get(j);

			// skip the process itself, it was already handled above
			if (iBrotherProcess.getId() == process.getId()) {
				continue;
			}

			Vector brothersLinks = null;

			// get brother's links
			brothersLinks = this.getLinkByType(iBrotherProcess, linkTypes,
					srcInd);

			// look among ancestors links for an identical link linked to a
			// brother
			// process
			for (int i = parentLinks.size() - 1; i >= 0; i--) {
				IXLinkEntry iLink = (IXLinkEntry) parentLinks.get(i);
				Enumeration enBrotherLinks = brothersLinks.elements();
				for (boolean stop = false; enBrotherLinks.hasMoreElements()
						&& !stop;) {
					IXLinkEntry iBrotherLink = (IXLinkEntry) enBrotherLinks
							.nextElement();
					if (this.isIdentical(iLink, iBrotherLink, srcInd)) {
						parentLinks.remove(i);
						stop = true; // exit from the loop after removal
					}
				}
			}
		}

		TestingAuxiliary.mergeVectors(allLinks, parentLinks);
		return allLinks;
	}

	// This functions returns the links that refer to the given entry
	// for objects and states it returns the list of incoming or outgoing
	// links
	// in accordance with the given list of types
	// for processes it applies the following logic:
	// if the process is low level (i.e. hasn't child process in zoomed in
	// OPD)
	// then it retrieves all the links linked directly to the process
	// and all none-event links linked to all ancestors of this process on
	// all
	// levels
	// except those that refer to process brothers or its ancestors'
	// brothers
	// but not to the process itself
	// if the process is not low level then it will retrieve
	// only the events linked directly to the process
	// except those that are linked also to process descendant.
	private Vector getProcessLinks(IXProcessEntry process, Vector linkTypes,
			boolean srcInd) {
		TestingEntry aProcess = this.findEntryById(process.getId());
		Vector allLinks = new Vector();

		// separate event links and none event links into two vectors
		Vector linkTypesNotEvents = new Vector();
		Vector linkTypesOnlyEvents = new Vector();
		for (int i = linkTypes.size() - 1; i >= 0; i--) {
			Integer linkType = (Integer) linkTypes.get(i);
			if (TestingAuxiliary.isEvent(linkType.intValue())) {
				linkTypesOnlyEvents.add(linkType);
			} else {
				linkTypesNotEvents.add(linkType);
			}
		}

		if (aProcess.isLowLevel()) {
			// if the process is low level
			// get all none event links of the process itself and its
			// ancestors
			allLinks.addAll(this.getProcessOwnAndAncestorsLinks(process,
					linkTypesNotEvents, srcInd));
		}

		// TestingAuxiliary.debug(allLinks);
		// now add the events linked directly to the current entry
		// linkTypes contains only events
		// none events are not required for zoomed in processes
		// and for low level process they already was collected above
		TestingAuxiliary.mergeVectors(allLinks, this.getLinkByType(process,
				linkTypesOnlyEvents, srcInd));
		// this was added to solve condition links problem.
		if (!aProcess.isLowLevel()) {
			TestingAuxiliary.mergeVectors(allLinks, this.getLinkByType(process,
					linkTypesNotEvents, srcInd));
		}
		// if process's link has an identical link
		// link to one of descendant processes (if the process is zoomed in)
		// this link should be treated only for the descendant and therefore
		// should be removed from the list of process own links
		// add all descendants
		Vector descendants = this.getAllDescendantProcesses(aProcess);

		// TestingAuxiliary.debug(descendants);
		for (int j = 0; j < descendants.size(); j++) {
			IXProcessEntry iDescProcess = (IXProcessEntry) descendants.get(j);
			Vector descendantLinks = new Vector();

			// get descendant's links
			descendantLinks = this.getLinkByType(iDescProcess, linkTypes,
					srcInd);

			// look for a link identical to the process own link
			// among the links of descendant
			for (int i = allLinks.size() - 1; i >= 0; i--) {
				IXLinkEntry iLink = (IXLinkEntry) allLinks.get(i);
				Enumeration enDescLinks = descendantLinks.elements();
				for (boolean stop = false; enDescLinks.hasMoreElements()
						&& !stop;) {
					IXLinkEntry iDescLink = (IXLinkEntry) enDescLinks
							.nextElement();
					if (this.isIdentical(iLink, iDescLink, srcInd)) {
						allLinks.remove(i);
						stop = true; // exit from the loop after removal.
					}
				}
			}
		}

		// return the resulting collection
		return allLinks;
	}

	// ///////////// TimeOut Treatment Functions ////////////////

	// this function applies time out treatment upon termination of an entry
	// it is called for states and processes
	// First it calculates the duration of process or being on state. Then
	// it goes through the list of time out exception links connected to the
	// entry
	// if the duration is less that the minimum duration, it activates the
	// exception
	// In any case it cancells the exception activation event scheduled max
	// time
	// after
	// entry activation (this scheduling is done in
	// treatTimeoutsUponActivation
	// function).
	private void treatTimeoutsUponTermination(TestingEntry aEntry,
			TestingEvent currentEvent) {
		// treat time outs
		Enumeration en = this.findTimeOutException(aEntry);
		for (; en.hasMoreElements();) {
			IXLinkEntry iLink = (IXLinkEntry) en.nextElement();
			TestingEntry aExceptionLink = this.findEntryById(iLink.getId());

			// compute duration
			long activationStep = aEntry.getCurrentEvent().getStep();
			long duration = (this.currentStep - activationStep)
					* TestingSettings.getInstance().getSTEP_DURATION();

			// activate exception if entry is deactivated before minimum
			// time
			if ((duration > 0) && (duration < aEntry.getMinTime())) {
				this.activate(aExceptionLink);
			}

			// cancel scheduled event - set cancel step = current step
			Enumeration enExAct = aExceptionLink
					.getScheduledActivations(activationStep
							+ this.computeStep(aEntry.getMaxTime()));
			try {
				TestingEvent aExceptionActvEvent = (TestingEvent) enExAct
						.nextElement();
				aExceptionActvEvent.setCancelStep(this.currentStep);
			} catch (Exception e) {
				OpcatLogger.logError(e);
			}
		}
	}

	// this function treats time outs upon entry activation.
	private void treatTimeoutsUponActivation(TestingEntry aEntry) {
		this.treatTimeoutsUponActivation(aEntry, this.currentStep);
	}

	// this function treats time outs upon entry activation
	// for each exception link connected to the entry it schedules future
	// activation
	// after the max time.
	private void treatTimeoutsUponActivation(TestingEntry aEntry, long step) {
		Enumeration en = this.findTimeOutException(aEntry);
		for (; en.hasMoreElements();) {
			IXLinkEntry iLink = (IXLinkEntry) en.nextElement();
			TestingEntry aLink = this.findEntryById(iLink.getId());
			long maxTime = aEntry.getMaxTime();
			aLink.scheduleActivation(step, step + this.computeStep(maxTime));
		}
	}

	// ///////////// Termination Consequences Treatment Functions
	// ////////////////

	private void treatTerminationConsequences(TestingEntry activatedEntry,
			TestingEvent aEvent) {
		if (TestingAuxiliary.isProcess(activatedEntry.getIXEntry())) {
			this.treatProcessTerminationConsequences(activatedEntry, aEvent);
			return;
		}
		if (TestingAuxiliary.isObject(activatedEntry.getIXEntry())) {
			this.treatObjectTerminationConsequences(activatedEntry, aEvent);
			return;
		}

		if (TestingAuxiliary.isState(activatedEntry.getIXEntry())) {
			this.treatStateTerminationConsequences(activatedEntry, aEvent);
			return;
		}
	}

	// this function treats the termination consequences of a process:
	// 1. activates results and invocations
	// 2. if process is a child process - call
	// treatChildProcessTerminationConsequences function
	// 3. if this is a zoomed in process - terminates all objects inside it
	// 4. treats time outs
	private void treatProcessTerminationConsequences(TestingEntry aProcess,
			TestingEvent currentEvent) {
		IXProcessEntry iProcess = (IXProcessEntry) aProcess.getIXEntry();

		// activate results
		Vector linkTypes = new Vector();
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.RESULT_LINK));
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.INVOCATION_LINK));
		ActivateResultsTask task = new ActivateResultsTask(aProcess,
				currentEvent);
		this.treatLinkBySource(iProcess, linkTypes, task);

		linkTypes.clear();
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.EFFECT_LINK));
		this.treatResourceLinkBySource(iProcess, linkTypes, false);
		this.treatResourceLinkByDestination(iProcess, linkTypes, false);

		// treat terminated states
		linkTypes.clear();
		linkTypes.add(new Integer(exportedAPI.OpcatConstants.CONSUMPTION_LINK));
		linkTypes.add(new Integer(
				exportedAPI.OpcatConstants.CONSUMPTION_EVENT_LINK));
		Enumeration en = this.getLinksByDestination(iProcess, linkTypes);
		for (; en.hasMoreElements();) {
			IXLinkEntry iLink = (IXLinkEntry) en.nextElement();
			TestingEntry aSrc = this.findEntryById(iLink.getSourceId());
			if (TestingAuxiliary.isState(aSrc.getIXEntry())) {
				IXObjectEntry object = ((IXStateEntry) aSrc.getIXEntry())
						.getParentIXObjectEntry();
				TestingEntry aObj = this.findEntryById(object.getId());
				if (aObj.isActive() && !this.hasActiveState(object)) {
					// if after state termination, there is no more active
					// states for this
					// object, the object should be deactivated
					this.deactivate(aObj, new TestingEvent(currentEvent));
				}
			}
		}

		// if the process was triggered by internal event
		// treat child process termination consequences of the process that
		// triggered it (source of the event link that invoked the current
		// process)
		if (this.isTriggeredByInternalEvent(aProcess, currentEvent
				.getParentProcess(), currentEvent)) {
			TestingEvent aEventNext = new TestingEvent(currentEvent);
			if (currentEvent.getParentProcess() == null) {
				// this might be true only if child process invoked its parent
				// set current process as a parent
				aEventNext.setParentProcess(aProcess);
			}
			long srcId = ((IXLinkEntry) currentEvent.getEventLink()
					.getIXEntry()).getSourceId();
			this.treatChildProcessTerminationConsequences(this
					.findEntryById(srcId), aEventNext);
		} else {
			// if the terminated process is a child process
			// of a zoomed in process activated in parent process context -
			// activate next level children or deactivate the parent
			if (currentEvent.getParentProcess() != null) { // parent
				// process
				// context
				this.treatChildProcessTerminationConsequences(aProcess,
						currentEvent);
			}
		}

		// if the terminated process is zoomed in
		// deactivate all objects inside it.
		if (aProcess.getNumberOfInstances() == 0) {
			this.deactivateChildObjects(aProcess);
		}

		// treat timeouts
		this.treatTimeoutsUponTermination(aProcess, currentEvent);

		// if terminated process is a specialization of another process
		// activate its generalization as well
		TestingEvent aEventTemp = new TestingEvent(currentEvent);
		aEventTemp.setEventType(TestingSettings.getInstance().getTERMINATION_EVENT());
		this.treatGeneralization(iProcess, aEventTemp);
	}

	// this function treats the consequences of child process termination
	// child process is a process that has parent and was activated in
	// parent's
	// context
	// this function implements the following rule:
	// 1. when last child process on the active level terminates next level
	// should be activated
	// 2. if current level is the last level, then parent process should be
	// terminated
	private void treatChildProcessTerminationConsequences(
			TestingEntry aProcess, TestingEvent currentEvent) {
		IXProcessEntry iProcess = (IXProcessEntry) aProcess.getIXEntry();
		TestingEvent aChildEvent = new TestingEvent();
		aChildEvent.setParentProcess(currentEvent.getParentProcess());
		aChildEvent.setExecutionId(currentEvent.getExecutionId());
		if (aProcess.getParents().hasMoreElements()) {
			Enumeration en = iProcess.getInstances();
			for (; en.hasMoreElements();) {
				IXProcessInstance activatedProcessInstance = (IXProcessInstance) en
						.nextElement();
				IXThingInstance parentProcessInstance = activatedProcessInstance
						.getParentIXThingInstance();
				if (parentProcessInstance != null) {
					TestingEntry aParentProcess = this
							.findEntryById(parentProcessInstance.getIXEntry()
									.getId());
					if (aParentProcess.isActive(currentEvent.getExecutionId())) {
						if (this.checkProcessBeforeTerminate(aParentProcess,
								currentEvent)) {
							// activate next level if all child processes
							// already terminated
							Enumeration nextLevelChildren = aProcess
									.getNextLevelChildren(activatedProcessInstance);
							// check again checkProcessBeforeTerminate
							// because
							// it can happen that although
							// one of children has been activated, but it
							// was
							// terminated immediately
							if (!this.activateLevel(nextLevelChildren,
									aChildEvent)
									|| this.checkProcessBeforeTerminate(
											aParentProcess, currentEvent)) {
								// if activateLevel returns false - there are no
								// more children
								// processes to be activated - terminate the
								// parent process
								if (aParentProcess.getCurrentEvent(currentEvent
										.getExecutionId()) != null) {
									// return to parent's OPD
									if (TestingSettings.getInstance().isMOVE_BETWEEN_OPD()
											&& TestingSettings.getInstance().isSHOW_GRAPHICS()) {
										if (!this.opdStack.isEmpty()) {
											long temp = ((Long) this.opdStack
													.pop()).longValue();
											lastShownOPD = (Opd) this.mySys
													.getCurrentIXOpd();
											this.mySys.showOPD(temp, false,
													false);
											// IXOpd parentOPD =
											// this.mySys.getCurrentIXOpd()
											// .getParentIXOpd();
											IXOpd parentOPD = lastShownOPD;
											Opd current = (Opd) this.mySys
													.getCurrentIXOpd();
											if (parentOPD != null) {

												if (((Opd) parentOPD)
														.getOpdContainer()
														.isMaximum()) {
													try {
														current
																.getOpdContainer()
																.setMaximum(
																		true);
													} catch (java.beans.PropertyVetoException pve) {
														OpcatLogger
																.logError(pve);
													}
												} else {
													current
															.getOpdContainer()
															.setSize(
																	((Opd) parentOPD)
																			.getOpdContainer()
																			.getSize());
												}
											} else {
												// if
												// (current.getOpdContainer().isMaximumSizeSet())
												// {
												try {
													current.getOpdContainer()
															.setMaximum(true);
												} catch (Exception ex) {
													OpcatLogger.logError(ex);
												}
												// }
											}
										}
									}
									// deactivate parent
									TestingEvent termEvent = new TestingEvent(
											aParentProcess
													.getCurrentEvent(currentEvent
															.getExecutionId()));
									termEvent.setStep(this.currentStep);
									termEvent
											.setSchedulingStep(this.currentStep);
									termEvent.setNumberOfInstances(1);
									this.deactivate(aParentProcess, termEvent);
								}
							}
						}
					}
				}
			}
		}
	}

	// this function treats object termination consequences
	// it deactivates all the states of the terminated object
	private void treatObjectTerminationConsequences(TestingEntry aObj,
			TestingEvent currentEvent) {
		// if there are no more active instances -> deactivate objects state
		IXObjectEntry terminatedObject = (IXObjectEntry) aObj.getIXEntry();
		if (aObj.isActive() == false) {
			IXObjectInstance objIns = (IXObjectInstance) (terminatedObject
					.getInstances().nextElement());
			Enumeration en = objIns.getStateInstances();
			for (; en.hasMoreElements();) {
				IXStateEntry state = (IXStateEntry) ((IXStateInstance) en
						.nextElement()).getIXEntry();
				TestingEntry aState = this.findEntryById(state.getId());
				if (aState.isActive()) {
					this.deactivate(aState);
				}
			}
		}
		// if terminated object is a specialization of another object
		// terminate its generalization as well
		TestingEvent aEventTemp = new TestingEvent();
		aEventTemp.setEventType(TestingSettings.getInstance().getTERMINATION_EVENT());
		this.treatGeneralization((IXThingEntry) aObj.getIXEntry(), aEventTemp);

		// treat exhibition relations
		this.treatExhibitions(aObj, false);
	}

	private void treatStateTerminationConsequences(
			TestingEntry terminatedState, TestingEvent currentEvent) {
		// treat state time out exception
		this.treatTimeoutsUponTermination(terminatedState, currentEvent);
	}

	// //////////////////////////////
	// Testing tasks classes //
	// //////////////////////////////

	// generic task interface
	private interface Task {
		public void run(IXEntry iEntry);

		public boolean validate(IXEntry iEntry, Enumeration enAllEntries);
	}

	private class AnimateResultsTask implements Task {
		TestingEntry aProcess;

		TestingEvent aEvent;

		public AnimateResultsTask() {
		}

		public AnimateResultsTask(TestingEntry aProcess, TestingEvent aEvent) {
			this.aProcess = aProcess;
			this.aEvent = aEvent;
		}

		public boolean validate(IXEntry iEntry, Enumeration en) {
			return true;
		}

		public void run(IXEntry iEntry) {
			IXLinkEntry link = (IXLinkEntry) iEntry;
			// TestingAuxiliary.debug("Running AnimateResultsTask");
			if ((this.aProcess.getPath().equals(""))
					|| (link.getPath().equals(this.aProcess.getPath()))
					|| (link.getPath().equals(""))) {
				// if process was activated without path,
				// and one of the links has path - set it to be the process
				// path.
				if (this.aProcess.getPath().equals("")
						&& !link.getPath().equals("")) {
					this.aProcess.setPath(link.getPath());
					this.aProcess.setPath(link.getPath(), this.aEvent
							.getExecutionId());
				}
				// animate
				TestingSystem.this.findEntryById(link.getId()).animate(
						this.aEvent);
				TestingSystem.this.findEntryById(link.getDestinationId())
						.animate(this.aEvent);
			}
		}
	}

	private class DeactivateConsumedEntriesTask implements Task {
		TestingEntry aProcess;

		TestingEvent aEvent;

		public DeactivateConsumedEntriesTask(TestingEntry aProcess,
				TestingEvent aEvent) {
			this.aProcess = aProcess;
			this.aEvent = aEvent;
		}

		public boolean validate(IXEntry iEntry, Enumeration enAllEntries) {
			IXLinkEntry link = (IXLinkEntry) iEntry;
			boolean retVal = TestingSystem.this.checkLinkBeforeActivate(
					this.aProcess, link);
			// TestingGridAddRow(TestingAuxiliary.ActionActivate, link,
			// retVal,link.getName() + "(Validate) - " + str);
			if (retVal) {
				// check logical relation
				if (TestingSystem.this.checkPath(link.getPath(), enAllEntries)) {
					return true;
				}
			}
			return false;
		}

		public void run(IXEntry iEntry) {
			IXLinkEntry link = (IXLinkEntry) iEntry;
			if ((this.aProcess.getPath().equals(""))
					|| (this.aProcess.getPath().equals(link.getPath()))
					|| (link.getPath().equals(""))) {
				// animate
				// TestingAuxiliary.debug("animate consumption link " +
				// link.getName());
				// TestingAuxiliary.debug("duration = " +
				// aEvent.getDuration());
				// TestingAuxiliary.debug("Current time " +
				// System.currentTimeMillis());
				TestingSystem.this.findEntryById(link.getId()).animate(
						this.aEvent);
				// deactivate the source object
				TestingSystem.this.deactivate(TestingSystem.this
						.findEntryById(link.getSourceId()), this.aEvent);
			}
		}

	}

	private class ActivateResultsTask implements Task {
		TestingEntry aProcess;

		TestingEvent aEvent;

		public ActivateResultsTask(TestingEntry aProcess, TestingEvent aEvent) {
			this.aProcess = aProcess;
			this.aEvent = aEvent;
		}

		public boolean validate(IXEntry iEntry, Enumeration en) {
			return true;
		}

		public void run(IXEntry iEntry) {
			IXLinkEntry link = (IXLinkEntry) iEntry;
			if ((this.aProcess.getPath().equals(""))
					|| (link.getPath().equals(this.aProcess.getPath()))
					|| (link.getPath().equals(""))) {
				// activate the link if process is not marked with path
				// or if link's path is equal to the path stored on the process.
				TestingEntry aLink = TestingSystem.this.findEntryById(link
						.getId());
				if (TestingAuxiliary.isEvent(link)) {
					TestingSystem.this.activate(aLink, new TestingEvent(
							this.aEvent));
				} else {
					TestingEntry entry;
					entry = TestingSystem.this.findEntryById(link
							.getDestinationId());
					TestingSystem.this.activate(entry);
				}
			}
		}
	}

	private TestingGridPanel InitTestingGridPanel() {
		ArrayList<String> cols = new ArrayList<String>();
		cols.add("Step");
		cols.add("Thing Type");
		cols.add("OPD");
		cols.add("Thing Name");
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       