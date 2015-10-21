package extensionTools.opl2;
import exportedAPI.opcatAPI.ISystem;
import gui.opdProject.OplColorScheme;

public class OPLBuilder{

private OPLgenerator oplGenerator;

public OPLBuilder(ISystem system, OplColorScheme colorScheme){
  this.oplGenerator = new OPLgenerator(system);
}

public StringBuffer getOplXmlScript(){
  return this.oplGenerator.extractOPLScript();
}

public StringBuffer getOplHTML(){
  return this.oplGenerator.getOplHTML();
}

public StringBuffer getOplText(){
  return this.oplGenerator.getOplText();
}

public StringBuffer getOplText(long opd){
  return this.oplGenerator.getOplText(opd);
}


public StringBuffer getOplHTML(long opd){
  return this.oplGenerator.getOplHTML(opd);
}

//public BufferedString getOplHTMLScript(long opd){
//}

//public BufferedString getOplHTMLScript(){
//  return oplGenerator.getOplHTMLScript();
//}

}