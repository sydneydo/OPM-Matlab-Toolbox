package extensionTools.opl2;

import java.util.ListIterator;

import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import extensionTools.opl2.generated.OPLscript;
import extensionTools.opl2.generated.StateClause;
import extensionTools.opl2.generated.ThingSentenceSet;


/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author OPCAT team
 * @version 1.0
 */

public class PlainTextPrinter extends PrettyPrinter{
  public PlainTextPrinter(ISystemStructure elems, ISystem opcatSystem_) {
    super(elems, opcatSystem_);
    this.startHTML = "";
    this.endHTML = "";
    this.setEndLine();
  }

  public void visit(OPLscript e){
   java.util.List things = e.getThingSentenceSet();
   if(things == null) {
	return;
}
   //bf.append("<HTML><TITLE>");
   //printAttr(OplColorScheme.OBJECT_STYLE,e.getSystemName());
   //bf.append(" - OPL script.</TITLE><BODY>");
   for(ListIterator itr = things.listIterator();itr.hasNext();) {
	this.visit((ThingSentenceSet)itr.next());
}
   //bf.append(endHTML);
   e.getThingSentenceSet();
 }

 protected void setEndLine(){
    this.endLine = ".\n";

  }

  public void printAttr(int attributeName, String value){
   this.bf.append(value);
 }

 public void printParagraph(java.util.List things, boolean isPerState){
 if(things.isEmpty()) {
	return;
}
   this.margin.append("\t");
   if(!isPerState){
     for(java.util.ListIterator itr = things.listIterator();itr.hasNext();){
       this.visit((ThingSentenceSet)itr.next());
     }
   }else{
     for(java.util.ListIterator itr = things.listIterator();itr.hasNext();){
       this.visit((StateClause)itr.next());
     }
   }
   this.margin.delete(this.margin.length()-1,this.margin.length());
 }





}