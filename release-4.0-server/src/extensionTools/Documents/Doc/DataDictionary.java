package extensionTools.Documents.Doc;



/**
 * The DataDictionary Class is part of the Info class, that contains all the
 * choices of the user regarding the fields of the
 * Elements dictionary to be printed in the document or saved
 * in a template.
 * Is divided to: Objects, Processes, Relation and Links.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */

public class DataDictionary {
  public DataDictionary() {
  }

  /**
   * The choices regarding the Objects
   */
 public Objects Obj = new Objects(); //info about objects
 /**
  * The choices regarding the Processes
  */
 public Processes Proc = new Processes(); //info about processes
 /**
  * The choices regarding the Relations
  */
 public Relations Rel = new Relations(); //info about relations
 /**
  * The choices regarding the Links
  */
 public Links Link = new Links(); //info about links

/**
 * The function checks if the user is interested in having the Elements
 * Dictionary part in the generated document.
 * @return TRUE if the part is selected (at least one of the fields is selected), FALSE if not
 */
public boolean isSelected() {
  if ((this.Obj.isSelected())||(this.Proc.isSelected())||(this.Rel.isSelected())||(this.Link.isSelected())) {
	return true;
}
  return false;
}
}