package extensionTools.opl2.alg;
import java.util.Comparator;
import java.util.Enumeration;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IInstance;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationEntry;
//import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IThingInstance;
/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class GeneralComparator implements Comparator {

  public GeneralComparator(OPLTreeBuilder builder_) {
    //myStructure = builder_.myStructure;
    this.builder = builder_;
  }

  public int compare(Object o1, Object o2){
    IEntry ent1 = (IEntry)o1;
    IEntry ent2 = (IEntry)o2;
    try{
      return this.compareSystem(ent1, ent2);
    }catch (Exception e){return 0;}
  }

  //Main Comparing Algorithm
  public int compareSystem(IEntry ent1, IEntry ent2)throws Exception{
    int first = 0;
    int second = 0;
    int fatherType1 = this.getFatherType(ent1,ent2);
    int fatherType2 = this.getFatherType(ent2,ent1);
    if(fatherType1>0) {
		first = -1;
	}
    if(fatherType2>0) {
		second = 1;
	}
    if(((first==0) && (second!=0))|| ((first!=0) && (second==0))){
      //System.err.println("ret val is" +(first+second));
      return (first + second);
    }
    else if(fatherType1!=fatherType2){
      //System.err.println("ret val is" +((fatherType1>fatherType2)?(-1):(1)));
      return (fatherType1>fatherType2)?(-1):(1);
    }
   /* if((ent1 instanceof IObjectEntry)&&(ent2 instanceof IProcessEntry)){
      System.err.println("ret val is -1");
      return -1;
    }
    if((ent1 instanceof IProcessEntry)&&(ent2 instanceof IObjectEntry)){
      System.err.println("ret val is 1");
      return 1;
    }*/
    //int ret = super.compare(ent1,ent2);
    //System.err.println("ret val is "+ret);
    //return ret;
    return 0;
  }

  public int getFatherType(IEntry ent1, IEntry ent2)throws Exception{
  //IObjectEntry tmpObj;
  //IProcessEntry tmpProc;
  IRelationEntry it;
  Enumeration relations = null;
  int retVal = 0,tmpVal=0;
  //ZOOMING is highest relation!!!

  IEntry zoomFather = this.builder.getZoomingFather(ent2);
  if(zoomFather!=null) {
	if(this.hasZoomingFather(ent1,ent2)) {
		return 1000;
	}
}
  if(ent1 instanceof IObjectEntry){
    //tmpObj = (IObjectEntry)ent1;
    relations = ((IObjectEntry)ent1).getRelationBySource();
  }
  else if(ent1 instanceof IProcessEntry){
    //tmpProc = (IProcessEntry)ent1;
    relations = ((IProcessEntry)ent1).getRelationBySource();
  }
  if(relations!=null) {
	while(relations.hasMoreElements()){
	    it = (IRelationEntry)relations.nextElement();
	    if(it.getDestinationId()==ent2.getId()){
	      tmpVal = it.getRelationType();
	      if((tmpVal == OpcatConstants.AGGREGATION_RELATION)||
	         (tmpVal == OpcatConstants.EXHIBITION_RELATION)||
	         (tmpVal == OpcatConstants.INSTANTINATION_RELATION)||
	         (tmpVal == OpcatConstants.SPECIALIZATION_RELATION)){
	        if(retVal<tmpVal) {
				retVal = it.getRelationType();
			}
	      }
	    }
	  }
}
  //System.err.println(" Comparing father: "+ent1.getName()+" for obj "+ ent2.getName()+" value is "+retVal);
  return retVal;
  }

  protected boolean hasZoomingFather(IEntry father, IEntry son) throws Exception{
    IThingInstance thing;
    IInstance inst;
    Enumeration e = son.getInstances();
    while(e.hasMoreElements()){
      inst = (IInstance)e.nextElement();
      if(inst instanceof IThingInstance){
        thing = ((IThingInstance)inst).getParentIThingInstance();
        if((thing!=null)&&(thing.getIEntry().equals(father))) {
			return true;
		}
      }
  }
  return false;
  }

  public boolean equals(Object obj) {
    //System.err.println(" Comapring if father: "+((IEntry)this.getName()+" for obj "+ ent2.getName());
    if(this.compare(this,obj)==0) {
		return true;
	}
    return false;
  }

 //private ISystemStructure myStructure;
 private OPLTreeBuilder builder;

}