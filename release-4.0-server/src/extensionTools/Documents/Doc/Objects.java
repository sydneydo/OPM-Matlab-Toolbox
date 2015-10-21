package extensionTools.Documents.Doc;

/**
 * <P>Objects is a class that contains all the fields describing an object in OPM
 * and information regarding the selection of those fields.
 * The class is a part of the DataDictionary.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */


public class Objects {

  public Objects() {
  }
  /**
   * Counts the number of fields that contains TRUE.
   */
private int Counter = 0;
private boolean Type;
private boolean Desc;
private boolean InValue;
private boolean Essence;
private boolean Origin;
private boolean Scope;
private boolean Index;
private boolean States;
private boolean StateDesc;
private boolean StateInitial;
private boolean StateTime;

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
 * Sets the value of Type field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setType(boolean value){
  this.Type=value;
  if (this.Type) {
	this.Counter++;
}
  return;
}
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
 * Sets the value of InValue field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setInValue(boolean value){
  this.InValue=value;
  if (this.InValue) {
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
 * Sets the value of Index field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setIndex(boolean value){
  this.Index=value;
  if (this.Index) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of States field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setStates(boolean value){
  this.States=value;
  if (this.States) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of StateDesc field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setStateDesc(boolean value){
  this.StateDesc=value;
  if (this.StateDesc) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of StateInitial field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setStateInitial(boolean value){
  this.StateInitial=value;
  if (this.StateInitial) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of StateTime field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setStateTime(boolean value){
  this.StateTime=value;
  if (this.StateTime) {
	this.Counter++;
}
  return;
}
//Get Functions
/**
 * Returns the value of Type field
 * @return value of Type field
 */
public boolean getType(){
  return this.Type;
}
/**
 * Returns the value of Desc field
 * @return value of Desc field
 */
public boolean getDesc(){
  return this.Desc;
}
/**
 * Returns the value of InValue field
 * @return value of InValue field
 */
public boolean getInValue(){
  return this.InValue;
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
 * Returns the value of Index field
 * @return value of Index field
 */
public boolean getIndex(){
  return this.Index;
}
/**
 * Returns the value of States field
 * @return value of States field
 */
public boolean getStates(){
  return this.States;
}
/**
 * Returns the value of StateDesc field
 * @return value of StateDesc field
 */
public boolean getStateDesc(){
  return this.StateDesc;
}
/**
 * Returns the value of StateInitial field
 * @return value of StateInitial field
 */
public boolean getStateInitial(){
  return this.StateInitial;
}
/**
 * Returns the value of StateTime field
 * @return value of StateTime field
 */
public boolean getStateTime(){
  return this.StateTime;
}

/**
 * Puts all the values in the object from the array
 * The order of fields in the array: Type, Desc, InValue, Essence, Index, Scope,
 * Origin, States, StateDesc, StateInitial, StateTime
 * @param array of booleans - the values of the fields
 */
 public void ObjInit(boolean [] array){
  this.setType(array[0]);
  this.setDesc(array[1]);
  this.setInValue(array[2]);
  this.setEssence(array[3]);
  this.setIndex(array[4]);
  this.setScope(array[5]);
  this.setOrigin(array[6]);
  this.setStates(array[7]);
  this.setStateDesc(array[8]);
  this.setStateInitial(array[9]);
  this.setStateTime(array[10]);
}
}