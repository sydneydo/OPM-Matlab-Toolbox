package extensionTools.opl2.alg;

import java.util.Hashtable;

import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import extensionTools.opl2.generated.OPLscript;
import extensionTools.opl2.generated.ObjectFactory;

/**
 *   The main algorithm class.
 *   <br> Date :  12:18PM  21/02/01
 *     General strategy to build OPL tree
 *     @author Valeriya Bodnya
 *     @version 0.1
 */

// Contains relevant data
// Dispatches control flow
// Depending on type of builded tree
public class OPLContainer {

  /**
   * @param system - OPD system
   */
  public OPLContainer(ISystem system,ObjectFactory ob_) {
    this.objFactory = ob_;
    this.myStructure = system.getISystemStructure();
    this.systemName = system.getName();
    this.refresh();
  }

  /**
   * Cleans all the structures
   */
  public void refresh() {
    this.oplByOpds.clear();
    this.nonUpdated = true;
  }

  /**
   * Returns full tree
   */
  public OPLscript getOPLTree() throws Exception {
    if (this.nonUpdated) {
      this.treeBuilder = new OPLTreePerSystemBuilder(this.myStructure,
                                                this.systemName, this.objFactory);
      try {
        this.opl = this.treeBuilder.buildGeneralTree();
      }
      catch (Exception e) {
        throw e;
      }
      this.nonUpdated = false;
    }
    return this.opl;
  }

  /**
   * Returns opd tree
   * @param system - OPD system
   */
  public OPLscript getOPLTreePerOPD(long opd) throws Exception {
    OPLscript tmpOpl;
    try {
      this.treeBuilder = new OPLTreePerOPDBuilder(this.myStructure,
                                             this.systemName, opd, this.objFactory);
      tmpOpl = this.treeBuilder.buildGeneralTree();
      //oplByOpds.put(opdID, tmpOpl);
    }
    catch (Exception e) {
      //System.err.println("Couldn't create new opl script");
      throw e;
    }
    return tmpOpl;
  }

  /***********************************************************************/
  //      My Data Structures!!!!
  /***********************************************************************/

  /** Structure received from OPCAT API**/
  private ISystemStructure myStructure;

  /**   The result XML tree for the whole script */
  private OPLscript opl;

  /**   Key - opdId, value- Collection of thingSentenceSets for the key*/
  private Hashtable oplByOpds = new Hashtable();

  private String systemName;

  private OPLTreeBuilder treeBuilder;

  // If since updating we didn't rebuild system tree
  // the flag is true
  private boolean nonUpdated = true;

  private ObjectFactory objFactory;

}