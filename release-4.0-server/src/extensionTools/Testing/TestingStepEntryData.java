package extensionTools.Testing;

import java.awt.Color;
import java.util.Iterator;

import exportedAPI.opcatAPIx.IXLinkEntry;
import exportedAPI.opcatAPIx.IXObjectEntry;
import gui.opmEntities.OpmObject;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ThingEntry;

public class TestingStepEntryData {

	private boolean animated = false;
	private String entryName = "";
	private long entryID = -1;
	private Color entryColor = null;
	private boolean hasStates = false;
	private String stateName = "";
	private boolean object = false;
	private double measurementUnitAcommulatedValue = 0;
	int numberOfInstances = 0;

	public TestingStepEntryData(TestingEntry entry) {

		this.animated = isAnimated(entry);
		this.entryName = entry.getIXEntry().getName();
		this.entryID = entry.getIXEntry().getId();
		this.entryColor = getColor(entry);
		if (entry.getIXEntry() instanceof ObjectEntry) {
			ObjectEntry object = (ObjectEntry) entry.getIXEntry();
			hasStates = object.hasStates();
			stateName = getStateName(entry);
		}
		if (entry.getIXEntry() instanceof IXObjectEntry) {
			object = true;
			measurementUnitAcommulatedValue = entry
					.getMeasurementUnitAcommulatedValue();
		}

		numberOfInstances = entry.getIXEntry().getInstancesNumber();

	}

	private String getStateName(TestingEntry entry) {
		Iterator<TestingEntry> iter = entry.getStatesTestingEntreies()
				.iterator();
		while (iter.hasNext()) {
			TestingEntry state = iter.next();

			if (state.isOnLine()) {
				return state.getIXEntry().getName();
			}
		}

		return "";
	}

	private boolean isAnimated(TestingEntry entry) {

		return entry.isOnLine();
	}

	private Color getColor(TestingEntry entry) {

		if (entry.getIXEntry() instanceof ThingEntry) {

			Color color = new Color(230, 230, 230);

			if (entry.isOnLine()) {
				if (entry.getIXEntry() instanceof ObjectEntry) {
					color = new Color(127, 182, 127);
				}
				if (entry.getIXEntry() instanceof ProcessEntry) {
					color = new Color(127, 127, 212);
				}
			}

			double resource = 0;
			if (entry.getIXEntry() instanceof ObjectEntry) {
				OpmObject opm = (OpmObject) ((ObjectEntry) entry.getIXEntry())
						.getLogicalEntity();
				resource = entry.getMeasurementUnitAcommulatedValue();
				if ((resource <= opm.getMesurementUnitMinValue())
						|| (resource >= opm.getMesurementUnitMaxValue())) {
					color = Color.RED;
				}
			}

			return color;
		}

		if (entry.getIXEntry() instanceof IXLinkEntry) {
			LinkInstance instance = (LinkInstance) entry.getIXEntry()
					.getInstances().nextElement();
			return instance.getBackgroundColor();
		}

		return null;
	}

	public boolean isAnimated() {
		return animated;
	}

	public String getEntryName() {
		return entryName;
	}

	public long getEntryID() {
		return entryID;
	}

	public Color getEntryColor() {
		return entryColor;
	}

	public boolean isHasStates() {
		return hasStates;
	}

	public String getStateName() {
		return stateName;
	}

	public boolean isObject() {
		return object;
	}

	public double getMeasurementUnitAcommulatedValue() {
		return measurementUnitAcommulatedValue;
	}

	public int getNumberOfInstances() {
		return numberOfInstances;
	}

	// for each entry we need
	// is animated in this step
	// acumulated resource in this step
	// if object then - the active state id (or name ?)

}
