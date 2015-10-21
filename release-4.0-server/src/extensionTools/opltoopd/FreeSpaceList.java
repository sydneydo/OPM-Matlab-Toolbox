package extensionTools.opltoopd;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXSystem;
import exportedAPI.opcatAPIx.IXSystemStructure;

/**
 * Represents a Free Space List, consists of
 * free areas of this screen.
 */
public class FreeSpaceList {
  /**
   * All free areas in this screen
   */
  private HashSet FreeSpace = new HashSet();

  /**
   * Creates  new Free Area List for given OPD
   * @param system the running IXSystem
   * @param aOpdId an OPD ID for needed OPD
   */
  public FreeSpaceList(IXSystem system, long aOpdId) {
    this.iXSystem = system;
    this.opdId = aOpdId;
    this.systStruct = this.iXSystem.getIXSystemStructure();

    this.instanceInOpdEnum = this.systStruct.getThingsInOpd(this.opdId);
    java.awt.Dimension dim = this.iXSystem.getUserAreaSize();
    FreeArea newArea = new FreeArea(0,0,(int)dim.getWidth(),(int)dim.getHeight());/* creating the first free area - all screen*/
    this.FreeSpace.add(newArea);
  }

  /**
   * return an iterator of the Free Space List
   * @return an iterator of the Free Space List
   */
  public Iterator getSpace(){
    return this.FreeSpace.iterator();
  }

/**
 * This function builds the Free Space List
 * @param spareSpaceForNew the size of needed spare space (beetween Free Areas)
 */
  public void updateList(int spareSpaceForNew){
    while(this.instanceInOpdEnum.hasMoreElements()){
      IXConnectionEdgeInstance inst = (IXConnectionEdgeInstance)this.instanceInOpdEnum.nextElement();
      FreeArea instance = new  FreeArea(inst.getX(),inst.getY(),inst.getWidth(),inst.getHeight());
      this.occupyAppropriateSpace (instance);
    }
    FreeArea areaI,areaJ;
    Iterator freeSpaceIteratorI = this.FreeSpace.iterator();
    freeSpaceIteratorI = this.FreeSpace.iterator();
    Iterator freeSpaceIteratorJ = this.FreeSpace.iterator();
    while(freeSpaceIteratorI.hasNext()){
      areaI = (FreeArea)freeSpaceIteratorI.next();
      if (areaI.getWidth()>=96 +spareSpaceForNew){
        while(freeSpaceIteratorJ.hasNext()){
          areaJ = (FreeArea)freeSpaceIteratorJ.next();
          if (areaJ.getWidth()>=96 +spareSpaceForNew){
            if ((areaJ.getHeight()>=(56 + spareSpaceForNew))&&
                (areaJ.getWidth()>=(96 + spareSpaceForNew))){
              return;
            }
            if((areaI.getY()+areaI.getHeight())==areaJ.getY()){
            int x_max;
            if (areaI.getX()>areaJ.getX()){ x_max = areaI.getX();}
            else { x_max = areaJ.getX(); }
            int w_min;
            if ((areaI.getX()+areaI.getWidth()) <(areaJ.getX() + areaJ.getWidth())){
              w_min = (areaI.getX()+areaI.getWidth());
            } else {
              w_min = (areaJ.getX() + areaJ.getWidth());
            }
            int w_needed = w_min - x_max;
            if (w_needed >= (96 + spareSpaceForNew)){
              FreeArea area_new = new FreeArea(x_max,areaI.getY(),w_needed ,(areaI.getHeight()+areaJ.getHeight()));
              this.FreeSpace.add(area_new);
              this.FreeSpace.remove(areaI);
              this.FreeSpace.remove(areaJ);
              if ((areaI.getHeight()+areaJ.getHeight())>= (56 + spareSpaceForNew)){
                return;
              }
            else {
              freeSpaceIteratorI = this.FreeSpace.iterator();
              freeSpaceIteratorJ = this.FreeSpace.iterator();
              break;
            }
            }
            }
          }
        }
      }
    }
  }
/**
 * This function removes given Free Area from Free Space List.
 * @param instance the Free Area block
 */

  public void occupyAppropriateSpace (FreeArea instance){
    FreeArea area;
    Iterator freeSpaceIterator = this.FreeSpace.iterator();
    /* this loop searches for free area which can fully contane Instance */
    while(freeSpaceIterator.hasNext()){
      area = (FreeArea)freeSpaceIterator.next();
      if ((instance.getX()>=area.getX())&&(instance.getX()< area.getX() + area.getWidth())&&
          (instance.getY()>=area.getY())&&(instance.getY()< area.getY() + area.getHeight())){
        if ((instance.getX()+instance.getWidth()<=area.getX() + area.getWidth())&&
            (instance.getY()+instance.getHeight()<=area.getY() + area.getHeight())){
          this.reduceFreeSpace(area,instance.getX(),instance.getY(),instance.getWidth(),instance.getHeight(),0);
          this.FreeSpace.remove(area);
          return;
        }
        else{
          this.divideInstanceArea(area,instance);
          return;
        }
      }
    }
  }

  /**
   * This function reduces place, occupied by new instance from given Free Area.
   * @param area the old Free Area
   * @param xInst the X coordinate of the new area
   * @param yInst the Y coordinate of the new area
   * @param widthInst the width of the new area
   * @param heightInst the height of the new area
   * @param spareSpace the size of space between near areas
   */

  public void reduceFreeSpace(FreeArea area, int xInst, int yInst, int widthInst,int heightInst, int spareSpace){
    int area_x = area.getX();
    int area_y = area.getY();
    int area_w = area.getWidth();
    int area_h = area.getHeight();

    if ((yInst-spareSpace)>area_y){
      FreeArea newArea1 = new FreeArea(area_x,area_y,area_w,yInst-spareSpace-area_y);
      this.FreeSpace.add(newArea1);
    }

    if ((xInst-spareSpace)>area_x){
       FreeArea newArea2 = new FreeArea(area_x,yInst-spareSpace,xInst-spareSpace-area_x,heightInst);
       this.FreeSpace.add(newArea2);
    }

    if((xInst+widthInst)<(area_x + area_w)){
      FreeArea newArea3 = new FreeArea(xInst+widthInst,yInst-spareSpace,area_x+area_w-xInst-widthInst,
                                       heightInst+spareSpace);
      this.FreeSpace.add(newArea3);
    }
    if((heightInst+yInst)<(area_y+area_h)){/*newArea4_x = area_x ?*/
      FreeArea newArea4 = new FreeArea(area_x,yInst+heightInst,area_w,area_y+area_h-yInst-heightInst);
      this.FreeSpace.add(newArea4);
    }

  }

  /**
   * If there is no place for the new Free Area in one old Area
   * divide the new one on smaller parts.
   * @param area the old Free Area
   * @param instance the new Free Area
   */

  private void divideInstanceArea(FreeArea area,FreeArea instance){

    int inst_x = instance.getX();
    int inst_y = instance.getY();
    int inst_w = instance.getWidth();
    int inst_h = instance.getHeight();
    int area_y = area.getY();
    int area_h = area.getHeight();

    this.reduceFreeSpace(area,inst_x,inst_y,inst_w,
                    area_y + area_h - inst_y,0);
    this.FreeSpace.remove(area);
    /*create new updated instance for place on opd*/
    FreeArea partToPlace =
        new FreeArea(inst_x,area_y+area_h,inst_w,inst_h-(area_y+area_h-inst_y));
    this.occupyAppropriateSpace(partToPlace);

  }

  private IXSystem iXSystem;
  private long opdId;
  private IXSystemStructure systStruct;
  private Enumeration instanceInOpdEnum;
}

/*This is the control vertion!!!!!!!!!!!!!!!!!
public void occupyAppropriateSpace (FreeArea instance){
    FreeArea area;
    Iterator freeSpaceIterator = FreeSpace.iterator();

    while(freeSpaceIterator.hasNext()){
      area = (FreeArea)freeSpaceIterator.next();
      if ((instance.getX()>=area.getX())&&(instance.getX()<= area.getX() + area.getWidth())&&
          (instance.getY()>=area.getY())&&(instance.getY()<= area.getY() + area.getHeight())){
        if ((instance.getWidth()<=area.getWidth())&&(instance.getHeight()<=area.getHeight())){
          reduceFreeSpace(area,instance.getX(),instance.getY(),instance.getWidth(),instance.getHeight(), 0);
          FreeSpace.remove((Object)area);
          return;
        }
        else{
          divideInstanceArea(area,instance);
          FreeSpace.remove((Object)area);
        }
      }
    }
  }

*/
