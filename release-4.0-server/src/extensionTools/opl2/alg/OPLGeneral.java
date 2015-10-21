package extensionTools.opl2.alg;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IThingInstance;
import extensionTools.opl2.generated.MaxReactionTimeType;
import extensionTools.opl2.generated.MaxTimeValueType;
import extensionTools.opl2.generated.MaxTimeoutValueType;
import extensionTools.opl2.generated.MinReactionTimeType;
import extensionTools.opl2.generated.MinTimeValueType;
import extensionTools.opl2.generated.TimeValueType;

/**
 * <p>Title: Extension Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author OPCAT team
 * @version 1.0
 */

public class OPLGeneral {

  public OPLGeneral() {
  }

  public static final int MSEC = 0, SECOND = 1, MINUTE = 2,
      HOUR = 3, DAY = 4, MONTH = 5, YEAR = 6;

  public static String[] timeUnitName = {
      "msec", "second", "minute", "hour",
      "day", "month", "year"};
  /**
         * The key name for the thing name representation.
         */
        public static final String THING_NAME = "thing";

        /**
         * The key name for the library name representation.
         */
        public static final String LIBRARY_NAME = "library";


  public static String[] basicType = {
      "Boolean", "char", "short",
      "integer", "unsigned integer", "long","float","double"};


  /**
     This function translates String type of "#;#;#;#;#;#;#",
               where # is a digit to time sentence. For example:
     "0;1;2;0;0;0;5" to " 1 minute 2 hours 5 years".
   */
  static public int[] getTime(String time) {
    int[] res = {
        -1, -1, -1, -1, -1, -1, -1};
    if (time.equals("") || time.equalsIgnoreCase("infinity")) {
      return res;
    }
    StringTokenizer st = new StringTokenizer(time.trim(), ";", false);
    int j=0;
    while(st.hasMoreElements()){
      res[j]=getInt(st.nextToken());
      j++;
    }
    return res;
  }

  static public boolean isNotEmpty(String str) {
    return (!str.equals("none")) && (!str.equals(""));
  }

  static public boolean isEmpty(String str) {
    return ( (str.equals("none") || str.equals("")));
  }

  /**
     Verify if the string has true value (both "y" and "1")
   */
  static public boolean isTrue(String m) {
    if (m.equalsIgnoreCase("y") || m.equals("1")) {
		return true;
	}
    return false;
  }

  static public List sort(Enumeration e, MyComparator comp) {
    java.util.List list = new java.util.LinkedList();
    while(e.hasMoreElements()){
      list.add(e.nextElement());
    }
    Collections.sort(list,comp);
    return list;
  }



  static public int findPlace(java.util.List lst,IEntry e1){
    int i=0;
    for(;i<lst.size();i++){
      if(((IEntry)lst.get(i)).getId()>e1.getId()){
        return i;
      }
    }
    return i;
  }

  static public List sort2(Enumeration e, GeneralComparator comp) {
    java.util.List list1 = new java.util.LinkedList();
    java.util.List list2 = new java.util.LinkedList();
    java.util.List list = new java.util.LinkedList();
    IEntry elem;
    if(e!=null) {
		while(e.hasMoreElements()){
		  elem = (IEntry)e.nextElement();
		  if(elem instanceof IObjectEntry){
		    int i = findPlace(list1,elem);
		    list1.add(i,elem);
		  }else if(elem instanceof IProcessEntry){
		    int i = findPlace(list1,elem);
		    list2.add(i,elem);
		  }
		}
	}
    list.addAll(list1);
    list.addAll(list2);
    //Collections.sort(list,proc);
    //Collections.sort(list,id);
    //java.util.List lst = new java.util.LinkedList();
    //lst.addAll(list);
    //Collections.sort(list,comp);
    //Collections.sort(list,comp);
    return list;
  }

  static public List sort1(Enumeration e, MyThingsComparator comp) {
   java.util.List list = new java.util.LinkedList();
   while(e.hasMoreElements()){
     IThingInstance ist = (IThingInstance)e.nextElement();
     list.add(ist.getIEntry());
   }
   Collections.sort(list,comp);
   return list;
  }

  static public void addLast(java.util.List list, Object obj) {
    //int last = list.size();
    //list.add(last, obj);
    list.add(obj);
  }

  static public boolean hasEqualPath(java.util.List pathLabel, String path){
    if(pathLabel.isEmpty() && path.equals("")) {
		return true;
	}
    java.util.List tokens = new java.util.LinkedList();
    StringTokenizer st = new StringTokenizer(path,",");
    while(st.hasMoreTokens()){
      tokens.add(st.nextToken());
    }
    return equalPaths(pathLabel,tokens);
  }



  static public boolean equalPaths(java.util.List sentencePath,java.util.List path){
    if((sentencePath.isEmpty()||sentencePath.equals("")) && (path.isEmpty()||path.equals(""))) {
		return true;
	}
    if(sentencePath.size()!=path.size()) {
		return false;
	}
    for(int i=0;i<sentencePath.size();i++){
      if(!((String)sentencePath.get(i)).equals(path.get(i))) {
		return false;
	}
    }
  return true;
  }

  /**
     Verify if the string has false value (both "n" and "0")
   */
  static public boolean isFalse(String m) {
    if (m.equalsIgnoreCase("n") || m.equals("0")) {
		return true;
	}
    return false;
  }

  /** Creates a cardinality string. */

  static public int[] getCardinality(String str) {
    int[] result = {
        DEFAULT, DEFAULT};
    int value;
    //System.err.println(" Before starting cardinality !!!");
    //System.err.println(" The card is: "+ str);
    if(str.charAt(0)=='+'){
     result[0]=1;result[1]=-1;
     return result;
    }
    if(str.charAt(0)=='*'){
     result[0]=0;result[1]=-1;
     return result;
    }
    StringTokenizer st = new StringTokenizer(str.trim(), ",.", false);
    String strTmp;
    if (st.countTokens() == 0){
      //System.err.println(" After starting cardinality !!!");
      //System.err.println(" Returning zero result!!!");
      return result;
    }
    if (st.countTokens() == 1) {
      strTmp = st.nextToken().trim();
      result[0] = result[1] = getInt(strTmp);
      //System.err.println(" After starting cardinality !!!");
      //System.err.println(" Returning same result(one token): "+result[0]);
      return result;
    }
    int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
    while (st.hasMoreTokens()) {
      strTmp = st.nextToken().trim();
        value = getInt(strTmp);
        if(value == -1) {
			max = Integer.MAX_VALUE;
		} else {
          if (value < min) {
			min = value;
		} else if (value > max) {
			max = value;
		}
        }
    }
    result[0] = min;
    if (max == Integer.MAX_VALUE) {
		result[1] = -1;
	} else {
		result[1] = max;
	}
    //System.err.println(" After starting cardinality !!!");
    //System.err.println(" Result returned is: min: "+ new Integer(min) +" max: "+ new Integer(max));
    //System.err.println(" Result returned is: min: "+result[0]+" max: "+result[1]);
    //System.err.println(" After starting cardinality !!!");
    return result;
  }

  public static int getInt(String str){
    if (Character.isLetter(str.charAt(0))) {
		return -1;
	} else if(str.length()>MAXLENGTH){
      return -1;
    }
    int myInt = Integer.parseInt(str, 10);
    if(myInt<0) {
		return 0;
	}
    return myInt;
  }

  public static boolean isByDestination(int rule) {
    return (rule == DESTINATION) ? true : false;
  }

  public static boolean isBasicType(String type){
    for(int i=0;i<basicType.length;i++){
      if(basicType[i].equals(type)) {
		return true;
	}
    }
    return false;
  }

  public static String visit(MinTimeValueType min){
    return visit(min.getTimeValue());
  }

  public static String visit(MaxTimeValueType min){
    return visit(min.getTimeValue());
  }

  public static String visit(MaxReactionTimeType min){
   return visit(min.getTimeValue());
 }

 public static String visit(MinReactionTimeType min){
  return visit(min.getTimeValue());
}

public static String visit(MaxTimeoutValueType min){
 return visit(min.getTimeValue());
}





  public static String visit(TimeValueType max){
   boolean needSpace = false;
   StringBuffer bf = new StringBuffer();
   int number = 0;
   if(max.getYears()>1){
     bf.append(Integer.toString(max.getYears()) + " years");
     needSpace = true;
     number++;
   }else if (max.getYears() == 1){
       bf.append("1 year");
       needSpace = true;
       number++;
   }
   if(max.getMonths()>1){
       if(needSpace) {
		bf.append(", ");
	}
       bf.append(Integer.toString(max.getMonths()) + " months");
       needSpace = true;
       number++;
   }else if (max.getMonths() == 1){
         if(needSpace) {
			bf.append(", ");
		}
         bf.append("1 month");
         needSpace = true;
         number++;
   }
   if(max.getDays()>1){
           if(needSpace) {
			bf.append(", ");
		}
           bf.append(Integer.toString(max.getDays()) + " days");
           needSpace = true;
           number++;
   }else if (max.getDays() == 1){
     if(needSpace) {
		bf.append(", ");
	}
     bf.append("1 day");
     needSpace = true;
     number++;
    }
    if(max.getHours()>1){
      if(needSpace) {
		bf.append(", ");
	}
      bf.append(Integer.toString(max.getHours()) + " hours");
      needSpace = true;
      number++;
    }else if (max.getHours() == 1){
      if(needSpace) {
		bf.append(", ");
	}
      bf.append("1 hour");
      needSpace = true;
      number++;
    }
    if(max.getMinutes()>1){
      if(needSpace) {
		bf.append(", ");
	}
      bf.append(Integer.toString(max.getMinutes()) + " minutes");
      number++;
      needSpace = true;
    }else if (max.getMinutes() == 1){
      if(needSpace) {
		bf.append(", ");
	}
      bf.append("1 minute");
      needSpace = true;
      number++;
    }
    if(max.getSeconds()>1){
      if(needSpace) {
		bf.append(", ");
	}
      bf.append(Integer.toString(max.getSeconds()) + " seconds");
      needSpace = true;
      number++;
    }else if (max.getSeconds() == 1){
      if(needSpace) {
		bf.append(", ");
	}
      bf.append("1 second");
      needSpace = true;
      number++;
    }
    if(max.getMilliSeconds()>1){
      if(needSpace) {
		bf.append(", ");
	}
      bf.append(Integer.toString(max.getMilliSeconds()) + " milliseconds");
      needSpace = true;
      number++;
    }else if (max.getMilliSeconds() == 1){
      if(needSpace) {
		bf.append(", ");
	}
      bf.append("1 millisecond");
      needSpace = true;
      number++;
    }
    int last = bf.toString().lastIndexOf(", ");
    if (number == 2){
      bf.replace(last,last+1, " and ");
    }else if(number>2) {
		bf.insert(last+2,"and ");
	}
    return bf.toString();
 }


  public static int SOURCE = 0;

  public static int DESTINATION = 1;

  public static final int DEFAULT = 1;

  public static final int MAXLENGTH = 6;

}