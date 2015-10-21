package extensionTools.opl2.alg;

import exportedAPI.opcatAPI.ILinkEntry;


/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author OPCAT team
 * @version 1.0
 */

public class MyLinksComparator extends MyComparator {

  public MyLinksComparator() {
  }

 public int compare(Object o, Object c) {
   ILinkEntry o1 = (ILinkEntry)o;
   ILinkEntry o2 = (ILinkEntry)c;
   if ((o1 == null) || (o2 == null) ) {
	   return 1 ; 
   }
   return o1.getPath().compareTo(o2.getPath());
  }

}
