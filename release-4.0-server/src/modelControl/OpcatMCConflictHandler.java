package modelControl;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNConflictHandler;
import org.tmatesoft.svn.core.wc.SVNConflictDescription;
import org.tmatesoft.svn.core.wc.SVNConflictResult;

public class OpcatMCConflictHandler implements ISVNConflictHandler {

	@Override
	public SVNConflictResult handleConflict(
			SVNConflictDescription conflictDescription) throws SVNException {
		// TODO Auto-generated method stub

		// /**
		// * handling messages conflicts
		// */
		// File base = conflictDescription.getMergeFiles().getBaseFile();
		// File messages = OpcatMCManager.getInstance(true).getMessagesFile();
		// if (base.getName().equalsIgnoreCase(messages.getName())) {
		// SVNConflictChoice choice = SVNConflictChoice.MERGED;
		//
		// SVNConflictReason reason = conflictDescription.getConflictReason();
		//
		// if (reason == SVNConflictReason.EDITED) {
		// choice = SVNConflictChoice.MERGED;
		// } else if (reason == SVNConflictReason.UNVERSIONED) {
		// choice = SVNConflictChoice.MERGED;
		// } else if (reason == SVNConflictReason.DELETED) {
		// choice = SVNConflictChoice.THEIRS_FULL;
		// } else if (reason == SVNConflictReason.OBSTRUCTED) {
		// choice = SVNConflictChoice.THEIRS_FULL;
		// }
		//
		// SVNConflictResult ret = new SVNConflictResult(choice,
		// conflictDescription.getMergeFiles().getResultFile());
		// return ret;
		// }
		return null;
	}
}
