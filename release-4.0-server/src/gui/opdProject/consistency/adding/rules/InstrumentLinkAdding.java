package gui.opdProject.consistency.adding.rules;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JOptionPane;

import util.Configuration;

import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyAction;
import gui.opdProject.consistency.ConsistencyCheckerInterface;
import gui.opdProject.consistency.ConsistencyOptions;
import gui.opdProject.consistency.ConsistencyResult;
import gui.opmEntities.OpmConditionLink;
import gui.opmEntities.OpmConsumptionLink;
import gui.opmEntities.OpmEffectLink;
import gui.opmEntities.OpmInstrument;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmResultLink;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;

public class InstrumentLinkAdding extends ConsistencyAbstractChecker implements
		ConsistencyCheckerInterface {

	public boolean isStoping() {
		return false;
	}

	public InstrumentLinkAdding(ConsistencyOptions options,
			ConsistencyResult results) {
		super(options, results);
	}

	public void check() {

		boolean found = false;
		Iterator addedEnum = this.getMyOptions().getInstences();
		while (addedEnum.hasNext()) {
			Instance addedIns = (Instance) addedEnum.next();
			Object entry = addedIns.getEntry().getLogicalEntity();
			if ((entry instanceof OpmInstrument)
					|| (entry instanceof OpmConditionLink)
					|| (entry instanceof OpmConsumptionLink)
					|| (entry instanceof OpmEffectLink)) {
				OpmProceduralLink addedLink = null;
				if (entry instanceof OpmInstrument) {
					addedLink = (OpmInstrument) addedIns.getEntry()
							.getLogicalEntity();
				}
				if (entry instanceof OpmConditionLink) {
					addedLink = (OpmConditionLink) addedIns.getEntry()
							.getLogicalEntity();
				}
				if (entry instanceof OpmConsumptionLink) {
					addedLink = (OpmConsumptionLink) addedIns.getEntry()
							.getLogicalEntity();
				}
				if (entry instanceof OpmEffectLink) {
					addedLink = (OpmEffectLink) addedIns.getEntry()
							.getLogicalEntity();
				}
				LinkInstance linkIns = (LinkInstance) addedIns;

				if (addedLink != null) {
					OpdConnectionEdge source = linkIns.getSource();
					if ((linkIns.getSourceInstance() instanceof StateInstance)
							&& !((StateEntry) source.getEntry()).isInitial()) {
						Enumeration sourceEnum = source.getEntry()
								.getInstances();
						while (sourceEnum.hasMoreElements() && !found) {
							ConnectionEdgeInstance connectionEdgeInstance = (ConnectionEdgeInstance) sourceEnum
									.nextElement();
							ConnectionEdgeEntry connectionEdgeEntry = (ConnectionEdgeEntry) connectionEdgeInstance
									.getEntry();
							Enumeration linksEnum = connectionEdgeEntry
									.getDestinationLinks();
							while (linksEnum.hasMoreElements() && !found) {
								OpmProceduralLink link = (OpmProceduralLink) linksEnum
										.nextElement();
								if ((link instanceof OpmResultLink)) {
									// this means we have a result link
									found = true;
								}
							}
						}
						// if we got to here then no result link was found
						// so we add a dummy action just to tell deploy that
						// it's not consistsent
						if (!found) {
							Instance[] insArray = new Instance[1];
							insArray[0] = addedIns;
							ConsistencyAction action = new ConsistencyAction(
									ConsistencyAction.INSTRUMENT_LINKS_ADDITION,
									linkIns.getSourceInstance().getOpd(),
									linkIns.getSourceInstance(), insArray);
							this.getResults().setAction(action);
						}
					}
				}
			}
		}
	}

	public void deploy(ConsistencyResult checkResult) {
		Vector results = checkResult.getActions();
		for (int i = 0; i < results.size(); i++) {
			ConsistencyAction action = (ConsistencyAction) results.get(i);
			if (action.getMyType() == ConsistencyAction.INSTRUMENT_LINKS_ADDITION) {
				JOptionPane
						.showMessageDialog(
								this.getMyOptions().getProject().getMainFrame(),
								"The State is not a Result, The condition may never be true",
								"OPCAT II warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
	}

	public boolean isDeploy() {
		/**
		 * turn off deployment of this rule
		 */
		String str = Configuration.getInstance().getProperty(
				"show_state_not_result_rule");
		if ((str != null) && (str.equalsIgnoreCase("yes"))) {
			return true;
		} else {
			return false;
		}
	}

}
