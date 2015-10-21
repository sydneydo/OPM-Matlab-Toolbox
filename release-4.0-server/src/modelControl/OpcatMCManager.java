package modelControl;

import expose.OpcatExposeKey;
import expose.OpcatExposeManager;
import gui.controls.FileControl;
import gui.opdProject.OpdProject;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.util.OpcatFile;
import gui.util.OpcatFileNameFilter;
import gui.util.OpcatLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Timer;

import javax.swing.tree.DefaultMutableTreeNode;

import messages.OpcatMessage;
import messages.OpcatMessagesManager;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_OP;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SEVIRITY;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SUBSYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_TYPE;
import modelControl.gui.OpcatSvnGridReporter;

import org.tmatesoft.svn.core.ISVNDirEntryHandler;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLock;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.util.SVNPathUtil;
import org.tmatesoft.svn.core.internal.util.SVNURLUtil;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.internal.wc.SVNPath;
import org.tmatesoft.svn.core.io.SVNFileRevision;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitPacket;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNPropertyData;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import user.OpcatUser;
import user.OpcatUser.OpcatUserStatus;
import util.Configuration;

/**
 * a class to handle SVN connectivity. this class can handle server side
 * invocation via RMI or client side invocation. it holds a SVNURL for it's
 * Repository. the repository location will be read from the properties file.
 * 
 * @author raanan
 * 
 */
public class OpcatMCManager implements ISVNEventHandler {

	private final String gridArraySeperator = "$";

	public static final String WORKING_COPY = Configuration.getInstance()
			.getProperty("models_directory");

	public static final String server = Configuration.getInstance()
			.getProperty("MCserver");

	private static final String svnURL = Configuration.getInstance()
			.getProperty("MCaccesstype")
			+ "://" + Configuration.getInstance().getProperty("MCserver");

	private static final String repo = svnURL + "/"
			+ Configuration.getInstance().getProperty("MCrepository");

	public static String getSvnURL() {
		return svnURL;
	}

	private static String TEMPLATES_RELATIVE_PATH = Configuration.getInstance()
			.getProperty("MCTemplates");

	public static String TEMPLATES_DIR = WORKING_COPY
			+ FileControl.fileSeparator
			+ Configuration.getInstance().getProperty("MCTemplates");

	private static final ArrayList<String> ignore = new ArrayList<String>();

	private static SVNURL tgtURL = null;
	private static SVNClientManager manager = null;
	private static SVNRepository repository = null;
	private static OpcatMCManager mySvn = null;

	private ArrayList<String> fileMsg = new ArrayList<String>();
	private ArrayList<String> adminMsg = new ArrayList<String>();

	private HashMap<File, Enum> processed = new HashMap<File, Enum>();

	private OpcatSvnGridReporter fileGrid;
	private OpcatSvnGridReporter adminGrid;

	private static boolean online = false;

	private static boolean connectedToerver = false;

	private static Timer onlineTimer = new Timer();

	private boolean isIgnored(String name) {
		return ignore.contains(name);
	}

	public OpcatSvnGridReporter getAdminGrid() {
		return adminGrid;
	}

	public OpcatSvnGridReporter getFileGrid() {
		return fileGrid;
	}

	/**
	 * sets the on-line status of the {@link OpcatMCManager} this method is used
	 * by the <code>onlineTimer</code> which checks the online status every 10
	 * seconds until on-line status is <code>true</code> after on-line it
	 * decreases the checks to every 1000 seconds
	 * 
	 * @param onlineNew
	 */
	protected static void setOnline(boolean onlineNew) {
		if (onlineNew != online) {
			OpcatMCManager.online = onlineNew;
			setTimers();
		}
	}

	/**
	 * sets the onlineTimer. first checks the on-line status, and then sets the
	 * timer.
	 */
	private static void setTimers() {

		if (onlineTimer != null) {
			onlineTimer.cancel();
			onlineTimer = null;
		}

		// onlineTimer = new Timer();
		OpcatMCTimerOnlineTask onlineTask = new OpcatMCTimerOnlineTask();
		onlineTask.run();
		// if (!online) {
		// onlineTimer.schedule(onlineTask, 10000, 100000);
		// } else {
		// onlineTimer.schedule(onlineTask, 1000000, 1000000);
		// }
		//
		if (connectedToerver) {
			// if (onlineTimer != null) {
			// onlineTimer.cancel();
			// onlineTimer = new Timer();
			// }
			OpcatMCTimerAdminTask messagesTask = new OpcatMCTimerAdminTask();
			messagesTask.run();
			// onlineTimer.schedule(messagesTask, 10000, 1000000);
		}

	}

	private void init() {
		initMessages();
		tgtURL = setRepo();
		setTimers();
	}

	public static File getOpcatMCTemporeryDirectory() throws IOException {
		File ret = File.createTempFile("opcat-", ".tmp");
		ret.delete();
		File opcat = new File(ret.getParentFile().getPath()
				+ FileControl.fileSeparator + "opcat.tmp");
		opcat.mkdirs();
		return opcat;
	}

	/**
	 * returns MC URL of the given file. If the file is not in MC then
	 * <code>null</code> is returned.
	 * 
	 * @param file
	 * @return
	 */
	public SVNURL getFileURL(File file) {
		SVNURL url = null;

		OpcatMCDirEntry entry = getEntry(file, false);
		if (entry != null) {
			url = entry.getURL();
		} else {

			OpcatMCInfoHandler handler = new OpcatMCInfoHandler();
			try {
				manager.getWCClient().doInfo(file, SVNRevision.WORKING,
						SVNRevision.WORKING, SVNDepth.EMPTY, null, handler);
			} catch (SVNException e) {
				// TODO Auto-generated catch block
				handleException(e, "Get File URL", file.getName());
			}
			if (handler.getInfo() != null) {
				return handler.getInfo().getURL();
			} else {
				return null;
			}
		}
		return url;
	}

	protected void updateAdmin() {

		try {
			if (!connectedToerver) {
				return;
			}

			// refresh templates
			File file = new File(TEMPLATES_DIR);

			if (!file.exists()) {
				doCheckOut(TEMPLATES_RELATIVE_PATH, SVNRevision.HEAD, false,
						file, SVNDepth.INFINITY);
			} else {
				Cleanup(file);
				doUpdate(file);
			}

			// // refrash metalibs of currentt model
			// OpdProject project =
			// FileControl.getInstance().getCurrentProject();
			//
			// if (project != null) {
			// Enumeration<Entry> entries = project.getSystemStructure()
			// .getElements();
			// while (entries.hasMoreElements()) {
			// Entry entry = entries.nextElement();
			// Enumeration<Instance> instances = entry.getInstances();
			// while (instances.hasMoreElements()) {
			// Instance instance = instances.nextElement();
			// if (instance.isPointerInstance()) {
			// OpcatExposeKey key = instance.getPointer();
			// if (!key.isPrivate())
			// OpcatExposeManager.getModelIntoOpcatTemp(key
			// .getModelEncodedURI());
			// }
			// }
			// }
			// project.getMetaManager().refresh(project, null);
			// }

		} catch (Exception ex) {
			handleException(ex, "Admin Update", null);
		}
	}

	/**
	 * create connected repo
	 */
	private OpcatMCManager() {
		init();
		if (isOnline()) {
			forceConnection();
		}

	}

	public static boolean isConnectedToerver() {
		return connectedToerver;
	}

	/**
	 * create disconected repo
	 * 
	 * @param local
	 */
	private OpcatMCManager(boolean isLocal) {
		init();
		if (!isLocal && !connectedToerver) {
			if (isOnline()) {
				forceConnection();
			}
		}
	}

	public String getRepositoryHostName() {
		return tgtURL.getHost();
	}

	public static String getFileGridName() {
		return "Repository File Console";
	}

	public static String getAdminGridName() {
		return "Repository Admin Console";
	}

	private void initMessages() {
		fileGrid = new OpcatSvnGridReporter(true, gridArraySeperator,
				"Repository File Console");
		fileGrid.show(false);

		adminGrid = new OpcatSvnGridReporter(true, gridArraySeperator,
				"Repository Admin Console");
		adminGrid.show(false);

		fileMsg.clear();
		adminMsg.clear();

		fileGrid.init("Number" + gridArraySeperator + "Type"
				+ gridArraySeperator + "Name" + gridArraySeperator + "Message"
				+ gridArraySeperator + "Author" + gridArraySeperator
				+ "Revision");

		adminGrid.init("Number" + gridArraySeperator + "Transaction Type"
				+ gridArraySeperator + "Message Type" + gridArraySeperator
				+ "Message" + gridArraySeperator + "Information");

	}

	/**
	 * returns the {@link OpcatMCManager} forcing it to connect
	 * 
	 * @return
	 */
	public static OpcatMCManager getInstance() {
		if (mySvn == null) {
			if (isOnline() && !isConnectedToerver()) {
				mySvn = new OpcatMCManager();
			}
		}

		if (!ignore.contains(TEMPLATES_RELATIVE_PATH)) {
			ignore.add(TEMPLATES_RELATIVE_PATH);
		}
		if (!ignore.contains(TEMPLATES_DIR)) {
			ignore.add(TEMPLATES_DIR);
		}

		return mySvn;
	}

	/**
	 * force connection of {@link OpcatMCManager} to the server no on-line
	 * checking is done.
	 * 
	 * @return
	 */
	public boolean forceConnection() {

		if (connectedToerver) {
			return true;
		}

		if (!connectedToerver) {
			connectedToerver = connectToRepo();
		}

		if (connectedToerver)
			setTimers();

		return connectedToerver;
	}

	/**
	 * returns all the unlocked entries
	 * 
	 * @param file
	 * @param commitMsg
	 * @return
	 */
	public LinkedList<OpcatMCDirEntry> doUnLock(File file, String commitMsg) {
		LinkedList<OpcatMCDirEntry> unlocked = new LinkedList<OpcatMCDirEntry>();

		try {

			if (!forceConnection()) {
				return null;
			}
			LinkedList<OpcatMCDirEntry> entries = getWCEntries(file);

			for (OpcatMCDirEntry entry : entries) {
				if (entry.getKind() == SVNNodeKind.FILE) {

					if (isIgnored(entry.getName())) {
						break;
					}

					SVNLock lock = getRemoteLock(entry);

					if (lock != null
							&& (lock.getOwner() != null)
							&& lock.getOwner().equalsIgnoreCase(
									OpcatUser.getCurrentUser().getName())) {
						/**
						 * i can break lock here as i am the lock owner
						 */
						manager.getWCClient().doUnlock(
								new SVNURL[] { entry.getURL() }, true);
						unlocked.add(entry);

					} else {

						String msg = "Can not unlock, "
								+ (lock == null ? "File not locked"
										: (lock.getOwner() == null ? "could not get owner of lock"
												: lock.getOwner()
														+ " is owner of lock"));

						addFileMsg(msg, "Unlock", entry);

					}
				}
			}

		} catch (Exception ex) {
			handleException(ex, "Unlock", file.getName());
		}

		return unlocked;
	}

	public void doUpdate(File file) {
		try {
			if (!forceConnection()) {
				return;
			}
			if (isConnectedToerver()) {

				((DefaultSVNOptions) manager.getDiffClient().getOptions())
						.setConflictHandler(new OpcatMCConflictHandler());

				manager.getUpdateClient().doUpdate(file, SVNRevision.HEAD,
						SVNDepth.INFINITY, false, true);

			}
		} catch (Exception ex) {
			handleException(ex, "Update", file.getName());
		}

	}

	public void doRevert(File file) {
		try {
			if (!forceConnection()) {
				return;
			}

			LinkedList<OpcatMCDirEntry> unlocked = doUnLock(file,
					"Unlocking before revert");

			manager.getWCClient().doRevert(new File[] { file },
					SVNDepth.INFINITY, null);

			doLock(unlocked, "Locking reverted file");

		} catch (Exception ex) {
			handleException(ex, "Revert", file.getName());
		}

	}

	public static boolean authenticate(String name, String password)
			throws SVNException {

		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(name, password);
		repository.setAuthenticationManager(authManager);

		tgtURL = repository.getRepositoryRoot(true);

		return true;

	}

	public void doLocalDelete(File file, boolean force) {
		try {

			SVNLock lock = getRemoteLock(file);
			if (lock == null) {

				manager.getWCClient().doDelete(file, force, false, false);

			} else if (lock.getOwner().equalsIgnoreCase(
					OpcatUser.getCurrentUser().getName())) {

				manager.getWCClient().doUnlock(new File[] { file }, true);

				manager.getWCClient().doDelete(file, force, false, false);

			} else {
				addFileMsg("Could not delete file, "
						+ file.getName()
						+ (lock.getOwner() == null ? "owner is empty" : lock
								.getOwner()
								+ " is owner of lock"), "Delete", file
						.getName(), SVNRevision.WORKING.getNumber(),
						"Delete file");
			}

		} catch (Exception ex) {
			handleException(ex, "Remote Delete", file.getName());
		}

	}

	public void doRemoteDelete(OpcatMCDirEntry entry, String msg) {
		try {
			if (entry.getKind() == SVNNodeKind.DIR) {
				DefaultMutableTreeNode node = getOneLevelRepositoryTree(entry,
						null);
				if (node.getChildCount() > 0) {
					addFileMsg("Could not delete, Directory not empty",
							"Delete", entry);
					return;
				}
			}

			SVNLock lock = getRemoteLock(entry);
			if (lock == null) {

				manager.getCommitClient().doDelete(
						new SVNURL[] { entry.getURL() }, msg, null);

			} else if (lock.getOwner().equalsIgnoreCase(
					OpcatUser.getCurrentUser().getName())) {

				manager.getWCClient().doUnlock(new SVNURL[] { entry.getURL() },
						true);

				manager.getCommitClient().doDelete(
						new SVNURL[] { entry.getURL() }, msg);

			} else {
				addFileMsg("Could not delete file, "
						+ (lock.getOwner() == null ? "owner is empty" : lock
								.getOwner()
								+ " is owner of lock"), "Lock", entry);
			}

		} catch (Exception ex) {
			handleException(ex, "Remote Delete", entry.getName());
		}
	}

	public void Cleanup(File file) {
		try {
			manager.getWCClient().doCleanup(file, true);
		} catch (Exception ex) {
			handleException(ex, "Cleanupf", file.getName());
		}

	}

	public static OpcatMCManager getInstance(boolean localAccessOnly) {
		if (mySvn == null) {
			mySvn = new OpcatMCManager(localAccessOnly);
		}

		if (!localAccessOnly && !connectedToerver) {
			mySvn = new OpcatMCManager(localAccessOnly);
		}

		return mySvn;
	}

	private SVNURL setRepo() {
		try {
			if (repo.startsWith("http") || repo.startsWith("https")) {
				DAVRepositoryFactory.setup();
			} else if (repo.startsWith("svn") || repo.startsWith("svn+ssh")) {
				SVNRepositoryFactoryImpl.setup();
			}

			tgtURL = SVNURL.parseURIDecoded(repo);
			repository = SVNRepositoryFactory.create(tgtURL);

			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(false);

			// options.setAutoProperty("*.opz", SVNProperty.NEEDS_LOCK +
			// "=true");
			// options.setAutoProperty("*.opx", SVNProperty.NEEDS_LOCK +
			// "=true");

			manager = SVNClientManager.newInstance(options, repository
					.getAuthenticationManager());

			manager.setEventHandler(this);

			return SVNURL.parseURIDecoded(repo);
		} catch (Exception ex) {
			handleException(ex, "Local Access", null);
		}
		return null;

	}

	private void handleException(Exception ex, String name, String filename) {

		handleException(ex, name, filename, null);
	}

	private void handleException(Exception ex, String name, String filename,
			File file) {

		handleException(ex, name, filename, (Object) file);
	}

	private void handleException(Exception ex, String name, String filename,
			Object cause) {
		handleException(ex, name, filename, cause, true);
	}

	private void handleException(Exception ex, String name, String filename,
			Object cause, boolean showMsg) {

		LinkedList<OpcatMCDirEntry> entries = new LinkedList<OpcatMCDirEntry>();

		if (filename != null) {
			try {
				entries = getWCEntries(new File(filename));
			} catch (Exception e) {
			}
		}

		if (ex instanceof SVNAuthenticationException) {
			if (showMsg)
				addAdminMsg("Error" + gridArraySeperator + ex.getMessage()
						+ gridArraySeperator + "Authentication Error", name);
			// OpcatLogger.logError(ex);
			return;
		}

		if (ex instanceof SVNException) {
			SVNException svnEx = (SVNException) ex;

			if (svnEx.getErrorMessage().getErrorCode().getCode() == SVNErrorCode.WC_NOT_DIRECTORY
					.getCode()) {

				if (cause instanceof OpcatFile) {
					OpcatFile file = (OpcatFile) cause;
					if (file.getParentFile().getPath().equalsIgnoreCase(
							WORKING_COPY)) {
						/**
						 * no permision ?
						 */
						// OpcatLogger.logError(ex);
						return;
					}
				}
			}

			SVNErrorMessage error = svnEx.getErrorMessage();
			if (showMsg)
				addAdminMsg("Error" + gridArraySeperator + error.getMessage()
						+ gridArraySeperator + "Error Code "
						+ error.getErrorCode().getCode() + ":"
						+ error.getErrorCode().getDescription(), name);

			if (error.hasChildErrorMessage()) {
				error = error.getChildErrorMessage();
				while (error != null) {

					if (showMsg)
						addAdminMsg("Error" + gridArraySeperator
								+ error.getMessage() + gridArraySeperator
								+ "Error Code "
								+ error.getErrorCode().getCode() + ":"
								+ error.getErrorCode().getDescription(), name);
					error = error.getChildErrorMessage();
				}
			}

			for (OpcatMCDirEntry entry : entries) {
				if (showMsg)
					addFileMsg(svnEx.getErrorMessage().getErrorCode()
							.getDescription(), name, entry);
			}

			// OpcatLogger.logError(ex);
			return;
		}

		if (ex instanceof Exception) {
			if (showMsg)
				addAdminMsg("Error" + gridArraySeperator + ex.getMessage()
						+ gridArraySeperator + "    ", name);

			for (OpcatMCDirEntry entry : entries) {
				if (showMsg)
					addFileMsg(ex.getMessage(), name, entry);
			}
			if (showMsg)
				OpcatLogger.logError(ex);
			return;
		}

	}

	public static boolean isOnline() {
		return online;
	}

	private boolean connectToRepo() {
		try {

			if (connectedToerver) {
				return true;
			}

			DAVRepositoryFactory.setup();

			tgtURL = SVNURL.parseURIDecoded(repo);

			repository = SVNRepositoryFactory.create(tgtURL);

			String name = OpcatUser.getCurrentUser().getName();
			if (OpcatUser.getCurrentUser().getCurrentStatus().currentStatus() != OpcatUserStatus.LOGIN_OK) {
				return false;
			}

			String password = OpcatUser.getCurrentUser().getDecodedPassword();

			if (OpcatUser.getCurrentUser().getCurrentStatus().currentStatus() != OpcatUserStatus.LOGIN_OK) {
				return false;
			}

			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(name, password);
			repository.setAuthenticationManager(authManager);

			repository.getRepositoryRoot(true);

			SVNNodeKind nodeKind = repository.checkPath("", -1);

			if (nodeKind == SVNNodeKind.NONE) {
				OpcatLogger.logError("There is no entry at '" + tgtURL + "'.");
				addAdminMsg("Error" + gridArraySeperator
						+ "Server Root is Empty" + gridArraySeperator
						+ "Connecting", "Connection");
				return false;
			} else if (nodeKind == SVNNodeKind.FILE) {
				OpcatLogger.logError("The entry at '" + tgtURL
						+ "' is a file while a directory was expected.");
				addAdminMsg("Error" + gridArraySeperator
						+ "Server Root is Corrupted" + gridArraySeperator
						+ "while connecting", "Connection");
				return false;
			}

			ISVNOptions options = SVNWCUtil.createDefaultOptions(false);
			manager = SVNClientManager.newInstance(options, repository
					.getAuthenticationManager());
			manager.setEventHandler(this);
			manager.setIgnoreExternals(false);

			return true;

		} catch (Exception ex) {
			handleException(ex, "Init Remote Repository", null);
		}

		return false;

	}

	/**
	 * 
	 * @return HashMap, key = Directories , values = Arraylist of files in the
	 *         director. value = empty arraylist if it's a file in the root
	 *         directory or a directory without files.
	 */
	public DefaultMutableTreeNode getOneLevelRepositoryTree(
			OpcatMCDirEntry startingDirEntry, OpcatFileNameFilter filter) {

		try {
			if (!forceConnection()) {
				return null;
			}
			DefaultMutableTreeNode root;
			if (startingDirEntry == null) {
				root = new DefaultMutableTreeNode(tgtURL);
			} else {
				root = new DefaultMutableTreeNode(startingDirEntry);
			}

			addEntries(root, startingDirEntry, filter);

			return root;

		} catch (Exception ex) {
			handleException(ex, "Get Repository Tree", null);
		}

		return null;

	}

	/**
	 * get all files from root down without the directories
	 * 
	 * @param rootPath
	 *            starting point getFileListFromRepo("") returns all Files in
	 *            repository
	 * @throws SVNException
	 */
	public LinkedList<OpcatMCDirEntry> getFileListFromRepo(String relativePath) {

		try {
			if (!forceConnection()) {
				return null;
			}
			SVNURL root = SVNURL.parseURIDecoded(SVNPathUtil.append(repo,
					relativePath));
			OpcatMCDirEntryHandler handler = new OpcatMCDirEntryHandler(null,
					true);
			manager.getLogClient().doList(root, SVNRevision.HEAD,
					SVNRevision.HEAD, true, SVNDepth.INFINITY,
					SVNDirEntry.DIRENT_ALL, handler);
			return handler.getEntries();
		} catch (Exception ex) {
			handleException(ex, "Get Files List", null);
			return null;
		}
	}

	/**
	 * get all files from root down without the directories
	 * 
	 * @param rootPath
	 *            starting point getFileListFromRepo("") returns all Files in
	 *            repository
	 * @throws SVNException
	 */
	public LinkedList<OpcatMCDirEntry> getFileListFromRepo(SVNURL root) {

		try {
			if (!forceConnection()) {
				return null;
			}
			OpcatMCDirEntryHandler handler = new OpcatMCDirEntryHandler(null,
					true);
			manager.getLogClient().doList(root, SVNRevision.HEAD,
					SVNRevision.HEAD, true, SVNDepth.INFINITY,
					SVNDirEntry.DIRENT_ALL, handler);
			return handler.getEntries();
		} catch (Exception ex) {
			handleException(ex, "Get Files List", null);
			return null;
		}
	}

	private void addEntries(DefaultMutableTreeNode root,
			OpcatMCDirEntry rootSvnEntry, OpcatFileNameFilter filter) {

		try {
			String repositoryRootPath = repository.getRepositoryRoot(true)
					.getPath();

			String currentPath = (rootSvnEntry != null ? rootSvnEntry.getURL()
					.getPath() : repositoryRootPath);

			String path = SVNPathUtil.getRelativePath(repositoryRootPath,
					currentPath);

			path = (path == null ? "/" : "/" + path);

			OpcatMCDirEntryHandler handler = new OpcatMCDirEntryHandler(null);
			repository
					.getDir(path, SVNRevision.HEAD.getNumber(), null, handler);

			LinkedList<OpcatMCDirEntry> entries = handler.getEntries();

			Iterator<OpcatMCDirEntry> iterator = entries.iterator();
			while (iterator.hasNext()) {
				OpcatMCDirEntry entry = iterator.next();
				if (entry.getKind() == SVNNodeKind.DIR) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(
							entry);

					DefaultMutableTreeNode empty = new DefaultMutableTreeNode(
							"emptyopcatnode");
					node.add(empty);

					if (filter.accept(null, entry.getName())) {
						root.add(node);
					}

				} else {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(
							entry);
					if (filter != null) {
						if (filter.accept(null, entry.getName())) {
							root.add(node);
						}
					} else {
						root.add(node);
					}
				}
			}
		} catch (Exception ex) {
			handleException(ex, "Add to Tree", null);
		}

	}

	public SVNURL getRepoURL() {
		return tgtURL;
	}

	public SVNLock getRemoteLock(File file) {
		if (!forceConnection()) {
			return null;
		}
		SVNInfo info = getInfo(file, SVNRevision.HEAD);

		if (info != null) {
			SVNLock lock = info.getLock();
			if (lock == null) {
				return null;
			} else if (lock.getID() == null) {
				return null;
			} else {
				return info.getLock();
			}
		} else {
			return null;
		}
	}

	public SVNLock getRemoteLock(OpcatMCDirEntry entry) throws SVNException {
		if (!forceConnection()) {
			return null;
		}
		SVNInfo info = manager.getWCClient().doInfo(entry.getURL(),
				SVNRevision.HEAD, SVNRevision.HEAD);
		if (info != null) {
			SVNLock lock = info.getLock();
			if (lock == null) {
				return null;
			} else if (lock.getID() == null) {
				return null;
			} else {
				return info.getLock();
			}
		} else {
			return null;
		}
	}

	public SVNProperties getRevisionProperties(SVNRevision revision) {
		try {
			if (!forceConnection()) {
				return null;
			}
			return repository.getRevisionProperties(revision.getNumber(),
					(SVNProperties) null);

		} catch (Exception ex) {
			handleException(ex, "Get Revisions", revision.getName());
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<SVNFileRevision> doLog(SVNURL url, String name) {
		try {
			if (!forceConnection()) {
				return null;
			}
			int startRevision = 0;
			return repository.getFileRevisions(url.getPath(),
					(Collection) null, startRevision, SVNRevision.HEAD
							.getNumber());

		} catch (Exception ex) {
			handleException(ex, "Log", name);
		}

		return null;
	}

	public OpcatMCDirEntry getEntry(File file, boolean showFailMsg) {
		try {
			LinkedList<OpcatMCDirEntry> list = getWCEntries(file,
					SVNDepth.EMPTY);
			return (list.size() == 0 ? null : list.get(0));
		} catch (Exception e) {
			handleException(e, "Get Entry", file.getName(), file, showFailMsg);
			return null;
		}
	}

	public OpcatMCDirEntry getEntry(File file) {
		return getEntry(file, true);
	}

	public SVNStatus getStatus(File wcPath, boolean isRemote)
			throws SVNException {
		if (isRemote) {
			if (!forceConnection()) {
				return null;
			}
		}
		return manager.getStatusClient().doStatus(wcPath, isRemote);
	}

	public File getWCRoot() {
		return new File(WORKING_COPY);
	}

	public void doImport(File file, SVNURL parent, String msg) {

		try {
			if (!forceConnection()) {
				return;
			}
			manager.getCommitClient().doImport(file, parent, msg, null, true,
					true, SVNDepth.INFINITY);
		} catch (Exception ex) {
			handleException(ex, "Import", file.getName());
		}
	}

	private File doCheckOut(OpcatMCDirEntry entry, SVNRevision revision,
			boolean lock, File destination, SVNDepth depth) {
		try {

			if (!forceConnection()) {
				return null;
			}

			manager.getUpdateClient().setIgnoreExternals(false);

			manager.getUpdateClient().doCheckout(entry.getURL(), destination,
					revision, revision, depth, false);

			LinkedList<OpcatMCDirEntry> entries = getWCEntries(destination);

			if (lock) {
				/**
				 * lock the checkedout files
				 */
				doLock(entries, null);
			}

			return destination;

		} catch (Exception ex) {
			handleException(ex, "Checkout", null);
			return null;
		}
	}

	private File doCheckOut(String uriDecodedRelativePath,
			SVNRevision revision, boolean lock, File destination, SVNDepth depth) {
		try {

			if (!forceConnection()) {
				return null;
			}

			SVNURL url = SVNURL.parseURIDecoded(SVNPathUtil.append(repo,
					uriDecodedRelativePath));

			manager.getUpdateClient().setIgnoreExternals(false);

			manager.getUpdateClient().doCheckout(url, destination, revision,
					revision, depth, false);

			// doUpdate(destination);

			LinkedList<OpcatMCDirEntry> entries = getWCEntries(destination);

			if (lock) {
				/**
				 * lock the checkedout files
				 */
				doLock(entries, null);
			}

			return destination;

		} catch (Exception ex) {
			handleException(ex, "Checkout", null);
			return null;
		}
	}

	public File doExport(String encodedRelativePath, SVNRevision revision,
			File destination) {
		try {

			if (!forceConnection()) {
				return null;
			}
			SVNURL url = SVNURL.parseURIEncoded(SVNPathUtil.append(svnURL,
					encodedRelativePath));

			manager.getUpdateClient().setIgnoreExternals(false);

			if (!ignore.contains(destination.getPath())) {
				ignore.add(destination.getPath());
			}

			manager.getUpdateClient().doExport(url, destination, revision,
					revision, null, true, SVNDepth.INFINITY);

			return destination;

		} catch (Exception ex) {
			handleException(ex, "Checkout", null);
			return null;
		}
	}

	public void adminForceUnlock(SVNURL url) {
		try {
			OpcatMCDirEntryHandler handler = new OpcatMCDirEntryHandler(null,
					true);

			manager.getLogClient().doList(url, SVNRevision.HEAD,
					SVNRevision.HEAD, false, true, handler);

			for (OpcatMCDirEntry entry : handler.getEntries()) {
				manager.getWCClient().doUnlock(new SVNURL[] { entry.getURL() },
						true);
			}
		} catch (SVNException e) {
			OpcatLogger.logError(e);
		}
	}

	public void adminAddNeedsLockProperty(SVNURL url) {
		try {
			OpcatMCDirEntryHandler handler = new OpcatMCDirEntryHandler(null,
					true);

			manager.getLogClient().doList(url, SVNRevision.HEAD,
					SVNRevision.HEAD, false, true, handler);

			for (OpcatMCDirEntry entry : handler.getEntries()) {
				manager.getWCClient().doSetProperty(entry.getURL(),
						SVNProperty.NEEDS_LOCK,
						SVNPropertyValue.create("true"), SVNRevision.HEAD,
						"set needs-lock on all", null, true, null);
			}
		} catch (SVNException e) {
			OpcatLogger.logError(e);
		}
	}

	public void adminRemoveNeedsLockProperty(SVNURL url) {
		try {
			OpcatMCDirEntryHandler handler = new OpcatMCDirEntryHandler(null,
					true);

			manager.getLogClient().doList(url, SVNRevision.HEAD,
					SVNRevision.HEAD, false, true, handler);

			for (OpcatMCDirEntry entry : handler.getEntries()) {
				manager.getWCClient().doSetProperty(entry.getURL(),
						SVNProperty.NEEDS_LOCK, null, SVNRevision.HEAD,
						"remove needs-lock on all", null, true, null);
			}
		} catch (SVNException e) {
			OpcatLogger.logError(e);
		}
	}

	/**
	 * check out entry, returns the file/directory of the check-out file/s
	 * 
	 * @param entry
	 * @param revision
	 * @param lock
	 * @return
	 */
	public File doCheckOut(OpcatMCDirEntry entry, SVNRevision revision,
			boolean lock) {
		try {
			/**
			 * use the root models directory as svn directory checking out only
			 * the root with-out any data
			 */

			SVNDepth depth = SVNDepth.INFINITY;

			SVNPath dst = new SVNPath(WORKING_COPY);

			SVNURL url = entry.getURL();

			String relPath = entry.getName();

			boolean locLock = lock;

			if (entry.getKind() == SVNNodeKind.FILE) {
				url = entry.getURL().removePathTail();
				relPath = SVNPathUtil.tail(url.getPath());
				depth = SVNDepth.EMPTY;
				locLock = false;
			}

			dst = new SVNPath(SVNPathUtil.append(WORKING_COPY, relPath), true);

			SVNInfo info = manager.getWCClient()
					.doInfo(url, revision, revision);

			// if (info.getDepth().getId() != depth.getId()) {
			// // manager.getWCClient().do
			// }
			//
			// File ret = null;
			// if ((depth.getId() == SVNDepth.EMPTY.getId())
			// && (new File(dst.getFile().getPath()).exists())) {
			//
			// } else {
			File ret = doCheckOut(SVNPathUtil.removeHead(SVNPathUtil
					.removeHead(url.getPath())), revision, locLock, dst
					.getFile(), depth);
			// }

			if (entry.getKind() == SVNNodeKind.FILE) {

				File file = new File(dst.getFile().getPath()
						+ FileControl.fileSeparator + entry.getName());

				manager.getUpdateClient().doUpdate(file, SVNRevision.HEAD,
						info.getDepth(), false, true);

				doLock(file, "Auto locking " + file.getPath());
			}

			return ret;

		} catch (Exception ex) {
			handleException(ex, "Checkout", null);
			return null;
		}
	}

	public void doCheckOut(OpcatMCDirEntry entry, SVNRevision revision) {
		doCheckOut(entry, revision, true);
	}

	public static boolean deleteDirectory(File path) {
		if (path.isDirectory()) {
			if (path.exists()) {
				File[] files = path.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (path.delete());
	}

	public SVNLock getEntryLock(OpcatMCDirEntry entry) {
		try {
			if (entry.getKind() == SVNNodeKind.FILE) {
				String myPath = SVNPathUtil.getRelativePath(entry
						.getRepositoryRoot().getPath(), entry.getURL()
						.getPath());
				if (!forceConnection()) {
					return null;
				}
				return repository.getLock(myPath);
			}
		} catch (Exception ex) {
			handleException(ex, "Get Entry Lock", entry.getName());
		}
		return null;
	}

	public LinkedList<OpcatMCDirEntry> getFileListFromWorkingCopy(File root,
			SVNRevision revision) {

		LinkedList<OpcatMCDirEntry> ret = new LinkedList<OpcatMCDirEntry>();

		try {
			OpcatMCDirEntryHandler handler = new OpcatMCDirEntryHandler(null,
					true);
			manager.getLogClient().doList(root, revision, revision, true,
					SVNDepth.INFINITY, SVNDirEntry.DIRENT_ALL, handler);

			return handler.getEntries();
		} catch (Exception ex) {
			handleException(ex, "Get Files List", null);
			return ret;
		}

	}

	public void setRepositoryProperty(OpcatMCDirEntry entry, String property,
			SVNPropertyValue value) {
		try {
			if (!forceConnection()) {
				return;
			}
			OpcatMCPropertyHandler myPropertyHandler = new OpcatMCPropertyHandler();

			SVNPropertyData data = manager.getWCClient().doGetProperty(
					entry.getURL(), property.toString(), SVNRevision.HEAD,
					SVNRevision.HEAD);
			if ((data == null) || (data.getValue() == null)) {

				manager.getWCClient().doSetProperty(entry.getURL(),
						property.toString(), value, SVNRevision.HEAD,
						"Auto Property", null, false, myPropertyHandler);

			}
		} catch (Exception ex) {
			handleException(ex, "Creating Property " + property, entry
					.getName());
		}
	}

	public void setLocalProperty(File file, String property,
			SVNPropertyValue value) {
		try {
			OpcatMCPropertyHandler myPropertyHandler = new OpcatMCPropertyHandler();
			manager.getWCClient().doSetProperty(file, property.toString(),
					value, false, SVNDepth.EMPTY, myPropertyHandler,
					(Collection) null);
		} catch (Exception ex) {
			handleException(ex, "Creating Property " + property, file.getName());
		}
	}

	public void doLock(File file, String commitMsg) {
		try {

			LinkedList<OpcatMCDirEntry> entries = getWCEntries(file);
			doLock(entries, commitMsg);

		} catch (Exception ex) {
			handleException(ex, "Lock", file.getName());
		}
	}

	public void doGetServerHistory() {
		// manager.getAdminClient().do
	}

	/**
	 * returns the locked entries
	 * 
	 * @param fileEntries
	 * @param msg
	 * @return
	 */
	private LinkedList<OpcatMCDirEntry> doLock(
			LinkedList<OpcatMCDirEntry> fileEntries, String msg) {

		LinkedList<OpcatMCDirEntry> locked = new LinkedList<OpcatMCDirEntry>();
		try {
			if (!forceConnection()) {
				return null;
			}
			for (OpcatMCDirEntry entry : fileEntries) {
				if (entry.getKind() == SVNNodeKind.FILE) {

					if (isIgnored(entry.getName())) {
						break;
					}

					SVNLock lock = getRemoteLock(entry);

					if ((lock != null)
							&& (lock.getOwner() != null)
							&& !(lock.getOwner().equalsIgnoreCase(OpcatUser
									.getCurrentUser().getName()))) {
						addFileMsg(
								"Could not lock file, "
										+ (lock.getOwner() == null ? "owner is empty"
												: lock.getOwner()
														+ " is owner of lock"),
								"Lock", entry);

					} else {
						manager.getWCClient().doLock(
								new SVNURL[] { entry.getURL() }, true,
								(msg == null ? "Locked By Opcat" : msg));
						locked.add(entry);

					}

				}
			}
		} catch (Exception ex) {
			handleException(ex, "Lock", null);
		}
		return locked;
	}

	public SVNInfo getInfo(File file, SVNRevision revision) {
		try {
			return manager.getWCClient().doInfo(file, revision);
		} catch (Exception e) {
			handleException(e, "Local Info", file.getName(), file);
		}

		return null;

	}

	public SVNInfo getInfo(OpcatMCDirEntry entry, SVNRevision revision) {
		try {
			return manager.getWCClient().doInfo(entry.getURL(), revision,
					revision);
		} catch (Exception e) {
			handleException(e, "Local Info", entry.getName(), null);
		}

		return null;

	}

	public SVNStatus getStatus(OpcatFile file, boolean allowRemote) {
		SVNStatus status = null;
		try {

			status = manager.getStatusClient().doStatus(file, allowRemote);

		} catch (Exception ex) {
			handleException(ex, "Log", file.getName(), file);
		}
		return status;

	}

	public void addEntry(File wcPath) {
		try {

			manager.getWCClient().doAdd(wcPath, true, false, true,
					SVNDepth.INFINITY, false, true);

		} catch (Exception ex) {
			handleException(ex, "Add", wcPath.getName());
		}
	}

	public SVNURL getSVNURL(File file) {
		try {
			SVNInfo info = getInfo(file, SVNRevision.WORKING);
			if (info == null) {
				SVNPath path = new SVNPath(file.getPath(), true);
				return path.getURL();
			}
			return info.getURL();
		} catch (Exception ex) {
			handleException(ex, "Get URL", file.getName());
		}
		return null;

	}

	public void doMakeDirectory(SVNURL parent, String newName) {
		try {
			if (!forceConnection()) {
				return;
			}
			SVNURL url = parent.appendPath(newName, false);

			manager.getCommitClient().doMkDir(
					new SVNURL[] { url },
					OpcatUser.getCurrentUser().getName() + " : Created "
							+ url.getPath());

			// send creation message here.

		} catch (Exception ex) {
			handleException(ex, "Make Remote Directory", parent.getPath());
		}
	}

	private LinkedList<OpcatMCDirEntry> getWCEntries(File file)
			throws SVNException {
		return getWCEntries(file, SVNDepth.INFINITY);
	}

	private LinkedList<OpcatMCDirEntry> getWCEntries(File file, SVNDepth depth)
			throws SVNException {

		OpcatMCDirEntryHandler handler = new OpcatMCDirEntryHandler(null);
		manager.getLogClient().setIgnoreExternals(false);

		manager.getLogClient().doList(file, SVNRevision.WORKING,
				SVNRevision.WORKING, true, depth, SVNDirEntry.DIRENT_ALL,
				handler);

		return handler.getEntries();
	}

	/**
	 * commit file into MC. works only on a single file.
	 * 
	 * @param file
	 *            - file to be commited
	 * @param msg
	 *            - commit message
	 * @param removeLock
	 *            - remove lock (if-exists) after commit.
	 * @param deleteFiles
	 *            - not used
	 * @return
	 */
	public boolean doCommit(OpcatFile file, String msg, boolean removeLock,
			boolean deleteFiles) {

		boolean ret = false;

		File[] files = new File[1];
		files[0] = file;

		try {

			if (!forceConnection()) {
				return false;
			}

			if (!isConnectedToerver()) {
				return false;
			}

			LinkedList<OpcatMCDirEntry> unlocked = doUnLock(file,
					"Auto unlock refresh");
			/**
			 * collect the commit packs in order to revise in a future version
			 * 
			 */
			String[] changelists = null;
			SVNCommitPacket packet = manager.getCommitClient()
					.doCollectCommitItems(new File[] { file }, true, true,
							SVNDepth.INFINITY, changelists);

			if (packet.getCommitItems().length == 0) {
				addAdminMsg("Information" + gridArraySeperator
						+ "Nothing to commit" + gridArraySeperator
						+ "Commit packet is empty   ", "Commit");
				return false;
			}

			SVNCommitInfo info = manager.getCommitClient().doCommit(packet,
					removeLock, msg);

			/**
			 * add the locks if needed
			 */
			if (!removeLock) {
				doLock(unlocked, "Auto lock refresh");
			}

			if (info.getNewRevision() < 0) {
				SVNErrorMessage error = info.getErrorMessage();
				while (error != null) {
					addAdminMsg("Error" + gridArraySeperator
							+ error.getMessage() + gridArraySeperator
							+ "Fail code " + error.getErrorCode(), "Commit");
					error = error.getChildErrorMessage();
				}
				ret = false;
			} else {
				ret = true;
			}
		} catch (Exception ex) {
			if (!removeLock) {
				doLock(file, "Recovery lock after Commit");
			}
			handleException(ex, "Commit", file.getName());
			ret = false;
		}

		return ret;

	}

	private void addFileMsg(String msg, String type, OpcatMCDirEntry entry) {
		msg = msg.replaceAll("svn", "OPCAT MC");
		msg = entry.getName() + gridArraySeperator + msg + gridArraySeperator
				+ entry.getAuthor() + gridArraySeperator + entry.getRevision();

		fileMsg.add(msg);
		fileGrid.addRow(fileMsg.size() + gridArraySeperator + type
				+ gridArraySeperator + msg);
	}

	private void addFileMsg(String msg, String type, String name,
			long revision, String author) {
		msg = msg.replaceAll("svn", "OPCAT MC");
		msg = name + gridArraySeperator + msg + gridArraySeperator + author
				+ gridArraySeperator + revision;

		fileMsg.add(msg);
		fileGrid.addRow(fileMsg.size() + gridArraySeperator + type
				+ gridArraySeperator + msg);
	}

	private void addAdminMsg(String msg, String type) {
		msg = msg.replaceAll("svn", "OPCAT MC");
		adminMsg.add(msg);
		adminGrid.addRow(adminMsg.size() + gridArraySeperator + type
				+ gridArraySeperator + msg);
	}

	@Override
	public void handleEvent(SVNEvent event, double progress)
			throws SVNException {
		/*
		 * Gets the current action. An action is represented by SVNEventAction.
		 * In case of a commit an action can be determined via comparing
		 * SVNEvent.getAction() with SVNEventAction.COMMIT_-like constants.
		 */
		try {
			SVNEventAction action = event.getAction();

			if ((event != null)
					&& (event.getFile() != null)
					&& (isIgnored(event.getFile().getName())
							|| isIgnored(event.getFile().getParentFile()
									.getPath()) || isIgnored(event.getFile()
							.getPath()))) {
				return;
			}

			/**
			 * Commit
			 */

			if (action == SVNEventAction.COMMIT_MODIFIED) {

				addFileMsg("Sending Changed File ", "Commit", event.getFile()
						.getName(), -1, OpcatUser.getCurrentUser().getName());

				processed.put(event.getFile(),
						OPCAT_MESSAGES_OP.FILES_COMMIT_MODIFIED);

			} else if (action == SVNEventAction.COMMIT_COMPLETED) {

				addFileMsg("Commit Completed", "Commit", null, repository
						.getLatestRevision(), OpcatUser.getCurrentUser()
						.getName());

				String message = "Commit Finished";
				if (processed.size() == 0) {
					message = "Commit : Nothing to commit";
				}

				for (File file : processed.keySet()) {
					OPCAT_MESSAGES_OP op = (OPCAT_MESSAGES_OP) processed
							.get(file);
					String subject = file.getName();
					OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.USER;
					OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.COMMIT;
					OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.INFORMATION;
					OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.FILES;
					OpcatMessage opcatMessage = new OpcatMessage(OpcatUser
							.getCurrentUser().getName(), OpcatUser
							.getCurrentUser().getName(), type, type, subsystem,
							subsystem, system, sevi, op, subject, message);
					OpcatMessagesManager.getInstance()
							.sendMessage(opcatMessage);
				}

				processed.clear();

			} else if (action == SVNEventAction.COMMIT_DELETED) {
				addFileMsg("Deleting", "Commit", event.getFile().getName(), -1,
						OpcatUser.getCurrentUser().getName());

				processed.put(event.getFile(),
						OPCAT_MESSAGES_OP.FILES_COMMIT_DELETED);

			} else if (action == SVNEventAction.COMMIT_REPLACED) {
				addFileMsg("Replecing", "Commit", event.getFile().getName(),
						-1, OpcatUser.getCurrentUser().getName());

				processed.put(event.getFile(),
						OPCAT_MESSAGES_OP.FILES_COMMIT_REPLACED);

			} else if (action == SVNEventAction.COMMIT_DELTA_SENT) {
				addFileMsg("Transmitting file data...", "Commit", event
						.getFile().getName(), -1, OpcatUser.getCurrentUser()
						.getName());

			} else if (action == SVNEventAction.COMMIT_ADDED) {

				/*
				 * Gets the MIME-type of the item.
				 */
				String mimeType = event.getMimeType();
				if (SVNProperty.isBinaryMimeType(mimeType)) {
					addFileMsg("Adding  (bin)", "Commit", event.getFile()
							.getName(), -1, OpcatUser.getCurrentUser()
							.getName());
				} else {
					addFileMsg("Adding", "Commit", event.getFile().getName(),
							-1, OpcatUser.getCurrentUser().getName());
				}

				processed.put(event.getFile(),
						OPCAT_MESSAGES_OP.FILES_COMMIT_ADDED);

				/**
				 * UPDATE
				 */
			} else if (action == SVNEventAction.UPDATE_NONE) {
				// processed.put(event.getFile(), "Modified Children");
				// addFileMsg("Modified Children", "Update",
				// event.getFile().getName(), -1, OpcatUser.getCurrentUser()
				// .getName());

			} else if (action == SVNEventAction.UPDATE_ADD) {

				processed.put(event.getFile(),
						OPCAT_MESSAGES_OP.FILES_UPDATE_ADD);

				addFileMsg("Adding", "Update", event.getFile().getName(), -1,
						OpcatUser.getCurrentUser().getName());
				return;
			} else if (action == SVNEventAction.UPDATE_DELETE) {
				addFileMsg("Deleting", "Update", event.getFile().getName(), -1,
						OpcatUser.getCurrentUser().getName());

				processed.put(event.getFile(),
						OPCAT_MESSAGES_OP.FILES_UPDATE_DELETE);
				return;
			} else if (action == SVNEventAction.UPDATE_UPDATE) {

				SVNStatusType contentsStatus = event.getContentsStatus();
				if (contentsStatus == SVNStatusType.CHANGED) {
					addFileMsg("Changed", "Update", event.getFile().getName(),
							-1, OpcatUser.getCurrentUser().getName());

					processed.put(event.getFile(),
							OPCAT_MESSAGES_OP.FILES_UPDATE_UPDATE_CHANGED);
				} else if (contentsStatus == SVNStatusType.CONFLICTED) {
					addFileMsg("CONFLICTED", "Update", event.getFile()
							.getName(), -1, OpcatUser.getCurrentUser()
							.getName());

					processed.put(event.getFile(),
							OPCAT_MESSAGES_OP.FILES_UPDATE_UPDATE_CONFLICTED);
				} else if (contentsStatus == SVNStatusType.MERGED) {
					addFileMsg("MERGED", "Update", event.getFile().getName(),
							-1, OpcatUser.getCurrentUser().getName());

					processed.put(event.getFile(),
							OPCAT_MESSAGES_OP.FILES_UPDATE_UPDATE_MERGED);
				}
			} else if (action == SVNEventAction.UPDATE_EXTERNAL) {
				addFileMsg("UPDATE EXTERNAL", "Update", event.getFile()
						.getName(), -1, OpcatUser.getCurrentUser().getName());

				processed.put(event.getFile(),
						OPCAT_MESSAGES_OP.FILES_UPDATE_EXTERNAL);
				return;
			} else if (action == SVNEventAction.RESTORE) {

				processed.put(event.getFile(), OPCAT_MESSAGES_OP.FILES_RESTORE);
				addFileMsg("Restoring", "Update", event.getFile().getName(),
						-1, OpcatUser.getCurrentUser().getName());

			} else if (action == SVNEventAction.UPDATE_COMPLETED) {

				if (processed.size() == 0) {
					return;
				}

				addFileMsg("Update Completed", "Update", null, repository
						.getLatestRevision(), OpcatUser.getCurrentUser()
						.getName());

				String message = "Update Finished";

				if (processed.size() == 0) {
					message = "Update : Nothing to update";
				}
				for (File file : processed.keySet()) {

					if (file.exists()) {
						SVNLock lock = getRemoteLock(file);
						if ((lock != null)
								&& !lock.getOwner().equalsIgnoreCase(
										OpcatUser.getCurrentUser().getName())) {
							file.setWritable(false);
						} else {
							file.setWritable(true);
						}
					}
					OPCAT_MESSAGES_OP op = (OPCAT_MESSAGES_OP) processed
							.get(file);
					String subject = file.getName();
					OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.USER;
					OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.CHECKOUT;
					OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.INFORMATION;
					OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.FILES;
					OpcatMessage opcatMessage = new OpcatMessage(OpcatUser
							.getCurrentUser().getName(), OpcatUser
							.getCurrentUser().getName(), type, type, subsystem,
							subsystem, system, sevi, op, subject, message);
					OpcatMessagesManager.getInstance()
							.sendMessage(opcatMessage);
				}

				processed.clear();
				return;

				/**
				 * ADD
				 */
			} else if (action == SVNEventAction.ADD) {
				addFileMsg("Added", "Add", event.getFile().getName(), -1,
						OpcatUser.getCurrentUser().getName());

				processed.put(event.getFile(), OPCAT_MESSAGES_OP.FILES_ADD);
				return;
				/**
				 * DELETE
				 */
			} else if (action == SVNEventAction.DELETE) {
				addFileMsg("Deleted", "Delete", event.getFile().getName(), -1,
						OpcatUser.getCurrentUser().getName());

				processed.put(event.getFile(), OPCAT_MESSAGES_OP.FILES_DELETE);
				return;
				/**
				 * LOCK
				 */
			} else if (action == SVNEventAction.LOCKED) {
				addFileMsg("Locked", "Lock", event.getFile().getName(), -1,
						OpcatUser.getCurrentUser().getName());

				processed.put(event.getFile(), OPCAT_MESSAGES_OP.FILES_LOCKED);
				return;
			} else if (action == SVNEventAction.LOCK_FAILED) {
				addFileMsg("Lock Failed", "Lock", event.getFile().getName(),
						-1, OpcatUser.getCurrentUser().getName());
				return;
				/**
				 * UNLOCK
				 */
			} else if (action == SVNEventAction.UNLOCK_FAILED) {
				addFileMsg("Unlock Failed", "Unlock",
						event.getFile().getName(), -1, OpcatUser
								.getCurrentUser().getName());
				return;
			} else if (action == SVNEventAction.UNLOCKED) {
				addFileMsg("Unlocked", "Unlock", event.getFile().getName(), -1,
						OpcatUser.getCurrentUser().getName());
				processed
						.put(event.getFile(), OPCAT_MESSAGES_OP.FILES_UNLOCKED);

				return;
				/**
				 * REVERT
				 */
			} else if (action == SVNEventAction.REVERT) {
				addFileMsg("Reverted", "Revert", event.getFile().getName(), -1,
						OpcatUser.getCurrentUser().getName());
				processed.put(event.getFile(), OPCAT_MESSAGES_OP.FILES_REVERT);
				return;
			} else if (action == null) {
				/**
				 * something failed
				 */
				SVNException e;
				if (event.getErrorMessage() != null) {
					e = new SVNException(event.getErrorMessage());
					handleException(e, "Repository Error", null);
				} else {
					addAdminMsg("Error" + gridArraySeperator + "Unknown error "
							+ gridArraySeperator + "  ", "Repository Handler");
				}
			}
		} catch (Exception ex) {
			handleException(ex, "MC Event Handler", null);
		}

	}

	@Override
	public void checkCancelled() throws SVNCancelException {

	}
}