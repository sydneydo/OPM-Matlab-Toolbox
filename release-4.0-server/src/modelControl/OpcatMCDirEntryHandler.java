package modelControl;

import java.io.File;
import java.util.LinkedList;

import org.tmatesoft.svn.core.ISVNDirEntryHandler;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;

public class OpcatMCDirEntryHandler implements ISVNDirEntryHandler {

	LinkedList<OpcatMCDirEntry> entries = new LinkedList<OpcatMCDirEntry>();
	LinkedList<File> files = new LinkedList<File>();

	boolean onlyFiles = false;
	String owner = null;

	public LinkedList<OpcatMCDirEntry> getEntries() {
		return entries;
	}

	private void initEntries(LinkedList<OpcatMCDirEntry> entries) {
		if (entries == null) {
			this.entries = new LinkedList<OpcatMCDirEntry>();

		} else {
			this.entries = entries;
		}
	}

	public OpcatMCDirEntryHandler(LinkedList<OpcatMCDirEntry> entries) {
		initEntries(entries);
	}

	public OpcatMCDirEntryHandler(LinkedList<OpcatMCDirEntry> entries,
			boolean onlyFiles) {
		initEntries(entries);
		this.onlyFiles = onlyFiles;
	}

	public OpcatMCDirEntryHandler(LinkedList<OpcatMCDirEntry> entries,
			boolean onlyFiles, String filesOwner) {
		initEntries(entries);
		this.onlyFiles = onlyFiles;
		this.owner = filesOwner;
	}

	@Override
	public void handleDirEntry(SVNDirEntry dirEntry) throws SVNException {

		OpcatMCDirEntry entry = new OpcatMCDirEntry(dirEntry.getURL(), dirEntry
				.getRepositoryRoot(), dirEntry.getName(), dirEntry.getKind(),
				dirEntry.getSize(), dirEntry.hasProperties(), dirEntry
						.getRevision(), dirEntry.getDate(), dirEntry
						.getAuthor());

		// String msg = "Fetching data for " + dirEntry.getRelativePath();
		// addFileMsg(msg, "List", entry);
		if (onlyFiles && (dirEntry.getKind() == SVNNodeKind.DIR)) {

		} else {
			entries.add(entry);
		}
	}

}
