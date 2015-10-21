package gui.opdProject.consistency.globel.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JLabel;
import exportedAPI.opcatAPIx.IXSystem;

import gui.opdProject.Opd;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyAction;
import gui.opdProject.consistency.ConsistencyCheckerInterface;
import gui.opdProject.consistency.ConsistencyOptions;
import gui.opdProject.consistency.ConsistencyResult;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.util.opcatGrid.GridPanel;

public class ThingInZoomCheckerReport extends ConsistencyAbstractChecker
		implements ConsistencyCheckerInterface {

	GridPanel gridpanel;

	public ThingInZoomCheckerReport(ConsistencyOptions options,
			ConsistencyResult results) {
		super(options, results);

		ArrayList cols = new ArrayList();
		cols.add("Thing");
		cols.add("OPD");
		cols.add("Problem");

		Iterator addedEnum = this.getMyOptions().getInstences();
		String name = "";
		while (addedEnum.hasNext()) {
			Instance addedIns = (Instance) addedEnum.next();
			name = addedIns.getEntry().getName() + " " + name;
		}
		gridpanel = new GridPanel(cols);
		gridpanel.setTabName(name + " Consistency");
		gridpanel.setInstanceTag((IXSystem) options.getProject());
		gridpanel.getGrid().setDuplicateRows(true);
		gridpanel.getButtonPane().add(new JLabel(""));
		gridpanel.getButtonPane().add(new JLabel(""));
		gridpanel.getButtonPane().add(new JLabel(""));
		gridpanel.RemoveFromExtensionToolsPanel();

	}

	public void check() {

		Iterator addedEnum = this.getMyOptions().getInstences();
		while (addedEnum.hasNext()) {
			Instance addedIns = (Instance) addedEnum.next();
			if (addedIns instanceof ThingInstance) {
				ThingInstance ti = (ThingInstance) addedIns;
				ThingEntry te = (ThingEntry) ti.getEntry();
				if (te.getZoomedInOpd() != null) {
					Opd zoomed = te.getZoomedInOpd();
					ArrayList outInstances = te
							.GetInZoomedNotIncludedInstances(zoomed);
					ArrayList outEntries = new ArrayList();
					Iterator iter = outInstances.iterator();
					while (iter.hasNext()) {
						ThingInstance thing = (ThingInstance) iter.next();
						outEntries.add(thing.getEntry());
					}

					/**
					 * all outInstances should be in the parent OPD.
					 */
					ArrayList parentInstances = Collections.list(getMyOptions()
							.getProject().getComponentsStructure()
							.getInstancesInOpd(ti.getOpd().getOpdId()));

					ArrayList parentEntries = new ArrayList();

					iter = outEntries.iterator();
					int i = -1;
					while (iter.hasNext()) {
						i++;
						ThingEntry thing = (ThingEntry) iter.next();
						/**
						 * if outInstances does include thing then add it to the
						 * bad arraylist
						 */
						Iterator parentIter = parentInstances.iterator();
						int count = 0;
						while (parentIter.hasNext()) {
							Instance parentInstance = (Instance) parentIter
									.next();

							Entry pe = parentInstance.getEntry();
							parentEntries.add(pe);
							if (pe.getId() == thing.getId()) {
								break;
							}
							count++;
						}
						/**
						 * there has to be two passes as we need also to eximane
						 * the ancestores of all bad objects, and see that they
						 * are not on the parent OPD, only then it should be
						 * marked as bad.
						 * 
						 * to do this it is better to prepare a few tree methods
						 * in a ThingEntry class to revel the Thing real
						 * structure, like get all his properties from the
						 * inharatince, get a list of his fatheres for a
						 * property,aggg etc....
						 */
						if (count == parentInstances.size()) {
							HashMap parents = ((ThingEntry) ((Instance) outInstances
									.get(i)).getEntry()).getAllParents();
							for (int j = 0; j < parentEntries.size(); j++) {
								Entry parentOPDThing = (Entry) parentEntries
										.get(j);

								if (parentOPDThing instanceof ThingEntry) {
									if (parents.containsKey(parentOPDThing)) {
										count = -1;
										break;
									}
								}
							}
						}

						/**
						 * end seconed pass
						 */
						if (count == parentInstances.size()) {
							Instance[] insArray = new Instance[1];
							insArray[0] = (Instance) outInstances.get(i);
							ConsistencyAction action = new ConsistencyAction(
									ConsistencyAction.GLOBAL_THING_REPORT,
									zoomed, (Instance) outInstances.get(i),
									insArray);
							this.getResults().setAction(action);
						}
					}
				}
			}
		}
	}

	public void deploy(ConsistencyResult checkResult) {
		gridpanel.RemoveFromExtensionToolsPanel();
		gridpanel.ClearData();

		Vector results = checkResult.getActions();
		for (int i = 0; i < results.size(); i++) {
			ConsistencyAction action = (ConsistencyAction) results.get(i);
			if (action.getMyType() == ConsistencyAction.GLOBAL_THING_REPORT) {
				ThingInstance thing = (ThingInstance) action
						.getMySourceInstance();
				/**
				 * here we add a row to the panel
				 */
				Object[] row = new Object[3];
				row[0] = thing.getEntry().getName();
				row[1] = thing.getOpd().getName();
				row[2] = "Thing not in parent OPD";

				Object[] rowTag = new Object[2];
				rowTag[0] = thing.getKey();
				rowTag[1] = new Long(thing.getEntry().getId());

				gridpanel.getGrid().addRow(row, rowTag);

			}
		}
		gridpanel.AddToExtensionToolsPanel();
	}

	public boolean isDeploy() {
		return true;
	}

	public boolean isStoping() {
		return false;
	}

}
