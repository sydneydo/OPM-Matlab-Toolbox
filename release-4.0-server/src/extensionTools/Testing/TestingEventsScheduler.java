package extensionTools.Testing;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/*
 TestingEventsScheduler class is a table of events scheduled for each step.
 It also contains various function to add, remove and search for a specific event
 */

class TestingEventsScheduler extends Hashtable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8468402460638500301L;

	// This hashtable holds scheduled testing events
	// Key is a step
	// Value is a list of events scheduled to this step
	public TestingEventsScheduler() {
	}

	public void scheduleEvent(TestingEvent aEvent) {
		Long key = new Long(aEvent.getStep());
		Vector value = null;
		if (this.containsKey(key)) {
			value = (Vector) this.get(key);
		} else {
			value = new Vector();
		}
		value.add(aEvent); // append to the end - FIFO
		this.put(key, value);
	}

	public void scheduleEvent(long currentStep, long eventStep) {
		TestingEvent aEvent = new TestingEvent();
		aEvent.setStep(eventStep);
		aEvent.setSchedulingStep(currentStep);
		aEvent.setExecutionId();
		this.scheduleEvent(aEvent);
	}

	public Enumeration getScheduledEvents(long step) {
		Long key = new Long(step);
		if (this.containsKey(key)) {
			Vector value = (Vector) this.get(key);
			return value.elements(); // check that event is not handled and
										// mark as handled
		}
		return (new Vector()).elements(); // empty enumeration
	}

	public void removeScheduledEvent(long step) {
		Long key = new Long(step);
		Vector value = null;
		if (this.containsKey(key)) {
			value = (Vector) this.get(key);
			value.remove(value.firstElement()); // remove first - FIFO
			// if there are no more events scheduled to this step - remove the
			// key
			// from the hash table
			if (value.isEmpty()) {
				this.remove(key);
			}
		}
	}

	// removes all events scheduled after the given step
	// used for undo with execution id = 0
	// used aslo to remove events scheduled for certain execution
	public void removeAllScheduledEvents(long schedulingStep, long executionId) {
		Enumeration en = this.elements();
		for (; en.hasMoreElements();) {
			Vector v = (Vector) en.nextElement();
			Long key = null;
			// go through the vector in backward order and remove the events
			// that were scheduled later than schedulingStep
			for (int i = v.size() - 1; i >= 0; i--) {
				TestingEvent aEvent = (TestingEvent) v.get(i);
				key = new Long(aEvent.getStep());
				// remove the events that were scheduled later than
				// schedulingStep
				if (aEvent.getSchedulingStep() > schedulingStep) {
					if ((executionId == 0)
							|| (executionId == aEvent.getExecutionId())) {
						TestingEvent aRemovedEvent = (TestingEvent) v
								.remove(i);
						// prevent rescheduling of the removed event if it
						// already
						// was taken by the nextStep function
						if (aRemovedEvent.getRecurringInd()) {
							aRemovedEvent.setRecurringInd(false);
						}
					}
				}

				// restore the events that were cancelled later than
				// schedulingStep
				if (aEvent.getCancelStep() > schedulingStep) {
					if ((executionId == 0)
							|| (executionId == aEvent.getExecutionId())) {
						aEvent.setCancelStep(0); // the event is not
													// cancelled
					}
				}
			}
			// remove the vector if it is empty
			if (v.isEmpty()) {
				this.remove(key);
			}
		}
	}


	// this function checks whether there are events
	// scheduled on or after given step
	public TestingEvent futureEventsScheduled(long currentStep,
			long executionId) {
		Enumeration en = this.keys();
		for (; en.hasMoreElements();) {
			Long key = (Long) en.nextElement();
			if (key.longValue() >= currentStep) {
				Vector v = (Vector) this.get(key);
				for (int i = 0; i < v.size(); i++) {
					TestingEvent aEvent = (TestingEvent) v.get(i);
					if ((aEvent.getExecutionId() == executionId)
							&& (aEvent.getEventType() != TestingSettings.getInstance().getTERMINATION_EVENT())) {
						return aEvent;
					}
				}
			}
		}
		return null;
	}
}