package extensionTools.Documents.Doc;


/**
 * <P>OPDOPL is a class that contains all the information regarding
 * the OPD and OPL Paragraphs that should appear in the document.
 * The class is a part of the Info.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */


public class OPDOPL {

  public OPDOPL() {
 }
 /**
  * Contains TRUE if no Diagrams are to be inserted
  */
private boolean OPDNone;
 /**
  * Contains TRUE if all Diagrams are to be inserted
  */
private boolean OPDAll;
 /**
  * Contains the maximum level of Diagrams to be inserted, "-1" if
  * this field is not relevant
  */
private String OPDUntil;
 /**
 * Contains TRUE if Diagrams are to be chosen by name
 */
private boolean OPDByName;
/**
 * Contains TRUE if no OPL Paragraphs are to be inserted
 */
private boolean OPLNone;
/**
 * Contains TRUE if all OPL Paragraphs are to be inserted
 */
private boolean OPLAll;
/**
 * Contains the maximum level of OPL Paragraphs to be inserted, "-1" if
 * this field is not relevant
 */
private String OPLUntil;
/**
 * Contains TRUE if OPL Paragraphs are to be chosen by name
 */
private boolean OPLByName;
/**
 * Contains TRUE if OPL Paragraphs are to be attached to the Diagrams
 */
private boolean OPLAccording;
/**
 * Contains a list of Diagram names to be inserted if ByName
 */
private Object[] arr_opd_list;
/**
 * Contains a list of OPL Paragraphs names to be inserted if ByName
 */
private Object[] arr_opl_list;

 /**
  * Is OPD part selected
  * @return TRUE if the part is selected
 */
boolean OPDisSelected(){
  if  (this.OPDNone) {
	return false;
} else {
	return true;
}
}
/**
 * Is OPL part selected
 * @return TRUE if the part is selected
*/

boolean OPLisSelected(){
  if (this.OPLNone || (this.OPDNone && this.OPLAccording)) {
	return false;
} else {
	return true;
}
}
/**
 * Is the OPL attached to the OPD
 * @return TRUE if the part is attached
*/

boolean isAttached() {
  if (this.OPLAccording || (this.OPDNone && this.OPLNone) ||(this.OPDAll && this.OPLAll) ||
         ((this.OPDUntil.compareTo(this.OPLUntil)==0)&&(this.OPDUntil.compareTo("-1")!=0))) {
	return true;
} else {
	return false;
}
}

//Set Functions
/**
 * Sets the value of OPDNone field to be 'value'
 * @param value of type boolean - the value of the field
 */

public void setOPDNone(boolean value){
  this.OPDNone=value;
  return;
}
/**
 * Sets the value of OPDAll field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOPDAll(boolean value){
  this.OPDAll=value;
  return;
}
/**
 * Sets the value of OPDUntil field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOPDUntil(String value){
  this.OPDUntil=value;
  return;
}
/**
 * Sets the value of OPDByName field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOPDByName(boolean value){
  this.OPDByName=value;
  return;
}
/**
 * Sets the value of OPLNone field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOPLNone(boolean value){
  this.OPLNone=value;
  return;
}
/**
 * Sets the value of OPLAll field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOPLAll(boolean value){
  this.OPLAll=value;
  return;
}
/**
 * Sets the value of OPLUntil field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOPLUntil(String value){
  this.OPLUntil=value;
  return;
}
/**
 * Sets the value of OPLByName field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOPLByName(boolean value){
  this.OPLByName=value;
  return;
}
/**
 * Sets the value of OPLAccording field to be 'value'
 * @param value of type boolean - the value of the field
 */
public void setOPLAccording(boolean value){
  this.OPLAccording=value;
  return;
}
/**
 * Sets the value of arr_opd_list - the list of OPDs
 * @param arr of type Object[] - an array of diagram names
 */
public void setOPDList(Object[] arr){
  this.arr_opd_list=arr;
  return;
}
/**
 * Sets the value of arr_opl_list - the list of OPLs
 * @param arr of type Object[] - an array of diagram names
 */

public void setOPLList(Object[] arr){
  this.arr_opl_list=arr;
  return;
}

//Get Functions
/**
 * Returns the value of OPDNone field
 * @return value of OPDNone field
 */
public boolean getOPDNone(){
  return this.OPDNone;
}
/**
 * Returns the value of OPDAll field
 * @return value of OPDAll field
 */
public boolean getOPDAll(){
  return this.OPDAll;
}
/**
 * Returns the value of OPDUntil field
 * @return value of OPDUntil field
 */
public String getOPDUntil(){
  return this.OPDUntil;
}
/**
 * Returns the value of OPDByName field
 * @return value of OPDByName field
 */
public boolean getOPDByName(){
  return this.OPDByName;
}
/**
 * Returns the value of OPLNone field
 * @return value of OPLNone field
 */
public boolean getOPLNone(){
  return this.OPLNone;
}
/**
 * Returns the value of OPLAll field
 * @return value of OPLAll field
 */
public boolean getOPLAll(){
  return this.OPLAll;
}
/**
 * Returns the value of OPLUntil field
 * @return value of OPLUntil field
 */
public String getOPLUntil(){
  return this.OPLUntil;
}
/**
 * Returns the value of OPLByName field
 * @return value of OPLByName field
 */
public boolean getOPLByName(){
  return this.OPLByName;
}
/**
 * Returns the value of OPLAccording field
 * @return value of OPLAccording field
 */
public boolean getOPLAccording(){
  return this.OPLAccording;
}

/**
 * Returns an array of diagram names - OPD list
 * @return array of diagram names Object[]
 */
public Object[] getOPDList1(){
  return this.arr_opd_list;
}
/**
 * Returns an array of diagram names - OPL list
 * @return array of diagram names Object[]
 */
public Object[] getOPLList1(){
  return this.arr_opl_list;
}

/**
 * Puts all the values in the OPD and OPL fields.
 * The order of fields in the array: OPDAll, OPDByName, OPDNone, OPLAll,
 * OPLByName, OPLNone, OPLAccording.
 * @param array array of values for boolean fields
 * @param OPDUntil of type string for OPD Until level
 * @param OPLUntil of type string for OPD Until level
 */
public void OPDOPLInit(boolean[] array,String OPDUntil, String OPLUntil){
this.setOPDAll(array[0]);
this.setOPDByName(array[1]);
this.setOPDNone(array[2]);
this.setOPLAll(array[3]);
this.setOPLByName(array[4]);
this.setOPLNone(array[5]);
this.setOPLAccording(array[6]);
this.setOPDUntil(OPDUntil);
this.setOPLUntil(OPLUntil);
}

}
