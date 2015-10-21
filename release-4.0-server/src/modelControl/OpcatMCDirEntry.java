package modelControl;

import java.util.Date;

import javax.swing.Icon;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;

public class OpcatMCDirEntry extends SVNDirEntry {

	private Icon openIcon = null;
	private Icon closedIcon = null;

	public void setIcon(Icon icon) {
		this.openIcon = icon;
	}

	public Icon getIcon() {
		return openIcon;
	}

	public Icon getOpenIcon() {
		return openIcon;
	}

	public void setOpenIcon(Icon openIcon) {
		this.openIcon = openIcon;
	}

	public Icon getClosedIcon() {
		return closedIcon;
	}

	public void setClosedIcon(Icon closedIcon) {
		this.closedIcon = closedIcon;
	}

	public OpcatMCDirEntry(SVNURL url, SVNURL repositoryRoot, String name,
			SVNNodeKind kind, long size, boolean hasProperties, long revision,
			Date createdDate, String lastAuthor) {
		super(url, repositoryRoot, name, kind, size, hasProperties, revision,
				createdDate, lastAuthor);
	}

	// public String toString() {
	// int i = getName().lastIndexOf(".");
	//
	// return getName().substring(0, i - 1);
	// }

}
