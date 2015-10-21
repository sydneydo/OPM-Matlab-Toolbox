package extensionTools.Documents.Doc;

import java.util.Comparator;
import java.util.StringTokenizer;

import exportedAPI.opcatAPI.IOpd;
import exportedAPI.opcatAPI.ISystem;

/**
 * <P>compOPD is a class that implements Comparator in order to compare between
 * OPD's, to make it possible to have them printed in the order from Root down.
 * @author		Olga Tavrovsky
 * @author		Anna Levit
 */

public class compOpd implements Comparator {

  ISystem mySys;

  public compOpd(ISystem sys) {
  this.mySys=sys;
  }
/**
 * The implementation of function compare for IOPD.<P>
 * Returns which of the two opd's should appear first in the document,
 * @param o1 object of class IOPD to be compared
 * @param o2 object of class IOPD to be compared
 * @return 1 if the first object entered should appear first,
 * -1 if the second object shoud appear first, 0 if there is no difference
 */
  public int compare(Object o1, Object o2){
  IOpd io1=(IOpd)o1;
  IOpd io2=(IOpd)o2;
  String name1 = io1.getName();
  String name2 = io2.getName();
//  String name11 = "";
//  String name22 = "";

  StringTokenizer st = new StringTokenizer(name1, "-", false);
  name1=st.nextToken();
  st = new StringTokenizer(name2, "-", false);
  name2=st.nextToken();

    if(name1.compareTo(name2)==0) {
      return 0;
    }
    if(name1.compareTo(name2)<0) {
		return -1;
	} else {
		return 1;
	}
  }


}