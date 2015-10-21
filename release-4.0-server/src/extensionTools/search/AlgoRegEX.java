package extensionTools.search;

import java.util.Enumeration;
import java.util.Vector;
import exportedAPI.opcatAPIx.IXEntry;
import exportedAPI.opcatAPIx.IXObjectEntry;
import exportedAPI.opcatAPIx.IXProcessEntry;
import exportedAPI.opcatAPIx.IXStateEntry;
import exportedAPI.opcatAPIx.IXSystem;
import gui.projectStructure.Entry;


/**
 * An In string implemantation of a search algorithem. 
 * 
 * @return Vector which holds the found {@link  IXEntry}. 
 * 
 * @author raanan
 */
public class AlgoRegEX implements AlgoInterface{
	private OptionsInString options ; 
	
	public AlgoRegEX (OptionsInString searchOptions ) {
		this.options = searchOptions ; 
	}
	
	public Vector PreformSearch(Entry parent , IXSystem opcat) {
		Vector out = new Vector(); 
		boolean found = false ; 
		Enumeration  entEnum = opcat.getIXSystemStructure().getElements();
		while(entEnum.hasMoreElements()) {
			IXEntry ent = (IXEntry) entEnum.nextElement() ; 
			found = false ; 
			
			if ( !(ent.getName().replaceAll(this.options.getSearchText().toLowerCase(), "X-X-X-X-X-").equalsIgnoreCase(this.options.getSearchText()))  && this.options.inName) {
				if ((ent instanceof IXProcessEntry) && this.options.forProcess) {
					found = true ; 
				}
				
				if ((ent instanceof IXStateEntry) && this.options.forStates) {
					found = true ;
				}
				
				if ((ent instanceof IXObjectEntry) && this.options.forObjects) {
					found = true ;
				}					
				
			}	
			
			if ( !(ent.getDescription().replaceAll(this.options.getSearchText().toLowerCase(), "X-X-X-X-X-").equalsIgnoreCase(this.options.getSearchText()))  && this.options.inDescription) {
				if ((ent instanceof IXProcessEntry) && this.options.forProcess) {
					found = true ;
				}
				
				if ((ent instanceof IXStateEntry) && this.options.forStates) {
					found = true ;
				}
				
				if ((ent instanceof IXObjectEntry) && this.options.forObjects) {
					found = true ;
				}					
				
			}		
			if (found) {
				out.add(ent) ; 
			}
		}
		return out ; 
	}
	

}
