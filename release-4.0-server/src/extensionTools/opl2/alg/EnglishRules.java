package extensionTools.opl2.alg;

import java.util.Hashtable;
import java.util.StringTokenizer;

public class EnglishRules {
  static Hashtable irregularSingulars;

  public static String pluralOf(String word) {
    StringTokenizer tokenizer = new StringTokenizer(word.trim());
    String head="";
    String tail="";
    String plural = null;

    while (tokenizer.hasMoreTokens())
    {
      head = head+" "+ tail;
      tail = tokenizer.nextToken();
    }

    head = head.trim();

    if (!head.equals(""))
    {
      head+=" ";
    }

    plural = (String)irregularSingulars.get(tail.toLowerCase());
    if (plural!=null)
    {
      return head + plural;
    }

    int length = tail.length();
    if (length <= 1) {
		return head + tail+"'s";
	}
    char lastLetter = tail.charAt(length-1);
    char secondLast = tail.charAt(length-2);
    if (("sxzo".indexOf(lastLetter) >= 0) ||
        ((lastLetter == 'h') && ((secondLast == 's') || (secondLast == 'c'))) ) {
		return head + tail+ "es";
	}
    if (lastLetter == 'y')
    {  if ("aeiou".indexOf(secondLast) >= 0) {
		return head + tail + "s";
	} else {
		return head + tail.substring(0, length-1) + "ies";
	}
    }

    return head + tail + "s";
  }

  static {
    irregularSingulars = new Hashtable();
    irregularSingulars.put("ache", "Aches");
    irregularSingulars.put("alumna", "Alumnae");
    irregularSingulars.put("alumnus", "Alumni");
    irregularSingulars.put("axis", "Axes");
    irregularSingulars.put("bison", "Bison");
    irregularSingulars.put("bus", "Buses");
    irregularSingulars.put("calf", "Calves");
    irregularSingulars.put("caribou", "Caribou");
    irregularSingulars.put("child", "Children");
    irregularSingulars.put("datum", "Data");
    irregularSingulars.put("deer", "Deer");
    irregularSingulars.put("die", "Dice");
    irregularSingulars.put("elf", "Elves");
    irregularSingulars.put("elk", "Elk");
    irregularSingulars.put("fish", "Fish");
    irregularSingulars.put("foot", "Feet");
    irregularSingulars.put("gentleman", "Gentlemen");
    irregularSingulars.put("gentlewoman", "Gentlewomen");
    irregularSingulars.put("go", "Goes");
    irregularSingulars.put("goose", "Geese");
    irregularSingulars.put("grouse", "Grouse");
    irregularSingulars.put("half", "Halves");
    irregularSingulars.put("hoof", "Hooves");
    irregularSingulars.put("knife", "Knives");
    irregularSingulars.put("leaf", "Leaves");
    irregularSingulars.put("life", "Lives");
    irregularSingulars.put("loaf", "Loaves");
    irregularSingulars.put("louse", "Lice");
    irregularSingulars.put("man", "Men");
    irregularSingulars.put("money", "Monies");
    irregularSingulars.put("moose", "Moose");
    irregularSingulars.put("mouse", "Mice");
    irregularSingulars.put("octopus", "Octopi");
    irregularSingulars.put("ox", "Oxen");
    irregularSingulars.put("plus", "Pluses");
    irregularSingulars.put("quail", "Quail");
    irregularSingulars.put("reindeer", "Reindeer");
    irregularSingulars.put("scarf", "Scarves");
    irregularSingulars.put("self", "Selves");
    irregularSingulars.put("sheaf", "Sheaves");
    irregularSingulars.put("sheep", "Sheep");
    irregularSingulars.put("shelf", "Shelves");
    irregularSingulars.put("squid", "Squid");
    irregularSingulars.put("thief", "Thieves");
    irregularSingulars.put("tooth", "Teeth");
    irregularSingulars.put("wharf", "Wharves");
    irregularSingulars.put("wife", "Wives");
    irregularSingulars.put("wolf", "Wolves");
    irregularSingulars.put("woman", "Women");

  }
}