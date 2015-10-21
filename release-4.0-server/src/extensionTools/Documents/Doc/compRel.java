package extensionTools.Documents.Doc;

import java.util.Comparator;

import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.ISystem;

/**
 * <P>compRel is a class that implements Comparator in order to compare between
 * relations, to make it possible to have them printed in the abc order,
 * when all the relations of the same type with the same source are printed
 * together, in the abc order of the destinaton.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */

public class compRel implements Comparator {

  ISystem mySys;

  public compRel(ISystem sys) {
  this.mySys=sys;
  }
/**
 * The implementation of function compare for IRelationEntry.<P>
 * Returns which of the two relations should appear first in the document.
 * @param o1 object of class IRelationEntry to be compared
 * @param o2 object of class IRelationEntry to be compared
 * @return 1 if the first object entered should appear first,
 * -1 if the second object shoud appear first, 0 if there is no difference
 */
  public int compare(Object o1, Object o2){
  IRelationEntry io1=(IRelationEntry)o1;
  IRelationEntry io2=(IRelationEntry)o2;
  String name1 = this.mySys.getISystemStructure().getIEntry(io1.getSourceId()).getName();
  String name2 = this.mySys.getISystemStructure().getIEntry(io2.getSourceId()).getName();
  String name11 = this.mySys.getISystemStructure().getIEntry(io1.getDestinationId()).getName();
  String name22 = this.mySys.getISystemStructure().getIEntry(io2.getDestinationId()).getName();

    if(name1.compareTo(name2)==0) {
      if (name11.compareTo(name22)==0) {
		return 0;
	}
      if (name11.compareTo(name22)<0) {
		return -1;
	} else {
		return 1;
	}
    }
    if(name1.compareTo(name2)<0) {
		return -1;
	} else {
		return 1;
	}
  }


}