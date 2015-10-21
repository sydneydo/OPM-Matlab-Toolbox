package extensionTools.Documents.Doc;

import java.util.Comparator;

import exportedAPI.opcatAPI.IEntry;

/**
 * <P>compar is a class that implements Comparator in order to compare between
 * objects or between processes, to make it possible to have them printed
 * in the abc order
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */

public class compar implements Comparator {

  public compar() {
  }

/**
 * The implementation of function compare for IEntry.
 * Returns which of the two objects/processes should appear first in the document.
 * @param o1 object of class IProcessEntry or IObjectEntry to be compared
 * @param o2 object of class IProcessEntry or IObjectEntry to be compared
 * @return 1 if the first object entered should appear first,
 * -1 if the second object shoud appear first, 0 if there is no difference
 */

  public int compare(Object o1, Object o2){
  IEntry io1=(IEntry)o1;
  IEntry io2=(IEntry)o2;
    if(io1.getName().compareTo(io2.getName())==0) {
		return 0;
	}
    if(io1.getName().compareTo(io2.getName())<0) {
		return -1;
	} else {
		return 1;
	}
  }


}