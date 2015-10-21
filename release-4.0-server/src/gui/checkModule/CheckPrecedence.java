package gui.checkModule;
import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPIx.IXLinkInstance;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.RelationEntry;

/************************************************************************/
/* Class: CheckPrecedence                                               */
/* purpose:                                                             */
/* handle all the logics between collision of links/relations           */
/************************************************************************/
public class CheckPrecedence {

  private static int[][] linkPrecedenceTable=new int[9][9];

  public final static int CONDITION_LINK=0;
  public final static int INSTRUMENT_LINK =1;
  public final static int INSTRUMENT_EVENT_LINK =2;
  public final static int AGENT_LINK=3;
  public final static int RESULT_CONSUMPTION_LINK =4;
  public final static int CONSUMPTION_EVENT_LINK=5;
  public final static int EFFECT_LINK =6;
  public final static int INVOCATION_LINK=7;
  public final static int EXECPTION_LINK=0;

  public final static int UNI_DIRECTION_RELATION = 0;
  public final static int BI_DIRECTION_RELATION = 1;
  public final static int INSTANSATION_RELATION = 2;
  public final static int FEATURING_RELATION = 3;
  public final static int AGGREGATION_RELATION = 4;
  public final static int SPECIALIZATION_RELATION = 5;
  public final static int EXHIBITION_RELATION =6;

  /************************************************************************/
  /* Class: CheckPrecedence                                               */
  /* purpose:                                                             */
  /* handle all the logics between collision of links/relations           */
  /************************************************************************/
  public CheckPrecedence() {
    this.initCheckPrecedneceTables();
  }
  /************************************************************************/
  /* Class: initCheckPrecedneceTables                                     */
  /* purpose:                                                             */
  /* call for initlization for all precednce tables                       */
  /************************************************************************/
  public void initCheckPrecedneceTables()
  {
    this.initLinkCheckPrecednceTable();
    this.initRelationCheckPrecedenceTable();
  }
  /************************************************************************/
  /* Class: initCheckPrecedneceTables                                     */
  /* purpose:                                                             */
  /* initlize the link precednce table                                    */
  /************************************************************************/
  public void initLinkCheckPrecednceTable()
  {
    for (int i=0;i<9;i++) {
		for ( int j=0;j<9;j++)
		{
		  if  (j>i) {
			linkPrecedenceTable[i][j]=0;
		} else {
			linkPrecedenceTable[i][j]=1;
		}
		}
	}
  }
  /************************************************************************/
  /* Class: initCheckPrecedneceTables                                     */
  /* purpose:                                                             */
  /* initlize the relation precednce table                                */
  /************************************************************************/
  public void initRelationCheckPrecedenceTable()
  {

  }

  /************************************************************************/
  /* Class: checkPrecedenceBetweenLinks                                   */
  /* purpose:                                                             */
  /* recieves two links and returns the ome with highest priority         */
  /************************************************************************/
  public LinkInstance checkPrecedenceBetweenLinks(LinkInstance aLink,LinkInstance bLink)
  {
    if ( linkPrecedenceTable[this.translateLinkToInt(aLink)][this.translateLinkToInt(bLink)]==1 ) {
		return bLink;
	} else {
		return aLink;
	}
  }
  /************************************************************************/
  /* Class: checkPredenceBetweenInstances                                 */
  /* purpose:                                                             */
  /* recieves two instances , parse them and call for suitable function   */
  /************************************************************************/
  public Instance checkPredenceBetweenInstances(Instance aInstance,Instance bInstance)
  {
    // checking the input of the function
    if (((!(aInstance instanceof LinkInstance)) &&
        (!(aInstance instanceof GeneralRelationInstance) ) &&
        (!(aInstance instanceof FundamentalRelationInstance) ))
        ||
        ((!(bInstance instanceof LinkInstance)) &&
        (!(bInstance instanceof GeneralRelationInstance) ) &&
        (!(bInstance instanceof FundamentalRelationInstance) ))) {
		return null;
	}

    if (((aInstance instanceof GeneralRelationInstance) && (bInstance instanceof LinkInstance))
        || ((aInstance instanceof FundamentalRelationInstance) && (bInstance instanceof LinkInstance))) {
		return aInstance;
	}
    if (((bInstance instanceof GeneralRelationInstance) && (aInstance instanceof LinkInstance))
        || ((bInstance instanceof FundamentalRelationInstance) && (aInstance instanceof LinkInstance))) {
		return bInstance;
	}

    if (aInstance instanceof LinkInstance ) {
		return this.checkPrecedenceBetweenLinks((LinkInstance)aInstance,(LinkInstance)bInstance);
	} else {
		return this.checkPrecedenceBetweenRelations(aInstance,bInstance);
	}
  }
  /************************************************************************/
  /* Class: checkPrecedenceBetweenRelations                               */
  /* purpose:                                                             */
  /* recieves two instances , parse them and call for suitable function   */
  /************************************************************************/
  public Instance checkPrecedenceBetweenRelations(Instance aRelation,Instance bRelation)
  {
    if (this.translateRelationToInt(aRelation) > this.translateRelationToInt(bRelation)) {
		return bRelation;
	} else {
		return aRelation;
	}
  }

  /************************************************************************/
  /* function: translateLinkToInt                                         */
  /* purpose:                                                             */
  /* translate the instance to Int                                        */
  /************************************************************************/
  public int translateLinkToInt(LinkInstance  aLink)
  {
    LinkEntry lEntry=(LinkEntry)aLink.getEntry();
    switch (lEntry.getLinkType())
    {
     case OpcatConstants.AGENT_LINK: return CONDITION_LINK;
     case OpcatConstants.AGGREGATION_RELATION: return AGGREGATION_RELATION;
     case OpcatConstants.BI_DIRECTIONAL_RELATION: return BI_DIRECTION_RELATION;
     case OpcatConstants.CONDITION_LINK: return CONDITION_LINK;
     case OpcatConstants.CONSUMPTION_EVENT_LINK : return CONSUMPTION_EVENT_LINK;
    // case OpcatConstants.CONSUMPTION_LINK: return
     case OpcatConstants.EFFECT_LINK : return EFFECT_LINK;
     case OpcatConstants.EXCEPTION_LINK : return EXECPTION_LINK;
     case OpcatConstants.INSTANTINATION_RELATION : return INSTANSATION_RELATION;
     case OpcatConstants.INSTRUMENT_EVENT_LINK : return INSTRUMENT_EVENT_LINK;
     case OpcatConstants.INSTRUMENT_LINK : return INSTRUMENT_LINK;
   //  case OpcatConstants.INSTRUMENT_EVENT_LINK : return INSTRUMENT_EVENT_LINK;
   }

    if (lEntry.getLinkType()==  OpcatConstants.CONDITION_LINK) {
		return CONDITION_LINK;
	}
    if (lEntry.getLinkType()==  OpcatConstants.INSTRUMENT_LINK) {
		return INSTRUMENT_LINK;
	}
    if (lEntry.getLinkType()==  OpcatConstants.AGENT_LINK) {
		return AGENT_LINK;
	}

    return 0;
  }
  /************************************************************************/
  /* function: translateRelationToInt                                     */
  /* purpose:                                                             */
  /* translate the instance to Int                                        */
  /************************************************************************/
  public int translateRelationToInt(Instance aInstance)
  {
    RelationEntry relationEntry=(RelationEntry)aInstance.getEntry();
    if (relationEntry.getRelationType() == OpcatConstants.AGGREGATION_RELATION ) {
		return AGGREGATION_RELATION;
	}
    if (relationEntry.getRelationType() == OpcatConstants.BI_DIRECTIONAL_RELATION ) {
		return BI_DIRECTION_RELATION;
	}
    if (relationEntry.getRelationType() == OpcatConstants.EXHIBITION_RELATION ) {
		return EXHIBITION_RELATION;
	}
    if (relationEntry.getRelationType() == OpcatConstants.INSTANTINATION_RELATION) {
		return INSTANSATION_RELATION;
	}
    if (relationEntry.getRelationType() == OpcatConstants.SPECIALIZATION_RELATION ) {
		return SPECIALIZATION_RELATION;
	}
    if (relationEntry.getRelationType() == OpcatConstants.UNI_DIRECTIONAL_RELATION ) {
		return UNI_DIRECTION_RELATION;
	}
    return 1000;
  }
  public IXLinkInstance checkPrecedenceBetweenLinks(IXLinkInstance aLink,IXLinkInstance bLink)
  {
    return this.checkPrecedenceBetweenLinks((LinkInstance)aLink,(LinkInstance)bLink);
  }
}