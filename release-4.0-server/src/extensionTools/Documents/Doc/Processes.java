package extensionTools.Documents.Doc;

/**
 * <P>Processes is a class that contains all the fields describing a process in OPM
 * and information regarding the selection of those fields. The class is a
 * part of the DataDictionary.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */


public class Processes {

  public Processes() {
  }

  /**
   * Counts the number of fields that contains TRUE.
   */
private int Counter = 0;//counts the selected fields
private boolean Desc;
private boolean Essence;
private boolean Origin;
private boolean Scope;
private boolean ActTime;
private boolean Body;

/**
 * Is at least one of the fields in this part selected
 * @return TRUE if the part is selected
*/
 boolean isSelected (){
  if (this.Counter>0) {
	return true;
} else {
	return false;
}
}

//Set Functions
/**
 * Sets the value of Desc field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setDesc(boolean value){
  this.Desc=value;
  if (this.Desc) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Essence field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setEssence(boolean value){
  this.Essence=value;
  if (this.Essence) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Origin field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOrigin(boolean value){
  this.Origin=value;
  if (this.Origin) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Scope field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setScope(boolean value){
  this.Scope=value;
  if (this.Scope) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of ActTime field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setActTime(boolean value){
  this.ActTime=value;
  if (this.ActTime) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Body field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setBody(boolean value){
  this.Body=value;
  if (this.Body) {
	this.Counter++;
}
  return;
}

//Get Functions
/**
 * Returns the value of Desc field
 * @return value of Desc field
 */
public boolean getDesc(){
  return this.Desc;
}
/**
 * Returns the value of Essence field
 * @return value of Essence field
 */
public boolean getEssence(){
  return this.Essence;
}
/**
 * Returns the value of Origin field
 * @return value of Origin field
 */
public boolean getOrigin(){
  return this.Origin;
}
/**
 * Returns the value of Scope field
 * @return value of Scope field
 */
public boolean getScope(){
  return this.Scope;
}
/**
 * Returns the value of ActTime field
 * @return value of ActTime field
 */
public boolean getActTime(){
  return this.ActTime;
}
/**
 * Returns the value of Body field
 * @return value of Body field
 */
public boolean getBody(){
  return this.Body;
}

/**
 * Puts all the values in the process from the array.
 * The order of fields in the array: Desc, Essence, Origin, Scope, Body, ActTime.
 * @param array of booleans - the values of the fields
 */

public void ProcInit(boolean [] array){
  this.setDesc(array[0]);
  this.setEssence(array[1]);
  this.setOrigin(array[2]);
  this.setScope(array[3]);
  this.setBody(array[4]);
  this.setActTime(array[5]);
 }


}