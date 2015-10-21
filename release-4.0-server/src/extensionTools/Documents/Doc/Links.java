package extensionTools.Documents.Doc;

/**
 * <P>Links is a class that contains all the link types, fields
 * describing a link in OPM and information regarding the selection of them.
 * The class is a part of the DataDictionary.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */

public class Links {

  public Links() {
  }
  /**
   * Counts the number of fields that contains TRUE.
   */
private int Counter = 0;
private boolean Agent;
private boolean Instrument;
private boolean ResCons;
private boolean Effect;
private boolean Event;
private boolean InstEvent;
private boolean Condition;
private boolean Exception;
private boolean Invocation;
private boolean Cond;
private boolean Path;
private boolean ReactTime;
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
 * Sets the value of Agent field to be 'value'
 * @param value of type boolean - the value of the field
 */

public void setAgent(boolean value){
  this.Agent=value;
  if (this.Agent) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Instrument field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setInstrument(boolean value){
  this.Instrument=value;
  if (this.Instrument) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of ResCons field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setResCons(boolean value){
  this.ResCons=value;
  if (this.ResCons) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Effect field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setEffect(boolean value){
  this.Effect=value;
  if (this.Effect) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Event field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setEvent(boolean value){
  this.Event=value;
  if (this.Event) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of InstEvent field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setInstEvent(boolean value){
  this.InstEvent=value;
  if (this.InstEvent) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Condition field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setCondition(boolean value){
  this.Condition=value;
  if (this.Condition) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Exception field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setException(boolean value){
  this.Exception=value;
  if (this.Exception) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Invocation field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setInvocation(boolean value){
  this.Invocation=value;
  if (this.Invocation) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Cond field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setCond(boolean value){
  this.Cond=value;
  if (this.Cond) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Path field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setPath(boolean value){
  this.Path=value;
  if (this.Path) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of ReactTime field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setReactTime(boolean value){
  this.ReactTime=value;
  if (this.ReactTime) {
	this.Counter++;
}
  return;
}


//Get Functions
/**
 * Returns the value of Agent field
 * @return value of Agent field
 */
public boolean getAgent(){
  return this.Agent;
}
/**
 * Returns the value of Instrument field
 * @return value of Instrument field
 */
public boolean getInstrument(){
  return this.Instrument;
}
/**
 * Returns the value of ResCons field
 * @return value of ResCons field
 */
public boolean getResCons(){
  return this.ResCons;
}
/**
 * Returns the value of Effect field
 * @return value of Effect field
 */
public boolean getEffect(){
  return this.Effect;
}
/**
 * Returns the value of Event field
 * @return value of Event field
 */
public boolean getEvent(){
  return this.Event;
}
/**
 * Returns the value of InstEvent field
 * @return value of InstEvent field
 */
public boolean getInstEvent(){
  return this.InstEvent;
}
/**
 * Returns the value of Condition field
 * @return value of Condition field
 */
public boolean getCondition(){
  return this.Condition;
}
/**
 * Returns the value of Exception field
 * @return value of Exception field
 */
public boolean getException(){
  return this.Exception;
}
/**
 * Returns the value of Invocation field
 * @return value of Invocation field
 */
public boolean getInvocation(){
  return this.Invocation;
}
/**
 * Returns the value of Cond field
 * @return value of Cond field
 */
public boolean getCond(){
  return this.Cond;
}
/**
 * Returns the value of Path field
 * @return value of Path field
 */
public boolean getPath(){
  return this.Path;
}
/**
 * Returns the value of ReactTime field
 * @return value of ReactTime field
 */
public boolean getReactTime(){
  return this.ReactTime;
}
/**
 * Puts all the values in the Link from the array.
 * The order of fields in the array: Agent, Condition, Effect, Event, Exception,
 * InstEvent, Invocation, ResCons, Instrument, ResCons, Instrument, Cond, Path,
 * ReactTime.
 * @param array of booleans - the values of the fields
 */

public void LinkInit(boolean [] array){
  this.setAgent(array[0]);
  this.setCondition(array[1]);
  this.setEffect(array[2]);
  this.setEvent(array[3]);
  this.setException(array[4]);
  this.setInstEvent(array[5]);
  this.setInvocation(array[6]);
  this.setResCons(array[7]);
  this.setInstrument(array[8]);
  this.setCond(array[9]);
  this.setPath(array[10]);
  this.setReactTime(array[11]);
}

}
