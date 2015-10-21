package gui.opmEntities;

import exportedAPI.OpcatConstants;

public class Constants {

  public static OpmStructuralRelation createRelation(int relType, long relId,
      OpmConnectionEdge source, OpmConnectionEdge destination)
  {
    switch (relType) {
      case OpcatConstants.SPECIALIZATION_RELATION:
        return new OpmSpecialization(relId,"", source, destination);
      case OpcatConstants.EXHIBITION_RELATION:
        return new OpmExhibition(relId, "", source, destination);
      case OpcatConstants.INSTANTINATION_RELATION:
        return new OpmInstantination(relId, "", source, destination);
      case OpcatConstants.AGGREGATION_RELATION:
        return new OpmAggregation(relId, "",source, destination);
      case OpcatConstants.UNI_DIRECTIONAL_RELATION:
        return new OpmUniDirectionalRelation(relId, "", source, destination);
      case OpcatConstants.BI_DIRECTIONAL_RELATION:
        return new OpmBiDirectionalRelation(relId, "", source, destination);
    }

    return null;
  }

  public static OpmProceduralLink createLink(int linkType, long linkId,
      long sourceId, long destinationId)
  {
    switch (linkType) {
      case OpcatConstants.CONSUMPTION_LINK:
        return new OpmConsumptionLink(linkId, "", sourceId, destinationId);
      case OpcatConstants.EFFECT_LINK:
        return new OpmEffectLink(linkId, "", sourceId, destinationId);
      case OpcatConstants.INSTRUMENT_LINK:
        return new OpmInstrument(linkId, "", sourceId, destinationId);
      case OpcatConstants.CONDITION_LINK:
        return new OpmConditionLink(linkId, "", sourceId, destinationId);
      case OpcatConstants.AGENT_LINK:
        return new OpmAgent(linkId, "", sourceId, destinationId);
      case OpcatConstants.RESULT_LINK:
        return new OpmResultLink(linkId, "", sourceId, destinationId);
      case OpcatConstants.INVOCATION_LINK:
        return new OpmInvocationLink(linkId, "", sourceId, destinationId);
      case OpcatConstants.INSTRUMENT_EVENT_LINK:
        return new OpmInstrumentEventLink(linkId, "", sourceId, destinationId);
      case OpcatConstants.EXCEPTION_LINK:
        return new OpmExceptionLink(linkId, "", sourceId, destinationId);
      case OpcatConstants.CONSUMPTION_EVENT_LINK:
        return new OpmConsumptionEventLink(linkId, "", sourceId, destinationId);
    }

    return null;
  }

  public static int getType4Link(OpmProceduralLink link) {
    if (link instanceof OpmConsumptionLink) {
      return OpcatConstants.CONSUMPTION_LINK;
    }

    if (link instanceof OpmEffectLink) {
      return OpcatConstants.EFFECT_LINK;
    }

    if (link instanceof OpmInstrument) {
      return OpcatConstants.INSTRUMENT_LINK;
    }

    if (link instanceof OpmConditionLink) {
      return OpcatConstants.CONDITION_LINK;
    }

    if (link instanceof OpmAgent) {
      return OpcatConstants.AGENT_LINK;
    }

    if (link instanceof OpmResultLink) {
      return OpcatConstants.RESULT_LINK;
    }

    if (link instanceof OpmInvocationLink) {
      return OpcatConstants.INVOCATION_LINK;
    }

    if (link instanceof OpmInstrumentEventLink) {
      return OpcatConstants.INSTRUMENT_EVENT_LINK;
    }

    if (link instanceof OpmExceptionLink) {
      return OpcatConstants.EXCEPTION_LINK;
    }

    if (link instanceof OpmConsumptionEventLink) {
      return OpcatConstants.CONSUMPTION_EVENT_LINK;
    }

    return 0;
  }

  public static String getDBType4Link(OpmProceduralLink link) {
    if (link instanceof OpmAgent) {
      return "AG";
    }

    if (link instanceof OpmInstrument) {
      return "IN";
    }

    if (link instanceof OpmResultLink) {
      return "RL";
    }

    if (link instanceof OpmConsumptionLink) {
      return "CL";
    }

    if (link instanceof OpmEffectLink) {
      return "EL";
    }

    if (link instanceof OpmInvocationLink) {
      return "IL";
    }

    if (link instanceof OpmConditionLink) {
      return "CN";
    }

    if (link instanceof OpmInstrumentEventLink) {
      return "EE";
    }

    if (link instanceof OpmExceptionLink) {
      return "EX";
    }

    if (link instanceof OpmConsumptionEventLink) {
      return "IE";
    }

    return null;
  }

  public static int getType4Relation(OpmStructuralRelation relation) {
    if (relation instanceof OpmInstantination) {
      return OpcatConstants.INSTANTINATION_RELATION;
    }

    if (relation instanceof OpmExhibition) {
      return OpcatConstants.EXHIBITION_RELATION;
    }

    if (relation instanceof OpmAggregation) {
      return OpcatConstants.AGGREGATION_RELATION;
    }

    if (relation instanceof OpmSpecialization) {
      return OpcatConstants.SPECIALIZATION_RELATION;
    }

    if (relation instanceof OpmBiDirectionalRelation) {
      return OpcatConstants.BI_DIRECTIONAL_RELATION;
    }

    if (relation instanceof OpmUniDirectionalRelation) {
      return OpcatConstants.UNI_DIRECTIONAL_RELATION;
    }

    return 0;
  }

  public static String getDBType4Relation(OpmStructuralRelation relation) {
    if (relation instanceof OpmExhibition) {
      return "CH";
    }

    if (relation instanceof OpmAggregation) {
      return "AG";
    }

    if (relation instanceof OpmSpecialization) {
      return "GN";
    }

    if (relation instanceof OpmInstantination) {
      return "IN";
    }

    if (relation instanceof OpmUniDirectionalRelation) {
      return "G1";
    }

    if (relation instanceof OpmBiDirectionalRelation) {
      return "G2";
    }

    return null;

  }

  public static String type2String(int relType) {
    switch (relType) {
      case OpcatConstants.SPECIALIZATION_RELATION:
        return "genealization-specialization relation";
      case OpcatConstants.EXHIBITION_RELATION:
        return "featuring-characterization relation";
      case OpcatConstants.INSTANTINATION_RELATION:
        return "classification-instantiation relation";
      case OpcatConstants.AGGREGATION_RELATION:
        return "Aggregation-Participation relation";
      case OpcatConstants.UNI_DIRECTIONAL_RELATION:
        return "uni-directional relation";
      case OpcatConstants.BI_DIRECTIONAL_RELATION:
        return "bi-directional relation";

      case OpcatConstants.CONSUMPTION_LINK:
        return "consumption link";
      case OpcatConstants.EFFECT_LINK:
        return "effect link";
      case OpcatConstants.INSTRUMENT_LINK:
        return "instrument link";
      case OpcatConstants.CONDITION_LINK:
        return "condition link";
      case OpcatConstants.AGENT_LINK:
        return "agent link";
      case OpcatConstants.RESULT_LINK:
        return "result link";
      case OpcatConstants.INVOCATION_LINK:
        return "invocation link";
      case OpcatConstants.INSTRUMENT_EVENT_LINK:
        return "instrument event link";
      case OpcatConstants.EXCEPTION_LINK:
        return "exception link";
      case OpcatConstants.CONSUMPTION_EVENT_LINK:
        return "consumption event link";

      case OpcatConstants.OBJECT:
        return "object";
      case OpcatConstants.PROCESS:
        return "process";
      case OpcatConstants.STATE:
        return "state";
    }
    return "EMPTY (" + relType + ")";
  }

  public static int string2type(String str) {
    if (str.equals("generalization-specialization relation")) {
		return OpcatConstants.SPECIALIZATION_RELATION;
	}
    if (str.equals("featuring-characterization relation")) {
		return OpcatConstants.EXHIBITION_RELATION;
	}
    if (str.equals("classification-instantiation relation")) {
		return OpcatConstants.INSTANTINATION_RELATION;
	}
    if (str.equals("aggregation-participation relation")) {
		return OpcatConstants.AGGREGATION_RELATION;
	}
    if (str.equals("uni-directional relation")) {
		return OpcatConstants.UNI_DIRECTIONAL_RELATION;
	}
    if (str.equals("bi-directional relation")) {
		return OpcatConstants.BI_DIRECTIONAL_RELATION;
	}

    if (str.equals("consumption link")) {
		return OpcatConstants.CONSUMPTION_LINK;
	}
    if (str.equals("effect link")) {
		return OpcatConstants.EFFECT_LINK;
	}
    if (str.equals("instrument link")) {
		return OpcatConstants.INSTRUMENT_LINK;
	}
    if (str.equals("condition link")) {
		return OpcatConstants.CONDITION_LINK;
	}
    if (str.equals("agent link")) {
		return OpcatConstants.AGENT_LINK;
	}
    if (str.equals("result link")) {
		return OpcatConstants.RESULT_LINK;
	}
    if (str.equals("invocation link")) {
		return OpcatConstants.INVOCATION_LINK;
	}
    if (str.equals("instrument event link")) {
		return OpcatConstants.INSTRUMENT_EVENT_LINK;
	}
    if (str.equals("exception link")) {
		return OpcatConstants.EXCEPTION_LINK;
	}
    if (str.equals("consumption event link")) {
		return OpcatConstants.CONSUMPTION_EVENT_LINK;
	}

    if (str.equals("object")) {
		return OpcatConstants.OBJECT;
	}
    if (str.equals("process")) {
		return OpcatConstants.PROCESS;
	}
    if (str.equals("state")) {
		return OpcatConstants.STATE;
	}
    return -1;
  }

  public static String a_anSelector(int type, boolean capital) {
    switch (type) {
      case OpcatConstants.SPECIALIZATION_RELATION:
      case OpcatConstants.EXHIBITION_RELATION:
      case OpcatConstants.UNI_DIRECTIONAL_RELATION:
      case OpcatConstants.BI_DIRECTIONAL_RELATION:
      case OpcatConstants.CONSUMPTION_LINK:
      case OpcatConstants.CONDITION_LINK:
      case OpcatConstants.RESULT_LINK:
      case OpcatConstants.PROCESS:
      case OpcatConstants.STATE:
        if (capital) {
          return "A";
        }
        else {
          return "a";
        }

      case OpcatConstants.INSTANTINATION_RELATION:
      case OpcatConstants.AGGREGATION_RELATION:
      case OpcatConstants.EFFECT_LINK:
      case OpcatConstants.INSTRUMENT_LINK:
      case OpcatConstants.AGENT_LINK:
      case OpcatConstants.INVOCATION_LINK:
      case OpcatConstants.INSTRUMENT_EVENT_LINK:
      case OpcatConstants.EXCEPTION_LINK:
      case OpcatConstants.CONSUMPTION_EVENT_LINK:
      case OpcatConstants.OBJECT:
        if (capital) {
          return "An";
        }
        else {
          return "an";
        }

      default:
        return "";

    }
  }
}