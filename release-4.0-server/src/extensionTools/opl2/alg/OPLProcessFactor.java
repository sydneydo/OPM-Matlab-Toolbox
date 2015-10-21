package extensionTools.opl2.alg;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.IStateEntry;
import extensionTools.opl2.generated.ChangingClause;
import extensionTools.opl2.generated.ChangingSentence;
import extensionTools.opl2.generated.ConditionClause;
import extensionTools.opl2.generated.ConditionSentence;
import extensionTools.opl2.generated.ConsumptionClause;
import extensionTools.opl2.generated.ConsumptionSentence;
import extensionTools.opl2.generated.EffectClause;
import extensionTools.opl2.generated.EffectSentence;
import extensionTools.opl2.generated.EnablingClause;
import extensionTools.opl2.generated.EnablingSentence;
import extensionTools.opl2.generated.ObjectFactory;
import extensionTools.opl2.generated.ProcessAggregationSentence;
import extensionTools.opl2.generated.ProcessAggregationSentenceSet;
import extensionTools.opl2.generated.ProcessBiDirectionalRelationSentence;
import extensionTools.opl2.generated.ProcessEnvironmentalPhysicalSentence;
import extensionTools.opl2.generated.ProcessExhibitionSentence;
import extensionTools.opl2.generated.ProcessExhibitionSentenceSet;
import extensionTools.opl2.generated.ProcessInZoomingSentence;
import extensionTools.opl2.generated.ProcessInZoomingSentenceSet;
import extensionTools.opl2.generated.ProcessInheritanceSentence;
import extensionTools.opl2.generated.ProcessInheritanceSentenceSet;
import extensionTools.opl2.generated.ProcessInstanceSentence;
import extensionTools.opl2.generated.ProcessInvocationSentence;
import extensionTools.opl2.generated.ProcessTimeoutSentence;
import extensionTools.opl2.generated.ProcessUniDirectionalRelationSentence;
import extensionTools.opl2.generated.ResultClause;
import extensionTools.opl2.generated.ResultSentence;
import gui.util.OpcatLogger;

/**
 * <p>
 * Title: Extension Tools
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author OPCAT team
 * @version 1.0
 */
public class OPLProcessFactor extends OPLFactor {

	public OPLProcessFactor(OPLTreeBuilder builder_, String systemName_,
			ObjectFactory ob) {
		super(builder_, systemName_, ob);
	}

	protected ProcessAggregationSentenceSet buildProcAggregSentenceSet(
			String objName) throws Exception {
		ProcessAggregationSentenceSet p;
		ProcessAggregationSentence sentence;
		try {
			p = objFactory.createProcessAggregationSentenceSet();
			sentence = objFactory.createProcessAggregationSentence();
			sentence.setProcessName(objName);
			p.setProcessAggregationSentence(sentence);
		} catch (Exception e) {
			throw e;
		}
		return p;
	}

	protected ProcessInheritanceSentenceSet buildProcInhSentenceSet(
			IRelationEntry rel) throws Exception {
		ProcessInheritanceSentenceSet sentence;
		IEntry destination = this.builder.myStructure.getIEntry(rel
				.getDestinationId());
		String childName = destination.getName();
		try {
			sentence = objFactory.createProcessInheritanceSentenceSet();
			sentence.setObjectName(childName);
			sentence.getProcessInheritanceSentence().add(
					this.buildProcInhSentence(rel));
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	protected ProcessInheritanceSentence buildProcInhSentence(IRelationEntry rel)
			throws Exception {
		ProcessInheritanceSentence particularSent;
		String fatherName = this.builder.myStructure.getIEntry(
				rel.getSourceId()).getName();
		try {
			particularSent = objFactory.createProcessInheritanceSentence();
			particularSent.setInheritanceFatherName(fatherName);
		} catch (Exception e) {
			throw e;
		}
		return particularSent;
	}

	protected ProcessInstanceSentence buildProcInstSentence(IRelationEntry rel)
			throws Exception {
		ProcessInstanceSentence sentence;
		String fatherName = this.builder.myStructure.getIEntry(
				rel.getSourceId()).getName();
		IEntry destination = this.builder.myStructure.getIEntry(rel
				.getDestinationId());
		String childName = destination.getName();
		try {
			sentence = objFactory.createProcessInstanceSentence();
			sentence.setInstanceFatherName(fatherName);
			sentence.setProcessName(childName);
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	protected ProcessUniDirectionalRelationSentence buildProcUniDirSentence(
			IRelationEntry rel) throws Exception {
		ProcessUniDirectionalRelationSentence sentence;
		String fatherName = this.builder.myStructure.getIEntry(
				rel.getSourceId()).getName();
		String destination = this.builder.myStructure.getIEntry(
				rel.getDestinationId()).getName();
		try {
			sentence = objFactory.createProcessUniDirectionalRelationSentence();
			sentence.setSourceName(fatherName);
			sentence.setDestinationName(destination);
			sentence.setRelationName(rel.getForwardRelationMeaning());
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	protected ProcessEnvironmentalPhysicalSentence buildProcEnvPhysicalSentence(
			IProcessEntry abstractEntry) throws Exception {
		ProcessEnvironmentalPhysicalSentence sentence;
		try {
			sentence = objFactory.createProcessEnvironmentalPhysicalSentence();
			sentence.setProcessName(abstractEntry.getName());
			sentence.setEnvironmental(abstractEntry.isEnvironmental());
			sentence.setPhysical(abstractEntry.isPhysical());
			return sentence;
		} catch (Exception e) {
			throw e;
		}
	}

	protected ProcessBiDirectionalRelationSentence buildProcBiDirSentence(
			IRelationEntry rel, int rule) throws Exception {
		ProcessBiDirectionalRelationSentence sentence;
		String fatherName = this.builder.myStructure.getIEntry(
				rel.getSourceId()).getName();
		String destination = this.builder.myStructure.getIEntry(
				rel.getDestinationId()).getName();
		try {
			sentence = objFactory.createProcessBiDirectionalRelationSentence();
			String meaning;
			if (rule == OPLGeneral.DESTINATION) {
				meaning = rel.getBackwardRelationMeaning();
				if (OPLGeneral.isEmpty(meaning)) {
					meaning = rel.getForwardRelationMeaning();
					if (OPLGeneral.isEmpty(meaning)) {
						meaning = "equivalent";
					}
				}
				if ((!OPLGeneral.isEmpty(rel.getForwardRelationMeaning()))
						&& (!OPLGeneral.isEmpty(rel
								.getBackwardRelationMeaning()))) {
					this.fillProcBiDirSentence(sentence, destination,
							fatherName, meaning);
					// addCardinality(sentence, destinationCardinality,
					// sourceCardinality);
					return sentence;
				}
			} else {
				meaning = rel.getForwardRelationMeaning();
			}
			this.fillProcBiDirSentence(sentence, fatherName, destination,
					meaning);
			// addCardinality(sentence, sourceCardinality,
			// destinationCardinality);
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	protected ProcessExhibitionSentenceSet buildProcExhibSentenceSet(
			String objName) throws Exception {
		ProcessExhibitionSentenceSet p;
		ProcessExhibitionSentence sentence;
		p = objFactory.createProcessExhibitionSentenceSet();
		sentence = objFactory.createProcessExhibitionSentence();
		sentence.setProcessName(objName);
		p.setProcessExhibitionSentence(sentence);
		return p;
	}

	/** *************************************************************************** */
	/* LINKS */
	/** *************************************************************************** */

	protected ProcessInvocationSentence buildProcessInvocationSentence(
			ILinkEntry link, String path) throws Exception {
		ProcessInvocationSentence agent = objFactory
				.createProcessInvocationSentence();
		String objName = this.builder.myStructure.getIEntry(link.getSourceId())
				.getName();
		agent.setProcessName(objName);
		this.fillPath(agent.getPathLabel(), path);
		agent.setLogicalRelation(this.setLogicBySource(link));
		agent.setMaxReactionTime(this.buildMaxReactionTime(link));
		agent.setMinReactionTime(this.buildMinReactionTime(link));
		return agent;
	}

	protected ProcessTimeoutSentence buildProcessTimeoutSentence(
			ILinkEntry link, String path) throws Exception {
		ProcessTimeoutSentence agent = objFactory
				.createProcessTimeoutSentence();
		IProcessEntry proc = (IProcessEntry) this.builder.myStructure
				.getIEntry(link.getSourceId());
		String objName = proc.getName();
		agent.setProcessName(objName);
		this.fillPath(agent.getPathLabel(), path);
		agent.setLogicalRelation(this.setLogicBySource(link));
		agent.setMaxReactionTime(this.buildMaxReactionTime(link));
		agent.setMinReactionTime(this.buildMinReactionTime(link));
		agent.setMaxTimeoutValue(this.buildMaxTimeoutValue(proc));
		return agent;
	}

	protected ChangingSentence buildChangingSentence(String procName,
			String logicRel, java.util.List path) throws Exception {
		ChangingSentence sentence = objFactory.createChangingSentence();
		sentence.setProcessName(procName);
		this.fillPath(sentence.getPathLabel(), path);
		sentence.setLogicalRelation(logicRel);
		return sentence;
	}

	protected ChangingClause buildChangingClause(ConsumptionClause cl,
			ResultClause res) throws Exception {
		String logicalRel = cl.getLogicalRelation();
		ChangingClause cond = objFactory.createChangingClause();
		cond.setSourceStateName(cl.getStateName());
		cond.setDestinationStateName(res.getStateName());
		cond.setLogicalRelation(logicalRel);
		cond.setMinimalCardinality(1);
		cond.setMaximalCardinality(1);
		cond.setObjectName(cl.getObjectName());
		cond.setObjectType(cl.getObjectType());
		cond.setSubjectAggregationFatherName(cl
				.getSubjectAggregationFatherName());
		cond
				.setSubjectExhibitionFatherName(cl
						.getSubjectExhibitionFatherName());
		cond.setSubjectThingName(cl.getSubjectThingName());
		return cond;
	}

	protected ConditionSentence buildConditionSentence(String procName,
			String logicRel, String path) throws Exception {
		ConditionSentence sentence = objFactory.createConditionSentence();
		sentence.setProcessName(procName);
		this.fillPath(sentence.getPathLabel(), path);
		sentence.setLogicalRelation(logicRel);
		this.fillPath(sentence.getPathLabel(), path);
		return sentence;
	}

	// What is logical relation?????
	protected ConditionClause buildConditionClause(ILinkEntry link)
			throws Exception {

		IEntry state = (IEntry) this.builder.myStructure.getIEntry(link
				.getSourceId());

		String stateName;
		IEntry obj;

		if (state instanceof IStateEntry) {
			stateName = state.getName();
			obj = ((IStateEntry) state).getParentIObjectEntry();

		} else if (state instanceof IObjectEntry) {
			obj = (IObjectEntry) state;
			stateName = "in existent";
		} else {
			obj = (IEntry) state;
			stateName = "in existent";
		}

		ConditionClause cond = objFactory.createConditionClause();
		cond.setLogicalRelation(this.setLogicByDestination(link));
		cond.setMinimalCardinality(1);
		cond.setMaximalCardinality(1);

		if (state == null) {
			OpcatLogger.logError("state is null " + link.getName()
					+ " sourceid = " + link.getSourceId());
		}
		cond.setObjectName(obj.getName());
		if (OPLGeneral.isNotEmpty(stateName)) {
			cond.setStateName(stateName);
		}

		if ((obj instanceof IObjectEntry)
				&& OPLGeneral.isNotEmpty(((IObjectEntry) obj).getType())) {
			cond.setObjectType(((IObjectEntry) obj).getType());
		} else {
			cond.setObjectType("none");
		}

		return cond;
	}

	protected EffectSentence buildEffectSentence(String procName,
			String logicRel, String path) throws Exception {
		EffectSentence sentence = objFactory.createEffectSentence();
		sentence.setProcessName(procName);
		this.fillPath(sentence.getPathLabel(), path);
		sentence.setLogicalRelation(logicRel);
		return sentence;
	}

	// What is logical relation?????
	protected EffectClause buildEffectClause(ILinkEntry link) throws Exception {
		IEntry obj = (IEntry) this.builder.myStructure.getIEntry(link
				.getSourceId());
		EffectClause cond = objFactory.createEffectClause();
		cond.setLogicalRelation(this.setLogicByDestination(link));
		cond.setMinimalCardinality(1);
		cond.setMaximalCardinality(1);
		cond.setObjectName(obj.getName());
		if ((obj instanceof IObjectEntry)
				&& OPLGeneral.isNotEmpty(((IObjectEntry) obj).getType())) {
			cond.setObjectType(((IObjectEntry) obj).getType());
		} else {
			cond.setObjectType("none");
		}
		return cond;
	}

	protected ConsumptionSentence buildConsumptionSentence(String procName,
			String logicRel, String path) throws Exception {
		ConsumptionSentence sentence = objFactory.createConsumptionSentence();
		sentence.setProcessName(procName);
		this.fillPath(sentence.getPathLabel(), path);
		sentence.setLogicalRelation(logicRel);
		return sentence;
	}

	// What is logical relation?????
	protected ConsumptionClause buildConsumptionClause(ILinkEntry link)
			throws Exception {
		IEntry entry = this.builder.myStructure.getIEntry(link.getSourceId());
		String stateName = "";
		IObjectEntry obj;
		// initStateObject(stateName, obj,entry);
		if (entry instanceof IStateEntry) {
			IStateEntry state = (IStateEntry) entry;
			stateName = state.getName();
			obj = state.getParentIObjectEntry();
		} else {
			obj = (IObjectEntry) entry;
			stateName = "";
		}
		ConsumptionClause cond = objFactory.createConsumptionClause();
		cond.setLogicalRelation(this.setLogicByDestination(link));
		cond.setMinimalCardinality(1);
		cond.setMaximalCardinality(1);
		cond.setObjectName(obj.getName());
		if (OPLGeneral.isNotEmpty(obj.getType())) {
			cond.setObjectType(obj.getType());
		} else {
			cond.setObjectType("none");
		}
		if (OPLGeneral.isNotEmpty(stateName)) {
			cond.setStateName(stateName);
		}
		return cond;
	}

	protected EnablingSentence buildEnablingSentence(String procName,
			String logicRel, String path) throws Exception {
		EnablingSentence sentence = objFactory.createEnablingSentence();
		sentence.setProcessName(procName);
		this.fillPath(sentence.getPathLabel(), path);
		sentence.setLogicalRelation(logicRel);
		return sentence;
	}

	// What is logical relation?????
	protected EnablingClause buildEnablingClause(ILinkEntry link)
			throws Exception {
		IEntry entry = this.builder.myStructure.getIEntry(link.getSourceId());
		String stateName = "";
		IObjectEntry obj;
		if (entry instanceof IStateEntry) {
			IStateEntry state = (IStateEntry) entry;
			stateName = state.getName();
			obj = state.getParentIObjectEntry();
		} else {
			obj = (IObjectEntry) entry;
			stateName = "";
		}
		EnablingClause cond = objFactory.createEnablingClause();
		cond.setLogicalRelation(this.setLogicByDestination(link));
		cond.setMinimalCardinality(1);
		cond.setMaximalCardinality(1);
		cond.setObjectName(obj.getName());
		if (OPLGeneral.isNotEmpty(obj.getType())) {
			cond.setObjectType(obj.getType());
		} else {
			cond.setObjectType("none");
		}
		if (OPLGeneral.isNotEmpty(stateName)) {
			cond.setStateName(stateName);
		}
		return cond;
	}

	protected ResultSentence buildResultSentence(String procName,
			String logicRel, String path) throws Exception {
		ResultSentence sentence = objFactory.createResultSentence();
		sentence.setProcessName(procName);
		this.fillPath(sentence.getPathLabel(), path);
		sentence.setLogicalRelation(logicRel);
		return sentence;
	}

	// What is logical relation?????
	protected ResultClause buildResultClause(ILinkEntry link) throws Exception {
		IEntry entry = this.builder.myStructure.getIEntry(link
				.getDestinationId());
		String stateName = "";
		IObjectEntry obj;
		if (entry instanceof IStateEntry) {
			IStateEntry state = (IStateEntry) entry;
			stateName = state.getName();
			obj = state.getParentIObjectEntry();
		} else {
			obj = (IObjectEntry) entry;
		}
		ResultClause cond = objFactory.createResultClause();
		cond.setLogicalRelation(this.setLogicBySource(link));
		cond.setMinimalCardinality(1);
		cond.setMaximalCardinality(1);
		cond.setObjectName(obj.getName());
		if (OPLGeneral.isNotEmpty(obj.getType())) {
			cond.setObjectType(obj.getType());
		} else {
			cond.setObjectType("none");
		}
		if (OPLGeneral.isNotEmpty(stateName)) {
			cond.setStateName(stateName);
		}
		return cond;
	}

	protected ProcessInZoomingSentenceSet buildProcessInZoomingSentenceSet(
			String procName) throws Exception {
		ProcessInZoomingSentenceSet sentences = objFactory
				.createProcessInZoomingSentenceSet();
		ProcessInZoomingSentence sentence = objFactory
				.createProcessInZoomingSentence();
		sentence.setProcessName(procName);
		sentences.setProcessInZoomingSentence(sentence);
		return sentences;
	}

	/** *************************************************************************** */

	protected void fillProcBiDirSentence(
			ProcessBiDirectionalRelationSentence sentence, String fatherName,
			String destName, String meaning) {
		sentence.setSourceName(fatherName);
		sentence.setDestinationName(destName);
		sentence.setRelationName(meaning);
	}

	/*
	 * public void initStateObject(String stateName, IObjectEntry obj, IEntry
	 * entry) { if (entry instanceof IStateEntry) { IStateEntry state =
	 * (IStateEntry) entry; stateName = state.getName(); obj =
	 * state.getParentIObjectEntry(); } else { obj = (IObjectEntry) entry;
	 * stateName = ""; } }
	 */

}