package extensionTools.Documents.Doc;

/**
 * <P>GenInfo is a class that contains all the information regarding
 * the fields that should be inserted in the general info part. The class is a
 * part of the Info.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */


public class GenInfo {

  public GenInfo() {
  }
  /**
   * Counts the number of fields that contains TRUE.
   */
  private int Counter;//how many fields are selected
  /**
   * Counts the number of fields that contains TRUE.
   */
  private boolean Client;
  private boolean Overview;
  private boolean Current;
  private boolean Goals;
  private boolean Business;
  private boolean Future;
  private boolean Hard;
  private boolean Inputs;
  private boolean Issues;
  private boolean Problems;
  private boolean Users;
  private boolean Oper;

  /**
   * Is at least one of the fields in this part selected.
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
 * Sets the value of Client field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setClient(boolean value){
  this.Client=value;
  if (this.Client) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Overview field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOverview(boolean value){
  this.Overview=value;
  if (this.Overview) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Goals field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setGoals(boolean value){
  this.Goals=value;
  if (this.Goals) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Users field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setUsers(boolean value){
  this.Users=value;
  if (this.Users) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Hardware field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setHard(boolean value){
  this.Hard=value;
  if (this.Hard) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Inputs field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setInputs(boolean value){
  this.Inputs=value;
  if (this.Inputs) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Current state field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setCurrent(boolean value){
  this.Current=value;
  if (this.Current) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Business field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setBusiness(boolean value){
  this.Business=value;
  if (this.Business) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Future field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setFuture(boolean value){
  this.Future=value;
  if (this.Future) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Issues field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setIssues(boolean value){
  this.Issues=value;
  if (this.Issues) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Problems field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setProblems(boolean value){
  this.Problems=value;
  if (this.Problems) {
	this.Counter++;
}
  return;
}
/**
 * Sets the value of Oper field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOper(boolean value){
  this.Oper=value;
  if (this.Oper) {
	this.Counter++;
}
  return;
}
//Get Functions
/**
 * Returns the value of Client field
 * @return value of client field
 */
public boolean getClient(){
  return this.Client;
}
/**
 * Returns the value of Overview field
 * @return value of Overview field
 */
public boolean getOverview(){
  return this.Overview;
}
/**
 * Returns the value of Goals field
 * @return value of Goals field
 */
public boolean getGoals(){
  return this.Goals;
}
/**
 * Returns the value of Users field
 * @return value of Users field
 */
public boolean getUsers(){
  return this.Users;
}
/**
 * Returns the value of Hard field
 * @return value of Hard field
 */
public boolean getHard(){
  return this.Hard;
}
/**
 * Returns the value of Inputs field
 * @return value of Inputs field
 */
public boolean getInputs(){
  return this.Inputs;
}
/**
 * Returns the value of Current field
 * @return value of Current field
 */
public boolean getCurrent(){
  return this.Current;
}
/**
 * Returns the value of Business field
 * @return value of Business field
 */
public boolean getBusiness(){
  return this.Business;
}
/**
 * Returns the value of Future field
 * @return value of Future field
 */
public boolean getFuture(){
  return this.Future;
}
/**
 * Returns the value of Problems field
 * @return value of Problems field
 */
public boolean getProblems(){
  return this.Problems;
}
/**
 * Returns the value of Oper field
 * @return value of Oper field
 */
public boolean getOper(){
  return this.Oper;
}
/**
 * Returns the value of Issues field
 * @return value of Issues field
 */
public boolean getIssues(){
  return this.Issues;
}
/**
 * Puts all the values in the General Info from the array.
 * The order of fields in the array: Client, Overview, Current, Goals,
 * Business, Future, Hard, Inputs, Issues, Problems, Users, Oper
 * @param array of booleans - the values of the fields
 */
public void GIInit(boolean [] array){
  this.setClient(array[0]);
  this.setOverview(array[1]);
  this.setCurrent(array[2]);
  this.setGoals(array[3]);
  this.setBusiness(array[4]);
  this.setFuture(array[5]);
  this.setHard(array[6]);
  this.setInputs(array[7]);
  this.setIssues(array[8]);
  this.setProblems(array[9]);
  this.setUsers(array[10]);
  this.setOper(array[11]);
  }
}
