package categories;

import java.util.HashMap;

import modelControl.OpcatMCDirEntry;


public class Categories {

	/**
	 * @return a Map of <ID,Name> of categories for type.
	 * 
	 */
	public static HashMap<Integer, String> getNames(CategoriesType type) {

		HashMap<Integer, String> cat = new HashMap<Integer, String>();

		cat.put(1, "Owner");
		cat.put(2, "System");
		cat.put(3, "Importence");
		return cat;
	}

	public Object getValue(int catID, int entityID) {
		return "Value";
	}

	/**
	 * get catagories for repository file 
	 * 
	 * @return HashMap<CatgoryName,CategoryValue>
	 */
	public static HashMap<String, String> getCategoriesNameValue(OpcatMCDirEntry entry) {
		return null;
	}

}
