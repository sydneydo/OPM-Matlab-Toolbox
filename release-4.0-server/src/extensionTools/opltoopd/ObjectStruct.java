package extensionTools.opltoopd;

import java.util.HashSet;
/**
 *Represents an Object sentence
 */
public class ObjectStruct {


  private boolean Environmental;
  private boolean IsPersistent = true;
  private boolean IsPhysical;
  private String ObjectType;
  private String Scope;
  private boolean IsKey;
  private String InitialValue;
  private String ObjectName;
  private String Role;
  /**
   *If the Object is destination, this is its cardinality
   */
   public String DestinationCardinality;

/**
 * The states of this Object like they appeared in the sentence
 */
  public HashSet States = new HashSet();

  public ObjectStruct(){ }
/**
 * Creates a new Object
 * @param environmental is true if Object is environmental
 * @param isPhysical is true if Object is physical
 * @param objectType the Object type
 * @param scope the Object scope
 * @param initialValue the Object initial Value
 * @param isKey is true if Object is Key
 * @param objectName the Object name
 * @param role the Object role
 * @param isPersistent is true if Object is persistent
 */
  public ObjectStruct(boolean environmental, boolean isPhysical,
                       String objectType, String scope,String initialValue,
                       boolean isKey, String objectName,String role,
                       boolean isPersistent) {

    this.Environmental = environmental;
    this.IsPersistent = isPersistent;
    this.IsPhysical = isPhysical;
    this.ObjectType = objectType;
    this.InitialValue = initialValue;
    this.Scope = scope;
    this.IsKey = isKey;
    this.ObjectName = objectName;
    this.Role = role;
  }

/**
 * Returns true if the Object is Environmental
 * @return isEnvironmental
 */
  public boolean getEnvironmental(){
    return this.Environmental;
  }

  /**
   * Returns true if the Object is Persistent
   * @return isPersistent
  */
  public boolean getIsPersistent(){
    return this.IsPersistent;
  }
  /**
   * Returns true if the Object is Physical
   * @return isPhysical
   */
  public boolean getIsPhysical(){
    return this.IsPhysical;
  }

  /**
   * Returns true if the Object is Key
   * @return isKey
   */
  public boolean getIsKey(){
    return this.IsKey;
  }

  /**
   * Returns the Object type
   * @return ObjectType
   */

  public String getObjectType(){
    return this.ObjectType;
  }

  /**
   * Returns Object's Initial Value or null if there is no Initial Value
   * @return InitialValue
   */
  public String getInitialValue(){
    return this.InitialValue;
  }

  /**
   * Returns the Scope or null if there is no Scope
   * @return Scope
   */
  public String getScope(){
    return this.Scope;
  }

  /**
   * Returns the Object Name
   * @return ObjectName
   */
  public String getObjectName(){
    return this.ObjectName;
  }

  /**
   * Returns the Role or null if there is no Role
   * @return Role of Object
   */
  public String getRole(){
    return this.Role;
  }

  /**
   * Sets the Environmental parameter of the Object
   * @param environmental
   */
  public void setEnvironmental(boolean environmental){
    this.Environmental = environmental;
  }

  /**
   * Sets the Persistent parameter of the Object
   * @param isPersistent true if Object is Persistent
   */
  public void setIsPersistent(boolean isPersistent){
    this.IsPersistent = isPersistent;
  }

  /**
   * Sets the Physical parameter of the Object
   * @param isPhysical true if Object is Physical
   */
  public void setIsPhysical(boolean isPhysical){
    this.IsPhysical = isPhysical;
  }

  /**
   * Sets the isKey true if the Object is Key
   * @param isKey true if the Object is Key
   */
  public void setIsKey(boolean isKey){
    this.IsKey = isKey;
  }

  /**
   * Sets the initial value of the Object
   * @param initialValue the initial Value of the Object
   *                     or null if there is no initial Value
   */
  public void setInitialValue(String initialValue){
    this.InitialValue = initialValue;
  }

  /**
   * Sets the Object type
   * @param objectType the type of the Object or null if there is no type
   */
  public void setObjectType(String objectType){
    this.ObjectType = objectType;
  }

  /**
   * Sets the Object scope
   * @param scope the scope of the Object or null if there is no scope
   */
  public void setScope(String scope){
    this.Scope = scope;
  }

  /**
   * Sets the Object name
   * @param objectName the name of the Object
   */
  public void setObjectName(String objectName){
    this.ObjectName = objectName;
  }

  /**
   * Sets the Object role
   * @param role the role of the Object or null if there is no role
   */
  public void setRole(String role){
    this.Role = role;
  }



}



