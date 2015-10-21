package extensionTools.uml.umlDiagrams;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;

/**
 * class Deployment_d creates the Deployment Diagram Part in XML file
 */
public class Deployment_d {

  public Deployment_d() {}
   ISystem mySys;

/**
 *This function creates a Deployment Diagram in XML code
 *@param ISystem sys
 *@param FileOutputStream file
 *@param Vector deplPrVec - responsible for outside printing of "nodes" in XML file
 *@param Vector deplRelVec - responsible for outside printing of in XML file
 */
    public void DeploymentDiagramCreate(ISystem sys, XmiWriter file,Vector deplPrVec,Vector deplRelVec) throws IOException{
    
      String temp;
      ISystemStructure MyStructure;
      Enumeration e,relS,relD;
      IEntry abstractEntry;
      IObjectEntry obj,dest,src;
      Vector objVec=new Vector(4,2);
      IRelationEntry rel;

      this.mySys=sys;

      MyStructure = this.mySys.getISystemStructure();
      e = MyStructure.getElements();
      while (e.hasMoreElements()) {
        abstractEntry = (IEntry)e.nextElement();
        if(abstractEntry instanceof IObjectEntry){  //if object
          obj= (IObjectEntry)abstractEntry;
          if ((obj.isPhysical()) &&(!obj.isEnvironmental())){//not physical and not Environmental
            objVec.addElement(obj);
            temp="<UML:Node xmi.id=\"G."+obj.getId()+"\" name=\""+obj.getName()+"\" visibility=\"public\" isSpecification=\"false\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\">";
            file.write(temp.getBytes());
            deplPrVec.addElement(obj); //outside printing
            temp="<UML:Namespace.ownedElement>";
            file.write(temp.getBytes());
            relS=obj.getRelationBySource();//relations going out from the object
            while(relS.hasMoreElements()){
              rel=(IRelationEntry)relS.nextElement();//relation
              deplRelVec.addElement(rel);//outside printing of relations
              abstractEntry=MyStructure.getIEntry(rel.getDestinationId());//destination of the relation
              if(abstractEntry instanceof IObjectEntry){//if object
                dest=(IObjectEntry)abstractEntry;
                if((dest.isPhysical())&&(!dest.isEnvironmental())){
                  temp="<UML:Comment xmi.id=\"XX."+rel.getId()+".1\" name=\"Connected to processor: "+dest.getName()+"\" visibility=\"public\" isSpecification=\"false\" /> ";
                  file.write(temp.getBytes());
                }
              }
            }
            relD=obj.getRelationByDestination();//relation by destination
            while(relD.hasMoreElements()){
              rel=(IRelationEntry)relD.nextElement();
              abstractEntry=MyStructure.getIEntry(rel.getSourceId());
              if(abstractEntry instanceof IObjectEntry){
                src=(IObjectEntry)abstractEntry;
                if((src.isPhysical())&&(!src.isEnvironmental())){
                  temp="<UML:Comment xmi.id=\"XX."+rel.getId()+".2\" name=\"Connected to processor: "+src.getName()+"\" visibility=\"public\" isSpecification=\"false\" /> ";
                  file.write(temp.getBytes());
                }
              }
            }
            temp="</UML:Namespace.ownedElement>";
            file.write(temp.getBytes());
            temp="</UML:Node>";
            file.write(temp.getBytes());

          }
        }
      }//end of while

      temp="<UML:Stereotype xmi.id=\"S.0000.66\" name=\"Processor\" visibility=\"public\" isSpecification=\"false\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" icon=\"\" baseClass=\"Node\" extendedElement=\" ";
      file.write(temp.getBytes());
      e=objVec.elements();
      while(e.hasMoreElements()){
        obj=(IObjectEntry)e.nextElement();
        temp="G."+obj.getId()+"  ";
        file.write(temp.getBytes());
      }
      temp="\" /> ";//closing the list
      file.write(temp.getBytes());

    

  }//end of func

}