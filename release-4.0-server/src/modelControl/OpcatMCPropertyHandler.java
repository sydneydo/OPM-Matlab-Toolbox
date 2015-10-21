package modelControl;

import java.io.File;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNPropertyHandler;
import org.tmatesoft.svn.core.wc.SVNPropertyData;

public class OpcatMCPropertyHandler implements ISVNPropertyHandler {
	
	private SVNPropertyData data ; 
	private SVNURL url ; 


	public SVNPropertyData getData() {
		return data;
	}

	public SVNURL getUrl() {
		return url;
	}

	@Override
	public void handleProperty(File path, SVNPropertyData property)
			throws SVNException {
		data = property; 
	}

	@Override
	public void handleProperty(SVNURL url, SVNPropertyData property)
			throws SVNException {
		data = property; 
	}

	@Override
	public void handleProperty(long revision, SVNPropertyData property)
			throws SVNException {
		data = property; 
	}

}
