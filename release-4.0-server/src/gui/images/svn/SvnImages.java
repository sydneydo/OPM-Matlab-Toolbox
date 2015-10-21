package gui.images.svn;

import javax.swing.ImageIcon;

public class SvnImages {

	private final static String basePath = "/gui/images/svn/";// Images.opmPath;
	// ********** Repository Images ***************

	/**
	 * SVN
	 */
	// Actions
	public final static ImageIcon ACTION_CHECKOUT = loadImage("checkout.gif");
	public final static ImageIcon ACTION_LOCK = loadImage("lock.gif");
	public final static ImageIcon ACTION_UNLOCK = loadImage("unlock.gif");
	public final static ImageIcon ACTION_COMMIT = loadImage("commit.gif");
	public final static ImageIcon ACTION_REVERT = loadImage("revert.gif");
	public final static ImageIcon ACTION_CLEANUP = loadImage("cleanup.gif");
	public final static ImageIcon ACTION_ADD = loadImage("add.gif");
	public final static ImageIcon ACTION_IMPORT = loadImage("import.gif");
	public final static ImageIcon ACTION_ADD_DIRECTORY = loadImage("add-directory.gif");

	public final static ImageIcon ACTION_UPDATE = loadImage("update.gif");
	public final static ImageIcon ACTION_REVISION_EDITOR = loadImage("revisions.gif");
	public final static ImageIcon ACTION_FILE_CONSOLE_VIEW = loadImage("console_view_file.gif");
	public final static ImageIcon ACTION_ADMIN_CONSOLE_VIEW = loadImage("console_view_admin.gif");
	public final static ImageIcon ACTION_MESSAGES_CONSOLE_VIEW = loadImage("console_view_messages.gif");
	
	public final static ImageIcon ACTION_REFRESH = loadImage("refresh.gif");


	public final static ImageIcon ACTION_SHOW_GRID = loadImage("show-grid.gif");

	// Files
	public final static ImageIcon FILE_NORMAL = loadImage("svn-file-normal.png");
	public final static ImageIcon FILE_LOCKED = loadImage("svn-file-locked.png");
	public final static ImageIcon FILE_NEW = loadImage("svn-file-new.png");
	public final static ImageIcon FILE_ADDED = loadImage("svn-file-added.png");
	public final static ImageIcon FILE_CHANGED = loadImage("svn-file-changed.png");
	public final static ImageIcon FILE_LOCKED_BYME = loadImage("svn-file-lockedbyme.png");
	public final static ImageIcon FILE_DELETED = loadImage("svn-file-deleted.png");

	// Folder
	public final static ImageIcon FOLDER_EMPTY = loadImage("svn-folder-empty.png");
	public final static ImageIcon FOLDER_OPEN = loadImage("svn-folder-open.png");
	public final static ImageIcon FOLDER_CLOSE = loadImage("svn-folder-close.png");
	public final static ImageIcon FOLDER_ADDED = loadImage("svn-folder-added.png");
	public final static ImageIcon FOLDER_NEW = loadImage("svn-folder-new.png");
	public final static ImageIcon FOLDER_CHANGED = loadImage("svn-folder-changed.png");
	public final static ImageIcon FOLDER_DELETED = loadImage("svn-folder-deleted.png");

	/**
	 * END SVN
	 */

	protected static ImageIcon loadImage(String file) {
		try {
			return new ImageIcon(SvnImages.class.getResource(basePath + file),
					"test");
		} catch (Exception e) {
			System.err.println("cannot load image " + file);
			return null;
		}
	}

}
