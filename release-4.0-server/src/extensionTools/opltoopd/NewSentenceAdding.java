package extensionTools.opltoopd;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JOptionPane;
import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPIx.IXCheckResult;
import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXEntry;
import exportedAPI.opcatAPIx.IXInstance;
import exportedAPI.opcatAPIx.IXLinkEntry;
import exportedAPI.opcatAPIx.IXLinkInstance;
import exportedAPI.opcatAPIx.IXObjectEntry;
import exportedAPI.opcatAPIx.IXObjectInstance;
import exportedAPI.opcatAPIx.IXOpd;
import exportedAPI.opcatAPIx.IXProcessEntry;
import exportedAPI.opcatAPIx.IXProcessInstance;
import exportedAPI.opcatAPIx.IXRelationEntry;
import exportedAPI.opcatAPIx.IXRelationInstance;
import exportedAPI.opcatAPIx.IXStateEntry;
import exportedAPI.opcatAPIx.IXStateInstance;
import exportedAPI.opcatAPIx.IXSystem;
import exportedAPI.opcatAPIx.IXSystemStructure;
import exportedAPI.opcatAPIx.IXThingInstance;

/**
 * Represents class which takes care
 * of new sentence adding to the OPD.
 */

public class NewSentenceAdding {

  /**
   * @param system the running IXSystem
   */

  public NewSentenceAdding(IXSystem system) {
    this.iXSystem = system;
    this.curOpdId = this.iXSystem.getCurrentIXOpd().getOpdId();
    this.systStruct = this.iXSystem.getIXSystemStructure();
  }

  /** This function returns an existing Object Instance in the needed OPD
   * if it already was in the system or the new Object Instance
   * (the existence of the Object Instance is checked
   * according to the object name and given OPD)
   * @param newSent an Object for checking
   * @param opdId an OPD ID of the needed OPD
   * @param newOne true if we added this Object in the running session.
   * @return IXObjectInstance
   */
  public IXObjectInstance newObjectAdding(ObjectStruct newSent,long opdId,boolean newOne){
    newOne = false;
    if(opdId==1111){
      return null;
    }
    if(opdId==0){
      opdId = this.curOpdId;
    }

    if (this.ifNewThing(newSent.getObjectName())){
      FreeSpaceList list = new FreeSpaceList(this.iXSystem,opdId);
      list.updateList(this.spareSpaceForNew);

      IXObjectInstance newObj = this.iXSystem.addObject(newSent.getObjectName(),50,30,opdId);
      if(!newSent.States.isEmpty()){
        Iterator iter = newSent.States.iterator();
        while(iter.hasNext()){
          State state = (State)iter.next();
          IXStateEntry newStateEntry = this.iXSystem.addState(state.getStateName(),newObj);
          this.newStateUpdate(state,newStateEntry);
          newOne = true;
        }
      }
      this.addThingToList(newObj, list);
      this.newObjectUpdate(newSent, newObj);
      return newObj;
    }
    Enumeration locEnum = this.getThingEntry(newSent.getObjectName()).getInstances();
    IXObjectInstance existingObj = null;
    while(locEnum.hasMoreElements()){
      existingObj = (IXObjectInstance)locEnum.nextElement();
      if(existingObj.getKey().getOpdId()==opdId){
        if(!newSent.States.isEmpty()){
          Iterator iter = newSent.States.iterator();
          while(iter.hasNext()){
            State state = (State)iter.next();
            Enumeration objStatesEnum = existingObj.getStateInstances();
            boolean stateFlag=true;
            while(objStatesEnum.hasMoreElements()){
              IXStateInstance newState = (IXStateInstance)objStatesEnum.nextElement();
              if ((newState.getIXEntry().getName()).equals(state.getStateName())){
                this.newStateUpdate(state,(IXStateEntry)newState.getIXEntry());
                stateFlag=false;
              }
            }
            if (stateFlag){
              IXStateEntry newStateEntry = this.iXSystem.addState(state.getStateName(),existingObj);
              this.newStateUpdate(state,newStateEntry);
            }
          }
        }
        this.newObjectUpdate(newSent, existingObj);
        newOne = false;
        return existingObj;
      }
    }
    FreeSpaceList list = new FreeSpaceList(this.iXSystem,opdId);
    list.updateList(this.spareSpaceForNew);
    IXObjectInstance newObj = this.iXSystem.addObject(newSent.getObjectName(),50,30,opdId);
    if(!newSent.States.isEmpty()){
      Iterator iter = newSent.States.iterator();
      while(iter.hasNext()){
        State state = (State)iter.next();
        IXStateEntry newStateEntry = this.iXSystem.addState(state.getStateName(),newObj);
        this.newStateUpdate(state,newStateEntry);
      }
    }
    this.addThingToList(newObj, list);
    this.newObjectUpdate(newSent, newObj);
    newOne = true;
    return newObj;
  }

  /**This function returns an existing Process Instance in the needed OPD
   * if it already was in the system or the new Process Instance
   * (the existence of the Process Instance is checked
   * according to the process name and given OPD)
   * @param newSent a Process for checking
   * @param opdId an OPD ID of the needed OPD
   * @param newOne true if we added this Object in the running session.
   * @return IXProcessInstance
   */

  public IXProcessInstance newProcessAdding(ProcessStruct newSent,long opdId, boolean newOne){
    newOne = false;
    if(opdId==1111){
      return null;
    }
    if (opdId==0){
      opdId = this.curOpdId;
    }
    if (this.ifNewThing(newSent.getProcessName())){
      FreeSpaceList list = new FreeSpaceList(this.iXSystem,opdId);
      list.updateList(this.spareSpaceForNew);
      IXProcessInstance newProc = this.iXSystem.addProcess(newSent.getProcessName(),50,30,opdId);
      this.addThingToList(newProc, list);
      this.newProcessUpdate(newSent, newProc);
      newOne = true;
      return newProc;
    }
    Enumeration locEnum = this.getThingEntry(newSent.getProcessName()).getInstances();
    IXProcessInstance existingProc = null;
    /*if process is new in hierarchy then it is one and only instance for entry
    and we will return it.
    if there is no hierarchy, and there are one or few instances then will
    return instance on current opd(if exists) or any other   */
    while(locEnum.hasMoreElements()){
      existingProc = (IXProcessInstance)locEnum.nextElement();
      if(existingProc.getKey().getOpdId()==opdId){
        this.newProcessUpdate(newSent,existingProc);
        return existingProc;
      }
    }
    FreeSpaceList list = new FreeSpaceList(this.iXSystem,opdId);
    list.updateList(this.spareSpaceForNew);
    IXProcessInstance newProc = this.iXSystem.addProcess(newSent.getProcessName(),50,30,opdId);
    this.addThingToList(newProc, list);
    this.newProcessUpdate(newSent, newProc);
    newOne = true;
    return newProc;
  }

  /**This function check with user to which OPD he wants to add the new Link sentence
   * and adds it to the needed OPD
   * @param newSent the new sentence
   */
  public void newLinkSentence(LinkStruct newSent){
    IXObjectInstance sourceObj = null;
    IXProcessInstance sourceProc = null;
    IXObjectInstance destObj;
    IXProcessInstance destProc;
    boolean newSourse = false;
    boolean newDest = false;
    long neededOpdId;
    Iterator iter = newSent.getDestObjects();

    if (this.ifNewSentence(newSent)){
      neededOpdId = this.curOpdId;
    }
    else{
      /*Here we give the list of the OPDs to User and he choose the only one she wants */
      HashSet proc_screens = new HashSet();
      HashSet obj_screens = new HashSet();
      if (newSent.getSourceProcess()!=null){
        if (!this.ifNewThing(newSent.getSourceProcess().getProcessName())){
          Enumeration insts  = this.getThingEntry(newSent.getSourceProcess().getProcessName()).getInstances();
          while(insts.hasMoreElements()){
            long key = (((IXProcessInstance)insts.nextElement()).getKey()).getOpdId();
            proc_screens.add((this.systStruct.getIXOpd(key)));
      }}}
      if (newSent.getSourceObject()!=null){
        if (!this.ifNewThing(newSent.getSourceObject().getObjectName())){
          Enumeration insts  = this.getThingEntry(newSent.getSourceObject().getObjectName()).getInstances();
          while(insts.hasMoreElements()){
            long key = (((IXObjectInstance)insts.nextElement()).getKey()).getOpdId();
            obj_screens.add((this.systStruct.getIXOpd(key)));
      }}}
      while(iter.hasNext()){
        ObjectStruct curDestObj = (ObjectStruct)iter.next();
        if (!this.ifNewThing(curDestObj.getObjectName())){
          Enumeration insts  = this.getThingEntry(curDestObj.getObjectName()).getInstances();
          while(insts.hasMoreElements()){
            long key = (((IXObjectInstance)insts.nextElement()).getKey()).getOpdId();
            obj_screens.add((this.systStruct.getIXOpd(key)));
      }}}
      iter = newSent.getDestProcesses();
      while(iter.hasNext()){
        ProcessStruct curDestProcess = (ProcessStruct)iter.next();
        if (!this.ifNewThing(curDestProcess.getProcessName())){
          Enumeration insts  = this.getThingEntry(curDestProcess.getProcessName()).getInstances();
          while(insts.hasMoreElements()){
            long key = (((IXProcessInstance)insts.nextElement()).getKey()).getOpdId();
            obj_screens.add((this.systStruct.getIXOpd(key)));
      }}}
      if (!proc_screens.isEmpty()){
        neededOpdId = this.getNumFromUser(proc_screens);
      }
      else {neededOpdId = this.getNumFromUser(obj_screens);}
      /****************************  end of OPD choose ********************************* */
    }
    iter = newSent.getDestObjects();
    /********************if the source is Object*************/
    if (newSent.getSourceObject()!=null){
      sourceObj = this.newObjectAdding(newSent.getSourceObject(),neededOpdId,newSourse);
      if (sourceObj==null){
        return;
      }
      /**********if the destinations are Objects*************/
      while(iter.hasNext()){
        ObjectStruct curDestObj = (ObjectStruct)iter.next();
        destObj = this.newObjectAdding(curDestObj,neededOpdId, newDest);
        if (destObj==null){
          return;
        }
        this.addnewLink (sourceObj,destObj,newSent,newSourse,newDest);
      }
      iter = newSent.getDestProcesses();
      while(iter.hasNext()){
        /**********if the destinations are Processes****************/
        ProcessStruct curDestProc = (ProcessStruct)iter.next();
        destProc = this.newProcessAdding(curDestProc,neededOpdId,newDest);
        if (destProc==null) {
			return;
		}
        /*adding Object or his states*/
        if (newSent.getSourceObject().States.isEmpty()){
          this.addnewLink (sourceObj,destProc,newSent,newSourse,newDest);
        }
        else{
          Enumeration objStatesEnum = sourceObj.getStateInstances();
          while (objStatesEnum.hasMoreElements()){
            Iterator iterStates = newSent.getSourceObject().States.iterator();
            while (iterStates.hasNext()){
              IXStateInstance sourceState = (IXStateInstance)objStatesEnum.nextElement();
              if(sourceState.getIXEntry().getName().equals(((State)iterStates.next()).getStateName())){
                this.addnewLink (sourceState,destProc,newSent,newSourse,newDest);
    }}}}}}
    /**************************if the source is Process***********************************************/
    if (newSent.getSourceProcess()!=null){
      sourceProc = this.newProcessAdding(newSent.getSourceProcess(),neededOpdId,newSourse);
      if(sourceProc==null) {
		return;
	}
      while(iter.hasNext()){
        /*************-------------if the destination are Objects------------*******************/
        ObjectStruct curDestObj = (ObjectStruct)iter.next();
        destObj = this.newObjectAdding(curDestObj,neededOpdId,newDest);
        if(destObj==null) {
			return;
		}
        if (curDestObj.States.isEmpty()){
          int linkType = this.getType(newSent.getLinkType());
          /*2 link sentences*/
          Enumeration stateEnum = destObj.getStateInstances();
          while(stateEnum.hasMoreElements()){
            IXStateInstance stateInst = (IXStateInstance)stateEnum.nextElement();
            IXLinkInstance oldLink = this.iXSystem.getIXLinkBetweenInstances(stateInst ,sourceProc);
            if (oldLink!=null){
              if (((IXLinkEntry)oldLink.getIXEntry()).getLinkType()==OpcatConstants.INSTRUMENT_EVENT_LINK){
                if(newSent.getLinkType().equals("requires")) {
					return;
				}
                if(newSent.getLinkType().equals("consumes")){
                  newSent.setLinkType("cons_event");
                  this.iXSystem.delete(oldLink);
            }}}
            this.addnewLink (stateInst,sourceProc,newSent,newSourse,newDest);
            return;
          }
          IXLinkInstance oldLink = this.iXSystem.getIXLinkBetweenInstances(destObj,sourceProc);
          if (oldLink!=null){
            if (((IXLinkEntry)oldLink.getIXEntry()).getLinkType()==OpcatConstants.INSTRUMENT_EVENT_LINK){
              if(newSent.getLinkType().equals("requires")) {
				return;
			}
            if(newSent.getLinkType().equals("consumes")){
              newSent.setLinkType("cons_event");
              this.iXSystem.delete(oldLink);
          }}}         if ((newSent.getLinkType().equals("cons_event"))||
              (newSent.getLinkType().equals("occurs"))||(linkType==OpcatConstants.INSTRUMENT_LINK)||
              (newSent.getLinkType().equals("consumes"))) {
            this.addnewLink (destObj,sourceProc,newSent,newSourse,newDest);
          }
          else{
            this.addnewLink (sourceProc,destObj,newSent,newSourse,newDest);
        }}
        else{
          if (newSent.getLinkType().equals("changes")){
            this.changesLink(sourceProc,destObj,curDestObj,newSent,newSourse,newDest);
          }
          else
          {/* state are need to be connected*/
            Enumeration objStatesEnum = destObj.getStateInstances();
            while (objStatesEnum.hasMoreElements()){
              Iterator iterStates = curDestObj.States.iterator();
              while (iterStates.hasNext()){
                IXStateInstance destState = (IXStateInstance)objStatesEnum.nextElement();
                if(destState.getIXEntry().getName().equals(((State)iterStates.next()).getStateName())){
                  int linkType = this.getType(newSent.getLinkType());
                  IXLinkInstance oldLink = this.iXSystem.getIXLinkBetweenInstances(destState,sourceProc);
                  if (oldLink!=null){
                    int oldtype = ((IXLinkEntry)oldLink.getIXEntry()).getLinkType();
                    if (oldtype==OpcatConstants.INSTRUMENT_EVENT_LINK){

                      if(newSent.getLinkType().equals("requires")) {
						return;
					}
                      if(newSent.getLinkType().equals("consumes")) {
						newSent.setLinkType("cons_event");
					}
                  }}
                  if ((newSent.getLinkType().equals("occurs"))||(linkType==OpcatConstants.INSTRUMENT_LINK)||
                      (newSent.getLinkType().equals("consumes"))){
                    this.addnewLink (destState,sourceProc,newSent,newSourse,newDest);
                  }
                  else{
                    this.addnewLink (sourceProc,destState,newSent,newSourse,newDest);
      }}}}}}}
      iter = newSent.getDestProcesses();
      while(iter.hasNext()){
        ProcessStruct curDestProc = (ProcessStruct)iter.next();
        destProc = this.newProcessAdding(curDestProc,neededOpdId,newDest);
        if (destProc==null) {
			return;
		}
        this.addnewLink (sourceProc,destProc,newSent,newSourse,newDest);
    }}
    return;
  }

  private void changesLink(IXProcessInstance proc,IXObjectInstance objInst,ObjectStruct objStr,LinkStruct newSent,boolean newProc, boolean newObj){

    Iterator stateIter = objStr.States.iterator();
    while(stateIter.hasNext()){
      State stateStr = (State)stateIter.next();
      Enumeration stateEnum = objInst.getStateInstances();
      while (stateEnum.hasMoreElements()){
        IXStateInstance stateInst = (IXStateInstance)stateEnum.nextElement();
        if (stateStr.getStateName().equals(stateInst .getIXEntry().getName())){
          if (stateStr.getFrom()){
            this.newLinkUpdate(newSent, this.iXSystem.addLink(stateInst,proc,OpcatConstants.CONSUMPTION_LINK),"");
          }
          else{
            this.newLinkUpdate(newSent, this.iXSystem.addLink(proc,stateInst,OpcatConstants.CONSUMPTION_LINK),"");
          }
        }
      }
    }
  }
/**
 *This function checks the legality of the new Link
 * @param source the link source
 * @param dest the link destination
 * @param newSent the new link sentence
 */
  public void addnewLink (IXConnectionEdgeInstance source, IXConnectionEdgeInstance dest,LinkStruct newSent,boolean newSource, boolean newDest){
    IXCheckResult res = this.iXSystem.checkLinkAddition(source,dest,this.getType(newSent.getLinkType()));
    if (res.getResult()==IXCheckResult.RIGHT){
      this.newLinkUpdate(newSent, this.iXSystem.addLink(source,dest,this.getType(newSent.getLinkType())),"");
    }
    if (res.getResult()==IXCheckResult.WRONG) {
      if (this.iXSystem.checkDeletion(source).getResult()==IXCheckResult.RIGHT) {
		if (newSource){
          this.iXSystem.delete(source);
        }
	}
      if (this.iXSystem.checkDeletion(dest).getResult()==IXCheckResult.RIGHT) {
		if(newDest){
          this.iXSystem.delete(dest);
        }
	}
      JOptionPane.showMessageDialog(this.iXSystem.getMainFrame(),res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);

    }
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.iXSystem.getMainFrame(),res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
      if (conf==JOptionPane.OK_OPTION) {
		this.newLinkUpdate(newSent, this.iXSystem.addLink(source,dest,this.getType(newSent.getLinkType())),"");
	} else{
        if (this.iXSystem.checkDeletion(source).getResult()==IXCheckResult.RIGHT) {
			if (newSource){
			     this.iXSystem.delete(source);
			   }
		}
        if (this.iXSystem.checkDeletion(dest).getResult()==IXCheckResult.RIGHT) {
			if(newDest){
			    this.iXSystem.delete(dest);
			  }
		}
      }
    }
  }


  /**This function gets from user the OPD to which he wants to add the new Relation sentence
   * and adds it to the needed OPD
   * @param newSent the new sentence
   */
  public void newRelationSentence(RelationStruct newSent){
    IXObjectInstance sourceObj = null;
    IXProcessInstance sourceProc = null;
    IXObjectInstance destObj;
    IXProcessInstance destProc;
    long neededOpdId;
    boolean newSourse = false;
    boolean newDest = false;
    Iterator iter = newSent.getDestObjects();


    if (this.ifNewSentence(newSent)){
      neededOpdId = this.curOpdId;
    }
    else{
      /*Here we give the list of the OPDs to User and he choose the only one she wants */
      HashSet proc_screens = new HashSet();
      HashSet obj_screens = new HashSet();
      if (newSent.getSourceProcess()!=null){
        if (!this.ifNewThing(newSent.getSourceProcess().getProcessName())){
          Enumeration insts  = this.getThingEntry(newSent.getSourceProcess().getProcessName()).getInstances();
          while(insts.hasMoreElements()){
            long key = (((IXConnectionEdgeInstance)insts.nextElement()).getKey()).getOpdId();
            proc_screens.add((this.systStruct.getIXOpd(key)));
          }}}
      if (newSent.getSourceObject()!=null){
        if (!this.ifNewThing(newSent.getSourceObject().getObjectName())){
          Enumeration insts  = this.getThingEntry(newSent.getSourceObject().getObjectName()).getInstances();
          while(insts.hasMoreElements()){
            long key = (((IXObjectInstance)insts.nextElement()).getKey()).getOpdId();
            obj_screens.add((this.systStruct.getIXOpd(key)));
          }}  }
      while(iter.hasNext()){
        ObjectStruct curDestObj = (ObjectStruct)iter.next();
        if (!this.ifNewThing(curDestObj.getObjectName())){
          Enumeration insts  = this.getThingEntry(curDestObj.getObjectName()).getInstances();
          while(insts.hasMoreElements()){
            long key = (((IXConnectionEdgeInstance)insts.nextElement()).getKey()).getOpdId();
            obj_screens.add((this.systStruct.getIXOpd(key)));
          }}}
     iter = newSent.getDestProcesses();
      while(iter.hasNext()){
        ProcessStruct curDestProcess = (ProcessStruct)iter.next();
        if (!this.ifNewThing(curDestProcess.getProcessName())){
          Enumeration insts  = this.getThingEntry(curDestProcess.getProcessName()).getInstances();
          while(insts.hasMoreElements()){
            long key = (((IXProcessInstance)insts.nextElement()).getKey()).getOpdId();
            obj_screens.add((this.systStruct.getIXOpd(key)));
          }
        }
      }
      if (!proc_screens.isEmpty()){
        neededOpdId = this.getNumFromUser(proc_screens);
      }
      else {neededOpdId = this.getNumFromUser(obj_screens);}
      /*-------------------  end of OPD choose------------------------------------  */
    }
     iter = newSent.getDestObjects();
     if (newSent.getSourceObject()!=null){/*if the source is Object*/

         sourceObj = this.newObjectAdding(newSent.getSourceObject(),neededOpdId,newSourse);
         if (sourceObj==null){
           return;
         }
          while(iter.hasNext()){
            ObjectStruct curDestObj = (ObjectStruct)iter.next();
            destObj = this.newObjectAdding(curDestObj,neededOpdId,newDest);
            if (destObj==null){
              return;
            }
            this.addnewRelation (sourceObj,destObj,newSent,curDestObj.DestinationCardinality);
          }
          iter = newSent.getDestProcesses();
          while(iter.hasNext()){
            ProcessStruct curDestProc = (ProcessStruct)iter.next();
            destProc = this.newProcessAdding(curDestProc,neededOpdId,newDest);
            if (destProc==null) {
				return;
			}
            this.addnewRelation (sourceObj,destProc,newSent, curDestProc.DestinationCardinality);
          }
        }
        if (newSent.getSourceProcess()!=null){/*if the source is Process*/
           sourceProc = this.newProcessAdding(newSent.getSourceProcess(),neededOpdId,newSourse);
           if(sourceProc==null) {
			return;
		}
            while(iter.hasNext()){
              ObjectStruct curDestObj = (ObjectStruct)iter.next();
              destObj = this.newObjectAdding(curDestObj,neededOpdId, newDest);
              if(destObj==null) {
				return;
			}
              this.addnewRelation (sourceProc,destObj,newSent,curDestObj.DestinationCardinality);
            }
            iter = newSent.getDestProcesses();
            while(iter.hasNext()){
              ProcessStruct curDestProc = (ProcessStruct)iter.next();
              destProc = this.newProcessAdding(curDestProc,neededOpdId,newDest);
              if (destProc==null) {
				return;
			}
              this.addnewRelation (sourceProc,destProc,newSent,curDestProc.DestinationCardinality);
            }
        }
        return;
  }
  /**
   *This function checks the legality of the new Relation
   * @param source the relation source
   * @param dest the relation destination
   * @param newSent the new relation sentence
 */
  public void addnewRelation (IXConnectionEdgeInstance source, IXConnectionEdgeInstance dest,RelationStruct newSent, String card){
    IXCheckResult res = this.iXSystem.checkRelationAddition(source,dest,this.getType(newSent.getRelationType()));
    if (res.getResult()==IXCheckResult.RIGHT){
      this.newRelationUpdate(newSent, this.iXSystem.addRelation(source,dest,this.getType(newSent.getRelationType())),card);
    }
    if (res.getResult()==IXCheckResult.WRONG) {
      if (this.iXSystem.checkDeletion(source).getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(source);
	}
      if (this.iXSystem.checkDeletion(dest).getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(dest);
	}
      JOptionPane.showMessageDialog(this.iXSystem.getMainFrame(),res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);

    }
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.iXSystem.getMainFrame(),res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
      if (conf==JOptionPane.OK_OPTION) {
		this.newRelationUpdate(newSent, this.iXSystem.addRelation(source,dest,this.getType(newSent.getRelationType())),card);
	} else{
        if (this.iXSystem.checkDeletion(source).getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(source);
		}
        if (this.iXSystem.checkDeletion(dest).getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(dest);
		}
      }
    }
  }

  /**
   * This function gets from user the OPD to which  he wants to add the new sentence.
   * @param screens the possible OPD screens.
   * @return an OPD ID of the chosen screen.
   */
    public long getNumFromUser(HashSet screens)
     {
       if (screens.size()==1) {
		return ((IXOpd)screens.iterator().next()).getOpdId();
	}
       Iterator iter = screens.iterator();
       Vector possibleValues = new Vector();
       long needOpdId;
        while(iter.hasNext()){
            possibleValues.add(((IXOpd)iter.next()).getName());
        }

       Object selectedValue = JOptionPane.showInputDialog(this.oPLComboBox,
           " Some of the things in your sentence are already appeared in the project. \n Please choose the OPD in which you want to add your new sentence.", " ",
           JOptionPane.QUESTION_MESSAGE, null,
           possibleValues.toArray(), null/*possibleValues*/);

       if (selectedValue!=null){
       String opdName = selectedValue.toString();

         Enumeration locEnum = this.iXSystem.getIXSystemStructure().getOpds();
         while(locEnum.hasMoreElements()){
           IXOpd opd = (IXOpd)locEnum.nextElement();
           if(opdName==opd.getName()){
             needOpdId = opd.getOpdId();
             return needOpdId;
           }
         }
       }
       return 1111;
     }


  private IXEntry getThingEntry (String name){
    Enumeration locEnum = this.systStruct.getElements();
    IXEntry entry;
    while (locEnum.hasMoreElements()){
      entry = (IXEntry)locEnum.nextElement();
      String thingName = entry.getName();
      if(thingName.equals(name)){
        return entry;
      }
    }
   return null;
 }


  private void addThingToList(IXThingInstance thing,FreeSpaceList list)
  {
    int thing_w = thing.getWidth();
    int thing_h = thing.getHeight();
    Iterator iter = list.getSpace();
    while (iter.hasNext()){
      FreeArea area = (FreeArea)iter.next();
      int area_x = area.getX();
      int area_y = area.getY();
      int area_w = area.getWidth();
      int area_h = area.getHeight();

      if ((this.spareSpaceForNew + thing_w <= area_w)&&(this.spareSpaceForNew + thing_h<=area_h)){
        thing.setLocation(area_x + this.spareSpaceForNew,area_y + this.spareSpaceForNew);
        return;
      }
    }
    thing.setLocation(900,this.Y);
    this.Y+=100;
  }

/*This function returns true if the thing is completely new*/
  private boolean ifNewThing(String newThingName ){

    Enumeration locEnum = this.iXSystem.getIXSystemStructure().getElements();
    String existingEntry;
    while(locEnum.hasMoreElements()){
      existingEntry = ((IXEntry)locEnum.nextElement()).getName();
      if (newThingName.compareTo(existingEntry)==0){
        return false;
      }
    }
    return true;
  }


/*This function returns true if the sentence is completely new*/
  private boolean ifNewSentence(LinkStruct newSent){
    Iterator iter = newSent.getDestObjects();
    while (iter.hasNext()){
      if (!this.ifNewThing(((ObjectStruct)iter.next()).getObjectName())) {
		return false;
	}
    }
    iter = newSent.getDestProcesses();
    while (iter.hasNext()){
      if (!this.ifNewThing(((ProcessStruct)iter.next()).getProcessName())) {
		return false;
	}
    }
    return true;
  }

  /*This function returns true if the sentence is completely new*/
  private boolean ifNewSentence(RelationStruct newSent){
    ObjectStruct obj = newSent.getSourceObject();
    if (obj==null){
      ProcessStruct proc = newSent.getSourceProcess();
      if (!this.ifNewThing(proc.getProcessName())) {
		return false;
	}
    }
    else{
      if (!this.ifNewThing(obj.getObjectName())) {
		return false;
	}
    }
    Iterator iter = newSent.getDestObjects();
    while (iter.hasNext()){
      if (!this.ifNewThing(((ObjectStruct)iter.next()).getObjectName())) {
		return false;
	}
    }
    iter = newSent.getDestProcesses();
    while (iter.hasNext()){
      if (!this.ifNewThing(((ProcessStruct)iter.next()).getProcessName())) {
		return false;
	}
    }
    return true;
  }


/**
 * This function updates the attributes of the added Object
 * @param obj the new Object
 * @param inst the new Object instance
 */

  public void newObjectUpdate(ObjectStruct obj, IXObjectInstance inst){
    IXObjectEntry entry = (IXObjectEntry)inst.getIXEntry();
    entry.setEnvironmental(obj.getEnvironmental());
    entry.setPersistent(obj.getIsPersistent());
    entry.setPhysical(obj.getIsPhysical());
    entry.setKey(obj.getIsKey());
    if(obj.getInitialValue()!=null){
      entry.setInitialValue(obj.getInitialValue());
    }
    if(obj.getRole()!=null){
      entry.setRole(obj.getRole());
    }
    if(obj.getScope()!=null){
      if (obj.getScope().equals("piblic")) {
		entry.setScope(OpcatConstants.PUBLIC);
	}
      if (obj.getScope().equals("private")) {
		entry.setScope(OpcatConstants.PRIVATE);
	}
      if (obj.getScope().equals("protected")) {
		entry.setScope(OpcatConstants.PROTECTED);
	}
    }
    if(obj.getObjectType()!=null){
      entry.setType(obj.getObjectType());
    }
    entry.updateInstances();
  }

  /**
 * This function updates the attributes of the added State
 * @param state the new State
 * @param stateEntry the new State Entry
 */

  public void newStateUpdate(State state, IXStateEntry stateEntry)
  {

    stateEntry.setInitial(state.getIsInitial());
    stateEntry.setFinal(state.getIsFinal());
    if (state.getMaxTime()!=null){
      stateEntry.setMaxTime(state.getMaxTime());
    }
    if (state.getMinTime()!=null){
      stateEntry.setMinTime(state.getMinTime());
    }
  }

  /**
   * This function updates the attributes of the added Process
   * @param prc the new Process
   * @param inst the new Process instance
   */
  public void newProcessUpdate(ProcessStruct prc, IXProcessInstance inst){
    IXProcessEntry entry = (IXProcessEntry)inst.getIXEntry();
    entry.setEnvironmental(prc.getEnvironmental());
    entry.setPhysical(prc.getIsPhysical());

    if (prc.getMaxTimeActivation() != null) {
        entry.setMaxTimeActivation(prc.getMaxTimeActivation());
    }

    if (prc.getMinTimeActivation() != null){
        entry.setMinTimeActivation(prc.getMinTimeActivation());
    }
    if (prc.getScope() != null){
        entry.setScope(prc.getScope());
    }
    entry.updateInstances();
  }

  /**
   * This function updates the attributes of the added Link
   * @param link the new Link
   * @param inst the new Link instance
   * @param destCard the destination cardinality
   */
  public void newLinkUpdate(LinkStruct link, IXLinkInstance inst, String destCard){

    IXLinkEntry entry = (IXLinkEntry)inst.getIXEntry();
    if (!(link.getLinkCondition()==null)){
      entry.setCondition(link.getLinkCondition());
    }
    if (!(link.getMaxReaction()==null)){
      entry.setMaxReactionTime(link.getMaxReaction());
    }
    if (!(link.getMinReaction()==null)){
      entry.setMinReactionTime(link.getMinReaction());
    }
    if (!(link.getPath()==null)){
      entry.setPath(link.getPath());
    }
    if (!(link.getLinkCondition()==null)){
      entry.setCondition(link.getLinkCondition());
    }

  }

  /**
   * This function updates the attributes of the added Relation
   * @param rel the new Relation
   * @param inst the new Relation instance
   * @param destCard the destination cardinality
   */

  public void newRelationUpdate(RelationStruct rel, IXRelationInstance inst,String destCard){
    IXRelationEntry entry = (IXRelationEntry)inst.getIXEntry();
    if (!(rel.getBackwardMeaning()==null)){
      entry.setBackwardRelationMeaning(rel.getBackwardMeaning());
    }
    if (rel.getDestinationCardinality()!=null){
      entry.setDestinationCardinality(rel.getDestinationCardinality());
    }
    if (rel.getForwardMeaning()!=null){
      entry.setForwardRelationMeaning(rel.getForwardMeaning());
    }
    if (rel.getSourceCardinality()!=null){
      entry.setSourceCardinality(rel.getSourceCardinality());
    }
    if (destCard!=null){
      entry.setDestinationCardinality(destCard);
    }
    entry.updateInstances();
  }


   private  int getType(String type){
     int intType = 0;
     if (type.compareTo("exhibits")==0) {
		return OpcatConstants.EXHIBITION_RELATION;
	}
     if (type.compareTo("consists of")==0) {
		return OpcatConstants.AGGREGATION_RELATION;
	}
     if (type.compareTo("is a")==0) {
		return OpcatConstants.SPECIALIZATION_RELATION;
	}
     if (type.compareTo("is an instance of")==0) {
		return OpcatConstants.INSTANTINATION_RELATION;
	}
     if (type.compareTo("relates to")==0) {
		return OpcatConstants.UNI_DIRECTIONAL_RELATION;
	}
     if (type.compareTo("are related")==0) {
		return OpcatConstants.BI_DIRECTIONAL_RELATION;
	}
     if (type.compareTo("are equivalent")==0) {
		return OpcatConstants.BI_DIRECTIONAL_RELATION;
	}
     if (type.compareTo("handles")==0) {
		return OpcatConstants.AGENT_LINK;
	}
     if (type.compareTo("requires")==0) {
		return OpcatConstants.INSTRUMENT_LINK;
	}
     if (type.compareTo("consumes")==0) {
		return OpcatConstants.CONSUMPTION_LINK;
	}
     if (type.compareTo("affects")==0) {
		return OpcatConstants.EFFECT_LINK;
	}
     if (type.compareTo("yields")==0) {
		return OpcatConstants.RESULT_LINK;
	}
     if (type.compareTo("triggers")==0) {
		return OpcatConstants.INVOCATION_LINK;
	}
     if (type.compareTo("inst_event")==0) {
		return OpcatConstants.INSTRUMENT_EVENT_LINK;
	}
     if (type.compareTo("exception")==0) {
		return OpcatConstants.EXCEPTION_LINK;
	}
     if (type.compareTo("occurs")==0) {
		return OpcatConstants.CONDITION_LINK;
	}
     if (type.compareTo("cons_event")==0) {
		return OpcatConstants.CONSUMPTION_EVENT_LINK;
	}

     return intType;
   }

   /**
    * Return the IXLinkInstance of the needed link if its exists or null.
    * @param linkType the type of needed link.
    * @param source the link source.
    * @param dest the link destination.
    * @param opdId the OPD ID of the neede OPD.
    * @return IXLinkInstance.
    */

  public IXLinkInstance getLinkInstance (int linkType, String source, String dest,long opdId){
    Enumeration locEnum = this.systStruct.getLinksInOpd (opdId);
    IXLinkInstance linkInst;
    IXLinkEntry linkEntry;
    while (locEnum.hasMoreElements()){
      linkInst = (IXLinkInstance)locEnum.nextElement();
      linkEntry = ( IXLinkEntry)linkInst.getIXEntry();
      if (linkEntry.getLinkType()==linkType){
        long sourceId = this.getThingId(this.getThingInstance(source,opdId));
        long destId = this.getThingId(this.getThingInstance(dest,opdId));
        if ((linkEntry.getDestinationId()==destId) && (linkEntry.getSourceId()==sourceId)){
          return linkInst;
        }
      }
    }
    return null;
  }

  /**
   * Return the IXThingInstance of the needed thing if its exists or null.
   * @param name the name of the needed Thing.
   * @param opdId the OPD ID of the needed OPD.
   * @return IXThingInstance.
    */
  public IXThingInstance getThingInstance (String name, long opdId){
       Enumeration locEnum = this.systStruct.getThingsInOpd(opdId);
       IXThingInstance thing;
       IXEntry entry;
       while (locEnum.hasMoreElements()){
         thing = (IXThingInstance)locEnum.nextElement();
         entry = thing.getIXEntry();
         String thingName = entry.getName();
         if(thingName.equals(name)){
           return thing;
         }
       }
      return null;
   }

   private long getThingId (IXInstance thing){
          IXEntry entry = thing.getIXEntry();
          return entry.getId();
   }
  private IXSystem iXSystem;
  private long curOpdId;
  private IXSystemStructure systStruct;
  private int spareSpaceForNew = 50;
  private OPLComboBox oPLComboBox;
  private int Y = 60;
}

