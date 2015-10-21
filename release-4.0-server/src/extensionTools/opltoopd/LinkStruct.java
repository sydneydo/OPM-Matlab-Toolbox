package extensionTools.opltoopd;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Represents the Link sentence
 */
public class LinkStruct {

  private String LinkType;
  private String LinkCondition;
  private String SourceCardinality;
  private String DestinationCardinality;
  private String MinReaction;
  private String MaxReaction;
  private String Path;
  private ObjectStruct SourceObject;
  private ProcessStruct SourceProcess;
  /**
   * The Object destinations of this Link sentence.
   */
  public HashSet DestObjects = new HashSet();
  /**
   * The Process destinations of this Link sentence.
   */
  public HashSet DestProcesses = new HashSet();
  /**
   * Creates new Link
   */
  public LinkStruct() { }

  /**
   * Creates new Link
   * @param LinkType the Link type
   * @param linkCondition the condition of the Link or null
   * @param sourceCardinality the source cardinality of the Link or null
   * @param destinationCardinality the destination cardinality of the Link or null
   * @param minReaction the Link min reaction time
   * @param maxReaction the Link max reaction time
   * @param path the Link path
   * @param sourceObject the source Object of the Link or null
   * @param sourceProcess the source Process of the Link or null
   */
  public LinkStruct(String linkType, String linkCondition,
                    String sourceCardinality, String destinationCardinality,
                    String minReaction, String maxReaction, String path,
                    ObjectStruct sourceObject, ProcessStruct sourceProcess) {
    this.LinkType = linkType;
    this.LinkCondition = linkCondition;
    this.SourceCardinality = sourceCardinality;
    this.DestinationCardinality = destinationCardinality;
    this.MinReaction = minReaction;
    this.MaxReaction = maxReaction;
    this.Path = path;
    this.SourceObject = sourceObject;
    this.SourceProcess = sourceProcess;
  }

  /**
   * Returns Link type
   * @return Link type
   */
  public String getLinkType() {
    return this.LinkType;
  }

  /**
   * Returns Link condition
   * @return Link condition
   */
  public String getLinkCondition() {
    return this.LinkCondition;
  }

  /**
   * Returns the source cardinality of the Link or null
   * @return Source Cardinality
   */
  public String getSourceCardinality() {
    return this.SourceCardinality;
  }

  /**
   * Returns the destination cardinality of the Link or null
   * @return Destination Cardinality
   */
  public String getDestinationCardinality() {
    return this.DestinationCardinality;
  }

  /**
   * Returns the min reaction time of the Link or null
   * @return Min Reaction Time
   */
  public String getMinReaction() {
    return this.MinReaction;
  }

  /**
   * Returns the max reaction time of the Link or null
   * @return Max Reaction Time
   */
  public String getMaxReaction() {
    return this.MaxReaction;
  }

  /**
   * Returns Link path or null
   * @return Path
   */
  public String getPath() {
    return this.Path;
  }

  /**
   * Returns the source object of the Link or null
   * @return Source Object
   */
  public ObjectStruct getSourceObject() {
    return this.SourceObject;
  }

  /**
   * Returns the source process of the Link or null
   * @return Source Process
   */
  public ProcessStruct getSourceProcess() {
    return this.SourceProcess;
  }

  /**
   * Returns the destination objects of the Link or null
   * @return Destination Objects
   */
  public Iterator getDestObjects(){
    return this.DestObjects.iterator();
  }

  /**
   * Returns the destination processes of the Link or null
   * @return Destination Processes
   */
  public Iterator getDestProcesses(){
    return this.DestProcesses.iterator();
  }

  /**
   * Sets type of the Link
   * @param linkType the type of the Link
   */
  public void setLinkType(String linkType) {
    this.LinkType = linkType;
  }

  /**
  * Sets Link condition
  * @param linkCondition the the condition of the Link
  *                     or null if there is no condition
   */
  public void setLinkCondition(String linkCondition) {
    this.LinkCondition = linkCondition;
  }

  /**
  * Sets source cardinality of the Link
  * @param sourceCardinality the source cardinality of the Link
  *                     or null if there is no source cardinality
   */
  public void setSourceCardinality(String sourceCardinality) {
    this.SourceCardinality = sourceCardinality;
  }

  /**
  * Sets destination cardinality of  the Link
  * @param destinationCardinality the destination cardinality of the Link
  *                     or null if there is no destination cardinality
   */
  public void setDestinationCardinality(String destinationCardinality) {
    this.DestinationCardinality = destinationCardinality;
  }

  /**
  * Sets min reaction time of the Link
  * @param minReaction the min reaction time of the Link
  *                     or null if there is no min reaction time
   */
  public void setMinReaction(String minReaction) {
    this.MinReaction = minReaction;
  }

  /**
  * Sets max reaction time of  the Link
  * @param maxReaction the max reaction time of the Link
  *                     or null if there is no max reaction time
   */
  public void setMaxReaction(String maxReaction) {
    this.MaxReaction = maxReaction;
  }

  /**
  * Sets the Link path
  * @param path the path of the Link
  *                     or null if there is no path
   */
  public void setPath(String path) {
    this.Path = path;
  }

  /**
  * Sets source object of the Link
  * @param sourceObject the source object of the Link
  *                     or null if there is no source object
   */
  public void setSourceObject(ObjectStruct sourceObject) {
    this.SourceObject = sourceObject;
  }

  /**
  * Sets source process of the Link
  * @param sourceProcess the source process of the Link
  *                     or null if there is no source process
   */
  public void setSourceProcess(ProcessStruct sourceProcess) {
    this.SourceProcess = sourceProcess;
  }
}

