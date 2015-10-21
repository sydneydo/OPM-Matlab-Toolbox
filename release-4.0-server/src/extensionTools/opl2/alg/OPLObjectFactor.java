package extensionTools.opl2.alg;

import java.util.Collections;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.IStateEntry;
import exportedAPI.opcatAPI.IThingEntry;
import extensionTools.opl2.generated.AgentSentence;
import extensionTools.opl2.generated.AggregatedObject;
import extensionTools.opl2.generated.GeneralEventSentence;
import extensionTools.opl2.generated.ObjectAggregationSentence;
import extensionTools.opl2.generated.ObjectAggregationSentenceSet;
import extensionTools.opl2.generated.ObjectBiDirectionalRelationSentence;
import extensionTools.opl2.generated.ObjectEnvironmentalPhysicalSentence;
import extensionTools.opl2.generated.ObjectExhibitionSentence;
import extensionTools.opl2.generated.ObjectExhibitionSentenceSet;
import extensionTools.opl2.generated.ObjectFactory;
import extensionTools.opl2.generated.ObjectInZoomingSentence;
import extensionTools.opl2.generated.ObjectInZoomingSentenceSet;
import extensionTools.opl2.generated.ObjectInheritanceSentence;
import extensionTools.opl2.generated.ObjectInheritanceSentenceSet;
import extensionTools.opl2.generated.ObjectInstanceSentence;
import extensionTools.opl2.generated.ObjectStateSentence;
import extensionTools.opl2.generated.ObjectUniDirectionalRelationSentence;
import extensionTools.opl2.generated.StateClause;
import extensionTools.opl2.generated.StateEntranceSentence;
import extensionTools.opl2.generated.StateTimeoutSentence;
import extensionTools.opl2.generated.TypeDeclarationSentence;
import gui.projectStructure.LinkInstance;

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

public class OPLObjectFactor extends OPLFactor {

	public OPLObjectFactor(OPLTreeBuilder builder_, String systemName_,
			ObjectFactory ob) {
		super(builder_, systemName_, ob);
	}

	protected TypeDeclarationSentence buildTypeDeclSentence(IObjectEntry obj)
			throws Exception {
		TypeDeclarationSentence sentence;
		try {
			sentence = objFactory.createTypeDeclarationSentence();
			if (OPLGeneral.isNotEmpty(obj.getInitialValue())) {
				sentence.setInitialValue(obj.getInitialValue());
			}
			sentence.setObjectName(obj.getName());
			if (obj.getScope().equals(OpcatConstants.PRIVATE)) {
				sentence.setObjectScope("private");
			} else {
				if (obj.getScope().equals(OpcatConstants.PROTECTED)) {
					sentence.setObjectScope("protected");
				} else {
					sentence.setObjectScope("public");
				}
			}
			String type = obj.getType();
			int index = type.indexOf("char[");
			if (index != -1) {
				sentence.setObjectType(type.substring(5, type.length() - 1));
			} else {
				sentence.setObjectType(type);
			}
			String fatherName = this.builder.findFatherName(obj);
			if (OPLGeneral.isNotEmpty(fatherName)) {
				sentence.setFatherName(fatherName);
			}
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	// Should add role parameter
	protected ObjectAggregationSentenceSet buildObjAggregSentenceSet(
			String objName) throws Exception {
		ObjectAggregationSentenceSet p;
		ObjectAggregationSentence sentence;
		try {
			p = objFactory.createObjectAggregationSentenceSet();
			sentence = objFactory.createObjectAggregationSentence();
			sentence.setObjectName(objName);
			// sentence.setRole("nothing");
			p.setObjectAggregationSentence(sentence);
		} catch (Exception e) {
			throw e;
		}
		return p;
	}

	// Added role
	protected ObjectInstanceSentence buildObjInstSentence(IRelationEntry rel)
			throws Exception {
		ObjectInstanceSentence sentence;
		String fatherName = this.builder.myStructure.getIEntry(
				rel.getSourceId()).getName();
		IEntry destination = this.builder.myStructure.getIEntry(rel
				.getDestinationId());
		String childName = destination.getName();
		try {
			sentence = objFactory.createObjectInstanceSentence();
			sentence.setInstanceFatherName(fatherName);
			sentence.setObjectName(childName);
			// if(!((IThingEntry)rel).getRole().equals(""))
			// sentence.setRole(((IThingEntry)rel).getRole());
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	// Should add parameter
	protected ObjectStateSentence buildObjStateSentence(String objName)
			throws Exception {
		ObjectStateSentence sentence;
		try {
			sentence = objFactory.createObjectStateSentence();
			sentence.setObjectName(objName);
			// sentence.setRole("nothing");
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	// Added role
	protected AgentSentence buildAgentSentence(ILinkEntry link, String path)
			throws Exception {
		AgentSentence agent = objFactory.createAgentSentence();

		String objName = this.builder.myStructure.getIEntry(link.getSourceId())
				.getName();
		agent.setObjectName(objName);
		this.fillPath(agent.getPathLabel(), path);
		agent.setLogicalRelation(this.setLogicBySource(link));
		// if(!((IThingEntry)link).getRole().equals(""))
		// agent.setRole(((IThingEntry)link).getRole());
		// MaxReactionTime max = objFactory.createMaxReactionTime();
		// max.setTimeValue(this.buildTimeValue(link.getMaxReactionTime()));
		// MinReactionTime min = objFactory.createMinReactionTime();
		// min.setTimeValue(this.buildTimeValue(link.getMinReactionTime()));
		agent.setMaxReactionTime(this.buildMaxReactionTime(link));
		agent.setMinReactionTime(this.buildMinReactionTime(link));
		return agent;
	}

	// Added role
	protected GeneralEventSentence buildGeneralEventSentence(ILinkEntry link,
			String path) throws Exception {
		GeneralEventSentence agent = objFactory.createGeneralEventSentence();
		// patch for link Entry
		Iterator iter = Collections.list(link.getInstances()).iterator();
		IEntry ent = null;
		while (iter.hasNext()) {
			ent = (IEntry) ((LinkInstance) iter.next()).getSource().getEntry();
			if (ent instanceof IObjectEntry) {
				break;
			}
		}
		if (ent == null)
			return null;
		// ///////////////////////////////////////
		IObjectEntry obj = (IObjectEntry) ent;
		boolean hasStates = obj.getStates().hasMoreElements();
		String objName = obj.getName();
		agent.setIsObjectWithStates(hasStates);
		agent.setObjectName(objName);
		// if(!((IThingEntry)link).getRole().equals(""))
		// agent.setRole(((IThingEntry)link).getRole());
		this.fillPath(agent.getPathLabel(), path);
		agent.setLogicalRelation(this.setLogicBySource(link));
		agent.setMaxReactionTime(this.buildMaxReactionTime(link));
		agent.setMinReactionTime(this.buildMinReactionTime(link));
		return agent;
	}

	// Added role
	protected StateTimeoutSentence buildStateTimeoutSentence(ILinkEntry link,
			String path) throws Exception {
		StateTimeoutSentence agent = objFactory.createStateTimeoutSentence();
		IStateEntry state = (IStateEntry) this.builder.myStructure
				.getIEntry(link.getSourceId());
		String objName = state.getParentIObjectEntry().getName();
		String stateName = state.getName();
		agent.setObjectName(objName);
		agent.setStateName(stateName);
		this.fillPath(agent.getPathLabel(), path);
		agent.setLogicalRelation(this.setLogicBySource(link));
		agent.setMaxReactionTime(this.buildMaxReactionTime(link));
		agent.setMinReactionTime(this.buildMinReactionTime(link));
		agent.setMaxTimeoutValue(this.buildMaxTimeoutValue(state));
		// if(!((IThingEntry)link).getRole().equals(""))
		// agent.setRole(((IThingEntry)link).getRole());
		return agent;
	}

	// Added role
	protected StateEntranceSentence buildStateEntranceSentence(ILinkEntry link,
			String path) throws Exception {
		StateEntranceSentence agent = objFactory.createStateEntranceSentence();
		IConnectionEdgeEntry conEdge = (IConnectionEdgeEntry) this.builder.myStructure
				.getIEntry(link.getSourceId());
		String objName = " "; 
		String stateName = " " ; 
		if (conEdge instanceof IStateEntry) {
			IStateEntry state = (IStateEntry) conEdge;
			objName = state.getParentIObjectEntry().getName();
			stateName = state.getName();			
		} else if (conEdge instanceof IObjectEntry){
			objName = conEdge.getName() ; 
		}

		agent.setObjectName(objName);
		agent.setStateName(stateName);
		this.fillPath(agent.getPathLabel(), path);
		agent.setLogicalRelation(this.setLogicBySource(link));
		agent.setMaxReactionTime(this.buildMaxReactionTime(link));
		agent.setMinReactionTime(this.buildMinReactionTime(link));
		// if(!((IThingEntry)link).getRole().equals(""))
		// agent.setRole(((IThingEntry)link).getRole());
		return agent;
	}

	protected StateClause buildStateClause(IStateEntry state) throws Exception {
		StateClause clause;
		try {
			clause = objFactory.createStateClause();
			clause.setStateName(state.getName());
			// ?????????????????????????????????Yevgeniy
			clause.setDefault(state.isDefault());
			clause.setFinal(state.isFinal());
			clause.setStateDescription(state.getDescription());
			String maxTime = state.getMaxTime();
			String minTime = state.getMinTime();
			if (!maxTime.equals("infinity")) {
				clause.setMaxTimeValue(this.buildMaxTimeValue(maxTime));
			}
			if (!minTime.equals("0;0;0;0;0;0;0")) {
				clause.setMinTimeValue(this.buildMinTimeValue(minTime));
			}
			clause.setInitial(state.isInitial());
		} catch (Exception e) {
			throw e;
		}
		return clause;
	}

	// Added role
	protected ObjectInheritanceSentenceSet buildObjInhSentenceSet(
			IRelationEntry rel) throws Exception {
		ObjectInheritanceSentenceSet sentence;
		IEntry destination = this.builder.myStructure.getIEntry(rel
				.getDestinationId());
		String childName = destination.getName();
		try {
			sentence = objFactory.createObjectInheritanceSentenceSet();
			sentence.setObjectName(childName);
			sentence.getObjectInheritanceSentence().add(
					this.buildObjInhSentence(rel));
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	protected ObjectInheritanceSentence buildObjInhSentence(IRelationEntry rel)
			throws Exception {
		ObjectInheritanceSentence particularSent;
		String fatherName = this.builder.myStructure.getIEntry(
				rel.getSourceId()).getName();
		try {
			particularSent = objFactory.createObjectInheritanceSentence();
			particularSent.setInheritanceFatherName(fatherName);
		} catch (Exception e) {
			throw e;
		}
		return particularSent;
	}

	// Added roles
	protected ObjectUniDirectionalRelationSentence buildObjUniDirSentence(
			IRelationEntry rel) throws Exception {
		ObjectUniDirectionalRelationSentence sentence;
		String fatherName = this.builder.myStructure.getIEntry(
				rel.getSourceId()).getName();
		String destination = this.builder.myStructure.getIEntry(
				rel.getDestinationId()).getName();
		try {
			sentence = objFactory.createObjectUniDirectionalRelationSentence();
			sentence.setSourceName(fatherName);
			sentence.setDestinationName(destination);
			if (!((IThingEntry) this.builder.myStructure.getIEntry(rel
					.getDestinationId())).getRole().equals("")) {
				;
			}
			// sentence.setDestinationRole(((IThingEntry)builder.myStructure.getIEntry(rel.getDestinationId())).getRole());
			if (!((IThingEntry) this.builder.myStructure.getIEntry(rel
					.getSourceId())).getRole().equals("")) {
				;
			}
			// sentence.setSourceRole(((IThingEntry)builder.myStructure.getIEntry(rel.getSourceId())).getRole());
			String meaning = rel.getForwardRelationMeaning();
			if (OPLGeneral.isEmpty(meaning)) {
				meaning = "relates to";
			}
			sentence.setRelationName(meaning);
			this.addCardinality(sentence, rel);
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	// Added role
	protected ObjectEnvironmentalPhysicalSentence buildObjEnvPhysicalSentence(
			IObjectEntry abstractEntry) throws Exception {
		ObjectEnvironmentalPhysicalSentence sentence;
		try {
			sentence = objFactory.createObjectEnvironmentalPhysicalSentence();
			sentence.setObjectName(abstractEntry.getName());
			sentence.setEnvironmental(abstractEntry.isEnvironmental());
			sentence.setPhysical(abstractEntry.isPhysical());
			// if(!abstractEntry.getRole().equals(""))
			// sentence.setRole(abstractEntry.getRole());
			return sentence;
		} catch (Exception e) {
			throw e;
		}
	}

	// Added role
	protected ObjectBiDirectionalRelationSentence buildObjBiDirSentence(
			IRelationEntry rel, int rule) throws Exception {
		ObjectBiDirectionalRelationSentence sentence;
		String fatherName = this.builder.myStructure.getIEntry(
				rel.getSourceId()).getName();
		String destination = this.builder.myStructure.getIEntry(
				rel.getDestinationId()).getName();
		int[] sourceCardinality = OPLGeneral.getCardinality(rel
				.getSourceCardinality());
		int[] destinationCardinality = OPLGeneral.getCardinality(rel
				.getDestinationCardinality());
		try {
			sentence = objFactory.createObjectBiDirectionalRelationSentence();
			if (!((IThingEntry) this.builder.myStructure.getIEntry(rel
					.getSourceId())).getRole().equals("")) {
				;
			}
			// sentence.setSourceRole(((IThingEntry)builder.myStructure.getIEntry(rel.getSourceId())).getRole());
			if (!((IThingEntry) this.builder.myStructure.getIEntry(rel
					.getDestinationId())).getRole().equals("")) {
				;
			}
			// sentence.setDestinationRole(((IThingEntry)builder.myStructure.getIEntry(rel.getDestinationId())).getRole());
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
					this.fillObjBiDirSentence(sentence, destination,
							fatherName, meaning);
					this.addCardinality(sentence, destinationCardinality,
							sourceCardinality);
					return sentence;
				}
			} else {
				meaning = rel.getForwardRelationMeaning();
			}
			this.fillObjBiDirSentence(sentence, fatherName, destination,
					meaning);
			this.addCardinality(sentence, sourceCardinality,
					destinationCardinality);
		} catch (Exception e) {
			throw e;
		}
		return sentence;
	}

	// Added role
	public AggregatedObject buildAggregatedObject(IEntry destination,
			IRelationEntry rel) throws JAXBException {
		AggregatedObject obj;
		try {
			obj = objFactory.createAggregatedObject();
		} catch (JAXBException e) {
			e.printStackTrace();
			throw e;
		}
		obj.setObjectName(destination.getName());
		// if(!((IThingEntry)rel).getRole().equals(""))
		// obj.setRole(((IThingEntry)rel).getRole());
		int[] cardinality = OPLGeneral.getCardinality(rel
				.getDestinationCardinality());
		obj.setMaximalCardinality(cardinality[this.MAX]);
		obj.setMinimalCardinality(cardinality[this.MIN]);
		return obj;
	}

	protected ObjectExhibitionSentenceSet buildObjExhibSentenceSet(
			String objName) throws Exception {
		ObjectExhibitionSentenceSet p;
		ObjectExhibitionSentence sentence;
		try {
			p = objFactory.createObjectExhibitionSentenceSet();
			sentence = objFactory.createObjectExhibitionSentence();
			sentence.setObjectName(objName);
			p.setObjectExhibitionSentence(sentence);
		} catch (Exception e) {
			throw e;
		}
		return p;
	}

	// Should add role!!!
	protected ObjectInZoomingSentenceSet buildObjectInZoomingSentenceSet(
			String procName) throws Exception {
		ObjectInZoomingSentenceSet sentences = objFactory
				.createObjectInZoomingSentenceSet();
		ObjectInZoomingSentence sentence = objFactory
				.createObjectInZoomingSentence();
		sentence.setObjectName(procName);
		// sentence.setRole("nothing");
		sentences.setObjectInZoomingSentence(sentence);
		return sentences;
	}

	protected void addCardinality(ObjectBiDirectionalRelationSentence sentence,
			int[] sourceCardinality, int[] destinationCardinality) {
		sentence.setSourceMaximalCardinality(sourceCardinality[this.MAX]);
		sentence.setSourceMinimalCardinality(sourceCardinality[this.MIN]);
		sentence
				.setDestinationMaximalCardinality(destinationCardinality[this.MAX]);
		sentence
				.setDestinationMinimalCardinality(destinationCardinality[this.MIN]);
	}

	protected void addCardinality(
			ObjectUniDirectionalRelationSentence sentence, IRelationEntry rel) {
		// System.err.println(" Adding cardinality to
		// "+builder.myStructure.getIEntry(rel.getSourceId()).getName());
		// System.err.println(" Adding cardinality to
		// "+builder.myStructure.getIEntry(rel.getDestinationId()).getName());
		int[] sourceCardinality = OPLGeneral.getCardinality(rel
				.getSourceCardinality());
		int[] destinationCardinality = OPLGeneral.getCardinality(rel
				.getDestinationCardinality());
		sentence.setSourceMaximalCardinality(sourceCardinality[this.MAX]);
		sentence.setSourceMinimalCardinality(sourceCardinality[this.MIN]);
		sentence
				.setDestinationMaximalCardinality(destinationCardinality[this.MAX]);
		sentence
				.setDestinationMinimalCardinality(destinationCardinality[this.MIN]);
	}

	protected void fillObjBiDirSentence(
			ObjectBiDirectionalRelationSentence sentence, String fatherName,
			String destName, String meaning) {
		sentence.setSourceName(fatherName);
		sentence.setDestinationName(destName);
		sentence.setRelationName(meaning);
	}
}