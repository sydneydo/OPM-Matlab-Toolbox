package extensionTools.opl2.alg;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IProcessInstance;
import exportedAPI.opcatAPI.IRelationEntry;
import extensionTools.opl2.generated.ChangingClause;
import extensionTools.opl2.generated.ChangingSentence;
import extensionTools.opl2.generated.ConditionClause;
import extensionTools.opl2.generated.ConditionSentence;
import extensionTools.opl2.generated.ConditionSentenceType;
import extensionTools.opl2.generated.ConsumptionClause;
import extensionTools.opl2.generated.ConsumptionSentence;
import extensionTools.opl2.generated.ConsumptionSentenceType;
import extensionTools.opl2.generated.EffectClause;
import extensionTools.opl2.generated.EffectSentence;
import extensionTools.opl2.generated.EffectSentenceType;
import extensionTools.opl2.generated.EnablingClause;
import extensionTools.opl2.generated.EnablingSentence;
import extensionTools.opl2.generated.EnablingSentenceType;
import extensionTools.opl2.generated.ExhibitedObject;
import extensionTools.opl2.generated.Operation;
import extensionTools.opl2.generated.ProcessAggregationSentenceSetType;
import extensionTools.opl2.generated.ProcessExhibitionSentenceSetType;
import extensionTools.opl2.generated.ProcessInZoomingSentenceSet;
import extensionTools.opl2.generated.ProcessInvocationSentence;
import extensionTools.opl2.generated.ProcessTimeoutSentence;
import extensionTools.opl2.generated.ResultClause;
import extensionTools.opl2.generated.ResultSentence;
import extensionTools.opl2.generated.ResultSentenceType;
import extensionTools.opl2.generated.ThingSentenceSet;

/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author OPCAT team
 * @version 1.0
 */

public class OPLProcSentencesAdder
    extends OPLSentencesAdder {

  private OPLProcessFactor helper;

  public OPLProcSentencesAdder(OPLProcessFactor factor) {
    this.helper = factor;
    this.ByProc = true;
  }

  protected void addAggregRel(ThingSentenceSet thing,
                              IRelationEntry rel, String objName) throws
  Exception {
    if (thing.getProcessAggregationSentenceSet() == null) {
      thing.setProcessAggregationSentenceSet(
          this.helper.buildProcAggregSentenceSet(objName));
    }
    this.addAggregProc(thing.getProcessAggregationSentenceSet(), rel);
  }

  protected void addExhibRel(ThingSentenceSet thing,
                             IRelationEntry rel, String objName) throws
  Exception {
    try {
      if (thing.getProcessExhibitionSentenceSet() == null) {
        thing.setProcessExhibitionSentenceSet(
            this.helper.buildProcExhibSentenceSet(objName));
      }
      this.addExhibProc(thing.getProcessExhibitionSentenceSet(), rel);
    }
    catch (Exception e) {
      throw e;
    }
  }
 
  protected void addInhRelSentence(ThingSentenceSet thing,
        IRelationEntry rel) throws Exception {
if(thing.getProcessInheritanceSentenceSet()==null){
thing.setProcessInheritanceSentenceSet(this.helper.buildProcInhSentenceSet(rel));
} else {
	thing.getProcessInheritanceSentenceSet().getProcessInheritanceSentence().add(this.helper.buildProcInhSentence(rel));
}
}

  protected void addInstRelSentence(ThingSentenceSet thing,
                                             IRelationEntry rel)throws
      Exception{
     thing.setProcessInstanceSentence(this.helper.buildProcInstSentence(rel));
  }


  protected void addUniDirRelSentence(ThingSentenceSet thing,
                                      IRelationEntry rel) throws Exception {
    OPLGeneral.addLast(thing.getProcessUniDirectionalRelationSentence(),
                       this.helper.buildProcUniDirSentence(rel));
  }

  protected void addBiDirRelSentence(ThingSentenceSet thing,
                                     IRelationEntry rel, int rule) throws
  Exception {
    OPLGeneral.addLast(thing.getProcessBiDirectionalRelationSentence(),
                       this.helper.buildProcBiDirSentence(rel, rule));
  }

  protected void addExhibProc(ProcessExhibitionSentenceSetType p,
                              IRelationEntry rel) {
    IEntry destination = this.helper.getEntry(rel);
    try {
      if (destination instanceof IObjectEntry) {
        ExhibitedObject obj = this.helper.buildExhibitedObject(destination, rel);
        OPLGeneral.addLast(p.getProcessExhibitionSentence().getExhibitedObject(),
                           obj);
      }
      else {
        Operation op = this.helper.buildOperation(destination.getName());
        OPLGeneral.addLast(p.getProcessExhibitionSentence().getOperation(), op);
      }
      //System.err.println("Before adding sentence about: "+ destination.getName());
      this.helper.builder.addThingSentence(p.getThingSentenceSet(), destination);

    }
    catch (Exception e) {
      ;
    }
  }

  protected void addAggregProc(ProcessAggregationSentenceSetType p,
                               IRelationEntry rel) {
    IEntry destination = this.helper.getEntry(rel);
    try {
      String aggregatedProcess = destination.getName();
      OPLGeneral.addLast(p.getProcessAggregationSentence().getAggregatedProcess(),
                         aggregatedProcess);
      this.helper.builder.addThingSentence(p.getThingSentenceSet(), destination);
    }
    catch (Exception e) {
      ;
    }
  }

  protected boolean specificLink(ThingSentenceSet thing,
                                 ILinkEntry link, String objName, int rule) throws
  Exception {
    int type = link.getLinkType();
    String path = link.getPath();
    try {
      switch (type) {
        case OpcatConstants.CONDITION_LINK:
          if (OPLGeneral.isByDestination(rule)) {
            this.addConditionLink(thing, link, objName, path);
            return true;
          }return false;
        case OpcatConstants.CONSUMPTION_LINK:
          if (OPLGeneral.isByDestination(rule)) {
            this.addConsumptionLink(thing, link, objName, path);
            return true;
          }return false;
        case OpcatConstants.CONSUMPTION_EVENT_LINK:
          if (OPLGeneral.isByDestination(rule)) {
             this.addConsumptionLink(thing, link, objName, path);
             return true;
           }return false;
        case OpcatConstants.INSTRUMENT_EVENT_LINK:
          if (OPLGeneral.isByDestination(rule)) {
            this.addEnablingLink(thing, link, objName, path);
            return true;
          }return false;
        case OpcatConstants.EFFECT_LINK:
          if (OPLGeneral.isByDestination(rule)) {
            this.addEffectLink(thing, link, objName, path);
            return true;
          }return false;
        case OpcatConstants.EXCEPTION_LINK:
          if (!OPLGeneral.isByDestination(rule)) {
            ProcessTimeoutSentence sentence = this.getProcessTimeoutSentenceByPath(thing, link, path);
            this.addProcessTimeoutSentenceDestination(sentence, link);
            return true;
          }return false;
        case OpcatConstants.INSTRUMENT_LINK:
          if (OPLGeneral.isByDestination(rule)) {
            this.addEnablingLink(thing, link, objName, path);
            return true;
          }return false;
        case OpcatConstants.INVOCATION_LINK:
          if (!OPLGeneral.isByDestination(rule)) {
            ProcessInvocationSentence sentence = this.getProcessInvocationSentenceByPath(thing, link, path);
            this.addProcessInvocationSentenceDestination(sentence, link);
            return true;
          }return false;
        case OpcatConstants.RESULT_LINK:
          if (!OPLGeneral.isByDestination(rule)) {
            this.addResultLink(thing, link, objName, path);
            return true;
          }
        default:
          return false;
      }
    }
    catch (Exception e) {
      throw e;
    }
  }

  protected ProcessInvocationSentence getProcessInvocationSentenceByPath(ThingSentenceSet thing,
      ILinkEntry link, String path) throws
  Exception {
    int size = thing.getProcessInvocationSentence().size();
    ProcessInvocationSentence sentence;
    for (int i = 0; i < size; i++) {
      sentence = (ProcessInvocationSentence) thing.getProcessInvocationSentence().get(i);
      if (OPLGeneral.hasEqualPath(sentence.getPathLabel(), path)) {
        if(this.helper.setLogicBySource(link).equals(sentence.getLogicalRelation())) {
			return sentence;
		}
      }
    }
    sentence = this.helper.buildProcessInvocationSentence(link, path);
    thing.getProcessInvocationSentence().add(sentence);
    return sentence;
  }

  protected void addProcessInvocationSentenceDestination(ProcessInvocationSentence sentence,
      ILinkEntry link) {
    String procName = this.helper.builder.myStructure.getIEntry(link.
        getDestinationId()).getName();
    sentence.getTriggeredProcess().add(procName);
  }

  protected ProcessTimeoutSentence getProcessTimeoutSentenceByPath(ThingSentenceSet thing,
      ILinkEntry link, String path) throws
  Exception {
    int size = thing.getProcessTimeoutSentence().size();
    ProcessTimeoutSentence sentence;
    for (int i = 0; i < size; i++) {
      sentence = (ProcessTimeoutSentence) thing.getProcessTimeoutSentence().get(i);
      if (OPLGeneral.hasEqualPath(sentence.getPathLabel(), path)) {
      if(this.helper.setLogicBySource(link).equals(sentence.getLogicalRelation())) {
		return sentence;
	}
      }
    }
    sentence = this.helper.buildProcessTimeoutSentence(link, path);
    thing.getProcessTimeoutSentence().add(sentence);
    return sentence;
  }

  protected void addProcessTimeoutSentenceDestination(ProcessTimeoutSentence sentence,
      ILinkEntry link) {
    String procName = this.helper.builder.myStructure.getIEntry(link.
        getDestinationId()).getName();
    sentence.getTriggeredProcess().add(procName);
  }

  private void addConditionLink(ThingSentenceSet thing,
                                ILinkEntry rel, String objName, String path) throws
  Exception {

    int size = thing.getConditionSentence().size();
    ConditionSentence sentence=null;
    boolean found = false;
    for (int i = 0; i < size; i++) {
      sentence = (ConditionSentence) thing.getConditionSentence().get(i);
      if (OPLGeneral.hasEqualPath(sentence.getPathLabel(), path)) {
        if(this.helper.setLogicByDestination(rel).equals(sentence.getLogicalRelation())){
        found=true;
        break;
       }
      }
    }
    if(!found){
      sentence = this.helper.buildConditionSentence(objName, this.helper.setLogicByDestination(rel), path);
      thing.getConditionSentence().add(sentence);
    }
    this.addConditionClause(sentence, rel, thing);
  }

  private void addConditionClause(ConditionSentenceType sentence,
                                  ILinkEntry link, ThingSentenceSet thing) throws
  Exception {
    ConditionClause clause = this.helper.buildConditionClause(link);
    clause.setSubjectAggregationFatherName(thing.
        getSubjectAggregationFatherName());
    clause.setSubjectExhibitionFatherName(thing.getSubjectExhibitionFatherName());
    clause.setSubjectThingName(thing.getSubjectThingName());
    OPLGeneral.addLast(sentence.getConditionClause(), clause);
  }

  protected ResultSentence getResultsSentenceByPath(ThingSentenceSet thing,
      java.util.List path) throws Exception {
    int size = thing.getResultSentence().size();
    ResultSentence sentence;
    for (int i = 0; i < size; i++) {
      sentence = (ResultSentence) thing.getResultSentence().get(i);
      if(OPLGeneral.equalPaths(sentence.getPathLabel(),path)) {
		if(sentence.getLogicalRelation().equals("and")) {
			return sentence;
		}
	}
    }
    return null;
  }

  /***************************************************************************/

  private void addEffectLink(ThingSentenceSet thing,
                             ILinkEntry rel, String objName, String path) throws Exception {
    int size = thing.getEffectSentence().size();
    EffectSentence sentence=null;
    boolean found = false;
    for (int i = 0; i < size; i++) {
      sentence = (EffectSentence) thing.getEffectSentence().get(i);
      if (OPLGeneral.hasEqualPath(sentence.getPathLabel(), path)) {
        if(this.helper.setLogicByDestination(rel).equals(sentence.getLogicalRelation())){
          found = true;
          break;
        }
      }
    }
    if(!found){
      sentence = this.helper.buildEffectSentence(objName, this.helper.setLogicByDestination(rel), path);
      thing.getEffectSentence().add(sentence);
    }
    this.addEffectClause(sentence, rel, thing);
  }

  private void addEffectClause(EffectSentenceType sentence,
                               ILinkEntry link, ThingSentenceSet thing) throws
  Exception {
    EffectClause clause = this.helper.buildEffectClause(link);
    clause.setSubjectAggregationFatherName(thing.
        getSubjectAggregationFatherName());
    clause.setSubjectExhibitionFatherName(thing.getSubjectExhibitionFatherName());
    clause.setSubjectThingName(thing.getSubjectThingName());
    OPLGeneral.addLast(sentence.getEffectClause(), clause);
  }

  private void addConsumptionLink(ThingSentenceSet thing,
                                  ILinkEntry rel, String objName, String path) throws
  Exception {
    int size = thing.getConsumptionSentence().size();
    ConsumptionSentence sentence=null;
    boolean found = false;
    for (int i = 0; i < size; i++) {
      sentence = (ConsumptionSentence) thing.getConsumptionSentence().get(i);
      if (OPLGeneral.hasEqualPath(sentence.getPathLabel(), path)) {
        if(this.helper.setLogicByDestination(rel).equals(sentence.getLogicalRelation())){
          found = true;
          break;
        }
      }
    }
    if(!found){
      sentence = this.helper.buildConsumptionSentence(objName, this.helper.setLogicByDestination(rel), path);
      thing.getConsumptionSentence().add(sentence);
    }
    this.addConsumptionClause(sentence, rel, thing);
  }

  private void addConsumptionClause(ConsumptionSentenceType sentence,
                                    ILinkEntry link, ThingSentenceSet thing) throws
  Exception {
    ConsumptionClause clause = this.helper.buildConsumptionClause(link);
    clause.setSubjectAggregationFatherName(thing.
        getSubjectAggregationFatherName());
    clause.setSubjectExhibitionFatherName(thing.getSubjectExhibitionFatherName());
    clause.setSubjectThingName(thing.getSubjectThingName());
    OPLGeneral.addLast(sentence.getConsumptionClause(), clause);
  }

  private void addEnablingLink(ThingSentenceSet thing,
                               ILinkEntry rel, String objName, String path) throws Exception {
   int size = thing.getEnablingSentence().size();
   EnablingSentence sentence=null;
   boolean found = false;
   for (int i = 0; i < size; i++) {
     sentence = (EnablingSentence) thing.getEnablingSentence().get(i);
     if (OPLGeneral.hasEqualPath(sentence.getPathLabel(), path)) {
       if(this.helper.setLogicByDestination(rel).equals(sentence.getLogicalRelation())){
         found = true;
         break;
       }
     }
   }
   if(!found){
     sentence = this.helper.buildEnablingSentence(objName, this.helper.setLogicByDestination(rel), path);
     thing.getEnablingSentence().add(sentence);
   }
    this.addEnablingClause(sentence, rel, thing);
  }

  private void addEnablingClause(EnablingSentenceType sentence,
                                 ILinkEntry link, ThingSentenceSet thing) throws
  Exception {
    EnablingClause clause = this.helper.buildEnablingClause(link);
    clause.setSubjectAggregationFatherName(thing.
        getSubjectAggregationFatherName());
    clause.setSubjectExhibitionFatherName(thing.getSubjectExhibitionFatherName());
    clause.setSubjectThingName(thing.getSubjectThingName());
    OPLGeneral.addLast(sentence.getEnablingClause(), clause);
  }

  private void addResultLink(ThingSentenceSet thing,
                             ILinkEntry rel, String objName, String path) throws Exception {
   int size = thing.getResultSentence().size();
   ResultSentence sentence=null;
   boolean found = false;
   for (int i = 0; i < size; i++) {
     sentence = (ResultSentence) thing.getResultSentence().get(i);
     if (OPLGeneral.hasEqualPath(sentence.getPathLabel(), path)) {
       if(this.helper.setLogicBySource(rel).equals(sentence.getLogicalRelation())){
       found=true;
       break;}
     }
   }
   if(!found){
     sentence = this.helper.buildResultSentence(objName, this.helper.setLogicBySource(rel), path);
     thing.getResultSentence().add(sentence);
   }
    this.addResultClause(sentence, rel, thing);
  }

  private void addResultClause(ResultSentenceType sentence,
                               ILinkEntry link, ThingSentenceSet thing) throws
  Exception {
    ResultClause clause = this.helper.buildResultClause(link);
    clause.setSubjectAggregationFatherName(thing.
        getSubjectAggregationFatherName());
    clause.setSubjectExhibitionFatherName(thing.getSubjectExhibitionFatherName());
    clause.setSubjectThingName(thing.getSubjectThingName());
    sentence.getResultClause().add(clause);
  }

  /***********************************************************************/

  public boolean fillLinks(Enumeration e, String objName,
                           ThingSentenceSet thing, int rule) throws Exception {
    boolean hasLink = false;
    ILinkEntry link;
    java.util.List lst = OPLGeneral.sort(e,new MyLinksComparator());
    //while (e.hasMoreElements()) {
    for(int i=0;i<lst.size();i++) {
      link = (ILinkEntry) lst.get(i);

//    while (e.hasMoreElements()) {
  //    link = (ILinkEntry) e.nextElement();
      if (this.helper.builder.testEntity(link) &&
          this.specificLink(thing, link, objName, rule)) {
		hasLink = true;
	}
    }
    return hasLink;
  }

  public boolean testRelation(IRelationEntry rel) {
    return this.helper.builder.testEntity(rel);
  }

  public boolean fillThingSentence(IProcessEntry abstractEntry,
                                   ThingSentenceSet thing) {
    String objName = abstractEntry.getName();
    Enumeration e = abstractEntry.getRelationBySource();
    boolean hasZooming = false;
    boolean hasLinks = false;
    boolean hasRelations = false;
    boolean hasEnvironmentalPhysical = false;
    try {
     // if(abstractEntry.getRoles()!=null){
          // Inserting role!!!
          
      //}

     if(this.helper.builder.isMainElement(abstractEntry)){
       hasZooming = this.createProcessInZoomingSentence(thing, abstractEntry);
     }

      for (int rule = 0; rule <= 1; rule++) {
        if (rule == 1) {
          e = abstractEntry.getRelationByDestination();
        }
          if (!hasRelations) {
			hasRelations = this.fillRelations(e, objName, rule, thing, this.helper.builder);
		} else {
			this.fillRelations(e, objName, rule, thing,this.helper.builder);
		}
      }
      hasEnvironmentalPhysical = this.addEnvironmentelPhysical(abstractEntry, thing);
      e = abstractEntry.getLinksBySource();
      for (int rule = 0; rule <= 1; rule++) {
        if (rule == 1) {
          e = abstractEntry.getLinksByDestination();
        }
      if (!hasLinks) {
		hasLinks = this.fillLinks(e, objName, thing,rule);
	} else {
		this.fillLinks(e, objName, thing,rule);
	}
      }
      if (hasLinks){
        this.createChangingSentence(thing);
        this.createChangingSentence(thing);
        this.createChangingSentence(thing);
      }
      if(hasZooming || hasLinks || hasRelations || hasEnvironmentalPhysical || hasZooming){
        this.addDescription(abstractEntry, thing);
        this.setScope(thing, abstractEntry);
        if((abstractEntry.getRole()!=null) && !abstractEntry.getRole().equals("")){
          // Inserting role!!!
          thing.getRole().addAll(this.helper.buildRoles(abstractEntry));
          //thing.setSubjectRole(abstractEntry.getRole());
        }
        thing.setTransID("2");
		thing.getRole().addAll(this.helper.buildRoles(abstractEntry));
        return true;
      }
	  thing.setExistential("operates");
	  thing.setTransID("3");
	  return true;
    }
    catch (Exception t) {
      t.printStackTrace();
    }
    return false;
  }


//Changing Clause is OK
  protected void createChangingSentence(ThingSentenceSet thing) throws
  Exception {
    ConsumptionSentenceType cons = null;
    ResultSentenceType resultByPath = null;
    Set tmpResults = new HashSet(), tmpConsumps = new HashSet();
    for(int i=0; i<thing.getConsumptionSentence().size();i++){
      cons = (ConsumptionSentence)thing.getConsumptionSentence().get(i);
      if(!cons.getLogicalRelation().equals("and")) {
		continue;
	}
      Iterator clauses = cons.getConsumptionClause().iterator();
      resultByPath = this.getResultsSentenceByPath(thing,cons.getPathLabel());
      if(resultByPath==null) {
		continue;
	}
      java.util.List results = resultByPath.getResultClause();
      while (clauses.hasNext()) {
        ConsumptionClause cl = (ConsumptionClause) clauses.next();
        if (cl.getStateName() != null) {
          java.util.List res = this.getResultWithObj(results, cl.getObjectName());
          if(!res.isEmpty()){
            tmpConsumps.add(cl);
            for(Iterator it=res.iterator();it.hasNext();){
              ResultClause ress = (ResultClause)it.next();
              if (ress != null) {
                this.addChangingClause(thing, ress, cl,cons.getPathLabel());
                tmpResults.add(ress);
              }
            }

          }
        }
      }
      this.cleanResults(resultByPath, tmpResults);
      this.cleanConsumps(cons, tmpConsumps);
      tmpResults.clear();
      tmpConsumps.clear();
      this.cleanThing(thing, resultByPath, cons);
    }
  }

  protected void cleanThing(ThingSentenceSet thing, ResultSentenceType tmpResults,
                            ConsumptionSentenceType tmpConsumps) {
    if(tmpResults.getResultClause().isEmpty()){
      thing.getResultSentence().remove(tmpResults);
    }
    if(tmpConsumps.getConsumptionClause().isEmpty()){
      thing.getConsumptionSentence().remove(tmpConsumps);
    }
  }

  protected void cleanResults(ResultSentenceType tmpResults,
                            java.util.Set closes) {
    tmpResults.getResultClause().removeAll(closes);
  }

  protected void cleanConsumps(ConsumptionSentenceType tmpResults,
                            java.util.Set closes) {
    tmpResults.getConsumptionClause().removeAll(closes);
  }


  protected java.util.List getResultWithObj(java.util.List results,
      String objName) {
    java.util.List ress = new LinkedList();
    ResultClause res;
    if (results == null) {
		return null;
	}
    Iterator it = results.iterator();
    while (it.hasNext()) {
      res = (ResultClause) it.next();
      if (res.getStateName() != null) {
		if (res.getObjectName() == objName) {
			ress.add(res);
		}
	}
    }
    return ress;
  }

//!!!!!!!!!!!!!!00000000000000000000000000000
  protected void addChangingClause(ThingSentenceSet thing,
                                   ResultClause res, ConsumptionClause cl,java.util.List path) throws
  Exception {
    ChangingSentence sentence=null;
    boolean found = false;
    //System.out.println("Creating new changing "+ res.getStateName()+" "+cl.getStateName());
    for (int i = 0; i < thing.getChangingSentence().size(); i++) {
      sentence = (ChangingSentence) thing.getChangingSentence().get(i);
      if (OPLGeneral.equalPaths(sentence.getPathLabel(), path)) {
        found=true;
        break;
      }
    }
    if(!found){
      sentence = this.helper.buildChangingSentence(res.getSubjectThingName(), res.getLogicalRelation(), path);
      //Here to add sentence according to its path!!!
      this.addChan(thing.getChangingSentence(),sentence);
      //thing.getChangingSentence().add(sentence);
    }
    ChangingClause change = this.helper.buildChangingClause(cl, res);
    sentence.getChangingClause().add(change);
  }

  private void addChan(java.util.List chans, ChangingSentence sentence){
    int index=-1;
    for(int i=0;(i<chans.size()) && (index==-1);i++){
       java.util.List pathSource = ((ChangingSentence)chans.get(i)).getPathLabel();
       if(pathSource.size()>sentence.getPathLabel().size()) {
		index=i;
	} else {
		for(int j=0;j<pathSource.size();j++){
		       if(pathSource.get(j).toString().compareTo(sentence.getPathLabel().get(j).toString())>-1) {
				index = i;
			}
		         break;
		     }
	}
     }
     if(index==-1) {
		chans.add(sentence);
	} else {
		chans.add(index,sentence);
	}
  }


  private boolean setScope(ThingSentenceSet sentence,IProcessEntry obj){
  if (obj.getScope().equals(OpcatConstants.PRIVATE)) {
	sentence.setProcessScope("private");
} else {
        if (obj.getScope().equals(OpcatConstants.PROTECTED)) {
			sentence.setProcessScope("protected");
		} else{
          sentence.setProcessScope("public");
          return false;
        }
      }
  return true;
  }


  protected boolean createProcessInZoomingSentence(ThingSentenceSet thing,
      IProcessEntry proc) throws Exception {
    long oldOPD = this.helper.builder.currOPD;
    if(!this.helper.builder.testEntity(proc)) {
		return false;
	}
    Enumeration it = proc.getInstances();
  //  thing.setTransID("2");
    IProcessInstance p;
    ProcessInZoomingSentenceSet procs=null;
    java.util.List tmpList = new LinkedList();
    while (it.hasMoreElements()) {
      p = (IProcessInstance) it.nextElement();
      if (p.isZoomedIn()) {
        procs =this.helper.buildProcessInZoomingSentenceSet(proc.getName());

        this.helper.builder.currOPD = p.getKey().getOpdId();
        tmpList = OPLGeneral.sort1((p).getChildThings(),
                                new MyThingsComparator(true, this.helper.builder));


        break;
      }
    }
    if(procs!=null){
      this.addZoomingThings(procs, tmpList);
      thing.setProcessInZoomingSentenceSet(procs);
      this.helper.builder.currOPD = oldOPD;
      return true;
    }
    this.helper.builder.currOPD = oldOPD;
    return false;
  }

  protected void addZoomingThings(ProcessInZoomingSentenceSet process, java.util.List tmpList)
  throws Exception{
    for(int i=0;i<tmpList.size();i++){
      IEntry ent = (IEntry)tmpList.get(i);
      if(ent instanceof IObjectEntry){
        process.getProcessInZoomingSentence().getInZoomedObject().add(ent.getName());
      }else if(ent instanceof IProcessEntry){
        process.getProcessInZoomingSentence().getInZoomedProcess().add(ent.getName());
      }
    try{
      //ThingSentenceSet s = helper.buildThingSentence(ent);
      this.helper.builder.addThingSentence(process.getThingSentenceSet(),ent);
      //process.getThingSentenceSet().add(helper.buildThingSentence(ent));
    }catch (Exception e){;}
    }
  }


  /********************************************************************/
  /***************************************************************88****8*/
//  protected boolean createProcessInZoomingSentence(ThingSentenceSet thing, IProcessEntry proc)
//throws Exception{
//
//  Enumeration it = proc.getInstances();
//  Enumeration children;
//  IProcessInstance p;
//  java.util.List tmpObjs = new LinkedList();
//  java.util.List tmpProcs = new LinkedList();
//  while(it.hasMoreElements()){
//      p = (IProcessInstance)it.nextElement();
//      if(p.isZoomedIn()){
//        ProcessInZoomingSentenceSet procs =
//            helper.buildProcessInZoomingSentenceSet(proc.getName());
//        ProcessInZoomingSentenceType sentence =
//            procs.getProcessInZoomingSentence();
//        children = p.getChildThings();
//        while(children.hasMoreElements()){
//          IEntry abstractEntry =
//              ((IThingInstance)children.nextElement()).getIEntry();
//          if(abstractEntry instanceof IObjectEntry){
//            tmpObjs.add(abstractEntry);
//            sentence.getInZoomedObject().add(abstractEntry.getName());
//          }
//          else if(abstractEntry instanceof IProcessEntry){
//            tmpProcs.add(abstractEntry);
//            sentence.getInZoomedProcess().add(abstractEntry.getName());
//          }
//        }
//        addInZoomedSentences(procs.getThingSentenceSet(),tmpObjs);
//        addInZoomedSentences(procs.getThingSentenceSet(),tmpProcs);
//        thing.setProcessInZoomingSentenceSet(procs);
//        return true;
//      }
//  }
//return false;
//}
//
//protected void addInZoomedSentences(java.util.List thing, java.util.List entities){
//  Iterator it = entities.iterator();
//  IEntry abstractEntry;
//  while(it.hasNext()){
//    abstractEntry = (IEntry)it.next();
//    helper.builder.addThingSentence(thing,abstractEntry);
//  }
//}


//  protected void addInZoomedSentences(java.util.List thing,
//                                      java.util.List entities) {
//    Iterator it = entities.iterator();
//    IEntry abstractEntry;
//    while (it.hasNext()) {
//      abstractEntry = (IEntry) it.next();
//      helper.builder.addThingSentence(thing, abstractEntry);
//    }
//  }

  protected boolean addEnvironmentelPhysical(IProcessEntry abstractEntry,
      ThingSentenceSet thing) throws
  Exception {
    if (abstractEntry.isPhysical() || abstractEntry.isEnvironmental()) {
      thing.setProcessEnvironmentalPhysicalSentence
      (this.helper.buildProcEnvPhysicalSentence(abstractEntry));
      return true;
    }
    return false;
  }
  public boolean ByProc;
}