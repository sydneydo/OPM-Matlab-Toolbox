package extensionTools.opl2.alg;

import java.util.Enumeration;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ISystemStructure;
import extensionTools.opl2.generated.ObjectFactory;

/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author OPCAT team
 * @version 1.0
 */

public final class OPLTreePerSystemBuilder
    extends OPLTreeBuilder {

  public OPLTreePerSystemBuilder(ISystemStructure myStructure_,
                                 String systemName_,ObjectFactory ob) {
    super(myStructure_, systemName_,ob);
    this.currOPD = -1;
    this.mainElem = null;
  }

  public boolean isMainElement(IEntry elem){
   return true;

  }

  public IEntry getMainElement(){
    return this.mainElem;
  }


  protected Enumeration getSystemElements() {
    Enumeration e;
    e = this.myStructure.getElements();
    return e;
  }

//  protected boolean testLink(ILinkEntry link) {
//    return true;
//  }

//  protected boolean testRelation(IRelationEntry rel, int rule) {
//    if (!OPLGeneral.isByDestination(rule))
//      return true;
//    int type = rel.getRelationType();
//    return ( (type == OpcatConstants.BI_DIRECTIONAL_RELATION &&
//              !rel.getBackwardRelationMeaning().equals("")
//              && !rel.getForwardRelationMeaning().equals("")) ||
//            (type == OpcatConstants.SPECIALIZATION_RELATION));
//  }


//  protected boolean testRelation(IRelationEntry link) {
//      return true;
//  }

  protected boolean testEntity(IEntry entity) {
     return true;
 }


//  protected boolean testState(IStateEntry entity) {
//      return true;
//  }



}
