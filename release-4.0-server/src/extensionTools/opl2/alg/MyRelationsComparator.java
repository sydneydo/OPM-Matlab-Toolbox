package extensionTools.opl2.alg;

import java.util.Iterator;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.ISystemStructure;
/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author OPCAT team
 * @version 1.0
 */

public class MyRelationsComparator extends MyComparator {

  public MyRelationsComparator(OPLTreeBuilder builder_, int rule_,boolean ByProc_) {
    this.myStructure = builder_.myStructure;
    this.builder = builder_;
    this.rule = rule_;
    this.ByProc = ByProc_;
  }

  private IEntry initEntry(IRelationEntry rel) {
    if(!OPLGeneral.isByDestination(this.rule)){
      return this.myStructure.getIEntry(rel.getDestinationId());
    }
    return this.myStructure.getIEntry(rel.getSourceId());
  }

 public int compare(Object o1, Object o2) {
 int result1 = 0;
 int result2 = 0;
 IRelationEntry rel1 = (IRelationEntry)o1;
 IRelationEntry rel2 = (IRelationEntry)o2;
 IEntry ent1=this.initEntry(rel1);
 IEntry ent2=this.initEntry(rel2);
 if(this.isFather(ent1, ent2)){
   result1 = 1;
 }
 if(this.isFather(ent2, ent1)){
   result2 = -1;
 }
 if(result1 == (-1*result2)){
   if((ent1 instanceof IObjectEntry)&&(ent2 instanceof IProcessEntry)){
     //System.err.println(ent1.getName()+"stronger then"+ent2.getName());
     return (this.ByProc?1:-1);
   }
   else if((ent1 instanceof IProcessEntry)&&(ent2 instanceof IObjectEntry)){
     //System.err.println(ent2.getName()+"stronger then"+ent1.getName());
     return (this.ByProc?-1:1);
   }
 }
 result1+=result2;
 //????????????????????????????
 if(result1!=0) {
	return result1;
}
 if((rel1.getRelationType()==rel2.getRelationType()) &&
    ((rel1.getRelationType()==OpcatConstants.BI_DIRECTIONAL_RELATION) ||
     (rel1.getRelationType()==OpcatConstants.UNI_DIRECTIONAL_RELATION))){
   return ent1.getName().compareTo(ent2.getName());
 }

 return super.compare(ent1,ent2);
  }

//  public int compare(Object o1, Object o2) {
//   int result1 = 0;
//   int result2 = 0;
//   IRelationEntry rel1 = (IRelationEntry)o1;
//   IRelationEntry rel2 = (IRelationEntry)o2;
//   IEntry ent1=initEntry(rel1);
//   IEntry ent2=initEntry(rel2);
//   if(isFather(ent1, ent2)){
//     result1 = 1;
//   }
//   if(isFather(ent2, ent1)){
//     result2 = -1;
//   }
//   if(result1 == (-1*result2)){
//     if((ent1 instanceof IObjectEntry)&&(ent2 instanceof IProcessEntry)){
//       //System.err.println(ent1.getName()+"stronger then"+ent2.getName());
//       return 1;
//     }
//     else if((ent1 instanceof IProcessEntry)&&(ent2 instanceof IObjectEntry)){
//       //System.err.println(ent2.getName()+"stronger then"+ent1.getName());
//       return -1;
//     }
//   }
//   result1+=result2;
//   //????????????????????????????
//   if(result1!=0)return -1*result1;
//   //if(result1!=0)return (-1*result1);
//   return super.compare(ent1,ent2);
//  }

  //Is ent1 father of ent2
  private boolean isFather(IEntry ent1, IEntry ent2){
   java.util.List list = this.builder.getFathersList(ent2);
   for(Iterator it = list.iterator();it.hasNext();){
     IEntry ent = (IEntry)it.next();
     if(ent1 == ent) {
		return true;
	}
   }
   return false;
  }

  private ISystemStructure myStructure;
  private OPLTreeBuilder builder;
  private int rule;
  private boolean ByProc;
}