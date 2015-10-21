package gui.checkModule;

import java.util.Enumeration;

import util.Configuration;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPIx.IXCheckResult;
import expose.OpcatExposeChangeResult;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_CHANGE_TYPE;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.Constants;
import gui.opmEntities.OpmProceduralLink;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmState;
import gui.opmEntities.OpmStructuralRelation;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;

public class CheckModule {
	private static int[][][] simpleRuleTable = {
			{ { 3, 0, 0 }, { 0, 3, 0 }, { 0, 0, 0 } }, // GENERALIZATION
			{ { 4, 1, 0 }, { 1, 1, 0 }, { 0, 0, 0 } }, // FEATURING
			{ { 2, 0, 0 }, { 0, 2, 0 }, { 0, 0, 0 } }, // INSTANTINATION
			{ { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } }, // AGGREGATION
			{ { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } }, // UNI_DIRECTIONAL
			{ { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } }, // BI_DIRECTIONAL
			{ { 0, 1, 0 }, { 0, 0, 0 }, { 0, 1, 0 } }, // CONSUMPTION
			{ { 0, 1, 0 }, { 1, 0, 0 }, { 0, 0, 0 } }, // EFFECT
			{ { 0, 1, 0 }, { 0, 0, 0 }, { 0, 1, 0 } }, // INSTRUMENT
			{ { 0, 1, 0 }, { 0, 0, 0 }, { 0, 1, 0 } }, // CONDITION
			{ { 0, 5, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, // AGENT
			{ { 0, 0, 0 }, { 1, 0, 1 }, { 0, 0, 0 } }, // RESULT
			{ { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } }, // INVOCATION
			{ { 0, 1, 0 }, { 0, 0, 0 }, { 0, 1, 0 } }, // EVENT
			{ { 0, 0, 0 }, { 0, 1, 0 }, { 0, 1, 0 } }, // EXCEPTION
			{ { 0, 1, 0 }, { 0, 0, 0 }, { 0, 1, 0 } } };// INSTRUMENT_EVENT

	private static int _mapConstant2Index(int c) {
		switch (c) {
		case OpcatConstants.SPECIALIZATION_RELATION:
			return 0;
		case OpcatConstants.EXHIBITION_RELATION:
			return 1;
		case OpcatConstants.INSTANTINATION_RELATION:
			return 2;
		case OpcatConstants.AGGREGATION_RELATION:
			return 3;
		case OpcatConstants.UNI_DIRECTIONAL_RELATION:
			return 4;
		case OpcatConstants.BI_DIRECTIONAL_RELATION:
			return 5;
		case OpcatConstants.CONSUMPTION_LINK:
			return 6;
		case OpcatConstants.EFFECT_LINK:
			return 7;
		case OpcatConstants.INSTRUMENT_LINK:
			return 8;
		case OpcatConstants.CONDITION_LINK:
			return 9;
		case OpcatConstants.AGENT_LINK:
			return 10;
		case OpcatConstants.RESULT_LINK:
			return 11;
		case OpcatConstants.INVOCATION_LINK:
			return 12;
		case OpcatConstants.INSTRUMENT_EVENT_LINK:
			return 13;
		case OpcatConstants.EXCEPTION_LINK:
			return 14;
		case OpcatConstants.CONSUMPTION_EVENT_LINK:
			return 15;

		default:
			return -1;
		}
	}

	// private String generateMessage(){return "";}

	public static CheckResult checkCommand(CommandWrapper cw) {
		int err_cnt = 1;

		CheckResult tmpRes;
		CheckResult cr = new CheckResult(IXCheckResult.RIGHT);

		int type = _mapConstant2Index(cw.getType());
		if (type < 0) {
			return new CheckResult(
					IXCheckResult.WRONG,
					"Serious internal problem.\nIllegal link or relation enum!!!\nCanceling command.");
		}
		int simpleRuleCheck = simpleRuleTable[type][cw.getSourceType()][cw
				.getDestinationType()];

		switch (simpleRuleCheck) {
		case 0:
			String message = "";

			if ((cw.getSourceEntry() instanceof ObjectEntry)
					&& (cw.getDestinationEntry() instanceof ObjectEntry)) {
				message = message + "Objects ";
			}

			if ((cw.getSourceEntry() instanceof ProcessEntry)
					&& (cw.getDestinationEntry() instanceof ProcessEntry)) {
				message = message + "Processes ";
			}

			if ((cw.getSourceEntry() instanceof StateEntry)
					&& (cw.getDestinationEntry() instanceof StateEntry)) {
				message = message + "States ";
			}

			if (!message.equals("")) {
				message += "cannot be connected";
			}

			if (cw.getSourceType() != cw.getDestinationType()) {
				message = message
						+ Constants.a_anSelector(cw.getConstantsSourceType(),
								true)
						+ " "
						+ Constants.type2String(cw.getConstantsSourceType())
						+ " and "
						+ Constants.a_anSelector(cw
								.getConstantsDestinationType(), false)
						+ " "
						+ Constants.type2String(cw
								.getConstantsDestinationType())
						+ " cannot be connected";
			}

			message = message + " with "
					+ Constants.a_anSelector(cw.getType(), false) + " "
					+ Constants.type2String(cw.getType()) + ".";

			cr = new CheckResult(IXCheckResult.WRONG, message);
			break;

		case 1:
			tmpRes = _singleLinkOrRelationBetweenTwoEntities(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _noLinkOrRelationOfSingleInstanceToItself(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _noConnectionOfTwoInstancesOfTheSameThing(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _funadamentalFromInstance(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _timeConstraintInException(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			break;

		case 2:
			tmpRes = _singleLinkOrRelationBetweenTwoEntities(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _singleParentInInstantiation(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _funadamentalFromInstance(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _noLinkOrRelationOfSingleInstanceToItself(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _noConnectionOfTwoInstancesOfTheSameThing(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			break;

		case 3:
			tmpRes = _singleLinkOrRelationBetweenTwoEntities(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _cyclicInheritanceInGeneralization(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _noLinkOrRelationOfSingleInstanceToItself(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _noConnectionOfTwoInstancesOfTheSameThing(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _funadamentalFromInstance(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _instanceCannotBeInherited(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			// warning
			tmpRes = _multiParentInGeneralization(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			break;

		case 4:
			tmpRes = _cyclicFeaturingRelation(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _singleLinkOrRelationBetweenTwoEntities(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _noLinkOrRelationOfSingleInstanceToItself(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _noConnectionOfTwoInstancesOfTheSameThing(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			tmpRes = _funadamentalFromInstance(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);

			break;

		case 5:
			tmpRes = _singleAgentBetweenTwoEntities(cw);
			err_cnt = _concatMessage(cr, tmpRes, err_cnt);
			break;

		default:
			cr = new CheckResult(IXCheckResult.WRONG,
					"This is not allowed in OPM.");
			break;
		}
		return cr;
	}

	private static int _concatMessage(CheckResult cr, CheckResult tmpRes,
			int err_cnt) {
		if ((tmpRes.getResult() == IXCheckResult.WRONG)
				|| (tmpRes.getResult() == IXCheckResult.WARNING)) {
			if (err_cnt > 1) {
				cr.setMessage(cr.getMessage() + "\n");
			}
			// cr.setMessage(cr.getMessage() + err_cnt + ". " +
			// tmpRes.getMessage());
			cr.setMessage(cr.getMessage() + tmpRes.getMessage());
			cr.setResult(tmpRes.getResult());
			err_cnt++;
		}
		return err_cnt;
	}

	/***************************************************************************
	 * Start of compound rules methods
	 **************************************************************************/

	/**
	 * Single parent for instance
	 */
	private static CheckResult _singleParentInInstantiation(CommandWrapper cw) {
		ConnectionEdgeEntry entry = cw.getDestinationEntry();
		Enumeration e = entry
				.getDestinationRelations(OpcatConstants.INSTANTINATION_RELATION);
		if (e.hasMoreElements()) {
			return new CheckResult(IXCheckResult.WRONG,
					"An instance cannot be instantiated from more than one class.");
		}
		return new CheckResult(IXCheckResult.RIGHT);
	}

	/**
	 * Multiple Inheritance in Generalization-Specialization relation (Warning).
	 */
	private static CheckResult _timeConstraintInException(CommandWrapper cw) {
		if (cw.getType() != OpcatConstants.EXCEPTION_LINK) {
			return new CheckResult(IXCheckResult.RIGHT);
		}

		String sourceType = "";
		String time = "";
		ConnectionEdgeEntry sEntry = cw.getSourceEntry();

		if (sEntry instanceof ProcessEntry) {
			sourceType = "process";
			time = ((OpmProcess) sEntry.getLogicalEntity())
					.getMaxTimeActivation();
		}

		if (sEntry instanceof StateEntry) {
			sourceType = "state";
			time = ((OpmState) sEntry.getLogicalEntity()).getMaxTime();
		}

		if (time.equalsIgnoreCase("infinity")) {
			return new CheckResult(IXCheckResult.WRONG, " The source "
					+ sourceType + " should include max time constraints.");
		}

		return new CheckResult(IXCheckResult.RIGHT);
	}

	/**
	 * Multiple Inheritance in Generalization-Specialization relation (Warning).
	 */
	private static CheckResult _multiParentInGeneralization(CommandWrapper cw) {
		ConnectionEdgeEntry dEntry = cw.getDestinationEntry();
		ConnectionEdgeEntry sEntry = cw.getSourceEntry();
		MainStructure ms = cw.getProject().getComponentsStructure();
		Enumeration e = dEntry
				.getDestinationRelations(OpcatConstants.SPECIALIZATION_RELATION);
		if (e.hasMoreElements()) {
			OpmStructuralRelation sr = (OpmStructuralRelation) e.nextElement();
			ConnectionEdgeEntry tmpEntry = ms.getSourceEntry(sr);
			if (tmpEntry != sEntry) {
				return new CheckResult(IXCheckResult.WARNING,
						" Multiple inheritance is not supported in Java.");
			}
		}
		return new CheckResult(IXCheckResult.RIGHT);
	}

	/**
	 * Cyclic inheritance
	 */
	private static CheckResult _cyclicInheritanceInGeneralization(
			CommandWrapper cw) {
		if (_cyclicRelationLoc(cw.getDestinationEntry(), cw.getSourceEntry(),
				OpcatConstants.SPECIALIZATION_RELATION, cw.getProject()
						.getComponentsStructure()) == IXCheckResult.WRONG) {
			return new CheckResult(IXCheckResult.WRONG,
					"Cyclic inheritance is not allowed.");
		}
		return new CheckResult(IXCheckResult.RIGHT);
	}

	/**
	 * Cyclic featuring
	 */
	private static CheckResult _cyclicFeaturingRelation(CommandWrapper cw) {
		if (_cyclicRelationLoc(cw.getDestinationEntry(), cw.getSourceEntry(),
				OpcatConstants.EXHIBITION_RELATION, cw.getProject()
						.getComponentsStructure()) == IXCheckResult.WRONG) {
			return new CheckResult(IXCheckResult.WRONG,
					"Cyclic featuring is not allowed.");
		}
		return new CheckResult(IXCheckResult.RIGHT);
	}

	// recursive method fo cyclicRelation
	private static int _cyclicRelationLoc(ConnectionEdgeEntry entry,
			ConnectionEdgeEntry mainEntry, int relType, MainStructure ms) {
		Enumeration e = entry.getSourceRelations(relType);
		OpmStructuralRelation sr;
		ConnectionEdgeEntry ent = null;
		while (e.hasMoreElements()) {
			sr = (OpmStructuralRelation) e.nextElement();
			ent = ms.getDestinationEntry(sr);
			if (ent == mainEntry) {
				return IXCheckResult.WRONG;
			}
			return _cyclicRelationLoc(ent, mainEntry, relType, ms);
		}
		return IXCheckResult.RIGHT;
	}

	/**
	 * funamental relation from instance
	 */
	private static CheckResult _funadamentalFromInstance(CommandWrapper cw) {
		ConnectionEdgeEntry entry = cw.getSourceEntry();
		Enumeration e = entry
				.getDestinationRelations(OpcatConstants.INSTANTINATION_RELATION);
		if (e.hasMoreElements()) {
			String message = "This is not allowed in OPM.";
			if (cw.getType() == OpcatConstants.AGGREGATION_RELATION) {
				message = "An instance cannot aggregate things.";
			}
			if (cw.getType() == OpcatConstants.EXHIBITION_RELATION) {
				message = "An instance cannot have characteristics.";
			}
			if (cw.getType() == OpcatConstants.SPECIALIZATION_RELATION) {
				message = "An instance cannot generalize things.";
			}
			if (cw.getType() == OpcatConstants.INSTANTINATION_RELATION) {
				message = "An instance cannot instantiate another instance.";
			}

			return new CheckResult(IXCheckResult.WRONG, message);
		}
		return new CheckResult(IXCheckResult.RIGHT);
	}

	/**
	 * Only one fundamental relation between two entities
	 */
	private static CheckResult _singleFundamentalRelationBetweenTwoEntities(
			CommandWrapper cw, MainStructure ms) {
		if ((cw.getType() == OpcatConstants.BI_DIRECTIONAL_RELATION)
				|| (cw.getType() == OpcatConstants.UNI_DIRECTIONAL_RELATION)) {
			return new CheckResult(IXCheckResult.RIGHT);
		}

		ConnectionEdgeEntry sEntry = cw.getSourceEntry();
		ConnectionEdgeEntry dEntry = cw.getDestinationEntry();
		OpmStructuralRelation sr;
		ConnectionEdgeEntry tmpEntry;

		OpcatExposeChangeResult ret = cw.getProject().getExposeManager()
				.allowExposeChange(cw.getSourceInstance(),
						cw.getDestinationInstance(),
						OPCAT_EXPOSE_CHANGE_TYPE.ADD_RELATION);
		if (ret.isDenied()) {
			return new CheckResult(CheckResult.WRONG, ret.getReaseon());
		} else if (ret.isWarning()) {
			return new CheckResult(CheckResult.WARNING, ret.getReaseon());
		}

		for (Enumeration e = sEntry.getSourceRelations(); e.hasMoreElements();) {
			sr = (OpmStructuralRelation) e.nextElement();
			tmpEntry = ms.getDestinationEntry(sr);
			if (tmpEntry.getId() == dEntry.getId()) {
				if (cw.getType() != Constants.getType4Relation(sr)) {
					return new CheckResult(IXCheckResult.WRONG,
							"Two things cannot be connected with more then one fundamental relation.");
				} else // check that there are different instances if
				// at least one instance is the same it's illegal
				{
					if (cw.isCheckGraphic()) {
						OpdConnectionEdge sThing = cw.getSourceInstance()
								.getConnectionEdge();
						OpdConnectionEdge dThing = cw.getDestinationInstance()
								.getConnectionEdge();
						FundamentalRelationEntry sre = (FundamentalRelationEntry) ms
								.getEntry(sr.getId());
						for (Enumeration e1 = sre.getInstances(); e1
								.hasMoreElements();) {
							FundamentalRelationInstance fri = (FundamentalRelationInstance) e1
									.nextElement();
							if ((fri.getDestination() == dThing)
									|| (fri
											.getGraphicalRelationRepresentation()
											.getSource() == sThing)) {
								return new CheckResult(IXCheckResult.WRONG,
										"Two things cannot be connected with more then one fundamental relation.");
							}
						}
					}
				}
			}
		}

		return new CheckResult(IXCheckResult.RIGHT);
	}

	private static CheckResult _singleAgentBetweenTwoEntities(
			CommandWrapper cw, MainStructure ms) {
		ConnectionEdgeEntry sEntry = cw.getSourceEntry();
		ConnectionEdgeEntry dEntry = cw.getDestinationEntry();
		OpmProceduralLink pl;
		ConnectionEdgeEntry tmpEntry;

		OpcatExposeChangeResult ret = cw.getProject().getExposeManager()
				.allowExposeChange(cw.getSourceInstance(),
						cw.getDestinationInstance(),
						OPCAT_EXPOSE_CHANGE_TYPE.ADD_LINK);
		if (ret.isDenied()) {
			return new CheckResult(CheckResult.WRONG, ret.getReaseon());
		} else if (ret.isWarning()) {
			return new CheckResult(CheckResult.WARNING, ret.getReaseon());
		}

		for (Enumeration e = sEntry.getSourceLinks(); e.hasMoreElements();) {
			pl = (OpmProceduralLink) e.nextElement();
			tmpEntry = ms.getDestinationEntry(pl);
			if (tmpEntry.getId() == dEntry.getId()) {
				if ((cw.getType() != OpcatConstants.AGENT_LINK)
						&& (cw.getType() != Constants.getType4Link(pl))) // the
				// same
				// link
				// type
				{
					return new CheckResult(IXCheckResult.WRONG,
							"Two things cannot be connected with more than one procedural link.");
				} else // check that there are different instances if
				// at least one instance is the same it's illegal
				{
					if (cw.isCheckGraphic()) {
						OpdConnectionEdge sThing = cw.getSourceInstance()
								.getConnectionEdge();
						OpdConnectionEdge dThing = cw.getDestinationInstance()
								.getConnectionEdge();
						LinkEntry sre = (LinkEntry) ms.getEntry(pl.getId());
						for (Enumeration e1 = sre.getInstances(); e1
								.hasMoreElements();) {
							LinkInstance li = (LinkInstance) e1.nextElement();
							if (Constants.getType4Link(li.getLink()) != OpcatConstants.AGENT_LINK)
								continue;
							if ((li.getDestination() == dThing)
									|| (li.getSource() == sThing)) {
								return new CheckResult(IXCheckResult.WRONG,
										"Two things cannot be connected with more than one procedural link.");
							}
						}
					}
				}
			}
		}

		return new CheckResult(CheckResult.RIGHT);
	}

	/**
	 * Only one fundamental relation between two entities
	 */
	private static CheckResult _singleLinkBetweenTwoEntities(CommandWrapper cw,
			MainStructure ms) {
		ConnectionEdgeEntry sEntry = cw.getSourceEntry();
		ConnectionEdgeEntry dEntry = cw.getDestinationEntry();
		OpmProceduralLink pl;
		ConnectionEdgeEntry tmpEntry;

		OpcatExposeChangeResult ret = cw.getProject().getExposeManager()
				.allowExposeChange(cw.getSourceInstance(),
						cw.getDestinationInstance(),
						OPCAT_EXPOSE_CHANGE_TYPE.ADD_LINK);
		if (ret.isDenied()) {
			return new CheckResult(CheckResult.WRONG, ret.getReaseon());
		} else if (ret.isWarning()) {
			return new CheckResult(CheckResult.WARNING, ret.getReaseon());
		}

		for (Enumeration e = sEntry.getSourceLinks(); e.hasMoreElements();) {
			pl = (OpmProceduralLink) e.nextElement();
			tmpEntry = ms.getDestinationEntry(pl);
			if (tmpEntry.getId() == dEntry.getId()) {
				if (cw.getType() != Constants.getType4Link(pl)) // the
				// same
				// link
				// type
				{
					return new CheckResult(IXCheckResult.WRONG,
							"Two things cannot be connected with more than one procedural link.");
				} else // check that there are different instances if
				// at least one instance is the same it's illegal
				{
					if (cw.isCheckGraphic()) {
						OpdConnectionEdge sThing = cw.getSourceInstance()
								.getConnectionEdge();
						OpdConnectionEdge dThing = cw.getDestinationInstance()
								.getConnectionEdge();
						LinkEntry sre = (LinkEntry) ms.getEntry(pl.getId());
						for (Enumeration e1 = sre.getInstances(); e1
								.hasMoreElements();) {
							LinkInstance li = (LinkInstance) e1.nextElement();
							if ((li.getDestination() == dThing)
									|| (li.getSource() == sThing)) {
								return new CheckResult(IXCheckResult.WRONG,
										"Two things cannot be connected with more than one procedural link.");
							}
						}
					}
				}
			}
		}

		return new CheckResult(CheckResult.RIGHT);
	}

	private static CheckResult _singleLinkOrRelationBetweenTwoEntities(
			CommandWrapper cw) {
		CheckResult cr;
		MainStructure ms = cw.getProject().getComponentsStructure();

		if (OpcatConstants.isRelation(cw.getType())) {
			cr = _singleFundamentalRelationBetweenTwoEntities(cw, ms);
			if ((cr.getResult() == CheckResult.WRONG)
					|| (cr.getResult() == CheckResult.WARNING)) {
				return cr;
			}
		}

		if (OpcatConstants.isLink(cw.getType())) {

			cr = _singleLinkBetweenTwoEntities(cw, ms);
			if ((cr.getResult() == CheckResult.WRONG)
					|| (cr.getResult() == CheckResult.WARNING)) {
				return cr;
			}
		}

		return new CheckResult(CheckResult.RIGHT);
	}

	private static CheckResult _singleAgentBetweenTwoEntities(CommandWrapper cw) {
		CheckResult cr;
		MainStructure ms = cw.getProject().getComponentsStructure();

		cr = _singleAgentBetweenTwoEntities(cw, ms);
		if (cr.getResult() == CheckResult.WRONG) {
			return cr;
		}
		return new CheckResult(CheckResult.RIGHT);

	}

	/**
	 * single graphic instance cannot be linked/related to itself
	 */

	private static CheckResult _noLinkOrRelationOfSingleInstanceToItself(
			CommandWrapper cw) {
		if (cw.isCheckGraphic()) {
			if (cw.getSourceInstance() == cw.getDestinationInstance()) {
				return new CheckResult(CheckResult.WRONG,
						"A thing cannot be connected to itself.");
			}
		}
		return new CheckResult(CheckResult.RIGHT);
	}

	/**
	 * Two instances of the same thing cannot be connected, unless it is
	 * Aggregation-Participation relation or Invocation link
	 */

	private static CheckResult _noConnectionOfTwoInstancesOfTheSameThing(
			CommandWrapper cw) {
		if (cw.isCheckGraphic()) {
			if ((cw.getSourceEntry() == cw.getDestinationEntry())
					&& (cw.getSourceInstance() != cw.getDestinationInstance())
					&& (cw.getType() != OpcatConstants.AGGREGATION_RELATION)
					&& (cw.getType() != OpcatConstants.INVOCATION_LINK)) {
				String message = "Two instances of "
						+ cw.getSourceEntry().getLogicalEntity().getName()
								.replace('\n', ' ')
						+ " cannot be connected by "
						+ Constants.a_anSelector(cw.getType(), false) + " "
						+ Constants.type2String(cw.getType()) + ".";
				return new CheckResult(CheckResult.WRONG, message);
			}
		}
		return new CheckResult(CheckResult.RIGHT);
	}

	/**
	 * Instance cannot specialize anything (instance cannot be inherited)
	 */

	private static CheckResult _instanceCannotBeInherited(CommandWrapper cw) {
		if (cw.getType() == OpcatConstants.SPECIALIZATION_RELATION) {
			ConnectionEdgeEntry entry = cw.getDestinationEntry();
			Enumeration e = entry
					.getDestinationRelations(OpcatConstants.INSTANTINATION_RELATION);
			if (e.hasMoreElements()) {
				return new CheckResult(CheckResult.WRONG,
						"An instance cannot inherit from things");
			}
		}
		return new CheckResult(CheckResult.RIGHT);
	}

	/***************************************************************************
	 * End of compound rules methods
	 **************************************************************************/

	public static CheckResult checkStateAddition(Instance paranet,
			OpdProject myProject) {

		OpcatExposeChangeResult ret = myProject.getExposeManager()
				.allowExposeChange(paranet, OPCAT_EXPOSE_CHANGE_TYPE.ADD_STATE);
		if (ret.isDenied()) {
			return new CheckResult(CheckResult.WRONG, ret.getReaseon());
		} else if (ret.isWarning()) {
			return new CheckResult(CheckResult.WARNING, ret.getReaseon());
		}

		return new CheckResult(CheckResult.RIGHT);
	}

	public static CheckResult checkDeletion(Instance insToCheck,
			OpdProject myProject) {

		if ((insToCheck.getOpd() == null)
				|| (insToCheck.getOpd().getOpdId() == OpdProject.CLIPBOARD_ID)) {
			return new CheckResult(CheckResult.RIGHT);
		}

		if (insToCheck instanceof ThingInstance) {
			return _checkThingDeletion((ThingInstance) insToCheck, myProject);
		}

		if (insToCheck instanceof StateInstance) {
			return _checkStateDeletion((StateInstance) insToCheck, myProject);
		}

		if (insToCheck instanceof LinkInstance) {
			OpcatExposeChangeResult ret = myProject.getExposeManager()
					.allowExposeChange(insToCheck,
							OPCAT_EXPOSE_CHANGE_TYPE.DELETE_LINK);
			if (ret.isDenied()) {
				return new CheckResult(CheckResult.WRONG, ret.getReaseon());
			} else if (ret.isWarning()) {
				return new CheckResult(CheckResult.WARNING, ret.getReaseon());
			}
		}

		OpcatExposeChangeResult ret = myProject.getExposeManager()
				.allowExposeChange(insToCheck,
						OPCAT_EXPOSE_CHANGE_TYPE.DELETE_REALTION);
		if (insToCheck instanceof FundamentalRelationInstance) {
			if (ret.isDenied()) {
				return new CheckResult(CheckResult.WARNING, ret.getReaseon());
			}
		}

		// Deletion of links or relation possible without additional checks
		return new CheckResult(CheckResult.RIGHT);
	}

	public static CheckResult checkOpdDeletion(Opd opdToDelete,
			OpdProject myProject) {

		OpcatExposeChangeResult ret = myProject.getExposeManager()
				.allowExposeChange(opdToDelete,
						OPCAT_EXPOSE_CHANGE_TYPE.DELETE_OPD);

		if (ret.isDenied()) {
			return new CheckResult(CheckResult.WRONG, ret.getReaseon());
		} else if (ret.isWarning()) {
			return new CheckResult(CheckResult.WARNING, ret.getReaseon());
		}
		return new CheckResult(CheckResult.RIGHT);
	}

	public static CheckResult checkCut(Instance insToCheck, OpdProject myProject) {

		if (insToCheck instanceof ThingInstance) {
			return _checkThingCUT((ThingInstance) insToCheck, myProject);
		}

		if (insToCheck instanceof StateInstance) {
			String msg = "Cutting a state is not supported";
			return new CheckResult(CheckResult.WRONG, msg);
		}

		if (insToCheck instanceof LinkInstance) {

			OpcatExposeChangeResult ret = myProject.getExposeManager()
					.allowExposeChange(insToCheck,
							OPCAT_EXPOSE_CHANGE_TYPE.CUT_LINK);
			if (ret.isDenied()) {
				return new CheckResult(CheckResult.WRONG, ret.getReaseon());
			} else if (ret.isWarning()) {
				return new CheckResult(CheckResult.WARNING, ret.getReaseon());
			}

		}

		if (insToCheck instanceof FundamentalRelationInstance) {

			OpcatExposeChangeResult ret = myProject.getExposeManager()
					.allowExposeChange(insToCheck,
							OPCAT_EXPOSE_CHANGE_TYPE.CUT_RELATION);
			if (ret.isDenied()) {
				return new CheckResult(CheckResult.WARNING, ret.getReaseon());
			}

		}
		// Deletion of links or relation possible without additional checks
		return new CheckResult(CheckResult.RIGHT);
	}

	private static CheckResult _checkStateDeletion(StateInstance delInstance,
			OpdProject myProject) {
		StateEntry myEntry = (StateEntry) delInstance.getEntry();

		if (myEntry.isRelated()) {
			String msg = "State "
					+ delInstance.getEntry().getLogicalEntity().getName()
							.replace('\n', ' ')
					+ " cannot be removed\n"
					+ "since it is connected to another thing. First delete the relations\n"
					+ "and links associated with this state.";

			return new CheckResult(CheckResult.WRONG, msg);
		}

		OpcatExposeChangeResult ret = myProject.getExposeManager()
				.allowExposeChange(delInstance,
						OPCAT_EXPOSE_CHANGE_TYPE.DELETE_STATE);

		if (ret.isDenied()) {
			return new CheckResult(CheckResult.WRONG, ret.getReaseon());
		} else if (ret.isWarning()) {
			return new CheckResult(CheckResult.WARNING, ret.getReaseon());
		}

		return new CheckResult(CheckResult.RIGHT);

	}

	private static CheckResult _checkMainEntity(ThingInstance delInstance,
			OpdProject myProject) {

		String ENTITY = " ";

		if (delInstance instanceof ProcessInstance) {
			ENTITY = "Process ";
		} else if (delInstance instanceof ObjectInstance) {
			ENTITY = "Object ";
		}

		String msg = ENTITY
				+ delInstance.getEntry().getLogicalEntity().getName().replace(
						'\n', ' ') + " cannot be removed since it is ";

		Opd processOpd = myProject.getComponentsStructure().getOpd(
				delInstance.getKey().getOpdId());

		if ((processOpd != null)
				&& (processOpd.getMainInstance() != null)
				&& processOpd.getMainInstance().getKey().equals(
						delInstance.getKey())) {
			msg += "main entity in '" + processOpd.getName() + "'.\n"
					+ "You can only delete the whole OPD.";
			return new CheckResult(CheckResult.WRONG, msg);
		}
		return new CheckResult(CheckResult.RIGHT);

	}

	private static CheckResult _checkThingCUT(ThingInstance delInstance,
			OpdProject myProject) {

		// String ENTITY = " ";
		//
		// if (delInstance instanceof ProcessInstance) {
		// ENTITY = "Process ";
		// }
		// else if (delInstance instanceof ObjectInstance) {
		// ENTITY = "Object ";
		// }
		//
		//
		// String msg = ENTITY +
		// delInstance.getEntry().getLogicalEntity().getName().replace('\n',' ')
		// + " cannot be removed since it is ";

		CheckResult cr = _checkMainEntity(delInstance, myProject);
		if (cr.getResult() == CheckResult.WRONG) {
			return cr;
		}

		OpcatExposeChangeResult ret = myProject.getExposeManager()
				.allowExposeChange(delInstance,
						OPCAT_EXPOSE_CHANGE_TYPE.CUT_THING);
		if (ret.isDenied()) {
			return new CheckResult(CheckResult.WRONG, ret.getReaseon());
		} else if (ret.isWarning()) {
			return new CheckResult(CheckResult.WARNING, ret.getReaseon());
		}

		return new CheckResult(CheckResult.RIGHT);

	}

	private static CheckResult _checkThingDeletion(ThingInstance delInstance,
			OpdProject myProject) {

		OpcatExposeChangeResult ret = myProject.getExposeManager()
				.allowExposeChange(delInstance,
						OPCAT_EXPOSE_CHANGE_TYPE.DELETE_THING);
		if (ret.isDenied()) {
			return new CheckResult(CheckResult.WRONG, ret.getReaseon());
		} else if (ret.isWarning()) {
			return new CheckResult(CheckResult.WARNING, ret.getReaseon());
		}

		String ENTITY = " ";

		if (delInstance instanceof ProcessInstance) {
			ENTITY = "Process ";
		} else if (delInstance instanceof ObjectInstance) {
			ENTITY = "Object ";
		}

		String msg = ENTITY
				+ delInstance.getEntry().getLogicalEntity().getName().replace(
						'\n', ' ') + " cannot be removed since it is ";

		// Opd processOpd =
		// myProject.getComponentsStructure().getOpd(delInstance.getKey().getOpdId());

		// if (processOpd.getMainInstance()!=null &&
		// processOpd.getMainInstance().getKey().equals(delInstance.getKey()))
		// {
		// msg += "main entity in '"+processOpd.getName()+"'.\n" +
		// "You can only delete the whole OPD.";
		// return new CheckResult(CheckResult.WRONG, msg);
		// }

		CheckResult cr = _checkMainEntity(delInstance, myProject);
		if (cr.getResult() == CheckResult.WRONG) {
			return cr;
		}

		ThingEntry selectedEntry = (ThingEntry) delInstance.getEntry();

		if (selectedEntry.getZoomedInOpd() != null) {
			Opd inzoomingOpd = selectedEntry.getZoomedInOpd();
			Opd parentOpd = inzoomingOpd.getParentOpd();

			// parentOpd of inzoomingOpd is never null!!!!

			if (parentOpd.getOpdId() == delInstance.getKey().getOpdId()) {
				int numOfInstanceInParentOpd = 0;
				for (Enumeration e = selectedEntry.getInstances(); e
						.hasMoreElements();) {
					Instance currInstance = (Instance) e.nextElement();
					if (parentOpd.getOpdId() == currInstance.getKey()
							.getOpdId()) {
						numOfInstanceInParentOpd++;
					}
				}

				if (numOfInstanceInParentOpd < 2) {
					msg += "in-zoomed.\nFirst delete the in-zooming OPD.";
					return new CheckResult(CheckResult.WRONG, msg);
				}
			}
		}

		if (delInstance.getUnfoldingOpd() != null) {
			Opd inzoomingOpd = selectedEntry.getUnfoldingOpd();
			Opd parentOpd = inzoomingOpd.getParentOpd();

			// parentOpd of inzoomingOpd is never null!!!!

			if (parentOpd.getOpdId() == delInstance.getKey().getOpdId()) {
				int numOfInstanceInParentOpd = 0;
				for (Enumeration e = selectedEntry.getInstances(); e
						.hasMoreElements();) {
					Instance currInstance = (Instance) e.nextElement();
					if (parentOpd.getOpdId() == currInstance.getKey()
							.getOpdId()) {
						numOfInstanceInParentOpd++;
					}
				}

				if (numOfInstanceInParentOpd < 2) {
					msg += "unfolded.\nFirst delete the unfolding OPD.";
					return new CheckResult(CheckResult.WRONG, msg);
				}
			}

		}

		if (delInstance.isContainsChilds()) {

			msg += "contains another thing.\n"
					+ "First delete things inside this process.";
			return new CheckResult(CheckResult.WRONG, msg);
		}

		boolean showMessage = Boolean.parseBoolean(Configuration.getInstance()
				.getProperty("show_reguler_links_messages"));

		if (showMessage) {
			if (delInstance.isRelated()) {
				msg = ENTITY
						+ delInstance.getEntry().getLogicalEntity().getName()
								.replace('\n', ' ')
						+ " is connected to another things.\n"
						+ "Proceeding will cause elimination of all links related to this "
						+ ENTITY + ". OK?";
				return new CheckResult(CheckResult.WARNING, msg);
			}
		}

		return new CheckResult(CheckResult.RIGHT);
	}
}