package extensionTools.opl2.alg;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IInstance;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IThingEntry;
import exportedAPI.opcatAPI.IThingInstance;
import extensionTools.opl2.generated.OPLscript;
import extensionTools.opl2.generated.ObjectFactory;
import extensionTools.opl2.generated.ThingSentenceSet;

/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author OPCAT team
 * @version 1.0
 */

public abstract class OPLTreeBuilder {

  public OPLTreeBuilder(ISystemStructure myStructure_, String systemName_,ObjectFactory ob) {
    this.myStructure = myStructure_;
    this.systemName = systemName_;
    this.helper = new OPLFactor(this, this.systemName,ob);
    this.objAddr = new OPLObjSentencesAdder(new OPLObjectFactor(this, this.systemName,ob));
    this.procAddr = new OPLProcSentencesAdder(new OPLProcessFactor(this, this.systemName,ob));
  }

  public OPLscript buildGeneralTree() throws Exception {
    OPLscript opl;
    java.util.List e = OPLGeneral.sort(this.getSystemElements(),new MyComparator());
    IEntry mainElem = this.getMainElement();
    if(mainElem!=null){
      e.add(mainElem);
    }
    this.VisitedThings = new HashSet();
    opl = OPLFactor.buildOPLscript(this.systemName);
    this.buildOrderedThings(opl.getThingSentenceSet(),e);
    return opl;
  }

  public void buildOrderedThings(java.util.List things, java.util.List tmpList) throws Exception {
    IEntry abstractEntry;
    Iterator it = tmpList.iterator();
    java.util.List nonVisitedObj = new LinkedList();
    java.util.List nonVisitedProc = new LinkedList();
    boolean isByRoot = true;
    if(tmpList.size()==1){
      abstractEntry = (IEntry)tmpList.get(0);
      this.addThingSentence(things, abstractEntry);
      return;
    }
    while (it.hasNext()) {
      abstractEntry = (IEntry) it.next();
      if (abstractEntry instanceof IObjectEntry){
        if (this.isRoot((IThingEntry) abstractEntry)){
          this.addThingSentence(things, abstractEntry);
        }
        else{
          nonVisitedObj.add(abstractEntry);
          continue;
        }
      }
      else if (abstractEntry instanceof IProcessEntry) {
		nonVisitedProc.add(abstractEntry);
	}
    }
    this.createSentences(things, nonVisitedObj, (!isByRoot));
    this.createSentences(things, nonVisitedProc, isByRoot);
  }

  protected boolean isRoot(IThingEntry abstractEntry) {
    Enumeration e;
    IRelationEntry relation;
    int type;
    try {
      e = abstractEntry.getRelationByDestination();
        //e = getRelationsList(abstractEntry,OPLGeneral.DESTINATION);
        for (; e.hasMoreElements(); ) {
          relation = (IRelationEntry) e.nextElement();
          type = relation.getRelationType();
          if (this.isChildByRelation(type)&&this.testEntity(relation)) {
            return false;
          }
        }
        //return (!hasNonVisitedProcFather(abstractEntry));
        return (!this.hasNonVisitedZoomingFather(abstractEntry));
    }
    catch (Exception p) {
      p.printStackTrace();
    }
    return true;
  }



  //Returns Relation List byDestination or not
  protected Enumeration getRelationsList(IEntry abstractEntry, int byDest) throws
      Exception {
    if (abstractEntry instanceof IObjectEntry) {
      IObjectEntry tmpObj = (IObjectEntry) abstractEntry;
      return (OPLGeneral.isByDestination(byDest)) ?
          tmpObj.getRelationByDestination() : tmpObj.getRelationBySource();
    }
    if (abstractEntry instanceof IProcessEntry) {
      IProcessEntry tmpObj = (IProcessEntry) abstractEntry;
      return (OPLGeneral.isByDestination(byDest)) ?
          tmpObj.getRelationByDestination() : tmpObj.getRelationBySource();
    }
    throw new Exception("The entity has no Relation List");
  }

//Gets ThingSentenceSet
 protected void createSentences(java.util.List things, Collection list,
                                 boolean isByRoot)
 throws Exception {
    IEntry abstractEntry;
    Iterator it = list.iterator();

    while (it.hasNext()) {
      abstractEntry = (IEntry) it.next();
      if((abstractEntry instanceof IObjectEntry) && this.hasNonVisitedProcFather((IObjectEntry)abstractEntry)){
        continue;
      }
      if (!isByRoot || (isByRoot && this.isRoot((IThingEntry) abstractEntry))) {
		this.addThingSentence(things, abstractEntry);
	}
    }
    if (!isByRoot) {
      return;
    }
    this.createSentences(things, list, false);
  }


//Allways first we check by source relations
  private boolean isChildByRelation(int type) {
    return
        ((type == OpcatConstants.SPECIALIZATION_RELATION) ||
         (type == OpcatConstants.EXHIBITION_RELATION) ||
         (type == OpcatConstants.AGGREGATION_RELATION) ||
         (type == OpcatConstants.INSTANTINATION_RELATION)
         );
  }

  //Any sentence should be added through the function
  protected void addThingSentence(java.util.List things, IEntry abstractEntry) {
    try {
      ThingSentenceSet thing = null;
      try {
        if((abstractEntry instanceof IObjectEntry) || (abstractEntry instanceof IProcessEntry)) {
			thing = this.helper.buildThingSentence(abstractEntry);
		}
      }
      catch (OPLFactor.EmptySentenceException t) {
        return;
      }
      if(thing!=null) {
		OPLGeneral.addLast(things, thing);
	}
    }
    catch (Exception e) {
      ;
    }
  }

  protected boolean fillThingSentence(IObjectEntry abstractEntry,
                                      ThingSentenceSet thing) {
    return ( (OPLObjSentencesAdder) this.objAddr).fillThingSentence(abstractEntry,
        thing);
  }

  protected boolean fillThingSentence(IProcessEntry abstractEntry,
                                      ThingSentenceSet thing) {
    return ( (OPLProcSentencesAdder) this.procAddr).fillThingSentence(abstractEntry,
        thing);
  }

      /********HELPER FUNCTIONS ****************************************************/
      /********HELPER FUNCTIONS ****************************************************/

  protected boolean hasNonVisitedProcFather(IThingEntry abstractEntry){
   Enumeration e = abstractEntry.getRelationByDestination();
   IRelationEntry relation;
   IEntry tmp;
   int type;
   while(e.hasMoreElements()){
     relation = (IRelationEntry)e.nextElement();
     if(!this.testEntity(relation)) {
		continue;
	}
     type = relation.getRelationType();
     tmp = this.myStructure.getIEntry(relation.getSourceId());
     if(tmp instanceof IProcessEntry){
     if((type == OpcatConstants.SPECIALIZATION_RELATION) ||
        (type == OpcatConstants.EXHIBITION_RELATION) ||
        (type == OpcatConstants.AGGREGATION_RELATION)){
         if(!this.VisitedThings.contains(tmp)&&this.testEntity(tmp)) {
			return true;
		}
     }
   }
   }
   return this.hasNonVisitedZoomingFather(abstractEntry);
 }



  protected String findFatherName(IEntry abstractEntry) throws Exception {
    Enumeration e;
    IRelationEntry relation;
    int type;
      e = this.getRelationsList(abstractEntry, OPLGeneral.DESTINATION);
      for (; e.hasMoreElements(); ) {
        relation = (IRelationEntry) e.nextElement();
        type = relation.getRelationType();
        if (this.isChildByRelation(type)) {
          return this.myStructure.getIEntry(relation.getSourceId()).getName();
        }
      }
    return this.getZoomingFatherName(abstractEntry);
  }


  protected IEntry getZoomingFather(IEntry abstractEntry) throws Exception{
    IThingInstance thing;
    IInstance inst;
    Enumeration e = abstractEntry.getInstances();
    while(e.hasMoreElements()){
      inst = (IInstance)e.nextElement();
      if(inst instanceof IThingInstance){
        thing = ((IThingInstance)inst).getParentIThingInstance();
        if(thing!=null) {
			return thing.getIEntry();
		}
      }
  }
  return null;
  }

  protected java.util.List getFathersList(IEntry abstractEntry){
    Enumeration e;
    IRelationEntry relation;
    java.util.List fathers = new LinkedList();
    int type;
    try{
      e = this.getRelationsList(abstractEntry, OPLGeneral.DESTINATION);
      for (; e.hasMoreElements(); ) {
        relation = (IRelationEntry) e.nextElement();
        type = relation.getRelationType();
        if (this.isChildByRelation(type)) {
          fathers.add(this.myStructure.getIEntry(relation.getSourceId()));
        }
      }
     IEntry father =this.getZoomingFather(abstractEntry);
     if(father!=null) {
		fathers.add(father);
	}
    }catch(Exception t){;}
  return fathers;
  }

  protected boolean hasNonVisitedZoomingFather(IEntry abstractEntry){
   IThingInstance thing;
   IEntry ent=null;
   IInstance inst;
   Enumeration e = abstractEntry.getInstances();
   while(e.hasMoreElements()){
     inst = (IInstance)e.nextElement();
     if(inst instanceof IThingInstance){
       thing = ((IThingInstance)inst).getParentIThingInstance();
       if(thing!=null){
         ent = thing.getIEntry();
         if((!this.VisitedThings.contains(ent))&&this.isMainElement(ent)&&(ent!=abstractEntry)) {
			return true;
		}
       }
     }
  }
     return false;
  }

  protected String getZoomingFatherName(IEntry abstractEntry) throws Exception{
    IEntry ent = this.getZoomingFather(abstractEntry);
    if(ent!=null) {
		return ent.getName();
	}
    return "";
  }

  protected void addFatherName(IEntry abstractEntry, ThingSentenceSet thing) {
    Enumeration e;
    IRelationEntry relation;
    int type;
    try {
      e = this.getRelationsList(abstractEntry, OPLGeneral.DESTINATION);
      for (; e.hasMoreElements(); ) {
        relation = (IRelationEntry) e.nextElement();
        type = relation.getRelationType();
        if (type == OpcatConstants.EXHIBITION_RELATION) {
          thing.setSubjectExhibitionFatherName(this.myStructure.getIEntry(relation.
              getSourceId()).getName());
        }
        if (type == OpcatConstants.AGGREGATION_RELATION) {
          thing.setSubjectAggregationFatherName(this.myStructure.getIEntry(relation.
              getSourceId()).getName());
        }
      }
    }
    catch (Exception p) {
      p.printStackTrace();
    }
  }

public IInstance getIInstanceByOPD(IEntry obj)
{
    IInstance inst;
    Enumeration el = this.myStructure.getInstancesInOpd(this.currOPD);
       while(el.hasMoreElements()){
         inst = (IInstance)el.nextElement();
         IEntry entry = inst.getIEntry();
         if(entry==obj) {
			return inst;
		}
       }
     return null;
}


  /********ABSTRACT FUNCTIONS ****************************************************/
  /********ABSTRACT FUNCTIONS ****************************************************/
  // Takes only relevant system elements
  protected abstract Enumeration getSystemElements();

  //protected abstract boolean testRelation(IRelationEntry rel);

  //protected abstract boolean testLink(ILinkEntry link);

  //protected abstract boolean testState(IStateEntry link);

  protected abstract boolean testEntity(IEntry link);

  public abstract boolean isMainElement(IEntry elem);

  public abstract IEntry getMainElement();
  /** Structure received from OPCAT API**/
  /** Structure received from OPCAT API**/
  protected ISystemStructure myStructure;

  protected String systemName;

  protected Set VisitedThings = new HashSet();

  protected OPLSentencesAdder objAddr, procAddr;

  protected OPLFactor helper;

  protected ObjectFactory ob;

  protected IEntry mainElem;

  // currOPD is -1 if per root opd
  protected long currOPD;

}
