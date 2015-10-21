package extensionTools.Documents.Doc;

import java.util.Comparator;

import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.ISystem;

/**
 * <P>compLink is a class that implements Comparator in order to compare between
 * links, to make it possible to have the links printed in the abc order,
 * when all the links of the same type with the same source are printed
 * together, in the abc order of the destinaton.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */


public class compLink implements Comparator {

  ISystem mySys;

  public compLink(ISystem sys) {
    this.mySys=sys;
  }

/**
 * The implementation of function compare for ILinkEntry.<P>
 * Returns which of the two links should appear first in the document.
 * @param o1 object of class ILinkEntry to be compared
 * @param o2 object of class ILinkEntry to be compared
 * @return 1 if the first object entered should appear first,
 * -1 if the second object shoud appear first, 0 if there is no difference
 */

  public int compare(Object o1, Object o2){
    ILinkEntry io1=(ILinkEntry)o1;
    ILinkEntry io2=(ILinkEntry)o2;
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