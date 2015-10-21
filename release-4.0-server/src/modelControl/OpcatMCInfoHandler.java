package modelControl;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNInfoHandler;
import org.tmatesoft.svn.core.wc.SVNInfo;

public class OpcatMCInfoHandler implements ISVNInfoHandler{

	private SVNInfo myInfo ;  
	@Override
	public void handleInfo(SVNInfo info) throws SVNException {
		myInfo = info ; 
		
	}
	public SVNInfo getInfo() {
		return myInfo;
	}

}
