package extensionTools.Testing;

import java.util.HashMap;

public class TestingStepData {

	private long myStep = -1;

	private HashMap<Long, TestingStepEntryData> data = new HashMap<Long, TestingStepEntryData>();

	public TestingStepData(long step) {
		this.myStep = step;
	}

	public void addEntryData(TestingEntry entry) {
		removeEntryData(entry);
		TestingStepEntryData entryData = new TestingStepEntryData(entry);
		Long EntryID = new Long(entry.getIXEntry().getId());
		data.put(EntryID, entryData);
	}

	public TestingStepEntryData getEntryData(TestingEntry entry) {
		return data.get(new Long(entry.getIXEntry().getId()));
	}

	private void removeEntryData(TestingEntry entry) {
		Long EntryID = new Long(entry.getIXEntry().getId());
		if (getEntryData(entry) != null) {
			data.remove(EntryID);
		}

	}

	public long getStep() {
		return myStep;
	}

}
