package extensionTools.search;

import javax.swing.JDialog;
import javax.swing.JPanel;
import exportedAPI.OpcatExtensionToolX;
import exportedAPI.opcatAPIx.IXSystem;


public class SearchExtensionTool implements OpcatExtensionToolX {
	static JDialog searchDialog = null ; 
	
	public JPanel execute(IXSystem opcatSystem) {
		searchDialog = SearchDialog.getInstance(opcatSystem) ; 
		searchDialog.setVisible(true) ; 
		return null ;
	}

	public JPanel getAboutBox() {
		return null;
	}

	public String getHelpURL() {
		return null;
	}

	public String getName() {
		return "Search";
	}
    
	
}
