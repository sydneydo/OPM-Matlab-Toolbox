package extensionTools.Documents.Doc;

/**
 * <P>Relations is a class that contains all the relation types in OPM
 * and information regarding the selection of them. The class is a
 * part of the DataDictionary.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */


public class Relations {

  public Relations() {
  }
  /**
   * Counts the number of fields that contains TRUE.
   */
private int Counter = 0;//counts the selected fields
private boolean AgPar;
private boolean FeChar;
private boolean GenSpec;
private boolean ClassInst;
private boolean UniDir;
private boolean BiDir;

/**
 * Is at least one of the fields in this part selected
 * @return TRUE if the part is selected
*/
public boolean isSelected (){
  if (this.Counter>0) {
	return true;
} else {
	return false;
}
}

//Set Functions
/**
 * Sets the value of AgPar field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setAgPar(boolean value){
  this.AgPar=value;
  if (this.AgPar) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of FeChar field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setFeChar(boolean value){
  this.FeChar=value;
  if (this.FeChar) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of GenSpec field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setGenSpec(boolean value){
  this.GenSpec=value;
  if (this.GenSpec) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of ClassInst field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setClassInst(boolean value){
  this.ClassInst=value;
  if (this.ClassInst) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of UniDir field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setUniDir(boolean value){
  this.UniDir=value;
  if (this.UniDir) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of BiDir field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setBiDir(boolean value){
  this.BiDir=value;
  if (this.BiDir) {
	this.Counter++;
}
  return;
}

//Get Functions
/**
 * Returns the value of AgPar field
 * @return value of AgPar field
 */
public boolean getAgPar(){
  return this.AgPar;
}
/**
 * Returns the value of FeChar field
 * @return value of FeChar field
 */
public boolean getFeChar(){
  return this.FeChar;
}
/**
 * Returns the value of GenSpec field
 * @return value of GenSpec field
 */
public boolean getGenSpec(){
  return this.GenSpec;
}
/**
 * Returns the value of ClassInst field
 * @return value of ClassInst field
 */
public boolean getClassInst(){
  return this.ClassInst;
}
/**
 * Returns the value of UniDir field
 * @return value of UniDir field
 */
public boolean getUniDir(){
  return this.UniDir;
}
/**
 * Returns the value of BiDir field
 * @return value of BiDir field
 */
public boolean getBiDir(){
  return this.BiDir;
}

/**
 * Puts all the values in the Relation from the array.
 * The order of fields in the array: AgPar, ClassInst, FeChar, GenSpec, BiDir,
 * UniDir.
 * @param array of booleans - the values of the fields
 */
public void RelInit(boolean [] array){
  this.setAgPar(array[0]);
  this.setClassInst(array[1]);
  this.setFeChar(array[2]);
  this.setGenSpec(array[3]);
  this.setBiDir(array[4]);
  this.setUniDir(array[5]);
}

}