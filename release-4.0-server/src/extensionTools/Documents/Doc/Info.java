package extensionTools.Documents.Doc;

/**
 * <P>Info is a class that contains all the information regarding
 * the fields that should be inserted in a document. The class is a
 * part of the Document.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */

public class Info {

  public Info() {
  }
  /**
   * Contains the selections of Objects, Things, Relations and Links.
   */
 public DataDictionary Data = new DataDictionary();
 /**
  * Contains the selections regarding OPD and OPL part.
  */
 public OPDOPL opdopl= new OPDOPL();
 /**
  * Contains the selections of General Info fields.
  */
 public GenInfo GI = new GenInfo();

  }