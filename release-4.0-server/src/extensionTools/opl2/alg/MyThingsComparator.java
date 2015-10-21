package extensionTools.opl2.alg;

import java.util.Iterator;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IThingInstance;

/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author OPCAT team
 * @version 1.0
 */

public class MyThingsComparator extends MyComparator{

  public MyThingsComparator(boolean isObjInZooming_,OPLTreeBuilder builder_) {
    this.isObjInZooming = isObjInZooming_;
    this.builder = builder_;
  }


  public int compare(Object o1, Object o2) {
    IEntry ent1 = (IEntry)o1;
    IEntry ent2 = (IEntry)o2;

    int sum=0;
    if(this.isFather(ent1,ent2)) {
		sum= -1;
	}
    if(this.isFather(ent2,ent1)) {
		sum+=1;
	}
    if(sum!=0) {
		return sum;
	}
    if((ent1 instanceof IObjectEntry) && (ent2 instanceof IProcessEntry)){
      if(this.isObjInZooming) {
		return -1;
	} else {
		return 1;
	}
    }
    if((ent1 instanceof IProcessEntry) && (ent2 instanceof IProcessEntry)){
      IThingInstance i1 = (IThingInstance)this.builder.getIInstanceByOPD(ent1);
      IThingInstance i2 = (IThingInstance)this.builder.getIInstanceByOPD(ent2);
      if(i1.getY()<i2.getY()) {
		return -1;
	} else if(i1.getY()>i2.getY()) {
			return 1;
		}
    }

    if((ent2 instanceof IObjectEntry) && (ent1 instanceof IProcessEntry)){
      if(this.isObjInZooming) {
		return 1;
	} else {
		return -1;
	}
    }
      long diff = ent1.getId()-ent2.getId();
      return (diff>0?-1:(diff<0?1:0));

  }

  public boolean equals(Object obj) {
    if(this.compare(this,obj)==0) {
		return true;
	}
    return false;
  }

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

  private OPLTreeBuilder builder;
  private boolean isObjInZooming;
}