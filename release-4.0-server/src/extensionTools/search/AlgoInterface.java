package extensionTools.search;

import exportedAPI.opcatAPIx.IXSystem;
import gui.projectStructure.Entry;

import java.util.Vector;

/**
 * the interface any search algorithem must implement. the Vector output is
 * filled with the found <b>IXEntries</b>.
 * 
 * @see AlgoInString
 * @see AlgoFactory
 * 
 * @author raanan
 * 
 */
public interface AlgoInterface {
	public Vector PreformSearch(Entry parent, IXSystem opcat);
}
