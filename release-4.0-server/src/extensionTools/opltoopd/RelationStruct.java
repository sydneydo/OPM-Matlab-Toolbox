package extensionTools.opltoopd;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Represents a Relation sentence
 */
public class RelationStruct {

  private String RelationType;
  private String BackwardMeaning;
  private String ForwardMeaning;
  private String SourceCardinality;
  private String DestinationCardinality;
  private ObjectStruct SourceObject;
  private ProcessStruct SourceProcess;
  /**
   * Object destinations of this Relation sentence.
   */
  public HashSet DestObjects = new HashSet();

  /**
   * Process destinations of this Relation sentence.
   */
  public HashSet DestProcesses = new HashSet();

  public RelationStruct() {}

  /**
   * Creates a new Relation
   * @param relationType the Relation type
   * @param backwardMeaning the backward meaning of the Relation or null
   * @param forwardMeaning the forward meaning of the Relation or null
   * @param sourceCardinality the source cardinality of the Relation or null
   * @param destinationCardinality the destination cardinality of the Relation or null
   * @param sourceObject the source Object of the Relation or null
   * @param sourceProcess the source Process of the Relation or null
   */
  public RelationStruct(String relationType, String backwardMeaning,
                        String forwardMeaning, String sourceCardinality,
                        String destinationCardinality, ObjectStruct sourceObject,
                        ProcessStruct sourceProcess) {

    this.RelationType = relationType;
    this.BackwardMeaning = backwardMeaning;
    this.ForwardMeaning = forwardMeaning;
    this.SourceCardinality = sourceCardinality;
    this.DestinationCardinality = destinationCardinality;
    this.SourceObject = sourceObject;
    this.SourceProcess = sourceProcess;
  }

  /**
   * Returns the type of the Relation
   * @return Relation type
   */
  public String getRelationType() {
    return this.RelationType;
  }

  /**
   * Returns the backward meaning of the Relation or null
   * @return Backward Meaning
   */
  public String getBackwardMeaning() {
    return this.BackwardMeaning;
  }

  /**
   * Returns the forward meaning of the Relation or null
   * @return Forward Meaning
   */
  public String getForwardMeaning() {
    return this.ForwardMeaning;
  }

  /**
   * Returns the source cardinality of the Relation or null
   * @return Source Cardinality
   */
  public String getSourceCardinality() {
    return this.SourceCardinality;
  }

  /**
   * Returns the destination cardinality of the Relation or null
   * @return Destination Cardinality
   */
  public String getDestinationCardinality() {
    return this.DestinationCardinality;
  }

  /**
  * Returns the source object of the Relation or null
  * @return Source Object
  */
  public ObjectStruct getSourceObject() {
    return this.SourceObject;
  }

  /**
   * Returns the source process  of the Relation or null
   * @return Source Process
   */
  public ProcessStruct getSourceProcess() {
    return this.SourceProcess;
  }

  /**
   * Returns the destination objects  of the Relation or null
   * @return Destination Objects
   */
  public Iterator getDestObjects(){
     return this.DestObjects.iterator();
   }

   /**
    * Returns the destination processes  of the Relation or null
    * @return Destination Processes
   */
   public Iterator getDestProcesses(){
     return this.DestProcesses.iterator();
  }

  /**
   * Sets the type of the Relation
   * @param relationType the type of the Relation
   */
  public void setRelationType(String relationType) {
    this.RelationType = relationType;
  }

  /**
   * Sets the backward meaning of the Relation
   * @param backwardMeaning the the backward meaning of the Relation
   *                     or null if there is no backward meaning
   */
  public void setBackwardMeaning(String backwardMeaning) {
    this.BackwardMeaning = backwardMeaning;
  }

  /**
   * Sets the forward meaning of the Relation
   * @param forwardMeaning the the forward meaning of the Relation
   *                     or null if there is no forward meaning
   */
  public void setForwardMeaning(String forwardMeaning) {
    this.ForwardMeaning = forwardMeaning;
  }

  /**
   * Sets the source cardinality of the Relation
   * @param sourceCardinality the the source cardinality of the Relation
   *                     or null if there is no source cardinality
   */
  public void setSourceCardinality(String sourceCardinality) {
    this.SourceCardinality = sourceCardinality;
  }

  /**
   * Sets the destination cardinality of the Relation
   * @param destinationCardinality the the destination cardinality of the Relation
   *                     or null if there is no destination cardinality
   */
  public void setDestinationCardinality(String destinationCardinality) {
    this.DestinationCardinality = destinationCardinality;
  }

  /**
   * Sets the source object of the Relation
   * @param sourceObject the the source object of the Relation
   *                     or null if there is no source object
   */
  public void setSourceObject(ObjectStruct sourceObject) {
    this.SourceObject = sourceObject;
  }

  /**
   * Sets the source process of the Relation
   * @param sourceProcess the the source process of the Relation
   *                     or null if there is no source process
   */
  public void setSourceProcess(ProcessStruct sourceProcess) {
    this.SourceProcess = sourceProcess;
  }

}