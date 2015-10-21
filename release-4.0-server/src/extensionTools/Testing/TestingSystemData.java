package extensionTools.Testing;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

public class TestingSystemData extends TreeMap<Long, TestingStepData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestingSystemData() {
		super();
	}

	public void addStep(TestingSystem sys, long currentStep) {

		TestingStepData step = new TestingStepData(currentStep);
		Vector<TestingEntry> testingEntries = sys.getTestingElements();
		Iterator<TestingEntry> entries = testingEntries.iterator();

		while (entries.hasNext()) {
			TestingEntry entry = entries.next();
			step.addEntryData(entry);
		}

		this.put(new Long(currentStep), step);
	}

	public void removeStep(long step) {
		this.remove(new Long(step));
	}

}
