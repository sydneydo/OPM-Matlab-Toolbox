package gui.controls;

import exportedAPI.opcatAPI.ISystem;
import expose.OpcatExposeKey;
import gui.Opcat2;
import gui.actions.edit.MetaColoringAction;
import gui.actions.edit.MetaHideAction;
import gui.actions.expose.OpcatExposeChange;
import gui.images.standard.StandardImages;
import gui.metaLibraries.dialogs.LibrariesLoadingWindow;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.MetaManager;
import gui.opdProject.GenericTable;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opdProject.ProjectPropertiesDialog;
import gui.opdProject.StateMachine;
import gui.opx.LoadException;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.scenarios.Scenario;
import gui.util.CustomFileFilter;
import gui.util.LastFileEntry;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;
import util.Configuration;
import util.FileUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Enable access to file-related operations of Opcat2. The class is a singleton,
 * which means that in order to invoke it and use it's methods, it is sufficient
 * to:
 * <p>
 * <code>
 * FileContorl.getInstance().doSomething();
 * </code><br>
 * 
 * @author Eran Toch
 */
public class FileControl {

	/**
	 * The singleton instance.
	 */
	private static FileControl instance = null;
	/**
	 * A reference to the Opcat instance.
	 */
	private Opcat2 myOpcat = null;
	protected GuiControl myGuiControl = GuiControl.getInstance();
	protected EditControl edit = EditControl.getInstance();
	protected boolean duringFileAction = false;
	public final static String FILE_ACTION_PENDING_MSG = "File Operation in Progress";
	/**
	 * A reference to the current project.
	 */
	private OpdProject currentProject = null;
	/**
	 * A string to the path of the last directory in which files were saved or
	 * loaded.
	 */
	private String lastDirectory = "";
	public final static String fileSeparator = System
			.getProperty("file.separator");

	/**
	 * A private constructor, exist to defy instansiation.
	 * 
	 */
	private FileControl() {
	}

	/**
	 * Returns the single instance of this class.
	 * 
	 * @return
	 */
	public static FileControl getInstance() {
		if (instance == null) {
			instance = new FileControl();
		}
		return instance;
	}

	/**
	 * Sets a reference for Opcat. It is necessary to perform this method
	 * <b>before </b> using any other method.
	 * 
	 * @param opcat
	 *            An instance of Opcat2.
	 */
	public void setOpcat(Opcat2 opcat) {
		this.myOpcat = opcat;
		this.currentProject = Opcat2.getCurrentProject();
	}

	/**
	 * Handles situations when the system was already opened, and a new system
	 * should be opened. The method closes the old system, if a system is
	 * opened.
	 * 
	 * @return <code>false</code> if the user cancelled the operation.
	 *         <code>true</code> otherwise.
	 */
	public boolean handleOpenedSystem() {
		if (this.myOpcat.isProjectOpened()) {
			return this.closeSystem();
		}

		return true;
	}

	/**
	 * Closes a system, allowing the user to save it if there are any changes
	 * since it was opnened. The changes are checked by calling
	 * <code>myUndoManager.canUndo()</code>. If a certain action does not have a
	 * reference in the undo manager, and no other references exist, and the
	 * system is closed, then data may be lost.
	 * 
	 * @return <code>false</code> if the user cancelled the closing operation.
	 * @see #closeCleanUp
	 */
	public boolean closeSystem() {
		if (!this.myOpcat.isProjectOpened()) {
			JOptionPane.showMessageDialog(Opcat2.getFrame(),
					"No system was opened", "Message",
					JOptionPane.ERROR_MESSAGE);
			return true;
		}

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		int retValue;
		// System.out.println("x="+
		// currentProject.getSystemChangeProbability());
		if (!this.canExitWithoutSave()) {
			retValue = JOptionPane.showConfirmDialog(Opcat2.getFrame(),
					"Do you want to save the system?", "Close System",
					JOptionPane.YES_NO_CANCEL_OPTION);

			switch (retValue) {
			case JOptionPane.YES_OPTION: {
				this._save(true);
				break;
			}
			case JOptionPane.CANCEL_OPTION: {
				return false;
			}
			}
		}
		// Performs cleaning operations when the project is closed down.
		this.myOpcat.closeCleanUp();

		return true;
	}

	/**
	 * loads an OPX file and initiates the current project. The method presents
	 * The method presents error messages to the user, in case the file was not
	 * found or the loading action failed. The method updates the last used file
	 * list.
	 * 
	 * @param fileName
	 *            The full path to the file
	 * @param showProgressBar
	 *            A flag indicating whether to show the progress bar while
	 *            loading the file.
	 * @see #_loadProject
	 */
	public void loadOpxFile(String fileName, boolean showProgressBar,
			boolean updateLastUsedFiles) {
		loadOpxFile(fileName, showProgressBar);
		if (updateLastUsedFiles) {
			// Update last used files list.
			this.updateLastUsedFile();
		}
	}

	public void loadOpxFile(String fileName, boolean showProgressBar) {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		GuiControl gui = GuiControl.getInstance();
		try {
			gui.getGlassPane().setVisible(true);
			gui.getGlassPane().start();

			JProgressBar pBar = null;
			if (showProgressBar) {
				pBar = new JProgressBar();
				this.myGuiControl.showWaitMessage("Loading System...", pBar);
			}
			this.currentProject = this.loadOpxFileIntoProject(fileName, pBar);
			this.currentProject.setFileName(fileName);

			this.myOpcat.setProjectWasOpened(true);
			this.myOpcat.setCurrentProject(this.currentProject);
			StateMachine.reset();
			if (currentProject.getPath() != null) {
				Opcat2.getFrame().setTitle(
						"Opcat II - " + currentProject.getName() + " : "
								+ currentProject.getPath());
			} else {
				Opcat2.getFrame().setTitle(
						"Opcat II - " + currentProject.getName() + " : "
								+ "warning - not saved yet");
			}

			GenericTable config = this.currentProject.getConfiguration();
			int currentSize = ((Integer) config.getProperty("CurrentSize"))
					.intValue();
			int normalSize = ((Integer) config.getProperty("NormalSize"))
					.intValue();

			this.myOpcat._enableZoomIn(true);
			this.myOpcat._enableZoomOut(true);

			if (currentSize == normalSize + 15) {
				this.myOpcat._enableZoomIn(false);
			}

			if (currentSize == normalSize - 4) {
				this.myOpcat._enableZoomOut(false);
			}

			// Loading ontology by their references
			// By Eran Toch
			/**
			 * TODO : strange
			 */
			// this.refreshMetaLibraries(this.currentProject.getMetaManager()
			// .getVectorClone());
			/**
			 * TODO : strange
			 */

			for (Iterator<Scenario> i = currentProject.getScen().values()
					.iterator(); i.hasNext();) {
				Scenario scen = (Scenario) i.next();
				refreshMetaLibraries(scen.getSettings(), scen.getSettings()
						.getVectorClone());
			}

			// myOpcat.getMetaWaitScreen().dispose() ;

			// try {
			// Enumeration<MetaLibrary> libs = currentProject
			// .getMetaLibraries();
			// while (libs.hasMoreElements()) {
			// MetaLibrary lib = libs.nextElement();
			// if (lib.isHidden()) {
			// if (lib.getRolesInProject(currentProject).size() == 0) {
			// currentProject.getMetaManager().removeMetaLibrary(
			// lib.getPath());
			// }
			// }
			// }
			// } catch (Exception ex) {
			//
			// }

			this.myOpcat.getRepository().setProject(this.currentProject);
			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE_TREE);

			// clean any empty entries.
			Opcat2.getCurrentProject().deleteEmptyEntries();
			Opcat2.getCurrentProject().getCurrentOpd().refit();

		} catch (gui.opx.LoadException le) {
			OpcatLogger.logError(le);
			// JOptionPane.showMessageDialog(this.myGuiControl.getFrame(), le
			// .getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (showProgressBar) {
				this.myGuiControl.hideWaitMessage();
			}
			StateMachine.reset();
			StateMachine.setWaiting(false);
			this.myGuiControl.setCursor4All(Cursor.DEFAULT_CURSOR);
			gui.getGlassPane().stop();

		}
	}

	/**
	 * Loading the project from a given input stream, using the <code>opx</code>
	 * library.
	 * 
	 * @param is
	 *            An input stream of the OPM model.
	 * @return The OpdProject, initiated from the model
	 * @throws gui.opx.LoadException
	 *             If the loading process failed.
	 * @see gui.opx.Loader#load
	 */
	public OpdProject _loadProject(InputStream is, JProgressBar pBar,
			String fileName) throws gui.opx.LoadException {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			throw new gui.opx.LoadException(EditControl.CUT_PENDING_MSG);
		}

		gui.opx.Loader ld = new gui.opx.Loader(fileName);
		OpdProject tProject = null;
		try {
			tProject = ld.load(this.myOpcat.getDesktop(), is, pBar);
		} catch (gui.opx.LoadException ex) {
			throw ex;
		} finally {
			duringFileAction = false;
		}
		return tProject;
	}

	/**
	 * Loads an external file into a project.
	 * 
	 * @param fileName
	 *            The name of the file
	 * @return An OPD project.
	 * @throws LoadException
	 *             If the file was not found, if there was an I/O problem or if
	 *             the load failed.
	 */
	public OpdProject loadOpxFileIntoProject(String fileName, JProgressBar pBar)
			throws LoadException {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			throw new gui.opx.LoadException(EditControl.CUT_PENDING_MSG);
		}

		try {
			File f = new File(fileName);
			InputStream is;
			if (fileName.endsWith(".opz")) {
				is = new GZIPInputStream(new FileInputStream(f), 4096);
			} else {
				is = new BufferedInputStream(new FileInputStream(f), 4096);
			}
			OpdProject project = this._loadProject(is, pBar, fileName);
			is.close();
			return project;
		} catch (FileNotFoundException e) {
			throw new LoadException("The file was not found:" + fileName
					+ "\n Please check the name and try again");
		} catch (IOException e) {
			throw new LoadException(
					"The file could not be loaded due to an I/O problem:"
							+ fileName
							+ "\n Please check the name and try again");
		} catch (LoadException e) {
			throw e;
		}
	}

	/**
	 * Handles the meta-libraries assigned to this project. The method laods the
	 * libraries and handles the changes done to the list in the
	 * <code>ProjectPropertiesDialog</code>.
	 * 
	 * @param metaLibraries
	 *            A Vector containing <code>MetaLibrary</code> objects. If null,
	 *            the method will return without carrying out any operation.
	 */
	public void refreshMetaLibraries(Vector<MetaLibrary> metaLibraries) {
		if ((metaLibraries == null)) {
			return;
		}
		this.currentProject.getMetaManager().setLibrariesVector(metaLibraries);
		JProgressBar pBar = new JProgressBar();
		this.myGuiControl
				.showMetaWaitMessage("Loading meta-libraries...", pBar);
		try {
			this.currentProject.getMetaManager().refresh(this.currentProject,
					pBar);
		} finally {
			this.myGuiControl.hideMetaWaitMessage();
		}
		if (Opcat2.getCurrentProject().getMetaManager().containFailed()) {
			LibrariesLoadingWindow libsLoad = new LibrariesLoadingWindow(
					this.currentProject.getMetaManager(), this.currentProject,
					Opcat2.getFrame());
			if (libsLoad.showDialog()) {
				this.refreshMetaLibraries(libsLoad.getUpdatedLibs());
			}
			/**
			 * JOptionPane.showMessageDialog( Opcat2.getFrame(),
			 * currentProject.getMetaManager().getFailedMessage(), "Opcat2 -
			 * Error", JOptionPane.INFORMATION_MESSAGE);
			 */
		}
	}

	public void refreshMetaLibraries(MetaManager man, Vector metaLibraries) {
		if (metaLibraries == null) {
			return;
		}
		man.setLibrariesVector(metaLibraries);

		JProgressBar pBar;
		if (myGuiControl.getMetaWaitMessage() != null) {
			pBar = null;

		} else {
			pBar = new JProgressBar();
		}

		this.myGuiControl
				.showMetaWaitMessage("Loading meta-libraries...", pBar);
		try {
			man.refresh(this.currentProject, pBar);
		} finally {
			this.myGuiControl.hideMetaWaitMessage();
		}

		if (man.containFailed()) {
			LibrariesLoadingWindow libsLoad = new LibrariesLoadingWindow(man,
					this.currentProject, Opcat2.getFrame());
			if (libsLoad.showDialog()) {
				this.refreshMetaLibraries(man, libsLoad.getUpdatedLibs());
			}

		}

	}

	/**
	 * save the given {@link OpdProject}. the project must have his
	 * {@link OpdProject#setFileName(String) fileName} property set in order to
	 * the save file. (<b>did not test this method yet</b>)
	 * 
	 * @param project
	 */
	public void save(OpdProject project) {

		if (duringFileAction) {
			OpcatLogger.logError(new Exception(
					"Error in save, OPCAT is preforming another IO action"));
			return;
		}

		duringFileAction = true;
		gui.opx.Saver sv = new gui.opx.Saver();
		sv.save(project, null, null);
		project.getMetaManager().refresh(currentProject, new JProgressBar());
		project.setCanClose(true);
		duringFileAction = false;

	}

	/**
	 * creates a new project.
	 * 
	 * @param opcat
	 *            Opcat2 reference
	 * @param showNewProjectDialog
	 *            should a New Project dialog be shown ?
	 * @param name
	 *            name of new project. ignored if showNewProjectDialog is true
	 * @param creator
	 *            Creator name of new project. ignored if showNewProjectDialog
	 *            is true
	 * @param file
	 *            a file which will contain the new project after performing
	 *            save. if file is null then the save as dialog will appear when
	 *            saving the project for the first time,
	 *            {@link #save(OpdProject) see save(OpdProject project)}
	 * @return the new project. If Opcat is not null then this project will be
	 *         set as the current project in Opcat.
	 */
	public OpdProject createNewProject(Opcat2 opcat,
			boolean showNewProjectDialog, String name, String creator, File file) {

		JDesktopPane desktop;
		if (opcat != null) {
			desktop = opcat.getDesktop();
		} else {
			desktop = null;
		}

		OpdProject tmpProject = new OpdProject(desktop, 1);

		OpdProject ret = new OpdProject(desktop, 1);

		ProjectPropertiesDialog ppd = null;

		boolean isOK = false;
		if (showNewProjectDialog) {
			ppd = new ProjectPropertiesDialog(tmpProject, this.myGuiControl
					.getFrame(), "New System Properties");
			isOK = ppd.showDialog();
			name = tmpProject.getName();
			creator = tmpProject.getCreator();
		} else {
			isOK = true;
		}

		if (isOK) {

			ret.setName(name);
			ret.setCreator(creator);
			ret.setCreationDate(tmpProject.getCreationDate());

			if (file != null) {
				ret.setFileName(file.getPath());
				ret.setPath(file.getPath());
			}

			ret.getGeneralInfo().setTable(
					tmpProject.getGeneralInfo().getTable());

			currentProject = ret;

			if (opcat != null) {
				opcat.setCurrentProject(ret);
				opcat.setProjectWasOpened(true);

				opcat._enableZoomIn(true);
				opcat._enableZoomOut(true);

			}
			// Setting the Opcat license for the project
			ret.createRootOPD();

			if (opcat != null) {
				ret.showRootOpd();
				opcat.getRepository().setProject(ret);
			}

			// Handling Meta-librares (ontologies) import
			// by Eran Toch
			if (ppd != null)
				this.refreshMetaLibraries(ppd.getMetaLibraries());
		}

		return ret;
	}

	/**
	 * Performs a saving operation. If the file was already saved, or loaded
	 * from an existing file, then the method saves the file to it's last
	 * location. Otherwise, the user is prompted for a file location. The method
	 * updates the last used file list.
	 * 
	 * @param updateLastUsedFiles
	 *            - If true the save is not recorded in the saved file list and
	 *            the data is not considered saved by the undo manager
	 * 
	 */
	public void _save(boolean updateLastUsedFiles) {

		GridPanel.updateColor(null);
		MetaColoringAction colorLevel = new MetaColoringAction("dummy", null,
				StandardImages.META_COLOR_DEF);
		colorLevel.actionPerformed(null);

		MetaHideAction hide = new MetaHideAction("dummy", null, false,
				StandardImages.System_Icon);
		hide.actionPerformed(null);

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// clean any empty entries.
		Opcat2.getCurrentProject().deleteEmptyEntries();

		if (this.currentProject.getFileName() == null) {
			this._saveAs();
			return;
		}

		StateMachine.setWaiting(true);
		this.myGuiControl.setCursor4All(Cursor.WAIT_CURSOR);

		// start save
		gui.opx.Saver sv = new gui.opx.Saver();
		sv.save(this.currentProject, new JProgressBar(), Opcat2.getFrame());

		// this.currentProject.getMetaManager().getMetaLibrary(
		// MetaLibrary.convertProjectToID(currentProject)).load(); // .refresh
		// (currentProject,
		// new
		// JProgressBar());

		//
		// end save

		if (updateLastUsedFiles) {
			this.updateLastUsedFile();
			if (Opcat2.getCurrentProject() != null) {
				Opcat2.getCurrentProject().setCanClose(true);
			}
		}

		StateMachine.setWaiting(false);
		this.myGuiControl.setCursor4All(Cursor.DEFAULT_CURSOR);
		if (updateLastUsedFiles) {
			this.myOpcat.getOpcatUndoManager().dataSaved();
		}

		if (currentProject.getPath() != null) {
			Opcat2.getFrame().setTitle(
					"Opcat II - " + currentProject.getName() + " : "
							+ currentProject.getPath());
		} else {
			Opcat2.getFrame().setTitle(
					"Opcat II - " + currentProject.getName() + " : "
							+ "warning - not saved yet");
		}
	}

	/**
	 * The method prompts the user for a file location, and then saves the file
	 * to that location using the {@link _save} method.
	 * 
	 */
	public void _saveAs() {

		GridPanel.updateColor(null);
		MetaColoringAction colorLevel = new MetaColoringAction("dummy", null,
				StandardImages.META_COLOR_DEF);
		colorLevel.actionPerformed(null);

		MetaHideAction hide = new MetaHideAction("dummy", null, false,
				StandardImages.System_Icon);
		hide.actionPerformed(null);

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		JFileChooser myFileChooser = new JFileChooser();
		myFileChooser.setSelectedFile(new File(this.currentProject.getName()));
		myFileChooser.resetChoosableFileFilters();
		CustomFileFilter opxChooser = new CustomFileFilter("opx",
				"Opcat2 XML Files");
		CustomFileFilter opzChooser = new CustomFileFilter("opz",
				"Opcat2 XML zipped Files");
		if (!this.getLastDirectory().equals("")) {
			myFileChooser
					.setCurrentDirectory(new File(this.getLastDirectory()));
		}
		// myFileChooser.setFileFilter(opzChooser);
		myFileChooser.addChoosableFileFilter(opxChooser);

		boolean done = false;
		while (!done) {
			int retVal = myFileChooser.showDialog(Opcat2.getFrame(),
					"Save System");

			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File p = myFileChooser.getSelectedFile().getParentFile();
			done = !(p.getPath().equalsIgnoreCase(OpcatMCManager.getInstance()
					.getWCRoot().getPath()));

			if (!done) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(),
						"Can not save in OPCAT MC root");
			}

		}

		String fileName = myFileChooser.getSelectedFile().getPath();

		// Checks if the file already exists
		if (!this.canSaveFile(fileName)) {
			return;
		}

		this.setLastDirectory(myFileChooser.getSelectedFile().getParent());
		if (myFileChooser.getFileFilter().getDescription().equals(
				opzChooser.getDescription())
				|| fileName.endsWith(".opz")) {
			if (!fileName.endsWith(".opz")) {
				fileName += ".opz";
			}
		} else {
			if (!fileName.endsWith(".opx")) {
				fileName += ".opx";
			}

		}

		this.currentProject.setFileName(fileName);
		this.currentProject.setPath(fileName);

		/**
		 * a patch to remove all exposed and reset the lastChange in the
		 * exposemanager.
		 */
		Enumeration<Entry> entries = currentProject.getSystemStructure()
				.getElements();
		while (entries.hasMoreElements()) {
			Entry entry = entries.nextElement();

			entry.getLogicalEntity().setPublicExposed(false);
			entry.getLogicalEntity().setPrivateExposed(false);
			entry.getLogicalEntity().setExposedChanged(false);
			entry.getLogicalEntity().getRolesManager().clear();

			Enumeration<Instance> instances = entry.getInstances();
			while (instances.hasMoreElements()) {
				Instance instance = instances.nextElement();
				if (instance.isPointerInstance()) {
					instance.setPointer(null);
				}
			}
		}
		ArrayList<OpcatExposeChange> changes = new ArrayList<OpcatExposeChange>();
		currentProject.getExposeManager().setLatestExposedChangeMap(changes);
		currentProject.setMcURL(null);
		/**
		 * patch end
		 */

		// set new global ID.
		currentProject.setGlobalID(currentProject.generateGlobalID());

		this._save(true);

		if (currentProject.getPath() != null) {
			Opcat2.getFrame().setTitle(
					"Opcat II - " + currentProject.getName() + " : "
							+ currentProject.getPath());
		} else {
			Opcat2.getFrame().setTitle(
					"Opcat II - " + currentProject.getName() + " : "
							+ "warning - not saved yet");
		}

	}

	/**
	 * Checks if a file with a given path can be saved to the file system. The
	 * method checks if the file exists, and if so, asks the user to decide if
	 * to overwrite the file or not.
	 * 
	 * @param fileName
	 *            The path to the file.
	 * @return <code>true</code> if a file with the fiven name does not exists
	 *         or if the user decided to overwrite the file.
	 */
	private boolean canSaveFile(String fileName) {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			return false;
		}

		File checkFile = new File(fileName);
		if (checkFile.exists()) {
			JOptionPane
					.showMessageDialog(
							Opcat2.getFrame(),
							fileName
									+ " already exist.\nUse opcat MC in order to delete the file before rewriting it");
			return false;
			// rewriting 

		}
		return true;
	}

	/**
	 * Checks wheather there is a need to save the system before closing it. The
	 * method cheks if the undo manager has any changes or the
	 * <code>systemChangeProbability</code> variable is set to <code>true</code>
	 * .
	 * 
	 * @return <code>true</code> if it's possible to exit without changes.
	 *         <code>false</code> otherwise.
	 * @see UndoManager
	 * @see #getSystemChangeProbability
	 */
	public boolean canExitWithoutSave() {
		if (Opcat2.getCurrentProject() == null) {
			return true;
		}
		return this.myOpcat.getOpcatUndoManager().canCloseProject()
				&& Opcat2.getCurrentProject().isCanClose();
	}

	/**
	 * @return Whether a project is currently open. Uses
	 *         {@link Opcat2#isProjectWasOpened()} in order to retrieve an
	 *         answer.
	 */
	public boolean isProjectOpened() {
		if (myOpcat != null) {
			return this.myOpcat.isProjectWasOpened();
		} else {
			return true;
		}
	}

	/**
	 * Exits gracefully from {@link Opcat2 Opcat2 GUI}, saving unclosed files
	 * and performing other cleaning tasks. These tasks include: closing
	 * connection, stopping the state machine.
	 * 
	 */
	public void _exit() {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!this.isProjectOpened()) {
			System.exit(0);
		}

		if (StateMachine.isAnimated()) {
			this.myOpcat.closeTesting.actionPerformed(new ActionEvent(
					this.myGuiControl.getFrame(), 0, ""));
			return;
		}

		if (!this.canExitWithoutSave()) {
			int retValue;
			retValue = JOptionPane.showConfirmDialog(this.myGuiControl
					.getFrame(), "Do you want to save the system?", "Exit",
					JOptionPane.YES_NO_CANCEL_OPTION);

			switch (retValue) {
			case JOptionPane.YES_OPTION: {
				this._save(true);
				this.myOpcat.closeCleanUp();
				System.exit(0);
				break;
			}
			case JOptionPane.NO_OPTION: {
				this.myOpcat.closeCleanUp();
				System.exit(0);
				break;
			}
			case JOptionPane.CANCEL_OPTION: {
				return;
			}
			}
		} else {
			System.exit(0);
		}
	}

	/**
	 * Creates a new {@link OpdProject}, including displaying a
	 * {@link ProjectPropertiesDialog project properties dialog } .
	 * <p>
	 * after this method finishes the {@link Opcat2#getCurrentProject()} method
	 * will return the newly project.
	 * </p>
	 * 
	 */
	public void createNewProject() {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		createNewProject(myOpcat, true, null, null, null);
		return;
	}

	/**
	 * Creates a new {@link OpdProject}, from the given MC entry. this method
	 * will get the model into a temporary directory from the MC repository, and
	 * load it into {@link Opcat2}
	 * <p>
	 * after this method finishes the {@link Opcat2#getCurrentProject()} method
	 * will return the newly project.
	 * </p>
	 * 
	 */
	public void createNewProject(OpcatMCDirEntry entry, boolean lock) {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		File source = new File(OpcatMCManager.TEMPLATES_DIR
				+ FileControl.fileSeparator + entry.getName());

		GuiControl.getInstance().getGlassPane().setVisible(true);
		GuiControl.getInstance().getGlassPane().start();

		try {

			File destination;
			destination = File.createTempFile(entry.getName(), ".opx");
			File tempDIr = destination.getParentFile();
			String fileName = entry.getName();
			File tempFile = new File(tempDIr.getPath()
					+ FileControl.fileSeparator + fileName);
			tempFile.delete();
			FileUtils.copyFile(source, tempFile);
			tempFile.setWritable(false);
			loadOpxFile(tempFile.getPath(), true, false);
		} catch (IOException e) {
			OpcatLogger.logError(e);
		}
		GuiControl.getInstance().getGlassPane().stop();
		GuiControl.getInstance().getGlassPane().setVisible(false);

		return;
	}

	public void loadFileWithOutFilesPrompt(String filename) {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int retValue;
		if (!this.canExitWithoutSave()) {
			retValue = JOptionPane.showConfirmDialog(Opcat2.getFrame(),
					"Do you want to save the system?", "Close System",
					JOptionPane.YES_NO_CANCEL_OPTION);

			switch (retValue) {
			case JOptionPane.YES_OPTION: {
				this._save(true);
				break;
			}
			case JOptionPane.CANCEL_OPTION: {
				return;
			}
			}
		}

		myOpcat.closeCleanUp();

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				GuiControl gui = GuiControl.getInstance();
				gui.getGlassPane().setVisible(true);
				gui.getGlassPane().start();
			}
		});

		this.loadOpxFile(filename, true);

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				GuiControl gui = GuiControl.getInstance();
				gui.getGlassPane().setVisible(false);
				gui.getGlassPane().stop();
			}
		});

	}

	public void loadExample(String actionCommand) {

		String fileName = getOPCATDirectory() + fileSeparator + "Examples"
				+ fileSeparator + actionCommand + ".opz";

		loadFileWithOutFilesPrompt(fileName);

	}

	/**
	 * Saves the current OPD as an JPG image, to a location choosen by the user.
	 * The method uses <code>JPEGEncodeParam</code> in order to perform the
	 * actual JPG encoding task. The image is retrieved from
	 * <code>currentProject.getCurrentOpd().getImageRepresentation()</code>
	 * 
	 */
	public void saveAsImage() {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!this.isProjectOpened()) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					"No OPD is opened", "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JFileChooser myFileChooser = new JFileChooser();
		myFileChooser.resetChoosableFileFilters();
		myFileChooser.setFileFilter(new CustomFileFilter("jpg", "JPEG Files"));

		// Setting the last directory
		String ld = this.getLastDirectory();
		if (!ld.equals("")) {
			myFileChooser
					.setCurrentDirectory(new File(this.getLastDirectory()));
		}
		int returnVal = myFileChooser.showSaveDialog(this.myGuiControl
				.getFrame());

		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}

		try {
			String file = myFileChooser.getSelectedFile().getAbsolutePath();
			if (!file.trim().toLowerCase().endsWith(".jpg")) {
				file = file.trim() + ".jpg";
			}
			if (!this.canSaveFile(file)) {
				return;
			}
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(file));
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

			BufferedImage bi = this.currentProject.getCurrentOpd()
					.getImageRepresentation();

			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
			int quality = 100;
			quality = Math.max(0, Math.min(quality, 100));
			param.setQuality(quality / 100.0f, false);
			encoder.setJPEGEncodeParam(param);
			encoder.encode(bi);
			out.close();
		} catch (Exception e23) {
		}

	}

	/**
	 * Returns the API read-only representation of the current project.
	 * 
	 * @return
	 */
	public ISystem getCurrentISystem() {
		return Opcat2.getCurrentProject();
	}

	/**
	 * Returns the current project, after checking if myOpcat is not null.
	 * 
	 * @return
	 */
	public OpdProject getCurrentProject() {
		if (this.myOpcat == null) {
			OpcatLogger.logError("FileControl was not initiated by Opcat2");
		}
		return Opcat2.getCurrentProject();
	}

	/**
	 * @return Returns the isWholeProjectCompiled.
	 */
	public boolean isWholeProjectCompiled() {
		return this.myOpcat.isWholeProjectCompiled();
	}

	/**
	 * @param isWholeProjectCompiled
	 *            The isWholeProjectCompiled to set.
	 */
	public void setWholeProjectCompiled(boolean isWholeProjectCompiled) {
		this.myOpcat.setWholeProjectCompiled(isWholeProjectCompiled);
	}

	/**
	 * Adds the current project to the list of the last used files. The data is
	 * stored in a configuration file, and can be used afterwards.
	 */
	public void updateLastUsedFile() {
		Configuration conf = Configuration.getInstance();
		LastFileEntry entry = new LastFileEntry(this.currentProject.getName(),
				this.currentProject.getFileName());
		conf.addFileToLastUsed(entry);
		this.myOpcat.refreshMenuBar();
	}

	/**
	 * @return Returns the lastDirectory - the last directory that an opcat file
	 *         was opened from or saved to.
	 */
	public String getLastDirectory() {
		if (lastDirectory.equalsIgnoreCase("")) {
			return Configuration.getInstance().getProperty("models_directory");
		} else {
			return this.lastDirectory;
		}
	}

	/**
	 * @param lastDirectory
	 *            The lastDirectory to set.
	 */
	public void setLastDirectory(String lastDirectory) {
		this.lastDirectory = lastDirectory;
	}

	/**
	 * Reloads the project, by saving it into a temp file and loading it. This
	 * method is used only by the import s a patch to solve a graphics bug. The
	 * method returns the user to the original OPD that was used by the user
	 * before the reload. it will not update the lastused file list
	 */
	public void reloadProject() {

		boolean cutPending = this.edit.IsCutPending();
		if (cutPending) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					EditControl.CUT_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (duringFileAction) {
			JOptionPane.showMessageDialog(this.myGuiControl.getFrame(),
					FILE_ACTION_PENDING_MSG, "Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			File file = File.createTempFile("temp", "opz");
			String oldFileName = this.currentProject.getFileName();
			String oldPath = currentProject.getPath();

			this.currentProject.setFileName(file.getPath());

			long currentOpdID = this.getCurrentProject().getCurrentOpd()
					.getOpdId();
			this._save(false);
			String filePath = new String(this.getCurrentProject().getFileName());
			this.closeSystem();
			Opcat2.setLoadedfilename(filePath);
			this.loadOpxFile(filePath, false, false);
			this.currentProject.setFileName(oldFileName);
			currentProject.setPath(oldPath);
			Opcat2.getFrame().setTitle(
					"Opcat II - " + currentProject.getName() + " : "
							+ "warning - not saved after import");
			// }

			currentProject.setCanClose(false);
			Opd originalOpd = this.getCurrentProject().getOpdByID(currentOpdID);
			this.getCurrentProject().setCurrentOpd(originalOpd);
			this.getCurrentProject().showOPD(currentOpdID);
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}
	}

	public String getOPCATDirectory() {
		String home = System.getenv().get("OPCAT_HOME");
		if (home == null) {
			JOptionPane.showMessageDialog(Opcat2.getFrame(),
					"OPCAT Home variable not defined", "Error",
					JOptionPane.ERROR_MESSAGE);
			/**
			 * TODO: move this to the proper place at OPCAT2 class.
			 */

			System.exit(1);
		}
		return home;
	}

	public File getOPCATJAR() {
		String home = System.getenv().get("OPCAT_HOME");
		if (home == null) {
			JOptionPane.showMessageDialog(Opcat2.getFrame(),
					"OPCAT Home variable not defined", "Error",
					JOptionPane.ERROR_MESSAGE);
			/**
			 * TODO: move this to the proper place at OPCAT2 class.
			 */

			System.exit(1);
		}
		return new File(home + fileSeparator
				+ Configuration.getInstance().getProperty("jarname"));
	}

	public void runNewOPCAT(File model, long entryID) {

		try {
			String command = "java -jar \"" + getOPCATJAR().getPath() + "\" "
					+ " \"" + model.getPath() + "\" " + String.valueOf(entryID);
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			OpcatLogger.logError(e);
		}
	}

	public void runNewOPCAT(File model) {

		try {
			runNewOPCAT(model, 0);
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}
	}

	public boolean isDuringFileAction() {
		return duringFileAction;
	}

	public void setDuringFileAction(boolean duringFileAction) {
		this.duringFileAction = duringFileAction;
	}
}